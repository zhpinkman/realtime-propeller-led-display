package com.example.front_sample.activities;

import android.Manifest;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapRegionDecoder;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.front_sample.R;
import com.example.front_sample.config.Config;
import com.example.front_sample.utils.ImageHandler;
import com.example.front_sample.utils.VideoHandler;
import com.example.front_sample.utils.udp.UDPHandler;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;


public class VideoActivity extends AppCompatActivity {

    private static final int VIDEO_GALLERY_REQUEST = 30;
    private static final String TAG = "Permission Error";
    private VideoView videoView;
    private ImageView imageView;
    private TextView textView;
    private MediaController mediaController;
    private UDPHandler udpHandler = UDPHandler.getInstance();
    private SeekBar samplingSeekBar;
    private SeekBar outputSeekBar;
    private TextView samplingSeekBarText;
    private TextView outputSeekBarText;
    private int sampleDuration = 50;
    private int outputDuration = 500;

    private int currentShowingFrameIndex = 0;
    private volatile List<Bitmap> videoFrames = null;
    private volatile int frameDuration = 50;  //ms
    private volatile boolean killVideoProcess = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);
        this.isStoragePermissionGranted();
        init();
//        setMediaCont();
//        playVideoRawFolder();
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.setLooping(true);
            }
        });


        samplingSeekBar = (SeekBar) findViewById(R.id.seekBar);
        samplingSeekBarText = (TextView) findViewById(R.id.textView3);
        outputSeekBar = (SeekBar) findViewById(R.id.seekBar2);
        outputSeekBarText = (TextView) findViewById(R.id.textView4);

        outputSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                outputDuration = 10 + progress;
                outputSeekBarText.setText("" + outputDuration + " ms");
                try {
                    udpHandler.setFrameDuration(outputDuration);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        samplingSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                sampleDuration = 10 + progress;
                samplingSeekBarText.setText("" + sampleDuration + " ms");
                frameDuration = sampleDuration;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


    }

    @Override
    protected void onStart() {
        udpHandler.startServer();
        super.onStart();
    }

    public void onSend(View view) {
        Intent photoPickerIntent = new Intent(Intent.ACTION_GET_CONTENT);
//        File pictureDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
//        String pictureDirectoryPath = pictureDirectory.getPath();
//        Uri data = Uri.parse(pictureDirectoryPath);
        photoPickerIntent.setType("video/*");
        startActivityForResult(photoPickerIntent, VIDEO_GALLERY_REQUEST);
    }

    public synchronized void clearVideoFrames() {
        if (videoFrames != null) {
            List<Bitmap> oldVideoFrames = videoFrames;
            videoFrames.clear();
            videoFrames = null;
            for (Bitmap bmp : oldVideoFrames) {
                bmp.recycle();
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == VIDEO_GALLERY_REQUEST && resultCode == RESULT_OK) {
            Uri uri = data.getData();
            if (uri != null) {
                setTextView("Killing old video process if exists...");
                killOldProcess();
                clearVideoFrames();
                Runnable r = new VideoProcessRunnable(this, uri);
                new Thread(r).start();

                videoView.setVideoURI(uri);
                setMediaCont();
                videoView.start();
                this.refreshImagePeriodically();
            }
        }
    }

    private void killOldProcess() {
        this.killVideoProcess = true;
        long startTime = System.currentTimeMillis();
        while (killVideoProcess){
            if(System.currentTimeMillis() - startTime > 2000){
                this.killVideoProcess = false;
                break;
            }
        }
    }

    public class VideoProcessRunnable implements Runnable {
        private Context context;
        private Uri uri;

        public VideoProcessRunnable(Context context, Uri uri) {
            this.context = context;
            this.uri = uri;
        }

        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        public void run() {
            System.out.println("videoProcessThread running");

            setTextView("Starting Video Processing...");
            MediaMetadataRetriever retriever = new MediaMetadataRetriever();
            String path = getPathFromUri(context, uri);
            retriever.setDataSource(context, uri);

            retrieveAndSendFramesAllAtOnce(retriever, sampleDuration);
//            retrieveAndSendFramesGradually(retriever, sampleDuration);
        }

        private void retrieveAndSendFramesAllAtOnce(MediaMetadataRetriever retriever, int frameDuration){  // Retrieve all frames and process them, then send all together
            setTextView("Retrieving Video Frames...");
            List<Bitmap> localVideoFrames;
            localVideoFrames = VideoHandler.getVideoFrames(retriever, frameDuration, context);

            setTextView("Cropping center...");
            localVideoFrames = VideoHandler.cropCenter(localVideoFrames);

            setTextView("Scaling down frames size...");
            localVideoFrames = VideoHandler.scale(localVideoFrames, Config.VIDEO_SIZE, Config.VIDEO_SIZE);

            setTextView("Converting to grayscale...");
            localVideoFrames = VideoHandler.toGrayscale(localVideoFrames);

            setTextView("Sending video to udp handler...");
            List<int[][]> framesBrightnessList = VideoHandler.bmpToArray(localVideoFrames);
            udpHandler.setSquareContext(framesBrightnessList.get(0));  // To remove old movie from board
            udpHandler.setSquareContext(framesBrightnessList);

            setVideoFrames(localVideoFrames);
            setTextView("Video Processing Finished");
        }

        private void retrieveAndSendFramesGradually(MediaMetadataRetriever retriever, int frameDuration) {  // Process each frame completely and send it to board
            try {
                String time = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
                long duration = Long.parseLong(time);  //ms
                for (int i = 0; i < duration; i += frameDuration) {
                    if(killVideoProcess){
                        killVideoProcess = false;
                        break;
                    }
                    double value = i * 1000;
                    System.out.println("Retrieving Video Frames " + i + "/" + duration + "ms");
                    setTextView("Retrieving Video Frames " + i + "/" + duration + "ms");
                    Bitmap frameBmp = retriever.getFrameAtTime((long) value,
                            MediaMetadataRetriever.OPTION_CLOSEST);
                    frameBmp = ImageHandler.cropCenter(frameBmp);
                    frameBmp = ImageHandler.scaleBitmap(frameBmp, Config.VIDEO_SIZE, Config.VIDEO_SIZE);
                    frameBmp = ImageHandler.toGrayscale(frameBmp);
                    if (i == 0) {
                        udpHandler.setSquareContext(ImageHandler.bmpToArray(frameBmp));  // To remove old movie from board
                        clearVideoFrames();
                    } else {
                        udpHandler.appendSquareContext(ImageHandler.bmpToArray(frameBmp));
                    }
                    appendVideoFrames(frameBmp);  // For showing in the UI
                }
                setTextView("Video Processing Finished");
            } catch (Exception e) {
                System.out.println(Arrays.toString(e.getStackTrace()));
                setTextView("Video Processing Failed");
            }

        }

    }


    public synchronized void setTextView(final String text) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                textView.setText(text);
            }
        });
    }

    private synchronized void setVideoFrames(final List<Bitmap> newFrames) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                System.out.println("NEW FRAMES" + newFrames.size());
                videoFrames = newFrames;
            }
        });
    }

    private synchronized void appendVideoFrames(final Bitmap newFrame) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(videoFrames == null)
                    videoFrames = Collections.synchronizedList(new ArrayList<Bitmap>());
                videoFrames.add(newFrame);
            }
        });
    }

    private void refreshImagePeriodically() {
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                //do something
                try {
                    Bitmap bmp = videoFrames.get(currentShowingFrameIndex);
                    imageView.setImageBitmap(bmp);
                    currentShowingFrameIndex = (currentShowingFrameIndex + 1) % videoFrames.size();
                } catch (Exception e) {
                    e.printStackTrace();
                    currentShowingFrameIndex = 0;
                    imageView.setImageResource(android.R.drawable.stat_notify_sync);
                }
                handler.postDelayed(this, outputDuration);
            }
        }, outputDuration);
    }

    @Override
    protected void onStop() {
        killOldProcess();
        super.onStop();
    }

    public void init() {
        videoView = findViewById(R.id.videoView3);
        imageView = findViewById(R.id.imageView2);
        textView = findViewById(R.id.textView);
        this.mediaController = new MediaController(this);
    }


    private void playVideoRawFolder() {
        Uri uri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.video1);
        videoView.setVideoURI(uri);
        videoView.start();
    }

    private void setMediaCont() {
        mediaController.setMediaPlayer(videoView);
        mediaController.setAnchorView(videoView);
        videoView.setMediaController(mediaController);
    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static String getPathFromUri(final Context context, final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }

                // TODO handle non-primary volumes
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{
                        split[1]
                };

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {

            // Return the remote address
            if (isGooglePhotosUri(uri))
                return uri.getLastPathSegment();

            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }


    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }


    public boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v(TAG, "Permission is granted");
                return true;
            } else {

                Log.v(TAG, "Permission is revoked");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                return false;
            }
        } else { //permission is automatically granted on sdk<23 upon installation
            Log.v(TAG, "Permission is granted");
            return true;
        }
    }


}

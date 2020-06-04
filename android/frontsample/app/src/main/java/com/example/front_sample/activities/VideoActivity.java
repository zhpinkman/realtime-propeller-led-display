package com.example.front_sample.activities;

import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.VideoView;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.front_sample.R;

import java.io.File;
import java.net.URISyntaxException;
import java.util.ArrayList;


public class VideoActivity extends AppCompatActivity {

    private static final int VIDEO_GALLERY_REQUEST = 30;
    private VideoView videoView;
    private ImageView imageView;
    private MediaController mediaController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);


        init();


//        setMediaCont();
//
//        playVideoRawFolder();




        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.setLooping(true);
            }
        });
    }


    public void onSend(View view) {

        Intent photoPickerIntent = new Intent(Intent.ACTION_GET_CONTENT);

//        File pictureDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
//        String pictureDirectoryPath = pictureDirectory.getPath();
//        Uri data = Uri.parse(pictureDirectoryPath);


        photoPickerIntent.setType("video/*");

        startActivityForResult(photoPickerIntent, VIDEO_GALLERY_REQUEST);

    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == VIDEO_GALLERY_REQUEST && resultCode == RESULT_OK) {
            Uri uri = data.getData();
            if (uri != null) {
                MediaMetadataRetriever retriever = new MediaMetadataRetriever();
                String path = getPathFromUri(this, uri);
                retriever.setDataSource(this, uri);
//                Bitmap btmp = retriever.getFrameAtTime(1000000 * 8, MediaMetadataRetriever.OPTION_CLOSEST_SYNC);




//                Bitmap squaredBitmap = getSquaredBitmap(btmp);

//                Bitmap grayScaleBitmap = toGrayscale(squaredBitmap);


//                imageView.setImageBitmap(grayScaleBitmap);

                ArrayList<Bitmap> videoFrames = getVideoFrames(retriever, 30);


                imageView.setImageBitmap(videoFrames.get(30 * 8 - 30));

//                videoView.setVideoURI(uri);
//                setMediaCont();
//                videoView.start();

            }
        }
    }

    private ArrayList<Bitmap> getVideoFrames(MediaMetadataRetriever retriever, int fps) {
        try {
            String time = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
            long duration = Long.parseLong(time);
            long durationSeconds = duration / 1000;
            ArrayList<Bitmap> bArray = new ArrayList<Bitmap>();
            bArray.clear();
//            30 fps
            for (int i = 0; i < fps * durationSeconds; i++) {
                double value = i * 1000000 / fps;
                System.out.println((long) value);
                bArray.add(retriever.getFrameAtTime((long) value,
                        MediaMetadataRetriever.OPTION_CLOSEST_SYNC));
            }
            return bArray;
        } catch (Exception e) { return null; }
    }


    public Bitmap getSquaredBitmap(Bitmap bitmap) {
        int height = bitmap.getHeight();
        int width = bitmap.getWidth();
        int minimumDimension = height < width ? height : width;
        int[] pixels = new int[minimumDimension*minimumDimension];
        if (minimumDimension == height) {
            bitmap.getPixels(pixels, 0, minimumDimension, (width/2 - height/2), 0, minimumDimension, minimumDimension);
        } else {
            bitmap.getPixels(pixels, 0, minimumDimension, (height/2 - width/2), 0, minimumDimension, minimumDimension);
        }
        Bitmap result = Bitmap.createBitmap(pixels, 0, minimumDimension, minimumDimension, minimumDimension, Bitmap.Config.ARGB_8888);
        return result;
    }

    public Bitmap toGrayscale(Bitmap bmpOriginal)
    {
        int width, height;
        height = bmpOriginal.getHeight();
        width = bmpOriginal.getWidth();

        Bitmap bmpGrayscale = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(bmpGrayscale);
        Paint paint = new Paint();
        ColorMatrix cm = new ColorMatrix();
        cm.setSaturation(0);
        ColorMatrixColorFilter f = new ColorMatrixColorFilter(cm);
        paint.setColorFilter(f);
        c.drawBitmap(bmpOriginal, 0, 0, paint);
        return bmpGrayscale;
    }



    public int[][] getRGBValues(Bitmap bmp) {

        int [][] rgbValues = new int[bmp.getWidth()][bmp.getHeight()];

        //get the ARGB value from each pixel of the image and store it into the array
        for(int i=0; i < bmp.getWidth(); i++)
        {
            for(int j=0; j < bmp.getHeight(); j++)
            {
                //This is a great opportunity to filter the ARGB values
                rgbValues[i][j] = bmp.getPixel(i, j);
            }
        }
        return rgbValues;
    }

    public void init() {
        videoView = findViewById(R.id.videoView3);
        imageView = findViewById(R.id.imageView2);
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
                final String[] selectionArgs = new String[] {
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


}

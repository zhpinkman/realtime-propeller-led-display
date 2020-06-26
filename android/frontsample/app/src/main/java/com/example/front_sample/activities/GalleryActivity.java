package com.example.front_sample.activities;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.front_sample.R;
import com.example.front_sample.config.Config;
import com.example.front_sample.utils.ImageHandler;
import com.example.front_sample.utils.VideoHandler;
import com.example.front_sample.utils.udp.UDPHandler;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GalleryActivity extends AppCompatActivity {

    public static final int IMAGE_GALLERY_REQUEST = 20;
    private TextView textView;
    private UDPHandler udpHandler = UDPHandler.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);
        textView = (TextView) findViewById(R.id.textView6);
    }


    public void onSend(View view) {

        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);

        File pictureDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        String pictureDirectoryPath = pictureDirectory.getPath();
        Uri data = Uri.parse(pictureDirectoryPath);


        photoPickerIntent.setDataAndType(data, "image/*");

        startActivityForResult(photoPickerIntent, IMAGE_GALLERY_REQUEST);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK)  {
            if (requestCode == IMAGE_GALLERY_REQUEST) {
                Uri imageUri = data.getData();
                InputStream inputStream;;


                try {
                    inputStream = getContentResolver().openInputStream(imageUri);
//                    byte[] inputData = getBytes(inputStream);
                    Bitmap bitmap = BitmapFactory.decodeStream(inputStream);

                    setUiImage(bitmap);

                    Runnable r = new GalleryActivity.GalleryProcessRunnable(bitmap);
                    new Thread(r).start();

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    Toast.makeText(this, "unable to open image", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    private synchronized void setUiImage(final Bitmap newPic) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ((ImageView) findViewById(R.id.imageView)).setImageBitmap(newPic);
            }
        });
    }


    public class GalleryProcessRunnable implements Runnable {
        private Bitmap bitmap;

        public GalleryProcessRunnable(Bitmap bitmap) {
            this.bitmap = bitmap;
        }

        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        public void run() {
            System.out.println("galleryProcessThread running");

            setTextView("Cropping center...");
            bitmap = ImageHandler.cropCenter(bitmap);

            setTextView("Scaling down frames size...");
            bitmap = ImageHandler.scaleBitmap(bitmap, Config.IMAGE_SIZE, Config.IMAGE_SIZE);

            setTextView("Converting to grayscale...");
            bitmap = ImageHandler.toGrayscale(bitmap);

            setTextView("Sending video to udp handler...");
            udpHandler.setSquareContext(ImageHandler.bmpToArray(bitmap));

//            setVideoFrames(localVideoFrames);
            setTextView("Image Processing Finished");
            setUiImage(bitmap);
        }
    }


    private void setTextView(final String text) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                textView.setText(text);
            }
        });
    }

    public int[][] getRGBValues(Bitmap bmp) {

        int [][] rgbValues = new int[bmp.getWidth()][bmp.getHeight()];

        //Print in LogCat's console each of one the RGB and alpha values from the 4 corners of the image
        //Top Left
        Log.i("Pixel Value", "Top Left pixel: " + Integer.toHexString(bmp.getPixel(0, 0)));
        //Top Right
        Log.i("Pixel Value", "Top Right pixel: " + Integer.toHexString(bmp.getPixel(31, 0)));
        //Bottom Left
        Log.i("Pixel Value", "Bottom Left pixel: " + Integer.toHexString(bmp.getPixel(0, 31)));
        //Bottom Right
        Log.i("Pixel Value", "Bottom Right pixel: " + Integer.toHexString(bmp.getPixel(31, 31)));

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
}

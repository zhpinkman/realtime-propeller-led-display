package com.example.front_sample;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class galleryActivity extends AppCompatActivity {

    public static final int IMAGE_GALLERY_REQUEST = 20;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);
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
                    int[][] rgbValues = getRGBValues(bitmap);
                    ((ImageView) findViewById(R.id.imageView)).setImageBitmap(bitmap);

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    Toast.makeText(this, "unable to open image", Toast.LENGTH_LONG).show();
                }
            }
        }
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

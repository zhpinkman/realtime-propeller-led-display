package com.example.front_sample.utils;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;

public class ImageHandler {
    public static Bitmap getSquaredBitmap(Bitmap bitmap) {
        int height = bitmap.getHeight();
        int width = bitmap.getWidth();
        int minimumDimension = Math.min(height, width);
        int[] pixels = new int[minimumDimension * minimumDimension];
        if (minimumDimension == height) {
            bitmap.getPixels(pixels, 0, minimumDimension, (width / 2 - height / 2), 0, minimumDimension, minimumDimension);
        } else {
            bitmap.getPixels(pixels, 0, minimumDimension, (height / 2 - width / 2), 0, minimumDimension, minimumDimension);
        }
        Bitmap result = Bitmap.createBitmap(pixels, 0, minimumDimension, minimumDimension, minimumDimension, Bitmap.Config.ARGB_8888);
        return result;
    }

    public static Bitmap toGrayscale(Bitmap bmpOriginal) {
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


    public static int[][] getRGBValues(Bitmap bmp) {

        int[][] rgbValues = new int[bmp.getWidth()][bmp.getHeight()];

        //get the ARGB value from each pixel of the image and store it into the array
        for (int i = 0; i < bmp.getWidth(); i++) {
            for (int j = 0; j < bmp.getHeight(); j++) {
                //This is a great opportunity to filter the ARGB values
                rgbValues[i][j] = bmp.getPixel(i, j);
            }
        }
        return rgbValues;
    }
}

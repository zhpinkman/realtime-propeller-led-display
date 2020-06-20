package com.example.front_sample.utils;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;

import com.example.front_sample.config.Config;

import java.util.ArrayList;
import java.util.List;

public class ImageHandler {
    public static Bitmap cropCenter(Bitmap bitmap) {
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

    public static Bitmap scaleBitmap(Bitmap bmp, int newWidth, int newHeight) {
        Bitmap newBmp = Bitmap.createScaledBitmap(bmp, newWidth, newHeight, false);
        bmp.recycle();
        return newBmp;
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

    public static int[][] squareToAngular(int[][] a) {
        int[][] angularResult = new int[Config.MAX_DEGREE][Config.NUM_OF_LEDS];

        for (int degree = 0; degree < 360; degree++) {
            double baseX = Math.cos(Math.toRadians(degree)) * (double) a.length / 2 / (double) Config.NUM_OF_LEDS;
            double baseY = Math.sin(Math.toRadians(degree)) * (double) a.length / 2 / (double) Config.NUM_OF_LEDS;
//            System.out.print(baseX);
//            System.out.print(", ");
//            System.out.println(baseY);
            for (int i = 0; i < Config.NUM_OF_LEDS; i++) {
                int x = (int) (Math.floor(baseX * i) + a.length / 2);
                int y = (int) (Math.floor(baseY * i) + a.length / 2);
//                System.out.print(x);
//                System.out.print(", ");
//                System.out.println(y);

//                if(degree < 180)
//                    angularResult[degree][i] = degree % 180 * 255 / 180;
//                if(degree >= 180)
//                    angularResult[degree][i] = 255 - (degree % 180 * 255 / 180);

                angularResult[degree][i] = a[y][x];
            }
        }
        return angularResult;
    }


    public static int[][] bmpToArray(Bitmap bmp) {
        int[][] grayscaleFrame = new int[bmp.getWidth()][bmp.getHeight()];
        for (int i = 0; i < bmp.getWidth(); i++) {
            for (int j = 0; j < bmp.getHeight(); j++) {
                //This is a great opportunity to filter the ARGB values
                int p = bmp.getPixel(i, j);
                int R = (p >> 16) & 0xff;
                int G = (p >> 8) & 0xff;
                int B = p & 0xff;
                grayscaleFrame[i][j] = (R + G + B) / 3;
            }
        }
        return grayscaleFrame;
    }
}

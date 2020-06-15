package com.example.front_sample.utils;

import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;

import java.util.ArrayList;
import java.util.List;

public class VideoHandler {
    public static ArrayList<Bitmap> getVideoFramesByFPS(MediaMetadataRetriever retriever, int fps) {
        try {
            String time = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
            long duration = Long.parseLong(time);
            long durationSeconds = duration / 1000;
            ArrayList<Bitmap> bArray = new ArrayList<Bitmap>();
            bArray.clear();
//            30 fps
            for (int i = 0; i < fps * durationSeconds; i++) {
                double value = (double) i * 1000000 / fps;
                System.out.println((long) value);
                bArray.add(retriever.getFrameAtTime((long) value,
                        MediaMetadataRetriever.OPTION_CLOSEST_SYNC));
            }
            return bArray;
        } catch (Exception e) {
            return null;
        }
    }

    public static ArrayList<Bitmap> getVideoFrames(MediaMetadataRetriever retriever, int frameDuration) {  // frameDuration in ms
        try {
            String time = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
            long duration = Long.parseLong(time);  //ms
            ArrayList<Bitmap> bArray = new ArrayList<Bitmap>();
            bArray.clear();
            for (int i = 0; i < duration; i += frameDuration) {
                double value = i * 1000;
//                System.out.println((long) value);
                bArray.add(retriever.getFrameAtTime((long) value,
                        MediaMetadataRetriever.OPTION_CLOSEST_SYNC));
            }
            return bArray;
        } catch (Exception e) {
            return null;
        }
    }

    public static List<Bitmap> cropCenter(List<Bitmap> bmpList) {
        List<Bitmap> croppedList = new ArrayList<>();
        for (Bitmap bmp: bmpList) {
            croppedList.add(ImageHandler.cropCenter(bmp));
            bmp.recycle();
        }
        return croppedList;
    }

    public static List<Bitmap> toGrayscale(List<Bitmap> bmpList) {
        List<Bitmap> grayscaleList = new ArrayList<>();
        for (Bitmap bmp: bmpList) {
            grayscaleList.add(ImageHandler.toGrayscale(bmp));
            bmp.recycle();
        }
        return grayscaleList;
    }

    public static List<int[][]> bmpToArray(List<Bitmap> bmpList) {
        List<int[][]> grayscaleResult = new ArrayList<>();
        for (Bitmap bmp : bmpList) {
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
            grayscaleResult.add(grayscaleFrame);
        }
        return grayscaleResult;
    }


}

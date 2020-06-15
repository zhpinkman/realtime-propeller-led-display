package com.example.front_sample.utils;

import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;

import java.util.ArrayList;

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
                System.out.println((long) value);
                bArray.add(retriever.getFrameAtTime((long) value,
                        MediaMetadataRetriever.OPTION_CLOSEST_SYNC));
            }
            return bArray;
        } catch (Exception e) {
            return null;
        }
    }
}

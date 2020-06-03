package com.example.front_sample.activities;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.MediaController;
import android.widget.VideoView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.front_sample.R;

import java.io.File;


public class VideoActivity extends AppCompatActivity {

    private static final int VIDEO_GALLERY_REQUEST = 30;
    private VideoView videoView;
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


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == VIDEO_GALLERY_REQUEST && resultCode == RESULT_OK) {
            Uri uri = data.getData();
            if (uri != null)
            videoView.setVideoURI(uri);
            videoView.start();
        }
    }

    public void init() {
        videoView = findViewById(R.id.videoView3);
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
}

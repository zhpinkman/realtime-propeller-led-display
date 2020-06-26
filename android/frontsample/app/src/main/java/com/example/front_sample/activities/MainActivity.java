package com.example.front_sample.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.front_sample.R;

public class MainActivity extends AppCompatActivity {

    public static final String EXTRA_MESSAGE = "com.example.myfirstapp.MESSAGE";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    public void onGallerySelect(View view) {
        Intent intent = new Intent(this, GalleryActivity.class);
        startActivity(intent);
    }

    public void onPaintSelect(View view) {
        Intent intent = new Intent(this, PaintActivity.class);
        startActivity(intent);
    }

    public void onUDPSelect(View view) {
        Intent intent = new Intent(this, UDPActivity.class);
        startActivity(intent);
    }

    public void onVideoActivity(View view) {
        Intent intent = new Intent(this, VideoActivity.class);
        startActivity(intent);
    }

    public void onCircularTextActivity(View view) {
        Intent intent = new Intent(this, CircularTextActivity.class);
        startActivity(intent);
    }

}

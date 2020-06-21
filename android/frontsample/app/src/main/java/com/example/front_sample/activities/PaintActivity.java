package com.example.front_sample.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.example.front_sample.R;
import com.example.front_sample.utils.PaintView;


public class PaintActivity extends AppCompatActivity {

    private PaintView paintView;
    private int finalWidth = 256;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paint);
        paintView = (PaintView) findViewById(R.id.myPaintView);
        DisplayMetrics metrics = new DisplayMetrics();

        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        metrics.heightPixels = metrics.widthPixels;
        int customLen = 0;
        while (customLen <= metrics.widthPixels)
            customLen += finalWidth;
        customLen -= finalWidth;
        metrics.heightPixels = customLen;
        metrics.widthPixels = customLen;
        paintView.init(metrics);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.saveImage:
                paintView.saveImage();
                return true;
            case R.id.clear:
                paintView.clear();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

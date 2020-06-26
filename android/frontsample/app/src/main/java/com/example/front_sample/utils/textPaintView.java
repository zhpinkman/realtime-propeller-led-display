package com.example.front_sample.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Environment;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;


import java.io.File;
import java.lang.Math;
import java.io.FileOutputStream;

import com.example.front_sample.utils.udp.UDPHandler;

public class textPaintView extends View {

    private UDPHandler udpHandler = UDPHandler.getInstance();
    private Bitmap mBitmap;
    private Bitmap circularBitmap;
    private Canvas mCanvas;
    private Paint drawPaint;
    private String textShown;
    private int DEFAULT_HEIGHT = 120;
    private int SPACING_DIST = 30;
    private int circularBitmapWidth = 600;
    private Paint mBitmapPaint = new Paint(Paint.DITHER_FLAG);
    private int finalWidth = circularBitmapWidth;
    private int[][] picArray = new int[finalWidth][finalWidth];

    public textPaintView(Context context) {
        this(context, null);
    }

    public textPaintView(Context context, AttributeSet attrs) {
        super(context, attrs);
        drawPaint = new Paint();
        drawPaint.setColor(Color.BLACK);
        drawPaint.setAntiAlias(true);
        drawPaint.setStrokeWidth(5);
        drawPaint.setStyle(Paint.Style.STROKE);
        drawPaint.setStrokeJoin(Paint.Join.ROUND);
        drawPaint.setStrokeCap(Paint.Cap.ROUND);

        //font setting
        drawPaint.setTextSize(100f);
        Typeface plain = Typeface.createFromAsset(getContext().getAssets(), "fonts/cmunbtl.ttf");
        drawPaint.setTypeface(plain);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if ( textShown == null) textShown = "";
        mCanvas.drawColor(Color.WHITE);
        mCanvas.drawText(textShown, 0, 90f, drawPaint);
        makeCircular();
        canvas.drawBitmap(mBitmap, 0, 0, mBitmapPaint);
        canvas.drawBitmap(circularBitmap, (mBitmap.getWidth() - circularBitmapWidth) / 2 , DEFAULT_HEIGHT + SPACING_DIST, mBitmapPaint);
    }

    private void makeCircular(){
        int w = circularBitmapWidth;
        int h = mBitmap.getHeight();
        //int alignToMid = mBitmap.getWidth()/2 - w/2;

        for ( int i = 0 ; i < w ; i++ )
            for ( int j = 0 ; j < w ; j++ )
            {
                int x = i - w/2;
                int y = j - w/2;
                double minRad = 0.5;
                double deg = 0;
                double rad = 0 ;
                int x2 = 0;
                int y2 = 0;

                rad = Math.sqrt(x*x + y*y) / (w/2);
                if ( rad >= 1 ) continue;
                if ( rad <= minRad ) continue ;

                if ( x == 0 && y >= 0)
                    deg = 0;
                else if ( x == 0 && y < 0 )
                    deg = Math.PI;
                else
                    deg = Math.atan2(y,x);

                deg = deg / (2*Math.PI);
                if ( deg < 0 ) deg += 1;

                x2 = (int) (deg * mBitmap.getWidth());
                y2 = (int) (((rad - minRad) * h) / (1 - minRad));
                int color = mBitmap.getPixel(x2, mBitmap.getHeight() - y2 - 1);
                circularBitmap.setPixel(w - i, w - j,color);
            }
    }

    public void init(DisplayMetrics metrics) {
        int width = metrics.widthPixels;
        int height = DEFAULT_HEIGHT;

        mBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        circularBitmap = Bitmap.createBitmap(circularBitmapWidth, circularBitmapWidth, Bitmap.Config.ARGB_8888);
        mCanvas = new Canvas(mBitmap);
    }

    public void editAndSend(){
        int w = circularBitmap.getWidth() ;
        int h = circularBitmap.getHeight();

        for (int i = 0; i < finalWidth; i++)
            for (int j = 0; j < finalWidth; j++) {
                if ( circularBitmap.getPixel(i,j) == Color.BLACK )
                    picArray[i][j] = 255;
            }
        udpHandler.setSquareContext(picArray);
    }

    public void foo(final String text) {
        textShown = text;
        invalidate();
        editAndSend();
    }

}

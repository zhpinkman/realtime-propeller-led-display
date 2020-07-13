package com.example.front_sample.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Environment;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;

import java.lang.Math;

import com.example.front_sample.config.Config;
import com.example.front_sample.utils.udp.UDPHandler;

public class textPaintView extends View {

    private UDPHandler udpHandler = UDPHandler.getInstance();
    private Bitmap mBitmap;
    private Bitmap circularBitmap;
    private Canvas mCanvas;
    private Paint drawPaint;
    private String textShown;
    private int DEFAULT_HEIGHT = 90;
    private int SPACING_DIST = 30;
    private int circularBitmapWidth = 600;
    private Paint mBitmapPaint = new Paint(Paint.DITHER_FLAG);
    private int finalWidth = circularBitmapWidth;
    private int[][] picArray = new int[finalWidth][finalWidth];

    private int[][][] Alphabet = new int[26][10][7];

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
        mCanvas.drawText(textShown, 0, 65f, drawPaint);
        stretchBitmap();
        makeCircular();
        previewBitmap(circularBitmap);
        canvas.drawBitmap(mBitmap, 0, 0, mBitmapPaint);
        canvas.drawBitmap(circularBitmap, (mBitmap.getWidth() - circularBitmapWidth) / 2 , DEFAULT_HEIGHT + SPACING_DIST, mBitmapPaint);
    }

    private void stretchBitmap() {
        int w = mBitmap.getWidth();
        int h = mBitmap.getHeight();
        int[][] bitmapArray = new int[w][h];
        for (int i = 0 ; i < w ; i++)
            for (int j = 0; j < h; j++)
                bitmapArray[i][j] = mBitmap.getPixel(i,j);

        for ( int i = 0 ; i < w ; i++ ) {
            for (int j = 0; j < h; j++) {
                mBitmap.setPixel(i, j, bitmapArray[i/2][j]);
            }
        }
    }

    private void previewBitmap(Bitmap tmp){
        int w = tmp.getWidth();
        int h = tmp.getHeight();
        int[][] bitmapArray = new int[w][h];
        int[][] bitmapFinal = new int[w][h];
        for (int i = 0 ; i < w ; i++)
            for (int j = 0; j < h; j++) {
                bitmapArray[i][j] = tmp.getPixel(i, j);
                bitmapFinal[i][j] = 0;
            }

        for (int degree = 0; degree < 360; degree++) {
            double baseX = Math.cos(Math.toRadians(degree)) * (double) w / 2 / (double) Config.NUM_OF_LEDS;
            double baseY = Math.sin(Math.toRadians(degree)) * (double) w / 2 / (double) Config.NUM_OF_LEDS;
            for (int i = 0; i < Config.NUM_OF_LEDS; i++) {
                int x = (int) (Math.floor(baseX * i) + w / 2);
                int y = (int) (Math.floor(baseY * i) + w / 2);
                bitmapFinal[y][x] = bitmapArray[y][x];

                for (int dx = -4; dx <= 4; dx++)
                    for (int dy = -4 ; dy <= 4 ; dy++)
                    {
                        bitmapFinal[dy + y][dx + x] = bitmapArray[y][x];
                    }
            }
        }
        for ( int i = 0 ; i < w ; i++ ) {
            for (int j = 0; j < h; j++) {
                tmp.setPixel(i, j, bitmapFinal[i][j]);
            }
        }

    }

    private void makeCircular(){
        int w = circularBitmapWidth;
        int h = mBitmap.getHeight();
        double minRad = 0.2;

        for ( int i = 0 ; i < w ; i++ )
            for ( int j = 0 ; j < w ; j++ )
            {
                int x = i - w/2;
                int y = j - w/2;
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
        setAlphabet();
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
        //udpHandler.setSquareContext(picArray);
    }

    public void foo(final String text) {
        textShown = text;
        invalidate();
        editAndSend();
    }

}

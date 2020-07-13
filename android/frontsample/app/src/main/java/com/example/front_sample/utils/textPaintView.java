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
    private String textShown;
    private Canvas mCanvas;
    private Paint mPaint;
    private int Scale = 20;
    private Paint mBitmapPaint = new Paint(Paint.DITHER_FLAG);
    int [][][] alphabet = new int[26][Config.ALPHABET_HEIGHT][Config.ALPHABET_LENGTH];

    public textPaintView(Context context) {
        this(context, null);
    }

    public textPaintView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mPaint = new Paint();
        mPaint.setColor(Color.WHITE);
        mPaint.setAntiAlias(true);
        mPaint.setStrokeWidth(5);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStyle(Paint.Style.FILL);

    }
    @Override
    protected void onDraw(Canvas canvas) {
        if ( textShown == null)
            textShown = "";
        /*
        if ( textShown.length() > 0 ) {
            char lastChar = textShown.charAt(textShown.length() - 1);
            if ('a' <= lastChar && lastChar <= 'z')
                setText(lastChar - 'a');
            if ('A' <= lastChar && lastChar <= 'Z')
                setText(lastChar - 'A');
        }*/
        //previewBitmap(circularBitmap);
        canvas.drawBitmap(mBitmap, 0, 0, mBitmapPaint);
    }

    private void setText(int c){

        int w = mBitmap.getWidth();
        for (int i = 0 ; i < Scale*Config.ALPHABET_HEIGHT  ; i++)
            for (int j = 0 ; j < Scale*Config.ALPHABET_LENGTH ; j++)
                mBitmap.setPixel(j,i,Color.WHITE);

        for ( int i = 0 ; i < Scale*Config.ALPHABET_HEIGHT ; i++ )
            for ( int j = 0 ; j < Scale*Config.ALPHABET_LENGTH ; j++ )
                if ( alphabet[c][i/Scale][j/Scale] == 1 ) {
                    mBitmap.setPixel(j, i, Color.BLACK);
                }

    }

    private void setAlphabet(){
        //a
        alphabet[0] = new int[][]  {{0, 0, 1, 0, 0},
                                    {0, 1, 0, 1, 0},
                                    {1, 0, 0, 0, 1},
                                    {1, 0, 0, 0, 1},
                                    {1, 1, 1, 1, 1},
                                    {1, 0, 0, 0, 1},
                                    {1, 0, 0, 0, 1},
                                    {0, 0, 0, 0, 0},
                                    {0, 0, 0, 0, 0},
                                    {0, 0, 0, 0, 0}};

        //b
        alphabet[1] = new int[][]  {{1,1,1,1,0},
                                    {1,0,0,0,1},
                                    {1,0,0,0,1},
                                    {1,1,1,1,0},
                                    {1,0,0,0,1},
                                    {1,0,0,0,1},
                                    {1,1,1,1,0},
                                    {0,0,0,0,0},
                                    {0,0,0,0,0},
                                    {0,0,0,0,0} };

        //c
        alphabet[2] = new int[][]  {{0,1,1,1,0},
                                    {1,0,0,0,1},
                                    {1,0,0,0,0},
                                    {1,0,0,0,0},
                                    {1,0,0,0,0},
                                    {1,0,0,0,1},
                                    {0,1,1,1,0},
                                    {0,0,0,0,0},
                                    {0,0,0,0,0},
                                    {0,0,0,0,0} };

        //d
        alphabet[3] = new int[][]  {{1,1,1,1,0},
                                    {1,0,0,0,1},
                                    {1,0,0,0,1},
                                    {1,0,0,0,1},
                                    {1,0,0,0,1},
                                    {1,0,0,0,1},
                                    {1,1,1,1,0},
                                    {0,0,0,0,0},
                                    {0,0,0,0,0},
                                    {0,0,0,0,0} };

        //e
        alphabet[4] = new int[][]  {{1,1,1,1,1},
                                    {1,0,0,0,0},
                                    {1,0,0,0,0},
                                    {1,1,1,1,0},
                                    {1,0,0,0,0},
                                    {1,0,0,0,0},
                                    {1,1,1,1,1},
                                    {0,0,0,0,0},
                                    {0,0,0,0,0},
                                    {0,0,0,0,0} };

        //f
        alphabet[5] = new int[][]  {{1,1,1,1,1},
                                    {1,0,0,0,0},
                                    {1,0,0,0,0},
                                    {1,1,1,1,0},
                                    {1,0,0,0,0},
                                    {1,0,0,0,0},
                                    {1,0,0,0,0},
                                    {0,0,0,0,0},
                                    {0,0,0,0,0},
                                    {0,0,0,0,0} };

        //g
        alphabet[6] = new int[][]  {{0,1,1,1,1},
                                    {1,0,0,0,0},
                                    {1,0,0,0,0},
                                    {1,0,0,0,0},
                                    {1,0,0,1,1},
                                    {1,0,0,0,1},
                                    {0,1,1,1,1},
                                    {0,0,0,0,0},
                                    {0,0,0,0,0},
                                    {0,0,0,0,0} };

        //h
        alphabet[7] = new int[][]  {{1,0,0,0,1},
                                    {1,0,0,0,1},
                                    {1,0,0,0,1},
                                    {1,1,1,1,1},
                                    {1,0,0,0,1},
                                    {1,0,0,0,1},
                                    {1,0,0,0,1},
                                    {0,0,0,0,0},
                                    {0,0,0,0,0},
                                    {0,0,0,0,0} };

        //i
        alphabet[8] = new int[][]  {{0,1,1,1,0},
                                    {0,0,1,0,0},
                                    {0,0,1,0,0},
                                    {0,0,1,0,0},
                                    {0,0,1,0,0},
                                    {0,0,1,0,0},
                                    {0,1,1,1,0},
                                    {0,0,0,0,0},
                                    {0,0,0,0,0},
                                    {0,0,0,0,0} };

        //j
        alphabet[9] = new int[][] {{0,0,0,0,1},
                                    {0,0,0,0,1},
                                    {0,0,0,0,1},
                                    {0,0,0,0,1},
                                    {0,0,0,0,1},
                                    {1,0,0,0,1},
                                    {0,1,1,1,0},
                                    {0,0,0,0,0},
                                    {0,0,0,0,0},
                                    {0,0,0,0,0} };

        //k
        alphabet[10] = new int[][]  {{1,0,0,0,1},
                {1,0,0,1,0},
                {1,0,1,0,0},
                {1,1,0,0,0},
                {1,0,1,0,0},
                {1,0,0,1,0},
                {1,0,0,0,1},
                {0,0,0,0,0},
                {0,0,0,0,0},
                {0,0,0,0,0} };

        //l
        alphabet[11] = new int[][]  {{1,0,0,0,0},
                {1,0,0,0,0},
                {1,0,0,0,0},
                {1,0,0,0,0},
                {1,0,0,0,0},
                {1,0,0,0,0},
                {1,1,1,1,1},
                {0,0,0,0,0},
                {0,0,0,0,0},
                {0,0,0,0,0} };

        //m
        alphabet[12] = new int[][]  {{1,0,0,0,1},
                {1,1,0,1,1},
                {1,0,1,0,1},
                {1,0,1,0,1},
                {1,0,0,0,1},
                {1,0,0,0,1},
                {1,0,0,0,1},
                {0,0,0,0,0},
                {0,0,0,0,0},
                {0,0,0,0,0} };

        //n
        alphabet[13] = new int[][]  {{1,0,0,0,1},
                {1,0,0,0,1},
                {1,1,0,0,1},
                {1,0,1,0,1},
                {1,0,0,1,1},
                {1,0,0,0,1},
                {1,0,0,0,1},
                {0,0,0,0,0},
                {0,0,0,0,0},
                {0,0,0,0,0} };

        //o
        alphabet[14] = new int[][]  {{0,1,1,1,0},
                {1,0,0,0,1},
                {1,0,0,0,1},
                {1,0,0,0,1},
                {1,0,0,0,1},
                {1,0,0,0,1},
                {0,1,1,1,0},
                {0,0,0,0,0},
                {0,0,0,0,0},
                {0,0,0,0,0} };

        //p
        alphabet[15] = new int[][]  {{1,1,1,1,0},
                {1,0,0,0,1},
                {1,0,0,0,1},
                {1,0,0,0,1},
                {1,1,1,1,0},
                {1,0,0,0,0},
                {1,0,0,0,0},
                {0,0,0,0,0},
                {0,0,0,0,0},
                {0,0,0,0,0} };

        //q
        alphabet[16] = new int[][]  {{0,1,1,1,0},
                {1,0,0,0,1},
                {1,0,0,0,1},
                {1,0,0,0,1},
                {1,0,1,0,1},
                {1,0,0,1,0},
                {0,1,1,0,1},
                {0,0,0,0,0},
                {0,0,0,0,0},
                {0,0,0,0,0} };

        //r
        alphabet[17] = new int[][]  {{1,1,1,1,1},
                {1,0,0,0,1},
                {1,0,0,0,1},
                {1,1,1,1,0},
                {1,0,1,0,0},
                {1,0,0,1,0},
                {1,0,0,0,1},
                {0,0,0,0,0},
                {0,0,0,0,0},
                {0,0,0,0,0} };

        //s
        alphabet[18] = new int[][]  {{0,1,1,1,0},
                {1,0,0,0,1},
                {1,0,0,0,0},
                {0,1,1,1,0},
                {0,0,0,0,1},
                {1,0,0,0,1},
                {1,1,1,1,1},
                {0,0,0,0,0},
                {0,0,0,0,0},
                {0,0,0,0,0} };

        //t
        alphabet[19] = new int[][]  {{1,1,1,1,1},
                {0,0,1,0,0},
                {0,0,1,0,0},
                {0,0,1,0,0},
                {0,0,1,0,0},
                {0,0,1,0,0},
                {0,0,1,0,0},
                {0,0,0,0,0},
                {0,0,0,0,0},
                {0,0,0,0,0} };

        //u
        alphabet[20] = new int[][]  {{1,0,0,0,1},
                {1,0,0,0,1},
                {1,0,0,0,1},
                {1,0,0,0,1},
                {1,0,0,0,1},
                {1,0,0,0,1},
                {0,1,1,1,0},
                {0,0,0,0,0},
                {0,0,0,0,0},
                {0,0,0,0,0} };

        //v
        alphabet[21] = new int[][]  {{1,0,0,0,1},
                {1,0,0,0,1},
                {1,0,0,0,1},
                {1,0,0,0,1},
                {1,0,0,0,1},
                {0,1,0,1,0},
                {0,0,1,0,0},
                {0,0,0,0,0},
                {0,0,0,0,0},
                {0,0,0,0,0} };

        //w
        alphabet[22] = new int[][]  {{1,0,0,0,1},
                {1,0,0,0,1},
                {1,0,0,0,1},
                {1,0,1,0,1},
                {1,0,1,0,1},
                {1,1,0,1,1},
                {1,0,0,0,1},
                {0,0,0,0,0},
                {0,0,0,0,0},
                {0,0,0,0,0} };

        //x
        alphabet[23] = new int[][]  {{1,0,0,0,1},
                {1,0,0,0,1},
                {0,1,0,1,0},
                {0,0,1,0,0},
                {0,1,0,1,0},
                {1,0,0,0,1},
                {1,0,0,0,1},
                {0,0,0,0,0},
                {0,0,0,0,0},
                {0,0,0,0,0} };

        //y
        alphabet[24] = new int[][]  {{1,0,0,0,1},
                {1,0,0,0,1},
                {0,1,1,1,0},
                {0,0,1,0,0},
                {0,0,1,0,0},
                {0,0,1,0,0},
                {0,0,1,0,0},
                {0,0,0,0,0},
                {0,0,0,0,0},
                {0,0,0,0,0} };

        //z
        alphabet[25] = new int[][]  {{1,1,1,1,1},
                {0,0,0,0,1},
                {0,0,0,1,0},
                {0,0,1,0,0},
                {0,1,0,0,0},
                {1,0,0,0,0},
                {1,1,1,1,1},
                {0,0,0,0,0},
                {0,0,0,0,0},
                {0,0,0,0,0} };
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

    public void init(DisplayMetrics metrics) {
        int width = metrics.widthPixels;
        int height = width;
        setAlphabet();
        mBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        mCanvas = new Canvas(mBitmap);
        mCanvas.drawColor(Color.BLACK);
    }

    private int getAlphabet(int c, int x, int y)
    {
        if ( alphabet[c][Config.ALPHABET_HEIGHT - y - 1][x] == 1 )
            return 255;
        return 0;
    }

    public void editAndSend(){
        int[][] sendData = new int[Config.NUM_OF_LEDS][Config.MAX_DEGREE];
        int[][] showOnboard = new int[Config.MAX_DEGREE][Config.NUM_OF_LEDS];
        int filled = 0;
        for (char ch: textShown.toCharArray()) {
            int c = -1;
            if ('a' <= ch && ch <= 'z')
                c = ch - 'a';
            if ('A' <= ch && ch <= 'Z')
                c = ch - 'A';
            if (0 <= c && c <= 25) {
                for (int i = 0; i < Config.ALPHABET_LENGTH; i++)
                    for (int j = 0; j < Config.ALPHABET_HEIGHT; j++) {
                        sendData[j][(filled + i) * 5] = getAlphabet(c, i, j);
                    }
            }

            filled += Config.ALPHABET_LENGTH + 1;
        }
        mCanvas.drawColor(Color.BLACK);
        double r = mBitmap.getWidth() / 2;
        for (int degree = 0; degree < 360; degree++) {
            double baseX = Math.cos(Math.toRadians(degree)) * (double) r / (double) Config.NUM_OF_LEDS;
            double baseY = Math.sin(Math.toRadians(degree)) * (double) r / (double) Config.NUM_OF_LEDS;
            for (int i = 0; i < Config.NUM_OF_LEDS; i++) {
                int x = (int) (Math.floor(baseX * i) + r);
                int y = (int) (Math.floor(baseY * i) + r);
                if ( sendData[i][359 - degree] != 0)
                {
                    showOnboard[359 - degree][i] = 255;
                    mCanvas.drawCircle(y, x, 10, mPaint);
                }
            }
        }

        udpHandler.setAngularContext(showOnboard);
    }

    public void foo(final String text) {
        textShown = text;
        invalidate();
        editAndSend();
    }

}

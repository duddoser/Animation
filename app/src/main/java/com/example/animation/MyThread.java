package com.example.animation;

import android.animation.ArgbEvaluator;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.SurfaceHolder;

public class MyThread extends Thread {

    private final int REDRAW_TIME=100;
    private final int ANIMATION_TIME=15000000;
    private boolean flag;
    private long startTime;
    private long prevRedrawTime;
    private Paint paint;
    private ArgbEvaluator argbEvaluator;
    private SurfaceHolder surfaceHolder;


    MyThread(SurfaceHolder h){
        surfaceHolder = h;
        flag = false;
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.FILL);
        argbEvaluator = new ArgbEvaluator();
    }

    public long getTime(){
        return System.nanoTime()/1000;
    }

    public void setRunning(boolean running){
        flag = running;
        prevRedrawTime = getTime();
    }


    @Override
    public void run() {
        Canvas canvas;
        startTime = getTime();
        while(flag){
            long currentTime = getTime();
            long elapsedTime = currentTime - prevRedrawTime;
            if (elapsedTime < REDRAW_TIME){
                continue;
            }
            canvas = null;
            canvas = surfaceHolder.lockCanvas();

            draw(canvas);

            surfaceHolder.unlockCanvasAndPost(canvas);
            prevRedrawTime = getTime();
        }
    }

    public void draw(Canvas canvas){
        long currentTime = getTime() - startTime;
        int width = canvas.getWidth();
        int height = canvas.getHeight();

        canvas.drawColor(Color.YELLOW);
        int centerX = width/2;
        int centerY = height/2;
        float maxRadius = Math.min(width, height)/2;
        float fraction = (float) (currentTime%ANIMATION_TIME)/ANIMATION_TIME;
        int color = (int) argbEvaluator.evaluate(fraction, Color.BLUE, Color.RED);
        paint.setColor(color);
        canvas.drawCircle(centerX, centerY, maxRadius * fraction, paint);
    }
}

package com.scott.honerv8loadingview.view;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>Author:    shijiale</p>
 * <p>Date:      2017/5/10.</p>
 * <p>Email:     shijl5@lenovo.com</p>
 * <p>Describe:</p>
 */

public class InterpolatorView extends View implements ValueAnimator.AnimatorListener,ValueAnimator.AnimatorUpdateListener{
    private Interpolator mInterpolator;
    private ValueAnimator mAnimator;
    private int rx = 8;
    private int ry = 8;
    private int h;
    private int w;
    private Paint mPaint;
    private float max = 0;
    private int peer_w = 50;
    private int peer_h = 10;
    private final String TAG = "InterpolatorView";
    private List<Point> list = new ArrayList<>();
    private long time;

    public InterpolatorView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(Color.BLUE);
        mPaint.setStrokeWidth(5);

        mAnimator = new ValueAnimator();
        mAnimator.setDuration(1000);
        mAnimator.setFloatValues(0,3000);
        mAnimator.setInterpolator(new DecelerateInterpolator());
        mAnimator.addUpdateListener(this);
        mAnimator.addListener(this);
        mAnimator.start();
    }

    public void setInterpolator(Interpolator itp) {
        mAnimator.setInterpolator(itp);
    }

    public void setDurTime(long time) {
        mAnimator.setDuration(time);
    }


    public void setMax(float value) {
        mAnimator.setFloatValues(0,value);
        this.max = value;
    }
    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawLine(rx,ry,h,rx,mPaint);
        canvas.drawLine(rx,ry,rx,w,mPaint);

        int allH = h - 8;
        int allW = w - 8;

        long dur = mAnimator.getDuration();
        long format = (dur * peer_w) / w;
        int count = (int) (allW / peer_w);
        mPaint.setColor(Color.GRAY);
        for(int i = 1; i <= count; i ++) {
            canvas.drawLine(rx + 5,ry + (i * peer_w),rx + peer_h + 5,ry + (i * peer_w),mPaint);
        }

        count = allH / peer_h;
        for(int i = 1; i <= count; i++) {
            canvas.drawLine(rx + (i * peer_w),ry + 5,rx + (i * peer_w),ry + peer_h + 5,mPaint);
        }

        if(list.isEmpty()) return;
        mPaint.setColor(Color.BLUE);
        mPaint.setStyle(Paint.Style.FILL);
        for(int i = 0; i < list.size() - 1; i++) {
            Point p = list.get(i);
            Point p2 = list.get(i + 1);
            p.x = (int) ((p.x * (1080 - 16)) / 3000);
            p.y = (int) ((p.y * allW) / dur);
            p2.x = (int) ((p2.x * (1080 - 16)) / 3000);
            p2.y = (int) ((p2.y * allW) / dur);
            canvas.drawLine(p.y,p2.x,p.x,p2.y,mPaint);
            Log.i(TAG,"X = " + p.x + ", Y = " + p.y);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        h = getWidth();
        w = getHeight();

        h -= 8;
        w -= 8;
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    public void onAnimationStart(Animator animation) {
        list.clear();
    }

    @Override
    public void onAnimationEnd(Animator animation) {
        postInvalidate();
    }

    @Override
    public void onAnimationCancel(Animator animation) {

    }

    @Override
    public void onAnimationRepeat(Animator animation) {

    }

    @Override
    public void onAnimationUpdate(ValueAnimator animation) {
        float value = Float.parseFloat(animation.getAnimatedValue().toString());
        long t = animation.getCurrentPlayTime();

        //Log.i(TAG, "y = " + value);
        list.add(new Point((int)value,(int)t));
    }
}

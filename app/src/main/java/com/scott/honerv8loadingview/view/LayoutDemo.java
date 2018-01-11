package com.scott.honerv8loadingview.view;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.DragEvent;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnticipateOvershootInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.LinearLayout;

/**
 * <p>Author:    shijiale</p>
 * <p>Date:      2017/5/10.</p>
 * <p>Email:     shijl5@lenovo.com</p>
 * <p>Describe:</p>
 */

public class LayoutDemo extends ViewGroup implements GestureDetector.OnGestureListener{
    private final String TAG = "LayoutDemo";
    private GestureDetector gd;

    public LayoutDemo(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        gd = new GestureDetector(context,this);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        //Log.i(TAG,"l = " + l + ",t = " + t + ",r = " + r + ",b = " + b);
        //Log.i(TAG,"onLayout === >");
        for(int i = 0; i < getChildCount(); i++) {
            View v = getChildAt(i);
            v.layout(l + (i * v.getMeasuredWidth()),t,(i + 1)*v.getMeasuredWidth(),v.getMeasuredHeight());
            Log.i(TAG,"child = " + v.getMeasuredWidth() + ",h = " + v.getMeasuredHeight());
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //Log.i(TAG,"onMeasure === >");

        int h = MeasureSpec.getSize(heightMeasureSpec);
        int mode = MeasureSpec.getMode(heightMeasureSpec);
        Log.i(TAG,"h = " + h + ",mode = " + mode);
        switch (mode) {
            case MeasureSpec.AT_MOST: {
                Log.i(TAG,"AT_MOST");
            }break;
            case MeasureSpec.EXACTLY: {
                Log.i(TAG,"EXACTLY");
            }break;
            case MeasureSpec.UNSPECIFIED: {
                Log.i(TAG,"UNSPECIFIED");
            }break;
        }

        h = h / getChildCount();
        for(int i = 0; i < getChildCount(); i++) {
            View v = getChildAt(i);
            //MarginLayoutParams lp = (MarginLayoutParams) v.getLayoutParams();
            Log.i(TAG,"h = " + h);
            int child = MeasureSpec.makeMeasureSpec(h,MeasureSpec.EXACTLY);
            v.measure(widthMeasureSpec,child);
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    private float x = 0;
    private float y = 0;
    private boolean isAutoScroll = false;
    ObjectAnimator anim;
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        gd.onTouchEvent(event);
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                if(anim != null  && anim.isRunning()) {
                    anim.pause();
                    setScrollX(0);
                }
                x = event.getX();
                y = event.getY();
                //Log.i(TAG,"down x = " + x + ",y = " + y);
            }break;
            case MotionEvent.ACTION_MOVE: {
                //Log.i(TAG,"move x = " + getX() + ",y = " + y);
                //Log.i(TAG,"scrollX = " + getScrollX());
                if(isAutoScroll) {
                    return true;
                }
                if(Math.abs(getScrollX()) <= getWidth()/2) {
                    scrollBy(-(int) (event.getX() - x), 0);
                    x = event.getX();
                }

                if(getScrollX() >= getWidth() / 5) {
                    anim = new ObjectAnimator();
                }

            }break;
            case MotionEvent.ACTION_UP: {
                final float scrollX = getScrollX();
                anim = new ObjectAnimator();
                anim.setTarget(this);
                anim.setDuration(1000);
                anim.setInterpolator(new OvershootInterpolator(0.67f));
                anim.setFloatValues(scrollX,0);
                anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        //Log.i(TAG,"value = " + animation.getAnimatedValue());
                        scrollTo((int)Float.parseFloat(animation.getAnimatedValue().toString()),0);
                    }
                });
                anim.start();
            }break;
        }
        return super.onTouchEvent(event);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        super.dispatchTouchEvent(ev);
        return true;
    }

    @Override
    public boolean onDown(MotionEvent e) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        Log.i(TAG,"onFling");
        return false;
    }
}

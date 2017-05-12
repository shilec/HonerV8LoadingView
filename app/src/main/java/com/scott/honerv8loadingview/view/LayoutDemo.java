package com.scott.honerv8loadingview.view;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

/**
 * <p>Author:    shijiale</p>
 * <p>Date:      2017/5/10.</p>
 * <p>Email:     shijl5@lenovo.com</p>
 * <p>Describe:</p>
 */

public class LayoutDemo extends ViewGroup {
    private final String TAG = "LayoutDemo";

    public LayoutDemo(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
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
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                x = event.getX();
                y = event.getY();
                Log.i(TAG,"down x = " + x + ",y = " + y);
            }break;
            case MotionEvent.ACTION_MOVE: {
                Log.i(TAG,"move x = " + x + ",y = " + y);
                Log.i(TAG,"scrollX = " + getScrollX());
                scrollBy((int) (x - getX()),0);
            }break;
            case MotionEvent.ACTION_UP: {

            }break;
        }
        return super.onTouchEvent(event);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        super.dispatchTouchEvent(ev);
        return true;
    }
}

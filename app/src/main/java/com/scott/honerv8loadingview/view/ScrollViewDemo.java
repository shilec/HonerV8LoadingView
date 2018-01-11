package com.scott.honerv8loadingview.view;


import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.Configuration;
import android.support.annotation.FloatRange;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AnticipateOvershootInterpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.OvershootInterpolator;

/**
 * <p>Author:    shijiale</p>
 * <p>Date:      2017/5/25.</p>
 * <p>Email:     shijl5@lenovo.com</p>
 * <p>Describe:</p>
 */

public class ScrollViewDemo extends ViewGroup implements ValueAnimator.AnimatorUpdateListener,Animator.AnimatorListener{
    private final String TAG = "ScrollViewDemo";
    private float allWidth = 0;
    private ValueAnimator mAnim;
    public ScrollViewDemo(Context context, AttributeSet attrs) {
        super(context, attrs);
        mAnim = new ValueAnimator();
        mAnim.setDuration(300);
        mAnim.setInterpolator(new LinearInterpolator());
        mAnim.addUpdateListener(this);
        mAnim.addListener(this);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int count = getChildCount();
        int left = 0;
        for(int i = 0; i < count; i++) {
            View v = getChildAt(i);
            MarginLayoutParams lp = (MarginLayoutParams) v.getLayoutParams();

            l = left;
            t = lp.topMargin;
            r = v.getMeasuredWidth() + left;
            b = t + v.getMeasuredHeight();
            v.layout(l,t,r,b);
            left += v.getMeasuredWidth();
        }
        allWidth = left - getWidth();
        Log.i(TAG,"allWidth = " + allWidth);
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MarginLayoutParams(getContext(),attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int count = getChildCount();
        int maxW = 0;
        int maxH = 0;
        for(int i = 0; i < count; i++) {
            View v = getChildAt(i);
            MarginLayoutParams lp = (MarginLayoutParams) v.getLayoutParams();

            int h = v.getMeasuredHeight();
            int w = v.getMeasuredWidth();
            if(w + lp.leftMargin > maxW) {
                maxW = w;
            }
            if((h + lp.topMargin + lp.bottomMargin) > maxH) {
                maxH = h + lp.topMargin + lp.bottomMargin;
            }
            if(i == 0) {
                v.measure(widthMeasureSpec, h);
            } else {
                v.measure(w,h);
            }
        }
        int mscH = MeasureSpec.makeMeasureSpec(maxH,MeasureSpec.EXACTLY);
        super.onMeasure(widthMeasureSpec,mscH);
    }

    private float mEventX = 0;
    private float mEventY = 0;

    /**
     * getScrollX 大于0 向左滑
     * getScrollX小于0 向右滑
     * @param event
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                mEventX = event.getX();
                mEventY = event.getY();
            }break;
            case MotionEvent.ACTION_MOVE: {
                float scrollX = event.getX() - mEventX;
                scrollBy(-(int) scrollX,0);
                Log.i(TAG,"getScrollX = " + getScrollX());
                Log.i(TAG,"scrollX = " + scrollX);
                mEventX = event.getX();
            }break;
            case MotionEvent.ACTION_UP: {
            }break;
        }
        return true;
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        super.dispatchTouchEvent(ev);
        return true;
    }



    @Override
    public void onAnimationUpdate(ValueAnimator animation) {
    }

    @Override
    public void onAnimationStart(Animator animation) {

    }

    @Override
    public void onAnimationEnd(Animator animation) {
    }

    @Override
    public void onAnimationCancel(Animator animation) {

    }

    @Override
    public void onAnimationRepeat(Animator animation) {

    }
}

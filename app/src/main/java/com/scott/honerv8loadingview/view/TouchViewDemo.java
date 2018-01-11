package com.scott.honerv8loadingview.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;

/**
 * <p>Author:    shijiale</p>
 * <p>Date:      2017/5/26.</p>
 * <p>Email:     shijl5@lenovo.com</p>
 * <p>Describe:</p>
 */

public class TouchViewDemo extends ViewGroup {

    private final String TAG = "TouchViewDemo";
    private int allHeight = 0;
    public TouchViewDemo(Context context, AttributeSet attrs) {
        super(context, attrs);
        float size = TypedValue.applyDimension(TypedValue.TYPE_DIMENSION,10,getResources().getDisplayMetrics());
        Log.i(TAG,"size ===== " + size);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        allHeight = 0;
        for(int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            MarginLayoutParams lp = (MarginLayoutParams) child.getLayoutParams();
            l = lp.leftMargin;
            t = lp.topMargin + allHeight;
            r = l + child.getMeasuredWidth() - lp.rightMargin;
            b = t + child.getMeasuredHeight() - lp.bottomMargin;
            Log.i(TAG,"l = " + l + ",t = " + t + ",r = " + r + ",b = " + b);
            child.layout(l,t,r,b);
            allHeight += child.getMeasuredHeight() + lp.topMargin;
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int height = MeasureSpec.getSize(heightMeasureSpec);
        for(int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            MarginLayoutParams lp = (MarginLayoutParams) child.getLayoutParams();
            Log.i(TAG,"lp ==== " + lp.topMargin);
            int h = MeasureSpec.makeMeasureSpec(height / getChildCount() - lp.topMargin,MeasureSpec.EXACTLY);
            child.measure(widthMeasureSpec,h);
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        Log.i(TAG,"generateLayoutParams");
        return new MarginLayoutParams(getContext(),attrs);
    }

    @Override
    protected LayoutParams generateDefaultLayoutParams() {
        Log.i(TAG,"generateDefaultLayoutParams");
        return super.generateDefaultLayoutParams();
    }

    @Override
    protected LayoutParams generateLayoutParams(LayoutParams p) {
        Log.i(TAG,"generateLayoutParams");
        return new MarginLayoutParams(p);
    }

    @Override
    public void addView(View child, int index, LayoutParams params) {
        Log.i(TAG,"===== addView" + params);
        super.addView(child, index, params);
    }
}

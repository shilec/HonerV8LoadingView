package com.scott.honerv8loadingview.view;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * <p>Author:    shijiale</p>
 * <p>Date:      2018-02-02 15:04</p>
 * <p>Email:     shilec@126.com</p>
 * <p>Describe:</p>
 */

public class CircleLoadingView extends View {

    private Paint mPaint;
    private int mTrackWidth = 22;

    private double mTrackX = 0;
    private double mTrackY = 0;
    private double mTrackR = 200;

    private double mPointX = 0;
    private double mPointY = 0;
    private double mPointR = 15;
    private int mStepDegress = 8;

    private int mCircleColor = Color.parseColor("#330000ff");
    private int mPointColor = Color.parseColor("#cc0000ff");


    public CircleLoadingView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        mTrackX = getMeasuredWidth() / 2;
        mTrackY = getMeasuredHeight() / 2;
        mPointX = mTrackX;
        mPointY = mTrackY - mTrackR;
    }

    @Override
    protected void onDraw(Canvas canvas) {

        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(mCircleColor);
        mPaint.setStrokeWidth(mTrackWidth);

        canvas.drawCircle((int)mTrackX,(int)mTrackY,(int)mTrackR,mPaint);

        mPaint.setStrokeWidth(1);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(mPointColor);

        canvas.drawCircle((int)mPointX,(int)mPointY,(int)mPointR,mPaint);

        postInvalidateDelayed(25);
        updatePoint();
    }

    private void updatePoint() {

        float radiu = (float) (((mStepDegress) * Math.PI) / 180);
        double x = mTrackX + (mPointX - mTrackX) * Math.cos(radiu) - (mPointY - mTrackY) * Math.sin(radiu);
        double y = mTrackY + (mPointX - mTrackX) * Math.sin(radiu) + (mPointY - mTrackY) * Math.cos(radiu);

        mPointX = x;
        mPointY = y;
    }
}

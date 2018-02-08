package com.scott.honerv8loadingview.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

/**
 * <p>Author:    shijiale</p>
 * <p>Date:      2018-02-08 11:17</p>
 * <p>Email:     shilec@126.com</p>
 * <p>Describe:</p>
 */

public class BesasierViewDemo extends View {

    private Paint mPaint;
    private Path mPath;

    private int mWidth;
    private int mHeight;

    private int mControlX;
    private int mControlY;
    private final String TAG = BesasierViewDemo.class.getSimpleName();

    public BesasierViewDemo(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mPaint = new Paint();
        mPaint.setColor(Color.BLUE);
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.STROKE);

        mPath = new Path();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        mWidth = getMeasuredWidth();
        mHeight = getMeasuredHeight();

        mControlX = mWidth / 2;
        mControlY = mHeight / 2;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mPath.reset();


        mPath.moveTo(0,mHeight / 2);
        //mPath.quadTo(0,mHeight/2,mWidth/2,mHeight / 2 - 100);
        mPath.quadTo(mControlX,mControlY,mWidth,mHeight / 2);

        canvas.drawPath(mPath,mPaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        int y = 0;
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mControlX = (int) event.getX();
                y = (int) event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                mControlY = mHeight / 2;
                mControlX = (int) event.getX();
                //mControlY -= (event.getY() - y);
                mControlY = (int) event.getY();

                Log.e(TAG," Y = " + (event.getY() - y));
                Log.e(TAG," controlX = " + mControlX + ", mControY = " + mControlY);
                invalidate();
                break;
        }

        super.onTouchEvent(event);
        return true;
    }
}

package com.scott.honerv8loadingview.view;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

/**
 * <p>Author:    shijiale</p>
 * <p>Date:      2018-02-08 11:51</p>
 * <p>Email:     shilec@126.com</p>
 * <p>Describe:</p>
 */

public class BesasierViewDemo2 extends View implements ValueAnimator.AnimatorUpdateListener {

    private double mCircleX;
    private double mCircleY;

    private double mCircle2X;
    private double mCircle2Y;

    private double mNowCircleR = 50;
    private double mCircle2R = 50;

    private double mScaleRadian = Math.PI / 10;
    private long mDurTime = 1000;
    private float mMaxLength = 200;
    private float mMaxLeaveLength = 30;

    private double mCircleR = 100;

    private double mLength = 0;
    private final String TAG = BesasierViewDemo2.class.getSimpleName();

    private Paint mPaint;
    private Path mPath;
    private ValueAnimator mAnimtor;

    public BesasierViewDemo2(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(Color.BLUE);

        mPath = new Path();
        mAnimtor = new ValueAnimator();
        mAnimtor.setInterpolator(new DecelerateInterpolator());
        mAnimtor.addUpdateListener(this);
        mAnimtor.setDuration(mDurTime);
        mAnimtor.setFloatValues((float) (-2f * mCircleR -2f * mCircle2R) - mMaxLength,mMaxLength);
        mAnimtor.setRepeatMode(ValueAnimator.REVERSE);
        mAnimtor.setRepeatCount(-1);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mAnimtor.start();
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        mCircleX = getMeasuredWidth() / 2;
        mCircleY = getMeasuredHeight() / 2;

        mCircle2X = mCircleX - mNowCircleR - mCircle2R - mLength;
        mCircle2Y = getMeasuredHeight() / 2;

    }

    @Override
    protected void onDraw(Canvas canvas) {
        mCircle2X = mCircleX + mNowCircleR + mCircle2R + mLength;

        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(Color.BLUE);
        canvas.drawCircle((int)mCircleX,(int)mCircleY,(int) mNowCircleR,mPaint);
        canvas.drawCircle((int)mCircle2X,(int)mCircle2Y,(int)mCircle2R,mPaint);
        calculate(canvas);
    }

    private void calculate(Canvas canvas) {
       double x = (2 * mCircle2R * mCircle2R + mCircle2R * mLength) / (mNowCircleR - mCircle2R);
       double x1 = x + 2 * mCircle2R + mNowCircleR;
       //Log.e(TAG,"x1 = " + x1);

       double a1;
       double a2;


       if(mCircle2X > mCircleX) {
           a1 = mScaleRadian;
           a2 = Math.PI - mScaleRadian;
       } else {
           a2 = mScaleRadian;
           a1 = Math.PI - mScaleRadian;
       }



//       double cmX = mCircleX + ((mNowCircleR + mCircleX) - mCircleX) * Math.cos(a1);
//       double cmY = mCircleY + ((mNowCircleR + mCircleX) - mCircleX) * Math.sin(a1);
//
//        Log.e(TAG,"a1 = " + a1 + ",cmx = " + cmX + ",cmy = " + cmY + ",a = " + (180 * a1 / Math.PI));
//        mPaint.setColor(Color.RED);
//        mPaint.setStrokeWidth(5);
//        canvas.drawPoint((int)cmX,(int)cmY,mPaint);
        drawTangentPoint(mCircleX,mCircleY,(mCircleX + mNowCircleR),mCircleY,a1,canvas);
        drawTangentPoint(mCircleX,mCircleY,(mCircleX + mNowCircleR),mCircleY,-a1,canvas);

        drawTangentPoint(mCircle2X,mCircle2Y,(mCircle2X + mCircle2R),mCircle2Y,a2,canvas);
        drawTangentPoint(mCircle2X,mCircle2Y,(mCircle2X + mCircle2R),mCircle2Y,-a2,canvas);
//        double a = 60;
//        Log.e(TAG,"a = " + Math.sin(Math.PI / 2));
//        Log.e(TAG,"a = " + Math.asin(1));

        if((mLength > 0 && mLength < mMaxLeaveLength) ||
                (mLength < 0 && mLength > (-2 * mCircleR - 2 * mCircle2R - mMaxLeaveLength))) {
            drawStick(a1, a2, canvas);
        }
    }

    private void drawStick(double a1,double a2,Canvas canvas) {
        Point p1 = getTangentPoint(mCircleX, mCircleY, (mCircleX + mNowCircleR), mCircleY, a1);
        Point p2 = getTangentPoint(mCircle2X,mCircle2Y,(mCircle2X + mCircle2R),mCircle2Y,-a2);
        //canvas.drawLine(p1.x,p1.y,p2.x,p2.y,mPaint);

        Point p11 = getTangentPoint(mCircleX, mCircleY, (mCircleX + mNowCircleR), mCircleY, -a1);
        Point p22 = getTangentPoint(mCircle2X,mCircle2Y,(mCircle2X + mCircle2R),mCircle2Y,a2);
        //canvas.drawLine(p11.x,p11.y,p22.x,p22.y,mPaint);

        double cx = (p1.x + p2.x) / 2;
        double cy = (p1.y + p2.y) / 2;

        double cx1 = (p11.x + p22.x) / 2;
        double cy1 = (p11.y + p22.y) / 2;

        //Log.e(TAG,"cx = " + cx + ",cy = " + cy);
        mPath.reset();

        mPath.moveTo(p1.x,p1.y);
        mPath.quadTo((int)cx,(int)cy,p22.x,p22.y);
        mPath.lineTo((int)mCircle2X,(int)mCircle2Y);
        mPath.lineTo(p2.x,p2.y);
        mPath.quadTo((int)cx1,(int)cy1,p11.x,p11.y);
        mPath.lineTo((int)mCircleX,(int)mCircleY);
        mPath.lineTo(p1.x,p1.y);

        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(Color.BLUE);
        canvas.drawPath(mPath,mPaint);
    }

    private void drawTangentPoint(double a,double b,double x,
                                  double y,double radian,Canvas canvas) {
        double x1 = a + (x - a) * Math.cos(radian);
        double y1 = b + (x - a) * Math.sin(radian);

        mPaint.setColor(Color.RED);
        mPaint.setStrokeWidth(5);
        canvas.drawPoint((int)x1,(int)y1,mPaint);

        //canvas.drawLine((int)a,(int)b,(int)x1,(int)y1,mPaint);
    }

    private Point getTangentPoint(double a,double b,double x,
                                  double y,double radian) {
        double x1 = a + (x - a) * Math.cos(radian);
        double y1 = b + (x - a) * Math.sin(radian);
        Point point = new Point((int)x1,(int)y1);
        return point;
    }


    float x = 0;
    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                x = event.getX();
                break;
            case MotionEvent.ACTION_MOVE:
                mLength += (event.getX() - x);
                //Log.e(TAG,"x = " + (event.getX() - x) + ",downX = " + x + ",length = " + mLength);
                x = event.getX();
                break;
        }
        if(mLength <= 150) {
            mNowCircleR = mCircleR - mLength / 10;
        }
        //invalidate();
        super.onTouchEvent(event);
        return true;
    }

    @Override
    public void onAnimationUpdate(ValueAnimator animation) {
        mLength = Float.parseFloat(animation.getAnimatedValue().toString());

        if(mCircle2X > (mCircleX + mNowCircleR) || mCircle2R < (mCircleX - mNowCircleR)) {
            if(mLength > 0) {
                mNowCircleR = mCircleR - mLength / 10;
            } else if(mLength < (-2 * mCircleR -2 * mCircle2R)) {
                mNowCircleR = mCircleR + (mLength + 2 * mCircleR + 2 * mCircle2R) / 10;
            }
        } else {
            if(mLength > 0) {
                mNowCircleR = mCircleR + mLength / 10;
            } else if(mLength < (-2 * mCircleR -2 * mCircle2R)) {
                mNowCircleR = mCircleR - (mLength + 2 * mCircleR + 2 * mCircle2R) / 10;
            }
        }
        invalidate();
        Log.e(TAG,"length = " + mLength);
    }
}

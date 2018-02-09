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
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.CycleInterpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.OvershootInterpolator;

import com.scott.honerv8loadingview.utils.ViewMathUtils;

/**
 * <p>Author:    shijiale</p>
 * <p>Date:      2018-02-09 11:18</p>
 * <p>Email:     shilec@126.com</p>
 * <p>Describe:</p>
 */

public class BesasierViewDemo3 extends View implements ValueAnimator.AnimatorUpdateListener {

    private int mRingColor = Color.BLUE;
    private int mMainRingNowR = 80;
    private int mDaemonRingR = 50;
    private float mMainRingR = 80;
    private int mInitOffset = -300;
    private float mNowOffset = mInitOffset;
    private int mLeaveThreshold = 60;
    private int mDegress = 60;
    private int mDegress2 = 60;

    private Paint mPaint;

    private Point mMainRingPoint;
    private Point mDaemonRingPoint;

    private Path mStickyPath = new Path();
    private ValueAnimator mAnim;

    private long mDurTime = 1300;
    private float mNowMainRingRIncrement = 0;
    private boolean isNeedDrawStickyPath = true;

    private float mDegreeFatRate = 10;

    private final String TAG = BesasierViewDemo3.class.getSimpleName();

    public BesasierViewDemo3(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(mRingColor);
        mPaint.setStyle(Paint.Style.FILL);

        mAnim = new ValueAnimator();
        mAnim.setDuration(mDurTime);
        mAnim.setRepeatCount(-1);
        mAnim.setRepeatMode(ValueAnimator.REVERSE);
        mAnim.setFloatValues(mInitOffset,-mInitOffset);
        mAnim.setInterpolator(new LinearInterpolator());
        mAnim.addUpdateListener(this);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        mMainRingPoint = new Point();
        mMainRingPoint.x = 0;
        mMainRingPoint.y = 0;

        mDaemonRingPoint = new Point();
        mDaemonRingPoint.x = mMainRingPoint.x + mInitOffset;
        mDaemonRingPoint.y = mMainRingPoint.y;

        mAnim.start();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.translate(getMeasuredWidth()/2,getMeasuredHeight()/2);
        drawRing(canvas);
        if(isNeedDrawStickyPath) {
            drawStickyPath(canvas);
        }
    }

    private void drawRing(Canvas canvas) {
        canvas.drawCircle(mMainRingPoint.x,mMainRingPoint.y, mMainRingNowR,mPaint);

        mDaemonRingPoint.x = (int) (mMainRingPoint.x + mNowOffset);
        mDaemonRingPoint.y = mMainRingPoint.y;
        canvas.drawCircle(mDaemonRingPoint.x,mDaemonRingPoint.y,mDaemonRingR,mPaint);
        //记录上一帧圆心距
    }

    private void drawStickyPath(Canvas canvas) {
        mStickyPath.reset();

        int mainDegress = 0;
        int daemonDegress = 0;
        if(mDaemonRingPoint.x < mMainRingPoint.x) {
            mainDegress = mDegress;
            daemonDegress = 180 - mDegress2;
        } else {
            mainDegress = 180 - mDegress;
            daemonDegress = mDegress2;
        }

        //大圆上的两个点
        Point pR = ViewMathUtils.getPointTurnDegress(mMainRingPoint.x,mMainRingPoint.y,
                mMainRingPoint.x - mMainRingNowR,mMainRingPoint.y,mainDegress);
        Point pR1 = ViewMathUtils.getPointTurnDegress(mMainRingPoint.x,mMainRingPoint.y,
                mMainRingPoint.x - mMainRingNowR,mMainRingPoint.y,-mainDegress);

        //小圆上的两个点
        Point pr = ViewMathUtils.getPointTurnDegress(mDaemonRingPoint.x,mDaemonRingPoint.y,
                mDaemonRingPoint.x - mDaemonRingR,mDaemonRingPoint.y,daemonDegress);
        Point pr1 = ViewMathUtils.getPointTurnDegress(mDaemonRingPoint.x,mDaemonRingPoint.y,
                mDaemonRingPoint.x - mDaemonRingR,mDaemonRingPoint.y,-daemonDegress);
        //大圆上面的点 和 小圆下面点连线 的中点
        Point midPoint = ViewMathUtils.getMidPoint(mDaemonRingPoint,mMainRingPoint);
        //大圆下面的点 个 小圆上面的点连线 的中点
        Point midPoint1 = ViewMathUtils.getMidPoint(mDaemonRingPoint,mMainRingPoint);

        //移动起始点到大圆圆心
        mStickyPath.moveTo(mMainRingPoint.x,mMainRingPoint.y);
        //圆心到上面点的连线
        mStickyPath.lineTo(pR.x,pR.y);
        //中点到小圆上面点的贝尔塞尔曲线
        mStickyPath.quadTo(midPoint.x,midPoint.y,pr.x,pr.y);
        //小圆上点到小圆心的连线
        mStickyPath.lineTo(mDaemonRingPoint.x,mDaemonRingPoint.y);
        //小圆心到小圆下点的连线
        mStickyPath.lineTo(pr1.x,pr1.y);
        //中点到大圆下点的被赛尔曲线
        mStickyPath.quadTo(midPoint1.x,midPoint1.y,pR1.x,pR1.y);
        //大圆下点到圆心连线
        mStickyPath.lineTo(mMainRingPoint.x,mMainRingPoint.y);

        canvas.drawPath(mStickyPath,mPaint);
    }

    @Override
    public void onAnimationUpdate(ValueAnimator animation) {
        mNowOffset = Float.parseFloat(animation.getAnimatedValue().toString());

        mNowMainRingRIncrement = Math.abs(mInitOffset / mDegreeFatRate) - Math.abs(mNowOffset / mDegreeFatRate);
        isNeedDrawStickyPath = Math.abs(mNowOffset) < (mMainRingNowR + mDaemonRingR + mLeaveThreshold);
        if(isNeedDrawStickyPath) {
            mMainRingNowR = (int) (mMainRingR + mNowMainRingRIncrement);
            mDegress = (int) (90 - Math.abs(mNowOffset / mDegreeFatRate));
        }
        Log.e(TAG,"mDegress = " + mDegress);
        invalidate();
    }
}

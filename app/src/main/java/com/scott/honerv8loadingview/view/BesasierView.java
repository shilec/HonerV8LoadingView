package com.scott.honerv8loadingview.view;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.animation.OvershootInterpolator;

/**
 * <p>Author:    shijiale</p>
 * <p>Date:      2017/5/12.</p>
 * <p>Email:     shijl5@lenovo.com</p>
 * <p>Describe:</p>
 */

public class BesasierView  extends SurfaceView implements SurfaceHolder.Callback{
    private Paint mPaint;
    private Path mPath;
    private float range = 500;
    private SurfaceHolder mHolder;
    public BesasierView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mPaint = new Paint();
        mPaint.setStrokeWidth(3);
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(Color.BLUE);
        mPath = new Path();
        mHolder = getHolder();
        mHolder.addCallback(this);
    }

    protected void onDraw(Canvas canvas) {

        float x = getWidth() / 2;
        float y = getHeight() / 2;

        mPath.reset();
        mPath.moveTo(0,y);
        mPath.rQuadTo(x,range,getWidth(),0);
        canvas.drawPath(mPath,mPaint);

    }

    @Override
    public void surfaceCreated(final SurfaceHolder holder) {
        final ValueAnimator anim = new ValueAnimator();
        anim.setDuration(1300);
        anim.setRepeatCount(-1);
        anim.setFloatValues(range,-range);
        anim.setRepeatMode(ValueAnimator.REVERSE);
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                range = Float.parseFloat(animation.getAnimatedValue().toString());
                Canvas canvas = holder.lockCanvas();
                canvas.drawColor(Color.WHITE);
                canvas.drawCircle(getWidth() / 2, getHeight() / 2 + range, 200, mPaint);
                holder.unlockCanvasAndPost(canvas);
            }
        });
        anim.addListener(new Animator.AnimatorListener() {
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
        });
        anim.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }
}

package com.scott.honerv8loadingview.view;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateInterpolator;

import com.scott.honerv8loadingview.R;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>Author:    shijiale</p>
 * <p>Date:      2017/5/9.</p>
 * <p>Email:     shijl5@lenovo.com</p>
 * <p>Describe:</p>
 */

public class HonerLoadingView extends View{

    private final int DEFAULT_RADIUS = 10;  //默认点的半径
    private final int MAX_RADIUS = 20;  //点的最大半径
    private final int MAX_TRACK_RADIUS = 200; //最大轨迹半径
    private final int DEFAULT_TRACK_RADIUS = 60; //默认轨迹半径
    private final int DEFAULT_COLOR = Color.BLUE; //默认点的颜色
    private final int DEFAULT_POINT_COUNT = 10; //默认点的个数
    private final int MAX_POINT_COUNT = 20; //最大点的个数
    private final float INTERUPTER_FACTOR = 1.3f; //插值参数
    private final int DEFAULT_DUR_TIME = 3200; //默认点绕一圈的时间
    private final int DEFAULT_PEER_DELAY = 290; //默认每个点比前一个延迟多久出现(ms)
    private final int DEFAULT_STEP_RANGE = 30; //默认每次轨迹上的转过的角度
    private final float DEFAULT_START = 0;
    private final float DEFAULT_END = 360 + DEFAULT_STEP_RANGE;
    private final float DEFAULT_SCALE_RATE = 0.7f; //每个点缩放的比例(为0时不缩,远大每个点直接的大小差远大)
    private final float MAX_SCALE_RATE = 5f;
    private Paint mPaint;
    private double x;
    private double y;
    private double x1;
    private double y1;

    private int mColor = DEFAULT_COLOR;
    private float mRadius = DEFAULT_RADIUS;
    private float mTrackRadius = DEFAULT_TRACK_RADIUS;
    private int mPointCount = DEFAULT_POINT_COUNT;
    private int mDurTime = DEFAULT_DUR_TIME;
    private float mNowAngle = 0;
    private float mStart_angle = DEFAULT_START;
    private float mEnd_angle = DEFAULT_END;
    private int mAngleStep = DEFAULT_STEP_RANGE;
    private float mRange = DEFAULT_END;
    private float mScaleRate = DEFAULT_SCALE_RATE;
    private int mPeerDelay = DEFAULT_PEER_DELAY;

    private List<ValueAnimator> mAnims = new ArrayList<>();
    private List<CPoint> mPoints = new ArrayList<>();

    public HonerLoadingView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.FILL);

        initParams(context,attrs);
        initAnimator();
        mPaint.setColor(mColor);
    }


    private void initParams(Context context, AttributeSet attrs) {
        TypedArray arry = context.obtainStyledAttributes(attrs,R.styleable.HonerLoadingView);
        mColor = arry.getColor(R.styleable.HonerLoadingView_loading_color,DEFAULT_COLOR);
        mRadius = arry.getDimension(R.styleable.HonerLoadingView_loading_point_radius,DEFAULT_RADIUS);
        mTrackRadius = arry.getDimension(R.styleable.HonerLoadingView_loading_track_radius,DEFAULT_TRACK_RADIUS);
        mPointCount = arry.getInteger(R.styleable.HonerLoadingView_loading_point_count,DEFAULT_POINT_COUNT);
        mDurTime = arry.getInteger(R.styleable.HonerLoadingView_loading_dur,DEFAULT_DUR_TIME);
        mAngleStep = arry.getInteger(R.styleable.HonerLoadingView_loading_angle_step,DEFAULT_STEP_RANGE);
        mStart_angle = arry.getFloat(R.styleable.HonerLoadingView_loading_start_angle,DEFAULT_START);
        mScaleRate = arry.getFloat(R.styleable.HonerLoadingView_loading_scale_rate,DEFAULT_SCALE_RATE);
        mPeerDelay = arry.getInteger(R.styleable.HonerLoadingView_loading_peer_delay,DEFAULT_PEER_DELAY);
        if(mPeerDelay < 0) {
            mPeerDelay = DEFAULT_PEER_DELAY;
        }
        if(mScaleRate > MAX_SCALE_RATE) {
            mScaleRate = MAX_SCALE_RATE;
        }
        if(mPointCount > MAX_POINT_COUNT || mPointCount < 0) {
            mPointCount = DEFAULT_POINT_COUNT;
        }
        if(mTrackRadius > MAX_TRACK_RADIUS || mTrackRadius < 0) {
            mTrackRadius = MAX_TRACK_RADIUS;
        }
        if(mAngleStep > 360 || mAngleStep < DEFAULT_STEP_RANGE) {
            mAngleStep = DEFAULT_STEP_RANGE;
        }
        if(mPointCount * mPeerDelay > mDurTime || mDurTime < 0) {
            mDurTime = mPointCount * mPeerDelay;
        }
        if(mStart_angle > 360) {
            mStart_angle = mStart_angle % 360;
        }
        if(mRadius > MAX_RADIUS) {
            mRadius = MAX_RADIUS;
        }
        mEnd_angle = mStart_angle + mAngleStep + 360;
        mRange = 360 + mAngleStep;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        x = getWidth() / 2;
        y = getHeight() / 2;

        x1 = x;
        y1 = y - mTrackRadius;
        initPoints();
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        for(int i = 0; i < mPoints.size(); i++) {
            canvas.drawCircle(mPoints.get(i).x, mPoints.get(i).y, (float) (mRadius - (mPoints.get(i).index * mScaleRate)), mPaint);
        }
        if(isRunning()) return;
        mPoints.clear();
        updateRange();
        startAnim();
    }

    /***
     * 刷新轨道角度
     */
    private void updateRange() {
        mStart_angle = mEnd_angle;
        if(mStart_angle == ((360 / mAngleStep) * mRange)) {
            mStart_angle = 0;
        }
        mEnd_angle = mStart_angle + DEFAULT_END;
        for(ValueAnimator va : mAnims) {
            va.setFloatValues(mStart_angle,mEnd_angle);
        }
    }

    /***
     * 初始化animator
     */
    private void initAnimator() {
        for(int i = 0; i < mPointCount; i++) {
            final int index = i;
            ValueAnimator anim = ValueAnimator.ofFloat(mStart_angle,mEnd_angle);
            anim.setDuration(mDurTime - (i * mPeerDelay));
            anim.setInterpolator(new AccelerateInterpolator(INTERUPTER_FACTOR));
            anim.setStartDelay((long) (i * mPeerDelay));
            anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    CPoint cp = getPoint(index,animation);
                    int id = mPoints.indexOf(cp);
                    if(id != -1) {
                        mPoints.remove(id);
                    }
                    mPoints.add(cp);
                    postInvalidate();
                }
            });
            mAnims.add(anim);
        }
    }

    private void initPoints() {
        for(int i = 0; i < mPointCount; i++) {
            mPoints.add(new CPoint((int)x1,(int)y1,i));
        }
    }

    /***
     * 根据已知圆心和圆周一点,求转过&角的点的位置
     * @param index
     * @param animation
     * @return
     */
    private CPoint getPoint(int index,ValueAnimator animation) {
        mNowAngle = Float.parseFloat(animation.getAnimatedValue().toString());
        float radiu = (float) (((mNowAngle) * Math.PI) / 180);
        double x2 = x + (x1 - x) * Math.cos(radiu) - (y1 - y) * Math.sin(radiu);
        double y2 = y + (x1 - x) * Math.sin(radiu) + (y1 - y) * Math.cos(radiu);
        return new CPoint((int)x2,(int)y2,index);
    }

    private void startAnim() {
        for(ValueAnimator anim : mAnims) {
            anim.start();
        }
    }

    private boolean isRunning() {
        for(ValueAnimator va : mAnims) {
            if(va.isRunning()) {
                return true;
            }
        }
        return false;
    }
    class CPoint {

        public CPoint(int x,int y,int index) {
            this.index = index;
            this.x = x;
            this.y = y;
        }
        int index;
        int x;
        int y;

        @Override
        public boolean equals(Object obj) {
            CPoint cp = (CPoint) obj;
            if(cp.index == index) {
                return true;
            } else {
                return false;
            }
        }
    }
}


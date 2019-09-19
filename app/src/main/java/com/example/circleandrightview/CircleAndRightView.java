package com.example.circleandrightview;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

public class CircleAndRightView extends View {
    private Paint mCirclePaint;
    private Paint mRightMarkPaint;

    private float mCirclePathLength;
    private PathMeasure mCirclePathMeasure;
    private Path mCircleDst;


    private PathMeasure mLeftLinePathMeasure;
    private float mLeftLinePathLength;
    private Path mLeftLineDst;


    private PathMeasure mRightLinePathMeasure;
    private float mRightLinePathLength;
    private Path mRightLineDst;


    private ValueAnimator mCircleAnimator;
    private float mCircleAnimatorValue;


    private ValueAnimator mRightMarkAnimator;
    private float mRightMarkAnimatorValue;

    public CircleAndRightView(Context context) {
        this(context, null);
    }

    public CircleAndRightView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        initCirclePaint();
        initRightMarkPaint();
        initCircleAnimator();
        initRightMarkAnimator();
        initCircle();
        initLeftLine();
        initRightLine();
    }


    private void initCirclePaint() {
        mCirclePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mCirclePaint.setStyle(Paint.Style.STROKE);
        mCirclePaint.setStrokeJoin(Paint.Join.MITER);
        mCirclePaint.setColor(Color.parseColor("#ffffff"));
        mCirclePaint.setStrokeWidth(10);
    }


    private void initRightMarkPaint() {
        mRightMarkPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mRightMarkPaint.setStyle(Paint.Style.STROKE);
        // 一定要设置,否则对勾无法无缝连接, 如果对勾连接处要圆角,则可以用Paint.Cap.ROUND
        mRightMarkPaint.setStrokeCap(Paint.Cap.ROUND);
        mRightMarkPaint.setColor(Color.parseColor("#ffffff"));
        mRightMarkPaint.setStrokeWidth(10);
    }

    private void initCircleAnimator() {
        mCircleAnimator = ValueAnimator.ofFloat(0, 1);
        mCircleAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                mCircleAnimatorValue = (float) valueAnimator.getAnimatedValue();
                invalidate();
            }
        });
        mCircleAnimator.setDuration(600);
    }


    private void initRightMarkAnimator() {
        mRightMarkAnimator = ValueAnimator.ofFloat(0, 1, 0.55f, 1.1f, 0.55f, 1);
        mRightMarkAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                mRightMarkAnimatorValue = (float) valueAnimator.getAnimatedValue();
                invalidate();
            }
        });
        mRightMarkAnimator.setDuration(600);
        mRightMarkAnimator.setInterpolator(new LinearInterpolator());
    }


    private void initCircle() {
        Path path = new Path();
        path.addArc(new RectF(5, 5, 135, 135), 270, 360);
        mCirclePathMeasure = new PathMeasure(path, true);
        mCirclePathLength = mCirclePathMeasure.getLength();
        mCircleDst = new Path();
    }


    private void initLeftLine() {

        Path path = new Path();
        // 对号起点
        float startX = (float) (0.43 * 140);
        float startY = (float) (0.66 * 140);
        path.moveTo(startX, startY);


        // 对号终点
        float endX = (float) (0.3 * 140);
        float endY = (float) (0.5 * 140);
        path.lineTo(endX, endY);

        mLeftLinePathMeasure = new PathMeasure(path, false);
        mLeftLinePathLength = mLeftLinePathMeasure.getLength();

        mLeftLineDst = new Path();


    }


    private void initRightLine() {
        Path path = new Path();
        // 对号起点
        float startX = (float) (0.43 * 140);
        float startY = (float) (0.66 * 140);
        path.moveTo(startX, startY);


        // 对号终点
        float endX = (float) (0.75 * 140);
        float endY = (float) (0.4 * 140);
        path.lineTo(endX, endY);

        mRightLinePathMeasure = new PathMeasure(path, false);
        mRightLinePathLength = mRightLinePathMeasure.getLength();
        mRightLineDst = new Path();
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        mCircleDst.reset();
        mLeftLineDst.reset();
        mRightLineDst.reset();

        mRightMarkPaint.setStrokeWidth(10 * mRightMarkAnimatorValue);


        float circleStop = mCirclePathLength * mCircleAnimatorValue;
        mCirclePathMeasure.getSegment(0, circleStop, mCircleDst, true);
        canvas.drawPath(mCircleDst, mCirclePaint);


        float leftLineStop = mLeftLinePathLength * mRightMarkAnimatorValue;
        mLeftLinePathMeasure.getSegment(0, leftLineStop, mLeftLineDst, true);
        canvas.drawPath(mLeftLineDst, mRightMarkPaint);

        float rightLineStop = mRightLinePathLength * mRightMarkAnimatorValue;
        mRightLinePathMeasure.getSegment(0, rightLineStop, mRightLineDst, true);
        canvas.drawPath(mRightLineDst, mRightMarkPaint);
    }

    public void startDraw() {
        mCircleAnimator.start();
        mRightMarkAnimator.start();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (mCircleAnimator != null && mCircleAnimator.isRunning()) {
            mCircleAnimator.cancel();
        }

        if (mRightMarkAnimator != null && mRightMarkAnimator.isRunning()) {
            mCircleAnimator.cancel();
        }
    }
}

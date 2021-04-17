package com.jeet.customwidgets.circularprogressbar;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

import androidx.annotation.Nullable;

public class CircularProgressBar extends View {
    Paint strokePaint = new Paint();
    Paint fillPaint = new Paint();
    ValueAnimator progressAnimator;
    private int MAX_PROGRESS_VALUE = 100;
    private int currentProgress = 10;
    private int MIN_PROGRESS_VALUE = 0;
    private float totalAnglePerProgress = 360f / MAX_PROGRESS_VALUE;

    private RectF rectToDraw = new RectF(0, 0, 100, 100);
    ;
    private Path arcPath;

    public int getMAX_PROGRESS_VALUE() {
        return MAX_PROGRESS_VALUE;
    }

    public void setMAX_PROGRESS_VALUE(int MAX_PROGRESS_VALUE) {
        this.MAX_PROGRESS_VALUE = MAX_PROGRESS_VALUE;
        totalAnglePerProgress = 360f / MAX_PROGRESS_VALUE;
        makeProgressArc();
    }

    public int getCurrentProgress() {
        return currentProgress;
    }

    public void setProgress(int progress) {
        if (progress > MAX_PROGRESS_VALUE) {
            progress = MAX_PROGRESS_VALUE;
        }
        if (progressAnimator != null) {
            progressAnimator.cancel();
        }
        progressAnimator = ValueAnimator.ofInt(currentProgress, progress);
        progressAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                setCurrentProgress((Integer) valueAnimator.getAnimatedValue());
            }
        });
        progressAnimator.setInterpolator(new LinearInterpolator());
        progressAnimator.setDuration(500);
        progressAnimator.start();
    }

    private void setCurrentProgress(int currentProgress) {
        this.currentProgress = currentProgress;
        makeProgressArc();
    }

    public int getMIN_PROGRESS_VALUE() {
        return MIN_PROGRESS_VALUE;
    }

    public void setMIN_PROGRESS_VALUE(int MIN_PROGRESS_VALUE) {
        this.MIN_PROGRESS_VALUE = MIN_PROGRESS_VALUE;
        makeProgressArc();
    }


    public CircularProgressBar(Context context) {
        super(context);
        initializeView();
    }

    public CircularProgressBar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initializeView();
    }

    public CircularProgressBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initializeView();
    }

    public CircularProgressBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initializeView();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        rectToDraw = new RectF(0, 0, w, h);
        makeProgressArc();
    }

    private void makeProgressArc() {
        float centreX = getWidth() / 2f;
        float centreY = getHeight() / 2f;
        arcPath = new Path();
        float angleOfArc = currentProgress * totalAnglePerProgress;
        arcPath.addArc(rectToDraw, 270, angleOfArc);
        invalidate();

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawPath(arcPath, strokePaint);
        Path linePath = new Path();
        linePath.moveTo(getWidth() / 2f, getHeight() / 2f);
        linePath.lineTo(getWidth() / 2f, 0);
//        canvas.drawLine(getWidth() / 2f, getHeight() / 2f, getWidth() / 2f, 0, fillPaint);
        float angleOfArc = currentProgress * totalAnglePerProgress;
        angleOfArc = angleOfArc - 90;
        float angleOfArcRadian = (float) (angleOfArc * (Math.PI / 180f)); //to radian
        double nextX = getWidth() / 2f + getWidth() / 2f * (Math.cos(angleOfArcRadian));
        double nextY = getHeight() / 2f + getHeight() / 2f * (Math.sin(angleOfArcRadian));

        linePath.moveTo(getWidth() / 2f, getHeight() / 2f);
        linePath.lineTo((float) nextX, (float) nextY);
        linePath.lineTo(getWidth() / 2f, 0);
        linePath.close();
        if (MAX_PROGRESS_VALUE / currentProgress >= 2) {
            canvas.drawPath(linePath, strokePaint);
        } else {
            canvas.drawPath(linePath, fillPaint);
        }
//        canvas.drawLine(getWidth() / 2f, getHeight() / 2f, (float) nextX, (float) nextY, fillPaint);
    }

    private void initializeView() {
        strokePaint.setColor(Color.GREEN);
        strokePaint.setStyle(Paint.Style.FILL);
        strokePaint.setStrokeWidth(10);
        fillPaint.setColor(Color.WHITE);
        fillPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        fillPaint.setStrokeWidth(0.5f);
//        strokePaint.setStrokeWidth(10);
    }

}

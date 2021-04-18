package com.jeet.circularprogressbar;

import android.animation.TimeInterpolator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.BounceInterpolator;
import android.view.animation.LinearInterpolator;

import androidx.annotation.Nullable;

public class CircularProgressBar extends View {
    private float mProgressTextSize = 20 * getResources().getDisplayMetrics().density;
    private int mProgressBarColor = getContext().getResources().getColor(R.color.design_default_color_primary);
    private int mProgressTextColor = getContext().getResources().getColor(R.color.design_default_color_secondary);
    private boolean mShouldAnimateProgress = true;
    private boolean mShouldShowProgressText = true;
    Paint strokePaint = new Paint();
    //    Paint fillPaint = new Paint();
    ValueAnimator progressAnimator;
    TimeInterpolator mSelectedInterpolator = new AccelerateDecelerateInterpolator();
    private int MAX_PROGRESS_VALUE = 100;
    private int currentProgress = 10;
    private int MIN_PROGRESS_VALUE = 0;
    private float totalAnglePerProgress = 360f / MAX_PROGRESS_VALUE;

    private RectF rectToDraw = new RectF(0, 0, 100, 100);

    private Path arcPath;
    private Paint textPaint = new Paint();
    private Path arcPathSecond;
    private RectF rectToDrawSecond = new RectF(0, 0, 100, 100);
    ;
    private Path linePath;
    private Path linePathSecond;

    public boolean shouldAnimateProgress() {
        return mShouldAnimateProgress;
    }

    public void setShouldAnimateProgress(boolean shouldAnimateProgress) {
        this.mShouldAnimateProgress = shouldAnimateProgress;
    }

    public boolean shouldShowProgressText() {
        return mShouldShowProgressText;
    }

    public void setShouldShowProgressText(boolean shouldShowProgressText) {
        this.mShouldShowProgressText = shouldShowProgressText;
    }

    public TimeInterpolator getSelectedInterpolator() {
        return mSelectedInterpolator;
    }

    public void setSelectedInterpolator(TimeInterpolator selectedInterpolator) {
        this.mSelectedInterpolator = selectedInterpolator;
    }

    public long getMaxAnimationTime() {
        return MAX_ANIMATION_TIME_MS;
    }

    public void setMaxAnimationTime(long millis) {
        this.MAX_ANIMATION_TIME_MS = millis;
    }

    private long MAX_ANIMATION_TIME_MS = 500;

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
        if (!mShouldAnimateProgress) {
            setCurrentProgress(progress);
            return;
        }
        if (progressAnimator != null) {
            progressAnimator.cancel();// cancel running value animation
            progressAnimator.removeAllUpdateListeners();
        }
        progressAnimator = ValueAnimator.ofInt(currentProgress, progress);
        progressAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                setCurrentProgress((Integer) valueAnimator.getAnimatedValue());
            }
        });
        progressAnimator.setInterpolator(mSelectedInterpolator);

        float progressDuration = currentProgress > progress ? MAX_ANIMATION_TIME_MS - MAX_ANIMATION_TIME_MS * ((float) progress / currentProgress) : MAX_ANIMATION_TIME_MS - MAX_ANIMATION_TIME_MS * ((float) currentProgress / progress);
        progressAnimator.setDuration((long) progressDuration);
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
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs,
                R.styleable.CircularProgressBar,
                0,
                0);
        try {
            MAX_PROGRESS_VALUE = a.getInt(R.styleable.CircularProgressBar_maxProgressLimit, 100);
            setMAX_PROGRESS_VALUE(MAX_PROGRESS_VALUE);
            MIN_PROGRESS_VALUE = a.getInt(R.styleable.CircularProgressBar_minProgressLimit, 0);
            setMIN_PROGRESS_VALUE(MIN_PROGRESS_VALUE);
            currentProgress = a.getInt(R.styleable.CircularProgressBar_progress, 10);
            setProgress(currentProgress);
            mShouldAnimateProgress = a.getBoolean(R.styleable.CircularProgressBar_animateProgress, true);
            MAX_ANIMATION_TIME_MS = a.getInteger(R.styleable.CircularProgressBar_maxAnimationDuration, 500);
            int selectedInterpolator = a.getInteger(R.styleable.CircularProgressBar_animationInterpolator, 1);
            selectInterpolator(selectedInterpolator);
            mShouldShowProgressText = a.getBoolean(R.styleable.CircularProgressBar_showProgressText, true);
            mProgressBarColor = a.getColor(R.styleable.CircularProgressBar_progressBarColor, getContext().getResources().getColor(R.color.design_default_color_primary));
            mProgressTextColor = a.getColor(R.styleable.CircularProgressBar_progressTextColor, getContext().getResources().getColor(R.color.design_default_color_secondary));
            mProgressTextSize = a.getDimension(R.styleable.CircularProgressBar_progressTextSize, 20);
        } catch (Exception ex) {
            ex.printStackTrace();

        } finally {
            a.recycle();
            initializeView();
//            invalidate();
        }

    }

    private void selectInterpolator(int selectedInterpolator) {
        switch (selectedInterpolator) {
            case 0:
                mSelectedInterpolator = new LinearInterpolator();
                break;
            case 1:
                mSelectedInterpolator = new AccelerateDecelerateInterpolator();
                break;
            case 2:
                mSelectedInterpolator = new BounceInterpolator();
                break;
            default:
                mSelectedInterpolator = new AccelerateDecelerateInterpolator();
                break;
        }
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        rectToDraw = new RectF(0, 0, w, h);
        rectToDrawSecond = new RectF(0, 0, w, h);
        makeProgressArc();
    }

    private void makeProgressArc() {
        float centreX = getWidth() / 2f;
        float centreY = getHeight() / 2f;
        arcPath = new Path();
        int progressLeftToDraw = currentProgress > MAX_PROGRESS_VALUE / 2 ? MAX_PROGRESS_VALUE / 2 : currentProgress;
        float angleOfArc = progressLeftToDraw * totalAnglePerProgress;
        angleOfArc = angleOfArc > 180 ? 180 : angleOfArc;
        arcPath.addArc(rectToDraw, 270, angleOfArc);

        makeLineClosingPath();
        makeProgressArc2();
        invalidate();

    }

    private void makeProgressArc2() {
        arcPathSecond = new Path();
        int progressLeftToBeDrawn = currentProgress - MAX_PROGRESS_VALUE / 2;
        float angleOfArc = progressLeftToBeDrawn * totalAnglePerProgress; // divide current progress by half
        angleOfArc = angleOfArc > 180 ? 180 : angleOfArc;
        arcPathSecond.addArc(rectToDrawSecond, 90, angleOfArc);
        makeLineClosingPathSecond();
    }

    private void makeLineClosingPath() {
        linePath = new Path();
        linePath.moveTo(getWidth() / 2f, getHeight() / 2f);
        linePath.lineTo(getWidth() / 2f, 0);
//        canvas.drawLine(getWidth() / 2f, getHeight() / 2f, getWidth() / 2f, 0, fillPaint);
        float angleOfArc = currentProgress * totalAnglePerProgress;
        angleOfArc = angleOfArc > 180 ? 180 : angleOfArc;
        angleOfArc = angleOfArc - 90;
        float angleOfArcRadian = (float) (angleOfArc * (Math.PI / 180f)); //to radian
        double nextX = getWidth() / 2f + getWidth() / 2f * (Math.cos(angleOfArcRadian));
        double nextY = getHeight() / 2f + getHeight() / 2f * (Math.sin(angleOfArcRadian));

        linePath.moveTo(getWidth() / 2f, getHeight() / 2f);
        linePath.lineTo((float) nextX, (float) nextY);
        linePath.lineTo(getWidth() / 2f, 0);
        linePath.close();
    }

    private void makeLineClosingPathSecond() {
        linePathSecond = new Path();
        linePathSecond.moveTo(getWidth() / 2f, getHeight() / 2f);
        linePathSecond.lineTo(getWidth() / 2f, getHeight());
//        canvas.drawLine(getWidth() / 2f, getHeight() / 2f, getWidth() / 2f, 0, fillPaint);
        int progressLeftToBeDrawn = currentProgress - MAX_PROGRESS_VALUE / 2;
        float angleOfArc = progressLeftToBeDrawn * totalAnglePerProgress; // divide current progress by half
        angleOfArc = angleOfArc > 180 ? 180 : angleOfArc;
        angleOfArc = angleOfArc + 90;
        float angleOfArcRadian = (float) (angleOfArc * (Math.PI / 180f)); //to radian
        double nextX = getWidth() / 2f + getWidth() / 2f * (Math.cos(angleOfArcRadian));
        double nextY = getHeight() / 2f + getHeight() / 2f * (Math.sin(angleOfArcRadian));

        linePathSecond.moveTo(getWidth() / 2f, getHeight() / 2f);
        linePathSecond.lineTo((float) nextX, (float) nextY);
        linePathSecond.lineTo(getWidth() / 2f, getHeight());
        linePathSecond.close();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawPath(arcPath, strokePaint);

        if (MAX_PROGRESS_VALUE / currentProgress >= 2) {

            canvas.drawPath(linePath, strokePaint);
//            canvas.drawPath(linePathSecond,textPaint);
        } else {
            canvas.drawPath(arcPathSecond, strokePaint);
//            canvas.drawPath(linePath, fillPaint);
            canvas.drawPath(linePathSecond, strokePaint);
        }
        if (mShouldShowProgressText)
            canvas.drawText(currentProgress + "", getWidth() / 2f, getHeight() / 2f + textPaint.getTextSize() / 2f, textPaint);
//        canvas.drawLine(getWidth() / 2f, getHeight() / 2f, (float) nextX, (float) nextY, fillPaint);
    }

    private void initializeView() {
        strokePaint.setColor(mProgressBarColor);
        strokePaint.setStyle(Paint.Style.FILL);
        strokePaint.setStrokeWidth(0f);
//        strokePaint.setAntiAlias(true);
//        fillPaint.setColor(mBackGroundProgressColor);
//        fillPaint.setStyle(Paint.Style.FILL);
//        fillPaint.setStrokeWidth(0f);
//        fillPaint.setAntiAlias(true);
        textPaint.setColor(mProgressTextColor);
        textPaint.setTextSize(mProgressTextSize);
//        textPaint.setTextSize(mProgressTextSize);
        textPaint.setStyle(Paint.Style.FILL);
        textPaint.setAntiAlias(true);
        textPaint.setTextAlign(Paint.Align.CENTER);
//        strokePaint.setStrokeWidth(10);
    }

}

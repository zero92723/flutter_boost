package com.idlefish.flutterboost.example.earn;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import com.idlefish.flutterboost.example.R;

/**
 * 带进度的进度条，线程安全的View，可直接在线程中更新进度
 */
public class EarnCoinCircleProgress extends View {
    /**
     * 画笔对象的引用
     */
    private Paint layerPaint;

    private Paint circleBgPaint;

    private Paint circleProgressPaint;

    /**
     * 圆环的颜色
     */
    private int layerCircleColor;

    /**
     * 圆环进度的颜色
     */
    private int progressColor;

    /**
     * 中间进度百分比的字符串的颜色
     */
    private int textColor;

    /**
     * 中间进度百分比的字符串的字体
     */
    private float textSize;

    /**
     * 圆环的宽度
     */
    private float roundWidth;

    /**
     * 最大进度
     */
    private int max;

    /**
     * 当前进度
     */
    private int progress;

    /**
     * 进度圆环的起始角度
     */
    private int startAngle = -90;

    /**
     * 进度圆环的扫描角度
     */
    private int sweepAngle = 360;

    public EarnCoinCircleProgress(Context context) {
        this(context, null);
        init(context);
    }

    public EarnCoinCircleProgress(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        init(context);
    }

    public EarnCoinCircleProgress(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    /**
     * 初始化
     *
     * @param context
     */
    private void init(Context context) {
        layerPaint = new Paint();
        circleBgPaint = new Paint();
        circleProgressPaint = new Paint();

        // 设置基础参数
        setCricleColor(context.getResources().getColor(R.color.earn_coin_progress_bg_Color));
        setCricleProgressColor(context.getResources().getColor(R.color.earn_coin_round_color));
        setMax(100);
        setRoundWidth(7);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        /**
         * 画底部的大圆
         */
        int centre = getWidth() / 2; // 获取圆心的x坐标
        int radius = (int)(centre - roundWidth / 2); // 圆环的半径
        layerPaint.setStrokeCap(Paint.Cap.ROUND);
        layerPaint.setColor(Color.TRANSPARENT); // 设置圆环的颜色
        layerPaint.setStyle(Paint.Style.FILL); // 设置空心
        layerPaint.setStrokeWidth(roundWidth); // 设置圆环的宽度
        layerPaint.setAntiAlias(true); // 消除锯齿
        canvas.drawCircle(centre, centre, radius, layerPaint); // 画出圆环

        /**
         * 画圆弧的轨道
         */
        @SuppressLint("DrawAllocation")
        RectF oval = new RectF(centre - radius, centre - radius, centre
            + radius - 2, centre + radius); // 用于定义的圆弧的形状和大小的界限
        circleBgPaint.setStyle(Paint.Style.STROKE);
        circleBgPaint.setAntiAlias(true); // 消除锯齿
        circleBgPaint.setStrokeWidth(roundWidth); // 设置圆环的宽度
        circleBgPaint.setColor(layerCircleColor); // 设置进度的颜色
        canvas.drawArc(oval, startAngle, sweepAngle, false, circleBgPaint); // 根据进度画圆弧

        /**
         * 画画圆环的进度
         */
        @SuppressLint("DrawAllocation")
        RectF ovalProgress = new RectF(centre - radius, centre - radius, centre
            + radius, centre + radius); // 用于定义的圆弧的形状和大小的界限
        circleProgressPaint.setStrokeWidth(roundWidth); // 设置圆环的宽度
        circleProgressPaint.setColor(progressColor); // 设置进度的颜色
        circleProgressPaint.setStyle(Paint.Style.STROKE);
        circleProgressPaint.setStrokeCap(Paint.Cap.ROUND);
        circleProgressPaint.setAntiAlias(true); // 消除锯齿
        canvas.drawArc(ovalProgress, startAngle, sweepAngle * progress / max, false, circleProgressPaint); // 根据进度画圆弧
    }

    /**
     * 设置进度，此为线程安全控件，由于考虑多线的问题，需要同步刷新界面调用postInvalidate()能在非UI线程刷新
     *
     * @param progress
     */
    public synchronized void setProgress(int progress) {
        if (progress < 0) {
            Log.e("EarnCoinView", "progress not less than 0");
        }

        if (progress > max) {
            progress = max;
        }

        this.progress = progress;
        postInvalidate();
    }

    public synchronized int getMax() {
        return max;
    }

    /**
     * 设置进度的最大值
     *
     * @param max
     */
    public synchronized void setMax(int max) {
        if (max < 0) {
            throw new IllegalArgumentException("max not less than 0");
        }

        this.max = max;
    }

    /**
     * 获取进度.需要同步
     *
     * @return
     */
    public synchronized int getProgress() {
        return progress;
    }

    public int getCricleColor() {
        return layerCircleColor;
    }

    public void setCricleColor(int cricleColor) {
        this.layerCircleColor = cricleColor;
    }

    public int getCricleProgressColor() {
        return progressColor;
    }

    public void setCricleProgressColor(int cricleProgressColor) {
        this.progressColor = cricleProgressColor;
    }

    public int getTextColor() {
        return textColor;
    }

    public void setTextColor(int textColor) {
        this.textColor = textColor;
    }

    public float getTextSize() {
        return textSize;
    }

    public void setTextSize(float textSize) {
        this.textSize = textSize;
    }

    public float getRoundWidth() {
        return roundWidth;
    }

    public void setRoundWidth(float roundWidth) {
        this.roundWidth = roundWidth;
    }
}
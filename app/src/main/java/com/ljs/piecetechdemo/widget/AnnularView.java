package com.ljs.piecetechdemo.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import com.ljs.piecetechdemo.R;

/**
 * Created by ljs on 2018/3/29.
 * Desc: 环形view
 */

public class AnnularView extends View {

    /**
     * 第一颜色、第二颜色、环形宽度、渐进速度
     */
    private int firstColor, secondColor, anWidth, speed;
    /**
     * 画笔
     */
    private Paint mPaint;
    /**
     * 当前进度
     */
    private int progress;
    /**
     * 是否应该开始下一个
     */
    private boolean isNext = false;

    public AnnularView(Context context) {
        this(context, null);
    }

    public AnnularView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AnnularView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = context.getTheme().obtainStyledAttributes(attrs, R.styleable.AnnularView, defStyleAttr, 0);
        int indexCount = typedArray.getIndexCount();
        for (int i = 0; i < indexCount; i++) {
            switch (typedArray.getIndex(i)) {
                case R.styleable.AnnularView_firstColor:
                    firstColor = typedArray.getColor(i, Color.BLUE);
                    break;
                case R.styleable.AnnularView_secondColor:
                    secondColor = typedArray.getColor(i, Color.GREEN);
                    break;
                case R.styleable.AnnularView_anWidth:
                    anWidth = (int) typedArray.getDimension(i, TypedValue.applyDimension(TypedValue
                            .COMPLEX_UNIT_DIP, 20, getResources().getDisplayMetrics()));
                    break;
                case R.styleable.AnnularView_speed:
                    speed = typedArray.getInt(i, 30);
                    break;
                default:
                    break;
            }
        }
        typedArray.recycle();
        mPaint = new Paint();
        //TODO 这里线程在页面销毁后仍存在，需要使用线程池管理，或是在页面销毁时候线程停止掉
        new Thread() {
            @Override
            public void run() {
                while (true) {
                    progress++;
                    if (progress == 360) {
                        progress = 0;
                        isNext = !isNext;
                    }
                    postInvalidate();
                    try {
                        Thread.sleep(speed);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //圆心坐标
        int center = getWidth() / 2;
        //半径
        int radius = center - anWidth/2;
        //圆环宽度
        mPaint.setStrokeWidth(anWidth);
        //消除锯齿
        mPaint.setAntiAlias(true);
        //设置空心
        mPaint.setStyle(Paint.Style.STROKE);
        //用于定义的圆弧的形状和大小界限
        RectF oval = new RectF(center - radius, center - radius, center + radius ,
                center + radius );
        if (isNext) {
            mPaint.setColor(secondColor);
            canvas.drawCircle(center, center, radius, mPaint);
            mPaint.setColor(firstColor);
            canvas.drawArc(oval, -90, progress, false, mPaint);
        } else {
            //第一颜色完整，第二颜色开跑--初始状态
            mPaint.setColor(firstColor);
            //画圆环
            canvas.drawCircle(center, center, radius, mPaint);
            mPaint.setColor(secondColor);
            //根据进度画圆弧
            canvas.drawArc(oval, -90, progress, false, mPaint);
        }
    }
}

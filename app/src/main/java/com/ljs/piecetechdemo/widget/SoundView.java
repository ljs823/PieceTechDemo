package com.ljs.piecetechdemo.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;

import com.ljs.piecetechdemo.R;

/**
 * Created by ljs on 2018/3/29.
 * Desc:
 */

public class SoundView extends View {

    /**
     * 第一、二圈颜色，圆的宽度、圆点个数、间隙宽、当前显示音量大小的个数
     */
    private int darkColor, lightColor, circleWidth, dotCount, splitSize, currentCount = 3;
    /**
     * 中间图标
     */
    private Bitmap mImg;
    /**
     * 画笔
     */
    private Paint mPaint;
    /**
     * 控件边界
     */
    private Rect mRect;

    public SoundView(Context context) {
        this(context, null);
    }

    public SoundView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SoundView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = context.getTheme().obtainStyledAttributes(attrs, R.styleable.SoundView, defStyleAttr, 0);
        int indexCount = typedArray.getIndexCount();
        for (int i = 0; i < indexCount; i++) {
            switch (typedArray.getIndex(i)) {
                case R.styleable.SoundView_darkColor:
                    darkColor = typedArray.getColor(i, Color.BLACK);
                    break;
                case R.styleable.SoundView_lightColor:
                    lightColor = typedArray.getColor(i, Color.GRAY);
                    break;
                case R.styleable.SoundView_circleWidth:
                    circleWidth = (int) typedArray.getDimension(i, TypedValue.applyDimension(TypedValue
                            .COMPLEX_UNIT_DIP, 10, getResources().getDisplayMetrics()));
                    break;
                case R.styleable.SoundView_dotCount:
                    dotCount = typedArray.getInt(i, 10);
                    break;
                case R.styleable.SoundView_splitSize:
                    splitSize = (int) typedArray.getDimension(i, TypedValue.applyDimension(TypedValue
                            .COMPLEX_UNIT_DIP, 5, getResources().getDisplayMetrics()));
                    break;
                case R.styleable.SoundView_bg:
                    mImg = BitmapFactory.decodeResource(getResources(), typedArray
                            .getResourceId(typedArray.getIndex(i), 0));
                    break;
                default:
                    break;
            }
        }
        typedArray.recycle();
        mPaint = new Paint();
        mRect = new Rect();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        mPaint.setAntiAlias(true);//消除锯齿
        mPaint.setStrokeWidth(circleWidth);//设置圆环宽度
        mPaint.setStrokeCap(Paint.Cap.ROUND);//设置圆环断点形状为圆头
        mPaint.setStyle(Paint.Style.STROKE);//设置圆心
        int center = getWidth() / 2;//圆心x坐标
        int radius = center - circleWidth;//半径
        //画块
        drawOval(canvas, center, radius);
        // 计算内切正方形位置--获取内圆半径
        int relRadius = radius - circleWidth;
        //内切正方形的距离左边 = mCircleWidth + relRadius - √2 / 2
        mRect.left = (int) (relRadius - Math.sqrt(2) * 1.0f / 2 * relRadius) + circleWidth * 2;
        mRect.top = (int) (relRadius - Math.sqrt(2) * 1.0f / 2 * relRadius) + circleWidth * 2;
        mRect.right = (int) (mRect.left + Math.sqrt(2) * relRadius);
        mRect.bottom = (int) (mRect.top + Math.sqrt(2) * relRadius);
        if (mImg.getWidth() < Math.sqrt(2) * relRadius) {
            mRect.left = (int) (mRect.left + Math.sqrt(2) * relRadius * 1.0f / 2 - mImg.getWidth() * 1.0f / 2);
            mRect.top = (int) (mRect.top + Math.sqrt(2) * relRadius * 1.0f / 2 - mImg.getHeight() * 1.0f / 2);
            mRect.right = mRect.left + mImg.getWidth();
            mRect.bottom = mRect.top + mImg.getHeight();
        }
        canvas.drawBitmap(mImg, null, mRect, mPaint);
    }

    /**
     * 根据参数画出每个小块
     */
    private void drawOval(Canvas canvas, int center, int radius) {
        //根据需要画的个数及间隙计算每个块所占比例
        float itemSize = (360 * 1.0f - dotCount * splitSize) / dotCount;
        //定义圆弧形状和大小界限
        RectF oval = new RectF(center - radius, center - radius, center
                + radius, center + radius);
        mPaint.setColor(darkColor);
        for (int i = 0; i < dotCount; i++) {
            //根据进度画小块
            canvas.drawArc(oval, i * (itemSize + splitSize), itemSize, false, mPaint);
        }
        mPaint.setColor(lightColor);
        for (int i = 0; i < currentCount; i++) {
            canvas.drawArc(oval, i * (itemSize + splitSize), itemSize, false, mPaint);
        }
    }

    private int xDown, xUp;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_UP:
                xUp = (int) event.getY();
                if (xUp > xDown) {
                    //下滑
                    down();
                } else {
                    up();
                }
                break;
            case MotionEvent.ACTION_DOWN:
                xDown = (int) event.getY();
                break;
            default:
                break;
        }
        return true;
    }

    private void up() {
        if (currentCount == dotCount) {
            return;
        }
        postInvalidate();
        currentCount++;
    }

    private void down() {
        if (currentCount==0){
            return;
        }
        currentCount--;
        postInvalidate();
    }
}

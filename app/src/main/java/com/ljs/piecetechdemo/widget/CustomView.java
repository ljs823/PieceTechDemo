package com.ljs.piecetechdemo.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;

import com.ljs.piecetechdemo.R;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

/**
 * Created by ljs on 2018/3/28.
 * Desc: 自定义View演练
 */

public class CustomView extends View {

    /**
     * 文本内容
     */
    private String mText;
    /**
     * 文本颜色
     */
    private int mTextColor;
    /**
     * 文本大小
     */
    private int mTextsSize;
    /**
     * 控制某些开关的标记值
     */
    private boolean isEnabled;
    /**
     * 设置某些作为特定使用的标记值
     */
    private int type;
    /**
     * 绘制时控制文本的范围
     */
    private Rect mBound;
    /**
     * 画笔
     */
    private Paint mPaint;

    /**
     * 上下文创建
     */
    public CustomView(Context context) {
        this(context, null);
    }

    /**
     * 布局文件调用两参构造函数
     */
    public CustomView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    /**
     * 三参构造函数中获取自定义属性
     */
    public CustomView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        //获取自定义样式属性
        TypedArray typedArray = context.getTheme().obtainStyledAttributes(attrs, R.styleable.CustomView, defStyleAttr, 0);
        int n = typedArray.getIndexCount();
        for (int i = 0; i < n; i++) {
            switch (typedArray.getIndex(i)) {
                case R.styleable.CustomView_titleText:
                    mText = typedArray.getString(i);
                    break;
                case R.styleable.CustomView_titleTextColor:
                    mTextColor = typedArray.getColor(i, Color.BLACK);
                    break;
                case R.styleable.CustomView_titleTextSize:
                    mTextsSize = (int) typedArray.getDimension(i, TypedValue.applyDimension(TypedValue
                            .COMPLEX_UNIT_SP, 16, getResources().getDisplayMetrics()));
                    break;
                case R.styleable.CustomView_isEnabled:
                    isEnabled=typedArray.getBoolean(i,false);
                    break;
                case R.styleable.CustomView_type:
                    //默认是其第一个，即first
                    type=typedArray.getInt(i,0);
                    break;
                default:
                    break;
            }
        }
        //资源释放
        typedArray.recycle();

        mPaint = new Paint();
        mPaint.setTextSize(mTextsSize);
        mPaint.setColor(mTextColor);
        mBound = new Rect();
        mPaint.getTextBounds(mText, 0, mText.length(), mBound);

        this.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mText = randomText();
                postInvalidate();
            }
        });
    }

    /**
     * 根据API获取宽高的Mode值，若EXACTLY则表示准确的值或match_parent，所以具体处理如下
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        Log.d("ljs", "onMeasure");
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int width, height;
        if (widthMode == MeasureSpec.EXACTLY) {
            width = widthSize;
        } else {
            mPaint.setTextSize(mTextsSize);
            mPaint.getTextBounds(mText, 0, mText.length(), mBound);
            int textWidth = mBound.width();
            width = getPaddingLeft() + textWidth + getPaddingRight();
        }
        if (heightMode == MeasureSpec.EXACTLY) {
            height = heightSize;
        } else {
            mPaint.setTextSize(mTextsSize);
            mPaint.getTextBounds(mText, 0, mText.length(), mBound);
            int textHeight = mBound.height();
            height = getPaddingTop() + textHeight + getPaddingBottom();
        }
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        Log.d("ljs", "onLayout");
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Log.d("ljs", "onDraw");
        mPaint.setColor(Color.BLUE);
        canvas.drawRect(0, 0, getMeasuredWidth(), getMeasuredHeight(), mPaint);

        mPaint.setColor(mTextColor);
        canvas.drawText(mText, getWidth() / 2 - mBound.width() / 2, getHeight() / 2 + mBound.height() / 2, mPaint);
    }

    private String randomText() {
        Random random = new Random();
        Set<Integer> set = new HashSet<>();
        while (set.size() < 4) {
            int randomInt = random.nextInt(10);
            set.add(randomInt);
        }
        StringBuilder buffer = new StringBuilder();
        for (Integer i : set) {
            buffer.append(i);
        }
        return buffer.toString();
    }
}

package com.ljs.piecetechdemo.widget;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;

import com.ljs.piecetechdemo.R;

/**
 * Created by ljs on 2018/3/28.
 * Desc:带图片的文本控件
 */

public class CusImgView extends View {

    /**
     * 文本
     */
    private String mText;
    private int mTextColor;
    private int mTextSize;

    /**
     * 图片
     */
    private Bitmap mImg;
    private int mImgScaleType;

    /**
     * 控件边界、文本边界
     */
    private Rect mRect, textBound;
    /**
     * 绘制画笔
     */
    private Paint mPaint;
    /**
     * 控件宽高--通过计算后要设置的值
     */
    private int mWidth, mHeight;

    public CusImgView(Context context) {
        this(context, null);
    }

    public CusImgView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CusImgView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = context.getTheme().obtainStyledAttributes(attrs, R.styleable.CusImgView, defStyleAttr, 0);
        int indexCount = typedArray.getIndexCount();
        for (int i = 0; i < indexCount; i++) {
            switch (typedArray.getIndex(i)) {
                case R.styleable.CusImgView_text:
                    mText = typedArray.getString(i);
                    break;
                case R.styleable.CusImgView_textSize:
                    mTextSize = (int) typedArray.getDimension(i, TypedValue.applyDimension(TypedValue
                            .COMPLEX_UNIT_SP, 14, getResources().getDisplayMetrics()));
                    break;
                case R.styleable.CusImgView_textColor:
                    mTextColor = typedArray.getColor(i, Color.BLACK);
                    break;
                case R.styleable.CusImgView_img:
                    mImg = BitmapFactory.decodeResource(getResources(), typedArray
                            .getResourceId(typedArray.getIndex(i), 0));
                    break;
                case R.styleable.CusImgView_imgScaleType:
                    mImgScaleType = typedArray.getInt(typedArray.getIndex(i), 0);
                    break;
                default:
                    break;
            }
        }
        typedArray.recycle();
        mRect = new Rect();
        textBound = new Rect();
        mPaint = new Paint();
        mPaint.setTextSize(mTextSize);
        mPaint.getTextBounds(mText, 0, mText.length(), textBound);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        //若是wrap_content，根据字体或图片宽度大小进行处理，高度为字体与图片高度之和
        if (widthMode == MeasureSpec.EXACTLY) {
            mWidth = widthSize;
        } else {
            int widImg = getPaddingLeft() + mImg.getWidth() + getPaddingRight();
            int widText = getPaddingLeft() + textBound.width() + getPaddingRight();
            if (widthMode == MeasureSpec.AT_MOST) {
                int wid = Math.max(widImg, widText);
                mWidth = Math.min(wid, widthSize);
            }
        }
        if (heightMode == MeasureSpec.EXACTLY) {
            mHeight = heightSize;
        } else {
            int hei = getPaddingTop()+ mImg.getHeight()  + textBound.height() + getPaddingRight();
            if (heightMode == MeasureSpec.AT_MOST) {
                mHeight = Math.min(hei, heightSize);
            }
        }
        setMeasuredDimension(mWidth, mHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //super.onDraw(canvas);
        mPaint.setStrokeWidth(4);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(Color.RED);
        canvas.drawRect(0, 0, getMeasuredWidth(), getMeasuredHeight(), mPaint);
        mRect.left = getPaddingLeft();
        mRect.right = mWidth - getPaddingRight();
        mRect.top = getPaddingTop();
        mRect.bottom = mHeight - getPaddingBottom();
        mPaint.setColor(mTextColor);
        mPaint.setStyle(Paint.Style.FILL);

        //当前设置宽度小于字体需要宽度。设置为xx...效果
        if (textBound.width() > mWidth) {
            TextPaint paint = new TextPaint(mPaint);
            String msg = TextUtils.ellipsize(mText, paint, mWidth - getPaddingLeft()
                    - getPaddingRight(), TextUtils.TruncateAt.END).toString();
            canvas.drawText(msg, getPaddingLeft(), mHeight - getPaddingBottom(), mPaint);
        } else {
            //居中
            canvas.drawText(mText, mWidth / 2 - textBound.width() / 2, mHeight - getPaddingBottom(), mPaint);
        }

        //下面一句设置字体范围高度为底，避免字体图片重叠
        mRect.bottom -= textBound.height();
        if (mImgScaleType == 0) {
            //fitXY——在attrs文件中查看
            canvas.drawBitmap(mImg, null, mRect, mPaint);
        } else {
            //center
            mRect.left = mWidth / 2 - mImg.getWidth() / 2;
            mRect.right = mWidth / 2 + mImg.getWidth() / 2;
            mRect.top = (mHeight - textBound.height()) / 2 - mImg.getHeight() / 2;
            mRect.bottom = (mHeight - textBound.height()) / 2 + mImg.getHeight() / 2;
            canvas.drawBitmap(mImg, null, mRect, mPaint);
        }
        Log.d("ljs", mImgScaleType + ":");
    }
}

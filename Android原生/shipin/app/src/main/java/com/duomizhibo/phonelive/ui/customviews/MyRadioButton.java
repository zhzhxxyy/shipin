package com.duomizhibo.phonelive.ui.customviews;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.RadioButton;

import com.duomizhibo.phonelive.R;


/**
 * Created by cxf on 2017/7/19.
 */

public class MyRadioButton extends RadioButton {

    private int mDrawableSize;
    private int mTopDrawableSize;
    private int mLeftDrawableSize;
    private int mRightDrawableSize;
    private int mBottomDrawableSize;
    private int mTopDrawableWidth;
    private int mTopDrawableHeight;
    private int mLeftDrawableWidth;
    private int mLeftDrawableHeight;
    private int mRightDrawableWidth;
    private int mRightDrawableHeight;
    private int mBottomDrawableWidth;
    private int mBottomDrawableHeight;
    private Drawable mTopDrawable;
    private Drawable mLeftDrawable;
    private Drawable mRightDrawable;
    private Drawable mBottomDrawable;


    public MyRadioButton(Context context) {
        this(context, null);
    }

    public MyRadioButton(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyRadioButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.MyRadioButton);
        mDrawableSize = (int) ta.getDimension(R.styleable.MyRadioButton_radio_drawableSize, dip2px(context, 20));
        mTopDrawableSize = (int) ta.getDimension(R.styleable.MyRadioButton_radio_top_drawableSize, mDrawableSize);
        mLeftDrawableSize = (int) ta.getDimension(R.styleable.MyRadioButton_radio_left_drawableSize, mDrawableSize);
        mRightDrawableSize = (int) ta.getDimension(R.styleable.MyRadioButton_radio_right_drawableSize, mDrawableSize);
        mBottomDrawableSize = (int) ta.getDimension(R.styleable.MyRadioButton_radio_bottom_drawableSize, mDrawableSize);
        mTopDrawableWidth = (int) ta.getDimension(R.styleable.MyRadioButton_radio_top_drawableWidth, mTopDrawableSize);
        mTopDrawableHeight = (int) ta.getDimension(R.styleable.MyRadioButton_radio_top_drawableHeight, mTopDrawableSize);
        mLeftDrawableWidth = (int) ta.getDimension(R.styleable.MyRadioButton_radio_left_drawableWidth, mLeftDrawableSize);
        mLeftDrawableHeight = (int) ta.getDimension(R.styleable.MyRadioButton_radio_left_drawableHeight, mLeftDrawableSize);
        mRightDrawableWidth = (int) ta.getDimension(R.styleable.MyRadioButton_radio_right_drawableWidth, mRightDrawableSize);
        mRightDrawableHeight = (int) ta.getDimension(R.styleable.MyRadioButton_radio_right_drawableHeight, mRightDrawableSize);
        mBottomDrawableWidth = (int) ta.getDimension(R.styleable.MyRadioButton_radio_bottom_drawableWidth, mBottomDrawableSize);
        mBottomDrawableHeight = (int) ta.getDimension(R.styleable.MyRadioButton_radio_bottom_drawableHeight, mBottomDrawableSize);
        mTopDrawable = ta.getDrawable(R.styleable.MyRadioButton_radio_top_drawable);
        mLeftDrawable = ta.getDrawable(R.styleable.MyRadioButton_radio_left_drawable);
        mRightDrawable = ta.getDrawable(R.styleable.MyRadioButton_radio_right_drawable);
        mBottomDrawable = ta.getDrawable(R.styleable.MyRadioButton_radio_bottom_drawable);

        ta.recycle();

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        setCompoundDrawablesWithIntrinsicBounds(mLeftDrawable, mTopDrawable, mRightDrawable, mBottomDrawable);
    }

    @Override
    public void setCompoundDrawablesWithIntrinsicBounds(Drawable left, Drawable top, Drawable right, Drawable bottom) {
        if (left != null) {
            left.setBounds(0, 0, mLeftDrawableWidth, mLeftDrawableHeight);
        }
        if (top != null) {
            top.setBounds(0, 0, mTopDrawableWidth, mTopDrawableHeight);
        }
        if (right != null) {
            right.setBounds(0, 0, mRightDrawableWidth, mRightDrawableHeight);
        }
        if (bottom != null) {
            bottom.setBounds(0, 0, mBottomDrawableWidth, mBottomDrawableHeight);
        }
        setCompoundDrawables(left, top, right, bottom);
    }

    private int dip2px(Context context, float dpValue) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }


}

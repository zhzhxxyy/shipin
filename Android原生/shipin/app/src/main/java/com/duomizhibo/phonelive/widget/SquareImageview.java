package com.duomizhibo.phonelive.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by debug on 2017/10/24.
 */

public class SquareImageview extends ImageView {
    public SquareImageview(Context context) {
        super(context);
    }

    public SquareImageview(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public SquareImageview(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec);
    }
}

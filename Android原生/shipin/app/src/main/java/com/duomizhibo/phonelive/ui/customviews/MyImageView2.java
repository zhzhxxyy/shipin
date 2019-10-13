package com.duomizhibo.phonelive.ui.customviews;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by weilian on 2017/9/8.
 */

public class MyImageView2 extends ImageView {
        public MyImageView2(Context context) {
            super(context);
        }

        public MyImageView2(Context context, @Nullable AttributeSet attrs) {
            super(context, attrs);
        }

        public MyImageView2(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
            super(context, attrs, defStyleAttr);
        }

        @Override
        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            super.onMeasure(widthMeasureSpec, widthMeasureSpec);
        }

}

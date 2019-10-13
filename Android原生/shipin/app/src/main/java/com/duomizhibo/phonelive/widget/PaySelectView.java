package com.duomizhibo.phonelive.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import com.duomizhibo.phonelive.R;

/**
 * Created by Administrator on 2016/3/11.
 */
public class PaySelectView extends RelativeLayout {
    public PaySelectView(Context context) {
        super(context);
    }

    public PaySelectView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PaySelectView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.pay_choose);
    }
}

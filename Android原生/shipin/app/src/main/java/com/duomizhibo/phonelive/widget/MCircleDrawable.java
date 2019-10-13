package com.duomizhibo.phonelive.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;

public class MCircleDrawable extends Drawable {

    private Paint   mPaint;
    private Bitmap  mBitmap;

    private static final int BLACK_COLOR          = 0xb2000000;//黑色 背景
    private static final int BLACKGROUDE_ADD_SIZE = -15;//背景比图片多出来的部分

    public MCircleDrawable(Bitmap bitmap) {
        mBitmap = bitmap;
        BitmapShader bitmapShader = new BitmapShader(bitmap,
                Shader.TileMode.CLAMP,
                Shader.TileMode.CLAMP);

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setShader(bitmapShader);
    }



    @Override
    public void draw(Canvas canvas) {

        //设置背景
        Paint backgroundPaint = new Paint();
        backgroundPaint.setAntiAlias(true);
        backgroundPaint.setColor(BLACK_COLOR);
        canvas.drawCircle(getIntrinsicWidth() / 2 + BLACKGROUDE_ADD_SIZE, getIntrinsicHeight() / 2 + BLACKGROUDE_ADD_SIZE,
                getIntrinsicWidth() / 2 + BLACKGROUDE_ADD_SIZE, backgroundPaint);

        //先将画布平移，防止图片不在正中间，然后绘制图片
        canvas.translate(BLACKGROUDE_ADD_SIZE + 20, BLACKGROUDE_ADD_SIZE);
        canvas.drawCircle(getIntrinsicWidth() / 2, getIntrinsicHeight() / 2, getIntrinsicWidth() / 2, mPaint);
    }

    @Override
    public int getIntrinsicWidth() {
        return mBitmap.getWidth();
    }

    @Override
    public int getIntrinsicHeight() {
        return mBitmap.getHeight();
    }

    @Override
    public void setAlpha(int alpha) {
        mPaint.setAlpha(alpha);
    }

    @Override
    public void setColorFilter(ColorFilter cf) {
        mPaint.setColorFilter(cf);
    }

    @Override
    public int getOpacity() {
        return PixelFormat.TRANSLUCENT;
    }
}


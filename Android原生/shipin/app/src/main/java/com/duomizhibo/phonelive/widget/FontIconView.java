package com.duomizhibo.phonelive.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * IconFont 使用
 */
@SuppressLint("AppCompatCustomView")
public class FontIconView extends TextView {

    public FontIconView(Context context) {
        super(context);
        init(context);

    }

    public FontIconView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);

    }

    public FontIconView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);

    }

    /**
     * 初始化
     * @param context
     */
    private void init(Context context) {
        //设置字体图标
        Typeface font = Typeface.createFromAsset(context.getAssets(), "iconfont.ttf");
        this.setTypeface(font);
    }

}

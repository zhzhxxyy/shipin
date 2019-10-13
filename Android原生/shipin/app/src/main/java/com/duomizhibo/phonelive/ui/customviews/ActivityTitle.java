package com.duomizhibo.phonelive.ui.customviews;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.duomizhibo.phonelive.R;

/**
 * 标题控件
 */
public class ActivityTitle extends RelativeLayout {

    private String titleText;
    private boolean canBack;
    private String backText;
    private String moreText;
    private int textColor;

    private TextView tvReturn;
    private TextView tvTitle;
    private TextView tvMore;
    private int tvBackImg;


    public ActivityTitle(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.view_title, this);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.ActivityTitle, 0, 0);
        try {
            titleText = ta.getString(R.styleable.ActivityTitle_titleText);
            canBack = ta.getBoolean(R.styleable.ActivityTitle_canBack, true);
            backText = ta.getString(R.styleable.ActivityTitle_backText);
            moreText = ta.getString(R.styleable.ActivityTitle_moreText);
            tvBackImg = ta.getResourceId(R.styleable.ActivityTitle_titleBackImg,R.drawable.icon_arrow_left);
            textColor = ta.getColor(R.styleable.ActivityTitle_titleColor,context.getResources().getColor(R.color.light_gray));
            setUpView(context);
        } finally {
            ta.recycle();
        }
    }

    private void setUpView(Context context){
        tvReturn = (TextView)findViewById(R.id.menu_return);

        tvTitle = (TextView)findViewById(R.id.title);
        tvTitle.setTextColor(textColor);
        tvMore = (TextView)findViewById(R.id.menu_more);

        if (!canBack){
            tvReturn.setVisibility(View.GONE);
        }
        Drawable drawable = context.getResources().getDrawable(tvBackImg);
        /// 这一步必须要做,否则不会显示.
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        tvReturn.setCompoundDrawables(drawable,null,null,null);
        tvReturn.setText(backText);
        tvMore.setText(moreText);
        tvTitle.setText(titleText);
    }

    /**
     * 设置标题
     * @param title 标题
     */
    public void setTitle(String title){
        titleText = title;
        tvTitle.setText(title);
    }

    /**
     * 设置扩展消息
     * @param title 扩展消息
     */
    public void setMoreText(String title){
        moreText = title;
        tvMore.setText(title);
    }

    /**
     * 设置返回文案
     * @param strReturn 返回文案
     */
    public void setReturnText(String strReturn){
        backText = strReturn;
        tvReturn.setText(strReturn);
    }

    /**
     * 设置返回消息事件
     * @param listener 返回消息listener
     */
    public void setReturnListener(OnClickListener listener){
        tvReturn.setOnClickListener(listener);
    }

    /**
     * 设置扩展事件
     * @param listener 扩展事件listener
     */
    public void setMoreListener(OnClickListener listener){
        if (!TextUtils.isEmpty(moreText)) {
            tvMore.setOnClickListener(listener);
        }
    }
}

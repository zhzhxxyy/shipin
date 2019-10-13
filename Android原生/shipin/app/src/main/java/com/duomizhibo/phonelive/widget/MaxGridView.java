package com.duomizhibo.phonelive.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;


public class MaxGridView extends GridView {

	public MaxGridView(Context context) {
		super(context);
	}

	public MaxGridView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	public MaxGridView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
		super.onMeasure(widthMeasureSpec, expandSpec);
	}
}

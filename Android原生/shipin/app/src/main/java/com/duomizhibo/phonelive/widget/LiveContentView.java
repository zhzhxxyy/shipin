package com.duomizhibo.phonelive.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.RelativeLayout;

/**
 * Created by Administrator on 2016/5/16.
 */
public class LiveContentView extends RelativeLayout {
    private int lastX;
    private int lastY;
    public LiveContentView(Context context) {
        this(context,null);
    }

    public LiveContentView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public LiveContentView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int rowX = (int) event.getRawX();
        int rowY = (int) event.getRawY();
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                lastX = rowX;
                lastY = rowY;
                break;
            case MotionEvent.ACTION_MOVE:
                int offsetX = rowX - lastX;
                int offsetY = rowY - lastY;
                layout(getLeft() + offsetX,
                        getTop(),
                        getRight(),
                        getBottom()
                );
                lastX = rowX;
                lastY = rowY;
                break;
        }
        return true;
    }
}

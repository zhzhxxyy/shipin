package com.duomizhibo.phonelive.ui.other;

import android.view.View;
import android.widget.AdapterView;

/**
 * Created by mengyunfeng on 17/3/31.
 */

public abstract class OnItemEvent implements AdapterView.OnItemClickListener {

    private static long lastTime;

    public abstract void singleClick(View v,int position);
    private long delay;

    public OnItemEvent(long delay) {
        this.delay = delay;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (onMoreClick(view)) {
            return;
        }
        singleClick(view, position);
    }

    public boolean onMoreClick(View v) {
        boolean flag = false;
        long time = System.currentTimeMillis() - lastTime;
        if (time < delay) {
            flag = true;
        }
        lastTime = System.currentTimeMillis();
        return flag;
    }
}

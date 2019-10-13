package com.duomizhibo.phonelive.widget;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by yunbao on 2016/10/29.
 */

public class SpaceRecycleView extends RecyclerView.ItemDecoration{
    private int space;

    public SpaceRecycleView(int space) {
        this.space = space;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {

        if(parent.getChildPosition(view) != 0)
            outRect.left = space;
    }
}

package com.duomizhibo.phonelive.adapter.video;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.duomizhibo.phonelive.bean.video.HomeTop;
import com.duomizhibo.phonelive.R;
import com.duomizhibo.phonelive.utils.SimpleUtils;


import java.util.List;

import cn.trinea.android.common.util.ListUtils;

public class ImageHomeAdapter extends RecyclingPagerAdapter {

    private Context context;
    private Activity activity;
    private List<HomeTop.Carousel> list;

    private int size;
    private boolean isInfiniteLoop;

    public ImageHomeAdapter(Context context, Activity activity, List<HomeTop.Carousel> list) {
        this.context = context;
        this.activity = activity;
        this.list = list;
        this.size = ListUtils.getSize(list);
        isInfiniteLoop = false;
    }

    @Override
    public int getCount() {
        return isInfiniteLoop ? Integer.MAX_VALUE : ListUtils.getSize(list);
    }

    private int getPosition(int position) {
        return isInfiniteLoop ? position % size : position;
    }

    @Override
    public View getView(final int position, View view, ViewGroup container) {
        ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = holder.imageView = new ImageView(context);
            view.setTag(R.string.app_name, holder);
        } else {
            holder = (ViewHolder) view.getTag(R.string.app_name);
        }
        holder.imageView.setScaleType(ImageView.ScaleType.FIT_XY);

        SimpleUtils.loadImageForView(activity,  holder.imageView, list.get(getPosition(position)).getUrl(), 0);

        return view;
    }

    private static class ViewHolder {

        ImageView imageView;
    }

    /**
     * @return the isInfiniteLoop
     */
    public boolean isInfiniteLoop() {
        return isInfiniteLoop;
    }

    /**
     * @param isInfiniteLoop the isInfiniteLoop to set
     */
    public ImageHomeAdapter setInfiniteLoop(boolean isInfiniteLoop) {
        this.isInfiniteLoop = isInfiniteLoop;
        return this;
    }
}

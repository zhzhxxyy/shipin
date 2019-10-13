package com.duomizhibo.phonelive.adapter;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import java.util.ArrayList;
import java.util.List;

/**
 * 礼物列表分页
 */
public class ViewPageGridViewAdapter extends PagerAdapter {
    private List<View> mGiftListView = new ArrayList<>();

    public ViewPageGridViewAdapter(List<View> mGiftListView) {
        this.mGiftListView = mGiftListView;
    }

    @Override
    public int getCount() {

        return mGiftListView.size();
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        if(mGiftListView.get(position).getParent() != null){
            ((ViewGroup)mGiftListView.get(position).getParent()).removeView(mGiftListView.get(position));
        }

        container.addView(mGiftListView.get(position));
        return mGiftListView.get(position);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }
}

package com.duomizhibo.phonelive.base;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.duomizhibo.phonelive.R;
import com.duomizhibo.phonelive.adapter.ViewPageFragmentAdapter;
import com.duomizhibo.phonelive.utils.TDevice;
import com.duomizhibo.phonelive.widget.PagerSlidingTabStrip;

/**
 * 带有导航条的基类
 */
public abstract class BaseViewPagerFragment extends BaseFragment {

    protected PagerSlidingTabStrip mTabStrip;
    protected ViewPager mViewPager;
    protected ViewPageFragmentAdapter mTabsAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        return inflater.inflate(R.layout.base_viewpage_fragment, null);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        mTabStrip = (PagerSlidingTabStrip) view
                .findViewById(R.id.tabs);

        mViewPager = (ViewPager) view.findViewById(R.id.pager);
        mTabsAdapter = new ViewPageFragmentAdapter(getChildFragmentManager(), mViewPager);
        setScreenPageLimit();
        onSetupTabAdapter(view,mTabsAdapter, mViewPager);
        mTabStrip.setViewPager(mViewPager);
        mTabStrip.setDividerColor(getResources().getColor(R.color.graywhite));
        mTabStrip.setIndicatorColor(getResources().getColor(R.color.black));
        mTabStrip.setTextColor(getResources().getColor(R.color.black));
        mTabStrip.setTextSize((int) TDevice.dpToPixel(15));
        mTabStrip.setIndicatorHeight(2);
        mTabStrip.setSelectedTextColor(Color.BLACK);
        mTabStrip.setZoomMax(0f);
        mTabStrip.setUnderlineColorResource(R.color.graywhite);

        // if (savedInstanceState != null) {
        // int pos = savedInstanceState.getInt("position");
        // mViewPager.setCurrentItem(pos, true);
        // }
    }
    
    protected void setScreenPageLimit() {
    }

    // @Override
    // public void onSaveInstanceState(Bundle outState) {
    // //No call for super(). Bug on API Level > 11.
    // if (outState != null && mViewPager != null) {
    // outState.putInt("position", mViewPager.getCurrentItem());
    // }
    // //super.onSaveInstanceState(outState);
    // }

    protected abstract void onSetupTabAdapter(View view,ViewPageFragmentAdapter adapter,ViewPager viewPager);
}
package com.duomizhibo.phonelive.fragment.video;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.duomizhibo.phonelive.R;
import com.duomizhibo.phonelive.base.AbsFragment;
import com.duomizhibo.phonelive.fragment.HotFragment;
import com.duomizhibo.phonelive.fragment.NearFragment;
import com.duomizhibo.phonelive.fragment.PersontoPersonFragment;
import com.duomizhibo.phonelive.fragment.VideoFragment;
import com.duomizhibo.phonelive.ui.activity.NewMainActivity;
import com.duomizhibo.phonelive.ui.customviews.ViewPagerIndicator;
import com.duomizhibo.phonelive.utils.UIHelper;

import java.util.ArrayList;
import java.util.List;


public class HomeFenleiFragment extends AbsFragment implements NewMainActivity.OnResumeCallback {

    private View mRootView;
    private ViewPagerIndicator mIndicator;
    private ViewPager mViewPager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.new_fragment_home_fenlei, container, false);
        return mRootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView(savedInstanceState);
    }

    private void initView(Bundle savedInstanceState) {

        mIndicator = (ViewPagerIndicator) mRootView.findViewById(R.id.indicator);
        mIndicator.setTitles(new String[]{"视频", "图片", "资讯"});
        mIndicator.setVisibleChildCount(3);
        mViewPager = (ViewPager) mRootView.findViewById(R.id.viewPager);
        if(savedInstanceState==null){
            final List<Fragment> fragmentList = new ArrayList<>();
            HotFragment f1 = new HotFragment();
            VideoFragment f2 = new VideoFragment();
            NearFragment f3 = new NearFragment();

            fragmentList.add(f1);
            fragmentList.add(f2);
            fragmentList.add(f3);
            mViewPager.setAdapter(new FragmentPagerAdapter(getChildFragmentManager()) {
                @Override
                public Fragment getItem(int position) {
                    return fragmentList.get(position);
                }

                @Override
                public int getCount() {
                    return fragmentList.size();
                }
            });
            mViewPager.setOffscreenPageLimit(3);
            mIndicator.setViewPager(mViewPager);
            mIndicator.setListener(new ViewPagerIndicator.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }
                @Override
                public void onPageSelected(int position) {
                    publishPosition(position);
                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });
        }


    }

    private void publishPosition(int position) {
//        for (PageChangedListener listener : mPageChangedListeners) {
//            listener.onPageChanged(position);
//        }
    }


    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            publishPosition(mViewPager.getCurrentItem());
        }
    }

    @Override
    public void onResumeRefresh() {
        publishPosition(mViewPager.getCurrentItem());

    }
}

package com.duomizhibo.phonelive.fragment;

import android.content.Context;
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
import com.duomizhibo.phonelive.ui.customviews.ViewPagerIndicator;
import com.duomizhibo.phonelive.utils.UIHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cxf on 2017/9/5.
 */

public class GuanzhuFragment extends AbsFragment implements View.OnClickListener {

    private Context mContext;
    private View mRootView;
    private ViewPagerIndicator mIndicator;
    private ViewPager mViewPager;
    private List<Fragment> mList;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mContext = getActivity();
        mRootView = inflater.inflate(R.layout.fragment_guanzhu, container, false);
        return mRootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView(savedInstanceState);
    }

    private void initView(Bundle savedInstanceState) {
        mIndicator = (ViewPagerIndicator) mRootView.findViewById(R.id.indicator);
        mIndicator.setTitles(new String[]{"直播", "视频"});
        mIndicator.setVisibleChildCount(2);
        if(savedInstanceState==null){
            mViewPager = (ViewPager) mRootView.findViewById(R.id.viewPager);
            mList = new ArrayList<>();
            mList.add(new GuanzhuLiveFragment());//直播
            mList.add(new GuanzhuVideoFragment());//视频

            mViewPager.setAdapter(new FragmentPagerAdapter(getChildFragmentManager()) {
                @Override
                public Fragment getItem(int position) {
                    return mList.get(position);
                }

                @Override
                public int getCount() {
                    return mList.size();
                }
            });

            mIndicator.setViewPager(mViewPager);

        }
        mRootView.findViewById(R.id.btn_search).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_search:
                UIHelper.showScreen(getActivity());
                break;
        }
    }
}

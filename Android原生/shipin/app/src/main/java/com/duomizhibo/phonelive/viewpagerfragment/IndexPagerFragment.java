package com.duomizhibo.phonelive.viewpagerfragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.duomizhibo.phonelive.fragment.GuanzhuLiveFragment;
import com.duomizhibo.phonelive.fragment.HotFragment;
import com.duomizhibo.phonelive.fragment.VideoFragment;
import com.duomizhibo.phonelive.utils.StringUtils;
import com.duomizhibo.phonelive.utils.UIHelper;
import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.duomizhibo.phonelive.AppContext;
import com.duomizhibo.phonelive.R;
import com.duomizhibo.phonelive.adapter.ViewPageFragmentAdapter;
import com.duomizhibo.phonelive.base.BaseFragment;
import com.duomizhibo.phonelive.fragment.AttentionFragment;
import com.duomizhibo.phonelive.interf.ListenMessage;
import com.duomizhibo.phonelive.interf.PagerSlidingInterface;
import com.duomizhibo.phonelive.ui.other.PhoneLivePrivateChat;
import com.duomizhibo.phonelive.widget.PagerSlidingTabStrip;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class IndexPagerFragment extends BaseFragment implements ListenMessage {

    private View view;
    @InjectView(R.id.mviewpager)
    ViewPager pager;

    @InjectView(R.id.tabs)
    PagerSlidingTabStrip tabs;

    @InjectView(R.id.fl_tab_container)
    RelativeLayout flTabContainer;

    @InjectView(R.id.iv_hot_new_message)
    ImageView mIvNewMessage;

    private ViewPageFragmentAdapter viewPageFragmentAdapter;

    public static int mSex = 0;

    public static String mArea = "";

    private EMMessageListener mMsgListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if (view == null) {
            view = inflater.inflate(R.layout.fragment_hot, null);
            ButterKnife.inject(this, view);

            initView();
            initData();

        } else {
            mIvNewMessage.setVisibility(View.GONE);
            tabs.setPageChangeListener();
        }

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        if (savedInstanceState != null) {
            int pos = savedInstanceState.getInt("position");
            pager.setCurrentItem(pos, true);

        }
    }

    @Override
    public void initData() {


    }


    @OnClick({R.id.iv_hot_private_chat, R.id.iv_hot_search})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_hot_private_chat:
                String uid = AppContext.getInstance().getLoginUid();
                if (0 < StringUtils.toInt(uid)) {
                    mIvNewMessage.setVisibility(View.GONE);
                    UIHelper.showPrivateChatSimple(getActivity(), uid);
                }

                break;
            case R.id.iv_hot_search:
                UIHelper.showScreen(getActivity());
                break;
        }
    }

    private void initView() {
        mIvNewMessage.setVisibility(View.GONE);
        viewPageFragmentAdapter = new ViewPageFragmentAdapter(getFragmentManager(), pager);
        viewPageFragmentAdapter.addTab(getString(R.string.attention), "gz", AttentionFragment.class, getBundle());
        viewPageFragmentAdapter.addTab(getString(R.string.hot), "rm", HotFragment.class, getBundle());
        viewPageFragmentAdapter.addTab(getString(R.string.daren), "dr", GuanzhuLiveFragment.class, getBundle());
        viewPageFragmentAdapter.addTab(getString(R.string.video), "sp", VideoFragment.class, getBundle());
        //viewPageFragmentAdapter.addTab(getString(R.string.game), "dr", GameFragment.class, getBundle());
        pager.setAdapter(viewPageFragmentAdapter);

        pager.setOffscreenPageLimit(2);

        tabs.setViewPager(pager);
        tabs.setUnderlineColor(getResources().getColor(R.color.white));
        tabs.setDividerColor(getResources().getColor(R.color.white));
        tabs.setTextColor(Color.BLACK);
        tabs.setTabPaddingLeftRight(10);
        tabs.setSelectedTextColor(getResources().getColor(R.color.global));
        tabs.setIndicatorHeight(4);
        tabs.setZoomMax(0.2f);
        tabs.setIndicatorColorResource(R.color.global);
        tabs.setPagerSlidingListen(new PagerSlidingInterface() {
            @Override
            public void onItemClick(View v, int currentPosition, int position) {

            }
        });

        pager.setCurrentItem(1);

        tabs.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {


            }
            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


    }

    @Override
    public void onResume() {
        super.onResume();
        //获取私信未读数量
        if (PhoneLivePrivateChat.getUnreadMsgsCount() > 0) {
            mIvNewMessage.setVisibility(View.VISIBLE);
        }else {
            mIvNewMessage.setVisibility(View.GONE);
        }
    }

    public void listenMessage() {
        mMsgListener = new EMMessageListener() {
            @Override
            public void onMessageReceived(List<EMMessage> messages) {
                //收到消息
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mIvNewMessage.setVisibility(View.VISIBLE);
                    }
                });
            }
            @Override
            public void onCmdMessageReceived(List<EMMessage> messages) {
                //收到透传消息
            }

            @Override
            public void onMessageRead(List<EMMessage> messages) {

            }

            @Override
            public void onMessageDelivered(List<EMMessage> messages) {

            }

            @Override
            public void onMessageChanged(EMMessage message, Object change) {
                //消息状态变动
            }
        };
        EMClient.getInstance().chatManager().addMessageListener(mMsgListener);
    }

    public void unListen() {
        EMClient.getInstance().chatManager().removeMessageListener(mMsgListener);
    }

    @Override
    public void onStart() {
        super.onStart();
        listenMessage();
    }

    @Override
    public void onPause() {
        super.onPause();
        unListen();
    }

    @Override
    public void onDestroy() {
        //注销广播
        super.onDestroy();
        unListen();
    }

    private Bundle getBundle() {
        Bundle bundle = new Bundle();
        return bundle;
    }

}

package com.duomizhibo.phonelive.viewpagerfragment;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.hyphenate.chat.EMClient;
import com.duomizhibo.phonelive.event.Event;
import com.duomizhibo.phonelive.fragment.FollowPrivateChatFragment;
import com.duomizhibo.phonelive.fragment.NotFollowPrivateChatFragment;
import com.duomizhibo.phonelive.R;
import com.duomizhibo.phonelive.adapter.ViewPageFragmentAdapter;
import com.duomizhibo.phonelive.base.BaseViewPagerFragment;

import org.greenrobot.eventbus.EventBus;

import butterknife.OnClick;


public class PrivateChatCorePagerFragment extends BaseViewPagerFragment {


    private TextView mTvIgnoreMessage;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view =inflater.inflate(R.layout.fragment_private_core_page, null);

        initView(view);
        initData();
        return view;
    }

    @Override
    protected void onSetupTabAdapter(View view,ViewPageFragmentAdapter adapter,ViewPager viewPager) {
        ((ImageView)view.findViewById(R.id.iv_private_chat_back)).setOnClickListener(this);
        initData();
        Bundle b1 = new Bundle();
        b1.putInt("ACTION",1);
        Bundle b2 = new Bundle();
        b2.putInt("ACTION",0);
        adapter.addTab(getString(R.string.friends), "friends", FollowPrivateChatFragment.class, b1);
        adapter.addTab(getString(R.string.nofollow), "nofollow", NotFollowPrivateChatFragment.class, b2);
        viewPager.setCurrentItem(1);
        viewPager.setOffscreenPageLimit(1);
        viewPager.setAdapter(adapter);

    }

    @Override
    public void initData() {

    }

    @Override
    public void initView(View view) {
        mTvIgnoreMessage = (TextView) view.findViewById(R.id.tv_ignore_message);
        mTvIgnoreMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //所有未读消息数清零
                EMClient.getInstance().chatManager().markAllConversationsAsRead();
                Event.PrivateChatEvent event = new Event.PrivateChatEvent();
                event.action = 1;
                EventBus.getDefault().post(event);
                Toast.makeText(getContext(),"已忽略未读消息",Toast.LENGTH_SHORT).show();
            }
        });
    }

    @OnClick({R.id.iv_private_chat_back})
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_private_chat_back:
                getActivity().finish();
                break;

            default:

                break;
        }
    }
}

package com.duomizhibo.phonelive.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import com.duomizhibo.phonelive.R;
import com.duomizhibo.phonelive.base.BaseFragment;
import com.duomizhibo.phonelive.utils.SharedPreUtil;

import butterknife.ButterKnife;
import butterknife.InjectView;
import cn.jpush.android.api.JPushInterface;

/**
 * 推送管理
 */
public class PushManageFragment extends BaseFragment {
    @InjectView(R.id.ib_push_manage_start_or_close)
    ImageButton mBtnStartOrClosePush;
    private final String IS_OPEN_PUSH = "isOpenPush";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_push_manage,null);
        ButterKnife.inject(this,view);
        initData();
        initView(view);
        return view;
    }

    @Override
    public void initData() {
        
    }

    @Override
    public void initView(View view) {
        //获取是否开启推送
        boolean isOpenPush = SharedPreUtil.getBoolean(getActivity(),IS_OPEN_PUSH);
        //修改ui状态
        changeBtnUI(isOpenPush,mBtnStartOrClosePush);
        //点击切换状态
        mBtnStartOrClosePush.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startOrClosePush();
            }
        });
    }

    private void changeBtnUI(boolean startOrClose,ImageButton ib) {
        ib.setImageResource(startOrClose?R.drawable.global_switch_on:R.drawable.global_switch_off);
    }

    //推送关闭开启
    private void startOrClosePush() {
        boolean isOpenPush = SharedPreUtil.getBoolean(getActivity(),IS_OPEN_PUSH);
        SharedPreUtil.put(getActivity(),IS_OPEN_PUSH,isOpenPush?false:true);
        changeBtnUI(isOpenPush?false:true,mBtnStartOrClosePush);
        if(isOpenPush){
            JPushInterface.stopPush(getActivity());
        }else{
            JPushInterface.resumePush(getActivity());
        }
    }

}

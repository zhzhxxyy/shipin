package com.duomizhibo.phonelive.widget;


import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hyphenate.EMCallBack;
import com.hyphenate.EMError;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.util.DateUtils;
import com.duomizhibo.phonelive.R;
import com.duomizhibo.phonelive.bean.PrivateMessage;

import java.util.Date;

public abstract class PhoneLiveChatRow extends LinearLayout {

    protected Context context;
    protected Activity activity;
    protected EMMessage message;
    protected int position;
    protected BaseAdapter adapter;
    protected LayoutInflater inflater;
    private AvatarView mUhead;
    protected EMCallBack messageSendCallback;
    protected TextView percentageView;


    public PhoneLiveChatRow(Context context, EMMessage message, int position, BaseAdapter adapter) {
        super(context);
        this.context = context;
        this.activity = (Activity) context;
        this.message = message;
        this.position = position;
        this.adapter = adapter;
        inflater = LayoutInflater.from(context);

        initView();
    }

    private void initView() {
        onInflatView();
//        mUhead = (AvatarView) findViewById(R.id.av_message_head);
        onFindViewById();
    }

    public void setUpView(EMMessage message,int position){
        this.message = message;
        this.position = position;
        setUpBaseView();
        onSetUpView();
    }

    private void setUpBaseView(){
        TextView timestamp = (TextView) findViewById(R.id.timestamp);
        if(timestamp != null){
            if(position == 0){
                timestamp.setText(DateUtils.getTimestampString(new Date(message.getMsgTime())));
                timestamp.setVisibility(View.VISIBLE);

            }else{
                PrivateMessage prevMessage = (PrivateMessage) adapter.getItem(position - 1);
                if(prevMessage != null&& DateUtils.isCloseEnough(message.getMsgTime(), prevMessage.message.getMsgTime())){
                    timestamp.setVisibility(View.GONE);
                }else{
                    timestamp.setText(DateUtils.getTimestampString(new Date(message.getMsgTime())));
                    timestamp.setVisibility(View.VISIBLE);
                }
            }


        }
    }
    /**
     * 设置消息发送callback
     */
    protected void setMessageSendCallback(){
        if(messageSendCallback == null){
            messageSendCallback = new EMCallBack() {

                @Override
                public void onSuccess() {
                    updateView();
                }

                @Override
                public void onProgress(final int progress, String status) {
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if(percentageView != null)
                                percentageView.setText(progress + "%");

                        }
                    });
                }

                @Override
                public void onError(int code, String error) {
                    updateView();
                }
            };
        }
        message.setMessageStatusCallback(messageSendCallback);
    }
    protected void updateView() {
        activity.runOnUiThread(new Runnable() {
            public void run() {

                onUpdateView();
            }
        });

    }


    /**
     * 填充layout
     */
    protected abstract void onInflatView();

    /**
     * 查找chatrow里的控件
     */
    protected abstract void onFindViewById();

    /**
     * 消息状态改变，刷新listview
     */
    protected abstract void onUpdateView();

    /**
     * 设置更新控件属性
     */
    protected abstract void onSetUpView();

    /**
     * 聊天气泡被点击事件
     */
    protected abstract void onBubbleClick();
}

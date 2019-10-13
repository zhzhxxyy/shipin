package com.duomizhibo.phonelive.adapter;


import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.duomizhibo.phonelive.widget.PhoneLiveChatRow;
import com.duomizhibo.phonelive.widget.PhoneLiveChatRowText;
import com.hyphenate.chat.EMMessage;
import com.duomizhibo.phonelive.bean.PrivateMessage;

import java.util.ArrayList;
import java.util.List;

//私信
public class MessageAdapter extends BaseAdapter {
    private List<PrivateMessage> mChats = new ArrayList<>();
    private Context context;
    public MessageAdapter(Context context) {
        this.context = context;

    }
    public void setChatList(List<PrivateMessage> mChats){
        this.mChats = mChats;
    }
    public void addMessage(PrivateMessage emMessage){
        this.mChats.add(emMessage);
    }


    @Override
    public int getCount() {
        return mChats.size();
    }

    @Override
    public Object getItem(int position) {
        return mChats.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        PrivateMessage e = mChats.get(position);

        convertView = createChatRow(context, e.message, position,e.uHead);
        //缓存的view的message很可能不是当前item的，传入当前message和position更新ui
        ((PhoneLiveChatRow)convertView).setUpView(e.message, position);
        return convertView;
    }

    private View createChatRow(Context context, EMMessage e, int position,String head) {
        PhoneLiveChatRow chatRow = null;
        switch (e.getType()) {
            case TXT:
                chatRow = new PhoneLiveChatRowText(context, e, position, this,head);
                break;

        }
        return chatRow;
    }


}

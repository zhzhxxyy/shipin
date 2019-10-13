package com.duomizhibo.phonelive.adapter;


import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.hyphenate.chat.EMMessage;
import com.duomizhibo.phonelive.bean.ChatListBean;
import com.duomizhibo.phonelive.bean.UserBean;
import com.duomizhibo.phonelive.widget.PhoneLiveChatRow;
import com.duomizhibo.phonelive.widget.PhoneLiveChatRowText;

import java.util.List;

public class ChatRoomAdapter extends BaseAdapter {
    private Context mContext;
    private List<EMMessage> mList;
    private ChatListBean mToUser;
    private UserBean mUser;

    public ChatRoomAdapter(Context context, List<EMMessage> list, UserBean user, ChatListBean toUser) {
        mContext = context;
        mList = list;
        mUser=user;
        mToUser=toUser;
    }

    public void addMessage(EMMessage message) {
        mList.add(message);
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public EMMessage getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        EMMessage e = mList.get(position);
        //convertView = createChatRow(mContext, e, position, mHead);
        ((PhoneLiveChatRow) convertView).setUpView(e, position);
        return convertView;
    }

    private View createChatRow(Context context, EMMessage e, int position, String head) {
        PhoneLiveChatRow chatRow = null;
        switch (e.getType()) {
            case TXT:
                chatRow = new PhoneLiveChatRowText(context, e, position, this, head);
                break;

        }
        return chatRow;
    }


}

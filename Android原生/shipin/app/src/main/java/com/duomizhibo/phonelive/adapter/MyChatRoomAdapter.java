package com.duomizhibo.phonelive.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMTextMessageBody;
import com.hyphenate.util.DateUtils;
import com.duomizhibo.phonelive.R;
import com.duomizhibo.phonelive.bean.UserBean;
import com.duomizhibo.phonelive.ui.customviews.CircleImageTransformation;

import java.util.Date;
import java.util.List;

/**
 * Created by cxf on 2017/7/1.
 */

public class MyChatRoomAdapter extends RecyclerView.Adapter {

    private final int LEFT = 0;
    private final int RIGHT = 1;
    private List<EMMessage> mList;
    private Context mContext;
    private UserBean mToUser;
    private UserBean mUser;
    private CircleImageTransformation mTransformation;
    private LayoutInflater mInflater;

    public MyChatRoomAdapter(Context context, List<EMMessage> list, UserBean toUser, UserBean user) {
        mList = list;
        mContext = context;
        mToUser = toUser;
        mUser = user;
        mTransformation = new CircleImageTransformation(mContext);
        mInflater = LayoutInflater.from(mContext);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v;
        if (viewType == LEFT) {
            v = mInflater.inflate(R.layout.item_message_left, parent, false);
        } else {
            v = mInflater.inflate(R.layout.item_message_right, parent, false);
        }
        return new Vh(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        EMMessage emMessage = mList.get(position);
        Vh vh = (Vh) holder;
        if (position > 0) {
            if (DateUtils.isCloseEnough(mList.get(position - 1).getMsgTime(), emMessage.getMsgTime())) {
                if (vh.timeStamp.getVisibility() == View.VISIBLE) {
                    vh.timeStamp.setVisibility(View.GONE);
                }
            } else {
                if (vh.timeStamp.getVisibility() == View.GONE) {
                    vh.timeStamp.setVisibility(View.VISIBLE);
                }
                vh.timeStamp.setText(DateUtils.getTimestampString(new Date(emMessage.getMsgTime())));
            }
        } else if (position == 0) {
            if (vh.timeStamp.getVisibility() == View.GONE) {
                vh.timeStamp.setVisibility(View.VISIBLE);
            }
            vh.timeStamp.setText(DateUtils.getTimestampString(new Date(emMessage.getMsgTime())));
        }

        if (emMessage.getFrom().equals(mUser.id)) {
            Glide.with(mContext).load(mUser.avatar).bitmapTransform(mTransformation).into(vh.headImg);
        } else {
            if ("1".equals(mToUser.id)) {
                Glide.with(mContext).load(R.mipmap.ic_launcher).into(vh.headImg);
            } else {
                Glide.with(mContext).load(mToUser.avatar).bitmapTransform(mTransformation).into(vh.headImg);
            }
        }
        vh.msg.setText(((EMTextMessageBody) emMessage.getBody()).getMessage());
    }

    @Override
    public int getItemViewType(int position) {
        if (mUser.id.equals(mList.get(position).getFrom())) {
            return RIGHT;
        } else {
            return LEFT;
        }
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    class Vh extends RecyclerView.ViewHolder {
        TextView timeStamp;
        TextView msg;
        ImageView headImg;

        public Vh(View itemView) {
            super(itemView);
            timeStamp = (TextView) itemView.findViewById(R.id.timestamp);
            msg = (TextView) itemView.findViewById(R.id.msg);
            headImg = (ImageView) itemView.findViewById(R.id.headImg);
        }
    }

}

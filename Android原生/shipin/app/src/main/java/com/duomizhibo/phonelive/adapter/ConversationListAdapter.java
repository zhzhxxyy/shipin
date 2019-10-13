package com.duomizhibo.phonelive.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.Transformation;
import com.duomizhibo.phonelive.R;
import com.duomizhibo.phonelive.bean.ChatListBean;
import com.duomizhibo.phonelive.fragment.MessageFragment;
import com.duomizhibo.phonelive.ui.customviews.CircleImageTransformation;
import com.duomizhibo.phonelive.utils.LiveUtils;

import java.util.List;

/**
 * Created by cxf on 2017/6/29.
 */

public class ConversationListAdapter extends RecyclerView.Adapter {

    private List<ChatListBean> mList;
    private Context mContext;
    private Transformation circleBitmapTransformation;//圆形头像
    private LayoutInflater mLayoutInflater;

    public ConversationListAdapter(Context context, List<ChatListBean> list) {
        mContext = context;
        mList = list;
        circleBitmapTransformation = new CircleImageTransformation(mContext);
        mLayoutInflater = LayoutInflater.from(mContext);
    }

    public void setList(List<ChatListBean> list) {
        mList = list;
    }

    public int insertItem(ChatListBean bean) {
        mList.add(bean);
        int position = mList.size() - 1;
        notifyItemInserted(position);
        if (mItemStateChangeListener != null) {
            mItemStateChangeListener.onInsertItem(bean);
        }
        return position;
    }

    public void removeItem(String uid) {
        for (int i = 0; i < mList.size(); i++) {
            ChatListBean bean = mList.get(i);
            if (uid.equals(bean.getId())) {
                mList.remove(i);
                notifyItemRemoved(i);
                if (mItemStateChangeListener != null) {
                    mItemStateChangeListener.onRemoveItem(bean);
                }
                break;
            }
        }
    }

    public void updateItem(ChatListBean bean, int unReadCount) {
        for (int i = 0; i < mList.size(); i++) {
            if (bean == mList.get(i)) {
                notifyItemChanged(i);
                if (mItemStateChangeListener != null) {
                    mItemStateChangeListener.onUpdateItem(bean, unReadCount);
                }
                break;
            }
        }

    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = mLayoutInflater.inflate(R.layout.item_private_chat, parent, false);
        return new Vh(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        Vh vh = (Vh) holder;
        vh.setData(mList.get(position), position);

    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }


    class Vh extends RecyclerView.ViewHolder {
        ImageView headImg;
        ImageView sex;
        ImageView anchorLevel;
        ImageView level;
        TextView name;
        TextView msg;
        TextView redPoint;
        ChatListBean mBean;
        int mPosition;

        public Vh(View itemView) {
            super(itemView);
            headImg = (ImageView) itemView.findViewById(R.id.headImg);
            sex = (ImageView) itemView.findViewById(R.id.sex);
            anchorLevel = (ImageView) itemView.findViewById(R.id.anchor_level);
            level = (ImageView) itemView.findViewById(R.id.level);
            name = (TextView) itemView.findViewById(R.id.name);
            redPoint = (TextView) itemView.findViewById(R.id.red_point);
            msg = (TextView) itemView.findViewById(R.id.msg);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mListener != null) {
                        mListener.OnItemClick(mBean, mPosition);
                    }
                }
            });
        }

        void setData(ChatListBean bean, int position) {
            mBean = bean;
            mPosition = position;
            Glide.with(mContext).load(bean.getAvatar()).bitmapTransform(circleBitmapTransformation).into(headImg);
            sex.setImageResource(LiveUtils.getSexRes(bean.getSex()));
            level.setImageResource(LiveUtils.getLevelRes(bean.getLevel()));
            anchorLevel.setImageResource(LiveUtils.getAnchorLevelRes(bean.getLevel()));
            name.setText(bean.getUser_nicename());
            msg.setText(bean.getLastMessage());
            int count = bean.getUnReadCount();
            if (count > 0 && !MessageFragment.ignoreUnReadMessage) {
                if (redPoint.getVisibility() == View.GONE) {
                    redPoint.setVisibility(View.VISIBLE);
                }
                redPoint.setText(count + "");
            } else {
                if (redPoint.getVisibility() == View.VISIBLE) {
                    redPoint.setVisibility(View.GONE);
                }
            }
        }
    }

    public interface OnItemClickListener {
        void OnItemClick(ChatListBean bean, int position);
    }

    private OnItemClickListener mListener;

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    public interface ItemStateChangeListener {
        void onInsertItem(ChatListBean bean);

        void onUpdateItem(ChatListBean bean, int unReadCount);

        void onRemoveItem(ChatListBean bean);
    }

    public ItemStateChangeListener mItemStateChangeListener;

    public void setItemStateChangeListener(ItemStateChangeListener itemStateChangeListener) {
        mItemStateChangeListener = itemStateChangeListener;
    }

}

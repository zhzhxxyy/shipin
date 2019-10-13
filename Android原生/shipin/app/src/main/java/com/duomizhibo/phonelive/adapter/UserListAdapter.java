package com.duomizhibo.phonelive.adapter;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.duomizhibo.phonelive.AppContext;
import com.duomizhibo.phonelive.bean.SimpleUserInfo;
import com.duomizhibo.phonelive.bean.UserBean;
import com.duomizhibo.phonelive.R;
import com.duomizhibo.phonelive.utils.LiveUtils;
import com.duomizhibo.phonelive.utils.StringUtils;
import com.duomizhibo.phonelive.widget.AvatarView;

import java.util.ArrayList;
import java.util.List;

/**
 * 用户列表adapter
 */
public class UserListAdapter extends RecyclerView.Adapter<UserListAdapter.ViewHolderUserList> {
    private List<SimpleUserInfo> mUsers = new ArrayList<SimpleUserInfo>();
    private LayoutInflater mLayoutInflater;
    private OnRecyclerViewItemClickListener mOnItemClickListener = null;
    public UserListAdapter(LayoutInflater layoutInflater) {
        this.mLayoutInflater = layoutInflater;
    }

    public void setUserList(List<SimpleUserInfo> users){
        mUsers = users;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolderUserList onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolderUserList holderUserList = new ViewHolderUserList(mLayoutInflater.inflate(R.layout.item_live_user_list,parent,false));

        return holderUserList;
    }

    @Override
    public void onBindViewHolder(final ViewHolderUserList holder, final int position) {
        final SimpleUserInfo u = mUsers.get(position);

        holder.mUhead.setAvatarUrl(u.avatar);
        holder.mLevel.setImageResource(LiveUtils.getLevelRes(u.level));


        //将数据保存在itemView的Tag中，以便点击时进行获取
        holder.mUhead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(null != mOnItemClickListener){
                    mOnItemClickListener.onItemClick(v,position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mUsers.size();
    }
    protected class ViewHolderUserList extends RecyclerView.ViewHolder {
        public AvatarView mUhead;
        public ImageView mLevel;

        public ViewHolderUserList(View itemView) {
            super(itemView);
            mUhead = (AvatarView) itemView.findViewById(R.id.av_userHead);
            mLevel = (ImageView) itemView.findViewById(R.id.item_live_user_list_level);
        }

    }
    public static interface OnRecyclerViewItemClickListener{
        void onItemClick(View view,int data);
    }
    public void setOnItemClickListener(OnRecyclerViewItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }
}

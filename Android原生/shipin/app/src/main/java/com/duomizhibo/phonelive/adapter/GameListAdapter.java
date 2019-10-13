package com.duomizhibo.phonelive.adapter;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.duomizhibo.phonelive.AppContext;
import com.duomizhibo.phonelive.bean.LiveJson;
import com.duomizhibo.phonelive.bean.SimpleUserInfo;
import com.duomizhibo.phonelive.bean.UserBean;
import com.duomizhibo.phonelive.R;
import com.duomizhibo.phonelive.utils.LiveUtils;
import com.duomizhibo.phonelive.utils.SimpleUtils;
import com.duomizhibo.phonelive.utils.StringUtils;
import com.duomizhibo.phonelive.widget.AvatarView;
import com.duomizhibo.phonelive.widget.BlackTextView;
import com.duomizhibo.phonelive.widget.LoadUrlImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * 用户列表adapter
 */
public class GameListAdapter extends BaseAdapter {
    private List<LiveJson> mUserList;
    private LayoutInflater inflater;
    public GameListAdapter(LayoutInflater inflater, List<LiveJson> mUserList) {
        this.mUserList = mUserList;
        this.inflater = inflater;
    }


    @Override
    public int getCount() {
        return mUserList.size();
    }

    @Override
    public Object getItem(int position) {
        return mUserList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        GameListAdapter.ViewHolder viewHolder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_live_game_list, null);
            viewHolder = new GameListAdapter.ViewHolder();
            viewHolder.mUserNick = (BlackTextView) convertView.findViewById(R.id.tv_live_nick);
            viewHolder.mUserLocal = (BlackTextView) convertView.findViewById(R.id.tv_live_local);
            viewHolder.mUserNums = (BlackTextView) convertView.findViewById(R.id.tv_live_usernum);
            viewHolder.mUserHead = (AvatarView) convertView.findViewById(R.id.iv_live_user_head);
            viewHolder.mUserPic = (LoadUrlImageView) convertView.findViewById(R.id.iv_live_user_pic);
            viewHolder.mRoomTitle = (BlackTextView) convertView.findViewById(R.id.tv_hot_room_title);
            viewHolder.mAnchorLevel = (ImageView) convertView.findViewById(R.id.iv_live_user_anchorlevel);
            convertView.setTag(viewHolder);
        }
        LiveJson live = mUserList.get(position);
        viewHolder = (GameListAdapter.ViewHolder) convertView.getTag();
        viewHolder.mUserNick.setText(live.user_nicename);
        viewHolder.mUserLocal.setText(live.city);
        viewHolder.mUserHead.setAvatarUrl(live.avatar_thumb);
        viewHolder.mUserNums.setText(live.nums+"在看");
        viewHolder.mAnchorLevel.setImageResource(LiveUtils.getAnchorLevelRes2(live.level_anchor));
        //用于平滑加载图片
        SimpleUtils.loadImageForView(AppContext.getInstance(), viewHolder.mUserPic, live.thumb, 0);

        if (!TextUtils.isEmpty(live.title)) {
            viewHolder.mRoomTitle.setVisibility(View.VISIBLE);
            viewHolder.mRoomTitle.setText(live.title);
        } else {
            viewHolder.mRoomTitle.setVisibility(View.GONE);
            viewHolder.mRoomTitle.setText("");
        }
        return convertView;
    }

    private class ViewHolder {
        public BlackTextView mUserNick, mUserLocal, mUserNums, mRoomTitle;
        public LoadUrlImageView mUserPic;
        public AvatarView mUserHead;
        public ImageView mAnchorLevel;
    }

}

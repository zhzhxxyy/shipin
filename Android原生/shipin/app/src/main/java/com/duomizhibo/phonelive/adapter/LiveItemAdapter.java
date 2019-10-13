package com.duomizhibo.phonelive.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.duomizhibo.phonelive.AppConfig;
import com.duomizhibo.phonelive.AppContext;
import com.duomizhibo.phonelive.R;
import com.duomizhibo.phonelive.bean.LiveAndVideoBean;
import com.duomizhibo.phonelive.bean.LiveJson;
import com.duomizhibo.phonelive.ui.VideoPlayerActivity;
import com.duomizhibo.phonelive.utils.LiveUtils;
import com.duomizhibo.phonelive.utils.SimpleUtils;
import com.duomizhibo.phonelive.widget.AvatarView;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by weilian on 2017/9/6.
 */

public class LiveItemAdapter extends RecyclerView.Adapter<LiveItemAdapter.Vh> {

    private List<LiveJson> mList;
    private Context mContext;
    private LayoutInflater mInflater;


    public LiveItemAdapter(List<LiveJson> mList, Context mContext) {
        this.mList = mList;
        this.mContext = mContext;
        mInflater = LayoutInflater.from(mContext);
    }

    public void setList(List<LiveJson> list) {
        mList = list;
        notifyDataSetChanged();
    }

    @Override
    public Vh onCreateViewHolder(ViewGroup parent, int viewType) {
        return new Vh(mInflater.inflate(R.layout.live_near_item, parent, false));
    }

    @Override
    public void onBindViewHolder(Vh vh, int position) {
        vh.setData(mList.get(position));
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    class Vh extends RecyclerView.ViewHolder {
        AvatarView urlImageView;
        TextView mName;
        LiveJson mJson;

        public Vh(View itemView) {
            super(itemView);
            urlImageView = (AvatarView) itemView.findViewById(R.id.li_head_view);
            mName = (TextView) itemView.findViewById(R.id.live_name);
            urlImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    VideoPlayerActivity.startVideoPlayerActivity(mContext, mJson);
                }
            });
        }

        public void setData(LiveJson json) {
            mJson = json;
            mName.setText(mJson.user_nicename);
            urlImageView.setAvatarUrl(mJson.avatar);
        }
    }
}

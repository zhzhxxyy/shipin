package com.duomizhibo.phonelive.adapter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.duomizhibo.phonelive.R;
import com.duomizhibo.phonelive.bean.ActiveBean;
import com.duomizhibo.phonelive.bean.LiveJson;
import com.duomizhibo.phonelive.bean.UserInfo;
import com.duomizhibo.phonelive.interf.OnItemClickListener;
import com.duomizhibo.phonelive.utils.DpOrSp2PxUtil;
import com.duomizhibo.phonelive.widget.AvatarView;
import com.duomizhibo.phonelive.widget.LoadUrlImageView;

import java.util.List;

/**
 * Created by weilian on 2017/9/6.
 */

public class NearAdapter extends RecyclerView.Adapter {
    private final int HEADER_TYPE = 0;
    private final int NORMAL_TYPE = 1;

    private Context mContext;
    private List<LiveJson> liveList;
    private List<ActiveBean> videoList;
    private LayoutInflater mInflater;
    private ImageView mNoLive;
    private HeadViewHolder mHeadViewHolder;
    private RecyclerView mRecyclerView;

    public NearAdapter(Context context, List<LiveJson> liveList, List<ActiveBean> videoList) {
        mContext = context;
        this.liveList = liveList;
        this.videoList = videoList;
        mInflater = LayoutInflater.from(mContext);
    }

    private OnItemClickListener<ActiveBean> mOnItemClickListener;

    public void setOnItemClickListener(OnItemClickListener<ActiveBean> onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    public void setData(List<LiveJson> liveList, List<ActiveBean> videoList) {
        this.liveList = liveList;
        this.videoList = videoList;
        notifyDataSetChanged();
        mHeadViewHolder.setData();
    }


    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return HEADER_TYPE;
        } else {
            return NORMAL_TYPE;
        }
    }

    public void insertList(List<ActiveBean> list) {
        int p = videoList.size() + 1;
        videoList.addAll(list);
        notifyItemRangeInserted(p, videoList.size());
        notifyItemRangeChanged(p, videoList.size());
        mRecyclerView.scrollToPosition(p);
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        this.mRecyclerView = recyclerView;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == HEADER_TYPE) {
            if (mHeadViewHolder == null) {
                mHeadViewHolder = new HeadViewHolder(mInflater.inflate(R.layout.view_live_near, parent, false));
            }
            return mHeadViewHolder;
        } else {
            View view = mInflater.inflate(R.layout.item_video_near, parent, false);
            return new ViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof ViewHolder) {
            ViewHolder vh = (ViewHolder) holder;
            vh.setData(videoList.get(position - 1));
        }
        View v = holder.itemView;
        RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) v.getLayoutParams();
        if (position % 2 == 0) {
            params.setMargins(DpOrSp2PxUtil.dp2pxConvertInt(mContext, 1f), 0, 0, 0);
        } else {
            params.setMargins(0, 0, DpOrSp2PxUtil.dp2pxConvertInt(mContext, 1f), 0);

        }
        v.setLayoutParams(params);
    }

    @Override
    public int getItemCount() {
        return videoList.size() + 1;
    }

    class HeadViewHolder extends RecyclerView.ViewHolder {
        RecyclerView mRecyclerView;
        LiveItemAdapter mAdapter;

        public HeadViewHolder(View itemView) {
            super(itemView);
            mRecyclerView = (RecyclerView) itemView.findViewById(R.id.lv_listView);
            mRecyclerView.setHasFixedSize(true);
            mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
            mNoLive = (ImageView) itemView.findViewById(R.id.iv_no_live);
            setData();
        }

        void setData() {
            if (mAdapter == null) {
                mAdapter = new LiveItemAdapter(liveList, mContext);
                mRecyclerView.setAdapter(mAdapter);
            } else {
                mAdapter.setList(liveList);
            }

            if (liveList.size() > 0) {
                if (mNoLive.getVisibility() == View.VISIBLE) {
                    mNoLive.setVisibility(View.GONE);
                }
            } else {
                if (mNoLive.getVisibility() == View.GONE) {
                    mNoLive.setVisibility(View.VISIBLE);
                }
            }
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView urlImageView;
        TextView mTitle;
        AvatarView mAva;
        TextView mName;
        TextView mDisTance;
        ActiveBean mBean;

        public ViewHolder(View itemView) {
            super(itemView);
            urlImageView = (ImageView) itemView.findViewById(R.id.img);
            mTitle = (TextView) itemView.findViewById(R.id.title);
            mAva = (AvatarView) itemView.findViewById(R.id.av_head);
            mName = (TextView) itemView.findViewById(R.id.tv_name);
            mDisTance = (TextView) itemView.findViewById(R.id.distance);

        }

        public void setData(ActiveBean item) {
            mBean = item;
            UserInfo u = item.getUserinfo();
            mName.setText(u.getUser_nicename());
            Glide.with(mContext).load(item.getThumb()).placeholder(R.drawable.bg_news_bottom).into(urlImageView);
            mDisTance.setText(item.getDistance());
            mTitle.setText(item.getTitle());
            mAva.setAvatarUrl(u.getAvatar());
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnItemClickListener != null) {
                        mOnItemClickListener.onItemClick(mBean);
                    }
                }
            });
        }
    }


}

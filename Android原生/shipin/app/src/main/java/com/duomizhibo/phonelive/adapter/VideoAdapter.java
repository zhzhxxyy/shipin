package com.duomizhibo.phonelive.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.duomizhibo.phonelive.AppContext;
import com.duomizhibo.phonelive.R;
import com.duomizhibo.phonelive.bean.ActiveBean;
import com.duomizhibo.phonelive.bean.UserInfo;
import com.duomizhibo.phonelive.ui.SmallVideoPlayerActivity;
import com.duomizhibo.phonelive.utils.DpOrSp2PxUtil;
import com.duomizhibo.phonelive.utils.StringUtils;
import com.duomizhibo.phonelive.widget.AvatarView;

import java.util.List;

/**
 * Created by weipeng on 2016/12/23.
 */

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.VH> {

    private List<ActiveBean> mUserList;
    private LayoutInflater mInflater;
    private Context mContext;
    private RecyclerView mRecyclerView;
    public ActiveBean bean;
    private int mMarginVal;
    private TextView mWeizhi;

    public VideoAdapter(Context context, List<ActiveBean> userList) {
        mContext = context;
        mUserList = userList;
        mInflater = LayoutInflater.from(mContext);
        mMarginVal = DpOrSp2PxUtil.dp2pxConvertInt(mContext, 1f);
    }

    public void insertList(List<ActiveBean> list) {
        int p = mUserList.size();
        mUserList.addAll(list);
        notifyItemRangeInserted(p, list.size());
        notifyItemRangeChanged(p, list.size());
        mRecyclerView.scrollToPosition(p);
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        mRecyclerView = recyclerView;
    }

    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        return new VH(mInflater.inflate(R.layout.item_video_user, parent, false));
    }

    public void setData(List<ActiveBean> mUserList) {
        this.mUserList = mUserList;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(VH holder, int position) {
        holder.showData(mUserList.get(position), position);
        View v = holder.itemView;
        RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) v.getLayoutParams();
        if (position % 2 == 0) {
            params.setMargins(0, 0, mMarginVal, 0);
        } else {
            params.setMargins(mMarginVal, 0, 0, 0);

        }
        v.setLayoutParams(params);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return mUserList.size();
    }

    public class VH extends RecyclerView.ViewHolder {
        ImageView iv_bg;
        TextView laudNum, title;
        AvatarView mAvHead;
        TextView name;
        int mPosition;

        public VH(View itemView) {
            super(itemView);
            iv_bg = (ImageView) itemView.findViewById(R.id.cover);
            mAvHead = (AvatarView) itemView.findViewById(R.id.item_tv_head);
            laudNum = (TextView) itemView.findViewById(R.id.tv_laud_num);
            title = (TextView) itemView.findViewById(R.id.tv_title);
            name = (TextView) itemView.findViewById(R.id.name);
            mWeizhi = (TextView) itemView.findViewById(R.id.tv_weizhi);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (AppContext.getInstance().getLoginUid() == null || StringUtils.toInt(AppContext.getInstance().getLoginUid()) == 0) {
                        Toast.makeText(mContext, "请登录..", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    SmallVideoPlayerActivity.startSmallVideoPlayerActivity(mContext, mUserList.get(mPosition));

                }
            });
        }

        public void showData(ActiveBean bean, int position) {
            UserInfo u = bean.getUserinfo();
            mPosition = position;
            Glide.with(mContext).load(bean.getThumb()).placeholder(R.drawable.bg_news_bottom).into(iv_bg);
            mAvHead.setAvatarUrl(u.getAvatar());
            laudNum.setText(bean.getComments());
            title.setText(bean.getTitle());
            name.setText(bean.getUserinfo().getUser_nicename());
            mWeizhi.setText(bean.getUserinfo().getCity());
        }
    }
}

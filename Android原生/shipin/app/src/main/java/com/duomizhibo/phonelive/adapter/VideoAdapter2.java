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

import java.util.List;

/**
 * Created by weilian on 2017/9/8.
 */

public class VideoAdapter2 extends RecyclerView.Adapter<VideoAdapter2.Vh> {

    private List<ActiveBean> mUserList;
    private LayoutInflater mInflater;
    private Context mContext;
    private RecyclerView mRecyclerView;

    public VideoAdapter2(Context context, List<ActiveBean> userList) {
        mContext = context;
        mUserList = userList;
        mInflater = LayoutInflater.from(mContext);
    }

    public void clear() {
        mUserList.clear();
        notifyDataSetChanged();
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


    public void setData(List<ActiveBean> mUserList) {
        this.mUserList = mUserList;
        notifyDataSetChanged();
    }

    @Override
    public Vh onCreateViewHolder(ViewGroup parent, int viewType) {
        return new Vh(mInflater.inflate(R.layout.items_video_user2, parent, false));
    }

    @Override
    public void onBindViewHolder(Vh holder, int position) {
        holder.showData(mUserList.get(position));
        View v = holder.itemView;
        RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) v.getLayoutParams();
        if (position % 2 == 0) {
            params.setMargins(0, 0, DpOrSp2PxUtil.dp2pxConvertInt(mContext, 1f), 0);
        } else {
            params.setMargins(DpOrSp2PxUtil.dp2pxConvertInt(mContext, 1f), 0, 0, 0);

        }
        v.setLayoutParams(params);
    }

    @Override
    public int getItemCount() {
        return mUserList.size();
    }

    class Vh extends RecyclerView.ViewHolder {
        ImageView iv_bg;
        TextView laudNum, title, mTime;
        ActiveBean mBean;

        public Vh(View itemView) {
            super(itemView);
            iv_bg = (ImageView) itemView.findViewById(R.id.cover);
            mTime = (TextView) itemView.findViewById(R.id.tv_time);
            laudNum = (TextView) itemView.findViewById(R.id.tv_laud_num);
            title = (TextView) itemView.findViewById(R.id.tv_title);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (AppContext.getInstance().getLoginUid() == null || StringUtils.toInt(AppContext.getInstance().getLoginUid()) == 0) {
                        Toast.makeText(mContext, "请登录..", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    SmallVideoPlayerActivity.startSmallVideoPlayerActivity(mContext, mBean);
                }
            });
        }

        public void showData(ActiveBean bean) {
            mBean=bean;
            UserInfo u = bean.getUserinfo();
            Glide.with(mContext).load(bean.getThumb()).placeholder(R.drawable.bg_news_bottom).into(iv_bg);
            mTime.setText(bean.getDatetime() + "." + u.getCity());
            laudNum.setText(bean.getComments());
            title.setText(bean.getTitle());

        }
    }
}

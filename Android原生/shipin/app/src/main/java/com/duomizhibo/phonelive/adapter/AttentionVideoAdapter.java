package com.duomizhibo.phonelive.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.duomizhibo.phonelive.R;
import com.duomizhibo.phonelive.bean.ActiveBean;
import com.duomizhibo.phonelive.bean.UserInfo;
import com.duomizhibo.phonelive.ui.SmallVideoPlayerActivity;
import com.duomizhibo.phonelive.utils.DpOrSp2PxUtil;
import com.duomizhibo.phonelive.widget.AvatarView;

import java.util.List;

/**
 * Created by cxf on 2017/9/7.
 */

public class AttentionVideoAdapter extends RecyclerView.Adapter<AttentionVideoAdapter.Vh> {

    private Context mContext;
    private List<ActiveBean> mList;
    private LayoutInflater mInflater;
   private  RecyclerView mRecyclerView;
    public AttentionVideoAdapter(Context context, List<ActiveBean> list) {
        mContext = context;
        mList = list;
        mInflater = LayoutInflater.from(mContext);
    }

    public void setList(List<ActiveBean> list) {
        mList = list;
        notifyDataSetChanged();
    }

    public void insertList(List<ActiveBean> list) {
        int p = mList.size();
        mList.addAll(list);
        notifyItemRangeInserted(p, list.size());
        notifyItemRangeChanged(p, list.size());
        mRecyclerView.scrollToPosition(p);
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        mRecyclerView=recyclerView;
    }

    @Override
    public Vh onCreateViewHolder(ViewGroup parent, int viewType) {
        return new Vh(mInflater.inflate(R.layout.item_list_attention_video, parent, false));

    }

    @Override
    public void onBindViewHolder(Vh vh, int position) {
        vh.setData(mList.get(position));
        View v = vh.itemView;
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
        return mList.size();
    }

    class Vh extends RecyclerView.ViewHolder {
        ImageView img;
        TextView title;
        AvatarView head;
        TextView name;
        TextView time;
        ActiveBean mBean;

        public Vh(View itemView) {
            super(itemView);
            img = (ImageView) itemView.findViewById(R.id.img);
            title = (TextView) itemView.findViewById(R.id.title);
            head = (AvatarView) itemView.findViewById(R.id.head);
            name = (TextView) itemView.findViewById(R.id.name);
            time = (TextView) itemView.findViewById(R.id.time);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SmallVideoPlayerActivity.startSmallVideoPlayerActivity(mContext, mBean);
                }
            });
        }

        void setData(ActiveBean bean) {
            mBean = bean;
            UserInfo u = bean.getUserinfo();
            Glide.with(mContext).load(bean.getThumb()).into(img);
            title.setText(bean.getTitle());
            name.setText(u.getUser_nicename());
            head.setAvatarUrl(u.getAvatar());
            time.setText(bean.getDatetime());
        }
    }
}

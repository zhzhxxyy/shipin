package com.duomizhibo.phonelive.adapter.video;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.duomizhibo.phonelive.R;
import com.duomizhibo.phonelive.bean.video.VideoObject;
import com.duomizhibo.phonelive.utils.DpOrSp2PxUtil;
import com.duomizhibo.phonelive.utils.UIHelper;

import java.util.List;

/**
 * @author Smile Wei MyVideoAdapter
 * @since 2017/03/01.
 */

public class MyVideoCollectAdapter extends RecyclerView.Adapter<MyVideoCollectAdapter.VH> {

    private List<VideoObject> mUserList;
    private LayoutInflater mInflater;
    private Context mContext;
    private RecyclerView mRecyclerView;
    private int mMarginVal;

    public MyVideoCollectAdapter(Context context, List<VideoObject> userList) {
        mContext = context;
        mUserList = userList;
        mInflater = LayoutInflater.from(mContext);
        mMarginVal = DpOrSp2PxUtil.dp2pxConvertInt(mContext, 1f);
    }

    public void insertList(List<VideoObject> list) {
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
        return new VH(mInflater.inflate(R.layout.adapter_home_video_item, parent, false));
    }

    public void setData(List<VideoObject> mUserList) {
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
        ImageView ivHome;
        TextView tvTitle;
        TextView tv_bofang;
        TextView tv_jinbi;
        TextView tv_zan;
        TextView tv_time;
        int mPosition;

        public VH(View itemView) {
            super(itemView);
            ivHome=(ImageView)itemView.findViewById(R.id.iv_home);
            tvTitle=(TextView)itemView.findViewById(R.id.tv_title);
            tv_bofang=(TextView)itemView.findViewById(R.id.tv_bofang);
            tv_jinbi=(TextView)itemView.findViewById(R.id.tv_jinbi);
            tv_zan=(TextView)itemView.findViewById(R.id.tv_zan);
            tv_time=(TextView)itemView.findViewById(R.id.tv_time);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    UIHelper.showVideoPlayerNew(mContext,mUserList.get(mPosition));

                }
            });
        }

        public void showData(VideoObject bean, int position) {
            mPosition = position;
            Glide.with(mContext).load(bean.getThumbnail()).placeholder(R.drawable.bg_news_bottom).into(ivHome);
            tvTitle.setText(bean.getTitle());
            tv_bofang.setText(""+bean.getClick());
            tv_jinbi.setText(""+bean.getGold());
            tv_zan.setText(""+bean.getGood());
            tv_time.setText(""+bean.getPlay_time());


        }

    }
}
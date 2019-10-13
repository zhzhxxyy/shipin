package com.duomizhibo.phonelive.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.duomizhibo.phonelive.AppContext;
import com.duomizhibo.phonelive.R;
import com.duomizhibo.phonelive.bean.LiveJson;
import com.duomizhibo.phonelive.interf.OnItemClickListener;
import com.duomizhibo.phonelive.utils.LiveUtils;
import com.duomizhibo.phonelive.utils.SimpleUtils;
import com.duomizhibo.phonelive.utils.TDevice;
import com.duomizhibo.phonelive.widget.RoundImageView;

import java.util.List;

//热门主播
public class PersontopersonAdapter extends RecyclerView.Adapter {
    private List<LiveJson> mUserList;
    private LayoutInflater inflater;
    RecyclerView mRecyclerView;
    int width= (int) ((TDevice.getScreenWidth()- TDevice.dpToPixel(20))/2);
    int margin= (int) TDevice.dpToPixel(2);

    public PersontopersonAdapter(LayoutInflater inflater, List<LiveJson> mUserList) {
        this.mUserList = mUserList;
        this.inflater = inflater;
    }

    public void insertList(List<LiveJson> list) {
        int p = mUserList.size();
        mUserList.addAll(list);
        notifyItemRangeInserted(p , list.size());
        notifyItemRangeChanged(p , list.size());
        mRecyclerView.scrollToPosition(p);
    }
    public void setData(List<LiveJson> itemList) {
        mUserList = itemList;
    }
    private OnItemClickListener<LiveJson> mOnItemClickListener;

    public void setOnItemClickListener(OnItemClickListener<LiveJson> onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        mRecyclerView = recyclerView;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_persontoperson, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof ViewHolder) {
            ViewHolder viewHolder = (ViewHolder) holder;
            {
                final LiveJson live = mUserList.get(position);
                viewHolder.iv_level.setImageResource(LiveUtils.getAnchorLevelRes(live.level_anchor));
                RelativeLayout.LayoutParams layoutParams=new RelativeLayout.LayoutParams(width,width);
                layoutParams.setMargins(margin,margin,margin,margin);
                viewHolder.rv_head.setLayoutParams(layoutParams);
                //用于平滑加载图片
                if (!live.avatar.equals("")){
                    SimpleUtils.loadImageForView(AppContext.getInstance(), viewHolder.rv_head, live.thumb, 0);
                }
                if (live.is_communicating.equals("0")){
                    viewHolder.iv_islive.setBackgroundResource(R.drawable.circle_live);

                }else {
                    viewHolder.iv_islive.setBackgroundResource(R.drawable.circle_living);
                }
                viewHolder.tv_name.setText(live.user_nicename);
                viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mOnItemClickListener != null) {
                            mOnItemClickListener.onItemClick(live);
                        }
                    }
                });
            }

        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return mUserList.size();
    }

    private class ViewHolder extends RecyclerView.ViewHolder{
        public TextView tv_name;
        public RoundImageView rv_head;
        public ImageView iv_level,iv_islive;

        public ViewHolder(View itemView) {
            super(itemView);
            rv_head = (RoundImageView) itemView.findViewById(R.id.rv_head);
            iv_islive = (ImageView) itemView.findViewById(R.id.iv_islive);
            tv_name = (TextView) itemView.findViewById(R.id.tv_name);
            iv_level = (ImageView) itemView.findViewById(R.id.iv_level);
        }
    }
}



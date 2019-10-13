package com.duomizhibo.phonelive.adapter.video;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.duomizhibo.phonelive.R;
import com.duomizhibo.phonelive.bean.video.RecordPay;
import com.duomizhibo.phonelive.bean.video.RecordVideo;
import com.duomizhibo.phonelive.bean.video.VideoObject;
import com.duomizhibo.phonelive.utils.DpOrSp2PxUtil;
import com.duomizhibo.phonelive.utils.UIHelper;
import com.duomizhibo.phonelive.widget.FontIconView;

import java.util.List;


public class RecordVideoAdapter extends RecyclerView.Adapter<RecordVideoAdapter.VH> {

    private List<RecordVideo> mUserList;
    private LayoutInflater mInflater;
    private Context mContext;
    private RecyclerView mRecyclerView;


    public RecordVideoAdapter(Context context, List<RecordVideo> userList) {
        mContext = context;
        mUserList = userList;
        mInflater = LayoutInflater.from(mContext);
    }

    public void insertList(List<RecordVideo> list) {
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
        return new VH(mInflater.inflate(R.layout.adapter_record_video_item, parent, false));
    }

    public void setData(List<RecordVideo> mUserList) {
        this.mUserList = mUserList;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(VH holder, int position) {
        holder.showData(mUserList.get(position), position);
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

        View ll_title;
        TextView tv_title;
        TextView tv_money;
        View ll_detail;
        TextView tv_detail_title;
        TextView tv_detail_time;
        TextView tv_detail_class;
        TextView tv_detail_ip;
        int mPosition;

        public VH(View itemView) {
            super(itemView);
            ll_title=itemView.findViewById(R.id.ll_title);
            tv_title=(TextView)itemView.findViewById(R.id.tv_title);
            tv_money=(TextView)itemView.findViewById(R.id.tv_money);
            ll_detail=itemView.findViewById(R.id.ll_detail);
            tv_detail_title=(TextView)itemView.findViewById(R.id.tv_detail_title);
            tv_detail_time=(TextView)itemView.findViewById(R.id.tv_detail_time);
            tv_detail_class=(TextView)itemView.findViewById(R.id.tv_detail_class);
            tv_detail_ip=(TextView)itemView.findViewById(R.id.tv_detail_ip);
            ll_title.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    final RecordVideo bean=  mUserList.get(mPosition);
                    bean.setOpen(!bean.isOpen());
                    notifyDataSetChanged();
                }
            });
            tv_detail_title.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    RecordVideo bean=  mUserList.get(mPosition);
                    VideoObject item=new VideoObject();
                    item.setId(bean.getVideo_id());
                    item.setTitle(bean.getTitle());
                    UIHelper.showVideoPlayerNew(mContext,item);
                }
            });
        }

        public void showData(RecordVideo bean, int position) {
            mPosition = position;
            tv_title.setText(bean.getTitle()+"");
            tv_money.setText(bean.getGold()+"金币");
            tv_detail_title.setText(bean.getTitle()+"");
            tv_detail_time.setText(bean.getViewTimeString()+"");
            tv_detail_class.setText(bean.getName()+"");
            tv_detail_ip.setText(bean.getUser_ip()+"");
            if(bean.isOpen()){
                ll_detail.setVisibility(View.VISIBLE);
            }else{
                ll_detail.setVisibility(View.GONE);
            }
        }

    }
}
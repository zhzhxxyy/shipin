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
import com.duomizhibo.phonelive.utils.DpOrSp2PxUtil;
import com.duomizhibo.phonelive.widget.FontIconView;

import java.util.List;


public class RecordPayAdapter extends RecyclerView.Adapter<RecordPayAdapter.VH> {

    private List<RecordPay> mUserList;
    private LayoutInflater mInflater;
    private Context mContext;
    private RecyclerView mRecyclerView;
    private int mMarginVal;


    public RecordPayAdapter(Context context, List<RecordPay> userList) {
        mContext = context;
        mUserList = userList;
        mInflater = LayoutInflater.from(mContext);
        mMarginVal = DpOrSp2PxUtil.dp2pxConvertInt(mContext, 1f);
    }

    public void insertList(List<RecordPay> list) {
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
        return new VH(mInflater.inflate(R.layout.adapter_record_pay_item, parent, false));
    }

    public void setData(List<RecordPay> mUserList) {
        this.mUserList = mUserList;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(VH holder, int position) {
        holder.showData(mUserList.get(position), position);
//        View v = holder.itemView;
//        RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) v.getLayoutParams();
//        if (position % 2 == 0) {
//            params.setMargins(0, 0, mMarginVal, 0);
//        } else {
//            params.setMargins(mMarginVal, 0, 0, 0);
//
//        }
//        v.setLayoutParams(params);
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

        TextView tv_order;
        TextView tv_money;
        TextView tv_info;
        TextView tv_time;
        FontIconView fi_payment;
        ImageView iv_pay_status;
        int mPosition;

        public VH(View itemView) {
            super(itemView);
            iv_pay_status=(ImageView)itemView.findViewById(R.id.iv_pay_status);
            tv_order=(TextView)itemView.findViewById(R.id.tv_order);
            tv_money=(TextView)itemView.findViewById(R.id.tv_money);
            tv_info=(TextView)itemView.findViewById(R.id.tv_info);
            fi_payment=(FontIconView)itemView.findViewById(R.id.fi_payment);
            tv_time=(TextView)itemView.findViewById(R.id.tv_time);
//            itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//
////                    UIHelper.showVideoPlayerNew(mContext,mUserList.get(mPosition));
//
//                }
//            });
        }

        public void showData(RecordPay bean, int position) {
            mPosition = position;
            tv_order.setText("订单号："+bean.getOrder_sn());
            tv_money.setText("¥ "+bean.getPrice());
            if(bean.getBuy_type()==1){
                tv_info.setText("充值：金币"+bean.getBuy_glod_num()+"个");
            }else{
                tv_info.setText("充值："+bean.getBuy_vip_info().getName()+"");
            }
            tv_time.setText(bean.getAddTimeString());
            if("aliPay".equals(bean.getPay_channel())){
                fi_payment.setText(mContext.getResources().getText(R.string.icon_zhifubao));
                fi_payment.setTextColor(Color.parseColor("#1E90FF"));
                //1E90FF
            }else if("wxPay".equals(bean.getPay_channel())){
                //32CD32
                fi_payment.setText(mContext.getResources().getText(R.string.icon_weixin));
                fi_payment.setTextColor(Color.parseColor("#32CD32"));
            }else if("qqPay".equals(bean.getPay_channel())){
                //ff0000
                fi_payment.setText(mContext.getResources().getText(R.string.icon_qq));
                fi_payment.setTextColor(Color.parseColor("#ff0000"));
            }else{
                fi_payment.setText("");
            }

            if(bean.getStatus()==1){
                iv_pay_status.setImageResource(R.drawable.yes_pay);
            }else{
                iv_pay_status.setImageResource(R.drawable.not_pay);
            }

        }

    }
}
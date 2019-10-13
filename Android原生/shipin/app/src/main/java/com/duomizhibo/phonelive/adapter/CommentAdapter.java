package com.duomizhibo.phonelive.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.Transformation;
import com.duomizhibo.phonelive.AppContext;
import com.duomizhibo.phonelive.R;
import com.duomizhibo.phonelive.api.remote.PhoneLiveApi;
import com.duomizhibo.phonelive.bean.CommentBean;
import com.duomizhibo.phonelive.bean.UserInfo;
import com.duomizhibo.phonelive.ui.customviews.CircleImageTransformation;
import com.duomizhibo.phonelive.widget.AvatarView;
import com.zhy.http.okhttp.callback.StringCallback;
import com.zhy.http.okhttp.utils.L;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import okhttp3.Call;

/**
 * Created by cxf on 2017/7/14.
 */

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.Vh> {

    private Context mContext;
    private List<CommentBean> mList;
    private LayoutInflater mInflater;
    private Transformation mTransformation;
    private OnItemClickListener mOnItemClickListener;
    private RecyclerView mRecyclerView;
    private String mUid;

    public CommentAdapter(Context context, List<CommentBean> list) {
        mContext = context;
        mList = list;
        mInflater = LayoutInflater.from(mContext);
        mTransformation = new CircleImageTransformation(mContext);
        mUid=AppContext.getInstance().getLoginUid();
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    public void refreshList(List<CommentBean> list) {
        mList = list;
        notifyDataSetChanged();
    }

    public void insertList(List<CommentBean> list) {
        int p = mList.size();
        mList.addAll(list);
        notifyItemRangeInserted(p, list.size());
        notifyItemRangeChanged(p, list.size());
        mRecyclerView.scrollToPosition(p);
    }

    public List<CommentBean> getList() {
        return mList;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        mRecyclerView = recyclerView;
    }

    @Override
    public Vh onCreateViewHolder(ViewGroup parent, int viewType) {
        return new Vh(mInflater.inflate(R.layout.item_video_comment, parent, false));
    }

    @Override
    public void onBindViewHolder(Vh vh, int position) {
        vh.showData(mList.get(position), position);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }


    class Vh extends RecyclerView.ViewHolder {
        int mPosition;
        ImageView head_img;
        TextView name;
        TextView time;
        TextView content;
        TextView likeNum;
        View more;
        TextView moreReply;
        CommentBean mBean;
        ImageView heart;

        public Vh(View itemView) {
            super(itemView);
            head_img = (ImageView) itemView.findViewById(R.id.head_img);
            name = (TextView) itemView.findViewById(R.id.name);
            time = (TextView) itemView.findViewById(R.id.time);
            content = (TextView) itemView.findViewById(R.id.content);
            likeNum = (TextView) itemView.findViewById(R.id.like_num);
            more = itemView.findViewById(R.id.more);
            moreReply = (TextView) itemView.findViewById(R.id.more_reply);
            heart = (ImageView) itemView.findViewById(R.id.heart);
            more.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnItemClickListener != null) {
                        mOnItemClickListener.OnMoreClick(mBean,mPosition);
                    }
                }
            });
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnItemClickListener != null) {
                        mOnItemClickListener.OnItemClick(mBean, mPosition);
                    }
                }
            });
            heart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mUid.equals(mBean.getUid())) {
                        AppContext.toast("不能给自己的评论点赞");
                        return;
                    }
                    if (0 == mBean.getIslike()) {
                        PhoneLiveApi.addCommentLike(mBean.getId(), new StringCallback() {
                            @Override
                            public void onError(Call call, Exception e, int id) {

                            }

                            @Override
                            public void onResponse(String response, int id) {
                                try {
                                    JSONObject obj = new JSONObject(response);
                                    if ("200".equals(obj.getString("ret"))) {
                                        JSONObject data = obj.getJSONObject("data");
                                        if (0 == data.getInt("code")) {
                                            JSONObject info0 = data.getJSONArray("info").getJSONObject(0);
                                            mBean.setIslike(info0.getInt("islike"));
                                            mBean.setLikes(info0.getString("likes"));
                                            notifyItemChanged(mPosition);
                                        }
                                        AppContext.toast(data.getString("msg"));
                                    } else {
                                        AppContext.toast("点赞失败");
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    }
                }
            });
        }

        public void showData(CommentBean bean, int position) {
            mBean = bean;
            mPosition = position;
            UserInfo u = bean.getUserinfo();
            Glide.with(mContext).load(u.getAvatar()).bitmapTransform(mTransformation).into(head_img);
            name.setText(u.getUser_nicename());
            time.setText(bean.getDatetime());
            if (!"0".equals(bean.getLikes())) {
                likeNum.setText(bean.getLikes());
            }else{
                likeNum.setText("赞");
            }
            content.setText(bean.getContent());
            if (bean.getReplys() > 0) {
                if (more.getVisibility() == View.GONE) {
                    more.setVisibility(View.VISIBLE);
                }
                moreReply.setText("查看全部" + mBean.getReplys() + "条回复");
            } else {
                if (more.getVisibility() == View.VISIBLE) {
                    more.setVisibility(View.GONE);
                }
            }
            if(bean.getIslike()==1){
                heart.setImageResource(R.drawable.icon_video_red_heart);
            }else{
                heart.setImageResource(R.drawable.icon_video_heart);
            }
        }

    }

    public interface OnItemClickListener {
        void OnItemClick(CommentBean bean, int position);

        void OnMoreClick(CommentBean bean,int position);
    }
}

package com.duomizhibo.phonelive.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.Transformation;
import com.duomizhibo.phonelive.AppContext;
import com.duomizhibo.phonelive.R;
import com.duomizhibo.phonelive.api.remote.PhoneLiveApi;
import com.duomizhibo.phonelive.bean.CommentBean;
import com.duomizhibo.phonelive.bean.UserInfo;
import com.duomizhibo.phonelive.event.DianzanEvent;
import com.duomizhibo.phonelive.interf.OnItemClickListener;
import com.duomizhibo.phonelive.ui.customviews.CircleImageTransformation;
import com.zhy.http.okhttp.callback.StringCallback;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import okhttp3.Call;

/**
 * Created by cxf on 2017/9/7.
 */

public class CommentReplyAdapter extends RecyclerView.Adapter {

    private Context mContext;
    private List<CommentBean> mList;
    private CommentBean mHostBean;
    private LayoutInflater mInflater;
    private Transformation mTransformation;
    private HeadVh mHeadVh;
    private OnItemClickListener<CommentBean> mOnItemClickListener;
    private final int HEAD = 0;
    private final int NORMAL = 1;
    private RecyclerView mRecyclerView;
    private String mUid;
    private int mCommentPosition;

    public CommentReplyAdapter(Context context, CommentBean hostBean, List<CommentBean> list, int commentPosition) {
        mContext = context;
        mHostBean = hostBean;
        mList = list;
        mInflater = LayoutInflater.from(mContext);
        mTransformation = new CircleImageTransformation(mContext);
        mUid = AppContext.getInstance().getLoginUid();
        mCommentPosition = commentPosition;
    }

    public void setOnItemClickListener(OnItemClickListener<CommentBean> onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    public void refreshList(List<CommentBean> list) {
        mList = list;
        notifyDataSetChanged();
    }

    public void setReplyCount(String count) {
        mHeadVh.setReplyCount(count);
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        mRecyclerView = recyclerView;
    }

    public void insertList(List<CommentBean> list) {
        int p = mList.size() + 1;
        mList.addAll(list);
        notifyItemRangeInserted(p, list.size());
        notifyItemRangeChanged(p, list.size());
        mRecyclerView.scrollToPosition(p);
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return HEAD;
        }
        return NORMAL;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == HEAD) {
            if (mHeadVh == null) {
                mHeadVh = new HeadVh(mInflater.inflate(R.layout.view_reply_head, parent, false));
                mHeadVh.setReplyCount(mHostBean.getReplys() + "");
                mHeadVh.setIsRecyclable(false);
            }
            return mHeadVh;
        }
        return new Vh(mInflater.inflate(R.layout.view_comment_normal, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof Vh) {
            ((Vh) holder).setData(mList.get(position - 1), position);
        }
    }


    @Override
    public int getItemCount() {
        return mList.size() + 1;
    }

    class HeadVh extends RecyclerView.ViewHolder {
        ImageView head;
        TextView name;
        TextView time;
        TextView likes;
        TextView content;
        TextView mReplyCount;
        ImageView heart;

        public HeadVh(View itemView) {
            super(itemView);
            head = (ImageView) itemView.findViewById(R.id.head_img);
            name = (TextView) itemView.findViewById(R.id.name);
            time = (TextView) itemView.findViewById(R.id.time);
            likes = (TextView) itemView.findViewById(R.id.like_num);
            content = (TextView) itemView.findViewById(R.id.content);
            mReplyCount = (TextView) itemView.findViewById(R.id.reply_count);
            heart = (ImageView) itemView.findViewById(R.id.heart);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnItemClickListener != null) {
                        mOnItemClickListener.onItemClick(mHostBean);
                    }
                }
            });
            heart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mUid.equals(mHostBean.getUid())) {
                        AppContext.toast("不能给自己的评论点赞");
                        return;
                    }
                    if (0 == mHostBean.getIslike()) {
                        PhoneLiveApi.addCommentLike(mHostBean.getId(), new StringCallback() {
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
                                            int islike = info0.getInt("islike");
                                            String likes = info0.getString("likes");
                                            mHostBean.setIslike(islike);
                                            mHostBean.setLikes(likes);
                                            EventBus.getDefault().post(new DianzanEvent(mCommentPosition, likes, islike));
                                            setData();
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
            setData();
        }

        void setData() {
            UserInfo u = mHostBean.getUserinfo();
            Glide.with(mContext).load(u.getAvatar()).bitmapTransform(mTransformation).into(head);
            name.setText(u.getUser_nicename());
            time.setText(mHostBean.getDatetime());
            content.setText(mHostBean.getContent());
            if (!"0".equals(mHostBean.getLikes())) {
                likes.setText(mHostBean.getLikes());
            } else {
                likes.setText("赞");
            }
            if (mHostBean.getIslike() == 1) {
                heart.setImageResource(R.drawable.icon_video_red_heart);
            } else {
                heart.setImageResource(R.drawable.icon_video_heart);
            }
        }

        void setReplyCount(String replyCount) {
            mReplyCount.setText("全部回复(" + replyCount + ")");
        }
    }

    class Vh extends RecyclerView.ViewHolder {
        ImageView head;
        TextView name;
        TextView time;
        TextView content;
        TextView question;
        CommentBean mBean;
        TextView likes;
        ImageView heart;
        int mPosition;

        public Vh(View itemView) {
            super(itemView);
            head = (ImageView) itemView.findViewById(R.id.head_img);
            name = (TextView) itemView.findViewById(R.id.name);
            time = (TextView) itemView.findViewById(R.id.time);
            content = (TextView) itemView.findViewById(R.id.content);
            question = (TextView) itemView.findViewById(R.id.question);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnItemClickListener != null) {
                        mOnItemClickListener.onItemClick(mBean);
                    }
                }
            });
            likes = (TextView) itemView.findViewById(R.id.like_num);
            heart = (ImageView) itemView.findViewById(R.id.heart);
            heart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mUid.equals(mBean.getUid())) {
                        AppContext.toast("不能给自己的回复点赞");
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

        void setData(CommentBean bean, int position) {
            mBean = bean;
            mPosition = position;
            UserInfo u = bean.getUserinfo();
            Glide.with(mContext).load(u.getAvatar()).bitmapTransform(mTransformation).into(head);
            name.setText(u.getUser_nicename());
            time.setText(bean.getDatetime());
            UserInfo tou = bean.getTouserinfo();
            if (tou.getId() != null) {
                if (question.getVisibility() == View.GONE) {
                    question.setVisibility(View.VISIBLE);
                }
                String toName = "<font color='#3399ee'>" + tou.getUser_nicename() + "</font>";
                content.setText(Html.fromHtml("回复 " + toName + "：" + mBean.getContent()));
                question.setText(Html.fromHtml(toName + "：" + bean.getTocommentinfo().getContent()));
            } else {
                content.setText(mBean.getContent());
                if (question.getVisibility() == View.VISIBLE) {
                    question.setVisibility(View.GONE);
                }
            }
            if (!"0".equals(bean.getLikes())) {
                likes.setText(bean.getLikes());
            }
            if (bean.getIslike() == 1) {
                heart.setImageResource(R.drawable.icon_video_red_heart);
            }
        }
    }
}

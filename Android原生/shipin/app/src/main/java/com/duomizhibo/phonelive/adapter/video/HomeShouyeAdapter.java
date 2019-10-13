package com.duomizhibo.phonelive.adapter.video;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.duomizhibo.phonelive.R;
import com.duomizhibo.phonelive.base.Type;
import com.duomizhibo.phonelive.bean.video.BannerObject;
import com.duomizhibo.phonelive.bean.video.NoticeObject;
import com.duomizhibo.phonelive.bean.video.VideoObject;
import com.duomizhibo.phonelive.utils.SimpleUtils;
import com.duomizhibo.phonelive.utils.UIHelper;
import com.duomizhibo.phonelive.widget.FooterLoading;
import com.duomizhibo.phonelive.widget.video.CirclePageIndicator;
import com.oushangfeng.marqueelayout.MarqueeLayout;
import com.oushangfeng.marqueelayout.MarqueeLayoutAdapter;

import java.util.List;

import cn.trinea.android.view.autoscrollviewpager.AutoScrollViewPager;

/**
 * @author Smile Wei
 * @since 2017/03/01.
 */

public class HomeShouyeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private Activity activity;
    private List<VideoObject> list;
    private List<NoticeObject> noticeList;//公告
    private List<BannerObject> loopList;

    private final SpanSizeLookup spanSizeLookup = new SpanSizeLookup();
    private LayoutInflater inflater;

    public HomeShouyeAdapter(Context context, Activity activity, List<VideoObject> list) {
        this.context = context;
        this.activity = activity;
        this.list = list;
        inflater = LayoutInflater.from(context);
    }

    public void setLoopList(List<BannerObject> loopList) {
        this.loopList = loopList;
    }

    public void setNoticeList(List<NoticeObject> noticeList) {
        this.noticeList = noticeList;
    }

    public GridLayoutManager.SpanSizeLookup getSpanSizeLookup() {
        return spanSizeLookup;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        switch (viewType) {
            case VideoObject.TYPE_CAROUSEL:
                view = inflater.inflate(R.layout.adapter_item_home_type_carousel, parent, false);
                return new CarouselHolder(view);
            case VideoObject.TYPE_HEADLINE:
                view = inflater.inflate(R.layout.adapter_item_home_type_headline, parent, false);
                return new HeadlineHolder(view);
            case VideoObject.TYPE_HEADER:
                view = inflater.inflate(R.layout.adapter_item_home_type_header, parent, false);
                return new HeaderHolder(view);
            case VideoObject.TYPE_DIVIDER:
                view = inflater.inflate(R.layout.adapter_item_home_type_divider, parent, false);
                return new PlaceHolder(view);
            case VideoObject.TYPE_RECOMMEND:
                view = inflater.inflate(R.layout.adapter_item_home_type_recommend, parent, false);
                return new RecommendHolder(view);
            default:
                view = inflater.inflate(R.layout.item_footer_loading, parent, false);
                return new FooterHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        VideoObject bean = list.get(position);
        if (viewHolder instanceof CarouselHolder) {
            CarouselHolder holder = (CarouselHolder) viewHolder;
            holder.viewPager.setAdapter(new BannerAdapter(context, activity, loopList));
            holder.indicator.setViewPager(holder.viewPager);
            holder.viewPager.setInterval(4000);
            holder.viewPager.setSlideBorderMode(AutoScrollViewPager.SLIDE_BORDER_MODE_TO_PARENT);
            holder.viewPager.startAutoScroll();
        } else if (viewHolder instanceof HeadlineHolder) {
            HeadlineHolder holder = (HeadlineHolder) viewHolder;
            MarqueeLayoutAdapter<NoticeObject> topAdapter = new MarqueeLayoutAdapter<NoticeObject>(noticeList) {
                @Override
                protected int getItemLayoutId() {
                    return R.layout.item_marquee;
                }

                @Override
                protected void initView(View view, int position, NoticeObject item) {
                    ((TextView) view).setText(item.getTitle());

                }
            };
            holder.marqueeLayout.setAdapter(topAdapter);
            holder.marqueeLayout.start();
        } else if (viewHolder instanceof HeaderHolder) {
            HeaderHolder holder = (HeaderHolder) viewHolder;
            holder.tvTitle.setText(bean.getTitle());


        } else if (viewHolder instanceof RecommendHolder) {
            RecommendHolder holder = (RecommendHolder) viewHolder;
            SimpleUtils.loadImageForView(activity,  holder.ivHome, bean.getThumbnail(), 0);

            holder.tvTitle.setText(bean.getTitle());
            holder.tv_bofang.setText(""+bean.getClick());
            holder.tv_jinbi.setText(""+bean.getGold());
            holder.tv_zan.setText(""+bean.getGood());
            holder.tv_time.setText(""+bean.getPlay_time());


        } else if (viewHolder instanceof FooterHolder) {
            FooterHolder holder = (FooterHolder) viewHolder;
            holder.footerLoading.onLoad(Type.TYPE_FOOTER_FULL == list.get(position).getType());
        }
    }

    @Override
    public int getItemViewType(int position) {
        return list.get(position).getType();
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    class CarouselHolder extends RecyclerView.ViewHolder {
        AutoScrollViewPager viewPager;
        CirclePageIndicator indicator;

        CarouselHolder(View itemView) {
            super(itemView);
            viewPager=(AutoScrollViewPager)itemView.findViewById(R.id.view_pager);
            indicator=(CirclePageIndicator)itemView.findViewById(R.id.indicator);
        }
    }

    class HeadlineHolder extends RecyclerView.ViewHolder {
        MarqueeLayout marqueeLayout;

        HeadlineHolder(View itemView) {
            super(itemView);
            marqueeLayout=(MarqueeLayout)itemView.findViewById(R.id.marquee_layout);
        }
    }




    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    private OnItemClickListener listener;

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    class RecommendHolder extends RecyclerView.ViewHolder {
        ImageView ivHome;
        TextView tvTitle;
        TextView tv_bofang;
        TextView tv_jinbi;
        TextView tv_zan;
        TextView tv_time;

        RecommendHolder(View itemView) {
            super(itemView);
            ivHome=(ImageView)itemView.findViewById(R.id.iv_home);
            tvTitle=(TextView)itemView.findViewById(R.id.tv_title);
            tv_bofang=(TextView)itemView.findViewById(R.id.tv_bofang);
            tv_jinbi=(TextView)itemView.findViewById(R.id.tv_jinbi);
            tv_zan=(TextView)itemView.findViewById(R.id.tv_zan);
            tv_time=(TextView)itemView.findViewById(R.id.tv_time);

//            itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//
//                    UIHelper.showVideoPlayerNew(mContext,mUserList.get(mPosition));
//
//                }
//            });
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null)
                        listener.onItemClick(getLayoutPosition());
                }
            });
        }

    }


    class HeaderHolder extends RecyclerView.ViewHolder {
        TextView tvTitle;
        View ll_more;

        HeaderHolder(View itemView) {
            super(itemView);
            tvTitle=(TextView)itemView.findViewById(R.id.tv_title);
            ll_more=itemView.findViewById(R.id.ll_more);
            ll_more.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null)
                        listener.onItemClick(getLayoutPosition());
                }
            });
        }
    }

    private class PlaceHolder extends RecyclerView.ViewHolder {
        PlaceHolder(View itemView) {
            super(itemView);
        }
    }


    private class SpanSizeLookup extends GridLayoutManager.SpanSizeLookup {
        @Override
        public int getSpanSize(int position) {
            return list.get(position).getSpanCount();
        }
    }

    class FooterHolder extends RecyclerView.ViewHolder {

        FooterLoading footerLoading;
        FooterHolder(View itemView) {
            super(itemView);
            footerLoading=(FooterLoading)itemView.findViewById(R.id.footer);
        }
    }
}

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

import com.duomizhibo.phonelive.base.Type;
import com.duomizhibo.phonelive.bean.video.HomeBase;
import com.duomizhibo.phonelive.bean.video.HomeTop;


import java.util.List;
import java.util.Random;

import com.duomizhibo.phonelive.R;
import com.duomizhibo.phonelive.utils.SimpleUtils;
import com.duomizhibo.phonelive.widget.FooterLoading;
import com.duomizhibo.phonelive.widget.video.CirclePageIndicator;
import com.oushangfeng.marqueelayout.MarqueeLayout;
import com.oushangfeng.marqueelayout.MarqueeLayoutAdapter;

import cn.trinea.android.view.autoscrollviewpager.AutoScrollViewPager;

/**
 * @author Smile Wei
 * @since 2017/03/01.
 */

public class FomeShouyeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private Activity activity;
    private List<HomeBase> list;
    private List<HomeTop.Carousel> loopList;
    private List<HomeBase> headlineList;
    private final SpanSizeLookup spanSizeLookup = new SpanSizeLookup();
    private LayoutInflater inflater;

    public FomeShouyeAdapter(Context context, Activity activity, List<HomeBase> list) {
        this.context = context;
        this.activity = activity;
        this.list = list;
        inflater = LayoutInflater.from(context);
    }

    public void setLoopList(List<HomeTop.Carousel> loopList) {
        this.loopList = loopList;
    }

    public void setHeadlineList(List<HomeBase> headlineList) {
        this.headlineList = headlineList;
    }

    public GridLayoutManager.SpanSizeLookup getSpanSizeLookup() {
        return spanSizeLookup;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        switch (viewType) {
            case HomeBase.TYPE_CAROUSEL:
                view = inflater.inflate(R.layout.item_home_type_carousel, parent, false);
                return new CarouselHolder(view);
            case HomeBase.TYPE_CATEGORY:
                view = inflater.inflate(R.layout.fragment_navigation_home_category, parent, false);
                return new CategoryHolder(view);
            case HomeBase.TYPE_HEADLINE:
                view = inflater.inflate(R.layout.item_home_type_headline, parent, false);
                return new HeadlineHolder(view);
            case HomeBase.TYPE_HEADER:
                view = inflater.inflate(R.layout.item_home_type_header, parent, false);
                return new HeaderHolder(view);
            case HomeBase.TYPE_DIVIDER:
                view = inflater.inflate(R.layout.item_home_type_divider, parent, false);
                return new PlaceHolder(view);
            case HomeBase.TYPE_LIVE:
                view = inflater.inflate(R.layout.item_home_type_live, parent, false);
                return new LiveHolder(view);
            case HomeBase.TYPE_HOT:
                view = inflater.inflate(R.layout.item_home_type_hot, parent, false);
                return new CommonHolder(view);
            case HomeBase.TYPE_RECOMMEND:
                view = inflater.inflate(R.layout.item_home_type_recommend, parent, false);
                return new RecommendHolder(view);
            case HomeBase.TYPE_PLACE:
                view = inflater.inflate(R.layout.item_place, parent, false);
                return new PlaceHolder(view);
            default:
                view = inflater.inflate(R.layout.item_footer_loading, parent, false);
                return new FooterHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        HomeBase bean = list.get(position);
        if (viewHolder instanceof CarouselHolder) {
            CarouselHolder holder = (CarouselHolder) viewHolder;
            holder.viewPager.setAdapter(new ImageHomeAdapter(context, activity, loopList));
            holder.indicator.setViewPager(holder.viewPager);
            holder.viewPager.setInterval(4000);
            holder.viewPager.setSlideBorderMode(AutoScrollViewPager.SLIDE_BORDER_MODE_TO_PARENT);
            holder.viewPager.startAutoScroll();
        } else if (viewHolder instanceof HeadlineHolder) {
            HeadlineHolder holder = (HeadlineHolder) viewHolder;
            MarqueeLayoutAdapter<HomeBase> topAdapter = new MarqueeLayoutAdapter<HomeBase>(headlineList) {
                @Override
                protected int getItemLayoutId() {
                    return R.layout.item_marquee;
                }

                @Override
                protected void initView(View view, int position, HomeBase item) {
                    ((TextView) view).setText(item.getName());

                }
            };
            holder.marqueeLayout.setAdapter(topAdapter);
            holder.marqueeLayout.start();
        } else if (viewHolder instanceof HeaderHolder) {
            HeaderHolder holder = (HeaderHolder) viewHolder;
            holder.tvTitle.setText(bean.getName());
        } else if (viewHolder instanceof LiveHolder) {
            LiveHolder holder = (LiveHolder) viewHolder;
            SimpleUtils.loadImageForView(activity,  holder.ivHome, bean.getUrl(), 0);

            holder.tvTitle.setText(bean.getName());
        } else if (viewHolder instanceof CommonHolder) {
            CommonHolder holder = (CommonHolder) viewHolder;
            SimpleUtils.loadImageForView(activity,  holder.ivHome, bean.getUrl(), 0);

            holder.tvTitle.setText(bean.getName());
            holder.tvPrice.setText(String.format("%s%s", bean.getCurrency(), bean.getPrice()));
        } else if (viewHolder instanceof RecommendHolder) {
            RecommendHolder holder = (RecommendHolder) viewHolder;
            SimpleUtils.loadImageForView(activity,  holder.ivHome, bean.getUrl(), 0);

            holder.tvTitle.setText(bean.getName());
            holder.tvPrice.setText(String.format("%s%s", bean.getCurrency(), bean.getPrice()));
            holder.tvCount.setText(String.format("%d人付款", new Random().nextInt(10000)));

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


    class CommonHolder extends RecyclerView.ViewHolder {
        ImageView ivHome;
        TextView tvTitle;
        TextView tvPrice;


        CommonHolder(View itemView) {
            super(itemView);
            ivHome= (ImageView)itemView.findViewById(R.id.iv_home);
            tvTitle= (TextView)itemView.findViewById(R.id.tv_title);
            tvPrice= (TextView)itemView.findViewById(R.id.tv_price);
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
        TextView tvPrice;
        TextView tvCount;

        RecommendHolder(View itemView) {
            super(itemView);
            ivHome=(ImageView)itemView.findViewById(R.id.iv_home);
            tvTitle=(TextView)itemView.findViewById(R.id.tv_title);
            tvPrice=(TextView)itemView.findViewById(R.id.tv_price);
            tvCount=(TextView)itemView.findViewById(R.id.tv_count);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null)
                        listener.onItemClick(getLayoutPosition());
                }
            });
        }

    }

    class LiveHolder extends RecyclerView.ViewHolder {
        ImageView ivHome;
        TextView tvTitle;

        LiveHolder(View itemView) {
            super(itemView);
            ivHome=(ImageView)itemView.findViewById(R.id.iv_home);
            tvTitle=(TextView)itemView.findViewById(R.id.tv_title);
        }

    }

    class HeaderHolder extends RecyclerView.ViewHolder {
        TextView tvTitle;

        HeaderHolder(View itemView) {
            super(itemView);
            tvTitle=(TextView)itemView.findViewById(R.id.tv_title);
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


    class CategoryHolder extends RecyclerView.ViewHolder {

        CategoryHolder(View view) {
            super(view);
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

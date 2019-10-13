package com.duomizhibo.phonelive.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.duomizhibo.phonelive.R;

/**
 * Created by debug on 2017/8/25.
 */

public class LoadListView extends ListView implements AbsListView.OnScrollListener {
    private LayoutInflater mInflater;
    //判断是否滚动最后一行
    private boolean isLastRow = false;
    //底部View布局
    public View mFooter;
    //实现接口加载更多数据
    public OnLoadMoreListener moreListener;

    public ProgressBar mProgress;
    public TextView mTvLoad;

    public void setLoadMoreListener(OnLoadMoreListener moreListener) {
        this.moreListener = moreListener;
    }

    public LoadListView(Context context) {
        super(context);
        initView();
    }

    public LoadListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public LoadListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        mInflater = LayoutInflater.from(getContext());
        mFooter = mInflater.inflate(R.layout.
                listview_footer, null);
        mProgress = (ProgressBar) mFooter.findViewById(R.id.pg_loader);
        mTvLoad = (TextView) mFooter.findViewById(R.id.id_loadmore);
        this.addFooterView(mFooter);
        mFooter.setVisibility(View.GONE);
        setOnScrollListener(this);
    }

    @Override
    public void onScrollStateChanged(AbsListView absListView, int scrollState) {
        //正在滚动时回调，回调2-3次，手指没抛则回调2次。scrollState = 2的这次不回调
        //回调顺序如下
        //第1次：scrollState = SCROLL_STATE_TOUCH_SCROLL(1) 正在滚动
        //第2次：scrollState = SCROLL_STATE_FLING(2) 手指做了抛的动作（手指离开屏幕前，用力滑了一下）
        //第3次：scrollState = SCROLL_STATE_IDLE(0) 停止滚动
        //当屏幕停止滚动时为0；当屏幕滚动且用户使用的触碰或手指还在屏幕上时为1；
        //由于用户的操作，屏幕产生惯性滑动时为2

        //当滚到最后一行且停止滚动时，执行加载
        if (isLastRow && scrollState == OnScrollListener.SCROLL_STATE_IDLE) {
            isLastRow = false;
            if (moreListener != null) {
                moreListener.loadMore();
            }
        }
    }

    @Override
    public void onScroll(AbsListView absListView, int firstVisibleItem,
                         int visibleItemCount, int totalItemCount) {
        //滚动时一直回调，直到停止滚动时才停止回调。单击时回调一次。
        //firstVisibleItem：当前能看见的第一个列表项ID（从0开始）
        //visibleItemCount：当前能看见的列表项个数（小半个也算）
        //totalItemCount：列表项共数
        //判断是否滚到最后一行
        if (firstVisibleItem + visibleItemCount == totalItemCount && totalItemCount > 0) {
            isLastRow = true;
        }
    }

    public interface OnLoadMoreListener {
        void loadMore();
    }
}

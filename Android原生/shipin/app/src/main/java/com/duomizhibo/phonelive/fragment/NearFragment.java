package com.duomizhibo.phonelive.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.duomizhibo.phonelive.AppContext;
import com.duomizhibo.phonelive.R;
import com.duomizhibo.phonelive.adapter.NearAdapter;
import com.duomizhibo.phonelive.api.remote.ApiUtils;
import com.duomizhibo.phonelive.api.remote.PhoneLiveApi;
import com.duomizhibo.phonelive.base.AbsFragment;
import com.duomizhibo.phonelive.bean.ActiveBean;
import com.duomizhibo.phonelive.bean.LiveAndVideoBean;
import com.duomizhibo.phonelive.bean.LiveJson;
import com.duomizhibo.phonelive.interf.OnItemClickListener;
import com.duomizhibo.phonelive.interf.OnLiveItemClickListener;
import com.duomizhibo.phonelive.ui.SmallVideoPlayerActivity;
import com.duomizhibo.phonelive.ui.VideoPlayerActivity;
import com.duomizhibo.phonelive.ui.customviews.RefreshLayout;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.List;

import okhttp3.Call;

/**
 * Created by cxf on 2017/9/5.
 */

public class NearFragment extends AbsFragment implements RefreshLayout.OnRefreshListener, OnLiveItemClickListener {
    private View mRootView;
    private Context mContext;
    private RefreshLayout mRefreshLayout;
    private RecyclerView mRecyclerView;
    private View mLoadFailure;//加载失败
    private NearAdapter mAdapter;
    private Gson mGson;
    private Type mType1;
    private Type mType2;
    private int mPage;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mContext = getActivity();
        mRootView = inflater.inflate(R.layout.fragment_near, container, false);
        return mRootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData();
        initView();
    }

    private void initView() {
        mRefreshLayout = (RefreshLayout) mRootView.findViewById(R.id.refreshLayout);
        mRecyclerView = (RecyclerView) mRootView.findViewById(R.id.recylcerView);
        mRecyclerView.setHasFixedSize(true);
        mRefreshLayout.setScorllView(mRecyclerView);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(mContext, 2, GridLayoutManager.VERTICAL, false);
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return position == 0 ? 2 : 1;
            }
        });
        mRecyclerView.setLayoutManager(gridLayoutManager);
        mLoadFailure = mRootView.findViewById(R.id.load_failure);
        mRefreshLayout.setOnRefreshListener(this);
        mGson = new Gson();
        mType1 = new TypeToken<List<LiveJson>>() {
        }.getType();
        mType2 = new TypeToken<List<ActiveBean>>() {
        }.getType();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onRefresh() {
        initData();
    }

    @Override
    public void onLoadMore() {
        mPage++;
        PhoneLiveApi.getDataFuJin(mPage, AppContext.getInstance().lat, AppContext.getInstance().lng, mLoadMorePage);
    }

    private StringCallback mLoadMorePage = new StringCallback() {
        @Override
        public void onError(Call call, Exception e, int id) {
            mRefreshLayout.completeLoadMore();
            if (mLoadFailure != null && mLoadFailure.getVisibility() == View.GONE) {
                mLoadFailure.setVisibility(View.VISIBLE);
            }
            if (mRecyclerView.getVisibility() == View.VISIBLE) {
                mRecyclerView.setVisibility(View.GONE);
            }
        }

        @Override
        public void onResponse(String response, int id) {
            mRefreshLayout.completeLoadMore();
            if (mRecyclerView.getVisibility() == View.GONE) {
                mRecyclerView.setVisibility(View.VISIBLE);
            }
            if (mLoadFailure != null && mLoadFailure.getVisibility() == View.VISIBLE) {
                mLoadFailure.setVisibility(View.GONE);
            }
            JSONArray array = ApiUtils.checkIsSuccess(response);
            if (array.length()>0) {
                try {
                    JSONObject info0 = array.getJSONObject(0);
                    List<ActiveBean> moreVideoList = mGson.fromJson(info0.getString("video"), mType2);
                    if (mAdapter != null) {
                        mAdapter.insertList(moreVideoList);
                    }
                    if (moreVideoList.size()==0){
                        AppContext.toast("已经没有更多数据了");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    };


    private void initData() {
        mPage = 1;
        PhoneLiveApi.getDataFuJin(mPage, AppContext.getInstance().lat, AppContext.getInstance().lng, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                mRefreshLayout.completeRefresh();

                if (mLoadFailure != null && mLoadFailure.getVisibility() == View.GONE) {
                    mLoadFailure.setVisibility(View.VISIBLE);
                }
                if (mRecyclerView.getVisibility() == View.VISIBLE) {
                    mRecyclerView.setVisibility(View.GONE);
                }
            }

            @Override
            public void onResponse(String response, int id) {
                mRefreshLayout.completeRefresh();
                if (mRecyclerView.getVisibility() == View.GONE) {
                    mRecyclerView.setVisibility(View.VISIBLE);
                }
                if (mLoadFailure != null && mLoadFailure.getVisibility() == View.VISIBLE) {
                    mLoadFailure.setVisibility(View.GONE);
                }
                JSONArray array = ApiUtils.checkIsSuccess(response);
                if (array != null) {
                    try {
                        JSONObject info0 = array.getJSONObject(0);

                        List<LiveJson> liveList = mGson.fromJson(info0.getString("live"), mType1);
                        List<ActiveBean> videoList = mGson.fromJson(info0.getString("video"), mType2);
                        if (mAdapter == null) {
                            mAdapter = new NearAdapter(mContext, liveList, videoList);
                            mAdapter.setOnItemClickListener(new OnItemClickListener<ActiveBean>() {
                                @Override
                                public void onItemClick(ActiveBean item) {
                                    SmallVideoPlayerActivity.startSmallVideoPlayerActivity(getContext(), item);
                                }
                            });
                            mRecyclerView.setAdapter(mAdapter);
                        } else {
                            mAdapter.setData(liveList, videoList);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    @Override
    public void LiveClick(LiveJson item, int position) {
        VideoPlayerActivity.startVideoPlayerActivity(mContext, item);
    }
}

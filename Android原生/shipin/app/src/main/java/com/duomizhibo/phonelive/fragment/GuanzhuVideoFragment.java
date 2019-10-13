package com.duomizhibo.phonelive.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.duomizhibo.phonelive.AppContext;
import com.duomizhibo.phonelive.R;
import com.duomizhibo.phonelive.adapter.AttentionVideoAdapter;
import com.duomizhibo.phonelive.api.remote.PhoneLiveApi;
import com.duomizhibo.phonelive.base.AbsFragment;
import com.duomizhibo.phonelive.bean.ActiveBean;
import com.duomizhibo.phonelive.ui.customviews.RefreshLayout;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.List;

import okhttp3.Call;

/**
 * Created by cxf on 2017/9/5.
 */

public class GuanzhuVideoFragment extends AbsFragment implements RefreshLayout.OnRefreshListener {
    private int mPage;
    private Context mContext;
    private View mRootView;
    private View mNoZhubo;
    private View mFailure;
    private RefreshLayout mRefreshLayout;
    private RecyclerView mRecyclerView;
    private Type mType;
    private Gson mGson;
    private AttentionVideoAdapter mAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mContext = getActivity();
        mRootView = inflater.inflate(R.layout.fragment_guanzhu_video, container, false);
        return mRootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
        initData();
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    private void initView() {
        mNoZhubo = mRootView.findViewById(R.id.no_zhubo);
        mFailure = mRootView.findViewById(R.id.load_failure);
        mRefreshLayout = (RefreshLayout) mRootView.findViewById(R.id.refreshLayout);
        mRefreshLayout.setOnRefreshListener(this);
        mRecyclerView = (RecyclerView) mRootView.findViewById(R.id.recylcerView);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new GridLayoutManager(mContext, 2, GridLayoutManager.VERTICAL, false));
        mRefreshLayout.setScorllView(mRecyclerView);
        mType = new TypeToken<List<ActiveBean>>() {
        }.getType();
        mGson = new Gson();
    }

    private void initData() {
        mPage = 1;
        PhoneLiveApi.getAttentionVideo(mPage, mCallback);
    }


    private StringCallback mCallback = new StringCallback() {
        @Override
        public void onError(Call call, Exception e, int id) {
            mRefreshLayout.completeRefresh();
            if (mFailure.getVisibility() == View.GONE) {
                mFailure.setVisibility(View.VISIBLE);
            }
            if (mNoZhubo.getVisibility() == View.VISIBLE) {
                mNoZhubo.setVisibility(View.GONE);
            }
            if (mRecyclerView.getVisibility() == View.VISIBLE) {
                mRecyclerView.setVisibility(View.GONE);
            }
        }

        @Override
        public void onResponse(String response, int id) {
            mRefreshLayout.completeRefresh();
            if (mFailure.getVisibility() == View.VISIBLE) {
                mFailure.setVisibility(View.GONE);
            }
            if (mRecyclerView.getVisibility() == View.GONE) {
                mRecyclerView.setVisibility(View.VISIBLE);
            }
            try {
                JSONObject obj = new JSONObject(response);
                if ("200".equals(obj.getString("ret"))) {
                    JSONObject data = obj.getJSONObject("data");
                    if (0 == data.getInt("code")) {
                        List<ActiveBean> list = mGson.fromJson(data.getString("info"), mType);
                        if (mAdapter == null) {
                            mAdapter = new AttentionVideoAdapter(mContext, list);
                            mRecyclerView.setAdapter(mAdapter);
                        } else {
                            mAdapter.setList(list);
                        }
                        if (list.size() > 0) {
                            if (mNoZhubo.getVisibility() == View.VISIBLE) {
                                mNoZhubo.setVisibility(View.GONE);
                            }
                        } else {
                            if (mNoZhubo.getVisibility() == View.GONE) {
                                mNoZhubo.setVisibility(View.VISIBLE);
                            }
                        }
                    } else {
                        AppContext.toast(data.getString("msg"));
                    }
                } else {
                    AppContext.toast("获取数据失败");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };


    @Override
    public void onRefresh() {
        initData();
    }

    @Override
    public void onLoadMore() {
        mPage++;
        PhoneLiveApi.getAttentionVideo(mPage, mLoadMoreVideoData);
    }

    private StringCallback mLoadMoreVideoData = new StringCallback() {
        @Override
        public void onError(Call call, Exception e, int id) {
            mRefreshLayout.completeRefresh();
            if (mFailure.getVisibility() == View.GONE) {
                mFailure.setVisibility(View.VISIBLE);
            }
            if (mNoZhubo.getVisibility() == View.VISIBLE) {
                mNoZhubo.setVisibility(View.GONE);
            }
            if (mRecyclerView.getVisibility() == View.VISIBLE) {
                mRecyclerView.setVisibility(View.GONE);
            }
        }

        @Override
        public void onResponse(String response, int id) {
            mRefreshLayout.completeRefresh();
            JSONObject obj = null;
            try {
                obj = new JSONObject(response);
                if ("200".equals(obj.getString("ret"))) {
                    JSONObject data = obj.getJSONObject("data");
                    if (0 == data.getInt("code")) {
                        List<ActiveBean> moreVideoList = mGson.fromJson(data.getString("info"), mType);
                        if (moreVideoList.size()>0){
                            mAdapter.insertList(moreVideoList);
                        }else {
                            AppContext.toast("已经没有更多数据了");
                        }
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    };
}

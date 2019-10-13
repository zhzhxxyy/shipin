package com.duomizhibo.phonelive.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.duomizhibo.phonelive.R;
import com.duomizhibo.phonelive.adapter.PersontopersonAdapter;
import com.duomizhibo.phonelive.api.remote.ApiUtils;
import com.duomizhibo.phonelive.api.remote.PhoneLiveApi;
import com.duomizhibo.phonelive.base.AbsFragment;
import com.duomizhibo.phonelive.bean.LiveJson;
import com.duomizhibo.phonelive.interf.OnItemClickListener;
import com.duomizhibo.phonelive.ui.customviews.RefreshLayout;
import com.duomizhibo.phonelive.utils.UIHelper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;

import java.lang.reflect.Type;
import java.util.List;

import okhttp3.Call;

/**
 * Created by debug on 2017/9/5.
 */

public class PersontoPersonFragment extends AbsFragment implements RefreshLayout.OnRefreshListener {
    private View mRootView;
    private Context mContext;
    private RefreshLayout mRefreshLayout;
    private RecyclerView mRecyclerView;
    private View mLoadFailure;//加载失败
    private PersontopersonAdapter mAdapter;
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
        mRecyclerView.setLayoutManager(gridLayoutManager);
        mLoadFailure = mRootView.findViewById(R.id.load_failure);
        mRefreshLayout.setOnRefreshListener(this);
        mGson = new Gson();
        mType1 = new TypeToken<List<LiveJson>>() {
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
        //mPage++;
        mRefreshLayout.completeRefresh();
    }

    private void initData() {
        mPage = 1;
        PhoneLiveApi.getprivatelive(new StringCallback() {
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

                        List<LiveJson> liveList = mGson.fromJson(array.toString(), mType1);
                        if (mAdapter == null) {
                            mAdapter = new PersontopersonAdapter(getActivity().getLayoutInflater(), liveList);
                            mAdapter.setOnItemClickListener(new OnItemClickListener<LiveJson>() {
                                @Override
                                public void onItemClick(LiveJson item) {
                                    UIHelper.shoPersonVideoActivity(getContext(),item);
                                }
                            });
                            mRecyclerView.setAdapter(mAdapter);
                        } else {
                            mAdapter.setData(liveList);
                            mAdapter.notifyDataSetChanged();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }


}

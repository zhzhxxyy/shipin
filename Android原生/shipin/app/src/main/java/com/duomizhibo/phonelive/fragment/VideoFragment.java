package com.duomizhibo.phonelive.fragment;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.gson.Gson;
import com.duomizhibo.phonelive.AppContext;
import com.duomizhibo.phonelive.R;
import com.duomizhibo.phonelive.adapter.NewestAdapter;
import com.duomizhibo.phonelive.adapter.VideoAdapter;
import com.duomizhibo.phonelive.api.remote.ApiUtils;
import com.duomizhibo.phonelive.api.remote.PhoneLiveApi;
import com.duomizhibo.phonelive.base.BaseFragment;
import com.duomizhibo.phonelive.bean.ActiveBean;
import com.duomizhibo.phonelive.bean.LiveJson;
import com.duomizhibo.phonelive.event.Event;
import com.duomizhibo.phonelive.ui.SmallVideoPlayerActivity;
import com.duomizhibo.phonelive.ui.VideoPlayerActivity;
import com.duomizhibo.phonelive.ui.customviews.RefreshLayout;
import com.duomizhibo.phonelive.ui.other.OnItemEvent;
import com.duomizhibo.phonelive.utils.StringUtils;
import com.duomizhibo.phonelive.utils.TDevice;
import com.duomizhibo.phonelive.widget.HeaderGridView;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import okhttp3.Call;

/**
 * 首页最新直播
 */
public class VideoFragment extends BaseFragment implements RefreshLayout.OnRefreshListener {

    List<ActiveBean> mUserList = new ArrayList<>();

    @InjectView(R.id.gv_newest)
    RecyclerView mRecyclerView;

    //默认提示
    @InjectView(R.id.newest_fensi)
    LinearLayout mFensi;

    @InjectView(R.id.newest_load)
    LinearLayout mLoad;
    @InjectView(R.id.refreshLayout)
    RefreshLayout mRefreshLayout;
    private int wh;
    private int page;

    private VideoAdapter newestAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_newest, null);

        ButterKnife.inject(this, view);
        initData();
        initView(view);
        return view;
    }

    @Override
    public void initData() {
        requestData();
    }

    @Override
    public void initView(View view) {
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        mRefreshLayout.setScorllView(mRecyclerView);
        mRefreshLayout.setOnRefreshListener(this);
    }


    //最新主播数据请求
    private void requestData() {
        page = 1;
        PhoneLiveApi.getVideo(page, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                mRefreshLayout.completeRefresh();
                mFensi.setVisibility(View.GONE);
                mLoad.setVisibility(View.VISIBLE);
                mRecyclerView.setVisibility(View.INVISIBLE);
                AppContext.toast("加载失败");
            }

            @Override
            public void onResponse(String response, int id) {
                if (mLoad.getVisibility() == View.VISIBLE) {
                    mLoad.setVisibility(View.GONE);
                }
                mRefreshLayout.completeRefresh();
                JSONArray resUserListJsonArr = ApiUtils.checkIsSuccess(response);

                if (null != resUserListJsonArr) {

                    try {
                        mUserList.clear();
                        Gson g = new Gson();
                        for (int i = 0; i < resUserListJsonArr.length(); i++) {
                            mUserList.add(g.fromJson(resUserListJsonArr.getString(i), ActiveBean.class));
                        }

                        if (mUserList.size() > 0) {
                            if (newestAdapter == null) {
                                newestAdapter = new VideoAdapter(getActivity(), mUserList);
                                mRecyclerView.setAdapter(newestAdapter);//BBB
                            } else {
                                newestAdapter.setData(mUserList);
                            }
                        } else {
                            mFensi.setVisibility(View.VISIBLE);
                            mLoad.setVisibility(View.GONE);
                            mRecyclerView.setVisibility(View.INVISIBLE);

                        }
                    } catch (JSONException e) {

                        e.printStackTrace();
                    }
                } else {
                    mFensi.setVisibility(View.VISIBLE);
                    mLoad.setVisibility(View.GONE);
                    mRecyclerView.setVisibility(View.INVISIBLE);
                }

            }
        });
    }


    @Override
    public void onRefresh() {
        requestData();
    }

    @Override
    public void onLoadMore() {
        page++;
        PhoneLiveApi.getVideo(page, mLoadMoreCallback);
    }

    private StringCallback mLoadMoreCallback=new StringCallback() {
        @Override
        public void onError(Call call, Exception e, int id) {
            mRefreshLayout.completeRefresh();
            mFensi.setVisibility(View.GONE);
            mLoad.setVisibility(View.VISIBLE);
            mRecyclerView.setVisibility(View.INVISIBLE);
            AppContext.toast("加载失败");
        }

        @Override
        public void onResponse(String response, int id) {
            if (mLoad.getVisibility() == View.VISIBLE) {
                mLoad.setVisibility(View.GONE);
            }
            mRefreshLayout.completeRefresh();
            JSONArray resUserListJsonArr = ApiUtils.checkIsSuccess(response);

            if (resUserListJsonArr.length()>0) {

                try {
                    List<ActiveBean> list=new ArrayList<>();
                    Gson g = new Gson();
                    for (int i = 0; i < resUserListJsonArr.length(); i++) {
                        list.add(g.fromJson(resUserListJsonArr.getString(i), ActiveBean.class));
                    }

                    if (list.size() > 0) {
                        if (newestAdapter != null) {
                            newestAdapter.insertList(list);
                        }
                    } else {
                        mFensi.setVisibility(View.VISIBLE);
                        mLoad.setVisibility(View.GONE);
                        mRecyclerView.setVisibility(View.INVISIBLE);

                    }
                } catch (JSONException e) {

                    e.printStackTrace();
                }
            } else {
                AppContext.toast("已经没有更多数据了");
            }
        }
    };
    @Override
    public void onResume() {
        super.onResume();
        requestData();
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        OkHttpUtils.getInstance().cancelTag("getNewestUserList");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

}

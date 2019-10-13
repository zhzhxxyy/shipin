package com.duomizhibo.phonelive.fragment;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.duomizhibo.phonelive.AppContext;
import com.duomizhibo.phonelive.adapter.NewestAdapter;
import com.duomizhibo.phonelive.api.remote.ApiUtils;
import com.duomizhibo.phonelive.base.BaseFragment;
import com.duomizhibo.phonelive.bean.LiveJson;
import com.duomizhibo.phonelive.ui.VideoPlayerActivity;
import com.duomizhibo.phonelive.ui.other.OnItemEvent;
import com.duomizhibo.phonelive.utils.StringUtils;
import com.duomizhibo.phonelive.utils.TDevice;
import com.duomizhibo.phonelive.utils.UIHelper;
import com.duomizhibo.phonelive.widget.HeaderGridView;
import com.duomizhibo.phonelive.R;
import com.duomizhibo.phonelive.api.remote.PhoneLiveApi;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import okhttp3.Call;

/**
 * 首页最新直播
 */
public class GuanzhuLiveFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener {

    List<LiveJson> mUserList = new ArrayList<>();

    @InjectView(R.id.gv_newest)
    HeaderGridView mNewestLiveView;

    @InjectView(R.id.sl_newest)
    SwipeRefreshLayout mRefresh;

    @InjectView(R.id.newest_load)
    LinearLayout mLoad;

    //默认提示
    @InjectView(R.id.newest_fensi)
    View mTvPrompt;


    private int wh;

    private NewestAdapter newestAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_guanzhu_live, null);
        ButterKnife.inject(this, view);
        initData();
        initView(view);
        return view;
    }

    @Override
    public void initData() {
        newestAdapter = new NewestAdapter(mUserList);
        mNewestLiveView.setAdapter(newestAdapter);//BBB
    }

    @Override
    public void initView(View view) {
        mNewestLiveView.setOnItemClickListener(new OnItemEvent(1000) {
            @Override
            public void singleClick(View v, int position) {
                if (AppContext.getInstance().getLoginUid() == null|| StringUtils.toInt(AppContext.getInstance().getLoginUid())==0) {
                    Toast.makeText(getContext(),"请登录..",Toast.LENGTH_SHORT).show();
                    return;
                }

                if (mUserList.get(position ).type.equals("6")){
                    UIHelper.shoPersonVideoActivity(getContext(),mUserList.get(position));
                }else {
                    VideoPlayerActivity.startVideoPlayerActivity(getContext(), mUserList.get(position ));
                }
            }

        });
        mRefresh.setColorSchemeColors(getResources().getColor(R.color.global));
        mRefresh.setOnRefreshListener(this);
    }


    private StringCallback callback = new StringCallback() {
        @Override
        public void onError(Call call, Exception e,int id) {
            mRefresh.setRefreshing(false);
            mTvPrompt.setVisibility(View.GONE);
            mLoad.setVisibility(View.VISIBLE);
            mNewestLiveView.setVisibility(View.INVISIBLE);
        }

        @Override
        public void onResponse(String response,int id) {
            mRefresh.setRefreshing(false);
            JSONArray liveAndAttentionUserJson = ApiUtils.checkIsSuccess(response);
            if (null != liveAndAttentionUserJson) {

                mUserList.clear();
                mUserList.addAll(ApiUtils.formatDataToList2(liveAndAttentionUserJson,LiveJson.class));
            }
            if (mUserList.size()>0){
                fillUI();
            }else{
                mTvPrompt.setVisibility(View.VISIBLE);
                mLoad.setVisibility(View.GONE);
                mNewestLiveView.setVisibility(View.INVISIBLE);
            }
        }
    };

    //最新主播数据请求
    private void requestData() {
        PhoneLiveApi.getAttentionLive(AppContext.getInstance().getLoginUid(), callback);
    }

    private void fillUI() {

        mTvPrompt.setVisibility(View.GONE);
        mLoad.setVisibility(View.GONE);
        mNewestLiveView.setVisibility(View.VISIBLE);
        if (getActivity() != null) {
            //设置每个主播宽度
            int w = (int) TDevice.getScreenWidth();
            wh = w / 2;
            mNewestLiveView.setColumnWidth(wh);
            newestAdapter.notifyDataSetChanged();

        }

    }

    @Override
    public void onRefresh() {
        requestData();
    }

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

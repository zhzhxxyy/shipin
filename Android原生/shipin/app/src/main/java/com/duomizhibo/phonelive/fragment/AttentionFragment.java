package com.duomizhibo.phonelive.fragment;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.duomizhibo.phonelive.bean.LiveJson;
import com.duomizhibo.phonelive.ui.other.OnItemEvent;
import com.duomizhibo.phonelive.utils.StringUtils;
import com.duomizhibo.phonelive.widget.WPSwipeRefreshLayout;
import com.google.gson.Gson;
import com.duomizhibo.phonelive.AppContext;
import com.duomizhibo.phonelive.R;
import com.duomizhibo.phonelive.adapter.LiveUserAdapter;
import com.duomizhibo.phonelive.api.remote.ApiUtils;
import com.duomizhibo.phonelive.api.remote.PhoneLiveApi;
import com.duomizhibo.phonelive.base.BaseFragment;
import com.duomizhibo.phonelive.bean.UserBean;
import com.duomizhibo.phonelive.ui.VideoPlayerActivity;
import com.duomizhibo.phonelive.utils.UIHelper;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import okhttp3.Call;

/**
 * 首页左边关注
 */
public class AttentionFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener {
    @InjectView(R.id.lv_attentions)
    ListView mLvAttentions;
    List<LiveJson> mUserList = new ArrayList<>();
    @InjectView(R.id.mSwipeRefreshLayout)
    WPSwipeRefreshLayout mRefresh;

    //默认提示
    @InjectView(R.id.fensi)
    LinearLayout mTvPrompt;
    @InjectView(R.id.load)
    LinearLayout mShibaiLoad;

    private View view;
    private LiveUserAdapter mAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_attention, null);
        ButterKnife.inject(this, view);
        initView(view);
        initData();
        return view;
    }

    @Override
    public void initView(View view) {
        mRefresh.setColorSchemeColors(getResources().getColor(R.color.global));
        mRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                PhoneLiveApi.getAttentionLive(AppContext.getInstance().getLoginUid(), callback);
            }
        });
        mLvAttentions.setOnItemClickListener(new OnItemEvent(1000) {
            @Override
            public void singleClick(View v, int position) {
                if (AppContext.getInstance().getLoginUid() == null|| StringUtils.toInt(AppContext.getInstance().getLoginUid())==0) {
                    Toast.makeText(getContext(),"请登录..",Toast.LENGTH_SHORT).show();
                    return;
                }
                VideoPlayerActivity.startVideoPlayerActivity(getContext(), mUserList.get(position ));
            }

        });
    }

    @Override
    public void initData() {

    }

    @Override
    public void onResume() {
        super.onResume();
        PhoneLiveApi.getAttentionLive(AppContext.getInstance().getLoginUid(), callback);
    }

    private StringCallback callback = new StringCallback() {
        @Override
        public void onError(Call call, Exception e,int id) {
            mRefresh.setRefreshing(false);
            mTvPrompt.setVisibility(View.GONE);
            mShibaiLoad.setVisibility(View.VISIBLE);
            mLvAttentions.setVisibility(View.INVISIBLE );
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
                mShibaiLoad.setVisibility(View.GONE);
                mLvAttentions.setVisibility(View.INVISIBLE);
            }
        }
    };

    private void fillUI() {
        mTvPrompt.setVisibility(View.GONE);
        mShibaiLoad.setVisibility(View.GONE);
        mLvAttentions.setVisibility(View.VISIBLE);
        mAdapter = new LiveUserAdapter(getActivity().getLayoutInflater(), mUserList);
        mLvAttentions.setAdapter(mAdapter);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        mAdapter = null;
        OkHttpUtils.getInstance().cancelTag("getAttentionLive");
    }

    @Override
    public void onRefresh() {
        PhoneLiveApi.getAttentionLive(AppContext.getInstance().getLoginUid(), callback);
    }
}
package com.duomizhibo.phonelive.ui;

import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.duomizhibo.phonelive.api.remote.ApiUtils;
import com.duomizhibo.phonelive.bean.SimpleUserInfo;
import com.duomizhibo.phonelive.bean.UserBean;
import com.duomizhibo.phonelive.ui.customviews.ActivityTitle;
import com.duomizhibo.phonelive.utils.UIHelper;
import com.google.gson.Gson;
import com.duomizhibo.phonelive.AppContext;
import com.duomizhibo.phonelive.R;
import com.duomizhibo.phonelive.adapter.UserBaseInfoAdapter;
import com.duomizhibo.phonelive.api.remote.PhoneLiveApi;
import com.duomizhibo.phonelive.base.ToolBarBaseActivity;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import butterknife.InjectView;
import okhttp3.Call;

/**
 * 粉丝列表
 */
public class FansActivity extends ToolBarBaseActivity {
    @InjectView(R.id.lv_fans)
    ListView mFansView;
    @InjectView(R.id.fensi)
    LinearLayout mFensi;
    @InjectView(R.id.load)
    LinearLayout mLoad;
    @InjectView(R.id.sr_refresh)
    SwipeRefreshLayout mRefreshLayout;

    @InjectView(R.id.view_title)
    ActivityTitle mActivityTitle;

    private List<SimpleUserInfo> mFanList = new ArrayList<>();

    @Override
    protected int getLayoutId() {
        return R.layout.activity_fans;
    }



    @Override
    public void initView() {

        mRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.global));
        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                //获取粉丝
                PhoneLiveApi.getFansList(getUserID(),getIntent().getStringExtra("uid"),callback);
                mRefreshLayout.setRefreshing(false);
            }
        });
        mFansView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                UIHelper.showHomePageActivity(FansActivity.this, mFanList.get(position).id);
            }
        });

        mActivityTitle.setReturnListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }


    @Override
    public void initData() {


    }

    @Override
    protected void onResume() {
        super.onResume();

        //获取粉丝
        PhoneLiveApi.getFansList(getUserID(),getIntent().getStringExtra("uid"),callback);
    }

    private void fillUI() {
        mFensi.setVisibility(View.GONE);
        mLoad.setVisibility(View.GONE);
        mFansView.setVisibility(View.VISIBLE);
        mFansView.setAdapter(new UserBaseInfoAdapter(mFanList));

    }
    private StringCallback callback = new StringCallback() {
        @Override
        public void onError(Call call, Exception e,int id) {
            mFensi.setVisibility(View.GONE);
            mLoad.setVisibility(View.VISIBLE);
            mFansView.setVisibility(View.GONE);
        }

        @Override
        public void onResponse(String response,int id) {
            JSONArray res = ApiUtils.checkIsSuccess(response);
            mFanList.clear();
            if(res != null){
                mFanList.addAll(ApiUtils.formatDataToList2(res,UserBean.class));
            }

            if (mFanList.size()>0){
                fillUI();
            }else{
                mFensi.setVisibility(View.VISIBLE);
                mLoad.setVisibility(View.GONE);
                mFansView.setVisibility(View.INVISIBLE);
            }
        }
    };
    @Override
    public void onClick(View v) {

    }


    @Override
    protected boolean hasActionBar() {
        return false;
    }

    @Override
    protected boolean hasBackButton() {
        return true;
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        OkHttpUtils.getInstance().cancelTag("getFansList");
    }
}

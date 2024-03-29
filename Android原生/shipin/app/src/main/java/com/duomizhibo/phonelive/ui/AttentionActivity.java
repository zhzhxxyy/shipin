package com.duomizhibo.phonelive.ui;

import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.duomizhibo.phonelive.adapter.UserBaseInfoAdapter;
import com.duomizhibo.phonelive.api.remote.ApiUtils;
import com.duomizhibo.phonelive.base.ToolBarBaseActivity;
import com.duomizhibo.phonelive.bean.SimpleUserInfo;
import com.duomizhibo.phonelive.bean.UserBean;
import com.duomizhibo.phonelive.ui.customviews.ActivityTitle;
import com.duomizhibo.phonelive.utils.UIHelper;
import com.google.gson.Gson;
import com.duomizhibo.phonelive.AppContext;
import com.duomizhibo.phonelive.R;
import com.duomizhibo.phonelive.api.remote.PhoneLiveApi;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import butterknife.InjectView;
import okhttp3.Call;

/**
 * 关注列表
 */
public class AttentionActivity extends ToolBarBaseActivity {
    @InjectView(R.id.lv_attentions)
    ListView mAttentionView;
    @InjectView(R.id.fensi)
    LinearLayout mLlFensi;
    @InjectView(R.id.load)
    LinearLayout mLoad;
    @InjectView(R.id.sr_refresh)
    SwipeRefreshLayout mRefreshLayout;

    @InjectView(R.id.view_title)
    ActivityTitle mActivityTitle;

    private List<SimpleUserInfo> mAttentionList = new ArrayList<>();

    @Override
    protected int getLayoutId() {
        return R.layout.activity_attention;
    }

    @Override
    public void initView() {
        mRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.global));
        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if(mAttentionList != null){
                    mAttentionList.clear();
                }

                requestGetData();
                mRefreshLayout.setRefreshing(false);
            }
        });

        mAttentionView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                UIHelper.showHomePageActivity(AttentionActivity.this,mAttentionList.get(position).id);
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
        setActionBarTitle(getResources().getString(R.string.attention));

    }

    private void requestGetData() {
        StringCallback callback = new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                mLlFensi.setVisibility(View.GONE);
                mLoad.setVisibility(View.VISIBLE);
                mAttentionView.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onResponse(String response,int id) {
                JSONArray res = ApiUtils.checkIsSuccess(response);
                mAttentionList.clear();

                if(res != null){
                    mAttentionList.addAll(ApiUtils.formatDataToList2(res,SimpleUserInfo.class));
                }
                if (mAttentionList.size()>0){
                    fillUI();
                }else{
                    mLlFensi.setVisibility(View.VISIBLE);
                    mLoad.setVisibility(View.GONE);
                    mAttentionView.setVisibility(View.INVISIBLE);
                }
            }
        };
        PhoneLiveApi.getAttentionList(getUserID(),getIntent().getStringExtra("uid"), callback);
    }

    private void fillUI() {

        mLlFensi.setVisibility(View.GONE);
        mLoad.setVisibility(View.GONE);
        mAttentionView.setVisibility(View.VISIBLE);
        mAttentionView.setAdapter(new UserBaseInfoAdapter(mAttentionList));


    }

    @Override
    public void onClick(View v) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        requestGetData();
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
        OkHttpUtils.getInstance().cancelTag("getAttentionList");
    }
}

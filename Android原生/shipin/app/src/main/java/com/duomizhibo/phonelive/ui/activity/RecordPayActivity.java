package com.duomizhibo.phonelive.ui.activity;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.duomizhibo.phonelive.AppContext;
import com.duomizhibo.phonelive.R;
import com.duomizhibo.phonelive.adapter.video.RecordPayAdapter;
import com.duomizhibo.phonelive.api.remote.ApiUtils;
import com.duomizhibo.phonelive.api.remote.PhoneLiveApi;
import com.duomizhibo.phonelive.base.ToolBarBaseActivity;
import com.duomizhibo.phonelive.bean.video.BaseData;
import com.duomizhibo.phonelive.bean.video.RecordPay;
import com.duomizhibo.phonelive.bean.video.RecordPayList;
import com.duomizhibo.phonelive.ui.customviews.ActivityTitle;
import com.duomizhibo.phonelive.ui.customviews.RefreshLayout;
import com.duomizhibo.phonelive.utils.TLog;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.List;

import butterknife.InjectView;
import okhttp3.Call;


public class RecordPayActivity extends ToolBarBaseActivity implements RefreshLayout.OnRefreshListener {

    @InjectView(R.id.view_title)
    ActivityTitle mActivityTitle;

    @InjectView(R.id.mRefreshLayout)
    RefreshLayout mRefreshLayout;
    @InjectView(R.id.mRecyclerView)
    RecyclerView mRecyclerView;
    @InjectView(R.id.view_fail)
    View view_fail;
    @InjectView(R.id.tv_fail)
    TextView tv_fail;
    @InjectView(R.id.iv_no_data)
    ImageView iv_no_data;


    private RecordPayAdapter adapter;
    private int page=1;
    private List<RecordPay> list = new ArrayList<RecordPay>();


    @Override
    protected int getLayoutId() {
        return R.layout.new_activity_record_pay;
    }

    @Override
    public void initView() {
        mActivityTitle.setReturnListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 1));
        mRefreshLayout.setScorllView(mRecyclerView);
        mRefreshLayout.setOnRefreshListener(this);
        adapter = new RecordPayAdapter(this, list);
        mRecyclerView.setAdapter(adapter);
    }

    @Override
    public void initData() {
        mRefreshLayout.beginRefresh();
    }

//    @OnClick({R.id.btn_doregister, R.id.btn_dologin})
    @Override
    public void onClick(View v) {


    }


    @Override
    protected boolean hasActionBar() {
        return false;
    }


    @Override
    public void onRefresh() {
        page=1;
        PhoneLiveApi.recordPay(page,10,AppContext.getInstance().getToken(),mLoadMoreCallback);

    }

    @Override
    public void onLoadMore() {
        page++;
        PhoneLiveApi.recordPay(page,10,AppContext.getInstance().getToken(),mLoadMoreCallback);

    }

    private StringCallback mLoadMoreCallback=new StringCallback() {
        @Override
        public void onError(Call call, Exception e, int id) {
            mRefreshLayout.completeRefresh();
            if(list.size()>0){
                view_fail.setVisibility(View.GONE);
                iv_no_data.setVisibility(View.GONE);
            }else{
                tv_fail.setText("获取数据失败");
                view_fail.setVisibility(View.VISIBLE);
                iv_no_data.setVisibility(View.GONE);
            }
            AppContext.toast("获取数据失败");
        }

        @Override
        public void onResponse(String response, int id) {
            mRefreshLayout.completeRefresh();
            BaseData baseData= ApiUtils.getBaseData(response);
            if(!baseData.isSuccess()){
                //todo 失败
                if(list.size()>0){
                    view_fail.setVisibility(View.GONE);
                    iv_no_data.setVisibility(View.GONE);
                }else{
                    iv_no_data.setVisibility(View.GONE);
                    tv_fail.setText(baseData.getRespMsg());
                    view_fail.setVisibility(View.VISIBLE);
                }
                AppContext.toast(baseData.getRespMsg());
                return;
            }

            RecordPayList recordPayList=null;
            try {
                recordPayList=com.alibaba.fastjson.JSONObject.parseObject(baseData.getData(), RecordPayList.class);
            } catch (Exception e) {

            }
            if(page==1){
                list.clear();
            }
            if(recordPayList!=null&&recordPayList.getRows()!=null&&recordPayList.getRows().size()>0){
                list.addAll(recordPayList.getRows());
            }else{
                AppContext.toast("已经没有更多数据了");
            }
            adapter.setData(list);
            if(list.size()>0){
                view_fail.setVisibility(View.GONE);
                iv_no_data.setVisibility(View.GONE);
            }else{
                view_fail.setVisibility(View.GONE);
                iv_no_data.setVisibility(View.VISIBLE);
            }
        }
    };
}

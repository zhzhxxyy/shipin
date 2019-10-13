package com.duomizhibo.phonelive.fragment.video;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.duomizhibo.phonelive.AppContext;
import com.duomizhibo.phonelive.R;
import com.duomizhibo.phonelive.api.remote.ApiUtils;
import com.duomizhibo.phonelive.api.remote.PhoneLiveApi;
import com.duomizhibo.phonelive.base.AbsFragment;
import com.duomizhibo.phonelive.bean.video.BaseData;
import com.duomizhibo.phonelive.bean.video.HomeObject;
import com.duomizhibo.phonelive.bean.video.ShareInfo;
import com.duomizhibo.phonelive.ui.activity.LoginActivity;
import com.duomizhibo.phonelive.ui.activity.NewMainActivity;
import com.duomizhibo.phonelive.utils.UIHelper;
import com.zhy.http.okhttp.callback.StringCallback;

import okhttp3.Call;


public class HomeXuanchuanFragment extends AbsFragment implements NewMainActivity.OnResumeCallback,View.OnClickListener,SwipeRefreshLayout.OnRefreshListener{

    private View mRootView;
    private SwipeRefreshLayout refreshLayout;
    private View ll_xuanchuan;
    private TextView tv_reward;
    private TextView tv_num;
    private TextView tv_url;
    private TextView tv_money;
    private View view_copy;
    private View view_fail;
    private TextView tv_fail;
    private  ShareInfo shareInfo=null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.new_fragment_home_xuanchuan, container, false);
        return mRootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView(savedInstanceState);
    }

    private void initView(Bundle savedInstanceState) {
        ll_xuanchuan =  mRootView.findViewById(R.id.ll_xuanchuan);
        refreshLayout =  (SwipeRefreshLayout)mRootView.findViewById(R.id.refresh_layout);
        refreshLayout.setColorSchemeResources(R.color.refresh_layout_color);
        refreshLayout.setOnRefreshListener(this);
        tv_reward = (TextView) mRootView.findViewById(R.id.tv_reward);
        tv_num = (TextView) mRootView.findViewById(R.id.tv_num);
        tv_url =(TextView)  mRootView.findViewById(R.id.tv_url);
        tv_money = (TextView) mRootView.findViewById(R.id.tv_money);
        view_copy = mRootView.findViewById(R.id.view_copy);
        view_fail = mRootView.findViewById(R.id.view_fail);
        tv_fail = (TextView)mRootView.findViewById(R.id.tv_fail);
        view_copy.setOnClickListener(this);
        view_fail.setOnClickListener(this);
        ll_xuanchuan.setVisibility(View.GONE);
        view_fail.setVisibility(View.GONE);
        onRefresh();
    }



    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);

    }

    @Override
    public void onResumeRefresh() {
        getShareInfo();
    }

    @Override
    public void onRefresh() {

        getShareInfo();
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.view_copy){
            String url=tv_url.getText().toString().trim();
            ClipboardManager mClipboardManager = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
            Uri uri=Uri.parse(url);
            ClipData clipData = ClipData.newUri(getActivity().getContentResolver(), "copy from demo", uri);
            mClipboardManager.setPrimaryClip(clipData);
            AppContext.showToast("复制成功，可以宣传给朋友们了。");
        }else if(v.getId()==R.id.view_fail){
            if(!AppContext.getInstance().isLogin()){
                UIHelper.showLogin(getActivity());
            }else{
                onRefresh();
            }
        }
    }

    private void getShareInfo(){
        view_fail.setVisibility(View.GONE);
        PhoneLiveApi.shareInfo(AppContext.getInstance().getToken(), callback);
    }



    //获取分享信息
    private final StringCallback callback = new StringCallback() {
        @Override
        public void onError(Call call, Exception e, int id) {
            refreshLayout.setRefreshing(false);
            dataToView(0,"网络请求出错!");
            AppContext.showToast("网络请求出错!");
        }
        @Override
        public void onResponse(String s, int id) {
            refreshLayout.setRefreshing(false);
            BaseData baseData= ApiUtils.getBaseData(s);
            if(!baseData.isSuccess()){
                shareInfo=null;
            }else{
                try {
                    shareInfo=com.alibaba.fastjson.JSONObject.parseObject(baseData.getData(), ShareInfo.class);
                } catch (Exception e) {
                    shareInfo=null;
                }
            }
            dataToView(baseData.getRespCode(),baseData.getRespMsg());
        }
    };


    private void dataToView(int respCode,String msg){
        if(shareInfo==null){
            tv_fail.setText(msg);
            view_fail.setVisibility(View.VISIBLE);
            ll_xuanchuan.setVisibility(View.GONE);
        }else{
            view_fail.setVisibility(View.GONE);
            ll_xuanchuan.setVisibility(View.VISIBLE);
            String contentReward="1.将宣传链接推送出去，当别人打开您当链接，您将会得到奖励<font color='#fa751e',font-size='16px'>"+shareInfo.getPropaganda_reward()+"</font>个金币。";
            tv_reward.setText(Html.fromHtml(contentReward));

            String contentNum="2.每天有效当宣传奖励共有<font color='#fa751e',font-size='16px'>"+shareInfo.getShare_num()+"</font>次，超过限制次数后将没有奖励。";
            tv_num.setText(Html.fromHtml(contentNum));

            String contentMoney="我的总金币：<font color='#fa751e',font-size='16px'>"+shareInfo.getMoney()+"</font>";
            tv_money.setText(Html.fromHtml(contentMoney));

            tv_url.setText(shareInfo.getShare_url()+"");
        }
    }
}

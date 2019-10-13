package com.duomizhibo.phonelive.ui;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.duomizhibo.phonelive.AppConfig;
import com.duomizhibo.phonelive.AppContext;
import com.duomizhibo.phonelive.api.remote.ApiUtils;
import com.duomizhibo.phonelive.api.remote.PhoneLiveApi;
import com.duomizhibo.phonelive.base.ToolBarBaseActivity;
import com.duomizhibo.phonelive.bean.ProfitBean;
import com.duomizhibo.phonelive.bean.UserBean;
import com.duomizhibo.phonelive.ui.customviews.ActivityTitle;
import com.duomizhibo.phonelive.utils.DialogHelp;
import com.duomizhibo.phonelive.utils.UIHelper;
import com.google.gson.Gson;
import com.duomizhibo.phonelive.R;
import com.duomizhibo.phonelive.widget.BlackTextView;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;

import butterknife.InjectView;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * 收益
 */
public class UserProfitActivity extends ToolBarBaseActivity {

    @InjectView(R.id.tv_votes)
    BlackTextView mVotes;
    @InjectView(R.id.tv_profit_canwithdraw)
    BlackTextView mCanwithDraw;
    @InjectView(R.id.tv_profit_withdraw)
    BlackTextView mWithDraw;

    @InjectView(R.id.view_title)
    ActivityTitle mActivityTitle;
    private ProfitBean mProfitBean;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_profit;
    }

    @Override
    public void initView() {
        mActivityTitle.setReturnListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        ((TextView)findViewById(R.id.tv_profit_tick_name)).setText(AppConfig.TICK_NAME);
    }

    @Override
    public void initData() {

    }

    private void  requestData() {

        PhoneLiveApi.getWithdraw(getUserID(),getUserToken(), new StringCallback() {
            @Override
            public void onError(Call call, Exception e,int id) {

            }

            @Override
            public void onResponse(String response,int id) {
                JSONArray res = ApiUtils.checkIsSuccess(response);

                if(null != res){
                    try {
                        mProfitBean = new Gson().fromJson(res.getString(0),ProfitBean.class);
                        fillUI();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }
        });
    }

    private void fillUI() {
        mCanwithDraw.setText(mProfitBean.total);
        mWithDraw.setText(mProfitBean.todaycash);
        mVotes.setText(mProfitBean.votes);
    }



    @OnClick({R.id.btn_profit_cash,R.id.tv_common_problem})
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_profit_cash:

                //UIHelper.showRequestCashActivity(UserProfitActivity.this);
                showWaitDialog2("正在提交信息",false);
                PhoneLiveApi.requestCash(getUserID(),getUserToken(),"",
                        new StringCallback(){

                            @Override
                            public void onError(Call call, Exception e,int id) {
                                hideWaitDialog();
                                AppContext.showToastAppMsg(UserProfitActivity.this,"接口请求失败");
                            }

                            @Override
                            public void onResponse(String response,int id) {
                                hideWaitDialog();
                                JSONArray res = ApiUtils.checkIsSuccess(response);
                                if(null != res){
                                    try {
                                        DialogHelp.getMessageDialog(UserProfitActivity.this,res.getJSONObject(0).getString("msg"))
                                                .create()
                                                .show();
                                        requestData();
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        });
                break;

            //常见问题
            case R.id.tv_common_problem:
                String model = android.os.Build.MODEL;
                String release = android.os.Build.VERSION.RELEASE;
                UIHelper.showWebView(this, AppConfig.MAIN_URL + "/index.php?g=portal&m=page&a=newslist&uid="
                        + getUserID() + "&version=" + release + "&model=" + model,"");
                break;
        }


    }

    @Override
    protected boolean hasActionBar() {
        return false;
    }

    @Override
    public void onResume() {

        super.onResume();
        requestData();

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        OkHttpUtils.getInstance().cancelTag("getWithdraw");
    }
}

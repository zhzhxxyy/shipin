package com.duomizhibo.phonelive.ui.activity;

import android.view.View;

import com.duomizhibo.phonelive.AppContext;
import com.duomizhibo.phonelive.R;
import com.duomizhibo.phonelive.api.remote.ApiUtils;
import com.duomizhibo.phonelive.api.remote.PhoneLiveApi;
import com.duomizhibo.phonelive.base.ToolBarBaseActivity;
import com.duomizhibo.phonelive.bean.video.BaseData;
import com.duomizhibo.phonelive.bean.video.MemberInfo;
import com.duomizhibo.phonelive.ui.customviews.ActivityTitle;
import com.duomizhibo.phonelive.utils.LoginUtils;
import com.duomizhibo.phonelive.utils.TDevice;
import com.duomizhibo.phonelive.utils.TLog;
import com.duomizhibo.phonelive.utils.UIHelper;
import com.duomizhibo.phonelive.widget.BlackEditText;
import com.zhy.http.okhttp.callback.StringCallback;

import butterknife.InjectView;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * 手机登陆
 */
public class LoginActivity extends ToolBarBaseActivity {

    @InjectView(R.id.view_title)
    ActivityTitle mActivityTitle;

    @InjectView(R.id.et_loginphone)
    BlackEditText mEtUserPhone;

    @InjectView(R.id.et_password)
    BlackEditText mEtUserPassword;



    @Override
    protected int getLayoutId() {
        return R.layout.new_activity_login;
    }

    @Override
    public void initView() {

        mActivityTitle.setMoreListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UIHelper.showReg(LoginActivity.this);
            }
        });

    }

    @Override
    public void initData() {

    }


    @OnClick({R.id.btn_dologin, R.id.btn_doReg,R.id.tv_login_clause})
    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.btn_dologin) {
            if (prepareForLogin()) {
                return;
            }
            String mUserName = mEtUserPhone.getText().toString().trim();
            String mPassword = mEtUserPassword.getText().toString().trim();
            showWaitDialog(R.string.loading);
            PhoneLiveApi.loginNew(mUserName, mPassword, callback);

        } else if (v.getId() == R.id.btn_doReg) {

            UIHelper.showReg(LoginActivity.this);

        }else if(v.getId()==R.id.tv_login_clause){
//            UIHelper.showNewWebView();
        }

    }



    //登录回调
    private final StringCallback callback = new StringCallback() {
        @Override
        public void onError(Call call, Exception e, int id) {
            hideWaitDialog();
            AppContext.showToast("网络请求出错!");
        }
        @Override
        public void onResponse(String s, int id) {
            hideWaitDialog();
            BaseData baseData= ApiUtils.getBaseData(s);
            if(baseData.isSuccess()){
                MemberInfo memberInfo=null;
                try {
                    memberInfo=com.alibaba.fastjson.JSONObject.parseObject(baseData.getData(), MemberInfo.class);
                } catch (Exception e) {
                    memberInfo=null;
                }
                if(memberInfo==null){
                    AppContext.showToast("解析失败");
                    return;
                }
                AppContext.getInstance().saveUserInfoNew(memberInfo);
                //LoginUtils.getInstance().OtherInit(LoginActivity.this);
                UIHelper.showNewMainActivity(LoginActivity.this);
            }else{
                AppContext.showToast(baseData.getRespMsg());
            }

        }
    };

    private boolean prepareForLogin() {
        if (!TDevice.hasInternet()) {
            AppContext.showToastShort(R.string.tip_no_internet);
            return true;
        }

        if (mEtUserPhone.length() == 0) {
            mEtUserPhone.setError("请输入登录账号");
            mEtUserPhone.requestFocus();
            return true;
        }

        if (mEtUserPassword.length() == 0) {
            mEtUserPassword.setError("请输入登录密码");
            mEtUserPassword.requestFocus();
            return true;
        }

        return false;
    }

    @Override
    protected boolean hasActionBar() {
        return false;
    }


}

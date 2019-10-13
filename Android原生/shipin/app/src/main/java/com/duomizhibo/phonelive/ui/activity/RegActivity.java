package com.duomizhibo.phonelive.ui.activity;

import android.view.View;

import com.duomizhibo.phonelive.AppContext;
import com.duomizhibo.phonelive.R;
import com.duomizhibo.phonelive.api.remote.ApiUtils;
import com.duomizhibo.phonelive.api.remote.PhoneLiveApi;
import com.duomizhibo.phonelive.base.ToolBarBaseActivity;
import com.duomizhibo.phonelive.bean.video.BaseData;
import com.duomizhibo.phonelive.ui.customviews.ActivityTitle;
import com.duomizhibo.phonelive.utils.DialogHelp;
import com.duomizhibo.phonelive.utils.TDevice;
import com.duomizhibo.phonelive.widget.BlackEditText;
import com.zhy.http.okhttp.callback.StringCallback;

import butterknife.InjectView;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * 手机登陆 HHH 2016-09-09
 */
public class RegActivity extends ToolBarBaseActivity {

    @InjectView(R.id.et_username)
    BlackEditText mEtUsername;
    @InjectView(R.id.et_nickname)
    BlackEditText mEtNickname;
    @InjectView(R.id.et_password)
    BlackEditText mEtPassword;
    @InjectView(R.id.et_confirm_password)
    BlackEditText mEtConfirmPassword;
    @InjectView(R.id.view_title)
    ActivityTitle mActivityTitle;

    private long clickTime=0;//点击时间
    private int minDuringClickTime=500;//最短的点击时间 毫秒

    @Override
    protected int getLayoutId() {
        return R.layout.new_activity_reg;
    }


    @Override
    public void initView() {

        mActivityTitle.setReturnListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }


    @Override
    public void initData() {
        setActionBarTitle("注册会员");
    }

    @OnClick({R.id.btn_doregister, R.id.btn_dologin})
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_doregister) {
            if(Math.abs(System.currentTimeMillis()-clickTime)<minDuringClickTime){
                return;
            }
            if (prepareForReg()) {
                return;
            }
            String username = mEtUsername.getText().toString().trim();
            String nickname = mEtNickname.getText().toString().trim();
            String password = mEtPassword.getText().toString().trim();
            String confirm_password = mEtConfirmPassword.getText().toString().trim();
            showWaitDialog(R.string.loading);
            PhoneLiveApi.regNew(username, nickname, password, confirm_password, callback);
        }else if(v.getId() == R.id.btn_dologin){
            finish();
        }


    }

    //注册回调
    private final StringCallback callback = new StringCallback() {
        @Override
        public void onError(Call call, Exception e, int id) {
            AppContext.showToast("网络请求出错!");
        }

        @Override
        public void onResponse(String s, int id) {
            hideWaitDialog();
            BaseData baseData= ApiUtils.getBaseData(s);
            if(baseData.isSuccess()){
                showToast3("注册成功", 0);
                finish();
            }else{
                AppContext.showToast(baseData.getRespMsg());
            }
        }
    };

    //HHH 2016-09-09
    private boolean prepareForReg() {
        if (!TDevice.hasInternet()) {
            DialogHelp.getMessageDialog(this, getResources().getString(R.string.tip_no_internet))
                    .create().show();
            return true;
        }

        if (mEtUsername.length() == 0) {
            mEtUsername.setError("请输入用户名");
            mEtUsername.requestFocus();
            return true;
        }

        if (mEtNickname.length() == 0) {
            mEtNickname.setError("请输入昵称");
            mEtNickname.requestFocus();
            return true;
        }

        if (mEtPassword.length() == 0) {
            mEtPassword.setError("请输入密码");
            mEtPassword.requestFocus();
            return true;
        }

        if (!mEtConfirmPassword.getText().toString().equals(mEtPassword.getText().toString())) {
            mEtConfirmPassword.setText("");
            mEtConfirmPassword.setError("密码不一致，请重新输入");
            mEtConfirmPassword.requestFocus();
            return true;
        }

        return false;
    }


    @Override
    protected boolean hasActionBar() {
        return false;
    }
}

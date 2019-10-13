package com.duomizhibo.phonelive.ui;

import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.hyphenate.util.NetUtils;
import com.duomizhibo.phonelive.AppContext;
import com.duomizhibo.phonelive.api.remote.ApiUtils;
import com.duomizhibo.phonelive.base.ToolBarBaseActivity;
import com.duomizhibo.phonelive.bean.UserBean;
import com.duomizhibo.phonelive.ui.customviews.ActivityTitle;
import com.duomizhibo.phonelive.utils.DialogHelp;
import com.duomizhibo.phonelive.utils.LoginUtils;
import com.duomizhibo.phonelive.utils.SimpleUtils;
import com.duomizhibo.phonelive.utils.StringUtils;
import com.duomizhibo.phonelive.utils.TDevice;
import com.google.gson.Gson;
import com.duomizhibo.phonelive.R;
import com.duomizhibo.phonelive.api.remote.PhoneLiveApi;
import com.duomizhibo.phonelive.widget.BlackButton;
import com.duomizhibo.phonelive.widget.BlackEditText;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;

import butterknife.InjectView;
import butterknife.OnClick;
import okhttp3.Call;

import static com.duomizhibo.phonelive.api.remote.ApiUtils.SUCCESS_CODE;

/**
 * 手机登陆 HHH 2016-09-09
 */
public class PhoneRegActivity extends ToolBarBaseActivity {
    @InjectView(R.id.et_loginphone)
    BlackEditText mEtUserPhone;
    @InjectView(R.id.et_logincode)
    BlackEditText mEtCode;
    @InjectView(R.id.btn_phone_login_send_code)
    TextView mBtnSendCode;

    @InjectView(R.id.et_password)
    BlackEditText mEtUserPassword;
    @InjectView(R.id.et_secondPassword)
    BlackEditText mEtSecondPassword;

    @InjectView(R.id.view_title)
    ActivityTitle mActivityTitle;


    //HHH 2016-09-09
    private String mUserName = "";


    @Override
    protected int getLayoutId() {
        return R.layout.activity_reg;
    }

    @Override
    public void initView() {
        mBtnSendCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendCode();
            }
        });

        mActivityTitle.setReturnListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void sendCode() {

        mUserName = mEtUserPhone.getText().toString();

        if (!mUserName.equals("") && mUserName.length() == 11) {

            if (!NetUtils.hasNetwork(PhoneRegActivity.this)) {
                showToast3("请检查网络设置", 0);
                return;
            }

            PhoneLiveApi.getMessageCode(mUserName, "Login.getCode", new StringCallback() {
                @Override
                public void onError(Call call, Exception e, int id) {

                }

                @Override
                public void onResponse(String response, int id) {
                    JSONArray res = ApiUtils.checkIsSuccess(response);
                    if (res != null) {

                        showToast3(getString(R.string.codehasbeensend), 0);
                        SimpleUtils.startTimer(new WeakReference<TextView>(mBtnSendCode), "发送验证码", 60, 1);
                    }
                }
            });

        } else {
            showToast3(getString(R.string.plase_check_you_num_is_correct), 0);
        }

    }


    @Override
    public void initData() {
        setActionBarTitle("用户注册");
    }

    @OnClick(R.id.btn_doReg)
    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.btn_doReg) {

            if (prepareForReg()) {
                return;
            }
            mUserName = mEtUserPhone.getText().toString();
            String mCode = mEtCode.getText().toString();
            String mPassword = mEtUserPassword.getText().toString();
            String mSecondPassword = mEtSecondPassword.getText().toString();
            showWaitDialog(R.string.loading);
            PhoneLiveApi.reg(mUserName, mPassword, mSecondPassword, mCode, callback);
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
            JSONArray res = ApiUtils.checkIsSuccess(s);
            if (res!=null){
                showToast3("注册成功", 0);
                finish();
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
        if (mEtUserPhone.length() == 0) {
            mEtUserPhone.setError("请输入手机号码/用户名");
            mEtUserPhone.requestFocus();
            return true;
        }

        if (mEtCode.length() == 0) {
            mEtCode.setError("请输入验证码");
            mEtCode.requestFocus();
            return true;
        }

        if (mEtUserPassword.length() == 0) {
            mEtUserPassword.setError("请输入密码");
            mEtUserPassword.requestFocus();
            return true;
        }

        if (!mEtSecondPassword.getText().toString().equals(mEtUserPassword.getText().toString())) {
            mEtSecondPassword.setText("");
            mEtSecondPassword.setError("密码不一致，请重新输入");
            mEtSecondPassword.requestFocus();
            return true;
        }

        return false;
    }


    @Override
    protected boolean hasActionBar() {
        return false;
    }
}

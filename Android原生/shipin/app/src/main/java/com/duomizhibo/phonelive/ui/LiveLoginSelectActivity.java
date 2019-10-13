package com.duomizhibo.phonelive.ui;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.widget.ImageView;

import com.mob.MobSDK;
import com.duomizhibo.phonelive.AppConfig;
import com.duomizhibo.phonelive.bean.UserBean;
import com.duomizhibo.phonelive.utils.SharedPreUtil;
import com.google.gson.Gson;
import com.duomizhibo.phonelive.AppContext;
import com.duomizhibo.phonelive.R;
import com.duomizhibo.phonelive.api.remote.ApiUtils;
import com.duomizhibo.phonelive.api.remote.PhoneLiveApi;
import com.duomizhibo.phonelive.base.ToolBarBaseActivity;
import com.duomizhibo.phonelive.utils.LoginUtils;
import com.duomizhibo.phonelive.utils.UIHelper;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import butterknife.InjectView;
import butterknife.OnClick;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.PlatformDb;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.wechat.friends.Wechat;
import okhttp3.Call;


//登录选择页面
public class LiveLoginSelectActivity extends ToolBarBaseActivity implements PlatformActionListener {
    private String[] names = {QQ.NAME,Wechat.NAME, SinaWeibo.NAME};
    private String type;
    @InjectView(R.id.iv_select_login_bg)
    ImageView mBg;

    @InjectView(R.id.iv_qqlogin)
    ImageView mIvQQLogin;

    @InjectView(R.id.iv_wxlogin)
    ImageView mIvWxLogin;

    @InjectView(R.id.iv_sllogin)
    ImageView mIvSlLogin;

    private Bitmap bmp;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_show_login;
    }


    @Override
    public void initView() {

        bmp = null;
        bmp = BitmapFactory.decodeResource(getResources(),R.drawable.live_show_login_bg);
        mBg.setImageBitmap(bmp);

    }

    @Override
    public void initData() {


        PhoneLiveApi.requestOtherLoginStatus(new StringCallback(){

            @Override
            public void onError(Call call, Exception e, int id) {

            }

            @Override
            public void onResponse(String response, int id) {

                JSONArray res = ApiUtils.checkIsSuccess(response);
                if(res != null){
                    try {
                        JSONObject object = res.getJSONObject(0);
                        if(object.getInt("login_qq") == 1){
                            mIvQQLogin.setVisibility(View.VISIBLE);
                        }
                        if(object.getInt("login_sina") == 1){
                            mIvSlLogin.setVisibility(View.VISIBLE);
                        }
                        if(object.getInt("login_wx") == 1){
                            mIvWxLogin.setVisibility(View.VISIBLE);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    @Override
    @OnClick({R.id.tv_login_clause,R.id.iv_qqlogin,R.id.iv_sllogin,R.id.iv_wxlogin,R.id.iv_mblogin})
    public void onClick(View v) {
        int id = v.getId();
        MobSDK.init(this);
        switch (id){
            case R.id.tv_login_clause:
                //条款
                UIHelper.showWebView(LiveLoginSelectActivity.this, AppConfig.MAIN_URL + "/index.php?g=portal&m=page&a=index&id=3","");
                break;
            case R.id.iv_qqlogin:
                type = "qq";
                otherLogin(names[0]);
                break;
            case R.id.iv_sllogin:
                type = "sina";
                AppContext.showToastAppMsg(this,"微博");
                otherLogin(names[2]);
                break;
            case R.id.iv_wxlogin:
                type = "wx";
                AppContext.showToastAppMsg(this,"微信");
                otherLogin(names[1]);
                break;
            case R.id.iv_mblogin:
                UIHelper.showMobilLogin(this);
                break;
        }
    }
    private void otherLogin(String name){
        showWaitDialog("正在授权登录...",false);
        Platform other = ShareSDK.getPlatform(name);
        other.showUser(null);//执行登录，登录后在回调里面获取用户资料
        other.SSOSetting(false);  //设置false表示使用SSO授权方式
        other.setPlatformActionListener(this);
        other.removeAccount(true);
    }

    @Override
    public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                hideWaitDialog();
                showToast3("授权成功正在登录....",0);
            }
        });

        //用户资源都保存到res
        //通过打印res数据看看有哪些数据是你想要的
        if (i == Platform.ACTION_USER_INFOR) {
            //showWaitDialog("正在登录...");
            PlatformDb platDB = platform.getDb();//获取数平台数据DB
            //通过DB获取各种数据
            PhoneLiveApi.otherLogin(type,platDB,new StringCallback() {
                @Override
                public void onError(Call call, Exception e,int id) {
                    showToast3("登录失败",0);

                }

                @Override
                public void onResponse(String response,int id) {

                    JSONArray requestRes = ApiUtils.checkIsSuccess(response);
                    if(requestRes != null){
                        Gson gson = new Gson();

                        try {
                            UserBean user  = gson.fromJson(requestRes.getString(0), UserBean.class);

                            AppContext.getInstance().saveUserInfo(user);

                            LoginUtils.getInstance().OtherInit(LiveLoginSelectActivity.this);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                }
            });
        }

    }

    @Override
    public void onError(Platform platform, int i, Throwable throwable) {
        hideWaitDialog();
        showToast3("授权登录失败",0);
    }

    @Override
    public void onCancel(Platform platform, int i) {
        hideWaitDialog();
        showToast3("授权已取消",0);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (bmp!=null)
        bmp.recycle();
        OkHttpUtils.getInstance().cancelTag("requestOtherLoginStatus");
    }

}

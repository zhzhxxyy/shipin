package com.duomizhibo.phonelive;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.view.View;

import com.duomizhibo.phonelive.api.remote.ApiUtils;
import com.duomizhibo.phonelive.api.remote.PhoneLiveApi;
import com.duomizhibo.phonelive.base.ToolBarBaseActivity;
import com.duomizhibo.phonelive.bean.video.BaseData;
import com.duomizhibo.phonelive.bean.video.MemberInfo;
import com.duomizhibo.phonelive.ui.activity.LoginActivity;
import com.duomizhibo.phonelive.ui.activity.NewMainActivity;
import com.duomizhibo.phonelive.ui.activity.VideoPlayerNewActivity;
import com.duomizhibo.phonelive.utils.DeviceUtils;
import com.duomizhibo.phonelive.utils.PermissionPageUtils;
import com.duomizhibo.phonelive.utils.StringUtils;
import com.duomizhibo.phonelive.utils.TLog;
import com.duomizhibo.phonelive.utils.UIHelper;
import com.duomizhibo.phonelive.widget.alert.BaseDialog;
import com.duomizhibo.phonelive.widget.alert.TipDialog;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.List;

import kr.co.namee.permissiongen.PermissionFail;
import kr.co.namee.permissiongen.PermissionGen;
import kr.co.namee.permissiongen.PermissionSuccess;
import okhttp3.Call;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * 应用启动界面
 *
 */
public class AppStart  extends ToolBarBaseActivity {

    private View ll_progress;
    private int processNum;
    //字体库
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onResume() {
        super.onResume();
//        JPushInterface.onResume(this);

    }

    @Override
    protected void onPause() {
        super.onPause();
//        JPushInterface.onPause(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        processNum=0;
        checkPermission();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.app_start;
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void initView() {
        ll_progress=findViewById(R.id.ll_progress);

//        //checkAPP(this);
//        // SystemTool.gc(this); //针对性能好的手机使用，加快应用相应速度
//        final View view = View.inflate(this, R.layout.app_start, null);
//        setContentView(view);
//        if(false){
//            Intent intent = new Intent(this, VideoPlayerTestActivity.class);
//            startActivity(intent);
//            finish();
//            return;
//        }
//        AlphaAnimation aa = new AlphaAnimation(0.5f, 1.0f);
//        aa.setDuration(800);
//        view.startAnimation(aa);
//        aa.setAnimationListener(new AnimationListener() {
//            @Override
//            public void onAnimationEnd(Animation arg0) {
//                redirectTo();
//            }
//            @Override
//            public void onAnimationRepeat(Animation animation) {}
//
//            @Override
//            public void onAnimationStart(Animation animation) {}
//        });
    }

    @Override
    public void initData() {

    }

    @Override
    protected boolean hasActionBar() {
        return false;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionGen.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
    }


    // 跳转到.......
    private void redirectTo() {
//      if(!AppContext.getInstance().isLogin()){
//           // UIHelper.showLoginSelectActivity(this);
//            UIHelper.showLogin(this);
//            finish();
//            return;
//        }
//        EMClient.getInstance().groupManager().loadAllGroups();
//        EMClient.getInstance().chatManager().loadAllConversations();
        if(processNum<2){
            return;
        }
        Intent intent = new Intent(this, NewMainActivity.class);
        startActivity(intent);
        finish();
    }

    private void checkRoute(){
        PhoneLiveApi.checkRoute(AppContext.getInstance().getToken(), new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                ++processNum;
                redirectTo();
            }

            @Override
            public void onResponse(String response, int id) {
                BaseData baseData= ApiUtils.getBaseData(response);
                if(baseData.isSuccess()){
                    List<String> routeList=null;
                    try {
                        routeList=com.alibaba.fastjson.JSONObject.parseArray(baseData.getData(), String.class);
                    } catch (Exception e) {
                    }
                    if(routeList!=null&&routeList.size()>0){
                        AppConfig.URL_COMMON=routeList.get(0);
                    }
                }
                ++processNum;
                redirectTo();
            }
        });
    }

    private void saveDeviceId(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    //获取保存在sd中的 设备唯一标识符 (第一次没有)
                    String readDeviceID = DeviceUtils.readDeviceID(AppStart.this);
                    //获取缓存在  sharepreference 里面的 设备唯一标识
                    String string = AppContext.getInstance().getProperty("device.uuid");
                    if(StringUtils.isEmpty(string)){
                        string=readDeviceID;
                    }
                    //判断 app 内部是否已经缓存,  若已经缓存则使用app 缓存的 设备id
                    if (!StringUtils.isEmpty(string)&&!string.equals(readDeviceID)) {
                        //app 缓存的和SD卡中保存的不相同 以app 保存的为准, 同时更新SD卡中保存的 唯一标识符
                        readDeviceID = string;
                        DeviceUtils.saveDeviceID(readDeviceID, AppStart.this);
                    }
                    // app 没有缓存 (这种情况只会发生在第一次启动的时候)
                    if (StringUtils.isEmpty(readDeviceID)) {
                        //保存设备id
                        readDeviceID = DeviceUtils.getDeviceId(AppStart.this);
                    }
                    //左后再次更新app 的缓存
                    AppContext.getInstance().setProperty("device.uuid",readDeviceID);
                    AppConfig.GLOBAL_UUID=readDeviceID;

                    handler.sendEmptyMessage(0);
                } catch (Exception e) {
                    e.printStackTrace();
                    handler.sendEmptyMessage(1);
                }
            }
        }).start();
    }

    private void loginByUUID(String uuid){
        if(AppContext.getInstance().isLogin()){
            ++processNum;
            redirectTo();
            return;
        }
        PhoneLiveApi.loginByUUID(uuid, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                ++processNum;
                redirectTo();
            }

            @Override
            public void onResponse(String response, int id) {
                BaseData baseData= ApiUtils.getBaseData(response);
                if(baseData.isSuccess()){
                    MemberInfo memberInfo=null;
                    try {
                        memberInfo=com.alibaba.fastjson.JSONObject.parseObject(baseData.getData(), MemberInfo.class);
                    } catch (Exception e) {
                        memberInfo=null;
                    }
                    if(memberInfo!=null){
                        AppContext.getInstance().saveUserInfoNew(memberInfo);
                    }
                }
                ++processNum;
                redirectTo();
            }
        });
    }

    private void checkPermission(){
        PermissionGen.with(this)
                .addRequestCode(200)
                .permissions(
                        android.Manifest.permission.READ_EXTERNAL_STORAGE,
                        android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        android.Manifest.permission.CAMERA,
                        android.Manifest.permission.RECORD_AUDIO,
                        android.Manifest.permission.READ_PHONE_STATE,
                        android.Manifest.permission.INTERNET,
                        android.Manifest.permission.ACCESS_NETWORK_STATE
                ).request();
    }

    @PermissionSuccess(requestCode = 200)
    private void permissionSuccess(){
        checkRoute();
        saveDeviceId();
    }

    @PermissionFail(requestCode = 200)
    private void permissionFail() {
        showPermissionDialog();
    }

    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 0://获取保存的UUID
                    loginByUUID(AppConfig.GLOBAL_UUID);
                    break;
                case 1://获取保存的UUID失败
                    loginByUUID(AppConfig.GLOBAL_UUID);
                    break;
            }
        }
    };


    private void showPermissionDialog(){
        TipDialog tipDialog=new TipDialog(this);
        tipDialog.setMessageText("是否前往设置系统权限？");
        tipDialog.show(new BaseDialog.ParameCallBack() {
            @Override
            public void onCall(Object object) {
                if (object instanceof Boolean) {
                    if ((Boolean) object) {
                        PermissionPageUtils pageUtils=new PermissionPageUtils(AppStart.this);
                        pageUtils.jumpPermissionPage();
                    }else{
                        finish();
                    }
                }
            }
        });
    }

}

package com.duomizhibo.phonelive.utils;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Toast;
import com.duomizhibo.phonelive.AppConfig;
import com.duomizhibo.phonelive.AppContext;
import com.duomizhibo.phonelive.R;
import com.duomizhibo.phonelive.api.remote.ApiUtils;
import com.duomizhibo.phonelive.api.remote.PhoneLiveApi;
import com.zhy.http.okhttp.callback.FileCallBack;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

import okhttp3.Call;

/**
 * 更新管理类
 */

public class UpdateManager {

    private Context mContext;

    private boolean isShow = false;

    private ProgressDialog _waitDialog;

    private Dialog dialog;

    public UpdateManager(Context context, boolean isShow) {
        this.mContext = context;
        this.isShow = isShow;
    }


    //检测是否需要更新
    public void checkUpdate() {
        if (isShow) {
            //showCheckDialog();
        }
        PhoneLiveApi.getConfig(callback);
    }

    private StringCallback callback = new StringCallback() {
        @Override
        public void onError(Call call, Exception e,int id) {
            //hideCheckDialog();
            Toast.makeText(mContext,"获取网络数据失败",Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onResponse(String response,int id) {
            //hideCheckDialog();
            JSONArray res = ApiUtils.checkIsSuccess(response);
            if(null != res){
                try {
                    JSONObject config = res.getJSONObject(0);
                    if(!String.valueOf(AppContext.getInstance().getPackageInfo().versionName).equals(
                            config.getString("apk_ver"))){
                        showUpdateInfo(config.getString("apk_url"));
                    }
                    else{
                        if(isShow) {
                            Toast.makeText(mContext, "您的版本已是最新", Toast.LENGTH_SHORT).show();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

        }
    };

    private void showCheckDialog() {
        if (_waitDialog == null) {
            _waitDialog = DialogHelp.getWaitDialog((Activity) mContext, "正在获取新版本信息...");
        }
        _waitDialog.show();
    }

    private void hideCheckDialog() {
        if (_waitDialog != null) {
            _waitDialog.dismiss();
        }
    }

    //弹窗提示
    private void showUpdateInfo(final String apiUrl) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle("提示");
        builder.setMessage("发现新版本!");
        /*builder.setNegativeButton("取消", new android.content.DialogInterface.OnClickListener() {
            @Override
            public void onClick(android.content.DialogInterface dialogInterface, int i) {

            }
        });*/
        builder.setPositiveButton("确定", new android.content.DialogInterface.OnClickListener() {
            @Override
            public void onClick(android.content.DialogInterface dialogInterface, int i) {
                //upDataApp(apiUrl);


                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(apiUrl));
                mContext.startActivity(intent);
                dialog.dismiss();
            }
        });
        builder.setNegativeButton("取消",new android.content.DialogInterface.OnClickListener() {
            @Override
            public void onClick(android.content.DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        } );
        dialog = builder.create();
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();


    }

    //下载app
    private void upDataApp(String apiUrl) {
        if(!apiUrl.substring(apiUrl.length() - 4,apiUrl.length()) .equals(".apk")){
            Toast.makeText(AppContext.getInstance(),"安装包不存在",Toast.LENGTH_LONG).show();
            return;
        }
        //判断是否存在
        File file = new File(AppConfig.DEFAULT_SAVE_FILE_PATH + "app.apk");
        if(file.exists()){
            file.delete();
        }


        View view = View.inflate(mContext,R.layout.dialog_show_download_view,null);

        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle("正在下载中...");
        builder.setView(view);
        builder.create().show();
        PhoneLiveApi.getNewVersionApk(apiUrl,new FileCallBack(AppConfig.DEFAULT_SAVE_FILE_PATH,"app.apk"){

            @Override
            public void onError(Call call, Exception e,int id) {
                Toast.makeText(mContext,"安装包下载失败!",Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }

            @Override
            public void onResponse(File response,int id) {
                installApk();
                dialog.dismiss();
            }

            @Override
            public void inProgress(float progress, long total,int id) {

            }
        });
    }
    //安装app
    private void installApk() {
        File file = new File(AppConfig.DEFAULT_SAVE_FILE_PATH + "app.apk");
        if(!file.exists()){
            return;
        }
        TDevice.installAPK(mContext,file);
    }

    private void showLatestDialog() {
        DialogHelp.getMessageDialog(mContext, "已经是新版本了").show();
    }

    private void showFaileDialog() {
        DialogHelp.getMessageDialog(mContext, "网络异常，无法获取新版本信息").show();
    }
}

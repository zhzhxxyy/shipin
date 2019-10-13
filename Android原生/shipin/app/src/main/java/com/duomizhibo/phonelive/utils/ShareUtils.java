package com.duomizhibo.phonelive.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.mob.MobSDK;
import com.duomizhibo.phonelive.AppConfig;
import com.duomizhibo.phonelive.api.remote.ApiUtils;
import com.duomizhibo.phonelive.api.remote.PhoneLiveApi;
import com.duomizhibo.phonelive.bean.SimpleUserInfo;
import com.duomizhibo.phonelive.bean.UserBean;
import com.duomizhibo.phonelive.R;
import com.duomizhibo.phonelive.ui.ReadyStartLiveActivity;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cn.sharesdk.facebook.Facebook;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.tencent.qzone.QZone;
import cn.sharesdk.twitter.Twitter;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;
import okhttp3.Call;

import static com.tencent.bugly.crashreport.inner.InnerAPI.context;

/**
 * Created by Administrator on 2016/4/14.
 */
public class ShareUtils {


    public static void share(Activity context, int id, SimpleUserInfo user) {
        share(context, id, user, null);
    }

    public static void share(final Context context, final int index, final SimpleUserInfo user, final PlatformActionListener listener) {
        PhoneLiveApi.getConfig(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                Toast.makeText(context, "获取分享地址失败", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(String response, int id) {
                JSONArray res = ApiUtils.checkIsSuccess(response);
                if (res != null) {
                    try {
                        JSONObject jsonObject = res.getJSONObject(0);
                        String shareUrl = jsonObject.getString("app_android");


                        String names = "";
                        if (AppConfig.SHARE_TYPE.getString(index).equals("qq")) {
                            names = QQ.NAME;
                        } else if (AppConfig.SHARE_TYPE.getString(index).equals("qzone")) {
                            names = QZone.NAME;
                        } else if (AppConfig.SHARE_TYPE.getString(index).equals("wchat")) {
                            shareUrl = jsonObject.getString("wx_siteurl") + user.id;
                            names = WechatMoments.NAME;
                        } else if (AppConfig.SHARE_TYPE.getString(index).equals("wx")) {
                            shareUrl = jsonObject.getString("wx_siteurl") + user.id;
                            names = Wechat.NAME;
                        } else if (AppConfig.SHARE_TYPE.getString(index).equals("facebook")) {
                            names = Facebook.NAME;
                        } else if (AppConfig.SHARE_TYPE.getString(index).equals("twitter")) {
                            names = Twitter.NAME;
                        }

                        share(context, names, jsonObject.getString("share_title")
                                , user.user_nicename + jsonObject.getString("share_des"), user,null, shareUrl, listener);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }

            }
        });

    }

    public static void share(final Context context, String name,  String title, String des,final SimpleUserInfo user, String thumb_s, String shareUrl, PlatformActionListener listener) {
        MobSDK.init(context);
        final OnekeyShare oks = new OnekeyShare();
        oks.setSilent(true);
        //关闭sso授权
        oks.disableSSOWhenAuthorize();
        oks.setPlatform(name);
        // 分享时Notification的图标和文字  2.5.9以后的版本不调用此方法
        //oks.setNotification(R.drawable.ic_launcher, getString(R.string.app_name));
        // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
        oks.setTitle(title);
        // titleUrl是标题的网络链接，仅在人人网和QQ空间使用

        // text是分享文本，所有平台都需要这个字段
        oks.setText(des);
        if (user != null) {
            oks.setImageUrl(user.avatar_thumb);
        } else {
            oks.setImageUrl(thumb_s);
        }


        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
        //oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
        // url仅在微信（包括好友和朋友圈）中使用
        if (name.equals(Wechat.NAME) || name.equals(WechatMoments.NAME)) {
            oks.setUrl(shareUrl);
            oks.setSiteUrl(shareUrl);
            oks.setTitleUrl(shareUrl);

        } else {
            oks.setUrl(shareUrl);
            oks.setSiteUrl(shareUrl);
            oks.setTitleUrl(shareUrl);
        }

        // comment是我对这条分享的评论，仅在人人网和QQ空间使用
        //oks.setComment(context.getString(R.string.shartitle));
        // site是分享此内容的网站名称，仅在QQ空间使用
        oks.setSite(context.getString(R.string.app_name));
        oks.setCallback(listener);
        // siteUrl是分享此内容的网站地址，仅在QQ空间使用

        // 启动分享GUI
        oks.show(context);
    }
}
package com.duomizhibo.phonelive.api.remote;

import com.duomizhibo.phonelive.AppConfig;
import com.duomizhibo.phonelive.AppContext;
import com.duomizhibo.phonelive.bean.GiftBean;
import com.duomizhibo.phonelive.bean.UserBean;
import com.duomizhibo.phonelive.bean.video.UploadParam;
import com.duomizhibo.phonelive.event.AttentEvent;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.builder.PostFormBuilder;
import com.zhy.http.okhttp.callback.FileCallBack;
import com.zhy.http.okhttp.callback.StringCallback;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONException;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

import cn.sharesdk.framework.PlatformDb;

/**
 * 接口获取
 */
public class PhoneLiveApi {

    /**
     * 登陆
     *
     * @param phone
     * @param password
     */

    //HHH 2016-09-09
    public static void login(String phone, String password, StringCallback callback) {
        String url = AppConfig.MAIN_URL_API;
        try {
            OkHttpUtils.get()
                    .url(url)
                    .addParams("service", "Login.userLogin")
                    .addParams("user_login", phone)
                    .addParams("user_pass", URLEncoder.encode(password, "UTF-8"))
                    .build()
                    .execute(callback);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

    }

    //HHH 2016-09-09
    public static void reg(String user_login, String user_pass, String user_pass2, String code, StringCallback callback) {
        String url = AppConfig.MAIN_URL_API;
        try {
            OkHttpUtils.get()
                    .url(url)
                    .addParams("service", "Login.userReg")
                    .addParams("user_login", user_login)
                    .addParams("user_pass", URLEncoder.encode(user_pass, "UTF-8"))
                    .addParams("user_pass2", URLEncoder.encode(user_pass2, "UTF-8"))
                    .addParams("code", code)
                    .addParams("upper", AppConfig.UPPER)
                    .build()
                    .execute(callback);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
    public static void findPass(String user_login, String user_pass, String user_pass2, String code, StringCallback callback) {
        String url = AppConfig.MAIN_URL_API;
        try {
            OkHttpUtils.get()
                    .url(url)
                    .addParams("service", "Login.userFindPass")
                    .addParams("user_login", user_login)
                    .addParams("user_pass", URLEncoder.encode(user_pass, "UTF-8"))
                    .addParams("user_pass2", URLEncoder.encode(user_pass2, "UTF-8"))
                    .addParams("code", code)
                    .tag("findPass")
                    .build()
                    .execute(callback);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

    }

    /**
     * 获取用户信息
     *
     * @param token    appkey
     * @param uid      用户id
     * @param callback 回调
     */
    public static void getMyUserInfo(String uid, String token, StringCallback callback) {
        OkHttpUtils.get()
                .url(AppConfig.MAIN_URL_API)
                .addParams("service", "User.getBaseInfo")
                .addParams("uid", uid)
                .addParams("token", token)
                .tag("getMyUserInfo")
                .build()
                .execute(callback);

    }

    /**
     * 获取其他用户信息
     *
     * @param uid      用户id
     * @param callback 回调
     */
    public static void getOtherUserInfo(int uid, StringCallback callback) {
        OkHttpUtils.get()
                .url(AppConfig.MAIN_URL_API)
                .addParams("service", "User.getUserInfo")
                .addParams("uid", String.valueOf(uid))
                .tag("getOtherUserInfo")
                .build()
                .execute(callback);

    }

    /**
     * @dw 修改用户信息
     */
    public static void saveInfo(String fields, String uid, String token, StringCallback callback) {
        OkHttpUtils.get()
                .url(AppConfig.MAIN_URL_API)
                .addParams("service", "User.updateFields")
                .addParams("fields", fields)
                .addParams("uid", uid)
                .addParams("token", token)
                .tag("saveInfo")
                .build()
                .execute(callback);
    }

    /**
     * @param uid    当前用户id
     * @param showId 主播id
     * @param token  token
     * @dw 进入直播间初始化信息
     */
    public static void enterRoom(String uid, String showId, String token, String address, String stream, StringCallback callback) {
        OkHttpUtils.get()
                .url(AppConfig.MAIN_URL_API)
                .addParams("service", "Live.enterRoom")
                .addParams("uid", uid)
                .addParams("liveuid", showId)
                .addParams("token", token)
                .addParams("city", address)
                .addParams("stream", stream)
                .tag("initRoomInfo")
                .build()
                .execute(callback);
    }


    /**
     * @param uid   主播id
     * @param title 开始直播标题
     * @param token
     * @dw 开始直播
     */
    public static void createLive(String uid, String a1, String a2, String title, String token, String name, File file, String type, String type_val, StringCallback callback) {
        try {
            PostFormBuilder postFormBuilder = OkHttpUtils.post()
                    .url(AppConfig.MAIN_URL_API)
                    .addParams("service", "Live.createRoom")
                    .addParams("uid", String.valueOf(uid))
                    .addParams("title", URLEncoder.encode(title, "UTF-8"))
                    .addParams("user_nicename", name)
                    .addParams("avatar", a1)
                    .addParams("avatar_thumb", a2)
                    .addParams("city", AppContext.address)
                    .addParams("province", AppContext.province)
                    .addParams("lat", AppContext.lat)  //HHH 2016-09-09
                    .addParams("lng", AppContext.lng)
                    .addParams("token", token)
                    .addParams("type", type)
                    .addParams("type_val", type_val);
            if (file != null) {
                postFormBuilder.addFile("file", file.getName(), file)
                        .tag("createLive")
                        .build()
                        .execute(callback);
            } else {
                postFormBuilder
                        .tag("createLive")
                        .build()
                        .execute(callback);
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

    }

    public static void closeGameJinhua(String stream, String liveuid, String token, String gameid, String type, StringCallback callback) {
        OkHttpUtils.get()
                .url(AppConfig.MAIN_URL_API)
                .addParams("service", "Game.endGame")
                .addParams("stream", stream)
                .addParams("liveuid", liveuid)
                .addParams("token", token)
                .addParams("gameid", gameid)
                .addParams("type", type)
                .tag("closeGameJinhua")
                .build()
                .execute(callback);
    }

    public static void closeGamePan(String stream, String liveuid, String token, String gameid, String type, StringCallback callback) {
        OkHttpUtils.get()
                .url(AppConfig.MAIN_URL_API)
                .addParams("service", "Game.Dial_end")
                .addParams("stream", stream)
                .addParams("liveuid", liveuid)
                .addParams("token", token)
                .addParams("gameid", gameid)
                .addParams("type", type)
                .tag("closeGameJinhua")
                .build()
                .execute(callback);
    }

    public static void closeGameHaiDao(String stream, String liveuid, String token, String gameid, String type, StringCallback callback) {
        OkHttpUtils.get()
                .url(AppConfig.MAIN_URL_API)
                .addParams("service", "Game.Taurus_end")
                .addParams("stream", stream)
                .addParams("liveuid", liveuid)
                .addParams("token", token)
                .addParams("gameid", gameid)
                .addParams("type", type)
                .tag("closeGameJinhua")
                .build()
                .execute(callback);
    }

    public static void closeGameNiuZai(String stream, String liveuid, String token, String gameid, String type, StringCallback callback) {
        OkHttpUtils.get()
                .url(AppConfig.MAIN_URL_API)
                .addParams("service", "Game.Cowboy_end")
                .addParams("stream", stream)
                .addParams("liveuid", liveuid)
                .addParams("token", token)
                .addParams("gameid", gameid)
                .addParams("type", type)
                .tag("closeGameJinhua")
                .build()
                .execute(callback);
    }

    /**
     * @param token 用户的token
     * @dw 关闭直播
     */
    public static void closeLive(String id, String token, String stream, StringCallback callback) {
        OkHttpUtils.get()
                .url(AppConfig.MAIN_URL_API)
                .addParams("service", "Live.stopRoom")
                .addParams("uid", id)
                .addParams("token", token)
                .addParams("stream", stream)
                .tag("closeLive")
                .build()
                .execute(callback);
    }

    /**
     * @param callback
     * @dw 获取礼物列表
     */
    public static void getGiftList(String uid, String token, StringCallback callback) {
        OkHttpUtils.get()
                .url(AppConfig.MAIN_URL_API)
                .addParams("service", "Live.getGiftList")
                .addParams("uid", uid)
                .addParams("token", token)
                .tag("getGiftList")
                .build()
                .execute(callback);
    }

    /**
     * @param g           赠送礼物信息
     * @param mUser       用户信息
     * @param mNowRoomNum 房间号(主播id)
     * @dw 赠送礼物
     */
    public static void sendGift(UserBean mUser, GiftBean g, String mNowRoomNum, String stream, StringCallback callback) {
        OkHttpUtils.get()
                .url(AppConfig.MAIN_URL_API)
                .addParams("service", "Live.sendGift")
                .addParams("token", mUser.token)
                .addParams("uid", String.valueOf(mUser.id))
                .addParams("liveuid", String.valueOf(mNowRoomNum))
                .addParams("giftid", String.valueOf(g.getId()))
                .addParams("giftcount", "1")
                .addParams("stream", stream)
                .tag("sendGift")
                .build()
                .execute(callback);
    }

    /**
     * @param content     弹幕信息
     * @param mUser       用户信息
     * @param mNowRoomNum 房间号(主播id)
     * @dw 发送弹幕 HHH
     */
    public static void sendBarrage(UserBean mUser, String content, String mNowRoomNum, String stream, StringCallback callback) {
        OkHttpUtils.get()
                .url(AppConfig.MAIN_URL_API)
                .addParams("service", "Live.sendBarrage")
                .addParams("token", mUser.token)
                .addParams("uid", mUser.id)
                .addParams("liveuid", mNowRoomNum)
                .addParams("content", content)
                .addParams("stream", stream)
                .addParams("giftid", "1")
                .addParams("giftcount", "1")
                .tag("sendBarrage")
                .build()
                .execute(callback);
    }

    /**
     * @param uid   其他用户id
     * @param ucuid 当前用户自己的id
     * @dw 获取其他用户信息
     */
    public static void getUserInfo(String uid, String ucuid, String liveuid, StringCallback callback) {
        OkHttpUtils.get()
                .url(AppConfig.MAIN_URL_API)
                .addParams("service", "Live.getPop")
                .addParams("uid", uid)
                .addParams("touid", ucuid)
                .addParams("liveuid", liveuid)
                .tag("getUserInfo")
                .build()
                .execute(callback);
    }

    /**
     * @param touid 当前主播id\
     * @param uid   当前用户uid
     * @dw 判断是否关注
     */
    public static void getIsFollow(String uid, String touid, StringCallback callback) {
        OkHttpUtils.get()
                .url(AppConfig.MAIN_URL_API)
                .addParams("service", "User.isAttent")
                .addParams("uid", uid)
                .addParams("touid", touid)
                .tag("getIsFollow")
                .build()
                .execute(callback);

    }

    public interface AttentionCallback {
        void callback(boolean isAttention);
    }

    /**
     * @param uid   当前用户id
     * @param touid 关注用户id
     * @dw 关注
     */
    public static void showFollow(String uid, final String touid, String token, final AttentionCallback callback) {
        OkHttpUtils.get()
                .url(AppConfig.MAIN_URL_API)
                .addParams("service", "User.setAttent")
                .addParams("uid", uid)
                .addParams("touid", touid)
                .addParams("token", token)
                .tag("showFollow")
                .build()
                .execute(new HttpCallback() {
                    @Override
                    public void onSuccess(int code, String msg, JSONArray info) throws JSONException {
                        String isattent = info.getJSONObject(0).getString("isattent");
                        boolean isAttented = "1".equals(isattent);
                        EventBus.getDefault().post(new AttentEvent(touid, isAttented));
                        if (callback != null) {
                            callback.callback(isAttented);
                        }
                    }
                });
    }

    /**
     * @param uid 查询用户id
     * @dw 获取homepage中的用户信息
     */
    public static void getHomePageUInfo(String uid, String touid, StringCallback callback) {
        OkHttpUtils.get()
                .url(AppConfig.MAIN_URL_API)
                .addParams("service", "User.getUserHome")
                .addParams("uid", uid)
                .addParams("touid", touid)
                .tag("getHomePageUInfo")
                .build()
                .execute(callback);
    }

    /**
     * @param uid   自己的id
     * @param touid 查询用户id
     * @dw 获取homepage用户的fans
     */
    public static void getFansList(String uid, String touid, StringCallback callback) {
        OkHttpUtils.get()
                .url(AppConfig.MAIN_URL_API)
                .addParams("service", "User.getFansList")
                .addParams("uid", uid)
                .addParams("touid", touid)
                .tag("getFansList")
                .build()
                .execute(callback);
    }

    /**
     * @param ucid 自己的id
     * @param uid  查询用户id
     * @dw 获取homepage用户的关注用户列表get
     */
    public static void getAttentionList(String uid, String ucid, StringCallback callback) {
        OkHttpUtils.get()
                .url(AppConfig.MAIN_URL_API)
                .addParams("service", "User.getFollowsList")
                .addParams("uid", uid)
                .addParams("touid", ucid)
                .tag("getAttentionList")
                .build()
                .execute(callback);
    }

    /**
     * @param uid 查询用户id
     * @dw 获取魅力值排行
     */
    public static void getYpOrder(int uid, StringCallback callback) {
        OkHttpUtils.get()
                .url(AppConfig.MAIN_URL_API)
                .addParams("service", "User.getCoinRecord")
                .addParams("uid", String.valueOf(uid))
                .tag("getYpOrder")
                .build()
                .execute(callback);

    }

    /**
     * @param uid   查询用户id
     * @param token token
     * @dw 获取收益信息
     */
    public static void getWithdraw(String uid, String token, StringCallback callback) {
        OkHttpUtils.get()
                .url(AppConfig.MAIN_URL_API)
                .addParams("service", "User.getProfit")
                .addParams("uid", uid)
                .addParams("token", token)
                .tag("getWithdraw")//BBB
                .build()
                .execute(callback);

    }

    /**
     * @dw 获取最新
     */
    public static void getNewestUserList(StringCallback callback) {

        //HHH 2016-09-09
        OkHttpUtils.get()
                .url(AppConfig.MAIN_URL_API)
                .addParams("service", "Home.getNew")
                .addParams("lng", AppContext.lng)
                .addParams("lat", AppContext.lat)
                .tag("getNewestUserList")
                .build()
                .execute(callback);

    }

    /**
     * @dw 获取小视频
     */
    public static void getVideo(int p,StringCallback callback) {
        //HHH 2016-09-09
        OkHttpUtils.get()
                .url(AppConfig.MAIN_URL_API)
                .addParams("service", "Video.getVideoList")
                .addParams("uid", AppContext.getInstance().getLoginUid())
                .addParams("p", String.valueOf(p))
                .tag("getVideoList")
                .build()
                .execute(callback);

    }

    /**
     * @dw 获取游戏
     */
    public static void getGameUserList(StringCallback callback) {

        //HHH 2016-09-09
        OkHttpUtils.get()
                .url(AppConfig.MAIN_URL_API)
                .addParams("service", "Home.getGameLive")
                .tag("getGameLive")
                .build()
                .execute(callback);

    }

    /**
     * @param uid          用户id
     * @param token
     * @param protraitFile 图片文件
     * @dw 更新头像
     */
    public static void updatePortrait(String uid, String token, File protraitFile, StringCallback callback) {
        OkHttpUtils.post()
                .url(AppConfig.MAIN_URL_API)
                .addParams("service", "User.updateAvatar")
                .addFile("file", "wp.png", protraitFile)
                .addParams("uid", uid)
                .addParams("token", token)
                //.url(AppConfig.MAIN_URL_API + "appapi/?service=User.upload")
                .tag("phonelive")
                .build()
                .execute(callback);

    }

    /**
     * @param uid   用户id
     * @param token
     * @dw 提现
     */
    public static void requestCash(String uid, String token, String money, StringCallback callback) {
        OkHttpUtils.get()
                .url(AppConfig.MAIN_URL_API)
                .addParams("service", "User.setCash")
                .addParams("uid", String.valueOf(uid))
                .addParams("token", token)
                .addParams("money", money)
                .tag("requestCash")
                .build()
                .execute(callback);
    }

    /**
     * @param uid 用户id
     * @dw 直播记录
     */
    public static void getLiveRecord(String uid, StringCallback callback) {
        OkHttpUtils.get()
                .url(AppConfig.MAIN_URL_API)
                .addParams("service", "User.getLiverecord")
                .addParams("uid", uid)
                .addParams("touid", uid)
                .tag("getLiveRecord")
                .build()
                .execute(callback);
    }

    /**
     * @param uid 用户id
     * @dw 支付宝下订单
     */
    public static void getAliPayOrderNum(String uid, String changeid, String num, String money, StringCallback callback) {
        OkHttpUtils.get()
                .url(AppConfig.MAIN_URL_API)
                .addParams("service", "Charge.getAliOrder")
                .addParams("uid", uid)
                .addParams("money", money)
                .addParams("changeid", changeid)
                .addParams("coin", num)
                .tag("getAliPayOrderNum")
                .build()
                .execute(callback);
    }

    //定位
    public static void getAddress(StringCallback callback) {
        OkHttpUtils.get()
                .url("http://int.dpool.sina.com.cn/iplookup/iplookup.php?format=json")
                .tag("phonelive")
                .build()
                .execute(callback);
    }

    /**
     * @param screenKey 搜索关键词
     * @param uid       用户id
     * @dw 搜索
     */
    public static void search(String screenKey, StringCallback callback, String uid) {
        OkHttpUtils.get()
                .url(AppConfig.MAIN_URL_API)
                .addParams("service", "Home.search")
                .addParams("key", screenKey)
                .addParams("uid", uid)
                .tag("search")
                .build()
                .execute(callback);
    }

    /**
     * @dw 获取地区列表
     */
    public static void getAreaList(StringCallback callback) {
        OkHttpUtils.get()
                .url(AppConfig.MAIN_URL_API)
                .addParams("service", "User.getArea")
                .tag("getAreaList")
                .build()
                .execute(callback);
    }

    /**
     * @param sex  性别
     * @param area 地区
     * @dw 地区检索
     */

    public static void selectTermsScreen(int sex, String area, StringCallback callback) {
        OkHttpUtils.get()
                .url(AppConfig.MAIN_URL_API)
                .addParams("service", "User.searchArea")
                .addParams("sex", String.valueOf(sex))
                .addParams("key", area)
                .tag("selectTermsScreen")
                .build()
                .execute(callback);
    }

    /**
     * @param uidList 用户id字符串 以逗号分割
     * @dw 批量获取用户信息
     */
    public static void getMultiBaseInfo(String type, String uidList, StringCallback callback) {

        OkHttpUtils.get()
                .url(AppConfig.MAIN_URL_API)
                .addParams("service", "User.getMultiInfo")
                .addParams("uids", uidList)
                .addParams("type", type)
                .addParams("uid", AppContext.getInstance().getLoginUid())
                .tag("getMultiBaseInfo")
                .build()
                .execute(callback);

    }

    /**
     * @param uid 用户id
     * @dw 获取已关注正在直播的用户
     */
    public static void getAttentionLive(String uid, StringCallback callback) {
        OkHttpUtils.get()
                .url(AppConfig.MAIN_URL_API)
                .addParams("service", "Home.getFollow")
                .addParams("uid", String.valueOf(uid))
                .tag("getAttentionLive")
                .build()
                .execute(callback);
    }


    //用来确定 聊天页面 陌生人是关注的还是未关注的
    public static void getPmUserInfo(String touid, StringCallback callback) {
        OkHttpUtils.get()
                .url(AppConfig.MAIN_URL_API)
                .addParams("service", "User.getPmUserInfo")
                .addParams("uid", AppContext.getInstance().getLoginUid())
                .addParams("touid", touid)
                .tag("getPmUserInfo")
                .build()
                .execute(callback);

    }

    /**
     * @param uid   用户id
     * @param price 价格
     * @dw 微信支付
     */
    public static void wxPay(String uid, String changeid, String price, String num, StringCallback callback) {
        OkHttpUtils.get()
                .url(AppConfig.MAIN_URL_API)
                .addParams("service", "Charge.getWxOrder")
                .addParams("uid", uid)
                .addParams("changeid", changeid)
                .addParams("coin", num)
                .addParams("money", price)
                .build()
                .execute(callback);

    }

    /**
     * @param platDB 用户信息
     * @param type   平台
     * @dw 第三方登录
     */
    public static void otherLogin(String type, PlatformDb platDB, StringCallback callback) {
        OkHttpUtils.get()
                .url(AppConfig.MAIN_URL_API)
                .addParams("service", "Login.userLoginByThird")
                .addParams("openid", platDB.getUserId())
                .addParams("nicename", platDB.getUserName())
                .addParams("type", type)
                .addParams("avatar", platDB.getUserIcon())
                .addParams("upper", AppConfig.UPPER)
                .tag("otherLogin")
                .build()
                .execute(callback);
    }

    /**
     * @param roomnum 房间号码
     * @param touid   操作id
     * @param token   用户登录token
     * @dw 设为管理员
     */
    public static void setManage(String roomnum, String touid, String token, String uid, StringCallback callback) {
        OkHttpUtils.get()
                .url(AppConfig.MAIN_URL_API)
                .addParams("service", "Live.setAdmin")
                .addParams("liveuid", roomnum)
                .addParams("touid", touid)
                .addParams("uid", uid)
                .addParams("token", token)
                .tag("setManage")
                .build()
                .execute(callback);

    }

    /**
     * @param uid    用户id
     * @param showid 房间号码
     * @dw 判断是否为管理员
     */

    public static void isManage(String showid, String uid, StringCallback callback) {
        OkHttpUtils.get()
                .url(AppConfig.MAIN_URL_API)
                .addParams("service", "User.getIsAdmin")
                .addParams("liveuid", showid)
                .addParams("uid", uid)
                .tag("isManage")
                .build()
                .execute(callback);

    }

    /**
     * @param showid 房间id
     * @param touid  被禁言用户id
     * @param token  用户登录token
     * @dw 禁言
     */
    public static void setShutUp(String showid, String touid, String uid, String token, StringCallback callback) {
        OkHttpUtils.get()
                .url(AppConfig.MAIN_URL_API)
                .addParams("service", "Live.setShutUp")
                .addParams("liveuid", showid)
                .addParams("touid", touid)
                .addParams("uid", uid)
                .addParams("token", token)
                .tag("setShutUp")
                .build()
                .execute(callback);
    }

    //是否禁言解除
    public static void isShutUp(int uid, int showid, StringCallback callback) {
        OkHttpUtils.get()
                .url(AppConfig.MAIN_URL_API)
                .addParams("service", "User.isShutUp")
                .addParams("liveuid", String.valueOf(showid))
                .addParams("uid", String.valueOf(uid))
                .tag("isShutUp")
                .build()
                .execute(callback);
    }

    //token是否过期
    public static void tokenIsOutTime(String uid, String token, StringCallback callback) {
        OkHttpUtils.get()
                .url(AppConfig.MAIN_URL_API)
                .addParams("service", "User.iftoken")
                .addParams("uid", uid)
                .addParams("token", token)
                .tag("tokenIsOutTime")
                .build()
                .execute(callback);
    }

    /**
     * @dw 拉黑
     */
    public static void pullTheBlack(String uid, String touid, String token, StringCallback callback) {
        OkHttpUtils.get()
                .url(AppConfig.MAIN_URL_API)
                .addParams("service", "User.setBlack")
                .addParams("uid", uid)
                .addParams("touid", touid)
                .addParams("token", token)
                .tag("pullTheBlack")
                .build()
                .execute(callback);

    }

    /**
     * @dw 黑名单列表
     */
    public static void getBlackList(String uid, StringCallback callback) {
        OkHttpUtils.get()
                .url(AppConfig.MAIN_URL_API)
                .addParams("service", "User.getBlackList")
                .addParams("uid", uid)
                .addParams("touid", uid)
                .tag("getBlackList")
                .build()
                .execute(callback);
    }

    public static void getMessageCode(String phoneNum, String methodName, StringCallback callback) {
        OkHttpUtils.get()
                .url(AppConfig.MAIN_URL_API)
                .addParams("service", methodName)
                .addParams("mobile", phoneNum)
                .tag("getMessageCode")
                .build()
                .execute(callback);
    }

    /**
     * @param uid 用户id
     * @dw 获取用户余额
     */
    public static void getUserDiamondsNum(String uid, String token, StringCallback callback) {
        OkHttpUtils.get()
                .url(AppConfig.MAIN_URL_API)
                .addParams("service", "User.getUserPrivateInfo")
                .addParams("uid", uid)
                .addParams("token", token)
                .tag("getUserDiamondsNum")
                .build()
                .execute(callback);
    }

    /**
     * @param uid    用户id
     * @param token  用户登录token
     * @param showid 房间号
     * @dw 点亮
     */
    public static void showLit(String uid, String token, String showid) {
        OkHttpUtils.get()
                .url(AppConfig.MAIN_URL_API)
                .addParams("service", "User.setLight")
                .addParams("uid", uid)
                .addParams("token", token)
                .addParams("showid", showid)
                .tag("showLit")
                .build()
                .execute(null);
    }

    /**
     * @param keyword 歌曲关键词
     * @dw 百度接口搜索音乐
     */
    public static void searchMusic(String keyword, StringCallback callback) {

        OkHttpUtils.get()
                .url(AppConfig.MAIN_URL_API)
                .addParams("service", "live.searchMusic")
                .addParams("key", keyword)
                .tag("searchMusic")
                .build()
                .execute(callback);

    }

    /**
     * @param songid 歌曲id
     * @dw 获取music信息
     */
    public static void getMusicFileUrl(String songid, StringCallback callback) {
        OkHttpUtils.get()
                .url(AppConfig.MAIN_URL_API)
                .addParams("service", "Live.getDownurl")
                .addParams("audio_id", songid)
                .tag("getMusicFileUrl")
                .build()
                .execute(callback);
    }

    /**
     * @param musicUrl 下载地址
     * @dw 下载音乐文件
     */
    public static void downloadMusic(String musicUrl, FileCallBack fileCallBack) {
        OkHttpUtils.get()
                .url(musicUrl)
                .tag("downloadMusic")
                .build()
                .execute(fileCallBack);
    }

    /**
     * @dw 开播等级限制
     */
    public static void getLevelLimit(String uid, StringCallback callback) {
        OkHttpUtils.get()
                .url(AppConfig.MAIN_URL_API)
                .addParams("service", "User.getLevelLimit")
                .addParams("uid", String.valueOf(uid))
                .tag("phonelive")
                .build()
                .execute(callback);
    }

    /**
     * @dw 检查新版本
     */
    public static void checkUpdate(StringCallback callback) {
        OkHttpUtils.get()
                .url(AppConfig.MAIN_URL_API)
                .addParams("service", "User.getVersion")
                .tag("checkUpdate")
                .build()
                .execute(callback);
    }

    public static void downloadLrc(String musicLrc, FileCallBack fileCallBack) {
        OkHttpUtils.get()
                .url(musicLrc)
                .tag("downloadLrc")
                .build()
                .execute(fileCallBack);
    }

    /**
     * @param apkUrl 下载地址
     * @dw 下载最新apk
     */
    public static void getNewVersionApk(String apkUrl, FileCallBack fileCallBack) {
        OkHttpUtils.get()
                .url(apkUrl)
                .tag("getNewVersionApk")
                .build()
                .execute(fileCallBack);
    }

    /**
     * @param uid 用户id
     * @dw 获取管理员列表
     */
    public static void getManageList(String uid, StringCallback callback) {
        OkHttpUtils.get()
                .url(AppConfig.MAIN_URL_API)
                .addParams("service", "Live.getAdminList")
                .addParams("liveuid", String.valueOf(uid))
                .tag("getManageList")
                .build()
                .execute(callback);
    }

    /**
     * @param mRoomNum 房间号码
     * @param size     当前人数
     * @dw 获取更多用户列表
     */
    public static void loadMoreUserList(int size, String mRoomNum, StringCallback callback) {
        OkHttpUtils.get()
                .url(AppConfig.MAIN_URL_API)
                .addParams("service", "User.getRedislist")
                .addParams("size", String.valueOf(size))
                .addParams("showid", mRoomNum)
                .tag("loadMoreUserList")
                .build()
                .execute(callback);
    }

    /**
     * @param uid 用户id
     * @dw 判断是否是第一次充值
     */

    public static void getCharge(String uid, StringCallback callback) {
        OkHttpUtils.get()
                .url(AppConfig.MAIN_URL_API)
                .addParams("service", "User.getCharge")
                .addParams("uid", String.valueOf(uid))
                .tag("getCharge")
                .build()
                .execute(callback);
    }

    //举报
    public static void report(String uid, String token, String touid) {
        OkHttpUtils.get()
                .url(AppConfig.MAIN_URL_API)
                .addParams("service", "Live.setReport")
                .addParams("uid", uid)
                .addParams("touid", touid)
                .addParams("token", token)
                .addParams("content", "涉嫌违规")
                .tag("report")
                .build()
                .execute(null);

    }


    //获取直播记录
    public static void getLiveRecordById(String showid, StringCallback callback) {
        OkHttpUtils.get()
                .url(AppConfig.MAIN_URL_API)
                .addParams("service", "User.getAliCdnRecord")
                .addParams("id", showid)
                .tag("getLiveRecordById")
                .build()
                .execute(callback);
    }

    //HHH 2016-09-13
    public static void getConfig(StringCallback buyVipCallback) {
        OkHttpUtils.get()
                .url(AppConfig.MAIN_URL_API)
                .addParams("service", "Home.getConfig")
                .tag("getConfig")
                .build()
                .execute(buyVipCallback);
    }

    public static void exchangVote(String uid, String votes, StringCallback exchangVoteCallback) {
        OkHttpUtils.get()
                .url(AppConfig.MAIN_URL_API)
                .addParams("service", "User.exchange")
                .addParams("uid", String.valueOf(uid))
                .addParams("votes", votes)
                .tag("exchangVote")
                .build()
                .execute(exchangVoteCallback);
    }

    public static void getChangePass(String uid, String token, String oldpass, String pass1st, String pass2nd, StringCallback getChangePassCallback) {
        try {
            OkHttpUtils.get()
                    .url(AppConfig.MAIN_URL_API)
                    .addParams("service", "User.updatePass")
                    .addParams("uid", uid)
                    .addParams("token", token)
                    .addParams("oldpass", URLEncoder.encode(oldpass, "UTF-8"))
                    .addParams("pass", URLEncoder.encode(pass1st, "UTF-8"))
                    .addParams("pass2", URLEncoder.encode(pass2nd, "UTF-8"))
                    .tag("getChangePass")
                    .build()
                    .execute(getChangePassCallback);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }


    //修改直播状态
    public static void changeLiveState(String uid, String token, String stream, String status, StringCallback callback) {
        OkHttpUtils.get()
                .url(AppConfig.MAIN_URL_API)
                .addParams("service", "Live.changeLive")
                .addParams("uid", uid)
                .addParams("token", token)
                .addParams("stream", stream)
                .addParams("status", status)
                .build()
                .execute(callback);
    }

    //检查房间状态
    public static void checkoutRoom(String uid, String token, String stream, String liveuid, StringCallback callback) {
        OkHttpUtils.get()
                .url(AppConfig.MAIN_URL_API)
                .addParams("service", "Live.checkLive")
                .addParams("uid", uid)
                .addParams("token", token)
                .addParams("stream", stream)
                .addParams("liveuid", liveuid)
                .build()
                .execute(callback);
    }

    public static void requestHotData(StringCallback callback) {

        OkHttpUtils.get()
                .url(AppConfig.MAIN_URL_API)
                .addParams("service", "Home.getHot")
                .tag("requestHotData")
                .build()
                .execute(callback);
    }

    //我的钻石
    public static void requestBalance(String userID, String userToken, StringCallback callback) {
        OkHttpUtils.get()
                .url(AppConfig.MAIN_URL_API)
                .addParams("service", "User.getBalance")
                .addParams("uid", userID)
                .addParams("token", userToken)
                .tag("requestBalance")
                .build()
                .execute(callback);
    }

    //三方登录开启状态
    public static void requestOtherLoginStatus(StringCallback callback) {
        OkHttpUtils.get()
                .url(AppConfig.MAIN_URL_API)
                .addParams("service", "Home.getLogin")
                .tag("requestOtherLoginStatus")
                .build()
                .execute(callback);
    }

    //踢人
    public static void setKick(String showid, String touid, String uid, String token, StringCallback callback) {
        OkHttpUtils.get()
                .url(AppConfig.MAIN_URL_API)
                .addParams("service", "Live.kicking")
                .addParams("liveuid", showid)
                .addParams("touid", touid)
                .addParams("uid", uid)
                .addParams("token", token)
                .tag("setKick")
                .build()
                .execute(callback);
    }

    //超管关闭直播
    public static void setCloseLive(String id, String token, String liveuid, String type, StringCallback callback) {

        OkHttpUtils.get()
                .url(AppConfig.MAIN_URL_API)
                .addParams("service", "Live.superStopRoom")
                .addParams("liveuid", liveuid)
                .addParams("token", token)
                .addParams("uid", id)
                .addParams("type", type)
                .tag("setCloseLive")
                .build()
                .execute(callback);
    }

    //房间扣费
    public static void requestCharging(String uid, String token, String liveuid, String stream, StringCallback callback) {
        OkHttpUtils.get()
                .url(AppConfig.MAIN_URL_API)
                .addParams("service", "Live.roomCharge")
                .addParams("liveuid", liveuid)
                .addParams("token", token)
                .addParams("uid", uid)
                .addParams("stream", stream)
                .tag("requestCharging")
                .build()
                .execute(callback);
    }

    public static void getLiveEndInfo(String stream, StringCallback callback) {
        OkHttpUtils.get()
                .url(AppConfig.MAIN_URL_API)
                .addParams("service", "Live.stopInfo")
                .addParams("stream", stream)
                .tag("getLiveEndInfo")
                .build()
                .execute(callback);
    }

    //炸金花发牌接口
    public static void requestStartGame(String liveuid, String stream, String token, StringCallback callback) {
        OkHttpUtils.get()
                .url(AppConfig.MAIN_URL_API)
                .addParams("service", "Game.Jinhua")
                .addParams("liveuid", liveuid)
                .addParams("stream", stream)
                .addParams("token", token)
                .tag("requestStartGame")
                .build()
                .execute(callback);
    }

    //转盘开始游戏接口
    public static void requestStartPanGame(String liveuid, String stream, String token, StringCallback callback) {
        OkHttpUtils.get()
                .url(AppConfig.MAIN_URL_API)
                .addParams("service", "Game.Dial")
                .addParams("liveuid", liveuid)
                .addParams("stream", stream)
                .addParams("token", token)
                .tag("requestStartGame")
                .build()
                .execute(callback);
    }

    //海盗船长开始游戏接口
    public static void requestStartHaidaoGame(String liveuid, String stream, String token, StringCallback callback) {
        OkHttpUtils.get()
                .url(AppConfig.MAIN_URL_API)
                .addParams("service", "Game.Taurus")
                .addParams("liveuid", liveuid)
                .addParams("stream", stream)
                .addParams("token", token)
                .tag("requestStartGame")
                .build()
                .execute(callback);
    }

    //开心牛仔开始游戏接口
    public static void requestStartNiuZaiGame(String liveuid, String stream, String token, StringCallback callback) {
        OkHttpUtils.get()
                .url(AppConfig.MAIN_URL_API)
                .addParams("service", "Game.Cowboy")
                .addParams("liveuid", liveuid)
                .addParams("stream", stream)
                .addParams("token", token)
                .tag("requestStartGame")
                .build()
                .execute(callback);
    }

    //扎金花下注
    public static void requestBetting(String gameId, int bettingCoin, int grade, String uid, String token, StringCallback callback) {
        OkHttpUtils.get()
                .url(AppConfig.MAIN_URL_API)
                .addParams("service", "Game.JinhuaBet")
                .addParams("gameid", gameId)
                .addParams("coin", String.valueOf(bettingCoin))
                .addParams("grade", String.valueOf(grade))
                .addParams("uid", uid)
                .addParams("token", token)
                .tag("requestBetting")
                .build()
                .execute(callback);

    }

    //转盘下注
    public static void requestPanBetting(String gameId, int bettingCoin, int grade, String uid, String token, StringCallback callback) {
        OkHttpUtils.get()
                .url(AppConfig.MAIN_URL_API)
                .addParams("service", "Game.Dial_Bet")
                .addParams("gameid", gameId)
                .addParams("coin", String.valueOf(bettingCoin))
                .addParams("grade", String.valueOf(grade))
                .addParams("uid", uid)
                .addParams("token", token)
                .tag("requestBetting")
                .build()
                .execute(callback);

    }

    //海盗船长下注
    public static void requestHaiDaoBetting(String gameId, int bettingCoin, int grade, String uid, String token, StringCallback callback) {
        OkHttpUtils.get()
                .url(AppConfig.MAIN_URL_API)
                .addParams("service", "Game.Taurus_Bet")
                .addParams("gameid", gameId)
                .addParams("coin", String.valueOf(bettingCoin))
                .addParams("grade", String.valueOf(grade))
                .addParams("uid", uid)
                .addParams("token", token)
                .tag("requestBetting")
                .build()
                .execute(callback);

    }

    //开心牛仔下注
    public static void requestNiuZaiBetting(String gameId, int bettingCoin, int grade, String uid, String token, StringCallback callback) {
        OkHttpUtils.get()
                .url(AppConfig.MAIN_URL_API)
                .addParams("service", "Game.Cowboy_Bet")
                .addParams("gameid", gameId)
                .addParams("coin", String.valueOf(bettingCoin))
                .addParams("grade", String.valueOf(grade))
                .addParams("uid", uid)
                .addParams("token", token)
                .tag("requestBetting")
                .build()
                .execute(callback);

    }


    //获取中奖结果
    public static void requestBettingResult(String uid, String gameId, StringCallback callback) {
        OkHttpUtils.get()
                .url(AppConfig.MAIN_URL_API)
                .addParams("service", "Game.settleGame")
                .addParams("gameid", gameId)
                .addParams("uid", uid)
                .build()
                .execute(callback);
    }

    public static void getBonus(String uid, String token, StringCallback callback) {
        OkHttpUtils.get()
                .url(AppConfig.MAIN_URL_API)
                .addParams("service", "User.Bonus")
                .addParams("uid", uid)
                .addParams("token", token)
                .build()
                .execute(callback);
    }

    //连麦
    //用于获取连麦推拉流地址
    public static void requestLVBAddrForLinkMic(String uid, StringCallback callback) {
        OkHttpUtils.get()
                .url(AppConfig.MAIN_URL_API)
                .addParams("service", "Linkmic.RequestLVBAddrForLinkMic")
                .addParams("uid", uid)
                .build()
                .execute(callback);
    }

    public static void mergeVideoStream(String uid, String mergeparams, StringCallback callback) {
        OkHttpUtils.get()
                .url(AppConfig.MAIN_URL_API)
                .addParams("service", "Linkmic.MergeVideoStream")
                .addParams("uid", uid)
                .addParams("mergeparams", mergeparams)
                .build()
                .execute(callback);
    }

    public static void requestPlayUrlWithSignForLinkMic(String uid, String originStreamUrl, StringCallback callback) {
        OkHttpUtils.get()
                .url(AppConfig.MAIN_URL_API)
                .addParams("service", "Linkmic.RequestPlayUrlWithSignForLinkMic")
                .addParams("uid", uid)
                .addParams("originStreamUrl", originStreamUrl)
                .build()
                .execute(callback);
    }

    public static boolean getUserLists(String liveuid, String stream, StringCallback callback) {
        OkHttpUtils.get()
                .url(AppConfig.MAIN_URL_API)
                .addParams("service", "Live.getUserLists")
                .addParams("liveuid", liveuid)
                .addParams("stream", stream)
                .build()
                .execute(callback);
        return true;
    }


    //判断自己有没有被对方拉黑
    public static void checkBlack(String touid, StringCallback callback) {
        OkHttpUtils.get()
                .url(AppConfig.MAIN_URL_API)
                .addParams("service", "User.checkBlack")
                .addParams("uid", AppContext.getInstance().getLoginUid())
                .addParams("touid", touid)
                .build()
                .execute(callback);
    }

    //用于房间计时扣费
    public static void timeCharge(String uid, String token, String liveuid, String stream, StringCallback callback) {
        OkHttpUtils.get()
                .url(AppConfig.MAIN_URL_API)
                .addParams("service", "Live.timeCharge")
                .addParams("uid", uid)
                .addParams("token", token)
                .addParams("stream", stream)
                .addParams("liveuid", liveuid)
                .build()
                .execute(callback);
    }

    //用于主播修改直播类型
    public static void changeLiveType(String uid, String token, String stream, String type, String type_val, StringCallback callback) {
        OkHttpUtils.get()
                .url(AppConfig.MAIN_URL_API)
                .addParams("service", "Live.changeLiveType")
                .addParams("uid", uid)
                .addParams("token", token)
                .addParams("stream", stream)
                .addParams("type", type)
                .addParams("type_val", type_val)
                .build()
                .execute(callback);
    }

    //获取用户余额
    public static void getCoin(String uid, String token, StringCallback callback) {
        OkHttpUtils.get().url(AppConfig.MAIN_URL_API)
                .addParams("service", "Live.getCoin")
                .addParams("uid", uid)
                .addParams("token", token)
                .tag("getCoin")
                .build()
                .execute(callback);
    }


    //获取七牛云token的接口
    public static void getQiniuToken(StringCallback callback) {
        OkHttpUtils.get()
                .url(AppConfig.MAIN_URL_API)
                .addParams("service", "Video.getQiniuToken")
                .build()
                .execute(callback);
    }

    //上传视频的接口
    public static void uploadVideo(String title, String href, String thumb, StringCallback callback) {
        OkHttpUtils.get()
                .url(AppConfig.MAIN_URL_API)
                .addParams("service", "Video.setVideo")
                .addParams("uid", AppContext.getInstance().getLoginUid())
                .addParams("token", AppContext.getInstance().getToken())
                .addParams("title", title)
                .addParams("thumb", thumb)
                .addParams("href", href)
                .addParams("lat",AppContext.lat)
                .addParams("lng",AppContext.lng)
                .build()
                .execute(callback);
    }

    public static void addLike(String uid, String videoid, StringCallback callback) {
        OkHttpUtils.get()
                .url(AppConfig.MAIN_URL_API)
                .addParams("service", "Video.addLike")
                .addParams("uid", uid)
                .addParams("videoid", videoid)
                .build()
                .execute(callback);
    }

    public static void getComments(String videoid, String p, StringCallback callback) {
        OkHttpUtils.get()
                .url(AppConfig.MAIN_URL_API)
                .addParams("service", "Video.getComments")
                .addParams("uid", AppContext.getInstance().getLoginUid())
                .addParams("videoid", videoid)
                .addParams("p", p)
                .build()
                .execute(callback);
    }


    public static void setComment(String touid, String videoid, String content, String commentid, String parentid, StringCallback callback) {
        OkHttpUtils.get()
                .url(AppConfig.MAIN_URL_API)
                .addParams("service", "Video.setComment")
                .addParams("videoid", videoid)
                .addParams("uid", AppContext.getInstance().getLoginUid())
                .addParams("token", AppContext.getInstance().getToken())
                .addParams("touid", touid)
                .addParams("content", content)
                .addParams("commentid", commentid)
                .addParams("parentid", parentid)
                .build()
                .execute(callback);
    }

    public static void getMyVideo(int p,String uid, String token, StringCallback callback) {
        OkHttpUtils.get()
                .url(AppConfig.MAIN_URL_API)
                .addParams("service", "Video.getMyVideo")
                .addParams("uid", uid)
                .addParams("token", token)
                .addParams("p",String.valueOf(p))
                .build()
                .execute(callback);
    }

    public static void setVideoReport(String uid, String token, String videoid, StringCallback callback) {
        OkHttpUtils.get()
                .url(AppConfig.MAIN_URL_API)
                .addParams("service", "Video.report")
                .addParams("uid", uid)
                .addParams("token", token)
                .addParams("videoid", videoid)
                .addParams("content", "涉嫌违规")
                .build()
                .execute(callback);
    }

    public static void setVideoRel(String uid, String token, String videoid, StringCallback callback) {
        OkHttpUtils.get()
                .url(AppConfig.MAIN_URL_API)
                .addParams("service", "Video.del")
                .addParams("uid", uid)
                .addParams("token", token)
                .addParams("videoid", videoid)
                .build()
                .execute(callback);
    }

    //用于用户首次登录设置分销关系
    public static void setDistribut(String uid, String token, String code, StringCallback callback) {
        OkHttpUtils.get()
                .url(AppConfig.MAIN_URL_API)
                .addParams("service", "User.setDistribut")
                .addParams("uid", uid)
                .addParams("token", token)
                .addParams("code", code)
                .build()
                .execute(callback);
    }

    //附近
    public static void getDataFuJin(int p,String lat, String lng, StringCallback callback) {
        OkHttpUtils.get()
                .url(AppConfig.MAIN_URL_VIDEO)
                .addParams("service", "Home.getNearby")
                .addParams("lat", lat)
                .addParams("lng", lng)
                .addParams("uid", AppContext.getInstance().getLoginUid())
                .addParams("p",String.valueOf(p))
                .build()
                .execute(callback);
    }

    //关注里面的视频
    public static void getAttentionVideo(int p,StringCallback callback) {
        OkHttpUtils.get()
                .url(AppConfig.MAIN_URL_VIDEO)
                .addParams("service", "video.getAttentionVideo")
                .addParams("uid", AppContext.getInstance().getLoginUid())
                .addParams("p",String.valueOf(p))
                .build()
                .execute(callback);
    }

    //关注里面的视频
    public static void getReplys(String commentid, String p, StringCallback callback) {
        OkHttpUtils.get()
                .url(AppConfig.MAIN_URL_VIDEO)
                .addParams("service", "Video.getReplys")
                .addParams("commentid", commentid)
                .addParams("uid", AppContext.getInstance().getLoginUid())
                .addParams("p", p)
                .build()
                .execute(callback);
    }

    //添加分享数
    public static void addShare(String videoid, StringCallback callback) {
        OkHttpUtils.get()
                .url(AppConfig.MAIN_URL_VIDEO)
                .addParams("service", "Video.addShare")
                .addParams("videoid", videoid)
                .addParams("uid", AppContext.getInstance().getLoginUid())
                .build()
                .execute(callback);
    }


    //评论点赞
    public static void addCommentLike(String commentid, StringCallback callback) {
        OkHttpUtils.get()
                .url(AppConfig.MAIN_URL_VIDEO)
                .addParams("service", "Video.addCommentLike")
                .addParams("commentid", commentid)
                .addParams("uid", AppContext.getInstance().getLoginUid())
                .build()
                .execute(callback);
    }

    //视频拉黑
    public static void setVideoBlack(String videoid, StringCallback callback) {
        OkHttpUtils.get()
                .url(AppConfig.MAIN_URL_VIDEO)
                .addParams("service", "Video.setBlack")
                .addParams("uid", AppContext.getInstance().getLoginUid())
                .addParams("videoid", videoid)
                .build()
                .execute(callback);
    }

    //视频踩
    public static void addVideoStep(String videoid, StringCallback callback) {
        OkHttpUtils.get()
                .url(AppConfig.MAIN_URL_VIDEO)
                .addParams("service", "Video.addStep")
                .addParams("uid", AppContext.getInstance().getLoginUid())
                .addParams("videoid", videoid)
                .build()
                .execute(callback);
    }

    //获取视频的最新信息等
    public static void getVideoInfo(String videoid, StringCallback callback) {
        OkHttpUtils.get()
                .url(AppConfig.MAIN_URL_VIDEO)
                .addParams("service", "Video.getVideo")
                .addParams("uid", AppContext.getInstance().getLoginUid())
                .addParams("videoid", videoid)
                .build()
                .execute(callback);
    }
    //获取视频的最新信息等
    public static void getHomeVideoInfo(String touid,int p, StringCallback callback) {
        OkHttpUtils.get()
                .url(AppConfig.MAIN_URL_VIDEO)
                .addParams("service", "Video.getHomeVideo")
                .addParams("uid", AppContext.getInstance().getLoginUid())
                .addParams("touid", touid)
                .addParams("p",String.valueOf(p))
                .build()
                .execute(callback);
    } //获取视频的最新信息等
    public static void getprivatelive( StringCallback callback) {
        OkHttpUtils.get()
                .url(AppConfig.MAIN_URL_VIDEO)
                .addParams("service", "home.getprivatelive")
                .build()
                .execute(callback);
    }

    /*================最新的接口数据================*/

    //登录
    public static void loginNew(String userName, String password, StringCallback callback) {
        String url = AppConfig.URL_COMMON;
        OkHttpUtils.post()
                .url(url+"/app_api/login.html")
                .addParams("userName", userName)
                .addParams("password", password)
                .addParams("uuid", AppConfig.GLOBAL_UUID)
                .build()
                .execute(callback);
    }

    //登录
    public static void loginByUUID(String uuid, StringCallback callback) {
        String url = AppConfig.URL_COMMON;
        OkHttpUtils.post()
                .url(url+"/app_api/loginByUUID.html")
                .addParams("uuid", uuid)
                .build()
                .execute(callback);
    }

    //注册
    public static void regNew(String username,String nickname, String password, String confirm_password,StringCallback callback) {
        String url = AppConfig.URL_COMMON;
        OkHttpUtils.post()
                .url(url+"/app_api/register.html")
                .addParams("username", username)
                .addParams("nickname", nickname)
                .addParams("password", password)
                .addParams("password", password)
                .addParams("confirm_password", confirm_password)
                .addParams("uuid", AppConfig.GLOBAL_UUID)
                .build()
                .execute(callback);

    }

    //首页数据
    public static void home(String token,StringCallback callback) {
        String url = AppConfig.URL_COMMON;
        OkHttpUtils.post()
                .url(url+"/app_index/home")
                .addParams("token", token)
                .build()
                .execute(callback);

    }

    //获取分享信息
    public static void shareInfo(String token,StringCallback callback) {
        String url = AppConfig.URL_COMMON;
        OkHttpUtils.post()
                .url(url+"/app_share/index.html")
                .addParams("token", token)
                .addParams("uuid", AppConfig.GLOBAL_UUID)
                .build()
                .execute(callback);

    }

    //获取用户信息
    public static void memberInfo(String token,StringCallback callback) {
        String url = AppConfig.URL_COMMON;
        OkHttpUtils.post()
                .url(url+"/app_member/member.html")
                .addParams("token", token)
                .addParams("uuid", AppConfig.GLOBAL_UUID)
                .build()
                .execute(callback);

    }

    //获取视频分类
    public static void videoClass(String token,StringCallback callback) {

        String url = AppConfig.URL_COMMON;
        OkHttpUtils.post()
                .url(url+"/app_video/getClassList.html")
                .addParams("token", token)
                .addParams("uuid", AppConfig.GLOBAL_UUID)
                .build()
                .execute(callback);
    }

    //获取视频列表
    public static void videoList(int page,int pagesize,int cid,int sub_cid,int tag_id,String keyWord,String token,String orderCode,StringCallback callback) {
        String url = AppConfig.URL_COMMON;
        OkHttpUtils.post()
                .url(url+"/app_video/lists.html")
                .addParams("token", token)
                .addParams("uuid", AppConfig.GLOBAL_UUID)
                .addParams("keyWord", keyWord)
                .addParams("tag_id", tag_id+"")
                .addParams("sub_cid", sub_cid+"")
                .addParams("cid", cid+"")
                .addParams("orderCode", orderCode+"")
                .addParams("pagesize", pagesize+"")
                .addParams("page", page+"")
                .build()
                .execute(callback);

    }

    //获取视频详情
    public static void videoDetail(int id,String token,StringCallback callback) {
        String url = AppConfig.URL_COMMON;
        OkHttpUtils.post()
                .url(url+"/app_video/play.html")
                .addParams("token", token)
                .addParams("uuid", AppConfig.GLOBAL_UUID)
                .addParams("id", id+"")
                .build()
                .execute(callback);

    }



    //评论 视频resourceType=1
    public static void commentNew(int resourceId,int resourceType,String content,String token,StringCallback callback) {
        String url = AppConfig.URL_COMMON;
        OkHttpUtils.post()
                .url(url+"/app_api/comment.html")
                .addParams("token", token)
                .addParams("uuid", AppConfig.GLOBAL_UUID)
                .addParams("content", content+"")
                .addParams("resourceId", resourceId+"")
                .addParams("resourceType", resourceType+"")
                .build()
                .execute(callback);

    }


    //获取评论 视频resourceType=1
    public static void getCommentList(int page,int pagesize,int resourceId,int resourceType,String token,StringCallback callback) {
        String url = AppConfig.URL_COMMON;
        OkHttpUtils.post()
                .url(url+"/app_api/getCommentList.html")
                .addParams("token", token)
                .addParams("uuid", AppConfig.GLOBAL_UUID)
                .addParams("page", page+"")
                .addParams("pagesize", pagesize+"")
                .addParams("resourceId", resourceId+"")
                .addParams("resourceType", resourceType+"")
                .build()
                .execute(callback);

    }


    //收藏 视频resourceType=1
    public static void colletionNew(int resourceId,int resourceType,String token,StringCallback callback) {
        String url = AppConfig.URL_COMMON;
        OkHttpUtils.post()
                .url(url+"/app_api/colletion.html")
                .addParams("token", token)
                .addParams("uuid", AppConfig.GLOBAL_UUID)
                .addParams("resourceId", resourceId+"")
                .addParams("resourceType", resourceType+"")
                .build()
                .execute(callback);
    }


    //点赞 视频resourceType=1
    public static void giveGood(int resourceId,int resourceType,String token,StringCallback callback) {
        String url = AppConfig.URL_COMMON;
        OkHttpUtils.post()
                .url(url+"/app_api/giveGood")
                .addParams("token", token)
                .addParams("uuid", AppConfig.GLOBAL_UUID)
                .addParams("resourceId", resourceId+"")
                .addParams("resourceType", resourceType+"")
                .build()
                .execute(callback);

    }

    //礼物
    public static void getgift(int resourceId,int resourceType,String token,StringCallback callback) {
        String url = AppConfig.URL_COMMON;
        OkHttpUtils.post()
                .url(url+"/app_api/getgift.html")
                .addParams("token", token)
                .addParams("uuid", AppConfig.GLOBAL_UUID)
                .addParams("resourceId", resourceId+"")
                .addParams("resourceType", resourceType+"")
                .addParams("func", "getNameAndTime")
                .build()
                .execute(callback);

    }


    /**
     * 付费观看视频
     * @param id
     * @param surePlay  1 表示确认观看 0 不是确认
     * @param isTrySee 是否试看
     * @param token
     * @param callback
     */
    public static void payVideo(int id,int surePlay,boolean isTrySee,String token,StringCallback callback) {
        String url = AppConfig.URL_COMMON;
        OkHttpUtils.post()
                .url(url+"/app_api/payVideo.html")
                .addParams("token", token)
                .addParams("uuid", AppConfig.GLOBAL_UUID)
                .addParams("id", id+"")
                .addParams("surePlay", surePlay+"")
                .addParams("isTrySee", isTrySee?"true":"false")
                .addParams("token", "token")
                .build()
                .execute(callback);

    }


    public static void recharge(String token,StringCallback callback) {
        String url = AppConfig.URL_COMMON;
        OkHttpUtils.post()
                .url(url+"/app_system_pay/recharge.html")
                .addParams("token", token)
                .addParams("uuid", AppConfig.GLOBAL_UUID)
                .build()
                .execute(callback);

    }


    /**
     *
     * @param buyType  1 金币充值  2 VIp
     * @param payCode   支付方式
     * @param price     金额
     * @param packageId  套餐id
     * @param token
     * @param callback
     */
    public static void createOrder(int buyType,String payCode,double price,int packageId,String token,StringCallback callback) {
        String url = AppConfig.URL_COMMON;
        OkHttpUtils.post()
                .url(url+"/app_system_pay/createOrder.html")
                .addParams("buyType", buyType+"")
                .addParams("payCode", payCode)
                .addParams("price", price+"")
                .addParams("packageId", packageId+"")
                .addParams("token", token)
                .addParams("uuid", AppConfig.GLOBAL_UUID)
                .build()
                .execute(callback);

    }


    //我的视频
    public static void myVideoList(int page,int pagesize,String token,StringCallback callback) {
        String url = AppConfig.URL_COMMON;
        OkHttpUtils.post()
                .url(url+"/app_member/video.html")
                .addParams("token", token)
                .addParams("uuid", AppConfig.GLOBAL_UUID)
                .addParams("pagesize", pagesize+"")
                .addParams("page", page+"")
                .build()
                .execute(callback);

    }

    //我收藏的视频
    public static void myCollectVideoList(int page,int pagesize,String token,StringCallback callback) {
        String url = AppConfig.URL_COMMON;
        OkHttpUtils.post()
                .url(url+"/app_member/collection_video.html")
                .addParams("token", token)
                .addParams("uuid", AppConfig.GLOBAL_UUID)
                .addParams("pagesize", pagesize+"")
                .addParams("page", page+"")
                .build()
                .execute(callback);
    }


    //获取签到状态
    public static void isSign(String token,StringCallback callback) {
        String url = AppConfig.URL_COMMON;
        OkHttpUtils.post()
                .url(url+"/app_api/isSign.html")
                .addParams("token", token)
                .addParams("uuid", AppConfig.GLOBAL_UUID)
                .build()
                .execute(callback);
    }

    //签到
    public static void sign(String token,StringCallback callback) {
        String url = AppConfig.URL_COMMON;
        OkHttpUtils.post()
                .url(url+"/app_api/sign.html")
                .addParams("token", token)
                .addParams("uuid", AppConfig.GLOBAL_UUID)
                .build()
                .execute(callback);
    }


    /**
     * 添加视频
     * @param title 标题  长度255
     * @param gold 金币
     * @param tag 标签 如1,2,3
     * @param classId 分类id
     * @param videoPath 视频播放地址
     * @param picPath  视频缩略图
     * @param play_time 播放时长 20:45  长度20
     * @param token
     * @param callback
     */
    public static void addVideo(String title,String gold,String tag,String classId,String videoPath,String picPath,String play_time,String actor,String token,StringCallback callback) {
        String url = AppConfig.URL_COMMON;
        OkHttpUtils.post()
                .url(url+"/app_member/addVideo.html")
                .addParams("token", token)
                .addParams("uuid", AppConfig.GLOBAL_UUID)
                .addParams("title", title)
                .addParams("gold", gold)
                .addParams("tag", tag)
                .addParams("class", classId)
                .addParams("url", videoPath)
                .addParams("thumbnail", picPath)
                .addParams("play_time", play_time)
                .addParams("actor", actor)
                .build()
                .execute(callback);
    }

    /**
     * 获取卡密信息
     * @param card_number
     * @param token
     * @param callback
     */
    public static void getCardPasswordInfo(String card_number,String token,StringCallback callback) {
        String url = AppConfig.URL_COMMON;
        OkHttpUtils.post()
                .url(url+"/app_api/get_card_password_info.html")
                .addParams("token", token)
                .addParams("uuid", AppConfig.GLOBAL_UUID)
                .addParams("card_number", card_number)
                .addParams("verifyCode", "")
                .build()
                .execute(callback);
    }


    /**
     * 使用卡密
     * @param card_number
     * @param id
     * @param token
     * @param callback
     */
    public static void useCardPassword(String card_number,int id,String token,StringCallback callback) {
        String url = AppConfig.URL_COMMON;
        OkHttpUtils.post()
                .url(url+"/app_api/use_card_password.html")
                .addParams("token", token)
                .addParams("card_number", card_number)
                .addParams("id", id+"")
                .addParams("uuid", AppConfig.GLOBAL_UUID)
                .build()
                .execute(callback);
    }

    /**
     * 卡密咨询客服 返回url 如果url等于# 则表示没配置 不跳转
     * @param token
     * @param callback
     */
    public static void buyCardPasswordUrl(String token,StringCallback callback) {
        String url = AppConfig.URL_COMMON;
        OkHttpUtils.post()
                .url(url+"/app_api/buy_cardpassword_uri.html")
                .addParams("token", token)
                .addParams("uuid", AppConfig.GLOBAL_UUID)
                .build()
                .execute(callback);
    }

    //充值记录
    public static void recordPay(int page,int pagesize,String token,StringCallback callback) {
        String url = AppConfig.URL_COMMON;
        OkHttpUtils.post()
                .url(url+"/app_member/record_pay.html")
                .addParams("token", token)
                .addParams("pagesize", pagesize+"")
                .addParams("page", page+"")
                .addParams("uuid", AppConfig.GLOBAL_UUID)
                .build()
                .execute(callback);
    }

    //视频消费记录
    public static void record_video(int page,int pagesize,String token,StringCallback callback) {
        String url = AppConfig.URL_COMMON;
        OkHttpUtils.post()
                .url(url+"/app_member/record_video.html")
                .addParams("token", token)
                .addParams("pagesize", pagesize+"")
                .addParams("page", page+"")
                .addParams("uuid", AppConfig.GLOBAL_UUID)
                .build()
                .execute(callback);
    }

    //获取版本更新信息
    public static void getVersion(int versionCode, String versionName, StringCallback callback) {
        String url = AppConfig.URL_COMMON;
        OkHttpUtils.post()
                .url(url+"/app_index/version.html")
                .addParams("versionType", "android")
                .addParams("versionCode", versionCode+"")
                .addParams("versionName", versionName+"")
                .addParams("uuid", AppConfig.GLOBAL_UUID)
                .build()
                .execute(callback);
    }





    //获取版本更新信息
    public static void uploadFile(String path, UploadParam uploadParam, String token, StringCallback callback) {
        File file=new File(path);
        String url = AppConfig.URL_COMMON;
        OkHttpUtils.post()
                .url(url+"/app_uploader/appXuploader.html")
                .addParams("token", token)
                .addParams("chunk", uploadParam.getChunk()+"")
                .addParams("chunks", uploadParam.getChunks()+"")
                .addParams("post_url", uploadParam.getPost_url()+"")
                .addParams("serverType", uploadParam.getServerType()+"")
                .addParams("uuid", AppConfig.GLOBAL_UUID)
                .addFile("file", file.getName(),file)
                .build()
                .execute(callback);
    }


    /**
     * 打赏
     * @param itemid 打赏id
     * @param projectid 视频的id
     * @param type  视频 1
     * @param itemprice 金额
     * @param token
     * @param callback
     */
    public static void reward(int  itemid, int  projectid, int type,double itemprice,String token, StringCallback callback) {
        String url = AppConfig.URL_COMMON;
        OkHttpUtils.post()
                .url(url+"/app_api/reward.html")
                .addParams("token", token)
                .addParams("itemid", itemid+"")
                .addParams("projectid", projectid+"")
                .addParams("type", type+"")
                .addParams("itemprice", itemprice+"")
                .addParams("uuid", AppConfig.GLOBAL_UUID)
                .build()
                .execute(callback);
    }

    /**
     * 检查线路
     * @param token
     * @param callback
     */
    public static void checkRoute(String token, StringCallback callback) {
        String url = AppConfig.URL_CHECK_ROUTE;
        OkHttpUtils.post()
                .url(url+"/app_index/checkRoute.html")
                .addParams("token", token)
                .addParams("uuid", AppConfig.GLOBAL_UUID)
                .build()
                .execute(callback);
    }


}

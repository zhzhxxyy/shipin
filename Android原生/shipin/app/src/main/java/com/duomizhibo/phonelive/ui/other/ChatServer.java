package com.duomizhibo.phonelive.ui.other;


import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.ImageSpan;

import com.google.gson.Gson;
import com.duomizhibo.phonelive.AppConfig;
import com.duomizhibo.phonelive.AppContext;
import com.duomizhibo.phonelive.R;
import com.duomizhibo.phonelive.api.remote.ApiUtils;
import com.duomizhibo.phonelive.api.remote.PhoneLiveApi;
import com.duomizhibo.phonelive.bean.ChatBean;
import com.duomizhibo.phonelive.bean.SendGiftBean;
import com.duomizhibo.phonelive.bean.SimpleUserInfo;
import com.duomizhibo.phonelive.bean.UserBean;
import com.duomizhibo.phonelive.interf.ChatServerInterface;
import com.duomizhibo.phonelive.utils.LiveUtils;
import com.duomizhibo.phonelive.utils.SocketMsgUtils;
import com.duomizhibo.phonelive.utils.StringUtils;
import com.duomizhibo.phonelive.utils.TDevice;
import com.duomizhibo.phonelive.utils.TLog;
import com.duomizhibo.phonelive.widget.CenteredImageSpan;
import com.duomizhibo.phonelive.widget.VerticalImageSpan;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import okhttp3.Call;

/**
 * 直播间业务逻辑处理
 */
public class ChatServer {

    public static final int[] heartImg = new int[]{R.drawable.plane_heart_cyan, R.drawable.plane_heart_pink, R.drawable.plane_heart_red, R.drawable.plane_heart_yellow, R.drawable.plane_heart_cyan};

    public static final String EVENT_NAME = "broadcast";

    private static final int SEND_CHAT = 2;//发言

    private static final int SYSTEM_NOT = 1;//系统消息

    private static final int NOTICE = 0;//提醒

    private static final int PRIVELEGE = 4;//特权操作

    private static final int JINHUA_GAME_MSG = 15;

    private static final int LUCKPAN = 16;

    private static final int HAIDAO = 18;

    private static final int KAIXINNIUZAI = 17;

    private static final int LINKMIC = 10;

    public static int LIVE_USER_NUMS = 0;

    private static final int CHARGE = 27;

    private static final int UPDATECOIN = 26;

    private static final int GAME_NOTICE = 99;

    private Socket mSocket;

    private Context context;

    private ChatServerInterface mChatServer;

    private Gson mGson;

    //服务器连接关闭监听
    private Emitter.Listener onDisconnect = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            TLog.log("socket断开连接");
        }
    };

    //服务器连接失败监听
    private Emitter.Listener onError = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            mChatServer.onError();
            TLog.log("socket连接Error");
        }
    };
    //服务器消息监听
    private Emitter.Listener onMessage = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            try {
                JSONArray jsonArray = (JSONArray) args[0];
                for (int i = 0; i < jsonArray.length(); i++) {
                    if (jsonArray.getString(i).equals("stopplay")) {
                        mChatServer.onSystemNot(1);
                        return;
                    }
                    SocketMsgUtils socketMsg = SocketMsgUtils.getFormatJsonMode(jsonArray.getString(i));

                    int action = StringUtils.toInt(socketMsg.getAction());

                    //获取用户动作
                    switch (StringUtils.toInt(socketMsg.getMsgtype())) {
                        case SEND_CHAT://聊天
                            if (action == 0) {//公聊
                                onMessage(socketMsg);
                            }
                            break;
                        case SYSTEM_NOT://系统
                            if (action == 0) {
                                //发送礼物
                                onSendGift(socketMsg);
                            } else if (action == 18) {
                                //房间关闭
                                mChatServer.onSystemNot(0);
                            } else if (action == 7) {
                                //弹幕
                                onDanmuMessage(socketMsg);
                            } else if (action == 19) {
                                //房间关闭
                                mChatServer.onSystemNot(2);
                            }
                            break;
                        case NOTICE://通知
                            if (action == 0) {
                                //上下线
                                ChatServer.LIVE_USER_NUMS += 1;
                                mChatServer.onUserStateChange(socketMsg, mGson.fromJson(socketMsg.getCt(), UserBean.class), true);
                            } else if (action == 1) {
                                ChatServer.LIVE_USER_NUMS -= 1;
                                mChatServer.onUserStateChange(socketMsg, mGson.fromJson(socketMsg.getCt(), UserBean.class), false);
                            } else if (action == 2) {
                                //点亮
                                mChatServer.onLit(socketMsg);
                            } else if (action == 3) {
                                //僵尸粉丝推送
                                mChatServer.onAddZombieFans(socketMsg);
                            }
                            break;
                        case PRIVELEGE:
                            onPrivate(socketMsg);
                            break;
                        case LINKMIC:
                            mChatServer.onLinkMic(socketMsg);
                            break;
                        case CHARGE:
                            mChatServer.onCharge(socketMsg);
                            break;
                        case UPDATECOIN:
                            mChatServer.onUpdateCoin(socketMsg);
                            break;
                        case GAME_NOTICE:
                            mChatServer.onGameNotice(socketMsg);
                            break;
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };
    //特权
    private void onPrivate(SocketMsgUtils msgUtils) throws JSONException {
        SpannableStringBuilder name = new SpannableStringBuilder("系统消息" + " : " + msgUtils.getCt());
        name.setSpan(new ForegroundColorSpan(Color.parseColor("#DFA640")), 0, 6,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        name.setSpan(new ForegroundColorSpan(Color.parseColor("#A98BE3")), 6, name.length(),
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ChatBean c = new ChatBean();
        c.setType(13);
        c.setUserNick(name);
        mChatServer.onPrivilegeAction(msgUtils, c);
    }

    private void onDanmuMessage(SocketMsgUtils msgUtils) throws JSONException {
        String ct = msgUtils.getCt();
        ChatBean c = new ChatBean();
        SimpleUserInfo userInfo = new SimpleUserInfo();
        userInfo.id = msgUtils.getUid();
        userInfo.level = msgUtils.getLevel();
        userInfo.user_nicename = msgUtils.getUname();
        userInfo.avatar = msgUtils.getUHead();
        c.setSimpleUserInfo(userInfo);

        JSONObject jsonObject = new JSONObject(ct);

        c.setContent(jsonObject.getString("content"));
        mChatServer.onMessageListen(msgUtils, 1, c);
    }



    //礼物信息
    private void onSendGift(SocketMsgUtils msgUtils) throws JSONException {
        ChatBean c = new ChatBean();
        SimpleUserInfo userInfo = new SimpleUserInfo();
        userInfo.id = msgUtils.getUid();
        userInfo.level = msgUtils.getLevel();
        userInfo.user_nicename = msgUtils.getUname();
        userInfo.avatar = msgUtils.getUHead();
        userInfo.vip_type = msgUtils.getParam("vip_type", "0");
        userInfo.goodnum = msgUtils.getParam("liangname", "0");
        c.setSimpleUserInfo(userInfo);
        SendGiftBean mSendGiftInfo = mGson.fromJson(msgUtils.getCt(), SendGiftBean.class);//gift info
        mSendGiftInfo.setAvatar(userInfo.avatar);
        mSendGiftInfo.setEvensend(msgUtils.getParam("evensend", "n"));
        mSendGiftInfo.setNicename(userInfo.user_nicename);
        String uname = "";
        SpannableStringBuilder name = null;
        VerticalImageSpan vipImage = null;
        VerticalImageSpan goodnumImage = null;
        //等级
        Drawable levelDrawable = context.getResources().getDrawable(LiveUtils.getLevelRes(userInfo.level));
        levelDrawable.setBounds(0, 0, (int) TDevice.dpToPixel(16), (int) TDevice.dpToPixel(15));
        VerticalImageSpan levelImage = new VerticalImageSpan(levelDrawable);
        //vip
        if (StringUtils.toInt(userInfo.vip_type) != 0) {
            Drawable vipDrawable = context.getResources().getDrawable(DrawableRes.Vip[StringUtils.toInt(userInfo.vip_type) - 1]);
            vipDrawable.setBounds(0, 0, (int) TDevice.dpToPixel(20), (int) TDevice.dpToPixel(20));
            vipImage = new VerticalImageSpan(vipDrawable);
        }
        //靓号
        if (StringUtils.toInt(userInfo.goodnum) != 0) {
            Drawable goodnumDrawable = context.getResources().getDrawable(R.drawable.goodnum);
            goodnumDrawable.setBounds(0, 0, (int) TDevice.dpToPixel(15), (int) TDevice.dpToPixel(15));
            goodnumImage = new VerticalImageSpan(goodnumDrawable);
        }
        if (StringUtils.toInt(userInfo.vip_type) != 0 && StringUtils.toInt(userInfo.goodnum) != 0) {
            uname = "_ _ _ " + userInfo.user_nicename + " : ";
            name = new SpannableStringBuilder(uname + "我送了" + mSendGiftInfo.getGiftcount() + "个" + mSendGiftInfo.getGiftname());
            //添加等级图文混合
            name.setSpan(new ForegroundColorSpan(Color.parseColor("#f2b437")), 6, uname.length(),
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            name.setSpan(vipImage, 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            name.setSpan(goodnumImage, 2, 3, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            name.setSpan(levelImage, 4, 5, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        } else if (StringUtils.toInt(userInfo.vip_type) != 0 && StringUtils.toInt(userInfo.goodnum) == 0) {
            uname = "_ _ " + userInfo.user_nicename + " : ";
            name = new SpannableStringBuilder(uname + "我送了" + mSendGiftInfo.getGiftcount() + "个" + mSendGiftInfo.getGiftname());
            //添加等级图文混合
            name.setSpan(new ForegroundColorSpan(Color.parseColor("#f2b437")), 4, uname.length(),
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            name.setSpan(vipImage, 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            name.setSpan(levelImage, 2, 3, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        } else if (StringUtils.toInt(userInfo.vip_type) == 0 && StringUtils.toInt(userInfo.goodnum) != 0) {
            uname = "_ _ " + userInfo.user_nicename + " : ";
            name = new SpannableStringBuilder(uname + "我送了" + mSendGiftInfo.getGiftcount() + "个" + mSendGiftInfo.getGiftname());
            //添加等级图文混合
            name.setSpan(new ForegroundColorSpan(Color.parseColor("#f2b437")), 4, uname.length(),
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            name.setSpan(goodnumImage, 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            name.setSpan(levelImage, 2, 3, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        } else if (StringUtils.toInt(userInfo.vip_type) == 0 && StringUtils.toInt(userInfo.goodnum) == 0) {
            uname = "_ " + userInfo.user_nicename + " : ";
            name = new SpannableStringBuilder(uname + "我送了" + mSendGiftInfo.getGiftcount() + "个" + mSendGiftInfo.getGiftname());
            //添加等级图文混合
            name.setSpan(new ForegroundColorSpan(Color.parseColor("#f2b437")), 1, uname.length(),
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            name.setSpan(levelImage, 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        name.setSpan(new ForegroundColorSpan(Color.parseColor("#f16678")), uname.length(), name.length(),
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        c.setUserNick(name);
        mChatServer.onShowSendGift(mSendGiftInfo, c);
    }


    //消息信息
    private void onMessage(SocketMsgUtils msgUtils) throws JSONException {
        ChatBean c = new ChatBean();
        SimpleUserInfo userInfo = new SimpleUserInfo();
        userInfo.id = msgUtils.getUid();
        userInfo.level = msgUtils.getLevel();
        userInfo.user_nicename = msgUtils.getUname();
        userInfo.avatar = msgUtils.getUHead();
        userInfo.vip_type = msgUtils.getParam("vip_type", "0");
        userInfo.goodnum = msgUtils.getParam("liangname", "0");
        c.setSimpleUserInfo(userInfo);
        String uname = "";
        SpannableStringBuilder name = null;
        VerticalImageSpan vipImage = null;
        VerticalImageSpan goodnumImage = null;
        //等级
        Drawable levelDrawable = context.getResources().getDrawable(LiveUtils.getLevelRes(userInfo.level));
        levelDrawable.setBounds(0, 0, (int) TDevice.dpToPixel(16), (int) TDevice.dpToPixel(15));
        VerticalImageSpan levelImage = new VerticalImageSpan(levelDrawable);
        //vip
        if (StringUtils.toInt(userInfo.vip_type) != 0) {
            Drawable vipDrawable = context.getResources().getDrawable(DrawableRes.Vip[StringUtils.toInt(userInfo.vip_type) - 1]);
            vipDrawable.setBounds(0, 0, (int) TDevice.dpToPixel(20), (int) TDevice.dpToPixel(20));
            vipImage = new VerticalImageSpan(vipDrawable);
        }
        //靓号
        if (StringUtils.toInt(userInfo.goodnum) != 0) {
            Drawable goodnumDrawable = context.getResources().getDrawable(R.drawable.goodnum);
            goodnumDrawable.setBounds(0, 0, (int) TDevice.dpToPixel(15), (int) TDevice.dpToPixel(15));
            goodnumImage = new VerticalImageSpan(goodnumDrawable);
        }

        if (StringUtils.toInt(userInfo.vip_type) != 0 && StringUtils.toInt(userInfo.goodnum) != 0) {
            uname = "_ _ _ " + userInfo.user_nicename + " : ";
            name = new SpannableStringBuilder(uname + msgUtils.getCt());
            //添加等级图文混合
            name.setSpan(new ForegroundColorSpan(Color.parseColor("#f2b437")), 6, uname.length(),
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            name.setSpan(vipImage, 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            name.setSpan(goodnumImage, 2, 3, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            name.setSpan(levelImage, 4, 5, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        } else if (StringUtils.toInt(userInfo.vip_type) != 0 && StringUtils.toInt(userInfo.goodnum) == 0) {
            uname = "_ _ " + userInfo.user_nicename + " : ";
            name = new SpannableStringBuilder(uname + msgUtils.getCt());
            //添加等级图文混合
            name.setSpan(new ForegroundColorSpan(Color.parseColor("#f2b437")), 4, uname.length(),
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            name.setSpan(vipImage, 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            name.setSpan(levelImage, 2, 3, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        } else if (StringUtils.toInt(userInfo.vip_type) == 0 && StringUtils.toInt(userInfo.goodnum) != 0) {
            uname = "_ _ " + userInfo.user_nicename + " : ";
            name = new SpannableStringBuilder(uname + msgUtils.getCt());
            //添加等级图文混合
            name.setSpan(new ForegroundColorSpan(Color.parseColor("#f2b437")), 4, uname.length(),
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            name.setSpan(goodnumImage, 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            name.setSpan(levelImage, 2, 3, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        } else if (StringUtils.toInt(userInfo.vip_type) == 0 && StringUtils.toInt(userInfo.goodnum) == 0) {
            uname = "_ " + userInfo.user_nicename + " : ";
            name = new SpannableStringBuilder(uname + msgUtils.getCt());
            //添加等级图文混合
            name.setSpan(new ForegroundColorSpan(Color.parseColor("#f2b437")), 1, uname.length(),
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            name.setSpan(levelImage, 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        //获取被@用户id
        String touid = String.valueOf(msgUtils.get2Uid());
        //判断如果是@方式聊天,被@方用户显示粉色字体
        if ((!touid.equals("0") && (touid.equals(AppContext.getInstance().getLoginUid())))) {
            name.setSpan(new ForegroundColorSpan(Color.rgb(232, 109, 130)), uname.length(), name.length(),
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        //判断是否是点亮
        if (msgUtils.getParam("heart", 0) > 0) {
            int index = msgUtils.getParam("heart", 0);
            name.append("❤");
            //添加点亮图文混合
            Drawable hearDrawable = context.getResources().getDrawable(heartImg[index]);
            hearDrawable.setBounds(0, 0, (int) TDevice.dpToPixel(20), (int) TDevice.dpToPixel(20));
            VerticalImageSpan hearImage = new VerticalImageSpan(hearDrawable);
            name.setSpan(hearImage, name.length() - 1, name.length(),
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        c.setUserNick(name);
        mChatServer.onMessageListen(msgUtils, 2, c);
    }


    //服务器连接结果监听
    private Emitter.Listener onConn = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            JSONArray jsonArray = (JSONArray) args[0];
            for (int i = 0; i < jsonArray.length(); i++) {
                try {
                    if (jsonArray.getString(i).equals("ok")) {
                        mChatServer.onConnect(true);
                    } else {
                        mChatServer.onConnect(false);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    };

    public ChatServer(ChatServerInterface chatServerInterface, Context context, String chaturl) throws URISyntaxException {
        this.mChatServer = chatServerInterface;
        this.context = context;

        mGson = new Gson();

        try {

            IO.Options option = new IO.Options();
            option.forceNew = true;
            option.reconnection = true;
            option.reconnectionDelay = 2000;
            mSocket = IO.socket(chaturl, option);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param u 用户信息json格式
     * @dw 连接socket服务端
     */
    public void connectSocketServer(UserBean u, final String stream, String liveuid) {
        publicSocketInitAction(u, stream, liveuid);
    }

    /**
     * @dw 公共的初始化方法
     */
    public void publicSocketInitAction(final UserBean u, final String stream, final String liveuid) {

        if (null == mSocket) return;
        try {
            mSocket.connect();
            JSONObject dataJson = new JSONObject();
            dataJson.put("uid", u.id);
            dataJson.put("token", u.token);
            dataJson.put("roomnum", liveuid);
            dataJson.put("stream", stream);
            mSocket.emit("conn", dataJson);

            TLog.log(dataJson.toString());
            mSocket.on("conn", onConn);
            mSocket.on("broadcastingListen", onMessage);
            mSocket.on(mSocket.EVENT_DISCONNECT, onDisconnect);
            mSocket.on(mSocket.EVENT_ERROR, onError);
            mSocket.on(mSocket.EVENT_RECONNECT, new Emitter.Listener() {
                @Override
                public void call(Object... args) {

                    TLog.log("重连");
                    try {
                        JSONObject dataJson = new JSONObject();
                        dataJson.put("uid", u.id);
                        dataJson.put("token", u.token);
                        dataJson.put("roomnum", liveuid);
                        dataJson.put("liveuid", liveuid);
                        mSocket.emit("conn", dataJson);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            });
           /* mSocket.on(mSocket.EVENT_RECONNECT_ATTEMPT, new Emitter.Listener() {
                @Override
                public void call(Object... args) {
                    TLog.log("尝试重连");
                }
            });
            mSocket.on(mSocket.EVENT_RECONNECT_ERROR, new Emitter.Listener() {
                @Override
                public void call(Object... args) {
                    TLog.log("尝试重连错误");
                }
            });
            mSocket.on(mSocket.EVENT_RECONNECT_FAILED, new Emitter.Listener() {
                @Override
                public void call(Object... args) {
                    TLog.log("尝试重连失败");
                }
            });
            mSocket.on(mSocket.EVENT_RECONNECTING, new Emitter.Listener() {
                @Override
                public void call(Object... args) {
                    TLog.log("尝试重连chengong");
                }
            });*/
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }
    //更新影票
    public void doSendUpdateCoin(String coin, String uid, String isfrist) {
        if (null != mSocket) {

            SocketMsgUtils.getNewJsonMode()
                    .set_method_("updateVotes")
                    .setAction("1")
                    .setMsgtype("26")
                    .addParamToJson1("votes", coin)
                    .addParamToJson1("uid", uid)
                    .addParamToJson1("isfirst", isfrist)
                    .build()
                    .sendMessage(mSocket);
        }
    }
    //切换为收费
    public boolean doSendSwitchToCharge(String coin) {
        if (null == mSocket) {
            return false;
        }

        SocketMsgUtils.getNewJsonMode()
                .set_method_("changeLive")
                .setAction("1")
                .setMsgtype("27")
                .addParamToJson1("type_val", coin)
                .build()
                .sendMessage(mSocket);
        return true;
    }

    //关闭房间
    public void closeLive() {
        if (null == mSocket) {
            return;
        }

        SocketMsgUtils.getNewJsonMode()
                .set_method_("StartEndLive")
                .setAction("18")
                .setMsgtype("1")
                .setCt(context.getString(R.string.livestart))
                .build()
                .sendMessage(mSocket);
    }

    //超管关闭直播
    public void doSetCloseLive() {
        if (null == mSocket) {
            return;
        }

        SocketMsgUtils.getNewJsonMode()
                .set_method_("stopLive")
                .setAction("19")
                .setMsgtype("1")
                .setCt(context.getString(R.string.livestart))
                .build()
                .sendMessage(mSocket);

    }

    /**
     * @param mUser    用户信息
     * @param evensend 是否在连送规定时间内
     * @dw token 发送礼物凭证
     */
    public void doSendGift(String token, UserBean mUser, String evensend, String goodNum, String vip_type) {
        if (null != mSocket) {

            SocketMsgUtils.getNewJsonMode()
                    .set_method_("SendGift")
                    .setAction("0")
                    .setMsgtype("1")
                    .setMyUserInfo(mUser)
                    .addParamToJson1("uhead", mUser.avatar_thumb)
                    .setCt(token)
                    .addParamToJson1("evensend", evensend)
                    .addParamToJson1("liangname", goodNum)
                    .addParamToJson1("vip_type", vip_type)
                    .build()
                    .sendMessage(mSocket);
        }

    }

    /**
     * @param mUser 用户信息
     * @dw token 发送弹幕凭证
     */
    public void doSendBarrage(String token, UserBean mUser) {
        if (null != mSocket) {
            SocketMsgUtils.getNewJsonMode()
                    .set_method_("SendBarrage")
                    .setAction("7")
                    .setMsgtype("1")
                    .setMyUserInfo(mUser)
                    .addParamToJson1("uhead", mUser.avatar_thumb)
                    .setCt(token)
                    .build()
                    .sendMessage(mSocket);

        }

    }

    /**
     * @param mUser   当前用户bean
     * @param mToUser 被操作用户bean
     * @dw 禁言
     */
    public void doSetShutUp(UserBean mUser, SimpleUserInfo mToUser) {
        if (null == mSocket) {
            return;
        }

        SocketMsgUtils.getNewJsonMode()
                .set_method_("ShutUpUser")
                .setAction("1")
                .setMsgtype("4")
                .setMyUserInfo(mUser)
                .set2UserInfo(mToUser)
                .setCt(mToUser.user_nicename + "被禁言"+AppConfig.SHUTUP_TIME)
                .build()
                .sendMessage(mSocket);


    }

    //踢人
    public void doSetKick(UserBean mUser, SimpleUserInfo mToUser) {

        if (null == mSocket) {
            return;
        }

        SocketMsgUtils.getNewJsonMode()
                .set_method_("KickUser")
                .setAction("2")
                .setMsgtype("4")
                .setMyUserInfo(mUser)
                .set2UserInfo(mToUser)
                .setCt(mToUser.user_nicename + "被踢出房间")
                .build()
                .sendMessage(mSocket);
    }

    //设为管理员
    public void doSetOrRemoveManage(UserBean user, SimpleUserInfo touser, String content) {
        if (null == mSocket) {
            return;
        }
        SocketMsgUtils.getNewJsonMode()
                .set_method_("SystemNot")
                .setAction("13")
                .setMsgtype("4")
                .setMyUserInfo(user)
                .set2UserInfo(touser)
                .setCt(content)
                .build()
                .sendMessage(mSocket);


    }

    //发送系统消息
    public void doSendSystemMessage(String msg, UserBean user) {
        if (null == mSocket) {
            return;
        }
        SocketMsgUtils.getNewJsonMode()
                .set_method_("SystemNot")
                .setAction("13")
                .setMsgtype("4")
                .setMyUserInfo(user)
                .setCt(msg)
                .build()
                .sendMessage(mSocket);
    }

    /**
     * @param sendMsg 发言内容
     * @param user    用户信息
     * @dw 发言
     */
    public void doSendMsg(String sendMsg, UserBean user, int reply, String goodNum, String vip_type) {
        if (null == mSocket) {
            return;
        }
        SocketMsgUtils.getNewJsonMode()
                .set_method_("SendMsg")
                .setAction("0")
                .setMsgtype("2")
                .setMyUserInfo(user)
                .setCt(sendMsg)
                .addParamToJson1("liangname", goodNum)
                .addParamToJson1("vip_type", vip_type)
                .build()
                .sendMessage(mSocket);

    }



    /**
     * @param index
     * @param user  用户信息
     * @dw 我点亮了
     */
    public void doSendLitMsg(UserBean user, int index, String goodNum, String vip_type) {
        if (null == mSocket) {
            return;
        }
        SocketMsgUtils.getNewJsonMode()
                .set_method_("SendMsg")
                .setAction("0")
                .setMsgtype("2")
                .setMyUserInfo(user)
                .setCt("我点亮了")
                .addParamToJson1("liangname", goodNum)
                .addParamToJson1("vip_type", vip_type)
                .addParamToJson1("heart", String.valueOf(index + 1))
                .build()
                .sendMessage(mSocket);

    }

    //获取僵尸粉丝
    public void getZombieFans() {
        if (null == mSocket) {
            return;
        }

        SocketMsgUtils.getNewJsonMode()
                .set_method_("requestFans")
                .setAction("")
                .setMsgtype("")
                .build()
                .sendMessage(mSocket);

    }

    /**
     * @param index 点亮心在数组中的下标
     * @dw 点亮
     */
    public void doSendLit(int index) {
        if (null == mSocket) {
            return;
        }
        SocketMsgUtils.getNewJsonMode()
                .set_method_("light")
                .setAction("2")
                .setMsgtype("0")
                .build()
                .sendMessage(mSocket);
    }

    //释放资源
    public void close() {
        if (mSocket != null) {
            mSocket.disconnect();
            mSocket.off("conn");
            mSocket.off("broadcastingListen");
            mSocket.off();
            mSocket.close();
            mSocket = null;
        }

    }

    //连麦
    public void doSendTinkMicRequst(UserBean user) {
        if (null == mSocket) {
            return;
        }
        SocketMsgUtils.getNewJsonMode()
                .set_method_("ConnectVideo")
                .setAction("1")
                .setMsgtype("10")
                .setMyUserInfo(user)
                .addParamToJson1("uhead",user.avatar)
                .build()
                .sendMessage(mSocket);
    }

    public void doSendTinkMicAgree(UserBean user, String touid, Object playitems) {
        if (null == mSocket) {
            return;
        }
        SocketMsgUtils.getNewJsonMode()
                .set_method_("ConnectVideo")
                .setAction("2")
                .setMsgtype("10")
                .setMyUserInfo(user)
                .addParamToJson1("touid", touid)
                .setObject(playitems)
                .build()
                .sendMessage(mSocket);
        TLog.error("接受playitems"+playitems);
    }

    public void doSendTinkMicRefuse(UserBean user, String touid) {
        if (null == mSocket) {
            return;
        }
        SocketMsgUtils.getNewJsonMode()
                .set_method_("ConnectVideo")
                .setAction("3")
                .setMsgtype("10")
                .setMyUserInfo(user)
                .addParamToJson1("touid", touid)
                .build()
                .sendMessage(mSocket);
    }

    public void doSendTinkMicPlayUrl(UserBean user, String playurl) {
        if (null == mSocket) {
            return;
        }
        SocketMsgUtils.getNewJsonMode()
                .set_method_("ConnectVideo")
                .setAction("4")
                .setMsgtype("10")
                .setMyUserInfo(user)
                .addParamToJson1("playurl", playurl)
                .build()
                .sendMessage(mSocket);
    }

    public void doSendKickUser(String touid, String uname) {
        if (null == mSocket) {
            return;
        }
        SocketMsgUtils.getNewJsonMode()
                .set_method_("ConnectVideo")
                .setAction("6")
                .setMsgtype("10")
                .addParamToJson1("touid", touid)
                .addParamToJson1("uname", uname)
                .build()
                .sendMessage(mSocket);
    }

    public void doSendExit(String uid, String uname) {
        if (null == mSocket) {
            return;
        }
        SocketMsgUtils.getNewJsonMode()
                .set_method_("ConnectVideo")
                .setAction("5")
                .setMsgtype("10")
                .addParamToJson1("uid", uid)
                .addParamToJson1("uname", uname)
                .build()
                .sendMessage(mSocket);
    }

    //7正忙碌 8未响应 9用户退出直播间
    public void doSendRes(String action,String uid) {
        if (null == mSocket) {
            return;
        }
        SocketMsgUtils.getNewJsonMode()
                .set_method_("ConnectVideo")
                .setAction(action)
                .setMsgtype("10")
                .addParamToJson1("touid", uid)
                .build()
                .sendMessage(mSocket);
    }
    //模糊自己
    public void dosendMoHu(String action) {
        if (null == mSocket) {
            return;
        }
        SocketMsgUtils.getNewJsonMode()
                .set_method_("ConnectVideo")
                .setAction(action)
                .setMsgtype("10")
                .build()
                .sendMessage(mSocket);
    }

}


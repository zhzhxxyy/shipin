package com.duomizhibo.phonelive.interf;

import com.duomizhibo.phonelive.bean.ChatBean;
import com.duomizhibo.phonelive.bean.SendGiftBean;
import com.duomizhibo.phonelive.bean.SimpleUserInfo;
import com.duomizhibo.phonelive.bean.UserBean;
import com.duomizhibo.phonelive.utils.SocketMsgUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by Administrator on 2016/3/17.
 */
public interface ChatServerInterface {
    void onMessageListen(SocketMsgUtils socketMsg,int type,ChatBean chatBean);
    void onConnect(boolean res);
    void onUserStateChange(SocketMsgUtils socketMsg,UserBean user, boolean upordown);
    void onSystemNot(int code);
    void onShowSendGift(SendGiftBean contentJson, ChatBean chatBean);
    void onPrivilegeAction(SocketMsgUtils socketMsg,ChatBean c);
    void onLit(SocketMsgUtils socketMsg);
    void onAddZombieFans(SocketMsgUtils socketMsg);
    void onError();
    void onLinkMic(SocketMsgUtils socketMsg) throws JSONException;
    void onCharge(SocketMsgUtils socketMsg);
    void onUpdateCoin(SocketMsgUtils socketMsg);
    void onGameNotice(SocketMsgUtils socketMsg);
}

package com.duomizhibo.phonelive.utils;

import com.google.gson.internal.LinkedTreeMap;
import com.duomizhibo.phonelive.bean.SimpleUserInfo;
import com.duomizhibo.phonelive.bean.UserBean;
import com.duomizhibo.phonelive.ui.other.ChatServer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;
import java.util.Set;

import io.socket.client.Socket;

/**
 * Created by weipeng on 2016/12/26.
 */

public class SocketMsgUtils {

    private String _method_;
    private String action;
    private String ct;
    private String msgtype;
    private String retcode = "000000";
    private String retmsg  = "ok";

    private int ctType = 0;

    private String json;


    private LinkedTreeMap<String,String> list = new LinkedTreeMap<>();
    private LinkedTreeMap<String,Object> list2 = new LinkedTreeMap<>();
    private JSONObject mContentJson;
    private JSONObject mMsgJson;

    public static SocketMsgUtils getNewJsonMode(){

        return new SocketMsgUtils();
    }

    public static SocketMsgUtils getFormatJsonMode(String json){

        return new SocketMsgUtils(json);
    }

    public SocketMsgUtils(){

    }

    public SocketMsgUtils(String json) {
        this.json = json;

        try {
            JSONObject resJson = new JSONObject(json);
            JSONArray msgArrayJson = resJson.getJSONArray("msg");

            mContentJson = msgArrayJson.getJSONObject(0);
            msgtype  = mContentJson.getString("msgtype");
            _method_ = mContentJson.getString("_method_");
            action   = mContentJson.getString("action");
            retcode  = resJson.getString("retcode");
            retmsg   = resJson.getString("retmsg");
            ct       = mContentJson.getString("ct");


        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
    public int getParam(String key,int def){

        try {
            return mContentJson.getInt(key);
        } catch (JSONException e) {
            e.printStackTrace();

        }

        return def;
    }

    public String getParam(String key,String def){

        try {
            return mContentJson.getString(key);
        } catch (JSONException e) {
            e.printStackTrace();

        }

        return def;
    }

    public String getUname(){
        if(mContentJson != null){
            try {
                return mContentJson.getString("uname");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return "";
    }

    public String getLevel() {
        if (mContentJson != null) {
            try {
                return mContentJson.getString("level");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return "0";
    }

    public String getUid() {
        if (mContentJson != null) {
            try {
                return mContentJson.getString("uid");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return "0";
    }

    public String getUHead(){
        if(mContentJson != null){
            try {
                return mContentJson.getString("uhead");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return "";
    }

    public String get2Uid() {
        if (mContentJson != null) {
            try {
                return mContentJson.getString("touid");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            System.out.println();
        }

        return "0";
    }

    public String get2Uname(){
        if(mContentJson != null){
            try {
                return mContentJson.getString("touname");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return "";
    }

    public int get2Level() {
        if (mContentJson != null) {
            try {
                return mContentJson.getInt("touid");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return 0;
    }

    public String get2UHead(){
        if(mContentJson != null){
            try {
                return mContentJson.getString("touhead");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return "";
    }

    public SocketMsgUtils addParamToJson1(String key, String val){

        list.put(key,val);

        return this;
    }

    public SocketMsgUtils addParamToJson2(String key,String val){

        list.put(key,val);

        return this;
    }

    public SocketMsgUtils setMyUserInfo(UserBean userInfo){
        list.put("level", userInfo.level);
        list.put("uname",userInfo.user_nicename);
        list.put("uid", userInfo.id);

        return this;
    }

    public SocketMsgUtils set2UserInfo(SimpleUserInfo userInfo){
        list.put("touid",userInfo.id);
        list.put("toname", userInfo.user_nicename);

        return this;
    }
    public SocketMsgUtils setObject(Object  playitems){
        list2.put("playitems",playitems);
        return this;
    }

    public SocketMsgUtils build(){

        mMsgJson = new JSONObject();
        JSONObject msgJson2 = new JSONObject();
        JSONArray msgArrJson = new JSONArray();
        try {


            msgJson2.put("_method_", _method_);
            msgJson2.put("action",action);
            msgJson2.put("msgtype",msgtype);

            Set<String> keySet = list.keySet();
            Set<String> keySet2 = list2.keySet();
            Iterator<String> iterator = keySet.iterator();
            Iterator<String> iterator2 = keySet2.iterator();
            while (iterator.hasNext()){
                String key = iterator.next();
                msgJson2.put(key,list.get(key));
            }
            while (iterator2.hasNext()){
                String key = iterator2.next();
                msgJson2.put(key,list2.get(key));
            }


            msgJson2.put("ct",ct == null ? "" : ct);

            msgArrJson.put(0, msgJson2);

            mMsgJson.put("retcode",retcode);
            mMsgJson.put("retmsg",retmsg);
            mMsgJson.put("msg", msgArrJson);
            TLog.log(mMsgJson.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return this;
    }

    public void sendMessage(Socket socket){
        if(socket != null && mMsgJson != null){
            //TLog.log(mMsgJson.toString());
            socket.emit(ChatServer.EVENT_NAME,mMsgJson);

        }

    }


    public JSONObject getMsgJson() {
        return mMsgJson;
    }

    public SocketMsgUtils set_method_(String _method_) {
        this._method_ = _method_;

        return this;
    }

    public String getAction() {
        return action;
    }

    public SocketMsgUtils setAction(String action) {
        this.action = action;
        return this;
    }

    public String getCt() {
        return ct;
    }

    public SocketMsgUtils setCt(String ct) {
        this.ct = ct;

        return this;
    }

    public String getMsgtype() {
        return msgtype;
    }

    public SocketMsgUtils setMsgtype(String msgtype) {
        this.msgtype = msgtype;

        return this;
    }

    public String getRetcode() {
        return retcode;
    }

    public SocketMsgUtils setRetcode(String retcode) {
        this.retcode = retcode;

        return this;
    }

    public String getRetmsg() {
        return retmsg;
    }

    public SocketMsgUtils setRetmsg(String retmsg) {
        this.retmsg = retmsg;

        return this;
    }

    public int getCtType() {
        return ctType;
    }

    public SocketMsgUtils setCtType(int ctType) {
        this.ctType = ctType;

        return this;
    }
}

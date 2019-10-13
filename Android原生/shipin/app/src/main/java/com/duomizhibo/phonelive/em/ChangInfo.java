package com.duomizhibo.phonelive.em;

/**
 * Created by Administrator on 2016/3/11.
 */
public enum  ChangInfo {
    CHANG_NICK("user_nicename"),CHANG_SIGN("signature");
    String action;
    ChangInfo(String action) {
        this.action = action;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }
}

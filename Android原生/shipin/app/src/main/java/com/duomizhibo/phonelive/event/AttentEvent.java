package com.duomizhibo.phonelive.event;

/**
 * Created by cxf on 2017/7/9.
 */

public class AttentEvent {
    private boolean isAttention;
    private String uid;

    public AttentEvent(String uid, boolean isAttention) {
        this.uid = uid;
        this.isAttention = isAttention;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public boolean isAttention() {
        return isAttention;
    }

    public void setAttention(boolean attention) {
        isAttention = attention;
    }
}

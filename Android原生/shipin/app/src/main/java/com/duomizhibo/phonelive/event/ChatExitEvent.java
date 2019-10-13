package com.duomizhibo.phonelive.event;

/**
 * Created by cxf on 2017/7/1.
 */

public class ChatExitEvent {
    private String lastMsg;
    private String touid;
    private int type;

    public ChatExitEvent(String lastMsg, String touid, int type) {
        this.lastMsg = lastMsg;
        this.touid = touid;
        this.type=type;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getLastMsg() {
        return lastMsg;
    }

    public void setLastMsg(String lastMsg) {
        this.lastMsg = lastMsg;
    }

    public String getTouid() {
        return touid;
    }

    public void setTouid(String touid) {
        this.touid = touid;
    }
}

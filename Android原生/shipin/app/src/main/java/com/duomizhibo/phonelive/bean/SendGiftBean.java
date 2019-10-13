package com.duomizhibo.phonelive.bean;

/**
 * Created by Administrator on 2016/3/30.
 */
public class SendGiftBean {
    private int uid;
    private int touid;
    private int giftid;
    private int giftcount;
    private int totalcoin;
    private String giftname;
    private String gifticon;
    private String evensend;
    private long sendTime;
    private String avatar;
    private String nicename;

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getNicename() {
        return nicename;
    }

    public void setNicename(String nicename) {
        this.nicename = nicename;
    }

    public long getSendTime() {
        return sendTime;
    }

    public void setSendTime(long sendTime) {
        this.sendTime = sendTime;
    }

    public String getEvensend() {
        return evensend;
    }

    public void setEvensend(String eventsend) {
        this.evensend = eventsend;
    }


    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public int getGiftid() {
        return giftid;
    }

    public void setGiftid(int giftid) {
        this.giftid = giftid;
    }

    public int getTouid() {
        return touid;
    }

    public void setTouid(int touid) {
        this.touid = touid;
    }

    public int getGiftcount() {
        return giftcount;
    }

    public void setGiftcount(int giftcount) {
        this.giftcount = giftcount;
    }

    public int getTotalcoin() {
        return totalcoin;
    }

    public void setTotalcoin(int totalcoin) {
        this.totalcoin = totalcoin;
    }


    public String getGiftname() {
        return giftname;
    }

    public void setGiftname(String giftname) {
        this.giftname = giftname;
    }

    public String getGifticon() {
        return gifticon;
    }

    public void setGifticon(String gifticon) {
        this.gifticon = gifticon;
    }
}

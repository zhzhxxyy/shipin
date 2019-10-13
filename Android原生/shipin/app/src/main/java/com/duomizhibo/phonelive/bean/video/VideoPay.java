package com.duomizhibo.phonelive.bean.video;



public class VideoPay {
    private int id;
    private int playState;//1:正常观看  2:需要扣金币观看，且金币够扣  3:需扣金币观看，且金币不够扣  4:视频收费，但未登陆
    private String playStateMsg;
    private String url;
    private int freeNum;
    private int freeType;//2按秒数  1按部
    private boolean surePlay;//是否确定播放
    private boolean isTrySee;//是否试看

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPlayState() {
        return playState;
    }

    public void setPlayState(int playState) {
        this.playState = playState;
    }

    public String getPlayStateMsg() {
        return playStateMsg;
    }

    public void setPlayStateMsg(String playStateMsg) {
        this.playStateMsg = playStateMsg;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getFreeNum() {
        return freeNum;
    }

    public void setFreeNum(int freeNum) {
        this.freeNum = freeNum;
    }

    public int getFreeType() {
        return freeType;
    }

    public void setFreeType(int freeType) {
        this.freeType = freeType;
    }

    public boolean isSurePlay() {
        return surePlay;
    }

    public void setSurePlay(boolean surePlay) {
        this.surePlay = surePlay;
    }

    public boolean isTrySee() {
        return isTrySee;
    }

    public void setIsTrySee(boolean isTrySee) {
        this.isTrySee = isTrySee;
    }

    @Override
    public String toString() {
        return "VideoPay{" +
                "id=" + id +
                ", playState=" + playState +
                ", playStateMsg='" + playStateMsg + '\'' +
                ", url='" + url + '\'' +
                ", freeNum=" + freeNum +
                ", freeType=" + freeType +
                ", surePlay=" + surePlay +
                ", isTrySee=" + isTrySee +
                '}';
    }
}

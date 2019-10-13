package com.duomizhibo.phonelive.bean.video;

/**
 * Created  on 2019/8/1.
 */

public class ShareInfo {
    private int id;
    private double money;
    private String nickname;
    private int propaganda_reward;
    private int share_num;
    private String share_url;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getMoney() {
        return money;
    }

    public void setMoney(double money) {
        this.money = money;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public int getPropaganda_reward() {
        return propaganda_reward;
    }

    public void setPropaganda_reward(int propaganda_reward) {
        this.propaganda_reward = propaganda_reward;
    }

    public int getShare_num() {
        return share_num;
    }

    public void setShare_num(int share_num) {
        this.share_num = share_num;
    }

    public String getShare_url() {
        return share_url;
    }

    public void setShare_url(String share_url) {
        this.share_url = share_url;
    }
}

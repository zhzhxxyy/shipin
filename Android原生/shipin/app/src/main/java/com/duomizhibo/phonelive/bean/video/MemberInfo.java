package com.duomizhibo.phonelive.bean.video;


import com.duomizhibo.phonelive.utils.StringUtils;

import java.math.BigInteger;

/**
 *
 */
public class MemberInfo {

    private String id;
    private String gid;//1 普通会员 2 vip会员
    private String username;
    private String tel;
    private String nickname;
    private String headimgurl;
    private String email;
    private double money;
    private String out_time;
    private int is_agent;//0不是代理 1是代理
    private int is_permanent;
    private int sex;//1 男 2女
    private boolean isVip;
    private boolean isEverVip;
    private String tel_asterisk;
    private String token;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getGid() {
        return gid;
    }

    public void setGid(String gid) {
        this.gid = gid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getHeadimgurl() {
        return headimgurl;
    }

    public void setHeadimgurl(String headimgurl) {
        this.headimgurl = headimgurl;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public double getMoney() {
        return money;
    }

    public void setMoney(double money) {
        this.money = money;
    }

    public String getOut_time() {
        return out_time;
    }

    public void setOut_time(String out_time) {
        this.out_time = out_time;
    }

    public int getIs_agent() {
        return is_agent;
    }

    public void setIs_agent(int is_agent) {
        this.is_agent = is_agent;
    }

    public int getIs_permanent() {
        return is_permanent;
    }

    public void setIs_permanent(int is_permanent) {
        this.is_permanent = is_permanent;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public boolean isVip() {
        return isVip;
    }

    public void setVip(boolean vip) {
        isVip = vip;
    }

    public boolean isEverVip() {
        return isEverVip;
    }

    public void setEverVip(boolean everVip) {
        isEverVip = everVip;
    }

    public String getTel_asterisk() {
        return tel_asterisk;
    }

    public void setTel_asterisk(String tel_asterisk) {
        this.tel_asterisk = tel_asterisk;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getOutTimeString(){
        if(StringUtils.isEmpty(out_time)){
            return  "";
        }
        return StringUtils.millisToStringDate(StringUtils.toInt(out_time,0)*1000L,"yyyy-MM-dd HH:mm:ss");
    }

    @Override
    public String toString() {
        return "MemberInfo{" +
                "id='" + id + '\'' +
                ", gid='" + gid + '\'' +
                ", username='" + username + '\'' +
                ", tel='" + tel + '\'' +
                ", nickname='" + nickname + '\'' +
                ", headimgurl='" + headimgurl + '\'' +
                ", email='" + email + '\'' +
                ", money=" + money +
                ", out_time='" + out_time + '\'' +
                ", is_agent=" + is_agent +
                ", is_permanent=" + is_permanent +
                ", sex=" + sex +
                ", isVip=" + isVip +
                ", isEverVip=" + isEverVip +
                ", tel_asterisk='" + tel_asterisk + '\'' +
                ", token='" + token + '\'' +
                '}';
    }
}

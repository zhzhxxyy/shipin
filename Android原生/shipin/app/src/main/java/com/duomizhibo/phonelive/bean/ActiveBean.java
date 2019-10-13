package com.duomizhibo.phonelive.bean;

import java.io.Serializable;

/**
 * Created by cxf on 2017/7/13.
 */

public class ActiveBean implements Serializable {


    /**
     * id : 5
     * uid : 8573
     * title :
     * thumb : http://livecdn.yunbaozhibo.com/TXUGC_20170804_161138.jpg
     * href : http://livecdn.yunbaozhibo.com/TXUGC_20170804_161138.mp4
     * likes : 0
     * views : 0
     * comments : 0
     * addtime : 1501834328
     * userinfo : {"id":"8573","user_nicename":"手机用户9060","avatar":"http://livedemo.yunbaozhibo.com/default.jpg","coin":"697","avatar_thumb":"http://livedemo.yunbaozhibo.com/default_thumb.jpg","sex":"2","signature":"这家伙很懒，什么都没留下","consumption":"1013","votestotal":"0","province":"","city":"泰安市","birthday":"","issuper":"0","level":"6","level_anchor":"1","vip":{"type":"1"},"liang":{"name":"0"}}
     * datetime : 30秒前
     * islike : 0
     */

    private String id;
    private String uid;
    private String title;
    private String thumb;
    private String href;
    private String likes;
    private String views;
    private String comments;
    private String addtime;
    private UserInfo userinfo;
    private String datetime;
    private String islike;
    private String isattent;
    private String distance;
    private String steps;
    private String shares;
    private int isstep;

    public int getIsstep() {
        return isstep;
    }

    public void setIsstep(int isstep) {
        this.isstep = isstep;
    }

    public String getSteps() {
        return steps;
    }

    public void setSteps(String steps) {
        this.steps = steps;
    }

    public String getShares() {
        return shares;
    }

    public void setShares(String shares) {
        this.shares = shares;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getIsattent() {
        return isattent;
    }

    public void setIsattent(String isattent) {
        this.isattent = isattent;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    public String getLikes() {
        return likes;
    }

    public void setLikes(String likes) {
        this.likes = likes;
    }

    public String getViews() {
        return views;
    }

    public void setViews(String views) {
        this.views = views;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getAddtime() {
        return addtime;
    }

    public void setAddtime(String addtime) {
        this.addtime = addtime;
    }

    public UserInfo getUserinfo() {
        return userinfo;
    }

    public void setUserinfo(UserInfo userinfo) {
        this.userinfo = userinfo;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public String getIslike() {
        return islike;
    }

    public void setIslike(String islike) {
        this.islike = islike;
    }


}

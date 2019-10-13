package com.duomizhibo.phonelive.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by cxf on 2017/7/14.
 */

public class CommentBean implements Serializable{

    /**
     * id : 1
     * uid : 11163
     * touid : 8209
     * videoid : 9
     * commentid : 0
     * parentid : 0
     * content : dddd
     * addtime : 1501924416
     * userinfo : {"id":"11163","user_nicename":"张小凡","avatar":"http://q.qlogo.cn/qqapp/100371282/D9337345784F5FD6BCD8073480BA1F85/40","coin":"60","avatar_thumb":"http://q.qlogo.cn/qqapp/100371282/D9337345784F5FD6BCD8073480BA1F85/40","sex":"2","signature":"这家伙很懒，什么都没留下","consumption":"0","votestotal":"0","province":"","city":"泰安市","birthday":"","issuper":"0","level":"1","level_anchor":"1","vip":{"type":"0"},"liang":{"name":"0"}}
     * datetime : 2分钟前
     */



    private String id;
    private String uid;
    private String touid;
    private String videoid;
    private String commentid;
    private String parentid;
    private String content;
    private String addtime;
    private UserInfo userinfo;
    private String likes;
    private int replys;
    private String datetime;
    private UserInfo touserinfo;
    private ToCommentInfo tocommentinfo;
    private int islike;


    public int getIslike() {
        return islike;
    }

    public void setIslike(int islike) {
        this.islike = islike;
    }

    public ToCommentInfo getTocommentinfo() {
        return tocommentinfo;
    }

    public void setTocommentinfo(ToCommentInfo tocommentinfo) {
        this.tocommentinfo = tocommentinfo;
    }

    public int getReplys() {
        return replys;
    }

    public void setReplys(int replys) {
        this.replys = replys;
    }

    public String getLikes() {
        return likes;
    }

    public void setLikes(String likes) {
        this.likes = likes;
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

    public String getTouid() {
        return touid;
    }

    public void setTouid(String touid) {
        this.touid = touid;
    }

    public String getVideoid() {
        return videoid;
    }

    public void setVideoid(String videoid) {
        this.videoid = videoid;
    }

    public String getCommentid() {
        return commentid;
    }

    public void setCommentid(String commentid) {
        this.commentid = commentid;
    }

    public String getParentid() {
        return parentid;
    }

    public void setParentid(String parentid) {
        this.parentid = parentid;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
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

    public UserInfo getTouserinfo() {
        return touserinfo;
    }

    public void setTouserinfo(UserInfo touserinfo) {
        this.touserinfo = touserinfo;
    }


    public static  class ToCommentInfo{
        private String content;

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }
    }


}

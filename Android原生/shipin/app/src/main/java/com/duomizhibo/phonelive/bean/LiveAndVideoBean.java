package com.duomizhibo.phonelive.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by weilian on 2017/9/5.
 */

public class LiveAndVideoBean implements Parcelable {
//    "uid":"8214",
//            "avatar":"http://livecdn.yunbaozhibo.com/20170525_5926c19e7e5bb.png",
//            "avatar_thumb":"http://livecdn.yunbaozhibo.com/20170525_5926c19e7e5bb.png",
//            "user_nicename":"我的直播间",
//            "title":"",
//            "province":"",
//            "city":"好像在火星",
//            "stream":"8214_1500722924",
//            "pull":"http://on8vsby8m.bkt.clouddn.com/1.flv",
//            "isvideo":"1",
//            "thumb":"http://livecdn.yunbaozhibo.com/20170525_5926c19e7e5bb.png",
//            "islive":"1",
//            "type":"0",
//            "type_val":"",
//            "goodnum":"0",
//            "nums":"0",
//            "level_anchor":"6",
//            "distance":"157km"

    public String uid;
    public String avatar;
    public String avatar_thumb;
    public String user_nicename;
    public String title;
    public String city;
    public String stream;
    public String islive;
    public String isvideo;
    public String nums;
    public String distance;
    public String pull;
    public String thumb;
    public String type;
    public String type_val;
    public String level_anchor;
    public String goodnum;
    public String id;
    public String thumb_s;
    public String href;
    public String likes;
    public String views;
    public String comments;
    public String addtime;
    public String datetime;

    public UserInfo getUserinfo() {
        return userinfo;
    }

    public void setUserinfo(UserInfo userinfo) {
        this.userinfo = userinfo;
    }

    public UserInfo userinfo;


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.uid);
        dest.writeString(this.avatar);
        dest.writeString(this.avatar_thumb);
        dest.writeString(this.user_nicename);
        dest.writeString(this.title);
        dest.writeString(this.city);
        dest.writeString(this.stream);
        dest.writeString(this.nums);
        dest.writeString(this.distance);
        dest.writeString(this.pull);
        dest.writeString(this.thumb);
        dest.writeString(this.type_val);
        dest.writeString(this.type);
        dest.writeString(this.level_anchor);
        dest.writeString(this.goodnum);
        dest.writeString(this.islive);
        dest.writeString(this.isvideo);
        dest.writeString(this.id);
        dest.writeString(this.thumb_s);
        dest.writeString(this.href);
        dest.writeString(this.likes);
        dest.writeString(this.views);
        dest.writeString(this.comments);
        dest.writeString(this.addtime);
        dest.writeString(this.datetime);
    }

    public LiveAndVideoBean() {
    }

    protected LiveAndVideoBean(Parcel in) {
        this.islive = in.readString();
        this.isvideo = in.readString();
        this.uid = in.readString();
        this.avatar = in.readString();
        this.avatar_thumb = in.readString();
        this.user_nicename = in.readString();
        this.title = in.readString();
        this.city = in.readString();
        this.stream = in.readString();
        this.nums = in.readString();
        this.distance = in.readString();
        this.pull = in.readString();
        this.thumb = in.readString();
        this.type_val = in.readString();
        this.type = in.readString();
        this.level_anchor = in.readString();
        this.goodnum = in.readString();
        this.id = in.readString();
        this.thumb_s = in.readString();
        this.href = in.readString();
        this.likes = in.readString();
        this.views = in.readString();
        this.comments = in.readString();
        this.addtime = in.readString();
        this.datetime = in.readString();
    }

    public static final Creator<LiveAndVideoBean> CREATOR = new Creator<LiveAndVideoBean>() {
        @Override
        public LiveAndVideoBean createFromParcel(Parcel source) {
            return new LiveAndVideoBean(source);
        }

        @Override
        public LiveAndVideoBean[] newArray(int size) {
            return new LiveAndVideoBean[size];
        }
    };
}

package com.duomizhibo.phonelive.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by weipeng on 2017/1/17.
 */

public class LiveJson implements Parcelable {


    /**
     * uid : 7671
     * avatar : http://live.yunbaozhibo.com/api/public/upload/avatar/default.jpg
     * avatar_thumb : http://live.yunbaozhibo.com/api/public/upload/avatar/default_thumb.jpg
     * user_nicename : 测试
     * title :
     * city : 好像在火星
     * stream : 7671_1484145728
     * nums : 0
     */

    public String uid;
    public String avatar;
    public String avatar_thumb;
    public String user_nicename;
    public String title;
    public String city;
    public String stream;
    public String nums;
    public String distance;
    public String pull;
    public String thumb;
    public String type;
    public String type_val;
    public String level_anchor;
    public String goodnum;
    /**
     * isvideo : 1
     * is_communicating : 0
     * age : 年龄未知
     */

    public String isvideo;
    public String is_communicating;
    public String age;
    public String sex;


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
        dest.writeString(this.isvideo);
        dest.writeString(this.is_communicating);
        dest.writeString(this.age);
        dest.writeString(this.sex);
    }

    public LiveJson() {
    }

    protected LiveJson(Parcel in) {
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
        this.isvideo = in.readString();
        this.is_communicating = in.readString();
        this.age = in.readString();
        this.sex = in.readString();
    }

    public static final Creator<LiveJson> CREATOR = new Creator<LiveJson>() {
        @Override
        public LiveJson createFromParcel(Parcel source) {
            return new LiveJson(source);
        }

        @Override
        public LiveJson[] newArray(int size) {
            return new LiveJson[size];
        }
    };


}

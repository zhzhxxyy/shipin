package com.duomizhibo.phonelive.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by weipeng on 2017/1/18.
 */

public class SimpleUserInfo implements Parcelable {

    public String id;
    public String user_nicename;
    public String avatar;
    public String avatar_thumb;
    public String sex;
    public String signature;
    public String level;
    public String level_anchor;
    public String isattention;
    public String city;
    public String vip_type;
    public String car_id;
    public String car_words;
    public String car_swf;
    public String car_swftime;
    public String goodnum;


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.user_nicename);
        dest.writeString(this.avatar);
        dest.writeString(this.avatar_thumb);
        dest.writeString(this.sex);
        dest.writeString(this.signature);
        dest.writeString(this.level);
        dest.writeString(this.level_anchor);
        dest.writeString(this.isattention);
        dest.writeString(this.city);
        dest.writeString(this.vip_type);
        dest.writeString(this.car_id);
        dest.writeString(this.car_words);
        dest.writeString(this.car_swf);
        dest.writeString(this.car_swftime);
        dest.writeString(this.goodnum);

    }

    public SimpleUserInfo() {
    }

    protected SimpleUserInfo(Parcel in) {
        this.id = in.readString();
        this.user_nicename = in.readString();
        this.avatar = in.readString();
        this.avatar_thumb = in.readString();
        this.sex = in.readString();
        this.signature = in.readString();
        this.level = in.readString();
        this.level_anchor = in.readString();
        this.isattention = in.readString();
        this.city = in.readString();
        this.vip_type = in.readString();
        this.car_id = in.readString();
        this.car_words = in.readString();
        this.car_swf = in.readString();
        this.car_swftime = in.readString();
        this.goodnum = in.readString();
    }

    public static final Creator<SimpleUserInfo> CREATOR = new Creator<SimpleUserInfo>() {
        @Override
        public SimpleUserInfo createFromParcel(Parcel source) {
            return new SimpleUserInfo(source);
        }

        @Override
        public SimpleUserInfo[] newArray(int size) {
            return new SimpleUserInfo[size];
        }
    };
}

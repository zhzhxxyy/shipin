package com.duomizhibo.phonelive.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.List;

/**
 *
 */
public class UserBean extends SimpleUserInfo implements Parcelable {

    public String birthday;
    public String coin;
    public String token;
    public String votes;
    public String consumption;
    public String uType;
     public String isreg;;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(this.birthday);
        dest.writeString(this.coin);
        dest.writeString(this.token);
        dest.writeString(this.votes);
        dest.writeString(this.consumption);
        dest.writeString(this.uType);
        dest.writeString(this.isreg);
    }

    public UserBean() {
    }

    protected UserBean(Parcel in) {
        super(in);
        this.birthday = in.readString();
        this.coin = in.readString();
        this.token = in.readString();
        this.votes = in.readString();
        this.consumption = in.readString();
        this.uType = in.readString();
        this.isreg = in.readString();

    }

    public static final Creator<UserBean> CREATOR = new Creator<UserBean>() {
        @Override
        public UserBean createFromParcel(Parcel source) {
            return new UserBean(source);
        }

        @Override
        public UserBean[] newArray(int size) {
            return new UserBean[size];
        }
    };
}

package com.duomizhibo.phonelive.bean;


import android.os.Parcel;
import android.os.Parcelable;

public class LiveRecordBean implements Parcelable {
    private int uid;
    private String showid;
    private int islive;
    private String starttime;
    private String endtime;
    private String nums;
    private String title;
    private String datetime;
    private String video_url;
    private String id;
    private String datestarttime;
    private String dateendtime;

    public String getDatestarttime() {
        return datestarttime;
    }

    public void setDatestarttime(String datestarttime) {
        this.datestarttime = datestarttime;
    }

    public String getDateendtime() {
        return dateendtime;
    }

    public void setDateendtime(String dateendtime) {
        this.dateendtime = dateendtime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getVideo_url() {
        return video_url;
    }

    public void setVideo_url(String video_url) {
        this.video_url = video_url;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getNums() {
        return nums;
    }

    public void setNums(String nums) {
        this.nums = nums;
    }

    public String getEndtime() {
        return endtime;
    }

    public void setEndtime(String endtime) {
        this.endtime = endtime;
    }

    public String getStarttime() {
        return starttime;
    }

    public void setStarttime(String starttime) {
        this.starttime = starttime;
    }

    public int getIslive() {
        return islive;
    }

    public void setIslive(int islive) {
        this.islive = islive;
    }

    public String getShowid() {
        return showid;
    }

    public void setShowid(String showid) {
        this.showid = showid;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.uid);
        dest.writeString(this.showid);
        dest.writeInt(this.islive);
        dest.writeString(this.starttime);
        dest.writeString(this.endtime);
        dest.writeString(this.nums);
        dest.writeString(this.title);
        dest.writeString(this.datetime);
        dest.writeString(this.video_url);
        dest.writeString(this.dateendtime);
        dest.writeString(this.datestarttime);
    }

    protected LiveRecordBean(Parcel in) {
        this.uid = in.readInt();
        this.showid = in.readString();
        this.islive = in.readInt();
        this.starttime = in.readString();
        this.endtime = in.readString();
        this.nums = in.readString();
        this.title = in.readString();
        this.datetime = in.readString();
        this.video_url = in.readString();
        this.datestarttime=in.readString();
        this.dateendtime=in.readString();
    }

    public static final Parcelable.Creator<LiveRecordBean> CREATOR = new Parcelable.Creator<LiveRecordBean>() {
        @Override
        public LiveRecordBean createFromParcel(Parcel source) {
            return new LiveRecordBean(source);
        }

        @Override
        public LiveRecordBean[] newArray(int size) {
            return new LiveRecordBean[size];
        }
    };
}

package com.duomizhibo.phonelive.bean.video;


import com.duomizhibo.phonelive.utils.StringUtils;

public class RecordVideo {

    private int id;
    private double gold;
    private int user_id;
    private int view_time;
    private String title;
    private int video_id;
    private int type;
    private String name;
    private String user_ip;

    private boolean isOpen;//自定义的字段 是否展开

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getGold() {
        return gold;
    }

    public void setGold(double gold) {
        this.gold = gold;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getView_time() {
        return view_time;
    }

    public void setView_time(int view_time) {
        this.view_time = view_time;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getVideo_id() {
        return video_id;
    }

    public void setVideo_id(int video_id) {
        this.video_id = video_id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUser_ip() {
        return user_ip;
    }

    public void setUser_ip(String user_ip) {
        this.user_ip = user_ip;
    }

    public boolean isOpen() {
        return isOpen;
    }

    public void setOpen(boolean open) {
        isOpen = open;
    }

    public String getViewTimeString(){
        if(view_time<=0){
            return  "";
        }
        return StringUtils.millisToStringDate(view_time*1000L,"yyyy-MM-dd HH:mm:ss");
    }

}

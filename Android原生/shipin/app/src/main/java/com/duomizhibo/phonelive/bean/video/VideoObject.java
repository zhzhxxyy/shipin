package com.duomizhibo.phonelive.bean.video;

import com.duomizhibo.phonelive.utils.StringUtils;

import java.io.Serializable;

/**
 * Created  on 2019/8/1.
 */

public class VideoObject implements Serializable {

    public static final int TYPE_CAROUSEL = 5;//轮播
    public static final int TYPE_HEADER = 6;//头部
    public static final int TYPE_DIVIDER = 7;//分割
    public static final int TYPE_RECOMMEND = 8;//视频
    public static final int TYPE_HEADLINE = 9;//公告
    private int type = TYPE_RECOMMEND;
    private int spanCount = 150;


    private int id;
    private int click;
    private int gold;
    private int good;
    private String play_time;
    private String thumbnail;
    private String title;
    private int update_time;
    private int add_time;
    private int reco;
    private int status;
    private int is_check;


    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getSpanCount() {
        return spanCount;
    }

    public void setSpanCount(int spanCount) {
        this.spanCount = spanCount;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getClick() {
        return click;
    }

    public void setClick(int click) {
        this.click = click;
    }

    public int getGold() {
        return gold;
    }

    public void setGold(int gold) {
        this.gold = gold;
    }

    public int getGood() {
        return good;
    }

    public void setGood(int good) {
        this.good = good;
    }

    public String getPlay_time() {
        return play_time;
    }

    public void setPlay_time(String play_time) {
        this.play_time = play_time;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getUpdate_time() {
        return update_time;
    }

    public void setUpdate_time(int update_time) {
        this.update_time = update_time;
    }

    public int getReco() {
        return reco;
    }

    public void setReco(int reco) {
        this.reco = reco;
    }

    public int getAdd_time() {
        return add_time;
    }

    public void setAdd_time(int add_time) {
        this.add_time = add_time;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getIs_check() {
        return is_check;
    }

    public void setIs_check(int is_check) {
        this.is_check = is_check;
    }

    public String getAddTimeString(){

        if(add_time<=0){
            return  "";
        }
        return StringUtils.millisToStringDate(add_time*1000L,"yyyy/MM/dd");

//        1565683386
//        2019/08/03
    }
}

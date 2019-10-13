package com.duomizhibo.phonelive.bean.video;

import android.database.DatabaseUtils;

import com.duomizhibo.phonelive.utils.StringUtils;

import java.util.List;

/**
 * Created  on 2019/8/1.
 */

public class VideoDetail {

    private int id;
    private int click;
    private int gold;
    private int good;
    private String play_time;
    private String thumbnail;
    private String title;
    private int update_time;
    private int reco;

    private int add_time;
    private int classid;
    private String classname;

    private int user_id;
    private String member;
    private String headimgurl;

    private boolean isCollected;
    private boolean isGooded;
    private boolean is_check;

    private List<VideoObject> recom_list;
    private List<VideoObject> videoSet;
    private List<VideoClass> taglist;

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

    public int getClassid() {
        return classid;
    }

    public void setClassid(int classid) {
        this.classid = classid;
    }

    public String getClassname() {
        return classname;
    }

    public void setClassname(String classname) {
        this.classname = classname;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getMember() {
        return member;
    }

    public void setMember(String member) {
        this.member = member;
    }

    public String getHeadimgurl() {
        return headimgurl;
    }

    public void setHeadimgurl(String headimgurl) {
        this.headimgurl = headimgurl;
    }

    public boolean isCollected() {
        return isCollected;
    }

    public void setCollected(boolean collected) {

        isCollected = collected;
    }
    public void setIsCollected(boolean isCollected) {
        this.isCollected = isCollected;
    }

    public boolean isGooded() {
        return isGooded;
    }

    public void setGooded(boolean gooded) {
        isGooded = gooded;
    }

    public void setIsGooded(boolean isGooded) {
        this.isGooded = isGooded;
    }

    public boolean is_check() {
        return is_check;
    }

    public void setIs_check(boolean is_check) {
        this.is_check = is_check;
    }

    public List<VideoObject> getRecom_list() {
        return recom_list;
    }

    public void setRecom_list(List<VideoObject> recom_list) {
        this.recom_list = recom_list;
    }

    public List<VideoObject> getVideoSet() {
        return videoSet;
    }

    public void setVideoSet(List<VideoObject> videoSet) {
        this.videoSet = videoSet;
    }

    public List<VideoClass> getTaglist() {
        return taglist;
    }

    public void setTaglist(List<VideoClass> taglist) {
        this.taglist = taglist;
    }



    public String getAddTimeString(){

        if(add_time<=0){
            return  "";
        }
        return StringUtils.millisToStringDate(add_time*1000L,"yyyy-MM-dd HH:mm:ss");
    }

    @Override
    public String toString() {
        return "VideoDetail{" +
                "id=" + id +
                ", click=" + click +
                ", gold=" + gold +
                ", good=" + good +
                ", play_time='" + play_time + '\'' +
                ", thumbnail='" + thumbnail + '\'' +
                ", title='" + title + '\'' +
                ", update_time=" + update_time +
                ", reco=" + reco +
                ", add_time=" + add_time +
                ", classid=" + classid +
                ", classname='" + classname + '\'' +
                ", user_id=" + user_id +
                ", member='" + member + '\'' +
                ", headimgurl='" + headimgurl + '\'' +
                ", isCollected=" + isCollected +
                ", isGooded=" + isGooded +
                ", is_check=" + is_check +
                ", recom_list=" + recom_list +
                ", videoSet=" + videoSet +
                ", taglist=" + taglist +
                '}';
    }
}

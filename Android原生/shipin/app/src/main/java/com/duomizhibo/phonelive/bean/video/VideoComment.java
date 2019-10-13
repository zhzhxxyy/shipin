package com.duomizhibo.phonelive.bean.video;

import com.duomizhibo.phonelive.utils.StringUtils;

import java.util.List;

/**
 * Created  on 2019/8/1.
 */

public class VideoComment {
    private int id;
    private int last_time;
    private String nickname;
    private int send_user;
    private String username;
    private String headimgurl;
    private String content;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getLast_time() {
        return last_time;
    }

    public void setLast_time(int last_time) {
        this.last_time = last_time;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public int getSend_user() {
        return send_user;
    }

    public void setSend_user(int send_user) {
        this.send_user = send_user;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getHeadimgurl() {
        return headimgurl;
    }

    public void setHeadimgurl(String headimgurl) {
        this.headimgurl = headimgurl;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }




    public String getLastTimeString(){
        if(last_time<=0){
            return  "";
        }
        return StringUtils.millisToStringDate(last_time,"yyyy-MM-dd HH:mm");
    }

    @Override
    public String toString() {
        return "VideoComment{" +
                "id=" + id +
                ", last_time=" + last_time +
                ", nickname='" + nickname + '\'' +
                ", send_user=" + send_user +
                ", username='" + username + '\'' +
                ", headimgurl='" + headimgurl + '\'' +
                ", content='" + content + '\'' +
                '}';
    }
}

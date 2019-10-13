package com.duomizhibo.phonelive.event;

/**
 * Created by cxf on 2017/9/11.
 */

public class DianzanEvent {
    private int position;
    private String likes;
    private int isLike;

    public DianzanEvent() {
    }

    public DianzanEvent(int position, String likes, int isLike) {
        this.position = position;
        this.likes = likes;
        this.isLike = isLike;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public String getLikes() {
        return likes;
    }

    public void setLikes(String likes) {
        this.likes = likes;
    }

    public int getIsLike() {
        return isLike;
    }

    public void setIsLike(int isLike) {
        this.isLike = isLike;
    }
}

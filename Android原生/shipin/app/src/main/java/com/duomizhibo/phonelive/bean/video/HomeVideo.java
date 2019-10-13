package com.duomizhibo.phonelive.bean.video;

import java.util.List;

/**
 * Created by  on 2019/8/1.
 * 首页视频加头部推荐
 */

public class HomeVideo {

    private int id;
    private String name;
    private int type;
    private List<VideoObject> list;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public List<VideoObject> getList() {
        return list;
    }

    public void setList(List<VideoObject> list) {
        this.list = list;
    }
}

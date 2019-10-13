package com.duomizhibo.phonelive.bean.video;

import java.util.List;

/**
 * Created by  on 2019/8/1.
 *
 */

public class HomeObject {

    private List<BannerObject> banner;
    private List<NoticeObject> notice;
    private List<HomeVideo> video;

    public List<BannerObject> getBanner() {
        return banner;
    }

    public void setBanner(List<BannerObject> banner) {
        this.banner = banner;
    }

    public List<NoticeObject> getNotice() {
        return notice;
    }

    public void setNotice(List<NoticeObject> notice) {
        this.notice = notice;
    }

    public List<HomeVideo> getVideo() {
        return video;
    }

    public void setVideo(List<HomeVideo> video) {
        this.video = video;
    }
}

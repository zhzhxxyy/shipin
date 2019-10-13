package com.duomizhibo.phonelive.bean.video;

/**
 * Created  on 2019/8/1.
 *
 * banner 轮播
 */

public class BannerObject {

    private String images_url;//图片地址
    private String info;
    private int target;
    private String url;//跳转的地址

    public String getImages_url() {
        return images_url;
    }

    public void setImages_url(String images_url) {
        this.images_url = images_url;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public int getTarget() {
        return target;
    }

    public void setTarget(int target) {
        this.target = target;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "BannerObject{" +
                "images_url='" + images_url + '\'' +
                ", info='" + info + '\'' +
                ", target=" + target +
                ", url='" + url + '\'' +
                '}';
    }
}

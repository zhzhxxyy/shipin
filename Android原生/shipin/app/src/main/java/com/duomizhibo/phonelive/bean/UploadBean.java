package com.duomizhibo.phonelive.bean;

import java.io.File;

/**
 * Created by cxf on 2017/7/12.
 */

public class UploadBean {
    private File video;
    private String videoName;
    private File coverPic;
    private String coverPicName;

    public UploadBean(File video, String videoName, File coverPic, String coverPicName) {
        this.video = video;
        this.videoName = videoName;
        this.coverPic = coverPic;
        this.coverPicName = coverPicName;
    }

    public File getVideo() {
        return video;
    }

    public void setVideo(File video) {
        this.video = video;
    }

    public String getVideoName() {
        return videoName;
    }

    public void setVideoName(String videoName) {
        this.videoName = videoName;
    }


    public File getCoverPic() {
        return coverPic;
    }

    public void setCoverPic(File coverPic) {
        this.coverPic = coverPic;
    }

    public String getCoverPicName() {
        return coverPicName;
    }

    public void setCoverPicName(String coverPicName) {
        this.coverPicName = coverPicName;
    }

}

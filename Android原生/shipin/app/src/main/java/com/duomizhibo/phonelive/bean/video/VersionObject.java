package com.duomizhibo.phonelive.bean.video;


public class VersionObject {
    private int versionCode;//获取版本
    private String versionName;//"1.0.0"
    private String uniqueID;//获取App唯一标识
    private String versionType="android";//android  ios
    private String description;
    private String url;
    private boolean isHasNew;//是否有新版本

    public int getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(int versionCode) {
        this.versionCode = versionCode;
    }

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    public String getUniqueID() {
        return uniqueID;
    }

    public void setUniqueID(String uniqueID) {
        this.uniqueID = uniqueID;
    }


    public String getVersionType() {
        return versionType;
    }

    public void setVersionType(String versionType) {
        this.versionType = versionType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public boolean isHasNew() {
        return isHasNew;
    }

    public void setIsHasNew(boolean isHasNew) {
        this.isHasNew = isHasNew;
    }
}

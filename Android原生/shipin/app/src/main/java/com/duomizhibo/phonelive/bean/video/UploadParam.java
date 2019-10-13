package com.duomizhibo.phonelive.bean.video;

//上次参数
public class UploadParam {

    private String post_url;//上次的地址
    private String serverType;//上次的地址
    private String name;
    private int chunk=0;
    private int chunks=1;
    private String newFileName;

    public String getPost_url() {
        return post_url;
    }

    public void setPost_url(String post_url) {
        this.post_url = post_url;
    }

    public String getServerType() {
        return serverType;
    }

    public void setServerType(String serverType) {
        this.serverType = serverType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getChunk() {
        return chunk;
    }

    public void setChunk(int chunk) {
        this.chunk = chunk;
    }

    public int getChunks() {
        return chunks;
    }

    public void setChunks(int chunks) {
        this.chunks = chunks;
    }

    public String getNewFileName() {
        return newFileName;
    }

    public void setNewFileName(String newFileName) {
        this.newFileName = newFileName;
    }


//image jpg,gif,png,jpeg
    //video rmvb,flv,mp4,mov,3gp,wmv,mp3,avi,mpeg
    //ico ico
}

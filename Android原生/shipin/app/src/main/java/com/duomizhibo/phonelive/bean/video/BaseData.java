package com.duomizhibo.phonelive.bean.video;



/**
 *
 */
public class BaseData {
    private int respCode;//0：错误，1：正常 2表示需要登录 3token失效
    private String respMsg;
    private String data;

    public int getRespCode() {
        return respCode;
    }

    public void setRespCode(int respCode) {
        this.respCode = respCode;
    }

    public String getRespMsg() {
        return respMsg;
    }

    public void setRespMsg(String respMsg) {
        this.respMsg = respMsg;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }


    public boolean isSuccess(){
       return this.respCode==1?true:false;
    }

    @Override
    public String toString() {
        return "BaseData{" +
                "respCode=" + respCode +
                ", respMsg='" + respMsg + '\'' +
                ", data='" + data + '\'' +
                '}';
    }
}

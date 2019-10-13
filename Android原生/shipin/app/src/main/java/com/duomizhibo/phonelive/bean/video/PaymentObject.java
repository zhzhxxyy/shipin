package com.duomizhibo.phonelive.bean.video;

import java.io.Serializable;


public class PaymentObject implements Serializable {

    private String payCode;//codePay|  nativePay|
    private String payIcon;
    private String payName;

    public String getPayCode() {
        return payCode;
    }

    public void setPayCode(String payCode) {
        this.payCode = payCode;
    }

    public String getPayIcon() {
        return payIcon;
    }

    public void setPayIcon(String payIcon) {
        this.payIcon = payIcon;
    }

    public String getPayName() {
        return payName;
    }

    public void setPayName(String payName) {
        this.payName = payName;
    }
}

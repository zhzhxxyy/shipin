package com.duomizhibo.phonelive.bean.video;

import java.io.Serializable;
import java.util.List;


public class RechargeObject implements Serializable {

   private String payCode;//codePay
   private double gold_exchange_rate;
   private List<GoldPackageObject> goldPackageList;
   private List<PaymentObject> paymentList;
   private List<RechargePackageObject> rechargeList;

    public String getPayCode() {
        return payCode;
    }

    public void setPayCode(String payCode) {
        this.payCode = payCode;
    }

    public double getGold_exchange_rate() {
        return gold_exchange_rate;
    }

    public void setGold_exchange_rate(double gold_exchange_rate) {
        this.gold_exchange_rate = gold_exchange_rate;
    }

    public List<GoldPackageObject> getGoldPackageList() {
        return goldPackageList;
    }

    public void setGoldPackageList(List<GoldPackageObject> goldPackageList) {
        this.goldPackageList = goldPackageList;
    }

    public List<PaymentObject> getPaymentList() {
        return paymentList;
    }

    public void setPaymentList(List<PaymentObject> paymentList) {
        this.paymentList = paymentList;
    }

    public List<RechargePackageObject> getRechargeList() {
        return rechargeList;
    }

    public void setRechargeList(List<RechargePackageObject> rechargeList) {
        this.rechargeList = rechargeList;
    }

    @Override
    public String toString() {
        return "RechargeObject{" +
                "payCode='" + payCode + '\'' +
                ", gold_exchange_rate=" + gold_exchange_rate +
                ", goldPackageList=" + goldPackageList +
                ", paymentList=" + paymentList +
                ", rechargeList=" + rechargeList +
                '}';
    }
}

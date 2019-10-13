package com.duomizhibo.phonelive.bean.video;


import com.duomizhibo.phonelive.utils.StringUtils;

public class RecordPay {

    private String order_sn;//订单号
    private int user_id;
    private String payment_code;
    private String pay_channel;

    private double price;
    private double real_pay_price;
    private String third_id;
    private int buy_type;//1 金币 2VIP
    private int buy_glod_num;
    private RechargePackageObject buy_vip_info;//套餐信息 如果是VIP的话
    private int status;//0未支付 1已支付
    private int add_time;
    private int update_time;
    private int pay_time;

    public String getOrder_sn() {
        return order_sn;
    }

    public void setOrder_sn(String order_sn) {
        this.order_sn = order_sn;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getPayment_code() {
        return payment_code;
    }

    public void setPayment_code(String payment_code) {
        this.payment_code = payment_code;
    }

    public String getPay_channel() {
        return pay_channel;
    }

    public void setPay_channel(String pay_channel) {
        this.pay_channel = pay_channel;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getReal_pay_price() {
        return real_pay_price;
    }

    public void setReal_pay_price(double real_pay_price) {
        this.real_pay_price = real_pay_price;
    }

    public String getThird_id() {
        return third_id;
    }

    public void setThird_id(String third_id) {
        this.third_id = third_id;
    }

    public int getBuy_type() {
        return buy_type;
    }

    public void setBuy_type(int buy_type) {
        this.buy_type = buy_type;
    }

    public int getBuy_glod_num() {
        return buy_glod_num;
    }

    public void setBuy_glod_num(int buy_glod_num) {
        this.buy_glod_num = buy_glod_num;
    }

    public RechargePackageObject getBuy_vip_info() {
        return buy_vip_info;
    }

    public void setBuy_vip_info(RechargePackageObject buy_vip_info) {
        this.buy_vip_info = buy_vip_info;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getAdd_time() {
        return add_time;
    }

    public void setAdd_time(int add_time) {
        this.add_time = add_time;
    }

    public int getUpdate_time() {
        return update_time;
    }

    public void setUpdate_time(int update_time) {
        this.update_time = update_time;
    }

    public int getPay_time() {
        return pay_time;
    }

    public void setPay_time(int pay_time) {
        this.pay_time = pay_time;
    }

    public String getAddTimeString(){

        if(add_time<=0){
            return  "";
        }
        return StringUtils.millisToStringDate(add_time*1000L,"yyyy-MM-dd HH:mm:ss");
    }


}

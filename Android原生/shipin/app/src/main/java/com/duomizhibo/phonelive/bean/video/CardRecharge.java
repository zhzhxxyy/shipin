package com.duomizhibo.phonelive.bean.video;

/**
 * Created  on 2019/8/1.
 */

public class CardRecharge {
    private int id;
    private String card_number;
    private int card_type;//1 VIP卡 2 金币卡
    private int out_time;
    private String out_times;
    private int status;//0 未使用 1已使用
    private int price;
    private int gold;
    private int vip_time;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCard_number() {
        return card_number;
    }

    public void setCard_number(String card_number) {
        this.card_number = card_number;
    }

    public int getCard_type() {
        return card_type;
    }

    public void setCard_type(int card_type) {
        this.card_type = card_type;
    }

    public int getOut_time() {
        return out_time;
    }

    public void setOut_time(int out_time) {
        this.out_time = out_time;
    }

    public String getOut_times() {
        return out_times;
    }

    public void setOut_times(String out_times) {
        this.out_times = out_times;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getGold() {
        return gold;
    }

    public void setGold(int gold) {
        this.gold = gold;
    }

    public int getVip_time() {
        return vip_time;
    }

    public void setVip_time(int vip_time) {
        this.vip_time = vip_time;
    }
}

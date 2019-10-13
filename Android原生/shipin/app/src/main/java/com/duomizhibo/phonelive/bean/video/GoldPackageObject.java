package com.duomizhibo.phonelive.bean.video;

import java.io.Serializable;



public class GoldPackageObject implements Serializable {

    private int id;
    private String name;
    private double price;
    private double gold;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getGold() {
        return gold;
    }

    public void setGold(double gold) {
        this.gold = gold;
    }
}

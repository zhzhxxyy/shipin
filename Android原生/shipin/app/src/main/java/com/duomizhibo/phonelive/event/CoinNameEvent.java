package com.duomizhibo.phonelive.event;

/**
 * Created by cxf on 2017/9/29.
 */

public class CoinNameEvent {
    private String coinName;

    public CoinNameEvent(String coinName) {
        this.coinName = coinName;
    }

    public String getCoinName() {
        return coinName;
    }

    public void setCoinName(String coinName) {
        this.coinName = coinName;
    }
}

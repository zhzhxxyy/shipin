package com.duomizhibo.phonelive.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by mengyunfeng on 17/4/7.
 */

public class BonusBean implements Serializable{
    /**
     * bonus_switch : 1
     * bonus_day : 0
     * bonus_list : [{"day":"1","coin":"10"},{"day":"2","coin":"20"},{"day":"3","coin":"30"},{"day":"4","coin":"40"},{"day":"5","coin":"50"},{"day":"6","coin":"60"},{"day":"7","coin":"70"}]
     */

    private String bonus_switch;
    private String bonus_day;
    private List<BonusListBean> bonus_list;

    public String getBonus_switch() {
        return bonus_switch;
    }

    public void setBonus_switch(String bonus_switch) {
        this.bonus_switch = bonus_switch;
    }

    public String getBonus_day() {
        return bonus_day;
    }

    public void setBonus_day(String bonus_day) {
        this.bonus_day = bonus_day;
    }

    public List<BonusListBean> getBonus_list() {
        return bonus_list;
    }

    public void setBonus_list(List<BonusListBean> bonus_list) {
        this.bonus_list = bonus_list;
    }

    public static class BonusListBean implements Serializable {
        /**
         * day : 1
         * coin : 10
         */

        private String day;
        private String coin;

        public String getDay() {
            return day;
        }

        public void setDay(String day) {
            this.day = day;
        }

        public String getCoin() {
            return coin;
        }

        public void setCoin(String coin) {
            this.coin = coin;
        }
    }
}

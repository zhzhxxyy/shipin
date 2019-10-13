package com.duomizhibo.phonelive.bean;

import android.graphics.Bitmap;

import java.io.Serializable;

/**
 * 奖项实体
 * Created by bakumon on 16-11-12.
 */

public class PrizeVo implements Serializable {
    public String id;//
    public String title;// 奖品名称,奖品名称,
    public String rate;// 倍率，大于1的整数,倍率，大于1的整数,
    public Bitmap img;
}

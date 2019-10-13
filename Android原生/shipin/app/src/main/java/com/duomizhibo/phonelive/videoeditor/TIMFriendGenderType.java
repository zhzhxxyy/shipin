package com.duomizhibo.phonelive.videoeditor;

/**
 * Created by cxf on 2017/9/8.
 */

public enum TIMFriendGenderType {
    Unknow(0L),
    Male(1L),
    Female(2L);

    private long gender = 0L;

    private TIMFriendGenderType(long var3) {
        this.gender = var3;
    }

    public final long getValue() {
        return this.gender;
    }
}

package com.duomizhibo.phonelive.bean;


import com.duomizhibo.phonelive.fragment.BlackListFragment;
import com.duomizhibo.phonelive.fragment.HotFragment;
import com.duomizhibo.phonelive.fragment.MessageDetailFragment;
import com.duomizhibo.phonelive.fragment.PushManageFragment;
import com.duomizhibo.phonelive.fragment.SearchFragment;
import com.duomizhibo.phonelive.fragment.SearchMusicDialogFragment;
import com.duomizhibo.phonelive.viewpagerfragment.PrivateChatCorePagerFragment;
import com.duomizhibo.phonelive.R;
import com.duomizhibo.phonelive.fragment.SelectAreaFragment;

public enum SimpleBackPage {
    USER_FAVORITE(1, 2, HotFragment.class),
    USER_PRIVATECORE(2,R.string.privatechat,PrivateChatCorePagerFragment.class),
    USER_PRIVATECORE_MESSAGE(3,R.string.privatechat,MessageDetailFragment.class),
    AREA_SELECT(4,R.string.area,SelectAreaFragment.class),
    INDEX_SECREEN(5,R.string.search, SearchFragment.class),
    USER_BLACK_LIST(6,R.string.blacklist, BlackListFragment.class),
    USER_PUSH_MANAGE(7,R.string.push,PushManageFragment.class),
    LIVE_START_MUSIC(8,R.string.diange,SearchMusicDialogFragment.class);
    private int title;
    private Class<?> clz;
    private int value;



    private SimpleBackPage(int value, int title, Class<?> clz) {
        this.value = value;
        this.title = title;
        this.clz = clz;
    }


    public int getTitle() {
        return title;
    }

    public void setTitle(int title) {
        this.title = title;
    }

    public Class<?> getClz() {
        return clz;
    }

    public void setClz(Class<?> clz) {
        this.clz = clz;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public static SimpleBackPage getPageByValue(int val) {
        for (SimpleBackPage p : values()) {
            if (p.getValue() == val)
                return p;
        }
        return null;
    }
}

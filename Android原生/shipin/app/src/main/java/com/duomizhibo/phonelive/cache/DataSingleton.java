package com.duomizhibo.phonelive.cache;

import com.duomizhibo.phonelive.bean.LiveJson;
import com.duomizhibo.phonelive.bean.UserBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/9/10. //HHH 2016-09-10
 */
public class DataSingleton {

    private static class SingletonHolder {
        /**
         * 单例对象实例
         */
        static final DataSingleton INSTANCE = new DataSingleton();

    }

    public static DataSingleton getInstance() {
        return SingletonHolder.INSTANCE;
    }

    /**
     * private的构造函数用于避免外界直接使用new来实例化对象
     */
    private DataSingleton() {
        userList = new ArrayList<>();
    }

    private List<LiveJson> userList;
    private int  postion;

    public List<LiveJson> getUserList() {
        return userList;
    }

    public void setUserList(List<LiveJson> userList) {
        this.userList = userList;
    }

    public int getPostion() {
        return postion;
    }

    public void setPostion(int postion) {
        this.postion = postion;
    }

    /**
     * readResolve方法应对单例对象被序列化时候
     */
    private Object readResolve() {
        return getInstance();
    }
}

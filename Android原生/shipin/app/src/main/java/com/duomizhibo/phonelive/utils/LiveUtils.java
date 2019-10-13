package com.duomizhibo.phonelive.utils;

import com.duomizhibo.phonelive.R;
import com.duomizhibo.phonelive.bean.LiveJson;
import com.duomizhibo.phonelive.bean.SimpleUserInfo;
import com.duomizhibo.phonelive.bean.UserBean;
import com.duomizhibo.phonelive.ui.other.DrawableRes;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscription;
import rx.functions.Action1;

/**
 * Created by weipeng on 16/8/16.
 */
public class LiveUtils {
    /**
     * @dw 获取歌词字符串
     * @param fileName 歌词文件目录
     * */
    public static String getFromFile(String fileName){
        try {
            FileInputStream fileInputStream = new FileInputStream(fileName);
            InputStreamReader inputReader = new InputStreamReader(fileInputStream);
            BufferedReader bufReader = new BufferedReader(inputReader);
            String line="";
            String Result="";
            while((line = bufReader.readLine()) != null){
                if(line.trim().equals(""))
                    continue;
                Result += line + "\r\n";
            }
            fileInputStream.close();
            inputReader.close();
            bufReader.close();
            return Result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static Subscription startInterval(final Runnable runnable){
        Subscription subscription = Observable.interval(1, 6 * 10000, TimeUnit.MILLISECONDS)
                .subscribe(new Action1<Long>() {
                    @Override
                    public void call(Long aLong) {
                        TLog.log("" + aLong);
                        runnable.run();
                    }
                });
        return subscription;
    }

    private static long lastClickTime;
    public synchronized static boolean isFastClick() {
        long time = System.currentTimeMillis();
        if ( time - lastClickTime < 500) {
            return true;
        }
        lastClickTime = time;
        return false;
    }


    //用户列表排序
    public static void sortUserList(List<SimpleUserInfo> mUserList){

        for(int i = 0; i < mUserList.size() - 1; i++){
            for(int j = 0 ; j < mUserList.size() - 1 -i; j++){

                //判断等级大小进行排序
                if(StringUtils.toInt(mUserList.get(j).level) <
                        StringUtils.toInt(mUserList.get(j + 1).level)){
                    SimpleUserInfo temp = mUserList.get(j);
                    mUserList.set(j,mUserList.get(j+1));
                    mUserList.set(j + 1,temp);
                }
            }
        }

    }

    //获取等级图片
    public static int getLevelRes(String level) {
        int l2 = StringUtils.toInt(level);
        if (l2 > 20) {
            l2 = 20;
        }
        return DrawableRes.LevelImg[(l2 == 0 ? 0 : l2 - 1)];

    }

    //获取等级图片
    public static int getAnchorLevelRes(String level) {
        int l2 = StringUtils.toInt(level);
        if (l2 > 20) {
            l2 = 20;
        }
        return DrawableRes.AnchorLevelImg[(l2 == 0 ? 0 : l2 - 1)];

    }

    //获取等级图片
    public static int getAnchorLevelRes2(String level) {
        int l2 = StringUtils.toInt(level);
        if (l2 > 20) {
            l2 = 20;
        }
        return DrawableRes.AnchorLevelImg2[(l2 == 0 ? 0 : l2 - 1)];

    }

    //获取性别图片
    public static int getSexRes(String sex){

        return StringUtils.toInt(sex) == 1 ? R.drawable.global_male : R.drawable.global_female;
    }

    public static SimpleUserInfo getSimleUserInfo(LiveJson liveJson){
        SimpleUserInfo simpleUserInfo = new SimpleUserInfo();

        simpleUserInfo.avatar = liveJson.avatar;
        simpleUserInfo.avatar_thumb = liveJson.avatar_thumb;
        simpleUserInfo.city = liveJson.city;
        simpleUserInfo.user_nicename = liveJson.user_nicename;
        simpleUserInfo.id = liveJson.uid;


        return simpleUserInfo;
    }

    public static String getFiledJson(String key,String val){

        return "{\"" +key+ "\":\""+ val +"\"}";
    }

    public static String getLimitString(String source, int length){
        if (null!=source && source.length()>length){
//            int reallen = 0;
            return source.substring(0, length)+"...";
        }
        return source;
    }

}

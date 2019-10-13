package com.duomizhibo.phonelive.api.remote;

import android.content.Intent;
import android.widget.Toast;

import com.duomizhibo.phonelive.bean.video.BaseData;
import com.google.gson.Gson;

import com.duomizhibo.phonelive.AppContext;
import com.duomizhibo.phonelive.ui.PhoneLoginActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ApiUtils {
    public final static int SUCCESS_CODE = 200;//成功请求到服务端
    public final static String TOKEN_TIMEOUT = "700";

    public static JSONArray checkIsSuccess(String res){
        try {
            JSONObject resJson = new JSONObject(res);

            if(Integer.parseInt(resJson.getString("ret")) == SUCCESS_CODE){
                JSONObject dataJson =  resJson.getJSONObject("data");
                String code = dataJson.getString("code");
                if(code.equals(TOKEN_TIMEOUT)){

                    //AppManager.getAppManager().finishAllActivity();
                    Intent intent = new Intent(AppContext.getInstance(), PhoneLoginActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    AppContext.getInstance().startActivity(intent);

                    return null;
                }else if(!code.equals("0")){
                    Toast.makeText(AppContext.getInstance(),dataJson.get("msg").toString(),Toast.LENGTH_LONG).show();
                    return null;
                }else {
                    return dataJson.getJSONArray("info");
                }
            }else{
                return null;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;

    }

    public static <T> List<T> formatDataToList2(JSONArray resUserListJsonArr, Class<T> c){

        List<T> datas = new ArrayList<>();
        try {
            Gson g = new Gson();
            for (int i = 0; i < resUserListJsonArr.length(); i++) {
                datas.add(g.fromJson(resUserListJsonArr.getString(i),c));
            }

            return datas;
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return datas;



    }

    public static <T> List<T> formatDataToList(String data, Class<T> c){

        List<T> datas = new ArrayList<>();
        JSONArray resUserListJsonArr = null;
        try {
            resUserListJsonArr = new JSONArray(data);
            Gson g = new Gson();

            for (int i = 0; i < resUserListJsonArr.length(); i++) {
                datas.add(g.fromJson(resUserListJsonArr.getString(i),c));
            }

            return datas;
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return datas;



    }



    /*==========视频请求=============*/
    public static BaseData getBaseData(String res){
        BaseData baseData;
        try {
              baseData=  com.alibaba.fastjson.JSONObject.parseObject(res, BaseData.class);
        } catch (Exception e) {
            baseData=new BaseData();
            baseData.setData(null);
            baseData.setRespCode(0);
            baseData.setRespMsg("请求失败：解析错误");
        }
        return baseData;
    }







}

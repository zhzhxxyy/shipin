package com.duomizhibo.phonelive.api.remote;

import android.util.Log;

import com.duomizhibo.phonelive.AppContext;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.Call;

/**
 * Created by cxf on 2017/7/6.
 */

public abstract class HttpCallback extends StringCallback {

    @Override
    public void onError(Call call, Exception e, int id) {
        Log.e("HttpCallback", "网络请求失败----> " + e.getMessage());
        onFailure();
    }

    @Override
    public void onResponse(String response, int id) {
        try {
            JSONObject obj = new JSONObject(response);
            JSONObject data = obj.getJSONObject("data");
            int code = data.getInt("code");
            String msg = data.getString("msg");
            JSONArray info = data.getJSONArray("info");
            if (code == 700) {
                AppContext.toast(msg);
                return;
            }
            onSuccess(code, msg, info);
        } catch (JSONException e) {
            e.printStackTrace();
            AppContext.toast("JSON解析错误！" + e.getMessage());
        }
    }

    public abstract void onSuccess(int code, String msg, JSONArray info) throws JSONException;

    public void onFailure() {

    }
}

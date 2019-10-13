package com.duomizhibo.phonelive.utils;

import android.util.Log;

import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UploadManager;
import com.duomizhibo.phonelive.api.remote.ApiUtils;
import com.duomizhibo.phonelive.api.remote.PhoneLiveApi;
import com.duomizhibo.phonelive.bean.UploadBean;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.Call;

/**
 * Created by cxf on 2017/7/12.
 */

public class UploadUtil {
    private static UploadUtil sInstance;
    private UploadManager mUploadManager;
    private final String TAG = "UploadUtil";
    private String mToken;

    private UploadUtil() {
        mUploadManager = new UploadManager();
    }

    public static UploadUtil getInstance() {
        if (sInstance == null) {
            synchronized (UploadUtil.class) {
                if (sInstance == null) {
                    sInstance = new UploadUtil();
                }
            }
        }
        return sInstance;
    }


    public void upload(final UploadBean bean, final Callback onCompleteCallback) {
        PhoneLiveApi.getQiniuToken(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {

            }

            @Override
            public void onResponse(String response, int id) {
                try {
                    JSONArray res = ApiUtils.checkIsSuccess(response);
                    mToken = res.getJSONObject(0).getString("token");
                    Log.e(TAG, "onResponse: -------上传的token------>" + mToken);
                    mUploadManager.put(bean.getVideo(), bean.getVideoName(), mToken, new UpCompletionHandler() {
                        @Override
                        public void complete(String key, ResponseInfo info, JSONObject response) {
                            Log.e(TAG, "complete: -key--> " + key);
                            Log.e(TAG, "complete: --info-> " + key);
                            Log.e(TAG, "complete: --response---> " + response);
                            mUploadManager.put(bean.getCoverPic(), bean.getCoverPicName(), mToken, new UpCompletionHandler() {
                                @Override
                                public void complete(String key, ResponseInfo info, JSONObject response) {
                                    Log.e(TAG, "complete: -key--> " + key);
                                    Log.e(TAG, "complete: --info-> " + key);
                                    Log.e(TAG, "complete: --response---> " + response);
                                    if (onCompleteCallback != null) {
                                        onCompleteCallback.callback(bean);
                                    }
                                }
                            }, null);
                        }
                    }, null);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    public interface Callback {
        void callback(UploadBean bean);
    }


}

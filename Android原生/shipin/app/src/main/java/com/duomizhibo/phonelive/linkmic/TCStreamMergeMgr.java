package com.duomizhibo.phonelive.linkmic;

import android.util.Log;


import com.duomizhibo.phonelive.AppConfig;
import com.duomizhibo.phonelive.AppContext;
import com.duomizhibo.phonelive.TCConstants;
import com.duomizhibo.phonelive.api.remote.PhoneLiveApi;
import com.duomizhibo.phonelive.utils.TCUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Vector;

import javax.net.ssl.HttpsURLConnection;

import okhttp3.Call;

/**
 * Created by dennyfeng on 17/3/27.
 */
public class TCStreamMergeMgr {

    private static final String TAG = TCStreamMergeMgr.class.getName();

    private static final int MAX_SUB_VIDEO_STREAM = 3;

    private String mMainStreamId = "";

    private int mMainStreamWidth = 540;
    private int mMainStreamHeight = 960;

    private Vector<String> mSubStreamIds = new Vector<String>();

    private static class TCStreamMergeMgrHolder {
        private static TCStreamMergeMgr instance = new TCStreamMergeMgr();
    }

    public static TCStreamMergeMgr getInstance() {
        return TCStreamMergeMgrHolder.instance;
    }

    public void setMainVideoStream(String streamUrl) {
        mMainStreamId = TCUtils.getStreamIDByStreamUrl(streamUrl);

        Log.e(TAG, "MergeVideoStream: setMainVideoStream " + mMainStreamId);
    }

    public void setMainVideoStreamResolution(int width, int height) {
        if (width > 0 && height > 0) {
            mMainStreamWidth = width;
            mMainStreamHeight = height;
        }
    }

    public void addSubVideoStream(String streamUrl) {
        if (mSubStreamIds.size() > 3) {
            return;
        }

        String streamId = TCUtils.getStreamIDByStreamUrl(streamUrl);

        Log.e(TAG, "MergeVideoStream: addSubVideoStream " + streamId);

        if (streamId == null || streamId.length() == 0) {
            return;
        }

        for (String item : mSubStreamIds) {
            if (item.equalsIgnoreCase(streamId)) {
                return;
            }
        }

        mSubStreamIds.add(streamId);
        sendStreamMergeRequest(5);
    }

    public void delSubVideoStream(String streamUrl) {
        String streamId = TCUtils.getStreamIDByStreamUrl(streamUrl);

        Log.e(TAG, "MergeVideoStream: delSubVideoStream " + streamId);

        boolean bExist = false;
        for (String item : mSubStreamIds) {
            if (item.equalsIgnoreCase(streamId)) {
                bExist = true;
                break;
            }
        }

        if (bExist == true) {
            mSubStreamIds.remove(streamId);
            sendStreamMergeRequest(1);
        }
    }

    public void resetMergeState() {
        Log.e(TAG, "MergeVideoStream: resetMergeState");

        mSubStreamIds.clear();

        if (mMainStreamId != null && mMainStreamId.length() > 0) {
            sendStreamMergeRequest(1);
        }

        mMainStreamId = null;
        mMainStreamWidth = 540;
        mMainStreamHeight = 960;
    }

    private void sendStreamMergeRequest(final int retryCount) {
//        if (mMainStreamId == null || mMainStreamId.length() == 0) {
//            return;
//        }

        final JSONObject requestParam = createRequestParam();
        if (requestParam == null) {
            return;
        }
            PhoneLiveApi.mergeVideoStream(AppContext.getInstance().getLoginUid(), requestParam.toString(), new StringCallback() {
                @Override
                public void onError(Call call, Exception e, int id) {

                }
                @Override
                public void onResponse(String response, int id) {

                }
            });



//        new Thread() {
//            @Override
//            public void run() {
//
//                for (int retryIndex = retryCount; retryIndex >= 1; --retryIndex) {
//                    try {
//                        //构造请求包
//                        JSONObject req = new JSONObject();
//                        req.put("service", "Linkmic.MergeVideoStream");
//                        req.put("userid", AppContext.getInstance().getLoginUid());
//                        req.put("mergeparams", requestParam);
//
//                        String strRequest = req.toString();
//
//                        String streamsInfo = "mainStream: " + mMainStreamId;
//                        for (int i = 0; i < mSubStreamIds.size(); ++i) {
//                            streamsInfo = streamsInfo + " subStream" + i + ": " + mSubStreamIds.get(i);
//                        }
//
//                        //初始化https请求
//                        URL url = new URL(AppConfig.MAIN_URL);
//                        URLConnection connection = url.openConnection();
//                        HttpsURLConnection httpsConnection = (HttpsURLConnection) connection;
//                        httpsConnection.setDoOutput(true);
//                        httpsConnection.setDoInput(true);
//                        httpsConnection.setUseCaches(false);
//                        httpsConnection.setConnectTimeout(5 * 1000);
//                        httpsConnection.setReadTimeout(5 * 1000);
//                        httpsConnection.setRequestMethod("POST");
//                        httpsConnection.setRequestProperty("Charsert", "UTF-8");
//                        httpsConnection.setRequestProperty("Content-Type", "text/plain;");
//                        httpsConnection.setRequestProperty("Content-Length", String.valueOf(strRequest.length()));
//
//                        Log.e(TAG, "MergeVideoStream: send request, " + streamsInfo + " retryIndex: " + retryIndex + "    " + strRequest);
//
//                        //发送请求包体
//                        OutputStream out = new DataOutputStream(httpsConnection.getOutputStream());
//                        out.write(strRequest.getBytes());
//
//                        //读取响应包
//                        String strResponse = "";
//                        BufferedReader reader = new BufferedReader(new InputStreamReader(httpsConnection.getInputStream()));
//                        String line = "";
//                        while ((line = reader.readLine()) != null) {
//                            strResponse += line;
//                        }
//
//                        //解析returnData字段
//                        JSONObject rsp = new JSONObject(strResponse);
//                        JSONObject retData = rsp.optJSONObject(AppConfig.SVR_RETURN_DATA);
//
//                        String strMessage = "";
//                        if (retData != null) {
//                            strMessage = retData.getString("msg");
//                        }
//
//                        Log.e(TAG, "MergeVideoStream: recv response, message = " + strMessage);
//
//                        if (strMessage.length() > 0) {
//                            JSONObject message = new JSONObject(strMessage);
//                            int code = message.getInt("code");
//                            if (code == 0) {
//                                return;
//                            }
//                        }
//                        sleep(2000, 0);
//                    }
//                    catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//        }.start();
    }

    private JSONObject createRequestParam() {

        JSONObject requestParam = null;

        try {
            // input_stream_list
            JSONArray inputStreamList = new JSONArray();

            // 大主播
            {
                JSONObject layoutParam = new JSONObject();
                layoutParam.put("image_layer", 1);

                JSONObject mainStream = new JSONObject();
                mainStream.put("input_stream_id", mMainStreamId);
                mainStream.put("layout_params", layoutParam);

                inputStreamList.put(mainStream);
            }

            int subWidth = 160;
            int subHeight = 240;
            int offsetHeight = 90;
            if (mMainStreamWidth < 540 || mMainStreamHeight < 960) {
                subWidth = 120;
                subHeight = 180;
                offsetHeight = 60;
            }
            int subLocationX = mMainStreamWidth - subWidth;
            int subLocationY = mMainStreamHeight - subHeight - offsetHeight;

            // 小主播
            int layerIndex = 0;
            for (String item : mSubStreamIds) {
                JSONObject layoutParam = new JSONObject();
                layoutParam.put("image_layer", layerIndex + 2);
                layoutParam.put("image_width", subWidth);
                layoutParam.put("image_height", subHeight);
                layoutParam.put("location_x", subLocationX);
                layoutParam.put("location_y", subLocationY - layerIndex * subHeight);

                JSONObject subStream = new JSONObject();
                subStream.put("input_stream_id", item);
                subStream.put("layout_params", layoutParam);

                inputStreamList.put(subStream);
                ++layerIndex;
            }

            // para
            JSONObject para = new JSONObject();
            para.put("app_id", AppConfig.XIAOZHIBO_APPID);
            para.put("interface", "mix_streamv2.start_mix_stream_advanced");
            para.put("mix_stream_session_id", mMainStreamId);
            para.put("output_stream_id", mMainStreamId);
            para.put("input_stream_list", inputStreamList);

            // interface
            JSONObject interfaceObj = new JSONObject();
            interfaceObj.put("interfaceName", "Mix_StreamV2");
            interfaceObj.put("para", para);

            // requestParam
            requestParam = new JSONObject();
            requestParam.put("timestamp", System.currentTimeMillis() / 1000);
            requestParam.put("eventId", System.currentTimeMillis() / 1000);
            requestParam.put("interface", interfaceObj);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return requestParam;
    }
}

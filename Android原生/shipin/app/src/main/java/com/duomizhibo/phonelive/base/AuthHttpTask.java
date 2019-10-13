package com.duomizhibo.phonelive.base;

import android.os.AsyncTask;
import android.os.Looper;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * get auther info from server
 */
public class AuthHttpTask extends AsyncTask<String, Void, Void> {
    private static final String TAG = "AuthHttpTask";
    private static final boolean VERBOSE = false;
    public static final int RESPONSE_PARSE_ERROR = 600;

    private KSYOnHttpResponse mOnHttpResponse;
    public AuthHttpTask(KSYOnHttpResponse onHttpResponse) {
        mOnHttpResponse = onHttpResponse;
    }

    @Override
    protected Void doInBackground(String... strings) {
        Looper.prepare();
        HttpURLConnection conn = null;

        try {
            URL url = new URL(strings[0]);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(5000);
            conn.setReadTimeout(5000);

            int responseCode = conn.getResponseCode();
            if (VERBOSE)
                Log.d(TAG, "responseCode=" + responseCode);
            if (responseCode == 200) {
                InputStream is = conn.getInputStream();
                String body = getStringFromInputStream(is);
                mOnHttpResponse.onHttpResponse(responseCode, body);
                if (VERBOSE)
                    Log.d(TAG, "response:" + body);
            } else {
                if (VERBOSE)
                    Log.e(TAG, "AuthHttpTask responseCode = " + responseCode);
                mOnHttpResponse.onHttpResponse(responseCode, null);
            }
        } catch (Exception e) {
            if (VERBOSE)
                Log.e(TAG, "AuthHttpTask failed");
            mOnHttpResponse.onHttpResponse(RESPONSE_PARSE_ERROR, null);
            e.printStackTrace();
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
        Looper.loop();
        return null;
    }

    public String getStringFromInputStream(InputStream inStream) throws IOException {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = 0;
        while ((len = inStream.read(buffer)) != -1) {
            outStream.write(buffer, 0, len);
        }
        inStream.close();
        String body = outStream.toString();
        outStream.close();
        return body;
    }

    public interface KSYOnHttpResponse {
        void onHttpResponse(int responseCode, String response);
    }
}
package com.duomizhibo.phonelive.linkmic;

import android.os.Bundle;

import com.tencent.rtmp.ITXLivePushListener;

public class TCLivePushListenerImpl implements ITXLivePushListener {

    public interface ITCLivePushListener {
        void onLivePushEvent(String pushUrl, int event, Bundle bundle);

        void onLivePushNetStatus(String pushUrl, Bundle status);
    }

    private String mPushUrl;
    private ITCLivePushListener mListener;

    public void setPushUrl(String pushUrl) {
        mPushUrl = pushUrl;
    }

    public void setListener(ITCLivePushListener listener) {
        mListener = listener;
    }

    @Override
    public void onPushEvent(int event, Bundle bundle) {
        if (mListener != null && mPushUrl != null && mPushUrl.length() > 0) {
            mListener.onLivePushEvent(mPushUrl, event, bundle);
        }
    }
    @Override
    public void onNetStatus(Bundle status) {
        if (mListener != null && mPushUrl != null && mPushUrl.length() > 0) {
            mListener.onLivePushNetStatus(mPushUrl, status);
        }
    }
}

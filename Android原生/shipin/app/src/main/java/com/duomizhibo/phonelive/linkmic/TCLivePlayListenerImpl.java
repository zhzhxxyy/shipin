package com.duomizhibo.phonelive.linkmic;

import android.os.Bundle;

import com.tencent.rtmp.ITXLivePlayListener;

public class TCLivePlayListenerImpl implements ITXLivePlayListener {

    public interface ITCLivePlayListener {
        void onLivePlayEvent(String playUrl, int event, Bundle bundle);

        void onLivePlayNetStatus(String playUrl, Bundle status);
    }

    private String mPlayUrl;
    private ITCLivePlayListener mListener;

    public void setPlayUrl(String playUrl) {
        mPlayUrl = playUrl;
    }

    public void setListener(ITCLivePlayListener listener) {
        mListener = listener;
    }

    @Override
    public void onPlayEvent(int event, Bundle bundle) {
        if (mListener != null && mPlayUrl != null && mPlayUrl.length() > 0) {
            mListener.onLivePlayEvent(mPlayUrl, event, bundle);
        }
    }

    @Override
    public void onNetStatus(Bundle status) {
        if (mListener != null && mPlayUrl != null && mPlayUrl.length() > 0) {
            mListener.onLivePlayNetStatus(mPlayUrl, status);
        }
    }
}
package com.duomizhibo.phonelive.linkmic;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.tencent.rtmp.TXLiveConstants;
import com.tencent.rtmp.TXLivePlayConfig;
import com.tencent.rtmp.TXLivePlayer;
import com.tencent.rtmp.TXLivePushConfig;
import com.tencent.rtmp.TXLivePusher;
import com.tencent.rtmp.ui.TXCloudVideoView;
import com.duomizhibo.phonelive.R;
import com.duomizhibo.phonelive.utils.TCUtils;


public class TCLinkMicPlayItem {
    public boolean              mPending = false;
    public String               mUserID = "";
    public String               mPlayUrl = "";
    public String               pushUrl = "";

    private TXCloudVideoView mVideoView;
    private ImageView           mLoadingImg;
    private FrameLayout         mLoadingBkg;
    private Button              mBtnKickout;
    
    private Runnable            mTimeOutRunnable;
    private TXLivePusher mTXLivePusher;
    private TXLivePlayer mTXLivePlayer;
    private TCLivePlayListenerImpl mTCPlayListener;
    private TCLivePushListenerImpl mTCLivePushListener;
    private TXLivePlayConfig mTXLivePlayConfig = new TXLivePlayConfig();
    private TXLivePushConfig mTXLivePushConfig;
    public TCLinkMicPlayItem(Activity activity, TCLivePlayListenerImpl.ITCLivePlayListener listener, TCLivePushListenerImpl.ITCLivePushListener listener2,boolean showLog, int index) {
        int videoViewId = R.id.play_video_view1;
        int loadingImgId = R.id.loading_imageview1;
        int loadingBkgId = R.id.loading_background1;
        int kickoutId = R.id.btn_kick_out1;
        if (index == 1) {
            videoViewId = R.id.play_video_view1;
            loadingImgId = R.id.loading_imageview1;
            loadingBkgId = R.id.loading_background1;
            kickoutId = R.id.btn_kick_out1;
        }
        else if (index == 2) {
            videoViewId = R.id.play_video_view2;
            loadingImgId = R.id.loading_imageview2;
            loadingBkgId = R.id.loading_background2;
            kickoutId = R.id.btn_kick_out2;
        }
        else if (index == 3) {
            videoViewId = R.id.play_video_view3;
            loadingImgId = R.id.loading_imageview3;
            loadingBkgId = R.id.loading_background3;
            kickoutId = R.id.btn_kick_out3;
        }

        mVideoView = (TXCloudVideoView) activity.findViewById(videoViewId);
        mLoadingImg = (ImageView) activity.findViewById(loadingImgId);
        mLoadingBkg = (FrameLayout) activity.findViewById(loadingBkgId);
        mBtnKickout = (Button) activity.findViewById(kickoutId);

//        mVideoView.disableLog(!showLog);

        mTXLivePlayConfig = new TXLivePlayConfig();
        mTXLivePlayConfig.enableAEC(true);
        mTXLivePlayConfig.setAutoAdjustCacheTime(true);
        mTXLivePlayConfig.setMinAutoAdjustCacheTime(0.2f);
        mTXLivePlayConfig.setMaxAutoAdjustCacheTime(0.2f);

        // 连麦推流
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        Bitmap bitmap = BitmapFactory.decodeResource(activity.getResources(), R.drawable.pause_publish, options);
        mTXLivePushConfig = new TXLivePushConfig();
        mTXLivePushConfig.setPauseImg(bitmap);
        mTXLivePushConfig.setPauseFlag(TXLiveConstants.PAUSE_FLAG_PAUSE_VIDEO | TXLiveConstants.PAUSE_FLAG_PAUSE_AUDIO);
        mTXLivePushConfig.setAudioSampleRate(48000);
        mTXLivePushConfig.setBeautyFilter(TCUtils.filtNumber(9, 100, 50), TCUtils.filtNumber(3, 100, 0));

        mTCPlayListener = new TCLivePlayListenerImpl();
        mTCPlayListener.setListener(listener);

        mTCLivePushListener = new TCLivePushListenerImpl();
        mTCLivePushListener.setListener(listener2);

        mTXLivePlayer = new TXLivePlayer(activity.getApplicationContext());
        mTXLivePlayer.setPlayListener(mTCPlayListener);
        mTXLivePlayer.setPlayerView(mVideoView);
        mTXLivePlayer.enableHardwareDecode(true);
        mTXLivePlayer.setConfig(mTXLivePlayConfig);

        mTXLivePusher = new TXLivePusher(activity.getApplicationContext());
        mTXLivePusher.setPushListener(mTCLivePushListener);
        mTXLivePusher.setConfig(mTXLivePushConfig);
        mTXLivePusher.setMicVolume(2.0f);
    }

    public void empty() {
        mPending = false;
        mUserID = "";
        mPlayUrl = "";
    }

    public void setmTimeOutRunnable(Runnable runnable) {
        mTimeOutRunnable = runnable;
    }

    public Runnable getTimeOutRunnable() {
        return mTimeOutRunnable;
    }

    public int getKickoutBtnId() {
        return mBtnKickout.getId();
    }

    public void startLoading() {
        mBtnKickout.setVisibility(View.INVISIBLE);
        mLoadingBkg.setVisibility(View.VISIBLE);
        mLoadingImg.setVisibility(View.VISIBLE);
        mLoadingImg.setImageResource(R.drawable.linkmic_loading);
        AnimationDrawable ad = (AnimationDrawable) mLoadingImg.getDrawable();
        ad.start();
    }

    public void stopLoading(boolean showKickoutBtn) {
        mBtnKickout.setVisibility(showKickoutBtn ? View.VISIBLE : View.GONE);
        mLoadingBkg.setVisibility(View.GONE);
        mLoadingImg.setVisibility(View.GONE);
        AnimationDrawable ad = (AnimationDrawable) mLoadingImg.getDrawable();
        if (ad != null) {
            ad.stop();
        }
    }

    public void stopLoading() {
        mBtnKickout.setVisibility(View.GONE);
        mLoadingBkg.setVisibility(View.GONE);
        mLoadingImg.setVisibility(View.GONE);
        AnimationDrawable ad = (AnimationDrawable) mLoadingImg.getDrawable();
        if (ad != null) {
            ad.stop();
        }
    }

    public void startPlay(String playUrl) {
        if (mVideoView != null) {
            mVideoView.clearLog();
        }
        if (playUrl != null && playUrl.length() > 0) {
            mPlayUrl = playUrl;
            mTCPlayListener.setPlayUrl(playUrl);
            if (mTXLivePlayer != null) {
                mTXLivePlayer.startPlay(playUrl, TXLivePlayer.PLAY_TYPE_LIVE_RTMP_ACC);
            }
        }
    }
    public void startPush(String pusherUrl) {
        if (mVideoView != null) {
            mVideoView.clearLog();
        }
        //启动推流
        if (mTXLivePusher != null) {
            if (pusherUrl!=null&&pusherUrl.length()>0){
                mVideoView.setVisibility(View.VISIBLE);
                mTCLivePushListener.setPushUrl(pusherUrl);
                mTXLivePusher.setVideoQuality(TXLiveConstants.VIDEO_QUALITY_LINKMIC_SUB_PUBLISHER);
                mTXLivePusher.startCameraPreview(mVideoView);
                mTXLivePusher.startPusher(pusherUrl);
            }

        }
    }
    public void stopPush(){
        //结束推流
        if (mTXLivePusher != null) {
            mTXLivePusher.stopCameraPreview(true);
            mTXLivePusher.stopPusher();
        }
    }

    public void stopPlay(boolean isNeedClearLastImg) {
        stopLoading(false);
        if (!TextUtils.isEmpty(mPlayUrl)) {
            if (mTXLivePlayer != null) {
                mTXLivePlayer.stopPlay(isNeedClearLastImg);
            }

        }
    }

    public void setLogText(Bundle status, Bundle event, int eventId) {
        if (mVideoView != null) {
            mVideoView.setLogText(status, event, eventId);
        }
    }

    public void clearLog() {
        if (mVideoView != null) {
            mVideoView.clearLog();
        }
    }

    public void showLog(boolean show) {
        if (mVideoView != null) {
            mVideoView.disableLog(show);
        }
    }

    public void pause() {
        if (mVideoView != null) {
            mVideoView.onPause();
        }
    }

    public void resume() {
        if (mVideoView != null) {
            mVideoView.onResume();
        }
    }

    public void destroy() {
        if (mVideoView != null) {
            mVideoView.onDestroy();
            mVideoView = null;
        }
        if (mTXLivePlayer != null) {
            mTXLivePlayer.setPlayListener(null);
            mTXLivePlayer = null;
        }
        if (mTCPlayListener != null) {
            mTCPlayListener.setListener(null);
        }
    }
}

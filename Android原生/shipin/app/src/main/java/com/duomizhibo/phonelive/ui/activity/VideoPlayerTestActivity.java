package com.duomizhibo.phonelive.ui.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.duomizhibo.phonelive.AppContext;
import com.duomizhibo.phonelive.R;
import com.duomizhibo.phonelive.TCConstants;
import com.duomizhibo.phonelive.bean.video.PlayState;
import com.duomizhibo.phonelive.interf.FullScreenGestureStateListener;
import com.duomizhibo.phonelive.utils.TLog;
import com.duomizhibo.phonelive.widget.FullScreenGestureView;
import com.tencent.rtmp.ITXLivePlayListener;
import com.tencent.rtmp.TXLiveConstants;
import com.tencent.rtmp.TXLivePlayConfig;
import com.tencent.rtmp.TXLivePlayer;
import com.tencent.rtmp.ui.TXCloudVideoView;

import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

/*

* 直播播放页面
* */
public class VideoPlayerTestActivity extends AppCompatActivity implements View.OnLayoutChangeListener, ITXLivePlayListener,View.OnClickListener,AudioManager.OnAudioFocusChangeListener,
        FullScreenGestureStateListener{

    private View rl_play;//播放布局
    private TXCloudVideoView  mTXCloudVideoView;//视频播放
    private ImageView iv_play_cover;//视频封面
    private ImageView iv_play_start;//点击开始播放
    private ProgressBar pb_loading;//加载中
    private TextView tv_play_fail;//播放出错

    private View ll_control_bottom;//底部控制器
    private ImageView iv_control_start;//点击开始播放 或暂停播放
    private SeekBar sb_control_progress;//播放进度
    private TextView tv_control_time;//播放进度时间显示
    private TextView tv_quanping;//全屏

    private View ll_top;//头部页面
    private View iv_top_back;//点击返回
    private TextView tv_top_title;//标题

    private TXLivePlayer mTXLivePlayer;//世纪播放器
    private TXLivePlayConfig mTXPlayConfig = new TXLivePlayConfig();
    protected boolean mIsLivePlay = false;//是否是直播
    private int mPlayType = TXLivePlayer.PLAY_TYPE_LIVE_RTMP;
    private String mPlayUrl = "http://f3.chahz.com/201908/02/ck4Vz2MV/index.m3u8";

    private long mTrackingTouchTS = 0;
    private boolean mStartSeek = false;
    private boolean mVideoPlay = false;
    private boolean mVideoPause = false;
    private boolean mAutoPause = false;

    private int heightView;//默认高度

    private FrameLayout fl_all;
    private FullScreenGestureView fsg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.new_activity_video_player_test);
        initView();
        heightView= rl_play.getLayoutParams().height;
        chushiView();
        initData();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        chushiView();
        initData();
    }


    @Override
    protected void onResume() {
        super.onResume();
        mTXCloudVideoView.onResume();
        if (mVideoPlay && mAutoPause&&mTXLivePlayer!=null) {
            mTXLivePlayer.resume();
            mAutoPause = false;
        }
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        requestAudioFocus();
    }


    @Override
    protected void onPause() {
        super.onPause();
        mTXCloudVideoView.onPause();
        if (mVideoPlay && !mVideoPause &&mTXLivePlayer!=null) {
            mTXLivePlayer.pause();
            mAutoPause = true;
        }
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        abandonAudioFocus();
    }


    @Override
    public void onBackPressed() {
        clickBack();
    }

    @Override
    protected void onDestroy() {//释放
        super.onDestroy();
        mTXCloudVideoView.onDestroy();
        stopPlay(true);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_play_start:
                startPlay();
                break;
            case R.id.tv_play_fail:
                startPlay();
                break;
            case R.id.iv_control_start:
                if (mVideoPlay) {
                    if (mVideoPause) {
                        mTXLivePlayer.resume();
                        iv_control_start.setBackgroundResource(R.drawable.icon_record_pause);
                        mVideoPause = false;
                    } else {
                        mTXLivePlayer.pause();
                        iv_control_start.setBackgroundResource(R.drawable.icon_record_start);
                        mVideoPause = true;
                    }
                } else {
                    startPlay();
                }
                break;
            case R.id.tv_quanping:
                clickQuanpin();
                break;
            case R.id.iv_top_back:
                clickBack();
                break;
        }
    }

    @Override
    public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {

    }


    @Override
    public void onPlayEvent(int i, Bundle bundle) {

        if (i == TXLiveConstants.PLAY_EVT_PLAY_BEGIN) {
            pb_loading.setVisibility(View.GONE);

        }else if (i == TXLiveConstants.PLAY_EVT_PLAY_PROGRESS) {
            if (mStartSeek) {
                return;
            }
            int progress = bundle.getInt(TXLiveConstants.EVT_PLAY_PROGRESS);
            int duration = bundle.getInt(TXLiveConstants.EVT_PLAY_DURATION);
            long curTS = System.currentTimeMillis();
            // 避免滑动进度条松开的瞬间可能出现滑动条瞬间跳到上一个位置
            if (Math.abs(curTS - mTrackingTouchTS) < 500) {
                return;
            }
            mTrackingTouchTS = curTS;

            if (sb_control_progress != null) {
                if(sb_control_progress.getMax()!=duration){
                    sb_control_progress.setMax(duration);
                }
                sb_control_progress.setProgress(progress);
            }
        } else if (i == TXLiveConstants.PLAY_ERR_NET_DISCONNECT) {

            tv_play_fail.setVisibility(View.VISIBLE);

        } else if (i == TXLiveConstants.PLAY_EVT_PLAY_END) {
            if (sb_control_progress != null) {
                sb_control_progress.setProgress(0);
            }
            stopPlay(false);

        }else if(i == TXLiveConstants.PLAY_EVT_PLAY_LOADING){
            TLog.log("------PLAY_EVT_PLAY_LOADING----->>",""+i);

            pb_loading.setVisibility(View.VISIBLE);

        }else{
            TLog.log("------其他----->>",""+i);
        }

//        if (i == TXLiveConstants.PLAY_EVT_PLAY_END) {
//            //循环播放
//            if (mTXLivePlayer != null) {
//                mTXLivePlayer.seek(0);
//                mTXLivePlayer.resume();
//            }
//        }
//
    }


    @Override
    public void onNetStatus(Bundle status) {
        if (status.getInt(TXLiveConstants.NET_STATUS_VIDEO_WIDTH) > status.getInt(TXLiveConstants.NET_STATUS_VIDEO_HEIGHT)) {
            if (mTXLivePlayer != null) {
                mTXLivePlayer.setRenderRotation(TXLiveConstants.RENDER_ROTATION_LANDSCAPE);
            }
        } else if (mTXLivePlayer != null) {
            mTXLivePlayer.setRenderRotation(TXLiveConstants.RENDER_ROTATION_PORTRAIT);
        }

    }


    public void initView() {
        rl_play=findViewById(R.id.rl_play);
        mTXCloudVideoView=(TXCloudVideoView)findViewById(R.id.tcv_play_view);
        iv_play_cover=(ImageView)findViewById(R.id.iv_play_cover);
        iv_play_start=(ImageView)findViewById(R.id.iv_play_start);
        pb_loading=(ProgressBar)findViewById(R.id.pb_loading);
        tv_play_fail=(TextView) findViewById(R.id.tv_play_fail);
        ll_control_bottom=findViewById(R.id.ll_control_bottom);
        iv_control_start=(ImageView) findViewById(R.id.iv_control_start);
        sb_control_progress=(SeekBar)findViewById(R.id.sb_control_progress);
        tv_control_time=(TextView)findViewById(R.id.tv_control_time);
        tv_quanping=(TextView) findViewById(R.id.tv_quanping);
        ll_top=findViewById(R.id.ll_top);
        iv_top_back=findViewById(R.id.iv_top_back);
        tv_top_title=(TextView) findViewById(R.id.tv_top_title);

        iv_play_start.setOnClickListener(this);
        tv_play_fail.setOnClickListener(this);
        iv_control_start.setOnClickListener(this);
        tv_quanping.setOnClickListener(this);
        iv_top_back.setOnClickListener(this);
        sb_control_progress.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean bFromUser) {
                if (tv_control_time != null) {
                    tv_control_time.setText(String.format(Locale.CHINA, "%02d:%02d/%02d:%02d", (progress)/60, (progress) % 60, (seekBar.getMax()) / 60, (seekBar.getMax()) % 60));
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                mStartSeek = true;
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if ( mTXLivePlayer != null) {
                    mTXLivePlayer.seek(seekBar.getProgress());
                }
                mTrackingTouchTS = System.currentTimeMillis();
                mStartSeek = false;
            }
        });
        rl_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(iv_play_cover.getVisibility()==View.GONE){
                    if(ll_top.getVisibility()!=View.GONE){
                        ll_top.setVisibility(View.GONE);
                        ll_control_bottom.setVisibility(View.GONE);
                    }else{
                        ll_top.setVisibility(View.VISIBLE);
                        ll_control_bottom.setVisibility(View.VISIBLE);

                    }
                }
            }
        });

        fl_all=(FrameLayout)findViewById(R.id.fl_all);
        fsg=(FullScreenGestureView)findViewById(R.id.fsg);
        fl_all.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                fsg.onTouch(event, VideoPlayerTestActivity.this,
                        100, PlayState.STATE_PLAYING);

                return false;
            }
        });
    }

    public void initData() {
        this.setVolumeControlStream(AudioManager.STREAM_MUSIC);
        //设置背景图
        // mIvLoadingBg.setVisibility(View.GONE);


    }

    //最初始页面
    private void chushiView(){
        stopPlay(true);
        ll_top.setVisibility(View.VISIBLE);
        tv_quanping.setText("全屏");
        ll_control_bottom.setVisibility(View.GONE);
        iv_play_cover.setVisibility(View.VISIBLE);
        iv_play_start.setVisibility(View.VISIBLE);
        pb_loading.setVisibility(View.GONE);
        tv_play_fail.setVisibility(View.GONE);
    }



    /*-----------播放相关处理----------------*/

    //点击开始播放
    protected void startPlay() {
        if (!checkPlayUrl()) {
            return;
        }
        if (mTXLivePlayer == null) {
            mTXLivePlayer = new TXLivePlayer(this);
        }
        mTXLivePlayer.setPlayerView(mTXCloudVideoView);
        mTXLivePlayer.setRenderRotation(TXLiveConstants.RENDER_ROTATION_PORTRAIT);
        mTXLivePlayer.setRenderMode(TXLiveConstants.RENDER_MODE_FULL_FILL_SCREEN);
        mTXLivePlayer.setPlayListener(this);
        mTXLivePlayer.setConfig(mTXPlayConfig);
        int result;
        result = mTXLivePlayer.startPlay(mPlayUrl, mPlayType);
        if (0 != result) {
            Intent rstData = new Intent();
            if (-1 == result) {
                rstData.putExtra(TCConstants.ACTIVITY_RESULT, TCConstants.ERROR_MSG_NOT_QCLOUD_LINK);
            } else {
                rstData.putExtra(TCConstants.ACTIVITY_RESULT, TCConstants.ERROR_MSG_NOT_QCLOUD_LINK);
            }
            AppContext.showToastShort("错误播放");
            mTXCloudVideoView.onPause();
            stopPlay(true);
            finish();
        } else {
            iv_play_cover.setVisibility(View.GONE);
            iv_play_start.setVisibility(View.GONE);
            ll_top.setVisibility(View.VISIBLE);
            ll_control_bottom.setVisibility(View.VISIBLE);
            iv_control_start.setBackgroundResource(R.drawable.icon_record_pause);
            pb_loading.setVisibility(View.VISIBLE);
            mVideoPlay = true;
        }
    }


    //停止播放
    private void stopPlay(boolean clearLastFrame) {
        if (mTXLivePlayer != null) {
            mTXLivePlayer.setPlayListener(null);
            mTXLivePlayer.stopPlay(clearLastFrame);
        }
        mVideoPlay = false;
    }


    //检查播放地址
    private boolean checkPlayUrl() {
        if (TextUtils.isEmpty(mPlayUrl) || (!mPlayUrl.startsWith("http://") && !mPlayUrl.startsWith("https://") && !mPlayUrl.startsWith("rtmp://"))) {
            AppContext.showToastShort("播放地址不合法，目前仅支持rtmp,flv,hls,mp4播放方式!");
            return false;
        }
        if (mIsLivePlay) {
            if (mPlayUrl.startsWith("rtmp://")) {
                mPlayType = TXLivePlayer.PLAY_TYPE_LIVE_RTMP;
            } else if ((mPlayUrl.startsWith("http://"))) {
                mPlayType = TXLivePlayer.PLAY_TYPE_VOD_MP4;
            } else {
                AppContext.showToastShort("播放地址不合法，直播目前仅支持rtmp,flv播放方式!");
                return false;
            }
        } else {
            if (mPlayUrl.startsWith("http://") || mPlayUrl.startsWith("https://")) {
                if (mPlayUrl.contains(".flv")) {
                    mPlayType = TXLivePlayer.PLAY_TYPE_VOD_FLV;
                } else if (mPlayUrl.contains(".m3u8")) {
                    mPlayType = TXLivePlayer.PLAY_TYPE_VOD_HLS;
                } else if (mPlayUrl.toLowerCase().contains(".mp4")) {
                    mPlayType = TXLivePlayer.PLAY_TYPE_VOD_MP4;
                } else {
                    AppContext.showToastShort("播放地址不合法，点播目前仅支持flv,hls,mp4播放方式!");
                    return false;
                }
            } else {
                AppContext.showToastShort("播放地址不合法，点播目前仅支持flv,hls,mp4播放方式!");
                return false;
            }
        }
        return true;
    }

    //全屏点击
    private void clickQuanpin(){
        RelativeLayout.LayoutParams param= (RelativeLayout.LayoutParams)rl_play.getLayoutParams();
        if(param.height!=-1){
            param.height=RelativeLayout.LayoutParams.MATCH_PARENT;
            param.width=RelativeLayout.LayoutParams.MATCH_PARENT;
            rl_play.setLayoutParams(param);
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            if (mTXLivePlayer != null) {
                mTXLivePlayer.setRenderRotation(TXLiveConstants.RENDER_ROTATION_LANDSCAPE);
            }
        }else{
            param.height=heightView;
            param.width=RelativeLayout.LayoutParams.MATCH_PARENT;
            rl_play.setLayoutParams(param);

            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            if (mTXLivePlayer != null) {
                mTXLivePlayer.setRenderRotation(TXLiveConstants.RENDER_ROTATION_PORTRAIT);
            }
        }

    }

    //点击返回或是按返回键
    private void clickBack(){
        RelativeLayout.LayoutParams param= (RelativeLayout.LayoutParams)rl_play.getLayoutParams();
        if(param.height==-1){
            //全屏状态 退出全屏
            param.height=heightView;
            param.width=RelativeLayout.LayoutParams.MATCH_PARENT;
            rl_play.setLayoutParams(param);
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            if (mTXLivePlayer != null) {
                mTXLivePlayer.setRenderRotation(TXLiveConstants.RENDER_ROTATION_PORTRAIT);
            }
        }else{
            finish();
        }
    }


    /**
     * 请求获取AudioFocus
     */
    private void requestAudioFocus() {

        AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        audioManager.requestAudioFocus(this, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);
    }

    /**
     * 释放AudioFocus
     */
    private void abandonAudioFocus() {

        AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        audioManager.abandonAudioFocus(this);
    }


    //音频焦点获取与释放
    @Override
    public void onAudioFocusChange(int focusChange) {
        switch (focusChange) {
            case AudioManager.AUDIOFOCUS_GAIN:

                break;
            case AudioManager.AUDIOFOCUS_LOSS:
                //长时间失去Focus
                if (mVideoPlay&&mTXLivePlayer!=null) {
                    mTXLivePlayer.pause();
                }
                break;
            case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:
                //短时间失去Focus
                if (mVideoPlay&&mTXLivePlayer!=null) {
                    mTXLivePlayer.pause();
                }
                break;
            case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK:
                break;
        }
    }


    @Override
    public void onFullScreenGestureStart() {

    }

    @Override
    public void onFullScreenGestureFinish() {

    }
}

package com.duomizhibo.phonelive.ui;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.drawable.BitmapDrawable;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.OvershootInterpolator;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.duomizhibo.phonelive.widget.AvatarView;
import com.tencent.rtmp.ITXLivePlayListener;
import com.tencent.rtmp.ITXLivePushListener;
import com.tencent.rtmp.TXLiveConstants;
import com.tencent.rtmp.TXLivePlayConfig;
import com.tencent.rtmp.TXLivePlayer;
import com.tencent.rtmp.TXLivePushConfig;
import com.tencent.rtmp.TXLivePusher;
import com.tencent.rtmp.ui.TXCloudVideoView;
import com.duomizhibo.phonelive.AppConfig;
import com.duomizhibo.phonelive.AppContext;
import com.duomizhibo.phonelive.R;
import com.duomizhibo.phonelive.TCConstants;
import com.duomizhibo.phonelive.api.remote.ApiUtils;
import com.duomizhibo.phonelive.api.remote.PhoneLiveApi;
import com.duomizhibo.phonelive.base.ShowLiveActivityBase;
import com.duomizhibo.phonelive.bean.ChatBean;
import com.duomizhibo.phonelive.bean.LiveJson;
import com.duomizhibo.phonelive.bean.SendGiftBean;
import com.duomizhibo.phonelive.bean.SimpleUserInfo;
import com.duomizhibo.phonelive.bean.UserBean;
import com.duomizhibo.phonelive.event.Event;
import com.duomizhibo.phonelive.fragment.GiftListDialogFragment;
import com.duomizhibo.phonelive.interf.ChatServerInterface;
import com.duomizhibo.phonelive.linkmic.TCLinkMicPlayItem;
import com.duomizhibo.phonelive.linkmic.TCLivePlayListenerImpl;
import com.duomizhibo.phonelive.linkmic.TCLivePushListenerImpl;
import com.duomizhibo.phonelive.ui.dialog.LiveCommon;
import com.duomizhibo.phonelive.ui.other.ChatServer;
import com.duomizhibo.phonelive.ui.other.SwipeAnimationController;
import com.duomizhibo.phonelive.utils.DialogHelp;
import com.duomizhibo.phonelive.utils.DpOrSp2PxUtil;
import com.duomizhibo.phonelive.utils.LiveUtils;
import com.duomizhibo.phonelive.utils.MD5;
import com.duomizhibo.phonelive.utils.ShareUtils;
import com.duomizhibo.phonelive.utils.SocketMsgUtils;
import com.duomizhibo.phonelive.utils.StringUtils;
import com.duomizhibo.phonelive.utils.TCUtils;
import com.duomizhibo.phonelive.utils.TDevice;
import com.duomizhibo.phonelive.utils.TLog;
import com.duomizhibo.phonelive.utils.UIHelper;
import com.duomizhibo.phonelive.widget.LoadUrlImageView;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.Timer;
import java.util.Vector;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import jp.wasabeef.glide.transformations.BlurTransformation;
import okhttp3.Call;

/*

* 直播播放页面
* */
public class VideoPlayerActivity extends ShowLiveActivityBase implements View.OnLayoutChangeListener, ITXLivePlayListener, ITXLivePushListener, TCLivePushListenerImpl.ITCLivePushListener, TCLivePlayListenerImpl.ITCLivePlayListener {

    public final static String USER_INFO = "USER_INFO";
    @InjectView(R.id.tv_no_money_black)
    TextView mBlack;
    @InjectView(R.id.video_view)
    protected TXCloudVideoView mTXCloudVideoView;
    //加载中的背景图
    @InjectView(R.id.iv_live_look_loading_bg)
    protected LoadUrlImageView mIvLoadingBg;

    @InjectView(R.id.iv_live_look_loading_bg0)
    protected LoadUrlImageView mIvLoadingBg0;


    @InjectView(R.id.tv_attention)
    protected TextView mIvAttention;

    @InjectView(R.id.iv_stop_rtc)
    protected ImageView mStopLianmai;

    @InjectView(R.id.iv_live_rtc)
    protected ImageView mBtnLinkMic;

    @InjectView(R.id.btn_pingbi)
    protected ImageView mBtnPingbi;

    @InjectView(R.id.rl_sixin)
    protected RelativeLayout mBtnprivatechat;

    @InjectView(R.id.iv_live_toSmall)
    View toSmallBtn;

    @InjectView(R.id.ll_top_menu)
    LinearLayout llTopMenu;

    @InjectView(R.id.ll_yp_labe)
    LinearLayout llYpLabe;

    @InjectView(R.id.iv_live_toBig)
    View toBigBtn;

    @InjectView(R.id.rl_content)
    RelativeLayout mRlContent;


    //私播
    @InjectView(R.id.av_head)
    protected AvatarView av_head;
    @InjectView(R.id.tv_name)
    protected TextView tv_name;
    @InjectView(R.id.tv_state)
    protected TextView tv_state;
    @InjectView(R.id.tv_content)
    protected TextView tv_content;
    @InjectView(R.id.tv_time)
    protected TextView tv_time;  @InjectView(R.id.tv_close)
    protected TextView tv_close;
    @InjectView(R.id.tv_hangup)
    protected ImageView tv_hangup;
    @InjectView(R.id.in_person_dialog)
    protected View in_person_dialog;
    @InjectView(R.id.btn_mohu)
    protected ImageView btn_mohu;

    private boolean isPingbi;

    boolean RequestPermissions = false;

    //主播信息
    public LiveJson mEmceeInfo;

    private String type2_val = "";

    private long mLitLastTime = 0;

    private GiftListDialogFragment mGiftListDialogFragment;

    private SwipeAnimationController mSwipeAnimationController;

    protected boolean mPausing = false;

    public boolean mIsRegisted;

    public boolean mIsConnected;

    boolean isRequst;

    private TXLivePlayer mTXLivePlayer;

    private TXLivePlayConfig mTXPlayConfig = new TXLivePlayConfig();

    private String mPlayUrl = "http://2527.vod.myqcloud.com/2527_000007d04afea41591336f60841b5774dcfd0001.f0.flv";

    private boolean mPlaying = false;

    private int mPlayType = TXLivePlayer.PLAY_TYPE_LIVE_RTMP;

    protected boolean mIsLivePlay = true;

    private boolean mWaitingLinkMicResponse = false;
    private boolean mIsBeingLinkMic = false;

    private ImageView mBtnSwitchCamera;

    private TXLivePusher mTXLivePusher;
    private TXLivePushConfig mTXLivePushConfig;
    private TCLivePushListenerImpl mTCLivePushListener;
    String playUrl;
    String pusherUrl;

    private int koufeiType;
    private String koufeiVal;

    private Vector<TCLinkMicPlayItem> mVecPlayItems = new Vector<>();
    private String mResponseParams;


    boolean isBig;//是否大屏显示
    Dialog dialogCharge;

    ScaleAnimation translateAnimation;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_live_look;
    }

    @Override
    public void initView() {
        super.initView();

        mLiveChat.setVisibility(View.VISIBLE);
        mRoot.addOnLayoutChangeListener(this);
        mTXCloudVideoView.addOnLayoutChangeListener(this);


        mDanmuControl.show();

        mRoot.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (isCanLit) {
                    sendLit(event);
                    showLit(mRandom.nextInt(3));
                }

                return mSwipeAnimationController.processEvent(event);
            }
        });

        mSwipeAnimationController = new SwipeAnimationController(this);

        mSwipeAnimationController.setAnimationView(mRlContent);

        mBtnSwitchCamera = (ImageView) findViewById(R.id.btn_switch_cam);

        mBtnSwitchCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mIsBeingLinkMic) {
                    mTXLivePusher.switchCamera();
                }
            }
        });

        // 连麦推流
        mTXLivePushConfig = new TXLivePushConfig();
        //mTXLivePushConfig.setPauseImg(bitmap);
        mTXLivePushConfig.setPauseFlag(TXLiveConstants.PAUSE_FLAG_PAUSE_VIDEO | TXLiveConstants.PAUSE_FLAG_PAUSE_AUDIO);
        mTXLivePushConfig.setAudioSampleRate(48000);
        mTXLivePushConfig.setBeautyFilter(TCUtils.filtNumber(9, 100, 50), TCUtils.filtNumber(3, 100, 0));

        mTCLivePushListener = new TCLivePushListenerImpl();
        mTCLivePushListener.setListener(this);

        //mTXLivePusher = new TXLivePusher(this);
        //mTXLivePusher.setPushListener(mTCLivePushListener);
        //mTXLivePusher.setConfig(mTXLivePushConfig);
        //mTXLivePusher.setMicVolume(2.0f);

        // 连麦拉流
        mVecPlayItems.add(new TCLinkMicPlayItem(this, this, this, false, 1));
        mVecPlayItems.add(new TCLinkMicPlayItem(this, this, this, false, 2));
        mVecPlayItems.add(new TCLinkMicPlayItem(this, this, this, false, 3));


        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            //横屏逻辑
            mLiveContent.setVisibility(View.GONE);
            mRlLiveGift.setVerticalGravity(View.GONE);
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            mTXCloudVideoView.setLayoutParams(params);
            toSmallBtn.setVisibility(View.VISIBLE);
            toBigBtn.setVisibility(View.GONE);
            //mCameraPreview.setVisibility(View.GONE);
            isBig = true;
        }
    }

    private void startLinkMic() {
        if (mIsBeingLinkMic) {
            return;
        }
        if (mWaitingLinkMicResponse) {
            showToast3("正在申请连麦,请稍后...", 0);
            return;
        }
        showToast3("请求连麦", 0);
        mChatServer.doSendTinkMicRequst(mUser);
        mWaitingLinkMicResponse = true;

//        mHandler.removeCallbacks(mRunnableLinkMicTimeOut);
        //mHandler.postDelayed(mRunnableLinkMicTimeOut, 30000);   //10秒超时
    }


    private synchronized void stopLinkMic() {
        if (mIsBeingLinkMic) {
            mIsBeingLinkMic = false;
            //小主播下麦
            mChatServer.doSendExit(mUser.id, mUser.user_nicename);
        }

        if (mWaitingLinkMicResponse) {
            mWaitingLinkMicResponse = false;
//            if (mHandler != null)
//                mHandler.removeCallbacks(mRunnableLinkMicTimeOut);
        }

        //启用连麦Button
        if (mBtnLinkMic != null) {
            mBtnLinkMic.setBackgroundResource(R.drawable.lianmai);
        }

        //隐藏切换摄像头Button
        if (mBtnSwitchCamera != null) {
            mBtnSwitchCamera.setVisibility(View.INVISIBLE);
        }
        //结束推流
        if (mTXLivePusher != null) {
            mTXLivePusher.stopCameraPreview(true);
            mTXLivePusher.stopPusher();
        }
        //结束拉流
        for (TCLinkMicPlayItem item : mVecPlayItems) {
            item.stopPlay(true);
            item.stopPush();
            item.empty();
        }
    }

    private void handleLinkMicFailed(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
        //结束连麦
        stopLinkMic();

        //重新从CDN拉流播放
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                if (!mPausing && !mIsBeingLinkMic) { //退到后台不用启动拉流
                    startPlay(mEmceeInfo.pull, mPlayType);
                }
            }
        });
    }

    private Runnable mRunnableLinkMicTimeOut = new Runnable() {
        @Override
        public void run() {
            if (koufeiType == 6) {
                showToast3("连接超时...", 0);
                closePlayer();
            }
            if (mWaitingLinkMicResponse) {
                mWaitingLinkMicResponse = false;
                mBtnLinkMic.setBackgroundResource(R.drawable.lianmai);
            }
        }
    };


    @Override
    public void initData() {
        super.initData();

        Bundle bundle = getIntent().getBundleExtra(USER_INFO);
        //获取用户登陆信息
        mUser = AppContext.getInstance().getLoginUser();
        //获取主播信息
        mEmceeInfo = bundle.getParcelable("USER_INFO");

        koufeiType = bundle.getInt("type");
        koufeiVal = bundle.getString("type_val");

        if (koufeiType == 5) {
            mIvLoadingBg0.setVisibility(View.VISIBLE);
            Glide.with(this).load(mUser.avatar_thumb).bitmapTransform(new BlurTransformation(this, 25)).into(mIvLoadingBg0);

        } else {
            Glide.with(this).load(mUser.avatar_thumb).bitmapTransform(new BlurTransformation(this, 25)).into(mIvLoadingBg);
        }

        mStreamName = mEmceeInfo.stream;

        mRoomNum = mEmceeInfo.uid;

        if (StringUtils.toInt(mEmceeInfo.goodnum) != 0) {
            mTvLiveNumber.setText("房间: 靓" + mEmceeInfo.goodnum);
        } else {
            mTvLiveNumber.setText("房间: " + mRoomNum);
        }
        if (koufeiType == 5) {
            mBtnPingbi.setVisibility(View.VISIBLE);
            mBtnLinkMic.setVisibility(View.GONE);
            mBtnprivatechat.setVisibility(View.GONE);
            setSurfaceViewSize();
        }
        this.setVolumeControlStream(AudioManager.STREAM_MUSIC);
        //初始化房间信息
        initRoomInfo();
    }

    protected void startPlay(String mPlayUrl, int playType) {
        if (!checkPlayUrl()) {
            return;
        }
        if (mTXLivePlayer == null) {
            mTXLivePlayer = new TXLivePlayer(this);
        }
//        if (mTXCloudVideoView != null) {
//            mTXCloudVideoView.clearLog();
//        }
        mTXLivePlayer.setPlayerView(mTXCloudVideoView);
        mTXLivePlayer.setRenderRotation(TXLiveConstants.RENDER_ROTATION_PORTRAIT);
        mTXLivePlayer.setRenderMode(TXLiveConstants.RENDER_MODE_FULL_FILL_SCREEN);
        mTXLivePlayer.setPlayListener(this);

        //极速模式
        mTXPlayConfig.setAutoAdjustCacheTime(true);
        mTXPlayConfig.setMinAutoAdjustCacheTime(1);
        mTXPlayConfig.setMaxAutoAdjustCacheTime(1);
        mTXLivePlayer.setConfig(mTXPlayConfig);
        int result;
        result = mTXLivePlayer.startPlay(mPlayUrl, playType);
        if (0 != result) {
            Intent rstData = new Intent();
            if (-1 == result) {
                rstData.putExtra(TCConstants.ACTIVITY_RESULT, TCConstants.ERROR_MSG_NOT_QCLOUD_LINK);
            } else {
                rstData.putExtra(TCConstants.ACTIVITY_RESULT, TCConstants.ERROR_MSG_NOT_QCLOUD_LINK);
            }
            mTXCloudVideoView.onPause();
            stopPlay(true);
            finish();
        } else {
            mPlaying = true;
        }
    }

    private void stopPlay(boolean clearLastFrame) {
        if (mTXLivePlayer != null) {
            mTXLivePlayer.setPlayListener(null);
            mTXLivePlayer.stopPlay(clearLastFrame);
            mPlaying = false;
        }
    }

    private boolean checkPlayUrl() {
        if (TextUtils.isEmpty(mPlayUrl) || (!mPlayUrl.startsWith("http://") && !mPlayUrl.startsWith("https://") && !mPlayUrl.startsWith("rtmp://"))) {
            Toast.makeText(getApplicationContext(), "播放地址不合法，目前仅支持rtmp,flv,hls,mp4播放方式!", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (mIsLivePlay) {
            if (mPlayUrl.startsWith("rtmp://")) {
                mPlayType = TXLivePlayer.PLAY_TYPE_LIVE_RTMP;
            } else if ((mPlayUrl.startsWith("http://") || mPlayUrl.startsWith("https://"))) {
                if (mPlayUrl.contains(".flv")) {
                    mPlayType = TXLivePlayer.PLAY_TYPE_LIVE_FLV;
                } else if (mPlayUrl.contains(".m3u8")) {
                    mPlayType = TXLivePlayer.PLAY_TYPE_VOD_HLS;
                } else if (mPlayUrl.toLowerCase().contains(".mp4")) {
                    mPlayType = TXLivePlayer.PLAY_TYPE_VOD_MP4;
                } else {
                    Toast.makeText(getApplicationContext(), "播放地址不合法，点播目前仅支持flv,hls,mp4播放方式!", Toast.LENGTH_SHORT).show();
                    return false;
                }
            } else {
                Toast.makeText(getApplicationContext(), "播放地址不合法，直播目前仅支持rtmp,flv播放方式!", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(getApplicationContext(), "播放地址不合法，点播目前仅支持flv,hls,mp4播放方式!", Toast.LENGTH_SHORT).show();
                    return false;
                }
            } else {
                Toast.makeText(getApplicationContext(), "播放地址不合法，点播目前仅支持flv,hls,mp4播放方式!", Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        return true;
    }

    private void initRoomInfo() {

        //设置背景图
        mIvLoadingBg.setVisibility(View.VISIBLE);

        mEmceeHead.setAvatarUrl(mEmceeInfo.thumb);

        mEmceeLevel.setImageResource(LiveUtils.getAnchorLevelRes2(mEmceeInfo.level_anchor));

        mPlayUrl = mEmceeInfo.pull;

        requestRoomInfo();

        //初始化直播播放器参数配置
        if (koufeiType != 6) {
            checkPlayUrl();
            startPlay(mPlayUrl, mPlayType);
        } else {
            translateAnimation = setTranste(tv_hangup);
            av_head.setAvatarUrl(mEmceeInfo.avatar);
            tv_name.setText(mEmceeInfo.user_nicename);
            in_person_dialog.setVisibility(View.VISIBLE);
            mBtnLinkMic.setVisibility(View.GONE);
            btn_mohu.setVisibility(View.VISIBLE);
        }
    }

    /**
     * @dw 获取房间信息
     */
    private void requestRoomInfo() {
        //请求服务端获取房间基本信息
        PhoneLiveApi.enterRoom(mUser.id
                , mEmceeInfo.uid
                , mUser.token
                , AppContext.address
                , mEmceeInfo.stream
                , new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(String s, int id) {

                        JSONArray res = ApiUtils.checkIsSuccess(s);

                        if (res != null) {

                            try {
                                JSONObject data = res.getJSONObject(0);

                                //用户列表
                                mUserList.addAll(ApiUtils.formatDataToList2(data.getJSONArray("userlists")
                                        , SimpleUserInfo.class));

                                //房间人数
                                ChatServer.LIVE_USER_NUMS = data.getInt("nums");

                                mTvLiveNum.setText(ChatServer.LIVE_USER_NUMS + "人观看");

                                AppConfig.USERLIST_TIME = data.getInt("userlist_time");

                                AppConfig.LINKMIC_LIMIT = data.getInt("linkmic_limit");

                                //弹幕价格
                                barrageFee = data.getInt("barrage_fee");
                                //映票数量
                                mTvYpNum.setText(data.getString("votestotal"));

                                TLog.error("votestotal" + data.getString("votestotal"));
                                LiveUtils.sortUserList(mUserList);
                                mUserListAdapter.setUserList(mUserList);

                                if (data.getInt("isattention") == 0) {
                                    mIvAttention.setVisibility(View.VISIBLE);

                                } else {
                                    mIvAttention.setVisibility(View.GONE);

                                }
                                connectToSocketService(data.getString("chatserver"));

                                if (koufeiType == 6) {
                                    mIsBeingLinkMic = true;
                                    mChatServer.doSendTinkMicRequst(mUser);
//                                    mHandler.postDelayed(mRunnableLinkMicTimeOut, 30000);
                                }
                                goodNum = data.getJSONObject("liang").getString("name");
                                vipType = data.getJSONObject("vip").getString("type");
                                //禁言时间
                                AppConfig.SHUTUP_TIME = data.getString("shut_time");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }else {
                            if (koufeiType==6){
                                closePlayer();
                            }
                        }
                    }
                });
    }


    @OnClick({R.id.ll_time,R.id.btn_mohu, R.id.tv_hangup, R.id.iv_live_toSmall, R.id.iv_live_toBig, R.id.btn_pingbi, R.id.iv_stop_rtc, R.id.iv_live_meiyan, R.id.iv_live_camera_control, R.id.iv_live_rtc, R.id.iv_live_emcee_head, R.id.tglbtn_danmu_setting, R.id.iv_live_shar, R.id.iv_live_privatechat, R.id.iv_live_back, R.id.ll_yp_labe, R.id.ll_live_room_info, R.id.iv_live_chat, R.id.iv_live_look_loading_bg, R.id.bt_send_chat, R.id.iv_live_gift, R.id.tv_attention})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_live_emcee_head:
                showUserInfoDialog(LiveUtils.getSimleUserInfo(mEmceeInfo));
                break;
            case R.id.iv_live_shar:
                showSharePopWindow(VideoPlayerActivity.this, v, LiveUtils.getSimleUserInfo(mEmceeInfo));
                break;
            //私信
            case R.id.iv_live_privatechat:
                showPrivateChat();
                break;
            //退出直播间
            case R.id.iv_live_back:
                back();
                break;
            //票数排行榜
            case R.id.ll_yp_labe:
                OrderWebViewActivity.startOrderWebView(this, mEmceeInfo.uid);
                break;
            //发言框
            case R.id.iv_live_chat:
                changeEditStatus(true);
                break;
            //开启关闭弹幕
            case R.id.tglbtn_danmu_setting:
                openOrCloseDanMu();
                break;
            case R.id.bt_send_chat:
                //等待优化，可能会造成卡顿
                mUser = AppContext.getInstance().getLoginUser();
                //弹幕判断 HHH
                if (mDanMuIsOpen) {
                    sendBarrage();
                } else {
                    sendChat();
                }
                break;
            case R.id.iv_live_look_loading_bg:
                changeEditStatus(false);
                break;
            case R.id.iv_live_gift:
                if (mGiftListDialogFragment == null) {
                    mGiftListDialogFragment = new GiftListDialogFragment();
                }
                mGiftListDialogFragment.show(getSupportFragmentManager(), "GiftListDialogFragment");
                break;
            case R.id.ll_live_room_info://左上角点击主播信息
                showUserInfoDialog(LiveUtils.getSimleUserInfo(mEmceeInfo));
                break;
            case R.id.tv_attention:
                //关注主播
                PhoneLiveApi.showFollow(mUser.id, mEmceeInfo.uid, AppContext.getInstance().getToken(), new PhoneLiveApi.AttentionCallback() {
                    @Override
                    public void callback(boolean isAttention) {
                        mIvAttention.setVisibility(View.GONE);
                        showToast2("关注成功");
                    }
                });
                mChatServer.doSendSystemMessage(mUser.user_nicename + "关注了主播", mUser);
                break;
            case R.id.iv_live_rtc:
                if (StringUtils.toInt(mUser.level) >= AppConfig.LINKMIC_LIMIT) {
                    if (mIsBeingLinkMic == false) {
                        startLinkMic();
                    } else {
                        stopLinkMic();
                        mPlayType = TXLivePlayer.PLAY_TYPE_VOD_FLV;
                        startPlay(mEmceeInfo.pull, mPlayType);
                    }
                } else {
                    showToast3("您的等级不足，无法使用此功能！", 0);
                }

                break;
            case R.id.iv_live_camera_control:
                showSettingPopUp(v);
                break;
            case R.id.iv_live_meiyan:
                break;
            case R.id.iv_stop_rtc:
                break;
            case R.id.btn_pingbi:
                if (isPingbi) {
                    mBtnPingbi.setBackgroundResource(R.drawable.nopingbi);
                    isPingbi = false;
                    mRlLiveGift.setVisibility(View.VISIBLE);
                    mLvChatList.setVisibility(View.VISIBLE);
                    mDanmakuView.setVisibility(View.VISIBLE);
                    mShowGiftAnimator.setVisibility(View.VISIBLE);
                    showToast3("已开启所有消息和礼物特效", 0);
                } else {
                    showToast3("已屏蔽所有消息和礼物特效", 0);
                    mBtnPingbi.setBackgroundResource(R.drawable.pingbi);
                    isPingbi = true;
                    mRlLiveGift.setVisibility(View.GONE);
                    mLvChatList.setVisibility(View.GONE);
                    mDanmakuView.setVisibility(View.GONE);
                    mShowGiftAnimator.setVisibility(View.GONE);
                }
                break;
            case R.id.iv_live_toBig:
                if (getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                }
                break;
            case R.id.iv_live_toSmall:
                if (getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                }
                break;
            case R.id.tv_hangup:
                if (!isClose) {
                    mChatServer.doSendExit(mUser.id, mUser.user_nicename);
                }
                closePlayer();
                break;
            case R.id.btn_mohu:
                if (isMohu) {
                    isMohu = false;
                    btn_mohu.setBackgroundResource(R.drawable.visiable);
                } else {
                    isMohu = true;
                    btn_mohu.setBackgroundResource(R.drawable.unvisiable);
                }
                mChatServer.dosendMoHu("10");
                break;
            case R.id.ll_time:
                back();
                break;
            default:
                break;
        }
    }

    boolean isMohu;
    boolean isClose;

    private void back() {
        if (koufeiType == 6) {
            isClose = true;
            tv_content.setVisibility(View.GONE);
            tv_time.setVisibility(View.VISIBLE);
            tv_time.setText("本次私播时长\n" + chronometer.getText());
            tv_state.setText("私播已结束");
            tv_close.setText("关闭");
            tv_hangup.setImageResource(R.drawable.close);
            in_person_dialog.setVisibility(View.VISIBLE);
            chronometer.stop();
            videoPlayerEnd();
        } else {
            closePlayer();
        }

    }


    @Override
    public void onBackPressed() {
        back();
        super.onBackPressed();
    }

    private void closePlayer() {
        videoPlayerEnd();
        finish();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 5: {
                // 判断权限请求是否通过
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults.length > 0 && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    if (!isRequst) {
                        if (!mIsConnected && mIsRegisted) {
                            showToast3("请求连麦", 0);
                            isRequst = true;
                        }
                    } else {
                        showToast3("正在请求连麦，请稍后...", 0);
                    }
                    return;
                }
                if (grantResults.length > 0 && grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    showToast3("您已拒绝使用摄像头权限,无法连麦,请去设置中修改", 0);
                    RequestPermissions = false;
                }

                return;
            }
        }
    }

    //分享操作
    public void share(View v) {
        ShareUtils.share(this, v.getId(), LiveUtils.getSimleUserInfo(mEmceeInfo));
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            //横屏逻辑
            mLiveContent.setVisibility(View.GONE);
            mRlLiveGift.setVisibility(View.GONE);

            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            mTXCloudVideoView.setLayoutParams(params);
            mIvLoadingBg.setLayoutParams(params);
            toSmallBtn.setVisibility(View.VISIBLE);
            toBigBtn.setVisibility(View.GONE);
            //mCameraPreview.setVisibility(View.GONE);
            isBig = true;
        }
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            //竖屏逻辑
            toSmallBtn.setVisibility(View.GONE);
            toBigBtn.setVisibility(View.VISIBLE);
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, DpOrSp2PxUtil.sp2pxConvertInt(VideoPlayerActivity.this, 200));
            params.setMargins(0, DpOrSp2PxUtil.dp2pxConvertInt(VideoPlayerActivity.this, 120), 0, 0);
            mTXCloudVideoView.setLayoutParams(params);
            mIvLoadingBg.setLayoutParams(params);

            params = new RelativeLayout.LayoutParams((int) TDevice.dpToPixel(30), (int) TDevice.dpToPixel(30));
            params.setMargins((int) TDevice.getScreenWidth() - (int) TDevice.dpToPixel(50), DpOrSp2PxUtil.dp2pxConvertInt(VideoPlayerActivity.this, 270), (int) TDevice.dpToPixel(10), 0);
            toBigBtn.setLayoutParams(params);

            mLiveContent.setVisibility(View.VISIBLE);
            mRlLiveGift.setVisibility(View.VISIBLE);
            mIvLoadingBg0.setVisibility(View.VISIBLE);
            isBig = false;
        }
    }

    private void setSurfaceViewSize() {
        //1是pc,0是手机
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) TDevice.dpToPixel(200));
            params.setMargins(0, (int) TDevice.dpToPixel(120), 0, 0);
            mTXCloudVideoView.setLayoutParams(params);
            mIvLoadingBg.setLayoutParams(params);
            mIvLoadingBg.setBackgroundResource(R.drawable.game_room_loading_land);
            mIvLoadingBg0.setVisibility(View.VISIBLE);
            toBigBtn.setVisibility(View.VISIBLE);
            params = new RelativeLayout.LayoutParams((int) TDevice.dpToPixel(40), (int) TDevice.dpToPixel(40));
            params.setMargins((int) TDevice.getScreenWidth() - (int) TDevice.dpToPixel(50), DpOrSp2PxUtil.dp2pxConvertInt(VideoPlayerActivity.this, 270), (int) TDevice.dpToPixel(10), 0);
            toBigBtn.setLayoutParams(params);
        }
    }

    //弹幕发送
    @Override
    protected void sendBarrageOnResponse(String response) {
        JSONArray s = ApiUtils.checkIsSuccess(response);
        if (s != null) {
            try {
                JSONObject tokenJson = s.getJSONObject(0);
                mUser.coin = tokenJson.getString("coin");
                mUser.level = tokenJson.getString("level");
                mChatServer.doSendBarrage(tokenJson.getString("barragetoken"), mUser);
                mChatInput.setText("");
                mChatInput.setHint("开启大喇叭，" + barrageFee + AppConfig.CURRENCY_NAME + "/条");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    //更新ui
    private void connectToSocketService(String chatUrl) {
        //连接socket服务器
        try {
            mChatServer = new ChatServer(new ChatListenUIRefresh(), this, chatUrl);
            mChatServer.connectSocketServer(mUser, mEmceeInfo.stream, mEmceeInfo.uid);//连接到socket服务端

            if (koufeiType == 3 || koufeiType == 2) {
                mChatServer.doSendUpdateCoin(koufeiVal, mUser.id, "1");
            }
        } catch (URISyntaxException e) {
            e.printStackTrace();
            TLog.log("connect error");
        }
    }

    @Override
    public void onPlayEvent(int i, Bundle bundle) {
        if (i == TXLiveConstants.PLAY_EVT_PLAY_BEGIN) {
            if (koufeiType == 3 ) {
                mHandler.postDelayed(perMCRunnable, 60 * 1000);
            }
            mIvLoadingBg.setVisibility(View.GONE);
        }

    }


    @Override
    public void onPushEvent(int i, Bundle bundle) {
        if (i == TXLiveConstants.PUSH_EVT_PUSH_BEGIN) {

        }
    }

    @Override
    public void onNetStatus(Bundle status) {
        if (status.getInt(TXLiveConstants.NET_STATUS_VIDEO_WIDTH) > status.getInt(TXLiveConstants.NET_STATUS_VIDEO_HEIGHT)) {
            if (mTXLivePlayer != null)
                mTXLivePlayer.setRenderRotation(TXLiveConstants.RENDER_ROTATION_LANDSCAPE);
        } else if (mTXLivePlayer != null)
            mTXLivePlayer.setRenderRotation(TXLiveConstants.RENDER_ROTATION_PORTRAIT);
    }

    @Override
    public void onLivePlayEvent(String playUrl, int event, Bundle bundle) {
        TCLinkMicPlayItem item = getPlayItemByPlayUrl(playUrl);
        if (item == null) {
            return;
        }
        if (event == TXLiveConstants.PLAY_EVT_PLAY_BEGIN) {
            //开始拉流，或者拉流失败，结束loading
            item.stopLoading();
        } else if (event == TXLiveConstants.PLAY_ERR_NET_DISCONNECT || event == TXLiveConstants.PLAY_EVT_PLAY_END || event == TXLiveConstants.PLAY_ERR_GET_RTMP_ACC_URL_FAIL) {
            if (item.mUserID.equalsIgnoreCase(mRoomNum)) {
                handleLinkMicFailed("主播的流拉取失败，结束连麦");
            } else {
                stopPlayVideoStream(item.mUserID);
            }
        }
    }

    @Override
    public void onLivePlayNetStatus(String playUrl, Bundle status) {

    }

    @Override
    public void onLivePushEvent(String pushUrl, int event, Bundle bundle) {
        if (event == TXLiveConstants.PUSH_EVT_PUSH_BEGIN && mIsBeingLinkMic) {
            //以下为正常流程  待优化

            //开始推流事件通知
//            mChatServer.doSendTinkMicPlayUrl(mUser, playUrl);
            // 2.拉取主播的低时延流
//            PhoneLiveApi.requestPlayUrlWithSignForLinkMic(mRoomNum, mEmceeInfo.pull, new StringCallback() {
//                @Override
//                public void onError(Call call, Exception e, int id) {
//
//                }
//                @Override
//                public void onResponse(String response, int id) {
//                    JSONArray res = ApiUtils.checkIsSuccess(response);
//                    if (res != null) {
//                        try {
//                            mPlayUrl = res.getJSONObject(0).getString("streamUrlWithSignature");
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                        startPlay();
//                    }
//                }
//            });

//            // 2.拉取主播的低时延流
//            PhoneLiveApi.requestPlayUrlWithSignForLinkMic(mRoomNum, mEmceeInfo.pull, new StringCallback() {
//                @Override
//                public void onError(Call call, Exception e, int id) {
//
//                }
//                @Override
//                public void onResponse(String response, int id) {
//                    JSONArray res = ApiUtils.checkIsSuccess(response);
//                    if (res != null) {
//                        String strPlayUrl = null;
//                        try {
//                            strPlayUrl = res.getJSONObject(0).getString("streamUrlWithSignature");
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                        if (mIsBeingLinkMic) {
//                            TCLinkMicPlayItem item = getPlayItemByUserID(mRoomNum);
//                            if (item != null) {
//                                item.mPlayUrl = strPlayUrl;
//                                if (!mPausing) {
//                                    item.startPlay(strPlayUrl);
//                                }
//                            }
//                        }
//                    }
//                }
//            });
//            try {
//                JSONArray jsonArray = new JSONArray(mResponseParams);
//                for (int i = 0; i < jsonArray.length(); ++i) {
//                    JSONObject item = jsonArray.getJSONObject(i);
//                    if (item != null) {
//                        startPlayVideoStream(item.getString("userid"), item.getString("playurl"));
//                    }
//                }
//            }
//            catch (Exception e) {
//                e.printStackTrace();
//            }

        } else if (event == TXLiveConstants.PUSH_ERR_NET_DISCONNECT) {            //推流失败事件通知
            handleLinkMicFailed("推流失败，结束连麦");
        } else if (event == TXLiveConstants.PUSH_ERR_OPEN_CAMERA_FAIL) {       //未获得摄像头权限
            handleLinkMicFailed("未获得摄像头权限，结束连麦");
        } else if (event == TXLiveConstants.PUSH_ERR_OPEN_MIC_FAIL) {           //未获得麦克风权限
            if (mIsBeingLinkMic) {
                handleLinkMicFailed("未获得麦克风权限，结束连麦");
            }
        }
    }

    private void startPlayVideoStream(String userID, String playUrl) {
        if (userID == null || userID.length() == 0 || playUrl == null || playUrl.length() == 0) {
            return;
        }
        boolean bExist = false;
        for (TCLinkMicPlayItem item : mVecPlayItems) {
            if (userID.equalsIgnoreCase(item.mUserID)/* || playUrl .equalsIgnoreCase(item.mPlayUrl)*/) {
                bExist = true;
                break;
            }
        }
        if (bExist == false) {
            for (TCLinkMicPlayItem item : mVecPlayItems) {
                if (item.mUserID == null || item.mUserID.length() == 0) {
                    item.mUserID = userID;
                    item.startLoading();
                    item.mPlayUrl = playUrl;
                    if (!mPausing) {
                        item.startPlay(playUrl);
                    }
                    break;
                }
            }
        }
    }

    private void stopPlayVideoStream(String userID) {
        TCLinkMicPlayItem item = getPlayItemByUserID(userID);
        if (item != null) {
            item.stopPlay(true);
            item.empty();
        }
    }

    @Override
    public void onLivePushNetStatus(String pushUrl, Bundle status) {

    }

    //socket客户端事件监听处理start
    private class ChatListenUIRefresh implements ChatServerInterface {

        @Override
        public void onMessageListen(final SocketMsgUtils socketMsg, final int type, final ChatBean c) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (type == 1) {
                        addDanmu(c);
                    } else if (type == 2) {

                        if (StringUtils.toInt(socketMsg.getRetcode()) == 409002) {
                            showToast3("你已经被禁言", 0);
                            return;
                        }
                        addChatMessage(c);
                    }
                }
            });
        }

        @Override
        public void onConnect(final boolean res) {
            //连接结果
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    onConnectRes(res);
                }
            });
        }

        //用户状态改变
        @Override
        public void onUserStateChange(SocketMsgUtils socketMsg, final UserBean user, final boolean state) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    onUserStatusChange(user, state);
                }
            });

        }

        //主播关闭直播
        @Override
        public void onSystemNot(final int code) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (koufeiType == 6) {
                        isClose = true;
                        tv_content.setVisibility(View.GONE);
                        tv_time.setVisibility(View.VISIBLE);
                        tv_time.setText("本次私播时长\n" + chronometer.getText());
                        tv_state.setText("私播已结束");
                        tv_hangup.setImageResource(R.drawable.close);
                        in_person_dialog.setVisibility(View.VISIBLE);
                        chronometer.stop();
                    } else {
                        showLiveEndDialog(mUser.id, 0, "");
                    }
                    videoPlayerEnd();
                }
            });

        }

        //送礼物展示
        @Override
        public void onShowSendGift(final SendGiftBean giftInfo, final ChatBean chatBean) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    showGiftInit(giftInfo);
                    addChatMessage(chatBean);
                }
            });

        }


        //特权操作
        @Override
        public void onPrivilegeAction(final SocketMsgUtils socketMsg, final ChatBean c) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    if (socketMsg.get2Uid().equals(mUser.id)) {

                        //禁言
                        if (socketMsg.getAction().equals("1")) {

                            changeEditStatus(false);

                        } else if (socketMsg.getAction().equals("2")) {
                            //踢人
                            videoPlayerEnd();

                            AlertDialog alertDialog = DialogHelp.getMessageDialog(VideoPlayerActivity.this, "您已被踢出房间",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            closePlayer();
                                        }
                                    })
                                    .create();
                            alertDialog.setCancelable(false);
                            alertDialog.setCanceledOnTouchOutside(false);

                            alertDialog.show();
                        } else if (socketMsg.getAction().equals("13")) {

                            DialogHelp.getMessageDialog(VideoPlayerActivity.this, socketMsg.getCt()).show();
                        }
                    }
                    addChatMessage(c);
                }
            });
        }

        //点亮
        @Override
        public void onLit(SocketMsgUtils socketMsg) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (isCanShowLit) {
                        showLit();
                    }
                }
            });
        }

        //添加僵尸粉丝
        @Override
        public void onAddZombieFans(final SocketMsgUtils socketMsg) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    addZombieFans(socketMsg.getCt());
                }
            });
        }

        //服务器连接错误
        @Override
        public void onError() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    AppContext.showToastAppMsg(VideoPlayerActivity.this, "服务器连接错误");
                }
            });
        }

        @Override
        public void onCharge(final SocketMsgUtils socketMsg) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (StringUtils.toInt(socketMsg.getAction()) == 1) {
                        if (dialogCharge != null) {
                            dialogCharge.dismiss();
                        }
                        mTXLivePlayer.pause();
                        mIvLoadingBg.setVisibility(View.VISIBLE);
                        koufeiVal = socketMsg.getParam("type_val", "");
                        showDialog("本房间为计时房间，每分钟需支付" + koufeiVal + AppConfig.CURRENCY_NAME);
                    }
                }
            });
        }

        @Override
        public void onUpdateCoin(final SocketMsgUtils msgUtils) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (msgUtils.getParam("isfirst", "").equals("0")) {
                        mTvYpNum.setText(StringUtils.toInt(mTvYpNum.getText().toString()) + StringUtils.toInt(msgUtils.getParam("votes", "")) + "");
                    } else if (msgUtils.getParam("isfirst", "").equals("1")) {
                        if (!msgUtils.getUid().equals(mUser.id)) {
                            mTvYpNum.setText(StringUtils.toInt(mTvYpNum.getText().toString()) + StringUtils.toInt(msgUtils.getParam("votes", "")) + "");
                        }
                    }
                }
            });
        }

        @Override
        public void onGameNotice(SocketMsgUtils socketMsg) {
            if (StringUtils.toInt(socketMsg.getAction()) == 2) {
                mIvLoadingBg.setVisibility(View.VISIBLE);
                mIvLoadingBg.setBackgroundResource(R.drawable.game_room_loading_land);
            } else {
                mIvLoadingBg.setVisibility(View.GONE);
            }
        }

        @Override
        public void onLinkMic(final SocketMsgUtils socketMsg) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (StringUtils.toInt(socketMsg.getAction()) == 2) {
                        mResponseParams = socketMsg.getParam("playitems", "");
//                        if (mHandler != null)
//                            mHandler.removeCallbacks(mRunnableLinkMicTimeOut);
                        try {
                            JSONArray jsonArray = new JSONArray(mResponseParams);
                            if (jsonArray.length() >= 3) {
                                if (mUser.id.equals(socketMsg.getParam("touid", ""))) {
                                    mWaitingLinkMicResponse = false;
                                    showToast3("连麦人数已满", 0);
                                    mBtnLinkMic.setBackgroundResource(R.drawable.lianmai);

                                }
                                return;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if (socketMsg.getParam("touid", "").equals(mUser.id)) {
                            Toast.makeText(VideoPlayerActivity.this, "主播接受了您的连麦请求，开始连麦", Toast.LENGTH_SHORT).show();
                            PhoneLiveApi.requestLVBAddrForLinkMic(mUser.id, new StringCallback() {
                                @Override
                                public void onError(Call call, Exception e, int id) {
                                    TLog.error("onErrorrequestLVBAddrForLinkMic");
                                }

                                @Override
                                public void onResponse(String response, int id) {
                                    JSONArray res = ApiUtils.checkIsSuccess(response);
                                    if (res != null) {
                                        try {
                                            mIsBeingLinkMic = true;
                                            mWaitingLinkMicResponse = false;
                                            mBtnLinkMic.setBackgroundResource(R.drawable.duanmai);
                                            playUrl = res.getJSONObject(0).getString("playurl");
                                            pusherUrl = res.getJSONObject(0).getString("pushurl");
                                        } catch (Exception e) {
                                        }
                                        if (koufeiType == 6) {
                                            cost();
                                        } else {
                                            lianmai();
                                        }

                                    } else {
                                        Toast.makeText(getApplicationContext(), "拉取连麦推流地址失败，error=", Toast.LENGTH_SHORT).show();
                                        mIsBeingLinkMic = false;
                                        mWaitingLinkMicResponse = false;
                                        // mBtnLinkMic.setEnabled(true);
                                        mBtnLinkMic.setBackgroundResource(R.drawable.lianmai);
//                                        if (mHandler != null)
//                                            mHandler.removeCallbacks(mRunnableLinkMicTimeOut);
                                    }
                                }
                            });
                        }
                    } else if (StringUtils.toInt(socketMsg.getAction()) == 3) {
                        if (socketMsg.getParam("touid", "").equals(mUser.id)) {
                            Toast.makeText(VideoPlayerActivity.this, "主播拒绝了您的连麦请求", Toast.LENGTH_SHORT).show();
                            mIsBeingLinkMic = false;
                            mWaitingLinkMicResponse = false;
                            //mHandler.removeCallbacks(mRunnableLinkMicTimeOut);
//                            mBtnLinkMic.setEnabled(true);
                            if (koufeiType == 6) {
                                mChatServer.doSendExit(mUser.id, mUser.user_nicename);
                                closePlayer();
                            }
                            mBtnLinkMic.setBackgroundResource(R.drawable.lianmai);
                        }
                    } else if (StringUtils.toInt(socketMsg.getAction()) == 6) {
                        if (socketMsg.getParam("touid", "").equals(mUser.id)) {
                            //结束连麦
                            stopLinkMic();
                            if (koufeiType == 6) {
                                showToast3("主播断开连麦...", 0);
                                back();
                            }
                            //重新从CDN拉流播放
                            mPlayType = TXLivePlayer.PLAY_TYPE_VOD_FLV;
                            startPlay(mEmceeInfo.pull, mPlayType);
                        }
                    } else if (socketMsg.getAction().equals("4")) {
                        //正在连麦时，收到其他小主播的流
                        if (mIsBeingLinkMic) {
                            if (!socketMsg.getUid().equals(mUser.id)) {
                                startPlayVideoStream(socketMsg.getUid(), socketMsg.getParam("playurl", ""));
                            }
                        }
                    } else if (socketMsg.getAction().equals("5")) {

                        if (mIsBeingLinkMic) {
                            TCLinkMicPlayItem item = getPlayItemByUserID(socketMsg.getUid());
                            if (item != null) {
                                if (socketMsg.getUid().equals(mUser.id)) {
                                    mIsBeingLinkMic = false;
                                    mWaitingLinkMicResponse = false;
                                    //mBtnLinkMic.setEnabled(true);
                                    mBtnLinkMic.setBackgroundResource(R.drawable.lianmai);
//                                    if (mHandler != null)
//                                        mHandler.removeCallbacks(mRunnableLinkMicTimeOut);
                                }
                                mVecPlayItems.remove(item.mUserID);
                                item.stopPlay(true);
                                item.empty();
                            }

                        }

                    } else if (socketMsg.getAction().equals("8")) {
                        if (mUser.id.equals(socketMsg.getParam("touid", ""))) {
                            showToast3("主播未响应", 0);
                            if (mWaitingLinkMicResponse) {
                                mWaitingLinkMicResponse = false;
                                mBtnLinkMic.setBackgroundResource(R.drawable.lianmai);
                            }
                            if (koufeiType==6){
                                closePlayer();
                            }
                        }
                    } else if (socketMsg.getAction().equals("7")) {
                        if (mUser.id.equals(socketMsg.getParam("touid", ""))) {
                            showToast3("主播正忙碌", 0);
                            if (mWaitingLinkMicResponse) {
                                mWaitingLinkMicResponse = false;
                                mBtnLinkMic.setBackgroundResource(R.drawable.lianmai);
                            }
                        }

                    }
                }
            });


        }


    }


    //直播结束释放资源
    private void videoPlayerEnd() {

        if (mIsBeingLinkMic) {
            stopLinkMic();
        }
        if (mTXLivePlayer != null) {
            mTXLivePlayer.setMute(true);
        }

        if (mWaitingLinkMicResponse) {
            mChatServer.doSendRes("9", mUser.id);
        }
        if (mShowGiftAnimator != null) {
            mShowGiftAnimator.removeAllViews();
        }

        if (mGiftListDialogFragment != null) {
            mGiftListDialogFragment.dismissAllowingStateLoss();
        }

        if (mButtonMenuFrame != null && mLvChatList != null) {
            mButtonMenuFrame.setVisibility(View.GONE);//隐藏菜单栏
            mLvChatList.setVisibility(View.GONE);
        }

        if (mChatServer != null) {
            mChatServer.close();
        }

        if (null != mHandler) {
            mHandler.removeCallbacksAndMessages(null);
            mHandler = null;
        }

        if (mRoot != null && mGiftView != null) {
            mRoot.removeView(mGiftView);
        }

        if (mDanmuControl != null) {
            mDanmuControl.hide();//关闭弹幕 HHH
        }

        if (mTXCloudVideoView != null) {
            mTXCloudVideoView.onDestroy();
            mTXCloudVideoView = null;
        }
        stopPlay(true);
        mTXLivePlayer = null;

        for (TCLinkMicPlayItem item : mVecPlayItems) {
            item.destroy();
        }

        if (mTXLivePusher != null) {
            mTXLivePusher.setPushListener(null);
        }

        if (mTCLivePushListener != null) {
            mTCLivePushListener.setListener(null);
        }

    }

    //切换房间释放资源
    private void switchRoomRelease() {
        mLitLastTime = 0;
        mGiftShowQueue.clear();
        mLuxuryGiftShowQueue.clear();
        mListChats.clear();
        mShowGiftAnimator.removeAllViews();
        mDanMuIsOpen = false;
        mBtnDanMu.setBackgroundResource(R.drawable.tanmubutton);
        if (mGiftView != null) {
            mRoot.removeView(mGiftView);
        }
        if (mGiftListDialogFragment != null) {
            mGiftListDialogFragment.dismiss();
        }
        mDanmuControl.hide();
        if (mChatServer != null) {
            mChatServer.close();
        }
        if (null != mHandler) {
            mHandler.removeCallbacksAndMessages(null);
        }
    }


    /**
     * @dw 当每个聊天被点击显示该用户详细信息弹窗
     */
    public void chatListItemClick(ChatBean chat) {
        if (chat.getType() != 13) {

            showUserInfoDialog(chat.mSimpleUserInfo);
        }
    }


    //点亮
    private void sendLit(MotionEvent event) {
        isCanLit = false;
        //按下并且当前屏幕不是清屏状态下
        if (event.getAction() == MotionEvent.ACTION_DOWN && !(mLiveContent.getLeft() > 10)) {
            int index = mRandom.nextInt(3);
            if (mLitLastTime == 0 || (System.currentTimeMillis() - mLitLastTime) > 500) {
                if (mLitLastTime == 0) {
                    //第一次点亮请求服务端纪录
                    //PhoneLiveApi.showLit(mUser.id, mUser.token, mEmceeInfo.uid);
                    if (mChatServer != null) {
                        mChatServer.doSendLitMsg(mUser, index, goodNum, vipType);
                    }
                }
                mLitLastTime = System.currentTimeMillis();
                if (mChatServer != null) {
                    mChatServer.doSendLit(index);
                }

            } else {
                showLit(mRandom.nextInt(3));
            }
        }
        if (mHandler == null) return;
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                isCanLit = true;
            }
        }, 1000);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(Event.VideoEvent event) {

        if (event.action == 0 && mChatServer != null) {

            mUser = AppContext.getInstance().getLoginUser();
            //送礼物
            mChatServer.doSendGift(event.data[0], mUser, event.data[1], goodNum, vipType);

        } else if (event.action == 1 && mChatServer != null) {

            //关注
            mChatServer.doSendSystemMessage(mUser.user_nicename + "关注了主播", mUser);
            mIvAttention.setVisibility(View.GONE);
        }

    }


    @Override
    public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
        if (v.getId() == R.id.video_view) {
            if (bottom != 0) {
                //防止聊天软键盘挤压屏幕导致视频变形
                //mVideoSurfaceView.setVideoDimension(mScreenWidth,mScreenHeight);
            }
        } else if (v.getId() == R.id.rl_live_root) {
            if (bottom > oldBottom) {
                //如果聊天窗口开启,收起软键盘时关闭聊天输入框
                changeEditStatus(false);
            }
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
        Log.e("VideoPlayerActivity", "onStart: -------->");
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
        Log.e("VideoPlayerActivity", "onStop: -------->");
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (mTXCloudVideoView != null) {
            mTXCloudVideoView.onResume();
        }
        for (TCLinkMicPlayItem item : mVecPlayItems) {
            item.resume();
            if (!TextUtils.isEmpty(item.mPlayUrl) && !TextUtils.isEmpty(item.mUserID)) {
                item.startLoading();
                item.startPlay(item.mPlayUrl);
            }
        }

        if (mIsBeingLinkMic) {
            if (mTXLivePusher != null) {
                mTXLivePusher.resumePusher();
            }
            mPausing = false;
        }
        if (mTXLivePlayer != null) {
            mTXLivePlayer.resume();
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mTXCloudVideoView != null) {
            mTXCloudVideoView.onPause();
        }
        for (TCLinkMicPlayItem item : mVecPlayItems) {
            item.pause();
        }
        if (mTXLivePlayer != null) {
            mTXLivePlayer.pause();
        }
        Log.e("VideoPlayerActivity", "onPause: -------->");
    }

    @Override
    protected void onDestroy() {//释放
        super.onDestroy();
        //解除广播
        ButterKnife.reset(this);
        Log.e("VideoPlayerActivity", "onDestroy: -------->");
    }

    private void showDialog(String data) {
        mHandler.removeCallbacks(perMCRunnable);
        dialogCharge = new Dialog(VideoPlayerActivity.this, R.style.dialog);
        dialogCharge.setContentView(R.layout.dialog_show_rtcmsg);
        dialogCharge.setCanceledOnTouchOutside(false);
        dialogCharge.setCancelable(false);
        dialogCharge.show();
        ((TextView) dialogCharge.findViewById(R.id.tv_title)).setText("提示");
        dialogCharge.findViewById(R.id.btn_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    closePlayer();
                } catch (Exception e) {
                    dialogCharge.dismiss();
                }
                dialogCharge.dismiss();
            }
        });
        TextView textView = (TextView) dialogCharge.findViewById(R.id.et_input);
        textView.setText(data);
        dialogCharge.findViewById(R.id.btn_confirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                roomCharge();
                dialogCharge.dismiss();
            }
        });
    }

    Runnable perMCRunnable = new Runnable() {
        @Override
        public void run() {
            perMinuteCharge();
        }
    };

    private void roomCharge() {
        PhoneLiveApi.requestCharging(mUser.id, mUser.token,
                mEmceeInfo.uid, mEmceeInfo.stream, new StringCallback() {

                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(String response, int id) {
                        JSONArray res = ApiUtils.checkIsSuccess(response);
                        if (res != null) {
                            try {
                                if (mIvLoadingBg.getVisibility() == View.VISIBLE) {
                                    startPlay(mPlayUrl, mPlayType);
                                    mIvLoadingBg.setVisibility(View.GONE);
                                }
                                mChatServer.doSendUpdateCoin(koufeiVal, mUser.id, "0");
                                mHandler.postDelayed(perMCRunnable, 60 * 1000);
                                mUser.coin = res.getJSONObject(0).getString("coin");
                                AppContext.getInstance().saveUserInfo(mUser);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        } else {
                            mHandler.removeCallbacks(perMCRunnable);
                            mTXLivePlayer.pause();
                            mIvLoadingBg.setVisibility(View.VISIBLE);
                            showBalanceLow();
                        }
                    }
                });
    }

    private void perMinuteCharge() {
        PhoneLiveApi.timeCharge(mUser.id, mUser.token,
                mEmceeInfo.uid, mEmceeInfo.stream, new StringCallback() {

                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(String response, int id) {
//                        JSONArray res = ApiUtils.checkIsSuccess(response);
                        try {
                            JSONObject resJson = new JSONObject(response);
                            if (Integer.parseInt(resJson.getString("ret")) == 200) {
                                JSONObject dataJson = resJson.getJSONObject("data");
                                String code = dataJson.getString("code");
                                if (code.equals("700")) {

                                    //AppManager.getAppManager().finishAllActivity();
                                    Intent intent = new Intent(AppContext.getInstance(), PhoneLoginActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    AppContext.getInstance().startActivity(intent);

                                } else if (!code.equals("0")) {
                                    mHandler.removeCallbacks(perMCRunnable);
                                    mTXLivePlayer.pause();
                                    mIvLoadingBg.setVisibility(View.VISIBLE);
                                    if (mIsBeingLinkMic) {
                                        stopLinkMic();
                                    }
//                                    mBlack.setVisibility(View.VISIBLE);
                                    if (koufeiType == 6) {
                                        chronometer.stop();
                                        Intent intent = new Intent();
                                        setResult(10, intent);
                                        closePlayer();

                                    } else {
                                        showBalanceLow();
                                    }
                                } else {
                                    if (mIvLoadingBg.getVisibility() == View.VISIBLE) {
                                        startPlay(mPlayUrl, mPlayType);
                                        mIvLoadingBg.setVisibility(View.GONE);
                                    }
                                    mChatServer.doSendUpdateCoin(koufeiVal, mUser.id, "0");
                                    mHandler.postDelayed(perMCRunnable, 60 * 1000);
                                    mUser.coin = dataJson.getJSONArray("info").getJSONObject(0).getString("coin");
                                    AppContext.getInstance().saveUserInfo(mUser);
                                }
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                });
    }

    private void showBalanceLow() {

        LiveCommon.noMoneyDialog("你当前余额不足无法观看", VideoPlayerActivity.this, new com.duomizhibo.phonelive.interf.DialogInterface() {
            @Override
            public void cancelDialog(View v, Dialog d) {

            }

            @Override
            public void determineDialog(View v, Dialog d) {
                d.dismiss();
                onBackPressed();

            }
        }).show();
    }

    public static void startVideoPlayerActivity(final Context context, final LiveJson live) {
        PhoneLiveApi.checkoutRoom(AppContext.getInstance().getLoginUid()
                , AppContext.getInstance().getToken(), live.stream, live.uid, new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(String response, int id) {
                        JSONArray res = ApiUtils.checkIsSuccess(response);
                        TLog.error("reschecklive" + response);
                        if (res != null) {
                            try {
                                final JSONObject data = res.getJSONObject(0);
                                final int type = data.getInt("type");
                                final String type_val = data.getString("type_val");
                                if (type == 2) {
                                    DialogHelp.getMessageDialog(context, data.getString("type_msg"), new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            PhoneLiveApi.requestCharging(AppContext.getInstance().getLoginUid(), AppContext.getInstance().getToken(),
                                                    live.uid, live.stream, new StringCallback() {

                                                        @Override
                                                        public void onError(Call call, Exception e, int id) {

                                                        }

                                                        @Override
                                                        public void onResponse(String response, int id) {
                                                            JSONArray res = ApiUtils.checkIsSuccess(response);
                                                            TLog.error("rescheckliveonResponse" + response);
                                                            if (res != null) {

                                                                UserBean userBean = AppContext.getInstance().getLoginUser();
                                                                try {
                                                                    userBean.coin = res.getJSONObject(0).getString("coin");
                                                                    AppContext.getInstance().saveUserInfo(userBean);
                                                                } catch (JSONException e) {
                                                                    e.printStackTrace();
                                                                }
                                                                Bundle bundle = new Bundle();
                                                                bundle.putInt("type", type);
                                                                bundle.putString("type_val", type_val);
                                                                bundle.putParcelable("USER_INFO", live);
                                                                UIHelper.showLookLiveActivity(context, bundle);
                                                            }
                                                        }
                                                    });

                                        }
                                    }).create().show();

                                } else if (type == 1) {
                                    LiveCommon.showInputContentDialog(context, "请输入房间密码", new com.duomizhibo.phonelive.interf.DialogInterface() {
                                        @Override
                                        public void cancelDialog(View v, Dialog d) {
                                            d.dismiss();
                                        }

                                        @Override
                                        public void determineDialog(View v, Dialog d) {
                                            try {
                                                EditText et = (EditText) d.findViewById(R.id.et_input);
                                                if (!data.getString("type_msg").equals(MD5.getMD5(et.getText().toString()))
                                                        && !data.getString("type_msg").contains(MD5.getMD5(et.getText().toString()))) {
                                                    Toast.makeText(context, "密码错误", Toast.LENGTH_SHORT).show();
                                                    return;
                                                }

                                                Bundle bundle = new Bundle();
                                                bundle.putInt("type", type);
                                                bundle.putString("type_val", type_val);
                                                bundle.putParcelable("USER_INFO", live);
                                                UIHelper.showLookLiveActivity(context, bundle);
                                                d.dismiss();

                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    });
                                } else if (type == 3) {
                                    DialogHelp.getMessageDialog(context, data.getString("type_msg"), new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            PhoneLiveApi.requestCharging(AppContext.getInstance().getLoginUid(), AppContext.getInstance().getToken(),
                                                    live.uid, live.stream, new StringCallback() {

                                                        @Override
                                                        public void onError(Call call, Exception e, int id) {

                                                        }

                                                        @Override
                                                        public void onResponse(String response, int id) {
                                                            JSONArray res = ApiUtils.checkIsSuccess(response);

                                                            if (res != null) {


                                                                UserBean userBean = AppContext.getInstance().getLoginUser();
                                                                try {
                                                                    userBean.coin = res.getJSONObject(0).getString("coin");
                                                                    AppContext.getInstance().saveUserInfo(userBean);
                                                                } catch (JSONException e) {
                                                                    e.printStackTrace();
                                                                }
                                                                Bundle bundle = new Bundle();
                                                                bundle.putInt("type", type);
                                                                bundle.putString("type_val", type_val);
                                                                bundle.putParcelable("USER_INFO", live);
                                                                UIHelper.showLookLiveActivity(context, bundle);
                                                            }
                                                        }
                                                    });

                                        }
                                    }).create().show();
                                } else {
                                    Bundle bundle = new Bundle();
                                    bundle.putInt("type", type);
                                    bundle.putString("type_val", type_val);
                                    bundle.putParcelable("USER_INFO", live);
                                    UIHelper.showLookLiveActivity(context, bundle);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    }
                });
    }

    //显示设置列表
    private void showSettingPopUp(View v) {
        View popView = getLayoutInflater().inflate(R.layout.pop_view_camera_control, null);
        LinearLayout llLiveCameraControl = (LinearLayout) popView.findViewById(R.id.ll_live_camera_control);
        llLiveCameraControl.measure(0, 0);
        int height = llLiveCameraControl.getMeasuredHeight();
        popView.findViewById(R.id.iv_live_flashing_light).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flashingLightOn = !flashingLightOn;
            }
        });
        popView.findViewById(R.id.iv_live_switch_camera).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
        popView.findViewById(R.id.iv_live_shar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSharePopWindow(VideoPlayerActivity.this, v, LiveUtils.getSimleUserInfo(mEmceeInfo));
            }
        });
        PopupWindow popupWindow = new PopupWindow(popView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        popView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        int[] location = new int[2];
        v.getLocationOnScreen(location);
        popupWindow.showAtLocation(v, Gravity.NO_GRAVITY, location[0], location[1] - height);
    }


    private TCLinkMicPlayItem getPlayItemByUserID(String strUserID) {
        if (strUserID == null || strUserID.length() == 0) {
            return null;
        }

        for (TCLinkMicPlayItem item : mVecPlayItems) {
            if (strUserID.equalsIgnoreCase(item.mUserID)) {
                return item;
            }
        }

        return null;
    }

    private TCLinkMicPlayItem getPlayItemByPlayUrl(String playUrl) {
        if (playUrl == null || playUrl.length() == 0) {
            return null;
        }

        for (TCLinkMicPlayItem item : mVecPlayItems) {
            if (playUrl.equalsIgnoreCase(item.mPlayUrl)) {
                return item;
            }
        }

        return null;
    }


    private ScaleAnimation setTranste(final View view) {
        ScaleAnimation animation = new ScaleAnimation(1,0.5f,1,0.5f,
                Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
        //3秒完成动画
        animation.setDuration(200);


//        TranslateAnimation animation = new TranslateAnimation(0, -5, 0, 0);
//        animation.setInterpolator(new OvershootInterpolator());
//        animation.setDuration(100);
        animation.setRepeatCount(3);
        animation.setRepeatMode(Animation.REVERSE);
        view.startAnimation(animation);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                view.startAnimation(animation);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        return animation;

    }

    boolean flag;

    private void cost() {
        PhoneLiveApi.requestCharging(AppContext.getInstance().getLoginUid(), AppContext.getInstance().getToken(),
                mEmceeInfo.uid, mEmceeInfo.stream, new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                    }
                    @Override
                    public void onResponse(String response, int id) {
                        try {
                            JSONObject resJson = new JSONObject(response);
                            if (Integer.parseInt(resJson.getString("ret")) == 200) {
                                JSONObject dataJson = resJson.getJSONObject("data");
                                String code = dataJson.getString("code");
                                if (code.equals("700")) {
                                    Intent intent = new Intent(AppContext.getInstance(), PhoneLoginActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                } else if (code.equals("0")) {
                                    UserBean userBean = AppContext.getInstance().getLoginUser();
                                    userBean.coin = dataJson.getJSONArray("info").getJSONObject(0).getString("coin");
                                    AppContext.getInstance().saveUserInfo(userBean);
                                    in_person_dialog.setVisibility(View.GONE);
                                    tv_hangup.clearAnimation();
                                    translateAnimation.cancel();
                                    translateAnimation.setAnimationListener(null);
                                    lianmai();
                                    mChatServer.doSendUpdateCoin(koufeiVal, mUser.id, "0");
                                }else {
                                    Intent intent = new Intent();
                                    setResult(10, intent);
                                    closePlayer();
                                }
                            }
                        } catch (Exception e) {

                        }

                    }
                });


    }

    private void lianmai() {

//                                            mNotifyPlayUrl = playUrl;
        //兼容大主播是旧版本，小主播是新版本的情况：“新版本小主播推流地址后面加上混流参数mix=layer:s;session_id:xxx;t_id:1”
        //1、如果连麦的大主播是旧版本，保证和旧版本大主播连麦时，能够互相拉取到对方的低时延流，也能够保证混流成功
        //2、如果连麦的大主播是新版本，那么大主播推流地址后面带的是&mix=session_id:xxx，这种情况下可以互相拉取低时延流，也可以混流成功（不会触发自动混流，由大主播通过CGI调用的方式启动混流）
        String sessionID = mRoomNum;
        if (sessionID != null && sessionID.length() > 0) {
            pusherUrl = pusherUrl + "&mix=session_id:" + sessionID;
        }
        //拉主播低延时流
        PhoneLiveApi.requestPlayUrlWithSignForLinkMic(mUser.id, mEmceeInfo.pull, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
            }

            @Override
            public void onResponse(String response, int id) {
                JSONArray res = ApiUtils.checkIsSuccess(response);
                if (res != null) {
                    try {
                        //结束从CDN拉流
                        stopPlay(true);
                        startPlay(res.getJSONObject(0).getString("streamUrlWithSignature"), TXLivePlayer.PLAY_TYPE_LIVE_RTMP_ACC);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }
        });
        //推自己的流
        for (TCLinkMicPlayItem item : mVecPlayItems) {
            if (item.mUserID == null || item.mUserID.length() == 0) {
                item.mUserID = mUser.id;
                item.startLoading();
                item.pushUrl = pusherUrl;
                if (!mPausing) {
                    if (item.mUserID.equals(mUser.id)) {
                        item.startPush(pusherUrl);
                    }
                    item.stopLoading();
                }
                break;
            }
        }
        //发送自己的流给主播和连麦小主播
        if (playUrl.length() != 0 && mIsBeingLinkMic) {
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    ll_time.setVisibility(View.VISIBLE);
                    chronometer.setBase(SystemClock.elapsedRealtime());
                    chronometer.start();
                    mHandler.postDelayed(perMCRunnable, 60 * 1000);
                    mChatServer.doSendTinkMicPlayUrl(mUser, playUrl);
                }
            }, 2 * 1000);

        }
        //查找空闲的TCLinkMicPlayItem, 开始loading
        //拉取其它正在和大主播连麦的小主播的视频流
        if (mIsBeingLinkMic) {
            try {
                JSONArray jsonArray = new JSONArray(mResponseParams);
                for (int i = 0; i < jsonArray.length(); ++i) {
                    JSONObject item = jsonArray.getJSONObject(i);
                    if (item != null) {
                        if (!item.getString("userid").equals(mUser.id)) {
                            startPlayVideoStream(item.getString("userid"), item.getString("playurl"));
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }


}

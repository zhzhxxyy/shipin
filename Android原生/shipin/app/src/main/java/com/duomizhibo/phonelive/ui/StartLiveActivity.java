package com.duomizhibo.phonelive.ui;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.duomizhibo.phonelive.interf.DialogInterface;
import com.duomizhibo.phonelive.ui.dialog.LiveCommon;
import com.hyphenate.util.NetUtils;
import com.tencent.rtmp.ITXLivePushListener;
import com.tencent.rtmp.TXLiveConstants;
import com.tencent.rtmp.TXLivePushConfig;
import com.tencent.rtmp.TXLivePusher;
import com.tencent.rtmp.audio.TXAudioPlayer;
import com.tencent.rtmp.ui.TXCloudVideoView;
import com.duomizhibo.phonelive.AppConfig;
import com.duomizhibo.phonelive.AppContext;
import com.duomizhibo.phonelive.R;
import com.duomizhibo.phonelive.TCConstants;
import com.duomizhibo.phonelive.api.remote.ApiUtils;
import com.duomizhibo.phonelive.api.remote.PhoneLiveApi;
import com.duomizhibo.phonelive.base.ShowLiveActivityBase;
import com.duomizhibo.phonelive.bean.ChatBean;
import com.duomizhibo.phonelive.bean.SendGiftBean;
import com.duomizhibo.phonelive.bean.UserBean;
import com.duomizhibo.phonelive.event.Event;
import com.duomizhibo.phonelive.fragment.BeautyDialogFragment;
import com.duomizhibo.phonelive.fragment.MusicPlayerDialogFragment;
import com.duomizhibo.phonelive.fragment.SearchMusicDialogFragment;
import com.duomizhibo.phonelive.interf.ChatServerInterface;
import com.duomizhibo.phonelive.linkmic.TCLinkMicPlayItem;
import com.duomizhibo.phonelive.linkmic.TCLivePlayListenerImpl;
import com.duomizhibo.phonelive.linkmic.TCLivePushListenerImpl;
import com.duomizhibo.phonelive.linkmic.TCStreamMergeMgr;
import com.duomizhibo.phonelive.ui.customviews.TCAudioControl;
import com.duomizhibo.phonelive.ui.other.ChatServer;
import com.duomizhibo.phonelive.ui.other.LiveStream;
import com.duomizhibo.phonelive.utils.DialogHelp;
import com.duomizhibo.phonelive.utils.InputMethodUtils;
import com.duomizhibo.phonelive.utils.LiveUtils;
import com.duomizhibo.phonelive.utils.SocketMsgUtils;
import com.duomizhibo.phonelive.utils.StringUtils;
import com.duomizhibo.phonelive.utils.TCUtils;
import com.duomizhibo.phonelive.utils.TLog;
import com.duomizhibo.phonelive.utils.ThreadManager;
import com.duomizhibo.phonelive.widget.music.DefaultLrcBuilder;
import com.duomizhibo.phonelive.widget.music.ILrcBuilder;
import com.duomizhibo.phonelive.widget.music.LrcRow;
import com.duomizhibo.phonelive.widget.music.LrcView;
import com.zhy.http.okhttp.callback.StringCallback;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import jp.wasabeef.glide.transformations.BlurTransformation;
import jp.wasabeef.glide.transformations.CropCircleTransformation;
import okhttp3.Call;


/**
 * 直播页面
 * 本页面包括点歌 分享 直播 聊天 僵尸粉丝 管理 点亮 歌词等功能详细参照每个方法的注释
 * 本页面继承基类和观看直播属于同一父类
 */
public class StartLiveActivity extends ShowLiveActivityBase implements ITXLivePushListener, SearchMusicDialogFragment.SearchMusicFragmentInterface, BeautyDialogFragment.OnBeautyParamsChangeListener, TCLivePushListenerImpl.ITCLivePushListener, TCLivePlayListenerImpl.ITCLivePlayListener {

    //渲染视频
    @InjectView(R.id.video_view)
    TXCloudVideoView mTXCloudVideoView;

    //歌词显示控件
    @InjectView(R.id.lcv_live_start)
    LrcView mLrcView;

    @InjectView(R.id.fl_bottom_menu)
    RelativeLayout mFlBottomMenu;

    @InjectView(R.id.rl_live_music)
    LinearLayout mViewShowLiveMusicLrc;

    @InjectView(R.id.iv_live_music)
    ImageView mMusic;

    @InjectView(R.id.iv_live_game)
    protected ImageView mIvGame;


    @InjectView(R.id.audio_plugin)
    LinearLayout mAudioPluginLayout;

    @InjectView(R.id.btn_live_end_music)
    Button mEndMusic;

    @InjectView(R.id.iv_live_charge)
    protected ImageView mIvCharge;
    //私播
    @InjectView(R.id.iv_mohu)
    protected ImageView iv_mohu;

    protected String rtmpPushAddress;

    @InjectView(R.id.iv_live_rtc)
    protected ImageView mBtnLinkMic;

    private int mBeautyLevel = 0;

    private int mWhiteningLevel = 0;

    //直播结束魅力值数量
    private int mLiveEndYpNum;

    private Timer mTimer;

    //是否开启直播
    private boolean IS_START_LIVE = true;

    public LiveStream mStreamer;

    private int mPlayTimerDuration = 1000;

    private int pauseTime = 0;

    private MediaPlayer mPlayer;

    protected TXLivePusher mTXLivePusher;
    protected TXLivePushConfig mTXPushConfig = new TXLivePushConfig();
    protected TXAudioPlayer mAudioPlayer;


    private int lastX;
    private int lastY;

    private BeautyDialogFragment mBeautyDialogFragment;
    private BeautyDialogFragment.BeautyParams mBeautyParams = new BeautyDialogFragment.BeautyParams();

    protected boolean mPasuing = false;

    private static final int MAX_LINKMIC_MEMBER_SUPPORT = 3;

    public String mSessionID;
    public boolean mHasPendingRequest = false;

    public Vector<TCLinkMicPlayItem> mVecPlayItems = new Vector<>();
    public Map<String, String> mMapLinkMicMember = new HashMap<>();

    public boolean mNeedResetVideoQuality = false;

    JSONArray json = new JSONArray();

    String type;
    String type_val;
    private int last_item;
    TextView oldView;
    android.app.AlertDialog alertDialog;
    String uid="";
    @Override
    protected int getLayoutId() {
        return R.layout.activity_live_show;
    }
    @Override
    public void initView() {
        super.initView();

        mBeautyDialogFragment = new BeautyDialogFragment();

        mLrcView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                int x = (int) event.getX();
                int y = (int) event.getY();
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        lastX = x;
                        lastY = y;
                        break;
                    case MotionEvent.ACTION_MOVE:
                        //计算移动的距离
                        int offX = x - lastX;
                        int offY = y - lastY;
                        //调用layout方法来重新放置它的位置
                        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 300);
                        layoutParams.setMargins(mViewShowLiveMusicLrc.getLeft() + offX, mViewShowLiveMusicLrc.getTop() + offY, 0, 0);
                        mViewShowLiveMusicLrc.setLayoutParams(layoutParams);
                        break;
                }
                return true;
            }
        });
        //防止聊天软键盘挤压屏幕
        mRoot.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                if (bottom > oldBottom && InputMethodUtils.isShowSoft(StartLiveActivity.this)) {
                    changeEditStatus(false);
                }
            }
        });


        if (TCUtils.supportLinkMic() == false) {
            return;
        }
        TCLinkMicPlayItem item1 = new TCLinkMicPlayItem(this, this, this, false, 1);
        TCLinkMicPlayItem item2 = new TCLinkMicPlayItem(this, this, this, false, 2);
        TCLinkMicPlayItem item3 = new TCLinkMicPlayItem(this, this, this, false, 3);
        item1.setmTimeOutRunnable(new TCLinkMicTimeoutRunnable());
        item2.setmTimeOutRunnable(new TCLinkMicTimeoutRunnable());
        item3.setmTimeOutRunnable(new TCLinkMicTimeoutRunnable());
        mVecPlayItems.add(item1);
        mVecPlayItems.add(item2);
        mVecPlayItems.add(item3);

    }


    @Override
    public void initData() {
        super.initData();
        mUser = AppContext.getInstance().getLoginUser();
        mRoomNum = mUser.id;
        mTvLiveNumber.setText("房间: " + mUser.id);
        //流名
        mStreamName = getIntent().getStringExtra("stream"); //HHH 2016-09-13
        //推流地址
        rtmpPushAddress = getIntent().getStringExtra("push");
        //直播码
        rtmpPushAddress = rtmpPushAddress + "&mix=session_id:" + mRoomNum;

        //是否开启镜像
        mTvLiveNum.setText(ChatServer.LIVE_USER_NUMS + "人观看");
        //映票
        mTvYpNum.setText(getIntent().getStringExtra("votestotal"));
        //弹幕价格
        barrageFee = StringUtils.toInt(getIntent().getStringExtra("barrage_fee"));
        //vip类型
        vipType = getIntent().getStringExtra("viptype");

        liveNum = getIntent().getStringExtra("livenum");

        //3为计时收费
        type = getIntent().getStringExtra("type");

        JSONArray res = AppConfig.LIVE_TYPE;
        if (res != null) {
            for (int i = 0; i < res.length(); i++) {
                try {
                    if (res.getJSONArray(i).getString(1).contains("计时房间")) {
                        if (!type.equals("1")&&!type.equals("6")) {
                            mIvCharge.setVisibility(View.VISIBLE);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }
        if (StringUtils.toInt(liveNum) != 0) {
            mTvLiveNumber.setText("房间: 靓" + liveNum);
        } else {
            mTvLiveNumber.setText("房间: " + mRoomNum);
        }
        goodNum = liveNum;
        mEmceeHead.setAvatarUrl(mUser.avatar);

        mEmceeLevel.setImageResource(LiveUtils.getAnchorLevelRes2(mUser.level_anchor));

        //连接聊天服务器
        initChatConnection();
        initLivePlay();
    }

    /**
     * @dw 初始化连接聊天服务器
     */
    private void initChatConnection() {
        //连接socket服务器
        try {
            mChatServer = new ChatServer(new ChatListenUIRefresh(), this, getIntent().getStringExtra("chaturl"));
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }


    /**
     * @dw 初始化直播播放器
     */
    private void initLivePlay() {
        //直播参数配置start
        startPublish();
    }

    protected void startPublish() {
        if (mTXLivePusher == null) {
            mTXLivePusher = new TXLivePusher(this);
            mBeautyDialogFragment.setBeautyParamsListner(mBeautyParams, this);
            mTXLivePusher.setPushListener(this);
            mTXPushConfig.setAutoAdjustBitrate(false);

            //切后台推流图片
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.ARGB_8888;
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.pause_publish, options);
            mTXPushConfig.setPauseImg(bitmap);
            mTXPushConfig.setPauseFlag(TXLiveConstants.PAUSE_FLAG_PAUSE_VIDEO | TXLiveConstants.PAUSE_FLAG_PAUSE_AUDIO);
            mTXPushConfig.setBeautyFilter(mBeautyParams.mBeautyProgress, mBeautyParams.mWhiteProgress);
            mTXPushConfig.setFaceSlimLevel(mBeautyParams.mFaceLiftProgress);
            mTXPushConfig.setEyeScaleLevel(mBeautyParams.mBigEyeProgress);
            mTXLivePusher.setConfig(mTXPushConfig);
        }

        mTXCloudVideoView.setVisibility(View.VISIBLE);
        //默认不开启美颜
        if (!mTXLivePusher.setBeautyFilter(TCUtils.filtNumber(9, 100, mBeautyLevel), TCUtils.filtNumber(3, 100, mWhiteningLevel))) {
            Toast.makeText(getApplicationContext(), "当前机型的性能无法支持美颜功能", Toast.LENGTH_SHORT).show();
        }
        //设置视频质量：高清
        mTXLivePusher.setVideoQuality(TXLiveConstants.VIDEO_QUALITY_HIGH_DEFINITION);
        //以游戏直播为主，一般都推荐开启硬件加速。
        //mTXCloudVideoView.enableHardwareDecode(true);
        mTXLivePusher.startCameraPreview(mTXCloudVideoView);
        mTXLivePusher.setMirror(true);
        mTXLivePusher.startPusher(rtmpPushAddress);
        startAnimation(3);

        //兼容大主播是新版本，小主播是旧版本的情况：“新版本大主播推流地址后面带上&mix=session_id:xxx”
        //1、采用这种方式，保证旧版本小主播可以拉到大主播的加速地址
        //2、新版本大主播拉小主播的加速地址，有没有session_id都没有影响
        //3、新版本混流由大主播调用CGI的方式启动混流，可以正常混流
        rtmpPushAddress = rtmpPushAddress + "&mix=session_id:" + mSessionID;

        TCStreamMergeMgr.getInstance().setMainVideoStream(rtmpPushAddress);
    }

    //开始直播
    private void startLiveStream() {
        //连接到socket服务端
        mChatServer.connectSocketServer(mUser, mStreamName, mUser.id);
    }

    @OnClick({R.id.ll_time, R.id.iv_live_charge, R.id.btn_live_end_music, R.id.iv_live_rtc, R.id.btn_audio_effect, R.id.iv_live_emcee_head, R.id.tglbtn_danmu_setting, R.id.ll_live_room_info, R.id.btn_audio_close, R.id.iv_live_music, R.id.iv_live_meiyan, R.id.iv_live_camera_control, R.id.iv_live_privatechat, R.id.iv_live_back, R.id.ll_yp_labe, R.id.iv_live_chat, R.id.bt_send_chat})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_audio_effect:
                //mAudioCtrl.setVisibility(mAudioCtrl.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
                break;
            case R.id.btn_audio_close:
                break;
            //展示主播信息弹窗
            case R.id.iv_live_emcee_head:
                showUserInfoDialog(mUser);
                break;
            //展示主播信息弹窗
            case R.id.ll_live_room_info:
                showUserInfoDialog(mUser);
                break;
            //展示点歌菜单
            case R.id.iv_live_music:
                //if (null != mAudioCtrl) {
                //mAudioCtrl.setVisibility(mAudioCtrl.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
                //                }
                showSearchMusicDialog();
                break;
            //美颜
            case R.id.iv_live_meiyan:
                if (mBeautyDialogFragment.isAdded())
                    mBeautyDialogFragment.dismiss();
                else
                    mBeautyDialogFragment.show(getFragmentManager(), "");
                break;
            //设置
            case R.id.iv_live_camera_control:
                showSettingPopUp(v);
                break;
            //开启关闭弹幕
            case R.id.tglbtn_danmu_setting:
                openOrCloseDanMu();
                break;
            //私信
            case R.id.iv_live_privatechat:
                showPrivateChat();
                break;
            case R.id.iv_live_back:
                onClickGoBack();
                break;
            //魅力值排行榜
            case R.id.ll_yp_labe:
                showDedicateOrder();
                break;
            //聊天输入框
            case R.id.iv_live_chat://chat gone or visble
                changeEditStatus(true);
                break;
            case R.id.bt_send_chat://send chat
                if (mDanMuIsOpen) {
                    sendBarrage();
                } else {
                    sendChat();
                }
                break;
            case R.id.iv_live_rtc:
                break;
            case R.id.btn_kick_out1:
            case R.id.btn_kick_out2:
            case R.id.btn_kick_out3:
            case R.id.ll_time:
                for (int i = 0; i < mVecPlayItems.size(); ++i) {
                    TCLinkMicPlayItem item = mVecPlayItems.get(i);
                    if (item.getKickoutBtnId() == v.getId()) {

                        //混流：减少一路
                        TCStreamMergeMgr.getInstance().delSubVideoStream(item.mPlayUrl);

                        //通知其它小主播：有小主播退出连麦
                        mChatServer.doSendKickUser(item.mUserID, "");
                        //清理数据
                        //mTCLinkMicMgr.kickOutLinkMicMember(item.mUserID);
                        if (item != null) {
                            mMapLinkMicMember.remove(item.mUserID);
                            //结束播放
                            item.stopPlay(true);
                            item.empty();
                        }

                        if (mMapLinkMicMember.size() == 0) {
                            //无人连麦，设置视频质量：高清
                            mTXLivePusher.setVideoQuality(TXLiveConstants.VIDEO_QUALITY_HIGH_DEFINITION);
                        }
                        if (type.equals("6")){
                            closeSiBo();
                        }
                        break;
                    }
                }
                break;
            case R.id.btn_live_end_music:
                stopMusic();
                mEndMusic.setVisibility(View.GONE);
                break;
            case R.id.iv_live_charge:
                changeRoomType();
                break;
            default:
                super.onClick(v);
        }
    }

    private void closeSiBo(){

        if (iv_mohu.getVisibility()==View.VISIBLE){
            iv_mohu.setVisibility(View.GONE);
        }
        if (chronometer!=null){
            chronometer.stop();
        }
        LiveCommon.showPersonDialog(StartLiveActivity.this, "1", "1", " 私播已结束\n本次私播时长\n"+chronometer.getText(), "", "关闭", "", new DialogInterface() {
            @Override
            public void cancelDialog(View v, Dialog d) {
                ll_time.setVisibility(View.GONE);
                d.dismiss();
            }
            @Override
            public void determineDialog(View v, Dialog d) {
            }
        });
    }

    private void changeRoomType() {
        final Dialog dialog = new Dialog(StartLiveActivity.this, R.style.dialog);
        dialog.setContentView(R.layout.dialog_set_room_charge);
        Window dialogWindow = dialog.getWindow();
        dialogWindow.setGravity(Gravity.CENTER);
        dialog.setCanceledOnTouchOutside(true);
        ListView listView = (ListView) dialog.findViewById(R.id.lv_ready_charge);
        TextView mTvCancel = (TextView) dialog.findViewById(R.id.btn_cancel);
        TextView mTvConfirm = (TextView) dialog.findViewById(R.id.btn_confirm);
        listView.setAdapter(new StartLiveActivity.ListViewAdapter());
        mTvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        mTvConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                type = "3";
                if (TextUtils.isEmpty(type_val)) {
                    showToast3("金额不能为空", 0);
                    return;
                }
                PhoneLiveApi.changeLiveType(mUser.id, mUser.token, mStreamName, type, type_val, new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        JSONArray res = ApiUtils.checkIsSuccess(response);
                        if (res != null) {
                            showToast3("切换成功", 0);
                            mChatServer.doSendSwitchToCharge(type_val);
                        }
                    }
                });
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    //打开魅力值排行
    private void showDedicateOrder() {
        DialogHelp.getMessageDialog(this, "正在直播点击排行会影响直播,是否继续", new android.content.DialogInterface.OnClickListener() {
            @Override
            public void onClick(android.content.DialogInterface dialog, int which) {
                OrderWebViewActivity.startOrderWebView(StartLiveActivity.this, mUser.id);
            }
        }).show();
    }

    //当每个聊天被点击显示该用户详细信息弹窗
    public void chatListItemClick(ChatBean chat) {
        if (chat.getType() != 13) {
            showUserInfoDialog(chat.mSimpleUserInfo);
        }
    }

    /**
     * @dw 显示搜索音乐弹窗
     */
    private void showSearchMusicDialog() {
        SearchMusicDialogFragment musicFragment = new SearchMusicDialogFragment();
        musicFragment.setStyle(SearchMusicDialogFragment.STYLE_NO_TITLE, 0);
        musicFragment.show(getSupportFragmentManager(), "SearchMusicDialogFragment");
    }

    //音效调教菜单
    private void showSoundEffectsDialog() {
        MusicPlayerDialogFragment musicPlayerDialogFragment = new MusicPlayerDialogFragment();
        musicPlayerDialogFragment.show(getSupportFragmentManager(), "MusicPlayerDialogFragment");
    }

    //当主播选中了某一首歌,开始播放
    @Override
    public void onSelectMusic(Intent data) {
        startMusicStrem(data);
        mEndMusic.setVisibility(View.VISIBLE);
    }

    //发送弹幕回调方法
    @Override
    protected void sendBarrageOnResponse(String response) {
        JSONArray s = ApiUtils.checkIsSuccess(response);
        if (s != null) {
            try {
                JSONObject tokenJson = s.getJSONObject(0);
                mChatServer.doSendBarrage(tokenJson.getString("barragetoken"), mUser);
                mChatInput.setText("");
                mChatInput.setHint("开启大喇叭，" + barrageFee + "钻石/条");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void showErrorAndQuit(String errorMsg) {

        if (mTXCloudVideoView != null) {
            mTXCloudVideoView.onPause();
        }
        stopPublish();
        quitRoom();
        //stopRecordAnimation();

        super.showErrorAndQuit(errorMsg);

    }

    @Override
    public void onPushEvent(int event, Bundle bundle) {
        if (event < 0) {
            if (event == TXLiveConstants.PUSH_ERR_NET_DISCONNECT) {//网络断开，弹对话框强提醒，推流过程中直播中断需要显示直播信息后退出
                showComfirmDialog(com.duomizhibo.phonelive.TCConstants.ERROR_MSG_NET_DISCONNECTED, true);//出现一次网络断开 就直接退出了
            } else if (event == TXLiveConstants.PUSH_ERR_OPEN_CAMERA_FAIL) {//未获得摄像头权限，弹对话框强提醒，并退出
                showErrorAndQuit(com.duomizhibo.phonelive.TCConstants.ERROR_MSG_OPEN_CAMERA_FAIL);
            } else if (event == TXLiveConstants.PUSH_ERR_OPEN_MIC_FAIL) { //未获得麦克风权限，弹对话框强提醒，并退出
                Toast.makeText(getApplicationContext(), bundle.getString(TXLiveConstants.EVT_DESCRIPTION), Toast.LENGTH_SHORT).show();
                showErrorAndQuit(com.duomizhibo.phonelive.TCConstants.ERROR_MSG_OPEN_MIC_FAIL);
            } else {
                //其他错误弹Toast弱提醒，并退出
                Toast.makeText(getApplicationContext(), bundle.getString(TXLiveConstants.EVT_DESCRIPTION), Toast.LENGTH_SHORT).show();

                mTXCloudVideoView.onPause();
                //改变直播状态
                //TCPusherMgr.getInstance().changeLiveStatus(mUserId, mUserToken, TCPusherMgr.TCLiveStatus_Offline);
                //stopRecordAnimation();
                finish();
            }
        }

        if (event == TXLiveConstants.PUSH_WARNING_HW_ACCELERATION_FAIL) {
            mTXPushConfig.setVideoResolution(TXLiveConstants.VIDEO_RESOLUTION_TYPE_360_640);
            mTXPushConfig.setVideoBitrate(700);
            mTXPushConfig.setHardwareAcceleration(TXLiveConstants.ENCODE_VIDEO_SOFTWARE);
            mTXLivePusher.setConfig(mTXPushConfig);
        }
        //改变直播状态
        if (event == TXLiveConstants.PUSH_EVT_PUSH_BEGIN) {
            PhoneLiveApi.changeLiveState(mUser.id, mUser.token, mStreamName, "1", null);
        }
    }

    @Override
    public void onNetStatus(Bundle bundle) {

    }


    /**
     * 显示确认消息
     *
     * @param msg     消息内容
     * @param isError true错误消息（必须退出） false提示消息（可选择是否退出）
     */
    public void showComfirmDialog(String msg, Boolean isError) {

        AlertDialog.Builder builder = new AlertDialog.Builder(StartLiveActivity.this);
        builder.setCancelable(true);
        builder.setTitle(msg);

        if (!isError) {
            builder.setPositiveButton("确定", new android.content.DialogInterface.OnClickListener() {
                @Override
                public void onClick(android.content.DialogInterface dialog, int which) {
                    dialog.dismiss();
                    stopPublish();
                    quitRoom();
                    //stopRecordAnimation();
                    showLiveEndDialog(mUser.id, mLiveEndYpNum, mStreamName);
                }
            });
            builder.setNegativeButton("取消", new android.content.DialogInterface.OnClickListener() {
                @Override
                public void onClick(android.content.DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
        } else {
            //当情况为错误的时候，直接停止推流
            stopPublish();
            quitRoom();
            builder.setPositiveButton("确定", new android.content.DialogInterface.OnClickListener() {
                @Override
                public void onClick(android.content.DialogInterface dialog, int which) {
                    dialog.dismiss();
                    // stopRecordAnimation();
                    showLiveEndDialog(mUser.id, mLiveEndYpNum, mStreamName);
                }
            });
        }
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    protected void stopPublish() {
        if (mTXLivePusher != null) {
            mTXLivePusher.stopCameraPreview(false);
            mTXLivePusher.setPushListener(null);
            mTXLivePusher.stopPusher();
        }
        if (mAudioPlayer != null) {
            mAudioPlayer.stop();
            mAudioPlayer = null;
        }

        for (TCLinkMicPlayItem item : mVecPlayItems) {
            item.stopPlay(false);
        }
        if (mVecPlayItems != null)
            mVecPlayItems.clear();

        if (json != null) {
            json = null;
        }
        //混流：清除状态
        TCStreamMergeMgr.getInstance().resetMergeState();
    }

    /**
     * 退出房间
     */
    public void quitRoom() {
    }

    @Override
    public void onBackPressed() {
        showComfirmDialog(TCConstants.TIPS_MSG_STOP_PUSH, false);
    }

    @Override
    public void onBeautyParamsChange(BeautyDialogFragment.BeautyParams params, int key) {
        switch (key) {
            case BeautyDialogFragment.BEAUTYPARAM_BEAUTY:
            case BeautyDialogFragment.BEAUTYPARAM_WHITE:
                if (mTXLivePusher != null) {
                    mTXLivePusher.setBeautyFilter(params.mBeautyProgress, params.mWhiteProgress);
                }
                break;
            case BeautyDialogFragment.BEAUTYPARAM_FACE_LIFT:
                if (mTXLivePusher != null) {
                    mTXLivePusher.setFaceSlimLevel(params.mFaceLiftProgress);
                }
                break;
            case BeautyDialogFragment.BEAUTYPARAM_BIG_EYE:
                if (mTXLivePusher != null) {
                    mTXLivePusher.setEyeScaleLevel(params.mBigEyeProgress);
                }
                break;
            case BeautyDialogFragment.BEAUTYPARAM_FILTER:
                if (mTXLivePusher != null) {
                    mTXLivePusher.setFilter(TCUtils.getFilterBitmap(getResources(), params.mFilterIdx));
                }
                break;
            case BeautyDialogFragment.BEAUTYPARAM_MOTION_TMPL:
                if (mTXLivePusher != null) {
                    mTXLivePusher.setMotionTmpl(params.mMotionTmplPath);
                }
                break;
            case BeautyDialogFragment.BEAUTYPARAM_GREEN:
                if (mTXLivePusher != null) {
                    mTXLivePusher.setGreenScreenFile(TCUtils.getGreenFileName(params.mGreenIdx));
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onLivePlayEvent(String playUrl, int event, Bundle bundle) {
        TCLinkMicPlayItem item = getPlayItemByPlayUrl(playUrl);
        if (item == null) {
            return;
        }
        if (event == TXLiveConstants.PLAY_ERR_NET_DISCONNECT || event == TXLiveConstants.PLAY_EVT_PLAY_END || event == TXLiveConstants.PLAY_ERR_GET_RTMP_ACC_URL_FAIL) {
            if (item.mPending == true) {
                if (json.length() > 0)
                    for (int i = 0; i < json.length(); i++) {
                        try {
                            if (json.getJSONObject(i).getString("playUrl").equals(playUrl)) {
                                mChatServer.doSendExit(json.getJSONObject(0).getString("userid"), json.getJSONObject(0).getString("userid") + "拉流失败");
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                                    json.remove(i);
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                handleLinkMicFailed(item, "拉流失败，结束连麦");
            } else {
                handleLinkMicFailed(item, "连麦观众视频断流，结束连麦");
                if (json.length() > 0)
                    for (int i = 0; i < json.length(); i++) {
                        try {
                            if (json.getJSONObject(i).getString("playUrl").equals(playUrl)) {
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                                    json.remove(i);
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                //混流：减少一路
                TCStreamMergeMgr.getInstance().delSubVideoStream(playUrl);
            }
        } else if (event == TXLiveConstants.PLAY_EVT_PLAY_BEGIN) {
            item.mPending = false;
            //结束loading
            item.stopLoading(true);
            //混流：增加一路
            TCStreamMergeMgr.getInstance().addSubVideoStream(item.mPlayUrl);
        }
    }

    private void handleLinkMicFailed(TCLinkMicPlayItem item, String message) {
        if (item == null) {
            return;
        }
        if (item.mPending == true) {
            mHandler.removeCallbacks(item.getTimeOutRunnable());
        }
        mMapLinkMicMember.remove(item.mUserID);

        item.stopPlay(true);
        item.empty();

        //无人连麦，设置视频质量：高清
        if (mMapLinkMicMember.size() == 0) {
            if (mPasuing == false) {
                mTXLivePusher.setVideoQuality(TXLiveConstants.VIDEO_QUALITY_HIGH_DEFINITION);
            } else {
                mNeedResetVideoQuality = true;
            }
        }
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onLivePlayNetStatus(String playUrl, Bundle status) {
    }

    @Override
    public void onLivePushEvent(String pushUrl, int event, Bundle bundle) {
    }

    @Override
    public void onLivePushNetStatus(String pushUrl, Bundle status) {

    }


    //socket客户端事件监听处理
    public class ChatListenUIRefresh implements ChatServerInterface {

        @Override
        public void onMessageListen(final SocketMsgUtils socketMsg, final int type, final ChatBean c) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (type == 1) {
                        addDanmu(c);
                    } else if (type == 2) {
                        if (StringUtils.toInt(socketMsg) == 409002) {
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

        //系统通知
        @Override
        public void onSystemNot(final int code) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (code == 1) {//后台关闭直播
                        videoPlayerEnd();
                        DialogHelp.getMessageDialog(StartLiveActivity.this, "直播内容涉嫌违规", new android.content.DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(android.content.DialogInterface dialog, int which) {
                                showLiveEndDialog(mUser.id, mLiveEndYpNum, mStreamName);
                            }
                        }).show();

                    } else if (code == 2) {
                        DialogHelp.getMessageDialog(StartLiveActivity.this, "当前账号已在其他设备登录", new android.content.DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(android.content.DialogInterface dialog, int which) {
                                videoPlayerEnd();
                                Intent intent = new Intent(StartLiveActivity.this, PhoneLoginActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                finish();
                            }
                        }).show();
                    }
                }
            });

        }

        //送礼物
        @Override
        public void onShowSendGift(final SendGiftBean giftInfo, final ChatBean chatBean) {//送礼物展示
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mLiveEndYpNum += giftInfo.getTotalcoin();
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
                    if (isCanShowLit)
                        showLit();
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

                    if (!NetUtils.hasNetwork(StartLiveActivity.this)) {
                        mChatServer.close();
                    }

                }
            });
        }


        @Override
        public void onLinkMic(final SocketMsgUtils socketMsg) throws JSONException {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    try {
                        if (StringUtils.toInt(socketMsg.getAction()) == 4) {
                            TLog.error("socketMsg连麦小主播列表item" + socketMsg.getParam("playurl", "0"));
                            if (type.equals("6")){
                                ll_time.setVisibility(View.VISIBLE);
                                chronometer.setBase(SystemClock.elapsedRealtime());
                                chronometer.start();
                            }
                            //连麦小主播列表
                            if (json.length() == 0) {
                                JSONObject jo = new JSONObject();
                                jo.put("userid", socketMsg.getUid());
                                jo.put("playurl", socketMsg.getParam("playurl", ""));
                                jo.put("pushurl", 0);
                                json.put(jo);
                            } else {
                                for (int i = 0; i < json.length(); i++) {
                                    if (!json.getJSONObject(i).getString("userid").equals(socketMsg.getUid()) && json.length() < 3) {
                                        JSONObject jo = new JSONObject();
                                        jo.put("userid", socketMsg.getUid());
                                        jo.put("playurl", socketMsg.getParam("playurl", ""));
                                        jo.put("pushurl", 0);
                                        json.put(jo);
                                        TLog.error("socketMsg连麦小主播列表item2" + socketMsg.getParam("playurl", "0"));
                                    }
                                }
                            }
                            final String strUserID = socketMsg.getUid();
                            final String strPlayUrl = socketMsg.getParam("playurl", "");
                            //添加连麦小主播
                            mHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    if (!mMapLinkMicMember.containsKey(strUserID)) {
                                        return;
                                    }
                                    TCLinkMicPlayItem item = getPlayItemByUserID(strUserID);
                                    if (item == null) {
                                        return;
                                    }
                                    mMapLinkMicMember.put(strUserID, strPlayUrl);
                                    if (item.mPlayUrl == null || item.mPlayUrl.length() == 0) {
                                        TCLinkMicPlayItem playItem = getPlayItemByUserID(strUserID);
                                        if (playItem != null) {
                                            if (!TextUtils.isEmpty(strPlayUrl)) {
                                                if (!mPasuing) {
                                                    playItem.clearLog();
                                                    playItem.startPlay(strPlayUrl);
                                                }
                                            }
                                        }
                                    }
                                }
                            });
                        }
                        if (StringUtils.toInt(socketMsg.getAction()) == 1) {
                            if (mMapLinkMicMember.size() < MAX_LINKMIC_MEMBER_SUPPORT) {
                                if (mHasPendingRequest) {
                                    mChatServer.doSendRes("7", socketMsg.getUid());
                                    return;
                                }
                                mHandler.postDelayed(mTimeRunnable, 10000);
                                uid = socketMsg.getUid();
                                if (type.equals("6")) {
                                    sibo(socketMsg.getUHead(), socketMsg.getUname(), socketMsg.getUid());
                                    return;
                                }
                                final android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(StartLiveActivity.this)
                                        .setCancelable(true)
                                        .setTitle("提示")
                                        .setMessage(socketMsg.getUname() + "向您发起连麦请求")
                                        .setPositiveButton("接受", new android.content.DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(android.content.DialogInterface dialog, int which) {
                                                mHandler.removeCallbacks(mTimeRunnable);
                                                TLog.error("接受json" + json);
                                                mChatServer.doSendTinkMicAgree(mUser, socketMsg.getUid(), json);
                                                for (TCLinkMicPlayItem item : mVecPlayItems) {
                                                    if (item.mUserID == null || item.mUserID.length() == 0) {
                                                        item.mUserID = socketMsg.getUid();
                                                        item.mPending = true;
                                                        item.startLoading();
                                                        //设置超时逻辑
                                                        TCLinkMicTimeoutRunnable timeoutRunnable = (TCLinkMicTimeoutRunnable) (item.getTimeOutRunnable());
                                                        timeoutRunnable.setUserID(socketMsg.getUid());
                                                        mHandler.removeCallbacks(timeoutRunnable);
                                                        mHandler.postDelayed(timeoutRunnable, 10000);
                                                        //加入连麦成员列表
                                                        mMapLinkMicMember.put(socketMsg.getUid(), "");

                                                        //第一个小主播加入连麦，设置视频质量：连麦大主播
                                                        if (mMapLinkMicMember.size() == 1) {
                                                            mNeedResetVideoQuality = false;
                                                            mTXLivePusher.setVideoQuality(TXLiveConstants.VIDEO_QUALITY_LINKMIC_MAIN_PUBLISHER);
                                                        }
                                                        break;
                                                    }
                                                }
                                                dialog.dismiss();
                                                mHasPendingRequest = false;
                                            }
                                        }).setNegativeButton("拒绝", new android.content.DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(android.content.DialogInterface dialog, int which) {
                                                mChatServer.doSendTinkMicRefuse(mUser, socketMsg.getUid());
                                                dialog.dismiss();
                                                mHasPendingRequest = false;
                                                mHandler.removeCallbacks(mTimeRunnable);
                                            }
                                        });
                                alertDialog = builder.create();
                                alertDialog.setCancelable(false);
                                alertDialog.setCanceledOnTouchOutside(false);
                                alertDialog.show();
                                mHasPendingRequest = true;

                                //mHandler.postDelayed(mTimeRunnable, 10000);
                            } else if (json.length() >= 3) {
                                mChatServer.doSendTinkMicAgree(mUser, socketMsg.getUid(), json);
                            }
                        } else if (StringUtils.toInt(socketMsg.getAction()) == 5) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if (siBoDialog != null) {
                                        mHandler.removeCallbacks(mTimeRunnable);
                                        siBoDialog.dismiss();
                                    }
                                    TCLinkMicPlayItem item = getPlayItemByUserID(socketMsg.getUid());
                                    if (item != null) {
                                        if (IS_START_LIVE) {
                                            if (type.equals("6")){
                                                closeSiBo();
                                            }else {
                                                showToast3(socketMsg.getUname() + "已下麦", 0);
                                            }


                                        }
                                    }
                                    if (item != null) {
                                        TCStreamMergeMgr.getInstance().delSubVideoStream(item.mPlayUrl);
                                        mMapLinkMicMember.remove(item.mUserID);
                                        item.stopPlay(true);
                                        item.empty();
                                    }
                                    if (json.length() > 0) {
                                        for (int i = 0; i < json.length(); i++) {
                                            try {
                                                if (json.getJSONObject(i).getString("userid").equals(socketMsg.getUid())) {
                                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                                                        json.remove(i);
                                                    }
                                                }
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    }

                                }
                            });

                        } else if (StringUtils.toInt(socketMsg.getAction()) == 9) {
                            if (alertDialog != null) {
                                alertDialog.dismiss();
                            } if (siBoDialog != null) {
                                siBoDialog.dismiss();
                            }
                        } else if (StringUtils.toInt(socketMsg.getAction()) == 10) {
                            if (iv_mohu.getVisibility()==View.VISIBLE){
                                iv_mohu.setVisibility(View.GONE);
                            }else {
                                iv_mohu.setVisibility(View.VISIBLE);
                            }
                        }
                    } catch (Exception e) {

                    }

                }
            });

        }

        @Override
        public void onCharge(SocketMsgUtils socketMsg) {
        }
        @Override
        public void onUpdateCoin(final SocketMsgUtils socketMsg) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mTvYpNum.setText(StringUtils.toInt(mTvYpNum.getText().toString()) + StringUtils.toInt(socketMsg.getParam("votes", "")) + "");
                }
            });
        }

        @Override
        public void onGameNotice(SocketMsgUtils socketMsg) {

        }
    }

    Runnable mTimeRunnable = new Runnable() {
        @Override
        public void run() {
            if (alertDialog!=null){
                alertDialog.dismiss();
            }
            if (siBoDialog!=null){
                showToast3("连接超时...",0);
                siBoDialog.dismiss();
            }
            mHasPendingRequest = false;
            mChatServer.doSendRes("8", uid);

        }
    };


    //播放音乐
    private void startMusicStrem(Intent data) {

        mViewShowLiveMusicLrc.setVisibility(View.VISIBLE);
        //获取音乐路径
        String musicPath = data.getStringExtra("filepath");
        //获取歌词字符串
        String lrcStr = LiveUtils.getFromFile(musicPath.substring(0, musicPath.length() - 3) + "lrc");
        if (mPlayer != null) {
            mPlayer.stop();
            mPlayer.release();
            mPlayer = null;
        }
        //插入耳机
        //mStreamer.setHeadsetPlugged(true)
        mPlayer = new MediaPlayer();
        try {
            mPlayer.setDataSource(musicPath);
            mPlayer.setLooping(true);
            mPlayer.setVolume(0.5f, 0.5f);
            mPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {

                public void onPrepared(MediaPlayer mp) {

                    mp.start();
                    if (mTimer == null) {
                        mTimer = new Timer();
                        mTimer.scheduleAtFixedRate(new TimerTask() {
                            long beginTime = -1;

                            @Override
                            public void run() {
                                if (beginTime == -1) {
                                    beginTime = System.currentTimeMillis();
                                }
                                if (null != mPlayer) {
                                    final long timePassed = mPlayer.getCurrentPosition();
                                    StartLiveActivity.this.runOnUiThread(new Runnable() {

                                        public void run() {
                                            mLrcView.seekLrcToTime(timePassed);
                                        }
                                    });
                                }

                            }
                        }, 0, mPlayTimerDuration);
                    }
                }
            });
            mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

                public void onCompletion(MediaPlayer mp) {
                    stopLrcPlay();
                }
            });
            mPlayer.prepare();
            mPlayer.start();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        ILrcBuilder builder = new DefaultLrcBuilder();
        List<LrcRow> rows = builder.getLrcRows(lrcStr);

        //设置歌词
        mLrcView.setLrc(rows);
    }

    //停止歌词滚动
    public void stopLrcPlay() {
        if (mTimer != null) {
            mTimer.cancel();
            mTimer = null;
        }
    }

    //停止播放音乐
    private void stopMusic() {
        if (mEndMusic != null) {
            mEndMusic.setVisibility(View.GONE);
        }
        if (mPlayer != null) {
            mPlayer.stop();
            mViewShowLiveMusicLrc.setVisibility(View.GONE);
            if (mTimer != null) {
                mTimer.cancel();
                mTimer = null;
            }
        }

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(Event.CommonEvent event) {

        if (event.action == 1) {

            EventBus.getDefault().unregister(this);
            if (!NetUtils.hasNetwork(StartLiveActivity.this)) {

                videoPlayerEnd();
                new AlertDialog.Builder(StartLiveActivity.this)
                        .setTitle("提示")
                        .setMessage("网络断开连接,请检查网络后重新开始直播")
                        .setNegativeButton("确定", new android.content.DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(android.content.DialogInterface dialogInterface, int i) {

                                showLiveEndDialog(mUser.id, mLiveEndYpNum, mStreamName);
                            }
                        })
                        .create()
                        .show();
            }
        }

    }


    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mTXLivePusher != null) {
//            mTXLivePusher.stopCameraPreview(false);
            mTXLivePusher.pausePusher();
        }
        EventBus.getDefault().unregister(this);
    }

    //返回键监听
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if ((!IS_START_LIVE)) {
                return super.onKeyDown(keyCode, event);
            } else {
                onClickGoBack();
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

    }

    //主播点击退出
    private void onClickGoBack() {
        DialogHelp.getConfirmDialog(this, getString(R.string.iscloselive), new android.content.DialogInterface.OnClickListener() {
            @Override
            public void onClick(android.content.DialogInterface dialogInterface, int i) {
                videoPlayerEnd();
                showLiveEndDialog(mUser.id, mLiveEndYpNum, mStreamName);
            }
        }).show();
    }

    //关闭直播
    private void videoPlayerEnd() {
        IS_START_LIVE = false;
        //停止播放音乐
        stopMusic();
        //停止直播

        ThreadManager.getThreadPool().execute(new Runnable() {
            @Override
            public void run() {
            }
        });

        mGiftShowQueue.clear();
        mLuxuryGiftShowQueue.clear();
        mListChats.clear();

        if (mRlLiveGift != null && mGiftView != null) {
            mRlLiveGift.removeView(mGiftView);
        }
        if (mShowGiftAnimator != null) {
            mShowGiftAnimator.removeAllViews();
        }
        if (mChatServer != null) {
            mChatServer.closeLive();
        }
        if (null != mHandler) {
            mHandler.removeCallbacksAndMessages(null);
            mHandler = null;
        }

        mDanmuControl.hide();//关闭弹幕
        stopPublish();

        if (mTXCloudVideoView != null) {
            mTXCloudVideoView.onDestroy();
        }
        Log.e("StartActivity", "videoPlayerEnd: -------->");
    }


    @Override
    public void onPause() {
        super.onPause();
        if (IS_START_LIVE && mHandler != null) {
            mHandler.postDelayed(pauseRunnable, 1000);
        }
        if (mChatServer != null) {
            //提示
            if (mTXCloudVideoView!=null){
                mTXCloudVideoView.onPause();
            }
            if (mTXLivePusher != null) {
                mTXLivePusher.pauseBGM();
            }
            mChatServer.doSendSystemMessage("主播暂时离开一下,马上回来!", mUser);
            mPasuing = true;
        }
        for (TCLinkMicPlayItem item: mVecPlayItems) {
            item.pause();
        }
        Log.e("StartActivity", "onPause: -------->");
    }

    private Runnable pauseRunnable = new Runnable() {
        @Override
        public void run() {
            pauseTime++;
            if (pauseTime >= 50) {
                if (mHandler != null)
                    mHandler.removeCallbacks(this);
                videoPlayerEnd();

                return;
            }
            TLog.log(pauseTime + "定时器");
            mHandler.postDelayed(this, 1000);
        }
    };

    @Override
    public void onResume() {
        super.onResume();
        // 一般可以在onResume中开启摄像头预览
        // 调用KSYStreamer的onResume接口
        //重置时间,如果超过预期则关闭直播
        mTXCloudVideoView.onResume();
        if (mTXLivePusher != null) {
            mTXLivePusher.resumeBGM();
        }
        if (pauseTime >= 50) {
            showLiveEndDialog(mUser.id, mLiveEndYpNum, mStreamName);
        } else if (pauseTime >0) {
            if (mPasuing) {
                mPasuing = false;
            }
            if (mHandler!=null){
                mHandler.removeCallbacks(pauseRunnable);
            }
            if (pauseTime > 0) {
                if (mTXLivePusher != null) {
                    mTXLivePusher.resumePusher();
                }
                mChatServer.doSendSystemMessage("主播回来了", mUser);
            }
        }

        for (TCLinkMicPlayItem item: mVecPlayItems) {
            item.resume();
            if (!TextUtils.isEmpty(item.mPlayUrl) && !TextUtils.isEmpty(item.mUserID)) {
                item.startLoading();
                item.startPlay(item.mPlayUrl);
            }
        }
        pauseTime = 0;
        Log.e("StartActivity", "onResume: -------->");
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.reset(this);
        if (mTXLivePusher != null) {
//            mTXLivePusher.stopCameraPreview(false);
            mTXLivePusher.pausePusher();
        }
        mChatServer.close();
        mChatServer = null;
        Log.e("StartActivity", "onDestroy: -------->");
    }

    /**
     * @param num 倒数时间
     * @dw 开始直播倒数计时
     */
    private void startAnimation(final int num) {
        final TextView tvNum = new TextView(this);
        tvNum.setTextColor(getResources().getColor(R.color.white));
        tvNum.setText(num + "");
        tvNum.setTextSize(30);
        mRoot.addView(tvNum);
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) tvNum.getLayoutParams();
        params.addRule(RelativeLayout.CENTER_IN_PARENT);
        tvNum.setLayoutParams(params);
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(tvNum, "scaleX", 5f, 1f);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(tvNum, "scaleY", 5f, 1f);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(scaleX, scaleY);
        animatorSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if (mRoot == null) return;
                mRoot.removeView(tvNum);
                if (num == 1) {
                    startLiveStream();
                    return;
                }
                startAnimation(num == 3 ? 2 : 1);
            }

            @Override
            public void onAnimationCancel(Animator animation) {
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
            }
        });
        animatorSet.setDuration(1000);
        animatorSet.start();

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
                if (!mTXLivePusher.turnOnFlashLight(!flashingLightOn)) {
                    Toast.makeText(getApplicationContext(), "打开闪光灯失败", Toast.LENGTH_SHORT).show();
                    return;
                }
                flashingLightOn = !flashingLightOn;
            }
        });
        popView.findViewById(R.id.iv_live_switch_camera).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTXLivePusher.switchCamera();
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

    public static void startLiveActivity(Context context, String stream, String barrage_fee, String votestotal, String push, String chaturl, boolean isFrontCameraMirro, String type, String livenum, String vipType) { //HHH 2016-09-13
        Intent intent = new Intent(context, StartLiveActivity.class);
        intent.putExtra("stream", stream);
        intent.putExtra("barrage_fee", barrage_fee);
        intent.putExtra("votestotal", votestotal);
        intent.putExtra("push", push);
        intent.putExtra("chaturl", chaturl);
        intent.putExtra("isFrontCameraMirro", isFrontCameraMirro);
        intent.putExtra("type", type);
        intent.putExtra("livenum", livenum);
        intent.putExtra("viptype", vipType);
        context.startActivity(intent);

    }


    private class GameGridAdapter extends BaseAdapter {
        int[] img = {R.drawable.kaixinniuzai, R.drawable.zhiyongsanzhang, R.drawable.haodaochuanzhang, R.drawable.erbabei, R.drawable.xinyunzhuanpan
        };
        String[] name = {
                "开心牛仔", "志勇三张", "海盗船长", "二八贝", "幸运转盘"
        };

        @Override
        public int getCount() {
            return img.length;
        }

        @Override
        public Object getItem(int position) {
            return img[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            ViewHolder viewHolder;
            if (convertView == null) {
                convertView = View.inflate(AppContext.getInstance(), R.layout.item_dialog_game, null);
                viewHolder = new GameGridAdapter.ViewHolder();
                viewHolder.mGameIcon = (ImageView) convertView.findViewById(R.id.iv_gamaicon);
                viewHolder.mGameName = (TextView) convertView.findViewById(R.id.tv_gamename);

                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            viewHolder.mGameIcon.setBackgroundResource(img[position]);
            viewHolder.mGameName.setText(name[position]);

            return convertView;
        }

        class ViewHolder {
            ImageView mGameIcon;
            TextView mGameName;
        }
    }


    class TCLinkMicTimeoutRunnable implements Runnable {
        private String strUserID = "";

        public void setUserID(String userID) {
            strUserID = userID;
        }

        @Override
        public void run() {
            TCLinkMicPlayItem item = getPlayItemByUserID(strUserID);
            if (item != null && item.mPending == true) {
//                mTCLinkMicMgr.kickOutLinkMicMember(strUserID);
                mMapLinkMicMember.remove(strUserID);

                item.stopPlay(true);
                item.empty();

                if (mMapLinkMicMember.size() == 0) {
                    //无人连麦，设置视频质量：高清
                    mTXLivePusher.setVideoQuality(TXLiveConstants.VIDEO_QUALITY_HIGH_DEFINITION);
                }
                if (json.length() > 0)
                    for (int i = 0; i < json.length(); i++) {
                        try {
                            if (json.getJSONObject(i).getString("userid").equals(strUserID)) {
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                                    json.remove(i);
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                Toast.makeText(getApplicationContext(), "连麦超时", Toast.LENGTH_SHORT).show();
            }
        }

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

    private class ListViewAdapter extends BaseAdapter {
        JSONArray res = AppConfig.LIVE_TIME_COIN;

        @Override
        public int getCount() {
            return res.length();
        }

        @Override
        public Object getItem(int position) {
            try {
                return res.getString(position);
            } catch (JSONException e) {
                e.printStackTrace();
                return "";
            }
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            final ListViewAdapter.ViewHolder viewHolder;
            if (convertView == null) {
                convertView = View.inflate(AppContext.getInstance(), R.layout.item_ready_charge, null);
                viewHolder = new ListViewAdapter.ViewHolder();

                viewHolder.mChargeCoin = (TextView) convertView.findViewById(R.id.tv_item_chargecoin);

                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ListViewAdapter.ViewHolder) convertView.getTag();
            }
            try {
                viewHolder.mChargeCoin.setText(res.getString(position) + AppConfig.CURRENCY_NAME + "/分钟");
                viewHolder.mChargeCoin.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        viewHolder.mChargeCoin.setTextColor(getResources().getColor(R.color.maintone));
                        try {
                            type_val = res.getString(position);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        if (last_item != -1 && last_item != position) {
                            //如果已经单击过条目并且上次保存的item位置和当前位置不同
                            if (oldView != null)
                                oldView.setTextColor(getResources().getColor(R.color.black));//把上次选中的样式去掉
                        }
                        last_item = position;//把当前的位置保存下来
                        oldView = (TextView) v.findViewById(R.id.tv_item_chargecoin);//把当前的条目保存下来


                    }
                });
            } catch (JSONException e) {
                e.printStackTrace();
            }


            return convertView;
        }

        class ViewHolder {
            TextView mChargeCoin;
        }
    }

    //私播
    Dialog siBoDialog;

    private void sibo(String head, String name, final String uid) {
        siBoDialog = LiveCommon.showPersonLianMaiDialog(this, head, name, new com.duomizhibo.phonelive.interf.DialogInterface() {
            @Override
            public void cancelDialog(View v, Dialog d) {
                mChatServer.doSendTinkMicRefuse(mUser, uid);
                mHasPendingRequest = false;
                mHandler.removeCallbacks(mTimeRunnable);
                d.dismiss();
            }

            @Override
            public void determineDialog(View v, Dialog d) {
                mHandler.removeCallbacks(mTimeRunnable);
                TLog.error("接受json" + json);
                mChatServer.doSendTinkMicAgree(mUser, uid, json);
                for (TCLinkMicPlayItem item : mVecPlayItems) {
                    if (item.mUserID == null || item.mUserID.length() == 0) {
                        item.mUserID = uid;
                        item.mPending = true;
                        item.startLoading();
//                        //10.26
//                        item.stopLoading(true);
//                        //混流：增加一路
//                        TCStreamMergeMgr.getInstance().addSubVideoStream(item.mPlayUrl);
                        //加入连麦成员列表
                        mMapLinkMicMember.put(uid, "");
                        //第一个小主播加入连麦，设置视频质量：连麦大主播
                        if (mMapLinkMicMember.size() == 1) {
                            mNeedResetVideoQuality = false;
                            mTXLivePusher.setVideoQuality(TXLiveConstants.VIDEO_QUALITY_LINKMIC_MAIN_PUBLISHER);

                        }
                        break;
                    }
                }
                d.dismiss();
                mHasPendingRequest = false;
            }
        });
    }
}

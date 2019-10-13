package com.duomizhibo.phonelive.ui;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.media.AudioManager;
import android.media.MediaMetadataRetriever;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.hyphenate.chat.EMChatManager;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.tencent.rtmp.ITXLivePlayListener;
import com.tencent.rtmp.TXLiveConstants;
import com.tencent.rtmp.TXLivePlayConfig;
import com.tencent.rtmp.TXLivePlayer;
import com.tencent.rtmp.ui.TXCloudVideoView;
import com.duomizhibo.phonelive.AppConfig;
import com.duomizhibo.phonelive.AppContext;
import com.duomizhibo.phonelive.R;
import com.duomizhibo.phonelive.TCConstants;
import com.duomizhibo.phonelive.api.remote.ApiUtils;
import com.duomizhibo.phonelive.api.remote.PhoneLiveApi;
import com.duomizhibo.phonelive.base.ToolBarBaseActivity;
import com.duomizhibo.phonelive.bean.ActiveBean;
import com.duomizhibo.phonelive.bean.SimpleUserInfo;
import com.duomizhibo.phonelive.bean.UserInfo;
import com.duomizhibo.phonelive.fragment.CommentDialogFragment;
import com.duomizhibo.phonelive.fragment.CommentFragment;
import com.duomizhibo.phonelive.fragment.UserInfoDialogFragment;
import com.duomizhibo.phonelive.fragment.VideoShareFragment;
import com.duomizhibo.phonelive.ui.dialog.LiveCommon;
import com.duomizhibo.phonelive.utils.InputMethodUtils;
import com.duomizhibo.phonelive.utils.ShareUtils;
import com.duomizhibo.phonelive.utils.TDevice;
import com.duomizhibo.phonelive.utils.UIHelper;
import com.duomizhibo.phonelive.widget.AvatarView;
import com.duomizhibo.phonelive.widget.LoadUrlImageView;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import okhttp3.Call;

/*

* 直播播放页面
* */
public class SmallVideoPlayerActivity extends ToolBarBaseActivity implements View.OnLayoutChangeListener, ITXLivePlayListener,VideoShareFragment.deleteClick {

    public final static String USER_INFO = "USER_INFO";

    @InjectView(R.id.video_view)
    protected TXCloudVideoView mTXCloudVideoView;
    //加载中的背景图
    @InjectView(R.id.iv_live_look_loading_bg)
    protected LoadUrlImageView mIvLoadingBg;

    @InjectView(R.id.tv_attention)
    protected ImageView mIvAttention;

    @InjectView(R.id.iv_live_emcee_head)
    protected AvatarView mAvEmcee;

    @InjectView(R.id.tv_video_commrntnum)
    protected TextView mTvCommentNum;

    @InjectView(R.id.tv_video_laudnum)
    protected TextView mTvLaudNum;

    @InjectView(R.id.iv_video_laud)
    protected ImageView mIvLaud;

    @InjectView(R.id.iv_video_laudgif)
    protected ImageView mIvGif;

    @InjectView(R.id.tv_name)
    protected TextView mUName;

    @InjectView(R.id.title)
    protected TextView mTitle;

    @InjectView(R.id.btn_cai)
    protected ImageView mCai;

    @InjectView(R.id.share_nums)
    protected TextView mShareCount;//分享数


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


    private CommentFragment mCommentFragment;


    private UserInfo mUserInfo;

    ActiveBean videoBean;

    String uid;

    private Handler mHandler;

    boolean isShowGif = true;

    private UserInfoDialogFragment mUserInfoDialog;

    private int mScreenHeight;

    private EMChatManager mChatManager;

    SimpleUserInfo mEmceeInfo = new SimpleUserInfo();

    private CommentDialogFragment mDialogFragment;

    private int mIsLike = -1;

    private MediaMetadataRetriever mmr;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_video_show;
    }

    @Override
    public void initView() {

        mTXCloudVideoView.setOnClickListener(mClickListener);
        findViewById(R.id.rl_live_root).getViewTreeObserver().addOnGlobalLayoutListener(mOnGlobalLayoutListenernew);
    }

    private ViewTreeObserver.OnGlobalLayoutListener mOnGlobalLayoutListenernew = new ViewTreeObserver.OnGlobalLayoutListener() {
        @Override
        public void onGlobalLayout() {
            Rect r = new Rect();
            //获取当前界面可视部分
            SmallVideoPlayerActivity.this.getWindow().getDecorView().getWindowVisibleDisplayFrame(r);
            //获取屏幕的高度
            if (mScreenHeight == 0) {
                mScreenHeight = r.height();
            }
            int visibleHeight = r.height();
            if (visibleHeight == mScreenHeight) {
                if (mCommentFragment != null) {
                    mCommentFragment.onSoftInputHide();
                }
            } else {
                if (mCommentFragment != null) {
                    mCommentFragment.onSoftInputShow(visibleHeight);
                }
            }
        }
    };


    @Override
    public void initData() {


        Bundle bundle = getIntent().getBundleExtra(USER_INFO);

        videoBean = (ActiveBean) bundle.getSerializable(USER_INFO);

        mUserInfo = videoBean.getUserinfo();

        mEmceeInfo.id = videoBean.getUid();
        mEmceeInfo.user_nicename = videoBean.getUserinfo().getUser_nicename();

        mUName.setText(mEmceeInfo.user_nicename);
        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (mIvGif != null && mIvGif.getVisibility() == View.VISIBLE) {
                    mIvGif.setVisibility(View.GONE);
                }
            }
        };

        uid = AppContext.getInstance().getLoginUid();

        mChatManager = EMClient.getInstance().chatManager();

        this.setVolumeControlStream(AudioManager.STREAM_MUSIC);

        //初始化房间信息
        initRoomInfo();
        mUserInfoDialog = new UserInfoDialogFragment();

        PhoneLiveApi.getVideoInfo(videoBean.getId(), new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {

            }

            @Override
            public void onResponse(String response, int id) {
                try {
                    JSONObject obj = new JSONObject(response);
                    if ("200".equals(obj.getString("ret"))) {
                        JSONObject data = obj.getJSONObject("data");
                        if (0 == data.getInt("code")) {
                            JSONObject info0 = data.getJSONArray("info").getJSONObject(0);
                            mIsLike = info0.getInt("islike");
                            if (1 == mIsLike) {
                                mIvLaud.setBackgroundResource(R.drawable.lauded);
                            }
                            if (info0.getInt("isattent") == 1) {
                                mIvAttention.setVisibility(View.GONE);
                            }
                            String comments = info0.getString("comments");
                            String shares = info0.getString("shares");
                            String likes = info0.getString("likes");
                            mTvLaudNum.setText(likes);
                            mTvCommentNum.setText(comments);
                            mShareCount.setText(shares);
                        }
                    } else {
                        AppContext.toast("获取数据失败");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    protected void startPlay(String mPlayUrl, int playType) {
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
            closePlayer();
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
            } else if ((mPlayUrl.startsWith("http://"))) {
                mPlayType = TXLivePlayer.PLAY_TYPE_VOD_MP4;
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

    public void setShareCount(String s) {
        mShareCount.setText(s);
    }

    public void setCommentNum(String s) {
        mTvCommentNum.setText(s);
    }

    private void initRoomInfo() {

        //设置背景图
        mIvLoadingBg.setVisibility(View.VISIBLE);
        mIvLoadingBg.setImageLoadUrl(videoBean.getThumb());

        mAvEmcee.setAvatarUrl(mUserInfo.getAvatar());

        // mTvCommentNum.setText(videoBean.getComments());

        //  mTvLaudNum.setText(videoBean.getLikes());

        // mShareCount.setText(videoBean.getShares());

        mTitle.setText(videoBean.getTitle());

        if (!uid.equals(videoBean.getUid())) {
            if ("1".equals(videoBean.getIslike())) {
                mIvLaud.setBackgroundResource(R.drawable.lauded);
            }

            if ("1".equals(videoBean.getIsattent())) {
                mIvAttention.setVisibility(View.GONE);
            }
            if (1 == videoBean.getIsstep()) {
                mCai.setImageResource(R.drawable.icon_video_cai_selected);
            }
        } else {
            mIvAttention.setVisibility(View.GONE);
            mCai.setVisibility(View.GONE);
        }
        mPlayUrl = videoBean.getHref();
        //初始化直播播放器参数配置
        checkPlayUrl();

        startPlay(mPlayUrl, mPlayType);
    }

    @OnClick({R.id.btn_cai, R.id.btn_comment, R.id.iv_video_more, R.id.iv_video_comment, R.id.iv_video_laud, R.id.iv_video_share, R.id.iv_video_close, R.id.tv_attention})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_live_room_info://左上角点击主播信息
                UIHelper.showHomePageActivity(SmallVideoPlayerActivity.this, videoBean.getUid());
                break;
            case R.id.tv_attention:
                //关注主播
                PhoneLiveApi.showFollow(AppContext.getInstance().getLoginUid(), videoBean.getUid(), AppContext.getInstance().getToken(), new PhoneLiveApi.AttentionCallback() {
                    @Override
                    public void callback(boolean isAttention) {
                        mIvAttention.setVisibility(View.GONE);
                        showToast2("关注成功");
                    }
                });
                break;
            case R.id.iv_video_comment:
                showCommentDialog();
                break;
            case R.id.btn_comment:
                showCommentDialog2();
                break;
            case R.id.iv_video_share:
            case R.id.iv_video_more:
                // showSharePopWindow(SmallVideoPlayerActivity.this, v, mEmceeInfo);
                showSharePopWindow2();
                break;
            case R.id.iv_video_laud:
                if (mIsLike == 0) {
                    showLaudGif();
                }
                addLikes();
                break;
            case R.id.iv_video_close:
                closePlayer();
                break;
            case R.id.btn_cai:
                cai();
                break;
        }
    }

    private void cai() {
        if (videoBean.getIsstep() == 1) {
            return;
        }
        PhoneLiveApi.addVideoStep(videoBean.getId(), new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {

            }

            @Override
            public void onResponse(String response, int id) {
                try {
                    JSONObject obj = new JSONObject(response);
                    if ("200".equals(obj.getString("ret"))) {
                        JSONObject data = obj.getJSONObject("data");
                        if (0 == data.getInt("code")) {
                            int isstep = data.getJSONArray("info").getJSONObject(0).getInt("isstep");
                            if (isstep == 1) {
                                videoBean.setIsstep(1);
                                mCai.setImageResource(R.drawable.icon_video_cai_selected);
                            }
                        }
                        AppContext.toast(data.getString("msg"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {

        }
    }


    @Override
    public void onPlayEvent(int i, Bundle bundle) {
        if (i == TXLiveConstants.PLAY_EVT_PLAY_BEGIN) {
            if (mIvLoadingBg != null) {
                mIvLoadingBg.setVisibility(View.GONE);
            }
        }
        if (i == TXLiveConstants.PLAY_EVT_PLAY_END) {
            //循环播放
            if (mTXLivePlayer != null) {
                mTXLivePlayer.seek(0);
                mTXLivePlayer.resume();
            }
        }
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


    private View.OnClickListener mClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (mIsLike == 0) {
                addLikes();
            }
            showLaudGif();
        }
    };

    private void showLaudGif() {
        if (mIvGif.getVisibility() == View.GONE) {
            mIvGif.setVisibility(View.VISIBLE);
            Glide.with(this).load(R.drawable.laud_gif).asGif().diskCacheStrategy(DiskCacheStrategy.SOURCE).into(mIvGif);
            mHandler.sendEmptyMessageDelayed(0, 2000);
        }
    }

    private void addLikes() {
        PhoneLiveApi.addLike(uid, videoBean.getId(), new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {

            }

            @Override
            public void onResponse(String response, int id) {
                JSONArray res = ApiUtils.checkIsSuccess(response);
                if (res != null) {
                    try {
                        if(videoBean!=null){
                            videoBean.setIslike(res.getJSONObject(0).getString("islike"));
                            videoBean.setLikes(res.getJSONObject(0).getString("likes"));
                        }
                        if(mTvLaudNum!=null){
                            mTvLaudNum.setText(videoBean.getLikes());
                        }
                        if(mIvLaud!=null){
                            mIsLike = res.getJSONObject(0).getInt("islike");
                            if (mIsLike == 1) {
                                mIvLaud.setBackgroundResource(R.drawable.lauded);
                            } else {
                                mIvLaud.setBackgroundResource(R.drawable.nolaud);
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            }
        });
    }

    //评论
    private void showCommentDialog() {
        mCommentFragment = new CommentFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("bean", videoBean);
        mCommentFragment.setArguments(bundle);
        mCommentFragment.show(getSupportFragmentManager(), "CommentFragment");
    }


    //评论
    private void showCommentDialog2() {
        if (mDialogFragment == null) {
            mDialogFragment = new CommentDialogFragment();
            Bundle bundle = new Bundle();
            bundle.putSerializable("bean", videoBean);
            mDialogFragment.setArguments(bundle);
        }
        mDialogFragment.show(getSupportFragmentManager(), "CommentDialogFragment");
    }

    //视频结束释放资源
    private void videoPlayerEnd() {
        if (mTXLivePlayer != null) {
            mTXLivePlayer.setPlayListener(null);
            mTXLivePlayer.stopPlay(true);
            mTXLivePlayer = null;
        }
        if (mHandler != null) {
            mHandler.removeCallbacksAndMessages(null);
            mHandler = null;
        }
        if (mTXCloudVideoView != null) {
            mTXCloudVideoView = null;
        }
        if (mIvLoadingBg != null) {
            mIvLoadingBg = null;
        }
        if (mCommentFragment != null) {
            mCommentFragment = null;
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
                //如果聊天窗口开启,收起软键盘时关闭聊天输入changeEditStatus(false);
            }
        }
    }


    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }


    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onPause() {
        super.onPause();


    }

    private void closePlayer(){
        videoPlayerEnd();
        finish();
    }

    @Override
    public void onBackPressed() {
        closePlayer();
    }

    @Override
    protected void onDestroy() {//释放
        super.onDestroy();
        findViewById(R.id.rl_live_root).getViewTreeObserver().removeGlobalOnLayoutListener(mOnGlobalLayoutListenernew);
        //解除广播
        OkHttpUtils.getInstance().cancelTag("initRoomInfo");
        ButterKnife.reset(this);
    }

    @Override
    protected boolean hasActionBar() {
        return false;
    }

    public static void startSmallVideoPlayerActivity(final Context context, final ActiveBean live) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("USER_INFO", live);
        UIHelper.showSmallLookLiveActivity(context, bundle);
    }

    public void sendEMMessage(String isfollow, String content, String touid) {
        EMMessage message = EMMessage.createTxtSendMessage(content, touid);
        message.setAttribute("isfollow", isfollow);
        mChatManager.sendMessage(message);
    }

    private void showReportDialog() {
        final Dialog dialog = new Dialog(SmallVideoPlayerActivity.this, R.style.dialog_no_background);
        dialog.setContentView(R.layout.dialog_report);
        Window dialogWindow = dialog.getWindow();
        dialogWindow.setGravity(Gravity.BOTTOM);
        dialogWindow.setWindowAnimations(R.style.dialogstyle); // 添加动画
        WindowManager.LayoutParams lp = dialogWindow.getAttributes(); // 获取对话框当前的参数值
        lp.width = (int) getResources().getDisplayMetrics().widthPixels - (int) TDevice.dpToPixel(15); // 宽度
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT; // 高度
        dialogWindow.setAttributes(lp);
        dialog.setCanceledOnTouchOutside(true);
        LinearLayout mLlReport = (LinearLayout) dialog.findViewById(R.id.ll_video_report);
        TextView mTvReport = (TextView) dialog.findViewById(R.id.tv_type);
        if (uid.equals(videoBean.getUid())) {
            mTvReport.setText("删除视频");
        } else {
            mTvReport.setText("举报该视频");
        }
        LinearLayout mLlCancel = (LinearLayout) dialog.findViewById(R.id.ll_viedo_cancel);
        mLlReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (uid.equals(videoBean.getUid())) {
                    PhoneLiveApi.setVideoRel(uid, AppContext.getInstance().getToken(), videoBean.getId(), new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {

                        }

                        @Override
                        public void onResponse(String response, int id) {
                            JSONArray res = ApiUtils.checkIsSuccess(response);
                            if (res != null) {
                                dialog.dismiss();
                                showToast3("删除成功", 0);
                                closePlayer();
                            }
                        }
                    });
                } else {
                    PhoneLiveApi.setVideoReport(uid, AppContext.getInstance().getToken(), videoBean.getId(), new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {

                        }

                        @Override
                        public void onResponse(String response, int id) {
                            JSONArray res = ApiUtils.checkIsSuccess(response);
                            if (res != null) {
                                showToast3("感谢您的举报,我们会尽快做出处理...", 0);
                                dialog.dismiss();
                            }
                        }
                    });
                }
            }
        });
        mLlCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    //分享pop弹窗
    private void showSharePopWindow(final Context context, View v, final SimpleUserInfo mUser) {

        View view = LayoutInflater.from(context).inflate(R.layout.pop_view_share, null);
        PopupWindow p = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        view.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        p.setBackgroundDrawable(new BitmapDrawable());
        p.setOutsideTouchable(true);
        LinearLayout mLlShare = (LinearLayout) view.findViewById(R.id.ll_live_shar);
        for (int i = 0; i < AppConfig.SHARE_TYPE.length(); i++) {
            final ImageView im = new ImageView(context);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams((int) TDevice.dpToPixel(40), (int) TDevice.dpToPixel(60));
            if (i > 0)
                lp.setMargins((int) TDevice.dpToPixel(15), 0, 0, 0);
            im.setLayoutParams(lp);
            try {
                im.setImageResource(context.getResources().getIdentifier(AppConfig.SHARE_TYPE.getString(i) + "_share", "drawable", "com.duomizhibo.phonelive"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            mLlShare.addView(im);
            final int finalI = i;
            im.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ShareUtils.share((Activity) context, finalI, mUser);
                }
            });
        }

        int[] location = new int[2];
        v.getLocationOnScreen(location);
        //p.showAtLocation(v, Gravity.NO_GRAVITY,location[0] + v.getWidth()/2 - view.getMeasuredWidth()/2,location[1]- view.getMeasuredHeight());
        p.showAtLocation(v, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
    }


    private void showSharePopWindow2() {
        VideoShareFragment f = new VideoShareFragment(this);
        Bundle bundle = new Bundle();
        bundle.putSerializable("bean", videoBean);
        f.setArguments(bundle);
        f.show(getSupportFragmentManager(), "VideoShareFragment");
    }

    private Dialog mLoadingDialog;

    public void showLoadingDialog() {
        if (mLoadingDialog == null) {
            mLoadingDialog = LiveCommon.loadingDialog(this);
        }
        mLoadingDialog.show();
    }

    public void hideLoadingDialog() {
        if (mLoadingDialog != null) {
            mLoadingDialog.dismiss();
        }
    }

    @Override
    public void delete() {
        mTXLivePlayer.stopPlay(true);
    }
}

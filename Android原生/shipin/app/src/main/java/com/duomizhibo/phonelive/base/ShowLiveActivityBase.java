package com.duomizhibo.phonelive.base;

import android.app.Activity;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.RectF;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.AppCompatSeekBar;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.style.ForegroundColorSpan;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.google.gson.Gson;
import com.hyphenate.chat.EMClient;
import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.AnimatorSet;
import com.nineoldandroids.animation.ObjectAnimator;
import com.nineoldandroids.animation.PropertyValuesHolder;
import com.duomizhibo.phonelive.AppConfig;
import com.duomizhibo.phonelive.AppContext;
import com.duomizhibo.phonelive.R;
import com.duomizhibo.phonelive.adapter.ChatListAdapter;
import com.duomizhibo.phonelive.adapter.UserListAdapter;
import com.duomizhibo.phonelive.api.remote.ApiUtils;
import com.duomizhibo.phonelive.api.remote.PhoneLiveApi;
import com.duomizhibo.phonelive.bean.ChatBean;
import com.duomizhibo.phonelive.bean.SendGiftBean;
import com.duomizhibo.phonelive.bean.SimpleUserInfo;
import com.duomizhibo.phonelive.bean.UserBean;
import com.duomizhibo.phonelive.event.ChatExitEvent;
import com.duomizhibo.phonelive.fragment.LiveEmceeEndFragmentDialog;
import com.duomizhibo.phonelive.fragment.LiveEndFragmentDialog;
import com.duomizhibo.phonelive.fragment.MessageFragment;
import com.duomizhibo.phonelive.fragment.UserInfoDialogFragment;
import com.duomizhibo.phonelive.interf.DialogInterface;
import com.duomizhibo.phonelive.model.Danmu;
import com.duomizhibo.phonelive.ui.customviews.ErrorDialogFragment;
import com.duomizhibo.phonelive.ui.other.ChatServer;
import com.duomizhibo.phonelive.ui.other.DrawableRes;
import com.duomizhibo.phonelive.ui.other.LiveStream;
import com.duomizhibo.phonelive.ui.other.PhoneLivePrivateChat;
import com.duomizhibo.phonelive.utils.DialogHelp;
import com.duomizhibo.phonelive.utils.InputMethodUtils;
import com.duomizhibo.phonelive.utils.LiveUtils;
import com.duomizhibo.phonelive.utils.ShareUtils;
import com.duomizhibo.phonelive.utils.StringUtils;
import com.duomizhibo.phonelive.utils.TDevice;
import com.duomizhibo.phonelive.utils.TLog;
import com.duomizhibo.phonelive.utils.ThreadManager;
import com.duomizhibo.phonelive.viewpagerfragment.PrivateChatCorePagerDialogFragment;
import com.duomizhibo.phonelive.widget.AvatarView;
import com.duomizhibo.phonelive.widget.BlackButton;
import com.duomizhibo.phonelive.widget.BlackEditText;
import com.duomizhibo.phonelive.widget.BlackTextView;
import com.duomizhibo.phonelive.widget.MyAnimationDrawable;
import com.duomizhibo.phonelive.widget.SpaceRecycleView;
import com.duomizhibo.phonelive.widget.VerticalImageSpan;
import com.duomizhibo.phonelive.widget.danmu.DanmuControl;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.lang.ref.SoftReference;

import butterknife.InjectView;
import master.flame.danmaku.controller.IDanmakuView;
import okhttp3.Call;
import rx.Observable;
import rx.Subscriber;
import rx.functions.Action1;

/**
 * @athor 魏鹏
 * @dw 直播间基类 本类主要封装了直播间豪华礼物动画 和一些公共逻辑
 */
public class ShowLiveActivityBase extends ToolBarBaseActivity {

    @InjectView(R.id.chronometer)
    protected Chronometer chronometer;

    @InjectView(R.id.ll_time)
    protected LinearLayout ll_time;
    @InjectView(R.id.rl_live_root)
    protected RelativeLayout mRoot;

    @InjectView(R.id.rl_live_gift)
    protected RelativeLayout mRlLiveGift;

    //连送礼物动画显示区
    @InjectView(R.id.ll_show_gift_animator)
    protected LinearLayout mShowGiftAnimator;

    @InjectView(R.id.view_live_content)
    protected RelativeLayout mLiveContent;

    //观众数量
    @InjectView(R.id.tv_live_num)
    protected BlackTextView mTvLiveNum;

    @InjectView(R.id.tv_yingpiao_num)
    protected BlackTextView mTvYpNum;

    //聊天listView
    @InjectView(R.id.lv_live_room)
    protected ListView mLvChatList;

    //点击chat按钮
    @InjectView(R.id.iv_live_chat)
    protected ImageView mLiveChat;

    //左上角主播head
    @InjectView(R.id.iv_live_emcee_head)
    protected AvatarView mEmceeHead;

    //底部menu
    @InjectView(R.id.ll_bottom_menu)
    protected RelativeLayout mButtonMenu;

    @InjectView(R.id.fl_bottom_menu)
    protected RelativeLayout mButtonMenuFrame;

    //chatInput
    @InjectView(R.id.ll_live_chat_edit)
    protected LinearLayout mLiveChatEdit;

    @InjectView(R.id.ll_yp_labe)
    protected LinearLayout mLiveLade;

    @InjectView(R.id.et_live_chat_input)
    protected BlackEditText mChatInput;

    @InjectView(R.id.tv_live_number)
    protected BlackTextView mTvLiveNumber;

    //观众列别listView
    @InjectView(R.id.hl_room_user_list)
    protected RecyclerView mRvUserList;


    @InjectView(R.id.tglbtn_danmu_setting)
    protected BlackButton mBtnDanMu;

    @InjectView(R.id.tv_live_join_room_animation)
    TextView mTvJoinRoomAnimation;

    @InjectView(R.id.ll_live_gif)
    protected LinearLayout mLlGif;

    @InjectView(R.id.iv_live_gif)
    protected ImageView mIvGif;

    @InjectView(R.id.tv_live_gif_words)
    protected TextView mTvGif;

    @InjectView(R.id.ll_live_all)
    protected LinearLayout mLlPan;

    @InjectView(R.id.iv_live_new_message)
    protected TextView mUnReadRedPoint;

    //弹幕控制 HHH
    protected DanmuControl mDanmuControl;

    @InjectView(R.id.danmakuView)
    protected IDanmakuView mDanmakuView;
    //连麦
    @InjectView(R.id.iv_live_rtc)
    protected ImageView mRtcView;
    //设置
    @InjectView(R.id.iv_live_camera_control)
    protected ImageView mIvCameraControl;
    //美颜
    @InjectView(R.id.iv_live_meiyan)
    protected ImageView mIvLiveMeiyan;

    //主播等级
    @InjectView(R.id.iv_live_emcee_level)
    protected ImageView mEmceeLevel;

    //闪光灯开启状态
    protected boolean flashingLightOn;

    protected boolean mBeauty = false;

    protected Gson mGson = new Gson();

    //当前正在显示的两个动画
    protected Map<Integer, View> mGiftShowNow = new HashMap<>();

    //礼物消息队列
    protected Map<Integer, SendGiftBean> mGiftShowQueue = new HashMap();

    //进入房间动画列队
    protected List<UserBean> mJoinRoomAnimationQueue = new ArrayList<>();

    //礼物队列
    protected List<SendGiftBean> mLuxuryGiftShowQueue = new ArrayList<>();

    //动画是否播放完毕
    protected boolean giftAnimationPlayEnd = true;

    protected int mShowGiftFirstUid = 0, mShowGiftSecondUid = 0;

    //socket服务器连接状态
    public boolean mConnectionState = false;

    //聊天adapter
    protected ChatListAdapter mChatListAdapter;

    //用户列表adapter
    protected UserListAdapter mUserListAdapter;

    //聊天list
    protected List<ChatBean> mListChats = new ArrayList<>();

    //用户列表list
    protected List<SimpleUserInfo> mUserList = new ArrayList<>();

    //socket
    public ChatServer mChatServer;

    protected UserBean mUser;

    protected int ACE_TEX_TO_USER = 0;

    protected Handler mHandler;
    //屏幕宽度
    protected int mScreenWidth, mScreenHeight;

    protected Random mRandom = new Random();
    //房间号码
    protected String mRoomNum, mStreamName;

    protected boolean mDanMuIsOpen = false;

    protected BroadcastReceiver broadCastReceiver;
    //礼物
    protected View mGiftView;

    protected int barrageFee;

    private UserInfoDialogFragment mUserInfoDialog;

    boolean isHandlerTime = true;

    boolean isReq = false;


    public boolean isCanLit = true;

    public boolean isCanShowLit = true;

    //号码，0表示无靓号
    public String goodNum;

    public String liveNum = "0";

    //VIP类型，0表示无VIP，1表示普通VIP，2表示至尊VIP
    public String vipType = "0";


    private MessageFragment.ChatStateObserver mChatStateObserver;
    private MessageFragment mMessageFragment;

    @Override
    public void initData() {
        //屏幕常量
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        mHandler = new Handler();
        mUserInfoDialog = new UserInfoDialogFragment();

    }



    @Override
    public void initView() {
        setTextBold(mTvGif);
        mChatListAdapter = new ChatListAdapter(this);
        mChatListAdapter.setChats(mListChats);
        mLvChatList.setAdapter(mChatListAdapter);

        final LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(OrientationHelper.HORIZONTAL);
        mRvUserList.setLayoutManager(manager);

        mLvChatList.setLayoutParams(new LinearLayout.LayoutParams((int) (TDevice.getScreenWidth() / 5 * 3),(int)TDevice.dpToPixel(150)));
        //BBB设置每个item间距
        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.space_5);
        mRvUserList.addItemDecoration(new SpaceRecycleView(spacingInPixels));
        mRvUserList.setAdapter(mUserListAdapter = new UserListAdapter(getLayoutInflater()));
        mScreenWidth = (int) TDevice.getScreenWidth();
        mScreenHeight = (int) TDevice.getScreenHeight();

        ((TextView) findViewById(R.id.tv_live_tick_name)).setText(AppConfig.TICK_NAME);

        mTvJoinRoomAnimation.setX(mScreenWidth);
        //聊天listView点击事件注册
        mLvChatList.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    changeEditStatus(false);
                }
                return false;
            }
        });

        //用户列表点击事件
        mUserListAdapter.setOnItemClickListener(new UserListAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                //展示用户详细信息弹窗
                if (!mUserInfoDialog.isAdded()) {
                    showUserInfoDialog(mUserList.get(position));
                }

            }
        });


        //弹幕控制器和视图关联 HHH
        mDanmuControl = new DanmuControl(this);
        mDanmuControl.setDanmakuView(mDanmakuView);

        mLvChatList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                chatListItemClick(mListChats.get(position));
            }
        });
        mErrDlgFragment = new ErrorDialogFragment();

        mLiveContent.setOnTouchListener(mTouchListener);


        mChatStateObserver = new MessageFragment.ChatStateObserver() {
            @Override
            public void onMessageCountChanged(int followUnReadCount, int notFollowUnReadCount) {
                int unReadCount = followUnReadCount + notFollowUnReadCount;
                showReadCount(unReadCount);
            }

            @Override
            public void onComeBack(ChatExitEvent e) {
                if (e != null && mMessageFragment != null) {
                    mMessageFragment.onChatBack(e);
                }
            }

            @Override
            public void onIgnoreUnReadMessage() {
                if (mUnReadRedPoint.getVisibility() == View.VISIBLE) {
                    mUnReadRedPoint.setVisibility(View.GONE);
                }
                mMessageFragment.onIgnoreUnReadMessage();
            }
        };
        MessageFragment.addChatStateObserver(mChatStateObserver);
        MessageFragment.chatExitEvent = null;
        int unReadCount = EMClient.getInstance().chatManager().getUnreadMessageCount();
        showReadCount(unReadCount);

    }

    //检查未读消息数量
    private void showReadCount(int unReadCount) {
        if (unReadCount > 0 && !MessageFragment.ignoreUnReadMessage) {
            if (mUnReadRedPoint.getVisibility() == View.GONE) {
                mUnReadRedPoint.setVisibility(View.VISIBLE);
            }
            mUnReadRedPoint.setText(unReadCount + "");
        } else {
            if (mUnReadRedPoint.getVisibility() == View.VISIBLE) {
                mUnReadRedPoint.setVisibility(View.GONE);
            }
        }
    }

    //关闭或者开启弹幕功能
    protected void openOrCloseDanMu() {
        mDanMuIsOpen = !mDanMuIsOpen;
        if (mDanMuIsOpen) {
            mDanmuControl.show();
            if (mChatInput.getText().toString().equals("")) {
                mChatInput.setHint("开启大喇叭，" + barrageFee + AppConfig.CURRENCY_NAME + "/条");
            }

        } else {
            mDanmuControl.hide();
            mChatInput.setHint("");
        }
        mBtnDanMu.setBackgroundResource(mDanMuIsOpen ? R.drawable.tuanmubutton1 : R.drawable.tanmubutton);
    }


    //发送聊天
    protected void sendChat() {
        String sendMsg = mChatInput.getText().toString();
        sendMsg = sendMsg.trim();
        if (mConnectionState && !sendMsg.equals("")) {
            mChatInput.setText("");
            mChatServer.doSendMsg(sendMsg, mUser, ACE_TEX_TO_USER, goodNum, vipType);

        }
    }

    @Override
    protected boolean hasActionBar() {
        return false;
    }

    //进入显示礼物队列信息初始化
    protected View initShowEvenSentSendGift(SendGiftBean mSendGiftInfo) {
        View view = getLayoutInflater().inflate(R.layout.item_show_gift_animator, null);
        if (mShowGiftFirstUid == 0) {
            mShowGiftFirstUid = mSendGiftInfo.getUid();
        } else {
            mShowGiftSecondUid = mSendGiftInfo.getUid();
        }
        mGiftShowNow.put(mSendGiftInfo.getUid(), view);
        return view;
    }


    //定时检测当前显示礼物是否超时过期
    protected boolean timingDelGiftAnimation(int index) {

        int id = index == 1 ? mShowGiftFirstUid : mShowGiftSecondUid;

        SendGiftBean mSendGiftInfo = mGiftShowQueue.get(id);

        if (mSendGiftInfo != null) {

            long time = System.currentTimeMillis() - mSendGiftInfo.getSendTime();
            if ((time > 4000) && (mShowGiftAnimator != null)) {
                //超时 从礼物队列和显示队列中移除
                mShowGiftAnimator.removeView(mGiftShowNow.get(id));

                mGiftShowQueue.remove(id);

                mGiftShowNow.remove(id);
                if (index == 1) {
                    mShowGiftFirstUid = 0;
                } else {
                    mShowGiftSecondUid = 0;
                }
                //从礼物队列中获取一条新的礼物信息进行显示
                if (mGiftShowQueue.size() != 0) {

                    Iterator iterator = mGiftShowQueue.entrySet().iterator();
                    while (iterator.hasNext()) {
                        Map.Entry entry = (Map.Entry) iterator.next();
                        SendGiftBean sendGift = (SendGiftBean) entry.getValue();

                        if (mShowGiftFirstUid != sendGift.getUid() && mShowGiftSecondUid != sendGift.getUid()) {//判断队列中的第一个礼物是否已经正在显示
                            showEvenSentGiftAnimation(initShowEvenSentSendGift(sendGift), sendGift, 1);
                            break;
                        }
                    }
                }
                return false;
            } else {
                return true;
            }
        }
        return true;
    }

    /**
     * @param
     * @dw 点亮
     */
    protected void showLit() {
        isCanShowLit = false;

        final ImageView heart = new ImageView(ShowLiveActivityBase.this);
        heart.setTag("heart");
        //点亮的背景图片

        heart.setBackgroundResource(ChatServer.heartImg[mRandom.nextInt(3)]);
        //尺寸参数
        heart.setLayoutParams(new RelativeLayout.LayoutParams((int) TDevice.dpToPixel(30)
                , (int) TDevice.dpToPixel(30)));
        //x位置
        heart.setX(mScreenWidth - mScreenWidth / 3);
        //y位置
        heart.setY(mScreenHeight - 200);
        mRlLiveGift.addView(heart);
        //点亮xy 平移动画
        ObjectAnimator translationX = ObjectAnimator.ofFloat(heart, "translationX", mRandom.nextInt(500) + (mScreenWidth - 200) - (mScreenWidth / 3));
        ObjectAnimator translationY = ObjectAnimator.ofFloat(heart, "translationY", mRandom.nextInt(mScreenHeight / 2) + 200);
        //渐变动画
        ObjectAnimator alpha = ObjectAnimator.ofFloat(heart, "alpha", 0f);
        //xy放大动画
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(heart, "scaleX", 0.8f, 1f);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(heart, "scaleY", 0.8f, 1f);

        AnimatorSet set = new AnimatorSet();
        set.playTogether(translationX, translationY, alpha, scaleX, scaleY);
        set.setDuration(5000);

        set.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if (null != mRlLiveGift) {
                    mRlLiveGift.removeView(heart);
                }


            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        set.start();


        if (mHandler == null) return;
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                isCanShowLit = true;
            }
        }, 800);
    }

    /**
     * @param index 点亮的图片位置下标
     * @dw 点亮
     */
    protected int showLit(final int index) {

        final ImageView heart = new ImageView(ShowLiveActivityBase.this);
        //点亮的背景图片

        heart.setTag("heart");
        heart.setBackgroundResource(ChatServer.heartImg[index]);
        //尺寸参数
        heart.setLayoutParams(new RelativeLayout.LayoutParams((int) TDevice.dpToPixel(30)
                , (int) TDevice.dpToPixel(30)));
        //x位置
        heart.setX(mScreenWidth - mScreenWidth / 3);
        //y位置
        heart.setY(mScreenHeight - 200);
        mRlLiveGift.addView(heart);
        //点亮xy 平移动画
        ObjectAnimator translationX = ObjectAnimator.ofFloat(heart, "translationX", mRandom.nextInt(500) + (mScreenWidth - 200) - (mScreenWidth / 3));
        ObjectAnimator translationY = ObjectAnimator.ofFloat(heart, "translationY", mRandom.nextInt(mScreenHeight / 2) + 200);
        //渐变动画
        ObjectAnimator alpha = ObjectAnimator.ofFloat(heart, "alpha", 0f);
        //xy放大动画
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(heart, "scaleX", 0.8f, 1f);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(heart, "scaleY", 0.8f, 1f);
        AnimatorSet set = new AnimatorSet();
        set.playTogether(translationX, translationY, alpha, scaleX, scaleY);
        set.setDuration(5000);
        set.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if (null != mRlLiveGift) {
                    mRlLiveGift.removeView(heart);

                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        set.start();
        return index;

    }

    protected void switchPlayAnimation(SendGiftBean mSendGiftBean) {
        switch (mSendGiftBean.getGiftid()) {
            case 22:
                //烟花礼物
                showFireworksAnimation(mSendGiftBean);
                break;
            case 21:
                //游轮礼物
                showCruisesAnimation(mSendGiftBean);
                break;
            case 9:
                //红色小轿车
                showRedCarAnimation(mSendGiftBean);
                break;
            case 19:
                //飞机礼物
                showPlainAnimation(mSendGiftBean);
                break;
            default:
                //普通连送礼物
                showOrdinaryGiftInit(mSendGiftBean);
                break;
        }
    }

    /**
     * @param mSendGiftBean 礼物信息
     * @dw 豪华礼物飞机动画
     */
    protected void showPlainAnimation(final SendGiftBean mSendGiftBean) {
        if (!giftAnimationPlayEnd) {
            return;
        }
        //飞机动画初始化
        giftAnimationPlayEnd = false;
        //撒花的颜色
        final int[] colorArr = new int[]{R.color.red, R.color.yellow, R.color.blue, R.color.green, R.color.orange, R.color.pink};

        mGiftView = getLayoutInflater().inflate(R.layout.view_live_gift_animation_plain, null);
        //用户头像
        AvatarView uHead = (AvatarView) mGiftView.findViewById(R.id.iv_animation_head);
        TextView mname = (TextView) mGiftView.findViewById(R.id.tv_liwu_name);
        mname.setText(mSendGiftBean.getNicename());
        uHead.setAvatarUrl(mSendGiftBean.getAvatar());
        mRlLiveGift.addView(mGiftView);
        final RelativeLayout mRootAnimation = (RelativeLayout) mGiftView.findViewById(R.id.rl_animation_flower);

        Observable<String> observable = Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(final Subscriber<? super String> subscriber) {

                ObjectAnimator plainAnimator = ObjectAnimator.ofFloat(mGiftView, "translationX", mScreenWidth, 0);
                plainAnimator.setDuration(3000);
                plainAnimator.addListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        subscriber.onNext("");
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                });
                plainAnimator.start();
            }
        });

        observable.subscribe(new Action1<String>() {
            @Override
            public void call(String s) {
                Random random = new Random();
                int num = random.nextInt(50) + 10;
                int width = mRootAnimation.getWidth();
                int height = mRootAnimation.getHeight();
                //获取花朵

                for (int i = 0; i < num; i++) {
                    int color = random.nextInt(5);
                    int x = random.nextInt(50);
                    final int tx = width == 0 ? 0 : random.nextInt(width);
                    final int ty = height == 0 ? 0 : random.nextInt(height);
                    TextView flower = new TextView(ShowLiveActivityBase.this);
                    flower.setX(x);
                    flower.setText("❀");
                    flower.setTextColor(getResources().getColor(colorArr[color]));
                    flower.setTextSize(50);
                    //每个花朵的动画
                    mRootAnimation.addView(flower);
                    flower.animate().alpha(0f).translationX(tx).translationY(ty).setDuration(5000).start();

                }
                if (mHandler == null) return;
                //飞机移动到中间后延迟一秒钟,开始继续前行-x
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        ObjectAnimator plainAnimator = ObjectAnimator.ofFloat(mGiftView, "translationX", -mGiftView.getWidth());
                        plainAnimator.setDuration(2000);
                        plainAnimator.addListener(new Animator.AnimatorListener() {
                            @Override
                            public void onAnimationStart(Animator animation) {

                            }

                            @Override
                            public void onAnimationEnd(Animator animation) {
                                if (null != mGiftView) {
                                    if (null != mRlLiveGift) {
                                        mRlLiveGift.removeView(mGiftView);
                                    }
                                    if (mLuxuryGiftShowQueue.size() > 0) {
                                        mLuxuryGiftShowQueue.remove(0);
                                    }
                                    giftAnimationPlayEnd = true;
                                    if (mLuxuryGiftShowQueue.size() > 0 && mHandler != null) {
                                        switchPlayAnimation(mLuxuryGiftShowQueue.get(0));
                                    }
                                }

                            }

                            @Override
                            public void onAnimationCancel(Animator animation) {

                            }

                            @Override
                            public void onAnimationRepeat(Animator animation) {

                            }
                        });
                        plainAnimator.start();
                    }
                }, 4000);
            }
        });

    }

    /**
     * @param sendGiftBean 赠送的礼物信息
     * @dw 红色小轿车动画
     * @author 魏鹏
     */
    protected void showRedCarAnimation(SendGiftBean sendGiftBean) {
        if (!giftAnimationPlayEnd) {
            return;
        }

        giftAnimationPlayEnd = false;
        //获取汽车动画布局
        mGiftView = getLayoutInflater().inflate(R.layout.view_live_gift_animation_car_general, null);
        AvatarView uHead = (AvatarView) mGiftView.findViewById(R.id.iv_animation_red_head);
        TextView mname = (TextView) mGiftView.findViewById(R.id.tv_liwu_name);
        mname.setText(sendGiftBean.getNicename());
        uHead.setAvatarUrl(sendGiftBean.getAvatar());
        //获取到汽车image控件
        final ImageView redCar = (ImageView) mGiftView.findViewById(R.id.iv_animation_red_car);
        //添加到当前页面
        mRlLiveGift.addView(mGiftView);

        final int vw = redCar.getLayoutParams().width;
        //动画第二次
        final Runnable carRunnable = new Runnable() {
            @Override
            public void run() {
                //小汽车切换帧动画开始继续移动向-x
                redCar.setImageResource(R.drawable.car_red1);
                mGiftView.animate().translationX(~vw)
                        .withEndAction(new Runnable() {
                            @Override
                            public void run() {
                                //小汽车从底部重新回来切换帧动画
                                redCar.setImageResource(R.drawable.car_10001);
                                ObjectAnimator oX = ObjectAnimator.ofFloat(mGiftView, "translationX", mScreenWidth, mScreenWidth / 2 - vw / 2);
                                ObjectAnimator oY = ObjectAnimator.ofFloat(mGiftView, "translationY", mScreenHeight / 2, mScreenHeight >> 2);

                                //重新初始化帧动画参数
                                AnimatorSet animatorSet = new AnimatorSet();
                                animatorSet.playTogether(oX, oY);
                                animatorSet.setDuration(2000);
                                animatorSet.addListener(new Animator.AnimatorListener() {
                                    @Override
                                    public void onAnimationStart(Animator animation) {

                                    }

                                    @Override
                                    public void onAnimationEnd(Animator animation) {
                                        //小汽车加速帧动画切换
                                        redCar.setImageResource(R.drawable.backcar1);
                                        mGiftView.animate().translationX(-vw).translationY(0)
                                                .setDuration(1000)
                                                .withEndAction(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        //从汽车队列中移除,开始下一个汽车动画
                                                        if (mGiftView == null || mRlLiveGift == null)
                                                            return;
                                                        mRlLiveGift.removeView(mGiftView);
                                                        if (mLuxuryGiftShowQueue.size() > 0) {
                                                            mLuxuryGiftShowQueue.remove(0);
                                                        }

                                                        giftAnimationPlayEnd = true;
                                                        if (mLuxuryGiftShowQueue.size() > 0 && mHandler != null) {
                                                            switchPlayAnimation(mLuxuryGiftShowQueue.get(0));
                                                        }
                                                    }
                                                })
                                                .start();

                                    }

                                    @Override
                                    public void onAnimationCancel(Animator animation) {

                                    }

                                    @Override
                                    public void onAnimationRepeat(Animator animation) {

                                    }
                                });
                                animatorSet.start();

                            }
                        })
                        .setDuration(1000).start();
            }
        };

        //汽车动画init
        ObjectAnimator ox = ObjectAnimator.ofFloat(mGiftView, "translationX", mScreenWidth + vw, mScreenWidth / 2 - vw / 2);
        ox.setDuration(1500);
        ObjectAnimator oy = ObjectAnimator.ofFloat(mGiftView, "translationY", mScreenHeight >> 2);
        //设置背景帧动画
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(ox, oy);
        animatorSet.setDuration(1500);
        animatorSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(final Animator animation) {
                //小汽车停在中间
                redCar.setImageResource(R.drawable.car_red6);
                if (mHandler == null) return;
                mHandler.postDelayed(carRunnable, 500);

            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        animatorSet.start();

    }

    /**
     * @param mSendGiftBean 赠送的礼物信息
     * @dw 邮轮
     * @author 魏鹏
     */
    protected void showCruisesAnimation(SendGiftBean mSendGiftBean) {
        if (!giftAnimationPlayEnd) {
            return;
        }
        giftAnimationPlayEnd = false;
        mGiftView = getLayoutInflater().inflate(R.layout.view_live_gift_animation_cruises, null);

        //游轮上的用户头像
        AvatarView mUhead = (AvatarView) mGiftView.findViewById(R.id.live_cruises_uhead);
        ((TextView) mGiftView.findViewById(R.id.tv_live_cruises_uname)).setText(mSendGiftBean.getNicename());
        mUhead.setAvatarUrl(mSendGiftBean.getAvatar());

        mRlLiveGift.addView(mGiftView);
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) mGiftView.getLayoutParams();
        params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        mGiftView.setLayoutParams(params);
        final RelativeLayout cruises = (RelativeLayout) mGiftView.findViewById(R.id.rl_live_cruises);

        //游轮开始平移动画
        cruises.animate().translationX(mScreenWidth / 2 + mScreenWidth / 3).translationY(120f).withEndAction(new Runnable() {
            @Override
            public void run() {
                if (mHandler == null) return;
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //游轮移动到中间后重新开始移动
                        cruises.animate().translationX(mScreenWidth * 2).translationY(200f).setDuration(3000)
                                .withEndAction(new Runnable() {
                                    @Override
                                    public void run() {
                                        //结束后从队列移除开始下一个邮轮动画
                                        if (mRlLiveGift == null) return;
                                        mRlLiveGift.removeView(mGiftView);
                                        if (mLuxuryGiftShowQueue.size() > 0) {
                                            mLuxuryGiftShowQueue.remove(0);
                                        }
                                        giftAnimationPlayEnd = true;
                                        if (mLuxuryGiftShowQueue.size() > 0 && mHandler != null) {
                                            switchPlayAnimation(mLuxuryGiftShowQueue.get(0));
                                        }
                                    }
                                }).start();
                    }
                }, 2000);

            }
        }).setDuration(3000).start();

        //邮轮海水动画
        ImageView seaOne = (ImageView) mGiftView.findViewById(R.id.iv_live_water_one);
        ImageView seaTwo = (ImageView) mGiftView.findViewById(R.id.iv_live_water_one2);
        ObjectAnimator obj = ObjectAnimator.ofFloat(seaOne, "translationX", -50, 50);
        obj.setDuration(1000);
        obj.setRepeatCount(-1);
        obj.setRepeatMode(ObjectAnimator.REVERSE);
        obj.start();
        ObjectAnimator obj2 = ObjectAnimator.ofFloat(seaTwo, "translationX", 50, -50);
        obj2.setDuration(1000);
        obj2.setRepeatCount(-1);
        obj2.setRepeatMode(ObjectAnimator.REVERSE);
        obj2.start();
    }

    /**
     * @dw 烟花
     * @author 魏鹏
     */
    protected void showFireworksAnimation(SendGiftBean mSendGiftBean) {
        if (!giftAnimationPlayEnd) {
            return;
        }
        giftAnimationPlayEnd = false;
        //初始化烟花动画
        final ImageView animation = new ImageView(this);
        RelativeLayout.LayoutParams pa = new RelativeLayout.LayoutParams((int)TDevice.getScreenWidth(), (int)(TDevice.getScreenWidth()-TDevice.dpToPixel(200)));
        pa.addRule(RelativeLayout.CENTER_IN_PARENT);
        animation.setLayoutParams(pa);
//        animation.setImageResource(R.drawable.gift_fireworks_heart_animation);
//        final AnimationDrawable an = (AnimationDrawable) animation.getDrawable();
        mLiveContent.addView(animation);
//        ThreadManager.getThreadPool().execute(new Runnable() {
//            @Override
//            public void run() {
//                int duration = 0;
//                for (int i = 0; i < an.getNumberOfFrames(); i++) {
//                    duration += an.getDuration(i);
//                }
//                an.start();
//                if (mHandler == null) return;
//                mHandler.postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        //删除当前礼物,开始队列下一个
//                        if (mLiveContent == null) return;
//                        mLiveContent.removeView(animation);
//                        if (mLuxuryGiftShowQueue.size() > 0) {
//                            mLuxuryGiftShowQueue.remove(0);
//                        }
//                        giftAnimationPlayEnd = true;
//                        if (mLuxuryGiftShowQueue.size() > 0 && mHandler != null) {
//                            switchPlayAnimation(mLuxuryGiftShowQueue.get(0));
//                        }
//
//                    }
//                }, duration);
//            }
//        });


        MyAnimationDrawable.animateRawManuallyFromXML(R.drawable.gift_fireworks_heart_animation,
                animation, new Runnable() {

                    @Override
                    public void run() {
                        // TODO onStart
                        // 动画开始时回调

                    }
                }, new Runnable() {

                    @Override
                    public void run() {
                        // TODO onComplete
                        // 动画结束时回调
                        //删除当前礼物,开始队列下一个
                        if (mLiveContent == null) return;
                        mLiveContent.removeView(animation);
                        if (mLuxuryGiftShowQueue.size() > 0) {
                            mLuxuryGiftShowQueue.remove(0);
                        }
                        giftAnimationPlayEnd = true;
                        if (mLuxuryGiftShowQueue.size() > 0 && mHandler != null) {
                            switchPlayAnimation(mLuxuryGiftShowQueue.get(0));
                        }
                    }
                });


    }


    /**
     * @param mShowGiftLayout 礼物显示View
     * @param gitInfo         赠送的礼物信息
     * @param num             赠送礼物的数量(无用)
     * @dw 连送
     * @author 魏鹏
     */
    protected void showEvenSentGiftAnimation(final View mShowGiftLayout, final SendGiftBean gitInfo, int num) {
        final AvatarView mGiftIcon = (AvatarView) mShowGiftLayout.findViewById(R.id.av_gift_icon);
        final TextView mGiftNum = (TextView) mShowGiftLayout.findViewById(R.id.tv_show_gift_num);
        ((AvatarView) mShowGiftLayout.findViewById(R.id.av_gift_uhead)).setAvatarUrl(gitInfo.getAvatar());
        ((TextView) mShowGiftLayout.findViewById(R.id.tv_gift_uname)).setText(gitInfo.getNicename());
        ((TextView) mShowGiftLayout.findViewById(R.id.tv_gift_gname)).setText(gitInfo.getGiftname());
        mGiftIcon.setAvatarUrl(gitInfo.getGifticon());

        if (mShowGiftAnimator != null) {
            mShowGiftAnimator.addView(mShowGiftLayout);//添加到动画区域显示效果
        }
        //动画开始平移
        ObjectAnimator oa1 = ObjectAnimator.ofFloat(mShowGiftLayout, "translationX", -340f, 0f);
        oa1.setDuration(300);
        oa1.start();
        oa1.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                showGiftNumAnimation(mGiftNum, gitInfo.getUid());
                //礼物图片平移动画
                ObjectAnimator giftIconAnimator = ObjectAnimator.ofFloat(mGiftIcon, "translationX", -40f, TDevice.dpToPixel(190));
                giftIconAnimator.setDuration(500);
                giftIconAnimator.start();
                //获取当前礼物是正在显示的哪一个
                final int index = mShowGiftFirstUid == gitInfo.getUid() ? 1 : 2;
                if (mHandler != null) {
                    mHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (timingDelGiftAnimation(index)) {
                                if (mHandler != null) {
                                    mHandler.postDelayed(this, 1000);
                                }
                            } else {
                                mHandler.removeCallbacks(this);
                            }

                        }
                    }, 1000);
                }

            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }


    /**
     * @param tv  显示数量的TextView
     * @param uid 送礼物用户id,需要根据id取出队列中的礼物信息进行赠送时间重置
     * @dw 礼物数量增加动画
     */
    protected void showGiftNumAnimation(TextView tv, int uid) {
        tv.setText("X" + mGiftShowQueue.get(uid).getGiftcount());
        PropertyValuesHolder p1 = PropertyValuesHolder.ofFloat("scaleX", 1.5f, 0.2f, 1f);
        PropertyValuesHolder p2 = PropertyValuesHolder.ofFloat("scaleY", 1.5f, 0.2f, 1f);
        ObjectAnimator.ofPropertyValuesHolder(tv, p1, p2).setDuration(200).start();
        mGiftShowQueue.get(uid).setSendTime(System.currentTimeMillis());//重置发送时间
    }

    /**
     * @param mSendGiftInfo 赠送礼物信息
     * @dw 连送礼物初始化
     */
    protected void showOrdinaryGiftInit(final SendGiftBean mSendGiftInfo) {
        //礼物动画View
        View mShowGiftLayout = mGiftShowNow.get(mSendGiftInfo.getUid());
        //设置当前礼物赠送时间
        mSendGiftInfo.setSendTime(System.currentTimeMillis());
        boolean isShow = false;//是否刚刚加入正在显示队列
        boolean isFirst = false;//是否是第一次赠送礼物
        //是否是第一次送礼物,为空表示礼物队列中没有查询到该用户的送礼纪录
        if (mGiftShowQueue.get(mSendGiftInfo.getUid()) == null) {
            mGiftShowQueue.put(mSendGiftInfo.getUid(), mSendGiftInfo);//加入礼物消息队列
            //将是否第一次送礼设为true
            isFirst = true;
        }
        //是否是新的礼物类型,对比两次礼物的id是否一致
        boolean isNewGift = !(mSendGiftInfo.getGiftid() == mGiftShowQueue.get(mSendGiftInfo.getUid()).getGiftid());
        //当前的正在显示礼物队列不够两条(最多两条),并且当前送礼物用户不在list中
        if ((mGiftShowNow.size() < 2) && (mShowGiftLayout == null)) {
            //初始化显示礼物布局和信息
            mShowGiftLayout = initShowEvenSentSendGift(mSendGiftInfo);
            isShow = true;
        }
        /*
        * mShowGiftLayout不等于null表示在正在显示的礼物队列中查询到了该用户送礼物纪录
        * 将是否正在显示isShow设置为true
        * */
        if (mShowGiftLayout != null) {
            isShow = true;
        }
        /*
        * 如果是新礼物(表示礼物队列中存在送礼物纪录)
        * 存在就将最新礼物的icon和数量重置,并且覆盖older信息
        * */
        if (isNewGift && mShowGiftLayout != null) {
            ((AvatarView) mShowGiftLayout.findViewById(R.id.av_gift_icon)).setAvatarUrl(mSendGiftInfo.getGifticon());
            ((TextView) mShowGiftLayout.findViewById(R.id.tv_show_gift_num)).setText("X1");
            ((TextView) mShowGiftLayout.findViewById(R.id.tv_gift_gname)).setText(mSendGiftInfo.getGiftname());
            //新礼物覆盖之前older礼物信息
            mGiftShowQueue.put(mSendGiftInfo.getUid(), mSendGiftInfo);
        }
        /*
        * 判断是否是连送礼物并且不是第一次赠送并且不是新礼物
        * 不是第一次赠送并且不是新礼物才需要添加数量(否则数量和礼物信息需要重置),
        * */
        if (mSendGiftInfo.getEvensend().equals("y") && (!isFirst) && (!isNewGift)) {//判断当前礼物是否属于连送礼物
            //是连送礼物,将消息队列中的礼物赠送数量加1
            mGiftShowQueue.get(mSendGiftInfo.getUid()).setGiftcount(mGiftShowQueue.get(mSendGiftInfo.getUid()).getGiftcount() + 1);
        }
        //需要显示在屏幕上并且是第一次送礼物需要进行动画初始化
        if (isShow && isFirst) {
            showEvenSentGiftAnimation(mShowGiftLayout, mSendGiftInfo, 1);
        } else if (isShow && (!isNewGift)) {//存在显示队列并且不是新礼物进行数量加一动画
            showGiftNumAnimation((TextView) mShowGiftLayout.findViewById(R.id.tv_show_gift_num), mSendGiftInfo.getUid());
        }
    }

    /**
     * @param mSendGiftInfo 赠送礼物信息
     * @dw 赠送礼物进行初始化操作
     * 判断当前礼物是属于豪华礼物还是连送礼物,并且对魅力值进行累加
     */
    protected void showGiftInit(SendGiftBean mSendGiftInfo) {
        //票数更新
        if (null != mTvYpNum && null != mSendGiftInfo) {
            mTvYpNum.setText(String.valueOf(StringUtils.toInt(mTvYpNum.getText().toString()) + mSendGiftInfo.getTotalcoin()));
        }
        //判断是要播放哪个豪华礼物
        int gId = mSendGiftInfo.getGiftid();
        if (gId == 19 || gId == 21 || gId == 22 || gId == 9 || gId == 19) {
            mLuxuryGiftShowQueue.add(mSendGiftInfo);
        }
        switchPlayAnimation(mSendGiftInfo);
    }

    public void chatListItemClick(ChatBean chat) {

    }

    protected View.OnTouchListener mTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            changeEditStatus(false);
            return false;
        }
    };

    //显示输入框
    protected void changeEditStatus(boolean status) {
        if (status) {
            mChatInput.setFocusable(true);
            mChatInput.setFocusableInTouchMode(true);
            mChatInput.requestFocus();
            InputMethodUtils.toggleSoftKeyboardState(this);
            mLiveChatEdit.setVisibility(View.VISIBLE);
            mButtonMenu.setVisibility(View.GONE);
        } else {
            if (mLiveChatEdit.getVisibility() != View.GONE && InputMethodUtils.isShowSoft(this)) {
                InputMethodUtils.closeSoftKeyboard(this);
                mButtonMenu.setVisibility(View.VISIBLE);
                mLiveChatEdit.setVisibility(View.GONE);
                ACE_TEX_TO_USER = 0;
            }
        }

    }

    //添加一条聊天
    protected void addChatMessage(ChatBean c) {
        if (mListChats.size() > 30) mListChats.remove(0);
        mListChats.add(c);
        mChatListAdapter.notifyDataSetChanged();
        //HHH 2016-09-08 Null指针退出
        if (mLvChatList != null) {
            mLvChatList.setSelection(mListChats.size() - 1);
        }

    }

    //添加弹幕 HHH

    protected void addDanmu(final ChatBean c) {

        final SoftReference<ChatBean> refChatBean = new SoftReference<ChatBean>(c);

        Glide.with(this).load(c.getSimpleUserInfo().avatar).asBitmap().into(new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                Danmu danmu = new Danmu(0, c.getSimpleUserInfo().id, "Comment", resource, c.getContent(), refChatBean);
                mDanmuControl.addDanmu(danmu, c.getSimpleUserInfo().user_nicename, 0);

            }

            @Override
            public void onLoadFailed(Exception e, Drawable errorDrawable) {
                SoftReference<ChatBean> refChatBean = new SoftReference<ChatBean>(c);
                Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.null_blacklist);
                Danmu danmu = new Danmu(0, c.getSimpleUserInfo().id, "Comment", bitmap, c.getContent(), refChatBean);
                mDanmuControl.addDanmu(danmu, c.getSimpleUserInfo().user_nicename, 0);
            }
        }); //方法中设置asBitmap可以设置回调类型
    }

    //用户信息弹窗
    protected void showUserInfoDialog(SimpleUserInfo toUserInfo) {
        if (!mUserInfoDialog.isAdded()) {
            Bundle b = new Bundle();
            b.putParcelable("MYUSERINFO", mUser);
            b.putParcelable("TOUSERINFO", toUserInfo);
            b.putString("ROOMNUM", mRoomNum);
            mUserInfoDialog.setArguments(b);
            mUserInfoDialog.show(getSupportFragmentManager(), "UserInfoDialogFragment");
        }
    }

    /**
     * @dw 显示私信页面
     */
    protected void showPrivateChat() {
        Bundle bundle = new Bundle();
        bundle.putInt("type", 1);
        mMessageFragment = new MessageFragment();
        mMessageFragment.setArguments(bundle);
        mMessageFragment.show(getSupportFragmentManager(), "MessageFragment");
    }

    //连接结果
    public void onConnectRes(boolean res) {
        if (res) {
            mConnectionState = true;

            //请求僵尸粉丝
            mChatServer.getZombieFans();
        }
    }


    //僵尸粉丝
    protected void addZombieFans(String zombies) {

        JSONArray fans = ApiUtils.checkIsSuccess(zombies);
        if (null != fans) {
            try {
                //设置在线用户数量
                JSONObject jsonInfoObj = fans.getJSONObject(0);
                JSONArray fansJsonArray = jsonInfoObj.getJSONArray("list");

                if (!(mUserList.size() >= 20) && fansJsonArray.length() > 0) {
                    for (int i = 0; i < fansJsonArray.length(); i++) {
                        UserBean u = mGson.fromJson(fansJsonArray.getString(i), UserBean.class);
                        mUserList.add(u);
                    }
                    LiveUtils.sortUserList(mUserList);
                    mUserListAdapter.setUserList(mUserList);
                }
                //在线人数统计
                if (fansJsonArray.length() > 0) {
                    ChatServer.LIVE_USER_NUMS = StringUtils.toInt(jsonInfoObj.getString("nums"), 0);
                    mTvLiveNum.setText(ChatServer.LIVE_USER_NUMS + "人观看");
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }


    //当用户状态改变
    protected void onUserStatusChange(UserBean user, boolean state) {
        //设置在线人数
        if(mTvLiveNum!=null){
            mTvLiveNum.setText(ChatServer.LIVE_USER_NUMS + "人观看");
        }
        for (int i = 0; i < mUserList.size(); i++) {
            if (user.id.equals(mUserList.get(i).id)) {
                mUserList.remove(i);
                break;
            }
        }
        if (state && !mUserList.contains(user)) {//用户上线
            //判断该用户是否存在列表中
            if (!isReq)
                if (mUserList.size() <= 20) {
                    mUserList.add(user);
                    //列表重新排序

                } else {
                    if (isHandlerTime) {
                        isHandlerTime = false;
                        mHandler.postDelayed(mRunTime, AppConfig.USERLIST_TIME * 1000);
                    }
                }
            TLog.log("加入" + user.id);
            if (!user.vip_type.equals("0") || StringUtils.toInt(user.car_id) != 0 || StringUtils.toInt(user.level) >= AppConfig.JOIN_ROOM_ANIMATION_LEVEL) {
                joinRoomAnimation(user);
            }

        } else {
            TLog.log("离开" + user.id);
        }
        LiveUtils.sortUserList(mUserList);
        if(mUserListAdapter!=null){
            mUserListAdapter.setUserList(mUserList);
        }
    }

    Runnable mRunTime = new Runnable() {
        @Override
        public void run() {
            isReq = PhoneLiveApi.getUserLists(mRoomNum, mStreamName, new StringCallback() {
                @Override
                public void onError(Call call, Exception e, int id) {
                }

                @Override
                public void onResponse(String response, int id) {
                    JSONArray res = ApiUtils.checkIsSuccess(response);
                    if (res != null) {
                        JSONObject data = null;
                        try {
                            data = res.getJSONObject(0);
                            mUserList.clear();
                            mRvUserList.removeAllViews();
                            mUserList.addAll(ApiUtils.formatDataToList2(data.getJSONArray("userlist")
                                    , SimpleUserInfo.class));
                            ChatServer.LIVE_USER_NUMS = data.getInt("nums");
                            mTvLiveNum.setText(ChatServer.LIVE_USER_NUMS + "人观看");
                            mTvYpNum.setText(data.getString("votestotal"));
                            mUserListAdapter.setUserList(mUserList);
                            isHandlerTime = true;
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                }
            });
        }
    };


    private void joinRoomAnimation(UserBean user) {

        mJoinRoomAnimationQueue.add(user);

        if (mJoinRoomAnimationQueue.size() == 1) {
            startJoinRoomAnimation();
        }


    }

    //进入动画
    //进入动画
    private void startJoinRoomAnimation() {
        //等级进场动画
        UserBean user = mJoinRoomAnimationQueue.get(0);
        SpannableStringBuilder name = null;
        //添加等级图文混合
        Drawable levelDrawable = getResources().getDrawable(LiveUtils.getLevelRes(user.level));
        levelDrawable.setBounds(0, 0, (int) TDevice.dpToPixel(20), (int) TDevice.dpToPixel(15));
        VerticalImageSpan levelImage = new VerticalImageSpan(levelDrawable);


        VerticalImageSpan vipImage = null;
        if (StringUtils.toInt(user.vip_type) != 0) {
            Drawable vipDrawable = getResources().getDrawable(DrawableRes.Vip[StringUtils.toInt(user.vip_type) - 1]);
            vipDrawable.setBounds(0, 0, (int) TDevice.dpToPixel(28), (int) TDevice.dpToPixel(30));
            vipImage = new VerticalImageSpan(vipDrawable);
        }

        String colorText = "";
        Drawable drawable = null;
        if (user.car_id.equals("0") && !user.vip_type.equals("0")) {
            colorText = "#F35ED9";
            drawable = getResources().getDrawable(R.drawable.vip_bg);
        } else if (!user.car_id.equals("0") && !user.vip_type.equals("0")) {
            colorText = "#FEF954";
            drawable = getResources().getDrawable(R.drawable.all_bg);
        } else if (!user.car_id.equals("0") && user.vip_type.equals("0")) {
            colorText = "#41D484";
            drawable = getResources().getDrawable(R.drawable.zuoji_bg);
        } else {
            colorText = "#41D483";
            drawable = getResources().getDrawable(R.drawable.bg_join_room_animation);
        }
        if ("0".equals(user.vip_type)) {
            name = new SpannableStringBuilder("_ " + user.user_nicename);
            name.setSpan(new ForegroundColorSpan(Color.parseColor(colorText)), 2, name.length(),
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            name.setSpan(levelImage, 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        } else {
            name = new SpannableStringBuilder("_ _ " + user.user_nicename);
            name.setSpan(new ForegroundColorSpan(Color.parseColor(colorText)), 3, name.length(),
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            name.setSpan(levelImage, 2, 3, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            name.setSpan(vipImage, 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        name.append("进入房间");

        mTvJoinRoomAnimation.setText(name);
        mTvJoinRoomAnimation.setBackground(drawable);
        ObjectAnimator animation = ObjectAnimator.ofFloat(mTvJoinRoomAnimation, "translationX", mScreenWidth, 0);
        animation.setDuration(1500);
        animation.start();
        animation.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {
            }

            @Override
            public void onAnimationEnd(Animator animator) {

                if (mHandler == null) return;

                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (mTvJoinRoomAnimation == null) return;
                        mTvJoinRoomAnimation.setX(mScreenWidth);
                        mJoinRoomAnimationQueue.remove(0);

                        if (mJoinRoomAnimationQueue.size() != 0) {
                            startJoinRoomAnimation();
                        }

                    }
                }, 1500);


            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
        //座骑动画
        if (!("0").equals(user.car_id)) {
            mLlGif.setVisibility(View.VISIBLE);
            Glide.with(this).load(user.car_swf).asGif().diskCacheStrategy(DiskCacheStrategy.SOURCE).into(mIvGif);
            mTvGif.setText(user.user_nicename + user.car_words);
            if (mHandler == null) return;
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (mLlGif == null) return;
                    mLlGif.setVisibility(View.GONE);
                }
            }, (long) (StringUtils.toDouble(user.car_swftime) * 1000));
        }

    }


    //直播结束弹窗遮罩
    protected void showLiveEndDialog(String uid, int liveEndYpNum, final String stream) {

        if (!uid.equals(mRoomNum)) {
            LiveEndFragmentDialog liveEndFragmentDialog = new LiveEndFragmentDialog();
            Bundle bundle = new Bundle();
            bundle.putString("roomnum", mRoomNum);
            liveEndFragmentDialog.setArguments(bundle);
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.add(liveEndFragmentDialog, "liveEndFragmentDialog");
            transaction.commitAllowingStateLoss();
        } else {
            //请求接口改变直播状态
            PhoneLiveApi.closeLive(mUser.id, mUser.token, mStreamName, new StringCallback() {
                @Override
                public void onError(Call call, Exception e, int id) {
                    //showToast3("关闭直播失败" ,0);
                }

                @Override
                public void onResponse(String response, int id) {
                    JSONArray res = ApiUtils.checkIsSuccess(response);
                    if (res != null) {
                        LiveEmceeEndFragmentDialog dialog = new LiveEmceeEndFragmentDialog();
                        Bundle bundle = new Bundle();
                        bundle.putString("stream", stream);
                        dialog.setArguments(bundle);
                        dialog.show(getSupportFragmentManager(), "LiveEmceeEndFragmentDialog");
                    }
                }
            });

        }

    }

    //弹幕状态控制 HHH
    @Override
    protected void onPause() {
        super.onPause();
        mDanmuControl.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mDanmuControl.resume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ChatServer.LIVE_USER_NUMS = 0;
        mDanmuControl.destroy();
        OkHttpUtils.getInstance().cancelTag("getConfig");
        OkHttpUtils.getInstance().cancelTag("sendBarrage");
        MessageFragment.removeChatStateObserver(mChatStateObserver);
        mChatStateObserver = null;
    }

    @Override
    protected void onStop() {
        super.onStop();
        //解除广播
        try {
            unregisterReceiver(broadCastReceiver);
        } catch (Exception e) {

        }
    }

    @Override
    public void onClick(View view) {

    }

    //发送弹幕 HHH
    protected void sendBarrage() {

        if (mChatInput.getText().toString().trim().length() == 0 || (!mConnectionState)) return;

        PhoneLiveApi.sendBarrage(mUser, mChatInput.getText().toString(), mRoomNum, mStreamName, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {

            }

            @Override
            public void onResponse(String response, int id) {

                sendBarrageOnResponse(response);
            }
        });

    }

    //错误消息弹窗
    protected ErrorDialogFragment mErrDlgFragment;

    protected void showErrorAndQuit(String errorMsg) {

        if (!mErrDlgFragment.isAdded() && !this.isFinishing()) {
            Bundle args = new Bundle();
            args.putString("errorMsg", errorMsg);
            mErrDlgFragment.setArguments(args);
            mErrDlgFragment.setCancelable(false);

            //此处不使用用.show(...)的方式加载dialogfragment，避免IllegalStateException
            android.app.FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.add(mErrDlgFragment, "loading");
            transaction.commitAllowingStateLoss();
        }
    }

    protected void sendBarrageOnResponse(String response) {

    }


    /**
     * 是否在小窗区域移动
     *
     * @param x      当前点击的相对小窗左上角的x坐标
     * @param y      当前点击的相对小窗左上角的y坐标
     * @param left   小窗左上角距离预览区域左上角的x轴距离
     * @param right  小窗右上角距离预览区域左上角的x轴距离
     * @param top    小窗左上角距离预览区域左上角的y轴距离
     * @param bottom 小窗右上角距离预览区域左上角的y轴距离
     * @return
     */
    protected boolean isSubScreenArea(float x, float y, int left, int right, int top, int bottom, boolean mIsConnected) {
        if (!mIsConnected) {
            return false;
        }
        if (x > left && x < right &&
                y > top && y < bottom) {
            return true;
        }

        return false;
    }


    //分享pop弹窗
    public static void showSharePopWindow(final Context context, View v, final SimpleUserInfo mUser) {

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
                im.setImageResource(context.getResources().getIdentifier(AppConfig.SHARE_TYPE.getString(i) + "_share_s", "drawable", "com.duomizhibo.phonelive"));
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

    protected void setTextBold(TextView tv) {
        TextPaint tp = tv.getPaint();
        tp.setFakeBoldText(true);
    }

}

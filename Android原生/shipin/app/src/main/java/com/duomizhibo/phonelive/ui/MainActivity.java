package com.duomizhibo.phonelive.ui;


import android.Manifest;
import android.animation.Animator;
import android.animation.ValueAnimator;
import android.app.Dialog;
import android.content.pm.PackageManager;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.duomizhibo.phonelive.AppConfig;
import com.duomizhibo.phonelive.AppContext;
import com.duomizhibo.phonelive.R;
import com.duomizhibo.phonelive.api.remote.ApiUtils;
import com.duomizhibo.phonelive.api.remote.PhoneLiveApi;
import com.duomizhibo.phonelive.bean.BonusBean;
import com.duomizhibo.phonelive.bean.UserBean;
import com.duomizhibo.phonelive.event.ChatExitEvent;
import com.duomizhibo.phonelive.event.CoinNameEvent;
import com.duomizhibo.phonelive.fragment.GuanzhuFragment;
import com.duomizhibo.phonelive.fragment.HomeFragment;
import com.duomizhibo.phonelive.fragment.LoginAwardDialogFragment;
import com.duomizhibo.phonelive.fragment.MessageFragment;
import com.duomizhibo.phonelive.fragment.PublisherDialogFragment;
import com.duomizhibo.phonelive.fragment.UserInformationFragment;
import com.duomizhibo.phonelive.ui.dialog.LiveCommon;
import com.duomizhibo.phonelive.utils.LocationUtil;
import com.duomizhibo.phonelive.utils.LoginUtils;
import com.duomizhibo.phonelive.utils.SharedPreUtil;
import com.duomizhibo.phonelive.utils.StringUtils;
import com.duomizhibo.phonelive.utils.TDevice;
import com.duomizhibo.phonelive.utils.TLog;
import com.duomizhibo.phonelive.utils.UIHelper;
import com.duomizhibo.phonelive.utils.UpdateManager;
import com.duomizhibo.phonelive.widget.LineEditText;
import com.duomizhibo.phonelive.widget.PwdEditText;
import com.zhy.http.okhttp.callback.StringCallback;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Set;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;
import okhttp3.Call;


//主页面
public class MainActivity extends AppCompatActivity implements LoginAwardDialogFragment.onLoginAwardImgShow {

    private View cart;
    private RelativeLayout ml;
    private PathMeasure mPathMeasure;
    private float[] mCurrentPosition = new float[2];
    private RelativeLayout layout;
    private boolean isEveryBouns;
    LoginAwardDialogFragment mAwardDialogFragment;

    private FragmentManager mFragmentManager;
    private int mCurIndex;
    private View mRedPoint;
    private SparseArray<Fragment>  mSparseArray;
    private HomeFragment mHomeFragment;
    private GuanzhuFragment mAttentionFragment;
    private MessageFragment mMessageFragment;
    private UserInformationFragment mUserFragment;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ml = (RelativeLayout) findViewById(R.id.rootLayout);
        cart = findViewById(R.id.btn_user);
        mRedPoint = findViewById(R.id.red_point);
        if(savedInstanceState==null){
            mHomeFragment = new HomeFragment();
            mAttentionFragment = new GuanzhuFragment();
            mMessageFragment = new MessageFragment();
            Bundle bundle = new Bundle();
            bundle.putInt("type", 0);
            mMessageFragment.setArguments(bundle);
            mUserFragment = new UserInformationFragment();
            mFragmentManager = getSupportFragmentManager();
            FragmentTransaction ft = mFragmentManager.beginTransaction();
            ft.add(R.id.replaced, mHomeFragment);
            ft.add(R.id.replaced, mAttentionFragment);
            ft.add(R.id.replaced, mMessageFragment);
            ft.add(R.id.replaced, mUserFragment);
            ft.show(mHomeFragment).hide(mAttentionFragment).hide(mMessageFragment).hide(mUserFragment).commit();

            mSparseArray = new SparseArray<>();
            mSparseArray.put(0, mHomeFragment);
            mSparseArray.put(1, mAttentionFragment);
            mSparseArray.put(2, mMessageFragment);
            mSparseArray.put(3, mUserFragment);
        }

        MessageFragment.addChatStateObserver(mChatStateObserver);
        initData();

    }

    MessageFragment.ChatStateObserver mChatStateObserver = new MessageFragment.ChatStateObserver() {
        @Override
        public void onMessageCountChanged(int followUnReadCount, int notFollowUnReadCount) {
            refreshUnReadCount(followUnReadCount + notFollowUnReadCount);
        }

        @Override
        public void onComeBack(ChatExitEvent e) {
            if (e != null) {
                mMessageFragment.onChatBack(e);
            }
        }

        @Override
        public void onIgnoreUnReadMessage() {
            IgnoreUnReadMessage();
        }
    };

    public void refreshUnReadCount(int count) {
        if (count > 0 && !MessageFragment.ignoreUnReadMessage) {
            if (mRedPoint.getVisibility() == View.GONE) {
                mRedPoint.setVisibility(View.VISIBLE);
            }
            //mRedPoint.setText(String.valueOf(count));
        } else {
            if (mRedPoint.getVisibility() == View.VISIBLE) {
                mRedPoint.setVisibility(View.GONE);
            }
        }
    }

    public void IgnoreUnReadMessage() {
        if (mRedPoint.getVisibility() == View.VISIBLE) {
            mRedPoint.setVisibility(View.GONE);
        }
        mMessageFragment.onIgnoreUnReadMessage();
    }

    public void mainClick(View v) {
        switch (v.getId()) {
            case R.id.btn_home:
                toggleFragment(0);
                break;
            case R.id.btn_attention:
                toggleFragment(1);
                break;
            case R.id.btn_msg:
                toggleFragment(2);
                break;
            case R.id.btn_user:
                toggleFragment(3);
                break;
            case R.id.btn_live:
                PublisherDialogFragment f = new PublisherDialogFragment();
                FragmentTransaction ft = mFragmentManager.beginTransaction();
                ft.add(f, "PublisherDialogFragment");
                ft.commit();
                break;
        }
    }


    private void toggleFragment(int index) {
        if (index == mCurIndex) {
            return;
        }
        if(mFragmentManager==null){
            mFragmentManager=getSupportFragmentManager();
        }
        FragmentTransaction ft = mFragmentManager.beginTransaction();
        if (mSparseArray!=null)
        for (int i = 0; i < mSparseArray.size(); i++) {
            if (index == mSparseArray.keyAt(i)) {
                mCurIndex = index;
                ft.show(mSparseArray.valueAt(i));
            } else {
                ft.hide(mSparseArray.valueAt(i));
            }
        }
        ft.commit();
    }

    public interface OnResumeCallback {
        void onResumeRefresh();
    }


    public void initData() {
        //检查token是否过期
        checkTokenIsOutTime();
        //注册极光推送
        registerJpush();
        //登录环信
        loginIM();
        //检查是否有最新版本
        checkNewVersion();

        updateConfig();

        Bundle bundle = getIntent().getBundleExtra("USER_INFO");

        if (bundle != null) {
            UIHelper.showLookLiveActivity(this, bundle);
        }


        startLoaction();
    }


    //开启高德定位
    private void startLoaction() {
        if (Build.VERSION.SDK_INT >= 23) {
            //定位权限检测
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                //进行权限请求
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 6);
            } else {
                LocationUtil.getInstance().startLocation();
            }
        } else {
            LocationUtil.getInstance().startLocation();
        }
    }

    private void showDialogInvite() {
        final Dialog dialog = new Dialog(this, R.style.dialog_no_background);
        dialog.setContentView(R.layout.activity_broker);
        Window dialogWindow = dialog.getWindow();
        dialogWindow.setGravity(Gravity.CENTER);
        dialogWindow.setWindowAnimations(R.style.dialogstyle); // 添加动画
        dialog.setCanceledOnTouchOutside(false);
        ImageView mTvCancel = (ImageView) dialog.findViewById(R.id.iv_close);
        final PwdEditText mEditText = (PwdEditText) dialog.findViewById(R.id.et_invite_num);
        final LineEditText mName = (LineEditText) dialog.findViewById(R.id.et_user_name);
        ;
        mTvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        mEditText.initStyle(R.drawable.edit_num_bg, 6, 0.33f, R.color.white, R.color.white, 20);

        mEditText.setOnTextFinishListener(new PwdEditText.OnTextFinishListener() {
            @Override
            public void onFinish(String str) {
                PhoneLiveApi.setDistribut(AppContext.getInstance().getLoginUid(),
                        AppContext.getInstance().getToken(), mEditText.getPwdText(), new StringCallback() {
                            @Override
                            public void onError(Call call, Exception e, int id) {

                            }

                            @Override
                            public void onResponse(String response, int id) {
                                JSONArray invisionArray = ApiUtils.checkIsSuccess(response);
                                if (invisionArray != null) {
                                    if (dialog != null) {
                                        dialog.dismiss();
                                    }
                                }
                            }
                        });

            }
        });
        dialog.show();
        UserBean mUser = AppContext.getInstance().getLoginUser();
        mName.setText(mUser.user_nicename);

        mUser.isreg = "0";
        AppContext.getInstance().saveUserInfo(mUser);
    }

    //
    private void updateConfig() {

        PhoneLiveApi.getConfig(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {

            }

            @Override
            public void onResponse(String response, int id) {
                JSONArray res = ApiUtils.checkIsSuccess(response);
                if (res != null) {
                    try {
                        JSONObject info0=res.getJSONObject(0);
                        AppConfig.TICK_NAME =info0 .getString("name_votes");
                        AppConfig.CURRENCY_NAME = info0.getString("name_coin");
                        AppConfig.JOIN_ROOM_ANIMATION_LEVEL = info0.getInt("enter_tip_level");
                        AppConfig.LIVE_TIME_COIN = info0.getJSONArray("live_time_coin");
                        AppConfig.LIVE_TYPE = info0.getJSONArray("live_type");
                        AppConfig.SHARE_TYPE = info0.getJSONArray("share_type");
                        AppConfig.APK_DES = info0.getString("apk_des");
                        AppConfig.VIDEO_SHARE_TITLE = info0.getString("video_share_title");
                        AppConfig.VIDEO_SHARE_DES = info0.getString("video_share_des");
                        SharedPreUtil.put(MainActivity.this, "name_votes", AppConfig.TICK_NAME);
                        SharedPreUtil.put(MainActivity.this, "name_coin", AppConfig.CURRENCY_NAME);
                        SharedPreUtil.put(MainActivity.this, "enter_tip_level", AppConfig.JOIN_ROOM_ANIMATION_LEVEL);
                        SharedPreUtil.put(MainActivity.this, "isSaveConfig", true);
                        int maintain_switch = res.getJSONObject(0).getInt("maintain_switch");
                        if (maintain_switch == 1) {
                            String maintain_tips = res.getJSONObject(0).getString("maintain_tips");
                            LiveCommon.showMainTainDialog(MainActivity.this, maintain_tips);
                        }
                        EventBus.getDefault().post(new CoinNameEvent(AppConfig.CURRENCY_NAME));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        if (AppContext.getInstance().getLoginUser().isreg != null && AppContext.getInstance().getLoginUser().isreg.equals("1")) {
            showDialogInvite();
        }
    }

    //
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 5: {
                // 判断权限请求是否通过
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults.length > 0 && grantResults[1] == PackageManager.PERMISSION_GRANTED
                        && grantResults.length > 0 && grantResults[2] != PackageManager.PERMISSION_GRANTED && grantResults.length > 0 && grantResults[3] != PackageManager.PERMISSION_GRANTED) {
                    requestStartLive();
                } else if (grantResults.length > 0 && grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    AppContext.showToast("您已拒绝使用摄像头权限,将无法正常直播,请去设置中修改");
                } else if (grantResults.length > 0 && grantResults[1] != PackageManager.PERMISSION_GRANTED) {
                    AppContext.showToast("您已拒绝使用录音权限,将无法正常直播,请去设置中修改");
                } else if (grantResults.length > 0 && grantResults[2] != PackageManager.PERMISSION_GRANTED || grantResults.length > 0 && grantResults[3] != PackageManager.PERMISSION_GRANTED) {
                    AppContext.toast("您没有同意使用读写文件权限,无法正常直播,请去设置中修改");
                } else if (grantResults.length > 0 && grantResults[3] != PackageManager.PERMISSION_GRANTED) {
                    AppContext.toast("定位权限未打开");
                } else if (grantResults.length > 0 && grantResults[4] != PackageManager.PERMISSION_GRANTED) {
                    AppContext.toast("您没有同意使用定位权限,无法正常直播,请去设置中修改");
                }
                return;
            }
        }
    }

    //请求服务端开始直播
    private void requestStartLive() {
        UIHelper.showStartLiveActivity(MainActivity.this);
    }


    //登录环信即时聊天
    private void loginIM() {
        String uid = String.valueOf(AppContext.getInstance().getLoginUid());

        EMClient.getInstance().login(uid,
                "fmscms" + uid, new EMCallBack() {//回调
                    @Override
                    public void onSuccess() {
                        runOnUiThread(new Runnable() {
                            public void run() {
                                TLog.log("环信[登录聊天服务器成功]");
                                int count=EMClient.getInstance().chatManager().getUnreadMessageCount();
                                TLog.log("环信[未读消息数量]--->"+count);
                                refreshUnReadCount(count);
                            }
                        });
                    }

                    @Override
                    public void onProgress(int progress, String status) {

                    }

                    @Override
                    public void onError(int code, String message) {
                        if (204 == code) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    AppContext.showToastAppMsg(MainActivity.this, "聊天服务器登录和失败,请重新登录");
                                }
                            });

                        }
                        TLog.log("环信[主页登录聊天服务器失败" + "code:" + code + "MESSAGE:" + message + "]");
                    }
                });


    }

    /**
     * @dw 注册极光推送
     */
    private void registerJpush() {
        JPushInterface.setAlias(this, AppContext.getInstance().getLoginUid() + "PUSH",
                new TagAliasCallback() {
                    @Override
                    public void gotResult(int i, String s, Set<String> set) {
                        TLog.log("极光推送注册[" + i + "I" + "S:-----" + s + "]");
                    }
                });

    }

    /**
     * @dw 检查token是否过期
     */
    private void checkTokenIsOutTime() {
        LoginUtils.tokenIsOutTime(null);
    }

    /**
     * @dw 检查是否有最新版本
     */
    private void checkNewVersion() {
        UpdateManager manager = new UpdateManager(this, false);
        manager.checkUpdate();

    }

    //开始直播初始化
    public void startLive() {
        if (Build.VERSION.SDK_INT >= 23) {
            //摄像头权限检测
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                    != PackageManager.PERMISSION_GRANTED ||
                    ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)
                            != PackageManager.PERMISSION_GRANTED ||
                    ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                            != PackageManager.PERMISSION_GRANTED ||
                    ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                            != PackageManager.PERMISSION_GRANTED
                    ||
                    ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                            != PackageManager.PERMISSION_GRANTED) {
                //进行权限请求
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE,
                                Manifest.permission.ACCESS_COARSE_LOCATION},
                        5);

            } else {
                requestStartLive();
            }
        } else {
            requestStartLive();
        }

    }

    @Override
    public void onLoginAwardImgShow(View view, String i) {

        layout = new RelativeLayout(this);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
        layout.setLayoutParams(layoutParams);
        ImageView imageView = new ImageView(this);
        imageView.setImageResource(R.drawable.banner);
        layoutParams = new RelativeLayout.LayoutParams((int) TDevice.dpToPixel(150), (int) TDevice.dpToPixel(50));
        layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
        imageView.setLayoutParams(layoutParams);
        layout.addView(imageView);
        TextView textView = new TextView(this);
        layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
        textView.setText("X" + i);
        textView.setTextColor(getResources().getColor(R.color.white));
        textView.setLayoutParams(layoutParams);
        layout.addView(textView);
        ml.addView(layout);
        layout.setAnimation(getAlphaAnimationOut());
        addCart(imageView);
    }

    public Animation getAlphaAnimationOut() {
        ScaleAnimation scaleAnimation = new ScaleAnimation(1.5f, 0.2f, 1.5f, 0.2f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        scaleAnimation.setDuration(1000);
        return scaleAnimation;
    }

    private void addCart(ImageView iv) {
//      一、创造出执行动画的主题---imageview
        //代码new一个imageview，图片资源是上面的imageview的图片
        // (这个图片就是执行动画的图片，从开始位置出发，经过一个抛物线（贝塞尔曲线），移动到购物车里)
        final ImageView goods = new ImageView(this);
        goods.setImageResource(R.drawable.star2);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(100, 100);
        ml.addView(goods, params);

//        二、计算动画开始/结束点的坐标的准备工作
        //得到父布局的起始点坐标（用于辅助计算动画开始/结束时的点的坐标）
        int[] parentLocation = new int[2];
        ml.getLocationInWindow(parentLocation);

        //得到商品图片的坐标（用于计算动画开始的坐标）
        int startLoc[] = new int[2];
        iv.getLocationInWindow(startLoc);

        //得到购物车图片的坐标(用于计算动画结束后的坐标)
        int endLoc[] = new int[2];
        cart.getLocationInWindow(endLoc);


//        三、正式开始计算动画开始/结束的坐标
        //开始掉落的商品的起始点：商品起始点-父布局起始点+该商品图片的一半
        float startX = TDevice.getScreenWidth() / 2;
        float startY = TDevice.getScreenHeight() / 2;

        //商品掉落后的终点坐标：购物车起始点-父布局起始点+购物车图片的1/5
        float toX = endLoc[0] - parentLocation[0] + cart.getWidth() / 5;
        float toY = endLoc[1] - parentLocation[1];

//        四、计算中间动画的插值坐标（贝塞尔曲线）（其实就是用贝塞尔曲线来完成起终点的过程）
        //开始绘制贝塞尔曲线
        Path path = new Path();
        //移动到起始点（贝塞尔曲线的起点）
        path.moveTo(startX, startY);
        //使用二次萨贝尔曲线：注意第一个起始坐标越大，贝塞尔曲线的横向距离就会越大，一般按照下面的式子取即可
        path.quadTo((startX + toX) / 2, startY, toX, toY);
        //mPathMeasure用来计算贝塞尔曲线的曲线长度和贝塞尔曲线中间插值的坐标，
        // 如果是true，path会形成一个闭环
        mPathMeasure = new PathMeasure(path, false);

        //★★★属性动画实现（从0到贝塞尔曲线的长度之间进行插值计算，获取中间过程的距离值）
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(0, mPathMeasure.getLength());
        valueAnimator.setDuration(1000);
        // 匀速线性插值器
        valueAnimator.setInterpolator(new LinearInterpolator());
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                // 当插值计算进行时，获取中间的每个值，
                // 这里这个值是中间过程中的曲线长度（下面根据这个值来得出中间点的坐标值）
                float value = (Float) animation.getAnimatedValue();
                // ★★★★★获取当前点坐标封装到mCurrentPosition
                // boolean getPosTan(float distance, float[] pos, float[] tan) ：
                // 传入一个距离distance(0<=distance<=getLength())，然后会计算当前距
                // 离的坐标点和切线，pos会自动填充上坐标，这个方法很重要。
                mPathMeasure.getPosTan(value, mCurrentPosition, null);//mCurrentPosition此时就是中间距离点的坐标值
                // 移动的商品图片（动画图片）的坐标设置为该中间点的坐标
                goods.setTranslationX(mCurrentPosition[0]);
                goods.setTranslationY(mCurrentPosition[1]);
            }
        });
//      五、 开始执行动画
        valueAnimator.start();

//      六、动画结束后的处理
        valueAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            //当动画结束后：
            @Override
            public void onAnimationEnd(Animator animation) {
                // 把移动的图片imageview从父布局里移除
                ml.removeView(layout);
                ml.removeView(goods);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(!isEveryBouns){
            everyBonus();
        }
    }

    private void everyBonus() {
        isEveryBouns=true;
        PhoneLiveApi.getBonus(AppContext.getInstance().getLoginUid(), AppContext.getInstance().getToken(), new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {

            }

            @Override
            public void onResponse(String response, int id) {
                try {
                    JSONArray res = ApiUtils.checkIsSuccess(response);
                    if (res != null) {
                        BonusBean mBonus = new Gson().fromJson(res.getString(0), BonusBean.class);
                        if (StringUtils.toInt(mBonus.getBonus_switch()) == 1 && StringUtils.toInt(mBonus.getBonus_day()) > 0) {
                            mAwardDialogFragment = new LoginAwardDialogFragment();
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("BonusBean", mBonus);
                            mAwardDialogFragment.setArguments(bundle);
                            if (!mAwardDialogFragment.isAdded()){
                                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                                ft.add(mAwardDialogFragment, "mAwardDialogFragment");
                                ft.commit();
                            }

                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        MessageFragment.removeChatStateObserver(mChatStateObserver);
        mChatStateObserver=null;
        super.onDestroy();
    }
    protected void onSaveInstanceState(Bundle outState) {
//        super.onSaveInstanceState(outState);  // 重写该方法，并注释掉该行。
    }
}

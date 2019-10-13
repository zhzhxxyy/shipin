package com.duomizhibo.phonelive;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.util.DisplayMetrics;
import android.widget.Toast;
import com.duomizhibo.phonelive.base.BaseApplication;
import com.duomizhibo.phonelive.bean.UserBean;
import com.duomizhibo.phonelive.bean.video.MemberInfo;
import com.duomizhibo.phonelive.bean.video.VersionObject;
import com.duomizhibo.phonelive.cache.DataCleanManager;
import com.duomizhibo.phonelive.event.Event;
import com.duomizhibo.phonelive.utils.StringUtils;
import com.duomizhibo.phonelive.utils.TLog;
import com.duomizhibo.phonelive.utils.UIHelper;
import com.hyphenate.EMConnectionListener;
import com.hyphenate.EMError;
import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.util.NetUtils;
import com.duomizhibo.phonelive.utils.MethodsCompat;
import com.umeng.analytics.MobclickAgent;
import com.umeng.commonsdk.UMConfigure;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.cookie.CookieJarImpl;
import com.zhy.http.okhttp.cookie.store.PersistentCookieStore;
import com.zhy.http.okhttp.log.LoggerInterceptor;

import org.greenrobot.eventbus.EventBus;

import java.util.List;
import java.util.Properties;
import java.util.UUID;

import okhttp3.OkHttpClient;


/**
 * 全局应用程序类：用于保存和调用全局应用配置及访问网络数据
 *
 * @version 1.0
 *
 *
 *需要修改的配置文件是
 * AppConfig.URL_COMMON  域名请求地址
 * AppConfig.URL_CHECK_ROUTE 线路检查域名地址 /app_index/checkRoute.html
 * AppConfig.UMENG_APP_KEY 友盟的key
 *
 * 配置好友盟的key需要在该文件onCreate 打开友盟统计
 *
 * 后台配置客服地址
 * 域名+/index/zixun.html
 * 客服QQ -->配置
 */
public class AppContext extends BaseApplication {


    private static AppContext instance;

    private String loginUid;
    private String Token;
    private boolean login = false;
    public static String address = "好像在火星";
    public static String province = "";
    //HHH 2016-09-09
    public static String lng = "1";
    public static String lat = "1";


    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        init();
        initLoginNew();
        UIHelper.sendBroadcastForNotice(this);
        setScreenMsg(this);
        MobclickAgent.setPageCollectionMode(MobclickAgent.PageMode.AUTO);

        //友盟统计 如果配置好key 需要打开
        UMConfigure.init(this,AppConfig.UMENG_APP_KEY,"Umeng",UMConfigure.DEVICE_TYPE_PHONE,null);
    }


    private void init() {


//        //环信初始化
//        EMOptions options = new EMOptions();
//        // 默认添加好友时，是不需要验证的，改成需要验证
//        options.setAcceptInvitationAlways(false);
//
//        //初始化
//        EMClient.getInstance().init(this, options);
//        //在做打包混淆时，关闭debug模式，避免消耗不必要的资源
//        EMClient.getInstance().setDebugMode(false);
//        setGlobalListeners();


        //初始化jpush
//        JPushInterface.setDebugMode(true);
//        JPushInterface.init(this);


        //网络请求初始化
        CookieJarImpl cookieJar = new CookieJarImpl(new PersistentCookieStore(getApplicationContext()));
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .cookieJar(cookieJar)
                .cache(null)
                .retryOnConnectionFailure(true)
                .addInterceptor(new LoggerInterceptor("weipeng"))
                //其他配置
                .build();

        OkHttpUtils.initClient(okHttpClient);

//        //字体库
//        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
//                .setDefaultFontPath("tqht.otf")
//                .setFontAttrId(R.attr.fontPath)
//                .build()
//        );

    }


    protected void setGlobalListeners() {
        EMClient.getInstance().addConnectionListener(new MyConnectionListener());
        EMClient.getInstance().chatManager().addMessageListener(msgListener);
    }


    //实现ConnectionListener接口
    private class MyConnectionListener implements EMConnectionListener {
        @Override
        public void onConnected() {
        }

        @Override
        public void onDisconnected(final int error) {
            if (error == EMError.USER_REMOVED) {
                // 显示帐号已经被移除
                TLog.log("显示帐号已经被移除");
            } else if (error == EMError.USER_LOGIN_ANOTHER_DEVICE) {
                // 显示帐号在其他设备登陆
                TLog.log("显示帐号在其他设备登陆");
            } else {
                if (NetUtils.hasNetwork(AppContext.getInstance())) {
                    //连接不到聊天服务器
                    TLog.log("连接不到聊天服务器");
                } else {
                    //当前网络不可用，请检查网络设置
                    TLog.log("当前网络不可用，请检查网络设置");
                    Event.CommonEvent event = new Event.CommonEvent();
                    event.action = 1;
                    EventBus.getDefault().post(event);
                }

            }
        }
    }

    private EMMessageListener msgListener = new EMMessageListener() {

        @Override
        public void onMessageReceived(List<EMMessage> messages) {
            TLog.error("--------收到消息:" + messages);
            Intent broadcastIntent = new Intent("com.duomizhibo.phonelive");
            broadcastIntent.putExtra("cmd_value", messages.get(0));
            sendBroadcast(broadcastIntent, null);
            //收到消息
        }

        @Override
        public void onCmdMessageReceived(List<EMMessage> messages) {
            //收到透传消息
            TLog.error("----------收到透传消息:" + messages);
        }

        @Override
        public void onMessageRead(List<EMMessage> messages) {
            TLog.error("----------onMessageRead:" + messages);
        }

        @Override
        public void onMessageDelivered(List<EMMessage> messages) {
            TLog.error("----------onMessageDelivered:" + messages);
        }


        @Override
        public void onMessageChanged(EMMessage message, Object change) {
            //消息状态变动
            TLog.log("----------消息状态变动:" + message);
        }
    };

    private void initLogin() {
        UserBean user = getLoginUser();
        if (null != user && StringUtils.toInt(user.id) > 0) {
            login = true;
            loginUid = user.id;
            Token = user.token;
        } else {
            this.cleanLoginInfo();
        }
    }

    private void initLoginNew() {
        MemberInfo user = getLoginUserNew();
        if (null != user && StringUtils.toInt(user.getId()) > 0 && null!=user.getToken() && !StringUtils.isEmpty(user.getToken())) {
            login = true;
            loginUid = user.getId();
            Token = user.getToken();
        } else {
            this.cleanLoginInfo();
        }
    }

    /**
     * 获得当前app运行的AppContext
     *
     * @return
     */
    public static AppContext getInstance() {
        return instance;
    }

    public boolean containsProperty(String key) {
        Properties props = getProperties();
        return props.containsKey(key);
    }

    public void setProperties(Properties ps) {
        AppConfig.getAppConfig(this).set(ps);
    }

    public Properties getProperties() {
        return AppConfig.getAppConfig(this).get();
    }

    public void setProperty(String key, String value) {
        AppConfig.getAppConfig(this).set(key, value);
    }

    /**
     * 获取cookie时传AppConfig.CONF_COOKIE
     *
     * @param key
     * @return
     */
    public String getProperty(String key) {
        String res = AppConfig.getAppConfig(this).get(key);
        return res;
    }

    public void removeProperty(String... key) {
        AppConfig.getAppConfig(this).remove(key);
    }

    /**
     * 获取App唯一标识
     *
     * @return
     */
    public String getAppId() {
        String uniqueID = getProperty(AppConfig.CONF_APP_UNIQUEID);
        if (StringUtils.isEmpty(uniqueID)) {
            uniqueID = UUID.randomUUID().toString();
            setProperty(AppConfig.CONF_APP_UNIQUEID, uniqueID);
        }
        return uniqueID;
    }

    /**
     * 获取App安装包信息
     *
     * @return
     */
    public PackageInfo getPackageInfo() {
        PackageInfo info = null;
        try {
            info = getPackageManager().getPackageInfo(getPackageName(), 0);
        } catch (NameNotFoundException e) {
            e.printStackTrace(System.err);
        }
        if (info == null)
            info = new PackageInfo();
        return info;
    }

    /**
     * 保存登录信息
     *
     * @param user 用户信息
     */
    @SuppressWarnings("serial")
    public void saveUserInfo(final UserBean user) {
        this.loginUid = user.id;
        this.Token = user.token;
        this.login = true;
        setProperties(new Properties() {
            {
                setProperty("user.uid", user.id);
                setProperty("user.name", user.user_nicename);
                setProperty("user.token", user.token);
                setProperty("user.sign", user.signature);
                setProperty("user.avatar", user.avatar);

                setProperty("user.coin", user.coin);
                setProperty("user.sex", user.sex);
                setProperty("user.signature", user.signature);
                setProperty("user.avatar_thumb", user.avatar_thumb);
                setProperty("user.level", user.level);
                setProperty("user.level_anchor", user.level_anchor);
                setProperty("user.isreg", user.isreg== null ? "" :user.isreg);

            }
        });
    }

    public void saveUserInfoNew(final MemberInfo user) {
        this.loginUid = user.getId();
        this.Token = user.getToken();
        this.login = true;
        setProperties(new Properties() {
            {

                setProperty("user.id",  StringUtils.isEmpty(user.getId())?"":user.getId());
                setProperty("user.gid",  StringUtils.isEmpty(user.getGid())?"":user.getGid());
                setProperty("user.username",  StringUtils.isEmpty(user.getUsername())?"":user.getUsername());
                setProperty("user.tel",  StringUtils.isEmpty(user.getTel())?"":user.getTel());
                setProperty("user.nickname",  StringUtils.isEmpty(user.getNickname())?"":user.getNickname());
                setProperty("user.headimgurl",  StringUtils.isEmpty(user.getHeadimgurl())?"":user.getHeadimgurl());
                setProperty("user.email",  StringUtils.isEmpty(user.getEmail())?"":user.getEmail());
                setProperty("user.money", user.getMoney()+"");
                setProperty("user.out_time", user.getOut_time()+"");
                setProperty("user.is_agent", user.getIs_agent()+"");
                setProperty("user.is_permanent", user.getIs_permanent()+"");
                setProperty("user.sex", user.getSex()+"");
                setProperty("user.isVip", user.isVip()==true?"1":"0");
                setProperty("user.isEverVip", user.isEverVip()?"1":"0");
                setProperty("user.tel_asterisk",  StringUtils.isEmpty(user.getTel())?"":user.getTel_asterisk());
                setProperty("user.token",  StringUtils.isEmpty(user.getToken())?"":user.getToken());

            }
        });
    }

    /**
     * 更新用户信息
     *
     * @param user
     */
    @SuppressWarnings("serial")
    public void updateUserInfo(final UserBean user) {
        setProperties(new Properties() {
            {
                setProperty("user.uid", user.id);
                setProperty("user.name", user.user_nicename);
                setProperty("user.sign", user.signature);
                setProperty("user.avatar", user.avatar);
                setProperty("user.city", user.city == null ? "" : user.city);
                setProperty("user.coin", user.coin);
                setProperty("user.sex", user.sex);
                setProperty("user.signature", user.signature);
                setProperty("user.avatar_thumb", user.avatar_thumb);
                setProperty("user.level", user.level);
                setProperty("user.level_anchor", user.level_anchor);
                setProperty("user.isreg", user.isreg== null ? "" :user.isreg);
            }
        });
    }

    public void updateUserInfoNew(final MemberInfo user) {
        setProperties(new Properties() {
            {
                setProperty("user.id",  StringUtils.isEmpty(user.getId())?"":user.getId());
                setProperty("user.gid",  StringUtils.isEmpty(user.getGid())?"":user.getGid());
                setProperty("user.username",  StringUtils.isEmpty(user.getUsername())?"":user.getUsername());
                setProperty("user.tel",  StringUtils.isEmpty(user.getTel())?"":user.getTel());
                setProperty("user.nickname",  StringUtils.isEmpty(user.getNickname())?"":user.getNickname());
                setProperty("user.headimgurl",  StringUtils.isEmpty(user.getHeadimgurl())?"":user.getHeadimgurl());
                setProperty("user.email",  StringUtils.isEmpty(user.getEmail())?"":user.getEmail());
                setProperty("user.money", user.getMoney()+"");
                setProperty("user.out_time", user.getOut_time()+"");
                setProperty("user.is_agent", user.getIs_agent()+"");
                setProperty("user.is_permanent", user.getIs_permanent()+"");
                setProperty("user.sex", user.getSex()+"");
                setProperty("user.isVip", user.isVip()==true?"1":"0");
                setProperty("user.isEverVip", user.isEverVip()?"1":"0");
                setProperty("user.tel_asterisk",  StringUtils.isEmpty(user.getTel())?"":user.getTel_asterisk());
                setProperty("user.token",  StringUtils.isEmpty(user.getToken())?"":user.getToken());

            }
        });
    }

    /**
     * 获得登录用户的信息
     *
     * @return
     */
    public UserBean getLoginUser() {
        UserBean user = new UserBean();
        user.id = getProperty("user.uid");
        user.avatar = getProperty("user.avatar");
        user.user_nicename = getProperty("user.name");
        user.signature = getProperty("user.sign");
        user.token = getProperty("user.token");
        user.votes = getProperty("user.votes");
        user.city = getProperty("user.city");
        user.coin = getProperty("user.coin");
        user.sex = getProperty("user.sex");
        user.signature = getProperty("user.signature");
        user.avatar = getProperty("user.avatar");
        user.level = getProperty("user.level");
        user.level_anchor = getProperty("user.level_anchor");
        user.avatar_thumb = getProperty("user.avatar_thumb");
        user.birthday = getProperty("user.birthday");
        user.isreg      = getProperty("user.isreg");
        return user;
    }

    public MemberInfo getLoginUserNew() {
        MemberInfo user = new MemberInfo();
        user.setId(getProperty("user.id"));
        user.setGid(getProperty("user.gid"));
        user.setUsername(getProperty("user.username"));
        user.setTel(getProperty("user.tel"));
        user.setNickname(getProperty("user.nickname"));
        user.setHeadimgurl(getProperty("user.headimgurl"));
        user.setEmail(getProperty("user.email"));
        user.setMoney(StringUtils.toDouble(getProperty("user.money")));
        user.setOut_time(getProperty("user.out_time"));
        user.setIs_agent(StringUtils.toInt(getProperty("user.is_agent")));
        user.setIs_permanent(StringUtils.toInt(getProperty("user.is_permanent")));
        user.setSex(StringUtils.toInt(getProperty("user.sex")));
        user.setVip("1".equals(getProperty("user.isVip"))?true:false);
        user.setEverVip("1".equals(getProperty("user.isEverVip"))?true:false);
        user.setTel_asterisk(getProperty("user.tel_asterisk"));
        user.setToken(getProperty("user.token"));
        return user;
    }

    /**
     * 清除登录信息
     */
    public void cleanLoginInfo() {
        this.loginUid = "0";
        this.login = false;
        this.Token="";
        removeProperty("user.isreg","user.birthday", "user.avatar_thumb", "user.uid", "user.token", "user.name", "user.pwd", "user.avatar", "user.sign", "user.city", "user.coin", "user.sex", "user.signature", "user.signature", "user.avatar", "user.level", "user.level_anchor");
        removeProperty("user.id","user.gid", "user.username", "user.tel", "user.nickname", "user.headimgurl", "user.email", "user.money", "user.out_time","user.is_agent", "user.is_permanent", "user.sex", "user.isVip", "user.isEverVip", "user.tel_asterisk", "user.token");
    }

    public String getLoginUid() {
        return loginUid;
    }

    public String getToken() {
        return Token;
    }

    public boolean isLogin() {
        return login;
    }

    /**
     * 用户注销
     */
    public void Logout() {
        cleanLoginInfo();

        this.login = false;
        this.loginUid = "0";
        this.Token = "";

    }

    /**
     * 清除app缓存
     */
    public void clearAppCache() {
        DataCleanManager.cleanDatabases(this);
        // 清除数据缓存
        DataCleanManager.cleanInternalCache(this);
        // 2.2版本才有将应用缓存转移到sd卡的功能
        if (isMethodsCompat(android.os.Build.VERSION_CODES.FROYO)) {
            DataCleanManager.cleanCustomCache(MethodsCompat
                    .getExternalCacheDir(this));
        }
        // 清除编辑器保存的临时内容
        Properties props = getProperties();
        for (Object key : props.keySet()) {
            String _key = key.toString();
            if (_key.startsWith("temp"))
                removeProperty(_key);
        }
    }


    /**
     * 判断当前版本是否兼容目标版本的方法
     *
     * @param VersionCode
     * @return
     */
    public static boolean isMethodsCompat(int VersionCode) {
        int currentVersion = android.os.Build.VERSION.SDK_INT;
        return currentVersion >= VersionCode;
    }

    public static String getTweetDraft() {
        return getPreferences().getString(
                AppConfig.KEY_TWEET_DRAFT + getInstance().getLoginUid(), "");
    }

    public static void setTweetDraft(String draft) {
        set(AppConfig.KEY_TWEET_DRAFT + getInstance().getLoginUid(), draft);
    }

    public static String getNoteDraft() {
        return getPreferences().getString(
                AppConfig.KEY_NOTE_DRAFT + getInstance().getLoginUid(), "");
    }

    public static void setNoteDraft(String draft) {
        set(AppConfig.KEY_NOTE_DRAFT + getInstance().getLoginUid(), draft);
    }

    public static boolean isFristStart() {
        return getPreferences().getBoolean(AppConfig.KEY_FRITST_START, true);
    }

    public static void setFristStart(boolean frist) {
        set(AppConfig.KEY_FRITST_START, frist);
    }

    //夜间模式
    public static boolean getNightModeSwitch() {
        return getPreferences().getBoolean(AppConfig.KEY_NIGHT_MODE_SWITCH, false);
    }

    // 设置夜间模式
    public static void setNightModeSwitch(boolean on) {
        set(AppConfig.KEY_NIGHT_MODE_SWITCH, on);
    }

    public static void showToastAppMsg(Context context, String msg) {
        if (context == null) return;
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }


    public static void toast(String msg) {
        Toast.makeText(instance, msg, Toast.LENGTH_SHORT).show();
    }






    /*------------------------------*/
    public static float density;
    public static int screenWidth;// 屏幕的宽  px
    public static int screenHeight;// 屏幕整体高度（包括手机状态栏）px
    public static int contentHeight;// 屏幕显示内容的像素高度（不包括手机状态栏的高度，一般手机状态栏的高度是25dip）px

    private void setScreenMsg(Context context){
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        density = dm.density;
        screenWidth = dm.widthPixels;
        screenHeight = dm.heightPixels;
        contentHeight = (int) (screenHeight)-getStatusBarHeight(context);
    }
    /**
     * 获取状态栏的高度
     * @param paramContext
     * @return
     */
    private static int getStatusBarHeight(Context paramContext) {
        try {
            Class localClass = Class.forName("com.android.internal.R$dimen");
            Object localObject = localClass.newInstance();
            int i = Integer.parseInt(localClass.getField("status_bar_height").get(localObject).toString());
            int j = paramContext.getResources().getDimensionPixelSize(i);
            return j;
        } catch (Exception localException) {
        }
        return (int) (25 * density + 0.5f);//获取失败，默认25dip
    }


    //获取版本信息
    public VersionObject getVersonObject(Context paramContext) {
        PackageManager pm = paramContext.getPackageManager();
        VersionObject versionObject=null;
        try {
            String uniqueID= getAppId();
            PackageInfo packageInfo = pm.getPackageInfo(paramContext.getPackageName(), 0);
            String versionName= packageInfo.versionName; //返回版本号
            int  versionCode= packageInfo.versionCode;
            versionObject=new VersionObject();
            versionObject.setUniqueID(uniqueID);
            versionObject.setVersionCode(versionCode);
            versionObject.setVersionName(versionName);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return  versionObject;
    }



}

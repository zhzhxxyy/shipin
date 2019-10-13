package com.duomizhibo.phonelive;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;
import android.preference.PreferenceManager;

import com.duomizhibo.phonelive.bean.LinkMicBean;

import org.json.JSONArray;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * 应用程序配置类：用于保存用户相关信息及设置
 */
public class AppConfig {

    public static String URL_COMMON = "http://spf.zhzhxxyy.com";//视频的地址
    public static final String URL_CHECK_ROUTE = "http://spf.zhzhxxyy.com";//线路检查
    public static final String UMENG_APP_KEY = ""; //友盟统计的key



    /*-----------以下没用 不管-------*/

    //域名
    public static final String MAIN_URL = "http://11.11.1.1";
    public static  final String MAIN_URL_VIDEO="http://11.11.1.1/api/public/";
    //api地址
    public static final String MAIN_URL_API = MAIN_URL + "/api/public/";
    //支付宝回调地址
    public static final String AP_LI_PAY_NOTIFY_URL = MAIN_URL + "/Appapi/Pay/notify_ali";

    public static String GLOBAL_UUID = "";//uuid
    public static String GLOBAL_WX_KEY = "";//微信KEY
    public static String TICK_NAME = ""; //魅力值昵称
    public static String CURRENCY_NAME = ""; //钻石
    public static int JOIN_ROOM_ANIMATION_LEVEL = 0;//进场动画等级限制
    public static int ROOM_CHARGE_SWITCH = 0;//收费房间开关
    public static int ROOM_PASSWORD_SWITCH = 0;//密码房间开关
    public static int USERLIST_TIME = 60;//用户列表定时刷新时间
    public static JSONArray LIVE_TIME_COIN ;
    public static JSONArray LIVE_TYPE ;
    public static JSONArray SHARE_TYPE ;
    public static String SHUTUP_TIME="";
    public static String APK_DES="";
    public static String  VIDEO_SHARE_TITLE="";
    public static String VIDEO_SHARE_DES="";
    public static int LINKMIC_LIMIT=0;

    public static String UPPER="11359";//渠道id


    private final static String APP_CONFIG = "config";

    public final static String CONF_APP_UNIQUEID = "APP_UNIQUEID";

    public static final String KEY_TWEET_DRAFT = "KEY_TWEET_DRAFT";

    public static final String KEY_NOTE_DRAFT = "KEY_NOTE_DRAFT";

    public static final String KEY_FRITST_START = "KEY_FRIST_START";

    public static final String KEY_NIGHT_MODE_SWITCH = "night_mode_switch";

    public static final int USER_INFO_MAXLEN = 20;

    public static final String SVR_RETURN_DATA = "returnData";

    public static final int XIAOZHIBO_APPID = 1251037496;

    public static final List<LinkMicBean> linkList = new ArrayList<>();
    // 默认存放图片的路径
    public final static String DEFAULT_SAVE_IMAGE_PATH = Environment
            .getExternalStorageDirectory()
            + File.separator
            + "phoneLive"
            + File.separator + "live_img" + File.separator;



    /**
     * 缓存文件保存路径
     */
    public static final String CACHE_FILE_PATH = "/smile/cache/";

    // 默认存放文件下载的路径
    public final static String DEFAULT_SAVE_FILE_PATH = Environment
            .getExternalStorageDirectory()
            + File.separator
            + "phoneLive"
            + File.separator + "download" + File.separator;

    public final static String DEFAULT_SAVE_MUSIC_PATH = Environment
            .getExternalStorageDirectory()
            + File.separator
            + "phoneLive"
            + File.separator + "music" + File.separator;


    private Context mContext;
    private static AppConfig appConfig;

    public static AppConfig getAppConfig(Context context) {
        if (appConfig == null) {
            appConfig = new AppConfig();
            appConfig.mContext = context;
        }
        return appConfig;
    }

    /**
     * 获取Preference设置
     */
    public static SharedPreferences getSharedPreferences(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    public String get(String key) {
        Properties props = get();
        return (props != null) ? props.getProperty(key) : null;
    }

    public Properties get() {
        FileInputStream fis = null;
        Properties props = new Properties();
        try {
            // 读取files目录下的config
            // fis = activity.openFileInput(APP_CONFIG);

            // 读取app_config目录下的config
            File dirConf = mContext.getDir(APP_CONFIG, Context.MODE_PRIVATE);
            fis = new FileInputStream(dirConf.getPath() + File.separator
                    + APP_CONFIG);

            props.load(fis);
        } catch (Exception e) {
        } finally {
            try {
                fis.close();
            } catch (Exception e) {
            }
        }
        return props;
    }

    private void setProps(Properties p) {
        FileOutputStream fos = null;
        try {
            // 把config建在files目录下
            // fos = activity.openFileOutput(APP_CONFIG, Context.MODE_PRIVATE);

            // 把config建在(自定义)app_config的目录下
            File dirConf = mContext.getDir(APP_CONFIG, Context.MODE_PRIVATE);
            File conf = new File(dirConf, APP_CONFIG);
            fos = new FileOutputStream(conf);

            p.store(fos, null);
            fos.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fos.close();
            } catch (Exception e) {
            }
        }
    }

    public void set(Properties ps) {
        Properties props = get();
        props.putAll(ps);
        setProps(props);
    }

    public void set(String key, String value) {
        Properties props = get();
        props.setProperty(key, value);
        setProps(props);
    }

    public void remove(String... key) {
        Properties props = get();
        for (String k : key)
            props.remove(k);
        setProps(props);
    }
}

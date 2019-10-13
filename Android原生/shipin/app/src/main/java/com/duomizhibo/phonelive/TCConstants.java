package com.duomizhibo.phonelive; /**
 * Created by mengyunfeng on 17/5/29.
 */

/**
 * 静态函数
 */
public class TCConstants {
    /**
     * UGC小视频录制信息
     */
    public static final String VIDEO_RECORD_TYPE        = "type";
    public static final String VIDEO_RECORD_RESULT      = "result";
    public static final String VIDEO_RECORD_DESCMSG     = "descmsg";
    public static final String VIDEO_RECORD_VIDEPATH    = "path";
    public static final String VIDEO_RECORD_COVERPATH   = "coverpath";
    public static final String VIDEO_RECORD_ROTATION    = "rotation";
    public static final String VIDEO_RECORD_NO_CACHE    = "nocache";
    public static final String VIDEO_RECORD_DURATION    =  "duration";
    public static final String ACTIVITY_RESULT  = "activity_result";
    public static final String VIDEO_RECORD_FILENAME  = "";
    public static final int VIDEO_RECORD_TYPE_PUBLISH   = 1;   // 推流端录制
    public static final int VIDEO_RECORD_TYPE_PLAY      = 2;   // 播放端录制


    public static final String DEFAULT_MEDIA_PACK_FOLDER = "txrtmp";      // UGC编辑器输出目录





    /**
     * 用户可见的错误提示语
     */
    public static final String ERROR_MSG_NET_DISCONNECTED    = "网络异常，请检查网络";

    //直播端错误信息
    public static final String ERROR_MSG_CREATE_GROUP_FAILED = "创建直播房间失败,Error:";
    public static final String ERROR_MSG_GET_PUSH_URL_FAILED = "拉取直播推流地址失败,Error:";
    public static final String ERROR_MSG_OPEN_CAMERA_FAIL    = "无法打开摄像头，需要摄像头权限";
    public static final String ERROR_MSG_OPEN_MIC_FAIL       = "无法打开麦克风，需要麦克风权限";
    public static final String ERROR_MSG_RECORD_PERMISSION_FAIL   = "无法进行录屏,需要录屏权限";
    public static final String ERROR_MSG_NO_LOGIN_CACHE   = "您的帐号已在其它地方登录";

    //播放端错误信息
    public static final String ERROR_MSG_GROUP_NOT_EXIT      = "直播已结束，加入失败";
    public static final String ERROR_MSG_JOIN_GROUP_FAILED   = "加入房间失败，Error:";
    public static final String ERROR_MSG_LIVE_STOPPED        = "直播已结束";
    public static final String ERROR_MSG_NOT_QCLOUD_LINK     = "非腾讯云链接，若要放开限制请联系腾讯云商务团队";
    public static final String ERROR_RTMP_PLAY_FAILED        = "视频流播放失败，Error:";

    public static final String TIPS_MSG_STOP_PUSH            = "当前正在直播，是否退出直播？";

    //网络类型
    public static final int NETTYPE_WIFI = 0;
    public static final int NETTYPE_NONE = 1;
    public static final int NETTYPE_2G   = 2;
    public static final int NETTYPE_3G   = 3;
    public static final int NETTYPE_4G   = 4;
}

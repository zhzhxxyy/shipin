package com.duomizhibo.phonelive.utils;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.Settings;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.support.annotation.NonNull;
import android.support.v4.content.PermissionChecker;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.duomizhibo.phonelive.R;
import com.duomizhibo.phonelive.TCConstants;
import com.duomizhibo.phonelive.ui.other.DrawableRes;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.Properties;

/**
 * Created by Administrator on 2016/8/3.
 */
public class TCUtils {

    private static Toast mToast;

    public static Toast getInstanceToast(String body){
        if (mToast == null) {
            synchronized (TCUtils.class) {
                if (mToast == null) {
//                    mToast = Toast.makeText(TCApplication.getInstance(),body,Toast.LENGTH_SHORT);
                }
            }
        }
        mToast.setText(body);

        return mToast;

    }

    /**
     * 判断手机号是否有效
     * @param phoneNum 手机号
     * @return 有效则返回true, 无效则返回false
     * */
    public static boolean isPhoneNumValid(String phoneNum) {
        return phoneNum.length() == 11 && phoneNum.matches("[0-9]{1,}");
    }

    /**
     *
     * @param password 用户输入密码
     * @return 有效则返回true, 无效则返回false
     */
    public static boolean isPasswordValid(String password) {
        return password.length() >= 6 && password.length() <= 30;
    }

    /**
     *
     * @param username 用户名
     * @return 同上
     */
    public static boolean isUsernameVaild(String username) {
        return !username.matches("[0-9]+") && username.matches("^[a-z0-9_-]{4,24}$");
    }

    /**
     * @param verifyCode 验证码
     * @return 同上
     */
    public static boolean isVerifyCodeValid(String verifyCode) {
        return verifyCode.length() > 3;
    }

    /**
    * @dw 检测密码是否一致
    * @param p1 密码
    * @param p2 确认密码
    * */

    public static boolean isAgreement(String p1,String p2){
        return p1.equals(p2);
    }
    /**
     *
     * @param countryCode 国家码
     * @param phoneNumber 手机号
     * @return 返回拼接后的字符串
     */
    public static String getWellFormatMobile(String countryCode, String phoneNumber) {
        return countryCode + "-" + phoneNumber;
    }

    // 根据原图绘制圆形图片
    static public Bitmap createCircleImage(Bitmap source, int min){
        final Paint paint = new Paint();
        paint.setAntiAlias(true);
        if (0 == min){
            min = source.getHeight()>source.getWidth() ? source.getWidth() : source.getHeight();
        }
        Bitmap target = Bitmap.createBitmap(min, min, Bitmap.Config.ARGB_8888);
        // 创建画布
        Canvas canvas = new Canvas(target);
        // 绘圆
        canvas.drawCircle(min/2, min/2, min/2, paint);
        // 设置交叉模式
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        // 绘制图片
        canvas.drawBitmap(source, 0, 0, paint);
        return target;
    }

    // 字符串截断
    public static String getLimitString(String source, int length){
        if (null!=source && source.length()>length){
//            int reallen = 0;
            return source.substring(0, length)+"...";
        }
        return source;
    }

    // 字符串截断
    public static String getLimitStringWithoutNode(String source, int length){
        if (null != source && source.length() > length){
            return source.substring(0, length);
        }
        return source;
    }

    public static String getStreamIDByStreamUrl(String strStreamUrl) {
        if (strStreamUrl == null || strStreamUrl.length() == 0) {
            return null;
        }

        strStreamUrl = strStreamUrl.toLowerCase();

        //推流地址格式：rtmp://8888.livepush.myqcloud.com/live/8888_test_12345_test?txSecret=aaaa&txTime=bbbb
        //拉流地址格式：rtmp://8888.liveplay.myqcloud.com/live/8888_test_12345_test
        //            http://8888.liveplay.myqcloud.com/live/8888_test_12345_test.flv
        //            http://8888.liveplay.myqcloud.com/live/8888_test_12345_test.m3u8

        String strLive = "/live/";
        int index = strStreamUrl.indexOf(strLive);
        if (index == -1) {
            return null;
        }

        String strSubString = strStreamUrl.substring(index + strLive.length());
        String [] strArrays = strSubString.split("[?.]");
        if (strArrays.length > 0) {
            return strArrays[0];
        }

        return null;
    }

//    //获取等级资源图片
//    public static int getLevelRes(@NonNull int level){
//
//        return DrawableRes.LEVEL_IMG[level == 0 ? 1 : level -1];
//    }
//
//    //获取性别
//    public static int getSexRes(@NonNull int sex){
//
//        return sex == 1 ? R.drawable.ic_man : R.drawable.ic_weman;
//    }
//    public static String getSexStr(@NonNull int sex){
//        return sex == 1 ? "男" : "女";
//    }
//
//    //安全返回
//    public static String setDefaultValue(String value,String defaultV){
//        return value == null ? defaultV : value;
//    }

//    /**
//    * @dw返回关注图片
//    * @param state 关注状态
//    *
//    * */
//    public static int returnAttentionRes(int state){
//        return state == 0 ? R.drawable.icon_gz : R.drawable.icon_ygz;
//    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }

    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context The context.
     * @param uri The Uri to query.
     * @param selection (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {

                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    /**
     * Get a file path from a Uri. This will get the the path for Storage Access
     * Framework Documents, as well as the _data field for the MediaStore and
     * other file-based ContentProviders.<br>
     * <br>
     * Callers should check whether the path is local before assuming it
     * represents a local file.
     *
     * @param context The context.
     * @param uri The Uri to query.
     */
    @TargetApi(19)
    public static String getPath(final Context context, final Uri uri) {
        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }

                // TODO handle non-primary volumes
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[] {
                        split[1]
                };

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {

            // Return the remote address
            if (isGooglePhotosUri(uri))
                return uri.getLastPathSegment();

            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    /**
     * 在按钮上启动一个定时器
     * @param tvVerifyCode 验证码控件
     * @param defaultString 按钮上默认的字符串
     * @param max 失效时间（单位：s）
     * @param interval 更新间隔（单位：s）
     * */
    public static void startTimer(final WeakReference<TextView> tvVerifyCode,
                                  final String defaultString,
                                  int max,
                                  int interval)
    {
        tvVerifyCode.get().setEnabled(false);

        // 由于CountDownTimer并不是准确计时，在onTick方法调用的时候，time会有1-10ms左右的误差，这会导致最后一秒不会调用onTick()
        // 因此，设置间隔的时候，默认减去了10ms，从而减去误差。
        // 经过以上的微调，最后一秒的显示时间会由于10ms延迟的积累，导致显示时间比1s长max*10ms的时间，其他时间的显示正常,总时间正常
        new CountDownTimer(max * 1000, interval * 1000 - 10) {

            @Override
            public void onTick(long time) {
                // 第一次调用会有1-10ms的误差，因此需要+15ms，防止第一个数不显示，第二个数显示2s
                if(null == tvVerifyCode.get())
                    this.cancel();
                else
                    tvVerifyCode.get().setText("" + ((time + 15) / 1000) + "s");
            }

            @Override
            public void onFinish() {
                if(null == tvVerifyCode.get()) {
                    this.cancel();
                    return;
                }
                tvVerifyCode.get().setEnabled(true);
                tvVerifyCode.get().setText(defaultString);

            }
        }.start();
    }


//    public static String EnumGenderToString (TIMFriendGenderType genderType) {
//        if (TIMFriendGenderType.Male == genderType)  return "男";
//        if (TIMFriendGenderType.Female == genderType) return "女";
//
//        return "";
//    }


    /**
     * 时间格式化
     */
    public static String formattedTime(long second) {
        String hs, ms, ss, formatTime;

        long h, m, s;
        h = second / 3600;
        m = (second % 3600) / 60;
        s = (second % 3600) % 60;
        if (h < 10) {
            hs = "0" + h;
        } else {
            hs = "" + h;
        }

        if (m < 10) {
            ms = "0" + m;
        } else {
            ms = "" + m;
        }

        if (s < 10) {
            ss = "0" + s;
        } else {
            ss = "" + s;
        }
//        if (hs.equals("00")) {
//            formatTime = ms + ":" + ss;
//        } else {
            formatTime = hs + ":" + ms + ":" + ss;
//        }

        return formatTime;
    }

    public static int dp2pxConvertInt(Context context, float dpValue) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpValue, context.getResources().getDisplayMetrics());
    }

    public static float sp2px(Context context, float spValue) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, spValue, context.getResources().getDisplayMetrics());
    }
    /**
     * 圆角显示图片
     *
     * @param context 一般为activtiy
     * @param view 图片显示类
     * @param url 图片url
     * @param defResId 默认图 id
     *
     */
    public static void showPicWithUrl(Context context, ImageView view, String url, int defResId) {
        if (context == null || view == null) {
            return;
        }
        //.transform(new TCGlideCircleTransform(context))
        try {
            if (TextUtils.isEmpty(url)){
                view.setImageResource(defResId);
            } else {
                RequestManager req = Glide.with(context);
                req.load(url).placeholder(defResId).dontAnimate().into(view);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 获取网络类型
     */
    public static void blurBgPic(final Context context, final ImageView view, final String url, int defResId) {
        if (context == null || view == null) {
            return;
        }

        if (TextUtils.isEmpty(url)) {
            Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), defResId);
            view.setImageBitmap(blurBitmap(bitmap, context.getApplicationContext()));
        } else {
            Glide.with(context.getApplicationContext())
                .load(url)
                .asBitmap()
                .into(new SimpleTarget<Bitmap>(Target.SIZE_ORIGINAL,Target.SIZE_ORIGINAL) {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation glideAnimation) {
                        if (resource == null) {
                            return;
                        }

                        final Bitmap bitmap = blurBitmap(resource, context.getApplicationContext());
                        view.post(new Runnable() {
                            @Override
                            public void run() {
                                view.setImageBitmap(bitmap);
                            }
                        });
                    }
                });
        }
    }

    private static Bitmap blurBitmap(Bitmap resource, Context context) {
        Bitmap bitmap = Bitmap.createBitmap(resource.getWidth(), resource.getHeight(), Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint();
        paint.setFlags(Paint.FILTER_BITMAP_FLAG | Paint.ANTI_ALIAS_FLAG);
        PorterDuffColorFilter filter =
                new PorterDuffColorFilter( Color.TRANSPARENT, PorterDuff.Mode.SRC_ATOP);
        paint.setColorFilter(filter);
        canvas.drawBitmap(resource, 0, 0, paint);

        RenderScript rs = RenderScript.create(context.getApplicationContext());
        Allocation input = Allocation.createFromBitmap(rs, bitmap, Allocation.MipmapControl.MIPMAP_NONE,
                Allocation.USAGE_SCRIPT);
        Allocation output = Allocation.createTyped(rs, input.getType());
        ScriptIntrinsicBlur blur = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));

        blur.setInput(input);
        blur.setRadius(10);
        blur.forEach(output);
        output.copyTo(bitmap);
        rs.destroy();

        return bitmap;
    }

    /*
    * 获取网络类型
    */
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo info = connectivity.getActiveNetworkInfo();
            if (info != null && info.isConnected())
            {
                // 当前网络是连接的
                if (info.getState() == NetworkInfo.State.CONNECTED)
                {
                    // 当前所连接的网络可用
                    return true;
                }
            }
        }
        return false;
    }



    /**
     * 获取一段字符串的字符个数（包含中英文，一个中文算2个字符）
     */
    public static int getCharacterNum(final String content) {
        if (null == content || "".equals(content)) {
            return 0;
        } else {
            return (content.length() + getChineseNum(content));
        }
    }

    /**
     * 返回字符串里中文字或者全角字符的个数
     */
    public static int getChineseNum(String s) {
        int num = 0;
        char[] myChar = s.toCharArray();
        for (int i = 0; i < myChar.length; i++) {
            if ((char) (byte) myChar[i] != myChar[i]) {
                num++;
            }
        }
        return num;
    }

    /**
     * 计算指定的 View 在屏幕中的坐标。
     */
    public static RectF calcViewScreenLocation(View view) {
        int[] location = new int[2];
        // 获取控件在屏幕中的位置，返回的数组分别为控件左顶点的 x、y 的值
        view.getLocationOnScreen(location);
        return new RectF(location[0], location[1], location[0] + view.getWidth(),
                location[1] + view.getHeight());
    }

    /**
     * 网络是否正常
     * @param context Context
     * @return true 表示网络可用
     */
    public static int getNetWorkType(Context context) {
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {
            String type = networkInfo.getTypeName();

            if (type.equalsIgnoreCase("WIFI")) {
                return TCConstants.NETTYPE_WIFI;
            } else if (type.equalsIgnoreCase("MOBILE")) {
                NetworkInfo mobileInfo = manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
                if(mobileInfo != null) {
                    switch (mobileInfo.getType()) {
                        case ConnectivityManager.TYPE_MOBILE:// 手机网络
                            switch (mobileInfo.getSubtype()) {
                                case TelephonyManager.NETWORK_TYPE_UMTS:
                                case TelephonyManager.NETWORK_TYPE_EVDO_0:
                                case TelephonyManager.NETWORK_TYPE_EVDO_A:
                                case TelephonyManager.NETWORK_TYPE_HSDPA:
                                case TelephonyManager.NETWORK_TYPE_HSUPA:
                                case TelephonyManager.NETWORK_TYPE_HSPA:
                                case TelephonyManager.NETWORK_TYPE_EVDO_B:
                                case TelephonyManager.NETWORK_TYPE_EHRPD:
                                case TelephonyManager.NETWORK_TYPE_HSPAP:
                                    return TCConstants.NETTYPE_3G;
                                case TelephonyManager.NETWORK_TYPE_CDMA:
                                case TelephonyManager.NETWORK_TYPE_GPRS:
                                case TelephonyManager.NETWORK_TYPE_EDGE:
                                case TelephonyManager.NETWORK_TYPE_1xRTT:
                                case TelephonyManager.NETWORK_TYPE_IDEN:
                                    return TCConstants.NETTYPE_2G;
                                case TelephonyManager.NETWORK_TYPE_LTE:
                                    return TCConstants.NETTYPE_4G;
                                default:
                                    return TCConstants.NETTYPE_NONE;
                            }
                    }
                }
            }
        }

        return TCConstants.NETTYPE_NONE;
    }
    public static boolean supportLinkMic() {
        return Build.VERSION.SDK_INT >= 18;
    }

    /**
     * 显示被踢下线通知
     * @param context activity
     */
//    public static void showKickOutDialog(final Context context) {
//        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.NormalDialog);
//        builder.setTitle(context.getResources().getString(R.string.tip_force_offline));
////        UserLoginMgr.getInstance().logout();
//        builder.setPositiveButton("退出", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                dialog.dismiss();
//
////                TCLoginMgr.getInstance().logout();
//                Intent intent = new Intent(context, UserLoginActivity.class);
//                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//
//                context.startActivity(intent);
//            }
//        });
//        builder.setNegativeButton("重新登录", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                dialog.dismiss();
//                TCLoginMgr.getInstance().reLogin();
//                Intent intent = new Intent(context, UserLoginActivity.class);
//                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                context.startActivity(intent);
//
//            }
//        });
//
//        AlertDialog alertDialog = builder.create();
//        alertDialog.setCancelable(false);
//        alertDialog.show();
//    }

    /**
     * 根据比例转化实际数值为相对值
     * @param gear 档位
     * @param max 最大值
     * @param curr 当前值
     * @return 相对值
     */
    public static int filtNumber(int gear, int max, int curr) {
        return curr / (max / gear);
    }

    /**
     * 悬浮窗权限检查
     * 当前仅对MIUI8作了适配，其余机型有待添加
     * @throws IOException
     */
    @TargetApi(Build.VERSION_CODES.KITKAT)
    public static void checkFloatWindowPermission(final Context context) throws IOException {

        Properties properties = new Properties();
        properties.load(new FileInputStream(new File(Environment.getRootDirectory(), "build.prop")));

        String versionmame = properties.getProperty("ro.miui.ui.version.name");
        if(null != versionmame){
            //miui8以上需要悬浮窗权限才能正常显示悬浮窗
            if (versionmame.equals("V8")) {

                //检测悬浮窗权限，无权限则弹出提示
                if (!checkPermission(context, Manifest.permission.SYSTEM_ALERT_WINDOW)) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.NormalDialog);
                    builder.setTitle(context.getString(R.string.float_window_not_allow));
                    builder.setPositiveButton("开启", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            final Intent intent = new Intent();
                            intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                            intent.addCategory(Intent.CATEGORY_DEFAULT);
                            intent.setData(Uri.parse("package:" + context.getPackageName()));
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                            intent.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                            context.startActivity(intent);
                        }
                    });
                    builder.setNegativeButton("不，谢谢", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    AlertDialog alertDialog = builder.create();
                    alertDialog.setCancelable(false);
                    alertDialog.show();
                }
            }
        }
    }

    /**
     * 权限检查
     * @param context context
     * @param permission permission
     * @return true -- 当前拥有该权限  false -- 当前无权限
     */
    @TargetApi(Build.VERSION_CODES.KITKAT)
    public static boolean checkPermission(@NonNull final Context context, String permission) {
        boolean result = false;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            try {
                final PackageInfo info = context.getPackageManager().getPackageInfo(
                        context.getPackageName(), 0);
                int targetSdkVersion = info.applicationInfo.targetSdkVersion;
                //targetSDKVersion为23时，可以直接通过context.checkSelfPermission检查权限
                if (targetSdkVersion >= Build.VERSION_CODES.M) {
                    result = context.checkSelfPermission(permission)
                            == PackageManager.PERMISSION_GRANTED;
                } else {
                    result = PermissionChecker.checkSelfPermission(context, permission)
                            == PermissionChecker.PERMISSION_GRANTED;
                }
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
        }

        return result;
    }

    /**
     * 滤镜定义
     */
    public static final int FILTERTYPE_NONE         = 0;    //无特效滤镜
    public static final int FILTERTYPE_langman      = 1;    //浪漫滤镜
    public static final int FILTERTYPE_qingxin      = 2;    //清新滤镜
    public static final int FILTERTYPE_weimei       = 3;    //唯美滤镜
    public static final int FILTERTYPE_fennen 		= 4;    //粉嫩滤镜
    public static final int FILTERTYPE_huaijiu 		= 5;    //怀旧滤镜
    public static final int FILTERTYPE_landiao 		= 6;    //蓝调滤镜
    public static final int FILTERTYPE_qingliang 	= 7;    //清凉滤镜
    public static final int FILTERTYPE_rixi 		= 8;    //日系滤镜

    private static Bitmap decodeResource(Resources resources, int id) {
        TypedValue value = new TypedValue();
        resources.openRawResource(id, value);
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inTargetDensity = value.density;
        return BitmapFactory.decodeResource(resources, id, opts);
    }
    public static Bitmap getFilterBitmap(Resources resources, int filterType) {
        Bitmap bmp = null;
        switch (filterType) {
            case FILTERTYPE_langman:
                bmp = decodeResource(resources, R.drawable.filter_langman);
                break;
            case FILTERTYPE_qingxin:
                bmp = decodeResource(resources, R.drawable.filter_qingxin);
                break;
            case FILTERTYPE_weimei:
                bmp = decodeResource(resources, R.drawable.filter_weimei);
                break;
            case FILTERTYPE_fennen:
                bmp = decodeResource(resources, R.drawable.filter_fennen);
                break;
            case FILTERTYPE_huaijiu:
                bmp = decodeResource(resources, R.drawable.filter_huaijiu);
                break;
            case FILTERTYPE_landiao:
                bmp = decodeResource(resources, R.drawable.filter_landiao);
                break;
            case FILTERTYPE_qingliang:
                bmp = decodeResource(resources, R.drawable.filter_qingliang);
                break;
            case FILTERTYPE_rixi:
                bmp = decodeResource(resources, R.drawable.filter_rixi);
                break;
            default:
                bmp = null;
                break;
        }
        return bmp;
    }

    public static String getGreenFileName(int index) {
        String strGreenFileName;
        switch (index) {
            case 0:
                strGreenFileName = "";
                break;
            case 1:
                strGreenFileName = "green_1.mp4";
                break;
            case 2:
                strGreenFileName = "green_2.mp4";
                break;
            default:
                strGreenFileName = "";
                break;
        }
        return strGreenFileName;
    }
}

package com.duomizhibo.phonelive.ui;

import android.app.Dialog;
import android.app.FragmentTransaction;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mob.MobSDK;
import com.tencent.rtmp.ITXLivePlayListener;
import com.tencent.rtmp.TXLiveConstants;
import com.tencent.rtmp.TXLivePlayConfig;
import com.tencent.rtmp.TXLivePlayer;
import com.tencent.rtmp.ugc.TXRecordCommon;
import com.tencent.rtmp.ugc.TXUGCPublish;
import com.tencent.rtmp.ui.TXCloudVideoView;
import com.duomizhibo.phonelive.AppConfig;
import com.duomizhibo.phonelive.AppContext;
import com.duomizhibo.phonelive.R;
import com.duomizhibo.phonelive.TCConstants;
import com.duomizhibo.phonelive.api.remote.ApiUtils;
import com.duomizhibo.phonelive.api.remote.PhoneLiveApi;
import com.duomizhibo.phonelive.base.ToolBarBaseActivity;
import com.duomizhibo.phonelive.bean.SimpleUserInfo;
import com.duomizhibo.phonelive.bean.UploadBean;
import com.duomizhibo.phonelive.ui.customviews.ErrorDialogFragment;
import com.duomizhibo.phonelive.ui.customviews.FixGridLayout;
import com.duomizhibo.phonelive.ui.customviews.ProgressPopWinFactory;
import com.duomizhibo.phonelive.ui.dialog.LiveCommon;
import com.duomizhibo.phonelive.utils.ShareUtils;
import com.duomizhibo.phonelive.utils.TCUtils;
import com.duomizhibo.phonelive.utils.TDevice;
import com.duomizhibo.phonelive.utils.TLog;
import com.duomizhibo.phonelive.utils.UploadUtil;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;

import cn.sharesdk.facebook.Facebook;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.tencent.qzone.QZone;
import cn.sharesdk.twitter.Twitter;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;
import okhttp3.Call;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.tencent.bugly.crashreport.inner.InnerAPI.context;

/**
 * Created by carolsuo on 2017/3/9.
 * UGC发布页面
 */
public class TCVideoPublisherActivity extends ToolBarBaseActivity implements CompoundButton.OnCheckedChangeListener, View.OnClickListener, ITXLivePlayListener {
    //分享相关
//    private SHARE_MEDIA mShare_meidia = SHARE_MEDIA.MORE;
    private CompoundButton mCbLastChecked = null;

    private String mVideoPath ;
    private String mCoverPath ;
    private String fileName;

    private TextView mBtnBack;
    private TextView mBtnPublish;

    private LinearLayout mLayoutEdit;
    private RelativeLayout mLayoutPublish;
    private FixGridLayout mShareType;

    boolean mIsPlayRecordType = false;

    private ImageView mIVPublishing;
    private TextView mTVPublish;
    private TextView mTVTitle;
    private EditText mTitleEditText;
    private boolean mIsFetchCosSig = false;
    String mCosSignature = null;
    Handler mHandler = new Handler();
    //private String mShareUrl = TCConstants.SVR_LivePlayShare_URL;
    private boolean mAllDone = false;

    private TXLivePlayer mTXLivePlayer = null;
    private TXLivePlayConfig mTXPlayConfig = null;
    private TXCloudVideoView mTXCloudVideoView;
    private NetchangeReceiver mNetchangeReceiver = null;
    private int mRotation;
    private boolean mDisableCache;
    private String mLocalVideoPath;
    private TextView mCity;

    //错误消息弹窗
    private ErrorDialogFragment mErrDlgFragment;

    int shareType=-2;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_video_publisher;
    }


    private void startPlay() {
        mTXLivePlayer.setPlayerView(mTXCloudVideoView);
        mTXLivePlayer.setPlayListener(this);

        mTXLivePlayer.enableHardwareDecode(false);
        mTXLivePlayer.setRenderRotation(mRotation);
        mTXLivePlayer.setRenderMode(TXLiveConstants.RENDER_MODE_FULL_FILL_SCREEN);

        mTXLivePlayer.setConfig(mTXPlayConfig);

        mTXLivePlayer.startPlay(mVideoPath, TXLivePlayer.PLAY_TYPE_LOCAL_VIDEO); // result返回值：0 success;  -1 empty url; -2 invalid url; -3 invalid playType;
    }


    //把视频上传到七牛云
    private void uploadVideoToQiNiu(final String title) {
        ProgressPopWinFactory.getInstance().show(TCVideoPublisherActivity.this, "正在发布，请耐心等待...");
        File mCoverPathFile = new File(mCoverPath);
        File mVideoPathFile = new File(mVideoPath);
        UploadBean datas = new UploadBean(
                mVideoPathFile, mVideoPathFile.getName() ,
                mCoverPathFile, mCoverPathFile.getName() );
        UploadUtil.getInstance().upload(datas, new UploadUtil.Callback() {
            @Override
            public void callback(UploadBean bean) {
                TLog.error("run: ----上传七牛云成功-->");
                requestUpload(title, bean);
            }
        });
    }

    private void requestUpload(String title, UploadBean bean) {
        PhoneLiveApi.uploadVideo(title, bean.getVideoName(), bean.getCoverPicName(), new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                ProgressPopWinFactory.getInstance().hide();
                showToast3("发布失败", 0);
            }

            @Override
            public void onResponse(String response, int id) {
                //ProgressPopWinFactory.getInstance().hide();
                JSONArray res= ApiUtils.checkIsSuccess(response);
                if (res!=null){
                    TLog.error("发布成功"+response);
                    ProgressPopWinFactory.getInstance().hide();
                    showToast3("发布成功", 0);
                    if (shareType==-2){
                        finishMainActivity();
                    }
                    try {
                        shareVideo(shareType,res.getJSONObject(0).getString("id"),res.getJSONObject(0).getString("thumb_s"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }


            }

        });
    }

    private void shareVideo(int index,String id,String thumb_s){
        try {
            String names = "";
            if (AppConfig.SHARE_TYPE.getString(index).equals("qq")){
                names= QQ.NAME;
            }else if (AppConfig.SHARE_TYPE.getString(index).equals("qzone")){
                names= QZone.NAME;
            }else if (AppConfig.SHARE_TYPE.getString(index).equals("wchat")){
                names= WechatMoments.NAME;
            }else if (AppConfig.SHARE_TYPE.getString(index).equals("wx")){
                names= Wechat.NAME;
            }else if (AppConfig.SHARE_TYPE.getString(index).equals("facebook")){
                names= Facebook.NAME;
            }else if (AppConfig.SHARE_TYPE.getString(index).equals("twitter")){
                names= Twitter.NAME;
            }
            share(this, names, AppConfig.VIDEO_SHARE_TITLE
                    ,  AppContext.getInstance().getLoginUser().user_nicename+ AppConfig.VIDEO_SHARE_DES, null, thumb_s, AppConfig.MAIN_URL+"/index.php?g=appapi&m=video&a=index&videoid="+id, null);
        }catch (Exception e){

        }

    }

    public  void share(final Context context, String name, String des, String title, final SimpleUserInfo user, String thumb_s, String shareUrl, PlatformActionListener listener) {
        MobSDK.init(context);
        final OnekeyShare oks = new OnekeyShare();
        oks.setSilent(true);
        //关闭sso授权
        oks.disableSSOWhenAuthorize();
        oks.setPlatform(name);
        // 分享时Notification的图标和文字  2.5.9以后的版本不调用此方法
        //oks.setNotification(R.drawable.ic_launcher, getString(R.string.app_name));
        // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
        oks.setTitle(title);
        // titleUrl是标题的网络链接，仅在人人网和QQ空间使用

        // text是分享文本，所有平台都需要这个字段
        oks.setText(des);
        if (user != null) {
            oks.setImageUrl(user.avatar_thumb);
        } else {
            oks.setImageUrl(thumb_s);
        }


        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
        //oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
        // url仅在微信（包括好友和朋友圈）中使用
        if (name.equals(Wechat.NAME) || name.equals(WechatMoments.NAME)) {
            oks.setUrl(shareUrl);
            oks.setSiteUrl(shareUrl);
            oks.setTitleUrl(shareUrl);

        } else {
            oks.setUrl(shareUrl);
            oks.setSiteUrl(shareUrl);
            oks.setTitleUrl(shareUrl);
        }

        // comment是我对这条分享的评论，仅在人人网和QQ空间使用
        //oks.setComment(context.getString(R.string.shartitle));
        // site是分享此内容的网站名称，仅在QQ空间使用
        oks.setSite(context.getString(R.string.app_name));
        oks.setCallback(new PlatformActionListener() {
            @Override
            public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
                Toast.makeText(TCVideoPublisherActivity.this,"分享成功",Toast.LENGTH_SHORT).show();
                finishMainActivity();
            }

            @Override
            public void onError(Platform platform, int i, Throwable throwable) {
                Toast.makeText(TCVideoPublisherActivity.this,"分享失败",Toast.LENGTH_SHORT).show();
                finishMainActivity();
            }

            @Override
            public void onCancel(Platform platform, int i) {
                Toast.makeText(TCVideoPublisherActivity.this,"分享取消",Toast.LENGTH_SHORT).show();
                finishMainActivity();
            }
        });
        // siteUrl是分享此内容的网站地址，仅在QQ空间使用

        // 启动分享GUI
        oks.show(context);
    }

    private void finishMainActivity() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                TCVideoPublisherActivity.this.finish();
                TCVideoRecordActivity.ActivityA.finish();
            }
        }, 2000);
    }


    private void addShareView(){
        if (AppConfig.SHARE_TYPE == null) return;
        for (int i = 0; i < AppConfig.SHARE_TYPE.length(); i++) {
            final ImageView im = new ImageView(TCVideoPublisherActivity.this);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams((int) TDevice.dpToPixel(60), (int) TDevice.dpToPixel(80));
            if (i > 0)
                lp.setMargins(0, 0, 0, 0);
            lp.gravity= Gravity.CENTER;
            im.setLayoutParams(lp);
            try {
                im.setImageResource(getResources().getIdentifier(AppConfig.SHARE_TYPE.getString(i) + "_share", "drawable", "com.duomizhibo.phonelive"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            WindowManager wm = (WindowManager) this
                    .getSystemService(Context.WINDOW_SERVICE);

            int width = wm.getDefaultDisplay().getWidth();
            mShareType.setmCellHeight((int) TDevice.dpToPixel(90));
            mShareType.setmCellWidth(width/4);
            mShareType.addView(im);
            final int finalI = i;
            im.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (shareType == finalI) {
                        try {
                            im.setImageResource(getResources().getIdentifier(AppConfig.SHARE_TYPE.getString(finalI) + "_share", "drawable", "com.duomizhibo.phonelive"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else {
                        try {
                            im.setImageResource(getResources().getIdentifier(AppConfig.SHARE_TYPE.getString(finalI) + "_share1", "drawable", "com.duomizhibo.phonelive"));
                            for (int j = 0; j < AppConfig.SHARE_TYPE.length(); j++) {
                                ImageView imJ = (ImageView) mShareType.getChildAt(j);
                                if (finalI != j) {
                                    imJ.setImageResource(getResources().getIdentifier(AppConfig.SHARE_TYPE.getString(j) + "_share", "drawable", "com.duomizhibo.phonelive"));
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    if (shareType == finalI) {
                        shareType = -2;
                    } else {
                        shareType = finalI;
                    }
                }
            });
        }
    }


    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked == false) {
            //  mShare_meidia = SHARE_MEDIA.MORE;
            mCbLastChecked = null;
            return;
        }
        if (mCbLastChecked != null) {
            mCbLastChecked.setChecked(false);
        }
        mCbLastChecked = buttonView;
//        switch (buttonView.getId()) {
//            case R.id.vpcb_share_wx:
//                // mShare_meidia = SHARE_MEDIA.WEIXIN;
//                break;
//            case R.id.vpcb_share_circle:
//                //  mShare_meidia = SHARE_MEDIA.WEIXIN_CIRCLE;
//                break;
//            case R.id.vpcb_share_qq:
//                // mShare_meidia = SHARE_MEDIA.QQ;
//                break;
//            case R.id.vpcb_share_qzone:
//                //   mShare_meidia = SHARE_MEDIA.QZONE;
//                break;
//            case R.id.vpcb_share_wb:
//                //  mShare_meidia = SHARE_MEDIA.SINA;
//                break;
//            default:
//                break;
//        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.btn_back:

                    this.finish();

                break;
            case R.id.btn_publish:
                if (mAllDone) {
                    finish();
                } else {
                    if (!TCUtils.isNetworkAvailable(this)) {
                        Toast.makeText(getApplicationContext(), "当前无网络连接", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (mTitleEditText.getText().toString().length()>20){
                        showToast3("标题不能超过20个字",0);
                        return;
                    }
                    uploadVideoToQiNiu(mTitleEditText.getText().toString());
                    mLayoutEdit.setVisibility(View.GONE);
                    mBtnPublish.setVisibility(View.GONE);
                    mTVTitle.setText("发布");
                    stopPlay(true);
                }
                break;
            default:
                break;
        }
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    }

    private void deleteCache() {
        if (mDisableCache) {
            File file = new File(mVideoPath);
            if (file.exists()) {
                file.delete();
            }
            if (!TextUtils.isEmpty(mCoverPath)) {
                file = new File(mCoverPath);
                if (file.exists()) {
                    file.delete();
                }
            }
            if (mLocalVideoPath != null) {
                Intent scanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                scanIntent.setData(Uri.fromFile(new File(mLocalVideoPath)));
                sendBroadcast(scanIntent);
            }
        }
    }


    //删除录制的视频，即放弃上传
    private void deleteVideo() {
        Observable.create(new Observable.OnSubscribe<Object>() {
            @Override
            public void call(Subscriber<? super Object> subscriber) {
                File file = new File(mVideoPath);
                if (file.exists()) {
                    file.delete();
                }
                file = new File(mCoverPath);
                if (file.exists()) {
                    file.delete();
                }
                subscriber.onNext("");
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Object>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(Object o) {
                        ProgressPopWinFactory.getInstance().hide();
                        showToast3("删除成功", 0);
                    }
                });
    }


    private void UploadUGCVideo(final String videoId, final String videoURL, final String coverURL) {
        String title = mTitleEditText.getText().toString();
    }

    // 事情都做完了
    void allDone() {
        mBtnBack.setVisibility(View.INVISIBLE);
        mBtnPublish.setText("完成");
        mBtnPublish.setVisibility(View.VISIBLE);
        mAllDone = true;
    }


    @Override
    protected void onResume() {
        super.onResume();
        mTXCloudVideoView.onResume();
        mTXLivePlayer.resume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mTXCloudVideoView.onPause();
        mTXLivePlayer.pause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mTXCloudVideoView.onDestroy();
        stopPlay(true);
        if (mNetchangeReceiver != null) {
            this.getApplicationContext().unregisterReceiver(mNetchangeReceiver);
        }

        deleteCache();
    }

    protected void stopPlay(boolean clearLastFrame) {
        if (mTXLivePlayer != null) {
            mTXLivePlayer.setPlayListener(null);
            mTXLivePlayer.stopPlay(clearLastFrame);
        }
    }


    @Override
    public void onPlayEvent(int event, Bundle param) {
        if (mTXCloudVideoView != null) {
            mTXCloudVideoView.setLogText(null, param, event);
        }
        if (event == TXLiveConstants.PLAY_ERR_NET_DISCONNECT) {

            showErrorAndQuit(TCConstants.ERROR_MSG_NET_DISCONNECTED);

        } else if (event == TXLiveConstants.PLAY_EVT_PLAY_END) {
            stopPlay(false);
            startPlay();
        }
//        else if (event == TXLiveConstants.PLAY_EVT_CHANGE_RESOLUTION) {
//            int width = param.getInt(TXLiveConstants.EVT_PARAM1, 0);
//            int height = param.getInt(TXLiveConstants.EVT_PARAM2, 0);
//            int showWidth = width;
//            int showHeight = mTXCloudVideoView.getHeight();
//            if (0 != width && 0 != height) {
//                showWidth = width * showHeight / height;
//            }
//            if (0 != showWidth && 0 != showHeight) {
//                LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) mTXCloudVideoView.getLayoutParams();
//                if (null == layoutParams) {
//                    layoutParams = new LinearLayout.LayoutParams(showWidth, showHeight);
//                } else {
//                    layoutParams.width = showWidth;
//                    layoutParams.height = showHeight;
//                }
//                mTXCloudVideoView.setLayoutParams(layoutParams);
//            }
//
//        }
    }

    @Override
    public void onNetStatus(Bundle bundle) {
//        if(bundle.getInt(TXLiveConstants.NET_STATUS_VIDEO_WIDTH) > bundle.getInt(TXLiveConstants.NET_STATUS_VIDEO_HEIGHT)) {
//            if(mTXLivePlayer != null) mTXLivePlayer.setRenderRotation(TXLiveConstants.RENDER_ROTATION_LANDSCAPE);
//        }
//        else if(mTXLivePlayer != null) mTXLivePlayer.setRenderRotation(TXLiveConstants.RENDER_ROTATION_PORTRAIT);
    }

    @Override
    public void initView() {
        mErrDlgFragment = new ErrorDialogFragment();

        mVideoPath = getIntent().getStringExtra(TCConstants.VIDEO_RECORD_VIDEPATH);
        mCoverPath = getIntent().getStringExtra(TCConstants.VIDEO_RECORD_COVERPATH);
        mRotation = getIntent().getIntExtra(TCConstants.VIDEO_RECORD_ROTATION, TXLiveConstants.RENDER_ROTATION_PORTRAIT);
        mDisableCache = getIntent().getBooleanExtra(TCConstants.VIDEO_RECORD_NO_CACHE, false);
        mLocalVideoPath = getIntent().getStringExtra(TCConstants.VIDEO_RECORD_VIDEPATH);

        mIsPlayRecordType = getIntent().getIntExtra(TCConstants.VIDEO_RECORD_TYPE, 0) == TCConstants.VIDEO_RECORD_TYPE_PLAY;

//        CheckBox cbShareWX = (CheckBox) findViewById(R.id.vpcb_share_wx);
//        CheckBox cbShareCircle = (CheckBox) findViewById(R.id.vpcb_share_circle);
//        CheckBox cbShareQQ = (CheckBox) findViewById(R.id.vpcb_share_qq);
//        CheckBox cbShareQzone = (CheckBox) findViewById(R.id.vpcb_share_qzone);
//        CheckBox cbShareWb = (CheckBox) findViewById(R.id.vpcb_share_wb);

//        cbShareWX.setOnCheckedChangeListener(this);
//        cbShareCircle.setOnCheckedChangeListener(this);
//        cbShareQQ.setOnCheckedChangeListener(this);
//        cbShareQzone.setOnCheckedChangeListener(this);
//        cbShareWb.setOnCheckedChangeListener(this);

        mBtnBack = (TextView) findViewById(R.id.btn_back);
        mBtnBack.setOnClickListener(this);

        mBtnPublish = (TextView) findViewById(R.id.btn_publish);
        mBtnPublish.setOnClickListener(this);

        mLayoutEdit = (LinearLayout) findViewById(R.id.layout_edit);
        mShareType = (FixGridLayout) findViewById(R.id.ll_video_share);
        mLayoutPublish = (RelativeLayout) findViewById(R.id.layout_publish);

        mIVPublishing = (ImageView) findViewById(R.id.publishing);
        mTVPublish = (TextView) findViewById(R.id.publish_text);

        mTVTitle = (TextView) findViewById(R.id.publish_title);

        mTitleEditText = (EditText) findViewById(R.id.edit_text);

        mTXLivePlayer = new TXLivePlayer(this);
        mTXPlayConfig = new TXLivePlayConfig();
        mTXCloudVideoView = (TXCloudVideoView) findViewById(R.id.video_view);
        mTXCloudVideoView.disableLog(true);

        Handler h = new Handler();
        h.postDelayed(new Runnable() {
            @Override
            public void run() {
                startPlay();
            }
        }, 500);

        addShareView();
//        startPlay();

        mCity= (TextView) findViewById(R.id.city);
        mCity.setText(AppContext.address);
    }

    @Override
    public void initData() {

    }

    public class NetchangeReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (ConnectivityManager.CONNECTIVITY_ACTION.equals(intent.getAction())) {
                if (!TCUtils.isNetworkAvailable(TCVideoPublisherActivity.this)) {
                    mIVPublishing.setVisibility(View.INVISIBLE);
                    mTVPublish.setText("网络连接断开，视频上传失败");
                }
            }
        }
    }

    @Override
    protected boolean hasActionBar() {
        return false;
    }

    protected void showErrorAndQuit(String errorMsg) {

        if (!mErrDlgFragment.isAdded() && !this.isFinishing()) {
            Bundle args = new Bundle();
            args.putString("errorMsg", errorMsg);
            mErrDlgFragment.setArguments(args);
            mErrDlgFragment.setCancelable(false);

            //此处不使用用.show(...)的方式加载dialogfragment，避免IllegalStateException
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.add(mErrDlgFragment, "loading");
            transaction.commitAllowingStateLoss();
        }
    }
}

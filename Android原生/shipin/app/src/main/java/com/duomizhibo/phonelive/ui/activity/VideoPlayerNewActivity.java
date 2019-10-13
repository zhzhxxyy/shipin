package com.duomizhibo.phonelive.ui.activity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.duomizhibo.phonelive.AppConfig;
import com.duomizhibo.phonelive.AppContext;
import com.duomizhibo.phonelive.R;
import com.duomizhibo.phonelive.adapter.video.VideoCommentAdapter;
import com.duomizhibo.phonelive.adapter.video.VideoTuijianAdapter;
import com.duomizhibo.phonelive.api.remote.ApiUtils;
import com.duomizhibo.phonelive.api.remote.PhoneLiveApi;
import com.duomizhibo.phonelive.bean.video.BaseData;
import com.duomizhibo.phonelive.bean.video.GiftObject;
import com.duomizhibo.phonelive.bean.video.GiftObjectList;
import com.duomizhibo.phonelive.bean.video.MemberInfo;
import com.duomizhibo.phonelive.bean.video.VideoClass;
import com.duomizhibo.phonelive.bean.video.VideoComment;
import com.duomizhibo.phonelive.bean.video.VideoCommentList;
import com.duomizhibo.phonelive.bean.video.VideoDetail;
import com.duomizhibo.phonelive.bean.video.VideoObject;
import com.duomizhibo.phonelive.bean.video.VideoPay;
import com.duomizhibo.phonelive.ui.TCVideoPublisherActivity;
import com.duomizhibo.phonelive.ui.TCVideoRecordActivity;
import com.duomizhibo.phonelive.utils.StringUtils;
import com.duomizhibo.phonelive.utils.TLog;
import com.duomizhibo.phonelive.utils.UIHelper;
import com.duomizhibo.phonelive.widget.LoadUrlImageView;
import com.duomizhibo.phonelive.widget.alert.BaseDialog;
import com.duomizhibo.phonelive.widget.alert.TipDialog;
import com.tencent.rtmp.ITXLivePlayListener;
import com.tencent.rtmp.TXLiveConstants;
import com.tencent.rtmp.TXLivePlayConfig;
import com.tencent.rtmp.TXLivePlayer;
import com.tencent.rtmp.ui.TXCloudVideoView;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import okhttp3.Call;

/*

* 直播播放页面
*
*
* */
public class VideoPlayerNewActivity extends AppCompatActivity implements  ITXLivePlayListener,View.OnClickListener,AudioManager.OnAudioFocusChangeListener {

    public final static String USER_INFO = "USER_INFO";

    /*------------播放相关控件-----------*/
    private View rl_play;//播放布局
    private TXCloudVideoView  mTXCloudVideoView;//视频播放
    private LoadUrlImageView iv_play_cover;//视频封面
    private ImageView iv_play_start;//点击开始播放
    private ProgressBar pb_loading;//加载中
    private TextView tv_play_fail;//播放出错

    private View ll_control_bottom;//底部控制器
    private ImageView iv_control_start;//点击开始播放 或暂停播放
    private SeekBar sb_control_progress;//播放进度
    private TextView tv_control_time;//播放进度时间显示
    private TextView tv_quanping;//全屏

    private View ll_top;//头部页面
    private View iv_top_back;//点击返回
    private TextView tv_top_title;//标题

    private TXLivePlayer mTXLivePlayer;//世纪播放器
    private TXLivePlayConfig mTXPlayConfig = new TXLivePlayConfig();
    protected boolean mIsLivePlay = false;//是否是直播
    private int mPlayType = TXLivePlayer.PLAY_TYPE_LIVE_RTMP;
    private String mPlayUrl = "http://f3.chahz.com/201908/02/ck4Vz2MV/index.m3u8";

    private long mTrackingTouchTS = 0;
    private boolean mStartSeek = false;
    private boolean mVideoPlay = false;
    private boolean mVideoPause = false;
    private boolean mAutoPause = false;

    private int heightView;//默认高度



    /*--------播放外控件---------*/

    private TextView tv_title;
    private TextView tv_bofang;
    private TextView tv_time;
    private TextView tv_down;

    //管理信息布局
    private View rl_video_info;
    private ImageView iv_user_head;
    private TextView tv_user_name;
    private TextView tv_fenlei_1;

    private TextView tv_biaoqian_1;
    private TextView tv_biaoqian_2;
    private TextView tv_biaoqian_3;

    //点赞 收藏 分享
    private View fi_zan;
    private TextView tv_zan;
    private View fi_shoucang;
    private TextView tv_shoucang;
    private View fi_fenxiang;
    private TextView tv_fenxiang;

    //打赏
    private View rl_dashang;
    private ImageView iv_dashang;
    private TextView tv_dashang_money;
    private TextView tv_dashang_name;
    
    //评论 推荐  剧集  按钮

    private int tab_num=0;//0 评论 1 推荐 2 剧集
    private TextView tv_comment;
    private TextView tv_reco;
    private TextView tv_set;
    private View view_move;

    //评论
    private View ll_comment_all;
    private ImageView iv_comment_head;
    private EditText et_comment_content;
    private TextView tv_comment_num;
    private View tv_comment_send;
    private View tv_comment_cancel;
    private ListView listView;
    private TextView tv_no_data;

    private int resourceId;//资源的id
    private VideoDetail videoDetail=null;
    private VideoPay videoPay=null;
    private VideoCommentAdapter videoCommentAdapter=null;//评论的适配器
    private List<VideoComment> commentList=new ArrayList<VideoComment>();
    private VideoTuijianAdapter videoTuijianAdapter=null;//推荐  剧集的 适配器

    private long clickTime=0;//点击时间
    private int minDuringClickTime=500;//最短的点击时间 毫秒
    private List<GiftObject> giftList=new ArrayList<GiftObject>();//礼物 打赏

    private int freeNum;//试看秒数 如果等于-1 则不需要试看

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.new_activity_video_player);
        initView();
        heightView= rl_play.getLayoutParams().height;
        Bundle bundle = getIntent().getBundleExtra(USER_INFO);
        VideoObject videoObject = (VideoObject) bundle.getSerializable(USER_INFO);
        iv_play_cover.setImageLoadUrl(videoObject.getThumbnail());
        tv_top_title.setText(videoObject.getTitle()+"_视频观看");
        resourceId=videoObject.getId();
        videoDetail=null;
        videoPay=null;
        chushiBofangView();
        initData();
    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        //todo 初始化一些其他页面
        Bundle bundle = intent.getBundleExtra(USER_INFO);
        VideoObject videoObject = (VideoObject) bundle.getSerializable(USER_INFO);
        iv_play_cover.setImageLoadUrl(videoObject.getThumbnail());
        tv_top_title.setText(videoObject.getTitle()+"_视频观看");
        tv_title.setText(videoObject.getTitle()+"");
        resourceId=videoObject.getId();
        videoDetail=null;
        videoPay=null;
        commentList.clear();
        giftList.clear();
        chushiBofangView();
        initData();
    }


    private void initView() {
        rl_play=findViewById(R.id.rl_play);
        mTXCloudVideoView=(TXCloudVideoView)findViewById(R.id.tcv_play_view);
        iv_play_cover=(LoadUrlImageView)findViewById(R.id.iv_play_cover);
        iv_play_start=(ImageView)findViewById(R.id.iv_play_start);
        pb_loading=(ProgressBar)findViewById(R.id.pb_loading);
        tv_play_fail=(TextView) findViewById(R.id.tv_play_fail);
        ll_control_bottom=findViewById(R.id.ll_control_bottom);
        iv_control_start=(ImageView) findViewById(R.id.iv_control_start);
        sb_control_progress=(SeekBar)findViewById(R.id.sb_control_progress);
        tv_control_time=(TextView)findViewById(R.id.tv_control_time);
        tv_quanping=(TextView) findViewById(R.id.tv_quanping);
        ll_top=findViewById(R.id.ll_top);
        iv_top_back=findViewById(R.id.iv_top_back);
        tv_top_title=(TextView) findViewById(R.id.tv_top_title);

        //--------------------
        tv_title=(TextView) findViewById(R.id.tv_title);
        tv_bofang=(TextView) findViewById(R.id.tv_bofang);
        tv_time=(TextView) findViewById(R.id.tv_time);
        tv_down=(TextView) findViewById(R.id.tv_down);
        rl_video_info= findViewById(R.id.rl_video_info);
        iv_user_head=(ImageView) findViewById(R.id.iv_user_head);
        tv_user_name=(TextView) findViewById(R.id.tv_user_name);
        tv_fenlei_1=(TextView) findViewById(R.id.tv_fenlei_1);
        tv_biaoqian_1=(TextView) findViewById(R.id.tv_biaoqian_1);
        tv_biaoqian_2=(TextView) findViewById(R.id.tv_biaoqian_2);
        tv_biaoqian_3=(TextView) findViewById(R.id.tv_biaoqian_3);
        fi_zan=findViewById(R.id.fi_zan);
        tv_zan=(TextView) findViewById(R.id.tv_zan);

        fi_shoucang=findViewById(R.id.fi_shoucang);
        tv_shoucang=(TextView) findViewById(R.id.tv_shoucang);
        fi_fenxiang=findViewById(R.id.fi_fenxiang);
        tv_fenxiang=(TextView) findViewById(R.id.tv_fenxiang);
        rl_dashang= findViewById(R.id.rl_dashang);
        iv_dashang=(ImageView) findViewById(R.id.iv_dashang);
        tv_dashang_money=(TextView) findViewById(R.id.tv_dashang_money);
        tv_dashang_name=(TextView) findViewById(R.id.tv_dashang_name);

        tv_comment=(TextView) findViewById(R.id.tv_comment);
        tv_reco=(TextView) findViewById(R.id.tv_reco);
        tv_set=(TextView) findViewById(R.id.tv_set);
        view_move=findViewById(R.id.view_move);

        ll_comment_all=findViewById(R.id.ll_comment_all);
        iv_comment_head=(ImageView) findViewById(R.id.iv_comment_head);
        et_comment_content=(EditText) findViewById(R.id.et_comment_content);
        tv_comment_num=(TextView) findViewById(R.id.tv_comment_num);
        tv_comment_send=findViewById(R.id.tv_comment_send);
        tv_comment_cancel=findViewById(R.id.tv_comment_cancel);
        listView=(ListView) findViewById(R.id.listView);
        tv_no_data=(TextView) findViewById(R.id.tv_no_data);

        iv_play_start.setOnClickListener(this);
        tv_play_fail.setOnClickListener(this);
        iv_control_start.setOnClickListener(this);
        tv_quanping.setOnClickListener(this);
        iv_top_back.setOnClickListener(this);
        sb_control_progress.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean bFromUser) {
                if (tv_control_time != null) {
                    tv_control_time.setText(String.format(Locale.CHINA, "%02d:%02d/%02d:%02d", (progress)/60, (progress) % 60, (seekBar.getMax()) / 60, (seekBar.getMax()) % 60));
                }
                if(freeNum!=-1&&progress>freeNum){
                    //试看结束
                    chushiBofangView();
                    showMsgDialog("试看时间结束,是否付费观看,需要"+videoDetail.getGold()+"元",new BaseDialog.ParameCallBack(){
                        @Override
                        public void onCall(Object object) {
                            if (object instanceof Boolean) {
                                if ((Boolean) object) {
                                    //
                                    payVideo(1,false);
                                } else {
                                    //
                                }
                            }
                        }
                    });
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                mStartSeek = true;
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if ( mTXLivePlayer != null) {
                    mTXLivePlayer.seek(seekBar.getProgress());
                }
                mTrackingTouchTS = System.currentTimeMillis();
                mStartSeek = false;
            }
        });

        tv_down.setOnClickListener(this);
        fi_zan.setOnClickListener(this);
        tv_zan.setOnClickListener(this);

        fi_shoucang.setOnClickListener(this);
        tv_shoucang.setOnClickListener(this);
        tv_zan.setOnClickListener(this);
        tv_zan.setOnClickListener(this);
        fi_fenxiang.setOnClickListener(this);
        tv_fenxiang.setOnClickListener(this);
        iv_dashang.setOnClickListener(this);


        tv_comment.setOnClickListener(this);
        tv_reco.setOnClickListener(this);
        tv_set.setOnClickListener(this);

        tv_comment_send.setOnClickListener(this);
        tv_comment_cancel.setOnClickListener(this);

        et_comment_content.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String content=  s.toString();
                tv_comment_num.setText(content.length()+"/300");
            }
        });

        tv_fenlei_1.setOnClickListener(this);
        tv_biaoqian_1.setOnClickListener(this);
        tv_biaoqian_2.setOnClickListener(this);
        tv_biaoqian_3.setOnClickListener(this);

        rl_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(iv_play_cover.getVisibility()==View.GONE){
                    if(ll_top.getVisibility()!=View.GONE){
                        ll_top.setVisibility(View.GONE);
                        ll_control_bottom.setVisibility(View.GONE);
                    }else{
                        ll_top.setVisibility(View.VISIBLE);
                        ll_control_bottom.setVisibility(View.VISIBLE);

                    }
                }
            }
        });
        tipDialog=new TipDialog(this);
    }


    //初始播放view
    private void  chushiBofangView(){
        stopPlay(true);
        ll_top.setVisibility(View.VISIBLE);
        tv_quanping.setText("全屏");
        ll_control_bottom.setVisibility(View.GONE);
        iv_play_cover.setVisibility(View.VISIBLE);
        iv_play_start.setVisibility(View.VISIBLE);
        pb_loading.setVisibility(View.GONE);
        tv_play_fail.setVisibility(View.GONE);
        RelativeLayout.LayoutParams param= (RelativeLayout.LayoutParams)rl_play.getLayoutParams();
        if(param.height==-1){
            param.height=heightView;
            param.width=RelativeLayout.LayoutParams.MATCH_PARENT;
            rl_play.setLayoutParams(param);

            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            if (mTXLivePlayer != null) {
//                mTXLivePlayer.setRenderRotation(TXLiveConstants.RENDER_ROTATION_PORTRAIT);
            }
        }

    }

    private void  initData(){
        this.setVolumeControlStream(AudioManager.STREAM_MUSIC);
        dataToView();
        getVideoInfoById();
        getCommentList();
        getGiftList();
        payVideo(0,false);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_play_start:

                clickPreStart(false);
                break;
            case R.id.tv_play_fail:
                startPlay();
                break;
            case R.id.iv_control_start:
                if (mVideoPlay) {
                    if (mVideoPause) {
                        mTXLivePlayer.resume();
                        iv_control_start.setBackgroundResource(R.drawable.icon_record_pause);
                        mVideoPause = false;
                    } else {
                        mTXLivePlayer.pause();
                        iv_control_start.setBackgroundResource(R.drawable.icon_record_start);
                        mVideoPause = true;
                    }
                } else {
                    startPlay();
                }
                break;
            case R.id.tv_quanping:
                clickQuanpin();
                break;
            case R.id.iv_top_back:
                clickBack();
                break;

            case R.id.tv_down:
                if(rl_video_info.getVisibility()!=View.GONE){
                    tv_down.setRotation(180);//默认隐藏用户信息
                    rl_video_info.setVisibility(View.GONE);
                }else{
                    tv_down.setRotation(0);//默认隐藏用户信息
                    rl_video_info.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.fi_zan:
            case R.id.tv_zan:
                sendGood();
                break;

            case R.id.fi_shoucang:
            case R.id.tv_shoucang:
                sendCollect();
                break;

            case R.id.fi_fenxiang:
            case R.id.tv_fenxiang:
                sendFenxiang();
                break;

            case R.id.iv_dashang:
                sendDashangPre();
                break;


            case R.id.tv_comment:
                if(tab_num!=0){
                    tab_num=0;
                    clickTabView();
                }
                break;
            case R.id.tv_reco:
                if(tab_num!=1){
                    tab_num=1;
                    clickTabView();
                }
                break;
            case R.id.tv_set:
                if(tab_num!=2){
                    tab_num=2;
                    clickTabView();
                }
                break;


            case R.id.tv_comment_send:
                sendComment();
                break;
            case R.id.tv_comment_cancel:
                et_comment_content.setText("");
                break;
            case R.id.tv_fenlei_1:
              //  AppContext.showToastShort("分类!");
                break;
            case R.id.tv_biaoqian_1:
              //  AppContext.showToastShort("标签1!");
                break;
            case R.id.tv_biaoqian_2:
               // AppContext.showToastShort("标签2!");
                break;
            case R.id.tv_biaoqian_3:
               // AppContext.showToastShort("标签3!");
                break;
        }
    }


    @Override
    public void onPlayEvent(int i, Bundle bundle) {
        if (i == TXLiveConstants.PLAY_EVT_PLAY_BEGIN) {
            pb_loading.setVisibility(View.GONE);
            tv_play_fail.setVisibility(View.GONE);
        }else if (i == TXLiveConstants.PLAY_EVT_PLAY_PROGRESS) {
            if (mStartSeek) {
                return;
            }
            int progress = bundle.getInt(TXLiveConstants.EVT_PLAY_PROGRESS);
            int duration = bundle.getInt(TXLiveConstants.EVT_PLAY_DURATION);
            long curTS = System.currentTimeMillis();
            // 避免滑动进度条松开的瞬间可能出现滑动条瞬间跳到上一个位置
            if (Math.abs(curTS - mTrackingTouchTS) < 500) {
                return;
            }
            mTrackingTouchTS = curTS;

            if (sb_control_progress != null) {
                if(sb_control_progress.getMax()!=duration){
                    sb_control_progress.setMax(duration);
                }
                sb_control_progress.setProgress(progress);
            }
        } else if (i == TXLiveConstants.PLAY_ERR_NET_DISCONNECT) {

            tv_play_fail.setVisibility(View.VISIBLE);

        } else if (i == TXLiveConstants.PLAY_EVT_PLAY_END) {
            if (sb_control_progress != null) {
                sb_control_progress.setProgress(0);
            }
            stopPlay(false);
        }else if(i == TXLiveConstants.PLAY_EVT_PLAY_LOADING){
            TLog.log("------PLAY_EVT_PLAY_LOADING----->>",""+i);
            pb_loading.setVisibility(View.VISIBLE);

        }else{
            TLog.log("------其他----->>",""+i);
        }

    }


    @Override
    public void onNetStatus(Bundle status) {
//        if (status.getInt(TXLiveConstants.NET_STATUS_VIDEO_WIDTH) > status.getInt(TXLiveConstants.NET_STATUS_VIDEO_HEIGHT)) {
//            if (mTXLivePlayer != null) {
//                mTXLivePlayer.setRenderRotation(TXLiveConstants.RENDER_ROTATION_LANDSCAPE);
//            }
//        } else if (mTXLivePlayer != null) {
//            mTXLivePlayer.setRenderRotation(TXLiveConstants.RENDER_ROTATION_PORTRAIT);
//        }

    }


    @Override
    protected void onResume() {
        super.onResume();
        mTXCloudVideoView.onResume();
        if (mVideoPlay && mAutoPause&&mTXLivePlayer!=null) {
            mTXLivePlayer.resume();
            mAutoPause = false;
        }
//        if(videoPay!=null&&videoPay.getPlayState()==4&&AppContext.getInstance().isLogin()){
//            videoPay=null;
//            payVideo(0,true);
//        }
        if(videoPay!=null&&!videoPay.isSurePlay()){
            payVideo(0,false);
        }
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        requestAudioFocus();
    }


    @Override
    protected void onPause() {
        super.onPause();
        mTXCloudVideoView.onPause();
        if (mVideoPlay && !mVideoPause &&mTXLivePlayer!=null) {
            mTXLivePlayer.pause();
            mAutoPause = true;
        }
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        abandonAudioFocus();
    }


    @Override
    public void onBackPressed() {
        clickBack();
    }

    @Override
    protected void onDestroy() {//释放
        super.onDestroy();
        //解除广播
        OkHttpUtils.getInstance().cancelTag("initRoomInfo");
        stopPlay(true);
        mTXLivePlayer = null;
        if (mTXCloudVideoView != null) {
            mTXCloudVideoView = null;
        }
    }



    /*-----------播放相关处理----------------*/

    //点击开始播放
    protected void startPlay() {
        if (!checkPlayUrl()) {
            return;
        }
        requestAudioFocus();
        if (mTXLivePlayer == null) {
            mTXLivePlayer = new TXLivePlayer(this);
        }
        mTXLivePlayer.setPlayerView(mTXCloudVideoView);
//        mTXLivePlayer.setRenderRotation(TXLiveConstants.RENDER_ROTATION_PORTRAIT);
//        mTXLivePlayer.setRenderMode(TXLiveConstants.RENDER_MODE_FULL_FILL_SCREEN);

//        RelativeLayout.LayoutParams param= (RelativeLayout.LayoutParams)rl_play.getLayoutParams();
//        if(param.height==-1){
//            mTXLivePlayer.setRenderRotation(TXLiveConstants.RENDER_ROTATION_LANDSCAPE);
//        }else{
//            mTXLivePlayer.setRenderRotation(TXLiveConstants.RENDER_ROTATION_PORTRAIT);
//        }

        mTXLivePlayer.setPlayListener(this);
        mTXLivePlayer.setConfig(mTXPlayConfig);
        int result;
        result = mTXLivePlayer.startPlay(mPlayUrl, mPlayType);
        if (0 != result) {
//            Intent rstData = new Intent();
//            if (-1 == result) {
//                rstData.putExtra(TCConstants.ACTIVITY_RESULT, TCConstants.ERROR_MSG_NOT_QCLOUD_LINK);
//            } else {
//                rstData.putExtra(TCConstants.ACTIVITY_RESULT, TCConstants.ERROR_MSG_NOT_QCLOUD_LINK);
//            }
            AppContext.showToastShort("错误播放");
            mTXCloudVideoView.onPause();
            stopPlay(true);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                   finish();
                }
            }, 1000);
        } else {
            iv_play_cover.setVisibility(View.GONE);
            iv_play_start.setVisibility(View.GONE);
            ll_top.setVisibility(View.VISIBLE);
            ll_control_bottom.setVisibility(View.VISIBLE);
            iv_control_start.setBackgroundResource(R.drawable.icon_record_pause);
            pb_loading.setVisibility(View.VISIBLE);
            mVideoPlay = true;
        }
    }


    //停止播放
    private void stopPlay(boolean clearLastFrame) {
        if (mTXLivePlayer != null) {
            mTXLivePlayer.setPlayListener(null);
            mTXLivePlayer.stopPlay(clearLastFrame);
        }
        mVideoPlay = false;
    }


    //检查播放地址
    private boolean checkPlayUrl() {
        if (TextUtils.isEmpty(mPlayUrl) || (!mPlayUrl.startsWith("http://") && !mPlayUrl.startsWith("https://") && !mPlayUrl.startsWith("rtmp://"))) {
            AppContext.showToastShort("播放地址不合法，目前仅支持rtmp,flv,hls,mp4播放方式!");
            return false;
        }
        if (mIsLivePlay) {
            if (mPlayUrl.startsWith("rtmp://")) {
                mPlayType = TXLivePlayer.PLAY_TYPE_LIVE_RTMP;
            } else if ((mPlayUrl.startsWith("http://"))) {
                mPlayType = TXLivePlayer.PLAY_TYPE_VOD_MP4;
            } else {
                AppContext.showToastShort("播放地址不合法，直播目前仅支持rtmp,flv播放方式!");
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
                    AppContext.showToastShort("播放地址不合法，点播目前仅支持flv,hls,mp4播放方式!");
                    return false;
                }
            } else {
                AppContext.showToastShort("播放地址不合法，点播目前仅支持flv,hls,mp4播放方式!");
                return false;
            }
        }
        return true;
    }

    //全屏点击
    private void clickQuanpin(){
        RelativeLayout.LayoutParams param= (RelativeLayout.LayoutParams)rl_play.getLayoutParams();
        if(param.height!=-1){
            tv_quanping.setText("缩放");
            param.height=RelativeLayout.LayoutParams.MATCH_PARENT;
            param.width=RelativeLayout.LayoutParams.MATCH_PARENT;
            rl_play.setLayoutParams(param);
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            if (mTXLivePlayer != null) {
//                mTXLivePlayer.setRenderRotation(TXLiveConstants.RENDER_ROTATION_LANDSCAPE);
            }
        }else{
            tv_quanping.setText("全屏");
            param.height=heightView;
            param.width=RelativeLayout.LayoutParams.MATCH_PARENT;
            rl_play.setLayoutParams(param);

            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            if (mTXLivePlayer != null) {
//                mTXLivePlayer.setRenderRotation(TXLiveConstants.RENDER_ROTATION_PORTRAIT);
            }
        }

    }

    //点击返回或是按返回键
    private void clickBack(){
        RelativeLayout.LayoutParams param= (RelativeLayout.LayoutParams)rl_play.getLayoutParams();
        if(param.height==-1){
            //全屏状态 退出全屏
            param.height=heightView;
            param.width=RelativeLayout.LayoutParams.MATCH_PARENT;
            rl_play.setLayoutParams(param);
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            if (mTXLivePlayer != null) {
//                mTXLivePlayer.setRenderRotation(TXLiveConstants.RENDER_ROTATION_PORTRAIT);
            }
        }else{
            finish();
        }
    }


    /**
     * 请求获取AudioFocus
     */
    private void requestAudioFocus() {

        AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        audioManager.requestAudioFocus(this, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);
    }

    /**
     * 释放AudioFocus
     */
    private void abandonAudioFocus() {

        AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        audioManager.abandonAudioFocus(this);
    }


    //音频焦点获取与释放
    @Override
    public void onAudioFocusChange(int focusChange) {
        switch (focusChange) {
            case AudioManager.AUDIOFOCUS_GAIN:

                break;
            case AudioManager.AUDIOFOCUS_LOSS:
                //长时间失去Focus
                if (mVideoPlay&&mTXLivePlayer!=null) {
                    mTXLivePlayer.pause();
                }
                break;
            case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:
                //短时间失去Focus
                if (mVideoPlay&&mTXLivePlayer!=null) {
                    mTXLivePlayer.pause();
                }
                break;
            case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK:
                break;
        }
    }

    /*--------------除播放外的信息处理-------------*/
    //数据绑定页面
    private void dataToView(){
        if(videoDetail==null){
            //未获取到数据时候 初始化
            tv_bofang.setText("0");
            tv_time.setText("");
            if(rl_video_info.getVisibility()!=View.GONE){
                tv_down.setRotation(0);//默认隐藏用户信息
                rl_video_info.setVisibility(View.VISIBLE);
            }else{
                tv_down.setRotation(180);//默认隐藏用户信息
                rl_video_info.setVisibility(View.GONE);
            }
            iv_user_head.setImageResource(R.drawable.user_dafault_headimg);
            tv_user_name.setText("");
            tv_fenlei_1.setVisibility(View.GONE);
            tv_biaoqian_1.setVisibility(View.GONE);
            tv_biaoqian_2.setVisibility(View.GONE);
            tv_biaoqian_3.setVisibility(View.GONE);
            tv_zan.setText("点赞(0)");
            tv_shoucang.setText("未收藏");

            tv_set.setVisibility(View.GONE);
        }else{

            tv_bofang.setText(videoDetail.getClick()+"");
            tv_time.setText(videoDetail.getAddTimeString());
            Glide.with(VideoPlayerNewActivity.this).load(videoDetail.getHeadimgurl()).placeholder(R.drawable.user_dafault_headimg).into(iv_user_head);
            tv_user_name.setText(videoDetail.getMember()+"");
            if(videoDetail.isGooded()){
                tv_zan.setText("已点赞("+videoDetail.getGood()+")");
            }else{
                tv_zan.setText("点赞("+videoDetail.getGood()+")");
            }


            if(videoDetail.isCollected()){
                tv_shoucang.setText("已收藏");
            }else{
                tv_shoucang.setText("未收藏");
            }
            if(!StringUtils.isEmpty(videoDetail.getClassname())&&videoDetail.getClassid()>0){
                tv_fenlei_1.setText(videoDetail.getClassname()+"");
                tv_fenlei_1.setVisibility(View.VISIBLE);
            }else{
                tv_fenlei_1.setVisibility(View.GONE);
            }

            tv_biaoqian_1.setVisibility(View.GONE);
            tv_biaoqian_2.setVisibility(View.GONE);
            tv_biaoqian_3.setVisibility(View.GONE);
            List<VideoClass> tagList= videoDetail.getTaglist();
            if(tagList!=null&&tagList.size()>0){
                for(int i=0;i<tagList.size();i++){
                    if(i==0){
                        tv_biaoqian_1.setText(tagList.get(i).getName()+"");
                        tv_biaoqian_1.setVisibility(View.VISIBLE);
                    }else if(i==1){
                        tv_biaoqian_2.setText(tagList.get(i).getName()+"");
                        tv_biaoqian_2.setVisibility(View.VISIBLE);
                    }else if(i==2){
                        tv_biaoqian_3.setText(tagList.get(i).getName()+"");
                        tv_biaoqian_3.setVisibility(View.VISIBLE);
                    }
                }
            }
            List<VideoObject> videoSet= videoDetail.getVideoSet();
            tv_set.setVisibility(View.GONE);
            if(videoSet!=null&&videoSet.size()>0){
                tv_set.setVisibility(View.VISIBLE);
            }
        }

        if(giftList.size()>0){
            // TODO: 2019/8/5
            rl_dashang.setVisibility(View.VISIBLE);
            Glide.with(VideoPlayerNewActivity.this).load(giftList.get(0).getImages()).placeholder(null).into(iv_dashang);
            tv_dashang_money.setText(giftList.get(0).getPrice()+"金币");
            tv_dashang_name.setText(giftList.get(0).getName()+"");
        }else{
            rl_dashang.setVisibility(View.GONE);
        }



        clickTabView();
    }
    //tab页面切换
    private void clickTabView(){
        if(tab_num<0||tab_num>2){
            tab_num=0;
        }
        if(tab_num==2){
            tv_comment.setTextColor(getResources().getColor(R.color.colorTextG4));
            tv_reco.setTextColor(getResources().getColor(R.color.colorTextG4));
            tv_set.setTextColor(getResources().getColor(R.color.red));
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)  view_move.getLayoutParams();
            params.addRule(RelativeLayout.ALIGN_RIGHT,R.id.tv_set);
            view_move.setLayoutParams(params);
            ll_comment_all.setVisibility(View.GONE);

            if(videoDetail!=null&&videoDetail.getVideoSet()!=null&&videoDetail.getVideoSet().size()>0){
                if(videoTuijianAdapter==null){
                    videoTuijianAdapter=new VideoTuijianAdapter();
                }
                videoTuijianAdapter.clear();
                videoTuijianAdapter.addItem(videoDetail.getVideoSet());
                listView.setAdapter(videoTuijianAdapter);
                videoTuijianAdapter.notifyDataSetChanged();
                tv_no_data.setVisibility(View.GONE);
                listView.setVisibility(View.VISIBLE);
            }else{
                tv_no_data.setText("暂无剧集～");
                tv_no_data.setVisibility(View.VISIBLE);
                listView.setVisibility(View.GONE);
            }

        }else if(tab_num==1){
            tv_comment.setTextColor(getResources().getColor(R.color.colorTextG4));
            tv_reco.setTextColor(getResources().getColor(R.color.red));
            tv_set.setTextColor(getResources().getColor(R.color.colorTextG4));
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)  view_move.getLayoutParams();
            params.addRule(RelativeLayout.ALIGN_RIGHT,R.id.tv_reco);
            view_move.setLayoutParams(params);
            ll_comment_all.setVisibility(View.GONE);

            if(videoDetail!=null&&videoDetail.getRecom_list()!=null&&videoDetail.getRecom_list().size()>0){
                if(videoTuijianAdapter==null){
                    videoTuijianAdapter=new VideoTuijianAdapter();
                }
                videoTuijianAdapter.clear();
                videoTuijianAdapter.addItem(videoDetail.getRecom_list());
                listView.setAdapter(videoTuijianAdapter);
                videoTuijianAdapter.notifyDataSetChanged();
                tv_no_data.setVisibility(View.GONE);
                listView.setVisibility(View.VISIBLE);
            }else{
                tv_no_data.setText("暂无推荐～");
                tv_no_data.setVisibility(View.VISIBLE);
                listView.setVisibility(View.GONE);
            }

        }else{
            tv_comment.setTextColor(getResources().getColor(R.color.red));
            tv_reco.setTextColor(getResources().getColor(R.color.colorTextG4));
            tv_set.setTextColor(getResources().getColor(R.color.colorTextG4));
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)  view_move.getLayoutParams();
            params.addRule(RelativeLayout.ALIGN_RIGHT,R.id.tv_comment);
            view_move.setLayoutParams(params);
            ll_comment_all.setVisibility(View.VISIBLE);

            boolean isLoginUrl=false;
            if(AppContext.getInstance().isLogin()){
                MemberInfo memberInfo= AppContext.getInstance().getLoginUserNew();
                if(!StringUtils.isEmpty(memberInfo.getHeadimgurl())){
                    Glide.with(VideoPlayerNewActivity.this).load(memberInfo.getHeadimgurl()).placeholder(R.drawable.video_info_et_head).into(iv_comment_head);
                    isLoginUrl=true;
                }
            }
            if(!isLoginUrl){
                iv_comment_head.setImageResource(R.drawable.video_info_et_head);
            }
            tv_no_data.setText("暂无评论～");
            if(commentList.size()<=0){
                tv_no_data.setVisibility(View.VISIBLE);
                listView.setVisibility(View.GONE);
            }else{
                if(videoCommentAdapter==null){
                    videoCommentAdapter=new VideoCommentAdapter();
                }
                videoCommentAdapter.clear();
                videoCommentAdapter.addItem(commentList);
                tv_no_data.setVisibility(View.GONE);
                listView.setVisibility(View.VISIBLE);
                listView.setAdapter(videoCommentAdapter);
                videoCommentAdapter.notifyDataSetChanged();
            }
        }
    }
    //获取视频信息
    private void getVideoInfoById(){
        PhoneLiveApi.videoDetail(resourceId, AppContext.getInstance().getToken(), new StringCallback(){
            @Override
            public void onError(Call call, Exception e, int id) {
                AppContext.showToast("网络请求出错!");
            }
            @Override
            public void onResponse(String s, int id) {
                BaseData baseData= ApiUtils.getBaseData(s);
                if(baseData.isSuccess()){
                    VideoDetail videoDetailTemp=null;
                    try {
                        videoDetailTemp=com.alibaba.fastjson.JSONObject.parseObject(baseData.getData(), VideoDetail.class);
                    } catch (Exception e) {

                    }
                    if(videoDetailTemp!=null){
                        if(videoDetailTemp.getId()!=resourceId){
                            return;
                        }
                       videoDetail=videoDetailTemp;
                       dataToView();
                    }
                }
            }
        });
    }
    //todo 获取评论信息  暂时限制获取100条数据 不会再加载
    private void getCommentList(){
        PhoneLiveApi.getCommentList(1,100,resourceId,1, AppContext.getInstance().getToken(), new StringCallback(){
            @Override
            public void onError(Call call, Exception e, int id) {

                AppContext.showToast("网络请求出错!");
            }
            @Override
            public void onResponse(String s, int id) {
                BaseData baseData= ApiUtils.getBaseData(s);
                if(baseData.isSuccess()){
                    VideoCommentList videoCommentList=null;
                    try {
                        videoCommentList=com.alibaba.fastjson.JSONObject.parseObject(baseData.getData(), VideoCommentList.class);
                    } catch (Exception e) {

                    }
                    commentList.clear();
                    if(videoCommentList!=null&&videoCommentList.getRows()!=null&&videoCommentList.getRows().size()>0){
                          commentList.addAll(videoCommentList.getRows());
                    }
                    if(tab_num==0){
                        clickTabView();
                    }
                    //todo 获取评论成功后
                }
            }
        });
    }
    //todo 获取礼物打赏
    private void getGiftList(){
        PhoneLiveApi.getgift(resourceId,1, AppContext.getInstance().getToken(), new StringCallback(){
            @Override
            public void onError(Call call, Exception e, int id) {

            }
            @Override
            public void onResponse(String s, int id) {
                BaseData baseData= ApiUtils.getBaseData(s);
                if(baseData.isSuccess()){

                    GiftObjectList giftObjectList=null;
                    try {
                        giftObjectList=com.alibaba.fastjson.JSONObject.parseObject(baseData.getData(), GiftObjectList.class);
                    } catch (Exception e) {

                    }
                    if(giftObjectList!=null&&giftObjectList.getRows()!=null&&giftObjectList.getRows().size()>0){
                        giftList.clear();
                        giftList.addAll(giftObjectList.getRows());
                        clickTabView();
                    }
                }
            }
        });
    }
    //评论
    private void sendComment(){
        if(!AppContext.getInstance().isLogin()){
            showLogingDialog();
            return;
        }
        if(Math.abs(System.currentTimeMillis()-clickTime)<minDuringClickTime){
            return;
        }
        clickTime=System.currentTimeMillis();

        final String commentStr= et_comment_content.getText().toString();
        if(!(!StringUtils.isEmpty(commentStr)&&!StringUtils.isEmpty(commentStr.trim()))){
            AppContext.showToastShort("请输入评论内容!");
            return;
        }

        PhoneLiveApi.commentNew(resourceId,1,commentStr, AppContext.getInstance().getToken(), new StringCallback(){
            @Override
            public void onError(Call call, Exception e, int id) {
                AppContext.showToast("评论失败：网络请求出错!");
            }

            @Override
            public void onResponse(String s, int id) {
                BaseData baseData= ApiUtils.getBaseData(s);
                if(baseData.isSuccess()){
                    AppContext.showToast(baseData.getRespMsg());
                    String etString=et_comment_content.getText().toString();
                    if(commentStr.equals(etString)){
                        et_comment_content.setText("");
                    }
                    VideoComment videoComment=null;
                    try {
                        videoComment=com.alibaba.fastjson.JSONObject.parseObject(baseData.getData(), VideoComment.class);
                    } catch (Exception e) {

                    }

                    if(videoComment!=null){
                        //添加评论
                        commentList.add(0,videoComment);
                        clickTabView();
                    }
                }else{
                    AppContext.showToast(baseData.getRespMsg());
                }
            }
        });
    }
    //点暂
    private void sendGood(){
        if(!AppContext.getInstance().isLogin()){
            showLogingDialog();
            return;
        }
        if( videoDetail!=null&& videoDetail.isGooded()){
            AppContext.showToastShort("已点赞!");
            return;
        }
        if(Math.abs(System.currentTimeMillis()-clickTime)<minDuringClickTime){
            return;
        }
        clickTime=System.currentTimeMillis();


        PhoneLiveApi.giveGood(resourceId,1, AppContext.getInstance().getToken(), new StringCallback(){
            @Override
            public void onError(Call call, Exception e, int id) {
                AppContext.showToast("点赞失败：网络请求出错!");
            }

            @Override
            public void onResponse(String s, int id) {
                BaseData baseData= ApiUtils.getBaseData(s);
                if(baseData.isSuccess()){
                    AppContext.showToast(baseData.getRespMsg());
                    if(videoDetail!=null){
                        videoDetail.setGood(videoDetail.getGood()+1);
                        videoDetail.setGooded(true);
                        tv_zan.setText("已点赞("+videoDetail.getGood()+")");
                    }
                }else{
                    AppContext.showToast(baseData.getRespMsg());
                }
            }
        });


    }
    //收藏
    private void sendCollect(){
        if(!AppContext.getInstance().isLogin()){
            showLogingDialog();
            return;
        }
        if( videoDetail!=null&& videoDetail.isCollected()){
            AppContext.showToastShort("已收藏!");
            return;
        }
        if(Math.abs(System.currentTimeMillis()-clickTime)<minDuringClickTime){
            return;
        }
        clickTime=System.currentTimeMillis();

        PhoneLiveApi.colletionNew(resourceId,1, AppContext.getInstance().getToken(), new StringCallback(){
            @Override
            public void onError(Call call, Exception e, int id) {
                AppContext.showToast("收藏失败：网络请求出错!");
            }

            @Override
            public void onResponse(String s, int id) {

                BaseData baseData= ApiUtils.getBaseData(s);
                if(baseData.isSuccess()){
                    AppContext.showToast(baseData.getRespMsg());
                    if(videoDetail!=null){
                        videoDetail.setCollected(true);
                    }
                    tv_shoucang.setText("已收藏");
                }else{
                    AppContext.showToast(baseData.getRespMsg());
                }
            }
        });
    }
    //分享
    private void sendFenxiang(){
        if(Math.abs(System.currentTimeMillis()-clickTime)<minDuringClickTime){
            return;
        }
        clickTime=System.currentTimeMillis();
        String url=AppConfig.URL_COMMON+"/video/play/id/"+resourceId+".html?uid="+AppContext.getInstance().getLoginUid();
        ClipboardManager mClipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        Uri uri=Uri.parse(url);
        ClipData clipData = ClipData.newUri(getContentResolver(), "copy from demo", uri);
        mClipboardManager.setPrimaryClip(clipData);
        AppContext.showToast("复制成功，可以分享给朋友们了。");
    }
    //打赏
    private void sendDashangPre(){
        if(!AppContext.getInstance().isLogin()){
            showLogingDialog();
            return;
        }
        if(giftList.size()<=0){
             AppContext.showToast("暂无打赏对象");
            return;
        }
       final GiftObject giftObject= giftList.get(0);
        if(Math.abs(System.currentTimeMillis()-clickTime)<minDuringClickTime){
            return;
        }
        clickTime=System.currentTimeMillis();
        showMsgDialog("你需要花费" + giftObject.getPrice() + "金币打赏礼物吗？", "取消", "打赏", new BaseDialog.ParameCallBack() {
            @Override
            public void onCall(Object o) {
                if(o instanceof Boolean){
                    if((Boolean) o){
                        sendDashang(giftObject);
                    }
                }
            }
        });
    }
    private void sendDashang(GiftObject giftObject){
        PhoneLiveApi.reward(giftObject.getId(), resourceId, 1, giftObject.getPrice(), AppContext.getInstance().getToken(), new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                AppContext.showToast("打赏失败：网络错误");
            }

            @Override
            public void onResponse(String response, int id) {
                BaseData baseData= ApiUtils.getBaseData(response);
                if(baseData.isSuccess()){

                }else{
                    //AppContext.showToast(baseData.getRespMsg());
                }
                AppContext.showToast(baseData.getRespMsg());
            }
        });

    }

    //todo 如果是试试看的话需要停止
    //还未获取地址前点击播放
    private void clickPreStart(boolean isAuto){
        if(!isAuto){
            if(Math.abs(System.currentTimeMillis()-clickTime)<minDuringClickTime){
                return;
            }
            clickTime=System.currentTimeMillis();
        }
        if(videoPay==null){
            payVideo(0,true);
            return;
        }
        if(videoDetail==null){
            AppContext.showToastShort("正在获取信息，请稍等!");
            getVideoInfoById();
            return;
        }
        if(videoPay.isSurePlay()){
            if(videoPay.isTrySee()&&videoPay.getFreeType()==2){
                freeNum=videoPay.getFreeNum();
            }else{
                freeNum=-1;
            }
            mPlayUrl=videoPay.getUrl();
            startPlay();
        }else{
            if(videoPay.getPlayState()==1){
                payVideo(1,false);
            }else if(videoPay.getPlayState()==2){
                //弹窗是否付费观看
                if(videoPay.getFreeNum()>0){
                    tipDialog.setCancelText("付费观看");
                    tipDialog.setOkText("试看");
                    tipDialog.show("付费观看需要" + videoDetail.getGold() + "元或免费试看", new BaseDialog.ParameCallBack() {
                        @Override
                        public void onCall(Object object) {
                            if (object instanceof Boolean) {
                                if ((Boolean) object) {
                                    payVideo(1,true);
                                } else {
                                    payVideo(1,false);
                                }
                            }
                        }
                    });
                }else{
                    showMsgDialog("是否付费观看，需要"+videoDetail.getGold()+"元",new BaseDialog.ParameCallBack(){
                        @Override
                        public void onCall(Object object) {
                            if (object instanceof Boolean) {
                                if ((Boolean) object) {
                                    payVideo(1,false);
                                }
                            }
                        }
                    });
                }
            }else if(videoPay.getPlayState()==3){
                if(videoPay.getFreeNum()>0){
                    //弹窗是否试看
                    showMsgDialog("当前余额不足，是否免费试看？",new BaseDialog.ParameCallBack(){
                        @Override
                        public void onCall(Object object) {
                            if (object instanceof Boolean) {
                                if ((Boolean) object) {
                                    //
                                    payVideo(1,true);
                                } else {
                                    //
                                }
                            }
                        }
                    });
                }else{
                    //充值
                    showMsgDialog("当前余额不足，请先充值",new BaseDialog.ParameCallBack(){
                        @Override
                        public void onCall(Object object) {
                            if (object instanceof Boolean) {
                                if ((Boolean) object) {
                                    UIHelper.showRechargeActivity(VideoPlayerNewActivity.this);
                                } else {
                                    //
                                }
                            }
                        }
                    });
                }
            }else if(videoPay.getPlayState()==4){
                showLogingDialog();
            }else{
                AppContext.showToastShort("请联系客服人员");
            }
        }
    }

    //支付观看接口
    private void payVideo(int surePlay,boolean isTrySee){
        PhoneLiveApi.payVideo(resourceId,surePlay, isTrySee,AppContext.getInstance().getToken(), new StringCallback(){
            @Override
            public void onError(Call call, Exception e, int id) {
                AppContext.showToast("失败：网络请求出错!");
            }

            @Override
            public void onResponse(String s, int id) {
                BaseData baseData= ApiUtils.getBaseData(s);
                if(baseData.isSuccess()){
                    VideoPay videoPayTemp;
                    try {
                        videoPayTemp=com.alibaba.fastjson.JSONObject.parseObject(baseData.getData(), VideoPay.class);
                    } catch (Exception e) {
                        videoPayTemp=null;
                    }
                    if(videoPayTemp==null){
                        AppContext.showToast("解析失败，请稍后播放");
                        return;
                    }
                    if(videoPayTemp.getId()!=resourceId){

                        return;
                    }
                    videoPay=videoPayTemp;
                    clickPreStart(true);
                }else{
                    AppContext.showToast(baseData.getRespMsg());
                }
            }
        });
    }


    //登录弹窗
    private void showLogingDialog() {
        tipDialog.setCancelText("取消");
        tipDialog.setOkText("确认");
        tipDialog.setMessageText("是否登录");
        tipDialog.show(new BaseDialog.ParameCallBack() {
            @Override
            public void onCall(Object object) {
                if (object instanceof Boolean) {
                    if ((Boolean) object) {
                        UIHelper.showLogin(VideoPlayerNewActivity.this);
                    }
                }
            }
        });
    }

    //弹窗信息
    private void showMsgDialog(String msg,BaseDialog.ParameCallBack callBack) {
        tipDialog.setCancelText("取消");
        tipDialog.setOkText("确认");
        tipDialog.setMessageText(msg);
        tipDialog.show(callBack);
    }

    private void showMsgDialog(String msg,String ok,String cancel,BaseDialog.ParameCallBack callBack) {
        tipDialog.setCancelText(cancel);
        tipDialog.setOkText(ok);
        tipDialog.setMessageText(msg);
        tipDialog.show(callBack);
    }
    private TipDialog tipDialog;
}

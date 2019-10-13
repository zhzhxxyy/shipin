package com.duomizhibo.phonelive.videoeditor;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.tencent.rtmp.TXLog;
import com.tencent.rtmp.videoedit.TXVideoEditConstants;
import com.tencent.rtmp.videoedit.TXVideoEditer;
import com.tencent.rtmp.videoedit.TXVideoInfoReader;
import com.duomizhibo.phonelive.R;
import com.duomizhibo.phonelive.ui.TCVideoPublisherActivity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * UGC短视频裁剪
 */
public class TCVideoEditerActivity extends Activity implements View.OnClickListener, TCVideoEditView.IOnRangeChangeListener,
        TXVideoEditer.TXVideoGenerateListener, TXVideoInfoReader.OnSampleProgrocess, TXVideoEditer.TXVideoPreviewListener, EditPannel.IOnEditCmdListener {

    private static final String TAG = TCVideoEditerActivity.class.getSimpleName();
    private final int STATE_NONE = 0;
    private final int STATE_RESUME = 1;
    private final int STATE_PAUSE = 2;
    private final int STATE_CUT = 3;

    private final int OP_PLAY = 0;
    private final int OP_PAUSE = 1;
    private final int OP_SEEK = 2;
    private final int OP_CUT = 3;
    private final int OP_CANCEL = 4;
    private int mCurrentState = STATE_NONE;
    private Dialog mDialog;

    private TextView mTvDone;
    private TextView mTvCurrent;
    private TextView mTvDuration;
    private ImageView mBtnPlay;
    private FrameLayout mVideoView;
    private LinearLayout mLayoutEditer;
    private EditPannel mEditPannel;

    private TXVideoEditer mTXVideoEditer;
    private TCVideoFileInfo mTCVideoFileInfo;
    private TXVideoInfoReader mTXVideoInfoReader;

    private String mVideoOutputPath;
    private BackGroundHandler mHandler;
    private final int MSG_LOAD_VIDEO_INFO = 1000;
    private final int MSG_RET_VIDEO_INFO = 1001;
    private ProgressBar mLoadProgress;
    private TXVideoEditConstants.TXGenerateResult mResult;
    private int mCutVideoDuration;//裁剪的视频时长

    private boolean mPublish = false;
    private boolean mNoCache = false;
    private Button mDialogBtnSave;
    private Button mDialogBtnPublish;
    private Button mDialogBtnOnlyPublish;

    private VideoWorkProgressFragment mWorkProgressDialog;

    private String mVideoName;
    private SimpleDateFormat mSdf;
    private String mOutputPath;
    private File mOutputFolder;


    class BackGroundHandler extends Handler {

        public BackGroundHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_LOAD_VIDEO_INFO:
                    TXVideoEditConstants.TXVideoInfo videoInfo = mTXVideoInfoReader.getVideoFileInfo(mTCVideoFileInfo.getFilePath());
                    if (videoInfo == null) {
                        mLoadProgress.setVisibility(View.GONE);
                        AlertDialog.Builder normalDialog = new AlertDialog.Builder(TCVideoEditerActivity.this, R.style.ConfirmDialogStyle);
                        normalDialog.setMessage("暂不支持Android 4.3以下的系统");
                        normalDialog.setCancelable(false);
                        normalDialog.setPositiveButton("知道了", null);
                        normalDialog.show();
                        return;
                    }
                    Message mainMsg = new Message();
                    mainMsg.what = MSG_RET_VIDEO_INFO;
                    mainMsg.obj = videoInfo;
                    mMainHandler.sendMessage(mainMsg);
                    break;
            }

        }
    }

    private TXVideoEditConstants.TXVideoInfo mTXVideoInfo;
    private Handler mMainHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_RET_VIDEO_INFO:
                    mTXVideoInfo = (TXVideoEditConstants.TXVideoInfo) msg.obj;

                    TXVideoEditConstants.TXPreviewParam param = new TXVideoEditConstants.TXPreviewParam();
                    param.videoView = mVideoView;
                    param.renderMode = TXVideoEditConstants.PREVIEW_RENDER_MODE_FILL_EDGE;
                    mTXVideoEditer.setVideoPath(mTCVideoFileInfo.getFilePath());
                    mTXVideoEditer.initWithPreview(param);

                    handleOp(OP_SEEK, 0, (int) mTXVideoInfo.duration);
                    mLoadProgress.setVisibility(View.GONE);
                    mEditPannel.setMediaFileInfo(mTXVideoInfo);
                    String duration = TCUtils.duration(mTXVideoInfo.duration);
                    String position = TCUtils.duration(0);

                    mTvCurrent.setText(position);
                    mTvDuration.setText(duration);
                    createThumbFile(mTXVideoInfo);
                    break;
            }
        }
    };
    private HandlerThread mHandlerThread;

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);

        setContentView(R.layout.activity_video_editer);

        initViews();
        initData();
    }

    @Override
    protected void onDestroy() {

        TelephonyManager tm = (TelephonyManager) this.getApplicationContext().getSystemService(Service.TELEPHONY_SERVICE);
        tm.listen(listener, PhoneStateListener.LISTEN_NONE);

        mHandlerThread.quit();
        handleOp(OP_CANCEL, 0, 0);

        mTXVideoInfoReader.cancel();
        mTXVideoEditer.setTXVideoPreviewListener(null);
        mTXVideoEditer.setVideoGenerateListener(null);
        super.onDestroy();
    }

    private void initViews() {

        mEditPannel = (EditPannel) findViewById(R.id.edit_pannel);
        mEditPannel.setRangeChangeListener(this);
        mEditPannel.setEditCmdListener(this);


        mTvCurrent = (TextView) findViewById(R.id.tv_current);
        mTvDuration = (TextView) findViewById(R.id.tv_duration);

        mVideoView = (FrameLayout) findViewById(R.id.video_view);

        mBtnPlay = (ImageView) findViewById(R.id.btn_play);
        mBtnPlay.setOnClickListener(this);

        mTvDone = (TextView) findViewById(R.id.btn_done);
        mTvDone.setOnClickListener(this);

        mLayoutEditer = (LinearLayout) findViewById(R.id.layout_editer);
        mLayoutEditer.setEnabled(true);

        findViewById(R.id.back_tv).setOnClickListener(this);
        mLoadProgress = (ProgressBar) findViewById(R.id.progress_load);
        initWorkProgressPopWin();

        mSdf=new SimpleDateFormat("yyyyMMdd_HHmmss");

        mOutputPath = Environment.getExternalStorageDirectory() + File.separator + TCConstants.DEFAULT_MEDIA_PACK_FOLDER;
        mOutputFolder = new File(mOutputPath);
        if (!mOutputFolder.exists()) {
            mOutputFolder.mkdirs();
        }
    }

    private void initWorkProgressPopWin() {
        if (mWorkProgressDialog == null) {
            mWorkProgressDialog = new VideoWorkProgressFragment();
            mWorkProgressDialog.setOnClickStopListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mTvDone.setClickable(true);
                    mTvDone.setEnabled(true);
                    mWorkProgressDialog.dismiss();
                    finish();
                }
            });
        }
        mWorkProgressDialog.setProgress(0);
    }

    private synchronized boolean handleOp(int state, int startPlayTime, int endPlayTime) {
        switch (state) {
            case OP_PLAY:
                if (mCurrentState == STATE_NONE) {
                    mTXVideoEditer.startPlayFromTime(startPlayTime, endPlayTime);
                    mCurrentState = STATE_RESUME;
                    return true;
                } else if (mCurrentState == STATE_PAUSE) {
                    mTXVideoEditer.resumePlay();
                    mCurrentState = STATE_RESUME;
                    return true;
                }
                break;
            case OP_PAUSE:
                if (mCurrentState == STATE_RESUME) {
                    mTXVideoEditer.pausePlay();
                    mCurrentState = STATE_PAUSE;
                    return true;
                }
                break;
            case OP_SEEK:
                if (mCurrentState == STATE_CUT) {
                    return false;
                }
                if (mCurrentState == STATE_RESUME || mCurrentState == STATE_PAUSE) {
                    mTXVideoEditer.stopPlay();
                }
                mTXVideoEditer.startPlayFromTime(startPlayTime, endPlayTime);
                mCurrentState = STATE_RESUME;
                return true;
            case OP_CUT:
                if (mCurrentState == STATE_RESUME || mCurrentState == STATE_PAUSE) {
                    mTXVideoEditer.stopPlay();
                }
                startTranscode();
                mCurrentState = STATE_CUT;
                return true;
            case OP_CANCEL:
                if (mCurrentState == STATE_RESUME || mCurrentState == STATE_PAUSE) {
                    mTXVideoEditer.stopPlay();
                } else if (mCurrentState == STATE_CUT) {
                    mTXVideoEditer.cancel();
                }
                mCurrentState = STATE_NONE;
                return true;
        }
        return false;
    }

    private void initData() {
        mHandlerThread = new HandlerThread("LoadData");
        mHandlerThread.start();
        mHandler = new BackGroundHandler(mHandlerThread.getLooper());

        mTCVideoFileInfo = (TCVideoFileInfo) getIntent().getSerializableExtra(TCConstants.INTENT_KEY_SINGLE_CHOOSE);
        mTXVideoInfoReader = TXVideoInfoReader.getInstance();
        mTXVideoEditer = new TXVideoEditer(this);
        mTXVideoEditer.setTXVideoPreviewListener(this);

        //加载视频基本信息
        mHandler.sendEmptyMessage(MSG_LOAD_VIDEO_INFO);

        TelephonyManager tm = (TelephonyManager) this.getApplicationContext().getSystemService(Service.TELEPHONY_SERVICE);
        tm.listen(listener, PhoneStateListener.LISTEN_NONE);

        //加载缩略图
        mTXVideoInfoReader.getSampleImages(TCConstants.THUMB_COUNT, mTCVideoFileInfo.getFilePath(), this);
    }

    private void createThumbFile(TXVideoEditConstants.TXVideoInfo videoInfo) {
        AsyncTask<TXVideoEditConstants.TXVideoInfo, String, String> task = new AsyncTask<TXVideoEditConstants.TXVideoInfo, String, String>() {
            @Override
            protected String doInBackground(TXVideoEditConstants.TXVideoInfo... params) {

                if(mVideoName==null){
                    String current = String.valueOf(System.currentTimeMillis() / 1000);
                    String time = mSdf.format(new Date(Long.valueOf(current + "000")));
                    mVideoName = String.format("TXVideo_%s", time);
                }
                File file = new File(mOutputFolder, mVideoName+".png");
                try {
                    FileOutputStream fos = new FileOutputStream(file);
                    if (params[0].coverImage != null)
                        params[0].coverImage.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                    fos.flush();
                    fos.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                if (mTCVideoFileInfo.getThumbPath() == null) {
                    mTCVideoFileInfo.setThumbPath(file.getAbsolutePath());
                }
                return null;
            }

        };
        task.execute(videoInfo);
    }


    @Override
    protected void onStart() {
        super.onStart();
        if (mCurrentState == STATE_PAUSE) {
            handleOp(OP_PLAY, mEditPannel.getSegmentFrom(), mEditPannel.getSegmentTo());
            mBtnPlay.setImageResource(mCurrentState == STATE_RESUME ? R.drawable.ic_pause : R.drawable.ic_play);
        } else {
            Log.d("state", "onStart");
//            mTXVideoEditer.startPlayFromTime(mEditPannel.getSegmentFrom(), mEditPannel.getSegmentTo());
        }
    }


    @Override
    protected void onStop() {
        super.onStop();
        if (mCurrentState == STATE_CUT) {
            handleOp(OP_CANCEL, 0, 0);
            if (mWorkProgressDialog != null && mWorkProgressDialog.isAdded()) {
                mWorkProgressDialog.dismiss();
            }
        } else {
            handleOp(OP_PAUSE, 0, 0);
            mBtnPlay.setImageResource(mCurrentState == STATE_RESUME ? R.drawable.ic_pause : R.drawable.ic_play);
        }
        mTvDone.setClickable(true);
        mTvDone.setEnabled(true);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_done:
                showDialog();
                break;
            case R.id.back_tv:
                mTXVideoInfoReader.cancel();
                mTXVideoEditer.stopPlay();
                mTXVideoEditer.setTXVideoPreviewListener(null);
                mTXVideoEditer.setVideoGenerateListener(null);
                finish();
                break;
            case R.id.dialog_btn_save:
                mPublish = false;
                dismissDialog();
                doSave();
                break;
            case R.id.dialog_btn_publish:
                mPublish = true;
                mNoCache = false;
                dismissDialog();
                doSave();
                break;
            case R.id.only_publish:
                mNoCache = true;
                mPublish = true;
                dismissDialog();
                doSave();
                break;
            case R.id.btn_play:
                playVideo();
                break;

        }
    }

    private void publishVideo() {
        Intent intent = new Intent(getApplicationContext(), TCVideoPublisherActivity.class);
        intent.putExtra(TCConstants.VIDEO_RECORD_TYPE, TCConstants.VIDEO_RECORD_TYPE_PLAY);
        intent.putExtra(TCConstants.VIDEO_RECORD_VIDEPATH, mVideoOutputPath);
        intent.putExtra(TCConstants.VIDEO_RECORD_COVERPATH, mTCVideoFileInfo.getThumbPath());
        intent.putExtra(TCConstants.VIDEO_RECORD_NO_CACHE, mNoCache);
        startActivity(intent);
    }

    private void showDialog() {
        if (mDialog == null) {
            mDialog = new Dialog(this, R.style.dialog_bottom_full);

            mDialog.setCanceledOnTouchOutside(true);
            mDialog.setCancelable(true);

            Window window = mDialog.getWindow();
            window.setGravity(Gravity.BOTTOM);

            View view = View.inflate(this, R.layout.dialog_ugcedit_publish, null);

            mDialogBtnSave = (Button) view.findViewById(R.id.dialog_btn_save);
            mDialogBtnSave.setOnClickListener(this);
            mDialogBtnPublish = (Button) view.findViewById(R.id.dialog_btn_publish);
            mDialogBtnPublish.setOnClickListener(this);
            mDialogBtnOnlyPublish = (Button) view.findViewById(R.id.only_publish);
            mDialogBtnOnlyPublish.setOnClickListener(this);

            window.setContentView(view);
            window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        }
        mDialog.show();
    }

    private void dismissDialog() {
        if (mDialog != null && mDialog.isShowing()) {
            mDialog.dismiss();
        }
    }


    private void playVideo() {
        if (mCurrentState == STATE_RESUME) {
            handleOp(OP_PAUSE, 0, 0);
        } else {
            handleOp(OP_PLAY, mEditPannel.getSegmentFrom(), mEditPannel.getSegmentTo());
        }
        mBtnPlay.setImageResource(mCurrentState == STATE_RESUME ? R.drawable.ic_pause : R.drawable.ic_play);
    }

    private void doTranscode() {
        mTvDone.setEnabled(false);
        mTvDone.setClickable(false);

        mTXVideoInfoReader.cancel();
        mLayoutEditer.setEnabled(false);
        handleOp(OP_CUT, 0, 0);
    }

    private void startTranscode() {
        mBtnPlay.setImageResource(R.drawable.ic_play);
        mCutVideoDuration = mEditPannel.getSegmentTo() - mEditPannel.getSegmentFrom();
        mWorkProgressDialog.setProgress(0);
        mWorkProgressDialog.setCancelable(false);
        mWorkProgressDialog.show(getFragmentManager(), "progress_dialog");
        try {
            mTXVideoEditer.setCutFromTime(mEditPannel.getSegmentFrom(), mEditPannel.getSegmentTo());


            if(mVideoName==null){
                String current = String.valueOf(System.currentTimeMillis() / 1000);
                String time = mSdf.format(new Date(Long.valueOf(current + "000")));
                mVideoName = String.format("TXVideo_%s", time);
            }
            mVideoOutputPath = mOutputPath + "/" + mVideoName+".mp4";
            mTXVideoEditer.setVideoGenerateListener(this);
            mTXVideoEditer.generateVideo(TXVideoEditConstants.VIDEO_COMPRESSED_540P, mVideoOutputPath);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void doSave() {
        mTvDone.setEnabled(false);
        mTvDone.setClickable(false);

        mTXVideoInfoReader.cancel();
        mTXVideoEditer.stopPlay();
        mLayoutEditer.setEnabled(false);
        doTranscode();
    }


    @Override
    public void onGenerateProgress(final float progress) {
        final int prog = (int) (progress * 100);
        mWorkProgressDialog.setProgress(prog);
    }

    @Override
    public void onGenerateComplete(TXVideoEditConstants.TXGenerateResult result) {
        if (result.retCode == TXVideoEditConstants.GENERATE_RESULT_OK) {
            updateMediaStore();
            if (mPublish) {
                publishVideo();
                mPublish = false;
            } else {
                finish();
            }
            finish();
        } else {
            TXVideoEditConstants.TXGenerateResult ret = result;
            Toast.makeText(TCVideoEditerActivity.this, ret.descMsg, Toast.LENGTH_SHORT).show();
            mTvDone.setEnabled(true);
            mTvDone.setClickable(true);
        }

        mCurrentState = STATE_NONE;
    }

    private void updateMediaStore() {
        Intent scanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        scanIntent.setData(Uri.fromFile(new File(mVideoOutputPath)));
        sendBroadcast(scanIntent);
    }

    @Override
    public void sampleProcess(int number, Bitmap bitmap) {
        int num = number;
        Bitmap bmp = bitmap;
        mEditPannel.addBitmap(num, bmp);
        TXLog.d(TAG, "number = " + number + ",bmp = " + bitmap);
    }


    @Override
    public void onPreviewProgress(final int time) {
//        Log.d(TAG, "onPreviewProgress time : " + time);
        if (mTvCurrent != null) {
            mTvCurrent.setText(TCUtils.duration(time / 1000));
        }
    }

    @Override
    public void onPreviewFinished() {
        TXLog.d("eof", "---------------onPreviewFinished-----------------");
        handleOp(OP_SEEK, mEditPannel.getSegmentFrom(), mEditPannel.getSegmentTo());
    }

    @Override
    public void onKeyDown() {
        mBtnPlay.setImageResource(R.drawable.ic_play);
        Log.d("state", "onKeyDown");
        handleOp(OP_PAUSE, 0, 0);
    }

    @Override
    public void onKeyUp(int startTime, int endTime) {
        mBtnPlay.setImageResource(R.drawable.ic_pause);
        Log.d("state", "onKeyUp");
        handleOp(OP_SEEK, mEditPannel.getSegmentFrom(), mEditPannel.getSegmentTo());
    }

    @Override
    public void onCmd(int cmd, EditPannel.EditParams params) {
        switch (cmd) {
            case EditPannel.CMD_SPEED:
//                mTXVideoEditer.setSpeed(params.mSpeedRate);
                break;
            case EditPannel.CMD_FILTER:
                mTXVideoEditer.setFilter(params.mFilterBmp);
                break;
        }
    }

    PhoneStateListener listener = new PhoneStateListener() {
        @Override
        public void onCallStateChanged(int state, String incomingNumber) {
            super.onCallStateChanged(state, incomingNumber);
            switch (state) {
                //电话等待接听
                case TelephonyManager.CALL_STATE_RINGING:
                    if (mTXVideoEditer != null) handleOp(OP_PAUSE, 0, 0);
                    break;
                //电话接听
                case TelephonyManager.CALL_STATE_OFFHOOK:
                    if (mTXVideoEditer != null) handleOp(OP_PAUSE, 0, 0);
                    break;
                //电话挂机
                case TelephonyManager.CALL_STATE_IDLE:
                    if (mTXVideoEditer != null)
                        handleOp(OP_PLAY, mEditPannel.getSegmentFrom(), mEditPannel.getSegmentTo());
                    break;
            }
        }
    };
}

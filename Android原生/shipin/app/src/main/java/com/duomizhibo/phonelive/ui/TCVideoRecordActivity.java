package com.duomizhibo.phonelive.ui;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.tencent.rtmp.ugc.TXRecordCommon;
import com.tencent.rtmp.ugc.TXUGCRecord;
import com.tencent.rtmp.ui.TXCloudVideoView;
import com.duomizhibo.phonelive.R;
import com.duomizhibo.phonelive.TCConstants;
import com.duomizhibo.phonelive.base.ToolBarBaseActivity;
import com.duomizhibo.phonelive.fragment.BeautyDialogFragment;
import com.duomizhibo.phonelive.utils.TCUtils;
import com.duomizhibo.phonelive.videoeditor.TCVideoChooseActivity;

import java.util.Locale;

/**
 * UGC主播端录制界面
 */
public class TCVideoRecordActivity extends ToolBarBaseActivity implements View.OnClickListener, BeautyDialogFragment.OnBeautyParamsChangeListener
                                                            ,TXRecordCommon.ITXVideoRecordListener
{

    private BeautyDialogFragment mBeautyDialogFragment;

    //录制相关
    private boolean mRecording = false;
    private TXUGCRecord mTXCameraRecord = null;
    private ProgressBar mRecordProgress = null;
    private long  mStartRecordTimeStamp = 0;
    private BeautyDialogFragment.BeautyParams mBeautyParams = new BeautyDialogFragment.BeautyParams();

    private boolean mFlashOn = false;
    private boolean mFront = true;
    TXRecordCommon.TXRecordResult mTXRecordResult = null;
    TXCloudVideoView mVideoView;
    TextView mProgressTime;
    static TCVideoRecordActivity ActivityA;
    AudioManager mAudioManager;
    AudioManager.OnAudioFocusChangeListener mOnAudioFocusListener;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_video_record;
    }


    @Override
    protected void onStop() {
        super.onStop();

        stopRecord(false);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (mTXCameraRecord != null) {
            mTXCameraRecord.stopCameraPreview();
            mTXCameraRecord.setVideoRecordListener(null);
            mTXCameraRecord = null;
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_beauty:
                Bundle args = new Bundle();
                args.putBoolean("hideMotionTable", true);
                try {
                    mBeautyDialogFragment.setArguments(args);
                    if (mBeautyDialogFragment.isAdded())
                        mBeautyDialogFragment.dismiss();
                    else
                        mBeautyDialogFragment.show(getFragmentManager(), "");
                } catch (Exception e) {
                    e.printStackTrace();
                }

                break;
            case R.id.btn_flash:
                mFlashOn = !mFlashOn;
                if (mTXCameraRecord != null) {
                    mTXCameraRecord.toggleTorch(mFlashOn);
                }
                break;
            case R.id.btn_switch_camera:
                mFront = !mFront;
                if (mTXCameraRecord != null) {
                    mTXCameraRecord.switchCamera(mFront);
                }
                break;
            case R.id.btn_close:
                if (mRecording && mTXCameraRecord != null) {
                    mTXCameraRecord.stopRecord();
                    mTXCameraRecord.setVideoRecordListener(null);
                }
                finish();
                break;
            case R.id.record:
                switchRecord();
                break;
            case R.id.btn_upload:
                startActivity(new Intent(this, TCVideoChooseActivity.class));
                break;
        }
    }

    private void switchRecord() {
        if (mRecording) {
           stopRecord(true);
        } else {
            startRecord();
        }
    }

    private void stopRecord(boolean showToast) {
        // 录制时间要大于5s
        if (System.currentTimeMillis() <= mStartRecordTimeStamp + 5*1000) {
            if (showToast) {
                showTooShortToast();
                return;
            } else {
                if (mTXCameraRecord != null) {
                    mTXCameraRecord.setVideoRecordListener(null);
                }
            }
        }
        if (mTXCameraRecord != null) {
            mTXCameraRecord.stopRecord();
        }
        ImageView liveRecord = (ImageView) findViewById(R.id.record);
        if(liveRecord != null) liveRecord.setBackgroundResource(R.drawable.start_record);
        mRecording = false;

        if (mRecordProgress != null) {
            mRecordProgress.setProgress(0);
        }
        if (mProgressTime != null) {
            mProgressTime.setText(String.format(Locale.CHINA, "%s","00:00"));
        }
        abandonAudioFocus();
    }

    private void showTooShortToast() {
        if (mRecordProgress != null) {
            int statusBarHeight = 0;
            int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
            if (resourceId > 0) {
                statusBarHeight = getResources().getDimensionPixelSize(resourceId);
            }

            int[] position = new int[2];
            mRecordProgress.getLocationOnScreen(position);
            Toast toast = Toast.makeText(this, "至少录到这里", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.TOP|Gravity.LEFT, position[0], position[1] - statusBarHeight - 110);
            toast.show();
        }
    }

    private void startRecord() {
        if (mTXCameraRecord == null) {
            mTXCameraRecord = TXUGCRecord.getInstance(this.getApplicationContext());
        }
        mRecordProgress = (ProgressBar) findViewById(R.id.record_progress);
        mTXCameraRecord.setVideoRecordListener(this);
        int result = mTXCameraRecord.startRecord();
        if (result != 0) {
            Toast.makeText(TCVideoRecordActivity.this.getApplicationContext(),"录制失败，错误码：" + result, Toast.LENGTH_SHORT).show();
            mTXCameraRecord.setVideoRecordListener(null);
            mTXCameraRecord.stopRecord();
            return;
        }
        mRecording = true;
        ImageView liveRecord = (ImageView) findViewById(R.id.record);
        if(liveRecord != null) liveRecord.setBackgroundResource(R.drawable.stop_record);
        mStartRecordTimeStamp = System.currentTimeMillis();
        requestAudioFocus();
    }

    void startPreview() {
        if (mTXRecordResult != null && mTXRecordResult.retCode == TXRecordCommon.RECORD_RESULT_OK) {
            Intent intent = new Intent(getApplicationContext(), TCVideoPreviewActivity.class);
            intent.putExtra(TCConstants.VIDEO_RECORD_TYPE, TCConstants.VIDEO_RECORD_TYPE_PUBLISH);
            intent.putExtra(TCConstants.VIDEO_RECORD_RESULT, mTXRecordResult.retCode);
            intent.putExtra(TCConstants.VIDEO_RECORD_DESCMSG, mTXRecordResult.descMsg);
            intent.putExtra(TCConstants.VIDEO_RECORD_VIDEPATH, mTXRecordResult.videoPath);
            intent.putExtra(TCConstants.VIDEO_RECORD_COVERPATH, mTXRecordResult.coverPath);
            startActivity(intent);
        }
    }


//    private void retryRecord() {
//        if (mRecording ) {
//            stopRecord();
//        }
//        View recordLayout = TCVideoRecordActivity.this.findViewById(R.id.record_layout);
//        View publishLayout = TCVideoRecordActivity.this.findViewById(R.id.publishLayout);
//        View controlLayout = TCVideoRecordActivity.this.findViewById(R.id.record_control);
//        if (recordLayout != null) {
//            recordLayout.setVisibility(View.VISIBLE);
//        }
//        if (publishLayout != null) {
//            publishLayout.setVisibility(View.GONE);
//        }
//        if (controlLayout != null) {
//            controlLayout.setVisibility(View.VISIBLE);
//        }
//
//        if (mRecordProgress != null) {
//            mRecordProgress.setProgress(0);
//        }
//
//        mLayoutPitu.setVisibility(View.GONE);
//        mPitu.setVisibility(View.VISIBLE);
//        mClosePitu.setVisibility(View.GONE);
//    }

    @Override
    public void onBeautyParamsChange(BeautyDialogFragment.BeautyParams params, int key) {
        switch (key){
            case BeautyDialogFragment.BEAUTYPARAM_BEAUTY:
            case BeautyDialogFragment.BEAUTYPARAM_WHITE:
                if (mTXCameraRecord != null) {
                    mTXCameraRecord.setBeautyDepth(params.mBeautyProgress, params.mWhiteProgress);
                }
                break;
            case BeautyDialogFragment.BEAUTYPARAM_FACE_LIFT:
                if (mTXCameraRecord != null) {
                    mTXCameraRecord.setFaceScaleLevel(params.mFaceLiftProgress);
                }
                break;
            case BeautyDialogFragment.BEAUTYPARAM_BIG_EYE:
                if (mTXCameraRecord != null) {
                    mTXCameraRecord.setEyeScaleLevel(params.mBigEyeProgress);
                }
                break;
            case BeautyDialogFragment.BEAUTYPARAM_FILTER:
                if (mTXCameraRecord != null) {
                    mTXCameraRecord.setFilter(TCUtils.getFilterBitmap(getResources(), params.mFilterIdx));
                }
                break;
            case BeautyDialogFragment.BEAUTYPARAM_MOTION_TMPL:
                if (mTXCameraRecord != null){
                    mTXCameraRecord.setMotionTmpl(params.mMotionTmplPath);
                }
                break;
            case BeautyDialogFragment.BEAUTYPARAM_GREEN:
                if (mTXCameraRecord != null){
                    mTXCameraRecord.setGreenScreenFile(TCUtils.getGreenFileName(params.mGreenIdx), true);
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onRecordEvent(int event, Bundle param) {

    }

    @Override
    public void onRecordProgress(long milliSecond) {
        if (mRecordProgress != null) {
            float progress = milliSecond / 60000.0f;
            mRecordProgress.setProgress((int) (progress * 100));
            mProgressTime.setText(String.format(Locale.CHINA, "00:%02d", milliSecond/1000));
            if (milliSecond >= 60000.0f) {
                stopRecord(true);
            }
        }
    }

    @Override
    public void onRecordComplete(TXRecordCommon.TXRecordResult result) {
        mTXRecordResult = result;
        if (mTXRecordResult.retCode != TXRecordCommon.RECORD_RESULT_OK) {
            ImageView liveRecord = (ImageView) findViewById(R.id.record);
            if(liveRecord != null) liveRecord.setBackgroundResource(R.drawable.start_record);
            mRecording = false;

            if (mRecordProgress != null) {
                mRecordProgress.setProgress(0);
            }
            if (mProgressTime != null) {
                mProgressTime.setText(String.format(Locale.CHINA, "%s","00:00"));
            }
            Toast.makeText(TCVideoRecordActivity.this.getApplicationContext(),"录制失败，原因：" + mTXRecordResult.descMsg, Toast.LENGTH_SHORT).show();
        } else {
            View recordLayout = TCVideoRecordActivity.this.findViewById(R.id.record_layout);
            View publishLayout = TCVideoRecordActivity.this.findViewById(R.id.publishLayout);
            View controlLayout = TCVideoRecordActivity.this.findViewById(R.id.record_control);
            if (recordLayout != null) {
                recordLayout.setVisibility(View.VISIBLE);
            }
            if (publishLayout != null) {
                publishLayout.setVisibility(View.GONE);
            }
            if (controlLayout != null) {
                controlLayout.setVisibility(View.VISIBLE);
            }

            if (mRecordProgress != null) {
                mRecordProgress.setProgress(0);
            }
            mProgressTime.setText(String.format(Locale.CHINA, "%s","00:00"));
            startPreview();
        }
    }

    private void requestAudioFocus() {

        if (null == mAudioManager) {
            mAudioManager = (AudioManager) this.getSystemService(Context.AUDIO_SERVICE);
        }

        if (null == mOnAudioFocusListener) {
            mOnAudioFocusListener = new AudioManager.OnAudioFocusChangeListener() {

                @Override
                public void onAudioFocusChange(int focusChange) {
                    try {
                        if (focusChange == AudioManager.AUDIOFOCUS_LOSS) {
                            mTXCameraRecord.setVideoRecordListener(null);
                            stopRecord(false);
                        } else if (focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT) {
                            mTXCameraRecord.setVideoRecordListener(null);
                            stopRecord(false);
                        } else if (focusChange == AudioManager.AUDIOFOCUS_GAIN) {

                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            };
        }
        try {
            mAudioManager.requestAudioFocus(mOnAudioFocusListener, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void abandonAudioFocus() {
        try {
            if (null != mAudioManager && null != mOnAudioFocusListener) {
                mAudioManager.abandonAudioFocus(mOnAudioFocusListener);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void initView() {

        ActivityA=this;
        mBeautyDialogFragment = new BeautyDialogFragment();
        mBeautyDialogFragment.setBeautyParamsListner(mBeautyParams, this);

        mTXCameraRecord = TXUGCRecord.getInstance(this.getApplicationContext());

        // 预览
        if (mTXCameraRecord == null) {
            mTXCameraRecord = TXUGCRecord.getInstance(TCVideoRecordActivity.this.getApplicationContext());
        }
        mVideoView = (TXCloudVideoView) findViewById(R.id.video_view);
        mVideoView.enableHardwareDecode(true);
        TXRecordCommon.TXUGCSimpleConfig param = new TXRecordCommon.TXUGCSimpleConfig();
        param.videoQuality = TXRecordCommon.VIDEO_QUALITY_MEDIUM;
        param.isFront = mFront;
        mTXCameraRecord.startCameraSimplePreview(param,mVideoView);
        mTXCameraRecord.setBeautyDepth(mBeautyParams.mBeautyProgress, mBeautyParams.mWhiteProgress);
        mTXCameraRecord.setMotionTmpl(mBeautyParams.mMotionTmplPath);

        mProgressTime = (TextView) findViewById(R.id.progress_time);
    }

    @Override
    public void initData() {

    }
    @Override
    protected boolean hasActionBar() {
        return false;
    }
}

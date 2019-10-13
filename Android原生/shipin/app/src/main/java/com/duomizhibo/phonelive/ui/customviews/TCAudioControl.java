package com.duomizhibo.phonelive.ui.customviews;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;


import com.tencent.rtmp.TXLivePusher;
import com.duomizhibo.phonelive.R;
import com.duomizhibo.phonelive.utils.TCUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Link on 2016/9/8.
 */
public class TCAudioControl extends LinearLayout implements SeekBar.OnSeekBarChangeListener, Button.OnClickListener {
    public static final int NEXTBGM = 1;
    public static final int PREVIOUSBGM = 2;
    public static final int RANDOMBGM = 3;
    //Audio Control
    public static final String TAG = TCAudioControl.class.getSimpleName();
    private SeekBar mMicVolumeSeekBar;
    private SeekBar mBGMVolumeSeekBar;

    private Button mBtnReverbDefalult;
    private Button mBtnReverb1;
    private Button mBtnReverb2;
    private Button mBtnReverb3;
    private Button mBtnReverb4;
    private Button mBtnReverb5;
    private Button mBtnReverb6;
    private int mLastReverbIndex;

    private Button mBtnStopBgm;

    private LinearLayout mAudioPluginLayout;
    private Button mBtnAutoSearch;
    private Button mBtnBGMLayout;
    private Button mBtnMicLayout;
    private ImageView mImgBGMIcon;
    private ImageView mImgMicIcon;
    private ToggleButton mTogBGM;
    private ToggleButton mTogMic;
    private boolean mTogCheck = false;
    private Button mBtnSelectActivity;
    private int mMicVolume = 100;
    private int mBGMVolume = 100;
    private RelativeLayout mBGMLayout;
    private RelativeLayout mMicLayout;
    private boolean mBGMSwitch = false;
    private boolean mScanning = false;
    Context mContext;
    List<MediaEntity> mMusicListData;
    MusicListView mMusicList;
    public TCMusicSelectView mMusicSelectView;
    public LinearLayout mMusicControlPart;
    private int mLastClickItemPos = -1;
    private long mLastClickItemTimeStamp = 0;
    private int mSelectItemPos = -1;
    private int mLastPlayingItemPos = -1;
    private TXLivePusher.OnBGMNotify mBGMNotify;
    private LinearLayout mCurMusicLayout;
    private TextView mCurMusic;
    private ProgressBar mCurMusicProgress;
    private Thread mCurMusicProgressThread;
    private UpdatePlayProgressThread mCurMusicProgressThreadUnity;
    private MusicScanner mMusicScanner;
    private Handler mMainThread;
    public static final int REQUESTCODE = 1;
    private Map<String, String> mPathSet;
    private static TCAudioControl _instance = null;
    private TXLivePusher mPusher;

    public void setPusher(TXLivePusher pusher) {
        mPusher = pusher;
        mPusher.setBGMNofify(mBGMNotify);
    }

    public static TCAudioControl getInstance() {
        return _instance;
    }

    public TCAudioControl(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        _instance = this;
        LayoutInflater.from(context).inflate(R.layout.audio_ctrl, this);
        init();
        mBGMNotify = new TXLivePusher.OnBGMNotify() {
            @Override
            public void onBGMProgress(long l, long l1) {

            }

            @Override
            public void onBGMComplete(int i) {

            }

            @Override
            public void onBGMStart() {
            }
        };
    }

    public TCAudioControl(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mContext = context;
        _instance = this;
        LayoutInflater.from(context).inflate(R.layout.audio_ctrl, this);
        init();
        mBGMNotify = new TXLivePusher.OnBGMNotify() {
            @Override
            public void onBGMProgress(long l, long l1) {

            }

            @Override
            public void onBGMComplete(int i) {

            }

            @Override
            public void onBGMStart() {
//                Log.w(TAG,"bgm start");
            }
        };
    }

    public TCAudioControl(Context context) {
        super(context);
        mContext = context;
        _instance = this;
        LayoutInflater.from(context).inflate(R.layout.audio_ctrl, this);
        init();
        mBGMNotify = new TXLivePusher.OnBGMNotify() {
            @Override
            public void onBGMProgress(long l, long l1) {

            }

            @Override
            public void onBGMComplete(int i) {

            }

            @Override
            public void onBGMStart() {
            }
        };
    }

    public void setPluginLayout(LinearLayout plugin) {
        mAudioPluginLayout = plugin;
    }

    public final Activity getActivity() {
        return (Activity) mContext;
    }

    private synchronized void playBGM(String name, String path, int pos) {
        if (mLastPlayingItemPos >= 0 && mLastPlayingItemPos != pos) {
            mMusicListData.get(mLastPlayingItemPos).state = 0;
        }
        if (!mPusher.playBGM(path)) {
            Toast.makeText(getActivity().getApplicationContext(), "打开BGM失败", Toast.LENGTH_SHORT).show();
            mMusicList.getAdapter().notifyDataSetChanged();
            return;
        }
        mBGMSwitch = true;
        mMusicListData.get(pos).state = 1;
        mLastPlayingItemPos = pos;
        mTogCheck = true;
//        mTogBGM.setChecked(true);
        mMusicList.getAdapter().notifyDataSetChanged();
//        if (mAudioPluginLayout != null){
//            mAudioPluginLayout.setVisibility(View.VISIBLE);
//        }
    }

    public synchronized void stopBGM() {
        mBGMSwitch = false;
        if (mPusher != null) mPusher.stopBGM();
//        mTogBGM.setBackgroundDrawable(getResources().getDrawable(R.drawable.switch_off));
//        if (mAudioPluginLayout != null){
//            mAudioPluginLayout.setVisibility(View.VISIBLE);
//        }
    }

    public synchronized boolean isPlayingBGM() {
        return mBGMSwitch;
    }

    private synchronized boolean updatePlayProgress() {
//        if (mBGMSwitch){
//            mCurMusicProgress.setProgress((int)(mPusher.curBGMProgress()*200));
//            return true;
//        }
//        else return false;
        return false;
    }

    public synchronized void playBGM(int order) {
        mSelectItemPos = mLastPlayingItemPos;
        switch (order) {
            case NEXTBGM:
                mSelectItemPos = mSelectItemPos + 1;
                if (mSelectItemPos >= mMusicListData.size()) {
                    mSelectItemPos = 0;
                }
                break;
            case PREVIOUSBGM:
                mSelectItemPos = mSelectItemPos - 1;
                if (mSelectItemPos < 0) {
                    mSelectItemPos = mMusicListData.size() - 1;
                }
                break;
            case RANDOMBGM:
                mSelectItemPos = (int) (Math.random() * mMusicListData.size());
                break;
        }
        mMusicList.requestFocus();
        mMusicList.setItemChecked(mSelectItemPos, true);
        playBGM(mMusicListData.get(mSelectItemPos).title, mMusicListData.get(mSelectItemPos).path, mSelectItemPos);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_reverb_default:
                mPusher.setReverb(0);
                break;
            case R.id.btn_reverb_1:
                mPusher.setReverb(1);
                break;
            case R.id.btn_reverb_2:
                mPusher.setReverb(2);
                break;
            case R.id.btn_reverb_3:
                mPusher.setReverb(3);
                break;
            case R.id.btn_reverb_4:
                mPusher.setReverb(4);
                break;
            case R.id.btn_reverb_5:
                mPusher.setReverb(5);
                break;
            case R.id.btn_reverb_6:
                mPusher.setReverb(6);
                break;
            case R.id.btn_stop_bgm:
                stopBGM();
                break;
        }

        if (R.id.btn_stop_bgm != v.getId() && v.getId() != mLastReverbIndex) {
            v.setBackgroundDrawable(getResources().getDrawable(R.drawable.round_button_3));

            View lastV = findViewById(mLastReverbIndex);
            if (null != lastV) {
                lastV.setBackgroundDrawable(getResources().getDrawable(R.drawable.round_button_2));
            }

            mLastReverbIndex = v.getId();
        }
    }

    class UpdatePlayProgressThread implements Runnable {
        public boolean mRun = true;
        TCAudioControl mPlayer;

        public UpdatePlayProgressThread(TCAudioControl musicPlayer) {
            mPlayer = musicPlayer;
        }

        @Override
        public void run() {
            while (mRun) {
                try {
                    mPlayer.updatePlayProgress();
                    Thread.sleep(1000);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void init() {
        mMainThread = new Handler(mContext.getMainLooper());
        mMicVolumeSeekBar = (SeekBar) findViewById(R.id.seekBar_voice_volume);
        mMicVolumeSeekBar.setOnSeekBarChangeListener(this);
        mBGMVolumeSeekBar = (SeekBar) findViewById(R.id.seekBar_bgm_volume);
        mBGMVolumeSeekBar.setOnSeekBarChangeListener(this);

        mBtnReverbDefalult = (Button) findViewById(R.id.btn_reverb_default);
        mBtnReverbDefalult.setOnClickListener(this);
        mBtnReverb1 = (Button) findViewById(R.id.btn_reverb_1);
        mBtnReverb1.setOnClickListener(this);
        mBtnReverb2 = (Button) findViewById(R.id.btn_reverb_2);
        mBtnReverb2.setOnClickListener(this);
        mBtnReverb3 = (Button) findViewById(R.id.btn_reverb_3);
        mBtnReverb3.setOnClickListener(this);
        mBtnReverb4 = (Button) findViewById(R.id.btn_reverb_4);
        mBtnReverb4.setOnClickListener(this);
        mBtnReverb5 = (Button) findViewById(R.id.btn_reverb_5);
        mBtnReverb5.setOnClickListener(this);
        mBtnReverb6 = (Button) findViewById(R.id.btn_reverb_6);
        mBtnReverb6.setOnClickListener(this);

        mBtnStopBgm = (Button) findViewById(R.id.btn_stop_bgm);
        mBtnStopBgm.setOnClickListener(this);

        mBtnSelectActivity = (Button) findViewById(R.id.btn_select_bgm);
        mMusicSelectView = (TCMusicSelectView) findViewById(R.id.xml_music_select_view);
        mMusicControlPart = (LinearLayout) findViewById(R.id.xml_music_control_part);
        mMusicListData = new ArrayList<MediaEntity>();
        mMusicSelectView.init(this, mMusicListData);
        mMusicList = mMusicSelectView.mMusicList;
        mPathSet = new HashMap<String, String>();
        mBtnAutoSearch = mMusicSelectView.mBtnAutoSearch;
        mMusicSelectView.setBackgroundColor(0xffffffff);
        WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        int height = wm.getDefaultDisplay().getHeight();
        mMusicSelectView.setMinimumHeight(height);

        mBtnSelectActivity.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mMusicSelectView.setVisibility(mMusicSelectView.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
                mMusicControlPart.setVisibility(View.GONE);
            }
        });

        mMusicList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                playBGM(mMusicListData.get(position).title, mMusicListData.get(position).path, position);
                mLastClickItemPos = position;
                mSelectItemPos = position;
                mLastClickItemTimeStamp = System.currentTimeMillis();
            }
        });


        mBtnAutoSearch.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mScanning) {
                    mScanning = false;
                    fPause = true;
                } else {
                    mScanning = true;
                    getMusicList(mContext, mMusicListData);
                    mScanning = false;
                    //mMusicScanner.startScanner(mContext,mCurScanPath,mMusicListData);
                    if (mMusicListData.size() > 0) {
//                        if (mMusicListData.size() >= 3){
//                            float scale = mContext.getResources().getDisplayMetrics().density;
//                            int height = (int)(scale * 100);
//                            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams)(new LinearLayout.LayoutParams(mMusicList.getLayoutParams().width,height));
//                            mMusicList.setLayoutParams(params);
//                        }
                        mMusicList.setupList(LayoutInflater.from(mContext), mMusicListData);
                        mSelectItemPos = 0;
                        mMusicList.requestFocus();
                        mMusicList.setItemChecked(0, true);
//                        mMusicList.setSelection(1);
                    }
                }
            }
        });
    }

    public void unInit() {
        if (mBGMSwitch) {
            stopBGM();
        }
        _instance = null;
    }

    public void processActivityResult(Uri uri) {
        Cursor cursor = mContext.getContentResolver().query(uri,
                new String[]{
                        MediaStore.Audio.Media._ID,
                        MediaStore.Audio.Media.TITLE,
                        MediaStore.Audio.Media.DISPLAY_NAME,
                        MediaStore.Audio.Media.DURATION,
                        MediaStore.Audio.Media.ARTIST,
                        MediaStore.Audio.Media.DATA,
                        MediaStore.Audio.Media.SIZE},
                null, null, null);
        MediaEntity mediaEntity = new MediaEntity();
        if (cursor == null) {
            Log.e(TAG, "GetMediaList cursor is null.");
            mediaEntity.duration = 0;
            mediaEntity.path = TCUtils.getPath(mContext, uri);
            String[] names = mediaEntity.path.split("/");
            if (names != null) {
                mediaEntity.display_name = names[names.length - 1];
                mediaEntity.title = mediaEntity.display_name;
            } else {
                mediaEntity.display_name = "未命名歌曲";
                mediaEntity.title = mediaEntity.display_name;
            }
        } else {
            int count = cursor.getCount();
            if (count <= 0) {
                Log.e(TAG, "GetMediaList cursor count is 0.");
                return;
            }
            cursor.moveToFirst();

            mediaEntity.id = cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Media._ID));
            //mediaEntity.title = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE));
            mediaEntity.display_name = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DISPLAY_NAME));
            String title = mediaEntity.display_name.split("\\.")[0];
            mediaEntity.title = title.equals("") ? mediaEntity.display_name : title;
            mediaEntity.size = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.SIZE));
//                if(!checkIsMusic(mediaEntity.duration, mediaEntity.size)) {
//                    continue;
//                }
            mediaEntity.artist = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST));
            mediaEntity.path = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA));
            if (mediaEntity.path == null) {
                mediaEntity.path = TCUtils.getPath(mContext, uri);
            }
            mediaEntity.duration = cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION));
        }
        if (mediaEntity.path == null) {
            Toast.makeText(mContext, "Get Music Path Error", Toast.LENGTH_SHORT);
            return;
        } else {
            if (mPathSet.get(mediaEntity.path) != null) {
                Toast.makeText(mContext, "请勿重复添加", Toast.LENGTH_SHORT);
                return;
            }
        }
        mPathSet.put(mediaEntity.path, mediaEntity.display_name);
        if (mediaEntity.duration == 0) {
            mediaEntity.duration = mPusher.getMusicDuration(mediaEntity.path);
        }
        mediaEntity.durationStr = longToStrTime(mediaEntity.duration);
        mMusicListData.add(mediaEntity);
        mSelectItemPos = mMusicListData.size() - 1;
        mMusicList.setupList(LayoutInflater.from(mContext), mMusicListData);
        mMusicList.requestFocus();
        mMusicList.setItemChecked(mSelectItemPos, true);
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (seekBar.getId() == R.id.seekBar_voice_volume) {
            mMicVolume = progress;
            mPusher.setMicVolume(mMicVolume / (float) 100);
        } else if (seekBar.getId() == R.id.seekBar_bgm_volume) {
            mBGMVolume = progress;
            mPusher.setBGMVolume(mBGMVolume / (float) 100);
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    class MusicScanner extends BroadcastReceiver {
        private AlertDialog.Builder builder = null;
        private AlertDialog ad = null;
        Context mContext;
        List<MediaEntity> mList;
        TextView mPathView;

        public void startScanner(Context context, TextView pathView, List<MediaEntity> list) {
            mContext = context;
            mList = list;
            mPathView = pathView;
            IntentFilter intentfilter = new IntentFilter(Intent.ACTION_MEDIA_SCANNER_STARTED);
            intentfilter.addAction(Intent.ACTION_MEDIA_SCANNER_FINISHED);
            intentfilter.addDataScheme("file");
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                String[] paths = new String[]{Environment.getExternalStorageDirectory().toString()};
                MediaScannerConnection.scanFile(mContext, paths, null, null);
            } else {
                mContext.registerReceiver(this, intentfilter);
                mContext.sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED, Uri.parse("file://" + Environment.getExternalStorageDirectory().getAbsolutePath())));
            }
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (Intent.ACTION_MEDIA_SCANNER_STARTED.equals(action)) {
                builder = new AlertDialog.Builder(context);
                builder.setMessage("正在扫描存储卡...");
                ad = builder.create();
                ad.show();
            } else if (Intent.ACTION_MEDIA_SCANNER_FINISHED.equals(action)) {
                getMusicList(mContext, mList);
                ad.dismiss();
            }
        }
    }

    String longToStrTime(long time) {
        time /= 1000;
        return (time / 60) + ":" + ((time % 60) > 9 ? (time % 60) : ("0" + (time % 60)));
    }

    static public boolean fPause = false;

    public void getMusicList(Context context, List<MediaEntity> list) {
        Cursor cursor = null;
        List<MediaEntity> mediaList = list;
        try {
            cursor = context.getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                    new String[]{
                            MediaStore.Audio.Media._ID,
                            MediaStore.Audio.Media.TITLE,
                            MediaStore.Audio.Media.DISPLAY_NAME,
                            MediaStore.Audio.Media.DURATION,
                            MediaStore.Audio.Media.ARTIST,
                            MediaStore.Audio.Media.DATA,
                            MediaStore.Audio.Media.SIZE},
                    null, null, MediaStore.Video.Media.DEFAULT_SORT_ORDER);
            //selection, null, MediaStore.Audio.Media.DATE_ADDED + " DESC");
            if (cursor == null) {
                Log.e(TAG, "GetMediaList cursor is null.");
                return;
            }
            int count = cursor.getCount();
            if (count <= 0) {
                Log.e(TAG, "GetMediaList cursor count is 0.");
                Toast.makeText(context,"没有本地音乐",Toast.LENGTH_SHORT).show();
                return;
            }
            MediaEntity mediaEntity = null;
//          String[] columns = cursor.getColumnNames();
            while (!fPause && cursor.moveToNext()) {
                mediaEntity = new MediaEntity();
                mediaEntity.id = cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Media._ID));
                mediaEntity.title = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE));
                mediaEntity.display_name = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DISPLAY_NAME));
                mediaEntity.size = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.SIZE));
//                if(!checkIsMusic(mediaEntity.duration, mediaEntity.size)) {
//                    continue;
//                }
                mediaEntity.artist = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST));
                mediaEntity.path = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA));
                if (mediaEntity.path == null) {
                    fPause = false;
                    Toast.makeText(mContext, "Get Music Path Error", Toast.LENGTH_SHORT);
                    return;
                } else {
                    if (mPathSet.get(mediaEntity.path) != null) {
                        Toast.makeText(mContext, "请勿重复添加", Toast.LENGTH_SHORT);
                        fPause = false;
                        return;
                    }
                }
                mPathSet.put(mediaEntity.path, mediaEntity.display_name);
                mediaEntity.duration = cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION));
                if (mediaEntity.duration == 0) {
                    mediaEntity.duration = mPusher.getMusicDuration(mediaEntity.path);
                }
                mediaEntity.durationStr = longToStrTime(mediaEntity.duration);
                mediaList.add(mediaEntity);
            }
            fPause = false;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return;
    }

    class MediaEntity implements Serializable {
        private static final long serialVersionUID = 1L;
        public int id; //id标识
        public String title; // 显示名称
        public String display_name; // 文件名称
        public String path; // 音乐文件的路径
        public int duration; // 媒体播放总时间
        public String albums; // 专辑
        public String artist; // 艺术家
        public String singer; //歌手
        public String durationStr;
        public long size;
        public char state = 0;//0:idle 1:playing

        MediaEntity() {

        }

    }

}



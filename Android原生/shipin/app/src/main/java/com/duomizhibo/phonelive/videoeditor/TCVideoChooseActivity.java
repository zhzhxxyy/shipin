package com.duomizhibo.phonelive.videoeditor;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.tencent.rtmp.TXLog;
import com.duomizhibo.phonelive.R;

import java.util.ArrayList;

public class TCVideoChooseActivity extends Activity implements View.OnClickListener {
    private static final String TAG = TCVideoChooseActivity.class.getSimpleName();

    private final int TYPE_SINGLE_CHOOSE = 0;
    private final int TYPE_MULTI_CHOOSE = 1;

    private Button mBtnOk;
    private TextView mTvBack;
    private RecyclerView mRecyclerView;

    private int mType;

    private TCVideoEditerListAdapter mAdapter;
    private TCVideoEditerMgr mTCVideoEditerMgr;

    private Handler mHandler;
    private HandlerThread mHandlerThread;

    private Handler mMainHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            ArrayList<TCVideoFileInfo> fileInfoArrayList = (ArrayList<TCVideoFileInfo>) msg.obj;
            mAdapter.addAll(fileInfoArrayList);
        }
    };


    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.activity_ugc_video_list);

        mTCVideoEditerMgr = TCVideoEditerMgr.getInstance(this);
        mHandlerThread = new HandlerThread("LoadList");
        mHandlerThread.start();
        mHandler = new Handler(mHandlerThread.getLooper());

//        String action = getIntent().getAction();
//        if (action == null) {
//            finish();
//            return;
//        }
        mType = TYPE_SINGLE_CHOOSE;
//        if (action.equalsIgnoreCase(TCConstants.ACTION_UGC_SINGLE_CHOOSE)) {
//
//        } else {
//            mType = TYPE_MULTI_CHOOSE;
//        }

        init();
        loadVideoList();
    }

    @Override
    protected void onDestroy() {
        mHandlerThread.getLooper().quit();
        mHandlerThread.quit();
        super.onDestroy();
    }

    private void loadVideoList() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    ArrayList<TCVideoFileInfo> fileInfoArrayList = mTCVideoEditerMgr.getAllVideo();

                    Message msg = new Message();
                    msg.obj = fileInfoArrayList;
                    mMainHandler.sendMessage(msg);
                }
            });
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults != null && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            loadVideoList();
        }
    }

    private void init() {
        mTvBack = (TextView) findViewById(R.id.btn_back);
        mTvBack.setOnClickListener(this);

        mBtnOk = (Button) findViewById(R.id.btn_ok);
        mBtnOk.setOnClickListener(this);

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 4));

        mAdapter = new TCVideoEditerListAdapter(this);
        mRecyclerView.setAdapter(mAdapter);

        if (mType == TYPE_SINGLE_CHOOSE) {
            mAdapter.setMultiplePick(false);
        } else {
            mAdapter.setMultiplePick(true);
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.btn_ok:
                doSelect();
                break;
            case R.id.btn_back:
                finish();
                break;
        }
    }

    private void doSelect() {
        if (mType == TYPE_SINGLE_CHOOSE) {
            Intent intent = new Intent(this, TCVideoEditerActivity.class);
            TCVideoFileInfo fileInfo = mAdapter.getSingleSelected();
            if (fileInfo == null) {
                TXLog.d(TAG, "select file null");
                return;
            }
            intent.putExtra(TCConstants.INTENT_KEY_SINGLE_CHOOSE, fileInfo);
            startActivity(intent);
        }
//        else {
//            Intent intent = new Intent(this, TCVideoJoinerActivity.class);
//            ArrayList<TCVideoFileInfo> videoFileInfos = mAdapter.getMultiSelected();
//            if (videoFileInfos == null || videoFileInfos.size() == 0) {
//                TXLog.d(TAG, "select file null");
//                return;
//            }
//            if (videoFileInfos.size() < 2) {
//                Toast.makeText(this, "必须选择两个以上视频文件", Toast.LENGTH_SHORT).show();
//                return;
//            }
//            intent.putExtra(TCConstants.INTENT_KEY_MULTI_CHOOSE, videoFileInfos);
//            startActivity(intent);
//        }
        finish();
    }
}

package com.duomizhibo.phonelive.videoeditor;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.tencent.rtmp.videoedit.TXVideoEditConstants;
import com.tencent.rtmp.videoedit.TXVideoEditer;
import com.duomizhibo.phonelive.R;

public class TCVideoEditView extends RelativeLayout implements RangeSlider.OnRangeChangeListener {

    public interface IOnRangeChangeListener {
        void onKeyDown();
        void onKeyUp(int startTime, int endTime);
    }

    private String TAG = TCVideoEditView.class.getSimpleName();

    private Context mContext;

    private TextView mTvTip;
    private RecyclerView mRecyclerView;
    private RangeSlider mRangeSlider;

    private long mVideoDuration;
    private long mVideoStartPos;
    private long mVideoEndPos;

    private TXVideoEditer mTXVideoEditer;
    private TCVideoEditerAdapter mAdapter;

    private IOnRangeChangeListener mRangeChangeListener;

    public TCVideoEditView(Context context) {
        super(context);

        init(context);
    }

    public TCVideoEditView(Context context, AttributeSet attrs) {
        super(context, attrs);

        init(context);
    }

    public TCVideoEditView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        init(context);
    }

    public void setRangeChangeListener(IOnRangeChangeListener listener) {
        mRangeChangeListener = listener;
    }

    private void init(Context context) {
        mContext = context;

        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.item_edit_view, this, true);

        mTvTip = (TextView) findViewById(R.id.tv_tip);

        mRangeSlider = (RangeSlider) findViewById(R.id.range_slider);
        mRangeSlider.setRangeChangeListener(this);

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        LinearLayoutManager manager = new LinearLayoutManager(mContext);
        manager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mRecyclerView.setLayoutManager(manager);

        mAdapter = new TCVideoEditerAdapter(mContext);
        mRecyclerView.setAdapter(mAdapter);
    }

    public int getSegmentFrom() {
        return (int) mVideoStartPos;
    }

    public int getSegmentTo() {
        return (int) mVideoEndPos;
    }

    public void setTXVideoEditer(TXVideoEditer TXVideoEditer) {
        mTXVideoEditer = TXVideoEditer;
    }

    public void setMediaFileInfo(TXVideoEditConstants.TXVideoInfo videoInfo) {
        if (videoInfo == null) {
            return;
        }
        mVideoDuration = videoInfo.duration;

        mVideoStartPos = 0;
        mVideoEndPos = mVideoDuration;
    }

    public void addBitmap(int index, Bitmap bitmap) {
        mAdapter.add(index, bitmap);
    }

    @Override
    public void onRangeChange(RangeSlider view, int type, int leftPinIndex, int rightPinIndex) {
//        int leftTime = (int) (mVideoDuration * leftPinIndex / 100); //ms
//        int rightTime = (int) (mVideoDuration * rightPinIndex / 100);
//
//        if (type == RangeSlider.TYPE_LEFT) {
//            mTXVideoEditer.previewAtTime(leftTime);
//            mVideoStartPos = leftTime;
//        } else {
//            mTXVideoEditer.previewAtTime(rightTime);
//            mVideoEndPos = rightTime;
//        }
//        if (mRangeChangeListener != null) {
//            mRangeChangeListener.onRangeChange((int)mVideoStartPos, (int)mVideoEndPos);
//        }
    }

    @Override
    public void onKeyDown(int type) {
        if (mRangeChangeListener != null) {
            mRangeChangeListener.onKeyDown();
        }
    }

    @Override
    public void onKeyUp(int type, int leftPinIndex, int rightPinIndex) {
        int leftTime = (int) (mVideoDuration * leftPinIndex / 100); //ms
        int rightTime = (int) (mVideoDuration * rightPinIndex / 100);

        if (type == RangeSlider.TYPE_LEFT) {
            mVideoStartPos = leftTime;
        } else {
            mVideoEndPos = rightTime;
        }
        if (mRangeChangeListener != null) {
            mRangeChangeListener.onKeyUp((int)mVideoStartPos, (int)mVideoEndPos);
        }
        mTvTip.setText(String.format("左侧 : %s, 右侧 : %s ", TCUtils.duration(leftTime), TCUtils.duration(rightTime)));
    }

}

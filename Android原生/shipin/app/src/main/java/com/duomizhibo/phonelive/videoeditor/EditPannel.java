package com.duomizhibo.phonelive.videoeditor;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.tencent.rtmp.videoedit.TXVideoEditConstants;
import com.duomizhibo.phonelive.R;

import java.util.ArrayList;

public class EditPannel extends LinearLayout {

    public static final int CMD_SPEED = 1;
    public static final int CMD_FILTER = 2;
    public static class EditParams {
        public int mSpeedRate;
        public Bitmap mFilterBmp;
    }

    public interface IOnEditCmdListener {
        void onCmd(int cmd, EditParams params);
    }
    private LinearLayout mCutLL;
    private LinearLayout mFilterLL;
    private TCVideoEditView mEditView;
    private TCHorizontalScrollView mFilterSV;
    private ImageView mCutBtn;
    private ImageView mFilterBtn;
  //  private CheckBox mSpeedCB;
    private ArrayAdapter<Integer> mFilterAdapter;
    private IOnEditCmdListener mCmdListener;
    private Context mContext;

    public EditPannel(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        init(context);
    }

    private void init(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.edit_pannel, this);
        mCutLL = (LinearLayout) view.findViewById(R.id.cut_ll);
        mFilterLL = (LinearLayout) view.findViewById(R.id.filter_ll);
        mEditView = (TCVideoEditView) view.findViewById(R.id.timelineItem);
        mFilterSV = (TCHorizontalScrollView) view.findViewById(R.id.filter_sv);
        mCutBtn = (ImageView) view.findViewById(R.id.btn_cut);
        mFilterBtn = (ImageView) view.findViewById(R.id.btn_filter);
        //mSpeedCB = (CheckBox) view.findViewById(R.id.cb_speed);

        mCutLL.setVisibility(VISIBLE);
        mFilterLL.setVisibility(GONE);

        mCutBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mCutLL.setVisibility(VISIBLE);
                mFilterLL.setVisibility(GONE);

                mCutBtn.setImageResource(R.drawable.ic_cut_press);
                mFilterBtn.setImageResource(R.drawable.ic_beautiful);
            }
        });
        mFilterBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mCutLL.setVisibility(GONE);
                mFilterLL.setVisibility(VISIBLE);

                mCutBtn.setImageResource(R.drawable.ic_cut);
                mFilterBtn.setImageResource(R.drawable.ic_beautiful_press);
            }
        });

//        mSpeedCB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                mSpeedCB.setSelected(isChecked);
//                if (mCmdListener != null) {
//                    EditParams params = new EditParams();
//                    params.mSpeedRate = isChecked ? 2 : 1;
//                    mCmdListener.onCmd(CMD_SPEED, params);
//                }
//            }
//        });

        final ArrayList<Integer> filterIDList = new ArrayList<Integer>();
        filterIDList.add(R.drawable.orginal);
        filterIDList.add(R.drawable.langman);
        filterIDList.add(R.drawable.qingxin);
        filterIDList.add(R.drawable.weimei);
        filterIDList.add(R.drawable.fennen);
        filterIDList.add(R.drawable.huaijiu);
        filterIDList.add(R.drawable.landiao);
        filterIDList.add(R.drawable.qingliang);
        filterIDList.add(R.drawable.rixi);
        mFilterAdapter = new ArrayAdapter<Integer>(context,0, filterIDList){

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                if (convertView == null) {
                    LayoutInflater inflater = LayoutInflater.from(getContext());
                    convertView = inflater.inflate(R.layout.filter_layout,null);
                }
                ImageView view = (ImageView) convertView.findViewById(R.id.filter_image);
                if (position == 0) {
                    ImageView view_tint = (ImageView) convertView.findViewById(R.id.filter_image_tint);
                    if (view_tint != null)
                        view_tint.setVisibility(View.VISIBLE);
                }
                view.setTag(position);
                view.setImageDrawable(getResources().getDrawable(getItem(position)));
                view.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int index = (int) view.getTag();
                        selectFilter(index);
                        if (mCmdListener != null) {
                            Bitmap bmp = getFilterBitmap(index);
                            EditParams params = new EditParams();
                            params.mFilterBmp = bmp;
                            mCmdListener.onCmd(CMD_FILTER, params);
                        }
                    }
                });
                return convertView;

            }
        };
        mFilterSV.setAdapter(mFilterAdapter);
    }

    public void setEditCmdListener(IOnEditCmdListener listener) {
        mCmdListener = listener;
    }

    public void setRangeChangeListener(TCVideoEditView.IOnRangeChangeListener listener) {
        mEditView.setRangeChangeListener(listener);
    }

    public int getSegmentFrom() {
        return mEditView.getSegmentFrom();
    }

    public int getSegmentTo() {
        return mEditView.getSegmentTo();
    }

    public void addBitmap(int index, Bitmap bitmap) {
        mEditView.addBitmap(index, bitmap);
    }

    public void setMediaFileInfo(TXVideoEditConstants.TXVideoInfo videoInfo) {
        mEditView.setMediaFileInfo(videoInfo);
    }

    private void selectFilter(int index) {
        ViewGroup group = (ViewGroup)mFilterSV.getChildAt(0);
        for (int i = 0; i < mFilterAdapter.getCount(); i++) {
            View v = group.getChildAt(i);
            ImageView IVTint = (ImageView) v.findViewById(R.id.filter_image_tint);
            if (IVTint != null) {
                if (i == index) {
                    IVTint.setVisibility(View.VISIBLE);
                } else {
                    IVTint.setVisibility(View.INVISIBLE);
                }
            }
        }
    }

    private static Bitmap decodeResource(Resources resources, int id) {
        TypedValue value = new TypedValue();
        resources.openRawResource(id, value);
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inTargetDensity = value.density;
        return BitmapFactory.decodeResource(resources, id, opts);
    }

    private Bitmap getFilterBitmap(int index) {
        Bitmap bmp = null;
        switch (index) {
            case 1:
                bmp = decodeResource(getResources(), R.drawable.filter_langman);
                break;
            case 2:
                bmp = decodeResource(getResources(), R.drawable.filter_qingxin);
                break;
            case 3:
                bmp = decodeResource(getResources(), R.drawable.filter_weimei);
                break;
            case 4:
                bmp = decodeResource(getResources(), R.drawable.filter_fennen);
                break;
            case 5:
                bmp = decodeResource(getResources(), R.drawable.filter_huaijiu);
                break;
            case 6:
                bmp = decodeResource(getResources(), R.drawable.filter_landiao);
                break;
            case 7:
                bmp = decodeResource(getResources(), R.drawable.filter_qingliang);
                break;
            case 8:
                bmp = decodeResource(getResources(), R.drawable.filter_rixi);
                break;
            default:
                bmp = null;
                break;
        }
        return bmp;
    }
}

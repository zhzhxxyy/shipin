package com.duomizhibo.phonelive.fragment;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.duomizhibo.phonelive.AppConfig;
import com.duomizhibo.phonelive.AppContext;
import com.duomizhibo.phonelive.R;
import com.duomizhibo.phonelive.api.remote.ApiUtils;
import com.duomizhibo.phonelive.api.remote.PhoneLiveApi;
import com.duomizhibo.phonelive.ui.ReadyStartLiveActivity;
import com.duomizhibo.phonelive.widget.AvatarView;
import com.duomizhibo.phonelive.widget.BlackButton;
import com.duomizhibo.phonelive.widget.BlackTextView;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import jp.wasabeef.glide.transformations.BlurTransformation;
import okhttp3.Call;

/**
 * 主播结束 直播 弹窗
 */
public class LiveEmceeEndFragmentDialog extends DialogFragment {

    @InjectView(R.id.rl_livestop)
    protected RelativeLayout mLayoutLiveStop;

    @InjectView(R.id.iv_bg)
    protected ImageView mIvBg;

    //结束直播收获魅力值数量
    @InjectView(R.id.tv_live_end_yp_num)
    BlackTextView mTvLiveEndYpNum;

    @InjectView(R.id.tv_live_duration)
    BlackTextView mTvLiveDuration;

    //直播结束房间人数
    @InjectView(R.id.tv_live_end_num)
    BlackTextView mTvLiveEndUserNum;

    @InjectView(R.id.av_user_head)
    AvatarView mIvUserHead;

    @InjectView(R.id.tv_user_nick)
    TextView mTvUserNick;

    @InjectView(R.id.btn_back_index)
    BlackButton mBtnBackIndex;

    @InjectView(R.id.tv_live_end_yp_name)
    TextView mTvEndYpName;


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_live_emcee_end_dialog,null);
        Dialog dialog = new Dialog(getActivity(),R.style.dialog_test);
        ButterKnife.inject(this,view);
        dialog.setContentView(view);

        initView(view);

        initData();
        return dialog;
    }

    private void initData() {


        String stream = getArguments().getString("stream");

        PhoneLiveApi.getLiveEndInfo(stream,new StringCallback(){

            @Override
            public void onError(Call call, Exception e, int id) {

            }

            @Override
            public void onResponse(String response, int id) {
                JSONArray res = ApiUtils.checkIsSuccess(response);
                if(res != null){
                    try {
                        JSONObject data = res.getJSONObject(0);
                        mTvLiveDuration.setText(data.getString("length"));
                        mTvLiveEndUserNum.setText(data.getString("nums"));
                        mTvLiveEndYpNum.setText(data.getString("votes"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

            }
        });


        mIvUserHead.setAvatarUrl(AppContext.getInstance().getLoginUser().avatar_thumb);
        mTvUserNick.setText(AppContext.getInstance().getLoginUser().user_nicename);
        Glide.with(this).load(AppContext.getInstance().getLoginUser().avatar_thumb).bitmapTransform(new BlurTransformation(getActivity(), 25)).into(mIvBg);

    }

    private void initView(View view) {
        mBtnBackIndex.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().finish();
                dismiss();
            }
        });

        mTvEndYpName.setText("收获" + AppConfig.TICK_NAME);
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        OkHttpUtils.getInstance().cancelTag("getLiveEndInfo");
        if(isAdded()){
            getActivity().finish();
        }
    }
}

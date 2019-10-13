package com.duomizhibo.phonelive.fragment;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.duomizhibo.phonelive.api.remote.ApiUtils;
import com.duomizhibo.phonelive.AppContext;
import com.duomizhibo.phonelive.R;
import com.duomizhibo.phonelive.api.remote.PhoneLiveApi;
import com.duomizhibo.phonelive.ui.VideoPlayerActivity;
import com.duomizhibo.phonelive.widget.BlackButton;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;

import butterknife.ButterKnife;
import butterknife.InjectView;
import jp.wasabeef.glide.transformations.BlurTransformation;
import okhttp3.Call;

/**
 * Created by weipeng on 16/8/18.
 */
public class LiveEndFragmentDialog extends DialogFragment {

    //直播结束关注btn
    @InjectView(R.id.btn_follow)
    BlackButton mFollowEmcee;
    @InjectView(R.id.btn_back_index)
    BlackButton mBtnBackIndex;
    private String roomnum;

    @InjectView(R.id.iv_bg)
    protected ImageView mIvBg;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_live_end_dialog,null);
        Dialog dialog = new Dialog(getActivity(),R.style.dialog_test);
        ButterKnife.inject(this,view);
        dialog.setContentView(view);

        initView(view);

        initData();

        return dialog;
    }

    private void initData() {
        roomnum = getArguments().getString("roomnum");

        showEndIsFollowEmcee();

        Glide.with(this).load(((VideoPlayerActivity)getActivity()).mEmceeInfo.avatar_thumb).bitmapTransform(new BlurTransformation(getActivity(), 25)).crossFade(1000).into(mIvBg);

    }

    private void initView(View view) {
        mFollowEmcee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showFollow(AppContext.getInstance().getLoginUid(), String.valueOf(roomnum));
            }
        });
        mBtnBackIndex.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().finish();
                dismiss();
            }
        });

    }
    /**
     * @dw 关注
     * @param uid 当前用户id
     * @param touid 关注用户id
     * */
    private void showFollow(String uid,String touid){

        PhoneLiveApi.showFollow(uid, touid, AppContext.getInstance().getToken(), new PhoneLiveApi.AttentionCallback() {
            @Override
            public void callback(boolean isAttention) {
                if(isAttention){
                    mFollowEmcee.setText(getResources().getString(R.string.alreadyfollow));
                }else{
                    mFollowEmcee.setText(getResources().getString(R.string.follow));
                }
            }
        });
    }

    //直播结束判断当前主播是否关注过
    private void showEndIsFollowEmcee() {

        //判断当前主播是否已经关注
        PhoneLiveApi.getIsFollow(AppContext.getInstance().getLoginUid(),roomnum,new StringCallback() {
            @Override
            public void onError(Call call, Exception e,int id) {

            }

            @Override
            public void onResponse(String response,int id) {
                JSONArray res = ApiUtils.checkIsSuccess(response);//0：未关注1:关注
                if(res != null && isAdded()){
                    try {
                        if(res.getJSONObject(0).getInt("isattent") == 0){

                            mFollowEmcee.setText(getString(R.string.follow));
                        }else{

                            mFollowEmcee.setText(getString(R.string.alreadyfollow));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        OkHttpUtils.getInstance().cancelTag("getIsFollow");
        if(isAdded()){
            getActivity().finish();
        }
    }
}

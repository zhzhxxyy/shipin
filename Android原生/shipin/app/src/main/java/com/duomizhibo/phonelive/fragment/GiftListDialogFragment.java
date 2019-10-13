package com.duomizhibo.phonelive.fragment;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.tandong.bottomview.view.BottomView;
import com.duomizhibo.phonelive.AppContext;
import com.duomizhibo.phonelive.R;
import com.duomizhibo.phonelive.adapter.GridViewAdapter;
import com.duomizhibo.phonelive.adapter.ViewPageGridViewAdapter;
import com.duomizhibo.phonelive.api.remote.ApiUtils;
import com.duomizhibo.phonelive.api.remote.PhoneLiveApi;
import com.duomizhibo.phonelive.bean.GiftBean;
import com.duomizhibo.phonelive.bean.UserBean;
import com.duomizhibo.phonelive.event.Event;
import com.duomizhibo.phonelive.ui.VideoPlayerActivity;
import com.duomizhibo.phonelive.ui.other.ChatServer;
import com.duomizhibo.phonelive.utils.UIHelper;
import com.duomizhibo.phonelive.widget.BlackButton;
import com.duomizhibo.phonelive.widget.BlackTextView;
import com.zhy.http.okhttp.callback.StringCallback;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;

/**
 * @dw 直播间礼物列表
 */

public class GiftListDialogFragment extends DialogFragment {


    private RelativeLayout mSendGiftLian;

    private BlackTextView mUserCoin;

    //赠送礼物按钮
    private BlackButton mSendGiftBtn;

    private List<GiftBean> mGiftList = new ArrayList<>();

    private ViewPageGridViewAdapter mVpGiftAdapter;

    //礼物view
    private ViewPager mVpGiftView;

    //礼物服务端返回数据
    private JSONArray mGiftResStr;

    //当前选中的礼物
    private GiftBean mSelectedGiftItem;

    private int mShowGiftSendOutTime = 5;

    protected List<GridView> mGiftViews = new ArrayList<>();

    private UserBean mUser;

    private Gson mGson = new Gson();

    private Handler mHandler = new Handler();


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        mUser = AppContext.getInstance().getLoginUser();

        Dialog dialog = new Dialog(getActivity(),R.style.dialog_gift);
        dialog.setContentView(R.layout.view_show_viewpager);
        Window window = dialog.getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        params.gravity = Gravity.BOTTOM;
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        window.setAttributes(params);
        initView(dialog);
        initData();
        return dialog;
    }

    private void initData() {

//        String data = AppContext.getInstance().getProperty("gift_data");
//        if(data != null){
//
//            try {
//                mGiftResStr = new JSONArray(data);
//                fillGift();
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//
//            return;
//        }
        //获取礼物列表
        PhoneLiveApi.getGiftList(mUser.id,mUser.token,new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                AppContext.showToastAppMsg(getActivity(), "获取礼物信息失败");
            }

            @Override
            public void onResponse(String s,int id) {
                JSONArray res = ApiUtils.checkIsSuccess(s);
                if(res != null && isAdded()){
                    try {
                        JSONObject data = res.getJSONObject(0);
                        mGiftResStr = data.getJSONArray("giftlist");
                        mUser.coin  = data.getString("coin");
                        mUserCoin.setText(mUser.coin);
                        //缓存数据
                        //AppContext.getInstance().setProperty("gift_data_temp",data.getString("giftlist"));

                        fillGift();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

            }
        });
    }

    /**
     * @param isOutTime 是否连送超时(如果是连送礼物的情况下)
     * @dw 赠送礼物, 请求服务端获取数据扣费
     */
    private void doSendGift(final String isOutTime) {
        if (mSelectedGiftItem != null) {
            if (mSelectedGiftItem.getType() == 1) {
                mSendGiftLian.setVisibility(View.VISIBLE);
            } else {
                changeSendGiftBtnStatue(true);
            }

            PhoneLiveApi.sendGift(mUser, mSelectedGiftItem, ((VideoPlayerActivity)getActivity()).mEmceeInfo.uid,
                    ((VideoPlayerActivity)getActivity()).mEmceeInfo.stream,new StringCallback() {
                @Override
                public void onError(Call call, Exception e,int id) {
                    AppContext.showToastAppMsg(getActivity(), getString(R.string.senderror));
                }

                @Override
                public void onResponse(String response,int id) {
                    JSONArray s = ApiUtils.checkIsSuccess(response);
                    if (s != null) {
                        try {
                            ((TextView) mSendGiftLian.findViewById(R.id.tv_show_gift_outtime)).setText(String.valueOf(mShowGiftSendOutTime));
                            JSONObject tokenJson = s.getJSONObject(0);
                            //获取剩余金额,重新赋值
                            mUser.coin = tokenJson.getString("coin");
                            mUserCoin.setText(mUser.coin);//重置余额
                            mUser.level = tokenJson.getString("level");
                            AppContext.getInstance().saveUserInfo(mUser);

                            Event.VideoEvent event = new Event.VideoEvent();
                            event.action = 0;
                            event.data = new String[2];
                            event.data[0] = tokenJson.getString("gifttoken");
                            event.data[1] = isOutTime;
                            EventBus.getDefault().post(event);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
        }
    }
    /**
     * @param statue 开启or关闭
     * @dw 赠送礼物按钮状态修改
     */
    private void changeSendGiftBtnStatue(boolean statue) {
        if (statue) {
            mSendGiftBtn.setBackgroundColor(getResources().getColor(R.color.global));
            mSendGiftBtn.setEnabled(true);
        } else {
            mSendGiftBtn.setBackgroundColor(getResources().getColor(R.color.light_gray2));
            mSendGiftBtn.setEnabled(false);
        }
    }

    //连送按钮隐藏
    private void recoverySendGiftBtnLayout() {
        ((TextView) mSendGiftLian.findViewById(R.id.tv_show_gift_outtime)).setText("");
        mSendGiftLian.setVisibility(View.GONE);
        mSendGiftBtn.setVisibility(View.VISIBLE);
        mShowGiftSendOutTime = 5;
    }

    //展示礼物列表
    private void initView(Dialog dialog) {

        mUserCoin = (BlackTextView) dialog.findViewById(R.id.tv_show_select_user_coin);
        //点击底部跳转充值页面
        dialog.findViewById(R.id.rl_show_gift_bottom).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UIHelper.showMyDiamonds(getActivity());
            }
        });
        mVpGiftView = (ViewPager) dialog.findViewById(R.id.vp_gift_page);
        mSendGiftLian = (RelativeLayout) dialog.findViewById(R.id.iv_show_send_gift_lian);
        mSendGiftLian.bringToFront();
        mSendGiftLian.setOnClickListener(new View.OnClickListener() {//礼物连送
            @Override
            public void onClick(View v) {
                doSendGift("y");//礼物发送
                mShowGiftSendOutTime = 5;
                ((TextView) mSendGiftLian.findViewById(R.id.tv_show_gift_outtime)).setText(String.valueOf(mShowGiftSendOutTime));
            }
        });
        mSendGiftBtn = (BlackButton) dialog.findViewById(R.id.btn_show_send_gift);
        mSendGiftBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickSendGift(v);
            }
        });
        if (mSelectedGiftItem != null) {
            mSendGiftBtn.setBackgroundColor(getResources().getColor(R.color.global));
        }

    }
    //赠送礼物单项被选中
    private void giftItemClick(AdapterView<?> parent, View view, int position) {


        if ((GiftBean) parent.getItemAtPosition(position) != mSelectedGiftItem) {
            recoverySendGiftBtnLayout();
            mSelectedGiftItem = (GiftBean) parent.getItemAtPosition(position);
            //点击其他礼物
            changeSendGiftBtnStatue(true);
            for (int i = 0; i < mGiftViews.size(); i++) {
                for (int j = 0; j < mGiftViews.get(i).getChildCount(); j++) {
                    if (((GiftBean) mGiftViews.get(i).getItemAtPosition(j)).getType() == 1) {
                        mGiftViews.get(i).getChildAt(j).findViewById(R.id.iv_show_gift_selected).setBackgroundResource(R.drawable.icon_continue_gift);
                    } else {
                        mGiftViews.get(i).getChildAt(j).findViewById(R.id.iv_show_gift_selected).setBackgroundResource(0);
                    }

                }
            }
            view.findViewById(R.id.iv_show_gift_selected).setBackgroundResource(R.drawable.icon_continue_gift_chosen);

        } else {
            if (((GiftBean) parent.getItemAtPosition(position)).getType() == 1) {
                view.findViewById(R.id.iv_show_gift_selected).setBackgroundResource(R.drawable.icon_continue_gift);
            } else {
                view.findViewById(R.id.iv_show_gift_selected).setBackgroundResource(0);
            }
            mSelectedGiftItem = null;
            changeSendGiftBtnStatue(false);

        }
    }
    /**
     * @param v btn
     * @dw 点击赠送礼物按钮
     */
    private void onClickSendGift(View v) {//赠送礼物
        if (!((VideoPlayerActivity)getActivity()).mConnectionState) {//没有连接ok
            return;
        }
        if ((mSelectedGiftItem != null) && (mSelectedGiftItem.getType() == 1)) {//连送礼物
            v.setVisibility(View.GONE);
            if (mHandler == null) return;
            mHandler.postDelayed(new Runnable() {//开启连送定时器
                @Override
                public void run() {
                    if (mShowGiftSendOutTime == 1) {
                        recoverySendGiftBtnLayout();
                        mHandler.removeCallbacks(this);
                        return;
                    }
                    mHandler.postDelayed(this, 1000);
                    mShowGiftSendOutTime --;
                    ((TextView) mSendGiftLian.findViewById(R.id.tv_show_gift_outtime)).setText(String.valueOf(mShowGiftSendOutTime));

                }
            }, 1000);
            doSendGift("y");//礼物发送
        } else {
            doSendGift("n");//礼物发送
        }
    }

    //礼物列表填充
    private void fillGift() {

        if (null == mVpGiftAdapter && null != mGiftResStr) {

            if (mGiftList.size() == 0) {
                try {
                    JSONArray giftListJson = mGiftResStr;
                    for (int i = 0; i < giftListJson.length(); i++) {

                        mGiftList.add(mGson.fromJson(giftListJson.getString(i), GiftBean.class));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            //礼物item填充
            List<View> mGiftViewList = new ArrayList<>();

            int index = 0;

            int giftsPageSize;

            if(mGiftList.size()%8 == 0)
            {
                giftsPageSize = mGiftList.size()/8;
            }else{
                giftsPageSize = (int)(mGiftList.size()/8)+1;
            }

            for (int i = 0; i < giftsPageSize; i++) {
                View v = LayoutInflater.from(getActivity()).inflate(R.layout.view_show_gifts_gv, null);
                mGiftViewList.add(v);
                List<GiftBean> l = new ArrayList<>();
                for (int j = 0; j < 8; j++) {
                    if (index >= mGiftList.size()) {
                        break;
                    }
                    l.add(mGiftList.get(index));
                    index++;
                }
                mGiftViews.add((GridView) v.findViewById(R.id.gv_gift_list));
                mGiftViews.get(i).setAdapter(new GridViewAdapter(l));
                mGiftViews.get(i).setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        giftItemClick(parent, view, position);
                    }
                });
            }
            mVpGiftAdapter = new ViewPageGridViewAdapter(mGiftViewList);
        }
        mVpGiftView.setAdapter(mVpGiftAdapter);
    }
}

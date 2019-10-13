package com.duomizhibo.phonelive.fragment;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.duomizhibo.phonelive.AppConfig;
import com.duomizhibo.phonelive.AppContext;
import com.duomizhibo.phonelive.AppManager;
import com.duomizhibo.phonelive.R;
import com.duomizhibo.phonelive.api.remote.ApiUtils;
import com.duomizhibo.phonelive.api.remote.PhoneLiveApi;
import com.duomizhibo.phonelive.base.BaseFragment;
import com.duomizhibo.phonelive.bean.UserBean;
import com.duomizhibo.phonelive.event.CoinNameEvent;
import com.duomizhibo.phonelive.interf.ListenMessage;
import com.duomizhibo.phonelive.ui.SmallVideoPlayerActivity;
import com.duomizhibo.phonelive.ui.customviews.ActivityTitle;
import com.duomizhibo.phonelive.ui.customviews.LineControllerView;
import com.duomizhibo.phonelive.ui.other.PhoneLivePrivateChat;
import com.duomizhibo.phonelive.utils.LiveUtils;
import com.duomizhibo.phonelive.utils.LoginUtils;
import com.duomizhibo.phonelive.utils.SharedPreUtil;
import com.duomizhibo.phonelive.utils.StringUtils;
import com.duomizhibo.phonelive.utils.TLog;
import com.duomizhibo.phonelive.utils.UIHelper;
import com.duomizhibo.phonelive.widget.AvatarView;
import com.duomizhibo.phonelive.widget.BlackTextView;
import com.zhy.http.okhttp.callback.StringCallback;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import okhttp3.Call;

/**
 * 登录用户中心页面
 */
public class UserInformationFragment extends BaseFragment {

    //头像
    @InjectView(R.id.iv_avatar)
    AvatarView mIvAvatar;
    //昵称
    @InjectView(R.id.tv_name)
    TextView mTvName;

    @InjectView(R.id.ll_user_container)
    View mUserContainer;

    //退出登陆
    @InjectView(R.id.ll_loginout)
    LinearLayout mLoginOut;

    //直播记录
    @InjectView(R.id.tv_info_u_live_num)
    TextView mLiveNum;

    //关注
    @InjectView(R.id.tv_info_u_follow_num)
    TextView mFollowNum;

    //粉丝
    @InjectView(R.id.tv_info_u_fans_num)
    TextView mFansNum;

    //id
    @InjectView(R.id.tv_id)
    TextView mUId;

    @InjectView(R.id.iv_sex)
    ImageView mIvSex;

    @InjectView(R.id.iv_level)
    ImageView mIvLevel;

    @InjectView(R.id.iv_anchorlevel)
    ImageView mIvAnchorLevel;

    @InjectView(R.id.ll_profit)
    LineControllerView mLcProfit;

    @InjectView(R.id.ll_family)
    LineControllerView mLlFamily;

    @InjectView(R.id.ll_familymanage)
    LineControllerView mLlFamilyManage;

    @InjectView(R.id.ll_distribution)
    LineControllerView mLlDistribution;

    private UserBean mInfo;

    private LineControllerView mCoinName;


    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_information,
                container, false);
        ButterKnife.inject(this, view);
        initView(view);
        initData();
        return view;
    }

    @Override
    public void onStart() {

        super.onStart();
        mInfo = AppContext.getInstance().getLoginUser();
        fillUI();


    }

    @Override
    public void initData() {

    }

    @Override
    public void initView(View view) {
        view.findViewById(R.id.ll_live).setOnClickListener(this);
        view.findViewById(R.id.ll_following).setOnClickListener(this);
        view.findViewById(R.id.ll_fans).setOnClickListener(this);
        view.findViewById(R.id.ll_profit).setOnClickListener(this);
        view.findViewById(R.id.ll_setting).setOnClickListener(this);
        view.findViewById(R.id.ll_level).setOnClickListener(this);
        mCoinName=(LineControllerView) view.findViewById(R.id.ll_diamonds);
        mCoinName.setOnClickListener(this);
        view.findViewById(R.id.ll_about).setOnClickListener(this);
        view.findViewById(R.id.ll_authenticate).setOnClickListener(this);
        view.findViewById(R.id.tv_edit_info).setOnClickListener(this);
        view.findViewById(R.id.ll_shopthings).setOnClickListener(this);
        view.findViewById(R.id.ll_shop).setOnClickListener(this);
        view.findViewById(R.id.ll_distribution).setOnClickListener(this);
        view.findViewById(R.id.ll_family).setOnClickListener(this);
        view.findViewById(R.id.ll_familymanage).setOnClickListener(this);
        view.findViewById(R.id.ll_details).setOnClickListener(this);
        view.findViewById(R.id.ll_video).setOnClickListener(this);
        mLoginOut.setOnClickListener(this);

        EventBus.getDefault().register(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onCoinNameEvent(CoinNameEvent e){
        mCoinName.setName("我的" + e.getCoinName());
    }

    private void fillUI() {
        if (mInfo == null)
            return;

        mIvAvatar.setAvatarUrl(mInfo.avatar);
        //昵称
        mTvName.setText(mInfo.user_nicename);


        mIvSex.setImageResource(LiveUtils.getSexRes(mInfo.sex));
        mIvLevel.setImageResource(LiveUtils.getLevelRes(mInfo.level));
        mIvAnchorLevel.setImageResource(LiveUtils.getAnchorLevelRes(mInfo.level_anchor));
    }

    protected void requestData(boolean refresh) {
        if (AppContext.getInstance().isLogin()) {

            sendRequestData();
        }

    }

    private void sendRequestData() {

        PhoneLiveApi.getMyUserInfo(AppContext.getInstance().getLoginUid(),
                AppContext.getInstance().getToken(), stringCallback);
    }

    private StringCallback stringCallback = new StringCallback() {
        @Override
        public void onError(Call call, Exception e, int id) {

        }

        @Override
        public void onResponse(String s, int id) {
            JSONArray res = ApiUtils.checkIsSuccess(s);
            if (res != null) {
                try {
                    JSONObject object = res.getJSONObject(0);
                    mInfo = new Gson().fromJson(object.toString(), UserBean.class);
                    AppContext.getInstance().updateUserInfo(mInfo);

                    mLiveNum.setText(object.getString("lives"));
                    mFollowNum.setText(object.getString("follows"));
                    mFansNum.setText(object.getString("fans"));
                    int goodnum = StringUtils.toInt(object.getJSONObject("liang").getString("name"));
                    if (goodnum != 0) {
                        mUId.setText("靓:" + goodnum);
                    } else {
                        mUId.setText("ID:" + mInfo.id);
                    }
                    if (object.getString("agent_switch").equals("1")){
                        mLlDistribution.setVisibility(View.VISIBLE);
                    } if (object.getString("family_switch").equals("1")){
                        mLlFamily.setVisibility(View.VISIBLE);
                        mLlFamilyManage.setVisibility(View.VISIBLE);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }


        }
    };

    @Override
    public void onClick(View v) {

        final int id = v.getId();
        switch (id) {

            case R.id.ll_authenticate://申请认证

                UIHelper.showWebView(getActivity(),
                        AppConfig.MAIN_URL + "/index.php?g=Appapi&m=auth&a=index&uid=" +
                                mInfo.id + "&token=" + AppContext.getInstance().getToken(), "");
                break;
            case R.id.iv_avatar:
                break;
            case R.id.ll_live:
                UIHelper.showLiveRecordActivity(getActivity(), mInfo.id);
                break;
            case R.id.ll_following:
                UIHelper.showAttentionActivity(getActivity(), mInfo.id);
                break;
            case R.id.ll_fans:
                UIHelper.showFansActivity(getActivity(), mInfo.id);
                break;
            case R.id.ll_setting:
                UIHelper.showSetting(getActivity());
                break;
            case R.id.ll_diamonds:
                //我的钻石
                UIHelper.showMyDiamonds(getActivity());
                break;
            case R.id.ll_level:
                //我的等级
                UIHelper.showWebView(getContext(), AppConfig.MAIN_URL + "/index.php?g=Appapi&m=level&a=index&uid="+mInfo.id + "&token=" + AppContext.getInstance().getToken(), "");
                break;

            //退出登录
            case R.id.ll_loginout:
                LoginUtils.outLogin(getActivity());
                getActivity().finish();
                break;

            case R.id.ll_profit:
                //收益
                UIHelper.showProfitActivity(getActivity());
                break;
            //编辑资料
            case R.id.tv_edit_info:
                UIHelper.showMyInfoDetailActivity(getContext());
                break;
            case R.id.ll_about:
                UIHelper.showWebView(getContext(), AppConfig.MAIN_URL + "/index.php?g=portal&m=page&a=lists", "");
                break;
            case R.id.ll_shop:
                UIHelper.shoShopActivity(getContext());
                break;
            case R.id.ll_shopthings:
                UIHelper.shoShopThingsActivity(getContext());
                break;
            case R.id.ll_family:
                UIHelper.showWebView(getContext(), AppConfig.MAIN_URL + "/index.php?g=Appapi&m=Family&a=index2&uid=" + mInfo.id + "&token=" + AppContext.getInstance().getToken(), "");
                break;
            case R.id.ll_familymanage:
                UIHelper.showWebView(getContext(), AppConfig.MAIN_URL + "/index.php?g=Appapi&m=Family&a=home&uid=" + mInfo.id + "&token=" + AppContext.getInstance().getToken(), "");
                break;
            case R.id.ll_distribution:
                UIHelper.showWebView(getContext(), AppConfig.MAIN_URL + "/index.php?g=Appapi&m=Agent&a=index&uid=" + mInfo.id + "&token=" + AppContext.getInstance().getToken(), "");
                break;
            case R.id.ll_details:
                UIHelper.showWebView(getContext(), AppConfig.MAIN_URL + "/index.php?g=Appapi&m=Detail&a=index&uid=" + mInfo.id + "&token=" + AppContext.getInstance().getToken(), "");
                break;
            case R.id.ll_video:
               UIHelper.shoMyVideoActivity(getContext());
            default:
                break;
        }
    }


    @Override
    public void onResume() {
        super.onResume();

        sendRequestData();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}

package com.duomizhibo.phonelive.fragment;

import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;
import com.tandong.bottomview.view.BottomView;
import com.duomizhibo.phonelive.AppContext;
import com.duomizhibo.phonelive.R;
import com.duomizhibo.phonelive.api.remote.ApiUtils;
import com.duomizhibo.phonelive.api.remote.PhoneLiveApi;
import com.duomizhibo.phonelive.base.AbsDialogFragment;
import com.duomizhibo.phonelive.base.AbsFragment;
import com.duomizhibo.phonelive.base.ShowLiveActivityBase;
import com.duomizhibo.phonelive.bean.PrivateChatUserBean;
import com.duomizhibo.phonelive.bean.SimpleUserInfo;
import com.duomizhibo.phonelive.bean.UserAlertInfoBean;
import com.duomizhibo.phonelive.bean.UserBean;
import com.duomizhibo.phonelive.event.Event;
import com.duomizhibo.phonelive.ui.other.DrawableRes;
import com.duomizhibo.phonelive.ui.other.ChatServer;
import com.duomizhibo.phonelive.utils.LiveUtils;
import com.duomizhibo.phonelive.utils.SimpleUtils;
import com.duomizhibo.phonelive.utils.StringUtils;
import com.duomizhibo.phonelive.utils.UIHelper;
import com.duomizhibo.phonelive.widget.AvatarView;
import com.duomizhibo.phonelive.widget.BlackTextView;
import com.duomizhibo.phonelive.ui.other.BottomMenuView;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import okhttp3.Call;
import rx.Observable;
import rx.Subscriber;
import rx.functions.Action1;

/**
 * 直播间用户列表点击弹窗页面
 */
public class UserInfoDialogFragment extends AbsDialogFragment {

    private UserBean mUser;
    private SimpleUserInfo mToUser;
    private String mRoomNum;
    private ChatServer mChatServer;

    @InjectView(R.id.tv_show_dialog_u_fllow_num)
    TextView mTvFollowNum;

    @InjectView(R.id.tv_show_dialog_u_send_num)
    TextView mTvSendNum;

    @InjectView(R.id.tv_show_dialog_u_fans_num)
    TextView mTvFansNum;

    @InjectView(R.id.tv_show_dialog_u_ticket)
    TextView mTvTicketNum;

    @InjectView(R.id.tv_show_dialog_u_follow_btn)
    TextView mTvFollowBtn;

    @InjectView(R.id.tv_live_manage_or_report)
    TextView mTvReportBtn;

    @InjectView(R.id.iv_dialog_setting)
    ImageView mIbSheZhi;

    @InjectView(R.id.tv_user_info_id)
    TextView mTvId;

    //只有主页的菜单
    @InjectView(R.id.ll_user_info_dialog2)
    LinearLayout mLLUserInfoDialogBottomMenuOwn;

    //私信,关注,主页的菜单
    @InjectView(R.id.ll_user_info_dialog)
    LinearLayout mLLUserInfoDialogBottomMenu;

    @InjectView(R.id.iv_show_dialog_level)
    ImageView mIvLevel;

    @InjectView(R.id.iv_show_dialog_anchorlevel)
    ImageView mIvAnchorLevel;

    @InjectView(R.id.tv_show_dialog_u_address)
    TextView mTvAddress;

    private int action;
    private BottomView mManageMenu;
    private AvatarView mAvatar;
    private TextView mTvUname;
    private ImageView mIvUserSex;


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        Dialog dialog = new Dialog(getActivity(), R.style.dialog_no_background);
        dialog.getWindow().setBackgroundDrawable(new
                ColorDrawable(Color.TRANSPARENT));
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_user_info_dialog, null);
        dialog.setContentView(view);
        ButterKnife.inject(this, view);
        initView(view);
        initData();
        return dialog;
    }

    private void initData() {
        mTvId.setText("ID:" + mToUser.id);
        //获取用户详细信息
        PhoneLiveApi.getUserInfo(mUser.id,mToUser.id,mRoomNum, new StringCallback() {
            @Override
            public void onError(Call call, Exception e,int id) {
            }
            @Override
            public void onResponse(String response,int id) {
                JSONArray res = ApiUtils.checkIsSuccess(response);
                if(res != null){
                    try {
                        JSONObject data = res.getJSONObject(0);

                        mTvFollowNum.setText("关注:  " + data.getString("follows"));
                        mTvFansNum.setText(  "粉丝:  " + data.getString("fans"));
                        mTvSendNum.setText(  "送出:  " + data.getString("consumption"));
                        mTvTicketNum.setText("收入:  " + data.getString("votestotal"));
                        int goodnum=StringUtils.toInt(data.getJSONObject("liang").getString("name"));
                        if (goodnum!=0){
                            mTvId.setText("靓:" + goodnum);
                        }else {
                            mTvId.setText("ID:" + mToUser.id);
                        }
                        //等级
                        mIvLevel.setImageResource(LiveUtils.getLevelRes(data.getString("level")));
                        mIvAnchorLevel.setImageResource(LiveUtils.getAnchorLevelRes2(data.getString("level_anchor")));
                        mTvAddress.setText(data.getString("city"));

                        mAvatar.setAvatarUrl(data.getString("avatar_thumb"));
                        mTvUname.setText(data.getString("user_nicename"));
                        mIvUserSex.setImageResource(LiveUtils.getSexRes(data.getString("sex")));

                        if (data.getInt("isattention") == 0 && isAdded()) {
                            mTvFollowBtn.setText("关注");
                            mTvFollowBtn.setEnabled(true);
                        } else {
                            mTvFollowBtn.setText("已关注");
                            mTvFollowBtn.setEnabled(false);
                            mTvFollowBtn.setTextColor(getResources().getColor(R.color.gray));
                        }


                        action = data.getInt("action");
                        switch (action){
                            //自己
                            case 0:
                                mTvReportBtn.setVisibility(View.GONE);
                                mIbSheZhi.setVisibility(View.GONE);
                                break;
                            //普通其他用户
                            case 30:
                                mTvReportBtn.setVisibility(View.VISIBLE);
                                mIbSheZhi.setVisibility(View.GONE);
                                break;
                            //自己是管理员
                            case 40:
                                mTvReportBtn.setVisibility(View.GONE);
                                mIbSheZhi.setVisibility(View.VISIBLE);
                                break;
                            //超管管理主播
                            case 60:
                                mTvReportBtn.setVisibility(View.GONE);
                                mIbSheZhi.setVisibility(View.VISIBLE);
                                break;
                            case 501:
                            case 502:
                                mTvReportBtn.setVisibility(View.GONE);
                                mIbSheZhi.setVisibility(View.VISIBLE);
                                break;


                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

    }

    //举报弹窗
    private void showReportAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("提示");
        builder.setMessage("确定举报?");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                PhoneLiveApi.report(mUser.id,mUser.token, mToUser.id);
                AppContext.showToastAppMsg(getActivity(), getString(R.string.reportsuccess));
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        builder.create().show();
    }

    private void initView(final View view) {
        mUser    = getArguments().getParcelable("MYUSERINFO");
        mToUser  = getArguments().getParcelable("TOUSERINFO");
        mRoomNum = getArguments().getString("ROOMNUM");
        mChatServer = ((ShowLiveActivityBase) getActivity()).mChatServer;

        //是否是自己点击弹窗
        if (mUser.id.equals(mToUser.id)) {
            mTvFollowBtn.setEnabled(false);
            //切换底部菜单
            mLLUserInfoDialogBottomMenuOwn.setVisibility(View.VISIBLE);
            mLLUserInfoDialogBottomMenu.setVisibility(View.GONE);
        }

        view.findViewById(R.id.ib_show_dialog_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        view.findViewById(R.id.tv_show_dialog_u_private_chat_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showPrivateMessage((ShowLiveActivityBase) getActivity(), mUser.id, mToUser.id);
            }
        });


        //主页
        view.findViewById(R.id.tv_show_dialog_u_home_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UIHelper.showHomePageActivity(getActivity(), mToUser.id);
                dismiss();//BBB
            }
        });
        //主页
        view.findViewById(R.id.tv_show_dialog_u_home_btn2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UIHelper.showHomePageActivity(getActivity(), mToUser.id);
                dismiss();//BBB
            }
        });

        mAvatar = ((AvatarView) view.findViewById(R.id.av_show_dialog_u_head));

        mAvatar.setAvatarUrl(mToUser.avatar);
        mTvUname = (TextView) view.findViewById(R.id.tv_show_dialog_u_name);
        mIvUserSex = (ImageView) view.findViewById(R.id.iv_show_dialog_sex);




        mIbSheZhi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showManageBottomMenu();
                //dismiss();
            }
        });
        mTvReportBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showReportAlert();
            }
        });


        mTvFollowBtn.setEnabled(false);
        mTvFollowBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    EMClient.getInstance().contactManager().removeUserFromBlackList(String.valueOf(mToUser.id));
                } catch (HyphenateException e) {
                    e.printStackTrace();
                }
                PhoneLiveApi.showFollow(AppContext.getInstance().getLoginUid(), mToUser.id,AppContext.getInstance().getToken(), null);
                mTvFollowBtn.setEnabled(false);
                mTvFollowBtn.setTextColor(getResources().getColor(R.color.gray));
                mTvFollowBtn.setText(getString(R.string.alreadyfollow));

                if(mToUser.id.equals(mRoomNum)) {
                    Event.VideoEvent event = new Event.VideoEvent();
                    event.action = 1;
                    EventBus.getDefault().post(event);
                }
            }
        });
    }

    //显示管理弹窗
    public void showManageBottomMenu() {
        BottomMenuView mBottomMenuView =  new BottomMenuView(getActivity());
        mManageMenu = new BottomView(getActivity(), R.style.BottomViewTheme_Transparent, mBottomMenuView);
        mBottomMenuView.setOptionData(action, mManageMenu);
        mBottomMenuView.setIsEmcee(mUser.id.equals(mRoomNum));
        mManageMenu.setAnimation(R.style.BottomToTopAnim);
        mManageMenu.showBottomView(false);
    }

    //跳转私信
    public void showPrivateMessage(final ShowLiveActivityBase activity, String uid, String touid) {
        UserBean userBean = new UserBean();
        userBean.id = mToUser.id;
        userBean.avatar = mToUser.avatar;
        userBean.user_nicename = mToUser.user_nicename;
        Bundle bundle = new Bundle();
        bundle.putParcelable("user", userBean);
        bundle.putInt("type", 1);
        MessageDetailFragment2 messageDetailFragment = new MessageDetailFragment2();
        messageDetailFragment.setArguments(bundle);
        messageDetailFragment.show(getActivity().getSupportFragmentManager(), "MessageDetailFragment");
        dismiss();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        OkHttpUtils.getInstance().cancelTag("getPmUserInfo");
        OkHttpUtils.getInstance().cancelTag("getUserInfo");
        OkHttpUtils.getInstance().cancelTag("getIsFollow");
        OkHttpUtils.getInstance().cancelTag("report");
        OkHttpUtils.getInstance().cancelTag("setCloseLive");
        OkHttpUtils.getInstance().cancelTag("setKick");
        OkHttpUtils.getInstance().cancelTag("setShutUp");
    }


    /**
     * @dw 设置禁言
     * */
    public void setShutUp(){

        PhoneLiveApi.setShutUp(
                mRoomNum,
                mToUser.id,
                mUser.id,
                mUser.token,
                new StringCallback(){

                    @Override
                    public void onError(Call call, Exception e,int id) {

                    }

                    @Override
                    public void onResponse(String response,int id) {
                        JSONArray res = ApiUtils.checkIsSuccess(response);

                        if(null == res) return;
                        mChatServer.doSetShutUp(mUser,mToUser);
                    }
                });
    }

    //设置踢人
    private void setKick() {
        PhoneLiveApi.setKick(
                mRoomNum,
                mToUser.id,
                mUser.id,
                mUser.token,
                new StringCallback(){

                    @Override
                    public void onError(Call call, Exception e,int id) {

                    }

                    @Override
                    public void onResponse(String response,int id) {
                        JSONArray res = ApiUtils.checkIsSuccess(response);

                        if(null == res) return;
                        mChatServer.doSetKick(mUser,mToUser);
                    }
                });
    }

    //超管关闭直播
    private void setCloseLive(String type) {
        PhoneLiveApi.setCloseLive(mUser.id,mUser.token,mToUser.id,type,new StringCallback(){

            @Override
            public void onError(Call call, Exception e, int id) {

            }

            @Override
            public void onResponse(String response, int id) {
                JSONArray res = ApiUtils.checkIsSuccess(response);

                if(null == res) return;
                mChatServer.doSetCloseLive();
            }
        });
    }


    //设置管理员
    private void setManage() {

        Observable<String> observable = Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(final Subscriber<? super String> subscriber) {
                PhoneLiveApi.setManage(
                        mRoomNum,
                        mToUser.id,
                        mUser.token,
                        mUser.id,
                        new StringCallback(){

                            @Override
                            public void onError(Call call, Exception e,int id) {
                                Toast.makeText(getContext(),"操作失败",Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onResponse(String response,int id) {
                                JSONArray res = ApiUtils.checkIsSuccess(response);
                                if(null == res) return;
                                subscriber.onNext("");
                                mManageMenu.dismissBottomView();

                            }
                        });
            }
        });
        observable.subscribe(new Action1<String>() {
            @Override
            public void call(String s) {
                if(action == 501){
                    action = 502;
                    mChatServer.doSetOrRemoveManage(mUser,mToUser,mToUser.user_nicename + "被设为管理员");
                }else{
                    action = 501;
                    mChatServer.doSetOrRemoveManage(mUser,mToUser,mToUser.user_nicename + "被删除管理员");
                }

            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(Event.DialogEvent event) {

        mManageMenu.dismissBottomView();
        if(event.action == 1){

            setShutUp();

        }else if(event.action == 0){

            setManage();

        }else if(event.action == 3){
            //踢人
            setKick();
        }else if(event.action == 2){
            //关闭直播
            setCloseLive("0");

        }else if(event.action == 4){
            //禁用
            setCloseLive("1");
        }else if(event.action == 5){

            ManageListDialogFragment fragment = new ManageListDialogFragment();
            fragment.setStyle(ManageListDialogFragment.STYLE_NO_TITLE,0);
            fragment.show(getActivity().getSupportFragmentManager(),"ManageListDialogFragment");
        }
    }




    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }
}

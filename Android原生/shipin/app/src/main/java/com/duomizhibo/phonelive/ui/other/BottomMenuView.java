package com.duomizhibo.phonelive.ui.other;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.duomizhibo.phonelive.AppContext;
import com.duomizhibo.phonelive.api.remote.ApiUtils;
import com.duomizhibo.phonelive.api.remote.PhoneLiveApi;
import com.duomizhibo.phonelive.base.ShowLiveActivityBase;
import com.duomizhibo.phonelive.bean.SimpleUserInfo;
import com.duomizhibo.phonelive.bean.UserBean;
import com.duomizhibo.phonelive.event.Event;
import com.duomizhibo.phonelive.fragment.ManageListDialogFragment;
import com.duomizhibo.phonelive.ui.other.ChatServer;
import com.tandong.bottomview.view.BottomView;
import com.duomizhibo.phonelive.R;
import com.duomizhibo.phonelive.widget.BlackTextView;
import com.zhy.http.okhttp.callback.StringCallback;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONException;

import okhttp3.Call;
import rx.Observable;
import rx.Subscriber;
import rx.functions.Action1;

/**
 * 直播间管理弹窗
 */
public class BottomMenuView extends FrameLayout implements View.OnClickListener {

    private LayoutInflater inflater;
    private RelativeLayout mRlSetManage,mRlReport,mRlKick,mRlShutUp,mRlCancel,mRlManageList,mRlCloseLive,mRlDisable;

    private BottomView mBottomView;
    private BlackTextView mManage;

    public BottomMenuView(Context context) {
        super(context);
        inflater = LayoutInflater.from(context);
        initView();
    }

    public BottomMenuView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }
    /**
    * @dw 设置操作选项
    * */
    public void setOptionData(int code, BottomView bottomView){

        this.mBottomView = bottomView;
        if(code == 502){
            mManage.setText("删除管理");
        }

        if(code == 60){
            mRlCloseLive.setVisibility(VISIBLE);
            mRlDisable.setVisibility(VISIBLE);
            mRlKick.setVisibility(GONE);
            mRlShutUp.setVisibility(GONE);
        }
    }
    /**
    *@dw 是否是主播
    *@param  isEmcee true是false管理
    * */
    public void setIsEmcee(boolean isEmcee){
        if(!isEmcee){
            mRlSetManage.setVisibility(View.GONE);
            mRlManageList.setVisibility(View.GONE);
            mRlReport.setVisibility(View.GONE);
        }else {
            mRlReport.setVisibility(View.GONE);
        }
    }

    private void initView() {

        inflater.inflate(R.layout.view_manage_menu,this);
        mRlSetManage = (RelativeLayout) findViewById(R.id.tv_manage_set_manage);
        mRlShutUp = (RelativeLayout) findViewById(R.id.tv_manage_shutup);
        mRlCancel = (RelativeLayout) findViewById(R.id.tv_manage_cancel);
        mRlManageList = (RelativeLayout) findViewById(R.id.tv_manage_manage_list);
        mRlReport = (RelativeLayout) findViewById(R.id.tv_manage_set_report);
        mManage = (BlackTextView) findViewById(R.id.tv_set_manage);
        mRlKick = (RelativeLayout) findViewById(R.id.tv_manage_kick);
        mRlCloseLive = (RelativeLayout) findViewById(R.id.tv_manage_set_close_live);
        mRlDisable = (RelativeLayout) findViewById(R.id.tv_manage_set_disable);
        mRlSetManage.setOnClickListener(this);
        mRlShutUp.setOnClickListener(this);
        mRlCancel.setOnClickListener(this);
        mRlManageList.setOnClickListener(this);
        mRlReport.setOnClickListener(this);
        mRlKick.setOnClickListener(this);
        mRlCloseLive.setOnClickListener(this);
        mRlDisable.setOnClickListener(this);


    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            //设为管理员
            case R.id.tv_manage_set_manage:
                postMessage(0);
                break;
            //禁言
            case R.id.tv_manage_shutup:
                postMessage(1);
                break;
            case R.id.tv_manage_cancel:
                if(mBottomView != null)
                    mBottomView.dismissBottomView();
                break;
            //管理员列表
            case R.id.tv_manage_manage_list:
                //UIHelper.shoManageListActivity(activity);
                postMessage(5);
                break;
            case R.id.tv_manage_set_report:
                break;
            //踢人
            case R.id.tv_manage_kick:
                postMessage(3);
                break;
            //关闭直播
            case R.id.tv_manage_set_close_live:
                postMessage(2);
                break;
            //禁用
            case R.id.tv_manage_set_disable:
                postMessage(4);
                break;
        }
    }

    private void postMessage(int action) {
        Event.DialogEvent event = new Event.DialogEvent();
        event.action = action;
        EventBus.getDefault().post(event);
    }


}

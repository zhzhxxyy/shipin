package com.duomizhibo.phonelive.fragment.video;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.duomizhibo.phonelive.AppContext;
import com.duomizhibo.phonelive.R;
import com.duomizhibo.phonelive.api.remote.ApiUtils;
import com.duomizhibo.phonelive.api.remote.PhoneLiveApi;
import com.duomizhibo.phonelive.base.AbsFragment;
import com.duomizhibo.phonelive.bean.video.BaseData;
import com.duomizhibo.phonelive.bean.video.MemberInfo;
import com.duomizhibo.phonelive.bean.video.VersionObject;
import com.duomizhibo.phonelive.ui.activity.CardPasswordActivity;
import com.duomizhibo.phonelive.ui.activity.LoginActivity;
import com.duomizhibo.phonelive.ui.activity.NewMainActivity;
import com.duomizhibo.phonelive.utils.SimpleUtils;
import com.duomizhibo.phonelive.utils.StringUtils;
import com.duomizhibo.phonelive.utils.TLog;
import com.duomizhibo.phonelive.utils.UIHelper;
import com.duomizhibo.phonelive.utils.UpdateManager;
import com.duomizhibo.phonelive.widget.alert.BaseDialog;
import com.duomizhibo.phonelive.widget.alert.TipDialog;
import com.nanchen.compresshelper.StringUtil;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.Date;

import butterknife.ButterKnife;
import butterknife.InjectView;
import okhttp3.Call;


public class HomeWodeFragment extends AbsFragment implements NewMainActivity.OnResumeCallback,View.OnClickListener {

    private View mRootView;
    private ImageView iv_head;
    private TextView tv_name;
    private View ll_other;
    private TextView tv_jinbi;
    private TextView tv_vip;
    private  View tv_kamichongzhi;
    private View tv_chongzhi;
    private  TextView tv_qiandao;
    private  View ll_myshipin;
//    private  View ll_myzixun;
//    private  View ll_mytupian;
    private  View ll_myshoucang;
//    private  View ll_tixian;
    private  View ll_chongzhijilu;
    private  View ll_xiaofeijilu;
//    private  View ll_dailishang;
//    private  View ll_xinxiguanli;
    private  View ll_kefu;
    private  View ll_version;
    private  TextView tv_version_old;
    private  TextView tv_version_new;
    private View ll_tuichu;
    private TextView tv_tuichu;
    private long clickTime=0;//点击时间
    private int minDuringClickTime=500;//最短的点击时间 毫秒

    private  VersionObject versionObjectOld=null;
    private  VersionObject versionObjectNew=null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.new_fragment_home_wode, container, false);
        return mRootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView(savedInstanceState);
    }

    private void initView(Bundle savedInstanceState) {
        iv_head=(ImageView) mRootView.findViewById(R.id.iv_head);
        tv_name=(TextView) mRootView.findViewById(R.id.tv_name);
        ll_other= mRootView.findViewById(R.id.ll_other);
        tv_jinbi=(TextView) mRootView.findViewById(R.id.tv_jinbi);
        tv_vip=(TextView) mRootView.findViewById(R.id.tv_vip);
        tv_kamichongzhi= mRootView.findViewById(R.id.tv_kamichongzhi);
        tv_chongzhi=mRootView.findViewById(R.id.tv_chongzhi);
        tv_qiandao=(TextView) mRootView.findViewById(R.id.tv_qiandao);
        ll_myshipin= mRootView.findViewById(R.id.ll_myshipin);
//        ll_myzixun= mRootView.findViewById(R.id.ll_myzixun);
//        ll_mytupian= mRootView.findViewById(R.id.ll_mytupian);
        ll_myshoucang= mRootView.findViewById(R.id.ll_myshoucang);
//        ll_tixian= mRootView.findViewById(R.id.ll_tixian);
        ll_chongzhijilu= mRootView.findViewById(R.id.ll_chongzhijilu);
        ll_xiaofeijilu= mRootView.findViewById(R.id.ll_xiaofeijilu);
//        ll_dailishang= mRootView.findViewById(R.id.ll_dailishang);
//        ll_xinxiguanli= mRootView.findViewById(R.id.ll_xinxiguanli);
        ll_kefu= mRootView.findViewById(R.id.ll_kefu);
        ll_version= mRootView.findViewById(R.id.ll_version);
        tv_version_old= (TextView) mRootView.findViewById(R.id.tv_version_old);
        tv_version_new= (TextView)mRootView.findViewById(R.id.tv_version_new);
        ll_tuichu= mRootView.findViewById(R.id.ll_tuichu);
        tv_tuichu= (TextView)mRootView.findViewById(R.id.tv_tuichu);
        iv_head.setOnClickListener(this);
        tv_kamichongzhi.setOnClickListener(this);
        tv_chongzhi.setOnClickListener(this);
        tv_qiandao.setOnClickListener(this);
        ll_myshipin.setOnClickListener(this);
//        ll_myzixun.setOnClickListener(this);
//        ll_mytupian.setOnClickListener(this);
        ll_myshoucang.setOnClickListener(this);
//        ll_tixian.setOnClickListener(this);
        ll_chongzhijilu.setOnClickListener(this);
        ll_xiaofeijilu.setOnClickListener(this);
//        ll_dailishang.setOnClickListener(this);
//        ll_xinxiguanli.setOnClickListener(this);
        ll_kefu.setOnClickListener(this);
        ll_version.setOnClickListener(this);
        ll_tuichu.setOnClickListener(this);

        versionObjectOld= AppContext.getInstance().getVersonObject(getActivity());
        dataToView();
        getMemberInfoData();
        buyCardPasswordUrl(true);
        checkVersion(true);
    }

    @Override
    public void onClick(View v) {
        if(Math.abs(System.currentTimeMillis()-clickTime)<minDuringClickTime){
            return;
        }
        clickTime=System.currentTimeMillis();
        if(v.getId()==R.id.iv_head){

        }else if(v.getId()==R.id.tv_kamichongzhi){
            if(AppContext.getInstance().isLogin()){
                UIHelper.showCardPasswordActivity(getActivity());
            }else{
                UIHelper.showLogin(getActivity());
            }
        }else if(v.getId()==R.id.tv_chongzhi){
            if(AppContext.getInstance().isLogin()){
                UIHelper.showRechargeActivity(getContext());
            }else{
                UIHelper.showLogin(getActivity());
            }
        }else if(v.getId()==R.id.tv_qiandao){
            if(AppContext.getInstance().isLogin()){
                todaySign();
            }else{
                UIHelper.showLogin(getActivity());
            }
        }else if(v.getId()==R.id.ll_myshipin){
            if(AppContext.getInstance().isLogin()){
                UIHelper.showMyVideoActivity(getContext());
            }else{
                UIHelper.showLogin(getActivity());
            }
        }else if(v.getId()==R.id.ll_myshoucang){
            if(AppContext.getInstance().isLogin()){
                UIHelper.showMyVideoCollectActivity(getContext());
            }else{
                UIHelper.showLogin(getActivity());
            }
        }else if(v.getId()==R.id.ll_chongzhijilu){
            if(AppContext.getInstance().isLogin()){
                UIHelper.showRecordPayActivity(getContext());
            }else{
                UIHelper.showLogin(getActivity());
            }
        }else if(v.getId()==R.id.ll_xiaofeijilu){
            if(AppContext.getInstance().isLogin()){
                UIHelper.showRecordVideoActivity(getContext());
            }else{
                UIHelper.showLogin(getActivity());
            }
        }else if(v.getId()==R.id.ll_kefu){
            if(AppContext.getInstance().isLogin()){
                openKefu();
            }else{
                UIHelper.showLogin(getActivity());
            }
        }else if(v.getId()==R.id.ll_version){
            checkVersion(false);
        }else if(v.getId()==R.id.ll_tuichu){
            AppContext.getInstance().cleanLoginInfo();
            dataToView();
            UIHelper.showLogin(getActivity());
        }
    }


    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);

    }

    @Override
    public void onResume() {
        super.onResume();
        getMemberInfoData();
        getSignState();
    }

    @Override
    public void onResumeRefresh() {
        getMemberInfoData();
    }

    //获取个人信息
    private void getMemberInfoData(){
        PhoneLiveApi.memberInfo(AppContext.getInstance().getToken(), callback);
    }


    //登录回调
    private final StringCallback callback = new StringCallback() {
        @Override
        public void onError(Call call, Exception e, int id) {
            AppContext.showToast("网络请求出错!");
            dataToView();
        }
        @Override
        public void onResponse(String s, int id) {
            BaseData baseData= ApiUtils.getBaseData(s);
            if(baseData.isSuccess()){
                try {
                    MemberInfo memberInfo=com.alibaba.fastjson.JSONObject.parseObject(baseData.getData(), MemberInfo.class);
                    AppContext.getInstance().updateUserInfoNew(memberInfo);

                } catch (Exception e) {
                }
            }else{
              if(baseData.getRespCode()==3){
                  AppContext.getInstance().cleanLoginInfo();
              }
            }
            dataToView();
        }
    };


    //数据绑定到view
    private void dataToView(){
        MemberInfo user = AppContext.getInstance().getLoginUserNew();
        if (null != user && StringUtils.toInt(user.getId()) > 0 && null!=user.getToken() && !StringUtils.isEmpty(user.getToken())) {
           //登录成功
            SimpleUtils.loadImageForView(getActivity(),iv_head, user.getHeadimgurl(), 0);
            tv_name.setText(user.getNickname());
            ll_other.setVisibility(View.VISIBLE);
            tv_jinbi.setText(user.getMoney()+"");
            if(user.getIs_permanent()==1){
                tv_vip.setText("永久vip");
            }else{
                if(StringUtils.isEmpty(user.getOut_time())|| "null".equals(user.getOut_time())){
                    tv_vip.setText("您还不是VIP");
                }else{
                  String nowTime=String.valueOf(System.currentTimeMillis());
                  if(nowTime.compareTo(user.getOut_time())>0){
                      tv_vip.setText("已过期");
                  }  else {
                      tv_vip.setText(user.getOutTimeString()+"截止");
                  }
                }
            }
            tv_tuichu.setText("退出登录");
        } else {
            //登录失败
            iv_head.setImageBitmap(null);
            tv_name.setText("请先登录！");
            ll_other.setVisibility(View.INVISIBLE);
            tv_tuichu.setText("点击登录");
        }

        if(versionObjectOld!=null){
            tv_version_old.setText("版本（"+versionObjectOld.getVersionName()+"）");
        }else{
            tv_version_old.setText("版本");
        }
        if(versionObjectNew!=null&&versionObjectNew.isHasNew()){
            tv_version_new.setText("新版本（"+versionObjectNew.getVersionName()+"）");
            tv_version_new.setVisibility(View.VISIBLE);
        }else{
            tv_version_new.setVisibility(View.GONE);
        }

    }


    //获取签到状态
    private void getSignState(){
        if(AppContext.getInstance().isLogin()){
            PhoneLiveApi.isSign(AppContext.getInstance().getToken(), new StringCallback() {
                @Override
                public void onError(Call call, Exception e, int id) {

                }

                @Override
                public void onResponse(String response, int id) {
                    BaseData baseData= ApiUtils.getBaseData(response);
                    if(baseData.isSuccess()&&"true".equals(baseData.getData())){
                        tv_qiandao.setBackgroundResource(R.drawable.btn_common_ccc);
                        tv_qiandao.setClickable(false);
                    }else{
                        tv_qiandao.setBackgroundResource(R.drawable.btn_common_ef98a7);
                        tv_qiandao.setClickable(true);
                    }
                }
            });
        }else{
            tv_qiandao.setBackgroundResource(R.drawable.btn_common_ef98a7);
            tv_qiandao.setClickable(true);
        }
    }

    //签到
    private void todaySign(){
        PhoneLiveApi.sign(AppContext.getInstance().getToken(), new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                AppContext.showToast("网络请求出错!");
            }

            @Override
            public void onResponse(String response, int id) {
                BaseData baseData= ApiUtils.getBaseData(response);
                if(baseData.isSuccess()){
                    tv_qiandao.setBackgroundResource(R.drawable.btn_common_ccc);
                    tv_qiandao.setClickable(false);
                }else{
                    AppContext.showToast(baseData.getRespMsg());
                }
            }
        });
    }
    private String url;//客服地址
    private void openKefu(){
        if(StringUtil.isEmpty(url)){
            AppContext.showToast("正在获取客服联系方式!");
            buyCardPasswordUrl(false);
        }else{
            if("#".equals(url)){
                AppContext.showToast("暂无客服联系方式!");
            }else{
                UIHelper.showNewWebView(getActivity(),url,"客服");
            }
        }
    }
    private void buyCardPasswordUrl(final boolean isAuto){
        PhoneLiveApi.buyCardPasswordUrl( AppContext.getInstance().getToken(), new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                AppContext.showToast("网络请求出错!");
            }

            @Override
            public void onResponse(String response, int id) {
                BaseData baseData= ApiUtils.getBaseData(response);
                url=baseData.getData();
                if(!isAuto){
                    if("#".equals(url)){
                        AppContext.showToast("暂无客服联系方式!");
                    }else{
                        UIHelper.showNewWebView(getActivity(),url,"客服");
                    }
                }
            }
        });
    }

    //检查更新
    private void checkVersion(final boolean isAuto){
        if(versionObjectNew!=null){
            if(isAuto){
                return;
            }
            if(versionObjectNew.isHasNew()){
              //有新版本 需要弹窗提示
                TipDialog tipDialog=new TipDialog(getActivity());
                tipDialog.setTitle("更新信息");
                tipDialog.setOkText("更新");
                tipDialog.setCancelText("取消");
                tipDialog.show(versionObjectNew.getDescription() + "", new BaseDialog.ParameCallBack() {
                    @Override
                    public void onCall(Object o) {
                        if(o instanceof Boolean){
                            if((Boolean) o){
                                Uri uri=Uri.parse(versionObjectNew.getUrl());
                                Intent intent=new Intent(Intent.ACTION_VIEW,uri);
                                startActivity(intent);
                            }
                        }
                    }
                });
            }else{
                AppContext.showToastAppMsg(getActivity(),"当前已是最新版本");
            }
            return;
        }
        if(versionObjectOld!=null){
            PhoneLiveApi.getVersion(versionObjectOld.getVersionCode(), versionObjectOld.getVersionName(), new StringCallback() {
                @Override
                public void onError(Call call, Exception e, int id) {
                    if(!isAuto){
                        AppContext.showToastAppMsg(getActivity(),"检查版本失败：网络问题");
                    }
                }
                @Override
                public void onResponse(String response, int id) {
                    BaseData baseData= ApiUtils.getBaseData(response);
                    VersionObject versionObject=null;
                    if(baseData.isSuccess()){
                        try {
                            versionObject=com.alibaba.fastjson.JSONObject.parseObject(baseData.getData(), VersionObject.class);
                        } catch (Exception e) {
                            versionObject=null;
                        }

                        if(versionObjectNew!=null&&versionObjectNew.isHasNew()){
                            tv_version_new.setText("新版本（"+versionObjectNew.getVersionName()+"）");
                            tv_version_new.setVisibility(View.VISIBLE);
                        }else{
                            tv_version_new.setVisibility(View.GONE);
                        }

                        if(versionObject==null&&!isAuto){
                            AppContext.showToastAppMsg(getActivity(),"检查版本失败");
                        }
                        if(versionObject!=null){
                            versionObjectNew=versionObject;
                            if(versionObjectNew!=null&&versionObjectNew.isHasNew()){
                                tv_version_new.setText("新版本（"+versionObjectNew.getVersionName()+"）");
                                tv_version_new.setVisibility(View.VISIBLE);
                            }else{
                                tv_version_new.setVisibility(View.GONE);
                            }
                        }
                    }else{
                        if(!isAuto){
                            AppContext.showToastAppMsg(getActivity(),baseData.getRespMsg());
                        }
                    }
                }
            });
        }else{
            if(!isAuto){
                AppContext.showToastAppMsg(getActivity(),"获取版本信息权限不足");
            }
        }
    }

}

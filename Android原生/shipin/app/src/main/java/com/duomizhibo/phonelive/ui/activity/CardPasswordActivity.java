package com.duomizhibo.phonelive.ui.activity;

import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.duomizhibo.phonelive.AppContext;
import com.duomizhibo.phonelive.R;
import com.duomizhibo.phonelive.api.remote.ApiUtils;
import com.duomizhibo.phonelive.api.remote.PhoneLiveApi;
import com.duomizhibo.phonelive.base.ToolBarBaseActivity;
import com.duomizhibo.phonelive.bean.video.BaseData;
import com.duomizhibo.phonelive.bean.video.CardRecharge;
import com.duomizhibo.phonelive.ui.customviews.ActivityTitle;
import com.duomizhibo.phonelive.utils.DialogHelp;
import com.duomizhibo.phonelive.utils.StringUtils;
import com.duomizhibo.phonelive.utils.TDevice;
import com.duomizhibo.phonelive.utils.UIHelper;
import com.duomizhibo.phonelive.widget.BlackEditText;
import com.zhy.http.okhttp.callback.StringCallback;

import butterknife.InjectView;
import butterknife.OnClick;
import okhttp3.Call;


public class CardPasswordActivity extends ToolBarBaseActivity {

    @InjectView(R.id.view_title)
    ActivityTitle mActivityTitle;

    @InjectView(R.id.et_card_password)
    EditText et_card_password;
    @InjectView(R.id.tv_query)
    View tv_query;
    @InjectView(R.id.tv_buy)
    View tv_buy;
    @InjectView(R.id.ll_info)
    View ll_info;
    @InjectView(R.id.tv_card_type)
    TextView tv_card_type;

    @InjectView(R.id.ll_vip_time)
    View ll_vip_time;
    @InjectView(R.id.tv_vip_time)
    TextView tv_vip_time;

    @InjectView(R.id.tv_price)
    TextView tv_price;
    @InjectView(R.id.tv_out_time)
    TextView tv_out_time;
    @InjectView(R.id.tv_status)
    TextView tv_status;
    @InjectView(R.id.tv_recharge)
    View tv_recharge;



    private CardRecharge cardRecharge=null;
    private long clickTime=0;//点击时间
    private int minDuringClickTime=500;//最短的点击时间 毫秒

    private String url=null;

    @Override
    protected int getLayoutId() {
        return R.layout.new_activity_card_password;
    }

    @Override
    public void initView() {

        mActivityTitle.setReturnListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }




    @Override
    public void initData() {
        dataToView();
        buyCardPasswordUrl(true);
    }

    @OnClick({R.id.tv_query, R.id.tv_buy, R.id.tv_recharge})
    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.tv_query) {

            getCardPasswordInfo();
        }else if(v.getId() == R.id.tv_buy){
            if(StringUtils.isEmpty(url)){
                AppContext.showToast("正在获取数据，请稍等!");
                buyCardPasswordUrl(false);
            }else{
                if("#".equals(url)){
                    AppContext.showToast("暂无客服联系方式!");
                }else{
                    UIHelper.showNewWebView(this,url,"客服");
                }
            }
        }else if(v.getId() == R.id.tv_recharge){
            useCardPassword();
        }


    }






    @Override
    protected boolean hasActionBar() {
        return false;
    }




    private void dataToView(){

        if(cardRecharge==null){
            ll_info.setVisibility(View.GONE);
            return;
        }
        ll_info.setVisibility(View.VISIBLE);

        if(cardRecharge.getCard_type()==1){
            tv_card_type.setText("VIP卡");
            ll_vip_time.setVisibility(View.VISIBLE);

            if(cardRecharge.getVip_time()==999999999){
                tv_vip_time.setText("永久");
            }else{
                tv_vip_time.setText(cardRecharge.getVip_time()+"天");
            }
        }else{
            tv_card_type.setText("金币卡");
            ll_vip_time.setVisibility(View.GONE);
            tv_vip_time.setText("");
        }

        tv_price.setText("￥"+cardRecharge.getPrice());
        tv_out_time.setText(cardRecharge.getOut_times()+"");
        if(cardRecharge.getStatus()==1){
            tv_status.setText("已使用");
        }else{
            tv_status.setText("未使用");
        }

    }


    private void getCardPasswordInfo(){
        String card_number=et_card_password.getText().toString().trim();
        if(StringUtils.isEmpty(card_number)){
            AppContext.showToast("请输入卡号");
            cardRecharge=null;
            dataToView();
            return;
        }

        if(Math.abs(System.currentTimeMillis()-clickTime)<minDuringClickTime){
            return;
        }

        clickTime=System.currentTimeMillis();
        PhoneLiveApi.getCardPasswordInfo(card_number, AppContext.getInstance().getToken(), new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                AppContext.showToast("网络请求出错!");
            }

            @Override
            public void onResponse(String response, int id) {
                BaseData baseData= ApiUtils.getBaseData(response);
                if(baseData.isSuccess()){
                    try {
                        cardRecharge=com.alibaba.fastjson.JSONObject.parseObject(baseData.getData(), CardRecharge.class);
                    } catch (Exception e) {
                        cardRecharge=null;
                    }
                    dataToView();
                }else{
                    cardRecharge=null;
                    dataToView();
                    AppContext.showToast(baseData.getRespMsg());
                }
            }
        });
    }

    private void useCardPassword(){
        if(cardRecharge==null){
            return;
        }
        if(Math.abs(System.currentTimeMillis()-clickTime)<minDuringClickTime){
            return;
        }
        clickTime=System.currentTimeMillis();
        PhoneLiveApi.useCardPassword(cardRecharge.getCard_number(),cardRecharge.getId(), AppContext.getInstance().getToken(), new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                AppContext.showToast("网络请求出错!");
            }

            @Override
            public void onResponse(String response, int id) {
                BaseData baseData= ApiUtils.getBaseData(response);
//                if(baseData.isSuccess()){
//                    AppContext.showToast(baseData.getRespMsg());
//                }else{
//                    AppContext.showToast(baseData.getRespMsg());
//                }
                AppContext.showToast(baseData.getRespMsg());
            }
        });
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
                        UIHelper.showNewWebView(CardPasswordActivity.this,url,"客服");
                    }
                }
            }
        });
    }







}

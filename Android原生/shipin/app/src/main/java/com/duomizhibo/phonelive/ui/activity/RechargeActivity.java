package com.duomizhibo.phonelive.ui.activity;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.duomizhibo.phonelive.AppContext;
import com.duomizhibo.phonelive.R;
import com.duomizhibo.phonelive.api.remote.ApiUtils;
import com.duomizhibo.phonelive.api.remote.PhoneLiveApi;
import com.duomizhibo.phonelive.base.ToolBarBaseActivity;
import com.duomizhibo.phonelive.bean.video.BaseData;
import com.duomizhibo.phonelive.bean.video.GoldPackageObject;
import com.duomizhibo.phonelive.bean.video.PaymentObject;
import com.duomizhibo.phonelive.bean.video.RechargeObject;
import com.duomizhibo.phonelive.bean.video.RechargePackageObject;
import com.duomizhibo.phonelive.ui.customviews.ActivityTitle;
import com.duomizhibo.phonelive.utils.UIHelper;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.List;

import butterknife.InjectView;
import butterknife.OnClick;
import okhttp3.Call;


public class RechargeActivity extends ToolBarBaseActivity {


    @InjectView(R.id.view_title)
    ActivityTitle mActivityTitle;
    @InjectView(R.id.tv_vip)
    TextView tv_vip;
    @InjectView(R.id.tv_gold)
    TextView tv_gold;
    @InjectView(R.id.ll_package_content)
    LinearLayout ll_package_content;
    @InjectView(R.id.ll_tip)
    View ll_tip;
    @InjectView(R.id.tv_tip)
    TextView tv_tip;
    @InjectView(R.id.ll_payment_content)
    LinearLayout ll_payment_content;
    @InjectView(R.id.tv_money)
    TextView tv_money;
    @InjectView(R.id.tv_btn_recharge)
    View tv_btn_recharge;

    @InjectView(R.id.view_fail)
    View view_fail;
    @InjectView(R.id.tv_fail)
    TextView tv_fail;



    private int showType=0;//1 显示金币充值 其他显示VIP
    private boolean isLoading=false;
    private RechargeObject rechargeObject;

    private long clickTime=0;//点击时间
    private int minDuringClickTime=500;//最短的点击时间 毫秒

    private GoldPackageObject selectGoldPackageObject=null;//选中
    private RechargePackageObject selectRechargePackageObject=null;
    private PaymentObject selectPaymentObject=null;
    @Override
    protected int getLayoutId() {
        return R.layout.new_activity_recharge;
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
        dataToViewInit(false);
        getRechargeData();
    }




    @OnClick({R.id.tv_vip,R.id.tv_gold, R.id.tv_btn_recharge,R.id.view_fail})
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_vip:
                if(showType==1){
                    showType=0;
                    clickTabView();
                }
                break;
            case R.id.tv_gold:
                if(showType!=1){
                    showType=1;
                    clickTabView();
                }
                break;
            case R.id.tv_btn_recharge:

                clickRecharge();
                break;

            case R.id.view_fail:
                getRechargeData();
                break;
        }
        //recharge_package_normal_item
        //recharge_package_et_item

        //recharge_payment_image_item

    }


    @Override
    protected boolean hasActionBar() {
        return false;
    }

    //获取初始数据
    private void getRechargeData(){
        PhoneLiveApi.recharge(AppContext.getInstance().getToken(), new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                dataToViewInit(true);
                AppContext.showToast("网络请求出错!");
            }

            @Override
            public void onResponse(String response, int id) {
                BaseData baseData= ApiUtils.getBaseData(response);
                if(baseData.isSuccess()){
                    RechargeObject   rechargeObjectTemp=null;
                    try {
                        rechargeObjectTemp=  com.alibaba.fastjson.JSONObject.parseObject(baseData.getData(), RechargeObject.class);
                    } catch (Exception e) {
                    }
                    if(rechargeObjectTemp!=null){
                        rechargeObject=rechargeObjectTemp;
                    }
                }else{
                    AppContext.showToast(baseData.getRespMsg());
                }
                dataToViewInit(false);
            }
        });
    }

    //提交创建订单

    //数据绑定view
    private void dataToViewInit(boolean isFail){
        if(isFail&&rechargeObject==null){
            view_fail.setVisibility(View.VISIBLE);
            return;
        }
        view_fail.setVisibility(View.GONE);

        if(rechargeObject==null){
            ll_payment_content.removeAllViews();
            ll_payment_content.setVisibility(View.GONE);
        }else{
            LayoutInflater inflater=LayoutInflater.from(this);
            ll_payment_content.setVisibility(View.VISIBLE);
            ll_payment_content.removeAllViews();
            List<PaymentObject> paymentList= rechargeObject.getPaymentList();

            if(paymentList!=null&&paymentList.size()>0){
                for(int i=0;i<paymentList.size();i++){
                    View viewItem = inflater.inflate(R.layout.recharge_payment_image_item,null);
                    ImageView iv_payment=(ImageView)viewItem.findViewById(R.id.iv_payment);
                    Glide.with(this).load(paymentList.get(i).getPayIcon()).placeholder(null).into(iv_payment);
                    if(i==0){
                        selectPaymentObject=paymentList.get(i);
                        iv_payment.setBackgroundResource(R.drawable.btn_corner_select);
                    }else{
                        iv_payment.setBackgroundResource(R.drawable.btn_corner_normal);
                    }
                    viewItem.setTag(paymentList.get(i));
                    viewItem.setOnClickListener(packageClick);
                    ll_payment_content.addView(viewItem);
                }
            }
        }
        clickTabView();
    }


    private void clickTabView(){
        if(showType==1){
            //金币
            tv_vip.setTextColor(getResources().getColor(R.color.main_black));
            tv_gold.setTextColor(getResources().getColor(R.color.color_fe5d02));
        }else{
            tv_vip.setTextColor(getResources().getColor(R.color.color_fe5d02));
            tv_gold.setTextColor(getResources().getColor(R.color.main_black));
        }
        ll_package_content.removeAllViews();
        ll_package_content.setVisibility(View.GONE);
        ll_tip.setVisibility(View.GONE);
        tv_tip.setVisibility(View.GONE);
        tv_money.setText("0.00");
        if(rechargeObject==null){

            return;
        }
        LayoutInflater inflater=LayoutInflater.from(this);
        if(showType==1){
            List<GoldPackageObject> goldList= rechargeObject.getGoldPackageList();
            ll_package_content.setVisibility(View.VISIBLE);
            if(goldList!=null&&goldList.size()>0){
                for(int i=0;i<goldList.size();i++){
                    View viewItem = inflater.inflate(R.layout.recharge_package_normal_item,null);
                    TextView tv_package_title=(TextView)viewItem.findViewById(R.id.tv_package_title);
                    View rl_package=viewItem.findViewById(R.id.rl_package);
                    TextView tv_package_info=(TextView)viewItem.findViewById(R.id.tv_package_info);
                    tv_package_title.setText(goldList.get(i).getName()+"\n"+goldList.get(i).getGold());
                    tv_package_info.setText("￥"+goldList.get(i).getPrice());
                    if(i==0){
                        selectGoldPackageObject=goldList.get(i);
                        tv_money.setText(goldList.get(i).getPrice()+"");
                        rl_package.setBackgroundResource(R.drawable.bg_corner_bottom_a38a51);
                    }else{
                        rl_package.setBackgroundResource(R.drawable.bg_corner_bottom_d8d8d8);
                    }
                    viewItem.setTag(goldList.get(i));
                    viewItem.setOnClickListener(packageClick);
                    ll_package_content.addView(viewItem);
                }
            }

            GoldPackageObject goldPackageObject=new GoldPackageObject();
            goldPackageObject.setName("自定义");
            goldPackageObject.setPrice(0);
            goldPackageObject.setId(0);
            goldPackageObject.setGold(0);
            View viewItem = inflater.inflate(R.layout.recharge_package_et_item,null);
            TextView tv_package_title=(TextView)viewItem.findViewById(R.id.tv_package_title);
            tv_package_title.setText(goldPackageObject.getName()+"\n  ");
            View rl_package=viewItem.findViewById(R.id.rl_package);
            EditText et_package_price=(EditText)viewItem.findViewById(R.id.et_package_price);
            et_package_price.setText("");
            et_package_price.addTextChangedListener(textWatcher);
            if(goldList!=null&&goldList.size()>0){
                rl_package.setBackgroundResource(R.drawable.bg_corner_bottom_d8d8d8);
            }else{
                selectGoldPackageObject=goldPackageObject;
                rl_package.setBackgroundResource(R.drawable.bg_corner_bottom_a38a51);
            }
            viewItem.setTag(goldPackageObject);
            viewItem.setOnClickListener(packageClick);
            ll_package_content.addView(viewItem);

            ll_tip.setVisibility(View.VISIBLE);
            tv_tip.setVisibility(View.VISIBLE);
            tv_tip.setText("1、 当前金币兑换比例：1元人民币可兑换"+rechargeObject.getGold_exchange_rate()+"个金币.\n2、 您输入的金币将会自动调整为整数.");

        }else{
            List<RechargePackageObject> rechargeList= rechargeObject.getRechargeList();

            ll_package_content.setVisibility(View.VISIBLE);
            if(rechargeList!=null&&rechargeList.size()>0){
                for(int i=0;i<rechargeList.size();i++){
                    View viewItem = inflater.inflate(R.layout.recharge_package_normal_item,null);
                    TextView tv_package_title=(TextView)viewItem.findViewById(R.id.tv_package_title);
                    View rl_package=viewItem.findViewById(R.id.rl_package);
                    TextView tv_package_info=(TextView)viewItem.findViewById(R.id.tv_package_info);
                    tv_package_title.setText(rechargeList.get(i).getName()+"\n￥"+rechargeList.get(i).getPrice());
                    if(rechargeList.get(i).getPermanent()==1){
                        tv_package_info.setText("永久VIP");
                    }else{
                        tv_package_info.setText("永久 "+rechargeList.get(i).getDays()+"天");
                    }
                    if(i==0){
                        selectRechargePackageObject=rechargeList.get(i);
                        tv_money.setText(rechargeList.get(i).getPrice()+"");
                        rl_package.setBackgroundResource(R.drawable.bg_corner_bottom_a38a51);
                    }else{
                        rl_package.setBackgroundResource(R.drawable.bg_corner_bottom_d8d8d8);
                    }
                    viewItem.setTag(rechargeList.get(i));
                    viewItem.setOnClickListener(packageClick);
                    ll_package_content.addView(viewItem);
                }
            }
        }

    }


    private View.OnClickListener packageClick=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
           Object o= v.getTag();
           if(o instanceof  GoldPackageObject){
               GoldPackageObject item=(GoldPackageObject)o;
               selectGoldPackageObject=item;
               tv_money.setText(item.getPrice()+"");
               int count= ll_package_content.getChildCount();
               for(int i=0;i<count;i++){
                   View view= ll_package_content.getChildAt(i);
                   if(view.equals(v)){
                       if(item.getId()==0&&"自定义".equals(item.getName())){
                           String str= ((EditText)view.findViewById(R.id.et_package_price)).getText().toString();
                           tv_money.setText(getDoubleByString(str)+"");
                       }
                       view.findViewById(R.id.rl_package).setBackgroundResource(R.drawable.bg_corner_bottom_a38a51);
                   }else{
                       view.findViewById(R.id.rl_package).setBackgroundResource(R.drawable.bg_corner_bottom_d8d8d8);
                   }
               }

           } else if(o instanceof RechargePackageObject){
               int count= ll_package_content.getChildCount();
               selectRechargePackageObject=(RechargePackageObject)o;
               for(int i=0;i<count;i++){
                   View view= ll_package_content.getChildAt(i);
                   if(view.equals(v)){
                       view.findViewById(R.id.rl_package).setBackgroundResource(R.drawable.bg_corner_bottom_a38a51);
                   }else{
                       view.findViewById(R.id.rl_package).setBackgroundResource(R.drawable.bg_corner_bottom_d8d8d8);
                   }
               }
               RechargePackageObject item=(RechargePackageObject)o;
               tv_money.setText(item.getPrice()+"");
           }else if(o instanceof PaymentObject){
               selectPaymentObject=(PaymentObject)o;
               int count= ll_payment_content.getChildCount();
               for(int i=0;i<count;i++){
                   View view= ll_payment_content.getChildAt(i);
                   if(view.equals(v)){
                       view.findViewById(R.id.iv_payment).setBackgroundResource(R.drawable.btn_corner_select);
                   }else{
                       view.findViewById(R.id.iv_payment).setBackgroundResource(R.drawable.btn_corner_normal);
                   }
               }

           }
        }
    };



    private TextWatcher textWatcher=new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            String str=s.toString().trim();
            tv_money.setText(getDoubleByString(str)+"");
        }
    };



    private double getDoubleByString(String str){
        double value=0;
        if(str==null||"".equals(str)){

        }else{
            try {
                value=  Double.parseDouble(str);
            } catch (Exception e) {
                value=0;
            }
        }
        return  value;
    }


    //点击充值
    private void clickRecharge(){
        if(Math.abs(System.currentTimeMillis()-clickTime)<minDuringClickTime){
            return;
        }
        clickTime=System.currentTimeMillis();
        int buyType=0;
        int packageId=0;
        if(selectPaymentObject==null){
            AppContext.showToast("请选择支付方式!");
            return;
        }
        String payCode=selectPaymentObject.getPayCode();
        if(showType!=1){
            buyType=2;
            //VIP
            if(selectRechargePackageObject==null){
                AppContext.showToast("请选择套餐!");
                return;
            }
            packageId=selectRechargePackageObject.getId();
        }else{
            buyType=1;
            //金币
            if(selectGoldPackageObject==null){
                AppContext.showToast("请选择套餐!");
                return;
            }
        }
        double price=getDoubleByString(tv_money.getText().toString());
        if(price<=0){
            AppContext.showToast("金额至少大于0!");
            return;
        }
        PhoneLiveApi.createOrder(buyType,payCode,price,packageId,AppContext.getInstance().getToken(), new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                AppContext.showToast("网络请求出错!");
            }

            @Override
            public void onResponse(String response, int id) {
                BaseData baseData= ApiUtils.getBaseData(response);
                if(baseData.isSuccess()){
                    AppContext.showToast("订单创建成功!");
                    UIHelper.showNewWebView(RechargeActivity.this,baseData.getData(),"订单支付");
                }else{
                    AppContext.showToast(baseData.getRespMsg());
                }
            }
        });



    }

}

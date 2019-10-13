package com.duomizhibo.phonelive.ui;

import android.view.View;
import android.widget.ImageView;

import com.duomizhibo.phonelive.AppContext;
import com.duomizhibo.phonelive.R;
import com.duomizhibo.phonelive.api.remote.PhoneLiveApi;
import com.duomizhibo.phonelive.base.ToolBarBaseActivity;
import com.duomizhibo.phonelive.bean.UserBean;
import com.duomizhibo.phonelive.ui.customviews.ActivityTitle;
import com.duomizhibo.phonelive.utils.LiveUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import butterknife.InjectView;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * 修改性别
 */
public class UserChangeSexActivity extends ToolBarBaseActivity {
    @InjectView(R.id.iv_change_sex_male)
    ImageView mIvMale;
    @InjectView(R.id.iv_change_sex_female)
    ImageView mIvFemale;

    @InjectView(R.id.view_title)
    ActivityTitle mActivityTitle;

    private String sex = "0";
    @Override
    protected int getLayoutId() {
        return R.layout.activity_change_sex;
    }

    @Override
    public void initData() {
        setActionBarTitle("性别");
        sex = AppContext.getInstance().getLoginUser().sex;

        boolean sexb = changeUI();
        mIvFemale.setImageResource(sexb?R.drawable.choice_sex_un_femal:R.drawable.choice_sex_femal);
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
    protected boolean hasBackButton() {
        return true;
    }

    @OnClick({R.id.iv_change_sex_male,R.id.iv_change_sex_female})
    public void OnClick(View v){
        switch (v.getId()){
            case R.id.iv_change_sex_male:
                sex = "1";
                changeSex();
                break;
            case R.id.iv_change_sex_female:
                sex = "2";
                changeSex();
                break;
        }
    }

    private void changeSex(){
        boolean sexb = changeUI();
        mIvFemale.setImageResource(sexb?R.drawable.choice_sex_un_femal:R.drawable.choice_sex_femal);
        PhoneLiveApi.saveInfo(LiveUtils.getFiledJson("sex",String.valueOf(sex)), getUserID(), getUserToken(), new StringCallback() {
            @Override
            public void onError(Call call, Exception e,int id) {
                AppContext.showToastAppMsg(UserChangeSexActivity.this,"修改性别失败");
            }

            @Override
            public void onResponse(String response,int id) {
                UserBean mUser = AppContext.getInstance().getLoginUser();
                mUser.sex = sex;
                AppContext.getInstance().saveUserInfo(mUser);
                finish();
            }
        });
    }

    private boolean changeUI() {
        boolean sexb = sex.equals("1");
        mIvMale.setImageResource(sexb? R.drawable.choice_sex_male:R.drawable.choice_sex_un_male);
        return sexb;
    }

    @Override
    protected boolean hasActionBar() {
        return false;
    }

    @Override
    public void onClick(View v) {

    }
}

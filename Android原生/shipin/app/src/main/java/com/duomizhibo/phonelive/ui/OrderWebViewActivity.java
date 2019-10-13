package com.duomizhibo.phonelive.ui;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.LinearLayout;

import com.duomizhibo.phonelive.AppConfig;
import com.duomizhibo.phonelive.R;
import com.duomizhibo.phonelive.base.ToolBarBaseActivity;
import com.duomizhibo.phonelive.ui.customviews.ActivityTitle;

import java.util.Locale;

import butterknife.InjectView;

public class OrderWebViewActivity extends ToolBarBaseActivity {


    @InjectView(R.id.order_wv)
    WebView mWebView;

    @InjectView(R.id.ll_week)
    LinearLayout mLlWeekView;

    @InjectView(R.id.ll_all)
    LinearLayout mLlAllView;

    @InjectView(R.id.view_all)
    View mViewAll;

    @InjectView(R.id.view_week)
    View mViewWeek;

    @InjectView(R.id.view_title)
    ActivityTitle mActivityTitle;

    private String mUid;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_order_web_view;
    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public void initView() {

        mActivityTitle.setReturnListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        mLlWeekView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showData(false);
            }
        });

        mLlAllView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               showData(true);
            }
        });
    }

    @Override
    public void initData() {

        mUid = getIntent().getStringExtra("uid");
        showData(false);

    }

    private void showData(boolean b) {

        mViewWeek.setVisibility(b ? View.GONE : View.VISIBLE);
        mViewAll.setVisibility(!b ? View.GONE : View.VISIBLE);
        String url = String.format(Locale.CHINA,
                AppConfig.MAIN_URL + "/index.php?g=appapi&m=Contribute&a=order&type=week&uid="+ mUid +"&type=%s",b ? "all" : "week");
        mWebView.loadUrl(url);
    }

    @Override
    protected boolean hasActionBar() {
        return false;
    }

    public static void startOrderWebView(Context context,String uid){
        Intent intent = new Intent(context,OrderWebViewActivity.class);
        intent.putExtra("uid",uid);
        context.startActivity(intent);
    }
}

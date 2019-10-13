package com.duomizhibo.phonelive.ui;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.duomizhibo.phonelive.AppConfig;
import com.duomizhibo.phonelive.AppContext;
import com.duomizhibo.phonelive.R;

public class ShopActivity extends Activity implements OnClickListener {
    TextView btn_head_a, btn_head_b, btn_head_c;
    WebView mWebView;
    ImageView mIvFinish;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop);
        btn_head_a = (TextView) findViewById(R.id.btn_head_a);
        btn_head_b = (TextView) findViewById(R.id.btn_head_b);
        btn_head_c = (TextView) findViewById(R.id.btn_head_c);
        mWebView = (WebView) findViewById(R.id.wb_shop);
        mIvFinish = (ImageView) findViewById(R.id.iv_fininsh);
        WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDisplayZoomControls(true);
        webSettings.setSupportZoom(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);
        mWebView.loadUrl(AppConfig.MAIN_URL + "/index.php?g=Appapi&m=Vip&a=index&uid=" + AppContext.getInstance().getLoginUid() + "&token=" + AppContext.getInstance().getToken());
        btn_head_a.setOnClickListener(this);
        btn_head_b.setOnClickListener(this);
        btn_head_c.setOnClickListener(this);
        mIvFinish.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_head_a:
                btn_head_a.setBackgroundResource(R.drawable.house_rent_selected);
                btn_head_a.setTextColor(getResources().getColor(R.color.white));
                btn_head_b.setBackgroundResource(R.color.white);
                btn_head_b.setTextColor(getResources().getColor(R.color.black));
                btn_head_c.setBackgroundResource(R.drawable.house_rent_1);
                btn_head_c.setTextColor(getResources().getColor(R.color.black));
                mWebView.loadUrl(AppConfig.MAIN_URL + "/index.php?g=Appapi&m=Vip&a=index&uid=" + AppContext.getInstance().getLoginUid() + "&token=" + AppContext.getInstance().getToken());
                break;
            case R.id.btn_head_b:
                btn_head_b.setBackgroundResource(R.color.maintone);
                btn_head_b.setTextColor(getResources().getColor(R.color.white));
                btn_head_a.setBackgroundResource(R.drawable.house_rent);
                btn_head_a.setTextColor(getResources().getColor(R.color.black));
                btn_head_c.setBackgroundResource(R.drawable.house_rent_1);
                btn_head_c.setTextColor(getResources().getColor(R.color.black));
                mWebView.loadUrl(AppConfig.MAIN_URL + "/index.php?g=Appapi&m=Liang&a=index&uid=" + AppContext.getInstance().getLoginUid() + "&token=" + AppContext.getInstance().getToken());
                break;
            case R.id.btn_head_c:
                btn_head_c.setBackgroundResource(R.drawable.house_rent_selected_1);
                btn_head_c.setTextColor(getResources().getColor(R.color.white));
                btn_head_b.setBackgroundResource(R.color.white);
                btn_head_b.setTextColor(getResources().getColor(R.color.black));
                btn_head_a.setBackgroundResource(R.drawable.house_rent);
                btn_head_a.setTextColor(getResources().getColor(R.color.black));
                mWebView.loadUrl(AppConfig.MAIN_URL + "/index.php?g=Appapi&m=Car&a=index&uid=" + AppContext.getInstance().getLoginUid() + "&token=" + AppContext.getInstance().getToken()
                );

                break;
            case R.id.iv_fininsh:
                finish();
                break;

            default:
                break;
        }
    }

}
package com.duomizhibo.phonelive.ui.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.SparseArray;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.duomizhibo.phonelive.AppContext;
import com.duomizhibo.phonelive.R;
import com.duomizhibo.phonelive.fragment.video.HomeShouyeFragment;
import com.duomizhibo.phonelive.fragment.video.HomeVideoFragment;
import com.duomizhibo.phonelive.fragment.video.HomeWodeFragment;
import com.duomizhibo.phonelive.fragment.video.HomeXuanchuanFragment;

public class NewMainActivity extends AppCompatActivity implements View.OnClickListener {

    private View ll_shouye;
    private TextView fi_shouye;
    private TextView tv_shouye;
    private View ll_fenlei;
    private TextView fi_fenlei;
    private  TextView tv_fenlei;
    private  View ll_xuanchuan;
    private TextView fi_xuanchuan;
    private  TextView tv_xuanchuan;
    private View ll_wode;
    private TextView fi_wode;
    private TextView tv_wode;

    private RelativeLayout ml;
    private FragmentManager mFragmentManager;
    private int mCurIndex;
    private SparseArray<Fragment>  mSparseArray;
    private HomeShouyeFragment homeShouyeFragment;
    private HomeVideoFragment homeVideoFragment;
    private HomeXuanchuanFragment homeXuanchuanFragment;
    private HomeWodeFragment homeWodeFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_activity_main);
        ll_shouye=findViewById(R.id.ll_shouye);
        fi_shouye=(TextView)findViewById(R.id.fi_shouye);
        tv_shouye=(TextView)findViewById(R.id.tv_shouye);
        ll_fenlei=findViewById(R.id.ll_fenlei);
        fi_fenlei=(TextView)findViewById(R.id.fi_fenlei);
        tv_fenlei=(TextView)findViewById(R.id.tv_fenlei);
        ll_xuanchuan=findViewById(R.id.ll_xuanchuan);
        fi_xuanchuan=(TextView)findViewById(R.id.fi_xuanchuan);
        tv_xuanchuan=(TextView)findViewById(R.id.tv_xuanchuan);
        ll_wode=findViewById(R.id.ll_wode);
        fi_wode=(TextView)findViewById(R.id.fi_wode);
        tv_wode=(TextView) findViewById(R.id.tv_wode);
        ml = (RelativeLayout) findViewById(R.id.rootLayout);
        ll_shouye.setOnClickListener(this);
        tv_shouye.setOnClickListener(this);
        fi_shouye.setOnClickListener(this);

        ll_fenlei.setOnClickListener(this);
        tv_fenlei.setOnClickListener(this);
        fi_fenlei.setOnClickListener(this);

        ll_xuanchuan.setOnClickListener(this);
        tv_xuanchuan.setOnClickListener(this);
        fi_xuanchuan.setOnClickListener(this);

        ll_wode.setOnClickListener(this);
        tv_wode.setOnClickListener(this);
        fi_wode.setOnClickListener(this);
        if(savedInstanceState==null){
            mCurIndex=-1;
            homeShouyeFragment = new HomeShouyeFragment();
            homeVideoFragment = new HomeVideoFragment();
            homeXuanchuanFragment = new HomeXuanchuanFragment();
            homeWodeFragment = new HomeWodeFragment();

            mFragmentManager = getSupportFragmentManager();
            FragmentTransaction ft = mFragmentManager.beginTransaction();
            ft.add(R.id.replaced, homeShouyeFragment);
            ft.add(R.id.replaced, homeVideoFragment);
            ft.add(R.id.replaced, homeXuanchuanFragment);
            ft.add(R.id.replaced, homeWodeFragment);
            ft.show(homeShouyeFragment).hide(homeVideoFragment).hide(homeXuanchuanFragment).hide(homeWodeFragment).commit();

            mSparseArray = new SparseArray<Fragment>();
            mSparseArray.put(0, homeShouyeFragment);
            mSparseArray.put(1, homeVideoFragment);
            mSparseArray.put(2, homeXuanchuanFragment);
            mSparseArray.put(3, homeWodeFragment);
            mCurIndex=-1;
            this.initBottomView(0);
        }
        initData();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ll_shouye:
            case R.id.tv_shouye:
            case R.id.fi_shouye:
                this.initBottomView(0);
                break;
            case R.id.ll_fenlei:
            case R.id.tv_fenlei:
            case R.id.fi_fenlei:
                this.initBottomView(1);
                break;

            case R.id.ll_xuanchuan:
            case R.id.tv_xuanchuan:
            case R.id.fi_xuanchuan:
                this.initBottomView(2);
                break;

            case R.id.ll_wode:
            case R.id.tv_wode:
            case R.id.fi_wode:
                this.initBottomView(3);
                break;

        }
    }

    private long mExitTime;
    @Override
    public void onBackPressed() {

        if ((System.currentTimeMillis() - mExitTime) > 2000) {
            AppContext.showToastShort("再按一次退出程序");
            mExitTime = System.currentTimeMillis();
        } else {
//            Process.killProcess(Process.myPid());
//            System.exit(0);
            finish();
        }
    }

    private void  initBottomView(int position){
        if (position == mCurIndex) {
            return;
        }
        mCurIndex = position;
        fi_shouye.setSelected(false);
        tv_shouye.setSelected(false);
        fi_fenlei.setSelected(false);
        tv_fenlei.setSelected(false);
        fi_xuanchuan.setSelected(false);
        tv_xuanchuan.setSelected(false);
        fi_wode.setSelected(false);
        tv_wode.setSelected(false);
        if(position==0){
            fi_shouye.setSelected(true);
            tv_shouye.setSelected(true);
        }else if(position==1){
            fi_fenlei.setSelected(true);
            tv_fenlei.setSelected(true);
        }else if(position==2){
            fi_xuanchuan.setSelected(true);
            tv_xuanchuan.setSelected(true);
        }else if(position==3){
            fi_wode.setSelected(true);
            tv_wode.setSelected(true);
        }
        if(mFragmentManager==null){
            mFragmentManager=getSupportFragmentManager();
        }
        FragmentTransaction ft = mFragmentManager.beginTransaction();
        if (mSparseArray!=null)
            for (int i = 0; i < mSparseArray.size(); i++) {
                if (position == mSparseArray.keyAt(i)) {
                    ft.show(mSparseArray.valueAt(i));
                } else {
                    ft.hide(mSparseArray.valueAt(i));
                }
            }
        ft.commit();
    }

    public void initData() {

    }


    public interface OnResumeCallback {
        void onResumeRefresh();
    }







}

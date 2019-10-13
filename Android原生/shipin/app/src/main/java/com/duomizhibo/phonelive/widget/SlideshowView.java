package com.duomizhibo.phonelive.widget;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.duomizhibo.phonelive.AppContext;
import com.duomizhibo.phonelive.R;
import com.duomizhibo.phonelive.api.remote.ApiUtils;
import com.duomizhibo.phonelive.api.remote.PhoneLiveApi;
import com.duomizhibo.phonelive.bean.RollPicBean;
import com.duomizhibo.phonelive.utils.TLog;
import com.duomizhibo.phonelive.utils.UIHelper;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;

import okhttp3.Call;

///轮播控件

public class SlideshowView extends FrameLayout {


    //轮播图图片数量
    private final static int IMAGE_COUNT = 2;
    //自动轮播的时间间隔
    private final static int TIME_INTERVAL = 5;
    //自动轮播启用开关
    private final static boolean isAutoPlay = true;

    //自定义轮播图的资源
    private String[] imageUrls;
    //跳转连接
    private String[] imageJumps;
    //放轮播图片的ImageView 的list
    private List<ImageView> imageViewsList;
    //放圆点的View的list
    private List<View> dotViewsList;

    private ViewPager viewPager;
    //当前轮播页
    private int currentItem  = 0;
    //定时任务
    private ScheduledExecutorService scheduledExecutorService;

    private Context context;

    //Handler
    private Handler handler = new Handler(){

        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            super.handleMessage(msg);
            viewPager.setCurrentItem(currentItem);
        }

    };

    public SlideshowView(Context context) {
        this(context,null);
        // TODO Auto-generated constructor stub
    }
    public SlideshowView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        // TODO Auto-generated constructor stub
    }
    public SlideshowView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.context = context;

        initData();
        if(isAutoPlay){
            startPlay();
        }

    }
    /**
     * 开始轮播图切换
     */
    private void startPlay(){
        scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        scheduledExecutorService.scheduleAtFixedRate(new SlideShowTask(), 1, 4, TimeUnit.SECONDS);
    }
    /**
     * 停止轮播图切换
     */
    private void stopPlay(){
        scheduledExecutorService.shutdown();
    }
    /**
     * 初始化相关Data
     */
    private void initData(){
        imageViewsList = new ArrayList<ImageView>();
        dotViewsList = new ArrayList<View>();

    }

    public void addDataToUI(JSONArray rollPics){

        imageUrls = new String[rollPics.length()];
        imageJumps = new String[rollPics.length()];

        try {
            for(int i = 0; i<rollPics.length(); i++){

                RollPicBean rollPicBean = new Gson().fromJson(rollPics.getJSONObject(i).toString(),RollPicBean.class);

                imageUrls[i] = rollPicBean.slide_pic;
                imageJumps[i] = rollPicBean.slide_url;
            }
            initUI(context);

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
    /**
     * 初始化Views等UI
     */
    private void initUI(final Context context){

        if(imageUrls == null || imageUrls.length == 0)
            return;

        LayoutInflater.from(context).inflate(R.layout.layout_slideshow, this, true);

        LinearLayout dotLayout = (LinearLayout)findViewById(R.id.dotLayout);
        dotLayout.removeAllViews();

        // 热点个数与图片特殊相等
        for (int i = 0; i < imageUrls.length; i++) {
            ImageView view =  new ImageView(context);

            if(i == 0)//给一个默认图
                view.setBackgroundResource(R.drawable.default_pic);
            view.setScaleType(ScaleType.FIT_XY);
            imageViewsList.add(view);

            ImageView dotView =  new ImageView(context);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
            params.leftMargin = 4;
            params.rightMargin = 4;
            dotLayout.addView(dotView, params);
            dotViewsList.add(dotView);
        }

        viewPager = (ViewPager) findViewById(R.id.viewPager);
        viewPager.setFocusable(true);

        viewPager.setAdapter(new BannerAdapter());
        viewPager.setOnPageChangeListener(new MyPageChangeListener());
    }

    /**
     * 填充ViewPager的页面适配器
     *
     */
    private class BannerAdapter  extends PagerAdapter{

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            //container.removeView(imageViewsList.get(toRealPosition(position)));
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {


            ImageView imageView = imageViewsList.get(toRealPosition(position));
            //点击跳转连接
            imageView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    UIHelper.showWebView(context, imageJumps[toRealPosition(position)],"hehe");
                }
            });

            Glide
                    .with(AppContext.getInstance())
                    .load(imageUrls[toRealPosition(position)])
                    .centerCrop()
                    .placeholder(R.drawable.default_pic)
                    .crossFade()
                    .into(imageView);
            ViewGroup viewGroup = ((ViewGroup)imageView.getParent());

            if(viewGroup != null){
                viewGroup.removeView(imageView);
            }


            container.addView(imageView);
            return imageView;
        }



        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            if(imageViewsList.size() <= 1 ){
                return 1;
            }
            return Integer.MAX_VALUE;
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            // TODO Auto-generated method stub
            return arg0 == arg1;
        }

        @Override
        public final void finishUpdate(ViewGroup container) {
            // 数量为1，不做position替换
            if (imageViewsList.size() <= 1) {
                return;
            }

            int position = viewPager.getCurrentItem();
            // ViewPager的更新即将完成，替换position，以达到无限循环的效果
            if (position == 0) {
                position = imageViewsList.size();
                viewPager.setCurrentItem(position, false);
            } else if (position == getCount() - 1) {
                position = imageViewsList.size() - 1;
                viewPager.setCurrentItem(position, false);
            }
        }
        public int toRealPosition(int position) {
            int realCount = imageViewsList.size();
            if (realCount == 0)
                return 0;

            int realPosition = position % realCount;
            if(realPosition < 0){
                realPosition += imageViewsList.size();
            }
            return realPosition;
        }

    }



    /**
     * ViewPager的监听器
     * 当ViewPager中页面的状态发生改变时调用
     *
     */
    private class MyPageChangeListener implements OnPageChangeListener{

        boolean isAutoPlay = false;

        @Override
        public void onPageScrollStateChanged(int arg0) {
            // TODO Auto-generated method stub
            switch (arg0) {
                case 1:// 手势滑动，空闲中
                    isAutoPlay = false;
                    break;
                case 2:// 界面切换中
                    isAutoPlay = true;
                    break;
                case 0:// 滑动结束，即切换完毕或者加载完毕
                    // 当前为最后一张，此时从右向左滑，则切换到第一张
                    if (viewPager.getCurrentItem() == viewPager.getAdapter().getCount() - 1 && !isAutoPlay) {
                        viewPager.setCurrentItem(0);
                    }
                    // 当前为第一张，此时从左向右滑，则切换到最后一张
                    else if (viewPager.getCurrentItem() == 0 && !isAutoPlay) {
                        viewPager.setCurrentItem(viewPager.getAdapter().getCount() - 1);
                    }
                    break;
            }
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onPageSelected(int pos) {
            // TODO Auto-generated method stub

            currentItem = pos;
            for(int i = 0; i < dotViewsList.size();i++){
                if(i == (pos%imageViewsList.size())){
                    ((View)dotViewsList.get(i)).setBackgroundResource(R.drawable.dot_focus);
                }else {
                    ((View)dotViewsList.get(i)).setBackgroundResource(R.drawable.dot_blur);
                }
            }
        }

    }

    /**
     *执行轮播图切换任务
     *
     */
    private class SlideShowTask implements Runnable{

        @Override
        public void run() {
            // TODO Auto-generated method stub
            synchronized (viewPager) {
                if(imageViewsList.size() <= 1){
                    return;
                }
                currentItem = currentItem+1;
                handler.obtainMessage().sendToTarget();
            }
        }

    }

}

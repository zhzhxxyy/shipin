package com.duomizhibo.phonelive.fragment.video;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.duomizhibo.phonelive.AppContext;
import com.duomizhibo.phonelive.R;
import com.duomizhibo.phonelive.adapter.video.FomeShouyeAdapter;
import com.duomizhibo.phonelive.api.remote.PhoneLiveApi;
import com.duomizhibo.phonelive.base.AbsFragment;
import com.duomizhibo.phonelive.base.Type;
import com.duomizhibo.phonelive.bean.video.HomeBase;
import com.duomizhibo.phonelive.bean.video.HomeBottom;
import com.duomizhibo.phonelive.bean.video.HomeTop;
import com.duomizhibo.phonelive.ui.activity.NewMainActivity;
import com.duomizhibo.phonelive.utils.TLog;
import com.duomizhibo.phonelive.widget.LoadMoreRecyclerView;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;


public class HomeTestFragment extends AbsFragment implements NewMainActivity.OnResumeCallback,SwipeRefreshLayout.OnRefreshListener,
        FomeShouyeAdapter.OnItemClickListener,LoadMoreRecyclerView.OnLoadMoreListener{

    private View mRootView;
    private SwipeRefreshLayout refreshLayout;
    private LoadMoreRecyclerView recyclerView;
    private FomeShouyeAdapter adapter;
    private Context context;
    private Activity activity;


    private List<HomeBase> list = new ArrayList<>();
    private HomeBase footerItem = new HomeBase();
    private int page = 1;
    private int pageSize = 10;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.new_fragment_home_test, container, false);
        activity = getActivity();
        context = activity.getApplicationContext();
        return mRootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView(savedInstanceState);
    }

    private void initView(Bundle savedInstanceState) {
        refreshLayout = (SwipeRefreshLayout) mRootView.findViewById(R.id.refresh_layout);
        recyclerView = (LoadMoreRecyclerView) mRootView.findViewById(R.id.recycler_view);
        refreshLayout.setColorSchemeResources(R.color.font_orange_color);
        refreshLayout.setOnRefreshListener(this);
        int spanCount = 300;
        GridLayoutManager layoutManager = new GridLayoutManager(activity, spanCount);
        recyclerView.setLayoutManager(layoutManager);

        adapter=new FomeShouyeAdapter(context, activity, list);
        adapter.setOnItemClickListener(this);

        layoutManager.setSpanSizeLookup(adapter.getSpanSizeLookup());
        recyclerView.setAdapter(adapter);
        recyclerView.setOnLoadMoreListener(this);

        footerItem.setType(Type.TYPE_FOOTER_LOAD);
        footerItem.setSpanCount(spanCount);

        onRefresh();
    }



    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);

    }

    @Override
    public void onResumeRefresh() {

    }

    @Override
    public void onRefresh() {
        //页面刷新时候使用
        setRefreshLoading(false);
        page = 1;
        getHomeData();
//        presenter.start(HOME_TOP);
//        recyclerView.setPage(page, bean.getTotalPage());
//        footerItem.setType(page < bean.getTotalPage() ? Type.TYPE_FOOTER_LOAD : Type.TYPE_FOOTER_FULL);
//        list.addAll(list.size() - 1, bean.getData());
//        adapter.notifyDataSetChanged();
//        setRefreshLoading(false);

    }

    @Override
    public void onLoadMore() {
        //加载更多时候使用
//        recyclerView.setLoading(true);
//        refreshLayout.setRefreshing(false);
        setRefreshLoading(true);
        page++;
        getHomeData();
//        presenter.start(HOME_BOTTOM, page, pageSize);
    }




    /**
     * 设置刷新和加载更多的状态
     *
     * @param isLoading 加载更多状态
     */
    private void setRefreshLoading(boolean isLoading) {
        if (!isLoading) {
            recyclerView.setLoading(false);
            refreshLayout.setRefreshing(false);
        }
    }


    //FomeShouyeAdapter 适配器item的点击事件
    @Override
    public void onItemClick(int position) {

//        Intent intent = new Intent();
//        intent.setClass(getActivity(), DetailActivity.class);
//        intent.putExtra(BundleKey.PARCELABLE, list.get(position));
//        startActivity(intent);
    }




    private void getHomeData(){
        TLog.log("=====开始=======","-----------"+page);
        PhoneLiveApi.loginNew("", "", callback);
    }

    private final StringCallback callback = new StringCallback() {
        @Override
        public void onError(Call call, Exception e, int id) {
            setRefreshLoading(false);
            AppContext.showToast("网络请求出错!");

        }
        @Override
        public void onResponse(String s, int id) {

            TLog.log("======结束======","-----------"+page);


            if(page==1){
                HomeTop homeTop= getTop();
                list.clear();
                adapter.setLoopList(homeTop.getCarousel());
                adapter.setHeadlineList(homeTop.getHeadlines());
                list.addAll(homeTop.getList());
                list.add(footerItem);
                adapter.notifyDataSetChanged();
                onLoadMore();
            }else{
                HomeBottom homeBottom=  getBottom(page-1);
                recyclerView.setPage(page-1, homeBottom.getTotalPage());
                footerItem.setType(page-1 < homeBottom.getTotalPage() ? Type.TYPE_FOOTER_LOAD : Type.TYPE_FOOTER_FULL);
                list.addAll(list.size() - 1, homeBottom.getData());
                adapter.notifyDataSetChanged();
                setRefreshLoading(false);
                TLog.log("======结束======","-----page------"+page+"--->total---:"+homeBottom.getTotalPage());
            }
        }
    };


    private HomeTop getTop() {
        HomeTop homeTop = new HomeTop();
        //模拟返回数据
        int spanCount = 300;
        List<HomeTop.Carousel> carouselList = new ArrayList<>();
        carouselList.add(new HomeTop.Carousel(1, "https://img.alicdn.com/simba/img/TB1AhFnPVXXXXa.XFXXSutbFXXX.jpg_q50.jpg"));
        carouselList.add(new HomeTop.Carousel(2, "https://gw.alicdn.com/imgextra/i2/118/TB22AjYg7qvpuFjSZFhXXaOgXXa_!!118-0-yamato.jpg_q50.jpg"));
        carouselList.add(new HomeTop.Carousel(3, "https://gw.alicdn.com/imgextra/i1/34/TB2Kea5fYVkpuFjSspcXXbSMVXa_!!34-0-yamato.jpg_q50.jpg"));
        carouselList.add(new HomeTop.Carousel(4, "https://gw.alicdn.com/imgextra/i1/101/TB2Dz1ScYtlpuFjSspoXXbcDpXa_!!101-0-yamato.jpg_q50.jpg"));
        carouselList.add(new HomeTop.Carousel(5, "https://gw.alicdn.com/imgextra/i4/140/TB2lWFJgmFjpuFjSspbXXXagVXa_!!140-0-yamato.jpg_q50.jpg"));
        carouselList.add(new HomeTop.Carousel(6, "https://aecpm.alicdn.com/tps/i4/TB1pgxYJXXXXXcAXpXXrVZt0FXX-640-200.jpg_q50.jpg"));
        homeTop.setCarousel(carouselList);

        List<HomeBase> headlineList = new ArrayList<>();
        headlineList.add(new HomeBase(6, 0, "", "AndroidNexus微信公众号", HomeBase.TYPE_HEADLINE, spanCount));
        headlineList.add(new HomeBase(7, 0, "", "Github:EasyToForget", HomeBase.TYPE_HEADLINE, spanCount));
        headlineList.add(new HomeBase(8, 0, "", "Email:zhiye.wei@gmail.com", HomeBase.TYPE_HEADLINE, spanCount));
        homeTop.setHeadlines(headlineList);

        List<HomeBase> list = new ArrayList<>();
        //list添加轮播图片
        list.add(new HomeBase(0, 0, "", "", HomeBase.TYPE_CAROUSEL, spanCount));
        //list添加分类
        list.add(new HomeBase(0, 0, "", "", HomeBase.TYPE_CATEGORY, spanCount));
        //list添加头条
        list.add(new HomeBase(0, 0, "", "", HomeBase.TYPE_HEADLINE, spanCount));
        list.add(new HomeBase(0, 0, "", "", HomeBase.TYPE_DIVIDER, spanCount));
        //list添加头部
        list.add(new HomeBase(0, 0, "", "热门直播", HomeBase.TYPE_HEADER, spanCount));

        list.add(new HomeBase(0, 0, "", "", HomeBase.TYPE_PLACE, 12));
        list.add(new HomeBase(9, 0, "https://img.alicdn.com/imgextra/i3/1405905017741074364/TB2q4z8g4BmpuFjSZFsXXcXpFXa_!!0-dgshop.jpg_160x160q90_.webp", "超美春装看这里", HomeBase.TYPE_LIVE, 84));
        list.add(new HomeBase(0, 0, "", "", HomeBase.TYPE_PLACE, 12));
        list.add(new HomeBase(10, 0, "https://img.alicdn.com/imgextra/i4/1425705018312671042/TB2L.RphkqvpuFjSZFhXXaOgXXa_!!0-dgshop.jpg_160x160q90_.webp", "装扮自己", HomeBase.TYPE_LIVE, 84));
        list.add(new HomeBase(0, 0, "", "", HomeBase.TYPE_PLACE, 12));
        list.add(new HomeBase(11, 0, "https://img.alicdn.com/imgextra/i1/1763105018174221191/TB2Mkx7fY0kpuFjy0FjXXcBbVXa_!!0-dgshop.jpg_160x160q90_.webp", "别out了", HomeBase.TYPE_LIVE, 84));
        list.add(new HomeBase(0, 0, "", "", HomeBase.TYPE_PLACE, 12));

        list.add(new HomeBase(0, 0, "", "", HomeBase.TYPE_DIVIDER, spanCount));

        //list添加头部
        list.add(new HomeBase(0, 0, "", "超实惠", HomeBase.TYPE_HEADER, spanCount));

        list.add(new HomeBase(12, 0, "https://img.alicdn.com/i3/1865005016582328322/TB2JOZGgUhnpuFjSZFEXXX0PFXa_!!0-juitemmedia.jpg_200x200.jpg", "非常大牌", HomeBase.TYPE_HOT, 120));
        list.add(new HomeBase(13, 0, "https://img.alicdn.com/i2/1789605013657704257/TB1j9S1PFXXXXbWaXXXXXXXXXXX_!!0-tejia.jpg_200x200.jpg", "全球精选", HomeBase.TYPE_HOT, 60));
        list.add(new HomeBase(14, 0, "https://img.alicdn.com/tps/TB1qA5nPFXXXXXjXpXXXXXXXXXX-320-320.jpg_200x200.jpg", "量贩优选", HomeBase.TYPE_HOT, 60));
        list.add(new HomeBase(15, 0, "https://img.alicdn.com/tps/TB1CbnsOXXXXXbLapXXXXXXXXXX-80-80.jpg_200x200.jpg", "聚名品", HomeBase.TYPE_HOT, 60));

        list.add(new HomeBase(0, 0, "", "", HomeBase.TYPE_DIVIDER, spanCount));

        list.add(new HomeBase(12, 0, "https://img.alicdn.com/i1/1111505010287051046/TB2rvsVexXkpuFjy0FiXXbUfFXa_!!0-juitemmedia.jpg_200x200.jpg", "天天特价", HomeBase.TYPE_HOT, 120));
        list.add(new HomeBase(13, 0, "https://img.alicdn.com/tps/TB1jWr5LVXXXXXOXXXXXXXXXXXX-564-558.png_200x200.jpg", "品牌店庆", HomeBase.TYPE_HOT, 60));
        list.add(new HomeBase(14, 0, "https://img.alicdn.com/i4/1211405014476935415/TB2Id7GadhvOuFjSZFBXXcZgFXa_!!0-juitemmedia.jpg_200x200.jpg", "品牌清仓", HomeBase.TYPE_HOT, 60));
        list.add(new HomeBase(15, 0, "https://gw.alicdn.com/tps/TB1aWHjOVXXXXXeXFXXXXXXXXXX-240-240.jpg_200x200.jpg", "阿里试用", HomeBase.TYPE_HOT, 60));

        list.add(new HomeBase(0, 0, "", "", HomeBase.TYPE_DIVIDER, spanCount));

        list.add(new HomeBase(0, 0, "", "天猫必逛", HomeBase.TYPE_HEADER, spanCount));
        list.add(new HomeBase(16, 0, "https://img.alicdn.com/tfscom/TB1n4cJOVXXXXahaXXXSutbFXXX_200x200.jpg", "全球时尚", HomeBase.TYPE_HOT, 150));
        list.add(new HomeBase(17, 0, "https://img.alicdn.com/tps/TB1Uv7NKFXXXXcUXVXXXXXXXXXX-622-622.jpg_200x200.jpg", "品牌街", HomeBase.TYPE_HOT, 150));

        list.add(new HomeBase(0, 0, "", "", HomeBase.TYPE_DIVIDER, spanCount));

        list.add(new HomeBase(18, 0, "https://img.alicdn.com/tfscom/TB19oVlPFXXXXbkXXXXSutbFXXX_200x200.jpg", "天猫焕新", HomeBase.TYPE_HOT, 75));
        list.add(new HomeBase(19, 0, "https://img.alicdn.com/i1/1111505010287051046/TB2rvsVexXkpuFjy0FiXXbUfFXa_!!0-juitemmedia.jpg_200x200.jpg", "天天搞基", HomeBase.TYPE_HOT, 75));
        list.add(new HomeBase(20, 0, "https://img.alicdn.com/tps/TB1_b6jMVXXXXaqXpXXXXXXXXXX-200-200.jpg_200x200.jpg", "最后一天", HomeBase.TYPE_HOT, 75));
        list.add(new HomeBase(21, 0, "https://img.alicdn.com/tps/TB1gWIvJpXXXXX.aXXXXXXXXXXX-200-200.jpg_200x200.jpg", "Android", HomeBase.TYPE_HOT, 75));

        list.add(new HomeBase(0, 0, "", "", HomeBase.TYPE_DIVIDER, spanCount));

        list.add(new HomeBase(0, 0, "", "特色好货", HomeBase.TYPE_HEADER, spanCount));
        list.add(new HomeBase(22, 0, "https://img.alicdn.com/imgextra/i3/745831182/TB2GUYgXBPzQeBjSZFLXXa3cXXa_!!745831182.jpg_60x60q90.jpg_.webp", "淘票票", HomeBase.TYPE_HOT, 75));
        list.add(new HomeBase(23, 0, "https://img.alicdn.com/tps/TB1CNqwLXXXXXbaXVXXXXXXXXXX-240-130.jpg_200x200q90.jpg_.webp", "淘宝众筹", HomeBase.TYPE_HOT, 75));
        list.add(new HomeBase(24, 0, "https://img.alicdn.com/i2/2/TB1OK3WNVXXXXbPaXXXSutbFXXX.jpg_60x60q90.jpg_.webp", "Nexus 5X", HomeBase.TYPE_HOT, 75));
        list.add(new HomeBase(25, 0, "https://img.alicdn.com/tps/TB1gui8OVXXXXbIXFXXXXXXXXXX-200-200.jpg_200x200.jpg", "拍卖", HomeBase.TYPE_HOT, 75));

        list.add(new HomeBase(0, 0, "", "", HomeBase.TYPE_DIVIDER, spanCount));

        homeTop.setList(list);

        return homeTop;
    }

    private HomeBottom getBottom(int page) {
        HomeBottom homeBottom = new HomeBottom();
        //模拟返回数据
        List<HomeBase> list = new ArrayList<>();
        if (page == 1) {
            list.add(new HomeBase(0, 0, "", "猜你喜欢", HomeBase.TYPE_HEADER, 300));
            list.add(new HomeBase(26, 99, "https://img.alicdn.com/bao/uploaded/i1/TB1sDqKLXXXXXanXXXXXXXXXXXX_!!0-item_pic.jpg_250x250.jpg", "卡米尔夹棉床裙床罩单件加棉床套1.5m1.8米加厚床单席梦思保护套", HomeBase.TYPE_RECOMMEND, 150));
            list.add(new HomeBase(27, 34, "https://img.alicdn.com/bao/uploaded/i2/2171322350/TB2SqaTaNaJ.eBjSsziXXaJ_XXa_!!2171322350.jpg_250x250.jpg", "全棉韩版田园碎花床裙四件套公主风蕾丝花边被套纯棉1.8m床上用品\n", HomeBase.TYPE_RECOMMEND, 150));
            list.add(new HomeBase(26, 544, "https://img.alicdn.com/bao/uploaded/i4/TB1Ke9CJpXXXXa9XpXXXXXXXXXX_!!0-item_pic.jpg_250x250.jpg", "2017免烫春秋男士英伦深灰色修身职业商务正装西服套装潮男西装\n", HomeBase.TYPE_RECOMMEND, 150));
            list.add(new HomeBase(26, 554, "https://asearch.alicdn.com/bao/uploaded/i2/159530341092325317/TB2mMMbaHBnpuFjSZFGXXX51pXa_!!0-saturn_solar.jpg_250x250.jpg", "现货 Huawei/华为 Mate 9 Pro全网通银钻灰 保时捷", HomeBase.TYPE_RECOMMEND, 150));
            list.add(new HomeBase(26, 77, "https://img.alicdn.com/bao/uploaded/i3/20821802/TB2S7E9abVkpuFjSspcXXbSMVXa_!!20821802.jpg_250x250.jpg", "各品牌平板修理电脑ipad维修安卓mid刷机升级固件芯片维修换屏\n", HomeBase.TYPE_RECOMMEND, 150));
            list.add(new HomeBase(26, 66, "https://img.alicdn.com/bao/uploaded/i1/1846357812/TB21uctkpXXXXX4XpXXXXXXXXXX_!!1846357812.jpg_250x250.jpg", "代写文章工作总结自我介绍竞聘演讲简报心得体会证婚词调查问卷", HomeBase.TYPE_RECOMMEND, 150));
            list.add(new HomeBase(26, 88, "https://img.alicdn.com/bao/uploaded/i2/TB1UypnPVXXXXXzXFXXXXXXXXXX_!!2-item_pic.png_250x250.jpg", "Mate9Pro现货Huawei/华为 Mate 9 Pro 6GB+128GB全网通保时捷现货", HomeBase.TYPE_RECOMMEND, 150));
            list.add(new HomeBase(26, 3300, "https://img.alicdn.com/bao/uploaded/i2/TB1cA.aPFXXXXc2XFXXXXXXXXXX_!!0-item_pic.jpg_250x250.jpg", "莱虎秋季新款时尚休闲男士西服马夹韩版修身西装马甲背心男英伦潮", HomeBase.TYPE_RECOMMEND, 150));
            list.add(new HomeBase(26, 999, "https://img.alicdn.com/bao/uploaded/i7/TB10A15PXXXXXa5XVXXYXGcGpXX_M2.SS2_250x250.jpg", "SXT 工作人员购买专区", HomeBase.TYPE_RECOMMEND, 150));
            list.add(new HomeBase(26, 655, "https://img.alicdn.com/bao/uploaded/i4/TB1ZQjAJFXXXXXEXFXXXXXXXXXX_!!0-item_pic.jpg_250x250.jpg", "梦蔻 全棉蕾丝床笠 韩式花边布艺床裙床罩 床单 1.51.8米 多花色", HomeBase.TYPE_RECOMMEND, 150));
        } else if (page == 2) {
            list.add(new HomeBase(26, 666, "https://img.alicdn.com/bao/uploaded/i1/TB1eKIaPXXXXXXiapXXXXXXXXXX_!!0-item_pic.jpg_250x250.jpg", "梦蔻 全棉蕾丝床笠 韩式花边布艺床裙床罩 床单 1.51.8米 多花色", HomeBase.TYPE_RECOMMEND, 150));
            list.add(new HomeBase(26, 97779, "https://img.alicdn.com/bao/uploaded/i1/1846357812/TB21uctkpXXXXX4XpXXXXXXXXXX_!!1846357812.jpg_250x250.jpg", "梦蔻 全棉蕾丝床笠 韩式花边布艺床裙床罩 床单 1.51.8米 多花色", HomeBase.TYPE_RECOMMEND, 150));
            list.add(new HomeBase(26, 89089, "https://img.alicdn.com/bao/uploaded/i7/TB10A15PXXXXXa5XVXXYXGcGpXX_M2.SS2_250x250.jpg", "梦蔻 全棉蕾丝床笠 韩式花边布艺床裙床罩 床单 1.51.8米 多花色", HomeBase.TYPE_RECOMMEND, 150));
            list.add(new HomeBase(26, 111, "https://img.alicdn.com/bao/uploaded/i1/TB1eKIaPXXXXXXiapXXXXXXXXXX_!!0-item_pic.jpg_250x250.jpg", "代写文章工作总结自我介绍竞聘演讲简报心得体会证婚词调查问卷", HomeBase.TYPE_RECOMMEND, 150));
            list.add(new HomeBase(26, 222, "https://img.alicdn.com/bao/uploaded/i2/TB1cA.aPFXXXXc2XFXXXXXXXXXX_!!0-item_pic.jpg_250x250.jpg", "代写文章工作总结自我介绍竞聘演讲简报心得体会证婚词调查问卷", HomeBase.TYPE_RECOMMEND, 150));
            list.add(new HomeBase(26, 333, "https://img.alicdn.com/bao/uploaded/i4/TB1ZQjAJFXXXXXEXFXXXXXXXXXX_!!0-item_pic.jpg_250x250.jpg", "各品牌平板修理电脑ipad维修安卓mid刷机升级固件芯片维修换屏", HomeBase.TYPE_RECOMMEND, 150));
            list.add(new HomeBase(26, 444, "https://img.alicdn.com/bao/uploaded/i2/TB1UypnPVXXXXXzXFXXXXXXXXXX_!!2-item_pic.png_250x250.jpg", "各品牌平板修理电脑ipad维修安卓mid刷机升级固件芯片维修换屏", HomeBase.TYPE_RECOMMEND, 150));
            list.add(new HomeBase(26, 9559, "https://img.alicdn.com/bao/uploaded/i1/TB1eKIaPXXXXXXiapXXXXXXXXXX_!!0-item_pic.jpg_250x250.jpg", "各品牌平板修理电脑ipad维修安卓mid刷机升级固件芯片维修换屏", HomeBase.TYPE_RECOMMEND, 150));
            list.add(new HomeBase(26, 678, "https://img.alicdn.com/bao/uploaded/i7/TB10A15PXXXXXa5XVXXYXGcGpXX_M2.SS2_250x250.jpg", "春季英伦修身西装马甲男士西服马夹背心商务休闲职业正装韩版潮", HomeBase.TYPE_RECOMMEND, 150));
            list.add(new HomeBase(26, 888, "https://img.alicdn.com/bao/uploaded/i1/TB1eKIaPXXXXXXiapXXXXXXXXXX_!!0-item_pic.jpg_250x250.jpg", "春季英伦修身西装马甲男士西服马夹背心商务休闲职业正装韩版潮", HomeBase.TYPE_RECOMMEND, 150));
        } else {
            list.add(new HomeBase(26, 46, "https://img.alicdn.com/bao/uploaded/i2/TB1cA.aPFXXXXc2XFXXXXXXXXXX_!!0-item_pic.jpg_250x250.jpg", "春季英伦修身西装马甲男士西服马夹背心商务休闲职业正装韩版潮", HomeBase.TYPE_RECOMMEND, 150));
            list.add(new HomeBase(26, 55, "https://img.alicdn.com/bao/uploaded/i4/TB1ZQjAJFXXXXXEXFXXXXXXXXXX_!!0-item_pic.jpg_250x250.jpg", "莱虎秋季新款时尚休闲男士西服马夹韩版修身西装马甲背心男英伦潮", HomeBase.TYPE_RECOMMEND, 150));
            list.add(new HomeBase(26, 32, "https://img.alicdn.com/bao/uploaded/i2/81209255/TB2wk8GfCXlpuFjy0FeXXcJbFXa_!!81209255.jpg_250x250.jpg", "莱虎秋季新款时尚休闲男士西服马夹韩版修身西装马甲背心男英伦潮", HomeBase.TYPE_RECOMMEND, 150));
            list.add(new HomeBase(26, 1, "https://img.alicdn.com/bao/uploaded/i2/2001343287/TB2sC8wpXXXXXbAXXXXXXXXXXXX_!!2001343287.jpg_250x250.jpg", "适用Google Nexus5触摸屏LG D820 D821 5X显示屏幕总成外屏换维修", HomeBase.TYPE_RECOMMEND, 150));
            list.add(new HomeBase(26, 43, "https://img.alicdn.com/bao/uploaded/i2/2001343287/TB2sC8wpXXXXXbAXXXXXXXXXXXX_!!2001343287.jpg_250x250.jpg", "适用Google Nexus5触摸屏LG D820 D821 5X显示屏幕总成外屏换维修", HomeBase.TYPE_RECOMMEND, 150));
            list.add(new HomeBase(26, 55, "https://asearch.alicdn.com/bao/uploaded/i1/1356505014527885209/TB23M5cgstnpuFjSZFKXXalFFXa_!!0-saturn_solar.jpg_250x250.jpg", "三星c5正品Samsung/三星 Galaxy C5 SM-C5000 C7000 全网通4G手机", HomeBase.TYPE_RECOMMEND, 150));
            list.add(new HomeBase(26, 44, "https://img.alicdn.com/bao/uploaded/i2/TB1vkF9MpXXXXb9XXXXXXXXXXXX_!!0-item_pic.jpg_250x250.jpg", "三星c5正品Samsung/三星 Galaxy C5 SM-C5000 C7000 全网通4G手机", HomeBase.TYPE_RECOMMEND, 150));
        }


        homeBottom.setData(list);
        homeBottom.setMsg("");
        homeBottom.setTotalPage(3);
        return homeBottom;
    }
}

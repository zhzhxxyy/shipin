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

import com.duomizhibo.phonelive.AppConfig;
import com.duomizhibo.phonelive.AppContext;
import com.duomizhibo.phonelive.R;
import com.duomizhibo.phonelive.adapter.video.HomeShouyeAdapter;
import com.duomizhibo.phonelive.api.remote.ApiUtils;
import com.duomizhibo.phonelive.api.remote.PhoneLiveApi;
import com.duomizhibo.phonelive.base.AbsFragment;
import com.duomizhibo.phonelive.base.Type;
import com.duomizhibo.phonelive.bean.video.BannerObject;
import com.duomizhibo.phonelive.bean.video.BaseData;

import com.duomizhibo.phonelive.bean.video.HomeObject;
import com.duomizhibo.phonelive.bean.video.HomeVideo;
import com.duomizhibo.phonelive.bean.video.NoticeObject;
import com.duomizhibo.phonelive.bean.video.VideoObject;
import com.duomizhibo.phonelive.ui.activity.NewMainActivity;
import com.duomizhibo.phonelive.utils.StringUtils;
import com.duomizhibo.phonelive.utils.TLog;
import com.duomizhibo.phonelive.utils.UIHelper;
import com.duomizhibo.phonelive.widget.LoadMoreRecyclerView;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;


public class HomeShouyeFragment extends AbsFragment implements NewMainActivity.OnResumeCallback,SwipeRefreshLayout.OnRefreshListener,
        HomeShouyeAdapter.OnItemClickListener,LoadMoreRecyclerView.OnLoadMoreListener{

    private View mRootView;
    private View view_fail;
    private SwipeRefreshLayout refreshLayout;
    private LoadMoreRecyclerView recyclerView;
    private HomeShouyeAdapter adapter;
    private Context context;
    private Activity activity;

    private List<VideoObject> list = new ArrayList<VideoObject>();
    private VideoObject footerItem = new VideoObject();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.new_fragment_home_shouye, container, false);
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
        view_fail =  mRootView.findViewById(R.id.view_fail);
        refreshLayout = (SwipeRefreshLayout) mRootView.findViewById(R.id.refresh_layout);
        recyclerView = (LoadMoreRecyclerView) mRootView.findViewById(R.id.recycler_view);
        refreshLayout.setColorSchemeResources(R.color.refresh_layout_color);
        refreshLayout.setOnRefreshListener(this);
        int spanCount = 300;
        GridLayoutManager layoutManager = new GridLayoutManager(activity, spanCount);
        recyclerView.setLayoutManager(layoutManager);

        adapter=new HomeShouyeAdapter(context, activity, list);
        adapter.setOnItemClickListener(this);

        layoutManager.setSpanSizeLookup(adapter.getSpanSizeLookup());
        recyclerView.setAdapter(adapter);
        recyclerView.setOnLoadMoreListener(this);

        footerItem.setType(Type.TYPE_FOOTER_LOAD);
        footerItem.setSpanCount(spanCount);

        String tx= AppConfig.getAppConfig(getActivity()).get("home.index");
        if(!StringUtils.isEmpty(tx)){
            try {
                HomeObject homeObject=com.alibaba.fastjson.JSONObject.parseObject(tx, HomeObject.class);
                if(homeObject!=null){
                    dataToView(homeObject);
                }
            } catch (Exception e) {

            }
        }
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
        getHomeData();
    }

    @Override
    public void onLoadMore() {
        //加载更多时候使用
//        recyclerView.setLoading(true);
//        refreshLayout.setRefreshing(false);
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

        if(VideoObject.TYPE_HEADER==list.get(position).getType()){
            UIHelper.showVideoSearchActivity(getActivity());
        }else{
            UIHelper.showVideoPlayerNew(getActivity(),list.get(position));
        }

    }


    private void getHomeData(){
        view_fail.setVisibility(View.GONE);
        PhoneLiveApi.home(AppContext.getInstance().getToken(), callback);
    }

    private final StringCallback callback = new StringCallback() {
        @Override
        public void onError(Call call, Exception e, int id) {
            setRefreshLoading(false);
            AppContext.showToast("网络请求出错!");
            if(list.isEmpty()){
                view_fail.setVisibility(View.VISIBLE);
            }

        }
        @Override
        public void onResponse(String s, int id) {
            BaseData baseData= ApiUtils.getBaseData(s);
            if(!baseData.isSuccess()){
                setRefreshLoading(false);
                AppContext.showToast(baseData.getRespMsg());
                if(list.isEmpty()){
                    view_fail.setVisibility(View.VISIBLE);
                }
                return ;
            }
            HomeObject homeObject=null;
            try {
                 homeObject=com.alibaba.fastjson.JSONObject.parseObject(baseData.getData(), HomeObject.class);
            } catch (Exception e) {

            }
            if(homeObject==null){
                setRefreshLoading(false);
                return;
            }
            AppConfig.getAppConfig(getActivity()).set("home.index",baseData.getData());
            dataToView(homeObject);
        }
    };



    private void dataToView( HomeObject homeObject){
        view_fail.setVisibility(View.GONE);
        int spanCount = 300;
        List<BannerObject> banner=  homeObject.getBanner();
        List<NoticeObject> notice =homeObject.getNotice();
        List<HomeVideo> video=homeObject.getVideo();
        list.clear();
        adapter.setLoopList(banner);
        adapter.setNoticeList(notice);

        //list添加轮播图片
        if(banner!=null&&banner.size()>0){
            VideoObject bannerVideo=new VideoObject();
            bannerVideo.setType(VideoObject.TYPE_CAROUSEL);
            bannerVideo.setSpanCount(spanCount);
            list.add(bannerVideo);
        }


        //list添加公告
        if(notice!=null&&notice.size()>0){
            VideoObject noticeVideo=new VideoObject();
            noticeVideo.setType(VideoObject.TYPE_HEADLINE);
            noticeVideo.setSpanCount(spanCount);
            list.add(noticeVideo);
        }

        if(video!=null&&video.size()>0){
            //添加视频
            for(int i=0;i<video.size();i++){
                //分割线
                VideoObject dividerVideo=new VideoObject();
                dividerVideo.setType(VideoObject.TYPE_DIVIDER);
                dividerVideo.setSpanCount(spanCount);
                list.add(dividerVideo);  //添加分割线

                VideoObject headerVideo=new VideoObject();
                headerVideo.setType(VideoObject.TYPE_HEADER);
                headerVideo.setSpanCount(spanCount);
                headerVideo.setTitle(video.get(i).getName());
                list.add(headerVideo);  //添加头部

                List<VideoObject> temp= video.get(i).getList();
                list.addAll(temp);
            }
        }
        list.add(footerItem);
        footerItem.setType(Type.TYPE_FOOTER_FULL);
        adapter.notifyDataSetChanged();
        setRefreshLoading(false);
    }

}

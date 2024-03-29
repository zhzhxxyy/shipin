package com.duomizhibo.phonelive.fragment.video;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.duomizhibo.phonelive.AppConfig;
import com.duomizhibo.phonelive.AppContext;
import com.duomizhibo.phonelive.R;
import com.duomizhibo.phonelive.adapter.video.HomeVideoAdapter;
import com.duomizhibo.phonelive.api.remote.ApiUtils;
import com.duomizhibo.phonelive.api.remote.PhoneLiveApi;
import com.duomizhibo.phonelive.base.BaseFragment;
import com.duomizhibo.phonelive.bean.video.BaseData;
import com.duomizhibo.phonelive.bean.video.VideoClass;
import com.duomizhibo.phonelive.bean.video.VideoClassList;
import com.duomizhibo.phonelive.bean.video.VideoList;
import com.duomizhibo.phonelive.bean.video.VideoObject;

import com.duomizhibo.phonelive.ui.activity.NewMainActivity;
import com.duomizhibo.phonelive.ui.customviews.RefreshLayout;
import com.duomizhibo.phonelive.utils.StringUtils;
import com.zhy.http.okhttp.callback.StringCallback;
import java.util.ArrayList;
import java.util.List;
import okhttp3.Call;


public class HomeVideoFragment extends BaseFragment implements NewMainActivity.OnResumeCallback,RefreshLayout.OnRefreshListener {

    private View ll_fenlei;
    private LinearLayout ll_fenlei_add;
    private View ll_biaoqian;
    private LinearLayout ll_biaoqian_add;
    private View ll_paixu;
    private LinearLayout ll_paixu_add;
    private View tv_more;
    RefreshLayout mRefreshLayout;
    RecyclerView mRecyclerView;
    private View view_fail;
    private TextView tv_fail;
    private List<VideoObject> mUserList = new ArrayList<>();
    private List<VideoClass> fenleiClass = new ArrayList<VideoClass>();
    private List<VideoClass> biaoqianClass = new ArrayList<VideoClass>();
    private List<VideoClass> paixuClass = new ArrayList<VideoClass>();

    private int page=1;
    private int cid=0;
    private int sub_cid=0;
    private int tag_id=0;
    private String orderCode="lastTime";//最新 lastTime  最热 hot 推荐 reco
    private HomeVideoAdapter adapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.new_fragment_home_video, null);
        initView(view);
        initData();
        return view;
    }

    @Override
    public void initData() {
        fenleiClass.clear();
        VideoClass itemFenlei=new VideoClass();
        itemFenlei.setName("全部");
        itemFenlei.setId(0);
        fenleiClass.add(itemFenlei);

        biaoqianClass.clear();
        VideoClass itemBiaoqian=new VideoClass();
        itemBiaoqian.setName("全部");
        itemBiaoqian.setId(0);
        biaoqianClass.add(itemBiaoqian);


        paixuClass.clear();
        VideoClass paixu1=new VideoClass();
        paixu1.setName("最新");
        paixu1.setId(0);
        paixuClass.add(itemBiaoqian);

        VideoClass paixu2=new VideoClass();
        paixu2.setName("最热");
        paixu2.setId(1);
        paixuClass.add(paixu2);

        VideoClass paixu3=new VideoClass();
        paixu3.setName("推荐");
        paixu3.setId(2);
        paixuClass.add(paixu3);

        addViewToLinearLayout(ll_fenlei_add,fenleiClass,0);
        addViewToLinearLayout(ll_biaoqian_add,biaoqianClass,1);
        addViewToLinearLayout(ll_paixu_add,paixuClass,2);

        String tx= AppConfig.getAppConfig(getActivity()).get("Video.class");
        if(!StringUtils.isEmpty(tx)){
            try {
                VideoClassList temp=com.alibaba.fastjson.JSONObject.parseObject(tx, VideoClassList.class);
                if(temp!=null){
                    videoClassToView(temp);
                }
            } catch (Exception e) {
            }
        }
        getVideoClassData();
        mRefreshLayout.beginRefresh();
    }

    @Override
    public void initView(View view) {

        ll_fenlei=view.findViewById(R.id.ll_fenlei);
        ll_fenlei_add=(LinearLayout)view.findViewById(R.id.ll_fenlei_add);
        ll_biaoqian=view.findViewById(R.id.ll_biaoqian);
        ll_biaoqian_add=(LinearLayout)view.findViewById(R.id.ll_biaoqian_add);
        ll_paixu=view.findViewById(R.id.ll_paixu);
        ll_paixu_add=(LinearLayout)view.findViewById(R.id.ll_paixu_add);
        tv_more=view.findViewById(R.id.tv_more);
        mRefreshLayout=(RefreshLayout)view.findViewById(R.id.mRefreshLayout);
        mRecyclerView=(RecyclerView)view.findViewById(R.id.mRecyclerView);
        view_fail=view.findViewById(R.id.view_fail);
        tv_fail=(TextView)view.findViewById(R.id.tv_fail);

        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        mRefreshLayout.setScorllView(mRecyclerView);
        mRefreshLayout.setOnRefreshListener(this);
        adapter = new HomeVideoAdapter(getActivity(), mUserList);
        mRecyclerView.setAdapter(adapter);
        tv_more.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        if(v.getId()==R.id.tv_more){
           if(ll_paixu.getVisibility()==View.VISIBLE){
               ll_biaoqian.setVisibility(View.GONE);
               ll_paixu.setVisibility(View.GONE);
           }else{
               ll_biaoqian.setVisibility(View.VISIBLE);
               ll_paixu.setVisibility(View.VISIBLE);
           }
        }
    }



    @Override
    public void onRefresh() {
        page=1;
        PhoneLiveApi.videoList(page,10,cid,sub_cid,tag_id,"",AppContext.getInstance().getToken(),orderCode,mLoadMoreCallback);
    }

    @Override
    public void onLoadMore() {
        page++;
        PhoneLiveApi.videoList(page,10,cid,sub_cid,tag_id,"",AppContext.getInstance().getToken(),orderCode,mLoadMoreCallback);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onResumeRefresh() {

    }




    private void addViewToLinearLayout(LinearLayout ll ,List<VideoClass> list,int type){
        //0 分类 1 标签 2 排序
        ll.removeAllViews();
        LayoutInflater inflater=LayoutInflater.from(getActivity());
        int color_ec4b4b=getActivity().getResources().getColor(R.color.color_ec4b4b);
        int color_888=getActivity().getResources().getColor(R.color.color_888);
        for(int i=0;i<list.size();i++){
            TextView textView = (TextView)inflater.inflate(R.layout.adapter_video_class_item,null);
            textView.setText(list.get(i).getName()+"");
            boolean isChoice=false;
            if(type==2){
                if("lastTime".equals(orderCode)&&list.get(i).getId()==0){
                    isChoice=true;
                }else if("hot".equals(orderCode)&&list.get(i).getId()==1){
                    isChoice=true;
                }else if("reco".equals(orderCode)&&list.get(i).getId()==2){
                    isChoice=true;
                }
            }else if(type==1){
                if(tag_id==list.get(i).getId()){
                    isChoice=true;
                }
            }else{
                if(cid==list.get(i).getId()){
                    isChoice=true;
                }
            }
            if(isChoice){
                textView.setTextColor(color_ec4b4b);
            }else{
                textView.setTextColor(color_888);
            }
            textView.setTag(R.id.tag_first,list.get(i));
            textView.setTag(R.id.tag_second,type);
            textView.setOnClickListener(classViewClick);
            ll.addView(textView);
        }



    }

    private void changeViewColor(LinearLayout ll,String name){
        int color_ec4b4b=getActivity().getResources().getColor(R.color.color_ec4b4b);
        int color_888=getActivity().getResources().getColor(R.color.color_888);
        int count= ll.getChildCount();
        for(int i=0;i<count;i++){
           TextView tv= (TextView)ll.getChildAt(i);
            if(tv.getText().equals(name)){
                tv.setTextColor(color_ec4b4b);
            }else{
                tv.setTextColor(color_888);
            }
        }
    }

    private View.OnClickListener classViewClick=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            VideoClass item=(VideoClass)v.getTag(R.id.tag_first);
            int type=(int)v.getTag(R.id.tag_second);
            if(type==2){
                String orderCodeTemp;
                if(item.getId()==2){
                     orderCodeTemp="reco";
                }else if(item.getId()==1){
                     orderCodeTemp="hot";
                }else{
                     orderCodeTemp="lastTime";
                }
                if(!orderCodeTemp.equals(orderCode)){
                    orderCode=orderCodeTemp;
                    changeViewColor(ll_paixu_add,item.getName());
                    mRefreshLayout.beginRefresh();
                }
            }else if(type==1){
                if(tag_id!=item.getId()){
                    tag_id=item.getId();
                    changeViewColor(ll_biaoqian_add,item.getName());
                    mRefreshLayout.beginRefresh();
                }
            }else{
                if(cid!=item.getId()){
                    cid=item.getId();
                    changeViewColor(ll_fenlei_add,item.getName());
                    mRefreshLayout.beginRefresh();
                }
            }
        }
    };



    //获取分类的数据
    private void getVideoClassData(){
        PhoneLiveApi.videoClass(AppContext.getInstance().getToken(), new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {

            }

            @Override
            public void onResponse(String response, int id) {
                BaseData baseData= ApiUtils.getBaseData(response);
                VideoClassList temp=null;
                if(baseData.isSuccess()){
                    try {
                        temp=com.alibaba.fastjson.JSONObject.parseObject(baseData.getData(), VideoClassList.class);
                    } catch (Exception e) {

                    }
                }
                if(temp==null){
                    return;
                }
                AppConfig.getAppConfig(getActivity()).set("Video.class",baseData.getData());
                videoClassToView(temp);
            }
        });
    }


    private void videoClassToView(VideoClassList temp){
        if(temp.getClass_list()!=null&&temp.getClass_list().size()>0){
            fenleiClass.clear();
            VideoClass itemFenlei=new VideoClass();
            itemFenlei.setName("全部");
            itemFenlei.setId(0);
            fenleiClass.add(itemFenlei);
            fenleiClass.addAll(temp.getClass_list());
            addViewToLinearLayout(ll_fenlei_add,fenleiClass,0);
        }

        if(temp.getTag_list()!=null&&temp.getTag_list().size()>0){
            biaoqianClass.clear();
            VideoClass itemBiaoqian=new VideoClass();
            itemBiaoqian.setName("全部");
            itemBiaoqian.setId(0);
            biaoqianClass.add(itemBiaoqian);
            biaoqianClass.addAll(temp.getTag_list());
            addViewToLinearLayout(ll_biaoqian_add,biaoqianClass,1);
        }
    }


    private StringCallback mLoadMoreCallback=new StringCallback() {
        @Override
        public void onError(Call call, Exception e, int id) {
            mRefreshLayout.completeRefresh();
            if(mUserList.size()>0){
                view_fail.setVisibility(View.GONE);
                mRecyclerView.setVisibility(View.VISIBLE);
            }else{
                mRecyclerView.setVisibility(View.INVISIBLE);
                tv_fail.setText("获取数据失败");
                view_fail.setVisibility(View.VISIBLE);
            }
            AppContext.toast("获取数据失败");
        }

        @Override
        public void onResponse(String response, int id) {
            mRefreshLayout.completeRefresh();
            BaseData baseData= ApiUtils.getBaseData(response);
            VideoList videoList=null;
            if(!baseData.isSuccess()){
                //todo 失败
                tv_fail.setText(baseData.getRespMsg());
            }else{
                try {
                    videoList=com.alibaba.fastjson.JSONObject.parseObject(baseData.getData(), VideoList.class);
                } catch (Exception e) {

                }
            }
            if(page==1){
                mUserList.clear();
            }
            if(videoList!=null&&videoList.getRows()!=null&&videoList.getRows().size()>0){
                mUserList.addAll(videoList.getRows());
            }else{
                AppContext.toast("已经没有更多数据了");
            }
            adapter.setData(mUserList);
            if(mUserList.size()>0){
                view_fail.setVisibility(View.GONE);
                mRecyclerView.setVisibility(View.VISIBLE);
            }else{
                mRecyclerView.setVisibility(View.INVISIBLE);
                tv_fail.setText("暂无数据");
                view_fail.setVisibility(View.VISIBLE);
            }
        }
    };

}

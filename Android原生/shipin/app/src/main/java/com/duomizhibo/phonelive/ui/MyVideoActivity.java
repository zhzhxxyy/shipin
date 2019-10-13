package com.duomizhibo.phonelive.ui;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.duomizhibo.phonelive.AppContext;
import com.duomizhibo.phonelive.R;
import com.duomizhibo.phonelive.adapter.VideoAdapter2;
import com.duomizhibo.phonelive.api.remote.ApiUtils;
import com.duomizhibo.phonelive.api.remote.PhoneLiveApi;
import com.duomizhibo.phonelive.base.ToolBarBaseActivity;
import com.duomizhibo.phonelive.bean.ActiveBean;
import com.duomizhibo.phonelive.ui.customviews.ActivityTitle;
import com.duomizhibo.phonelive.ui.customviews.RefreshLayout;
import com.duomizhibo.phonelive.ui.other.OnItemEvent;
import com.duomizhibo.phonelive.utils.StringUtils;
import com.duomizhibo.phonelive.widget.HeaderGridView;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.List;

import butterknife.InjectView;
import okhttp3.Call;

/**
 * 我的视频
 */
public class MyVideoActivity extends ToolBarBaseActivity implements RefreshLayout.OnRefreshListener {
     private int mPage;
    @InjectView(R.id.view_title)
    ActivityTitle mActivityTitle;

    @InjectView(R.id.gv_newest)
    RecyclerView mRecyclerView;

    @InjectView(R.id.sl_newest)
    RefreshLayout mRefresh;

    @InjectView(R.id.newest_load)
    LinearLayout mFailure;

    @InjectView(R.id.no)
    View mNoVideo;

    private Gson mGson;

    private Type mType;

    private VideoAdapter2 mAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_myvideo;
    }

    @Override
    public void initView() {
        mActivityTitle.setVisibility(View.VISIBLE);
        mActivityTitle.setTitle("我的视频");
        mActivityTitle.setReturnListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        mRefresh.setOnRefreshListener(this);
        mGson = new Gson();
        mType = new TypeToken<List<ActiveBean>>() {
        }.getType();
        mRefresh.setScorllView(mRecyclerView);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this,2,GridLayoutManager.VERTICAL,false));
    }

    @Override
    public void initData() {

    }

    private void requestData() {
        mPage=1;
        PhoneLiveApi.getMyVideo(mPage,AppContext.getInstance().getLoginUid(), AppContext.getInstance().getToken(), new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                mRefresh.completeRefresh();
                if (mFailure.getVisibility() == View.GONE) {
                    mFailure.setVisibility(View.VISIBLE);
                }
                if(mAdapter!=null){
                    mAdapter.clear();
                }
            }

            @Override
            public void onResponse(String response, int id) {

              mRefresh.completeRefresh();
                if (mFailure.getVisibility() == View.VISIBLE) {
                    mFailure.setVisibility(View.GONE);
                }

                try {
                    JSONObject obj = new JSONObject(response);
                    if ("200".equals(obj.getString("ret"))) {
                        JSONObject data = obj.getJSONObject("data");
                        if (0 == data.getInt("code")) {
                            List<ActiveBean> list = mGson.fromJson(data.getString("info"), mType);
                            if (list.size() > 0) {
                                if(mNoVideo.getVisibility()==View.VISIBLE){
                                    mNoVideo.setVisibility(View.GONE);
                                }
                                if (mAdapter == null) {
                                    mAdapter = new VideoAdapter2(MyVideoActivity.this, list);

                                    mRecyclerView.setAdapter(mAdapter);
                                }else{
                                    mAdapter.setData(list);
                                }
                            } else {
                                if(mNoVideo.getVisibility()==View.GONE){
                                    mNoVideo.setVisibility(View.VISIBLE);
                                     if (mAdapter!=null){
                                         mAdapter.setData(list);
                                     }
                                }
                            }
                        }else{
                            AppContext.toast(data.getString("msg"));
                        }
                    } else {
                        AppContext.toast("获取数据失败");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        OkHttpUtils.getInstance().cancelTag("getNewestUserList");


    }

    @Override
    protected boolean hasActionBar() {
        return false;
    }

    @Override
    public void onRefresh() {
        requestData();
    }

    @Override
    public void onLoadMore() {
        mPage++;
        PhoneLiveApi.getMyVideo(mPage,AppContext.getInstance().getLoginUid(), AppContext.getInstance().getToken(),mLoadMorePage);

    }
  private StringCallback mLoadMorePage=new StringCallback() {
      @Override
      public void onError(Call call, Exception e, int id) {
          mRefresh.completeRefresh();
          if (mFailure.getVisibility() == View.GONE) {
              mFailure.setVisibility(View.VISIBLE);
          }
          if(mAdapter!=null){
              mAdapter.clear();
          }
      }

      @Override
      public void onResponse(String response, int id) {
        mRefresh.completeRefresh();
          if (mFailure.getVisibility() == View.VISIBLE) {
              mFailure.setVisibility(View.GONE);
          }

          try {
              JSONObject obj = new JSONObject(response);
              if ("200".equals(obj.getString("ret"))) {
                  JSONObject data = obj.getJSONObject("data");
                  if (0 == data.getInt("code")) {
                      List<ActiveBean> moreData = mGson.fromJson(data.getString("info"), mType);
                      if (moreData.size() > 0) {
                          if (mAdapter != null) {
                             mAdapter.insertList(moreData);
                          }
                      } else {
                          AppContext.toast("已经没有更多数据了");
                      }
                  }

              } else {
                  AppContext.toast("获取数据失败");
              }
          } catch (JSONException e) {
              e.printStackTrace();
          }
      }
  };
    @Override
    protected void onResume() {
        super.onResume();
        requestData();
    }
}

package com.duomizhibo.phonelive.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hyphenate.chat.EMChatManager;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.duomizhibo.phonelive.AppContext;
import com.duomizhibo.phonelive.R;
import com.duomizhibo.phonelive.adapter.CommentReplyAdapter;
import com.duomizhibo.phonelive.api.remote.PhoneLiveApi;
import com.duomizhibo.phonelive.bean.CommentBean;
import com.duomizhibo.phonelive.bean.UserInfo;
import com.duomizhibo.phonelive.interf.OnItemClickListener;
import com.duomizhibo.phonelive.ui.customviews.RefreshLayout;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.List;

import okhttp3.Call;

/**
 * Created by cxf on 2017/9/7.
 */

public class ReplyActivity extends AppCompatActivity implements RefreshLayout.OnRefreshListener, View.OnClickListener {

    private RefreshLayout mRefreshLayout;
    private RecyclerView mRecyclerView;
    private EditText mInput;
    private CommentBean mHostBean;
    private Gson mGson;
    private Type mType;
    private CommentReplyAdapter mAdapter;
    private int mPage = 1;
    private InputMethodManager imm;
    private UserInfo mCurReplyUser;
    private String mParentId = "0";
    private String mCurCommentId = "0";
    private String mEMContent;//发送环信的内容
    private String mVideoId;
    private EMChatManager mChatManager;
    private String mReplys = "";
    private String mUid;
    private View mLoading;
    private final int EMPTY = 0;
    private final int NOT_EMPTY = 1;
    private int status = EMPTY;
    private TextView mBtnSend;
    private Drawable mDrawable1;
    private Drawable mDrawable2;
    private int mCommentPosition;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reply);
        initView();
        initData();
    }

    private void initView() {
        mRefreshLayout = (RefreshLayout) findViewById(R.id.refreshLayout);
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mRefreshLayout.setOnRefreshListener(this);
        mRefreshLayout.setScorllView(mRecyclerView);
        mLoading = findViewById(R.id.loading);
        mGson = new Gson();
        mType = new TypeToken<List<CommentBean>>() {
        }.getType();
        mInput = (EditText) findViewById(R.id.comment_edit);
        mInput.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEND) {
                    sendComment();
                }
                return false;
            }
        });
        mBtnSend = (TextView) findViewById(R.id.btn_send);
        mDrawable1 = ContextCompat.getDrawable(this, R.drawable.bg_comment_btn_send);
        mDrawable2 = ContextCompat.getDrawable(this, R.drawable.bg_comment_btn_send2);
        mInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    if (status == EMPTY) {
                        status = NOT_EMPTY;
                        mBtnSend.setBackgroundDrawable(mDrawable2);
                        mBtnSend.setTextColor(0xffffffff);
                    }
                } else {
                    if (status == NOT_EMPTY) {
                        status = EMPTY;
                        mBtnSend.setBackgroundDrawable(mDrawable1);
                        mBtnSend.setTextColor(0xff808080);
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        findViewById(R.id.btn_close).setOnClickListener(this);
        findViewById(R.id.btn_send).setOnClickListener(this);
        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        Intent intent = getIntent();
        mUid = AppContext.getInstance().getLoginUid();
        mHostBean = (CommentBean) intent.getSerializableExtra("host");
        mVideoId = intent.getStringExtra("videoId");
        mCommentPosition = intent.getIntExtra("p", -1);
        mCurReplyUser = mHostBean.getUserinfo();
        mCurCommentId = mHostBean.getCommentid();
        mParentId = mHostBean.getId();
        mInput.setHint("回复" + mCurReplyUser.getUser_nicename());
        mChatManager = EMClient.getInstance().chatManager();
    }

    private void initData() {
        PhoneLiveApi.getReplys(mHostBean.getId(), String.valueOf(mPage), mCallback);
    }

    private StringCallback mCallback = new StringCallback() {
        @Override
        public void onError(Call call, Exception e, int id) {
            mRefreshLayout.completeRefresh();
            if (mLoading.getVisibility() == View.VISIBLE) {
                mLoading.setVisibility(View.GONE);
            }
            AppContext.toast("加载失败");
        }

        @Override
        public void onResponse(String response, int id) {
            if (mLoading.getVisibility() == View.VISIBLE) {
                mLoading.setVisibility(View.GONE);
            }
            mRefreshLayout.completeRefresh();
            try {
                JSONObject obj = new JSONObject(response);
                if ("200".equals(obj.getString("ret"))) {
                    JSONObject data = obj.getJSONObject("data");
                    if (0 == data.getInt("code")) {
                        List<CommentBean> list = mGson.fromJson(data.getString("info"), mType);
                        if (mAdapter == null) {
                            mAdapter = new CommentReplyAdapter(ReplyActivity.this, mHostBean, list,mCommentPosition);
                            mAdapter.setOnItemClickListener(new OnItemClickListener<CommentBean>() {
                                @Override
                                public void onItemClick(CommentBean bean) {
                                    if (mUid.equals(bean.getUid())) {
                                        return;
                                    }
                                    mCurReplyUser = bean.getUserinfo();
                                    mCurCommentId = bean.getCommentid();
                                    mParentId = bean.getId();
                                    mInput.setHint("回复" + mCurReplyUser.getUser_nicename());
                                    mInput.requestFocus();
                                    imm.showSoftInput(mInput, InputMethodManager.SHOW_FORCED);
                                }
                            });
                            mRecyclerView.setAdapter(mAdapter);
                        } else {
                            mAdapter.refreshList(list);
                            if (!"".equals(mReplys)) {
                                mAdapter.setReplyCount(mReplys);
                            }
                        }
                    } else {
                        AppContext.toast(data.getString("msg"));
                    }
                } else {
                    AppContext.toast("获取数据失败");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };

    private StringCallback mLoadMoreCallback = new StringCallback() {
        @Override
        public void onError(Call call, Exception e, int id) {
            mRefreshLayout.completeLoadMore();
            if (mLoading.getVisibility() == View.VISIBLE) {
                mLoading.setVisibility(View.GONE);
            }
            AppContext.toast("加载失败");
        }

        @Override
        public void onResponse(String response, int id) {
            if (mLoading.getVisibility() == View.VISIBLE) {
                mLoading.setVisibility(View.GONE);
            }
            mRefreshLayout.completeLoadMore();
            try {
                JSONObject obj = new JSONObject(response);
                if ("200".equals(obj.getString("ret"))) {
                    JSONObject data = obj.getJSONObject("data");
                    if (0 == data.getInt("code")) {
                        List<CommentBean> list = mGson.fromJson(data.getString("info"), mType);
                        if (list.size() > 0) {
                            mAdapter.insertList(list);
                        } else {
                            AppContext.toast("已无更多数据");
                        }
                    } else {
                        AppContext.toast(data.getString("msg"));
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
    public void onRefresh() {
        mPage = 1;
        initData();
    }

    @Override
    public void onLoadMore() {
        mPage++;
        PhoneLiveApi.getReplys(mHostBean.getId(), String.valueOf(mPage), mLoadMoreCallback);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_close:
                finish();
                break;
            case R.id.btn_send:
                sendComment();
                break;
        }
    }

    private void sendComment() {
        if (mUid.equals(mCurReplyUser.getId())) {
            AppContext.toast("不能回复自己的评论");
            return;
        }
        String content = mInput.getText().toString();
        if ("".equals(content)) {
            Toast.makeText(this, "评论内容不能为空", Toast.LENGTH_SHORT).show();
        }
        mEMContent = "回复 " + mCurReplyUser.getUser_nicename() + ": " + content;
        imm.hideSoftInputFromWindow(mInput.getWindowToken(), 0); //强制隐藏键盘
        PhoneLiveApi.setComment(mCurReplyUser.getId(), mVideoId, content, mCurCommentId, mParentId, mSetCommentCallback);
        mInput.setText("");
        if (mLoading.getVisibility() == View.GONE) {
            mLoading.setVisibility(View.VISIBLE);
        }
    }

    private StringCallback mSetCommentCallback = new StringCallback() {

        @Override
        public void onError(Call call, Exception e, int id) {
        }

        @Override
        public void onResponse(String response, int id) {
            try {
                JSONObject obj = new JSONObject(response);
                if ("200".equals(obj.getString("ret"))) {
                    JSONObject data = obj.getJSONObject("data");
                    if (0 == data.getInt("code")) {
                        JSONObject info0 = data.getJSONArray("info").getJSONObject(0);
                        sendEMMessage(info0.getString("isattent"), mEMContent, mCurReplyUser.getId());
                        mReplys = info0.getString("replys");
                        mCurReplyUser = mHostBean.getUserinfo();
                        mCurCommentId = mHostBean.getCommentid();
                        mParentId = mHostBean.getId();
                        mInput.setHint("回复" + mCurReplyUser.getUser_nicename());
                        Intent intent = getIntent();
                        intent.putExtra("reply", mReplys);
                        setResult(RESULT_OK, intent);
                        finish();
                    }
                    AppContext.toast(data.getString("msg"));
                } else {
                    AppContext.toast("获取数据失败");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };


    public void sendEMMessage(String isfollow, String content, String touid) {
        Log.e("sendEMMessage", "sendEMMessage:-----------> " +mCurReplyUser.getUser_nicename()+mCurReplyUser.getId());
        EMMessage message = EMMessage.createTxtSendMessage(content, touid);
        message.setAttribute("isfollow", isfollow);
        mChatManager.sendMessage(message);
    }
}

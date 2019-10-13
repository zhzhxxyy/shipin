package com.duomizhibo.phonelive.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.duomizhibo.phonelive.AppContext;
import com.duomizhibo.phonelive.R;
import com.duomizhibo.phonelive.adapter.CommentAdapter;
import com.duomizhibo.phonelive.api.remote.PhoneLiveApi;
import com.duomizhibo.phonelive.base.AbsDialogFragment;
import com.duomizhibo.phonelive.bean.ActiveBean;
import com.duomizhibo.phonelive.bean.CommentBean;
import com.duomizhibo.phonelive.bean.UserInfo;
import com.duomizhibo.phonelive.event.DianzanEvent;
import com.duomizhibo.phonelive.ui.ReplyActivity;
import com.duomizhibo.phonelive.ui.SmallVideoPlayerActivity;
import com.duomizhibo.phonelive.ui.customviews.RefreshLayout;
import com.duomizhibo.phonelive.utils.TDevice;
import com.zhy.http.okhttp.callback.StringCallback;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.List;

import okhttp3.Call;

/**
 * Created by cxf on 2017/7/14.
 */

public class CommentFragment extends AbsDialogFragment implements View.OnClickListener, RefreshLayout.OnRefreshListener {

    private Context mContext;
    private View mRootView;
    private RecyclerView mRecyclerView;
    private CommentAdapter mAdapter;
    private TextView mCommentCount;
    private ActiveBean mActiveBean;
    private InputMethodManager imm;
    private Type mType;
    private Gson mGson;
    private int mHeight;
    private int mTempHeight;
    private EditText mEditText;
    private String mCurCommentId = "0";
    private RefreshLayout mRefreshLayout;
    private int mPage;
    private UserInfo mCurReplyUser;
    private String mParentId = "0";
    private String mEMContent;//发送环信的内容
    private String mUid;
    private int mPosition = -1;
    private String mCommentNum;
    private View mLoading;
    private final int EMPTY = 0;
    private final int NOT_EMPTY = 1;
    private int status = EMPTY;
    private TextView mBtnSend;
    private Drawable mDrawable1;
    private Drawable mDrawable2;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        mContext = getActivity();
        Dialog dialog = new Dialog(mContext, R.style.BottomViewTheme_Transparent);
        mRootView = LayoutInflater.from(mContext).inflate(R.layout.dialog_video_comment, null, false);
        dialog.setContentView(mRootView);
        dialog.setCanceledOnTouchOutside(true);
        Window window = dialog.getWindow();
        //window.setWindowAnimations(R.style.BottomToTopAnim);
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        mHeight = (int) TDevice.dpToPixel(400);
        mTempHeight = mHeight;
        params.height = mTempHeight;
        params.gravity = Gravity.BOTTOM;
        window.setAttributes(params);
        return dialog;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
        initData();
    }

    @Override
    public void onPause() {
        super.onPause();
        ((SmallVideoPlayerActivity) mContext).setCommentNum(mCommentNum);
    }

    private void initView() {
        Bundle bundle = getArguments();
        mActiveBean = (ActiveBean) bundle.getSerializable("bean");
        mRootView.findViewById(R.id.close).setOnClickListener(this);
        mRootView.findViewById(R.id.btn_send).setOnClickListener(this);
        mRecyclerView = (RecyclerView) mRootView.findViewById(R.id.listView);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));

        mRefreshLayout = (RefreshLayout) mRootView.findViewById(R.id.refreshLayout);
        mRefreshLayout.setScorllView(mRecyclerView);
        mRefreshLayout.setOnRefreshListener(this);

        mEditText = (EditText) mRootView.findViewById(R.id.comment_edit);
        mEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEND) {
                    sendComment();
                }
                return false;
            }
        });
        mBtnSend = (TextView) mRootView.findViewById(R.id.btn_send);
        mDrawable1 = ContextCompat.getDrawable(mContext, R.drawable.bg_comment_btn_send);
        mDrawable2 = ContextCompat.getDrawable(mContext, R.drawable.bg_comment_btn_send2);
        mEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEND) {
                    sendComment();
                    dismiss();
                }
                return false;
            }
        });
        mEditText.addTextChangedListener(new TextWatcher() {
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
        mLoading = mRootView.findViewById(R.id.loading);
        mCommentCount = (TextView) mRootView.findViewById(R.id.nums);
        mType = new TypeToken<List<CommentBean>>() {
        }.getType();
        mGson = new Gson();
        mUid = AppContext.getInstance().getLoginUid();
        imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        EventBus.getDefault().register(this);
    }

    private void resize() {
        Dialog dialog = getDialog();
        if (dialog != null) {
            Window window = getDialog().getWindow();
            WindowManager.LayoutParams params = window.getAttributes();
            params.height = mTempHeight;
            window.setAttributes(params);
            if (mPosition != -1) {
                mRecyclerView.scrollToPosition(mPosition);
            }
        }
    }

    public void onSoftInputShow(int height) {
        if (mTempHeight != height) {
            mTempHeight = height;
            resize();
        }
    }

    public void onSoftInputHide() {
        if (mTempHeight != mHeight) {
            mTempHeight = mHeight;
            resize();
            mCurCommentId = "0";
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.close:
                dismiss();
                break;
            case R.id.btn_send:
                sendComment();
                break;
        }
    }

    private void sendComment() {
        String content = mEditText.getText().toString();
        String touid = "";
        if ("".equals(content)) {
            Toast.makeText(getActivity(), "评论内容不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        if (mCurReplyUser != null) {
            mEMContent = "回复 " + mCurReplyUser.getUser_nicename() + ": " + content;
            touid = mCurReplyUser.getId();
        }
        imm.hideSoftInputFromWindow(mEditText.getWindowToken(), 0); //强制隐藏键盘
        PhoneLiveApi.setComment(touid, mActiveBean.getId(), content, mCurCommentId, mParentId, mSetCommentCallback);
        mEditText.setText("");
        if (mLoading.getVisibility() == View.GONE) {
            mLoading.setVisibility(View.VISIBLE);
        }
    }

    private StringCallback mSetCommentCallback = new StringCallback() {

        @Override
        public void onError(Call call, Exception e, int id) {
            AppContext.toast("评论失败");
        }

        @Override
        public void onResponse(String response, int id) {
            try {
                JSONObject obj = new JSONObject(response);
                if ("200".equals(obj.getString("ret"))) {
                    JSONObject data = obj.getJSONObject("data");
                    if (0 == data.getInt("code")) {
                        JSONObject info0 = data.getJSONArray("info").getJSONObject(0);
                        if (mCurReplyUser != null) {
                            sendEMMessage(info0.getString("isattent"), mEMContent);
                            mCurReplyUser = null;
                            mParentId = "0";
                            mCurCommentId = "0";
                        }
                        mEditText.setHint("说点什么...");
                        mCommentNum = info0.getString("comments");
                        ((SmallVideoPlayerActivity) mContext).setCommentNum(mCommentNum);
                        dismiss();
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


    private void sendEMMessage(String isfollow, String content) {
        Log.e("sendEMMessage", "sendEMMessage:-----------> " +mCurReplyUser.getUser_nicename()+mCurReplyUser.getId());
        ((SmallVideoPlayerActivity) getActivity()).sendEMMessage(isfollow, content, mCurReplyUser.getId());
    }

    public void initData() {
        mPage = 1;
        PhoneLiveApi.getComments(mActiveBean.getId(), String.valueOf(mPage), mCallback);
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
            mRefreshLayout.completeRefresh();
            if (mLoading.getVisibility() == View.VISIBLE) {
                mLoading.setVisibility(View.GONE);
            }
            try {
                JSONObject obj = new JSONObject(response);
                if ("200".equals(obj.getString("ret"))) {
                    JSONObject data = obj.getJSONObject("data");
                    if (0 == data.getInt("code")) {
                        JSONObject info0 = data.getJSONArray("info").getJSONObject(0);
                        mCommentNum = info0.getString("comments");
                        mCommentCount.setText("全部评论(" + mCommentNum + ")");
                        List<CommentBean> list = mGson.fromJson(info0.getString("commentlist"), mType);
                        if (mAdapter == null) {
                            mAdapter = new CommentAdapter(mContext, list);
                            mAdapter.setOnItemClickListener(new CommentAdapter.OnItemClickListener() {
                                @Override
                                public void OnItemClick(CommentBean bean, int position) {
                                    if (!bean.getUid().equals(mUid)) {
                                        mCurReplyUser = bean.getUserinfo();
                                        mCurCommentId = bean.getCommentid();
                                        mParentId = bean.getId();
                                        mEditText.setHint("回复" + mCurReplyUser.getUser_nicename());
                                        mEditText.requestFocus();
                                        imm.showSoftInput(mEditText, InputMethodManager.SHOW_FORCED);
                                        mPosition = position;
                                    }
                                }

                                @Override
                                public void OnMoreClick(CommentBean bean, int position) {
                                    forwardReplyActivity(bean, position);
                                }
                            });
                            mRecyclerView.setAdapter(mAdapter);
                        } else {
                            mAdapter.refreshList(list);
                            if (mPosition != -1) {
                                mRecyclerView.scrollToPosition(mPosition);
                                mPosition = -1;
                            } else {
                                mRecyclerView.scrollToPosition(0);
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


    private void forwardReplyActivity(CommentBean bean, int position) {
        Intent intent = new Intent(mContext, ReplyActivity.class);
        intent.putExtra("host", bean);
        intent.putExtra("videoId", mActiveBean.getId());
        intent.putExtra("p", position);
        startActivityForResult(intent, 100);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 100 && resultCode == Activity.RESULT_OK) {
            int position = data.getIntExtra("p", -1);
            if (position >= 0 && position < mAdapter.getItemCount()) {
                String reply = data.getStringExtra("reply");
                if (!"".equals(reply)) {
                    mAdapter.getList().get(position).setReplys(Integer.parseInt(reply));
                }
                mAdapter.notifyItemChanged(position);
            }
        }
    }

    private StringCallback mloadMoreCallback = new StringCallback() {
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
                        JSONObject info0 = data.getJSONArray("info").getJSONObject(0);
                        mCommentNum = info0.getString("comments");
                        mCommentCount.setText("全部评论(" + mCommentNum + ")");
                        List<CommentBean> list = mGson.fromJson(info0.getString("commentlist"), mType);
                        if (list.size() > 0) {
                            if (mAdapter != null) {
                                mAdapter.insertList(list);
                            }
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
        initData();
    }

    @Override
    public void onLoadMore() {
        mPage++;
        PhoneLiveApi.getComments(mActiveBean.getId(), String.valueOf(mPage), mloadMoreCallback);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onDianzan(DianzanEvent e) {
        int position = e.getPosition();
        if (position >= 0 && position < mAdapter.getItemCount()) {
            CommentBean bean = mAdapter.getList().get(position);
            bean.setLikes(e.getLikes());
            bean.setIslike(e.getIsLike());
            mAdapter.notifyItemChanged(position);
        }
    }
}

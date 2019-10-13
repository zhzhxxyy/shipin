package com.duomizhibo.phonelive.fragment;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
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

import com.duomizhibo.phonelive.AppContext;
import com.duomizhibo.phonelive.R;
import com.duomizhibo.phonelive.api.remote.PhoneLiveApi;
import com.duomizhibo.phonelive.base.AbsDialogFragment;
import com.duomizhibo.phonelive.bean.ActiveBean;
import com.duomizhibo.phonelive.ui.SmallVideoPlayerActivity;
import com.duomizhibo.phonelive.utils.TDevice;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.Call;

/**
 * Created by cxf on 2017/9/9.
 */

public class CommentDialogFragment extends AbsDialogFragment implements View.OnClickListener {

    private Context mContext;
    private View mRootView;
    private EditText mEditText;
    private String mEMContent;//发送环信的内容
    private ActiveBean mActiveBean;
    private String mCurCommentId;
    private InputMethodManager imm;
    private Handler mHandler;
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
        mRootView = LayoutInflater.from(mContext).inflate(R.layout.fragment_comment_dialog, null);
        Dialog dialog = new Dialog(mContext, R.style.BottomViewTheme_Transparent);
        dialog.setContentView(mRootView);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);
        Window window = dialog.getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = (int) TDevice.dpToPixel(50);
        params.gravity = Gravity.BOTTOM;
        window.setAttributes(params);
        return dialog;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (mActiveBean == null) {
            Bundle bundle = getArguments();
            mActiveBean = (ActiveBean) bundle.getSerializable("bean");
        }
        mEditText = (EditText) mRootView.findViewById(R.id.comment_edit);
        mCurCommentId = mActiveBean.getUid();
        mBtnSend = (TextView) mRootView.findViewById(R.id.btn_send);
        mDrawable1 = ContextCompat.getDrawable(mContext, R.drawable.bg_comment_btn_send);
        mDrawable2 = ContextCompat.getDrawable(mContext, R.drawable.bg_comment_btn_send2);
        mEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEND) {
                    sendComment();
//                    dismiss();
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
        imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (mHandler == null) {
            mHandler = new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                }
            };
        }
        mRootView.findViewById(R.id.btn_send).setOnClickListener(this);
    }

    private void sendComment() {
        String content = mEditText.getText().toString();
        if ("".equals(content)) {
            Toast.makeText(getActivity(), "请添加内容后在尝试", Toast.LENGTH_SHORT).show();
        }
        mEMContent = "回复 " + mActiveBean.getUserinfo().getUser_nicename() + ": " + content;
        if (!"".equals(content)) {
            dismiss();
            PhoneLiveApi.setComment(mActiveBean.getUid(), mActiveBean.getId(), content, "0", "0", mSetCommentCallback);
        }
        mEditText.setText("");

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
                    JSONObject info0 = data.getJSONArray("info").getJSONObject(0);
                    if (0 == data.getInt("code")) {
                        sendEMMessage(info0.getString("isattent"), mEMContent);
                        ((SmallVideoPlayerActivity) mContext).setCommentNum(info0.getString("comments"));
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
        ((SmallVideoPlayerActivity) mContext).sendEMMessage(isfollow, content, mCurCommentId);
    }

    @Override
    public void onResume() {
        super.onResume();
        //不加延时，软键盘不会自动弹出
        mHandler.sendEmptyMessageDelayed(0, 200);
    }

    @Override
    public void onPause() {
        super.onPause();
        imm.hideSoftInputFromWindow(getActivity().getWindow().getDecorView().getWindowToken(), 0);
    }

    @Override
    public void onClick(View v) {
        sendComment();
//        dismiss();
    }
}

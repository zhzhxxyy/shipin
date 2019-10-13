package com.duomizhibo.phonelive.fragment;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.duomizhibo.phonelive.AppConfig;
import com.duomizhibo.phonelive.AppContext;
import com.duomizhibo.phonelive.R;
import com.duomizhibo.phonelive.api.remote.ApiUtils;
import com.duomizhibo.phonelive.api.remote.PhoneLiveApi;
import com.duomizhibo.phonelive.bean.ActiveBean;
import com.duomizhibo.phonelive.bean.UserBean;
import com.duomizhibo.phonelive.ui.SmallVideoPlayerActivity;
import com.duomizhibo.phonelive.utils.ShareUtils;
import com.duomizhibo.phonelive.utils.TDevice;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.FileCallBack;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import cn.sharesdk.facebook.Facebook;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.tencent.qzone.QZone;
import cn.sharesdk.twitter.Twitter;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;
import okhttp3.Call;

/**
 * Created by cxf on 2017/9/7.
 */
@SuppressLint("ValidFragment")
public class VideoShareFragment extends DialogFragment implements View.OnClickListener {

    private Context mContext;
    private View mRootView;
    private LinearLayout mShareGroup;
    private Map<String, Integer> mMap;
    private UserBean mUser;
    private ActiveBean mActiveBean;
    public interface deleteClick {
        void delete();
    }
    public deleteClick mDeleteClick;
    public VideoShareFragment(deleteClick mDeleteClick) {
        this.mDeleteClick = mDeleteClick;
    }
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        mContext = getActivity();
        mRootView = LayoutInflater.from(mContext).inflate(R.layout.fragment_video_share, null);
        Dialog dialog = new Dialog(mContext, R.style.BottomViewTheme_Transparent);
        dialog.setContentView(mRootView);
        dialog.setCanceledOnTouchOutside(true);
        Window window = dialog.getWindow();
        window.setWindowAnimations(R.style.BottomToTopAnim);
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = (int) TDevice.dpToPixel(280);
        params.gravity = Gravity.BOTTOM;
        window.setAttributes(params);
        return dialog;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mActiveBean = (ActiveBean) getArguments().getSerializable("bean");
        mMap = new HashMap<>();
        mMap.put("qq", R.drawable.icon_share_qq);
        mMap.put("qzone", R.drawable.icon_share_qzone);
        mMap.put("wx", R.drawable.icon_share_wx);
        mMap.put("wchat", R.drawable.icon_share_pyq);
        mMap.put("facebook", R.drawable.icon_share_facebook);
        mMap.put("twitter", R.drawable.icon_share_twitter);
        mUser = AppContext.getInstance().getLoginUser();
        initView();
    }

    private void initView() {
        mShareGroup = (LinearLayout) mRootView.findViewById(R.id.share_group);
        for (int i = 0; i < AppConfig.SHARE_TYPE.length(); i++) {
            final ImageView imageView = new ImageView(mContext);
            LinearLayout.LayoutParams p = new LinearLayout.LayoutParams((int) TDevice.dpToPixel(90), ViewGroup.LayoutParams.MATCH_PARENT);
            imageView.setLayoutParams(p);
            int padding = (int) TDevice.dpToPixel(10);
            imageView.setPadding(padding, padding, padding, padding);
            try {
                imageView.setImageResource(mMap.get(AppConfig.SHARE_TYPE.getString(i)));
            } catch (JSONException e) {
                e.printStackTrace();
            }

            mShareGroup.addView(imageView);
            final int finalI = i;
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    shareVideo(finalI);
                    dismiss();
                }
            });
        }

        if (mUser.id.equals(mActiveBean.getUid())) {//自己
            View delete = mRootView.findViewById(R.id.btn_delete);
            delete.setVisibility(View.VISIBLE);
            delete.setOnClickListener(this);
        } else {
            View black = mRootView.findViewById(R.id.btn_black);
            black.setVisibility(View.VISIBLE);
            black.setOnClickListener(this);
            View report = mRootView.findViewById(R.id.btn_report);
            report.setVisibility(View.VISIBLE);
            report.setOnClickListener(this);
        }
        mRootView.findViewById(R.id.btn_link).setOnClickListener(this);
        mRootView.findViewById(R.id.btn_save).setOnClickListener(this);
        mRootView.findViewById(R.id.btn_cancel).setOnClickListener(this);
    }

    private void shareVideo(int index) {
        try {
            String names = "";
            String s = AppConfig.SHARE_TYPE.getString(index);
            if (s.equals("qq")) {
                names = QQ.NAME;
            } else if (s.equals("qzone")) {
                names = QZone.NAME;
            } else if (s.equals("wchat")) {
                names = WechatMoments.NAME;
            } else if (s.equals("wx")) {
                names = Wechat.NAME;
            } else if (s.equals("facebook")) {
                names = Facebook.NAME;
            } else if (s.equals("twitter")) {
                names = Twitter.NAME;
            }
            String des = mActiveBean.getTitle();
            if ("".equals(des)) {
                des = AppContext.getInstance().getLoginUser().user_nicename + AppConfig.VIDEO_SHARE_DES;
            }
            ShareUtils.share(AppContext.getInstance(), names, AppConfig.VIDEO_SHARE_TITLE
                    , des, null, mActiveBean.getThumb(), AppConfig.MAIN_URL + "/index.php?g=appapi&m=video&a=index&videoid=" + mActiveBean.getId(), new PlatformActionListener() {
                        @Override
                        public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
                            PhoneLiveApi.addShare(mActiveBean.getId(), new StringCallback() {
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
                                                ((SmallVideoPlayerActivity) mContext).setShareCount(info0.getString("shares"));
                                            }
                                            AppContext.toast(data.getString("msg"));
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                        }

                        @Override
                        public void onError(Platform platform, int i, Throwable throwable) {
                            Log.e("分享", "onComplete: ---->分享失败");
                            AppContext.toast("分享失败");
                        }

                        @Override
                        public void onCancel(Platform platform, int i) {
                            Log.e("分享", "onComplete: ---->分享取消");
                            AppContext.toast("分享取消");
                        }
                    });
        } catch (Exception e) {
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_delete:
                delete();
                break;
            case R.id.btn_report:
                report();
                break;
            case R.id.btn_black:
                setblack();
                break;
            case R.id.btn_link:
                copyLink();
                break;
            case R.id.btn_save:
                downLoad();
                break;
            case R.id.btn_cancel:
                dismiss();
                break;
        }
    }

    /**
     * 拉黑
     */
    private void setblack() {
        dismiss();
        PhoneLiveApi.setVideoBlack(mActiveBean.getId(), new StringCallback() {
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
                            ((SmallVideoPlayerActivity)mContext).finish();
                        }
                        AppContext.toast(data.getString("msg"));
                    } else {
                        AppContext.toast("拉黑失败");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void copyLink() {
        ClipboardManager cm = (ClipboardManager) mContext.getSystemService(Context.CLIPBOARD_SERVICE);
        // 将文本内容放到系统剪贴板里。
        cm.setText(mActiveBean.getHref());
        AppContext.toast("复制成功");
        dismiss();
    }

    private void downLoad() {
        dismiss();
        ((SmallVideoPlayerActivity) mContext).showLoadingDialog();
        String href = mActiveBean.getHref();
        String fileName = href.substring(href.lastIndexOf("/"));
        OkHttpUtils.get().url(href).build().execute(new FileCallBack(AppConfig.DEFAULT_SAVE_FILE_PATH, fileName) {
            @Override
            public void onError(Call call, Exception e, int id) {
                AppContext.toast("下载失败");
            }

            @Override
            public void onResponse(File response, int id) {
                ((SmallVideoPlayerActivity) mContext).hideLoadingDialog();
                AppContext.toast("下载成功");
            }
        });
    }

    private void report() {
        PhoneLiveApi.setVideoReport(mUser.id, AppContext.getInstance().getToken(), mActiveBean.getId(), new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {

            }

            @Override
            public void onResponse(String response, int id) {
                JSONArray res = ApiUtils.checkIsSuccess(response);
                if (res != null) {
                    AppContext.toast("感谢您的举报,我们会尽快做出处理...");
                    dismiss();
                }
            }
        });
    }

    private void delete() {
        PhoneLiveApi.setVideoRel(mUser.id, AppContext.getInstance().getToken(), mActiveBean.getId(), new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {

            }

            @Override
            public void onResponse(String response, int id) {
                JSONArray res = ApiUtils.checkIsSuccess(response);
                if (res != null) {
                    dismiss();
                    mDeleteClick.delete();
                    AppContext.toast("删除成功");
                    getActivity().finish();
                }
            }
        });
    }
}

package com.duomizhibo.phonelive.ui;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.duomizhibo.phonelive.AppContext;
import com.duomizhibo.phonelive.R;
import com.duomizhibo.phonelive.api.remote.ApiUtils;
import com.duomizhibo.phonelive.api.remote.PhoneLiveApi;
import com.duomizhibo.phonelive.base.ToolBarBaseActivity;
import com.duomizhibo.phonelive.bean.LiveJson;
import com.duomizhibo.phonelive.bean.UserBean;
import com.duomizhibo.phonelive.interf.DialogInterface;
import com.duomizhibo.phonelive.ui.dialog.LiveCommon;
import com.duomizhibo.phonelive.utils.LiveUtils;
import com.duomizhibo.phonelive.utils.UIHelper;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import butterknife.InjectView;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * 私播
 */
public class VideoInfoActivity extends ToolBarBaseActivity {
    @InjectView(R.id.iv_bg)
    protected ImageView iv_bg;
    @InjectView(R.id.tv_name)
    protected TextView tv_name;
    @InjectView(R.id.iv_level)
    protected ImageView iv_level;
    @InjectView(R.id.iv_sex)
    protected ImageView iv_sex;
    @InjectView(R.id.tv_num)
    protected TextView tv_num;
    @InjectView(R.id.tv_address)
    protected TextView tv_address;
    @InjectView(R.id.tv_des)
    protected TextView tv_des;

    LiveJson bean;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_videoinfo;
    }

    @Override
    public void initView() {
        bean = getIntent().getParcelableExtra("VIDEO");
        if (bean != null) {
            Glide.with(this).load(bean.thumb).into(iv_bg);
            tv_name.setText(bean.user_nicename + "(ID:" + bean.uid + ")");
            iv_level.setImageResource(LiveUtils.getAnchorLevelRes(bean.level_anchor));
            tv_address.setText(bean.city);
            tv_num.setText(bean.age);
            tv_des.setText(bean.title);
            if (bean.sex.equals("2")) {
                iv_sex.setImageResource(R.drawable.girl);
            } else {
                iv_sex.setImageResource(R.drawable.boy);
            }
        }
    }

    @Override
    public void initData() {

    }

    @OnClick({R.id.tv_sendvideo, R.id.tv_sendinfo, R.id.iv_ret})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_sendvideo:
                sendLianmai();
                break;
            case R.id.tv_sendinfo:
                Intent intent = new Intent(VideoInfoActivity.this, ChatRoomActivity.class);
                UserBean userBean = new UserBean();
                userBean.id = bean.uid;
                userBean.user_nicename = bean.user_nicename;
                userBean.avatar = bean.avatar;
                intent.putExtra("user", userBean);
                startActivity(intent);
                break;
            case R.id.iv_ret:
                onBackPressed();
                break;

        }
    }

    @Override
    protected boolean hasActionBar() {
        return false;
    }

    private void sendLianmai() {
        PhoneLiveApi.checkoutRoom(AppContext.getInstance().getLoginUid()
                , AppContext.getInstance().getToken(), bean.stream, bean.uid, new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(String response, int id) {
                        try {
                            JSONObject res = new JSONObject(response);
                            if (Integer.parseInt(res.getString("ret")) == 200) {
                                JSONObject dataJson = res.getJSONObject("data");
                                String code = dataJson.getString("code");
                                if (code.equals("700")) {

                                    //AppManager.getAppManager().finishAllActivity();
                                    Intent intent = new Intent(AppContext.getInstance(), PhoneLoginActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    AppContext.getInstance().startActivity(intent);

                                } else if (code.equals("1009")) {
                                    LiveCommon.showPersonDialog(VideoInfoActivity.this, "1", "1", dataJson.get("msg").toString(), "", "取消", "", new DialogInterface() {
                                        @Override
                                        public void cancelDialog(View v, Dialog d) {
                                            d.dismiss();
                                        }

                                        @Override
                                        public void determineDialog(View v, Dialog d) {

                                        }
                                    });
                                } else if (code.equals("0")) {
                                    final JSONArray jsonArray = dataJson.getJSONArray("info");
                                    final String type_val = jsonArray.getJSONObject(0).getString("type_val");
                                    final int type = jsonArray.getJSONObject(0).getInt("type");
                                    final String type_msg = jsonArray.getJSONObject(0).getString("type_msg");
                                    if (jsonArray.getJSONObject(0).getString("fee_type").equals("1")) {
                                        LiveCommon.showPersonDialog(VideoInfoActivity.this, "2", "1", type_msg, "", "取消", "继续", new DialogInterface() {
                                            @Override
                                            public void cancelDialog(View v, Dialog d) {
                                                d.dismiss();
                                            }

                                            @Override
                                            public void determineDialog(View v, Dialog d) {
                                                Bundle bundle = new Bundle();
                                                bundle.putInt("type", type);
                                                bundle.putString("type_val", type_val);
                                                bundle.putParcelable("USER_INFO", bean);
                                                Intent intent = new Intent(VideoInfoActivity.this, VideoPlayerActivity.class);
                                                intent.putExtra(VideoPlayerActivity.USER_INFO, bundle);
                                                startActivityForResult(intent, 0);
                                                d.dismiss();
                                            }
                                        });

                                    } else {
                                        LiveCommon.showPersonDialog(VideoInfoActivity.this, "2", "1", "您的余额不足\n请充值后重试", "", "取消", "去充值", new com.duomizhibo.phonelive.interf.DialogInterface() {
                                            @Override
                                            public void cancelDialog(View v, Dialog d) {
                                                d.dismiss();

                                            }

                                            @Override
                                            public void determineDialog(View v, Dialog d) {
                                                d.dismiss();
                                                UIHelper.showMyDiamonds(VideoInfoActivity.this);
                                            }
                                        });

                                    }

                                } else {
                                    Toast.makeText(AppContext.getInstance(), dataJson.get("msg").toString(), Toast.LENGTH_LONG).show();

                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                });
//        LiveCommon.showPersonDialog(this, "1", "2", "主播正与其他用户私播中", "请更换其他主播进行私播", "取消", "", new DialogInterface() {
//            @Override
//            public void cancelDialog(View v, Dialog d) {
//                d.dismiss();
//            }
//
//            @Override
//            public void determineDialog(View v, Dialog d) {
//
//            }
//        });

//        LiveCommon.showPersonDialog(this, "2", "2", "与主播发起视频私播需要缴纳", "500"+AppConfig.CURRENCY_NAME+"/分钟的费用", "取消", "继续", new DialogInterface() {
//            @Override
//            public void cancelDialog(View v, Dialog d) {
//                d.dismiss();
//            }
//
//            @Override
//            public void determineDialog(View v, Dialog d) {
//                d.dismiss();
//            }
//        });


//        LiveCommon.showPersonDialog(this, "1", "1", "您的余额不足\n即将退出房间", "", "退出房间", "", new DialogInterface() {
//            @Override
//            public void cancelDialog(View v, Dialog d) {
//                d.dismiss();
//            }
//
//            @Override
//            public void determineDialog(View v, Dialog d) {
//
//            }
//        });

//        LiveCommon.showPersonLianMaiDialog(this, new DialogInterface() {
//            @Override
//            public void cancelDialog(View v, Dialog d) {
//                d.dismiss();
//            }
//
//            @Override
//            public void determineDialog(View v, Dialog d) {
//                d.dismiss();
//            }
//        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 10) {
            LiveCommon.showPersonDialog(VideoInfoActivity.this, "2", "1", "您的余额不足\n即将退出房间", "", "取消", "去充值", new com.duomizhibo.phonelive.interf.DialogInterface() {
                @Override
                public void cancelDialog(View v, Dialog d) {
                    d.dismiss();

                }

                @Override
                public void determineDialog(View v, Dialog d) {
                    d.dismiss();
                    UIHelper.showMyDiamonds(VideoInfoActivity.this);
                }
            });
        }
    }
}

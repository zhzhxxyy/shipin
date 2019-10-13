package com.duomizhibo.phonelive.fragment;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.duomizhibo.phonelive.AppContext;
import com.duomizhibo.phonelive.R;
import com.duomizhibo.phonelive.bean.BonusBean;
import com.duomizhibo.phonelive.utils.StringUtils;
import com.duomizhibo.phonelive.utils.TDevice;
import com.duomizhibo.phonelive.widget.GridViewWithHeaderAndFooter;


import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by mengyunfeng on 17/4/5.
 */

public class LoginAwardDialogFragment extends DialogFragment {
    GridViewWithHeaderAndFooter mLoginAwardCycle;
    TextView mLoginReceive, mLoginAwardSeven;
    ImageView cart;
    LoginGridAdapter mLoginGridAdapter = new LoginGridAdapter();
    View view;
    int wh;
    BonusBean mBonus;

    //可以通过实现此接口到相应的activity传值
    public interface onLoginAwardImgShow {
        public void onLoginAwardImgShow(View view, String i);
    }

    private onLoginAwardImgShow listener;


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = new Dialog(getActivity(), R.style.dialog_no_background);
        dialog.getWindow().setBackgroundDrawable(new
                ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.fragmentdialog_login);
        Window window = dialog.getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        params.gravity = Gravity.CENTER;
        window.setAttributes(params);
        int w = TDevice.getDisplayMetrics().widthPixels;
        wh = (int) ((w - TDevice.dpToPixel(50)) / 3);
        initView(dialog);
        return dialog;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof onLoginAwardImgShow) {
            listener = (onLoginAwardImgShow) activity;
        } else {
            try {
                throw new Exception("activityy should implements OnButtonClick Interface");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void initView(Dialog dialog) {
        mBonus = (BonusBean) getArguments().getSerializable("BonusBean");
        mLoginAwardCycle = (GridViewWithHeaderAndFooter) dialog.findViewById(R.id.login_award_cycle);
        mLoginReceive = (TextView) dialog.findViewById(R.id.login_receive);
        mLoginAwardSeven = (TextView) dialog.findViewById(R.id.login_award_seven);
        if (StringUtils.toInt(mBonus.getBonus_day()) == 7) {
            mLoginAwardSeven.setBackgroundResource(R.drawable.opensunday);
        }
        RelativeLayout.LayoutParams layoutManager = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, wh);
        mLoginAwardSeven.setLayoutParams(layoutManager);
        mLoginReceive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                if (listener != null) {
                    listener.onLoginAwardImgShow(v, mBonus.getBonus_list().get(StringUtils.toInt(mBonus.getBonus_day()) - 1).getCoin());
                }
            }
        });
        if (mLoginAwardCycle != null) {
            mLoginAwardCycle.setAdapter(mLoginGridAdapter);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    private class LoginGridAdapter extends BaseAdapter {


        @Override
        public int getCount() {
            return mBonus.getBonus_list().size() - 1;
        }

        @Override
        public Object getItem(int position) {
            return mBonus.getBonus_list().get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            final ViewHolder viewHolder;
            if (convertView == null) {
                convertView = View.inflate(AppContext.getInstance(), R.layout.item_loginaward, null);
                viewHolder = new ViewHolder();
                viewHolder.mBg = (ImageView) convertView.findViewById(R.id.bg);
                viewHolder.mBg.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, wh));
                viewHolder.mBlingBling = (ImageView) convertView.findViewById(R.id.bling_bling);
                viewHolder.mStar = (ImageView) convertView.findViewById(R.id.login_award_star);
                viewHolder.mReceive = (ImageView) convertView.findViewById(R.id.login_award_receive);
                viewHolder.mDayNum = (TextView) convertView.findViewById(R.id.login_award_day);
                viewHolder.mDayCoin = (TextView) convertView.findViewById(R.id.login_award_coin);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            BonusBean.BonusListBean bonusListBean = mBonus.getBonus_list().get(position);
            viewHolder.mBg.setBackgroundResource(R.drawable.background_blue);
            viewHolder.mDayNum.setText("第" + bonusListBean.getDay() + "天");
            viewHolder.mDayCoin.setText("X" + bonusListBean.getCoin());
            if (position < StringUtils.toInt(mBonus.getBonus_day()) - 1) {
                viewHolder.mReceive.setVisibility(View.VISIBLE);
            }
            if (position == StringUtils.toInt(mBonus.getBonus_day()) - 1) {
                viewHolder.mBlingBling.setVisibility(View.VISIBLE);
                final Animation animationIn = getAlphaAnimationIn();
                final Animation animationOut = getAlphaAnimationOut();
                viewHolder.mBlingBling.setAnimation(animationIn);
                viewHolder.mBlingBling.setAnimation(animationOut);
                animationOut.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        viewHolder.mBlingBling.startAnimation(animationIn);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {
                    }
                });
                animationIn.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        viewHolder.mBlingBling.startAnimation(animationOut);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {
                    }
                });
            }
            return convertView;
        }

        class ViewHolder {
            ImageView mBlingBling, mStar, mBg, mReceive;
            TextView mDayNum, mDayCoin;
        }
    }


    public Animation getAlphaAnimationIn() {
        RotateAnimation rotate = new RotateAnimation(0f, 360f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        LinearInterpolator lin = new LinearInterpolator();
        rotate.setInterpolator(lin);
        rotate.setDuration(2000);//设置动画持续时间
        rotate.setFillAfter(true);//动画执行完后是否停留在执行完的状态
        return rotate;
    }

    public Animation getAlphaAnimationOut() {
        RotateAnimation rotate = new RotateAnimation(0f, 360f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        LinearInterpolator lin = new LinearInterpolator();
        rotate.setInterpolator(lin);
        rotate.setDuration(2000);//设置动画持续时间
        rotate.setFillAfter(true);//动画执行完后是否停留在执行完的状态
        return rotate;
    }
}

package com.duomizhibo.phonelive.utils;

import android.content.Context;
import android.os.CountDownTimer;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.DrawableRequestBuilder;
import com.bumptech.glide.Glide;

import java.lang.ref.WeakReference;

/**
 * Created by weipeng on 2016/12/14.
 */

public class SimpleUtils {

    /*
    *
    * 简单的加载图片,依赖Glide
    * */
    public static void loadImageForView(Context context,ImageView imageView, String url, int def){

        DrawableRequestBuilder<String> drawableRequestBuilder = Glide.with(context).load(url).dontAnimate();

        if(def != 0){
            drawableRequestBuilder.placeholder(def).into(imageView);
            return;
        }
        drawableRequestBuilder.into(imageView);
    }

    /**
     * 在按钮上启动一个定时器
     * @param tvVerifyCode 验证码控件
     * @param defaultString 按钮上默认的字符串
     * @param max 失效时间（单位：s）
     * @param interval 更新间隔（单位：s）
     * */
    public static void startTimer(final WeakReference<TextView> tvVerifyCode,
                                  final String defaultString,
                                  int max,
                                  int interval)
    {
        tvVerifyCode.get().setEnabled(false);

        // 由于CountDownTimer并不是准确计时，在onTick方法调用的时候，time会有1-10ms左右的误差，这会导致最后一秒不会调用onTick()
        // 因此，设置间隔的时候，默认减去了10ms，从而减去误差。
        // 经过以上的微调，最后一秒的显示时间会由于10ms延迟的积累，导致显示时间比1s长max*10ms的时间，其他时间的显示正常,总时间正常
        new CountDownTimer(max * 1000, interval * 1000 - 10) {

            @Override
            public void onTick(long time) {
                // 第一次调用会有1-10ms的误差，因此需要+15ms，防止第一个数不显示，第二个数显示2s
                if(null == tvVerifyCode.get())
                    this.cancel();
                else
                    tvVerifyCode.get().setText("" + ((time + 15) / 1000) + "s");
            }

            @Override
            public void onFinish() {
                if(null == tvVerifyCode.get()) {
                    this.cancel();
                    return;
                }
                tvVerifyCode.get().setEnabled(true);
                tvVerifyCode.get().setText(defaultString);

            }
        }.start();
    }

    public static String formatNumbers(int num){

        if(num >= 10000){
            int w = ((int)num/10000);
            int q = (num % 10000) / 1000;
            return w + "." + q + "万";
        }

        return String.valueOf(num);
    }
}

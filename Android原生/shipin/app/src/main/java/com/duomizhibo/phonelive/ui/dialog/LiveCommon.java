package com.duomizhibo.phonelive.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.TextView;

import com.duomizhibo.phonelive.R;
import com.duomizhibo.phonelive.interf.DialogInterface;
import com.duomizhibo.phonelive.widget.AvatarView;

/**
 * UI公共类
 */
public class LiveCommon {
    public static void showInputContentDialog(Context context, String title, final DialogInterface dialogInterface) {
        final Dialog dialog = new Dialog(context, R.style.dialog);
        dialog.setContentView(R.layout.dialog_set_room_pass);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.show();
        ((TextView) dialog.findViewById(R.id.tv_title)).setText(title);
        dialog.findViewById(R.id.btn_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogInterface.cancelDialog(view, dialog);
            }
        });
        dialog.findViewById(R.id.btn_confirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogInterface.determineDialog(view, dialog);
            }
        });


    }

    public static void showIRtcDialog(Context context, String title, String content, final DialogInterface dialogInterface) {
        final Dialog dialog = new Dialog(context, R.style.dialog);
        dialog.setContentView(R.layout.dialog_show_rtcmsg);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.show();
        ((TextView) dialog.findViewById(R.id.tv_title)).setText(title);
        dialog.findViewById(R.id.btn_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogInterface.cancelDialog(view, dialog);
            }
        });
        TextView textView = (TextView) dialog.findViewById(R.id.et_input);
        textView.setText(content);
        dialog.findViewById(R.id.btn_confirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogInterface.determineDialog(view, dialog);
            }
        });
    }

    public static void showMainTainDialog(Context context, String content) {
        final Dialog dialog = new Dialog(context, R.style.dialog);
        dialog.setContentView(R.layout.dialog_maintain);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.show();

        ((TextView) dialog.findViewById(R.id.tv_content)).setText(content);
        dialog.findViewById(R.id.btn_confirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }


    public static Dialog loadingDialog(Context context) {
        final Dialog dialog = new Dialog(context, R.style.loading_dialog);
        dialog.setContentView(R.layout.dialog_loading);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        return dialog;
    }
    public static Dialog noMoneyDialog(String msg, Context context, final DialogInterface dialogInterface) {
        final Dialog dialog = new Dialog(context, R.style.loading_dialog);
        dialog.setContentView(R.layout.dialog_no_money);
        ( (TextView)dialog.findViewById(R.id.tv_content)).setText(msg);
        dialog.findViewById(R.id.tv_confirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogInterface.determineDialog(v,dialog);
            }
        });
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        return dialog;
    }

    //私播弹窗
    public static void showPersonDialog(Context context, String type,String type2,String content,String content2,String cancel,String confrim,final DialogInterface dialogInterface) {
        final Dialog dialog = new Dialog(context, R.style.dialog);
        dialog.setContentView(R.layout.dialog_person);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        TextView tv_content=((TextView) dialog.findViewById(R.id.tv_content));
        TextView tv_content2=((TextView) dialog.findViewById(R.id.tv_content2));
        TextView tv_cancel=((TextView) dialog.findViewById(R.id.tv_cancel));
        TextView tv_confirm=((TextView) dialog.findViewById(R.id.tv_confirm));
        View line=dialog.findViewById(R.id.line);
        if (type.equals("1")){
            tv_confirm.setVisibility(View.GONE);
            line.setVisibility(View.GONE);
        }
        if (type2.equals("1"))
        {
            tv_content2.setVisibility(View.GONE);
        }
        tv_content.setText(content);
        tv_content2.setText(content2);
        tv_cancel.setText(cancel);
        tv_confirm.setText(confrim);
//        tv_content.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//
//            }
//        })
        setTVColor(content2,"钟",context.getResources().getColor(R.color.global2),tv_content2);
        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogInterface.cancelDialog(view,dialog);
            }
        });  tv_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogInterface.determineDialog(view,dialog);
            }
        });
    }
    //私播连麦主播弹窗
    public static Dialog showPersonLianMaiDialog(Context context, String head, String name, final DialogInterface dialogInterface) {
        final Dialog dialog = new Dialog(context, R.style.dialog_no_background);
        dialog.setContentView(R.layout.dialog_acceptlianmai);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

        TextView tv_cancel=((TextView) dialog.findViewById(R.id.tv_cancel));
        TextView tv_confirm=((TextView) dialog.findViewById(R.id.tv_confirm));
        TextView tv_name=((TextView) dialog.findViewById(R.id.tv_name));
        AvatarView av_head=((AvatarView) dialog.findViewById(R.id.av_head));
        av_head.setAvatarUrl(head);
        tv_name.setText(name);

        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogInterface.cancelDialog(view,dialog);
            }
        });  tv_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogInterface.determineDialog(view,dialog);
            }
        });
        return dialog;
    }
    private static void setTVColor(String str , String ch2 , int color , TextView tv){
        int a =0; //从字符ch1的下标开始
        int b = str.indexOf(ch2)+1; //到字符ch2的下标+1结束,因为SpannableStringBuilder的setSpan方法中区间为[ a,b )左闭右开
        SpannableStringBuilder builder = new SpannableStringBuilder(str);
        builder.setSpan(new ForegroundColorSpan(color), a, b, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        tv.setText(builder);
    }


}

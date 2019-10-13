package com.duomizhibo.phonelive.widget.alert;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.duomizhibo.phonelive.AppContext;
import com.duomizhibo.phonelive.R;


public class BaseDialog {

    protected String TAG = this.getClass().getSimpleName();

    protected Dialog dialog;
    protected WindowManager.LayoutParams lp;
    protected Window dialogWindow;
    protected boolean isShow = false;

    public BaseDialog(Context context) {
        dialog = new Dialog(context, R.style.dialognew);
    }

    /**
     * dialog显示
     * @param v：显示的view
     * @param heightScale：显示高度占屏幕设为百分比
     * @param widthScale：显示宽度占屏幕设为百分比
     */
    protected void show(View v, float heightScale, float widthScale) {
        show(v);
        lp = dialogWindow.getAttributes();
        lp.height = (int) (AppContext.contentHeight * heightScale);
        lp.width = (int) (AppContext.screenWidth * widthScale);
        dialogWindow.setAttributes(lp);
    }

    /**
     * dialog显示：高度以view的实际高度显示
     * @param v：显示的view
     * @param widthScale：显示宽度占屏幕设为百分比
     */
    protected void show(View v, float widthScale) {
        show(v);
        lp = dialogWindow.getAttributes();
        lp.width = (int) (AppContext.screenWidth * widthScale);
        dialogWindow.setAttributes(lp);
    }

    /**
     * dialog显示：宽高以自己定义的view的宽高显示
     * @param v：显示的view
     */
    protected void show(View v) {
        if (dialog.isShowing()) {
            return;
        }
        try {
            dialog.show();
        } catch (WindowManager.BadTokenException e) {
            //activity已经销毁，再显示就会报此异常
            //报此异常说明activity销毁，无需再弹出;
            dismiss();
            return;
        }
        dialogWindow = this.dialog.getWindow();
        isShow = true;
        dialog.getWindow().setContentView(v);
        this.dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            public void onDismiss(DialogInterface dialog) {
                isShow = false;
                doOnDismiss();
            }
        });
    }

    // 在窗口底部显示
    protected void showBottom(View v) {
        dialog.getWindow().setGravity(Gravity.BOTTOM);
        dialog.getWindow().setWindowAnimations(R.style.dialog_bottom_in_out);
        show(v, 1);
    }

    // 在窗口底部显示
    protected void showBottom(View v, float heightScale) {
        dialog.getWindow().setGravity(Gravity.BOTTOM);
        dialog.getWindow().setWindowAnimations(R.style.dialog_bottom_in_out);
        show(v, heightScale, 1);
    }

    // dialog消失后调用的方法
    protected void doOnDismiss() {
    }

    public void dismiss() {
        this.dialog.cancel();
        isShow = false;
    }

    public boolean isShow() {
        return isShow;
    }

    // 显示toast
    protected void showToast(String msg) {
        AppContext.showToastShort(msg);
    }


    public interface ParameCallBack {

        public void onCall(Object o);

    }
}

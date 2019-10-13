package com.duomizhibo.phonelive.widget.alert;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

;import com.duomizhibo.phonelive.R;

/**
 * 自定义输入弹出窗体
 *
 */
public class TipDialog extends BaseDialog implements View.OnClickListener{

	private View view;
	private TextView title;//提示信息
	private TextView message;//信息内容
	private TextView cancel;//取消按钮
	private TextView ok;//确定按钮
	private ParameCallBack callBack;


	public TipDialog(Context context) {
		super(context);
		
		view = View.inflate(context, R.layout.dialog_tip_view, null);
		title = (TextView) view.findViewById(R.id.title);
		message = (TextView) view.findViewById(R.id.message);
		cancel = (TextView) view.findViewById(R.id.cancel);
		ok = (TextView) view.findViewById(R.id.ok);
		
		cancel.setOnClickListener(this);
		ok.setOnClickListener(this);
		//点击弹出窗体外，弹出窗消失
		dialog.setCanceledOnTouchOutside(true);
	}


	public void onClick(View view) {
		if(view==ok){
			//确定
			if(callBack!=null){
				callBack.onCall(true);
			}
			dismiss();
		}else if(view==cancel){
			//取消
			if(callBack!=null){
				callBack.onCall(false);
			}
			dismiss();
		}
		
	}

	public void show(ParameCallBack callBack){
		this.callBack = callBack;
		show(view);
	}
	
	public void show(String msg, ParameCallBack callBack){
		this.callBack = callBack;
		message.setText(msg+"");
		show(view);
	}
	//设置title
	public TipDialog setTitle(String titleText) {
		title.setText(titleText+"");
		return this;
	}
	//设置取消按钮显示
	public TipDialog setCancelText(String cancelText) {
		cancel.setText(cancelText+"");
		return this;
	}
	//设置确定按钮显示
	public TipDialog setOkText(String okText) {
		ok.setText(okText+"");
		return this;
	}
	//设置message信息
	public TipDialog setMessageText(String messageText) {
		message.setText(messageText+"");
		return this;
	}
	//设置点击弹出窗口外是否消失
	public TipDialog setCanceledOnTouchOutside(boolean flag) {
		dialog.setCanceledOnTouchOutside(flag);
		return this;
	}
	//设置取消按钮是否显示
	public TipDialog setCancelBtnVisible(boolean isVisible){
		if(isVisible){
			cancel.setVisibility(View.VISIBLE);
			ok.setBackgroundResource(R.drawable.dialog_bottom_right_view_bg_selector);
		}else{
			cancel.setVisibility(View.GONE);
			ok.setBackgroundResource(R.drawable.dialog_bottom_view_bg_selector);
		}
		return this;
	}
}

package com.museumguild.view.view;

import android.app.Activity;
import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.museumguild.R;

public class CustomAlertDialog {
	
	private Context mContext;
	
	private Activity mActivity;

	private AlertDialog mAlertDialog;
	
	private View rootView;

	/**
	 * 左边按钮
	 */
	private Button leftBtn;
	
	/**
	 * 中间按钮
	 */
	private Button middleBtn;
	
	/**
	 * 右边按钮
	 */
	private Button rightBtn;
	
	/**
	 * 左边按钮与中间按钮的分隔线
	 */
	private View leftMiddleDivider;
	
	/**
	 * 中间按钮与右边按钮的分隔线
	 */
	private View middleRightDivider;
	
	/**
	 * 提示
	 */
	private TextView promptTextView;
	
	private LinearLayout promptTextBox;

	/**
	 * 具体内容
	 */
	private TextView msgTextView;
	
	private OnButtonClickListener onButtonClickListener;
	
	public CustomAlertDialog(Context context, Activity activity) {
		this.mContext = context;
		this.mActivity = activity;
		
		LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		rootView = inflater.inflate(R.layout.custom_dialog, null);
		initView();
	}
	
	private void initView() {
		promptTextBox = (LinearLayout) rootView.findViewById(R.id.custom_dialog_title_box);
		promptTextView = (TextView) rootView.findViewById(R.id.custom_dialog_title_text);
		msgTextView = (TextView) rootView.findViewById(R.id.custom_dialog_msg_text);
		leftBtn = (Button) rootView.findViewById(R.id.custom_dialog_left_btn);
		leftBtn.setVisibility(View.GONE);
		middleBtn = (Button) rootView.findViewById(R.id.custom_dialog_middle_btn);
		middleBtn.setVisibility(View.GONE);
		rightBtn = (Button) rootView.findViewById(R.id.custom_dialog_right_btn);
		rightBtn.setVisibility(View.GONE);
		leftMiddleDivider = rootView.findViewById(R.id.custom_dialog_divider_left);
		leftMiddleDivider.setVisibility(View.GONE);
		middleRightDivider = rootView.findViewById(R.id.custom_dialog_divider_right);
		middleRightDivider.setVisibility(View.GONE);
	}
	
	/**
	 * 创建对话框
	 * @param msg 提示信息
	 * @return 自定义对话框实例
	 */
	public CustomAlertDialog buildDialog(String msg) {
		return buildDialog("", msg);
	}
	
	/**
	 * 创建对话框
	 * @param title 标题
	 * @param msg 提示信息
	 * @return 自定义对话框实例
	 */
	public CustomAlertDialog buildDialog(String title, String message) {
		mAlertDialog = new AlertDialog.Builder(mContext).create();
		mAlertDialog.setView(rootView);
		
		// 设置标题
		if (TextUtils.isEmpty(title)) {
			promptTextBox.setVisibility(View.GONE);
		}
		else {
			promptTextView.setText(title);
		}
		
		// 设置提示信息
		if (!TextUtils.isEmpty(message)) {
			msgTextView.setText(message);
		}else {
			msgTextView.setVisibility(View.GONE);
		}
		
		return this;
	}
	
	/**
	 * 设置标题
	 * @param title 标题
	 * @return 自定义对话框实例
	 */
	public CustomAlertDialog setTitle(String title) {
		// 设置标题
		if (TextUtils.isEmpty(title)) {
			promptTextBox.setVisibility(View.GONE);
		}
		else {
			promptTextView.setText(title);
		}
		return this;
	}
	
	/**
	 * 设置提示信息
	 * @param message 提示信息
	 * @return 自定义对话框实例
	 */
	public CustomAlertDialog setMessage(String message) {
		// 设置提示信息
		if (message != null) {
			msgTextView.setText(message);
		}
		return this;
	}
	
	/**
	 * 设置左边按钮
	 * @param text 按钮文字
	 * @return 自定义对话框实例
	 */
	public CustomAlertDialog setLeftButton(String text) {
		if (!TextUtils.isEmpty(text)) {
			leftBtn.setVisibility(View.VISIBLE);
			leftBtn.setText(text);
			setOnClickListener(leftBtn, OnButtonClickListener.LEFT_CLICK);
		}
		return this;
	}
	
	/**
	 * 设置中间按钮
	 * @param text 按钮文字
	 * @return 自定义对话框实例
	 */
	public CustomAlertDialog setMiddleButton(String text) {
		if (!TextUtils.isEmpty(text)) {
			middleBtn.setVisibility(View.VISIBLE);
			middleBtn.setText(text);
			setOnClickListener(middleBtn, OnButtonClickListener.MIDDLE_CLICK);
		}
		return this;
	}
	
	/**
	 * 设置右边按钮
	 * @param text 按钮文字
	 * @return 自定义对话框实例
	 */
	public CustomAlertDialog setRightButton(String text) {
		if (!TextUtils.isEmpty(text)) {
			rightBtn.setVisibility(View.VISIBLE);
			rightBtn.setText(text);
			setOnClickListener(rightBtn, OnButtonClickListener.RIGHT_CLICK);
		}
		return this;
	}
	
	/**
	 * 设置按钮的点击事件
	 * @param view 需要设置点击事件的按钮
	 * @param clickID 点击之后的返回值
	 */
	private void setOnClickListener(View view, final int clickID) {
		view.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				mAlertDialog.dismiss();
				if (onButtonClickListener != null) {
					onButtonClickListener.OnButtonClick(clickID);
				}
			}
		});
	}
	
	public CustomAlertDialog setOnButtonClickListener(OnButtonClickListener onButtonClickListener2) {
		this.onButtonClickListener = onButtonClickListener2;
		return this;
	}
	
	public void show() {
		// 先判断有几个按钮，视情况而定显示哪条分隔线
		if (leftBtn.getVisibility() == View.VISIBLE) {
			if (middleBtn.getVisibility() == View.VISIBLE) {
				leftMiddleDivider.setVisibility(View.VISIBLE);
				if (rightBtn.getVisibility() == View.VISIBLE) {
					middleRightDivider.setVisibility(View.VISIBLE);
				}
			}
			else if (rightBtn.getVisibility() == View.VISIBLE) {
				middleRightDivider.setVisibility(View.VISIBLE);
			}
		}
		else if(middleBtn.getVisibility() == View.VISIBLE) {
			if (rightBtn.getVisibility() == View.VISIBLE) {
				middleRightDivider.setVisibility(View.VISIBLE);
			}
		}
		
		if (mAlertDialog != null) {
			mAlertDialog.show();
		}
	}
	
	public void dismiss() {
		if (mAlertDialog != null && mAlertDialog.isShowing()) {
			mAlertDialog.dismiss();
		}
	}
	
	public interface OnButtonClickListener {
		
		/**
		 * 自定义对话框{@link CustomAlertDialog}左边按钮点击返回值
		 */
		public static final int LEFT_CLICK = 0;
		
		/**
		 * 自定义对话框{@link CustomAlertDialog}中间按钮点击返回值
		 */
		public static final int MIDDLE_CLICK = 1;
		
		/**
		 * 自定义对话框{@link CustomAlertDialog}右边按钮点击返回值
		 */
		public static final int RIGHT_CLICK = 2;
		
		/**
		 * 按钮点击之后会返回相应的值
		 * <p>点击左边按钮返回{@link #LEFT_CLICK}：0
		 * <br>点击中间按钮返回{@link #MIDDLE_CLICK}：1
		 * <br>点击右边按钮返回{@link #RIGHT_CLICK}：2
		 * @param clickID 按钮点击之后的返回值
		 */
		void OnButtonClick(int clickID);
	}
	
}

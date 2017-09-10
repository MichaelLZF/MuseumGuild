package com.museumguild.utils.share;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.museumguild.R;


public class PopShare extends PopupWindow implements OnClickListener {
	private OnClickListener mClickListener;
	public PopShare(Context context) {
		super(context);
		initView(context, LayoutParams.MATCH_PARENT);
	}
	
	public PopShare(Context context , int contentWidth){
		super(context);
		initView(context, contentWidth);
	}

	private void initView(Context context, int contentWidth) {
		LayoutInflater inflater = LayoutInflater.from(context);
		View view = inflater.inflate(R.layout.share_layout, null);
		view.findViewById(R.id.txtShareQQ).setOnClickListener(this);
		view.findViewById(R.id.txtShareSinaWB).setOnClickListener(this);
		view.findViewById(R.id.txtShareWX).setOnClickListener(this);
		view.findViewById(R.id.txtShareFriend).setOnClickListener(this);
		view.findViewById(R.id.txtShareCopy).setOnClickListener(this);
		view.findViewById(R.id.imgClose).setOnClickListener(this);
		setWidth(LayoutParams.MATCH_PARENT);
		setHeight(LayoutParams.MATCH_PARENT);
		LinearLayout mainLy = (LinearLayout)view.findViewById(R.id.share_main_ly);
		LinearLayout.LayoutParams param = (android.widget.LinearLayout.LayoutParams) mainLy.getLayoutParams();
		param.width = contentWidth;
		mainLy.setLayoutParams(param);
		setContentView(view);
		setBackgroundDrawable(context.getResources().getDrawable(
				R.color.other_color_pop));
		setOutsideTouchable(false);
		setFocusable(true);
	}

	
	@Override
	public void onClick(View v) {
		if(mClickListener != null){
			mClickListener.onClick(v);
		}
		dismiss();
	}

	
	public void showAtLocation(View parent, OnClickListener l) {
		mClickListener = l;
		if (!isShowing()) {
			showAtLocation(parent, Gravity.CENTER, 0, 0);
		}
	}
}

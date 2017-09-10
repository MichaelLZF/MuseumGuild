package com.museumguild.view.activities;


import android.os.Bundle;
import android.view.Window;

import com.museumguild.R;

public class TmpActivity extends BaseActivity{
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		finish();
		android.os.Process.killProcess(android.os.Process
					.myPid());
		System.exit(0);
	}

	@Override
	protected int initContentViewId() {
		return R.layout.tmp_layout;
	}

	@Override
	protected void initView() {

	}

	@Override
	protected void initData() {

	}

}

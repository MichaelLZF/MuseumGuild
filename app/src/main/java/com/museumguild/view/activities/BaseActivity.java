package com.museumguild.view.activities;

import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;

import com.museumguild.R;
import com.museumguild.manage.ActManager;

public abstract class BaseActivity extends AppCompatActivity implements OnClickListener {
	public final String TAG = getClass().getSimpleName();
	private int contentViewId;
	private View progressBar;

	protected Handler mHandler = new Handler();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ActManager.addToActList(this);
		contentViewId = initContentViewId();
		setContentView(contentViewId);
		initView();
		progressBar = findViewById(R.id.progress_wheel);
		initData();
		initToolbar();
	}

	protected abstract int initContentViewId();
	protected abstract void initView();
	protected abstract void initData();

	public void showProgress() {
		if(null == progressBar){
			return;
		}
		progressBar.setVisibility(View.VISIBLE);
		progressBar.bringToFront();
	}

	public void hideProgress() {
		if(null == progressBar){
			return;
		}
		progressBar.setVisibility(View.GONE);
	}

	@Override
	protected void onDestroy() {
		ActManager.removeFromActList(this);
		super.onDestroy();
	}
	
	@Override
	protected void onResume() {
		ActManager.setTopActivity(this);
		super.onResume();
	}

	@Override
	public void onClick(View v) {

	}

	/**
	 * 沉浸式状态栏
	 */
	public void initToolbar() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			//透明状态栏
			getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
			//透明导航栏
			getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
		}
	}

	public void showCustomDialog() {
		AlertDialog dialog = new AlertDialog.Builder(this).create();
		dialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				ActManager.exitApp();
			}
		});
		dialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		dialog.setTitle("Are your sure to exit?");
		dialog.show();
	}
	
	protected void lightOn() {
		WindowManager.LayoutParams lp = getWindow().getAttributes();
		lp.alpha = 1.0f;
		getWindow().setAttributes(lp);

	}
	protected void lightOff() {
		WindowManager.LayoutParams lp = getWindow().getAttributes();
		lp.alpha = 0.7f;
		getWindow().setAttributes(lp);

	}
}

package com.museumguild.view.holder;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;

import com.museumguild.view.activities.BaseActivity;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;

public abstract class BaseHolder extends Handler implements OnClickListener {
	protected View rootView;

	public BaseHolder(View rootView, Object value) {
		this.rootView = rootView;
		initData(value);
		initComp(value);
	}

	public BaseHolder(LayoutInflater inflater, int layoutID, Object value) {
		this(inflater.inflate(layoutID, null), value);
	}

	public void sendMessage(int what, Object object) {
		sendMessage(obtainMessage(what, object));
	}

	@Override
	final public void handleMessage(Message msg) {
		onMessage(msg.what, msg.obj, msg.getData());
	}

	public void onMessage(int what, Object object, Bundle bundle) {
	}

	protected View findViewById(int resId) {
		return rootView.findViewById(resId);
	}

	protected abstract void initData(Object value);

	protected abstract void initComp(Object value);
	
	public void layout(Bundle saveInstance){};

	protected Context getContext() {
		return rootView.getContext();
	}

	public View getRootView() {
		return rootView;
	}

	public String getString(int resId) {
		return getContext().getResources().getString(resId);
	}

	public void show(boolean isShow) {
		if (isShow) {
			if (View.VISIBLE != rootView.getVisibility()) {
				rootView.setVisibility(View.VISIBLE);
			}
		} else {
			if (View.GONE != rootView.getVisibility()) {
				rootView.setVisibility(View.GONE);
			}
		}
	}
	
    public boolean isShow()
    {
    	return rootView.getVisibility() == View.VISIBLE ? true : false;
    }
    
	protected void startIntent(Class<?> activityClass, Bundle bundle) {
		Intent intent = new Intent(getContext(), activityClass);
		intent.putExtras(bundle);
		getContext().startActivity(intent);
	}

	protected void startIntent(Class<?> activityClass) {
		Intent intent = new Intent(getContext(), activityClass);
		getContext().startActivity(intent);
	}

	protected  void startActivityForResult(Class<?> activityClass, int requestCode, String key, String value)
	{
		Intent intent = new Intent(getContext(), activityClass);
		intent.putExtra(key, value);
		((Activity)getContext()).startActivityForResult(intent, requestCode);
	}
	
	public void onSaveInstence(Bundle outState){}
	public void onRestoreInstanceState(Bundle savedInstanceState){}

	public boolean isNotValidToken()
	{
		return false;// 8/21 更新 ,藏品的请求 不需要token了
		//return null == LoginManager.getIns().getToken() || "".equals(LoginManager.getIns().getToken());
	}

	public void showProgress()
	{
		((BaseActivity)getContext()).showProgress();
	}

	public void hideProgress()
	{
		((BaseActivity)getContext()).hideProgress();
	}

	public DisplayImageOptions getRectOptions(int defResDrawable) {
		DisplayImageOptions options = new DisplayImageOptions.Builder()
				.showImageOnFail(defResDrawable)
				.showImageForEmptyUri(defResDrawable)
				.showStubImage(defResDrawable).cacheInMemory()
				.resetViewBeforeLoading().bitmapConfig(Bitmap.Config.RGB_565)
				.displayer(new SimpleBitmapDisplayer()).build();
		return options;
	}
}

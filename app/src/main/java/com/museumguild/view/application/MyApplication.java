package com.museumguild.view.application;

import android.app.Application;
import android.content.Context;

import com.museumguild.http.OkHttpClientManager;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.squareup.okhttp.OkHttpClient;
import com.museumguild.R;

public class MyApplication extends Application {
	private static final String TAG = "MyApplication";
	public static Context context;
	@Override
	public void onCreate() {
		super.onCreate();
		//Log.m(TAG, "oncreate");
		MyApplication.context = getApplicationContext();
		OkHttpClient client = OkHttpClientManager.getInstance().getOkHttpClient();
        DisplayImageOptions defaultOptions = new DisplayImageOptions
				.Builder()
				.showImageForEmptyUri(R.drawable.ic_no_picture) 
				.showImageOnFail(R.drawable.ic_no_picture)
				.cacheInMemory(true)
				.cacheOnDisc(true)
				.displayer(new FadeInBitmapDisplayer(100))
				//.displayer(new RoundedBitmapDisplayer(20))//是否设置为圆角，弧度为多少
				.build();

		ImageLoaderConfiguration config = new ImageLoaderConfiguration
				.Builder(getApplicationContext())
				.defaultDisplayImageOptions(defaultOptions)
				.discCacheSize(20 * 1024 * 1024)//20M
				.discCacheFileCount(20)//缓存x张图片
				.writeDebugLogs()
				.build();
		ImageLoader.getInstance().init(config);
	}
	
}

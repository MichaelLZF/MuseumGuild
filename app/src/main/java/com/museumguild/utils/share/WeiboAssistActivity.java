package com.museumguild.utils.share;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.museumguild.utils.Log;
import com.museumguild.utils.Util;
import com.sina.weibo.sdk.api.share.BaseResponse;
import com.sina.weibo.sdk.api.share.IWeiboHandler;

public class WeiboAssistActivity extends Activity implements IWeiboHandler.Response{
	private static Handler mHandler = new Handler();
	private static final String TAG = "WeiboAssistActivity";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if(null == savedInstanceState)
		{
			boolean isNeedShare = getIntent().getBooleanExtra("isNeedShare", false);
			Log.m(TAG, "onCreate() called with: "+"isNeedShare:"+isNeedShare + ",getIntent() = [" + getIntent() + "]");
			if(isNeedShare) {
				//Util.shareSinaWeibo(this, mHandler);
			}
		}
		
		// 当 Activity 被重新初始化时（该 Activity 处于后台时，可能会由于内存不足被杀掉了），
        // 需要调用 {@link IWeiboShareAPI#handleWeiboResponse} 来接收微博客户端返回的数据。
        // 执行成功，返回 true，并调用 {@link IWeiboHandler.Response#onResponse}；
        // 失败返回 false，不调用上述回调
        if (savedInstanceState != null) {
            Util.handleWeiboResponse(getIntent(), this);
        }
	}
	
	@Override
	protected void onNewIntent(Intent intent) {
		Log.m(TAG, "onNewIntent() called with: " + "intent = [" + intent + "]");
		super.onNewIntent(intent);
        // 从当前应用唤起微博并进行分享后，返回到当前应用时，需要在此处调用该函数
        // 来接收微博客户端返回的数据；执行成功，返回 true，并调用
        // {@link IWeiboHandler.Response#onResponse}；失败返回 false，不调用上述回调
		Util.handleWeiboResponse(intent, this);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		Log.m(TAG, "onActivityResult() requestCode =" + resultCode + " resultCode = " + resultCode);
		Util.onSinaAuthorResult(requestCode, resultCode, data);
		if(resultCode != -1)
		{
			finish();
		}
	}
	
	@Override
	protected void onDestroy() {
		Log.m(TAG, "onDestroy() called with: " + "");
		super.onDestroy();
	}

	@Override
	public void onResponse(BaseResponse arg0) {
		runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				finish();
			}
		});
		if(null != arg0)
		{
		  Log.m(TAG, "onResponse " + arg0.errCode + " errMsg = "  + arg0.errMsg);
		}
	}
}

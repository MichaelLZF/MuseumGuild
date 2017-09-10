package com.museumguild.utils.share;

import android.app.Activity;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.ClipboardManager;
import android.text.TextUtils;

import com.museumguild.utils.Log;
import com.sina.weibo.sdk.api.TextObject;
import com.sina.weibo.sdk.api.WeiboMessage;
import com.sina.weibo.sdk.api.WeiboMultiMessage;
import com.sina.weibo.sdk.api.share.BaseResponse;
import com.sina.weibo.sdk.api.share.IWeiboHandler;
import com.sina.weibo.sdk.api.share.IWeiboShareAPI;
import com.sina.weibo.sdk.api.share.SendMessageToWeiboRequest;
import com.sina.weibo.sdk.api.share.SendMultiMessageToWeiboRequest;
import com.sina.weibo.sdk.api.share.WeiboShareSDK;
import com.sina.weibo.sdk.auth.AuthInfo;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WeiboAuthListener;
import com.sina.weibo.sdk.auth.sso.SsoHandler;
import com.sina.weibo.sdk.exception.WeiboException;
import com.tencent.connect.share.QQShare;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXWebpageObject;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

@SuppressWarnings("deprecation")
/**
 * MD5 : CB:2F:1D:C8:A1:92:EC:01:55:D0:E1:8F:87:84:84:0C
 * SHA1: A6:AB:46:28:0B:83:EE:A2:C8:6E:18:0A:81:5C:1E:C7:A0:5F:BF:50
 * @author Administrator
 *       CB:2F:1D:C8:A1:92:EC:01:55:D0:E1:8F:87:84:84:0C
 *       
         CB:2F:1D:C8:A1:92:EC:01:55:D0:E1:8F:87:84:84:0C
 */
public class Share implements IWeiboHandler.Response,WeiboAuthListener {
	private static final String KEY_SINAWEIBO_TOKEN = "key.sinaweibo.token";
	
	/**
	 * 新浪微博
	 */
	public static final String APP_KEY_SINA_WB = "1251958004";//"234122725";
	private static final String APP_SINA_REDIRECTURL = "http://www.onelv.cn";
	
	/**
	 * QQ
	 */
	private static final String APP_KEY_QQ = "1105846710";
	// APPID 1105846710
	// APPKEY gCoQkIgrCEOV4TwC

	/**
	 * 微信  微信朋友圈
	 */
	private static final String APP_KEY_WX = "wxdbc1672b742a16de";

	private static final String TAG = "share";
	private static final String DEF_URL = "http://www.onelv.cn";
	

	private Oauth2AccessToken accessToken;
	private IWeiboShareAPI sinaShare;
	
	private Context mContext;
	private String tmpTitle;
	private String tmpContent;
	private Handler tmpHandler;
	private SsoHandler mSsoHandler;
	
	/**********************************新浪微博***********************************/

	@Override
	public void onCancel() {
		Log.m(TAG, "sinawb share onCancel sina wb share cancel");
		if(null != mContext && mContext instanceof WeiboAssistActivity){
			((WeiboAssistActivity) mContext).finish();
		}
	}

	@Override
	public void onComplete(Bundle arg0) {
		if(null != arg0)
		{
			accessToken = Oauth2AccessToken.parseAccessToken(arg0);
			if (accessToken.isSessionValid()) {
				AccessTokenKeeper.writeAccessToken(mContext, accessToken);
				shareSinaWeibo(mContext, tmpTitle, tmpContent, tmpHandler);
			}
			String code = arg0.getString("code", "");
			Log.m(TAG, "sinawb share  onComplete accessToken = " + accessToken.isSessionValid() + " ： " + accessToken.toString());
			Log.m(TAG, "sinawb share  onComplete code = " + code);
		}
	}

	@Override
	public void onWeiboException(WeiboException arg0) {
		Log.m(TAG, "sinawb share  onWeiboException " + arg0.getMessage());
		if(null != mContext && (mContext instanceof Activity))
		{
			((Activity)mContext).runOnUiThread(new Runnable() {

				@Override
				public void run() {
					((Activity)mContext).finish();
				}
			});
		}
	}
	
	private void init(Context c){
		Log.m(TAG, "init authorize");
		   // 快速授权时，请不要传入 SCOPE，否则可能会授权不成功
		AuthInfo mAuthInfo = new AuthInfo(c, APP_KEY_SINA_WB, APP_SINA_REDIRECTURL, "");
		mSsoHandler = new SsoHandler((Activity)c, mAuthInfo);
		mSsoHandler.authorize(this);
		
	}
	
	public void onSinaAuthorResult(int requestCode, int resultCode, Intent data)
	{
		Log.m(TAG, "onSinaAuthorResult " );
        // SSO 授权回调
        // 重要：发起 SSO 登陆的 Activity 必须重写 onActivityResults
        if (mSsoHandler != null) {
            mSsoHandler.authorizeCallBack(requestCode, resultCode, data);
        }
	}

	public void shareSinaWeibo(final Context context, final String title,
                               final String content, Handler h) {
		mContext = context;
		tmpTitle = title;
		tmpContent = content;
		tmpHandler = h;
		
		
		sinaShare = WeiboShareSDK.createWeiboAPI(context, Share.APP_KEY_SINA_WB);
		sinaShare.registerApp();
		shareSinaWeibo(context, title, content);
		
        // 从 SharedPreferences 中读取上次已保存好 AccessToken 等信息，
        // 第一次启动本应用，AccessToken 不可用
//		Oauth2AccessToken mAccessToken = AccessTokenKeeper.readAccessToken(context);
//        if (mAccessToken.isSessionValid()) {
////        	if(null == sinaShare)
////        	{
////        		Log.m(TAG, "shareSinaWeibo shareSinaWeibo WeiboShareSDK.createWeiboAPI");
//	    		sinaShare = WeiboShareSDK.createWeiboAPI(context, Share.APP_KEY_SINA_WB);
//	    		sinaShare.registerApp();
//	    		shareSinaWeibo(context, title, content);
////        	}
//        }
//        else
//        {
//          	sinaShare = null;
//        	init(context);
//        }
		

	}
	
	private void shareSinaWeibo(final Context context, String title, String content) {

		// WebpageObject webpageObject = new WebpageObject();
		// webpageObject.identify = Utility.generateGUID();
		// webpageObject.title = title == null ?"":title;
		// webpageObject.actionUrl = content == null?"":content;
		// webpageObject.defaultText = title == null ?"":title;
		// webpageObject.description = title == null ?"":title;

		TextObject textObject = new TextObject();
		textObject.title = title == null ? "" : title;
		textObject.text = content == null ? "" : content;
		String now = String.valueOf(System.currentTimeMillis());
		textObject.text = textObject.title + textObject.text + "?t="
				+ now;
		if (sinaShare.isWeiboAppInstalled()) {
			Oauth2AccessToken mAccessToken = AccessTokenKeeper.readAccessToken(context);
			if (!mAccessToken.isSessionValid()) {
	          	sinaShare = null;
	        	init(context);
	        	return;
			}
			int supportApi = sinaShare.getWeiboAppSupportAPI();
			Log.m(TAG, "shareSinaWeibo client supportApi >= 10351 is " + (supportApi >= 10351));
			if(supportApi >= 10351)
			{
				WeiboMultiMessage msg = new WeiboMultiMessage();
				msg.mediaObject = textObject;
				SendMultiMessageToWeiboRequest request = new SendMultiMessageToWeiboRequest();
				request.transaction = now;
				request.multiMessage = msg;
				sinaShare.sendRequest((Activity)context, request);
			}
			else
			{
				WeiboMessage message = new WeiboMessage();
				message.mediaObject = textObject;
				SendMessageToWeiboRequest request2 = new SendMessageToWeiboRequest();
				request2.message = message;
				request2.transaction = now;
				sinaShare.sendRequest((Activity) context, request2);
			}
		} else 
		{
			Log.m(TAG, "shareSinaWeibo web share" );
            AuthInfo authInfo = new AuthInfo((Activity)context, APP_KEY_SINA_WB, APP_SINA_REDIRECTURL, "");
            accessToken = AccessTokenKeeper.readAccessToken(context);
            String token = "";
            if (accessToken != null) {
                token = accessToken.getToken();
            }
			WeiboMultiMessage msg = new WeiboMultiMessage();
			msg.mediaObject = textObject;
			SendMultiMessageToWeiboRequest request = new SendMultiMessageToWeiboRequest();
			request.transaction = now;
			request.multiMessage = msg;
			sinaShare.sendRequest((Activity) context, request, authInfo, token,
					new WeiboAuthListener() {
						
						@Override
						public void onWeiboException(WeiboException arg0) {
							Log.m(TAG, "sinawb share web onWeiboException = " + arg0.getMessage());
							if(null != context && (context instanceof Activity))
							{
								((Activity)context).runOnUiThread(new Runnable() {
									
									@Override
									public void run() {
										((Activity)context).finish();
									}
								});
							}
						}
						
						@Override
						public void onComplete(Bundle arg0) {
							if(null != arg0)
							{
								accessToken = Oauth2AccessToken.parseAccessToken(arg0);
								if (accessToken.isSessionValid()) {
									AccessTokenKeeper.writeAccessToken(mContext, accessToken);
								}
								String code = arg0.getString("code", "");
								Log.m(TAG, "sinawb share web onComplete accessToken = " + accessToken.isSessionValid() + " ： " + accessToken.toString());
								Log.m(TAG, "sinawb share web onComplete code = " + code);
							}
						}
						
						@Override
						public void onCancel() {
							Log.m(TAG, "sinawb share web onCancel");
							if(null != context && (context instanceof Activity))
							{
								((Activity)context).runOnUiThread(new Runnable() {
									
									@Override
									public void run() {
										((Activity)context).finish();
									}
								});
							}
						}
					});
		}
	}
	
	
	@Override
	public void onResponse(BaseResponse arg0) {
		Log.m(TAG, "sinawb share  onResponse " + arg0.errCode);
	}

	public void handleWeiboResponse(Intent intent, IWeiboHandler.Response response){
		Log.m(TAG, "handleWeiboResponse 1");
		if(sinaShare != null){
			Log.m(TAG, "handleWeiboResponse 2");
			boolean bTrue = sinaShare.handleWeiboResponse(intent, response);	
			Log.m(TAG, "handleWeiboResponse 3 bTrue = " + bTrue);
		}
	}

	
	/****************************qq********************************/
	private Tencent tencent;
	/*
	 * http://wiki.open.qq.com/wiki/mobile/SDK下载
	 */
	
	public  void shareQQ(Context context, String appName, String title, String content, String openUrl, String imgUrl) {
		if (tencent == null) {
			tencent = Tencent.createInstance(APP_KEY_QQ, context);
		}
	    Bundle params = new Bundle();
	    params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_DEFAULT);
	    params.putString(QQShare.SHARE_TO_QQ_TITLE, title == null?"":title);
	    params.putString(QQShare.SHARE_TO_QQ_SUMMARY,  content==null?"":content);
	    //TODO url and img
	    params.putString(QQShare.SHARE_TO_QQ_TARGET_URL,openUrl == null?DEF_URL:openUrl);
		if(TextUtils.isEmpty(imgUrl)){
			params.putString(QQShare.SHARE_TO_QQ_IMAGE_LOCAL_URL,copyImg(context,"img/logo_144_rect.png","logo_144_rect.png"));
		}else{
			params.putString(QQShare.SHARE_TO_QQ_IMAGE_URL, imgUrl);
		}
		params.putString(QQShare.SHARE_TO_QQ_APP_NAME,  appName);
	    tencent.shareToQQ((Activity)context, params, new IUiListener() {
			
			@Override
			public void onError(UiError arg0) {
				Log.m(TAG, "shareQQ onError code =" + arg0.errorCode + " msg = " + arg0.errorMessage);
			}
			
			@Override
			public void onComplete(Object arg0) {
				Log.m(TAG, "shareQQ onComplete");
			}
			
			@Override
			public void onCancel() {
				Log.m(TAG, "shareQQ onCancel");
			}
		});
	}
	
	private String copyImg(Context context, String assetPath, String tmpName){
		File dir = context.getExternalCacheDir();
		String path = dir.getAbsolutePath()+ "/" + tmpName;
		File img = new File(path);
        if(null != img && img.exists()){
            img.delete();
        }
        try {
            img.createNewFile();
            FileOutputStream outputStream = new FileOutputStream(img);
            InputStream in = context.getResources().getAssets()
                    .open(assetPath);
            byte[] buffer = new byte[512];
            int len = -1;
            while ((len = in.read(buffer)) > 0) {
                outputStream.write(buffer, 0, len);
            }
            in.close();
            outputStream.flush();
            outputStream.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
		return path;
	}
	/***************************************WX*****************************************/
	private IWXAPI wxapi;

	public void shareWX(Context context, String title, String content, String openUrl, String imgUrl,
                        boolean isWX) {
		Log.m(TAG,"shareWX start");
		if (wxapi == null) {
			wxapi = WXAPIFactory.createWXAPI(context, APP_KEY_WX);
			Log.m(TAG,"shareWX registerApp");
			wxapi.registerApp(APP_KEY_WX);
		}
		WXWebpageObject webpage = new WXWebpageObject();
		webpage.webpageUrl = openUrl == null ? DEF_URL : openUrl;
		WXMediaMessage msg = new WXMediaMessage(webpage);
		msg.title = title == null ? "" : title;
		if (isWX) {
			msg.description = content == null ? "" : content;
		}
//		Bitmap thumb = BitmapFactory.decodeResource(context.getResources(),
//				R.drawable.logo_144_rect);
		Bitmap thumb = null;
//		File file = ImageLoader.getInstance().getDiscCache().get(imgUrl);
//        if(null != file && !TextUtils.isEmpty(file.getPath())){
//			thumb = BitmapUtil.scaleBitmap(file);
//			msg.thumbData = BitmapUtil.bitmap2Bytes(thumb,32);
//		}else{
//
//		}
        thumb = BitmapFactory.decodeFile(copyImg(context,"img/logo_144_rect.png","logo_144_rect.png"));
        if(thumb != null){
            Log.m(TAG, "thumb w = " + thumb.getWidth() + " h = " + thumb.getHeight());
            msg.thumbData = bmpToByteArray(thumb, true);
			thumb.recycle();
        }

		SendMessageToWX.Req req = new SendMessageToWX.Req();
		req.transaction = String.valueOf(System.currentTimeMillis());
		req.message = msg;
		req.scene = isWX ? SendMessageToWX.Req.WXSceneSession
				: SendMessageToWX.Req.WXSceneTimeline;
		Log.m(TAG,"shareWX sendReq");
		wxapi.sendReq(req);
	}
	
	
	public static byte[] bmpToByteArray(final Bitmap bmp,
			final boolean needRecycle) {
		int i;
		int j;
		if (bmp.getHeight() > bmp.getWidth()) {
			i = bmp.getWidth();
			j = bmp.getWidth();
		} else {
			i = bmp.getHeight();
			j = bmp.getHeight();
		}

		Bitmap localBitmap = Bitmap.createBitmap(i, j, Bitmap.Config.RGB_565);
		Canvas localCanvas = new Canvas(localBitmap);

		while (true) {
			localCanvas.drawBitmap(bmp, new Rect(0, 0, i, j), new Rect(0, 0, i,
					j), null);
			if (needRecycle)
				bmp.recycle();
			ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream();
			localBitmap.compress(Bitmap.CompressFormat.JPEG, 100,
					localByteArrayOutputStream);
			localBitmap.recycle();
			byte[] arrayOfByte = localByteArrayOutputStream.toByteArray();
			try {
				localByteArrayOutputStream.close();
				return arrayOfByte;
			} catch (Exception e) {
				// F.out(e);
			}
			i = bmp.getHeight();
			j = bmp.getHeight();
		}
	}
	
	
	public void shareCopy(Context context, String content) {
		if (content != null) {
			if (Build.VERSION.SDK_INT <= 11) {
				ClipboardManager cmb = (ClipboardManager) context
						.getSystemService(Context.CLIPBOARD_SERVICE);
				cmb.setText(content.trim());
			} else {
				android.content.ClipboardManager m = (android.content.ClipboardManager) context
						.getSystemService(Context.CLIPBOARD_SERVICE);
				m.setPrimaryClip(ClipData.newPlainText(null, content.trim()));
			}
		}
	}
}

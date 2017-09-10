package com.museumguild.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Build.VERSION;
import android.os.Environment;
import android.os.Handler;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.museumguild.R;
import com.museumguild.manage.ActManager;
import com.museumguild.utils.share.Share;
import com.museumguild.utils.share.WeiboAssistActivity;
import com.museumguild.view.activities.BaseActivity;
import com.museumguild.view.activities.TmpActivity;
import com.museumguild.view.application.MyApplication;
import com.museumguild.view.view.CircleBitmapDisplayer;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.sina.weibo.sdk.api.share.IWeiboHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Util {
	private static Share share = new Share();
	private static final String TAG = "Util";
	private static long lastClickTime;
	private static String mVersionName;
	public static HashMap<String, String> communitiesCacheMap = new HashMap<String, String>();
	/**
	 * 将一个字符串转成MD5格式
	 * @param val
	 * @return MD5码
	 */
	public static String transferToMD5(String val)
	{
		try {
			MessageDigest md5 = MessageDigest.getInstance("MD5");
			md5.update(val.getBytes("UTF-8"));
			//加密
			byte[] hash = md5.digest();
			StringBuilder hex = new StringBuilder(hash.length * 2);
		    for (byte b : hash) {
		        if ((b & 0xFF) < 0x10) hex.append("0");
		        hex.append(Integer.toHexString(b & 0xFF));
		    }
		    Log.m(TAG , "md5:"+hex.toString());
		    return hex.toString();
			
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 组装url的后缀 注:该方法会在头部加上&符号,尾部不会加上
	 * @param strings
	 * @return
	 */
	public static String assembleUrlSuffix(String...strings){
		if(null==strings || strings.length == 0){
			Log.e(TAG, "assembleUrlSuffix input param error");
		}
		StringBuffer buffer = new StringBuffer();
		int length = strings.length;
		if(length >0 )
		{
			buffer.append("&");
		}
		for (int i = 0; i < length; i++) {
			if( (i+1)%2 != 0)
			{
				buffer.append(strings[i]);
			}
			else {
				buffer.append("=").append(strings[i]).append("&");
			}
		}
		String ret = buffer.toString();
		if(ret.endsWith("&")){
			ret = ret.substring(0, buffer.length()-1);
		}
		return ret;
	}
	
	/**
	 * 拍照获取图片
	 */
	public static Uri takePhoto(Activity activity) {
		return takePhoto(activity, 0);
	}
	
	public static Uri takePhoto(Activity activity, int requstCode) {
		int code = Constant.SELECT_PIC_BY_TACK_PHOTO;
		if(0 != requstCode)
		{
			code = requstCode;
		}
		/*// 执行拍照前，应该先判断SD卡是否存在
		String SDState = Environment.getExternalStorageState();
		if (SDState.equals(Environment.MEDIA_MOUNTED)) {
			Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);// "android.media.action.IMAGE_CAPTURE"
			*//***
			 * 需要说明一下，以下操作使用照相机拍照，拍照后的图片会存放在相册中的 这里使用的这种方式有一个好处就是获取的图片是拍照后的原图
			 * 如果不实用ContentValues存放照片路径的话，拍照后获取的图片为缩略图不清晰
			 *//*
			ContentValues values = new ContentValues();
			Uri photoUri = activity.getContentResolver().insert(
					MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
			intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, photoUri);
			*//** ----------------- *//*
			activity.startActivityForResult(intent, Constant.SELECT_PIC_BY_TACK_PHOTO);
		} else {
			Toast.makeText(activity, "内存卡不存在", Toast.LENGTH_LONG).show();
		}*/
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		SimpleDateFormat timeStampFormat = new SimpleDateFormat(
		"yyyy_MM_dd_HH_mm_ss");
		String filename = timeStampFormat.format(new Date());
//		ContentValues values = new ContentValues();
//		values.put(Media.TITLE, filename);
//		Uri photoUri = activity.getContentResolver().insert(
//		MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
//		intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);

		String filePath = Environment.getExternalStorageDirectory()
				.getAbsolutePath() + File.separator + filename + ".png";
		Uri photoUri = Uri.fromFile(new File(filePath));
		intent.putExtra(MediaStore.EXTRA_OUTPUT,
				photoUri);
		activity.startActivityForResult(intent, code);
		return photoUri;
	}
	/***
	 * 从相册中取图片
	 */
	public static void pickPhoto(Activity activity) {
		pickPhoto(activity, 0);
	}
	
	public static void pickPhoto(Activity activity, int requstCode) {
		int code = Constant.SELECT_PIC_BY_PICK_PHOTO;
		if(0 != requstCode)
		{
			code = requstCode;
		}
		Intent intent = new Intent();
		intent.setType("image/*");
		intent.setAction(Intent.ACTION_GET_CONTENT);
		activity.startActivityForResult(intent, code);
	}

	/**
	 * 根据Activity返回的data，获取图片的路径
	 * 
	 * @param data
	 */
	public static String doPhoto(Intent data, Activity activity) {
		return getPhotoPath(data, null, activity);
	}
	
	/**
	 * 根据Activity返回的data，获取图片的路径
	 * 
	 * @param data
	 */
	public static String getPhotoPath(Intent data, Uri picUri , Activity activity) {
		 // 从相册取图片，有些手机有异常情况，请注意
		Uri photoUri =null;
		if (data == null || data.getData()==null  ) {
			if( picUri ==null)
			{
				Toast.makeText(activity, "选择图片文件出错", Toast.LENGTH_LONG).show();
				return null;
			}
			photoUri = picUri ;
		}
		else
		{
			photoUri = data.getData();
		}
		Log.m(TAG, "photoUri:"+photoUri);
		String picPath = "";
		String[] proj = { MediaStore.Images.Media.DATA };
		Cursor cursor = activity.managedQuery(photoUri, proj, null, null, null);
		if (cursor != null) {
			Log.e(TAG, "cursor != null");
			int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
			cursor.moveToFirst();
			picPath = cursor.getString(column_index);
			if(VERSION.SDK_INT < 14) {
			    cursor.close();  
			 }  
		}
		if(TextUtils.isEmpty(picPath)){
			picPath = getPath(activity, photoUri);
		}
		Log.m(TAG, "imagePath = " + picPath);
		if (picPath != null&& (picPath.endsWith(".png") || picPath.endsWith(".PNG")|| picPath.endsWith(".jpg") || picPath.endsWith(".JPG"))) 
		{
			return picPath;
		} else {
			Log.e(TAG, "某些手机上无法获取原图: 选择图片文件不正确");
		}
		return null;
	}
	
	

	
    /**
     * 回到系统桌面
     * @param context
     */
	public static void showDesktop(Context context) {
		Intent home  = new Intent(Intent.ACTION_MAIN);
		home.addCategory(Intent.CATEGORY_HOME);
		context.startActivity(home);
	}

	/**
	 * 退出应用
	 */
	public static void exitApp(Context context) {
		LinkedList<BaseActivity> actLists = ActManager.getActList();
		for (int i = 0; i < actLists.size(); i++) {
			actLists.remove(i).finish();
		}

		//目前最为通用的 关闭进程的方法以后的版本使用
		Intent startMain = new Intent(context, TmpActivity.class);
		startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
		context.startActivity(startMain);
	}
	
	/**
	 * 连续点击2次返回退出应用
	 */
	public static void onBackEvent(Context context)
	{
		if((System.currentTimeMillis()-lastClickTime)>2000)
		{//间隔2秒则不退出
			toast("再按一次退出程序",300);
			lastClickTime = System.currentTimeMillis();
		}
		else{
			exitApp(context);
		}
	}
	
	public static void toast(String msg, int time) {
		if(msg== null){
			return;
		}
		Toast.makeText(MyApplication.context, msg, time).show();
	}
	
	public static void toast(String msg) {
		if(msg== null){
			return;
		}
		msg = msg.replace("token值已过期", "请重新登录");
		toast(msg, 500);
	}

	public static String getRefreshTime(String articleType)
	{
		String time  = PrefenceUtil.readString("ARTICLE_"+articleType);
		if(TextUtils.isEmpty(time))
		{
			return "我好笨,忘记刷新了...";
		}
		return time;
	}
	
	/**
	 * 根据文章类型设置更新上次更新时间
	 * @param articleType
	 */
	@SuppressLint("SimpleDateFormat")
	public static void setRefreshTime(String articleType)
	{
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		PrefenceUtil.write("ARTICLE_"+articleType, format.format(new Date()));
	}
	
	public static boolean isNetAvailable(Context context)
	{
		//判断网络连接方式
		boolean wifiConnected = isWIFIConnected(context);
		boolean mobileConnected = isMOBILEConnected(context);
		if(!wifiConnected && !mobileConnected)
		{
			Log.e(TAG, " no network ");
			return false;
		}
		else
		{
			return true;
		}
	}

	/**
	 * 判断手机是否采用wifi连接
	 * @param context
	 * @return
	 */
	public static boolean isWIFIConnected(Context context) {
		ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info =manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		if(null!=info && info.isConnected())
		{
			return true;
		}
		return false;
	}
	/**
	 * 判断手机是否采用数据网络连接
	 * @param context
	 * @return
	 */
	public static boolean isMOBILEConnected(Context context) {
		ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
		if (networkInfo != null && networkInfo.isConnected())
		{
			return true;
		}
		return false;
	}
	
	
	public static String getCurrentTime()
	{
		SimpleDateFormat format =  new SimpleDateFormat("yyyy-MM-dd HH:mm");
		return format.format(new Date());
	}
	
	/**
	 * 给content中
	 * <img src=\"/dbcV2_66810/ueditor/jsp/upload/1445653978873.jpg\">字段的
	 * 图片URL添加前缀
	 */
	public static String appendWebSitePrefixUrl(String content) {
		if( null == content )
		{
			return null;
		}
		String target = "/" + Constant.WEB_PROJECT_NAME+"/" + Constant.WEB_PATH_UEEDIT_JSP_UPLOAD;
		
		return content.replace(target, Constant.WEB_SITE_URL+target);
	}

	/**
	 * 根据Uri,获取图片路径
	 * @param context
	 * @param uri
	 * @return
	 */
	@SuppressLint("NewApi")
	public static String getPath(final Context context, final Uri uri) {
		  
	    final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
	  
	    // DocumentProvider  
	    if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
	        // ExternalStorageProvider  
	        if (isExternalStorageDocument(uri)) {  
	            final String docId = DocumentsContract.getDocumentId(uri);
	            final String[] split = docId.split(":");
	            final String type = split[0];
	  
	            if ("primary".equalsIgnoreCase(type)) {  
	                return Environment.getExternalStorageDirectory() + "/" + split[1];
	            }  
	  
	            // TODO handle non-primary volumes  
	        }  
	        // DownloadsProvider  
	        else if (isDownloadsDocument(uri)) {  
	  
	            final String id = DocumentsContract.getDocumentId(uri);
	            final Uri contentUri = ContentUris.withAppendedId(
	                    Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));
	  
	            return getDataColumn(context, contentUri, null, null);  
	        }  
	        // MediaProvider  
	        else if (isMediaDocument(uri)) {  
	            final String docId = DocumentsContract.getDocumentId(uri);
	            final String[] split = docId.split(":");
	            final String type = split[0];
	  
	            Uri contentUri = null;
	            if ("image".equals(type)) {  
	                contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
	            } else if ("video".equals(type)) {  
	                contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
	            } else if ("audio".equals(type)) {  
	                contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
	            }  
	  
	            final String selection = "_id=?";
	            final String[] selectionArgs = new String[] {
	                    split[1]  
	            };  
	  
	            return getDataColumn(context, contentUri, selection, selectionArgs);  
	        }  
	    }  
	    // MediaStore (and general)  
	    else if ("content".equalsIgnoreCase(uri.getScheme())) {  
	  
	        // Return the remote address  
	        if (isGooglePhotosUri(uri))  
	            return uri.getLastPathSegment();  
	  
	        return getDataColumn(context, uri, null, null);  
	    }  
	    // File  
	    else if ("file".equalsIgnoreCase(uri.getScheme())) {  
	        return uri.getPath();  
	    }  
	  
	    return null;  
	}  
	  
	/** 
	 * Get the value of the data column for this Uri. This is useful for 
	 * MediaStore Uris, and other file-based ContentProviders. 
	 * 
	 * @param context The context. 
	 * @param uri The Uri to query. 
	 * @param selection (Optional) Filter used in the query. 
	 * @param selectionArgs (Optional) Selection arguments used in the query. 
	 * @return The value of the _data column, which is typically a file path. 
	 */  
	public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {
	  
	    Cursor cursor = null;
	    final String column = "_data";
	    final String[] projection = {
	            column  
	    };  
	  
	    try {  
	        cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,  
	                null);  
	        if (cursor != null && cursor.moveToFirst()) {  
	            final int index = cursor.getColumnIndexOrThrow(column);  
	            return cursor.getString(index);  
	        }  
	    } finally {  
	        if (cursor != null)  
	            cursor.close();  
	    }  
	    return null;  
	}  
	  
	  
	/** 
	 * @param uri The Uri to check. 
	 * @return Whether the Uri authority is ExternalStorageProvider. 
	 */  
	public static boolean isExternalStorageDocument(Uri uri) {
	    return "com.android.externalstorage.documents".equals(uri.getAuthority());  
	}  
	  
	/** 
	 * @param uri The Uri to check. 
	 * @return Whether the Uri authority is DownloadsProvider. 
	 */  
	public static boolean isDownloadsDocument(Uri uri) {
	    return "com.android.providers.downloads.documents".equals(uri.getAuthority());  
	}  
	  
	/** 
	 * @param uri The Uri to check. 
	 * @return Whether the Uri authority is MediaProvider. 
	 */  
	public static boolean isMediaDocument(Uri uri) {
	    return "com.android.providers.media.documents".equals(uri.getAuthority());  
	}  
	  
	/** 
	 * @param uri The Uri to check. 
	 * @return Whether the Uri authority is Google Photos. 
	 */  
	public static boolean isGooglePhotosUri(Uri uri) {
	    return "com.google.android.apps.photos.content".equals(uri.getAuthority());  
	}  
	
	/**
	 * 获取xml中的dp对应的px值
	 * @param context
	 * @param dip
	 * @return
	 */
	public static int getPixFromDipSize(Context context, int dip)
	{
		return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dip,context.getResources().getDisplayMetrics());
	}
	
	/**
	 * 用于解析请求返回的结果中的code,是否成功
	 * 
	 * @param result
	 *            后台返回的请求结果
	 * @return 请求成功/失败
	 */
	public static boolean parseResultIsRequstOk(String result) {
		boolean isSucces = false;
		if (!TextUtils.isEmpty(result)) {
			try {
				JSONObject jo = new JSONObject(result);
				if (jo.getInt("code") == 1) {
					isSucces = true;
				} else {
					isSucces = false;
				}
			} catch (JSONException e) {
				Log.e(TAG,"parseResultIsRequstOk JSONException:" + e.toString());
				e.printStackTrace();
			}
		}
		return isSucces;
	}
	/**
	 * 解析返回的错误消息
	 * @param result
	 * @return
	 */
	public static String parseResultFailMsg(String result) {
		String msg = "";
		if (!TextUtils.isEmpty(result)) {
			try {
				JSONObject jo = new JSONObject(result);
				if(!TextUtils.isEmpty(jo.getString("msg"))){
					msg = jo.getString("msg");
				}
			} catch (JSONException e) {
				Log.e(TAG,"parseResultFailMsg JSONException:" + e.toString());
				e.printStackTrace();
			}
		}
		return msg;
	}
	
	/**
	 * 只对url中的中文做编码
	 * @param url
	 * @return
	 */
	public static String urlEncodeCN(String url)
	{  
	    try {  
	         Matcher matcher = Pattern.compile("[\\u4e00-\\u9fa5]").matcher(url);
	         while (matcher.find()) {  
	           String tmp=matcher.group();
	           url=url.replaceAll(tmp,java.net.URLEncoder.encode(tmp,"utf-8"));  
	         }  
	    } catch (UnsupportedEncodingException e) {
	        e.printStackTrace();  
	    }  
	    return url;  
	}
	
	/**
	 * 处理服务端返回各种奇葩URL
	 * @param url 服务器返回的url
	 * @return 可以识别的url
	 */
	public static String dealWithServerPicPath(String url)
	{
		if(TextUtils.isEmpty(url)){
			//Log.e(TAG, "when dealWithServerPicPath ,input url isEmpty ,return null");
			return null;
		}
		String imgUrl = "";
		url = url.replace("\\", "/");
		if(url.startsWith("http"))
		{
			imgUrl = url;
		}
		else
		{
			imgUrl = Constant.WEB_SITE_ROOT_URL+ url;
		}
		return urlEncodeCN(imgUrl);
	}
	
	public static InputStream Bitmap2ISWithCompress(Bitmap bm , int rate){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, rate, baos);//质量压缩方法，这里100表示不压缩，40表示压缩40%,把压缩后的数据存放到baos中
		int options = 100;
		while ( baos.toByteArray().length / 1024>300) {	//循环判断如果压缩后图片是否大于100kb,大于继续压缩		
			baos.reset();//重置baos即清空baos
			bm.compress(Bitmap.CompressFormat.JPEG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中
			options -= 20;//每次都减少10
		}
        InputStream sbs = new ByteArrayInputStream(baos.toByteArray());
        return sbs;  
	}
	
	public static InputStream Bitmap2IS(Bitmap bm){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            InputStream sbs = new ByteArrayInputStream(baos.toByteArray());
            return sbs;  
	}
	
	public static boolean isConnected(Context context) {
        ConnectivityManager conn = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = conn.getActiveNetworkInfo();
        return (info != null && info.isConnected());
    }
    
	public static String getImei(Context context, String imei) {
		try {
			TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
			imei = telephonyManager.getDeviceId();
		} catch (Exception e) {
			Log.e(TAG, "getImei Exception:"+e);
			e.printStackTrace();
		}
		return imei;
	}
	
	public static int getVersionCode(Context context) {
		try {
			PackageManager packageManager = context.getPackageManager();
			PackageInfo packageInfo = packageManager.getPackageInfo(
					context.getPackageName(), 0);
			return packageInfo.versionCode;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
			return 0;
		}
	}
	
	public static String getVersionName(Context context) {
		if(!TextUtils.isEmpty(mVersionName))
		{
			return mVersionName;
		}
		try {
			PackageManager packageManager = context.getPackageManager();
			PackageInfo packageInfo = packageManager.getPackageInfo(
					context.getPackageName(), 0);
			mVersionName = packageInfo.versionName;
			return mVersionName;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
			return "";
		}
	}
	
	public static void dial(Activity act , String phoneNumber)
	{
		Intent intent = new Intent(Intent.ACTION_DIAL);
		Uri data = Uri.parse("tel:" + phoneNumber);
		intent.setData(data);
		act.startActivity(intent);
	}
	
	/**
	 * 处理给服务器 的用户输入文字
	 * @param str
	 * @return 
	 */
	public static String dealUserInputTextToServer(String str)
	{
		if(TextUtils.isEmpty(str)){
			return null;
		}
		str = str.replace("\\", "\\\\");
		return str;
	}

	public static void hideKeyBoard(Context context, View view){
		InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
		//imm.showSoftInput(view,InputMethodManager.SHOW_FORCED);
		imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
	}

	public static void showKeyBoard(View et) {
		InputMethodManager inputManager =
				(InputMethodManager)et.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
		inputManager.showSoftInput(et, 0);
	}

	public static DisplayImageOptions getRoundImageOption(){
		DisplayImageOptions options = new DisplayImageOptions.Builder()
				.cacheInMemory(true)
				.cacheOnDisk(true)
				.displayer(new CircleBitmapDisplayer())
				.build();
		return options;
	}

	public static void onSinaAuthorResult(int requestCode, int resultCode, Intent data)
	{
		Log.m(TAG, "Util onActivityResult requestCode = " + requestCode);
		share.onSinaAuthorResult(requestCode, resultCode, data);
	}

//	public static void shareSinaWeibo(Context context ,Handler h, String title)
//	{
//		share.shareSinaWeibo(context,"[" + title + "]" + "share sina content",getUrl(),h);
//	}

	/**
	 * 使用 {@link WeiboAssistActivity} 中间跳转来分享
	 * @param context
	 * @param h
	 */
	public static void shareSinaWeiboByActivity(Context context , Handler h)
	{
		Intent intent = new Intent(context,WeiboAssistActivity.class);
		intent.putExtra("isNeedShare", true);
		context.startActivity(intent);
	}

	public static void shareQQ(Context context , String title, String content , String openUrl , String imgUrl)
	{
		share.shareQQ(context, context.getString(R.string.app_name),title ,content , openUrl, imgUrl);
	}

	public static void shareWX(Context context, String title, String content , String openUrl , String imgUrl)
	{
		share.shareWX(context, title,content, openUrl, imgUrl, true);
	}

	public static void shareWXFriend(Context context, String title, String content , String openUrl , String imgUrl)
	{
		share.shareWX(context, title,content, openUrl, imgUrl, false);
	}

//	public static void shareCopy(Context context)
//	{
//		share.shareCopy(context,getUrl());
//	}

	public static void handleWeiboResponse(Intent intent, IWeiboHandler.Response response){
		share.handleWeiboResponse(intent, response);
	}


	//public static String getUrl() {
//		String url = GLiveSharePreferences.getIns().getShareAddr();;
//		if ("".equals(url)) {
//			url = RTSharedPref.getIns().getString(RTSharedPref.KEY_WEB_PLAYER_URL,"");
//		}
		//TODO
	//	return "http://www.qq.com";
	//}

	public static void clearAllCache(Context context) {
		ImageLoader.getInstance().clearDiskCache();
		ImageLoader.getInstance().clearMemoryCache();
//		deleteDir(context.getCacheDir());
//		if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
//			deleteDir(context.getExternalCacheDir());
//		}
		FileUtil.cleanApplicationData(context);

		toast("缓存清理成功");
	}

	private static boolean deleteDir(File dir) {
		if (dir != null && dir.isDirectory()) {
			String[] children = dir.list();
			for (int i = 0; i < children.length; i++) {
				boolean success = deleteDir(new File(dir, children[i]));
				if (!success) {
					return false;
				}
			}
		}
		return dir.delete();
	}

	public static String getShortDate(String longDate) {
		if(TextUtils.isEmpty(longDate)){
			return longDate.substring(0, 10);
		}
		else{
			return  null;
		}
	}

	public static String checkEmoji(String str) {
        if (null == str) {
            return "";
        }
        Pattern pattern = Pattern.compile(
                "[\ud83c\udc00-\ud83c\udfff]|[\ud83d\udc00-\ud83d\udfff]|[\u2600-\u27ff]",
                Pattern.UNICODE_CASE | Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(str);
        while (matcher.find()) {
            str = str.replace(matcher.group(),
                    "[Emoji]");
        }
        return str;
    }

}

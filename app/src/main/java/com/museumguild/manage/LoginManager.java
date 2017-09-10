package com.museumguild.manage;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.museumguild.bean.UserInfo;
import com.museumguild.http.ResultCallback;
import com.museumguild.utils.Event;
import com.museumguild.utils.Log;
import com.museumguild.utils.PrefenceUtil;
import com.museumguild.utils.Util;
import com.museumguild.view.activities.LoginActivity;
import com.squareup.okhttp.Request;

import org.json.JSONException;
import org.json.JSONObject;

import de.greenrobot.event.EventBus;

public class LoginManager {
	public static final String DEFAULT_USERNAME = " ";
	private String token = "";
	private String userid = "";
	private String username = "";
	private String pic;
	private String sex;
	private static final String TAG = "LoginManager";

	public String getToken() {
		return token;
	}

	public String getUserid() {
		return null == userid? "":userid;
	}

	public String getUsername() {
		return username;
	}

	public String getPic() {
		return pic;
	}

	public String getSex() {
		return sex;
	}

	private static LoginManager loginManager;
	private LoginManager(){};
	public static LoginManager getIns(){
		if(null == loginManager){
			synchronized (LoginManager.class){
				if(null == loginManager){
					loginManager = new LoginManager();
				}
			}
		}
		return loginManager;
	}

	public boolean isLogin(){
		return !TextUtils.isEmpty(token);
	}

	public void gotoLoginPage(Context context, String msg){
		Util.toast(msg);
		context.startActivity(new Intent(context,LoginActivity.class));
	}

	public void gotoLoginPage(Context context){
		gotoLoginPage(context,"请先登录~~");
	}

	public void setUserInfoAndToken(String response){
		if(TextUtils.isEmpty(response)){
			return;
		}
		try {
			JSONObject jo = new JSONObject(response);
			token = jo.getString("token");
			userid = jo.optString("userid","");
			username = jo.optString("username","");
			pic = jo.optString("pic","");
			pic = Util.dealWithServerPicPath(pic);
			sex = jo.optString("sex","");
			UserInfo userInfo = new UserInfo(token, userid, username, pic, sex);
			EventBus.getDefault().post(new Event(Event.EVENT_LOGINED,userInfo));
			//TODO
		} catch (JSONException e) {
			e.printStackTrace();
		}

	}

	public void logoutAndBackToLoginPage(Context context) {
		//清除本地保存的密码
		PrefenceUtil.write("password", "");
		PrefenceUtil.write("username", LoginManager.DEFAULT_USERNAME);
		//清空内存中的一些参数,只留用户名
		token = "";
		userid = "";
		username = "";
		pic = "";
		sex = "";
		UserInfo userInfo = new UserInfo(token, userid, username, pic, sex);
		EventBus.getDefault().post(new Event(Event.EVENT_LOGINED,userInfo));
		Intent intent = new Intent(context, LoginActivity.class);
		intent.putExtra("isLogOut", true);
		context.startActivity(intent);
	}

	public void backgroundLogin(){
		Log.m(TAG,"backgroundLogin start");
		final String username = PrefenceUtil.readString("username", LoginManager.DEFAULT_USERNAME);
		final String password = PrefenceUtil.readString("password", "1");
		new Thread(){
			@Override
			public void run() {
				ServerApiManager.getIns().login(username,password,new ResultCallback<String>() {
					@Override
					public void onError(Request request, Exception e) {
						Log.m(TAG, "background Login onError:"+ e);
					}
					@Override
					public void onResponse(String response) {
						Log.m(TAG, "background Login result:"+response);
						//回调
						try {
							JSONObject jo = new JSONObject(response);
							//Util.toast(jo.optInt("code")==1?"登录成功":jo.optString("msg"));;
							if(jo.optInt("code")==1)
							{
								PrefenceUtil.write("username", username);
								PrefenceUtil.write("password", password);
								//登录成功,直接进入
								setUserInfoAndToken(response);
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				});
			}
		}.start();
	}
	
}

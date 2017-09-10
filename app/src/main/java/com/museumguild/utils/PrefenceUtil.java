package com.museumguild.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import com.museumguild.view.application.MyApplication;


public class PrefenceUtil {

	public static void write(String key, String value)
	{
		SharedPreferences sp = MyApplication.context.getSharedPreferences(Constant.PRE_MUSEUM_APP, Activity.MODE_PRIVATE);
		sp.edit().putString(key, value).commit();
	}
	
	public static void write(String key, int value)
	{
		SharedPreferences sp = MyApplication.context.getSharedPreferences(Constant.PRE_MUSEUM_APP, Activity.MODE_PRIVATE);
		sp.edit().putInt(key, value).commit();
	}
	public static void write(String key, boolean value)
	{
		SharedPreferences sp = MyApplication.context.getSharedPreferences(Constant.PRE_MUSEUM_APP, Activity.MODE_PRIVATE);
		sp.edit().putBoolean(key, value).commit();
	}
	
	public static String readString(String key)
	{
		return readString(key, "");
	}
	
	public static String readString(String key, String defValue)
	{
		SharedPreferences sp = MyApplication.context.getSharedPreferences(Constant.PRE_MUSEUM_APP, Activity.MODE_PRIVATE);
		return sp.getString(key, defValue);
	}
	
	public static int readInt(String key)
	{
		return readInt(key, 0);
	}
	
	public static int readInt(String key, int defValue)
	{
		SharedPreferences sp = MyApplication.context.getSharedPreferences(Constant.PRE_MUSEUM_APP, Activity.MODE_PRIVATE);
		return sp.getInt(key, defValue);
	}
	
	public static boolean readBoolean(String key)
	{
		return readBoolean(key, false);
	}
	
	public static boolean readBoolean(String key , boolean defValue)
	{
		SharedPreferences sp = MyApplication.context.getSharedPreferences(Constant.PRE_MUSEUM_APP, Activity.MODE_PRIVATE);
		return sp.getBoolean(key, defValue);
	}
	
	public static void remove(Context context , String key)
	{
		SharedPreferences sharedPreferences = context.getSharedPreferences(Constant.PRE_MUSEUM_APP,
				Context.MODE_PRIVATE);
		sharedPreferences.edit().remove(key).commit();
	}
}

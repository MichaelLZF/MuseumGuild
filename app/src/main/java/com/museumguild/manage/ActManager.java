package com.museumguild.manage;

import android.app.Activity;


import com.museumguild.view.activities.BaseActivity;

import java.util.LinkedList;

public class ActManager {
	private static LinkedList<BaseActivity> actList = new LinkedList<BaseActivity>();
	private static BaseActivity topActivity;
	
	
	public static LinkedList<BaseActivity> getActList() {
		return actList;
	}

	public static void setActList(LinkedList<BaseActivity> actList) {
		ActManager.actList = actList;
	}

	public static void addToActList(BaseActivity act) {
		actList.add(act);
	}
	
	public static void removeFromActList(BaseActivity act) {
		actList.remove(act);
	}
	
	public static Activity getActivity(String actName)
	{
		for (Activity act : actList) {
			if(act.getComponentName().getClassName().equals(actName))
			{
				return act;
			}
		}
		return null;
	}
	
	public static void finishAllActivity()
	{
		actList.clear();
	}
	
	
	/**
	 * 退出应用
	 */
	public static void exitApp() {
		for (int i = 0; i < actList.size(); i++) {
			actList.remove(i).finish();
		}
		System.exit(0);
	}
	
	public static void setTopActivity(BaseActivity activity) {
		topActivity = activity;
	}
	
	public static Activity getTopActivity()
	{
		return topActivity != null ? topActivity: null;
	}
	
}

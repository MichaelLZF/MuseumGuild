package com.museumguild.manage;

import android.text.TextUtils;

import com.museumguild.http.ResultCallback;
import com.museumguild.http.request.OkHttpRequest;
import com.museumguild.utils.Constant;
import com.museumguild.utils.Log;
import com.museumguild.utils.Util;

import java.util.List;

/**
 * 与后台交互的接口
 * @author 
 *
 */
public class ServerApiManager {
	private static final String TAG = "ServerApiManager";
	private static ServerApiManager serverApi = null;
	public static final String PAGE_SIZE_STRING = "15";
	private ServerApiManager(){};
	public static ServerApiManager getIns() {
		if(null == serverApi){
			synchronized (ServerApiManager.class) {
				if (null == serverApi) {
					serverApi = new ServerApiManager();
				}
			}
		}
		return serverApi;
	}

	/**
	 * 向服务器注册
	 * @param username
	 * @param email
	 * @param password
	 * @param callback
     */
	public void register(String username, String email, String password, final ResultCallback<String> callback)
	{
		String param = Util.assembleUrlSuffix("username",username,"email",email,"password",password);
		String url = Constant.URL_PREFIX + "register" + param;
		Log.m(TAG, "register start url:"+url);
		new OkHttpRequest.Builder()
				.url(url)
				.get(callback);
	}

	public void login(String username, String password, final ResultCallback<String> callback)
	{
		String param = Util.assembleUrlSuffix("username",username,"password",password);
		String url = Constant.URL_PREFIX + "login" + param;
		Log.m(TAG, "login start url:"+url);
		new OkHttpRequest.Builder()
		.url(url)
		.get(callback);
	}

	public void getQuestionType(String token, final ResultCallback<String> callback )
	{
		String param = Util.assembleUrlSuffix("token",token);
		String url = Constant.URL_PREFIX + "list_question_type" + param;
		Log.m(TAG, "getQuestionType start url:"+url);
		new OkHttpRequest.Builder()
				.url(url)
				.get(callback);
	}

	public void getStudyList(String token, String question_type, final ResultCallback<String> callback)
	{
		String param = Util.assembleUrlSuffix("token",token, "question_type", question_type);
		String url = Constant.URL_PREFIX + "list_studybyquestion_type" + param;
		Log.m(TAG, "getStudyList start url:"+url);
		new OkHttpRequest.Builder()
				.url(url)
				.get(callback);
	}

	public void list_shequ(String gotopageString, final ResultCallback<String> callback) {
		String param = Util.assembleUrlSuffix("token",LoginManager.getIns().getToken(),
				"pagesizeString",PAGE_SIZE_STRING,"gotopageString",gotopageString);
		String url = Constant.URL_PREFIX + "list_shequ" + param;
		Log.m(TAG, "list_shequ start url:"+url);
		new OkHttpRequest.Builder()
				.url(url)
				.get(callback);
	}

	public void list_myshequ(String gotopageString, final ResultCallback<String> callback) {
		String param = Util.assembleUrlSuffix("token",LoginManager.getIns().getToken(),
				"pagesizeString",PAGE_SIZE_STRING,"gotopageString",gotopageString);
		String url = Constant.URL_PREFIX + "list_myshequ" + param;
		Log.m(TAG, "list_myshequ start url:"+url);
		new OkHttpRequest.Builder()
				.url(url)
				.get(callback);
	}


	public void add_shequ(String neirong, List<String> serverPaths, final ResultCallback<String> callback) {
		String param = Util.assembleUrlSuffix("token",LoginManager.getIns().getToken());
		if(!TextUtils.isEmpty(neirong)){
			param += Util.assembleUrlSuffix("neirong",neirong);
		}
		if (null != serverPaths) {
			for (int i = 0; i < serverPaths.size(); i++) {
				param += "&pic" + (i + 1) + "=" + serverPaths.get(i);
			}
		}
		String url = Constant.URL_PREFIX + "add_shequ" + param;
		Log.m(TAG, "add_shequ start url:"+url);
		new OkHttpRequest.Builder()
				.url(url)
				.get(callback);
	}

	public void list_pinglun(String shequid, String pagesizeString, String gotopageString, final ResultCallback<String> callback)
	{
		String param = Util.assembleUrlSuffix("token",LoginManager.getIns().getToken(), "shequid", shequid);
		if(!TextUtils.isEmpty(pagesizeString)){
			param += Util.assembleUrlSuffix("pagesizeString", pagesizeString);
		}
		if(!TextUtils.isEmpty(gotopageString)){
			param += Util.assembleUrlSuffix("gotopageString", gotopageString);
		}
		String url = Constant.URL_PREFIX + "list_pinglun" + param;
		Log.m(TAG, "list_pinglun start url:"+url);
		new OkHttpRequest.Builder()
				.url(url)
				.get(callback);
	}

	public void list_zan(String shequid, final ResultCallback<String> callback)
	{
		String param = Util.assembleUrlSuffix("token",LoginManager.getIns().getToken(), "shequid", shequid);
		String url = Constant.URL_PREFIX + "list_zan" + param;
		Log.m(TAG, "list_zan start url:"+url);
		new OkHttpRequest.Builder()
				.url(url)
				.get(callback);
	}

	public void set_zan(String shequid, String zantype, final ResultCallback<String> callback)
	{
		String param = Util.assembleUrlSuffix("token",LoginManager.getIns().getToken(), "shequid",shequid, "zantype",zantype);
		String url = Constant.URL_PREFIX + "set_zan" + param;
		Log.m(TAG, "set_zan start url:"+url);
		new OkHttpRequest.Builder()
				.url(url)
				.get(callback);
	}

	public void add_pinglun(String shequid, String neirong, final ResultCallback<String> callback)
	{
		String param = Util.assembleUrlSuffix("token",LoginManager.getIns().getToken(), "shequid",shequid, "neirong",neirong);
		String url = Constant.URL_PREFIX + "add_pinglun" + param;
		Log.m(TAG, "add_pinglun start url:"+url);
		new OkHttpRequest.Builder()
				.url(url)
				.get(callback);
	}
	
	public void show_myinfo(final ResultCallback<String> callback)
	{
		String param = Util.assembleUrlSuffix("token",LoginManager.getIns().getToken());
		String url = Constant.URL_PREFIX + "show_myinfo" + param;
		Log.m(TAG, "show_myinfo start url:"+url);
		new OkHttpRequest.Builder()
				.url(url)
				.get(callback);
	}

	public void set_myinfo(String nickname, String picture, String sex, String birthday, final ResultCallback<String> callback)
	{
		String param = Util.assembleUrlSuffix("token",LoginManager.getIns().getToken());
		if(!TextUtils.isEmpty(nickname)){
			param += Util.assembleUrlSuffix("nickname", nickname);
		}
		if(!TextUtils.isEmpty(picture)){
			param += Util.assembleUrlSuffix("picture", picture);
		}
		if(!TextUtils.isEmpty(sex)){
			param += Util.assembleUrlSuffix("sex", sex);
		}
		if(!TextUtils.isEmpty(birthday)){
				param += Util.assembleUrlSuffix("birthday", birthday);
		}
		String url = Constant.URL_PREFIX + "set_myinfo" + param;
		Log.m(TAG, "set_myinfo start url:"+url);
		new OkHttpRequest.Builder()
				.url(url)
				.get(callback);
	}

	//8/21修改 , 藏品类型 修改为 展厅
	public void getListCollectionType(final ResultCallback<String> callback)
	{
		//String url = Constant.URL_PREFIX + "list_collection_type" ;
		String url = Constant.URL_PREFIX + "list_room" ;
		Log.m(TAG, "getListCollectionType start url:"+url);
		new OkHttpRequest.Builder()
				.url(url)
				.get(callback);
	}

	public void getListYearCollectionType(final ResultCallback<String> callback )
	{
		String url = Constant.URL_PREFIX + "list_year" ;
		Log.m(TAG, "getListYearCollectionType start url:"+url);
		new OkHttpRequest.Builder()
				.url(url)
				.get(callback);
	}

	public void getListWwCollectionType(final ResultCallback<String> callback )
	{
		String url = Constant.URL_PREFIX + "list_collection_type" ;
		Log.m(TAG, "getListWwCollectionType start url:"+url);
		new OkHttpRequest.Builder()
				.url(url)
				.get(callback);
	}

	public void getListCollection(String name, String room, String year, String wwlx, String pagesizeString, String gotopageString, final ResultCallback<String> callback)
	{
		String param = Util.assembleUrlSuffix("name",name, "room",room, "year", year, "wwlx", wwlx, "pagesizeString", pagesizeString, "gotopageString", gotopageString);
		String url = Constant.URL_PREFIX + "list_collection" + param;
		Log.m(TAG, "add_pinglun start url:"+url);
		new OkHttpRequest.Builder()
				.url(url)
				.get(callback);
	}

	public void getListCollection(String token, String name, String room, String year, String wwlx, final ResultCallback<String> callback)
	{
		String param = Util.assembleUrlSuffix("token",LoginManager.getIns().getToken(), "name",name, "room",room, "year", year, "wwlx", wwlx);
		String url = Constant.URL_PREFIX + "list_collection" + param;
		Log.m(TAG, "add_pinglun start url:"+url);
		new OkHttpRequest.Builder()
				.url(url)
				.get(callback);
	}

	public void getListCollectionById(String token, String id, String type, final ResultCallback<String> callback)
	{
		String param = Util.assembleUrlSuffix("token",LoginManager.getIns().getToken(), "id",id, "type",type);
		String url = Constant.URL_PREFIX + "list_collection" + param;
		Log.m(TAG, "list_collection start url:"+url);
		new OkHttpRequest.Builder()
				.url(url)
				.get(callback);
	}

	public void list_line_type(final ResultCallback<String> callback)
	{
		String param = Util.assembleUrlSuffix("token",LoginManager.getIns().getToken());
		String url = Constant.URL_PREFIX + "list_line_type" + param;
		Log.m(TAG, "list_line_type start url:"+url);
		new OkHttpRequest.Builder()
				.url(url)
				.get(callback);
	}

	public void list_line(String line_type, final ResultCallback<String> callback)
	{
		String param = Util.assembleUrlSuffix("token",LoginManager.getIns().getToken(),"line_type",line_type);
		String url = Constant.URL_PREFIX + "list_line" + param;
		Log.m(TAG, "list_line start url:"+url);
		new OkHttpRequest.Builder()
				.url(url)
				.get(callback);
	}

	public void get_version(final ResultCallback<String> callback){
		Log.m(TAG, "get_version start");
		String url = Constant.URL_PREFIX + "get_version";
		new OkHttpRequest.Builder()
				.url(url)
				.get(callback);
	}

	public void list_article(String treeid, String gotopageString, final ResultCallback<String> callback){
		String param = Util.assembleUrlSuffix("treeid",treeid,"pagesizeString",PAGE_SIZE_STRING,"gotopageString",gotopageString);
		String url = Constant.URL_PREFIX + "list_article" + param;
		Log.m(TAG, "list_article start url:"+url);
		new OkHttpRequest.Builder()
				.url(url)
				.get(callback);
	}

	public static String getCollectionShareUrl(String id){
		if(TextUtils.isEmpty(id)){
			return null ;
		}
		String param = Util.assembleUrlSuffix("id",id);
		String url = Constant.SHARE_URL_PREFIX + "show_collection" + param;
		Log.m(TAG, "show_collection url:"+url);
		return url;
	}

	public static String getArticleShareUrl(String id){
		if(TextUtils.isEmpty(id)){
			return null ;
		}
		String param = Util.assembleUrlSuffix("id",id);
		String url = Constant.SHARE_URL_PREFIX + "show_article" + param;
		Log.m(TAG, "show_article url:"+url);
		return url;
	}

}

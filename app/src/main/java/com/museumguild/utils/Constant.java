package com.museumguild.utils;

public class Constant {
	public static final int REQUEST_SCAN_CODE = 1 ;
	public static final String APP_NAME = "MuseumGuild";
	public static final boolean isDebug = true;
	public static final String PRE_MUSEUM_APP = "museum_app";
	public static final int SELECT_PIC_BY_TACK_PHOTO = 0x1;
	public static final int SELECT_PIC_BY_PICK_PHOTO = 0x2;
	
	public static final String UPLOAD_IMG_PATH_SUFFIX = "upload/image";
	//public static final String WEB_SITE_IP = "http://115.29.151.74";
	public static final String WEB_SITE_PORT = "8080";
//	public static final String WEB_SITE_URL = isDebug?"http://115.29.151.74:8080/":"http://www.onelv.net:8080/";
	public static final String WEB_SITE_URL = "http://10.121.32.57:8080/";
//	public static final String WEB_SITE_ROOT_URL = isDebug? "http://115.29.151.74:8080/xjapp/":"http://www.onelv.net:8080/xjapp/";
	public static final String WEB_SITE_ROOT_URL = "http://10.121.32.57:8080/dbcV4_CloudApp/";
	public static final String WEB_SITE_ROOT_URL1 = "http:\\\\www.onelv.net:8080\\xjapp\\";
//	public static final String WEB_PROJECT_NAME = "xjapp";
	public static final String WEB_PROJECT_NAME = "dbcV4_CloudApp";
	public static final String WEB_PATH_UEEDIT_JSP_UPLOAD = "ueditor/jsp/upload";
	public static final String WEB_METHODE = "xjapp_api.action?methode=";
	public static final String SHARE_WEB_METHODE = "xjapp.action?methode=";
//	public static final String URL_PREFIX = WEB_SITE_ROOT_URL + WEB_METHODE;
//	public static final String URL_PREFIX ="http://10.121.32.57:8080/dbcV4_CloudApp/dbc_userinfo.action?methode=";
	public static final String URL_PREFIX ="http://10.121.32.57:8080/dbcV4_CloudApp/xjapp_api.action?methode=";
	public static final String SHARE_URL_PREFIX = WEB_SITE_ROOT_URL + SHARE_WEB_METHODE;
	public static final String URL_PREFIX_WITHOUT_METHODE = WEB_SITE_ROOT_URL
			+ "xjapp_api.action?";
	public static final String URL_STRING_METHODE = "methode";
	public static final String SQ_API_ACTION_UPLOAD = "uploadfile";

	
}

package com.museumguild.utils;

import android.os.Environment;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Log {

	private final static String TAG_PREFIX = "<museum>:";
	private final static String path = Environment.getExternalStorageDirectory()+"/museum/log/";
	private static final int MAX_LOG_LENGHT = 3000;
	//用于格式化日期,作为日志文件名的一部分  
    private static DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
     
	public static void m(String tag, String msg){
		String time = formatter.format(new Date());
		if(msg.length() > MAX_LOG_LENGHT)
		{
			int index = 0;
			int len = msg.length();
			String sub ;
			while (index < len) {
				if(len <= index+ MAX_LOG_LENGHT)
				{
					sub = msg.substring(index);
				}
				else
				{
					sub = msg.substring(index, index+MAX_LOG_LENGHT);
				}
				index += MAX_LOG_LENGHT;
				android.util.Log.i(TAG_PREFIX+tag, sub);
				writeAllLogToFile(time+"--->"+ TAG_PREFIX + tag+"--->"+sub , path ,"museum.txt");
			}
		}
		else
		{
			android.util.Log.i(TAG_PREFIX+tag, msg);
			writeAllLogToFile(time+"--->"+ TAG_PREFIX + tag+"--->"+msg , path ,"museum.txt");
		}
	}
	public static void e(String tag, String msg){
		String time = formatter.format(new Date());
		if(msg.length() > MAX_LOG_LENGHT)
		{
			int index = 0;
			int len = msg.length();
			String sub ;
			while (index < len) {
				if(len <= index+ MAX_LOG_LENGHT)
				{
					sub = msg.substring(index);
				}
				else
				{
					sub = msg.substring(index, index+MAX_LOG_LENGHT);
				}
				index += MAX_LOG_LENGHT;
				android.util.Log.e(TAG_PREFIX+tag, sub);
				writeAllLogToFile(time+"--->"+ TAG_PREFIX + tag+"--->"+sub , path ,"museum.txt");
			}
		}
		else
		{
			android.util.Log.i(TAG_PREFIX+tag, msg);
			writeAllLogToFile(time+"--->"+ TAG_PREFIX + tag+"--->"+msg , path ,"museum.txt");
		}
	}
	
	private static void  writeAllLogToFile(String info, String path, String fileName)
	{
		if(info.length() > MAX_LOG_LENGHT)
		{
			int index = 0;
			int len = info.length();
			String sub ;
			while (index < len) {
				if(len <= index+ MAX_LOG_LENGHT)
				{
					sub = info.substring(index);
				}
				else
				{
					sub = info.substring(index, index+MAX_LOG_LENGHT);
				}
				index += MAX_LOG_LENGHT;
				writeLogToFile( sub , path,  fileName);
			}
		}
		else
		{
			writeLogToFile( info, path,  fileName);
		}
	}
	private static void writeLogToFile(String info, String path, String fileName) {
		if(!isSdCardExist())
		{
			return;
		}
		try {
			File f = new File(path);
			if(f.exists() && f.length() >= 10*1024*1024)
			{//TODO 以后完善,只删一半日志
				f.delete();
			}
			if (!f.exists()) {
				f.mkdirs();
			}
		    //第二个参数意义是说是否以append方式添加内容  
		    BufferedWriter bw = new BufferedWriter(new FileWriter(path+fileName, true));
		    bw.write(info+"\r\n");  
		    bw.flush();  
		    bw.close();
		} catch (Exception e) {
		    e.printStackTrace();  
		}  
	}
	
	/** 
	 * 判断SDCard是否存在 [当没有外挂SD卡时，内置ROM也被识别为存在sd卡] 
	 *  
	 * @return 
	 */  
	public static boolean isSdCardExist() {  
	    return Environment.getExternalStorageState().equals(
	            Environment.MEDIA_MOUNTED);
	}  

}

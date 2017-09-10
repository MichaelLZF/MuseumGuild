package com.museumguild.manage;

import com.museumguild.http.HttpConnService;
import com.museumguild.utils.Constant;
import com.museumguild.utils.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.Semaphore;


/**
 * 
 * 上传工具类
 */
public class UploadFileManager {
	private static UploadFileManager uploadUtil;
	private static final String BOUNDARY = UUID.randomUUID().toString(); // 边界标识
																			// 随机生成
	private static final String PREFIX = "--";
	private static final String LINE_END = "\r\n";
	private static final String CONTENT_TYPE = "multipart/form-data"; // 内容类型

	private UploadFileManager() {

	}

	/**
	 * 单例模式获取上传工具类
	 * 
	 * @return
	 */
	public static UploadFileManager getInstance() {
		if (null == uploadUtil) {
			uploadUtil = new UploadFileManager();
		}
		return uploadUtil;
	}
	private static final String TAG = "UploadFileManager";
	private int readTimeOut = 10 * 1000; // 读取超时
	private int connectTimeout = 10 * 1000; // 超时时间
	/***
	 * 请求使用多长时间
	 */
	private static int requestTime = 0;

	private static final String CHARSET = "utf-8"; // 设置编码

	/***
	 * 上传成功
	 */
	public static final int UPLOAD_SUCCESS_CODE = 1;
	/**
	 * 文件不存在
	 */
	public static final int UPLOAD_FILE_NOT_EXISTS_CODE = 2;
	/**
	 * 服务器出错
	 */
	public static final int UPLOAD_SERVER_ERROR_CODE = 3;
	protected static final int WHAT_TO_UPLOAD = 1;
	protected static final int WHAT_UPLOAD_DONE = 2;

	/**
	 * android上传文件到服务器
	 * 
	 * @param filePath
	 *            需要上传的文件的路径
	 * @param fileKey
	 *            在网页上<input type=file name=xxx/> xxx就是这里的fileKey
	 * @param RequestURL
	 *            请求的URL
	 */
	public void uploadFile(String filePath, String fileKey, String RequestURL,
                           Map<String, String> param) {
		if (filePath == null) {
			sendMessage(UPLOAD_FILE_NOT_EXISTS_CODE, "文件不存在");
			return;
		}
		try {
			File file = new File(filePath);
			toUploadFile(file, fileKey, RequestURL, param);
		} catch (Exception e) {
			sendMessage(UPLOAD_FILE_NOT_EXISTS_CODE, "文件不存在");
			e.printStackTrace();
			return;
		}
	}

	
/*	public String uploadFiles(String fileType, String[] fileKeys, File[] files, Param[] params, ResultCallback callback, Object tag)
	{
		String url = Constant.URL_PREFIX_WITHOUT_METHODE  +Util.assembleUrlSuffix(
				Constant.URL_STRING_METHODE,Constant.SQ_API_ACTION_UPLOAD);
		OkHttpClientManager.getUploadDelegate().postAsyn(url, fileKeys, files, params, callback, tag);
		return url;
	}*/
/**
 * 上传多个文件到服务器
 * @param RequestURL
 * @param params 一些请求格式参数
 * @param files key是文件名 value是文件
 * @return
 */
	public String uploadFiles(final String RequestURL, final Map<String, String> params, final Map<String, File> files ) {
		Log.m(TAG, "uploadFiles 请求的URL=" + RequestURL);
		try {
			String result = HttpConnService.httpPostFile(RequestURL, params,files);
			if(null!=result)
			{
				return URLDecoder.decode(result, "UTF-8");
			}
		} catch (IOException e) {
			Log.e(TAG,
					"HttpConnService.httpPostFile IOException:"
							+ e.toString());
			e.printStackTrace();
		}
		return null;
	}


	private void toUploadFile(File file, String fileKey, String RequestURL,
                              Map<String, String> param) {
		String result = null;
		requestTime = 0;

		long requestTime = System.currentTimeMillis();
		long responseTime = 0;

		try {
			URL url = new URL(RequestURL);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setReadTimeout(readTimeOut);
			conn.setConnectTimeout(connectTimeout);
			conn.setDoInput(true); // 允许输入流
			conn.setDoOutput(true); // 允许输出流
			conn.setUseCaches(false); // 不允许使用缓存
			conn.setRequestMethod("POST"); // 请求方式
			conn.setRequestProperty("Charset", CHARSET); // 设置编码
			conn.setRequestProperty("connection", "keep-alive");
			conn.setRequestProperty("user-agent",
					"Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1)");
			conn.setRequestProperty("Content-Type", CONTENT_TYPE + ";boundary="
					+ BOUNDARY);
			// conn.setRequestProperty("Content-Type",
			// "application/x-www-form-urlencoded");

			/**
			 * 当文件不为空，把文件包装并且上传
			 */
			DataOutputStream dos = new DataOutputStream(conn.getOutputStream());
			StringBuffer sb = null;
			String params = "";

			/***
			 * 以下是用于上传参数
			 */
			if (param != null && param.size() > 0) {
				Iterator<String> it = param.keySet().iterator();
				while (it.hasNext()) {
					sb = null;
					sb = new StringBuffer();
					String key = it.next();
					String value = param.get(key);
					sb.append(PREFIX).append(BOUNDARY).append(LINE_END);
					sb.append("Content-Disposition: form-data; name=\"")
							.append(key).append("\"").append(LINE_END)
							.append(LINE_END);
					sb.append(value).append(LINE_END);
					params = sb.toString();
					Log.m(TAG, key + "=" + params + "##");
					dos.write(params.getBytes());
					// dos.flush();
				}
			}

			sb = null;
			params = null;
			sb = new StringBuffer();
			/**
			 * 这里重点注意： name里面的值为服务器端需要key 只有这个key 才可以得到对应的文件
			 * filename是文件的名字，包含后缀名的 比如:abc.png
			 */
			sb.append(PREFIX).append(BOUNDARY).append(LINE_END);
			sb.append("Content-Disposition:form-data; name=\"" + fileKey
					+ "\"; filename=\"" + file.getName() + "\"" + LINE_END);
			//TODO maybe error
			sb.append("Content-Type:image/jpeg" + LINE_END); // 这里配置的Content-type很重要的
																// ，用于服务器端辨别文件的类型的
			sb.append(LINE_END);
			params = sb.toString();
			sb = null;

			Log.m(TAG, file.getName() + "=" + params + "##");
			dos.write(params.getBytes());
			/** 上传文件 */
			InputStream is = new FileInputStream(file);
			onUploadProcessListener.initUpload((int) file.length());
			byte[] bytes = new byte[1024];
			int len = 0;
			int curLen = 0;
			while ((len = is.read(bytes)) != -1) {
				curLen += len;
				dos.write(bytes, 0, len);
				onUploadProcessListener.onUploadProcess(curLen);
			}
			is.close();

			dos.write(LINE_END.getBytes());
			byte[] end_data = (PREFIX + BOUNDARY + PREFIX + LINE_END)
					.getBytes();
			dos.write(end_data);
			dos.flush();
			//
			// dos.write(tempOutputStream.toByteArray());
			/**
			 * 获取响应码 200=成功 当响应成功，获取响应的流
			 */
			int res = conn.getResponseCode();
			responseTime = System.currentTimeMillis();
			this.requestTime = (int) ((responseTime - requestTime) / 1000);
			Log.e(TAG, "response code:" + res);
			if (res == 200) {
				Log.e(TAG, "request success");
				InputStream input = conn.getInputStream();
				StringBuffer sb1 = new StringBuffer();
				int ss;
				while ((ss = input.read()) != -1) {
					sb1.append((char) ss);
				}
				result = sb1.toString();
				Log.e(TAG, "toUploadFile -> result : " + result);
				sendMessage(UPLOAD_SUCCESS_CODE, result);
				return;
			} else {
				Log.e(TAG, "request error");
				sendMessage(UPLOAD_SERVER_ERROR_CODE, "上传失败：code=" + res);
				return;
			}
		} catch (MalformedURLException e) {
			sendMessage(UPLOAD_SERVER_ERROR_CODE,
					"上传失败：error=" + e.getMessage());
			e.printStackTrace();
			return;
		} catch (IOException e) {
			sendMessage(UPLOAD_SERVER_ERROR_CODE,
					"上传失败：error=" + e.getMessage());
			e.printStackTrace();
			return;
		}
	}

	/**
	 * 发送上传结果
	 * 
	 * @param responseCode
	 * @param responseMessage
	 */
	private void sendMessage(int responseCode, String responseMessage) {
		onUploadProcessListener.onUploadDone(responseCode, responseMessage);
	}

	/**
	 * 下面是一个自定义的回调函数，用到回调上传文件是否完成
	 * 
	 * @author shimingzheng
	 * 
	 */
	public static interface OnUploadProcessListener {
		/**
		 * 上传响应
		 * 
		 * @param responseCode
		 * @param message
		 */
		void onUploadDone(int responseCode, String message);

		/**
		 * 上传中
		 * 
		 * @param uploadSize
		 */
		void onUploadProcess(int uploadSize);

		/**
		 * 准备上传
		 * 
		 * @param fileSize
		 */
		void initUpload(int fileSize);
	}

	private OnUploadProcessListener onUploadProcessListener;

	public void setOnUploadProcessListener(
			OnUploadProcessListener onUploadProcessListener) {
		this.onUploadProcessListener = onUploadProcessListener;
	}

	public int getReadTimeOut() {
		return readTimeOut;
	}

	public void setReadTimeOut(int readTimeOut) {
		this.readTimeOut = readTimeOut;
	}

	public int getConnectTimeout() {
		return connectTimeout;
	}

	public void setConnectTimeout(int connectTimeout) {
		this.connectTimeout = connectTimeout;
	}

	/**
	 * 获取上传使用的时间
	 * 
	 * @return
	 */
	public static int getRequestTime() {
		return requestTime;
	}

	public static interface uploadProcessListener {

	}
	
    /** 
     *  
     * @param params 
     *            传递的普通参数 
     * @param uploadFile 
     *            需要上传的文件名 
     * @param fileFormName 
     *            需要上传文件表单中的名字 
     * @param newFileName 
     *            上传的文件名称，不填写将为uploadFile的名称 
     * @param urlStr 
     *            上传的服务器的路径 
     * @throws IOException
     */  
    public void uploadForm(Map<String, String> params, String fileFormName,
                           File uploadFile, String newFileName, String urlStr)
            throws IOException {
        if (newFileName == null || newFileName.trim().equals("")) {  
            newFileName = uploadFile.getName();  
        }  
  
        StringBuilder sb = new StringBuilder();
        /** 
         * 普通的表单数据 
         */  
        for (String key : params.keySet()) {
            sb.append("--" + BOUNDARY + "\r\n");  
            sb.append("Content-Disposition: form-data; name=\"" + key + "\""  
                    + "\r\n");  
            sb.append("\r\n");  
            sb.append(params.get(key) + "\r\n");  
        }  
        /** 
         * 上传文件的头 
         */  
        sb.append("--" + BOUNDARY + "\r\n");  
        sb.append("Content-Disposition: form-data; name=\"" + fileFormName  
                + "\"; filename=\"" + newFileName + "\"" + "\r\n");  
        sb.append("Content-Type: image/jpeg" + "\r\n");// 如果服务器端有文件类型的校验，必须明确指定ContentType  
        sb.append("\r\n");  
  
        byte[] headerInfo = sb.toString().getBytes("UTF-8");  
        byte[] endInfo = ("\r\n--" + BOUNDARY + "--\r\n").getBytes("UTF-8");  
        System.out.println(sb.toString());
        URL url = new URL(urlStr);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");  
        conn.setRequestProperty("Content-Type",  
                "multipart/form-data; boundary=" + BOUNDARY);  
        conn.setRequestProperty("Content-Length", String
                .valueOf(headerInfo.length + uploadFile.length()  
                        + endInfo.length));  
        conn.setDoOutput(true);  
  
        OutputStream out = conn.getOutputStream();
        InputStream in = new FileInputStream(uploadFile);
        out.write(headerInfo);  
  
        byte[] buf = new byte[1024];  
        int len;  
        while ((len = in.read(buf)) != -1)  
            out.write(buf, 0, len);  
  
        out.write(endInfo);  
        in.close();  
        out.close();  
        if (conn.getResponseCode() == 200) {  
            System.out.println("上传成功 result:");
        }  
    }  
    
    /**
	 * 向服务端上传照片
	 * @param imgList 要上传的图片列表
	 * @return 返回的服务端上的图片地址 列表
	 */
	public List<String> uploadImg(final List<String> imgList) {
		List<String> serverPaths =  new ArrayList<String>();//返回的服务器上的图片地址
		if (null == imgList || imgList.size() ==0) {
			Log.e(TAG, "uploadImg ->imgList == null");
			return serverPaths;
		}
		Semaphore semaphore = new Semaphore(1);
		final String url = Constant.URL_PREFIX_WITHOUT_METHODE+ Constant.URL_STRING_METHODE+"="+ Constant.SQ_API_ACTION_UPLOAD;
		for (int i = 0; i < imgList.size(); i++) {
			final int j = i;
			try {
				// 请求许可
				semaphore.acquire();
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			Map<String, String> params = new HashMap<String, String>();
			Map<String, File> files = new HashMap<String, File>();
			params.put("token", LoginManager.getIns().getToken());
			params.put("fileType", "image");
			File file = new File(imgList.get(j));
			/*Bitmap bitmap =BitmapUtil.getImage(imgList.get(j));
			InputStream in = Bitmap2IS(bitmap);
			File tempFile = new File("/mnt/sdcard/community/"+System.currentTimeMillis()+".jpg");
			if (!tempFile.exists()) {
			    try {//如果文件不存在就创建文件，写入图片
			    	tempFile.createNewFile();
			        FileOutputStream fo = new FileOutputStream(tempFile);
			        int read = in.read();
			        while (read != -1) {
			                fo.write(read);
			                read = in.read();
			        }
			        //关闭流
			        fo.flush();
			        fo.close();
			        in.close();
			    } catch (IOException e) {
			        e.printStackTrace();
			    }
			}*/
			files.put(file.getName(), file);
			Log.m(TAG, "start uploading images index=" + j);
			String result = UploadFileManager.getInstance().uploadFiles(url,
					params, files);
			Log.m(TAG, "start uploading images result size:" + result.length());
			if (null != result) {
				try {
					JSONObject jo = new JSONObject(result);
					if (jo.getInt("code") == 1) {// 服务器返回ok
						String path = jo.getString("filepath");
						serverPaths.add(j, path.substring(path.indexOf(Constant.UPLOAD_IMG_PATH_SUFFIX), path.length()));
					}
				} catch (JSONException e) {
					Log.e(TAG, "uploading images result JSONException :" + e);
					e.printStackTrace();
				}
			}
			/*if(tempFile.exists())
			{
				tempFile.delete();
			}*/
			Log.m(TAG, "end uploading images index=" + j + " .ok ! result :"
					+ result);
			// 释放许可
			semaphore.release();
		};
		return serverPaths;
	}
	
}

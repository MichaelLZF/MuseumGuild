package com.museumguild.http;

import android.text.TextUtils;

import com.museumguild.manage.LoginManager;
import com.museumguild.utils.BitmapUtil;
import com.museumguild.utils.Log;
import com.museumguild.utils.Util;

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Map;

/**
 * http请求的服务类
 * 
 * @author shenqiang
 * 
 */
public class HttpConnService {
	private static String tag = "[HttpConnService]";
	private final static int HTTP_RESPONSE_OK = 200;
	public final static int HTTP_SUCCESS =1;

	/**
	 * 该方法支持多个文件上传,但是66810只支持单个文件上传
	 * @param actionUrl 上传文件的url
	 * @param params 携带的参数,不包括文件
	 * @param files 要上传的文件 File类型
	 * @return 返回上传的结果
	 * @throws IOException
	 */
	public static String httpPostFile(String actionUrl, Map<String, String> params,
                                      Map<String, File> files) throws IOException {
        StringBuilder sb2 = new StringBuilder();
        String BOUNDARY = java.util.UUID.randomUUID().toString();
        String PREFIX = "--", LINEND = "\r\n";
        String MULTIPART_FROM_DATA = "multipart/form-data";
        String CHARSET = "UTF-8";
 
        URL uri = new URL(actionUrl);
        HttpURLConnection conn = (HttpURLConnection) uri.openConnection();
        conn.setReadTimeout(30 * 1000);
        conn.setDoInput(true);
        conn.setDoOutput(true);
        conn.setUseCaches(false);
        conn.setRequestMethod("POST");
        conn.setRequestProperty("connection", "keep-alive");
        conn.setRequestProperty("Charsert", "UTF-8");
        conn.setRequestProperty("Content-Type", MULTIPART_FROM_DATA
                + ";boundary=" + BOUNDARY);
        conn.setRequestProperty("token", LoginManager.getIns().getToken());
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, String> entry : params.entrySet()) {
            sb.append(PREFIX);
            sb.append(BOUNDARY);
            sb.append(LINEND);
            sb.append("Content-Disposition: form-data; name=\""
                    + entry.getKey() + "\"" + LINEND);
            sb.append("Content-Type: image/jpeg; charset=" + CHARSET + LINEND);
            sb.append("Content-Transfer-Encoding: 8bit" + LINEND);
            sb.append(LINEND);
            sb.append(entry.getValue());
            sb.append(LINEND);
        }
 
        DataOutputStream outStream = new DataOutputStream(
                conn.getOutputStream());
        outStream.write(sb.toString().getBytes());
        if (files != null) {
            // int i = 0;
            for (Map.Entry<String, File> file : files.entrySet()) {
                StringBuilder sb1 = new StringBuilder();
                sb1.append(PREFIX);
                sb1.append(BOUNDARY);
                sb1.append(LINEND);
                //name=xxx,这里的名字由服务器定义
                sb1.append("Content-Disposition: form-data; name=\"myFile\"; filename=\""
                        + file.getKey() + "\"" + LINEND);
                sb1.append("Content-Type: application/octet-stream; charset="
                        + CHARSET + LINEND);
                sb1.append(LINEND);
                outStream.write(sb1.toString().getBytes());
 
                //InputStream is = new FileInputStream(file.getValue());
                long time = System.currentTimeMillis();
                InputStream is = Util.Bitmap2IS(BitmapUtil.getImage(file.getValue().getCanonicalPath()));
                //Log.m(tag ,"upload a file use time:"+(System.currentTimeMillis()-time));
                byte[] buffer = new byte[1024];
                int len = 0;
                while ((len = is.read(buffer)) != -1) {
                    outStream.write(buffer, 0, len);
                }
 
                is.close();
                outStream.write(LINEND.getBytes());
            }
        }
 
        byte[] end_data = (PREFIX + BOUNDARY + PREFIX + LINEND).getBytes();
        outStream.write(end_data);
        outStream.flush();
 
        int res = conn.getResponseCode();
        InputStream in = null;
        if (res == 200) {
            in = conn.getInputStream();
            int ch;
 
            while ((ch = in.read()) != -1) {
                sb2.append((char) ch);
            }
        }
        String result = sb2.toString();
        Log.m(tag, "upload file end result:"+result);
        if(!TextUtils.isEmpty(result))
        {
        	result = URLDecoder.decode(result, "utf-8");
        }
        return in == null ? null : result;
    }
		
}

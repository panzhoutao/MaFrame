package com.cydroid.coreframe.web.http.ssl;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.CookieStore;
import java.net.HttpCookie;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.URLEncoder;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

//import javax.net.ssl.HttpURLConnection;

import android.content.Context;

public class CustomHttpsConnection {
	 private static final String DEFAULT_CHARSET = "UTF-8"; // 默认字符集
	 
	    private static final String _GET = "GET"; // GET
	    private static final String _POST = "POST";// POST
	    public static Context context;
	    
	    public static String cookie="";
	/**
     * 初始化http请求参数
	 * @throws NoSuchAlgorithmException 
	 * @throws KeyManagementException 
     */
    private static HttpURLConnection initHttps(String url, String method, Map<String, String> headers)
            throws IOException, KeyManagementException, NoSuchAlgorithmException{
        
        URL _url = new URL(url);
//        HttpURLConnection.setFollowRedirects(
        HttpURLConnection http = (HttpURLConnection) _url.openConnection();
        // 设置域名校验
        
        // 连接超时
        http.setConnectTimeout(10000);
        // 读取超时 --服务器响应比较慢，增大时间
        http.setReadTimeout(10000);
        http.setRequestMethod(method);
        http.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
//        http.setRequestProperty("User-Agent",
//                "Mozilla/5.0 (Windows NT 6.3; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/33.0.1750.146 Safari/537.36");
        if (null != headers && !headers.isEmpty()) {
            for (Entry<String, String> entry : headers.entrySet()) {
                http.setRequestProperty(entry.getKey(), entry.getValue());
            }
        }
//        http.setUseCaches(false);
        http.setRequestProperty("Content-Type",
				"application/x-www-form-urlencoded");
		HttpURLConnection.setFollowRedirects(false);
		http.setRequestProperty("Cookie","Cookie: "+cookie);
        
//        http.setSSLSocketFactory(TwoWaysAuthenticationSSLSocketFactory.getSSLSocketFactory(context));
        http.setDoOutput(true);
        http.setDoInput(true);
        http.connect();
        
        return http;
    }
    /**
     * get请求
     */
    public static String get(String url, Map<String, String> params, Map<String, String> headers) {
        StringBuffer bufferRes = null;
        try {
            HttpURLConnection http = null;
            
            http = initHttps(initParams(url, params), _GET, headers);
            
            InputStream in = http.getInputStream();
            BufferedReader read = new BufferedReader(new InputStreamReader(in, DEFAULT_CHARSET));
            String valueString = null;
            int statusCode = http.getResponseCode(); 
//			if (statusCode == 403) {
//				HttpJsonTool.state403 = true;
//				HttpJsonTool.httpjsontool = null;
//			}
            bufferRes = new StringBuffer();
            while ((valueString = read.readLine()) != null) {
                bufferRes.append(valueString);
            }
            in.close();
            if (http != null) {
                http.disconnect();// 关闭连接
            }
            return bufferRes.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }
 
    /**
     * get请求
     */
    public static String get(String url) {
        return get(url, null);
    }
 
    /**
     * get请求
     */
    public static String get(String url, Map<String, String> params) {
        return get(url, params, null);
    }
 
    /**
     * post请求
     */
    public static String post(String url, String params, Map<String, String> headers) {
        StringBuffer bufferRes = null;
        try {
            HttpURLConnection http = null;
            http = initHttps(url, _POST, headers);
            
            OutputStream out = http.getOutputStream();
            if(params!=null){
            	out.write(params.getBytes(DEFAULT_CHARSET));
            }
            out.flush();
            out.close();
 
            InputStream in = http.getInputStream();
            BufferedReader read = new BufferedReader(new InputStreamReader(in, DEFAULT_CHARSET));
            String valueString = null;
            
            
            int statusCode = http.getResponseCode(); 
//			if (statusCode == 403) {
//				HttpJsonTool.state403 = true;
//				HttpJsonTool.httpjsontool = null;
//			}
            bufferRes = new StringBuffer();
            while ((valueString = read.readLine()) != null) {
                bufferRes.append(valueString);
            }
            in.close();
            if (http != null) {
                http.disconnect();// 关闭连接
            }
            return bufferRes.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }
 
    /**
     * post请求
     */
    public static String post(String url, Map<String, String> params) {
        return post(url, map2Url(params), null);
    }
 
    /**
     * post请求
     */
    public static String post(String url, Map<String, String> params, Map<String, String> headers) {
        return post(url, map2Url(params), headers);
    }
 
    /**
     * 初始化参数
     */
    public static String initParams(String url, Map<String, String> params) {
        if (null == params || params.isEmpty()) {
            return url;
        }
        StringBuilder sb = new StringBuilder(url);
        if (url.indexOf("?") == -1) {
            sb.append("?");
        }
        sb.append(map2Url(params));
        return sb.toString();
    }
 
    /**
     * map转url参数
     */
    public static String map2Url(Map<String, String> paramToMap) {
        if (null == paramToMap || paramToMap.isEmpty()) {
            return null;
        }
        StringBuffer url = new StringBuffer();
        boolean isfist = true;
        for (Entry<String, String> entry : paramToMap.entrySet()) {
            if (isfist) {
                isfist = false;
            } else {
                url.append("&");
            }
            url.append(entry.getKey()).append("=");
            String value = entry.getValue();
            if (value!=null&&value.length()>0) {
                try {
                    url.append(URLEncoder.encode(value, DEFAULT_CHARSET));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        }
        return url.toString();
    }
    
    
    private static CookieManager manager = new CookieManager();
    public static void storecoo(URI uri,String strcoo) {  
        
        // 创建一个默认的 CookieManager  
          
  
        // 将规则改掉，接受所有的 Cookie  
        manager.setCookiePolicy(CookiePolicy.ACCEPT_ALL);  
  
        // 保存这个定制的 CookieManager  
        CookieHandler.setDefault(manager);  
  
        // 接受 HTTP 请求的时候，得到和保存新的 Cookie  
        HttpCookie cookie = new HttpCookie("Cookie: ", strcoo);  
        //cookie.setMaxAge(60000);//没这个也行。  
        manager.getCookieStore().add(uri, cookie);  
    }  
      
    public static HttpCookie getcookies(){  
          
        HttpCookie res = null;  
        // 使用 Cookie 的时候：  
        // 取出 CookieStore  
        CookieStore store = manager.getCookieStore();  
  
        // 得到所有的 URI  
        List<URI> uris = store.getURIs();  
        for (URI ur : uris) {  
            // 筛选需要的 URI  
            // 得到属于这个 URI 的所有 Cookie  
            List<HttpCookie> cookies = store.get(ur);  
            for (HttpCookie coo : cookies) {  
                res = coo;  
            }  
        }  
        return res;  
    } 
    
    
    
    /**
	 * 上传图片
	 * @param urlStr
	 * @param textMap
	 * @param fileMap
	 * @return
	 */
	public static String formUpload(String urlStr, Map<String, String> textMap, Map<String, String> fileMap) {
		String res = "";
		HttpURLConnection conn = null;
		String BOUNDARY = "---------------------------123821742118716"; //boundary就是request头和上传文件内容的分隔符  
		try {
			URL url = new URL(urlStr);
			conn = (HttpURLConnection) url.openConnection();
			conn.setConnectTimeout(10000);
			conn.setReadTimeout(30000);
//			conn.setSSLSocketFactory(TwoWaysAuthenticationSSLSocketFactory.getSSLSocketFactory(context));
			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setUseCaches(false);
			HttpURLConnection.setFollowRedirects(false);
			conn.setRequestProperty("Cookie","Cookie: "+cookie);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Connection", "Keep-Alive");
//			conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows; U; Windows NT 6.1; zh-CN; rv:1.9.2.6)");
			conn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + BOUNDARY);

			OutputStream out = new DataOutputStream(conn.getOutputStream());
			// text  
			if (textMap != null&&textMap.size()>0) {
				StringBuffer strBuf = new StringBuffer();
				Iterator<Entry<String, String>> iter = textMap.entrySet().iterator();
				while (iter.hasNext()) {
					Entry<String, String> entry = iter.next();
					String inputName = (String) entry.getKey();
					String inputValue = (String) entry.getValue();
					if (inputValue == null) {
						continue;
					}
					strBuf.append("\r\n").append("--").append(BOUNDARY).append("\r\n");
					strBuf.append("Content-Disposition: form-data; name=\"" + inputName + "\"\r\n\r\n");
					strBuf.append(inputValue);
				}
				out.write(strBuf.toString().getBytes());
			}

			// file  
			if (fileMap != null&&fileMap.size()>0) {
				Iterator<Entry<String, String>> iter = fileMap.entrySet().iterator();
				while (iter.hasNext()) {
					Entry<String, String> entry = iter.next();
					String inputName = (String) entry.getKey();
					String inputValue = (String) entry.getValue();
					if (inputValue == null) {
						continue;
					}
					File file = new File(inputValue);
					String filename = file.getName();
//					MagicMatch match = Magic.getMagicMatch(file, false, true);
//					String contentType = match.getMimeType();

					StringBuffer strBuf = new StringBuffer();
					strBuf.append("\r\n").append("--").append(BOUNDARY).append("\r\n");
					strBuf.append("Content-Disposition: form-data; name=\"" + inputName + "\"; filename=\"" + filename + "\"\r\n");
					strBuf.append("Content-Type:"  + "\r\n\r\n");

					out.write(strBuf.toString().getBytes());

					DataInputStream in = new DataInputStream(new FileInputStream(file));
					int bytes = 0;
					byte[] bufferOut = new byte[1024];
					while ((bytes = in.read(bufferOut)) != -1) {
						out.write(bufferOut, 0, bytes);
					}
					in.close();
				}
			}

			byte[] endData = ("\r\n--" + BOUNDARY + "--\r\n").getBytes();
			out.write(endData);
			out.flush();
			out.close();

			// 读取返回数据  
			StringBuffer strBuf = new StringBuffer();
			BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String line = null;
			while ((line = reader.readLine()) != null) {
				strBuf.append(line).append("\n");
			}
			res = strBuf.toString();
			reader.close();
			reader = null;
		} catch (Exception e) {
			System.out.println("发送POST请求出错。" + urlStr);
			e.printStackTrace();
		} finally {
			if (conn != null) {
				conn.disconnect();
				conn = null;
			}
		}
		return res;
	}
}

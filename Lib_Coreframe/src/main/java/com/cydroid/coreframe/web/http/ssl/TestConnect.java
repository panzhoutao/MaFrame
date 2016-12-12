package com.cydroid.coreframe.web.http.ssl;



import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

import org.apache.http.conn.ssl.SSLSocketFactory;

public class TestConnect {

	/**
	 * String Base url
	 * */
//	private static final String baseurl = "http://rexian.beijing.gov.cn/index.jsp?agMode=1";

	private static final String Dataencoding = "UTF-8";
	/**
	 * int timeout 超时时间 默认 6000ms
	 * */
	public int timeout = 1800000;
	/**
	 * boolean doinput 是否向服务器端发送数据 默认为 true
	 * */
	public boolean doinput = true;
	/**
	 * boolean dooutput 是否接收服务器端发送的数据 默认为 true
	 * */
	public boolean dooutput = true;

	/**
	 * The HttpURLConnection to connect the website.
	 * */
	public HttpsURLConnection hc = null;

	/**
	 * sendCoding String 发送请求的编码方式。
	 * */
	public String sendCoding = "UTF-8";

	/**
	 * Parsecode String 本地解析时的编码方式。
	 * */
	public static final String Parsecode = "GBK";

	/**
	 * 
	 * */
	public String cookie = null;

	public TestConnect(String ec, String cookie) {
		this.sendCoding = ec;
		this.cookie = cookie;
	}

	public TestConnect() {

	}

	/**
	 * @throws Exception
	 * 
	 * */
	public void initCon(String str) throws Exception {
		URL url = null;
		if (str != null && !str.equals("")) {
			url = new URL(str);
		} else
			url = new URL(HttpsInitApplication.ServerUrl+"/1.json");
		
		HttpURLConnection.setFollowRedirects(true);
		hc = (HttpsURLConnection) url.openConnection();
		hc.setRequestMethod("POST");
//		hc.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/33.0.1750.154 Safari/537.36");
		hc.setDoOutput(true);
		hc.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
		hc.setRequestProperty("Content-Language", "zh-cn");
		
		hc.setRequestProperty("Connection", "keep-alive");
		hc.setRequestProperty("Cache-Control", "no-cache");
//		javax.net.ssl.SSLSocketFactory factory=TwoWaysAuthenticationSSLSocketFactory.getSSLSocketFactory(CustomHttpsConnection.context);
//		hc.setSSLSocketFactory(factory);
		// hc.setRequestProperty("Cookie", cookie); // 注入cookie （String cookie）
	}

	/**
	 * @param postdata
	 *            String 要发送的数据。
	 * @throws Exception
	 * */
	public void sendPost(String postdata) throws Exception {
		// String send = URLEncoder.encode(postdata, MHttpConnect.Dataencoding);
		OutputStream os = hc.getOutputStream();
		OutputStreamWriter osw = new OutputStreamWriter(os, this.sendCoding);
		osw.write(postdata);
		osw.flush();
		osw.close();
		os.close();
	}

	/**
	 * 读取数据
	 * 
	 * @return String 读取的内容。
	 * */
	public String readData() throws IOException {
		int code = hc.getResponseCode();
		StringBuffer sb = null;
		//getthefiled2(code);
		if (code == HttpURLConnection.HTTP_OK) {
			sb = new StringBuffer();
			InputStream is = hc.getInputStream();// 获取输入流
			InputStreamReader isr = new InputStreamReader(is,
					TestConnect.Parsecode);// 包装流并且指定编码方式。
			BufferedReader br = new BufferedReader(isr);
			//getthefiled(code);
			//getthefiled2(code);
			
			String line = null;
			do {
				line = br.readLine();// 读取内容
				if (line != null && !line.equals("")) {
					sb.append(line);
				}
			} while (line != null);
			// 关闭流
			br.close();
			isr.close();
			is.close();
			//System.out.println(sb.toString());
			return sb.toString();
		} else
			return null;
	}

	private void getthefiled2(int code) {

		String keys[] = { "Content-Language", "Date", "Transfer-Encoding",
				"Expires", "Keep-Alive", "Via", "Set-Cookie", "Connection",
				"Content-Type", "Server", "Cache-Control" ,"Pragma"};
		if (code == HttpURLConnection.HTTP_OK) {
			Map<String, List<String>> maps = hc.getHeaderFields();
			for (String ky : keys) {
				List<String> values = maps.get(ky);
				System.out.print(ky + ":");
				if (values != null)
					for (String str : values) {
						System.out.println(str);
					}
				else
					System.out.print("value is null");
				System.out.println();
			}
		}
	}

	public String getcookie() {
		String cookieskey = "Set-Cookie";
		Map<String, List<String>> maps = hc.getHeaderFields();
		List<String> coolist = maps.get(cookieskey);
		Iterator<String> it = coolist.iterator();
		StringBuffer sbu = new StringBuffer();
		sbu.append("eos_style_cookie=default; ");
		while(it.hasNext()){
			sbu.append(it.next()+" ");
		}
		System.out.println(sbu.toString());
		return sbu.toString();
	}

	/**
	 * 关闭连接
	 * */
	public void killconnet() {
		hc.disconnect();

	}

/*	public void getthefiled(int code) {
		if (code == HttpURLConnection.HTTP_OK) {
			Map<String, List<String>> maps = hc.getHeaderFields();
			Collection<List<String>> coll = maps.values();
			Iterator<List<String>> it = coll.iterator();
			System.out.println("values:");
			while (it.hasNext()) {
				List<String> ls = it.next();
				if (ls != null)
					for (int i = 0; i < ls.size(); i++) {
						String str = ls.get(i);
						System.out.println(str);
					}
			}

			System.out.println("keys:");
			Set<String> keys = maps.keySet();
			Iterator<String> keyit = keys.iterator();
			while (keyit.hasNext()) {
				System.out.println(keyit.next());
			}

		}
	}*/

	public String test() {
		String res = null;
		try {
			
			this.initCon("");
			this.sendPost("");
			res = this.getcookie();
			res = res.trim();
			this.readData();
			
			
			// this.killconnet();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return res;
	}

	
	
	public static void main(String str[]) {
		TestConnect tc = new TestConnect();
		tc.test();
		
	}

}

package com.cydroid.coreframe.web.http.ssl;

import java.net.HttpCookie;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;

import android.app.Application;

public class HttpsInitApplication extends Application{
	public static final String ServerUrl="https://eaccess.syrailway.cn:8443/mapping/sjznbg/carLocation/sftp/sytlj/jyzb/1/";
	
//	public static final String ServerUrl = "http://slwly.xzh-soft.com:10001/carLocation/sftp/sytlj/jyzb/1/";
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
//		HttpsClient.getInstance().init(getApplicationContext());
		CustomHttpsConnection.context=getApplicationContext();
		getCookieInfo();
	}
	
	
	public void getCookieInfo() {
		Thread th = new Thread() {
			public void run() {
//				HttpClient httpClient = HttpsClient.getInstance()
//						.getHttpsClient();
//				HttpGet httpRequest = new HttpGet(ServerUrl + "/1.jsp");
				TestConnect tc = new TestConnect();
				URI uri = null;
				try {
					uri = new URI(ServerUrl + "/1.jsp");
				} catch (URISyntaxException e) {
					
					e.printStackTrace();
				}
				HttpURLConnection.setFollowRedirects(true);  
				CustomHttpsConnection.storecoo(uri, tc.test());
				HttpCookie hcoo = CustomHttpsConnection.getcookies();
				if(!hcoo.hasExpired()){
					CustomHttpsConnection.cookie = hcoo.getValue();
				}
//				try {
//					httpClient.execute(httpRequest);
//				} catch (ClientProtocolException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				} catch (IOException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//				cookieInfo = ((AbstractHttpClient) httpClient).getCookieStore();
			}
		};
		th.start();
	}
}

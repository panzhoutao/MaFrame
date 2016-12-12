package com.cydroid.coreframe.web.http;

import android.content.Context;

import org.apache.http.client.HttpClient;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.params.HttpParams;

import java.io.InputStream;
import java.security.KeyStore;

import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;

public class HttpsClient {
//	private static final String HTTPS_URL = "https://10.10.1.45/1.jsp";
	private static final String KEY_STORE_TYPE_BKS = "BKS";
	private static final String KEY_STORE_TYPE_P12 = "PKCS12";
	private static final String SCHEME_HTTPS = "https";
	private static final int HTTPS_PORT = 8443;
	//private static final String HTTPS_URL = "https://192.168.137.117:8443/1.jsp";
	private static final String KEY_STORE_CLIENT_PATH = "peer.pfx";
//	private static final String KEY_STORE_CLIENT_PATH = "client.p12";
	private static final String KEY_STORE_TRUST_PATH = "client.bks";
	private static final String KEY_STORE_PASSWORD = "11111111";
	private static final String KEY_STORE_TRUST_PASSWORD = "11111111";
//	private static final String KEY_STORE_PASSWORD = "c_sjy108";
//	private static final String KEY_STORE_TRUST_PASSWORD = "sjy108";

	private KeyStore keyStore;
	private KeyStore trustStore;
	private static HttpsClient mHttpsClient=null;
	public static HttpsClient getInstance(){
		if(mHttpsClient==null)
			mHttpsClient=new HttpsClient();
		return mHttpsClient;
	}
	public void init(final Context context){
		try {
			keyStore = KeyStore.getInstance(KEY_STORE_TYPE_P12);
			trustStore = KeyStore.getInstance(KEY_STORE_TYPE_BKS);
			InputStream ksIn = context.getAssets().open(KEY_STORE_CLIENT_PATH);
			InputStream tsIn = context.getAssets().open(KEY_STORE_TRUST_PATH);
			keyStore.load(ksIn, KEY_STORE_PASSWORD.toCharArray());
			trustStore.load(tsIn, KEY_STORE_TRUST_PASSWORD.toCharArray());
			try {
				ksIn.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				tsIn.close();
			} catch (Exception ignore) {
			}
		} catch (Exception ignore) {
		}
	}
	public HttpClient getHttpsClient(){
		HttpClient httpClient = new DefaultHttpClient();
		try {
			SSLSocketFactory socketFactory = new SSLSocketFactory(keyStore, KEY_STORE_PASSWORD, trustStore);
			Scheme sch = new Scheme(SCHEME_HTTPS, socketFactory, HTTPS_PORT);
			httpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 20000);//����ʱ��20s
			httpClient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 30000);
			httpClient.getConnectionManager().getSchemeRegistry().register(sch);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return httpClient;
	}
	public HttpClient getHttpsClient(ClientConnectionManager manager,HttpParams httpParams){
		HttpClient httpClient = new DefaultHttpClient(manager,httpParams);
		try {
			SSLSocketFactory socketFactory = new SSLSocketFactory(keyStore, KEY_STORE_PASSWORD, trustStore);
			Scheme sch = new Scheme(SCHEME_HTTPS, socketFactory, HTTPS_PORT);
			//httpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 20000);//����ʱ��20s
			//httpClient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 30000);
			httpClient.getConnectionManager().getSchemeRegistry().register(sch);

		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return httpClient;
	}


	public javax.net.ssl.SSLSocketFactory getSSLSocketFactory(){
		String keystorepw = "sjznbg123";
		javax.net.ssl.SSLSocketFactory sslSocketFactory=null;
		try {
			KeyManagerFactory keymanagerfactory = KeyManagerFactory
					.getInstance("X509");
			keymanagerfactory.init(keyStore, keystorepw.toCharArray());
			KeyManager akeymanager[] = keymanagerfactory.getKeyManagers();
			TrustManagerFactory trustmanagerfactory = TrustManagerFactory
					.getInstance("X509");
			trustmanagerfactory.init(trustStore);
			TrustManager atrustmanager[] = trustmanagerfactory
					.getTrustManagers();
			SSLContext sslcontext = SSLContext.getInstance("TLS");
			sslcontext.init(akeymanager, atrustmanager, null);
			sslSocketFactory = sslcontext.getSocketFactory();
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return sslSocketFactory;
	}
}
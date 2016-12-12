package com.cydroid.coreframe.web.http;

import android.content.Context;
import android.support.annotation.Nullable;

import com.cydroid.coreframe.model.base.BaseHttpAsyncModel;
import com.cydroid.coreframe.model.base.BaseResponesBean;
import com.cydroid.coreframe.tool.util.LogUtil;
import com.cydroid.coreframe.tool.util.StatusUtil;

import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.params.HttpClientParams;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.conn.params.ConnPerRouteBean;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.AbstractHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public class HttpJsonInterface {
    private String TAG = HttpJsonInterface.class.getName();
    // public static String ServerUrl = "http://10.10.2.1:8080/hwky";

    private static HttpJsonInterface httpjsontool = null;
    public static final int CONNECTION_TIMEOUT = 20000;
    public static final int SO_TIMEOUT = 20000;
    public static final int DEFAULT_HOST_CONNECTIONS = 5;
    public static final int DEFAULT_MAX_CONNECTIONS = 10;
    public static synchronized HttpJsonInterface getInstance() {
        if (httpjsontool == null) {
            httpjsontool = new HttpJsonInterface();
        }
        return httpjsontool;
    }

    private static CookieStore cookieInfo = null;
    private HttpClient httpClient;
    private HttpClient getHttpClientG() {
        if(httpClient==null) {
            httpClient = HttpsClient.getInstance()
                    .getHttpsClient();
            httpClient.getParams().setParameter(
                    CoreConnectionPNames.CONNECTION_TIMEOUT, CONNECTION_TIMEOUT);
            httpClient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT,
                    SO_TIMEOUT);
            HttpConnectionParams.setTcpNoDelay(httpClient.getParams(), true);
            if (cookieInfo != null) {
                ((AbstractHttpClient) httpClient).setCookieStore(cookieInfo);
            }
        }
        return httpClient;
    }
    private synchronized HttpClient getHttpClient() {
        if(httpClient == null) {
            final HttpParams httpParams = new BasicHttpParams();

            // timeout: get connections from connection pool
            ConnManagerParams.setTimeout(httpParams, 1000);
            // timeout: connect to the server
            HttpConnectionParams.setConnectionTimeout(httpParams, CONNECTION_TIMEOUT);
            // timeout: transfer data from server
            HttpConnectionParams.setSoTimeout(httpParams, SO_TIMEOUT);

            // set max connections per host
            ConnManagerParams.setMaxConnectionsPerRoute(httpParams, new ConnPerRouteBean(DEFAULT_HOST_CONNECTIONS));
            // set max total connections
            ConnManagerParams.setMaxTotalConnections(httpParams, DEFAULT_MAX_CONNECTIONS);

            // use expect-continue handshake
            HttpProtocolParams.setUseExpectContinue(httpParams, true);
            // disable stale check
            HttpConnectionParams.setStaleCheckingEnabled(httpParams, false);

            HttpProtocolParams.setVersion(httpParams, HttpVersion.HTTP_1_1);
            HttpProtocolParams.setContentCharset(httpParams, HTTP.UTF_8);

            HttpClientParams.setRedirecting(httpParams, false);

            // set user agent
            String userAgent = "Android client";
            HttpProtocolParams.setUserAgent(httpParams, userAgent);

            // disable Nagle algorithm
            HttpConnectionParams.setTcpNoDelay(httpParams, true);

            //HttpConnectionParams.setSocketBufferSize(httpParams, DEFAULT_SOCKET_BUFFER_SIZE);

            // scheme: http and https
            SchemeRegistry schemeRegistry = new SchemeRegistry();
            schemeRegistry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
            schemeRegistry.register(new Scheme("https", SSLSocketFactory.getSocketFactory(), 443));

            ClientConnectionManager manager = new ThreadSafeClientConnManager(httpParams, schemeRegistry);
            httpClient = HttpsClient.getInstance().getHttpsClient(manager, httpParams);

        }

        return httpClient;
    }
    /**
     * 获得cookie并保存
     */
    public void getCookieInfo() {
        Thread th = new Thread() {
            public void run() {
                HttpClient httpClient = HttpsClient.getInstance()
                        .getHttpsClient();
                HttpGet httpRequest = new HttpGet(BaseHttpAsyncModel.ServerUrl
                        + "/1.jsp");
                try {
                    httpClient.execute(httpRequest);
                } catch (ClientProtocolException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                cookieInfo = ((AbstractHttpClient) httpClient).getCookieStore();
                if (httpRequest != null) {
                    httpRequest.abort();
                }
                httpClient.getConnectionManager().shutdown();
            }
        };
        th.start();
    }

    public synchronized BaseResponesBean Upload(Context context,String url,Map<String,File> files
            ,Map<String,String>entity,@Nullable ProgressMultipartEntity.ProgressListener listener) {
        BaseResponesBean bean = new BaseResponesBean();
        HttpClient client = getHttpClientG();
        HttpPost httpRequest = new HttpPost(url);
        try {
            client.getParams().setParameter(
                    CoreProtocolPNames.HTTP_CONTENT_CHARSET, Charset.forName("UTF-8"));
            StringBuilder builder = new StringBuilder();
            ProgressMultipartEntity
                    mpEntity = new ProgressMultipartEntity(
                    HttpMultipartMode.BROWSER_COMPATIBLE, null, Charset.forName("UTF-8"),
                    listener);
            Iterator<String> filekKeys=files.keySet().iterator();

//            httpRequest.setHeader("Content-Type", "application/octet-stream");
            while (filekKeys.hasNext()) {
                String key=filekKeys.next();
                File content=files.get(key);
                if(content==null||content.length()==0){
                    continue;
                }
                ContentBody cbFile = new FileBody(content, "", "UTF-8");
                mpEntity.addPart(key, cbFile);
            }
            if(null!=entity){
                Iterator<String> contentKeys=entity.keySet().iterator();

                while (contentKeys.hasNext()) {
                    String key=contentKeys.next();
                    String content=entity.get(key);
                    if(content==null||content.length()==0){
                        continue;
                    }
                    StringBody cbFile = new StringBody(content,Charset.forName("UTF-8"));
                    mpEntity.addPart(key, cbFile);
                }
            }


            httpRequest.setEntity(mpEntity);
            HttpResponse response = client.execute(httpRequest);
            int statusCode = response.getStatusLine().getStatusCode();
            bean.setStatus(statusCode);
            if (statusCode != 200) {
                String strStatus = StatusUtil.parseCode(bean.getStatus());
                bean.setInfo(strStatus);
                return bean;
            }
            BufferedReader reader = new
                    BufferedReader(new InputStreamReader(
                    response.getEntity().getContent()));
            for (String s = reader.readLine(); s
                    != null; s = reader.readLine()) {
                builder.append(s);
            }
            LogUtil.i(TAG,builder.toString());
            JSONObject jsonObject = new
                    JSONObject(builder.toString());
            bean.setJson(jsonObject);
            bean.setJsonString(builder.toString());
        } catch (ConnectTimeoutException e) {
            e.printStackTrace();
            bean.setStatus(StatusUtil.ConnectTimeoutException);
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            bean.setStatus(StatusUtil.UnsupportedEncodingException);
        } catch (ClientProtocolException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            bean.setStatus(StatusUtil.ClientProtocolException);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            bean.setStatus(StatusUtil.IOException);
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            bean.setStatus(StatusUtil.JSONException);
            e.printStackTrace();
        } finally {
            if (httpRequest != null) {
                httpRequest.abort();
            }
//            client.getConnectionManager().shutdown();
        }
        String strStatus = StatusUtil.parseCode(bean.getStatus());
        bean.setInfo(strStatus);
        return bean;
    }

    public synchronized BaseResponesBean doPost(String url,
                                                List<NameValuePair> params) {
        BaseResponesBean bean = new BaseResponesBean();
        HttpClient client = getHttpClientG();
        HttpPost httpRequest = null;
        try {
            StringBuilder builder = new StringBuilder();
            httpRequest = new HttpPost(url);
//            httpRequest.setHeader("Accept-Encoding", "gzip, deflate");
            httpRequest.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
            httpRequest.setHeader("Content-Type", "application/x-www-form-urlencoded; charset=utf-8");
            HttpResponse response = client.execute(httpRequest);
            int statusCode = response.getStatusLine().getStatusCode();
            bean.setStatus(statusCode);
            if (statusCode != 200) {
                String strStatus = StatusUtil.parseCode(bean.getStatus());
                bean.setInfo(strStatus);
                return bean;
            }
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    response.getEntity().getContent()));
            // EntityUtils.toString(new
            // GzipDecompressingEntity(responseEntity));
            for (String s = reader.readLine(); s != null; s = reader.readLine()) {
                builder.append(s);
            }
            String jsonString=builder.toString();
//            jsonString=jsonString.replaceAll("null","\u3000");
            JSONObject jsonObject = new JSONObject(jsonString);

            LogUtil.i(TAG, statusCode + "/" + builder.toString());
            bean.setJson(jsonObject);
            bean.setJsonString(jsonString);
//            bean.setStatus(StatusUtil.SUCCESS);
        } catch (ConnectTimeoutException e) {
            e.printStackTrace();
            bean.setStatus(StatusUtil.ConnectTimeoutException);
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            bean.setStatus(StatusUtil.UnsupportedEncodingException);
        } catch (ClientProtocolException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            bean.setStatus(StatusUtil.ClientProtocolException);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            bean.setStatus(StatusUtil.IOException);
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            bean.setStatus(StatusUtil.JSONException);
            e.printStackTrace();
        } finally {
            if (httpRequest != null) {
                httpRequest.abort();
            }
//            client.getConnectionManager().shutdown();
        }
        LogUtil.i("bean.getStatus()："+bean.getStatus());
        String strStatus = StatusUtil.parseCode(bean.getStatus());
        bean.setInfo(strStatus);
        return bean;
    }

    public synchronized BaseResponesBean doGet(String url) {
        BaseResponesBean bean = new BaseResponesBean();
        HttpClient client = getHttpClientG();
        HttpGet httpRequest = null;
        try {
            StringBuilder builder = new StringBuilder();

            httpRequest = new HttpGet(url);
            httpRequest.setHeader("Content-Type", "application/x-www-form-urlencoded; charset=utf-8");
            HttpResponse response = client.execute(httpRequest);
            int statusCode = response.getStatusLine().getStatusCode();
            bean.setStatus(statusCode);
            if (statusCode != 200) {
                String strStatus = StatusUtil.parseCode(bean.getStatus());
                bean.setInfo(strStatus);
                return bean;
            }
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    response.getEntity().getContent()));
            for (String s = reader.readLine(); s != null; s = reader.readLine()) {
                builder.append(s);
            }
            String jsonString=builder.toString();
//            jsonString=jsonString.replaceAll("null","0");
//            jsonString=jsonString.replaceAll("null","\u3000");
            JSONObject jsonObject = new JSONObject(jsonString);
            LogUtil.i(TAG, statusCode + "/" + builder.toString());
            bean.setJson(jsonObject);
//            bean.setStatus(StatusUtil.SUCCESS);
            bean.setJsonString(jsonString);
        } catch (ConnectTimeoutException e) {
            e.printStackTrace();
            bean.setStatus(StatusUtil.ConnectTimeoutException);
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            bean.setStatus(StatusUtil.UnsupportedEncodingException);
        } catch (ClientProtocolException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            bean.setStatus(StatusUtil.ClientProtocolException);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            bean.setStatus(StatusUtil.IOException);
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            bean.setStatus(StatusUtil.JSONException);
            e.printStackTrace();
        } finally {
            if (httpRequest != null) {
                httpRequest.abort();
            }
//            client.getConnectionManager().shutdown();
        }
        String strStatus = StatusUtil.parseCode(bean.getStatus());
        bean.setInfo(strStatus);
        return bean;
    }
    public synchronized BaseResponesBean doGetJsonArray(String url) {
        BaseResponesBean bean = new BaseResponesBean();
        HttpClient client = getHttpClientG();
        HttpGet httpRequest = null;
        try {
            StringBuilder builder = new StringBuilder();

            httpRequest = new HttpGet(url);
            HttpResponse response = client.execute(httpRequest);
            int statusCode = response.getStatusLine().getStatusCode();
            bean.setStatus(statusCode);
            if (statusCode != 200) {
                String strStatus = StatusUtil.parseCode(bean.getStatus());
                bean.setInfo(strStatus);
                return bean;
            }
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    response.getEntity().getContent()));
            for (String s = reader.readLine(); s != null; s = reader.readLine()) {
                builder.append(s);
            }
            JSONArray jsonArray = new JSONArray(builder.toString());
            JSONObject jsonObject=new JSONObject();
            jsonObject.put("list",jsonArray);
            LogUtil.i(TAG, statusCode + "/" + builder.toString());
            bean.setJson(jsonObject);
//            bean.setStatus(StatusUtil.SUCCESS);

        } catch (ConnectTimeoutException e) {
            e.printStackTrace();
            bean.setStatus(StatusUtil.ConnectTimeoutException);
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            bean.setStatus(StatusUtil.UnsupportedEncodingException);
        } catch (ClientProtocolException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            bean.setStatus(StatusUtil.ClientProtocolException);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            bean.setStatus(StatusUtil.IOException);
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            bean.setStatus(StatusUtil.JSONException);
            e.printStackTrace();
        } finally {
            if (httpRequest != null) {
                httpRequest.abort();
            }
//            client.getConnectionManager().shutdown();
        }
        String strStatus = StatusUtil.parseCode(bean.getStatus());
        bean.setInfo(strStatus);
        return bean;
    }
}

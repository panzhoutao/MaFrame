package com.cydroid.coreframe.web.http.download;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;

import com.cydroid.coreframe.web.http.HttpsClient;
import com.cydroid.coreframe.web.http.X509TrustManageTool;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.params.CoreProtocolPNames;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.charset.Charset;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;


public class DownloadAsyncTaskProgressDialog extends AsyncTask<String, Integer, Boolean> {

	private final ProgressDialog bar;
	private int count = 0;
	private File downloadFile = null;
	private DownloadCallbackListener listener;

	private boolean awaysDownload=false;
	public DownloadAsyncTaskProgressDialog(ProgressDialog bar) {
		super();
		this.bar = bar;
		if(bar!=null) {
			bar.show();
			bar.setProgress(count);
		}
	}

	public boolean isAwaysDownload() {
		return awaysDownload;
	}

	public void setAwaysDownload(boolean awaysDownload) {
		this.awaysDownload = awaysDownload;
	}

	static TrustManager[] xtmArray = new X509TrustManageTool[] { new X509TrustManageTool() };


	static HostnameVerifier DO_NOT_VERIFY = new HostnameVerifier() {
		@Override
		public boolean verify(String hostname, SSLSession session) {
			return true;
		}
	};

	@Override
	protected Boolean doInBackground(String... params) {
		try {
			//
			downloadFile = new File(params[1]);
//			if(downloadFile.exists()&&!awaysDownload){
//				return true;
//			}
			HttpClient client = HttpsClient.getInstance().getHttpsClient();
			client.getParams().setParameter(
					CoreProtocolPNames.HTTP_CONTENT_CHARSET,
					Charset.forName("UTF-8"));
			HttpPost httpRequest = new HttpPost(params[0]);
			Log.i("httpdownload",  params[0]);
			HttpResponse response;
			response = client.execute(httpRequest);
			HttpEntity entity = response.getEntity();

			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {

				Header headers[] =response.getHeaders("Content-Length");
				if(headers.length>0) {
					if(bar!=null)
					bar.setMax(Integer.parseInt(headers[0].getValue()));
				}
//				bar.setMax(response.getEntity().getContentLength());
				downloadFile.createNewFile();
				InputStream inputStream = entity.getContent();
				FileOutputStream fileOutputStream = new FileOutputStream(
						downloadFile);
				byte[] buffer = new byte[1024 * 1024];
				try{
				while (true) {
					int len = inputStream.read(buffer);
					publishProgress(len);
					if (len == -1) {
						break;
					}
					fileOutputStream.write(buffer, 0, len);
				}
				}catch(OutOfMemoryError e){
					
				}
				inputStream.close();
				fileOutputStream.close();
			}

		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
		}
		return true;
	}

	@Override
	protected void onCancelled() {
		// TODO Auto-generated method stub
		super.onCancelled();
		if (downloadFile != null && downloadFile.exists()) {
			downloadFile.delete();
		}
		if(bar!=null)
		bar.dismiss();
	}

	public void setdownloadCallbackListener(DownloadCallbackListener listener) {
		this.listener = listener;
	}

	@Override
	protected void onPostExecute(Boolean result) {
		super.onPostExecute(result);
		if(bar!=null)
		bar.dismiss();
		if (downloadFile != null && downloadFile.exists()) {
			if(downloadFile.length()==0){
				downloadFile.delete();
			}
		}
		if(listener!=null){
			listener.onComplete(downloadFile);
		}
	}
	
	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		super.onPreExecute();
	}

	@Override
	protected void onProgressUpdate(Integer... values) {
		count += values[0];
		if(bar!=null)
		bar.setProgress(count);
		super.onProgressUpdate(values);
	}

}

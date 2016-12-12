package com.cydroid.coreframe.web.http.download;

import java.io.File;

public interface DownloadCallbackListener {
	public void onComplete(File downloadFile);
}

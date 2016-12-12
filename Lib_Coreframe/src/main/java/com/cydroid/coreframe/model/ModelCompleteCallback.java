package com.cydroid.coreframe.model;

public interface ModelCompleteCallback<T> {
	void onTaskPostExecute(int taskid,T result);
}

package com.cydroid.coreframe.model.base;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.cydroid.coreframe.R;
import com.cydroid.coreframe.model.Model;
import com.cydroid.coreframe.model.ModelCompleteCallback;
import com.cydroid.coreframe.tool.util.ActivityTool;
import com.cydroid.coreframe.tool.util.NetUtil;
import com.cydroid.coreframe.tool.util.StatusUtil;
import com.cydroid.coreframe.tool.util.ToastUtil;

public abstract class BaseHttpAsyncModel<T> extends AsyncTask<String, Void, BaseResponesBean> implements Model<T> {
	protected Context context;
	protected int TaskId;
//	public static String ServerUrl = "http://10.10.2.230:8078/HJMeeting/";
//	public static String ServerUrl = "http://223.100.5.189:8078/HJMeeting/";
//	public static String ServerUrl = "https://eaccess.harb-railway.com.cn:8443/mapping/ddml/HJMeeting/";
//	public static String ServerUrl = "http://10.10.2.67:9091/";
//	public static String ServerUrl = "http://10.10.2.231:9091/";

//	public static String IP="10.10.2.231";
//	public static String IP="223.100.5.189";
//	public static String IP="10.10.2.231";
//	public static String IP="sdwly.xzh-soft.com";
	public static String ServerUrl = "http://syz.xzh-soft.com:8083/";
//	public static String IMGServerUrl = "http://"+IP+":7072/";
//	public static String IMGServerUrl2 = "http://"+IP+":7073/";
//	public static final String updsateServerUrl = "http://"+IP+":7073/app/detail/";
	protected T holder;


	protected ModelCompleteCallback<BaseResponesBean> callback;
	public BaseHttpAsyncModel(Context context,ModelCompleteCallback< BaseResponesBean> callback){
		this.context=context;
		this.callback=callback;
	}
	public BaseHttpAsyncModel(Context context){
		this.context=context;
	}
	public void setCompleteCallBack(ModelCompleteCallback<BaseResponesBean> callback){
		this.callback=callback;
	}

	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		super.onPreExecute();
		if(NetUtil.getNetworkState(context)==NetUtil.NETWORN_NONE){
			ToastUtil.showShort(context, R.string.internet_not_connect);
			cancel(true);
		}
	}

	@Override
	protected void onPostExecute(BaseResponesBean result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
		if(result.getStatus()==403){
			ActivityTool.gotoLoginView(context);
			return;
		}
		if(result.getStatus()!= StatusUtil.STATUS_OK&&result.getStatus()!= StatusUtil.INTERFACE_OK){
			ToastUtil.show(context,result.getInfo(), Toast.LENGTH_LONG);
		}
		if(callback!=null){
			callback.onTaskPostExecute(TaskId, result);
		}
	}

	@Override
	protected void onCancelled() {
		super.onCancelled();
		if(callback!=null){
			callback.onTaskPostExecute(TaskId, null);
		}
	}

	@Override
	public void close(boolean mayInterrupt) {
		this.cancel(mayInterrupt);
	}

	@Override
	public void excuteParams(String[] kxrq){
        execute(kxrq);
	}
	@Override
	public void excuteParams(T holder) {
		this.holder=holder;
		executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
//		execute();
	}
}

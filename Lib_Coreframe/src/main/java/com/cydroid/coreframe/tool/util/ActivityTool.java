package com.cydroid.coreframe.tool.util;



import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.widget.Toast;


public class ActivityTool {
	public static void gotoLoginView(Context context) {
		try {
//			Intent intent = new Intent(context,
//					LoginActivity.class);
//			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK;)
			ToastUtil.show(context, "您的账号已在其他设备上登录，请重新登录",
					Toast.LENGTH_LONG);
//			context.startActivity(intent);
		} catch (Exception e) {
		}
	}

	public static boolean getInsert(Context context,String packageName){
		PackageInfo packageInfo;

		try {
			packageInfo = context.getPackageManager().getPackageInfo(
					packageName, 0);
		} catch (PackageManager.NameNotFoundException e) {
			packageInfo = null;
			e.printStackTrace();
		}
		if(packageInfo ==null){
			return false;
		}else{
			return true;
		}
	}
}

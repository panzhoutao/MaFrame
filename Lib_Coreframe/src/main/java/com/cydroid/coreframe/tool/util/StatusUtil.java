package com.cydroid.coreframe.tool.util;



public class StatusUtil {
	public static final int STATUS_OK = 200;
	public static final int INTERFACE_OK = 0;
	public static final int INTERFACE_ERROR = -1;
	public static final int CODE403 = 403;
	public static final int CODE404 = 404;
	
	public static final int NETWORKERROR = 10003;
	public static final int JSONException = 10004;
	public static final int IOException = 10005;
	public static final int ClientProtocolException = 10006;
	public static final int UnsupportedEncodingException = 10007;
	public static final int ConnectTimeoutException = 10008;

	public static String parseCode(int code){
		String str_status="";
		switch (code) {
			case INTERFACE_ERROR:
				str_status="发送失败，服务升级中";
				break;
		case NETWORKERROR:
			str_status="网络连接不稳定";
			break;
		case CODE403:
			str_status="您的账号已经在其他设备上登录，请重新登录。";
			break;
		case STATUS_OK:
			str_status="成功";
			break;
		case JSONException:
			str_status="系统数据升级中";
			break;
		case IOException:
			str_status="网络忙，请稍后";
			break;
		case ClientProtocolException:
			str_status="请升级客户端";
			break;
		case UnsupportedEncodingException:
			str_status="客户端版本过旧";
			break;
		case ConnectTimeoutException:
			str_status="网络连接超时";
			break;
		case CODE404:
			str_status="无更多数据";
			break;
		default:
			str_status="读取数据失败，请检查网络";
			break;
		}
		return str_status;
	}
}

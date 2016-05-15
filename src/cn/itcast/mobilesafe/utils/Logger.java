package cn.itcast.mobilesafe.utils;


import android.util.Log;

public class Logger {

	private final static int LEVEL = 7;//  0 测试级别    7 上线级别  
	
	private final static int VERBOSE = Log.VERBOSE;//2
	private final static int DEBUG = Log.DEBUG;//3
	private final static int INFO = Log.INFO;//4
	private final static int WARN = Log.WARN;//5
	private final static int ERROR = Log.ERROR;//6
	
	public static void v(String tag,String msg){
		if(LEVEL < VERBOSE){
			Log.v(tag, msg);
		}
		
	}
	
	public static void d(String tag,String msg){
		if(LEVEL < DEBUG){
			Logger.i(tag, msg);
		}
		
	}
	
	public static void i(String tag,String msg){
		if(LEVEL < INFO){
			Logger.i(tag, msg);
		}

	}
	public static void w(String tag,String msg){
		if(LEVEL < WARN){
			Logger.i(tag, msg);
		}
	}
	public static void e(String tag,String msg){
		if(LEVEL < ERROR){
			Logger.i(tag, msg);
		}
	}
	
}

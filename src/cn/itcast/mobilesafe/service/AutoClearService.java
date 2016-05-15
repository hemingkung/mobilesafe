package cn.itcast.mobilesafe.service;

import cn.itcast.mobilesafe.utils.TaskUtil;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.IBinder;

public class AutoClearService extends Service {

	private SharedPreferences sp;
	
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		
		sp = getSharedPreferences("config", Context.MODE_PRIVATE);
		
		setForeground(true);//设置为前台进程
		IntentFilter filter = new IntentFilter();
		filter.addAction(Intent.ACTION_SCREEN_OFF);
		LockScreenReceiver receiver = new LockScreenReceiver();
		registerReceiver(receiver, filter);
	}
	
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
	
	private final class LockScreenReceiver extends BroadcastReceiver{

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			//清理内存的操作
			boolean isautoclear = sp.getBoolean("isautoclear", false);
			if(isautoclear){
				TaskUtil.killAllProcess(context);
			}
		}
		
	}

}

package cn.itcast.mobilesafe.service;

import java.util.ArrayList;
import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.KeyguardManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.SystemClock;
import cn.itcast.mobilesafe.MobileSafeApplication;
import cn.itcast.mobilesafe.activity.EnterPasswordActivity;
import cn.itcast.mobilesafe.dao.AppLockDao;
import cn.itcast.mobilesafe.inter.IService;
import cn.itcast.mobilesafe.utils.Logger;

public class AppLockService extends Service {
	
	private ActivityManager am;
	private AppLockDao appLockDao;
	private List<String> appLocks;
	private List<String> tempAppLocks;//放置临时解锁的应用
	
	private MyContentObserver observer;
	
	//手机锁屏和解锁知道的服务
	private KeyguardManager keyguardManager;
	
	private LockScreenReceiver receiver;
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		
		am = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
		keyguardManager = (KeyguardManager) getSystemService(Context.KEYGUARD_SERVICE);
		appLockDao = new AppLockDao(this);
		appLocks = appLockDao.findAll();
		tempAppLocks = new ArrayList<String>();
		
		//注册一个内容观察者
		observer = new MyContentObserver(new Handler());
		Uri uri = Uri.parse("content://applock/applock");
		getContentResolver().registerContentObserver(uri, true, observer);
		
		//注册锁屏广播接收者
		IntentFilter filter = new IntentFilter();
		filter.addAction(Intent.ACTION_SCREEN_OFF);
		receiver = new LockScreenReceiver();
		registerReceiver(receiver, filter);
		
		new Thread(){
			public void run() {
				//类似一个看门狗
				while(true){
					
					boolean isLockScreen = keyguardManager.inKeyguardRestrictedInputMode();
					if(isLockScreen){
						SystemClock.sleep(500);
						Logger.i("i", " 现在处于锁屏模式。。。");
					}else{
						Logger.i("i", " 现在处于监听模式。。。");
						//得到当前正在运行的任务栈
						List<RunningTaskInfo>  runningTaskInfos = am.getRunningTasks(1);
						//得到前台显示的任务栈
						RunningTaskInfo runningTaskInfo = runningTaskInfos.get(0);
						ComponentName component = runningTaskInfo.topActivity;
						String packageName = component.getPackageName();
						//判断当前的应用程序是否在程序锁里面
						if(appLocks.contains(packageName)){
							
							if(!tempAppLocks.contains(packageName)){
								MobileSafeApplication application = (MobileSafeApplication) getApplication();
								application.setPackname(packageName);
								//弹出输入密码的界面
								Intent intent = new Intent(getApplicationContext(),EnterPasswordActivity.class);
								//服务也是不存在于任务栈
								intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
								startActivity(intent);
							}
						}
						SystemClock.sleep(500);
					}
				}
			};
		}.start();
	}

	private final class MyContentObserver extends ContentObserver{

		public MyContentObserver(Handler handler) {
			super(handler);
			// TODO Auto-generated constructor stub
		}
		
		@Override
		public void onChange(boolean selfChange) {
			// TODO Auto-generated method stub
			super.onChange(selfChange);
			appLocks = appLockDao.findAll();
			tempAppLocks.clear();
		}
	}
	
	
	private final class LockScreenReceiver extends BroadcastReceiver{

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			tempAppLocks.clear();
		}
		
	}
	
	private MyBinder ibinder = new MyBinder();
	
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return ibinder;
	}

	private final class MyBinder extends Binder implements IService{

		public void addTemp(String packageName) {
			// TODO Auto-generated method stub
			addTempAppLock(packageName);
		}
		
	}
	
	private void addTempAppLock(String packageName){
		tempAppLocks.add(packageName);
	}
	
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		
		//取消监听
		getContentResolver().unregisterContentObserver(observer);
		
		//取消锁屏的广播监听
		unregisterReceiver(receiver);
	}
}

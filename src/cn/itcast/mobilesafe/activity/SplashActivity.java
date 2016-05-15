package cn.itcast.mobilesafe.activity;

import java.io.File;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.view.animation.AlphaAnimation;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import cn.itcast.mobilesafe.R;
import cn.itcast.mobilesafe.domain.UpdateInfo;
import cn.itcast.mobilesafe.engine.AddressQueryService;
import cn.itcast.mobilesafe.engine.UpdateInfoService;
import cn.itcast.mobilesafe.utils.DownloadManager;
import cn.itcast.mobilesafe.utils.Logger;

public class SplashActivity extends Activity {
	
	private static final int ERROR_GET_VERSION = 0;
	public static final int ERROR_GET_UPDATEINFO = 1;
	public static final int SHOW_UPDATE_DIALOG = 2;
	public static final int NOT_SHOW_UPDATE_DIALOG = 3;
	protected static final int ERROR_DOWNLOAD_APK = 4;
	public static final int SUCCESS_DOWNLOAD_APK = 5;
	public static final int SDCARD_NOT_EXIST = 6;
	public static final int ERROR_DOWNLOAD_DB = 7;
	public static final int SUCCESS_DOWNLOAD_DB = 8;
	private TextView tv_version;
	private PackageManager pm;//包管理器（系统提供好的服务，他能过获取的数据是：Manifest.xml文件里面  manifest节点里面信息）
	private String version;
	private UpdateInfoService updateInfoService;
	private UpdateInfo updateInfo;
	private ProgressDialog mPd;
	private RelativeLayout rl_splash;
	private long startTime;
	private SharedPreferences sp;
	private AddressQueryService addressQueryService;
	
	private Handler mHandler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case ERROR_GET_VERSION:
				Toast.makeText(getApplicationContext(), "获取应用程序的版本号失败", 1).show();
				break;
			case ERROR_GET_UPDATEINFO:
				Toast.makeText(getApplicationContext(), "获取服务器上的最新版本信息失败", 1).show();
				loginMainUI();
				break;
			case SHOW_UPDATE_DIALOG:
				showUpdateDialog();
				break;
			case NOT_SHOW_UPDATE_DIALOG:
				loginMainUI();
				break;
			case ERROR_DOWNLOAD_APK:
				mPd.dismiss();
				Toast.makeText(getApplicationContext(), "下载最新apk失败", 1).show();
				loginMainUI();
				break;
			case SUCCESS_DOWNLOAD_APK:
				mPd.dismiss();
				//安装应用程序
				installApk();
				break;
			case SDCARD_NOT_EXIST:
				mPd.dismiss();
				Toast.makeText(getApplicationContext(), "sdcard 不存在", 1).show();
				loginMainUI();
				break;
			case ERROR_DOWNLOAD_DB:
				mPd.dismiss();
				Toast.makeText(getApplicationContext(), "归属地数据库下载失败", 1).show();
				loginMainUI();
				break;
			case SUCCESS_DOWNLOAD_DB:
				mPd.dismiss();
				//判断应用程序是否是最新版本
				new Thread(new CheckVersionTask()).start();
			    break;
			default:
				break;
			}
		};
	};
	
	private void installApk(){
		Intent intent = new Intent();
		intent.setAction(Intent.ACTION_VIEW);
		String name = DownloadManager.getFileName(updateInfo.getUrl());
		File file = new File(Environment.getExternalStorageDirectory(),name);
		intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
		startActivity(intent);
	}
	
	//显示更新的提示对话框
	private void showUpdateDialog(){
		/**
		 * 1 创建Builder
		 * 2 给builder 设置属性  :标题 提示信息  图标  按钮
		 * 3 创建Dialog
		 * 4 显示dialog
		 */
		
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("请升级");
		builder.setMessage(updateInfo.getDescription());
		builder.setCancelable(false);//屏蔽后退按钮
		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				    mPd = new ProgressDialog(SplashActivity.this);
				    mPd.setMessage("正在下载最新的apk");
				    mPd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
				    mPd.show();
					new Thread(new DownloadApkTask()).start();
			}
		});
		builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
			
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				loginMainUI();
			}
		});
		AlertDialog dialog  = builder.create();
		dialog.show();
	}
	
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		//setTheme(android.R.style.Theme_Light_NoTitleBar_Fullscreen); 通过代码设置主题必须要在onCreate()之前
		super.onCreate(savedInstanceState);
		//requestWindowFeature(Window.FEATURE_NO_TITLE);//去除标题
		setContentView(R.layout.splash);
		//getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);//去除状态栏
		rl_splash = (RelativeLayout) findViewById(R.id.rl_splash);
		tv_version = (TextView) findViewById(R.id.tv_version);
		pm = getPackageManager();
		updateInfoService = new UpdateInfoService();
		
		version = getVersion();
		tv_version.setText("版本号:" + version);
		
		//执行一个透明度改变动画
		AlphaAnimation animation = new AlphaAnimation(0.0f, 1.0f);
		animation.setDuration(2000);
		rl_splash.startAnimation(animation);
		startTime = System.currentTimeMillis();
		
		sp = getSharedPreferences("config", Context.MODE_PRIVATE);
		
		addressQueryService = new AddressQueryService();
		
		//判断归属地数据库是否存在
		boolean isExist = addressQueryService.isExist();
		if(isExist){
			//判断应用程序是否是最新版本
			new Thread(new CheckVersionTask()).start();
		}else{
			mPd = new ProgressDialog(this);
			mPd.setTitle("正在加载归属地数据库");
			mPd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
			mPd.show();
			//下载归属地数据库
			new Thread(new DownloadDBTask()).start();
		}
		

	}
	
	//下载归属地数据库的任务
	private final class DownloadDBTask implements Runnable{

		public void run() {
			// TODO Auto-generated method stub
			try {
				boolean result = DownloadManager.download(getString(R.string.address_url), mPd);
				if(result){
					Message msg = new Message();
					msg.what = SUCCESS_DOWNLOAD_DB;
					mHandler.sendMessage(msg);
				}else{
					Message msg = new Message();
					msg.what = ERROR_DOWNLOAD_DB;
					mHandler.sendMessage(msg);
				}
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Message msg = new Message();
				msg.what = ERROR_DOWNLOAD_DB;
				mHandler.sendMessage(msg);
			}
		}
		
	}
	
	
	//下载apk的任务
	private final class DownloadApkTask implements Runnable{

		public void run() {
			// TODO Auto-generated method stub
			//判断sdcard是否存在
			if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
				try {
					boolean result = DownloadManager.download(updateInfo.getUrl(),mPd);
					if(result){
						Message msg = new Message();
						msg.what = SUCCESS_DOWNLOAD_APK;
						mHandler.sendMessage(msg);
					}else{
						Message msg = new Message();
						msg.what = ERROR_DOWNLOAD_APK;
						mHandler.sendMessage(msg);
					}
				} catch (Exception e) {
					// TODO: handle exception
					Message msg = new Message();
					msg.what = ERROR_DOWNLOAD_APK;
					mHandler.sendMessage(msg);
				}
			}else{
				Message msg = new Message();
				msg.what = SDCARD_NOT_EXIST;
				mHandler.sendMessage(msg);
			}
		}
		
	}
	
	//检查版本的任务
	private final class CheckVersionTask implements Runnable{

		public void run() {
			// TODO Auto-generated method stub
			long endTime = System.currentTimeMillis();
			long sleepTime = endTime - startTime;
			if(sleepTime < 2000){
				SystemClock.sleep(2000 - sleepTime);
			}
			boolean isautoupdate = sp.getBoolean("isautoupdate", true);
			if(isautoupdate){
				try {
					updateInfo = updateInfoService.getUpdateInfo(getString(R.string.updateinfo_url));
					if(updateInfo == null){
						Logger.i("i", "获取最新版本信息失败");
						Message msg = new Message();
						msg.what = ERROR_GET_UPDATEINFO;
						mHandler.sendMessage(msg);
					}else{
						Logger.i("i", updateInfo.toString());
						if(version.equals(updateInfo.getVersion())){
							//不用显示更新的对话框
							Message msg = new Message();
							msg.what = NOT_SHOW_UPDATE_DIALOG;
							mHandler.sendMessage(msg);
						}else{
							//显示更新的对话框
							Message msg = new Message();
							msg.what = SHOW_UPDATE_DIALOG;
							mHandler.sendMessage(msg);
						}
						
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					Logger.i("i", "获取最新版本信息失败");
					Message msg = new Message();
					msg.what = ERROR_GET_UPDATEINFO;
					mHandler.sendMessage(msg);
				}
			}else{
				loginMainUI();
			}

		}
		
	}
	
	/**
	 * 获取应用程序的版本号
	 * @return
	 */
	private String getVersion(){
		try {
			PackageInfo packageInfo = pm.getPackageInfo(getPackageName(), 0);
			return packageInfo.versionName;
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Message msg = new Message();
			msg.what = ERROR_GET_VERSION;
			mHandler.sendMessage(msg);
		}
		return null;
	}
	
	
	private void loginMainUI(){
		Intent intent = new Intent(this,MainActivity.class);
		startActivity(intent);
		finish();
	}
}

package cn.itcast.mobilesafe.activity;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.IBinder;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import cn.itcast.mobilesafe.MobileSafeApplication;
import cn.itcast.mobilesafe.R;
import cn.itcast.mobilesafe.inter.IService;
import cn.itcast.mobilesafe.service.AppLockService;
import cn.itcast.mobilesafe.utils.MD5;

public class EnterPasswordActivity extends Activity {

	private ImageView iv_appicon;
	private TextView tv_appname;
	private EditText et_pwd;
	private SharedPreferences sp;
	private PackageManager pm;
	private MyServiceConnection conn;
	private IService ibinder;
	private String packageName;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.enter_password);
		
		iv_appicon = (ImageView) findViewById(R.id.iv_appicon);
		tv_appname = (TextView) findViewById(R.id.tv_appname);
		et_pwd = (EditText) findViewById(R.id.et_pwd);
		sp = getSharedPreferences("config", Context.MODE_PRIVATE);
		pm = getPackageManager();
		MobileSafeApplication application = (MobileSafeApplication) getApplication();
		packageName = application.getPackname();
		try {
			ApplicationInfo applicationInfo = pm.getApplicationInfo(packageName, 0);
			Drawable appicon = applicationInfo.loadIcon(pm);
			String appname = applicationInfo.loadLabel(pm).toString();
			iv_appicon.setImageDrawable(appicon);
			tv_appname.setText(appname);
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		conn = new MyServiceConnection();
		Intent service = new Intent(this,AppLockService.class);
		bindService(service, conn, BIND_AUTO_CREATE);
	}
	
	private final class MyServiceConnection implements ServiceConnection{

		public void onServiceConnected(ComponentName name, IBinder service) {
			// TODO Auto-generated method stub
			ibinder = (IService) service;
		}

		public void onServiceDisconnected(ComponentName name) {
			// TODO Auto-generated method stub
			conn = null;
		}
		
	}
	
	public void ok(View v){
		String pwd = et_pwd.getText().toString();
		String md5_pwd = MD5.getData(pwd);
		String old_pwd = sp.getString("pwd", "");
		if("".equals(pwd)){
			Toast.makeText(getApplicationContext(), "密码不能为空", 1).show();
		}else if(old_pwd.equals(md5_pwd)){
			//把当前的activity关闭，并且程序锁服务不对当前的解锁的应用进行处理
			ibinder.addTemp(packageName);
			finish();
		}else{
			Toast.makeText(getApplicationContext(), "密码输入不正确", 1).show();
		}
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		//解绑服务
		unbindService(conn);
	}
}

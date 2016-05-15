package cn.itcast.mobilesafe.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import cn.itcast.mobilesafe.R;
import cn.itcast.mobilesafe.receiver.MyAdmin;

public class Setup4ConfigActivity extends Activity implements OnClickListener{

	private SharedPreferences sp;
	private CheckBox cb_setup4_start_protect;
	private DevicePolicyManager devicePolicyManager;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.setup4config);
		cb_setup4_start_protect = (CheckBox) findViewById(R.id.cb_setup4_start_protect);
		sp = getSharedPreferences("config", Context.MODE_PRIVATE);
		
		cb_setup4_start_protect.setOnClickListener(this);
		
		boolean isprotected = sp.getBoolean("isprotected", false);
		if(isprotected){
			cb_setup4_start_protect.setChecked(true);
			cb_setup4_start_protect.setText("防盗保护已经开启");
		}else{
			cb_setup4_start_protect.setChecked(false);
			cb_setup4_start_protect.setText("防盗保护没有开启");
		}
		
		//申请系统管理员的权限
    	devicePolicyManager = (DevicePolicyManager) getSystemService(Context.DEVICE_POLICY_SERVICE);
	}
	
	public void pre(View v){
		Intent intent = new Intent(this,Setup3ConfigActivity.class);
		startActivity(intent);
		finish();
		overridePendingTransition(R.anim.tran_enter, R.anim.tran_exit);
	}
	
	public void next(View v){
		//判断防盗保护是否已经开启
		boolean isprotected = sp.getBoolean("isprotected", false);
		if(isprotected){
			Editor editor = sp.edit();
			editor.putBoolean("issetup", true);//设置向导完成
			editor.commit();
			finish();
		}else{
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle("强烈建议");
			builder.setMessage("手机防盗极大的保护了手机的安全，请勾选开启防盗");
			builder.setPositiveButton("开启", new DialogInterface.OnClickListener() {
				
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					cb_setup4_start_protect.setChecked(true);
					cb_setup4_start_protect.setText("防盗保护已经开启");
					Editor editor = sp.edit();
					editor.putBoolean("isprotected", true);
					editor.putBoolean("issetup", true);//设置向导完成
					editor.commit();
					finish();
					
					activeAdmin();
				}
			});
			builder.setNegativeButton("放弃", new DialogInterface.OnClickListener() {
				
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					Editor editor = sp.edit();
					editor.putBoolean("issetup", true);//设置向导完成
					editor.commit();
					finish();
				}
			});
			AlertDialog dialog = builder.create();
			dialog.show();
		}
	}

	public void onClick(View v) {
		// TODO Auto-generated method stub
		int id = v.getId();
		switch (id) {
		case R.id.cb_setup4_start_protect:
			boolean isprotected = sp.getBoolean("isprotected", false);
			if(isprotected){
				cb_setup4_start_protect.setChecked(false);
				cb_setup4_start_protect.setText("防盗保护没有开启");
				Editor editor = sp.edit();
				editor.putBoolean("isprotected", false);
				editor.commit();
			}else{
				cb_setup4_start_protect.setChecked(true);
				cb_setup4_start_protect.setText("防盗保护已经开启");
				Editor editor = sp.edit();
				editor.putBoolean("isprotected", true);
				editor.commit();
				

		    	activeAdmin();
			}
			break;

		default:
			break;
		}
	}

	//申请系统管理员的权限
	private void activeAdmin() {
		//申请权限
		ComponentName componentName = new ComponentName(this, MyAdmin.class);
		//判断该组件是否有系统管理员的权限
		boolean isAdminActive = devicePolicyManager.isAdminActive(componentName);
		if(!isAdminActive){
			Intent intent = new Intent();
			//指定动作
			intent.setAction(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
			//指定给那个组件授权
			intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, componentName);
			startActivity(intent);
		}
	}
}

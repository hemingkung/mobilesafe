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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import cn.itcast.mobilesafe.R;
import cn.itcast.mobilesafe.receiver.MyAdmin;

public class LostProtectedSettingActivity extends Activity implements OnClickListener{

	private static final int MENU_CHANGE_NAME_ID = 0;
	private TextView tv_lost_protected_setting_safe_number;
	private CheckBox cb_lost_protected_setting_protecting;
	private TextView tv_lost_protected_setting_reset;
	private SharedPreferences sp;
	private DevicePolicyManager devicePolicyManager;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		//申请系统管理员的权限
    	devicePolicyManager = (DevicePolicyManager) getSystemService(Context.DEVICE_POLICY_SERVICE);
    	
		sp = getSharedPreferences("config", Context.MODE_PRIVATE);
		setContentView(R.layout.lost_protected_setting);
		
		tv_lost_protected_setting_safe_number = (TextView) findViewById(R.id.tv_lost_protected_setting_safe_number);
		String safe_number = sp.getString("safe_number", "");
		tv_lost_protected_setting_safe_number.setText(safe_number);
		
		cb_lost_protected_setting_protecting = (CheckBox) findViewById(R.id.cb_lost_protected_setting_protecting);
		boolean isprotected = sp.getBoolean("isprotected", false);
		if(isprotected){
			cb_lost_protected_setting_protecting.setChecked(true);
			cb_lost_protected_setting_protecting.setText("防盗保护已经开启");
		}else{
			cb_lost_protected_setting_protecting.setChecked(false);
			cb_lost_protected_setting_protecting.setText("防盗保护没有开启");
		}
		cb_lost_protected_setting_protecting.setOnClickListener(this);
		
		tv_lost_protected_setting_reset = (TextView) findViewById(R.id.tv_lost_protected_setting_reset);
		tv_lost_protected_setting_reset.setOnClickListener(this);
	}

	public void onClick(View v) {
		// TODO Auto-generated method stub
		int id = v.getId();
		switch (id) {
		case R.id.tv_lost_protected_setting_reset:
			Intent intent = new Intent(this,Setup1ConfigActivity.class);
			startActivity(intent);
			finish();
			break;
		case R.id.cb_lost_protected_setting_protecting:
			boolean isprotected = sp.getBoolean("isprotected", false);
			if(isprotected){
				cb_lost_protected_setting_protecting.setChecked(false);
				cb_lost_protected_setting_protecting.setText("防盗保护没有开启");
				Editor editor = sp.edit();
				editor.putBoolean("isprotected", false);
				editor.commit();
			}else{
				cb_lost_protected_setting_protecting.setChecked(true);
				cb_lost_protected_setting_protecting.setText("防盗保护已经开启");
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
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		menu.add(0, MENU_CHANGE_NAME_ID, 0, "修改名称");
		return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		int id = item.getItemId();
		switch (id) {
		case MENU_CHANGE_NAME_ID:
			View view = View.inflate(this, R.layout.change_name, null);
			final EditText et_change_name = (EditText) view.findViewById(R.id.et_change_name);
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle("修改手机防盗的名称");
			builder.setView(view);
			builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
				
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					String name = et_change_name.getText().toString();
					if("".equals(name)){
						Toast.makeText(getApplicationContext(), "名字不能为空", 1).show();
					}else{
						Editor editor = sp.edit();
						editor.putString("name", name);
						editor.commit();
					}
				}
			});
			builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
				
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					
				}
			});
			AlertDialog dialog = builder.create();
			dialog.show();
			break;

		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}
}

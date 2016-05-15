package cn.itcast.mobilesafe.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.TextView;
import cn.itcast.mobilesafe.R;
import cn.itcast.mobilesafe.service.AppLockService;
import cn.itcast.mobilesafe.service.BlackNumberService;
import cn.itcast.mobilesafe.service.ShowAddressService;

public class SettingCenterActivity extends Activity implements OnClickListener{

	private CheckBox cb_setting_ceneter_auto_update;
	private CheckBox cb_setting_ceneter_auto_ipcall;
	private CheckBox cb_setting_ceneter_address_query;
	private TextView tv_setting_center_change_show_style;
	private TextView tv_setting_center_change_location;
	private CheckBox cb_setting_ceneter_blacknumber_call;
	private CheckBox cb_setting_ceneter_app_lock;
	private SharedPreferences sp;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.setting_center);
		
		sp = getSharedPreferences("config", Context.MODE_PRIVATE);
		cb_setting_ceneter_auto_update = (CheckBox) findViewById(R.id.cb_setting_ceneter_auto_update);
		boolean isautoupdate = sp.getBoolean("isautoupdate", true);
		if(isautoupdate){
			cb_setting_ceneter_auto_update.setChecked(true);
			cb_setting_ceneter_auto_update.setText("自动更新已开启");
		}else{
			cb_setting_ceneter_auto_update.setChecked(false);
			cb_setting_ceneter_auto_update.setText("自动更新已关闭");
		}
		cb_setting_ceneter_auto_update.setOnClickListener(this);
		
		cb_setting_ceneter_auto_ipcall = (CheckBox) findViewById(R.id.cb_setting_ceneter_auto_ipcall);
		cb_setting_ceneter_auto_ipcall.setOnClickListener(this);
		boolean isautoipcall = sp.getBoolean("isautoipcall", false);
		if(isautoipcall){
			cb_setting_ceneter_auto_ipcall.setChecked(true);
			cb_setting_ceneter_auto_ipcall.setText("ip拨号已开启");
		}else{
			cb_setting_ceneter_auto_ipcall.setChecked(false);
			cb_setting_ceneter_auto_ipcall.setText("ip拨号已关闭");
		}
		
		
		cb_setting_ceneter_address_query = (CheckBox) findViewById(R.id.cb_setting_ceneter_address_query);
		cb_setting_ceneter_address_query.setOnClickListener(this);
		boolean isshowaddress = sp.getBoolean("isshowaddress", false);
		if(isshowaddress){
			cb_setting_ceneter_address_query.setChecked(true);
			cb_setting_ceneter_address_query.setText("归属地查询已开启");
			Intent intent = new Intent(this,ShowAddressService.class);
			startService(intent);
		}else{
			cb_setting_ceneter_address_query.setChecked(false);
			cb_setting_ceneter_address_query.setText("归属地查询已关闭");
			Intent intent = new Intent(this,ShowAddressService.class);
			stopService(intent);
		}
		
		
		tv_setting_center_change_show_style = (TextView) findViewById(R.id.tv_setting_center_change_show_style);
		tv_setting_center_change_show_style.setOnClickListener(this);
		
		tv_setting_center_change_location = (TextView) findViewById(R.id.tv_setting_center_change_location);
		tv_setting_center_change_location.setOnClickListener(this);
		
		
		cb_setting_ceneter_blacknumber_call = (CheckBox) findViewById(R.id.cb_setting_ceneter_blacknumber_call);
		cb_setting_ceneter_blacknumber_call.setOnClickListener(this);
		boolean isblacknumber = sp.getBoolean("isblacknumber", false); 
		if(isblacknumber){
			cb_setting_ceneter_blacknumber_call.setChecked(true);
			cb_setting_ceneter_blacknumber_call.setText("来电黑名单已开启");
			Intent service = new Intent(this,BlackNumberService.class);
			startService(service);
		}else{
			cb_setting_ceneter_blacknumber_call.setChecked(false);
			cb_setting_ceneter_blacknumber_call.setText("来电黑名单已关闭");
		}
		
		cb_setting_ceneter_app_lock = (CheckBox) findViewById(R.id.cb_setting_ceneter_app_lock);
		cb_setting_ceneter_app_lock.setOnClickListener(this);
		boolean isapplock = sp.getBoolean("isapplock", false);
		if(isapplock){
			cb_setting_ceneter_app_lock.setChecked(true);
			cb_setting_ceneter_app_lock.setText("程序锁已开启");
			Intent service = new Intent(this,AppLockService.class);
			startService(service);
		}else{
			cb_setting_ceneter_app_lock.setChecked(false);
			cb_setting_ceneter_app_lock.setText("程序锁已关闭");
			Intent service = new Intent(this,AppLockService.class);
			stopService(service);
		}
	}
	
	public void onClick(View v) {
		// TODO Auto-generated method stub
		int id = v.getId();
		switch (id) {
		case R.id.cb_setting_ceneter_auto_update:
			boolean isautoupdate = sp.getBoolean("isautoupdate", true);
			if(isautoupdate){
				cb_setting_ceneter_auto_update.setChecked(false);
				cb_setting_ceneter_auto_update.setText("自动更新已关闭");
				Editor editor = sp.edit();
				editor.putBoolean("isautoupdate", false);
				editor.commit();
			}else{
				cb_setting_ceneter_auto_update.setChecked(true);
				cb_setting_ceneter_auto_update.setText("自动更新已开启");
				Editor editor = sp.edit();
				editor.putBoolean("isautoupdate", true);
				editor.commit();
			}
			break;
		case R.id.cb_setting_ceneter_auto_ipcall:
			boolean isautoipcall = sp.getBoolean("isautoipcall", false);
			if(isautoipcall){
				cb_setting_ceneter_auto_ipcall.setChecked(false);
				cb_setting_ceneter_auto_ipcall.setText("ip拨号已关闭");
				Editor editor = sp.edit();
				editor.putBoolean("isautoipcall", false);
				editor.commit();
			}else{
				cb_setting_ceneter_auto_ipcall.setChecked(true);
				cb_setting_ceneter_auto_ipcall.setText("ip拨号已开启");
				Editor editor = sp.edit();
				editor.putBoolean("isautoipcall", true);
				editor.commit();
			}
			break;
		case R.id.cb_setting_ceneter_address_query:
			boolean isshowaddress = sp.getBoolean("isshowaddress", false);
			if(isshowaddress){
				cb_setting_ceneter_address_query.setChecked(false);
				cb_setting_ceneter_address_query.setText("归属地查询已关闭");
				Editor editor = sp.edit();
				editor.putBoolean("isshowaddress", false);
				editor.commit();
				
				Intent intent = new Intent(this,ShowAddressService.class);
				stopService(intent);
			}else{
				cb_setting_ceneter_address_query.setChecked(true);
				cb_setting_ceneter_address_query.setText("归属地查询已开启");
				Editor editor = sp.edit();
				editor.putBoolean("isshowaddress", true);
				editor.commit();
				Intent intent = new Intent(this,ShowAddressService.class);
				startService(intent);
			}
			break;
		case R.id.tv_setting_center_change_show_style:
			//判断之前选择的是什么风格
			int index = 1;
			int address_bg = sp.getInt("address_bg", 0);
			switch (address_bg) {
			case R.drawable.call_locate_white:
				index = 0;
				break;
			case R.drawable.call_locate_orange:
				index = 1;
				break;
			case R.drawable.call_locate_blue:
				index = 2;
				break;
			case R.drawable.call_locate_gray:
				index = 3;
				break;
			case R.drawable.call_locate_green:
				index = 4;
				break;

			default:
				break;
			}
			
			String[] items = new String[]{"半透明","活力橙","卫士蓝","金属灰","苹果绿"};
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle("归属地提示框风格");
			builder.setSingleChoiceItems(items, index, new DialogInterface.OnClickListener() {
				
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					int address_bg = R.drawable.call_locate_orange;
					switch (which) {
					case 0:
						address_bg = R.drawable.call_locate_white;
						break;
					case 1:
						address_bg = R.drawable.call_locate_orange;
						break;
					case 2:
						address_bg = R.drawable.call_locate_blue;
						break;
					case 3:
						address_bg = R.drawable.call_locate_gray;
						break;
					case 4:
						address_bg = R.drawable.call_locate_green;
						break;

					default:
						break;
					}
					Editor editor = sp.edit();
					editor.putInt("address_bg", address_bg);
					editor.commit();
					
					//关闭对话框
					dialog.dismiss();
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
		case R.id.tv_setting_center_change_location:
			Intent intent = new Intent(this,DragViewActivity.class);
			startActivity(intent);
			break;
		case R.id.cb_setting_ceneter_blacknumber_call:
			boolean isblacknumber = sp.getBoolean("isblacknumber", false);
			if(isblacknumber){
				cb_setting_ceneter_blacknumber_call.setChecked(false);
				cb_setting_ceneter_blacknumber_call.setText("来电黑名单已关闭");
				Editor editor = sp.edit();
				editor.putBoolean("isblacknumber", false);
				editor.commit();
			}else{
				cb_setting_ceneter_blacknumber_call.setChecked(true);
				cb_setting_ceneter_blacknumber_call.setText("来电黑名单已开启");
				Editor editor = sp.edit();
				editor.putBoolean("isblacknumber", true);
				editor.commit();
				Intent service = new Intent(this,BlackNumberService.class);
				startService(service);
			}
			break;
		case R.id.cb_setting_ceneter_app_lock:
			boolean isapplock = sp.getBoolean("isapplock", false);
			if(isapplock){
				cb_setting_ceneter_app_lock.setChecked(false);
				cb_setting_ceneter_app_lock.setText("程序锁已关闭");
				Editor editor = sp.edit();
				editor.putBoolean("isapplock", false);
				editor.commit();
				Intent service = new Intent(this,AppLockService.class);
				stopService(service);
			}else{
				cb_setting_ceneter_app_lock.setChecked(true);
				cb_setting_ceneter_app_lock.setText("程序锁已开启");
				Editor editor = sp.edit();
				editor.putBoolean("isapplock", true);
				editor.commit();
				Intent service = new Intent(this,AppLockService.class);
				startService(service);
			}
			break;
		default:
			break;
		}
	}
}

package cn.itcast.mobilesafe.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import cn.itcast.mobilesafe.R;
import cn.itcast.mobilesafe.service.AutoClearService;

public class TaskManagerSettingActivity extends Activity implements OnClickListener{

	private CheckBox cb_task_manager_setting_display;
	private CheckBox cb_task_manager_setting_auto_clear;
	private SharedPreferences sp;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		sp = getSharedPreferences("config", Context.MODE_PRIVATE);
		setContentView(R.layout.task_manager_setting);
		
		cb_task_manager_setting_display = (CheckBox) findViewById(R.id.cb_task_manager_setting_display);
		cb_task_manager_setting_display.setOnClickListener(this);
		boolean isdisplaysystem = sp.getBoolean("isdisplaysystem", true);
		if(isdisplaysystem){
			cb_task_manager_setting_display.setChecked(true);
			cb_task_manager_setting_display.setText("显示系统进程");
		}else{
			cb_task_manager_setting_display.setChecked(false);
			cb_task_manager_setting_display.setText("隐藏系统进程");
		}
		
		cb_task_manager_setting_auto_clear = (CheckBox) findViewById(R.id.cb_task_manager_setting_auto_clear);
		cb_task_manager_setting_auto_clear.setOnClickListener(this);
		boolean isautoclear = sp.getBoolean("isautoclear", false);
		if(isautoclear){
			cb_task_manager_setting_auto_clear.setChecked(true);
			Intent intent = new Intent(this,AutoClearService.class);
			startService(intent);
		}else{
			cb_task_manager_setting_auto_clear.setChecked(false);
		}
	}

	public void onClick(View v) {
		// TODO Auto-generated method stub
		int id = v.getId();
		switch (id) {
		case R.id.cb_task_manager_setting_display:
			boolean isdisplaysystem = sp.getBoolean("isdisplaysystem", true);
			if(isdisplaysystem){
				cb_task_manager_setting_display.setChecked(false);
				cb_task_manager_setting_display.setText("隐藏系统进程");
				
				Editor editor = sp.edit();
				editor.putBoolean("isdisplaysystem", false);
				editor.commit();
			}else{
				cb_task_manager_setting_display.setChecked(true);
				cb_task_manager_setting_display.setText("显示系统进程");
				
				Editor editor = sp.edit();
				editor.putBoolean("isdisplaysystem", true);
				editor.commit();
			}
			setResult(200);
			break;
		case R.id.cb_task_manager_setting_auto_clear:
			boolean isautoclear = sp.getBoolean("isautoclear", false);
			if(isautoclear){
				cb_task_manager_setting_auto_clear.setChecked(false);
				Editor editor = sp.edit();
				editor.putBoolean("isautoclear", false);
				editor.commit();
			}else{
				cb_task_manager_setting_auto_clear.setChecked(true);
				Editor editor = sp.edit();
				editor.putBoolean("isautoclear", true);
				editor.commit();
				Intent intent = new Intent(this,AutoClearService.class);
				startService(intent);
			}
			break;
		default:
			break;
		}
	}
	
	
}

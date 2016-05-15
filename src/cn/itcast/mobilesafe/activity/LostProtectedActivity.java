package cn.itcast.mobilesafe.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import cn.itcast.mobilesafe.R;
import cn.itcast.mobilesafe.utils.MD5;

public class LostProtectedActivity extends Activity {

	private SharedPreferences sp;
	private LayoutInflater mInflater;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		mInflater = LayoutInflater.from(this);
		sp = getSharedPreferences("config", Context.MODE_PRIVATE);
		String pwd = sp.getString("pwd", "");
		if("".equals(pwd)){
			//第一次进入手机防盗界面
			showFirstEntryDialog();
		}else{
			//正常的进入手机防盗界面
			showNormalEntryDialog();
		}
		
	}


	//正常的进入手机防盗界面
	private void showNormalEntryDialog() {
		// TODO Auto-generated method stub
		View view = mInflater.inflate(R.layout.normal_entry_dialog, null);
		final EditText et_pwd = (EditText) view.findViewById(R.id.et_pwd_normal_entry_dialog);
		Button bt_ok = (Button) view.findViewById(R.id.bt_ok_normal_entry_dialog);
		Button bt_cancel = (Button) view.findViewById(R.id.bt_cancel_normal_entry_dialog);
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("请输入密码");
		builder.setView(view);
		final AlertDialog dialog = builder.create();
		dialog.show();
		
		bt_ok.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String pwd = et_pwd.getText().toString();
				String md5_pwd = MD5.getData(pwd);
				String old_pwd = sp.getString("pwd", "");
				if("".equals(pwd)){
					Toast.makeText(getApplicationContext(), "密码不能为空", 1).show();
				}else if(old_pwd.equals(md5_pwd)){
					//进入手机防盗的向导界面
					loadGuideUI();
					dialog.dismiss();
					finish();
				}else{
					Toast.makeText(getApplicationContext(), "您输入的密码不正确", 1).show();
				}
			}
		});
		bt_cancel.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dialog.dismiss();
				finish();
			}
		});
	}



	//第一次进入手机防盗界面
	private void showFirstEntryDialog() {
		// TODO Auto-generated method stub
		View view = mInflater.inflate(R.layout.first_entry_dialog, null);
		final EditText et_pwd_first_entry_dialog = (EditText) view.findViewById(R.id.et_pwd_first_entry_dialog);
		final EditText et_repwd_first_entry_dialog = (EditText) view.findViewById(R.id.et_repwd_first_entry_dialog);
		Button bt_ok_first_entry_dialog = (Button) view.findViewById(R.id.bt_ok_first_entry_dialog);
		Button bt_cancel_first_entry_dialog = (Button) view.findViewById(R.id.bt_cancel_first_entry_dialog);
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setCancelable(false);
		builder.setTitle("输入密码");
		builder.setView(view);
		final AlertDialog dialog = builder.create();
		dialog.show();
		bt_ok_first_entry_dialog.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String pwd = et_pwd_first_entry_dialog.getText().toString();
				String repwd = et_repwd_first_entry_dialog.getText().toString();
				if("".equals(pwd)||"".equals(repwd)){
					Toast.makeText(getApplicationContext(), "密码不能为空", 1).show();
				}else if(pwd.equals(repwd)){
					//对密码进行加密
					String md5_pwd = MD5.getData(pwd);
					//密码相同保存密码
					Editor editor = sp.edit();
					editor.putString("pwd", md5_pwd);
					editor.commit();
					
					dialog.dismiss();
					finish();
				}else{
					Toast.makeText(getApplicationContext(), "两次输入的密码不相同", 1).show();
				}
				
			}
		});
		bt_cancel_first_entry_dialog.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dialog.dismiss();
				finish();
			}
		});
	}
	
	
	//进入手机向导界面  如果向导界面已经设置完成，直接进入手机防盗设置界面
	private void loadGuideUI(){
		//是否完成设置向导
		boolean issetup = sp.getBoolean("issetup", false);
		if(issetup){
			//直接进入手机防盗设置界面
			Intent intent = new Intent(this,LostProtectedSettingActivity.class);
			startActivity(intent);
			
		}else{
			//进入防盗向导界面
			Intent intent = new Intent(this,Setup1ConfigActivity.class);
			startActivity(intent);
		}
	}
}

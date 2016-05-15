package cn.itcast.mobilesafe.activity;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import cn.itcast.mobilesafe.R;

public class AddIpCallActivity extends Activity {

	private EditText et_ip_number;
	private SharedPreferences sp;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.add_ipcall);
		
		et_ip_number = (EditText) findViewById(R.id.et_ip_number);
		sp = getSharedPreferences("config", Context.MODE_PRIVATE);
	}
	
	public void add(View v){
		
		String ip_number = et_ip_number.getText().toString();
		if("".equals(ip_number)){
			Toast.makeText(this, "ip号码不能为空", 1).show();
		}else{
			Editor editor = sp.edit();
			editor.putString("ip_number", ip_number);
			editor.commit();
			Toast.makeText(this, "ip号码设置成功", 1).show();
			finish();
		}
	}
}

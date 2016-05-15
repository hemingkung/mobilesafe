package cn.itcast.mobilesafe.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import cn.itcast.mobilesafe.R;

public class Setup2ConfigActivity extends Activity implements OnClickListener{

	private ImageView iv_bind_sim;
	private TelephonyManager tm;
	private SharedPreferences sp;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.setup2config);
		
		iv_bind_sim = (ImageView) findViewById(R.id.iv_bind_sim);
		iv_bind_sim.setOnClickListener(this);
		tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
		sp = getSharedPreferences("config", Context.MODE_PRIVATE);
		String sim_serial = sp.getString("sim_serial", "");
		if("".equals(sim_serial)){
			iv_bind_sim.setImageResource(R.drawable.switch_off_normal);
		}else{
			iv_bind_sim.setImageResource(R.drawable.switch_on_normal);
		}
	}
	
	public void onClick(View v) {
		// TODO Auto-generated method stub
		//得到sim的唯一识别号
		String sim_serial = tm.getSimSerialNumber();
		Editor editor = sp.edit();
		editor.putString("sim_serial", sim_serial);
		editor.commit();
		iv_bind_sim.setImageResource(R.drawable.switch_on_normal);
	}
	
	public void pre(View v){
		Intent intent = new Intent(this,Setup1ConfigActivity.class);
		startActivity(intent);
		finish();
		overridePendingTransition(R.anim.tran_enter, R.anim.tran_exit);
	}
	
	public void next(View v){
		Intent intent = new Intent(this,Setup3ConfigActivity.class);
		startActivity(intent);
		finish();
		overridePendingTransition(R.anim.alpha_enter, R.anim.alpha_exit);
	}
}

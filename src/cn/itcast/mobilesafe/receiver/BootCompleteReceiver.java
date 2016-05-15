package cn.itcast.mobilesafe.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import cn.itcast.mobilesafe.service.AutoClearService;
import cn.itcast.mobilesafe.utils.Logger;

public class BootCompleteReceiver extends BroadcastReceiver {

	private SharedPreferences sp;
	private TelephonyManager tm;
	
	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		Logger.i("i", "已经拦截到开机广播");
		
		Intent i = new Intent(context,AutoClearService.class);
		context.startService(i);
		
        tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
		boolean isprotected = sp.getBoolean("isprotected", false);
		if(isprotected){
		//得到当前手机sim卡的识别号
		    String sim_serial = tm.getSimSerialNumber();
		    String old_sim_serial = sp.getString("sim_serial", "");
		    if(!sim_serial.equals(old_sim_serial)){
		    	Logger.i("i", "您的手机已经被盗");
		    	//发送短信给安全号码
		    	String safe_number = sp.getString("safe_number", "");
		    	SmsManager smsmanager = SmsManager.getDefault();
		    	smsmanager.sendTextMessage(safe_number, null, "nin de shouji keneng beidao ", null, null);
		    }
		}
		
		
	}

}

package cn.itcast.mobilesafe.receiver;

import android.app.admin.DevicePolicyManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import cn.itcast.mobilesafe.R;
import cn.itcast.mobilesafe.dao.BlackNumberDao;
import cn.itcast.mobilesafe.engine.GPSInfoService;
import cn.itcast.mobilesafe.utils.Logger;

public class SmsRecevier extends BroadcastReceiver {

	private SharedPreferences sp;
	private DevicePolicyManager devicePolicyManager;
	private BlackNumberDao blackNumberDao;
	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub

		Logger.i("i", "已经拦截到了短信");
		sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
		blackNumberDao = new BlackNumberDao(context);
		//判断保护是否开启
		boolean isprotected = sp.getBoolean("isprotected", false);
		if(isprotected){
			devicePolicyManager = (DevicePolicyManager) context.getSystemService(Context.DEVICE_POLICY_SERVICE);
			Object[] pdus = (Object[]) intent.getExtras().get("pdus");
			for(Object pdu:pdus){
				SmsMessage smsMessage = SmsMessage.createFromPdu((byte[])pdu);
				String body = smsMessage.getDisplayMessageBody();
				String address = smsMessage.getDisplayOriginatingAddress();
				String safe_number = sp.getString("safe_number", "");
				
				if("#*location*#".equals(body)){
					
					//获取手机的位置
					//把位置发送给安全号码
					//中断广播
					GPSInfoService service = GPSInfoService.getInstance(context);
					service.registenerLocationChangeListener();
					String last_location = service.getLastLocation();
					
					SmsManager smsManager = SmsManager.getDefault();
					smsManager.sendTextMessage(safe_number, null, "shou ji de wei zhi :" + last_location, null, null);
					
					abortBroadcast();
				}else if("#*lockscreen*#".equals(body)){
					//锁屏
					devicePolicyManager.lockNow();
					//重设密码
					devicePolicyManager.resetPassword("233223", 0);
					abortBroadcast();
				}else if("#*delete*#".equals(body)){
					//恢复出厂设置
					devicePolicyManager.wipeData(0);
					abortBroadcast();
				}else if("#*alarm*#".equals(body)){
					//发出报警音乐
					MediaPlayer mediaPlayer = MediaPlayer.create(context, R.raw.alarm);
					mediaPlayer.setVolume(1.0f, 1.0f);
					mediaPlayer.start();
					abortBroadcast();
				}else if(body.contains("6+1") || body.contains("cctv")){
					abortBroadcast();
				}
				Logger.i("i", body);
				
				//判断是不是黑名单的一个短信
				boolean isBlackNumber = blackNumberDao.isBlackNumber(address);
				if(isBlackNumber){
					abortBroadcast();
				}
			}
		}
	}

}

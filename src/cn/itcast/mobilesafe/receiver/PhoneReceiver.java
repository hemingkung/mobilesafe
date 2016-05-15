package cn.itcast.mobilesafe.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import cn.itcast.mobilesafe.activity.LostProtectedActivity;
import cn.itcast.mobilesafe.utils.Logger;

public class PhoneReceiver extends BroadcastReceiver {

	private SharedPreferences sp;
	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
        Logger.i("i", "已经拦截到了外拨通话");
        //1 清除数据
        //2 激活一个activity
        String number = getResultData();
        if("20122012".equals(number)){
        	setResultData(null);
        	//receiver是不存在于任务栈里面,在里面启动activity必须要指定flag  FLAG_ACTIVITY_NEW_TASK
        	Intent i = new Intent(context,LostProtectedActivity.class);
        	i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        	context.startActivity(i);
        }else{
        	sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
        	boolean isautoipcall = sp.getBoolean("isautoipcall", false);
        	if(isautoipcall){
        		String ip_number = sp.getString("ip_number", "");
        		setResultData(ip_number + number);
        	}
        }
        
	}

}

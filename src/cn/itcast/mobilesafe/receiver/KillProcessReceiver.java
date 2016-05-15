package cn.itcast.mobilesafe.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import cn.itcast.mobilesafe.utils.Logger;
import cn.itcast.mobilesafe.utils.TaskUtil;

public class KillProcessReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		Logger.i("i", " 得到广播  ，清理进程");
		TaskUtil.killAllProcess(context);
	}

}

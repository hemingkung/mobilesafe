package cn.itcast.mobilesafe.utils;

import cn.itcast.mobilesafe.R;
import android.content.Context;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class MyToast {

	public static void show(Context context,String msg){
		Toast toast = new Toast(context);
		View view = View.inflate(context, R.layout.my_toast, null);
		TextView tv_msg = (TextView) view.findViewById(R.id.tv_msg);
		tv_msg.setText(msg);
		toast.setView(view);
		toast.setDuration(Toast.LENGTH_LONG);
		toast.show();
	}
}

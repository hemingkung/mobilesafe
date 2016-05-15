package cn.itcast.mobilesafe.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import cn.itcast.mobilesafe.R;

public class MainAdapter extends BaseAdapter {

	private SharedPreferences sp;
	private int[] images = new int[]{R.drawable.lost_protect,R.drawable.callsms,R.drawable.appmanager,
			R.drawable.taskmanager,R.drawable.trafficmanager,R.drawable.antivirus,
			R.drawable.systemoptimize,R.drawable.atools,R.drawable.settingcenter};
	
	private String[] names = new String[]{"手机防盗","通讯卫士","软件管理","任务管理","流量管理","手机杀毒","系统优化","高级工具","设置中心"};
	
	private LayoutInflater mInflater;
	
	public MainAdapter(Context context) {
		// TODO Auto-generated constructor stub
		mInflater = LayoutInflater.from(context);
		sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
	}
	
	public int getCount() {
		// TODO Auto-generated method stub
		return names.length;
	}

	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return names[position];
	}

	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		View view = mInflater.inflate(R.layout.main_item, null);
		
		ImageView iv_main_item = (ImageView) view.findViewById(R.id.iv_main_item);
		TextView tv_main_item = (TextView) view.findViewById(R.id.tv_main_item);
		
		iv_main_item.setImageResource(images[position]);
		
		if(position == 0){
			String name = sp.getString("name", names[position]);
			tv_main_item.setText(name);
		}else{
			tv_main_item.setText(names[position]);
		}
		
		
		return view;
	}

}

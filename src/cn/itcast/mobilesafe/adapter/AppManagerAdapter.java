package cn.itcast.mobilesafe.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import cn.itcast.mobilesafe.R;
import cn.itcast.mobilesafe.domain.AppInfo;

public class AppManagerAdapter extends BaseAdapter {

	private Context context;
	private LayoutInflater mInflater;
	private List<AppInfo> appInfos;
	
	
	public void setAppInfos(List<AppInfo> appInfos) {
		this.appInfos = appInfos;
	}

	public AppManagerAdapter(Context context,List<AppInfo> appInfos) {
		// TODO Auto-generated constructor stub
		this.context = context;
		this.appInfos = appInfos;
		mInflater = LayoutInflater.from(context);
	}
	
	public int getCount() {
		// TODO Auto-generated method stub
		return appInfos.size();
	}

	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return appInfos.get(position);
	}

	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		//1 得到控件
		//2 得到数据
		//3 绑定数据
		View view = null;
		if(convertView != null){
			view = convertView;
		}else{
			view = mInflater.inflate(R.layout.applationinstall_item, null);
		}
		
		ImageView iv_appicon = (ImageView) view.findViewById(R.id.iv_appicon);
		TextView tv_appname = (TextView) view.findViewById(R.id.tv_appname);
		TextView tv_appversion = (TextView) view.findViewById(R.id.tv_appversion);
		
		AppInfo appInfo = appInfos.get(position);
		
		iv_appicon.setImageDrawable(appInfo.getApp_icon());
		tv_appname.setText(appInfo.getApp_name());
		tv_appversion.setText(appInfo.getApp_version());
		return view;
	}

}

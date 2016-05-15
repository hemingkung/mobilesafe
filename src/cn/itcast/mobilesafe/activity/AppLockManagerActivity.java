package cn.itcast.mobilesafe.activity;

import java.util.List;

import android.app.Activity;
import android.content.ContentValues;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import cn.itcast.mobilesafe.R;
import cn.itcast.mobilesafe.dao.AppLockDao;
import cn.itcast.mobilesafe.domain.AppInfo;
import cn.itcast.mobilesafe.engine.AppInfoService;
import cn.itcast.mobilesafe.utils.Logger;

public class AppLockManagerActivity extends Activity {

	protected static final int SUCCESS_GET_APPLICATION = 0;
	private ListView lv_applockmanager;
	private RelativeLayout rl_loading;
	private AppInfoService appInfoService;
	private List<AppInfo> appInfos;
	
	private List<String> appLocks;//程序锁应用集合
	
	private AppLockManagerAdapter mAdapter;
	private AppLockDao appLockDao;
	private Handler mHandler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case SUCCESS_GET_APPLICATION:
				appLocks = appLockDao.findAll();
				mAdapter = new AppLockManagerAdapter();
				lv_applockmanager.setAdapter(mAdapter);
				rl_loading.setVisibility(View.GONE);
				break;

			default:
				break;
			}
		};
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.app_lock_manager);
		
		lv_applockmanager = (ListView) findViewById(R.id.lv_applockmanager);
		rl_loading = (RelativeLayout) findViewById(R.id.rl_loading);
		appInfoService = new AppInfoService(this);
		new Thread(){
			public void run() {
				appInfos = appInfoService.getAppInfos();
				Message msg = new Message();
				msg.what = SUCCESS_GET_APPLICATION;
				mHandler.sendMessage(msg);
			};
		}.start();
		
		appLockDao = new AppLockDao(this);
		
		lv_applockmanager.setOnItemClickListener(new MyOnItemClickListener());
	}
	
	private final class MyOnItemClickListener implements OnItemClickListener{

		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			// TODO Auto-generated method stub
			ImageView iv_lock = ((ViewHolder)view.getTag()).iv_lock;
			AppInfo appInfo = (AppInfo) mAdapter.getItem(position);
//			boolean isLockApp = appLockDao.isLockApp(appInfo.getPackagename());
			boolean isLockApp = appLocks.contains(appInfo.getPackagename());
			if(isLockApp){
//				appLockDao.delete(appInfo.getPackagename());
				Uri uri = Uri.parse("content://applock/applock");
				String where = " packageName = ?";
				String[] selectionArgs = new String[]{appInfo.getPackagename()};
				getContentResolver().delete(uri, where, selectionArgs);
				appLocks.remove(appInfo.getPackagename());
				iv_lock.setImageResource(R.drawable.unlock);
			}else{
//				appLockDao.add(appInfo.getPackagename());
				Uri uri = Uri.parse("content://applock/applock");
				ContentValues values = new ContentValues();
				values.put("packageName", appInfo.getPackagename());
				getContentResolver().insert(uri, values);
				appLocks.add(appInfo.getPackagename());
				iv_lock.setImageResource(R.drawable.lock);
			}
			
			TranslateAnimation animation = new TranslateAnimation(0, 80, 0, 0);
			animation.setDuration(500);
			view.startAnimation(animation);
		}
		
	}
	
	private final class AppLockManagerAdapter extends BaseAdapter{

		private LayoutInflater mInflater;
		
		public AppLockManagerAdapter() {
			// TODO Auto-generated constructor stub
			mInflater = getLayoutInflater();
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
			View view = null;
			ViewHolder holder = null;
			if(convertView != null){
				view = convertView;
				holder = (ViewHolder) view.getTag();
				
				Logger.i("i", " &&&&&&&&&  convertView != null ");
			}else{
				
				Logger.i("i", " ################ convertView == null ");
				view = mInflater.inflate(R.layout.app_lock_manager_item, null);
				holder = new ViewHolder();
				holder.iv_appicon = (ImageView) view.findViewById(R.id.iv_appicon);
				holder.tv_appname = (TextView) view.findViewById(R.id.tv_appname);
				holder.iv_lock = (ImageView) view.findViewById(R.id.iv_lock);
				view.setTag(holder);
			}
			AppInfo appInfo = appInfos.get(position);
			
			
			
			ImageView iv_appicon = holder.iv_appicon;
			TextView tv_appname = holder.tv_appname;
			ImageView iv_lock = holder.iv_lock;
			
			iv_appicon.setImageDrawable(appInfo.getApp_icon());
			tv_appname.setText(appInfo.getApp_name());
			
			//boolean isLockApp = appLockDao.isLockApp(appInfo.getPackagename());
			boolean isLockApp = appLocks.contains(appInfo.getPackagename());
			if(isLockApp){
				iv_lock.setImageResource(R.drawable.lock);
			}else{
				iv_lock.setImageResource(R.drawable.unlock);
			}
			
			return view;
		}
		
	}
	
	static class ViewHolder{
		ImageView iv_appicon;
		TextView tv_appname;
		ImageView iv_lock;
	}
}

package cn.itcast.mobilesafe.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import cn.itcast.mobilesafe.R;
import cn.itcast.mobilesafe.adapter.AppManagerAdapter;
import cn.itcast.mobilesafe.domain.AppInfo;
import cn.itcast.mobilesafe.engine.AppInfoService;
import cn.itcast.mobilesafe.utils.DensityUtil;

public class AppManagerActivity extends Activity implements OnClickListener{

	protected static final int SUCCESS_GET_APPLICAITON = 0;
	
	private RelativeLayout rl_loading;
	private ListView lv_appmanage;
	private TextView tv_title;
	private PackageManager pm;
	private AppInfoService appInfoService;
	private List<AppInfo> appInfos;
	private List<AppInfo> userAppInfos;
	private boolean isAllApp = true;
	private AppManagerAdapter mAdapter;
	
	private PopupWindow mPopupWindow;
	private Handler mHandler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case SUCCESS_GET_APPLICAITON:
				if(mAdapter != null){
					if(isAllApp){
						mAdapter.setAppInfos(appInfos);
					}else{
						mAdapter.setAppInfos(userAppInfos);
					}
					mAdapter.notifyDataSetChanged();
					rl_loading.setVisibility(View.GONE);
				}else{
					//给listview去绑定数据，隐藏加载的控件
					mAdapter = new AppManagerAdapter(getApplicationContext(), appInfos);
					lv_appmanage.setAdapter(mAdapter);
					rl_loading.setVisibility(View.GONE);
					//View.VISIBLE (控件显示)View.INVISIBLE（控件隐藏  但占据空间）  View.GONE（控件隐藏  不占据空间）
				}
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
		
		setContentView(R.layout.applationinstall);
		rl_loading = (RelativeLayout) findViewById(R.id.rl_loading);
		lv_appmanage = (ListView) findViewById(R.id.lv_appmanage);
		tv_title = (TextView) findViewById(R.id.tv_title);
		tv_title.setOnClickListener(this);
		appInfoService = new AppInfoService(this);
		//包管理器
		pm = getPackageManager();
		
		initData();
		
		lv_appmanage.setOnItemClickListener(new MyOnItemClickListener());
	}

	//初始化数据
	private void initData() {
		rl_loading.setVisibility(View.VISIBLE);
		new Thread(){
			public void run() {
				appInfos = appInfoService.getAppInfos();
				
				userAppInfos = new ArrayList<AppInfo>();
				for(AppInfo appInfo:appInfos){
					if(appInfo.isUserApp()){
						userAppInfos.add(appInfo);
					}
				}
				Message msg = new Message();
				msg.what = SUCCESS_GET_APPLICAITON;
				mHandler.sendMessage(msg);
			};
		}.start();
	}
	
	private final class MyOnItemClickListener implements OnItemClickListener{

		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			// TODO Auto-generated method stub
			View contentView = View.inflate(getApplicationContext(), R.layout.popup_window, null);
			LinearLayout ll_popup_window = (LinearLayout) contentView.findViewById(R.id.ll_popup_window);
			
			ScaleAnimation animation = new ScaleAnimation(0, 1.0f,
					0, 1.0f,
					Animation.RELATIVE_TO_SELF, 0,
					Animation.RELATIVE_TO_SELF, 0.5f);
			animation.setDuration(200);
			ll_popup_window.startAnimation(animation);
			
			LinearLayout ll_uninstall = (LinearLayout) contentView.findViewById(R.id.ll_uninstall);
			LinearLayout ll_start = (LinearLayout) contentView.findViewById(R.id.ll_start);
			LinearLayout ll_share = (LinearLayout) contentView.findViewById(R.id.ll_share);
			
			MyOnClickListener l = new MyOnClickListener(position);
			ll_uninstall.setOnClickListener(l);
			ll_start.setOnClickListener(l);
			ll_share.setOnClickListener(l);
			
			
			mPopupWindow = new PopupWindow(contentView, ViewGroup.LayoutParams.WRAP_CONTENT, DensityUtil.px2dip(getApplicationContext(), 70));
			int[] arrayOfInt = new int[2];
			view.getLocationInWindow(arrayOfInt);
			int x = arrayOfInt[0] + DensityUtil.px2dip(getApplicationContext(), 60);
			int y = arrayOfInt[1];
			
			//1 指定popupwindow的背景   2 popupwindow能够获得焦点
			mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
			mPopupWindow.setFocusable(true);
			mPopupWindow.showAtLocation(view, Gravity.LEFT|Gravity.TOP, x, y);
		}
		
	}
	
	private final class MyOnClickListener implements OnClickListener{

		private int position;
		
		public MyOnClickListener(int position) {
			// TODO Auto-generated constructor stub
			this.position = position;
		}
		
		public void onClick(View v) {
			// TODO Auto-generated method stub
			AppInfo appInfo = (AppInfo) mAdapter.getItem(position);
			String packageName = appInfo.getPackagename();
			int id = v.getId();
			switch (id) {
			case R.id.ll_uninstall:
				//系统应用不能被卸载
				if(!appInfo.isUserApp()){
					Toast.makeText(getApplicationContext(), "系统应用不能被卸载", 1).show();
				}else{
					//自身不能被卸载
					if(packageName.equals(getPackageName())){
						Toast.makeText(getApplicationContext(), "自身应用不能被卸载", 1).show();
					}else{
						Intent uninstall_intent = new Intent();
						uninstall_intent.setAction(Intent.ACTION_DELETE);
						uninstall_intent.setData(Uri.parse("package:" + packageName));
//						startActivity(uninstall_intent);
						startActivityForResult(uninstall_intent, 100);
					}
				}
				break;
			case R.id.ll_start:
				try {
					PackageInfo packgeInfo = pm.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES);
					ActivityInfo[] activites = packgeInfo.activities;
					if(activites == null || activites.length == 0){
						Toast.makeText(getApplicationContext(), "该应用程序不能被启动", 0).show();
					}else{
						ActivityInfo activityInfo = activites[0];
						String name = activityInfo.name;
						ComponentName component = new ComponentName(packageName, name);
						Intent start_intent = new Intent();
						start_intent.setComponent(component);
						startActivity(start_intent);
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					Toast.makeText(getApplicationContext(), "该应用程序不能被启动", 0).show();
				}
				break;
			case R.id.ll_share:
				Intent share_intent = new Intent();
				share_intent.setAction(Intent.ACTION_SEND);
				share_intent.setType("text/plain");
				share_intent.putExtra(Intent.EXTRA_SUBJECT, "f分享");
				share_intent.putExtra(Intent.EXTRA_TEXT, "HI 推荐您使用一款软件：" + appInfo.getApp_name());
				share_intent = Intent.createChooser(share_intent, "分享");
				startActivity(share_intent);
				break;

			default:
				break;
			}
			mPopupWindow.dismiss();
		}
		
	}
	
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode == 100){
			initData();
		}
	}
	
	public void onClick(View v) {
		// TODO Auto-generated method stub
		int id = v.getId();
		switch (id) {
		case R.id.tv_title:
			if(isAllApp){
				mAdapter.setAppInfos(userAppInfos);
				mAdapter.notifyDataSetChanged();
				tv_title.setText("用  户  程  序");
				isAllApp = false;
			}else{
				mAdapter.setAppInfos(appInfos);
				mAdapter.notifyDataSetChanged();
				tv_title.setText("所  有  程  序");
				isAllApp = true;
			}
			break;

		default:
			break;
		}
	}
}

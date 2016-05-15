package cn.itcast.mobilesafe.engine;

import java.util.ArrayList;
import java.util.List;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import cn.itcast.mobilesafe.domain.TrafficInfo;

public class TrafficManagerService {

	private Context context;
	private PackageManager pm;
	
	public TrafficManagerService(Context context) {
		// TODO Auto-generated constructor stub
		this.context = context;
		pm = context.getPackageManager();
	}
	
	/**
	 * 得到所有能启动的应用
	 * @return
	 */
	public List<TrafficInfo> getLauncherTrafficInfos(){
		
		List<TrafficInfo> trafficInfos = new ArrayList<TrafficInfo>();
		//查询能够启动的应用程序  
		Intent intent = new Intent();
		intent.setAction(Intent.ACTION_MAIN);
		intent.addCategory(Intent.CATEGORY_LAUNCHER);
		
		//ResolveInfo  就类似于一个IntentFilter
		List<ResolveInfo> resolveInfos = pm.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
		for(ResolveInfo info:resolveInfos){
			ApplicationInfo appInfo = info.activityInfo.applicationInfo;
			Drawable appicon = appInfo.loadIcon(pm);
			String appname = appInfo.loadLabel(pm).toString();
			
			String packageName = appInfo.packageName;
			int uid = appInfo.uid;
			
			trafficInfos.add(new TrafficInfo(appicon, appname, packageName, uid));
		}
		return trafficInfos;
	}
	
	/**
	 * 得到有internet访问权限的应用
	 * @return
	 */
	public List<TrafficInfo> getInternetTrafficInfos(){
		List<TrafficInfo> trafficInfos = new ArrayList<TrafficInfo>();
		List<PackageInfo>  packageInfos = pm.getInstalledPackages(PackageManager.GET_UNINSTALLED_PACKAGES|PackageManager.GET_PERMISSIONS);
		for(PackageInfo info:packageInfos){
			String[] permissions = info.requestedPermissions;
			if(permissions != null){
				for(String permission:permissions){
					if(permission.equals(Manifest.permission.INTERNET)){
						ApplicationInfo appInfo = info.applicationInfo;
						Drawable appicon = appInfo.loadIcon(pm);
						String appname = appInfo.loadLabel(pm).toString();
						String packageName = appInfo.packageName;
						int uid = appInfo.uid;
						trafficInfos.add(new TrafficInfo(appicon, appname, packageName, uid));
					}
				}
			}
		}
		return trafficInfos;
	}
}

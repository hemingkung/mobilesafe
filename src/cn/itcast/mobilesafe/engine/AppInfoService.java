package cn.itcast.mobilesafe.engine;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.drawable.Drawable;
import cn.itcast.mobilesafe.domain.AppInfo;

public class AppInfoService {

	private Context context;
	private PackageManager pm;
	public AppInfoService(Context context) {
		// TODO Auto-generated constructor stub
		this.context = context;
		pm = context.getPackageManager();
	}
	
	//得到所有的应用程序信息
	public List<AppInfo> getAppInfos(){
		List<AppInfo> appInfos = new ArrayList<AppInfo>();
		List<ApplicationInfo> applicationInfos = pm.getInstalledApplications(PackageManager.GET_UNINSTALLED_PACKAGES);
		for(ApplicationInfo info:applicationInfos){
			AppInfo appInfo = new AppInfo();
			
			Drawable app_icon = info.loadIcon(pm);
			appInfo.setApp_icon(app_icon);
			
			String app_name = info.loadLabel(pm).toString();
			appInfo.setApp_name(app_name);
			
			String packageName = info.packageName;
			appInfo.setPackagename(packageName);
			try {
				PackageInfo packageInfo = pm.getPackageInfo(packageName, 0);
				String app_version = packageInfo.versionName;
				appInfo.setApp_version(app_version);
			} catch (NameNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			boolean isUserApp = filterApp(info);
			appInfo.setUserApp(isUserApp);
			appInfos.add(appInfo);
		}
		return appInfos;
	}
	
	//判断应用程序是否是用户程序
    public boolean filterApp(ApplicationInfo info) {
    	//原来是系统应用，用户手动升级
        if ((info.flags & ApplicationInfo.FLAG_UPDATED_SYSTEM_APP) != 0) {
            return true;
            //用户自己安装的应用程序
        } else if ((info.flags & ApplicationInfo.FLAG_SYSTEM) == 0) {
            return true;
        }
        return false;
    }
}

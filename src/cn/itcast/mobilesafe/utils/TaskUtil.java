package cn.itcast.mobilesafe.utils;

import java.util.ArrayList;
import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.drawable.Drawable;
import android.os.Debug.MemoryInfo;
import cn.itcast.mobilesafe.R;
import cn.itcast.mobilesafe.domain.TaskInfo;

public class TaskUtil {

	/**
	 * 得到当前正在运行的进程的数量
	 * @param context
	 * @return
	 */
	public static int getRuninngAppProcessInfoSize(Context context){
		ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		return am.getRunningAppProcesses().size();
	}
	
	/**
	 * 得到当前系统的可用内存
	 * @param context
	 * @return
	 */
	public static long getAvailMem(Context context){
		ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		android.app.ActivityManager.MemoryInfo outInfo = new android.app.ActivityManager.MemoryInfo();
		am.getMemoryInfo(outInfo);
		long availMem = outInfo.availMem;// byte
		return availMem;
	}
	

	/**
	 * 得到进程所有的信息
	 * @param context
	 * @return
	 */
	public static List<TaskInfo> getTaskInfos(Context context){
		List<TaskInfo> taskInfos = new ArrayList<TaskInfo>();
		PackageManager pm = context.getPackageManager();
		ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningAppProcessInfo> runningAppProcessInfos = am.getRunningAppProcesses();
		for(RunningAppProcessInfo info:runningAppProcessInfos){
			TaskInfo taskInfo = new TaskInfo();
			String packageName = info.processName;
			taskInfo.setPackageName(packageName);
			try {
				ApplicationInfo appInfo = pm.getApplicationInfo(packageName, 0);
				Drawable task_icon = appInfo.loadIcon(pm);
				String task_name = appInfo.loadLabel(pm).toString();
				if(task_icon == null){
					taskInfo.setTask_icon(context.getResources().getDrawable(R.drawable.ic_launcher));
				}else{
					taskInfo.setTask_icon(task_icon);
				}
				taskInfo.setTask_name(task_name);
				
				boolean isUserTask = filterApp(appInfo);
				taskInfo.setUserTask(isUserTask);
				
			} catch (NameNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				taskInfo.setTask_icon(context.getResources().getDrawable(R.drawable.ic_launcher));
				taskInfo.setTask_name(packageName);
			}
			
			int pid = info.pid;
			taskInfo.setPid(pid);
			MemoryInfo[] memoryInfos = am.getProcessMemoryInfo(new int[]{pid});
			MemoryInfo memoryInfo = memoryInfos[0];
			long task_memory = memoryInfo.getTotalPrivateDirty();//KB
			taskInfo.setTask_memory(task_memory);
			
			
			
			
			taskInfos.add(taskInfo);
		}
		return taskInfos;
	}
	
	//判断应用程序是否是用户程序
    public static boolean filterApp(ApplicationInfo info) {
    	//原来是系统应用，用户手动升级
        if ((info.flags & ApplicationInfo.FLAG_UPDATED_SYSTEM_APP) != 0) {
            return true;
            //用户自己安装的应用程序
        } else if ((info.flags & ApplicationInfo.FLAG_SYSTEM) == 0) {
            return true;
        }
        return false;
    }
    
    /**
     * 杀死所有的进程
     * @param context
     */
    public static void killAllProcess(Context context){
    	ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningAppProcessInfo> runningAppProcessInfos = am.getRunningAppProcesses();
		for(RunningAppProcessInfo info:runningAppProcessInfos){
			am.killBackgroundProcesses(info.processName);
		}
    }
}

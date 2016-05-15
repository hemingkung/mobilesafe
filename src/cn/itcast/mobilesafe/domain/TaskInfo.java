package cn.itcast.mobilesafe.domain;

import android.graphics.drawable.Drawable;

public class TaskInfo {

	private Drawable task_icon;
	private String task_name;
	private String packageName;
	private int pid;
	private long task_memory;
	private boolean isChecked;
	private boolean isUserTask;//是不是用户进程
	
	public TaskInfo() {
		super();
		// TODO Auto-generated constructor stub
	}
	public TaskInfo(Drawable task_icon, String task_name, String packageName,
			int pid, long task_memory) {
		super();
		this.task_icon = task_icon;
		this.task_name = task_name;
		this.packageName = packageName;
		this.pid = pid;
		this.task_memory = task_memory;
	}
	
	
	public Drawable getTask_icon() {
		return task_icon;
	}
	public void setTask_icon(Drawable task_icon) {
		this.task_icon = task_icon;
	}
	public String getTask_name() {
		return task_name;
	}
	public void setTask_name(String task_name) {
		this.task_name = task_name;
	}
	public String getPackageName() {
		return packageName;
	}
	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}
	public int getPid() {
		return pid;
	}
	public void setPid(int pid) {
		this.pid = pid;
	}
	public long getTask_memory() {
		return task_memory;
	}
	public void setTask_memory(long task_memory) {
		this.task_memory = task_memory;
	}
	
	
	public boolean isChecked() {
		return isChecked;
	}
	public void setChecked(boolean isChecked) {
		this.isChecked = isChecked;
	}
	
	
	public boolean isUserTask() {
		return isUserTask;
	}
	public void setUserTask(boolean isUserTask) {
		this.isUserTask = isUserTask;
	}
	@Override
	public String toString() {
		return "TaskInfo [task_icon=" + task_icon + ", task_name=" + task_name
				+ ", packageName=" + packageName + ", pid=" + pid
				+ ", task_memory=" + task_memory + "]";
	}
	
	
}

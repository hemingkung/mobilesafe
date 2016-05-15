package cn.itcast.mobilesafe.domain;

import android.graphics.drawable.Drawable;

public class TrafficInfo {

	private Drawable appicon;
	private String appname;
	private String packageName;
	private int uid;
	
	
	
	public TrafficInfo() {
		super();
		// TODO Auto-generated constructor stub
	}
	public TrafficInfo(Drawable appicon, String appname, String packageName,
			int uid) {
		super();
		this.appicon = appicon;
		this.appname = appname;
		this.packageName = packageName;
		this.uid = uid;
	}
	public Drawable getAppicon() {
		return appicon;
	}
	public void setAppicon(Drawable appicon) {
		this.appicon = appicon;
	}
	public String getAppname() {
		return appname;
	}
	public void setAppname(String appname) {
		this.appname = appname;
	}
	public String getPackageName() {
		return packageName;
	}
	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}
	public int getUid() {
		return uid;
	}
	public void setUid(int uid) {
		this.uid = uid;
	}
	@Override
	public String toString() {
		return "TrafficInfo [appicon=" + appicon + ", appname=" + appname
				+ ", packageName=" + packageName + ", uid=" + uid + "]";
	}
	
	
	
}

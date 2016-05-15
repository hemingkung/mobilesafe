package cn.itcast.mobilesafe.domain;

import android.graphics.drawable.Drawable;

public class AppInfo {

	private Drawable app_icon;
	private String app_name;
	private String app_version;
	private String packagename;
	private boolean isUserApp;
	
	
	public AppInfo() {
		super();
		// TODO Auto-generated constructor stub
	}
	public AppInfo(Drawable app_icon, String app_name, String app_version,
			String packagename) {
		super();
		this.app_icon = app_icon;
		this.app_name = app_name;
		this.app_version = app_version;
		this.packagename = packagename;
	}
	
	
	public AppInfo(Drawable app_icon, String app_name, String app_version,
			String packagename, boolean isUserApp) {
		super();
		this.app_icon = app_icon;
		this.app_name = app_name;
		this.app_version = app_version;
		this.packagename = packagename;
		this.isUserApp = isUserApp;
	}
	public Drawable getApp_icon() {
		return app_icon;
	}
	public void setApp_icon(Drawable app_icon) {
		this.app_icon = app_icon;
	}
	public String getApp_name() {
		return app_name;
	}
	public void setApp_name(String app_name) {
		this.app_name = app_name;
	}
	public String getApp_version() {
		return app_version;
	}
	public void setApp_version(String app_version) {
		this.app_version = app_version;
	}
	public String getPackagename() {
		return packagename;
	}
	public void setPackagename(String packagename) {
		this.packagename = packagename;
	}
	
	public boolean isUserApp() {
		return isUserApp;
	}
	public void setUserApp(boolean isUserApp) {
		this.isUserApp = isUserApp;
	}
	
	@Override
	public String toString() {
		return "AppInfo [app_icon=" + app_icon + ", app_name=" + app_name
				+ ", app_version=" + app_version + ", packagename="
				+ packagename + ", isUserApp=" + isUserApp + "]";
	}

	
	
	
}

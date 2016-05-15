package cn.itcast.mobilesafe;

import android.app.Application;

public class MobileSafeApplication extends Application {

	private String packname;
	
	public String getPackname() {
		return packname;
	}

	public void setPackname(String packname) {
		this.packname = packname;
	}



	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
	}

	
}

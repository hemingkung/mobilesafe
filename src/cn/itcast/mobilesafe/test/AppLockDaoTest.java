package cn.itcast.mobilesafe.test;

import java.util.List;

import android.test.AndroidTestCase;
import cn.itcast.mobilesafe.dao.AppLockDao;
import cn.itcast.mobilesafe.utils.Logger;

public class AppLockDaoTest extends AndroidTestCase {

	public void testAdd() throws Exception{
		AppLockDao dao = new AppLockDao(getContext());
		dao.add("cn.itcast.service");
		dao.add("com.android.alarmclock");
	}
	
	public void testDelete() throws Exception{
		AppLockDao dao = new AppLockDao(getContext());
		dao.delete("cn.itcast.service");
	}
	
	public void testIsLockApp() throws Exception{
		AppLockDao dao = new AppLockDao(getContext());
		Boolean isLockApp = dao.isLockApp("cn.itcast.service");
		Logger.i("i", " isLockApp "  + isLockApp);
	}
	
	public void testFindAll() throws Exception{
		AppLockDao dao = new AppLockDao(getContext());
		List<String> appLocks = dao.findAll();
		for(String packageName: appLocks){
			Logger.i("i", " packageName "  + packageName);
		}
		
	}
	
	
	
}

package cn.itcast.mobilesafe.test;

import cn.itcast.mobilesafe.engine.GPSInfoService;
import android.test.AndroidTestCase;

public class GPSInfoServiceTest extends AndroidTestCase {

	
	public void testRegisterLocationChangeListener() throws Exception{
		GPSInfoService service = GPSInfoService.getInstance(getContext());
		service.registenerLocationChangeListener();
	}
}

package cn.itcast.mobilesafe.test;

import android.test.AndroidTestCase;
import cn.itcast.mobilesafe.engine.AddressQueryService;
import cn.itcast.mobilesafe.utils.Logger;

public class AddressQueryServiceTest extends AndroidTestCase {

	
	public void testQuery() throws Exception{
		AddressQueryService service = new AddressQueryService();
		String address = service.query("0105441053");
		Logger.i("i", " address " + address);
	}
}

package cn.itcast.mobilesafe.test;

import java.util.List;

import android.test.AndroidTestCase;
import cn.itcast.mobilesafe.domain.SmsInfo;
import cn.itcast.mobilesafe.engine.SmsInfoService;
import cn.itcast.mobilesafe.utils.Logger;

public class SmsInfoServiceTest extends AndroidTestCase {

	public void testGetSmsInfos() throws Exception{
		SmsInfoService service = new SmsInfoService(getContext());
		List<SmsInfo> smsInfos = service.getSmsInfos();
		for(SmsInfo info:smsInfos){
			Logger.i("i", info.toString());
		}
	}
	
	public void testCreateXml() throws Exception{
		SmsInfoService service = new SmsInfoService(getContext());
		List<SmsInfo> smsInfos = service.getSmsInfos();
		service.createXml(smsInfos);
	}
}

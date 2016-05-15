package cn.itcast.mobilesafe.test;

import java.util.List;

import android.test.AndroidTestCase;
import cn.itcast.mobilesafe.domain.ContactInfo;
import cn.itcast.mobilesafe.engine.ContactInfoService;
import cn.itcast.mobilesafe.utils.Logger;

public class ContactInfoServiceTest extends AndroidTestCase {

	public void testGetContacts() throws Exception{
		ContactInfoService service = new ContactInfoService(getContext());
		List<ContactInfo> contacts = service.getContacts();
		for(ContactInfo info:contacts){
			Logger.i("i", info.toString());
		}
			 
	}
}

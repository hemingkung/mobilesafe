package cn.itcast.mobilesafe.engine;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;
import cn.itcast.mobilesafe.domain.ContactInfo;

public class ContactInfoService {
	
	private Context context;
	
	public ContactInfoService(Context context) {
		// TODO Auto-generated constructor stub
		this.context = context;
	}

	//获得手机里面所有的联系人
	public List<ContactInfo> getContacts(){
		List<ContactInfo> contactInfos = new ArrayList<ContactInfo>();
		ContentResolver cr = context.getContentResolver();
		//查询raw_contacts表得到联系人的_id
		Uri uri = Uri.parse("content://com.android.contacts/raw_contacts");
		Cursor c = cr.query(uri, new String[]{"_id","display_name"}, null, null, null);
//		String[] names = c.getColumnNames();
//		for(String name:names){
//			Logger.i("i", name);
//		}
		while(c.moveToNext()){
			ContactInfo info = new ContactInfo();
			String _id = c.getString(c.getColumnIndex("_id"));
			String name = c.getString(c.getColumnIndex("display_name"));
			info.setName(name);
			
			//查询data表
			uri = Uri.parse("content://com.android.contacts/raw_contacts/" + _id + "/data");
			Cursor c1 = cr.query(uri, new String[]{"data1","mimetype"}, null, null, null);
            while(c1.moveToNext()){
            	String data1 = c1.getString(c1.getColumnIndex("data1"));
            	String mimetype = c1.getString(c1.getColumnIndex("mimetype"));
            	if("vnd.android.cursor.item/phone_v2".equals(mimetype)){
            		info.setNumber(data1);
            		contactInfos.add(info);
            	}
            }
            c1.close();
		}
		c.close();
		return contactInfos;
	}
}

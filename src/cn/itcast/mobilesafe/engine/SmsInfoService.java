package cn.itcast.mobilesafe.engine;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlSerializer;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.util.Xml;
import cn.itcast.mobilesafe.domain.SmsInfo;

public class SmsInfoService {

	private Context context;
	public SmsInfoService(Context context) {
		// TODO Auto-generated constructor stub
		this.context = context;
	}
	
	//得到所有的短信
	public List<SmsInfo> getSmsInfos(){
		List<SmsInfo> smsInfos = new ArrayList<SmsInfo>();
		Uri uri = Uri.parse("content://sms");
		Cursor c = context.getContentResolver().query(uri, new String[]{"address","date","type","body"}, null, null, null);
		while(c.moveToNext()){
			String address = c.getString(c.getColumnIndex("address"));
			String date = c.getString(c.getColumnIndex("date"));
			String type = c.getString(c.getColumnIndex("type"));
			String body = c.getString(c.getColumnIndex("body"));
			
			SmsInfo smsInfo = new SmsInfo(address, date, type, body);
			smsInfos.add(smsInfo);
		}
		return smsInfos;
	}
	
	//把短信数据写入到xml文件
	public void createXml(List<SmsInfo> smsInfos) throws Exception{
		XmlSerializer serializer = Xml.newSerializer();
		File file = new File(Environment.getExternalStorageDirectory(), "smsbackup.xml");
		OutputStream os = new FileOutputStream(file);
		serializer.setOutput(os, "UTF-8");
		
		serializer.startDocument("UTF-8", true);
		serializer.startTag(null, "smsinfos");
		
		for(SmsInfo info:smsInfos){
			serializer.startTag(null, "smsinfo");
			//address
			serializer.startTag(null, "address");
			serializer.text(info.getAddress());
			serializer.endTag(null, "address");
			
			//date
			serializer.startTag(null, "date");
			serializer.text(info.getDate());
			serializer.endTag(null, "date");
			
			//type
			serializer.startTag(null, "type");
			serializer.text(info.getType());
			serializer.endTag(null, "type");
			
			//body
			serializer.startTag(null, "body");
			serializer.text(info.getBody());
			serializer.endTag(null, "body");
			
			serializer.endTag(null, "smsinfo");
		}
		serializer.endTag(null, "smsinfos");
		serializer.endDocument();
		os.close();
	}
	
	//从xml文件中得到短信数据
	public List<SmsInfo> getSmsInfosFromXml() throws Exception{
		List<SmsInfo> smsInfos =null;
		SmsInfo smsInfo = null;
		XmlPullParser parser = Xml.newPullParser();
		File file = new File(Environment.getExternalStorageDirectory(), "smsbackup.xml");
		InputStream inputStream = new FileInputStream(file);
		parser.setInput(inputStream, "UTF-8");
		int eventType = parser.getEventType();
		while(eventType != XmlPullParser.END_DOCUMENT){
			switch (eventType) {
			case XmlPullParser.START_TAG:
				if("smsinfos".equals(parser.getName())){
					smsInfos = new ArrayList<SmsInfo>();
				}else if("smsinfo".equals(parser.getName())){
					smsInfo = new SmsInfo();
				}else if("address".equals(parser.getName())){
					String address = parser.nextText();
					smsInfo.setAddress(address);
				}else if("date".equals(parser.getName())){
					String date = parser.nextText();
					smsInfo.setDate(date);
				}else if("type".equals(parser.getName())){
					String type = parser.nextText();
					smsInfo.setType(type);
				}else if("body".equals(parser.getName())){
					String body = parser.nextText();
					smsInfo.setBody(body);
				}
				
				break;
			case XmlPullParser.END_TAG:
                if("smsinfo".equals(parser.getName())){
					smsInfos.add(smsInfo);
					smsInfo = null;
				}
				break;

			default:
				break;
			}
			eventType = parser.next();
		}
		return smsInfos;
	}
}

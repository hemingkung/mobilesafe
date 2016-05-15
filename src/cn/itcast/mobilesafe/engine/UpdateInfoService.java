package cn.itcast.mobilesafe.engine;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.util.Xml;

import cn.itcast.mobilesafe.domain.UpdateInfo;

public class UpdateInfoService {

	/**
	 * 获取服务上的最新的版本信息
	 * @param path  路径
	 * @return
	 * @throws Exception 
	 */
	public UpdateInfo getUpdateInfo(String path) throws Exception{
		URL url = new URL(path);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setConnectTimeout(5000);
		conn.setRequestMethod("GET");
		if(conn.getResponseCode() == 200){
			InputStream is = conn.getInputStream();
			return parserUpdateInfo(is);
		}
		return null;
	}

	/**
	 * 解析服务的流信息为UpdateInfo对象
	 * @param is
	 * @return
	 * @throws Exception 
	 */
	private UpdateInfo parserUpdateInfo(InputStream is) throws Exception {
		// TODO Auto-generated method stub
		UpdateInfo updateInfo = null;
		XmlPullParser parser = Xml.newPullParser();
		parser.setInput(is, "UTF-8");
		int eventType = parser.getEventType();
		while(eventType != XmlPullParser.END_DOCUMENT){
			switch (eventType) {
			case XmlPullParser.START_TAG:
				if("updateinfo".equals(parser.getName())){
					updateInfo = new UpdateInfo();
				}else if("version".equals(parser.getName())){
					String version = parser.nextText();
					updateInfo.setVersion(version);
				}else if("url".equals(parser.getName())){
					String url = parser.nextText();
					updateInfo.setUrl(url);
				}else if("description".equals(parser.getName())){
					String description = parser.nextText();
					updateInfo.setDescription(description);
				}
				break;

			default:
				break;
			}
			eventType = parser.next();
		}
		return updateInfo;
	}
}

package cn.itcast.mobilesafe.engine;

import java.io.File;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;

/**
 * 1 webservice 手机必须联网  
 * 2 可以放置一个数据在手机里面 
 *   2.1 可以把资源放置assets目录下面  不能大于1m
 *   2.2 把数据文件分成很多个  放置在res/raw
 *   2.3 从服务上下载一个数据库放置在手机的sdcard
 *
 */
public class AddressQueryService {

	/**
	 * 判断归属地数据库是否存在
	 * @return
	 */
	public boolean isExist(){
		File file = new File(Environment.getExternalStorageDirectory(), "address.db");
		return file.exists();
	}
	
	
	public String query(String number){
		String address = null;
		File file = new File(Environment.getExternalStorageDirectory(), "address.db");
		SQLiteDatabase db = SQLiteDatabase.openDatabase(file.getAbsolutePath(), null, SQLiteDatabase.OPEN_READONLY);
		if(db.isOpen()){
			//是否是手机号码
			String regularExpression = "^1[358]\\d{9}$";
			boolean isphone = number.matches(regularExpression);
			if(isphone){
				String prefix_number = number.substring(0, 7);
				Cursor c = db.query("info",
						new String[]{"city"},
						" mobileprefix = ? ",
						new String[]{prefix_number}, null, null, null);
				if(c.moveToFirst()){
					address = c.getString(0);
				}
				c.close();
			}else{
				//处理电话号码
				//3位区号 + 7位电话号码
				if(number.length() == 10){
					String area = number.substring(0, 3);
					Cursor c = db.query("info", new String[]{"city"},
							" area = ?", 
							new String[]{area}, null, null, null);
					if(c.moveToFirst()){
						address = c.getString(0);
					}
					c.close();
				}else if(number.length() == 11){
					//3位区号 + 8位电话号码
					String area1 = number.substring(0, 3);
					Cursor c1 = db.query("info", new String[]{"city"},
							" area = ?", 
							new String[]{area1}, null, null, null);
					if(c1.moveToFirst()){
						address = c1.getString(0);
					}
					c1.close();
					
					//4位区号 + 7位电话号码
					String area2 = number.substring(0, 4);
					Cursor c2 = db.query("info", new String[]{"city"},
							" area = ?", 
							new String[]{area2}, null, null, null);
					if(c2.moveToFirst()){
						address = c2.getString(0);
					}
					c2.close();
				}else if(number.length() == 12){
					//4位区号 + 8位电话号码
					String area = number.substring(0, 4);
					Cursor c = db.query("info", new String[]{"city"},
							" area = ?", 
							new String[]{area}, null, null, null);
					if(c.moveToFirst()){
						address = c.getString(0);
					}
					c.close();
				}else if(number.length() == 7 || number.length() == 8){
					address = "本地号码";
				}else if(number.length() == 4){
					address = "模拟器";
				}else if(number.length() == 3){
					address = "紧急号码";
				}
			}

			db.close();
		}
		
		if(address == null){
			address = "未知号码";
		}
		
		return address;
	}
}

package cn.itcast.mobilesafe.engine;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class CommonNumberService {

	private Context context;
	public CommonNumberService(Context context) {
		// TODO Auto-generated constructor stub
		this.context = context;
	}
	
	/**
	 * 得到组数据
	 * @return
	 */
	public List<Map<String,String>> getGroupData(){
		List<Map<String,String>> groupData = new ArrayList<Map<String,String>>();
		File file = new File(context.getFilesDir(), "commonnum.db");
		SQLiteDatabase db = SQLiteDatabase.openDatabase(file.getAbsolutePath(), null, SQLiteDatabase.OPEN_READONLY);
		if(db.isOpen()){
			Cursor c = db.query("classlist", new String[]{"name","idx"}, null, null, null, null, null);
			while(c.moveToNext()){
				Map<String,String> map = new HashMap<String, String>();
				String name = c.getString(c.getColumnIndex("name"));
				String idx = c.getString(c.getColumnIndex("idx"));
				map.put("name", name);
				map.put("idx", idx);
				groupData.add(map);
			}
			c.close();
			db.close();
		}
		return groupData;
	}
	
	/**
	 * 得到孩子的数据
	 * @return
	 */
	public List<List<Map<String,String>>> getChildData(){
		List<List<Map<String,String>>> childData = new ArrayList<List<Map<String,String>>>();
		List<Map<String,String>> parent = getGroupData();
		for(int i = 0;i< parent.size();i++){
			List<Map<String,String>> list = new ArrayList<Map<String,String>>();
			File file = new File(context.getFilesDir(), "commonnum.db");
			SQLiteDatabase db = SQLiteDatabase.openDatabase(file.getAbsolutePath(), null, SQLiteDatabase.OPEN_READONLY);
			if(db.isOpen()){
				String idx = parent.get(i).get("idx");
				Cursor c = db.query("table" + idx, 
						new String[]{"_id","number","name"},
						null, 
						null, null, null, null);
				while(c.moveToNext()){
					Map<String,String> map = new HashMap<String, String>();
					String name = c.getString(c.getColumnIndex("name"));
					String number = c.getString(c.getColumnIndex("number"));
				    map.put("name", name);
				    map.put("number", number);
				    list.add(map);
				}
				c.close();
				db.close();
				
			}
			childData.add(list);
		}
		return childData;
	}
}

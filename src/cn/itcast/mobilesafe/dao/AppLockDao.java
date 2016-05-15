package cn.itcast.mobilesafe.dao;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import cn.itcast.mobilesafe.db.AppLockDBHelper;

public class AppLockDao {

	private SQLiteOpenHelper mOpenHelper;
	
	public AppLockDao(Context context) {
		// TODO Auto-generated constructor stub
		mOpenHelper = AppLockDBHelper.getInstance(context);
	}
	
	/**
	 * 添加应用程序到程序锁
	 * @param packageName
	 */
	public void add(String packageName){
		SQLiteDatabase db = mOpenHelper.getWritableDatabase();
		if(db.isOpen()){
			ContentValues values = new ContentValues();
			values.put("packageName", packageName);
			db.insert("applock", "_id", values);
			db.close();
		}
	}
	
	/**
	 *从程序锁中删除指定的应用程序
	 * @param packageName
	 */
	public void delete(String packageName){
		SQLiteDatabase db = mOpenHelper.getWritableDatabase();
		if(db.isOpen()){
			db.delete("applock", "packageName = ?", new String[]{packageName});
			db.close();
		}
	}
	
	/**
	 * 判断应用程序是否在程序锁中
	 * @param packageName
	 * @return
	 */
	public boolean isLockApp(String packageName){
		boolean isLockApp = false;
		SQLiteDatabase db = mOpenHelper.getReadableDatabase();
		if(db.isOpen()){
			Cursor c = db.query("applock", null, "packageName = ?", new String[]{packageName}, null, null, null);
			if(c.moveToNext()){
				isLockApp = true;
			}
			c.close();
			db.close();
		}
		return isLockApp;
	}
	
	/**
	 * 得到所有的加锁程序
	 * @return
	 */
	public List<String> findAll(){
		List<String> applocks = new ArrayList<String>();
		SQLiteDatabase db = mOpenHelper.getReadableDatabase();
		if(db.isOpen()){
			Cursor c = db.query("applock", new String[]{"packageName"}, null, null, null, null, null);
			while(c.moveToNext()){
				String packageName = c.getString(0);
				applocks.add(packageName);
			}
			c.close();
			db.close();
		}
		return applocks;
	}
	
}

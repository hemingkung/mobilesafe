package cn.itcast.mobilesafe.dao;

import java.util.ArrayList;
import java.util.List;

import cn.itcast.mobilesafe.db.BlackNumberDBHelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class BlackNumberDao {

	private SQLiteOpenHelper mOpenHelper;
	
	public BlackNumberDao(Context context) {
		// TODO Auto-generated constructor stub
		mOpenHelper = BlackNumberDBHelper.getInstance(context);
	}
	
	//添加黑名单
	public void add(String number){
		SQLiteDatabase db = mOpenHelper.getWritableDatabase();
		if(db.isOpen()){
			ContentValues values = new ContentValues();
			values.put("number", number);
			db.insert("blacknumber", "_id", values);
			db.close();
		}
	}
	
	//判断号码是否是黑名单
	public boolean isBlackNumber(String number){
		boolean isExist = false;
		SQLiteDatabase db = mOpenHelper.getReadableDatabase();
		if(db.isOpen()){
			Cursor c = db.query("blacknumber", null, " number = ? ", new String[]{number}, null, null, null);
			if(c.moveToFirst()){
				isExist = true;
			}
			c.close();
			db.close();
		}
		return isExist;
	}
	
	//根据号码查询id
	public int queryId(String number){
		int _id = 0;
		SQLiteDatabase db = mOpenHelper.getReadableDatabase();
		if(db.isOpen()){
			Cursor c = db.query("blacknumber", new String[]{"_id"}, " number = ? ", new String[]{number}, null, null, null);
			if(c.moveToFirst()){
				_id = c.getInt(0);
			}
			c.close();
			db.close();
		}
		return _id;
	}
	
	//删除黑名单
	public void delete(String number){
		SQLiteDatabase db = mOpenHelper.getWritableDatabase();
		if(db.isOpen()){
			db.delete("blacknumber", " number = ? ", new String[]{number});
			db.close();
		}
	}
	
	//更新黑名单
	public void update(int id,String number){
		SQLiteDatabase db = mOpenHelper.getWritableDatabase();
		if(db.isOpen()){
			ContentValues values = new ContentValues();
			values.put("number", number);
			db.update("blacknumber", values, " _id = ? ", new String[]{id+""});
			db.close();
		}
	}
	
	//得到所有的黑名单
	public List<String> findAll(){
		List<String> blacknumbers = new ArrayList<String>();
		SQLiteDatabase db = mOpenHelper.getReadableDatabase();
		if(db.isOpen()){
			Cursor c = db.query("blacknumber", new String[]{"number"}, null, null, null, null, null);
			while(c.moveToNext()){
				String number = c.getString(0);
				blacknumbers.add(number);
			}
			c.close();
			db.close();
		}
		return blacknumbers;
	}
	
}

package cn.itcast.mobilesafe.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class BlackNumberDBHelper extends SQLiteOpenHelper {

	private static SQLiteOpenHelper mInstance;
	
	private final static String name = "blacknumber.db";
	
	public static SQLiteOpenHelper getInstance(Context context){
		if(mInstance == null){
			mInstance = new BlackNumberDBHelper(context, name, null, 1);
		}
		return mInstance;
	}
	
	private BlackNumberDBHelper(Context context, String name,
			CursorFactory factory, int version) {
		super(context, name, factory, version);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
        db.execSQL("create table blacknumber(_id integer primary key autoincrement,number text)");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub

	}

}

package cn.itcast.mobilesafe.activity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.SimpleExpandableListAdapter;
import cn.itcast.mobilesafe.R;
import cn.itcast.mobilesafe.engine.CommonNumberService;

public class CommonNumberActivity extends Activity {

	
	private ExpandableListView elv_commom_number;
	private CommonNumberService service;
	private SimpleExpandableListAdapter adapter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.common_number);
		
		elv_commom_number = (ExpandableListView) findViewById(R.id.elv_commom_number);
		service = new CommonNumberService(this);
		
		PackageManager pm = null;
		
		//拷贝数据库到files目录
		try {
			File file = new File(getFilesDir(), "commonnum.db");
			if(!file.exists()){
				FileOutputStream fos = new FileOutputStream(file);
				InputStream is = getAssets().open("commonnum.db");
				byte[] buffer = new byte[1024];
				int len = 0;
				while((len = is.read(buffer)) != -1){
					fos.write(buffer, 0, len);
				}
				is.close();
				fos.close();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		List<Map<String,String>> groupData = service.getGroupData();
		List<List<Map<String,String>>> childData = service.getChildData();
		
		adapter = new SimpleExpandableListAdapter(this,
				groupData,
				android.R.layout.simple_expandable_list_item_1,
				new String[]{"name"}, 
				new int[]{android.R.id.text1},
				childData, 
				android.R.layout.simple_expandable_list_item_2, 
				new String[]{"name","number"}, 
				new int[]{android.R.id.text1,android.R.id.text2});
		
		elv_commom_number.setAdapter(adapter);
		
		elv_commom_number.setOnChildClickListener(new MyOnChildClickListener());
	}
	
	private final class MyOnChildClickListener implements OnChildClickListener{

		public boolean onChildClick(ExpandableListView parent, View v,
				int groupPosition, int childPosition, long id) {
			// TODO Auto-generated method stub
			Map<String,String> map = (Map<String, String>) adapter.getChild(groupPosition, childPosition);
			String number = map.get("number");
			
			Intent intent = new Intent();
			intent.setAction(Intent.ACTION_DIAL);
			intent.setData(Uri.parse("tel:" + number));
			startActivity(intent);
			return false;
		}
		
	}
}

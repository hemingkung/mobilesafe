package cn.itcast.mobilesafe.activity;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import cn.itcast.mobilesafe.R;
import cn.itcast.mobilesafe.adapter.ContactInfoAdapter;
import cn.itcast.mobilesafe.domain.ContactInfo;
import cn.itcast.mobilesafe.engine.ContactInfoService;

public class ContactListActivity extends Activity {

	private ListView lv_contact;
	
	private ContactInfoService service;
	
	private ContactInfoAdapter mAdater;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.contact_list);
		
		lv_contact = (ListView) findViewById(R.id.lv_contact);
		
		service = new ContactInfoService(this);
		List<ContactInfo> contacts = service.getContacts();
		
		mAdater = new ContactInfoAdapter(this,contacts);
		lv_contact.setAdapter(mAdater);
		
		lv_contact.setOnItemClickListener(new MyOnItemClickListener());
	}
	
	private final class MyOnItemClickListener implements OnItemClickListener{

		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			// TODO Auto-generated method stub
			ContactInfo info = (ContactInfo) mAdater.getItem(position);
			String number = info.getNumber();
			Intent data = new Intent();
			data.putExtra("number", number);
			setResult(200, data);//往上一个activity返回数据
			finish();
		}
		
	}
}

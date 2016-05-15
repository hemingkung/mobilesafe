package cn.itcast.mobilesafe.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import cn.itcast.mobilesafe.R;
import cn.itcast.mobilesafe.domain.ContactInfo;

public class ContactInfoAdapter extends BaseAdapter {
	
	private Context context;
	private List<ContactInfo> contacts;
	private LayoutInflater mInflater;

	public ContactInfoAdapter(Context context,
			List<ContactInfo> contacts) {
		// TODO Auto-generated constructor stub
		this.context = context;
		this.contacts = contacts;
		mInflater = LayoutInflater.from(context);
	}

	public int getCount() {
		// TODO Auto-generated method stub
		return contacts.size();
	}

	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return contacts.get(position);
	}

	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		View view = mInflater.inflate(R.layout.contact_item, null);
		
		TextView tv_name = (TextView) view.findViewById(R.id.tv_name);
		TextView tv_number = (TextView) view.findViewById(R.id.tv_number);
		
		ContactInfo info = contacts.get(position);
		tv_name.setText(info.getName());
		tv_number.setText(info.getNumber());
		return view;
	}

}

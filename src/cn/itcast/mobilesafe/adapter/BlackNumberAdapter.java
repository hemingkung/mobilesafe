package cn.itcast.mobilesafe.adapter;

import java.util.List;

import cn.itcast.mobilesafe.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class BlackNumberAdapter extends BaseAdapter {
	
	private Context context;
	private List<String> blacknumbers;
	private LayoutInflater mInflater;
	public BlackNumberAdapter(Context context,List<String> blacknumbers) {
		// TODO Auto-generated constructor stub
		this.context = context;
		this.blacknumbers = blacknumbers;
		
		mInflater = LayoutInflater.from(context);
	}

	
	
	public void setBlacknumbers(List<String> blacknumbers) {
		this.blacknumbers = blacknumbers;
	}



	public int getCount() {
		// TODO Auto-generated method stub
		return blacknumbers.size();
	}

	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return blacknumbers.get(position);
	}

	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		View view = null;
		if(convertView != null){
			view = convertView;
		}else{
			view = mInflater.inflate(R.layout.blacknumber_item, null);
		}
		
		TextView tv_blacknumber = (TextView) view.findViewById(R.id.tv_blacknumber);
		
		String blacknumber = blacknumbers.get(position);
		
		tv_blacknumber.setText(blacknumber);
		return view;
	}

}

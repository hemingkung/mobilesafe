package cn.itcast.mobilesafe.activity;

import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import cn.itcast.mobilesafe.R;
import cn.itcast.mobilesafe.adapter.BlackNumberAdapter;
import cn.itcast.mobilesafe.dao.BlackNumberDao;
import cn.itcast.mobilesafe.utils.Logger;

public class BlackNumberListActivity extends Activity implements OnClickListener{

	
	private static final int MENU_UPDATE_ID = 0;
	private static final int MENU_DELETE_ID = 1;
	private TextView tv_add_blacknumber;
	private ListView lv_blacknumber;
	private TextView empty;
	private View view;
	private LayoutInflater mInflater;
	private EditText et_number_blacknumber_dialog;
	private Button bt_ok_blacknumber_dialog;
	private Button bt_cancel_blacknumber_dialog;
	private BlackNumberDao blackNumberDao;
	private AlertDialog dialog;
	private BlackNumberAdapter mAdapter;
	
	private int flag = 0;
	private final static int ADD = 1;
	private final static int UPDATE = 2;
	private String blacknumber;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		Logger.i("i", "  on create ");
		blacknumber = getIntent().getStringExtra("number");
		
		setContentView(R.layout.blacknumber_list);
		
		tv_add_blacknumber = (TextView) findViewById(R.id.tv_add_blacknumber);
		lv_blacknumber = (ListView) findViewById(R.id.lv_blacknumber);
		empty = (TextView) findViewById(R.id.empty);
		
		mInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		view = mInflater.inflate(R.layout.add_blacknumber_dialog, null);
		et_number_blacknumber_dialog = (EditText) view.findViewById(R.id.et_number_blacknumber_dialog);
		bt_ok_blacknumber_dialog = (Button) view.findViewById(R.id.bt_ok_blacknumber_dialog);
		bt_cancel_blacknumber_dialog = (Button) view.findViewById(R.id.bt_cancel_blacknumber_dialog);
		
		
		bt_ok_blacknumber_dialog.setOnClickListener(this);
		bt_cancel_blacknumber_dialog.setOnClickListener(this);
		
		//当listview没有数据的显示内容
		lv_blacknumber.setEmptyView(empty);
		
		blackNumberDao = new BlackNumberDao(this);
		List<String> blacknumbers = blackNumberDao.findAll();
		mAdapter = new BlackNumberAdapter(this, blacknumbers);
		lv_blacknumber.setAdapter(mAdapter);
		tv_add_blacknumber.setOnClickListener(this);
		
		//给一个控件注册上下文菜单
		registerForContextMenu(lv_blacknumber);
		
		if(blacknumber != null){
			boolean isBlackNumber = blackNumberDao.isBlackNumber(blacknumber);
			if(!isBlackNumber){
				ViewGroup parent = (ViewGroup) view.getParent();
				if(parent != null){
					parent.removeAllViews();
				}
				et_number_blacknumber_dialog.setText(blacknumber);
				AlertDialog.Builder builder = new AlertDialog.Builder(this);
				builder.setTitle("添加黑名单");
				builder.setView(view);
				dialog = builder.create();
				dialog.show();
				flag = ADD;
			}
		}

	}
	
	@Override
	protected void onNewIntent(Intent intent) {
		// TODO Auto-generated method stub
		super.onNewIntent(intent);
		Logger.i("i", "  on new  intent");
		blacknumber = intent.getStringExtra("number");
		if(blacknumber != null){
			boolean isBlackNumber = blackNumberDao.isBlackNumber(blacknumber);
			if(!isBlackNumber){
				ViewGroup parent = (ViewGroup) view.getParent();
				if(parent != null){
					parent.removeAllViews();
				}
				et_number_blacknumber_dialog.setText(blacknumber);
				AlertDialog.Builder builder = new AlertDialog.Builder(this);
				builder.setTitle("添加黑名单");
				builder.setView(view);
				dialog = builder.create();
				dialog.show();
				flag = ADD;
			}
		}

	}
	
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		// TODO Auto-generated method stub
		super.onCreateContextMenu(menu, v, menuInfo);
		
		menu.add(0, MENU_UPDATE_ID, 0, "更新黑名单号码");
		menu.add(0, MENU_DELETE_ID, 0, "删除黑名单号码");
	}
	
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		AdapterContextMenuInfo acmi = (AdapterContextMenuInfo) item.getMenuInfo();
		int position = acmi.position;
		blacknumber = (String) mAdapter.getItem(position);
		int id = item.getItemId();
		switch (id) {
		case MENU_UPDATE_ID:
			ViewGroup parent = (ViewGroup) view.getParent();
			if(parent != null){
				parent.removeAllViews();
			}
			et_number_blacknumber_dialog.setText(blacknumber);
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle("更新黑名单");
			builder.setView(view);
			dialog = builder.create();
			dialog.show();
			
			flag = UPDATE;
			break;
		case MENU_DELETE_ID:
			blackNumberDao.delete(blacknumber);
			mAdapter.setBlacknumbers(blackNumberDao.findAll());
			mAdapter.notifyDataSetChanged();
			break;

		default:
			break;
		}
		return super.onContextItemSelected(item);
	}

	public void onClick(View v) {
		// TODO Auto-generated method stub
		int id = v.getId();
		switch (id) {
		case R.id.tv_add_blacknumber:
			
			ViewGroup parent = (ViewGroup) view.getParent();
			if(parent != null){
				parent.removeAllViews();
			}
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle("添加黑名单");
			builder.setView(view);
			dialog = builder.create();
			dialog.show();
			flag = ADD;
			
			break;
		case R.id.bt_ok_blacknumber_dialog:
			String number = et_number_blacknumber_dialog.getText().toString();
			if("".equals(number)){
				Toast.makeText(this, "黑名单号码不能为空", 1).show();
			}else{
				boolean isBlackNumber = blackNumberDao.isBlackNumber(number);
				if(isBlackNumber){
					Toast.makeText(this, "号码已经存在于黑名单中", 1).show();
				}else{
					if(flag == ADD){
						blackNumberDao.add(number);
						Toast.makeText(this, "黑名单号码添加成功", 1).show();
					}else{
						int _id = blackNumberDao.queryId(blacknumber);
						blackNumberDao.update(_id, number);
						Toast.makeText(this, "黑名单号码修改成功", 1).show();
					}

					dialog.dismiss();
					
					List<String> blacknumbers = blackNumberDao.findAll();
					mAdapter.setBlacknumbers(blacknumbers);
					mAdapter.notifyDataSetChanged();//让listview自动刷新
				}
			}
			
			break;
		case R.id.bt_cancel_blacknumber_dialog:
			dialog.dismiss();
			break;
		default:
			break;
		}
	}
}

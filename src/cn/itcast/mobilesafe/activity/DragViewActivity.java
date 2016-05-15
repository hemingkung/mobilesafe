package cn.itcast.mobilesafe.activity;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import cn.itcast.mobilesafe.R;
import cn.itcast.mobilesafe.utils.Logger;

public class DragViewActivity extends Activity {

	
	private RelativeLayout rl_change_location;
	private TextView tv_info;
	//归属地显示的初始位置
	private int initX;
	private int initY;
	
	private int heightPixels;
	private int widthPixels;
	private SharedPreferences sp;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.drag_view);
		sp = getSharedPreferences("config", Context.MODE_PRIVATE);
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		widthPixels = dm.widthPixels;//屏幕宽
		heightPixels = dm.heightPixels;//屏幕高
		
		initX = widthPixels/2;
		initY = heightPixels/2;
		tv_info = (TextView) findViewById(R.id.tv_info);
		rl_change_location = (RelativeLayout) findViewById(R.id.rl_change_location);
		rl_change_location.setOnTouchListener(new MyOnTouchListener());
	}
	
	private final class MyOnTouchListener implements OnTouchListener{

		//按下的位置
		private int startX;
		private int startY;
		
		public boolean onTouch(View v, MotionEvent event) {
			// TODO Auto-generated method stub
			int action = event.getAction();
			switch (action) {
			case MotionEvent.ACTION_DOWN:
				Logger.i("i", " 按下");
				startX = (int) event.getRawX();
				startY = (int) event.getRawY();
				break;
			case MotionEvent.ACTION_MOVE:
				Logger.i("i", " 移动");
				int dx = (int) (event.getRawX() - startX);
				int dy = (int) (event.getRawY() - startY);
				
				int l = rl_change_location.getLeft() + dx;
				int t = rl_change_location.getTop() + dy;
				int r = rl_change_location.getRight() + dx;
				int b = rl_change_location.getBottom() + dy;
				
				if(l < 0 || r > widthPixels || t < 0 || b > heightPixels){
					//超出了范围，不移动
				}else{
					rl_change_location.layout(l, t, r, b);
				}
				
				
				int centerY = (rl_change_location.getTop() + rl_change_location.getBottom())/2;//归属地中心点的位置
				if(centerY > initY){//显示信息在上面
					tv_info.layout(tv_info.getLeft(), 20, tv_info.getRight(), tv_info.getHeight() + 20);
				}else{//显示信息在下面
					tv_info.layout(tv_info.getLeft(), heightPixels - tv_info.getHeight() -20, tv_info.getRight(), heightPixels - 20);
				}
				
				startX = (int) event.getRawX();
				startY = (int) event.getRawY();
				break;
			case MotionEvent.ACTION_UP:
				Logger.i("i", " 弹起");
				int endX = (rl_change_location.getLeft() + rl_change_location.getRight())/2;
				int endY = (rl_change_location.getTop() + rl_change_location.getBottom())/2;
				
				//得到了x轴和y轴的移动距离
				int x = endX - initX;
				int y = endY - initY;
				Editor editor = sp.edit();
				editor.putInt("x", x);
				editor.putInt("y", y);
				editor.commit();
				break;

			default:
				break;
			}
			return true;
		}
		
	}
}

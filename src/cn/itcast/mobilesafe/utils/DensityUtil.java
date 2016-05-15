package cn.itcast.mobilesafe.utils;

import android.content.Context;

public class DensityUtil {

	/**
	 * 把px 转化为dip
	 * @param context
	 * @param px
	 * @return
	 */
	public static int px2dip(Context context,float px){
		float density = context.getResources().getDisplayMetrics().density;//密度
		int dip = (int) (px * density);
		return dip;
	}
}

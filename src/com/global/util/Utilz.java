package com.global.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.content.Context;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Animation.AnimationListener;

public class Utilz {
	/**
	 * 获取指定样式的当前时间
	 * @param timeStyle
	 * @return
	 */
	public static String getCurrentFomatTime(String timeStyle){
		Calendar calendar = Calendar.getInstance();
    	long time = calendar.getTimeInMillis();
    	SimpleDateFormat simpleDateFormat = new SimpleDateFormat(timeStyle);
    	return simpleDateFormat.format(time);
	}
	
	public static void execAnim(Context context, final View radioGroup, int id,
			AnimationListener listener) {
		// radioGroup
		Animation in_bottom2bop = AnimationUtils.loadAnimation(context, id);
		in_bottom2bop.setAnimationListener(listener);
		radioGroup.startAnimation(in_bottom2bop);

	}
}

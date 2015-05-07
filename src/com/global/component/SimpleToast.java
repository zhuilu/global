package com.global.component;

import android.content.Context;
import android.view.Gravity;
import android.widget.Toast;

public class SimpleToast {
	
	/**
	 * 实例化Toast 
	 * @param context	上下文
	 * @param drawableId 图片资源
	 * @param tipSrc	文字提示id
	 * @param time	显示时间  Toast.Long/Toast.Short
	 * @return Toast
	 */
	public static Toast makeToast(Context context,int drawableId,int tipSrcId,int duration){
		CharSequence tipSrc = context.getResources().getText(tipSrcId);
		return makeToast(context, drawableId, tipSrc, duration);
	}
	
	/**
	 * 创建Toast
	 * @param context 上下文
	 * @param tipSrcId 提示信息
	 * @param duration	时间
	 * @return
	 */
	public static Toast makeToast(Context context,int tipSrcId,int duration){
		CharSequence tipSrc = context.getResources().getText(tipSrcId);
		return makeToast(context, 0, tipSrc, duration);
	}
	
    /**
     * Make a toast that just contains a image view and a text view.
     *
     * @param context  The context to use.  Usually your {@link android.app.Application}
     *                 or {@link android.app.Activity} object.
     * @param drawableId image resourceid
     * @param tipSrc     The text to show.  Can be formatted text.
     * @param duration How long to display the message.  Either {@link #LENGTH_SHORT} or
     *                 {@link #LENGTH_LONG}
     *
     */
	public static Toast makeToast(Context context,int drawableId,CharSequence tipSrc,int duration){
//		Toast toast = new Toast(context); 
		
		/*View toastView = LayoutInflater.from(context).inflate(R.layout.simple_toast, null);
		ImageView indexDrawable = (ImageView) toastView.findViewById(R.id.index_drawable);
		TextView tipContent = (TextView) toastView.findViewById(R.id.tip_content);
		
		if(drawableId == 0)
			indexDrawable.setVisibility(View.GONE);
		else
			indexDrawable.setBackgroundResource(drawableId);
			
		tipContent.setText(tipSrc);
		
		toast.setView(toastView);
		toast.setGravity(Gravity.CENTER,0,0);
		toast.setDuration(duration);*/
//		toast.setText(tipSrc);
		
		Toast toast = Toast.makeText(context, tipSrc, duration);
		toast.setGravity(Gravity.CENTER, 0, 0);
		return toast;
	}
}

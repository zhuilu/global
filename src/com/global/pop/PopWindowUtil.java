package com.global.pop;

import java.util.zip.Inflater;
import com.global.R;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager.LayoutParams;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.PopupWindow.OnDismissListener;

public class PopWindowUtil {

	/**
	 * 创建PopupWindow
	 */
	static PopupWindow popupWindow;

	public static void initPopuptWindow(final LayoutInflater inflater,
			final View view, Context context,
			final OnPopupWindowSelectListener listener) {
		// 获取自定义布局文件popupwindow_assitent.xml的视图
		ListView popupWindow_view = (ListView) inflater.inflate(
				R.layout.popupwindow_assitent, null, false);
		final ArrayAdapter adapter = new ArrayAdapter(context,
				R.layout.assist_simple_list_item, R.id.assist_list_item_text1,
				new String[] { "拍照", "相册", "运维宝典", "导航" });
		popupWindow_view.setAdapter(adapter);
		popupWindow = new PopupWindow(popupWindow_view, 200,
				LayoutParams.WRAP_CONTENT);
		popupWindow.setBackgroundDrawable(new BitmapDrawable());
		popupWindow.setOutsideTouchable(true);

		// 设置动画效果
		popupWindow.setAnimationStyle(R.style.AnimationFade);

		// 加了下面这行，onItemClick才好用
		popupWindow.setFocusable(true);
		popupWindow.setOnDismissListener(new OnDismissListener() {
			@Override
			public void onDismiss() {

			}
		});
		popupWindow_view.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if (popupWindow != null && popupWindow.isShowing()) {
					popupWindow.dismiss();
					popupWindow = null;
				}
				listener.onPopWindowSelect(parent, view, position, id);

			}
		});

		popupWindow.showAtLocation(view, Gravity.CENTER_VERTICAL
				| Gravity.RIGHT, 0, 0);
	}

	public interface OnPopupWindowSelectListener {

		public void onPopWindowSelect(AdapterView<?> parent, View view,
				int position, long id);
	}
}

package com.global.component;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.Button;

public class UIButton extends Button {
	Drawable backgroud_pressed;
	Drawable backgroud;

	public UIButton(Context context, AttributeSet attrs) {

		super(context, attrs);
		backgroud = getBackground();
		int id = attrs
				.getAttributeResourceValue(null, "background_pressed", -1);
		if (id != -1) {
			backgroud_pressed = getResources().getDrawable(id);
		} else {
			backgroud_pressed = backgroud;
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		int action = event.getAction();
		switch (action) {
		case MotionEvent.ACTION_DOWN:
			this.setBackgroundDrawable(backgroud_pressed);
			break;
		case MotionEvent.ACTION_MOVE:
			break;
		case MotionEvent.ACTION_UP:
			this.setBackgroundDrawable(backgroud);
			break;
		case MotionEvent.ACTION_CANCEL:
			break;
		}
		return super.onTouchEvent(event);
	}

}

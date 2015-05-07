package com.global.component;

import android.content.Context;
import android.graphics.Canvas;

import com.global.component.PullRefreshView.OverView;

public class PullOverView extends OverView {
	public PullOverView(Context context) {
		super(context);
		// (OverView) inflater.inflate(R.layout.refresh_loading, null);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
	}

	@Override
	public void init() {

	}

	@Override
	protected void onOpen() {

	}

	@Override
	public void onOver() {

	}

	@Override
	public void onLoad() {

	}

	@Override
	public void onFinish() {

	}
}

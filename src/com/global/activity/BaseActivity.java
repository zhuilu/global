package com.global.activity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.global.menu.MenuConstants;
import com.global.menu.MenuHelper;
import com.global.menu.MenuItem;
import com.global.menu.MenuView;
import com.global.menu.OnMenuItemClickListener;
import com.global.R;

import android.content.ComponentName;
import android.content.Context;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Handler.Callback;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.accessibility.AccessibilityEvent;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

public abstract class BaseActivity extends FragmentActivity implements Callback {
	protected LayoutInflater inflater;
	public Handler handler;
	MenuView menuView;
	ArrayList<MenuItem> lists;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		handler = new Handler(this);
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	public boolean dispatchKeyEvent(final KeyEvent event) {
		if (event.getKeyCode() == KeyEvent.KEYCODE_F11) {
			try {
				if (this.getClass().asSubclass(BaseActivity.class)
						.newInstance() != null
						&& ((this.getClass().asSubclass(BaseActivity.class))
								.newInstance()) instanceof OnRightButtonClick) {
					View rightView = ((OnRightButtonClick) this.getClass()
							.asSubclass(BaseActivity.class).newInstance())
							.onRightButtonClick(event.getAction());
					if (rightView != null) {
						long now = System.currentTimeMillis();
						MotionEvent touchEvent = MotionEvent.obtain(now, now,
								event.getAction(), rightView.getX(),
								rightView.getY(), 0);
						rightView.dispatchTouchEvent(touchEvent);
					}

				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return false;
		} else if (event.getKeyCode() == KeyEvent.KEYCODE_F12) {
			try {
				if (this.getClass().asSubclass(BaseActivity.class)
						.newInstance() != null
						&& ((this.getClass().asSubclass(BaseActivity.class))
								.newInstance()) instanceof OnLeftButtonClick) {
					View rightView = ((OnLeftButtonClick) this.getClass()
							.asSubclass(BaseActivity.class).newInstance())
							.onLeftButtonClick(event.getAction());
					if (rightView != null) {
						long now = System.currentTimeMillis();
						MotionEvent touchEvent = MotionEvent.obtain(now, now,
								event.getAction(), rightView.getX(),
								rightView.getY(), 0);

						rightView.dispatchTouchEvent(touchEvent);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return false;
		}

		return super.dispatchKeyEvent(event);
	}

	@Override
	protected void onPause() {
		super.onPause();
		// serial.close();
	}

	public void myClickHandler(View view) {
		switch (view.getId()) {
		case 1:
			break;
		default:
			break;
		}
	}

	protected void showMenu() {
		lists = MenuHelper.processMenu(MenuConstants.MENU);
		menuView = MenuHelper.constructMenu(this, lists);
		menuView.show();
		menuView.setOnMenuItemClickListenr(new OnMenuItemClickListener() {
			@Override
			public void onMenuItemClick(AdapterView<?> parent, View view,
					int position, int tag) {
				logout();
			}
		});
	}

	private void logout() {

	}

	public static ServiceConnection connection = new ServiceConnection() {
		public void onServiceDisconnected(ComponentName name) {
			Log.d("LOG", "Activity ->Disconnected the LocalService");
		}

		public void onServiceConnected(ComponentName name, IBinder service) {
			// 获取连接的服务对象
			System.out.println("Connection!!!");
			// InfraService iService = (InfraService) service;
			// System.out.println(iService.getName());
		}
	};

}

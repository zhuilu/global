package com.global.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.global.bean.Node;
import com.global.db.DbHelper;
import com.global.toast.Toaster;
import com.global.wheel.OnWheelChangedListener;
import com.global.wheel.WheelView;
import com.global.wheel.adapters.AbstractWheelTextAdapter;
import com.global.R;

/**
 * @author:daishulin@163.com @time:2014-06-04
 */

public class WheelActivity extends BaseActivity {
	private WheelView stepBig;
	private WheelView stepSmall;
	private final int fresh = 5;
	private static WheelAdapter adapter;
	private static WheelAdapter smallAdapter;
	private static TextView step_title;
	private static LinearLayout toplayout;
	private static FrameLayout bottomlayout;
	private static WheelActivity mActivity;
	private static boolean mFragmentOpen = false;
	private List<Node> mList;
	private Animation bottom_turn_large;
	private Animation top_turn_small;
	private Animation top_turn_large;
	private Animation bottom_turn_small;
	private static String framentTag = "framentTag";
	List<Node> smallItem = new ArrayList<Node>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.wheel_view);
		mActivity = this;
		mList = new ArrayList<Node>();
		bottom_turn_large = AnimationUtils.loadAnimation(this,
				R.anim.bottom_turn_large);
		top_turn_small = AnimationUtils.loadAnimation(this,
				R.anim.top_turn_small);
		top_turn_large = AnimationUtils.loadAnimation(this,
				R.anim.top_turn_large);
		bottom_turn_small = AnimationUtils.loadAnimation(this,
				R.anim.bottom_turn_small);
		SQLiteDatabase db = DbHelper.getInStance().getReadableDatabase();
		Cursor cursor = db.query(DbHelper.TABLE_NodeStep, null, null, null,
				null, null, null);
		while (cursor.moveToNext()) {
			Node node = new Node();
			node.CursorToObj(cursor);
			mList.add(node);
		}
		cursor.close();
		init();
	}

	@Override
	public void onBackPressed() {
		if (mFragmentOpen) {
			toDebugStep();
		} else {
			super.onBackPressed();
		}
	}

	private void init() {
		step_title = (TextView) findViewById(R.id.step_title);
		toplayout = (LinearLayout) findViewById(R.id.toplayout);
		bottomlayout = (FrameLayout) findViewById(R.id.bottomlayout);
		stepBig = (WheelView) findViewById(R.id.step_big_debug);
		stepSmall = (WheelView) findViewById(R.id.step_small_debug);

		List<Node> bigItem = new ArrayList<Node>();
		for (Node n : mList) {
			if (n.getNodePId() == null) {
				bigItem.add(n);
			}
		}
		adapter = new WheelAdapter(this, bigItem);
		stepBig.setViewAdapter(adapter, false);

		smallAdapter = new WheelAdapter(WheelActivity.this, smallItem);
		stepSmall.setViewAdapter(smallAdapter, true);
		stepSmall.addChangingListener(new OnWheelChangedListener() {
			@Override
			public void onChanged(WheelView wheel, int oldValue, int newValue) {
				System.out.println(smallItem.size() + " "
						+ stepSmall.getCurrentItem() + "  newValue=" + newValue);
				Node currentItem = smallItem.get(stepSmall.getCurrentItem());
				step_title.setText(currentItem.getNodeName());
			}
		});

		stepSmall.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// Toaster.getInstance().displayToast("click");
				Class cls = null;
				try {
					if (stepSmall.getCurrentItem() < 0
							|| smallItem.get(stepSmall.getCurrentItem()) == null) {
						return;
					}
					Node currentItem = smallItem.get(stepSmall.getCurrentItem());
					String fragmentStrShort = currentItem.getFragmentName();
					// String fragmentStrShort = "TextFragment";
					String fragmentStr = getPackageName() + ".fragment."
							+ fragmentStrShort;

					cls = Class.forName(fragmentStr);
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
					Toaster.getInstance().displayToast(
							R.string.err_debug_step_fragment);
					return;
				}
				FragmentTransaction transAction = getFragmentManager()
						.beginTransaction();
				try {
					transAction.replace(R.id.bottomlayout,
							(Fragment) cls.newInstance(), framentTag);
				} catch (Exception e) {
					e.printStackTrace();
					Toaster.getInstance().displayToast(
							R.string.err_debug_step_fragment);
					return;
				}
				transAction.commit();
				toFragment();
			}
		});

		stepBig.addChangingListener(new OnWheelChangedListener() {
			@Override
			public void onChanged(WheelView wheel, int oldValue, int newValue) {
				smallItem.clear();
				for (Node n : mList) {
					if (n.getNodePId() != null
							&& n.getNodePId().equals(
									adapter.getItem(newValue).getNodeId())) {
						smallItem.add(n);

					}
					// 刷新
					stepSmall.invalidateWheel(true);
				}

			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		return true;
	}

	private void addShortcutToDesktop() {
		Intent shortcut = new Intent(
				"com.android.launcher.action.INSTALL_SHORTCUT");
		// 不允许重建
		shortcut.putExtra("duplicate", false);
		// 设置名字
		shortcut.putExtra(Intent.EXTRA_SHORTCUT_NAME,
				this.getString(R.string.app_name));
		// 设置图标
		shortcut.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE,
				Intent.ShortcutIconResource.fromContext(this,
						R.drawable.ic_launcher));
		// 设置意图和快捷方式关联程序
		shortcut.putExtra(Intent.EXTRA_SHORTCUT_INTENT,
				new Intent(this, this.getClass()).setAction(Intent.ACTION_MAIN));
		// 发送广播
		sendBroadcast(shortcut);
	}

	private boolean hasInstallShortcut() {
		boolean hasInstall = false;
		final String AUTHORITY = "com.android.launcher2.settings";
		Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY
				+ "/favorites?notify=true");
		Cursor cursor = managedQuery(CONTENT_URI, new String[] { "title",
				"iconResource" }, "title=?",
				new String[] { this.getString(R.string.app_name) }, null);
		if (cursor != null && cursor.getCount() > 0) {
			hasInstall = true;
		}
		return hasInstall;
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	public void myClickHandler(View view) {
		switch (view.getId()) {

		default:
			super.myClickHandler(view);
		}

	}

	@Override
	public boolean handleMessage(Message msg) {
		switch (msg.what) {
		case fresh:

			break;
		default:

		}
		return false;
	}

	private class WheelAdapter extends AbstractWheelTextAdapter {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private List<Node> items;

		protected WheelAdapter(Context context, List<Node> items) {
			super(context, R.layout.cityset_item, NO_RESOURCE);
			this.items = items;
			setItemTextResource(R.id.item_name);
		}

		@Override
		public View getItem(int index, View cachedView, ViewGroup parent) {
			View view = super.getItem(index, cachedView, parent);
			return view;
		}

		public Node getItem(int index) {
			return items.get(index);
		}

		@Override
		public int getItemsCount() {
			return items.size();
		}

		@Override
		protected CharSequence getItemText(int index) {
			Node node = items.get(index);
			return node.getNodeName();
		}
	}

	public void toFragment() {
		toplayout.startAnimation(top_turn_small);
		bottomlayout.startAnimation(bottom_turn_large);
		toplayout.setFocusable(false);
		bottomlayout.setFocusable(true);
		toplayout.setVisibility(View.GONE);
		bottomlayout.setVisibility(View.VISIBLE);
		mFragmentOpen = true;

	}

	public void toDebugStep() {
		toplayout.startAnimation(top_turn_large);
		bottomlayout.startAnimation(bottom_turn_small);
		toplayout.setFocusable(true);
		toplayout.setVisibility(View.VISIBLE);
		bottomlayout.setVisibility(View.GONE);
		stepSmall.requestFocus();
		bottomlayout.setFocusable(false);
		mFragmentOpen = false;
	}

}

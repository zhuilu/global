package com.global.fragment;

import com.global.R;
import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.ActionBar.OnNavigationListener;
import android.app.Activity;
import android.app.FragmentManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.SpinnerAdapter;

@SuppressLint("HandlerLeak")
@SuppressWarnings("deprecation")
public class WaitDoFragemnt extends BaseFragment implements
		NavigationDrawerFragment.NavigationDrawerCallbacks {
	/**
	 * Fragment managing the behaviors, interactions and presentation of the
	 * navigation drawer.
	 */
	private NavigationDrawerFragment mNavigationDrawerFragment;
	/**
	 * Used to store the last screen title. For use in
	 */
	private CheckSimFragment fragment;
	private int selectedDropDownItem = -1;
	static View homeView;
	private MenuItem mItem;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.activity_wait_do, null);
		mNavigationDrawerFragment = (NavigationDrawerFragment) getFragmentManager()
				.findFragmentById(R.id.navigation_drawer);
		// Set up the drawer.
		// mNavigationDrawerFragment.setUp(R.id.navigation_drawer,
		// (DrawerLayout) view.findViewById(R.id.drawer_layout));
		// add fragment
		FragmentManager fragmentManager = getFragmentManager();
		fragment = new CheckSimFragment();
		fragmentManager.beginTransaction().add(R.id.container, fragment)
				.commit();
		return view;
	}

	class DropDownListenser implements OnNavigationListener {
		// 得到和SpinnerAdapter里一致的字符数组

		@Override
		public boolean onNavigationItemSelected(int itemPosition, long itemId) {

			if (selectedDropDownItem != itemPosition) {
				System.out.println("itemPosition=" + itemPosition);
				switch (itemPosition) {
				case 0:
					// yhlx = "03";
					break;
				case 1:
					// yhlx = "01";
					break;

				default:
					break;
				}
				mNavigationDrawerFragment.setDrawerListViewSelectedItem(0);
				if (fragment != null) {
					// fragment.yhlx = yhlx;
					// fragment.personalNotifyDataSetChanged(null);
				}
			}
			selectedDropDownItem = itemPosition;
			return true;
		}
	}

	private int selectedNavigationDrawerItem = -1;

	@Override
	public void onNavigationDrawerItemSelected(int position) {
		if (selectedNavigationDrawerItem != position) {
			if (fragment != null) {
				System.out.println("position=" + position);
				String status = null;
				switch (position) {
				case 0:
					status = null;
					break;

				default:
					break;
				}
				// mTitle = status;
				// fragment.personalNotifyDataSetChanged(status);
			}
		}
		selectedNavigationDrawerItem = position;

	}

	public Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			fragment.msgHandler.sendEmptyMessage(msg.what);
			switch (msg.what) {
			case requestSuccess:
				mItem.setActionView(null);
				break;
			case requestError:
				mItem.setActionView(null);
				break;

			default:
				break;
			}
		};
	};

}

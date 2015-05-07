/*
 * Copyright 2013 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use getActivity() file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.global.fragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.SearchManager;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.global.R;
import com.global.adapter.ExpandAdapter;
import com.global.bean.ChildBean;
import com.global.bean.ExpandBean;
import com.global.classR.ClassUtil;

/**
 * getActivity() example illustrates a common usage of the DrawerLayout widget
 * in the Android support library.
 * <p/>
 * <p>
 * When a navigation (left) drawer is present, the host activity should detect
 * presses of the action bar's Up affordance as a signal to open and close the
 * navigation drawer. The ActionBarDrawerToggle facilitates getActivity()
 * behavior. Items within the drawer should fall into one of two categories:
 * </p>
 * <p/>
 * <ul>
 * <li><strong>View switches</strong>. A view switch follows the same basic
 * policies as list or tab navigation in that a view switch does not create
 * navigation history. getActivity() pattern should only be used at the root
 * activity of a task, leaving some form of Up navigation active for activities
 * further down the navigation hierarchy.</li>
 * <li><strong>Selective Up</strong>. The drawer allows the user to choose an
 * alternate parent for Up navigation. getActivity() allows a user to jump
 * across an app's navigation hierarchy at will. The application should treat
 * getActivity() as it treats Up navigation from a different task, replacing the
 * current task stack using TaskStackBuilder or similar. getActivity() is the
 * only form of navigation drawer that should be used outside of the root
 * activity of a task.</li>
 * </ul>
 * <p/>
 * <p>
 * Right side drawers should be used for actions, not navigation. getActivity()
 * follows the pattern established by the Action Bar that navigation should be
 * to the left and actions to the right. An action should be an operation
 * performed on the current contents of the window, for example enabling or
 * disabling a data overlay on top of the current content.
 * </p>
 */
public class NativeMenuFragment extends BaseFragment implements
		OnChildClickListener {
	private DrawerLayout mDrawerLayout;
	private ExpandableListView mDrawerList;
	private ActionBarDrawerToggle mDrawerToggle;
	List<ExpandBean> data = new ArrayList<ExpandBean>();
	private CharSequence mDrawerTitle;
	private CharSequence mTitle = "";
	private String[] mPlanetTitles;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.drawablemenu, null);
		mPlanetTitles = getResources().getStringArray(R.array.planets_array);
		mDrawerLayout = (DrawerLayout) view.findViewById(R.id.drawer_layout);
		mDrawerList = (ExpandableListView) view.findViewById(R.id.left_drawer);

		// set a custom shadow that overlays the main content when the drawer
		// opens
		mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow,
				GravityCompat.START);
		// set up the drawer's list view with items and click listener

		data.add(new ExpandBean("功能区", new ChildBean("SIM卡测试",
				"CheckSimFragment"),
				new ChildBean("集中器测试", "CheckJZQFragment"), new ChildBean(
						"采集器测试", "CheckCJQFragment")));
		data.add(new ExpandBean("终端维护", new ChildBean("硬件重启",
				"CheckRebootFragment"), new ChildBean("读取软件版本号",
				"CheckReadVersionFragment"), new ChildBean("终端地址读取",
				"CheckReadAddressFragment"), new ChildBean("终端地址设置",
				"CheckSetAddressFragment"), new ChildBean("电量读取",
				"CheckReadPowerFragment"), new ChildBean("主程序升级",
				"CheckSimFragment")));
		mDrawerList.setAdapter(new ExpandAdapter(getActivity(), data));
		mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
		mDrawerList.setOnChildClickListener(this);
		// enable ActionBar app icon to behave as action to toggle nav drawer
		getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);
		getActivity().getActionBar().setHomeButtonEnabled(true);

		// ActionBarDrawerToggle ties together the the proper interactions
		// between the sliding drawer and the action bar app icon
		mDrawerToggle = new ActionBarDrawerToggle(getActivity(), /* host Activity */
		mDrawerLayout, /* DrawerLayout object */
		R.drawable.ic_drawer, /* nav drawer image to replace 'Up' caret */
		R.string.drawer_open, /* "open drawer" description for accessibility */
		R.string.drawer_close /* "close drawer" description for accessibility */
		) {
			public void onDrawerClosed(View view) {
				getActivity().getActionBar().setTitle(mTitle);
				getActivity().invalidateOptionsMenu(); // creates call to
				// onPrepareOptionsMenu()
			}

			public void onDrawerOpened(View drawerView) {
				getActivity().getActionBar().setTitle(mDrawerTitle);
				getActivity().invalidateOptionsMenu(); // creates call to
				// onPrepareOptionsMenu()
			}
		};
		mDrawerLayout.setDrawerListener(mDrawerToggle);
		mDrawerToggle.syncState();
		if (savedInstanceState == null) {
			selectItem(0);
		}
		return view;
	}

	// @Override
	// public boolean onCreateOptionsMenu(Menu menu) {
	// MenuInflater inflater = getMenuInflater();
	// inflater.inflate(R.menu.main, menu);
	// return super.onCreateOptionsMenu(menu);
	// }

	/* Called whenever we call invalidateOptionsMenu() */
	// @Override
	// public boolean onPrepareOptionsMenu(Menu menu) {
	// // If the nav drawer is open, hide action items related to the content
	// // view
	// boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
	// // menu.findItem(R.id.action_websearch).setVisible(!drawerOpen);
	// return super.onPrepareOptionsMenu(menu);
	// }

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// The action bar home/up action should open or close the drawer.
		// ActionBarDrawerToggle will take care of getActivity().
		if (mDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		}
		// Handle action buttons
		switch (item.getItemId()) {
		case 1:
			// // create intent to perform web search for getActivity() planet
			// Intent intent = new Intent(Intent.ACTION_WEB_SEARCH);
			// intent.putExtra(SearchManager.QUERY, getActionBar().getTitle());
			// // catch event that there's no activity to handle intent
			// if (intent.resolveActivity(getPackageManager()) != null) {
			// startActivity(intent);
			// } else {
			// Toast.makeText(getActivity(), R.string.app_not_available,
			// Toast.LENGTH_LONG).show();
			return true;

		default:
			return super.onOptionsItemSelected(item);
		}
	}

	/* The click listner for ListView in the navigation drawer */
	private class DrawerItemClickListener implements
			ListView.OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			selectItem(position);
		}
	}

	private void selectItem(int position) {
		// update the main content by replacing fragments
		Fragment fragment = new CheckSimFragment();
		Bundle args = new Bundle();
		args.putInt(PlanetFragment.ARG_PLANET_NUMBER, position);
		fragment.setArguments(args);

		FragmentManager fragmentManager = getFragmentManager();
		fragmentManager.beginTransaction()
				.replace(R.id.content_frame, fragment).commit();

		// update selected item and title, then close the drawer
		mDrawerList.setItemChecked(position, true);
		// setTitle(mPlanetTitles[position]);
		mDrawerLayout.closeDrawer(mDrawerList);
	}

	// @Override
	// public void setTitle(CharSequence title) {
	// mTitle = title;
	// getActionBar().setTitle(mTitle);
	// }

	/**
	 * When using the ActionBarDrawerToggle, you must call it during
	 * onPostCreate() and onConfigurationChanged()...
	 */

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		// Pass any configuration change to the drawer toggls
		mDrawerToggle.onConfigurationChanged(newConfig);
	}

	/**
	 * Fragment that appears in the "content_frame", shows a planet
	 */
	public static class PlanetFragment extends Fragment {
		public static final String ARG_PLANET_NUMBER = "planet_number";

		public PlanetFragment() {
			// Empty constructor required for fragment subclasses
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_planet,
					container, false);
			int i = getArguments().getInt(ARG_PLANET_NUMBER);
			String planet = getResources()
					.getStringArray(R.array.planets_array)[i];

			int imageId = getResources().getIdentifier(
					planet.toLowerCase(Locale.getDefault()), "drawable",
					getActivity().getPackageName());
			((ImageView) rootView.findViewById(R.id.image))
					.setImageResource(imageId);
			getActivity().setTitle(planet);
			return rootView;
		}
	}

	@Override
	public boolean onChildClick(ExpandableListView parent, View v,
			int groupPosition, int childPosition, long id) {
		selectChildItem(groupPosition, childPosition);

		return false;
	}

	public void selectChildItem(int groupPosition, int childPosition) {
		ClassUtil.jumpFragment(getActivity(), getFragmentManager(),
				R.id.content_frame,
				data.get(groupPosition).getChildren()[childPosition]
						.getChildFragment());
		// setTitle(mPlanetTitles[position]);
		mDrawerLayout.closeDrawer(mDrawerList);

	}
}
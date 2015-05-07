/*
 * Copyright (C) 2013 Sergej Shafarenka, halfbit.de
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use getActivity() file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.global.fragment;

import java.util.Locale;

import com.global.R;
import com.global.component.PinnedSectionListView;
import com.global.component.PinnedSectionListView.PinnedSectionListAdapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SectionIndexer;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("NewApi")
public class PinnedSectionListFragment extends BaseFragment implements
		OnClickListener, OnItemClickListener {
	PinnedSectionListView mList;
	private boolean hasHeaderAndFooter = true;
	private boolean isFastScroll;
	private boolean addPadding;
	private boolean isShadowVisible = true;
	private int mDatasetUpdateCount;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.simple_list, null);
		mList = (PinnedSectionListView) view.findViewById(R.id.list);
		mList.setOnItemClickListener(this);
		initializeHeaderAndFooter();
		initializeAdapter();
		initializePadding();
		return view;
	}

	@SuppressLint("NewApi")
	private void initializeAdapter() {
		mList.setFastScrollEnabled(isFastScroll);
		if (isFastScroll) {
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
				mList.setFastScrollAlwaysVisible(true);
			}
			mList.setAdapter(new FastScrollAdapter(getActivity(),
					android.R.layout.simple_list_item_1, android.R.id.text1));
		} else {
			mList.setAdapter(new SimpleAdapter(getActivity(),
					android.R.layout.simple_list_item_1, android.R.id.text1));
		}
	}

	static class SimpleAdapter extends ArrayAdapter<Item> implements
			PinnedSectionListAdapter {

		private static final int[] COLORS = new int[] { R.color.green_light,
				R.color.orange_light, R.color.blue_light, R.color.red_light };

		public SimpleAdapter(Context context, int resource,
				int textViewResourceId) {
			super(context, resource, textViewResourceId);
			generateDataset('A', 'Z', false);
		}

		public void generateDataset(char from, char to, boolean clear) {

			if (clear)
				clear();

			final int sectionsNumber = to - from + 1;
			prepareSections(sectionsNumber);

			int sectionPosition = 0, listPosition = 0;
			for (char i = 0; i < sectionsNumber; i++) {
				Item section = new Item(Item.SECTION,
						String.valueOf((char) ('A' + i)));
				section.sectionPosition = sectionPosition;
				section.listPosition = listPosition++;
				onSectionAdded(section, sectionPosition);
				add(section);

				final int itemsNumber = (int) Math.abs((Math.cos(2f * Math.PI
						/ 3f * sectionsNumber / (i + 1f)) * 25f));
				for (int j = 0; j < itemsNumber; j++) {
					Item item = new Item(Item.ITEM,
							section.text.toUpperCase(Locale.ENGLISH) + " - "
									+ j);
					item.sectionPosition = sectionPosition;
					item.listPosition = listPosition++;
					add(item);
				}

				sectionPosition++;
			}
		}

		protected void prepareSections(int sectionsNumber) {
		}

		protected void onSectionAdded(Item section, int sectionPosition) {
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			TextView view = (TextView) super.getView(position, convertView,
					parent);
			view.setTextColor(Color.DKGRAY);
			view.setTag("" + position);
			Item item = getItem(position);
			if (item.type == Item.SECTION) {
				// view.setOnClickListener(PinnedSectionListActivity.getActivity());
				view.setBackgroundColor(parent.getResources().getColor(
						COLORS[item.sectionPosition % COLORS.length]));
			}
			return view;
		}

		@Override
		public int getViewTypeCount() {
			return 2;
		}

		@Override
		public int getItemViewType(int position) {
			return getItem(position).type;
		}

		@Override
		public boolean isItemViewTypePinned(int viewType) {
			return viewType == Item.SECTION;
		}

	}

	static class FastScrollAdapter extends SimpleAdapter implements
			SectionIndexer {

		private Item[] sections;

		public FastScrollAdapter(Context context, int resource,
				int textViewResourceId) {
			super(context, resource, textViewResourceId);
		}

		@Override
		protected void prepareSections(int sectionsNumber) {
			sections = new Item[sectionsNumber];
		}

		@Override
		protected void onSectionAdded(Item section, int sectionPosition) {
			sections[sectionPosition] = section;
		}

		@Override
		public Item[] getSections() {
			return sections;
		}

		@Override
		public int getPositionForSection(int section) {
			if (section >= sections.length) {
				section = sections.length - 1;
			}
			return sections[section].listPosition;
		}

		@Override
		public int getSectionForPosition(int position) {
			if (position >= getCount()) {
				position = getCount() - 1;
			}
			return getItem(position).sectionPosition;
		}

	}

	static class Item {
		public static final int ITEM = 0;
		public static final int SECTION = 1;

		public final int type;
		public final String text;

		public int sectionPosition;
		public int listPosition;

		public Item(int type, String text) {
			this.type = type;
			this.text = text;
		}

		@Override
		public String toString() {
			return text;
		}

	}

	// @Override
	// public boolean onCreateOptionsMenu(Menu menu) {
	// getMenuInflater().inflate(R.menu.main, menu);
	// menu.getItem(0).setChecked(isFastScroll);
	// menu.getItem(1).setChecked(addPadding);
	// menu.getItem(2).setChecked(isShadowVisible);
	// return true;
	// }

	// @Override
	// public boolean onOptionsItemSelected(MenuItem item) {
	// switch (item.getItemId()) {
	// case R.id.action_fastscroll:
	// isFastScroll = !isFastScroll;
	// item.setChecked(isFastScroll);
	// initializeAdapter();
	// break;
	// case R.id.action_addpadding:
	// addPadding = !addPadding;
	// item.setChecked(addPadding);
	// initializePadding();
	// break;
	// case R.id.action_showShadow:
	// isShadowVisible = !isShadowVisible;
	// item.setChecked(isShadowVisible);
	// ((PinnedSectionListView) mList).setShadowVisible(isShadowVisible);
	// break;
	// case R.id.action_showHeaderAndFooter:
	// hasHeaderAndFooter = !hasHeaderAndFooter;
	// item.setChecked(hasHeaderAndFooter);
	// initializeHeaderAndFooter();
	// break;
	// case R.id.action_updateDataset:
	// updateDataset();
	// break;
	// }
	// return true;
	// }

	private void updateDataset() {
		mDatasetUpdateCount++;
		SimpleAdapter adapter = (SimpleAdapter) mList.getAdapter();
		switch (mDatasetUpdateCount % 4) {
		case 0:
			adapter.generateDataset('A', 'B', true);
			break;
		case 1:
			adapter.generateDataset('C', 'M', true);
			break;
		case 2:
			adapter.generateDataset('P', 'Z', true);
			break;
		case 3:
			adapter.generateDataset('A', 'Z', true);
			break;
		}
		adapter.notifyDataSetChanged();
	}

	private void initializePadding() {
		float density = getResources().getDisplayMetrics().density;
		int padding = addPadding ? (int) (16 * density) : 0;
		mList.setPadding(padding, padding, padding, padding);
	}

	private void initializeHeaderAndFooter() {
		mList.setAdapter(null);
		if (hasHeaderAndFooter) {
			ListView list = mList;
			LayoutInflater inflater = LayoutInflater.from(getActivity());
			TextView header1 = (TextView) inflater.inflate(
					android.R.layout.simple_list_item_1, list, false);
			header1.setText("First header");
			list.addHeaderView(header1);

			TextView header2 = (TextView) inflater.inflate(
					android.R.layout.simple_list_item_1, list, false);
			header2.setText("Second header");
			list.addHeaderView(header2);

			TextView footer = (TextView) inflater.inflate(
					android.R.layout.simple_list_item_1, list, false);
			footer.setText("Single footer");
			list.addFooterView(footer);
		}
		initializeAdapter();
	}

	@Override
	public void onClick(View v) {
		Toast.makeText(getActivity(), "Item: " + v.getTag(), Toast.LENGTH_SHORT)
				.show();
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		Item item = (Item) mList.getAdapter().getItem(position);
		if (item != null) {
			Toast.makeText(getActivity(),
					"Item " + position + ": " + item.text, Toast.LENGTH_SHORT)
					.show();
		} else {
			Toast.makeText(getActivity(), "Item " + position,
					Toast.LENGTH_SHORT).show();
		}

	}

}
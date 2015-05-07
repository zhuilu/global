package com.global.adapter;

import java.util.List;
import java.util.Map;
import com.global.R;
import com.global.bean.ChildBean;
import com.global.bean.ExpandBean;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ExpandAdapter extends BaseExpandableListAdapter {

	private Context mContext;
	private LayoutInflater mInflater = null;
	private List<ExpandBean> mData = null;

	public ExpandAdapter(Context ctx, List<ExpandBean> list) {
		mContext = ctx;
		mInflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mData = list;
	}

	public void setData(List<ExpandBean> map) {
		mData = map;
	}

	@Override
	public int getGroupCount() {
		System.out.println("getGroupCount=" + mData.size());
		return mData.size();
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		return mData.get(groupPosition).getChildren().length;
	}

	@Override
	public ExpandBean getGroup(int groupPosition) {
		return mData.get(groupPosition);
	}

	@Override
	public ChildBean getChild(int groupPosition, int childPosition) {
		return mData.get(groupPosition).getChildren()[childPosition];
	}

	@Override
	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	@Override
	public boolean hasStableIds() {
		return false;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		GroupViewHolder holder;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.group_item, null);
			holder = new GroupViewHolder();
			holder.mGroupName = (TextView) convertView
					.findViewById(R.id.textView1);
			convertView.setTag(holder);

		}

		holder = (GroupViewHolder) convertView.getTag();
		holder.mGroupName.setText(mData.get(groupPosition).groupName);

		return convertView;

	}

	@Override
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		ItemViewHolder itmeHolder;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.child_item, null);
			itmeHolder = new ItemViewHolder();
			itmeHolder.mItmeName = (TextView) convertView
					.findViewById(R.id.textView1);
			convertView.setTag(itmeHolder);

		}

		itmeHolder = (ItemViewHolder) convertView.getTag();
		itmeHolder.mItmeName
				.setText(mData.get(groupPosition).getChildren()[childPosition]
						.getChildName());

		return convertView;
	}

	private class GroupViewHolder {
		TextView mGroupName;
	}

	private class ItemViewHolder {
		TextView mItmeName;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		/* 很重要：实现ChildView点击事件，必须返回true */
		return true;
	}

}

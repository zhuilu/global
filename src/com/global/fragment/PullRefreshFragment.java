package com.global.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ListView;
import com.global.R;
import com.global.toast.Toaster;
import com.global.component.DefaultPullRefreshOverView;
import com.global.component.PullRefreshView;
import com.global.component.PullRefreshView.OverView;

public class PullRefreshFragment extends BaseFragment implements
		PullRefreshView.RefreshListener, OnClickListener {
	public ListView listView;
	PullRefreshView mRefreshListContainer;
	Handler handler = new Handler() {
		@Override
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 1:
				onRefreshEnd();
				break;
			}

		};
	};

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.unservery_list, null);
		mRefreshListContainer = (PullRefreshView) view
				.findViewById(R.id.todoListContainer);
		listView = (ListView) view.findViewById(R.id.common_listviews);
		mRefreshListContainer.setRefreshListener(this);
		return view;
	}

	@Override
	public void onStartRefresh() {
		// do something
		Toaster.getInstance().displayToast("开始刷新");
		handler.sendEmptyMessageDelayed(1, 3000);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {

		}

	}

	@Override
	public OverView getOverView() {
		return (DefaultPullRefreshOverView) inflater.inflate(
				R.layout.framework_pullrefresh_overview, null);
	}

	@Override
	public void onRefreshEnd() {
		Toaster.getInstance().displayToast("刷新成功");
		mRefreshListContainer.refreshFinished();
		mRefreshListContainer.setEnablePull(true);
	}
}

package com.global.fragment;

import com.global.R;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class ReadScreenFragment extends BaseFragment {
	public ReadScreenFragment() {
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.readscreen, null);
		TextView text = (TextView) view.findViewById(R.id.textView1);
		DisplayMetrics mDisplayMetrics = new DisplayMetrics();

		getActivity().getWindowManager().getDefaultDisplay()
				.getMetrics(mDisplayMetrics);
		int W = mDisplayMetrics.widthPixels;
		int H = mDisplayMetrics.heightPixels;
		text.setText("Height = " + H + " Width = " + W);
		return view;
	}
}

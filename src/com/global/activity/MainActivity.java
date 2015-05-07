package com.global.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.TextView;

import com.global.R.id;
import com.global.R.layout;
import com.global.R.menu;
import com.global.pop.PopWindowUtil;
import com.global.pop.PopWindowUtil.OnPopupWindowSelectListener;
import com.global.R;

public class MainActivity extends Activity {
	TextView textView1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		textView1 = (TextView) findViewById(R.id.textView1);
		textView1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	protected void onResume() {
		super.onResume();

	}
}

package com.global.component;

import com.global.alert.AlertWaitService;
import com.global.app.SysApplicationImpl;

import java.io.InputStreamReader;

import java.io.BufferedReader;

import java.io.StringBufferInputStream;

import java.net.MalformedURLException;

import java.io.InputStream;

import java.net.URLConnection;

import android.graphics.Bitmap;

import java.net.URL;

import com.global.log.CLog;

import android.content.Context;
import android.util.AttributeSet;
import android.webkit.WebView;
import android.widget.ProgressBar;

@SuppressWarnings("deprecation")
public class ProgressWebView extends WebView {

	private ProgressBar progressbar;
	private boolean flag = false;
	Context mContext;

	public ProgressWebView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
		progressbar = new ProgressBar(context, null,
				android.R.attr.progressBarStyleHorizontal);
		progressbar.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,
				10, 0, 0));
		addView(progressbar);
		setWebChromeClient(new WebChromeClient());

	}

	public class WebChromeClient extends android.webkit.WebChromeClient {

		@Override
		public void onProgressChanged(WebView view, int newProgress) {

			CLog.i("onProgressChanged=" + newProgress);
			if (newProgress == 100) {
				progressbar.setVisibility(GONE);
			} else {
				if (progressbar.getVisibility() == GONE)
					progressbar.setVisibility(VISIBLE);
				progressbar.setProgress(newProgress);
			}
			super.onProgressChanged(view, newProgress);
		}

	}

	@Override
	protected void onScrollChanged(int l, int t, int oldl, int oldt) {
		LayoutParams lp = (LayoutParams) progressbar.getLayoutParams();
		lp.x = l;
		lp.y = t;
		progressbar.setLayoutParams(lp);
		super.onScrollChanged(l, t, oldl, oldt);
	}

}
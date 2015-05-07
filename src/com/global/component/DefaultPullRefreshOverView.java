package com.global.component;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.global.component.PullRefreshView.OverView;
import com.global.util.Utilz;
import com.global.R;

/**
 * 默认下拉刷新View的OverView
 * 
 * @author daishulin@163.com
 * 
 */
public class DefaultPullRefreshOverView extends OverView {

	private View mNormalView;
	private View mLoadingView;

	private ProgressBar mProgressBar;

	private TextView mPrompt;
	private ImageView mIndicator;

	private TextView after_pullrefresh_time;
	private TextView before_pullrefresh_time;
	private String refreshTime;

	// private int mTextColor;
	private Drawable mIndicatorUpDrawable;
	private Drawable mIndicatorDownDrawable;
	private Drawable mProgressDrawable;

	private Animation mOpenAnimation;
	private Animation mCloseAnimation;
	private AnimationListener mAnimationListener;
	private final static String mOverViewTimeSP = "overViewSP";
	private String mKey = "timeKey";

	public DefaultPullRefreshOverView(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
		initAttrs(context, attrs, defStyle);
	}

	public DefaultPullRefreshOverView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initAttrs(context, attrs, 0);
	}

	public DefaultPullRefreshOverView(Context context) {
		super(context);
	}

	private void initAttrs(Context context, AttributeSet attrs, int defStyle) {
		TypedArray a = context.obtainStyledAttributes(attrs,
				R.styleable.framework_pullrefresh_overview, defStyle,
				R.style.framework_pullrefresh_overview);
		mIndicatorUpDrawable = a
				.getDrawable(R.styleable.framework_pullrefresh_overview_framework_pullrefresh_indicator_upDrawable);
		mIndicatorDownDrawable = a
				.getDrawable(R.styleable.framework_pullrefresh_overview_framework_pullrefresh_indicator_downDrawable);
		// mTextColor = R.color.colorBlack;
		mProgressDrawable = a
				.getDrawable(R.styleable.framework_pullrefresh_overview_framework_pullrefresh_progressDrawable);
		a.recycle();
	}

	@Override
	public void init() {
		mAnimationListener = new AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {

			}

			@Override
			public void onAnimationRepeat(Animation animation) {

			}

			@Override
			public void onAnimationEnd(Animation animation) {
				if (animation == mCloseAnimation) {
					if (mIndicatorUpDrawable != null) {
						mIndicator.setImageDrawable(mIndicatorUpDrawable);
					}
				} else {
					if (mIndicatorDownDrawable != null) {
						mIndicator.setImageDrawable(mIndicatorDownDrawable);
						mIndicatorDownDrawable.setLevel(10000);
					}
				}
			}
		};
		// Load all of the animations we need in code rather than through XML
		mOpenAnimation = new RotateAnimation(0, -180,
				RotateAnimation.RELATIVE_TO_SELF, 0.5f,
				RotateAnimation.RELATIVE_TO_SELF, 0.5f);
		mOpenAnimation.setInterpolator(new LinearInterpolator());
		mOpenAnimation.setDuration(250);
		mOpenAnimation.setAnimationListener(mAnimationListener);
		mCloseAnimation = new RotateAnimation(0, 180,
				RotateAnimation.RELATIVE_TO_SELF, 0.5f,
				RotateAnimation.RELATIVE_TO_SELF, 0.5f);
		mCloseAnimation.setInterpolator(new LinearInterpolator());
		mCloseAnimation.setDuration(250);
		mCloseAnimation.setAnimationListener(mAnimationListener);
	}

	/**
	 * 记录每次下拉刷新的时间
	 */
	protected void recordRefreshTime() {
		if (mKey == null) {
			throw new RuntimeException(
					"must set key for logging refresh time,you can set it in getOverView() in your UI");
		}
		refreshTime = Utilz.getCurrentFomatTime("yyyy-MM-dd HH:mm:ss");
		// String key = getId()+"";
		SharedPreferences preferences = getContext().getSharedPreferences(
				mOverViewTimeSP, Context.MODE_PRIVATE);
		preferences.edit().putString(mKey, refreshTime).commit();
	}

	private void setRefreshTime(String refreshTime) {
		if (refreshTime == null || refreshTime.equalsIgnoreCase("")) {
			after_pullrefresh_time.setVisibility(View.INVISIBLE);
			before_pullrefresh_time.setVisibility(View.INVISIBLE);
		} else {
			after_pullrefresh_time.setVisibility(View.VISIBLE);
			before_pullrefresh_time.setVisibility(View.VISIBLE);
			after_pullrefresh_time.setText(String.format(getResources()
					.getString(R.string.time_update), refreshTime));
			before_pullrefresh_time.setText(String.format(getResources()
					.getString(R.string.time_update), refreshTime));
		}
	}

	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		if (getId() == NO_ID) {
			throw new RuntimeException("must set id");
		}

		mNormalView = findViewById(R.id.framework_pullrefresh_normal);
		mLoadingView = findViewById(R.id.framework_pullrefresh_loading);
		mProgressBar = (ProgressBar) findViewById(R.id.framework_pullrefresh_progress);
		mPrompt = (TextView) findViewById(R.id.framework_pullrefresh_prompt);
		mIndicator = (ImageView) findViewById(R.id.framework_pullrefresh_indicator);

		after_pullrefresh_time = (TextView) findViewById(R.id.after_pullrefresh_time);
		before_pullrefresh_time = (TextView) findViewById(R.id.before_pullrefresh_time);
		// after_pullrefresh_time.setText(String.format(getResources().getString(R.string.time_update),
		// refreshTime));
		// before_pullrefresh_time.setText(String.format(getResources().getString(R.string.time_update),
		// refreshTime));
		if (mIndicatorUpDrawable != null) {
			mIndicator.setImageDrawable(mIndicatorUpDrawable);
		}
		if (mProgressDrawable != null) {
			mProgressBar.setIndeterminateDrawable(mProgressDrawable);
		}

		mPrompt.setTextColor(Color.parseColor("#333333"));
	}

	/**
	 * 为下拉刷新控件设置记录本地时间key
	 * 
	 * @param key
	 *            时间的key
	 */
	public void setTimeKey(String key) {
		mKey = key;
		SharedPreferences preferences = getContext().getSharedPreferences(
				mOverViewTimeSP, Context.MODE_PRIVATE);
		refreshTime = preferences.getString(mKey, "");
		setRefreshTime(refreshTime);
	}

	@Override
	public void onOpen() {
		mIndicator.clearAnimation();
		mPrompt.setText(R.string.framework_pull_refresh);
		mIndicator.startAnimation(mOpenAnimation);
		setRefreshTime(refreshTime);
	}

	@Override
	public void onOver() {
		mIndicator.clearAnimation();
		mPrompt.setText(R.string.framework_release_refresh);
		mIndicator.startAnimation(mCloseAnimation);
		setRefreshTime(refreshTime);
	}

	@Override
	public void onLoad() {
		mNormalView.setVisibility(GONE);
		mLoadingView.setVisibility(VISIBLE);
	}

	@Override
	public void onFinish() {
		mPrompt.setText(R.string.framework_refresh_loading);
		mNormalView.setVisibility(VISIBLE);
		mLoadingView.setVisibility(GONE);
		recordRefreshTime();
	}

	public View getNormalView() {
		return mNormalView;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		return super.onTouchEvent(event);
	}
}

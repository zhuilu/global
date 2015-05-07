package com.global.component;

import com.global.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.Scroller;

/**
 * 下拉刷新View
 * 
 * @author daishulin@163.com
 * 
 */
public class PullRefreshView extends FrameLayout implements OnGestureListener {
	private static final byte STATE_CLOSE = 0;
	private static final byte STATE_OPEN = STATE_CLOSE + 1;
	private static final byte STATE_OVER = STATE_OPEN + 1;
	private static final byte STATE_OPEN_RELEASE = STATE_OVER + 1;
	private static final byte STATE_OVER_RELEASE = STATE_OPEN_RELEASE + 1;
	private static final byte STATE_REFRESH = STATE_OVER_RELEASE + 1;
	private static final byte STATE_REFRESH_RELEASE = STATE_REFRESH + 1;
	private byte mState;

	private GestureDetector mGestureDetector;
	private Flinger mFlinger;

	private RefreshListener mRefreshListener;
	/**
	 * Overlay视图
	 */
	protected OverView mOverView;

	private int mLastY;
	// private int mMargin;
	protected int mMaxMagin;

	private boolean mEnablePull = true;

	public PullRefreshView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public PullRefreshView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	public PullRefreshView(Context context) {
		super(context);
		init();
	}

	@SuppressWarnings("deprecation")
	private void init() {
		mGestureDetector = new GestureDetector(this);
		mFlinger = new Flinger();
	}

	@SuppressWarnings("deprecation")
	private void initListener() {
		mOverView = mRefreshListener.getOverView();
		FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
				FrameLayout.LayoutParams.FILL_PARENT,
				FrameLayout.LayoutParams.WRAP_CONTENT);
		addView(mOverView, 0, params);

		getViewTreeObserver().addOnGlobalLayoutListener(
				new ViewTreeObserver.OnGlobalLayoutListener() {
					@Override
					public void onGlobalLayout() {
						int height = mOverView.findViewById(
								R.id.framework_pullrefresh_loading)
								.getMeasuredHeight();
						mMaxMagin = height;

						getViewTreeObserver()
								.removeGlobalOnLayoutListener(this);
					}
				});
	}

	@Override
	public boolean onDown(MotionEvent evn) {
		return false;
	}

	@Override
	public boolean onFling(MotionEvent arg0, MotionEvent arg1, float arg2,
			float arg3) {
		return false;
	}

	@Override
	public void onLongPress(MotionEvent arg0) {

	}

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float disX,
			float disY) {
		View head = getChildAt(0);
		View child = getChildAt(1);
		if (child instanceof AdapterView<?>) {
			if (((AdapterView<?>) child).getFirstVisiblePosition() != 0
					|| (((AdapterView<?>) child).getFirstVisiblePosition() == 0
							&& ((AdapterView<?>) child).getChildAt(0) != null && ((AdapterView<?>) child)
							.getChildAt(0).getTop() < 0))
				return false;
		}
		if ((mState == STATE_REFRESH && head.getTop() > 0 && disY > 0)
				|| (child.getTop() <= 0 && disY > 0)) {
			return false;
		}
		if (mState == STATE_OPEN_RELEASE || mState == STATE_OVER_RELEASE
				|| mState == STATE_REFRESH_RELEASE)
			return false;
		int speed = mLastY;
		if (head.getTop() >= 0)
			speed = mLastY / 2;
		boolean bool = moveDown(speed, true);
		mLastY = (int) -disY;
		return bool;
	}

	private void release(int dis) {
		if (mRefreshListener != null && dis > mMaxMagin) {
			mState = STATE_OVER_RELEASE;
			mFlinger.recover(dis - mMaxMagin);
		} else {
			mState = STATE_OPEN_RELEASE;
			mFlinger.recover(dis);
		}
	}

	@Override
	public void onShowPress(MotionEvent arg0) {
	}

	@Override
	public boolean onSingleTapUp(MotionEvent arg0) {
		return false;
	}

	@SuppressWarnings("deprecation")
	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		if (!mEnablePull)
			return super.dispatchTouchEvent(ev);
		if (!mFlinger.isFinished())
			return false;
		View head = getChildAt(0);
		if (ev.getAction() == MotionEvent.ACTION_UP
				|| ev.getAction() == MotionEvent.ACTION_CANCEL
				|| ev.getAction() == MotionEvent.ACTION_POINTER_2_UP
				|| ev.getAction() == MotionEvent.ACTION_POINTER_3_UP) {
			if (head.getBottom() > 0) {
				if (mState == STATE_REFRESH && head.getBottom() > mMaxMagin) {
					release(head.getBottom() - mMaxMagin);
					return false;
				} else if (mState != STATE_REFRESH) {
					release(head.getBottom());
					return false;
				}
			}
		}
		boolean bool = mGestureDetector.onTouchEvent(ev);

		if ((bool || (mState != STATE_CLOSE && mState != STATE_REFRESH))
				&& head.getBottom() != 0) {
			ev.setAction(MotionEvent.ACTION_CANCEL);
			return super.dispatchTouchEvent(ev);
		}

		if (bool)
			return true;
		else
			return super.dispatchTouchEvent(ev);
	}

	/**
	 * 自动滚动
	 * 
	 */
	private class Flinger implements Runnable {
		private Scroller mScroller;
		private int mLastY;
		private boolean mIsFinished;

		public Flinger() {
			mScroller = new Scroller(getContext());
			mIsFinished = true;
		}

		@Override
		public void run() {
			boolean b = mScroller.computeScrollOffset();
			if (b) {
				moveDown(mLastY - mScroller.getCurrY(), false);
				mLastY = mScroller.getCurrY();
				post(this);
			} else {
				mIsFinished = true;
				removeCallbacks(this);
			}
		}

		public void recover(int dis) {
			if (dis <= 0)
				return;
			removeCallbacks(this);
			mLastY = 0;
			mIsFinished = false;
			mScroller.startScroll(0, 0, 0, dis, 300);
			post(this);
		}

		public boolean isFinished() {
			return mIsFinished;
		}
	}

	@Override
	protected void onLayout(boolean changed, int left, int top, int right,
			int bottom) {
		View head = getChildAt(0);
		View child = getChildAt(1);
		if (head != null && child != null) {
			int y = child.getTop();
			if (mState == STATE_REFRESH) {
				head.layout(0, mMaxMagin - head.getMeasuredHeight(), right,
						head.getMeasuredHeight());
				child.layout(0, mMaxMagin, right,
						mMaxMagin + child.getMeasuredHeight());
			} else {
				head.layout(0, y - head.getMeasuredHeight(), right, y);
				child.layout(0, y, right, y + child.getMeasuredHeight());
			}

			View other = null;
			for (int i = 2; i < getChildCount(); ++i) {
				other = getChildAt(i);
				other.layout(0, top, right, bottom);
			}
		}
	}

	private boolean moveDown(int dis, boolean changeState) {
		View head = getChildAt(0);
		View child = getChildAt(1);
		int childTop = child.getTop() + dis;
		if (childTop <= 0) {
			if (childTop < 0)
				dis = -child.getTop();
			head.offsetTopAndBottom(dis);
			child.offsetTopAndBottom(dis);
			if (mState != STATE_REFRESH)
				mState = STATE_CLOSE;
		} else if (childTop <= mMaxMagin) {
			if (mOverView.getState() != OverView.STATE_OPEN) {
				mOverView.onOpen();
				mOverView.setState(OverView.STATE_OPEN);
			}
			head.offsetTopAndBottom(dis);
			child.offsetTopAndBottom(dis);
			if (changeState && mState != STATE_REFRESH)
				mState = STATE_OPEN;
			else if (childTop <= mMaxMagin && mState == STATE_OVER_RELEASE) {
				refresh();
			}
		} else if (mState != STATE_REFRESH) {
			if (mOverView.getState() != OverView.STATE_OVER) {
				mOverView.onOver();
				mOverView.setState(OverView.STATE_OVER);
			}
			head.offsetTopAndBottom(dis);
			child.offsetTopAndBottom(dis);
			if (changeState)
				mState = STATE_OVER;
		}
		// mMargin = child.getTop() - head.getTop();
		invalidate();
		return true;
	}

	/**
	 * 刷新
	 */
	private void refresh() {
		if (mRefreshListener != null) {
			mState = STATE_REFRESH;
			mOverView.onLoad();
			mOverView.setState(OverView.STATE_LOAD);
			mRefreshListener.onStartRefresh();
		}
	}

	/**
	 * 刷新完成
	 * 
	 * 必须在主线程调用，且需要在refresh被调用后的下个tick才能调用
	 */
	public void refreshFinished() {
		View head = getChildAt(0);
		mOverView.onFinish();
		mOverView.setState(OverView.STATE_FINISH);
		if (head.getBottom() > 0) {
			mState = STATE_REFRESH_RELEASE;
			release(head.getBottom());
		} else
			mState = STATE_CLOSE;
	}

	/**
	 * 设置是否可下拉刷新
	 * 
	 * @param enablePull
	 *            是否可下拉刷新
	 */
	public void setEnablePull(boolean enablePull) {
		mEnablePull = enablePull;
	}

	/**
	 * 设置刷新接口
	 */
	public void setRefreshListener(RefreshListener refreshListener) {
		if (mOverView != null) {
			removeView(mOverView);
		}
		mRefreshListener = refreshListener;
		initListener();
	}

	/**
	 * 刷新接口
	 */
	public interface RefreshListener {
		/**
		 * 刷新
		 */
		public void onStartRefresh();

		public void onRefreshEnd();

		/**
		 * 获取OverView
		 * 
		 * @return OverView
		 */
		public OverView getOverView();

	}

	/**
	 * 下拉刷新的Overlay视图,可以重载这个类来定义自己的Overlay
	 * 
	 * @author daishulin
	 * 
	 */
	public static abstract class OverView extends FrameLayout {
		public static final byte STATE_CLOSE = 0;
		public static final byte STATE_OPEN = STATE_CLOSE + 1;
		public static final byte STATE_OVER = STATE_OPEN + 1;
		public static final byte STATE_LOAD = STATE_OVER + 1;
		public static final byte STATE_FINISH = STATE_LOAD + 1;

		protected byte mState = STATE_CLOSE;

		public OverView(Context context, AttributeSet attrs, int defStyle) {
			super(context, attrs, defStyle);
			init();
		}

		public OverView(Context context, AttributeSet attrs) {
			super(context, attrs);
			init();
		}

		public OverView(Context context) {
			super(context);
			init();
		}

		/**
		 * 初始化
		 */
		public abstract void init();

		/**
		 * 显示Overlay
		 */
		protected abstract void onOpen();

		/**
		 * 超过Overlay，释放就会加载
		 */
		public abstract void onOver();

		/**
		 * 开始加载
		 */
		public abstract void onLoad();

		/**
		 * 加载完成
		 */
		public abstract void onFinish();

		/**
		 * 设置状态
		 * 
		 * @param state
		 *            状态
		 */
		public void setState(byte state) {
			mState = state;
		}

		/**
		 * 获取状态
		 * 
		 * @return 状态
		 */
		public byte getState() {
			return mState;
		}
	}
}

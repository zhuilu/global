package com.global.alert;

import java.text.DecimalFormat;
import java.util.Timer;
import java.util.TimerTask;

import com.global.R;
import com.global.log.CLog;
import com.global.util.BeanUtils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Service;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * 
 * @author dai.sl
 * 
 */
public class AlertWaitService extends Service {
	private static long mTimeOut;
	private static final long defTimeout = 10000;
	private static AlertDialog mDialog = null;
	public static String mMessage = "";
	static AlertDialog.Builder mBuilder;
	private static String stringSpear = "...";
	public static View mView;
	public static AlertWait alertWait;
	private static TextView messT;
	private static Handler mHandler;
	static Timer mTimer;
	public static ImageView animImage;
	LinearLayout progressLayout;
	DecimalFormat df1 = new DecimalFormat("0.00%");

	class CustomThread extends Thread {
		@Override
		public void run() {
			Looper.prepare();
			mHandler = new Handler() {
				public void handleMessage(android.os.Message msg) {
					switch (msg.what) {
					case 1:// setText
						String cleanmess = (String) msg.obj;
						mMessage = cleanmess + stringSpear;
						animImage.setVisibility(View.GONE);
						progressLayout.setVisibility(View.VISIBLE);
						messT.setText(mMessage);
						mDialog.show();
						break;
					case 2:// hide
						mMessage = "";
						mDialog.hide();
						break;
					case 3:
						String[] paras = (String[]) msg.obj;
						CLog.i(paras[0] + "/" + paras[1]);

						if (paras[0].equals(paras[1])) {
							alertWait.forceEndAlertWait();
							break;
						}
						int x = Integer.parseInt(paras[0]);
						int total = Integer.parseInt(paras[1]);
						double x_double = x * 1.0;
						double tempresult = x_double / total;
						mMessage = "";
						mMessage = df1.format(tempresult);
						mMessage = "上传中" + stringSpear + mMessage;
						messT.setText(mMessage);
						if (!mDialog.isShowing()) {
							mDialog.show();
						}
						break;
					case 10:
						int anim = (Integer) msg.obj;
						AnimationDrawable animDrawable = (AnimationDrawable) getResources()
								.getDrawable(anim);
						// 设置手否重复播放，false为重复
						animDrawable.setOneShot(false);
						animImage.setImageDrawable(animDrawable);
						mHandler.sendEmptyMessageDelayed(11, 200);
						break;
					case 11:
						((AnimationDrawable) animImage.getDrawable()).start();
						animImage.setVisibility(View.VISIBLE);
						progressLayout.setVisibility(View.GONE);
						mDialog.show();
						break;
					default:
						break;
					}
				};
			};
			Looper.loop();
		}
	}

	public static AlertWait getInstance() {
		if (alertWait == null) {
			alertWait = new AlertWait();
		}
		return alertWait;
	}

	public static class AlertWait {

		public void startWait(int timeOut, final String... mess) {
			if (null != mess && null != mHandler) {
				mTimeOut = timeOut;

				mHandler.obtainMessage(1, mess[0]).sendToTarget();
				if (mTimer != null) {
					mTimer.cancel();
					mTimer = null;
				}
				mTimer = new Timer();
				mTimer.schedule(new TimerTask() {
					@Override
					public void run() {
						forceEndAlertWait();
					}
				}, mTimeOut + 5000);
			}
		}

		public void startWait(final String... mess) {
			if (null != mess && null != mHandler) {
				startWait((int) defTimeout, mess);
			}
		}

		public void startAnim(int res) {
			mTimeOut = defTimeout;
			mHandler.obtainMessage(10, res).sendToTarget();
			if (mTimer != null) {
				mTimer.cancel();
				mTimer = null;
			}
			mTimer = new Timer();
			mTimer.schedule(new TimerTask() {
				@Override
				public void run() {
					forceEndAlertWait();
				}
			}, mTimeOut + 5000);

		}

		public void forceEndAlertWait() {
			mHandler.obtainMessage(2, "").sendToTarget();
		}

		public void updateProgress(final String... paras) {
			mHandler.obtainMessage(3, paras[0]).sendToTarget();
		}
	}

	@Override
	public void onCreate() {
		initView();
		super.onCreate();
	}

	public void initView() {
		mView = View.inflate(AlertWaitService.this, R.layout.wait, null);
		messT = (TextView) mView.findViewById(R.id.text);
		progressLayout = (LinearLayout) mView.findViewById(R.id.progressLayout);
		animImage = (ImageView) mView.findViewById(R.id.image);
		mBuilder = new AlertDialog.Builder(this);

		mBuilder.setView(mView);
		mBuilder.setCancelable(false);
		mDialog = mBuilder.create();

		mDialog.getWindow().setType(
				WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
		new CustomThread().start();
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	private static void updateProgressStyle(ProgressBar progressBar,
			boolean booleanVal) {
		BeanUtils.setFieldValue(progressBar, "mOnlyIndeterminate", new Boolean(
				booleanVal));
		progressBar.setIndeterminate(false);
		progressBar.setProgressDrawable(messT.getResources().getDrawable(
				android.R.drawable.progress_horizontal));
		progressBar.setIndeterminateDrawable(messT.getResources().getDrawable(
				android.R.drawable.progress_indeterminate_horizontal));
		progressBar.setMinimumHeight(20);
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {

		return super.onStartCommand(intent, flags, startId);
	}

}

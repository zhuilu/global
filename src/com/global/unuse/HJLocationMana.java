//package com.global.unuse;
//
//import com.global.app.SysApplicationImpl;
//import com.hjdata.base.HJLog;
//import com.hjdata.ui.media.SimpleMapActivity;
//
//import android.app.Activity;
//import android.content.Context;
//import android.content.Intent;
//import android.location.Criteria;
//import android.location.Location;
//import android.location.LocationListener;
//import android.location.LocationManager;
//import android.location.LocationProvider;
//import android.os.Bundle;
//import android.os.Handler;
//import android.os.HandlerThread;
//import android.provider.Settings;
//import android.widget.Toast;
//
//public abstract class HJLocationMana {
//	private static HJLocationMana location;
//	private LocationManager locationManager;
//	private LocationListener llistener;
//	private static Activity mActivity;
//	LocationProvider gpsProvider;
//	String provider;
//
//	public HJLocationMana(Activity activity) {
//		mActivity = activity;
//
//		openGPSSettings();
//
//		String serviceName = Context.LOCATION_SERVICE;
//		locationManager = (LocationManager) activity
//				.getSystemService(serviceName);
//		// gpsProvider =
//		// locationManager.getProvider(LocationManager.GPS_PROVIDER);
//
//		// 查找到服务信息
//		Criteria criteria = new Criteria();
//		criteria.setAccuracy(Criteria.ACCURACY_FINE); // 高精度
//		criteria.setAltitudeRequired(false);
//		criteria.setBearingRequired(false);
//		criteria.setCostAllowed(true);
//		criteria.setPowerRequirement(Criteria.POWER_LOW); // 低功耗 String
//		provider = locationManager.getBestProvider(criteria, true);
//		com.global.log.HJLog.i("provider=" + provider);
//		// 获取GPS信息
//		Location location = locationManager.getLastKnownLocation(provider); // 通过GPS获取位置
//		HJLog.i("come in  getLastKnownLocation init");
//		if (location != null) {
//			HJLog.i("come in  getLastKnownLocation ");
//			onCurrentLocation(location);
//		}
//
//		start();
//	}
//
//	public void start() {
//		// HandlerThread thread = new HandlerThread("Location thread");
//		// thread.start();
//		// Handler handler = new Handler(thread.getLooper());
//		// handler.post(new Runnable() {
//		// @Override
//		// public void run() {
//		llistener = new LocationListener() {
//			@Override
//			public void onLocationChanged(final Location location) {
//				mActivity.runOnUiThread(new Runnable() {
//					@Override
//					public void run() {
//						HJLog.i("come in run ");
//						onCurrentLocation(location);
//					}
//				});
//
//			}
//
//			@Override
//			public void onProviderDisabled(String provider) {
//				HJLog.i("come in onProviderDisabled " + provider);
//
//			}
//
//			@Override
//			public void onProviderEnabled(String provider) {
//				HJLog.i("come in onProviderEnabled " + provider);
//			}
//
//			@Override
//			public void onStatusChanged(String provider, int status,
//					Bundle extras) {
//				// TODO Auto-generated method stub
//				HJLog.i("come in  onStatusChanged " + provider);
//
//			}
//
//		};
//
//		locationManager.requestLocationUpdates(provider, 500, 0, llistener);
//		 
//	}
//
//	// });
//	// }
//
//	private void openGPSSettings() {
//		LocationManager alm = (LocationManager) mActivity
//				.getSystemService(Context.LOCATION_SERVICE);
//		if (alm.isProviderEnabled(android.location.LocationManager.GPS_PROVIDER)) {
//			return;
//		}
//
//		Toast.makeText(mActivity, "请开启GPS！", Toast.LENGTH_SHORT).show();
//		Intent intent = new Intent(Settings.ACTION_SECURITY_SETTINGS);
//		mActivity.startActivityForResult(intent, 0); // 此为设置完成后返回到获取界面
//
//	}
//
//	public abstract void onCurrentLocation(Location location);
//}

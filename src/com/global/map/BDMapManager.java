//package com.global.map;
//
//import java.io.IOException;
//
//import android.app.Activity;
//import android.content.Context;
//import android.location.Location;
//import android.location.LocationListener;
//import android.os.Bundle;
//import android.widget.Toast;
//import com.baidu.mapapi.*;
//import com.baidu.mapapi.map.MapController;
//import com.baidu.mapapi.map.MapView;
//import com.baidu.platform.comapi.basestruct.GeoPoint;
//import com.global.app.SysApplicationImpl;
//import com.global.file.FileUtil;
//import com.global.log.HJLog;
//
//public class BDMapManager extends BMapManager {
//	public BMapManager mMapManager;
//	public MapController mapController;
//	public Activity mActivity;
//	OnGetLocationListener listener;
//
//	public BDMapManager(Context context) {
//		super(context);
//	}
//
//	public BDMapManager(Activity activity, MapView mapview,
//			OnGetLocationListener listen) {
//		super(activity);
//		 mActivity = activity;
//		 listener = listen;
//		 mMapManager = new BMapManager(SysApplicationImpl.getInstance());
//		 mMapManager.init("205114502786B06C4C95CEB0F55822F25E46AED2", null);
//		 ((MapActivity) activity).initMapActivity(mMapManager);
//		
//		 mMapManager.start();
//		 mapview.setBuiltInZoomControls(true);
//		 // mapview.setTraffic(true);
//		 mapController = mapview.getController();
//		 mapController.setZoom(12); // 设置地图zoom级别
//		
//		 mMapManager.getLocationManager().enableProvider(
//		 MKLocationManager.MK_GPS_PROVIDER);
//		 mMapManager.getLocationManager().enableProvider(
//		 MKLocationManager.MK_NETWORK_PROVIDER);
//		 mMapManager.getLocationManager().requestLocationUpdates(
//		 mLocationListener);
//		mMapManager.getLocationManager().setNoitifyInternal(5, 2);
//
//	}
//
//	private LocationListener mLocationListener = new LocationListener() {
//		@Override
//		public void onLocationChanged(Location location) {
//			if (location != null) {
//				listener.onGetLocation(location);
//			}
//		}
//
//		@Override
//		public void onProviderDisabled(String provider) {
//
//		}
//
//		@Override
//		public void onProviderEnabled(String provider) {
//
//		}
//
//		@Override
//		public void onStatusChanged(String provider, int status, Bundle extras) {
//
//		}
//	};
//
//	public void startpManager() {
//		mMapManager.start();
//	}
//
//	public void stopManager() {
//		mMapManager.stop();
//
//	}
//
//	public interface OnGetLocationListener {
//		public void onGetLocation(Location location);
//
//	}
//
//	public void setLastLocation(Location location) {
//		try {
//			Double latitude = location.getLatitude() * 1E6;
//			Double longitude = location.getLongitude() * 1E6;
//			GeoPoint geopoint = new GeoPoint(latitude.intValue(),
//					longitude.intValue());
//
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
// }

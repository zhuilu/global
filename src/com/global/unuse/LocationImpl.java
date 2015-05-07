//package com.global.unuse;
//
//import java.io.BufferedReader;
//import java.io.File;
//import java.io.InputStreamReader;
//
//import org.apache.http.HttpEntity;
//import org.apache.http.HttpResponse;
//import org.apache.http.client.HttpClient;
//import org.apache.http.client.methods.HttpGet;
//import org.apache.http.client.methods.HttpPost;
//import org.apache.http.entity.StringEntity;
//import org.apache.http.impl.client.DefaultHttpClient;
//import org.json.JSONArray;
//import org.json.JSONObject;
//
//import android.app.Service;
//import android.content.Context;
//import android.location.Location;
//import android.location.LocationListener;
//import android.location.LocationManager;
//import android.os.Bundle;
//import android.os.Looper;
//import android.os.SystemClock;
//import android.util.Log;
//
//import com.global.file.FileUtil;
//import com.global.http.HttpUtil;
//import com.global.json.JSONUtil;
//import com.hjdata.component.location.HJLocationManager.LocationResult;
//
//public final class LocationImpl {
//	private static LocationImpl mLocationImpl;
//	private static Context mContext;
//
//	/**
//	 * 定位管理类
//	 */
//	private LocationManager mLocationMgr;
//
//	/**
//	 * 当前位置
//	 */
//	private Location mLocation;
//
//	/**
//	 * 获取位置监听最短间隔时间
//	 */
//	private static final long MIN_TIME = 500;
//
//	/**
//	 * 获取位置监听最短间隔距离
//	 */
//	private static final float MIN_INSTANCE = 20;
//
//	private static final String TAG = "LocationImpl";
//
//	private LocationImpl(Context context) {
//		mContext = context;
//		mLocationMgr = (LocationManager) context
//				.getSystemService(Service.LOCATION_SERVICE);
//	}
//
//	/**
//	 * 获取位置服务实例
//	 * 
//	 * @param context
//	 *            上下文
//	 * @return 位置服务实例
//	 */
//	public static LocationImpl getLocationImpl(Context context) {
//		synchronized (LocationImpl.class) {
//			if (mLocationImpl == null) {
//				mLocationImpl = new LocationImpl(context);
//			}
//		}
//		return mLocationImpl;
//	}
//
//	/**
//	 * 
//	 * 获取用户位置 根据GPS or WiFi Or 基站定位用户位置
//	 * 
//	 * @return Location 经纬度Location
//	 */
//	public LocationResult getLocation() {
//		Log.d(TAG, "------getLocation---start---");
//		mLocation = null;
//
//		long init = System.currentTimeMillis();
//		LocationResult locationInfo = new LocationResult();
//		boolean hasStartListener = false;
//		try {
//			if (mLocationMgr
//					.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
//				Log.d(TAG, "startListenLocation NETWORK_PROVIDER");
//				startListenLocation(LocationManager.NETWORK_PROVIDER);
//				hasStartListener = true;
//			}
//		} catch (Exception e) {
//			HJLog.e( "不支持Network " + " LocationManager.isProviderEnabled() "
//					+ e.getMessage());
//		}
//
//		try {
//			if (mLocationMgr.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
//				Log.d(TAG, "startListenLocation GPS_PROVIDER");
//				startListenLocation(LocationManager.GPS_PROVIDER);
//				hasStartListener = true;
//			}
//		} catch (Exception e) {
//			HJLog.e(
//					"不支持GPS " + " LocationManager.isProviderEnabled() "
//							+ e.getMessage());
//		}
//		if (locationInfo.getLocation() == null) {
//			long start = System.currentTimeMillis();
//			locationInfo = getLocationInfo();
//			Log.d(TAG, "getByCellOrWifi = "
//					+ (System.currentTimeMillis() - start));
//		}
//		if (hasStartListener) {
//			if (locationInfo.getLocation() == null) {
//				int counter = 0;
//				while (counter++ < 20) {
//					SystemClock.sleep(MIN_TIME);
//					if (mLocation != null) {
//						Log.d(TAG, "got location by provider");
//						locationInfo.setLocation(mLocation);
//						break;
//					}
//				}
//			}
//			// 移除监听
//			stopListenLocation();
//		}
//
//		if (locationInfo.getLocation() != null) {
//			Log.d(TAG, "end    =  " + locationInfo.getLocation().getLatitude()
//					+ "*****" + locationInfo.getLocation().getLongitude());
//		}
//		Log.d(TAG, "totalTime = " + (System.currentTimeMillis() - init));
//		return locationInfo;
//	}
//
//	/**
//	 * 位置变化监听
//	 */
//	private LocationListener mLocationListener = new LocationListener() {
//
//		@Override
//		public void onLocationChanged(Location location) {
//			Log.d(TAG, "====location changed====");
//
//			Log.d(TAG, "changed    =  " + location.getLatitude() + "*****"
//					+ location.getLongitude());
//			LocationImpl.this.mLocation = location;
//		}
//
//		@Override
//		public void onStatusChanged(String s, int i, Bundle bundle) {
//		}
//
//		@Override
//		public void onProviderEnabled(String s) {
//		}
//
//		@Override
//		public void onProviderDisabled(String s) {
//		}
//	};
//
//	/**
//	 * 开始监听位置信息变化
//	 * 
//	 * @param locationProvider
//	 *            位置服务提供者
//	 */
//	private void startListenLocation(String locationProvider) {
//		mLocationMgr.requestLocationUpdates(locationProvider, MIN_TIME,
//				MIN_INSTANCE, mLocationListener, Looper.getMainLooper());
//	}
//
//	/**
//	 * 停止监听位置信息变化
//	 */
//	private void stopListenLocation() {
//		mLocationMgr.removeUpdates(mLocationListener);
//		mLocation = null;
//	}
//
//	/**
//	 * 
//	 * 根据基站或者Wifi获取位置详细信息
//	 * 
//	 * @param jsonArray
//	 * @param isByCell
//	 * @return
//	 */
//	private LocationResult getLocationInfo() {
//		LocationResult info = new LocationResult();
//
//		CellInfoManager cellManager = new CellInfoManager(mContext);
//		WifiInfoManager mWiFiManager = new WifiInfoManager(mContext);
//		JSONArray cellArray = cellManager.cellTowers();
//		JSONArray wifiArray = mWiFiManager.wifiTowers();
//		Log.d(TAG, "getLocationInfo----cellArray=" + cellArray + ", wifiArray="
//				+ wifiArray);
//		if (cellArray.length() < 1 && wifiArray.length() < 1) {
//			// 如果本地的定位信息缺失，不去服务器查询
//			return info;
//		}
//		JSONObject holder = new JSONObject();
//		JSONObject retLocationJson, locationData, addressData;
//		try {
//			holder.put("version", "1.1.0");
//			holder.put("host", "maps.google.com");
//			holder.put("address_language", "zh_CN");
//			if (cellArray.length() > 0) {
//				if (cellManager.isGsm()) {
//					holder.put("radio_type", "gsm");
//
//				} else if (cellManager.isCdma()) {
//
//					holder.put("radio_type", "cdma");
//				}
//			}
//			holder.put("request_address", true);
//			holder.put("cell_towers", cellArray);
//			holder.put("wifi_towers", wifiArray);
//			HttpClient client = HttpUtil.getHttpClient();
//			HttpPost localHttpPost = new HttpPost(
//					"http://www.google.com/loc/json");
//			String strJson = holder.toString();
//			StringEntity objJsonEntity = new StringEntity(strJson);
//			Log.d(TAG, "getLocationInfo: Location Send*****" + strJson);
//			localHttpPost.setEntity(objJsonEntity);
//			HttpResponse objResponse = client.execute(localHttpPost);
//			HttpEntity httpEntity = objResponse.getEntity();
//			BufferedReader br = new BufferedReader(new InputStreamReader(
//					httpEntity.getContent()));
//			StringBuffer sb = new StringBuffer();
//			String result = null;
//			while ((result = br.readLine()) != null) {
//				Log.d(TAG, "getLocationInfo: Locaiton receive*****" + result);
//				sb.append(result);
//			}
//			FileUtil.closeStream(br);
//
//			retLocationJson = new JSONObject(sb.toString());
//			locationData = JSONUtil.getJSONObject(retLocationJson, "location");
//			if (locationData != null) {
//				Location loc = new Location(LocationManager.NETWORK_PROVIDER);
//				loc.setLatitude(JSONUtil.getDouble(locationData, "latitude"));
//				loc.setLongitude(JSONUtil.getDouble(locationData, "longitude"));
//				loc.setAccuracy((float) JSONUtil.getDouble(locationData,
//						"accuracy"));
//				loc.setTime(System.currentTimeMillis());
//				info.setLocation(loc);
//
//				addressData = JSONUtil.getJSONObject(locationData, "address");
//				if (addressData != null) {
//					StringBuffer strBuffer = new StringBuffer();
//					if (JSONUtil.getString(addressData, "country") != null) {
//						strBuffer.append(JSONUtil.getString(addressData,
//								"country"));
//					}
//					if (JSONUtil.getString(addressData, "region") != null) {
//						strBuffer.append(JSONUtil.getString(addressData,
//								"region"));
//					}
//					if (JSONUtil.getString(addressData, "city") != null) {
//						strBuffer.append(JSONUtil
//								.getString(addressData, "city"));
//					}
//					if (JSONUtil.getString(addressData, "street") != null) {
//						strBuffer.append(JSONUtil.getString(addressData,
//								"street"));
//					}
//					if (JSONUtil.getString(addressData, "street_number") != null) {
//						strBuffer.append(JSONUtil.getString(addressData,
//								"street_number"));
//					}
//					info.setAddressInfo(strBuffer.toString());
//				}
//			}
//
//			return info;
//		} catch (Exception e) {
//			e.printStackTrace();
//			Log.d(TAG, "getLocationInfo Exception =" + e.toString());
//		}
//		return info;
//	}
//
//	/**
//	 * 
//	 * 根据经纬度获取到位置信息
//	 * 
//	 * @param location
//	 *            location
//	 * @return String 位置信息
//	 */
//	public String getAddress(double lat, double lon) {
//		// 也可以是http://maps.google.cn/maps/geo?output=csv&key=abcdef&q=%s,%s，不过解析出来的是英文地址
//		// 密钥可以随便写一个key=abc
//		// output=csv,也可以是xml或json，不过使用csv返回的数据最简洁方便解析
//		String url = String.format(
//				"http://ditu.google.cn/maps/geo?output=csv&key=abcdef&q=%s,%s",
//				lat, lon);
//		Log.d(TAG, "getAddress: Address send*****" + url);
//		String result = "";
//		HttpClient client = new DefaultHttpClient();
//		try {
//			HttpResponse hr = client.execute(new HttpGet(url));
//			HttpEntity entity = hr.getEntity();
//			BufferedReader br = new BufferedReader(new InputStreamReader(
//					entity.getContent()));
//
//			if ((result = br.readLine()) != null) {
//				Log.d(TAG, "getAddress: Address receive*****" + result);
//				String[] retList = result.split(",");
//				if (retList.length > 2 && ("200".equals(retList[0]))) {
//					result = retList[2];
//					result = result.replace("\"", "");
//				} else {
//					result = "";
//				}
//			}
//			FileUtil.closeStream(br);
//			Log.d(TAG, "getAddress: Address =" + result);
//		} catch (Exception e) {
//			e.printStackTrace();
//			Log.d(TAG, "getAddress: Exception =" + e.toString());
//			return null;
//		}
//		return result;
//	}
//
//	/**
//	 * 
//	 * 判断/system/framework/中是否有com.google.android.maps.jar
//	 * 
//	 */
//	public boolean isJarExisits() {
//		return new File("/system/framework/com.google.android.maps.jar")
//				.exists();
//	}
//}

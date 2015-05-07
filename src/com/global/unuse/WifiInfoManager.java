package com.global.unuse;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;

public class WifiInfoManager {
	private WifiManager wifiManager;

	/**
	 * 
	 * 构造方法
	 * 
	 * @param paramContext
	 *            Context
	 */
	public WifiInfoManager(Context paramContext) {
		wifiManager = (WifiManager) paramContext.getSystemService(Context.WIFI_SERVICE);
	}

	/**
	 * 
	 * 获取wifi相关信息
	 * 
	 * @return Wifi相关参数列表
	 */
	private List<WifiInfo> dump() {
		if (!this.wifiManager.isWifiEnabled()) {
			return new ArrayList<WifiInfo>();
		}
		android.net.wifi.WifiInfo wifiConnection = this.wifiManager.getConnectionInfo();
		WifiInfo currentWIFI = null;
		if (wifiConnection != null) {
			String s = wifiConnection.getBSSID();
			int i = wifiConnection.getRssi();
			String s1 = wifiConnection.getSSID();
			currentWIFI = new WifiInfo(s, i, s1);

		}
		ArrayList<WifiInfo> lsAllWIFI = new ArrayList<WifiInfo>();
		if (currentWIFI != null) {
			lsAllWIFI.add(currentWIFI);
		}
		List<ScanResult> lsScanResult = this.wifiManager.getScanResults();
		for (ScanResult result : lsScanResult) {
			WifiInfo scanWIFI = new WifiInfo(result);
			if (!scanWIFI.equals(currentWIFI)) {
				lsAllWIFI.add(scanWIFI);
			}
		}
		return lsAllWIFI;
	}

	/**
	 * 
	 * 判断wifi是否开启
	 * 
	 * @return wifi是否可用
	 */
	public boolean isWifiEnabled() {
		return this.wifiManager.isWifiEnabled();
	}

	/**
	 * 
	 * WifiManager
	 * 
	 * @return WifiManager
	 */
	public WifiManager wifiManager() {
		return this.wifiManager;
	}

	/**
	 * 将WIFI信息组合成JSON包，去服务器查询位置时做为数据上传至Google的位置服务器
	 * 
	 * @return JSONArray
	 */
	public JSONArray wifiTowers() {
		JSONArray jsonArray = new JSONArray();
		if (isWifiEnabled()) {
			try {
				Iterator<WifiInfo> localObject = dump().iterator();
				while (true) {
					if (!localObject.hasNext()) {
						return jsonArray;
					}

					jsonArray.put(localObject.next().towerJson());
				}
			} catch (Exception localException) {
			}
		}

		return jsonArray;
	}

	/**
	 * 
	 * Wifi信息类
	 * 
	 * @author raulxiao
	 */
	private class WifiInfo implements Comparable<WifiInfo> {
		private final String bssid;

		private final int dBm;

		private final String ssid;

		/**
		 * 
		 * @param s
		 *            String
		 * @param i
		 *            int
		 * @param s1
		 *            String
		 */
		public WifiInfo(String s, int i, String s1) {
			bssid = s;
			dBm = i;
			ssid = s1;
		}

		/**
		 * 
		 * @param scanresult
		 */
		public WifiInfo(ScanResult scanresult) {
			String s = scanresult.BSSID;
			bssid = s;
			int i = scanresult.level;
			dBm = i;
			String s1 = scanresult.SSID;
			ssid = s1;
		}

		public int compareTo(WifiInfo wifiinfo) {
			int i = wifiinfo.dBm;
			int j = dBm;
			return i - j;
		}

		public boolean equals(Object obj) {
			boolean flag = false;
			if (obj == this) {
				flag = true;
				return flag;
			} else {
				if (obj instanceof WifiInfo) {
					WifiInfo wifiinfo = (WifiInfo) obj;
					String s = wifiinfo.bssid;
					String s1 = bssid;
					if (s.equals(s1)) {
						flag = true;
						return flag;
					}
				} else {
					flag = false;
				}
			}
			return flag;
		}

		public int hashCode() {
			int i = dBm;
			int j = bssid.hashCode();
			return i ^ j;
		}

		/**
		 * 
		 * wifi mac JSON
		 * 
		 * @return JSONObject
		 */
		@SuppressWarnings("unused")
		private JSONObject info() {
			JSONObject jsonobject = new JSONObject();
			try {
				String s = bssid;
				jsonobject.put("mac", s);
				String s1 = ssid;
				jsonobject.put("ssid", s1);
				int i = dBm;
				jsonobject.put("dbm", i);
			} catch (Exception ex) {
			}
			return jsonobject;
		}

		/**
		 * 
		 * wifi mac JSON
		 * 
		 * @return JSONObject
		 */
		private JSONObject towerJson() {
			JSONObject jsonobject = new JSONObject();
			try {
				String s = bssid;
				jsonobject.put("mac_address", s);
				int i = dBm;
				jsonobject.put("signal_strength", i);
				String s1 = ssid;
				jsonobject.put("ssid", s1);
				jsonobject.put("age", 0);
			} catch (Exception ex) {
			}
			return jsonobject;
		}
	}
}

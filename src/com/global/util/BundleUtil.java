package com.global.util;

import android.os.Bundle;

/**
 * Bundle转换
 * 
 * @author wb-wuxiaofan@163.com
 * 
 */
public class BundleUtil {
	/**
	 * 将Bundle转换为key-value字符串形式 key1=111&key2=aaa...
	 * 
	 * @param bundle
	 * @return
	 */
	public static String deserialBundle(Bundle bundle) {
		if (bundle == null)
			return null;
		StringBuilder params = new StringBuilder();
		for (String key : bundle.keySet()) {
			params.append(key + "=" + bundle.get(key) + "&");
		}

		return params.length() > 0 ? params.substring(0, params.length() - 1)
				: null;
	}

	/**
	 * 将key-value格式转换为Bundle
	 * 
	 * @param params
	 * @return
	 */
	public static Bundle serialBundle(String params) {
		Bundle bundle = null;
		if (params != null && params.length() > 0) {
			bundle = new Bundle();
			String[] strs = params.split("&");
			String[] pairs = null;
			for (String str : strs) {
				pairs = str.split("=");
				if (pairs.length >= 2) {
					bundle.putString(pairs[0], pairs[1]);
				}
			}
		}

		return bundle;
	}
}

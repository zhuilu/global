package com.global.json;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.global.string.StringUtil;

/**
 * 
 * JSON操作封装
 * 
 */
public abstract class JSONUtil {

	/**
	 * 生成一个空的JSON对象
	 * 
	 * @return 空的JSON对象
	 */
	public static JSONObject newJSONObject() {
		return new JSONObject();
	}

	/**
	 * 
	 * 封装JSON数据
	 * 
	 * @param object
	 *            JSON对象
	 * @param key
	 *            key值
	 * @param value
	 *            value值
	 */
	public static void putPairIntoJSONObject(JSONObject object, String key,
			Object value) {
		try {
			object.put(key, value);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * 根据JSON数据封装一个新的JSON对象
	 * 
	 * @param content
	 *            JSON数据
	 * @return 新的JSON数据
	 */
	public static JSONObject newJSONObject(String content) {
		if (StringUtil.isNullOrEmpty(content)) {
			return null;
		}
		JSONObject retObj = null;
		try {
			String filterStr = StringUtil.filterHtmlTag(content);
			retObj = new JSONObject(filterStr);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return retObj;
	}

	/**
	 * 
	 * 根据JSON数据封装一个新的JSON数据数组
	 * 
	 * @param content
	 *            JSON数据
	 * @return JSON数组对象
	 */
	public static JSONArray newJSONArray(String content) {
		JSONArray jArray = null;
		try {
			jArray = new JSONArray(content);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return jArray;
	}

	/**
	 * 
	 * 获取JSON数组对象
	 * 
	 * @param parentObj
	 *            JSON对象
	 * @param key
	 *            key值
	 * @return JSON数组对象
	 */
	public static JSONArray getJSONArray(JSONObject parentObj, String key) {
		JSONArray jArray = null;
		try {
			if (parentObj.has(key)) {
				jArray = parentObj.getJSONArray(key);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return jArray;
	}

	/**
	 * 
	 * 获取JSON对象
	 * 
	 * @param parentArray
	 *            JSON数组
	 * @param index
	 *            数组索引
	 * @return JSON对象
	 */
	public static JSONObject getJSONObject(JSONArray parentArray, int index) {
		JSONObject retObj = null;
		try {
			retObj = parentArray.getJSONObject(index);
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return retObj;
	}

	/**
	 * 
	 * 获取JSON对象
	 * 
	 * @param parentObj
	 *            JSON对象
	 * @param key
	 *            key值
	 * @return JSON对象
	 */
	public static JSONObject getJSONObject(JSONObject parentObj, String key) {
		JSONObject retObj = null;
		try {
			if (parentObj.has(key)) {
				retObj = parentObj.getJSONObject(key);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return retObj;
	}

	/**
	 * 
	 * 获取JSON数据
	 * 
	 * @param parentObj
	 *            JSON对象
	 * @param key
	 *            key值
	 * @return JSON数据
	 */
	public static String getString(JSONObject parentObj, String key) {
		try {
			if (parentObj.has(key)) {
				return parentObj.getString(key);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return null;
	}

	public static int getInt(JSONObject parentObj, String key) {
		try {
			if (parentObj.has(key)) {
				return parentObj.getInt(key);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return 0;
	}

	public static double getDouble(JSONObject parentObj, String key) {
		try {
			if (parentObj.has(key)) {
				return parentObj.getDouble(key);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return 0;
	}
}

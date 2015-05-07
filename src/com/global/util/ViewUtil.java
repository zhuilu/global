package com.global.util;

public class ViewUtil {
	
	/**
	 * mask字符串, 如mask手机号码 maskString(phone, "*", 3, 4)
	 * @param target 目标字符串
	 * @param mask	  mask串，一般为*，多于１个则取首字符
	 * @param start  开始mask的index
	 * @param len    mask长度
	 * @return
	 */
	public static String maskString(String target, String mask, int start, int len) {
		StringBuilder masks = new StringBuilder();
		if (mask.length() > 1) {
			mask = mask.substring(0, 1);
		}
		for (int i = 0; i < len; i ++) {
			masks.append(mask); 
		}
		return target.substring(0, start) + masks.toString() + target.substring(start + len);
	}
	
}

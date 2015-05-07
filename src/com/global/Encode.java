package com.global;

import java.security.MessageDigest;

import android.util.Base64;

public class Encode {/*
					 * MD5加密 000000 ZwsUcorZkCrsujLiL6T2vQ==
					 */
	public String getMD5Str(String str) throws Exception {
		MessageDigest messageDigest = null;
		messageDigest = MessageDigest.getInstance("MD5");
		messageDigest.reset();
		messageDigest.update(str.getBytes("UTF-8"));
		byte[] byteArray = messageDigest.digest();
		return Base64.encodeToString(byteArray, 0).replaceAll("=", "%3D")
				.replaceAll("\n", "");
	}
}

package com.global;

public class User {
	// 登录名
	public static String userId[] = new String[] { "", "userId" };
	// id
	public static String operatorid[] = new String[] { "", "operatorid" };
	// 当前用户token
	public static String token[] = new String[] { "", "token" };

	public static String[] getUserId() {
		return userId;
	}

	public static void setUserId(String[] userId) {
		User.userId = userId;
	}

	public static String[] getOperatorid() {
		return operatorid;
	}

	public static void setOperatorid(String[] operatorid) {
		User.operatorid = operatorid;
	}

	public static String[] getToken() {
		return token;
	}

	public static void setToken(String[] token) {
		User.token = token;
	}

}

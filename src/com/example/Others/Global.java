package com.example.Others;

import java.util.HashMap;
import java.util.Map;

public class Global {
	public static int REC_CMD_NUM = 0;
	public static int SET_INFRA_COMM_PARAM = -1;// 0：设置失败，1：设置成功
	public static int GET_INFRA_COMM_PARAM = -1;// 0：设置失败，1：设置成功
	public static String INFRA_PARAMS = "";// 红外参数
	public static Map<String, byte[]> INFRA_REC = new HashMap<String, byte[]>();// 红外通讯返回的报文
	public static int SET_OVER_TIME = -1;
	public static String PIN_ID = "8260";

	public static int LINK_STATE = 0;// 0:未连接 1：已连接
}

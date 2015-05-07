package com.example.Others;

import java.util.Locale;

public class Tool {
	/**
	 * 将传进来的十六进制表示的字符串转换成byte数组
	 * 
	 * @param hexString
	 * @return 二进制表示的byte[]数组
	 */
	public static byte[] hexStringToBytes(String hexString) {
		if (hexString == null || hexString.equals("")) {
			return null;
		}
		hexString = hexString.toUpperCase(Locale.getDefault());
		int length = hexString.length() / 2;
		// 将十六进制字符串转换成字符数组
		char[] hexChars = hexString.toCharArray();
		byte[] d = new byte[length];
		for (int i = 0; i < length; i++) {
			// 一次去两个字符
			int pos = i * 2;
			// 两个字符一个对应byte的高四位一个对应低四位
			d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));
		}
		return d;
	}

	/**
	 * 将传进来的字符代表的数字转换成二进制数
	 * 
	 * @param c
	 *            要转换的字符
	 * @return 以byte的数据类型返回字符代表的数字的二进制表示形式
	 */
	private static byte charToByte(char c) {
		return (byte) "0123456789ABCDEF".indexOf(c);
	}

	// 字符串转为十六进制数组
	public static byte[] getHexBytes(String message) {
		int len = message.length() / 2;
		char[] chars = message.toCharArray();
		String[] hexStr = new String[len];
		byte[] bytes = new byte[len];
		for (int i = 0, j = 0; j < len; i += 2, j++) {
			hexStr[j] = "" + chars[i] + chars[i + 1];
			bytes[j] = (byte) Integer.parseInt(hexStr[j], 16);
		}
		return bytes;
	}

	// byte数组转换为十六进制字符串
	public static String bytesToHexString(byte[] bytes) {
		String result = "";
		for (int i = 0; i < bytes.length; i++) {
			String hexString = Integer.toHexString(bytes[i] & 0xFF);
			if (hexString.length() == 1) {
				hexString = '0' + hexString;
			}
			result += hexString.toUpperCase();
		}
		return result;
	}

	// 得到校验码的方法
	public static String getCheckCode(String data) {
		byte[] msg = hexStringToBytes(data);
		byte[] tmp_result = new byte[1];
		for (int i = 0; i < msg.length; i++) {
			tmp_result[0] = (byte) (tmp_result[0] + msg[i]);
		}

		return bytesToHexString(tmp_result);
	}

	/*
	 * byte数组中的数据转为Int数据 b : byte数组(低字节在前，高字节在后) offset : 转换开始的位置 len :
	 * byte数据的个数
	 */
	public static int byteToInt(byte[] b, int offset, int len) {

		int mask = 0xff;
		int temp = 0;
		int n = 0;
		for (int i = offset + len - 1; i >= offset; i--) {
			n <<= 8;
			temp = b[i] & mask;
			n |= temp;
		}
		return n;
	}

	/**
	 * 整形转换为十六进制字符串(转换后低字节在前，高字节在后)
	 * 
	 * @param data
	 *            : 整形数据
	 * @param hex_str_len
	 *            : 要转换成的字符串的长度
	 * @return : 十六进制字符串
	 */
	public static String IntToHexString(int data, int hex_str_len) {
		String hexString = Integer.toHexString(data);
		int len = hexString.length();
		if (len < hex_str_len) {
			for (int i = 0; i < hex_str_len - len; i++)
				hexString = "0" + hexString;
		}

		return lowByteFront(hexString.toUpperCase());
	}

	// 把十六进制的字符串改为低字节在前，高字节在后
	private static String lowByteFront(String hexString) {
		String result = "";
		int len = hexString.length();
		for (int i = 0; i < len / 2; i++) {
			result = result
					+ hexString.substring(len - (2 * i + 2), len - (2 * i));
		}
		return result;
	}

	// 将一个字节数据转为两个字条长度的十六进制字符串
	public static String toHexString(byte value) {
		String stmp = Integer.toHexString(value & 0xFF);
		return (stmp.length() == 1) ? "0" + stmp : stmp;
	}
}

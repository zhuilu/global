package com.global.util;

public class ByteUtils {

	public static byte[] intToBytes(int value, int length) {
		byte[] src = new byte[length];
		for (int i = 0; i < src.length; i++) {
			src[src.length - 1 - i] = (byte) ((value >> (8 * i)) & 0xFF);
		}
		return src;
	}

	public static String bytesToHexString(byte[] src) {
		StringBuilder stringBuilder = new StringBuilder("");
		if (src == null || src.length <= 0) {
			return "";
		}
		for (int i = 0; i < src.length; i++) {
			if (i % 16 == 0) {
				stringBuilder.append("\n");
			}
			int v = src[i] & 0xFF;
			String hv = Integer.toHexString(v);
			stringBuilder.append("0x");
			if (hv.length() < 2) {
				stringBuilder.append(0);
			}
			stringBuilder.append(hv + "\t");
		}
		return stringBuilder.toString();
	}

	public static byte[] intToByteArray(int iSource, int iArrayLen) {
		byte[] bLocalArr = new byte[iArrayLen];
		for (int i = 0; (i < 4) && (i < iArrayLen); i++) {
			bLocalArr[i] = (byte) (iSource >> 8 * i & 0xFF);

		}
		return bLocalArr;
	}

	public static byte getCheckCode(byte[] data, int byte_len) {
		byte result = 0;
		for (int i = 0; i < byte_len; i++) {
			result = (byte) (result + data[i]);
		}

		return result;
	}

	public static byte getCheckCode(byte[] data, int startPostion,
			int endPostion) {
		byte result = 0;
		for (int i = startPostion; i < endPostion; i++) {
			result = (byte) (result + data[i]);
		}

		return result;
	}
}

package com.thinta.device;

/**
 * 条码扫描头 操作操作类
 * 
 * @author ywj@Thinta
 * @version 0.1
 */
public class Laser {
	static {
		System.loadLibrary("laser");
	}

	/**
	 * 打开条码扫描头
	 * 
	 * @return 成功返回 True，失败返回 False
	 */
	public native static boolean open();

	/**
	 * 关闭条码扫描头
	 * 
	 * @return 成功返回 True，失败返回 False
	 */
	public native static boolean close();

	/**
	 * 查询条码扫描头状态
	 * 
	 * @return 条码扫描头未启用返回0，已启用返回1，查询失败返回-1
	 */
	public native static int status();

	/**
	 * 初始化条码扫描头（恢复出厂设置、配置振中默认参数）
	 * 
	 * @return 成功返回 True，失败返回 False
	 */
	public native static boolean init();

	/**
	 * 软触发条码扫描头出光，并返回得到的条码
	 * 
	 * @return 扫描得到的条码内容，失败返回 null
	 */
	public native static String softScan();

	/**
	 * 设置扫描头参数
	 * 
	 * @param paramNum
	 *            扫描头设置参数的编号
	 * @param values
	 *            设置参数的内容
	 * @return 成功返回 True，失败返回 False
	 */
	public native static boolean paramSend(int paramNum, byte[] values);

	/**
	 * 读取扫描头参数
	 * 
	 * @param paramNum
	 *            扫描头参数的编号
	 * @return 从扫描头内读取到的参数值
	 */
	public native static byte[] paramRequest(int paramNum);
}

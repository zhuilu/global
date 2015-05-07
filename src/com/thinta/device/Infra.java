package com.thinta.device;

/**
 * 电力红外 操作操作类
 * 
 * @author ywj@Thinta
 * @version 0.1
 */
public class Infra {
	static {
		System.loadLibrary("infra");
	}

	/**
	 * 打开电力红外
	 * 
	 * @return 成功返回 True，失败返回 False
	 */
	public native static boolean open();

	/**
	 * 关闭电力红外
	 * 
	 * @return 成功返回 True，失败返回 False
	 */
	public native static boolean close();

	/**
	 * 查询电力红外状态
	 * 
	 * @return 电力红外未启用返回0，已启用返回1，查询失败返回-1
	 */
	public native static int status();

	/**
	 * 设置电力红外通讯参数
	 * 
	 * @param baudRate
	 *            波特率，支持 300、600、1200、2400、4800、9600、19200、38400、57600、115200 等
	 * @param parity
	 *            校验位，0:无校验，1:奇校验，2:偶校验
	 * @param dataBits
	 *            数据位，5:5个数据位，6:6个数据位，7:7个数据位，8:8个数据位
	 * @param stopBits
	 *            停止位，1:一个停止位，2:两个停止位
	 * @return 成功返回 True，失败返回 False
	 */
	public native static boolean set(int baudRate, int parity, int dataBits,
			int stopBits);

	/**
	 * 通过电力红外发送数据
	 * 
	 * @param buffer
	 *            发送的数据字节数组
	 * @param offset
	 *            发送数据在字节数组中的起始位置
	 * @param length
	 *            发送数据的长度
	 * @return 实际发送的字节数
	 */
	public native static int send(byte[] buffer, int offset, int length);

	/**
	 * 通过电力红外接收数据，单次最大接收512字节
	 * 
	 * @param timeout
	 *            接收超时时间，单位毫秒
	 * @param partTimeout
	 *            数据中断间隔超时，单位毫秒，为0表示使用默认值
	 * @return 接收到的数据内容
	 */
	public native static byte[] receive(int timeout, int partTimeout);
}

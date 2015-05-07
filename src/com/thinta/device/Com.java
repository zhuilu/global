package com.thinta.device;

/* 电力串口 操作操作类
 */
public class Com {
	static {
		System.loadLibrary("comtest");
	}

	/*
	 * open 打开串口 参数: 无 返回: 成功返回 true，失败返回 false
	 */
	public native static int open();

	/*
	 * close 关闭串口 参数: 无 返回: 成功返回 true，失败返回false
	 */
	public native static int close();

	/*
	 * set 设置串口通讯参数 参数: baudRate 波特率，支持
	 * 300、600、1200、2400、4800、9600、19200、38400、57600、115200 等 parity 校验位 0:无校验
	 * 1:奇校验 2:偶校验 dataBits 数据位 5:5个数据位 6:6个数据位 7:7个数据位 8:8个数据位 stopBits 停止位
	 * 1:一个停止位 2:两个停止位 返回: 成功返回 true，失败返回 true
	 */
	public native static boolean set(int baudRate, int parity, int dataBits,
			int stopBits);

	/*
	 * send 通过电力红外发送数据 参数: buffer 发送的数据字节数组 offset 发送数据在字节数组中的起始位置 length
	 * 发送数据的长度 返回: 实际发送的字节数
	 */
	public native static int send(byte[] buffer, int offset, int length);

	/*
	 * receive 通过电力红外接收数据，单次最大接收512字节 参数: timeout 接收超时时间，单位毫秒，<=0表示使用默认超时，2000毫秒
	 * partTime 开始接收数据后的最大超时时间，<=0表示使用默认超时 默认超时根据波特率不同而不同 波特率<600，默认超时750ms
	 * 波特率>=600，<1200，默认超时500ms 波特率>=1200，默认超时250ms 返回: 接收到的数据内容
	 */
	public native static byte[] receive(int timeout);
}

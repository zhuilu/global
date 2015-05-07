package com.example.transport;

import com.example.Others.Global;
import com.example.Others.Tool;
import com.longshine.serial.BlueToothManager;

public class ProtocolParser {

	/**
	 * 解析接收到的报文
	 * 
	 * @param obj
	 *            ：接收到的报文
	 * @param back_obj
	 *            ：解析后的报文返回到此对象的回调函数中
	 * @return
	 */
	public boolean unpackProtocol(Object obj, Object back_obj, int cmd_num) {
		if (obj == null)
			return false;

		Global.INFRA_REC.put("返回报文" + Global.REC_CMD_NUM, (byte[]) obj);
		((BlueToothManager) back_obj).receiveIRData(Global.INFRA_REC);

		int _eventID = -1;

		_eventID = ((byte[]) obj)[1];// 接收到报文的命令类型

		System.out.println("--------------ProtocolParser-------------");
		System.out.println("eventId====" + _eventID);

		if (_eventID == (byte) 0x91)// 表示是红外返回正常应答的报文
		{
			byte[] msg_645 = get645((byte[]) obj);
			Global.REC_CMD_NUM++;
			Global.INFRA_REC.put("返回报文" + Global.REC_CMD_NUM, msg_645);
			if (Global.REC_CMD_NUM == cmd_num) {
				((BlueToothManager) back_obj).receiveIRData(Global.INFRA_REC);
			}
		} else if (_eventID == (byte) 0xD1)// 红外返回异常报文
		{
			String err = getErrorFlag((byte[]) obj);
			((BlueToothManager) back_obj).overTimeMessage(err);
		} else if (_eventID == (byte) 0x95)// 表示是扫描返回正常应答的报文
		{
			String scan_msg = getScan_msg((byte[]) obj);
			((BlueToothManager) back_obj).receiveBarCode(scan_msg);
		} else if (_eventID == (byte) 0xB7)// 设置红外通讯参数成功
		{
			Global.SET_INFRA_COMM_PARAM = 1;
		} else if (_eventID == (byte) 0xF7)// 设置红外通讯参数失败
		{
			Global.SET_INFRA_COMM_PARAM = 0;
		} else if (_eventID == (byte) 0xB8)// 读取红外通讯参数成功
		{
			Global.GET_INFRA_COMM_PARAM = 1;
			int[] params = getMoudleParamValue((byte[]) obj);
			Global.INFRA_PARAMS = "abud_rate: " + params[0] + "\nparity: "
					+ params[1] + "\ndata_bits:" + params[2] + "\nstop_bits:"
					+ params[3] + "\n";
		} else if (_eventID == (byte) 0xF8)// 读取红外通讯参数失败
		{
			Global.GET_INFRA_COMM_PARAM = 0;
		} else if (_eventID == (byte) 0xB1)// 设置红外超时时间成功
		{
			Global.SET_OVER_TIME = 1;
		} else if (_eventID == (byte) 0xF1)// 设置红外超时时间超时
		{
			Global.SET_OVER_TIME = 0;
		}

		return true;
	}

	// 获取红外通讯参数的值
	public static int[] getMoudleParamValue(byte[] msg_data) {
		int result[] = new int[4];

		int data_filed_len = getDataFiledLen(msg_data);
		byte[] param_value = new byte[data_filed_len - 1];// 数据域的第一个字节为模块参数标识，不属于参数值
		System.arraycopy(msg_data, 7, param_value, 0, data_filed_len - 1);// 将数据域拷贝出来

		result[0] = Tool.byteToInt(param_value, 0, 4);// 波特率
		result[1] = Tool.byteToInt(param_value, 4, 1);// 校验位
		result[2] = Tool.byteToInt(param_value, 5, 1);// 数据位
		result[3] = Tool.byteToInt(param_value, 6, 1);// 停止位

		return result;
	}

	// 获取错误标识
	public static String getErrorFlag(byte[] msg_data) {
		String result = "";

		int data_filed_len = getDataFiledLen(msg_data);
		byte[] value = new byte[data_filed_len];
		System.arraycopy(msg_data, 6, value, 0, data_filed_len);// 将数据域拷贝出来

		int error = Tool.byteToInt(value, 0, 2);
		if (error == 0x0001) {
			result = "红外通讯接收超时";
		} else if (error == 0x1001) {
			result = "PAD发送报文，X4校验失败";
		} else if (error == 0x1002) {
			result = "红外任务已满";
		} else {
			result = "其他错误";
		}

		return result;
	}

	public String getScan_msg(byte[] data) {
		String result = "";

		int data_filed_len = getDataFiledLen(data);
		byte[] value = new byte[data_filed_len];
		System.arraycopy(data, 6, value, 0, data_filed_len);// 将数据域拷贝出来
		result = new String(value);

		return result;
	}

	private byte[] get645(byte[] data) {
		byte[] result = null;

		int data_filed_len = getDataFiledLen(data);
		result = new byte[data_filed_len];
		System.arraycopy(data, 6, result, 0, data_filed_len);// 将数据域拷贝出来

		return result;
	}

	// 获取数据域长度
	private static int getDataFiledLen(byte[] msg_data) {
		byte[] temp_buf = new byte[2];
		temp_buf[0] = msg_data[3];
		temp_buf[1] = msg_data[4];
		return Tool.byteToInt(temp_buf, 0, 2);// 数据域长度
	}
}

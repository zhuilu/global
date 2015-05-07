package com.example.Others;

import java.util.Arrays;

/*
 * 自定义缓冲区的类
 */
public class CustomBuffer {

	// 判断整个报文是否完整的标识
	private final static int MSG_START_FLAG = -55;// （0XC9）
	private final static int MSG_STOP_FLAG = -100;// （0X9C）

	private static int BUFFER_SIZE = 4096;// 缓冲区的大小
	private byte[] mBuffer = new byte[BUFFER_SIZE];
	private int effective_len = 0;// 缓冲区中有效数据的长度

	public int getEffectiveLen() {
		return effective_len;
	}

	// 往缓冲区中追加数据
	public void InData(byte[] in_data, int data_len) {
		System.arraycopy(in_data, 0, mBuffer, effective_len, data_len);
		effective_len += data_len;
	}

	/**
	 * 从缓冲区中读取整个报文的数据(flag = 0为不正常报文，flag = 1为正常报文)
	 * 
	 * @param out_data
	 *            读取出来的整个报文
	 * @param out_len
	 *            报文的字节数
	 * @return
	 */
	public int GetCompleteMessage(byte[] out_data, int[] out_len) {
		int flag = -1;

		int start_index = 0;// 报文开始的下标
		int stop_index = 0;// 报文结束的下标

		for (int start = 0; start < BUFFER_SIZE; start++) {
			if (mBuffer[0] != MSG_START_FLAG || mBuffer[5] != MSG_START_FLAG) // 起始符和标识符正确
			{
				continue;
			}

			start_index = start;

			byte[] temp_buf = new byte[2];
			temp_buf[0] = mBuffer[start_index + 3];
			temp_buf[1] = mBuffer[start_index + 4];
			int data_filed_len = Tool.byteToInt(temp_buf, 0, 2);// 数据域长度

			System.out.println("=======data_filed_len ======" + data_filed_len);

			out_len[0] = 8 + data_filed_len;

			stop_index = start + out_len[0] - 1;

			if (mBuffer[stop_index] == MSG_STOP_FLAG)// 结束符正确
			{
				byte check_code = mBuffer[stop_index - 1];
				byte temp_code = 0;
				for (int i = start_index; i <= stop_index - 2; i++) {
					temp_code = (byte) (temp_code + mBuffer[i]);
				}
				if (temp_code == check_code) {// 检验码正确
					// 是正常完整报文，返回
					flag = 1;
					System.arraycopy(mBuffer, start_index, out_data, 0,
							out_len[0]);// 将整个报文读出来
					if (out_len[0] <= effective_len) {

						effective_len -= 8 + data_filed_len;
					} else {
						Arrays.fill(mBuffer, (byte) 0);
						effective_len = 0;
					}
				} else {
					// 是不正常报文，返回
					flag = 0;
					if (out_len[0] <= effective_len) {
						System.arraycopy(mBuffer, out_len[0], mBuffer, 0,
								effective_len - out_len[0]);
						effective_len -= 8 + data_filed_len;
					} else {
						Arrays.fill(mBuffer, (byte) 0);
						effective_len = 0;
					}
				}
			}
			return flag;
		}
		return flag;
	}

}

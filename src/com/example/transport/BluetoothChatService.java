package com.example.transport;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.bluetooth.BluetoothSocket;

import com.example.Others.CustomBuffer;
import com.example.Others.Global;
import com.example.Others.Tool;

public class BluetoothChatService {

	private ConnectedThread mConnectedThread = null;
	private ProtocolParser mProtocolParser = null;// 协议分析类
	private CustomBuffer custom_buffer = null;
	private ReadThread mReadThread = null;
	private Object obj = null;
	public boolean registerReceiver = false;
	private int cmd_num = 0;

	private static BluetoothChatService bt_service = null;

	public static BluetoothChatService GetInstance() {
		if (bt_service == null) {
			bt_service = new BluetoothChatService();
		}

		return bt_service;
	}

	private BluetoothChatService() {
		mProtocolParser = new ProtocolParser();
		custom_buffer = new CustomBuffer();
	}

	public void addListener(Object obj, int cmd_num) {
		this.obj = obj;
		this.cmd_num = cmd_num;
	}

	public void Start(BluetoothSocket socket) {
		System.out.println("BluetoothChatService=======Start");
		if (mConnectedThread == null) {
			// 启动线程管理连接和传输
			mConnectedThread = new ConnectedThread(socket);
			mConnectedThread.start();
			System.out.println("mConnectedThread=======start");
		}

		if (mReadThread == null) {
			mReadThread = new ReadThread();
			mReadThread.start();
			System.out.println("mReadThread=======start");
		}
	}

	public void Stop() {

		if (mConnectedThread != null) {
			mConnectedThread.cancel();
			mConnectedThread = null;
		}

		if (mReadThread != null) {
			mReadThread.cancel();
			mReadThread = null;
		}
	}

	public void write(byte[] out) {
		// 创建临时对象
		ConnectedThread r;

		if (Global.LINK_STATE != 1)
			return;

		if (mConnectedThread != null) {
			r = mConnectedThread;

			// 执行写同步
			r.write(out);
		}
	}

	// 本线程负责处理所有的传入和传出（连接之后的管理线程）
	private class ConnectedThread extends Thread {
		private BluetoothSocket mmSocket;
		private InputStream mmInStream;
		private OutputStream mmOutStream;

		public ConnectedThread(BluetoothSocket socket) {
			mmSocket = socket;
			InputStream tmpIn = null;
			OutputStream tmpOut = null;
			try {
				tmpIn = socket.getInputStream();
				tmpOut = socket.getOutputStream();
				System.out
						.println("BluetoothChatService===========getOutputStream===========");
			} catch (IOException e) {
				e.printStackTrace();
			}

			mmInStream = tmpIn;
			mmOutStream = tmpOut;
		}

		public void run() {
			byte[] rBuffer = new byte[1024];// 1024
			int read_len;

			while (true) {
				try {
					try {
						ConnectedThread.sleep(100);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					// 读入输入流，用于接收远程信息
					read_len = mmInStream.read(rBuffer);
					byte[] temp_buffer = new byte[read_len];
					System.arraycopy(rBuffer, 0, temp_buffer, 0, read_len);
					custom_buffer.InData(temp_buffer, read_len);
					System.out.println("-------------read----------------");

					try {
						ConnectedThread.sleep(500);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}

				} catch (IOException e) {
					System.out.println("BluetoothChatService Lost ");
					// 装置连接丢失
					Global.LINK_STATE = 0;
					break;
				}
			}
		}

		public void write(byte[] buffer) {
			try {
				// 得到输出流，用于发送给远程的信息
				mmOutStream.write(buffer);
				System.out.println("------write--------");

			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		public void cancel() {
			try {
				mmSocket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private class ReadThread extends Thread {
		boolean flag = true;

		public void run() {
			while (flag) {
				if (custom_buffer.getEffectiveLen() >= 8) {
					byte[] read_buffer = new byte[1024];
					int[] len = new int[1];
					int flag = custom_buffer.GetCompleteMessage(read_buffer,
							len);
					byte[] tmp = new byte[len[0]];
					System.arraycopy(read_buffer, 0, tmp, 0, len[0]);
					if (flag == 1) {
						System.out
								.println("返回数据：" + Tool.bytesToHexString(tmp));
						if (obj != null) {
							mProtocolParser.unpackProtocol(tmp, obj, cmd_num);
						}
						if (registerReceiver) {
							write(tmp);
						}
					} else {
						// 错误报文
						System.out.println("错误报文！");
					}
				}
			}
		}

		public void cancel() {
			flag = false;
		}

	}

}

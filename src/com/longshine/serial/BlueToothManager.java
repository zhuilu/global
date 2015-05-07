package com.longshine.serial;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import com.example.Others.Global;
import com.example.Others.Tool;
import com.example.bluetooth.BluetoothConnectActivityReceiver;
import com.example.transport.BluetoothChatService;
import com.global.classR.ClsUtils;
import com.global.toast.Toaster;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.widget.Toast;

public class BlueToothManager {
	private Handler mHandler;
	private BluetoothAdapter bluetooth_adapter = null;// 蓝牙适配器
	private String deviceName = "Lianway";
	private BluetoothDevice mConnectedDevice = null;
	ConnectThread m_connect_thread = null;

	public static int open_result = 0;

	private Context mContext;

	/**
	 * 构造方法
	 * 
	 * @param context
	 *            上下文
	 */
	public BlueToothManager(Context context) {
		this.mContext = context;
	}

	public void setUiHandler(Handler handler) {
		mHandler = handler;
	}

	/**
	 * 红外读示报文回调
	 * 
	 * @param data
	 *            返回红外读数报文
	 */
	public void receiveIRData(Map<String, byte[]> data) {

		if (mHandler != null) {
			Message msg = mHandler.obtainMessage(0x5001, data);
			mHandler.sendMessage(msg);
		}
	}

	/**
	 * 接收条码回调
	 * 
	 * @param data
	 *            返回条码
	 */
	public void receiveBarCode(String barcode) {
		if (mHandler != null) {
			Message msg = mHandler.obtainMessage(0x5002, barcode);
			mHandler.sendMessage(msg);
		}
	}

	/**
	 * 红外读数超时后回调
	 * 
	 * @param msg
	 */
	public void overTimeMessage(String msg) {// 红外通讯接收超时

		if (mHandler != null) {
			Message omsg = mHandler.obtainMessage(0x5003, msg);
			mHandler.sendMessage(omsg);
		}
	}

	/**
	 * 打开串口
	 * 
	 * @param context
	 * @return
	 */
	public int open(Context context)// 打开蓝牙并连接
	{
		if (Global.LINK_STATE == 1) {
			return 1;
		}
		// 打开蓝牙设备
		openBluetooth();
		// 注册蓝牙请求配对广播接收器
		BluetoothConnectActivityReceiver receiver = new BluetoothConnectActivityReceiver();
		IntentFilter intentRequestPaird = new IntentFilter();
		intentRequestPaird
				.addAction("android.bluetooth.device.action.PAIRING_REQUEST");
		intentRequestPaird.addAction(BluetoothDevice.ACTION_FOUND);
		intentRequestPaird.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
		intentRequestPaird.addAction(BluetoothDevice.ACTION_FOUND);
		intentRequestPaird.addAction(BluetoothDevice.ACTION_FOUND);
		context.registerReceiver(receiver, intentRequestPaird);

		// 注册蓝牙设备扫描广播接收器
		IntentFilter intentFilter = new IntentFilter(
				BluetoothDevice.ACTION_FOUND);
		context.registerReceiver(bReceiver, intentFilter);
		BluetoothChatService.GetInstance().addListener(this, 0);

		boolean isPaired = false;
		Set<BluetoothDevice> pairedDevices = bluetooth_adapter
				.getBondedDevices();
		// 判断已配对的设备中是否有Lianway
		if (pairedDevices.size() > 0) {
			for (BluetoothDevice device : pairedDevices) {
				if (device.getName().equals(deviceName)) {
					this.mConnectedDevice = device;
					isPaired = true;
					break;
				}
			}
		}
		if (isPaired) {
			// 如果已经配对则直接连接
			// 启动线程连接的设备
			m_connect_thread = new ConnectThread(mConnectedDevice);
			m_connect_thread.start();
			try {
				m_connect_thread.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		} else {
			scanDevice t_scan = new scanDevice();
			t_scan.start();

			try {
				t_scan.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		return open_result;
	}

	/**
	 * 关闭串口
	 * 
	 * @return 0:失败 1：成功
	 */
	public int close()// 断开连接
	{
		int result = 1;
		if (bReceiver != null) {
			this.mContext.unregisterReceiver(bReceiver);
		}

		BluetoothChatService.GetInstance().Stop();

		if (m_connect_thread != null) {
			m_connect_thread.cancel();
			m_connect_thread = null;
		}

		return result;
	}

	/**
	 * 发送红外读数指令
	 * 
	 * @param address
	 *            表地址
	 * @param data
	 *            多条指令
	 * @return
	 */
	public int sendData(Map<String, byte[]> data) {
		// 清除红外接收结果的列表
		Global.INFRA_REC.clear();

		int result = 0;
		byte[] frame = null;// 得到645报文

		Global.REC_CMD_NUM = 0;

		BluetoothChatService.GetInstance().addListener(this, data.size());

		Iterator<String> it = data.keySet().iterator();
		int sendTime = 0;
		while (it.hasNext()) {
			String key = (String) it.next();
			frame = data.get(key);
			// 把byte数组转换为十六进制字符串
			// String msg_645 = Tool.bytesToHexString(frame);
			// int data_filed_len = frame.length;
			// String hex_len = Tool.IntToHexString(data_filed_len, 4);
			// String cmd_sequence_id = "01";// ((Global.CMD_ID++)%255) 命令序号
			// String tmp_msg = "C911" + cmd_sequence_id + hex_len + "C9"
			// + msg_645;// 起始符到数据域间的数据，用来计算校验码
			// String check_code = Tool.getCheckCode(tmp_msg);
			// String message = tmp_msg + check_code + "9C";
			// byte[] send = Tool.hexStringToBytes(message);
			// 发送
			BluetoothChatService.GetInstance().write(frame);
			sendTime++;
		}

		if (sendTime == data.size()) {
			result = 1;
		} else {
			result = 0;
		}
		return result;
	}

	// ===============================================================
	// 打开蓝牙设备
	private void openBluetooth() {
		// 检查设备是否支持蓝牙
		bluetooth_adapter = BluetoothAdapter.getDefaultAdapter();
		if (bluetooth_adapter == null) {
			Toast.makeText(this.mContext, "此设备不支持蓝牙！", Toast.LENGTH_SHORT)
					.show();
			return;
		}
		// 打开蓝牙

		if (!bluetooth_adapter.isEnabled())// isEnabled()蓝牙功能是否启用
		{
			bluetooth_adapter.enable();
			// Intent intent = new
			// Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
			// // 设置蓝牙可见性，最多300秒
			// intent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION,
			// 800);
			// this.mContext.startActivity(intent);
		}
	}

	private BroadcastReceiver bReceiver = new BroadcastReceiver() {
		public void onReceive(android.content.Context context, Intent intent) {
			try {
				if (BluetoothDevice.ACTION_FOUND.equals(intent.getAction())) {
					BluetoothDevice temdevice = intent
							.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
					System.out.println("搜索到设备===>设备名称为:====>"
							+ temdevice.getName() + temdevice.getAddress());
					if (temdevice.getName() != null
							&& temdevice.getName().trim().equals(deviceName)) {
						mConnectedDevice = temdevice;
						if (mConnectedDevice != null) {
							if (connection(mConnectedDevice)) {
								// while (true) {
								System.out.println("connecting...");
								if (mConnectedDevice.getBondState() == BluetoothDevice.BOND_BONDED) {
									Toaster.getInstance().displayToast(
											"该设备已绑定其它设备");
								}
								// 启动线程连接的设备
								m_connect_thread = new ConnectThread(
										mConnectedDevice);
								m_connect_thread.start();
								try {
									m_connect_thread.join();
									return;
								} catch (InterruptedException e) {
									e.printStackTrace();
								}
								// }
								// }
							}
						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		};
	};

	public void connectFail() {
		Toaster.getInstance().displayToast("connet bluetooth fail");
		// 无法扫描到蓝牙设备Lianway
		setState("无法扫描到蓝牙设备" + getDeviceName());
		open_result = 0;
		Global.LINK_STATE = 0;
	}

	private class scanDevice extends Thread {
		@Override
		public void run() {
			mConnectedDevice = null;
			bluetooth_adapter.startDiscovery();
		}
	}

	private void setState(String state) {
		Message msg = mHandler.obtainMessage(0x5004, state);
		mHandler.sendMessage(msg);
	}

	private class ConnectThread extends Thread {
		private BluetoothSocket mmSocket;

		public ConnectThread(BluetoothDevice device) {

			BluetoothSocket tmp = null;
			try {
				Method m = device.getClass().getMethod("createRfcommSocket",
						new Class[] { int.class });
				tmp = (BluetoothSocket) m.invoke(device, 1);
				mmSocket = tmp;
			} catch (Exception e) {
				e.printStackTrace();
			}

		}

		public void run() {
			bluetooth_adapter.cancelDiscovery();
			try {
				mmSocket.connect();
				Toaster.getInstance().displayToast("蓝牙连接成功");
				BluetoothChatService.GetInstance().Start(mmSocket);
				open_result = 1;
				Global.LINK_STATE = 1;
			} catch (IOException e) {
				open_result = 0;
				// 关闭这个socket
				try {
					mmSocket.close();
				} catch (IOException e2) {
				}
			}

		}

		public void cancel() {
			try {
				mmSocket.close();
			} catch (IOException e) {
			}
		}
	}

	public boolean connection(BluetoothDevice device) {
		boolean result = false;
		int pairNum = 0;
		if (device != null) {
			try {
				System.out.println("currentBondState=====>"
						+ device.getBondState());
				if (device.getBondState() != BluetoothDevice.BOND_BONDED) {// 判断是否已配对

					try {
						boolean setFlag = ClsUtils.setPin(device.getClass(),
								device, Global.PIN_ID);
						if (setFlag) {
							System.out.println("自动配对设置成功");
						} else {
							System.out.println("自动配对设置失败");
							// return false;
						}

					} catch (Exception e) {
						e.printStackTrace();
						System.out.println("自动配对设置失败");
						return false;
					}// 设置pin值
					boolean returnValue = false;
					while (!returnValue && pairNum < 50) {
						try {
							returnValue = ClsUtils.createBond(
									device.getClass(), device);
						} catch (Exception e) {
							e.printStackTrace();
							System.out.println("配对失败");
						}
						pairNum++;
					}
					if (pairNum == 50) {
						result = false;
					} else {
						result = true;
					}
				} else {
					return true;
				}
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
				return false;
			}
		}

		return result;
	}

	public String getDeviceName() {
		return deviceName;
	}

	public void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
	}
}

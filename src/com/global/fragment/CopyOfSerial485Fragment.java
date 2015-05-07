package com.global.fragment;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.transport.BluetoothChatService;
import com.global.R;
import com.global.db.DatabaseManager;
import com.global.format.Format;
import com.global.toast.Toaster;
import com.global.util.BcdUtils;
import com.global.util.ByteUtils;
import com.longshine.serial.BlueToothManager;
import com.thinta.device.Com;
import com.thinta.device.Infra;

public class CopyOfSerial485Fragment extends BaseFragment implements
		OnClickListener,

		android.widget.CheckBox.OnCheckedChangeListener {
	Boolean revFlag = false;
	TextView revTextView = null;
	Timer timer;
	Spinner parity;
	Spinner baudRate;
	ScrollView scrollView;
	static View view;
	boolean serialflag;// 串口是否开启
	BlueToothManager blueTooth;
	static CheckBox type_bluetooth;
	Handler msgHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0x1111:
				byte[] rev = (byte[]) msg.obj;
				if (rev.length > 0) {
					StringBuilder value = new StringBuilder();
					for (int i = 0; i < rev.length; i++) {
						value.append(String.format("%02X", rev[i]) + " ");
					}

					revTextView.append("\n <<<"
							+ Format.getDateFormat().format(new Date()) + ":"
							+ "\n" + value + "\n");
					msgHandler.sendEmptyMessageDelayed(0x1122, 200);
				}
				break;
			case 0x1110:
				byte[] send = (byte[]) msg.obj;
				if (send.length > 0) {
					StringBuilder value = new StringBuilder();
					for (int i = 0; i < send.length; i++) {
						value.append(String.format("%02X", send[i]) + " ");
					}
					revTextView.append("\n 发送："
							+ Format.getDateFormat().format(new Date()) + ":"
							+ "\n" + value + "\n");
					System.out.println(" 发送：=" + value);
					msgHandler.sendEmptyMessageDelayed(0x1122, 200);
				}
				break;
			case 0x1000:
				revTextView.append("\n"
						+ Format.getDateFormat().format(new Date()) + ":"
						+ "\n" + (String) msg.obj + "\n");
				scrollView.fullScroll(ScrollView.FOCUS_DOWN);
				break;
			case 0x1122:
				scrollView.fullScroll(ScrollView.FOCUS_DOWN);
				break;

			case 0x5001:
				Map<String, byte[]> result = (Map<String, byte[]>) msg.obj;
				Iterator<String> iter = result.keySet().iterator();
				while (iter.hasNext()) {
					String key = iter.next();
					byte[] t = result.get(key);
					StringBuilder strb = new StringBuilder();
					for (int j = 0; j < t.length; j++)
						strb.append(" "
								+ Integer.toHexString(((int) t[j] & 0xFF)));

					revTextView.append("\n 蓝牙报文 [" + key + "]:"
							+ strb.toString() + "\n");
				}
				msgHandler.sendEmptyMessageDelayed(0x1122, 200);
				break;
			case 0x5002:
				revTextView.append("\n 条码: " + (String) msg.obj + "\n");
				msgHandler.sendEmptyMessageDelayed(0x1122, 200);
				break;
			case 0x5003:
				revTextView.append("\n" + (String) msg.obj + "\n");
				msgHandler.sendEmptyMessageDelayed(0x1122, 200);
				break;
			case 0x5004:// 扫描蓝牙的状态变化

				revTextView.append("\n " + (String) msg.obj);
				msgHandler.sendEmptyMessageDelayed(0x1122, 200);
				break;

			}
		}
	};

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.copy_of_serial485, null);
		revTextView = (TextView) view.findViewById(R.id.textViewRev);
		scrollView = (ScrollView) view.findViewById(R.id.scrollView);
		type_bluetooth = (CheckBox) view.findViewById(R.id.type_bluetooth);
		baudRate = (Spinner) view.findViewById(R.id.baudRate);
		parity = (Spinner) view.findViewById(R.id.parity);
		type_bluetooth.setOnCheckedChangeListener(this);
		view.findViewById(R.id.buttonOpen).setOnClickListener(this);
		view.findViewById(R.id.buttonSend).setOnClickListener(this);
		view.findViewById(R.id.buttonClose).setOnClickListener(this);
		view.findViewById(R.id.buttonZaiboSend).setOnClickListener(this);
		view.findViewById(R.id.buttonZaiboSendWifi).setOnClickListener(this);
		view.findViewById(R.id.buttonZaiboSendTest_cjA1_02).setOnClickListener(
				this);
		view.findViewById(R.id.buttonZaiboReadTimeTest)
				.setOnClickListener(this);
		view.findViewById(R.id.buttonZaiboSend_CaijiA1_01).setOnClickListener(
				this);
		view.findViewById(R.id.buttonZaiboSend_CaijiA3)
				.setOnClickListener(this);
		view.findViewById(R.id.buttonZaiboSend_jzq_infra_zhongjireadAdress)
				.setOnClickListener(this);
		view.findViewById(R.id.buttonZaiboSendTest_JZ_A1).setOnClickListener(
				this);
		view.findViewById(R.id.buttonZaiboSend_jzq_infra_serial_readbj)
				.setOnClickListener(this);

		return view;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
	}

	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.buttonOpen:
			int ret = Com.open();
			if (ret == 0) {
				revFlag = true;
				serialflag = Com.set(
						Integer.valueOf(baudRate.getSelectedItem().toString()),
						Integer.valueOf(parity.getSelectedItem().toString()),
						8, 1);
				Toast.makeText(
						getActivity(),
						"串口打开成功,波特率="
								+ Integer.valueOf(baudRate.getSelectedItem()
										.toString())
								+ " 效验位="
								+ Integer.valueOf(parity.getSelectedItem()
										.toString()) + serialflag,
						Toast.LENGTH_SHORT).show();
			} else {
				Toast.makeText(getActivity(), "串口打开失败:" + ret,
						Toast.LENGTH_SHORT).show();
			}
			break;
		case R.id.buttonSend:
			byte[] buf = new byte[] { (byte) 0xFE, (byte) 0xFE, (byte) 0xFE,
					(byte) 0xFE, (byte) 0x68, (byte) 0xAA, (byte) 0xAA,
					(byte) 0xAA, (byte) 0xAA, (byte) 0xAA, (byte) 0xAA,
					(byte) 0x68, (byte) 0x11, (byte) 0x04, (byte) 0x35,
					(byte) 0x34, (byte) 0x33, (byte) 0x37, (byte) 0xB4,
					(byte) 0x16 };
			sendMessage(buf);

			break;
		case R.id.buttonZaiboSendTest_JZ_A1:
			byte[] zaiboSendJZ_A1 = new byte[] { (byte) 0xc9, (byte) 0x02,
					(byte) 0x00, (byte) 0x08, (byte) 0x00, (byte) 0xc9,
					(byte) 0x01, (byte) 0x78, (byte) 0x04, (byte) 0x79,
					(byte) 0x11, (byte) 0x00, (byte) 0x00, (byte) 0x03,
					(byte) 0xa6, (byte) 0x9c };
			sendMessage(zaiboSendJZ_A1);
			break;
		case R.id.buttonZaiboSend:
			byte[] zaiboSend = new byte[] { (byte) 0xFE, (byte) 0xFE,
					(byte) 0xFE, (byte) 0xFE, (byte) 0x68, (byte) 0x78,
					(byte) 0x04, (byte) 0x79, (byte) 0x11, (byte) 0x00,
					(byte) 0x00, (byte) 0x68, (byte) 0x11, (byte) 0x04,
					(byte) 0x35, (byte) 0x34, (byte) 0x33, (byte) 0x37,
					(byte) 0xBE, (byte) 0x16 };
			sendMessage(zaiboSend);
			break;
		case R.id.buttonZaiboSendTest_cjA1_02:
			byte[] send2 = new byte[] { (byte) 0xC9, (byte) 0x02, (byte) 0x08,
					(byte) 0x02, (byte) 0x00, (byte) 0xC9, (byte) 0x03,
					(byte) 0x02, (byte) 0xA3, (byte) 0x9C };
			sendMessage(send2);
			break;
		case R.id.buttonZaiboSend_jzq_infra_zhongjireadAdress:
			BluetoothChatService.GetInstance().registerReceiver = true;
			byte[] zaiboSendCaijijzq_readAdress = new byte[] { (byte) 0x68,
					(byte) 0xCA, (byte) 0x00, (byte) 0xCA, (byte) 0x00,
					(byte) 0x68, (byte) 0x5B, (byte) 0xFF, (byte) 0xFF,
					(byte) 0xFF, (byte) 0xFF, (byte) 0x14, (byte) 0x10,
					(byte) 0x71, (byte) 0x00, (byte) 0x00, (byte) 0x01,
					(byte) 0x00, (byte) 0x1F, (byte) 0x6B, (byte) 0x64,
					(byte) 0x64, (byte) 0x10, (byte) 0x00, (byte) 0x68,
					(byte) 0x78, (byte) 0x04, (byte) 0x79, (byte) 0x11,
					(byte) 0x00, (byte) 0x00, (byte) 0x68, (byte) 0x11,
					(byte) 0x04, (byte) 0x33, (byte) 0x33, (byte) 0x34,
					(byte) 0x33, (byte) 0xB8, (byte) 0x16, (byte) 0x11,
					(byte) 0x11, (byte) 0x11, (byte) 0x11, (byte) 0x11,
					(byte) 0x11, (byte) 0x11, (byte) 0x11, (byte) 0x11,
					(byte) 0x11, (byte) 0x11, (byte) 0x11, (byte) 0x11,
					(byte) 0x11, (byte) 0x11, (byte) 0x11, (byte) 0xE5,
					(byte) 0x16 };
			sendMessage(zaiboSendCaijijzq_readAdress);
			break;
		case R.id.buttonZaiboSend_jzq_infra_serial_readbj:
			if (!serialflag) {
				Toaster.getInstance().displayToast("请先打开串口");
				return;
			}
			byte[] zaiboSendCaijijzq_infra_serial_readbj = new byte[] {
					(byte) 0xc9, (byte) 0x0b, (byte) 0x0c, (byte) 0x17,
					(byte) 0x00, (byte) 0xc9, (byte) 0x01, (byte) 0x07,
					(byte) 0x02, (byte) 0x12, (byte) 0x20, (byte) 0x00,
					(byte) 0x02, (byte) 0x68, (byte) 0x01, (byte) 0x07,
					(byte) 0x02, (byte) 0x12, (byte) 0x20, (byte) 0x00,
					(byte) 0x68, (byte) 0x11, (byte) 0x04, (byte) 0x33,
					(byte) 0x33, (byte) 0x34, (byte) 0x33, (byte) 0xee,
					(byte) 0x16, (byte) 0xf0, (byte) 0x9c };
			sendMessage(zaiboSendCaijijzq_infra_serial_readbj);

			Timer timer = new Timer();
			timer.scheduleAtFixedRate(new TimerTask() {
				@Override
				public void run() {
					new ReadAsyncTask().execute(new Integer[] { 35000, 25000 });
				}
			}, 2000, 60000);

			break;
		case R.id.buttonZaiboReadTimeTest:
			byte[] zaiboSendReadTime645 = new byte[] { (byte) 0x78,
					(byte) 0x04, (byte) 0x79, (byte) 0x11, (byte) 0x00,
					(byte) 0x00, (byte) 0x02, (byte) 0x68, (byte) 0x78,
					(byte) 0x04, (byte) 0x79, (byte) 0x11, (byte) 0x00,
					(byte) 0x00, (byte) 0x68, (byte) 0x11, (byte) 0x04,
					(byte) 0x35, (byte) 0x34, (byte) 0x33, (byte) 0x37,
					(byte) 0xBE, (byte) 0x16 };
			byte[] send = packMsg(new byte[] { 0x0B, 0x01 },
					zaiboSendReadTime645);
			sendMessage(send);
			break;
		case R.id.buttonZaiboSendWifi:
			// byte[] zaiboSendWifi = new byte[] { (byte) 0xc9, (byte) 0x0c,
			// (byte) 0x03, (byte) 0x02, (byte) 0x00, (byte) 0xc9,
			// (byte) 0x00, (byte) 0x01, (byte) 0xa4, (byte) 0x9c };
			//
			EditText ljdz = (EditText) view.findViewById(R.id.id_edittext_ljdz);
			EditText ip = (EditText) view.findViewById(R.id.id_edittext_ip);
			EditText port = (EditText) view.findViewById(R.id.id_edittext_port);
			EditText apn = (EditText) view.findViewById(R.id.id_edittext_apn);
			generalByteCheckSIM(ljdz.getText().toString(), ip.getText()
					.toString(), port.getText().toString(), apn.getText()
					.toString());
			// sendMessage(zaiboSendWifi);

			break;
		case R.id.buttonZaiboSend_CaijiA1_01:
			byte[] zaiboSendCaiji = new byte[] { (byte) 0xc9, (byte) 0x02,
					(byte) 0x08, (byte) 0x02, (byte) 0x00, (byte) 0xc9,
					(byte) 0x03, (byte) 0x01, (byte) 0xa2, (byte) 0x9c };
			sendMessage(zaiboSendCaiji);
			break;
		case R.id.buttonZaiboSend_CaijiA3:
			byte[] zaiboSendCaijiA1 = new byte[] { (byte) 0xc9, (byte) 0x02,
					(byte) 0x0a, (byte) 0x01, (byte) 0x00, (byte) 0xc9,
					(byte) 0x04, (byte) 0xa3, (byte) 0x9c };
			sendMessage(zaiboSendCaijiA1);
			break;

		case R.id.buttonClose:
			ret = Com.close();
			revFlag = false;
			if (ret == 0) {
				Toast.makeText(getActivity(), "串口关闭成功", Toast.LENGTH_SHORT)
						.show();
			} else {
				Toast.makeText(getActivity(), "串口关闭失败:" + ret,
						Toast.LENGTH_SHORT).show();
			}
			break;
		default:
			break;
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		Infra.open();
	}

	@Override
	public void onPause() {
		if (blueTooth != null) {
			blueTooth.close();
		}
		Infra.close();
		super.onPause();
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
	}

	@Override
	public void onDetach() {
		super.onDetach();
		Com.close();
	}

	public class ReadAsyncTask extends AsyncTask<Integer, Void, byte[]> {
		@Override
		protected byte[] doInBackground(Integer... mTimeOut) {
			try {
				byte[] buffer = Com.receive(mTimeOut[0]);
				if (buffer.length > 0) {
					return buffer;
				} else {
					buffer = Com.receive(mTimeOut[1]);
					if (buffer.length > 0) {
						return buffer;
					} else {
						Toaster.getInstance().displayToast("read fail");
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;

		}

		@Override
		protected void onPostExecute(byte[] result) {
			if (result != null) {
				Message msg = msgHandler.obtainMessage();
				msg.obj = result;
				msg.what = 0x1111;
				msgHandler.sendMessage(msg);
			}
			this.cancel(false);
		}
	}

	public static byte[] toByteArray(int iSource, int iArrayLen) {
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

	public void sendMessage(byte[] data) {
		showNext(data);
	}

	public void sendLog(String data) {
		Message msgZaibo = msgHandler.obtainMessage();
		msgZaibo.obj = data;
		msgZaibo.what = 0x1000;
		msgHandler.sendMessage(msgZaibo);

	}

	public void serialInit() {
		new BlueToothAsyncTask().execute(new Object[] {});
	}

	public class BlueToothAsyncTask extends AsyncTask<Object, Void, byte[]> {
		@Override
		protected byte[] doInBackground(Object... mInputStream) {
			try {
				blueTooth = new BlueToothManager(getActivity());
				blueTooth.setUiHandler(msgHandler);
				blueTooth.setDeviceName("ZZDZ");
				// serial.setDeviceName("Galaxy S4");
				blueTooth.open(getActivity());
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;

		}

	}

	public class DatabaseManagerAsyncTask extends
			AsyncTask<Object, Void, byte[]> {
		@Override
		protected byte[] doInBackground(Object... mInputStream) {
			try {
				DatabaseManager.getInstance().openDatabase();
				Thread.sleep(2000);
				DatabaseManager.getInstance().closeDatabase();
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;

		}

	}

	public class InfraAsyncTask extends AsyncTask<byte[], Void, byte[]> {
		@Override
		protected byte[] doInBackground(byte[]... mInputStream) {
			try {
				Infra.send(mInputStream[0], 0, mInputStream[0].length);
				return Infra.receive(3000, 500);
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}

		}

		@Override
		protected void onPostExecute(byte[] result) {
			if (result != null) {
				Message msg = msgHandler.obtainMessage();
				msg.obj = result;
				msg.what = 0x1111;
				msgHandler.sendMessage(msg);
			}
		}

	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		if (isChecked) {
			sendLog("开启蓝牙");
			serialInit();
		}
	}

	protected void showNext(final byte[] dataSend) {
		AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
		int position = 0;
		dialog.setSingleChoiceItems(new String[] { "红外", "串口", "蓝牙" },
				position, new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						switch (which) {
						case 0:
							new InfraAsyncTask().execute(dataSend);
							break;
						case 1:
							Com.send(dataSend, 0, dataSend.length);
							new ReadAsyncTask().execute(new Integer[] { 15000,
									55000 });
							break;
						case 2:
							if (blueTooth == null) {
								Toaster.getInstance().displayToast("请开启蓝牙");
								dialog.cancel();
								return;
							}
							Map<String, byte[]> param = new HashMap();
							param.put(System.currentTimeMillis() + "", dataSend);
							blueTooth.sendData(param);
							Message msgZaibo = msgHandler.obtainMessage();
							msgZaibo.obj = dataSend;
							msgZaibo.what = 0x1110;
							msgHandler.sendMessage(msgZaibo);
							break;
						default:
							break;
						}
						Message msgZaibo = msgHandler.obtainMessage();
						msgZaibo.obj = dataSend;
						msgZaibo.what = 0x1110;
						msgHandler.sendMessage(msgZaibo);
						dialog.cancel();
					}
				});
		dialog.setTitle("请选择通信类型");
		dialog.create().show();
	}

	/**
	 * SIM 卡测试
	 * 
	 * @param ljdz
	 * @param ip
	 * @param port
	 * @param apn
	 */
	// IP 可登录的主站IP地址（四个字节，BCD编码）；
	// LJDZ 逻辑地址 （四个字节，BCD编码）
	// PN 可登录的主站IP地址对应的端口号（两个字节，BIN编码）；
	// APN 网络APN字段（低字节在前高字节在后，16个字节，不足补0）；
	// TU 01表示TCP传输方式；
	// 02表示UDP传输方式；
	// UN 用户名字段（低字节在前高字节在后，16个字节，不足补0）；
	// PW 用户名密码字段（低字节在前高字节在后，16个字节，不足补0）。

	public void generalByteCheckSIM(String ljdz, String ip, String port,
			String apn) {
		if (ljdz == null || ljdz.trim().length() != 8) {
			Toaster.getInstance().displayToast("请输入8位逻辑地址");
			return;
		}
		if (ip == null || ip.trim().length() == 0) {
			Toaster.getInstance().displayToast("请输入ip");
			return;
		}
		if (port == null || port.trim().length() == 0) {
			Toaster.getInstance().displayToast("请输入端口号");
			return;
		}
		if (apn == null || apn.trim().length() == 0) {
			Toaster.getInstance().displayToast("请输入APN");
			return;
		}
		byte[] data01 = BcdUtils.reverseBytes(new byte[] { 0x01 });
		byte[] LJDZ = BcdUtils.reverseBytes(BcdUtils.stringToByteArray(ljdz));
		String[] p = ip.split("\\.");
		byte[] pArgu = new byte[4];
		for (int i = 0; i < p.length; i++) {
			pArgu[i] = (byte) ((int) (Integer.valueOf(p[i])));
		}
		byte[] IP = BcdUtils.reverseBytes(pArgu);
		System.out.println("pArgu=" + BcdUtils.binArrayToString(IP));

		byte[] PN = BcdUtils.reverseBytes(ByteUtils.intToBytes(
				Integer.valueOf(port), 2));
		System.out.println("PN=" + BcdUtils.binArrayToString(PN));
		byte[] apnTemp = new byte[16];
		System.arraycopy(apn.getBytes(), 0, apnTemp,
				apnTemp.length - apn.length(), apn.length());
		byte[] APN = BcdUtils.reverseBytes(apnTemp);
		System.out.println("APN=" + BcdUtils.binArrayToString(APN));
		byte[] TU = BcdUtils.reverseBytes(new byte[] { 0x01 });
		byte[] UN = BcdUtils.reverseBytes(new byte[16]);
		byte[] PW = BcdUtils.reverseBytes(new byte[16]);

		System.out.println("LJDZ=" + BcdUtils.binArrayToString(LJDZ));
		byte[] data = BcdUtils.combBytes(data01, LJDZ, IP, PN, APN, TU, UN, PW);
		byte[] mess = packMsg(new byte[] { 0x04, 0x01 }, data);
		System.out.println(BcdUtils.binArrayToString(mess));
	}
}

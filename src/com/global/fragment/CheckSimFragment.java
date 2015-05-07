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

public class CheckSimFragment extends BaseFragment implements OnClickListener,

android.widget.CheckBox.OnCheckedChangeListener {
	Boolean revFlag = false;
	TextView revTextView = null;
	Timer timer;
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

			}
		}
	};

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.serial485, null);
		revTextView = (TextView) view.findViewById(R.id.textViewRev);
		scrollView = (ScrollView) view.findViewById(R.id.scrollView);
		type_bluetooth = (CheckBox) view.findViewById(R.id.type_bluetooth);
		type_bluetooth.setOnCheckedChangeListener(this);
		view.findViewById(R.id.buttonZaiboSendSet).setOnClickListener(this);
		view.findViewById(R.id.buttonZaiboSendStart).setOnClickListener(this);
		return view;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
	}

	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.buttonZaiboSendSet:

			// byte[] zaiboSendWifi = new byte[] { (byte) 0xc9, (byte) 0x0c,
			// (byte) 0x03, (byte) 0x02, (byte) 0x00, (byte) 0xc9,
			// (byte) 0x00, (byte) 0x01, (byte) 0xa4, (byte) 0x9c };
			//
			if (blueTooth == null) {
				Toaster.getInstance().displayToast("请开启蓝牙");
				return;
			}
			EditText ljdz = (EditText) view.findViewById(R.id.id_edittext_ljdz);
			EditText ip = (EditText) view.findViewById(R.id.id_edittext_ip);
			EditText port = (EditText) view.findViewById(R.id.id_edittext_port);
			EditText apn = (EditText) view.findViewById(R.id.id_edittext_apn);
			byte[] dataSend = generalByteCheckSIM(ljdz.getText().toString(), ip
					.getText().toString(), port.getText().toString(), apn
					.getText().toString());
			if (dataSend != null) {
				Map<String, byte[]> param = new HashMap();
				param.put(System.currentTimeMillis() + "", dataSend);
				blueTooth.sendData(param);
				Message msgZaibo = msgHandler.obtainMessage();
				msgZaibo.obj = dataSend;
				msgZaibo.what = 0x1110;
				msgHandler.sendMessage(msgZaibo);
				sendByte(dataSend);
			}
			break;
		case R.id.buttonZaiboSendStart:
			startTest();
			break;

		default:
			break;
		}
	}

	public void startTest() {
		byte[] zaiboSendStart = new byte[] { (byte) 0xc9, (byte) 0x0c,
				(byte) 0x03, (byte) 0x02, (byte) 0x00, (byte) 0xc9,
				(byte) 0x00, (byte) 0x01, (byte) 0xa4, (byte) 0x9c };
		Map<String, byte[]> param = new HashMap();
		param.put(System.currentTimeMillis() + "", zaiboSendStart);
		blueTooth.sendData(param);
		Message msgZaibo = msgHandler.obtainMessage();
		msgZaibo.obj = zaiboSendStart;
		msgZaibo.what = 0x1110;
		msgHandler.sendMessage(msgZaibo);
		sendByte(zaiboSendStart);
	}

	@Override
	public void onResume() {
		super.onResume();

	}

	@Override
	public void onPause() {
		if (blueTooth != null) {
			blueTooth.close();
		}
		super.onPause();
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
	}

	@Override
	public void onDetach() {
		super.onDetach();
	}

	public void sendLog(String data) {
		Message msgZaibo = msgHandler.obtainMessage();
		msgZaibo.obj = data;
		msgZaibo.what = 0x1000;
		msgHandler.sendMessage(msgZaibo);

	}

	public void sendByte(byte[] dataSend) {
		Message msgZaibo = msgHandler.obtainMessage();
		msgZaibo.obj = dataSend;
		msgZaibo.what = 0x1110;
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
				blueTooth.open(getActivity());
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;

		}

	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		if (isChecked) {
			sendLog("开启蓝牙");
			serialInit();
		}
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

	public byte[] generalByteCheckSIM(String ljdz, String ip, String port,
			String apn) {
		if (ljdz == null || ljdz.trim().length() != 8) {
			Toaster.getInstance().displayToast("请输入8位逻辑地址");
			return null;
		}
		if (ip == null || ip.trim().length() == 0) {
			Toaster.getInstance().displayToast("请输入ip");
			return null;
		}
		if (port == null || port.trim().length() == 0) {
			Toaster.getInstance().displayToast("请输入端口号");
			return null;
		}
		if (apn == null || apn.trim().length() == 0) {
			Toaster.getInstance().displayToast("请输入APN");
			return null;
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
		return mess;
	}
}

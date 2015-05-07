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
import com.global.fragment.CopyOfSerial485Fragment.ReadAsyncTask;
import com.global.toast.Toaster;
import com.global.util.BcdUtils;
import com.global.util.ByteUtils;
import com.longshine.serial.BlueToothManager;
import com.thinta.device.Com;
import com.thinta.device.Infra;

public class CheckJZQFragment extends BaseFragment implements OnClickListener,

android.widget.CheckBox.OnCheckedChangeListener {
	Boolean revFlag = false;
	TextView revTextView = null;
	Timer timer;
	ScrollView scrollView;
	Spinner baudRate, parity;
	static Spinner bjgySpinner;
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
					for (int j = 0; j < t.length; j++) {
						strb.append(" "
								+ Integer.toHexString(((int) t[j] & 0xFF)));
					}
					revTextView.append("\n 蓝牙报文 [" + key + "]:"
							+ strb.toString() + "\n");
					if (t.length < 20) {
						return;
					}
					System.out.println("蓝牙" + BcdUtils.binArrayToString(t));
					switch (bjgySpinner.getSelectedItemPosition()) {
					case 0:
						byte[] data07 = new byte[4];
						System.arraycopy(t, 16, data07, 0, data07.length);

						if (data07[0] == 0x33 && data07[1] == 0x33
								&& data07[2] == 0x34 && data07[3] == 0x33) {

							// 返回
							byte[] data = new byte[] { (byte) 0xC9,
									(byte) 0x0A, (byte) 0x00, (byte) 0x14,
									(byte) 0x00, (byte) 0xC9, (byte) 0x68,
									(byte) 0x78, (byte) 0x04, (byte) 0x79,
									(byte) 0x11, (byte) 0x00, (byte) 0x00,
									(byte) 0x68, (byte) 0x91, (byte) 0x08,
									(byte) 0x33, (byte) 0x33, (byte) 0x34,
									(byte) 0x33, (byte) 0x33, (byte) 0x34,
									(byte) 0x35, (byte) 0x36, (byte) 0x0E,
									(byte) 0x16, (byte) 0xE2, (byte) 0x9C };

							Map<String, byte[]> param = new HashMap<String, byte[]>();
							param.put(System.currentTimeMillis() + "", data);
							blueTooth.sendData(param);
							Message msgZaibo = msgHandler.obtainMessage();
							msgZaibo.obj = data;
							msgZaibo.what = 0x1110;
							msgHandler.sendMessage(msgZaibo);
							// if(true){
							// C9 0A 00 14 00 C9 68 78 04 79 11 00 00 68 91 08
							// 33 33 34 33 33 34 35 36 0E 16 B2 9C
							// }

						}
						break;
					case 1:
						byte[] data97 = new byte[2];
						System.arraycopy(t, 16, data97, 0, data97.length);
						if (data97[0] == 0x43 && data97[1] == 0xc3) {

							// 返回
							byte[] data = new byte[] { (byte) 0xC9,
									(byte) 0x0A, (byte) 0x00, (byte) 0x10,
									(byte) 0x00, (byte) 0xC9, (byte) 0x68,
									(byte) 0x78, (byte) 0x04, (byte) 0x79,
									(byte) 0x11, (byte) 0x00, (byte) 0x00,
									(byte) 0x68, (byte) 0x81, (byte) 0x04,
									(byte) 0x43, (byte) 0xc3, (byte) 0x34,
									(byte) 0x33, (byte) 0x0E, (byte) 0x16,
									(byte) 0xE2, (byte) 0x9C };
							data[data.length - 4] = ByteUtils.getCheckCode(
									data, 6, data.length - 4);
							data[data.length - 2] = ByteUtils.getCheckCode(
									data, 0, data.length - 2);

							Map<String, byte[]> param = new HashMap<String, byte[]>();
							param.put(System.currentTimeMillis() + "", data);
							blueTooth.sendData(param);
							Message msgZaibo = msgHandler.obtainMessage();
							msgZaibo.obj = data;
							msgZaibo.what = 0x1110;
							msgHandler.sendMessage(msgZaibo);
							// if(true){
							// C9 0A 00 14 00 C9 68 78 04 79 11 00 00 68 91 08
							// 33 33 34 33 33 34 35 36 0E 16 B2 9C
							// }

						}
						break;
					}
					msgHandler.sendEmptyMessageDelayed(0x1122, 200);

					// o4 to 08

				}
				break;

			}
		}
	};

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.check_jzq, null);
		revTextView = (TextView) view.findViewById(R.id.textViewRev);
		scrollView = (ScrollView) view.findViewById(R.id.scrollView);
		type_bluetooth = (CheckBox) view.findViewById(R.id.type_bluetooth);
		bjgySpinner = (Spinner) view.findViewById(R.id.id_bjgy);
		type_bluetooth.setOnCheckedChangeListener(this);
		view.findViewById(R.id.buttonZaiboSendSetStop).setOnClickListener(this);
		view.findViewById(R.id.buttonZaiboSendSet).setOnClickListener(this);
		view.findViewById(R.id.buttonZaiboSendStart).setOnClickListener(this);
		view.findViewById(R.id.buttonZaiboSendStartBlutTooth)
				.setOnClickListener(this);
		baudRate = (Spinner) view.findViewById(R.id.baudRate);
		parity = (Spinner) view.findViewById(R.id.parity);
		return view;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);

	}

	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.buttonZaiboSendSetStop:
			if (true) {
				if (blueTooth == null) {
					Toaster.getInstance().displayToast("请开启蓝牙");
					return;
				}
				byte[] dataSend = new byte[] { (byte) 0xC9, (byte) 0x02,
						(byte) 0x00, (byte) 0x01, (byte) 0x00, (byte) 0xC9,
						(byte) 0x02, (byte) 0x97, (byte) 0x9C, };
				Map<String, byte[]> param = new HashMap<String, byte[]>();
				param.put(System.currentTimeMillis() + "", dataSend);
				blueTooth.sendData(param);
				Message msgZaibo = msgHandler.obtainMessage();
				msgZaibo.obj = dataSend;
				msgZaibo.what = 0x1110;
				msgHandler.sendMessage(msgZaibo);
			}
			break;
		case R.id.buttonZaiboSendStartBlutTooth:
			boolean ret = Infra.open();
			if (ret) {
				revFlag = true;
				serialflag = Infra.set(
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
		case R.id.buttonZaiboSendSet:
			if (blueTooth == null) {
				Toaster.getInstance().displayToast("请开启蓝牙");
				return;
			}
			if (true) {
				EditText bjdz = (EditText) view
						.findViewById(R.id.id_edittext_bjdz);
				if (bjdz == null
						|| bjdz.getText().toString().trim().length() != 12) {
					Toaster.getInstance().displayToast("请输入12位电表地址");
					return;
				}
				byte[] bjdzByte = BcdUtils.reverseBytes(BcdUtils
						.stringToByteArray(bjdz.getText().toString()));
				byte[] zaiboSendJZ_A1 = new byte[] { (byte) 0xc9, (byte) 0x02,
						(byte) 0x00, (byte) 0x08, (byte) 0x00, (byte) 0xc9,
						(byte) 0x01, (byte) 0x78, (byte) 0x04, (byte) 0x79,
						(byte) 0x11, (byte) 0x00, (byte) 0x00, (byte) 0x03,
						(byte) 0xa6, (byte) 0x9c };
				System.arraycopy(bjdzByte, 0, zaiboSendJZ_A1, 7,
						bjdzByte.length);
				byte checkCode = ByteUtils.getCheckCode(zaiboSendJZ_A1, 0,
						zaiboSendJZ_A1.length - 2);
				zaiboSendJZ_A1[zaiboSendJZ_A1.length - 2] = checkCode;

				Message msgZaibo = msgHandler.obtainMessage();
				msgZaibo.obj = zaiboSendJZ_A1;
				msgZaibo.what = 0x1110;
				msgHandler.sendMessage(msgZaibo);
				Map<String, byte[]> param = new HashMap<String, byte[]>();
				param.put(System.currentTimeMillis() + "", zaiboSendJZ_A1);
				blueTooth.sendData(param);
			}
			break;
		case R.id.buttonZaiboSendStart:
			if (!revFlag) {
				Toaster.getInstance().displayToast("请开启红外");
				return;
			}
			EditText ljdz = (EditText) view.findViewById(R.id.id_edittext_ljdz);
			EditText bjdz = (EditText) view.findViewById(R.id.id_edittext_bjdz);
			int bjgy = ((Spinner) view.findViewById(R.id.id_bjgy))
					.getSelectedItemPosition();
			int port = ((Spinner) view.findViewById(R.id.id_port))
					.getSelectedItemPosition();
			int time = ((Spinner) view.findViewById(R.id.id_time))
					.getSelectedItemPosition();
			byte[] dataSend = generalByteCheckZJQ(ljdz.getText().toString(),
					bjgy, bjdz.getText().toString(), port, time);
			new InfraAsyncTask().execute(dataSend);
			Message msgZaibo = msgHandler.obtainMessage();
			msgZaibo.obj = dataSend;
			msgZaibo.what = 0x1110;
			msgHandler.sendMessage(msgZaibo);

			break;

		default:
			break;
		}
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
	}

	public class InfraAsyncTask extends AsyncTask<byte[], Void, byte[]> {
		@Override
		protected byte[] doInBackground(byte[]... mInputStream) {
			try {
				Infra.send(mInputStream[0], 0, mInputStream[0].length);
				return Infra.receive(60000, 500);
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

	public byte[] generalByteCheckZJQ(String ljdz, int bjgy, String bjdz,
			int port, int time) {
		byte[] zaiboSendCaijijzq_readAdress07 = new byte[] { (byte) 0x68,
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
				(byte) 0x11, (byte) 0x11, (byte) 0x11, (byte) 0xE5, (byte) 0x16 };
		//
		byte[] zaiboSendCaijijzq_readAdress97 = new byte[] { (byte) 0x68,
				(byte) 0xC2, (byte) 0x00, (byte) 0xC2, (byte) 0x00,
				(byte) 0x68, (byte) 0x5B, (byte) 0xFF, (byte) 0xFF,
				(byte) 0xFF, (byte) 0xFF, (byte) 0x14, (byte) 0x10,
				(byte) 0x71, (byte) 0x00, (byte) 0x00, (byte) 0x01,
				(byte) 0x00, (byte) 0x1F, (byte) 0x4B, (byte) 0x64,
				(byte) 0x64, (byte) 0x0E, (byte) 0x00, (byte) 0x68,
				(byte) 0x78, (byte) 0x04, (byte) 0x79, (byte) 0x11,
				(byte) 0x00, (byte) 0x00, (byte) 0x68, (byte) 0x01,
				(byte) 0x02, (byte) 0x43, (byte) 0xC3, (byte) 0xB8,
				(byte) 0x16, (byte) 0x11, (byte) 0x11, (byte) 0x11,
				(byte) 0x11, (byte) 0x11, (byte) 0x11, (byte) 0x11,
				(byte) 0x11, (byte) 0x11, (byte) 0x11, (byte) 0x11,
				(byte) 0x11, (byte) 0x11, (byte) 0x11, (byte) 0x11,
				(byte) 0x11, (byte) 0xE5, (byte) 0x16 };
		// 表计规约
		byte[] zaiboSendCaijijzq_readAdress = null;
		switch (bjgy) {
		case 0:
			zaiboSendCaijijzq_readAdress = new byte[zaiboSendCaijijzq_readAdress07.length];
			System.arraycopy(zaiboSendCaijijzq_readAdress07, 0,
					zaiboSendCaijijzq_readAdress, 0,
					zaiboSendCaijijzq_readAdress.length);
			break;
		case 1:
			zaiboSendCaijijzq_readAdress = new byte[zaiboSendCaijijzq_readAdress97.length];
			System.arraycopy(zaiboSendCaijijzq_readAdress97, 0,
					zaiboSendCaijijzq_readAdress, 0,
					zaiboSendCaijijzq_readAdress.length);
			break;

		default:
			break;
		}
		byte[] gylx = bjgy == 0 ? new byte[] { 0x6B } : new byte[] { 0x4B };
		System.arraycopy(gylx, 0, zaiboSendCaijijzq_readAdress, 19, 1);

		// 逻辑地址
		byte[] ljdzArray = new byte[4];
		byte[] ljdzArrayTemp = BcdUtils.stringToByteArray(ljdz);
		ljdzArray[0] = ljdzArrayTemp[1];
		ljdzArray[1] = ljdzArrayTemp[0];
		ljdzArray[2] = ljdzArrayTemp[3];
		ljdzArray[3] = ljdzArrayTemp[2];
		System.arraycopy(ljdzArray, 0, zaiboSendCaijijzq_readAdress, 7,
				ljdzArray.length);

		// 延时
		byte[] timeArray = new byte[1];
		switch (time) {
		case 0:
			timeArray[0] = (byte) 0x9E;
			break;
		case 1:
			timeArray[0] = (byte) 0xBC;
			break;
		case 2:
			timeArray[0] = (byte) 0xDA;
			break;
		default:
			break;
		}
		System.arraycopy(timeArray, 0, zaiboSendCaijijzq_readAdress, 20, 1);

		// 端口
		byte[] portArray = new byte[1];
		switch (port) {
		case 0:
			portArray[0] = (byte) 0x02;
			break;
		case 1:
			portArray[0] = (byte) 0x03;
			break;
		case 2:
			portArray[0] = (byte) 0x1F;
			break;
		default:
			break;
		}
		System.arraycopy(portArray, 0, zaiboSendCaijijzq_readAdress, 18, 1);
		// 表计地址
		byte[] bjdzArrayTemp = new byte[6];
		byte[] bjdzArrayIn = BcdUtils.stringToByteArray(bjdz);
		System.arraycopy(bjdzArrayIn, 0, bjdzArrayTemp, bjdzArrayTemp.length
				- bjdzArrayIn.length, bjdzArrayIn.length);
		byte[] bjdzArray = BcdUtils.reverseBytes(bjdzArrayTemp);
		System.arraycopy(bjdzArray, 0, zaiboSendCaijijzq_readAdress, 25,
				bjdzArray.length);

		switch (bjgy) {
		case 0:
			// 485效验位 07
			byte chckByte07 = ByteUtils.getCheckCode(
					zaiboSendCaijijzq_readAdress, 24, 38);
			System.arraycopy(new byte[] { chckByte07 }, 0,
					zaiboSendCaijijzq_readAdress, 38, 1);
			// 376效验位 07
			byte chckByte_376_07 = ByteUtils.getCheckCode(
					zaiboSendCaijijzq_readAdress, 6, 56);
			System.arraycopy(new byte[] { chckByte_376_07 }, 0,
					zaiboSendCaijijzq_readAdress, 56, 1);
			break;
		case 1:
			// 485效验位 97
			byte chckByte97 = ByteUtils.getCheckCode(
					zaiboSendCaijijzq_readAdress, 24, 36);
			System.arraycopy(new byte[] { chckByte97 }, 0,
					zaiboSendCaijijzq_readAdress, 36, 1);
			// 485效验位 97
			byte chckByte376_97 = ByteUtils.getCheckCode(
					zaiboSendCaijijzq_readAdress, 6, 54);
			System.arraycopy(new byte[] { chckByte376_97 }, 0,
					zaiboSendCaijijzq_readAdress, 54, 1);
			break;
		}
		System.out.println(BcdUtils
				.binArrayToString(zaiboSendCaijijzq_readAdress));
		return zaiboSendCaijijzq_readAdress;
	}
}

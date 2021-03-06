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

public class CheckCJQFragment extends BaseFragment implements OnClickListener,

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
				System.out.println("send=" + BcdUtils.binArrayToString(send));
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
		view = inflater.inflate(R.layout.check_cjq, null);
		revTextView = (TextView) view.findViewById(R.id.textViewRev);
		scrollView = (ScrollView) view.findViewById(R.id.scrollView);
		type_bluetooth = (CheckBox) view.findViewById(R.id.type_bluetooth);
		type_bluetooth.setOnCheckedChangeListener(this);

		view.findViewById(R.id.buttonZaiboSendStart).setOnClickListener(this);
		view.findViewById(R.id.buttonZaiboSendSet).setOnClickListener(this);
		view.findViewById(R.id.buttonZaiboSendSetStop).setOnClickListener(this);

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
						(byte) 0x04, (byte) 0x99, (byte) 0x9C, };
				Map<String, byte[]> param = new HashMap<String, byte[]>();
				param.put(System.currentTimeMillis() + "", dataSend);
				blueTooth.sendData(param);
				Message msgZaibo = msgHandler.obtainMessage();
				msgZaibo.obj = dataSend;
				msgZaibo.what = 0x1110;
				msgHandler.sendMessage(msgZaibo);
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
				// case R.id.buttonZaiboSend_CaijiA1_01:
				// byte[] zaiboSendCaiji = new byte[] { (byte) 0xc9, (byte)
				// 0x02,
				// (byte) 0x08, (byte) 0x02, (byte) 0x00, (byte) 0xc9,
				// (byte) 0x03, (byte) 0x01, (byte) 0xa2, (byte) 0x9c };
				// break;
				// case R.id.buttonZaiboSend_CaijiA3:
				byte[] zaiboSendCaijiA3 = new byte[] { (byte) 0xC9,
						(byte) 0x02, (byte) 0x08, (byte) 0x02, (byte) 0x00,
						(byte) 0xC9, (byte) 0x03, (byte) 0x02, (byte) 0xA3,
						(byte) 0x9C };
				Map<String, byte[]> param = new HashMap<String, byte[]>();
				param.put(System.currentTimeMillis() + "", zaiboSendCaijiA3);
				blueTooth.sendData(param);
				Message msgZaibo = msgHandler.obtainMessage();
				msgZaibo.obj = zaiboSendCaijiA3;
				msgZaibo.what = 0x1110;
				msgHandler.sendMessage(msgZaibo);
			}
			break;
		case R.id.buttonZaiboSendStart:
			if (blueTooth == null) {
				Toaster.getInstance().displayToast("请开启蓝牙");
				return;
			}
			if (true) {
				// EditText ljdz = (EditText)
				// view.findViewById(R.id.id_edittext_ljdz);
				EditText bjdz = (EditText) view
						.findViewById(R.id.id_edittext_bjdz);
				if (bjdz == null
						|| bjdz.getText().toString().trim().length() != 12) {
					Toaster.getInstance().displayToast("请输入12位电表地址");
					return;
				}
				// int bjgy = ((Spinner) view.findViewById(R.id.id_bjgy))
				// .getSelectedItemPosition();
				// int port = ((Spinner) view.findViewById(R.id.id_port))
				// .getSelectedItemPosition();
				// int time = ((Spinner) view.findViewById(R.id.id_time))
				// .getSelectedItemPosition();
				// byte[] dataSend =
				// generalByteCheckZJQ(ljdz.getText().toString(),
				// bjgy, bjdz.getText().toString(), port, time);
				byte[] bjdzByte = BcdUtils.reverseBytes(BcdUtils
						.stringToByteArray(bjdz.getText().toString()));
				Map<String, byte[]> param = new HashMap<String, byte[]>();
				byte[] dataSend = new byte[] { (byte) 0xc9, (byte) 0x0b,
						(byte) 0x0c, (byte) 0x17, (byte) 0x00, (byte) 0xc9,
						(byte) 0x01, (byte) 0x07, (byte) 0x02, (byte) 0x12,
						(byte) 0x20, (byte) 0x00, (byte) 0x02, (byte) 0x68,
						(byte) 0x01, (byte) 0x07, (byte) 0x02, (byte) 0x12,
						(byte) 0x20, (byte) 0x00, (byte) 0x68, (byte) 0x11,
						(byte) 0x04, (byte) 0x33, (byte) 0x33, (byte) 0x34,
						(byte) 0x33, (byte) 0xee, (byte) 0x16, (byte) 0xf0,
						(byte) 0x9c };
				System.arraycopy(bjdzByte, 0, dataSend, 6, bjdzByte.length);
				System.arraycopy(bjdzByte, 0, dataSend, 14, bjdzByte.length);
				byte checkCode = ByteUtils.getCheckCode(dataSend, 13,
						dataSend.length - 4);
				System.arraycopy(new byte[] { checkCode }, 0, dataSend,
						dataSend.length - 4, 1);
				byte checkCodeZaibo = ByteUtils.getCheckCode(dataSend, 0,
						dataSend.length - 2);
				System.arraycopy(new byte[] { checkCodeZaibo }, 0, dataSend,
						dataSend.length - 2, 1);
				param.put(System.currentTimeMillis() + "", dataSend);

				blueTooth.sendData(param);
				Message msgZaibo = msgHandler.obtainMessage();
				msgZaibo.obj = dataSend;
				msgZaibo.what = 0x1110;
				msgHandler.sendMessage(msgZaibo);
			}
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

}

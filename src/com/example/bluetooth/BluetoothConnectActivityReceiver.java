package com.example.bluetooth;

import com.example.Others.Global;
import com.global.classR.ClsUtils;

import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class BluetoothConnectActivityReceiver extends BroadcastReceiver {

	String strPsw = Global.PIN_ID;// "8260"

	@Override
	public void onReceive(Context context, Intent intent) {
		if (intent.getAction().equals(
				"android.bluetooth.device.action.PAIRING_REQUEST")) {
			BluetoothDevice btDevice = intent
					.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

			try {
				ClsUtils.setPin(btDevice.getClass(), btDevice, strPsw); // 手机和蓝牙采集器配对
				ClsUtils.createBond(btDevice.getClass(), btDevice);
				ClsUtils.cancelPairingUserInput(btDevice.getClass(), btDevice);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}
}
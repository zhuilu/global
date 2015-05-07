package com.global.fragment;

import com.global.activity.BaseActivity;
import com.global.util.ByteUtils;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;

public abstract class BaseFragment extends Fragment {

	protected LayoutInflater inflater;
	protected RadioButton radioBtn, radioBtnNo;
	protected TextView step2_describe;
	protected Button debug_step_save;
	protected TextView step3_deal;
	protected BaseActivity baseActivity;
	public static final int requestError = 5;
	public static final int requestSuccess = 6;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		inflater = (LayoutInflater) getActivity().getSystemService(
				Context.LAYOUT_INFLATER_SERVICE);
		baseActivity = (BaseActivity) getActivity();
	}

	// public abstract View onLeftButtonClick(int keyEventAction);
	//
	// public abstract View onRightButtonClick(int keyEventAction);

	protected void initTitle(View convertView) {
	}

	protected byte[] packMsg(byte[] conf, byte[] msg645) {
		int data_filed_len = msg645.length;
		byte[] send_msg = new byte[8 + data_filed_len];
		byte[] filed_len = new byte[2];
		filed_len = ByteUtils.intToByteArray(data_filed_len, filed_len.length);
		send_msg[0] = (byte) 0xC9;
		System.arraycopy(conf, 0, send_msg, 1, conf.length);
		System.arraycopy(filed_len, 0, send_msg, 3, filed_len.length);
		send_msg[5] = (byte) 0xC9;
		System.arraycopy(msg645, 0, send_msg, 6, data_filed_len);
		send_msg[6 + data_filed_len] = ByteUtils.getCheckCode(send_msg,
				6 + data_filed_len);
		send_msg[7 + data_filed_len] = (byte) 0x9C;

		return send_msg;
	}
}

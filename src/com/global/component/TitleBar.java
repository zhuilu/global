package com.global.component;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.global.*;
import com.global.R;

/**
 * 客户端状态栏控件
 * 
 * @author wb-wuxiaofan@163.com
 * 
 */
public class TitleBar extends RelativeLayout {
	private TextView mTitleTextView;// 状态栏名称
	private Button mGenericButton;// 通用按钮
	private Button mLeftButton;// 切换按钮的左半部分
	private Button mRightButton;// 切换按钮的右半部分
	private ViewGroup mSwitchContainer;// 左右按钮容器

	private String mTitleText;// 标题
	private String mGenericButtonText;// 通用按钮文案
	private String mRightButtonText;// 右半部分按钮文案
	private String mLeftButtonText;// 左半部分按钮文案
	private boolean showSwitchContainer;// 是否显示两个按钮
	private boolean showGenericButton;// 是否通用按钮

	public TitleBar(Context context) {
		super(context);
	}

	public TitleBar(Context context, AttributeSet set) {
		super(context, set);
		LayoutInflater.from(context).inflate(R.layout.title_bar, this, true);

		TypedArray a = context
				.obtainStyledAttributes(set, R.styleable.titleBar);

		mTitleText = a.getString(R.styleable.titleBar_titleText);
		mGenericButtonText = a
				.getString(R.styleable.titleBar_genericButtonText);
		mRightButtonText = a.getString(R.styleable.titleBar_rightText);
		mLeftButtonText = a.getString(R.styleable.titleBar_leftText);
		showSwitchContainer = a.getBoolean(R.styleable.titleBar_showSwitch,
				false);
		showGenericButton = a.getBoolean(
				R.styleable.titleBar_showGenericButton, true);

		a.recycle();
	}

	@Override
	protected void onFinishInflate() {
		mTitleTextView = (TextView) findViewById(R.id.title_bar_title);
		mGenericButton = (Button) findViewById(R.id.title_bar_generic_button);

		mSwitchContainer = (ViewGroup) findViewById(R.id.switch_container);
		mLeftButton = (Button) findViewById(R.id.title_bar_left_button);
		mRightButton = (Button) findViewById(R.id.title_bar_right_button);

		setTitleText(mTitleText);
		setGenericButtonText(mGenericButtonText);
		setLeftButtonText(mLeftButtonText);
		setRightButtonText(mRightButtonText);
		setSwitchContainerVisiable(showSwitchContainer);
		setGenericButtonVisiable(showGenericButton);
	}

	/**
	 * 设置标题文案
	 * 
	 * @param text
	 */
	public void setTitleText(String text) {
		mTitleTextView.setText(text);
	}

	/**
	 * 设置是否显示左右按钮container
	 * 
	 * @param visiable
	 *            可见性
	 */
	public void setSwitchContainerVisiable(boolean visiable) {
		mSwitchContainer.setVisibility(visiable ? View.VISIBLE : View.GONE);
	}

	/**
	 * 设置是否显示通用按钮
	 * 
	 * @param visiable
	 */
	public void setGenericButtonVisiable(boolean visiable) {
		mGenericButton.setVisibility(visiable ? View.VISIBLE : View.GONE);
	}

	/**
	 * 设置通用按钮事件
	 * 
	 * @param l
	 */
	public void setGenericButtonListener(OnClickListener l) {
		mGenericButton.setOnClickListener(l);
	}

	/**
	 * 左半部分按钮事件
	 * 
	 * @param l
	 */
	public void setLeftButtonListener(OnClickListener l) {
		mLeftButton.setOnClickListener(l);
	}

	/**
	 * 设置右半部分按钮事件
	 * 
	 * @param l
	 */
	public void setRightButtonListener(OnClickListener l) {
		mRightButton.setOnClickListener(l);
	}

	/**
	 * 通用按钮文案
	 * 
	 * @param text
	 */
	public void setGenericButtonText(String text) {
		mGenericButton.setText(text);
	}

	/**
	 * 左半部分按钮文案
	 * 
	 * @param text
	 */
	public void setLeftButtonText(String text) {
		mLeftButton.setText(text);
	}

	/**
	 * 右半部分按钮文案
	 * 
	 * @param text
	 */
	public void setRightButtonText(String text) {
		mRightButton.setText(text);
	}

	/**
	 * 返回通用按钮
	 */
	public Button getGenericButton() {
		return mGenericButton;
	}

	/**
	 * 返回左半部分按钮
	 */
	public Button getLeftButton() {
		return mLeftButton;
	}

	/**
	 * 返回右半部分按钮
	 */
	public Button getRightButton() {
		return mGenericButton;
	}

	/**
	 * 返回标题
	 */
	public TextView getTitleTextView() {
		return mTitleTextView;
	}
}

package com.global.component;

import android.content.Context;
import android.content.DialogInterface;
import android.util.AttributeSet;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;

public class ClickControlledSpinner extends Button {
	protected String dictId;
	protected String dictParentId;
	protected String dictTypeId;
	protected String instanceValue;
	protected String cascade_group;
	protected String cascade_sort;
	protected String mFieldName;
	protected int mLastClickPosition = 0;

	public int getmLastClickPosition() {
		return mLastClickPosition;
	}

	public void setmLastClickPosition(int mLastClickPosition) {
		this.mLastClickPosition = mLastClickPosition;
	}

	protected boolean flag = true;
	private Object obj;

	public ClickControlledSpinner(Context context) {
		super(context);
	}

	public ClickControlledSpinner(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
	}

	public ClickControlledSpinner(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public boolean isFirstClick() {
		return flag;
	}

	public void setFirstClickFalse() {
		flag = false;
	}

	public void setFirstClickTrue() {
		flag = true;
	}

	/**
	 * 注册自定义的点击事件监听 Register the click event self-fulfilling listener.
	 * 
	 * @param onClickMyListener
	 */

	/**
	 * 自定义点击事件监听. Click event self-fulfilling listener.
	 * 
	 * @author Wison Xu
	 */

	public Object getObj() {
		return obj;
	}

	public void setObj(Object obj) {
		this.obj = obj;
	}

	public String getCascade_group() {
		return cascade_group;
	}

	public void setCascade_group(String cascade_group) {
		this.cascade_group = cascade_group;
	}

	public String getCascade_sort() {
		return cascade_sort;
	}

	public void setCascade_sort(String cascade_sort) {
		this.cascade_sort = cascade_sort;
	}

	public String getDictTypeId() {
		return dictTypeId;
	}

	public void setDictTypeId(String dictTypeId) {
		this.dictTypeId = dictTypeId;
	}

	public String getInstanceValue() {
		return instanceValue;
	}

	public void setInstanceValue(String instanceValue) {
		this.instanceValue = instanceValue;
	}

	public String getDictId() {
		return dictId;
	}

	public void setDictId(String dictId) {
		this.dictId = dictId;
	}

	public String getDictParentId() {
		return dictParentId;
	}

	public void setDictParentId(String dictParentId) {
		this.dictParentId = dictParentId;
	}

	public String getmFieldName() {
		return mFieldName;
	}

	public void setmFieldName(String mFieldName) {
		this.mFieldName = mFieldName;
	}

}

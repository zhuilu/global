package com.global.bean;

public class ChildBean {
	public ChildBean(String childName, String childFragment) {
		this.childName = childName;
		this.childFragment = childFragment;
	}

	String childName;
	String childFragment;

	public String getChildName() {
		return childName;
	}

	public void setChildName(String childName) {
		this.childName = childName;
	}

	public String getChildFragment() {
		return childFragment;
	}

	public void setChildFragment(String childFragment) {
		this.childFragment = childFragment;
	}

}

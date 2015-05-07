package com.global.bean;

public class ExpandBean {
	public ExpandBean(String groupName, ChildBean... child) {
		this.groupName = groupName;
		this.children = child;
	}

	public String groupName;
	public ChildBean[] children;

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public ChildBean[] getChildren() {
		return children;
	}

	public void setChildren(ChildBean[] children) {
		this.children = children;
	}

}

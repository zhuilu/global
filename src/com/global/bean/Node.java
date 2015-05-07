package com.global.bean;

import android.content.ContentValues;
import android.database.Cursor;

import com.global.db.DbHelper;

public class Node extends CWBaseType {

	private String nodeId;//
	private String nodePId;//
	private String nodeName;//
	private String type;//
	private String fragmentName;//

	public Node() {
	}

	public Node(String nodeId, String nodePId, String nodeName, String type,
			String fragmentName) {
		super();
		this.nodeId = nodeId;
		this.nodePId = nodePId;
		this.nodeName = nodeName;
		this.type = type;
		this.fragmentName = fragmentName;
	}

	@Override
	public String getTableName() {
		return DbHelper.TABLE_NodeStep;
	}

	@Override
	public ContentValues toContentValues() {
		ContentValues values = new ContentValues();
		values.put(Columns.nodeId, nodeId);
		values.put(Columns.nodePId, nodePId);
		values.put(Columns.nodeName, nodeName);
		values.put(Columns.type, type);
		values.put(Columns.fragmentName, fragmentName);
		return values;
	}

	@Override
	public Object CursorToObj(Cursor cursor) {
		nodeId = cursor.getString(cursor.getColumnIndexOrThrow(Columns.nodeId));
		nodePId = cursor.getString(cursor
				.getColumnIndexOrThrow(Columns.nodePId));
		nodeName = cursor.getString(cursor
				.getColumnIndexOrThrow(Columns.nodeName));
		type = cursor.getString(cursor.getColumnIndexOrThrow(Columns.type));
		fragmentName = cursor.getString(cursor
				.getColumnIndexOrThrow(Columns.fragmentName));
		return this;
	}

	public class Columns {
		public final static String nodeId = "nodeId";
		public final static String nodePId = "nodePId";
		public final static String nodeName = "nodeName";
		public final static String type = "type";
		public final static String fragmentName = "fragmentName";
	}

	public String getNodeId() {
		return nodeId;
	}

	public void setNodeId(String nodeId) {
		this.nodeId = nodeId;
	}

	public String getNodePId() {
		return nodePId;
	}

	public void setNodePId(String nodePId) {
		this.nodePId = nodePId;
	}

	public String getNodeName() {
		return nodeName;
	}

	public void setNodeName(String nodeName) {
		this.nodeName = nodeName;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		StringBuilder builder = new StringBuilder();
		builder.append("\nnodeId  =" + nodeId);
		builder.append("\nnodePId  =" + nodePId);
		builder.append("\nnodeName  =" + nodeName);
		return builder.toString();
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getFragmentName() {
		return fragmentName;
	}

	public void setFragmentName(String fragmentName) {
		this.fragmentName = fragmentName;
	}
}

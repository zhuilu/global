package com.global;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import com.global.file.FileUtil;
import com.global.log.CLog;

public class TreeModel implements Serializable {
	private final long serialVersionUID = 1L;
	private HashMap<String, Node> alldataMap = null;
	public boolean isRootNOde = false;
	private Node rootNode;

	public TreeModel() {
		alldataMap = new HashMap<String, Node>();

	}

	public Node getNodeById(String nodeId) {
		if (alldataMap.containsKey(nodeId)) {
			return alldataMap.get(nodeId);
		} else {
			Iterator<Node> itea = alldataMap.values().iterator();
			return alldataMap.get(itea.next().getNodeId());

		}
	}

	public boolean isRootNode() {
		return false;
	}

	public Node getLeftLeaf(Node currentNode) {
		return new Node(alldataMap.get(currentNode).getParentId());
	}

	public Node[] getRightLeafs(Node currentNode) {
		ArrayList<Node> temp = alldataMap.get(currentNode).getChilerenLeafs();
		Node[] nodes = new Node[temp.size()];
		temp.toArray(nodes);
		return nodes;
	}

	public Node[][] getNearThreeLeafs(Node currentNode) {
		return null;
	}

	public Node addNode(String id, String parentId) {
		CLog.i("id=" + id + "   parentId=" + parentId);
	 
		Node temp = new Node(id);
		temp.setParentId(parentId);
		alldataMap.put(id, temp);
		if (!parentId.equals(id)) {
			alldataMap.get(parentId).getChilerenLeafs().add(temp);
		}
		return temp;
	}

	public static class Node implements Serializable {

		public Node(String id) {
			setNodeId(id);
		}

		public Node getRootNode() {
			return rootNode;
		}

		public void setRootNode(Node rootNode) {
			this.rootNode = rootNode;
		}

		public String getNodeId() {
			return nodeId;
		}

		public void setNodeId(String nodeId) {
			this.nodeId = nodeId;
		}

		public String getParentId() {
			return parentId;
		}

		public void setParentId(String parentId) {
			this.parentId = parentId;
		}

		public void setChilerenLeafs(ArrayList<Node> chilerenLeafs) {
			this.chilerenLeafs = chilerenLeafs;
		}

		public Node rootNode;
		public String nodeId = "";
		public String parentId = "";
		public ArrayList<Node> chilerenLeafs;

		public ArrayList<Node> getChilerenLeafs() {
			if (chilerenLeafs == null) {
				chilerenLeafs = new ArrayList<Node>();
			}
			return chilerenLeafs;
		}

	}

	public HashMap<String, Node> getAlldataMap() {
		return alldataMap;
	}

	public void setAlldataMap(HashMap<String, Node> alldataMap) {
		this.alldataMap = alldataMap;
	}

	public boolean isRootNOde() {
		return isRootNOde;
	}

	public void setRootNOde(boolean isRootNOde) {
		this.isRootNOde = isRootNOde;
	}

	public long getSerialVersionUID() {
		return serialVersionUID;
	}

	public Node getRootNode() {
		return rootNode;
	}

	public void setRootNode(Node rootNode) {
		this.rootNode = rootNode;
	}
}
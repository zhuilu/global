package com.global.xml;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import org.xml.sax.SAXException;
import org.xmlpull.v1.XmlSerializer;

import com.global.app.SysApplicationImpl;
import com.global.log.CLog;

import android.os.Environment;
import android.util.Log;
import android.util.Xml;

public class XmlUtil {
	Document document;
	File systemInfoFile;

	// ����plugininfo�ļ�
	public static void create() {
		File pluginInfoFile = new File("HJLogGlobal.HJLogHome",
				"plugininfo.xml");
		try {
			pluginInfoFile.createNewFile();
		} catch (IOException e) {
			CLog.e("exception in createNewFile() method");
		}
		FileOutputStream fileos = null;
		try {
			fileos = new FileOutputStream(pluginInfoFile);
		} catch (FileNotFoundException e) {
			CLog.e("can't create FileOutputStream");
		}
		XmlSerializer serializer = Xml.newSerializer();

		try {
			serializer.setOutput(fileos, "UTF-8");
			serializer.startDocument(null, true);
			serializer.startTag(null, "plugins");

			serializer.startTag(null, "plugin");
			serializer.startTag(null, "pluginName");
			serializer.text("mobileshield");
			serializer.endTag(null, "pluginName");
			serializer.startTag(null, "pluginVer");
			serializer.text("1.0");
			serializer.endTag(null, "pluginVer");
			serializer.startTag(null, "manufacturer");
			serializer.text("HJLog");
			serializer.endTag(null, "manufacturer");
			serializer.endTag(null, "plugin");

			serializer.startTag(null, "plugin");
			serializer.startTag(null, "pluginName");
			serializer.text("ipos");
			serializer.endTag(null, "pluginName");
			serializer.startTag(null, "pluginVer");
			serializer.text("1.0");
			serializer.endTag(null, "pluginVer");
			serializer.startTag(null, "manufacturer");
			serializer.text("HJLog");
			serializer.endTag(null, "manufacturer");
			serializer.endTag(null, "plugin");

			serializer.endTag(null, "plugins");
			serializer.endDocument();
			serializer.flush();
			fileos.close();
		} catch (Exception e) {
			CLog.e("error occurred while creating xml file");
		}
		CLog.e("create file ok");

	}

	public static void createItemXmlByList(File pluginInfoFile,
			List<Map<String, String>> list) {
		if (!pluginInfoFile.exists()) {
			try {
				pluginInfoFile.createNewFile();
			} catch (IOException e) {
				CLog.e("exception in createNewFile() method");
			}

		}
		FileOutputStream fileos = null;
		try {
			fileos = new FileOutputStream(pluginInfoFile);
		} catch (FileNotFoundException e) {
			CLog.e("can't create FileOutputStream");
		}
		XmlSerializer serializer = Xml.newSerializer();
		try {
			serializer.setOutput(fileos, "UTF-8");
			serializer.startDocument(null, true);
			serializer.startTag(null, "items");
			for (Map<String, String> map : list) {
				serializer.startTag(null, "item");
				Set<String> keyset = map.keySet();
				Iterator<String> iterator = keyset.iterator();

				while (iterator.hasNext()) {
					String temp = iterator.next();
					serializer.startTag(null, temp);
					String val = map.get(temp);
					val = val != null ? val : "";
					serializer.text(val);
					serializer.endTag(null, temp);
				}
				serializer.endTag(null, "item");
			}
			serializer.endTag(null, "items");
			serializer.endDocument();
			serializer.flush();
			fileos.close();
			CLog.i("create file ok");
		} catch (Exception e) {
			if (SysApplicationImpl.getInstance().isDebug()) {
				e.printStackTrace();
			}
			CLog.e("error occurred while creating xml file");
			return;
		}

	}

	public static void createItemXmlByMap(File pluginInfoFile,
			Map<String, String> map) {
		if (!pluginInfoFile.exists()) {
			try {
				pluginInfoFile.createNewFile();
			} catch (IOException e) {
				CLog.e("exception in createNewFile() method");
			}

		}
		FileOutputStream fileos = null;
		try {
			fileos = new FileOutputStream(pluginInfoFile);
		} catch (FileNotFoundException e) {
			CLog.e("can't create FileOutputStream");
		}
		XmlSerializer serializer = Xml.newSerializer();

		try {
			serializer.setOutput(fileos, "UTF-8");
			serializer.startDocument(null, true);
			serializer.startTag(null, "item");
			Set<String> keyset = map.keySet();
			Iterator<String> iterator = keyset.iterator();

			while (iterator.hasNext()) {
				String temp = iterator.next();
				serializer.startTag(null, temp);
				serializer.text(map.get(temp));
				serializer.endTag(null, temp);
			}

			serializer.endTag(null, "item");
			serializer.endDocument();
			serializer.flush();
			fileos.close();
		} catch (Exception e) {
			CLog.e("error occurred while creating xml file");
			return;
		}
		CLog.i("create file ok");
	}

	public static void createTasksXmlByMap(File pluginInfoFile,
			Map<String, String> map) {
		try {
			if (!pluginInfoFile.exists()) {
				pluginInfoFile.delete();
			}
			pluginInfoFile.createNewFile();
		} catch (IOException e) {
			CLog.e("exception in createNewFile() method");

		}
		FileOutputStream fileos = null;
		try {
			fileos = new FileOutputStream(pluginInfoFile);
		} catch (FileNotFoundException e) {
			CLog.e("can't create FileOutputStream");
		}
		XmlSerializer serializer = Xml.newSerializer();

		try {
			serializer.setOutput(fileos, "UTF-8");
			serializer.startDocument(null, true);
			serializer.startTag(null, "tasks");
			Set<String> keyset = map.keySet();
			Iterator<String> iterator = keyset.iterator();

			while (iterator.hasNext()) {
				String temp = iterator.next();
				serializer.startTag(null, temp);
				serializer.text(map.get(temp));
				serializer.endTag(null, temp);
			}

			serializer.endTag(null, "tasks");
			serializer.endDocument();
			serializer.flush();
			fileos.close();
		} catch (Exception e) {
			CLog.e("error occurred while creating xml file");
		}
		CLog.i("create file ok");
	}

	// �ڵ�����
	public void add(String pluginname, String pluginVersion,
			String pluginManufactory) {
		File pluginInfoFile = new File("plugininfo.xml");
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = null;

		try {
			db = dbf.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			CLog.e(e.toString());
			return;
		}
		Document doc = null;
		try {
			doc = db.parse(pluginInfoFile);
			doc.normalize();
		} catch (SAXException e) {
			CLog.e(e.toString());
			return;
		} catch (IOException e) {
			CLog.e(e.toString());
			return;
		}
		// Element root = doc.getDocumentElement();

		Text textseg;
		Element plugin = doc.createElement("plugin");

		Element pluginName = doc.createElement("pluginName");
		textseg = doc.createTextNode(pluginname);
		pluginName.appendChild(textseg);
		plugin.appendChild(pluginName);

		Element pluginVer = doc.createElement("pluginVer");
		textseg = doc.createTextNode(pluginVersion);
		pluginVer.appendChild(textseg);
		plugin.appendChild(pluginVer);

		Element manufacturer = doc.createElement("manufacturer");
		textseg = doc.createTextNode(pluginManufactory);
		manufacturer.appendChild(textseg);
		plugin.appendChild(manufacturer);

		doc.getDocumentElement().appendChild(plugin);
		TransformerFactory tFactory = TransformerFactory.newInstance();
		Transformer transformer = null;
		try {
			transformer = tFactory.newTransformer();
		} catch (TransformerConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		DOMSource source = new DOMSource(doc);
		StreamResult result = new StreamResult(pluginInfoFile);
		try {
			transformer.transform(source, result);
		} catch (TransformerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	//

	public static void update(String pluginname, String pluginVersion,
			String pluginManufactory) {
		File pluginInfoFile = new File("plugininfo.xml");
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = null;

		try {
			db = dbf.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			CLog.e(e.toString());
			return;
		}
		Document doc = null;
		try {
			doc = db.parse(pluginInfoFile);
		} catch (SAXException e) {
			CLog.e(e.toString());
			return;
		} catch (IOException e) {
			CLog.e(e.toString());
			return;
		}
		Element root = doc.getDocumentElement();
		NodeList plugins = root.getElementsByTagName("plugin");

		for (int i = 0; i < plugins.getLength(); i++) {
			Element plugin = (Element) plugins.item(i);
			Element pluginName = (Element) plugin.getElementsByTagName(
					"pluginName").item(0);
			if (pluginName.getFirstChild().getNodeValue().equals(pluginname)) {
				plugin.getElementsByTagName("pluginVer").item(0)
						.getFirstChild().setNodeValue(pluginVersion);
				plugin.getElementsByTagName("manufacturer").item(0)
						.getFirstChild().setNodeValue(pluginManufactory);
				break;
			}
		}

		TransformerFactory tFactory = TransformerFactory.newInstance();
		Transformer transformer;
		try {
			transformer = tFactory.newTransformer();
			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(pluginInfoFile);
			transformer.transform(source, result);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	// ��ѯ�����еĽڵ���
	public String[] queryAllPLUName() {
		File pluginInfoFile = new File("plugininfo.xml");
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = null;

		try {
			db = dbf.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			CLog.e(e.toString());
			return null;
		}
		Document doc = null;
		try {
			doc = db.parse(pluginInfoFile);
		} catch (SAXException e) {
			CLog.e(e.toString());
			return null;
		} catch (IOException e) {
			CLog.e(e.toString());
			return null;
		}
		Element root = doc.getDocumentElement();
		NodeList plugins = root.getElementsByTagName("plugin");
		String[] res = new String[plugins.getLength()];
		for (int i = 0; i < plugins.getLength(); i++) {
			Element plugin = (Element) plugins.item(i);
			Element pluginName = (Element) plugin.getElementsByTagName(
					"pluginName").item(0);
			if (pluginName.getFirstChild().getNodeValue() != null) {
				res[i] = pluginName.getFirstChild().getNodeValue().toString();
			}
		}
		return res;
	}

	// ɾ��ڵ�
	public static void delPlugin(String delePluginName) {
		try {
			File pluginInfoFile = new File("plugininfo.xml");
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = null;

			try {
				db = dbf.newDocumentBuilder();
			} catch (ParserConfigurationException e) {
				CLog.e(e.toString());

			}
			Document doc = null;
			try {
				doc = db.parse(pluginInfoFile);
			} catch (SAXException e) {
				CLog.e(e.toString());

			} catch (IOException e) {
				CLog.e(e.toString());

			}
			Element root = doc.getDocumentElement();
			NodeList plugins = root.getElementsByTagName("plugin");

			for (int i = 0; i < plugins.getLength(); i++) {
				Element plugin = (Element) plugins.item(i);
				Element pluginName = (Element) plugin.getElementsByTagName(
						"pluginName").item(0);
				if (pluginName.getFirstChild().getNodeValue() != null
						&& pluginName.getFirstChild().getNodeValue()
								.equals(delePluginName)) {
					plugin.removeChild(plugin.getElementsByTagName("pluginVer")
							.item(0));
					plugin.removeChild(plugin.getElementsByTagName(
							"manufacturer").item(0));
					plugin.removeChild(plugin
							.getElementsByTagName("pluginName").item(0));
					root.removeChild(plugin);
					System.out.println(delePluginName + "��ɾ��");
				}
			}
			// for (int p = 0; p < plugins.getLength(); p++) {
			//
			// Element elink = (Element) plugins.item(p);
			// if (elink.getElementsByTagName("pluginName").item(0)
			// .getFirstChild().getNodeValue().toString().equals(
			// delePluginName)) {
			//
			// elink.removeChild(elink.getElementsByTagName("pluginName")
			// .item(0));
			//
			// elink.removeChild(elink.getElementsByTagName("pluginVer").item(
			// 0));
			// elink
			// .removeChild(elink.getElementsByTagName("manufacturer")
			// .item(0));
			// doc.getFirstChild().removeChild(elink);
			//
			// }
			// }

			TransformerFactory tFactory = TransformerFactory.newInstance();
			Transformer transformer = tFactory.newTransformer();
			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(pluginInfoFile);
			transformer.transform(source, result);

		} catch (Exception e) {
			CLog.e(e.toString());

		}

	}

	// ��ѯ�ڵ��ֵ
	public static String[] queryNode(String name) {
		File pluginInfoFile = new File("plugininfo.xml");
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = null;

		try {
			db = dbf.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			CLog.e(e.toString());
			return null;
		}
		Document doc = null;
		try {
			doc = db.parse(pluginInfoFile);
		} catch (SAXException e) {
			CLog.e(e.toString());
			return null;
		} catch (IOException e) {
			CLog.e(e.toString());
			return null;
		}
		Element root = doc.getDocumentElement();
		NodeList plugins = root.getElementsByTagName("plugin");
		String[] result = new String[2];
		for (int i = 0; i < plugins.getLength(); i++) {
			Element plugin = (Element) plugins.item(i);
			Element pluginName = (Element) plugin.getElementsByTagName(
					"pluginName").item(0);
			if (pluginName.getFirstChild().getNodeValue().equals(name)) {
				result[0] = plugin.getElementsByTagName("pluginVer").item(0)
						.getFirstChild().getNodeValue();
				result[1] = plugin.getElementsByTagName("manufacturer").item(0)
						.getFirstChild().getNodeValue();
				break;
			}
		}

		return result;
	}

	public static String ROOT_DIR = "";

	public static void createSystemInfoXmlFile() {
		File systemInfoFile = new File(ROOT_DIR, "sydsysteminfo.xml");
		try {
			systemInfoFile.createNewFile();
		} catch (IOException e) {
			CLog.e("exception in createNewFile() method");
		}
		FileOutputStream fileos = null;
		try {
			fileos = new FileOutputStream(systemInfoFile);
		} catch (FileNotFoundException e) {
			CLog.e("can't create FileOutputStream");
		}
		XmlSerializer serializer = Xml.newSerializer();
		try {
			serializer.setOutput(fileos, "UTF-8");
			serializer.startDocument(null, true);
			serializer.startTag(null, "systems");

			serializer.startTag(null, "system");
			serializer.startTag(null, "systemname");
			serializer.text("��ȫ֧�ŷ���");
			serializer.endTag(null, "systemname");
			serializer.startTag(null, "port");
			serializer.text("9551");
			serializer.endTag(null, "port");
			serializer.startTag(null, "ssl");
			serializer.text("no");
			serializer.endTag(null, "ssl");
			serializer.startTag(null, "autorun");
			serializer.text("no");
			serializer.endTag(null, "autorun");
			serializer.endTag(null, "system");

			serializer.endTag(null, "systems");
			serializer.endDocument();
			serializer.flush();
			fileos.close();
		} catch (Exception e) {
			CLog.e("error occurred while creating xml file");
		}
		CLog.e("create file ok");

	}

	// ��ȡ֧�ż����
	public static String getSystemname() {
		File pluginInfoFile = new File(ROOT_DIR, "sydsysteminfo.xml");
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = null;

		try {
			db = dbf.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			CLog.e(e.toString());
			return null;
		}
		Document doc = null;
		try {
			doc = db.parse(pluginInfoFile);
		} catch (SAXException e) {
			CLog.e(e.toString());
			return null;
		} catch (IOException e) {
			CLog.e(e.toString());
			return null;
		}
		Element root = doc.getDocumentElement();
		NodeList plugins = root.getElementsByTagName("system");

		for (int i = 0; i < plugins.getLength(); i++) {
			Element plugin = (Element) plugins.item(i);
			Element portElement = (Element) plugin.getElementsByTagName(
					"systemname").item(0);
			if (portElement.getFirstChild().getNodeValue() != null) {
				return portElement.getFirstChild().getNodeValue().toString();
			}
		}

		return null;
	}

	// ��ȡ�˿ں�
	public static String getPort() {
		File pluginInfoFile = new File(ROOT_DIR, "sydsysteminfo.xml");
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = null;

		try {
			db = dbf.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			CLog.e(e.toString());
			return null;
		}
		Document doc = null;
		try {
			doc = db.parse(pluginInfoFile);
		} catch (SAXException e) {
			CLog.e(e.toString());
			return null;
		} catch (IOException e) {
			CLog.e(e.toString());
			return null;
		}
		Element root = doc.getDocumentElement();
		NodeList plugins = root.getElementsByTagName("system");

		for (int i = 0; i < plugins.getLength(); i++) {
			Element plugin = (Element) plugins.item(i);
			Element portElement = (Element) plugin.getElementsByTagName("port")
					.item(0);
			if (portElement.getFirstChild().getNodeValue() != null) {
				return portElement.getFirstChild().getNodeValue().toString();
			}
		}

		return null;
	}

	// �޸Ķ˿�
	public static String updatePort(String newport) {
		File pluginInfoFile = new File(ROOT_DIR, "sydsysteminfo.xml");
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = null;

		try {
			db = dbf.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			CLog.e(e.toString());
			return null;
		}
		Document doc = null;
		try {
			doc = db.parse(pluginInfoFile);
		} catch (SAXException e) {
			CLog.e(e.toString());
			return null;
		} catch (IOException e) {
			CLog.e(e.toString());
			return null;
		}
		Element root = doc.getDocumentElement();
		NodeList plugins = root.getElementsByTagName("system");

		for (int i = 0; i < plugins.getLength(); i++) {
			Element plugin = (Element) plugins.item(i);
			Element portElement = (Element) plugin.getElementsByTagName("port")
					.item(0);
			if (portElement.getFirstChild().getNodeValue() != null) {
				portElement.getFirstChild().setNodeValue(newport);
			}
		}
		TransformerFactory tFactory = TransformerFactory.newInstance();
		Transformer transformer;
		try {
			transformer = tFactory.newTransformer();
			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(pluginInfoFile);
			transformer.transform(source, result);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return "success";
	}

	// �޸�ϵͳ���
	public static boolean updateSysName(String newname) {

		File pluginInfoFile = new File(ROOT_DIR, "sydsysteminfo.xml");
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = null;

		try {
			db = dbf.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			CLog.e(e.toString());
			return false;
		}
		Document doc = null;
		try {
			doc = db.parse(pluginInfoFile);
		} catch (SAXException e) {
			CLog.e(e.toString());
			return false;
		} catch (IOException e) {
			CLog.e(e.toString());
			return false;
		}
		Element root = doc.getDocumentElement();
		NodeList plugins = root.getElementsByTagName("system");

		for (int i = 0; i < plugins.getLength(); i++) {
			Element plugin = (Element) plugins.item(i);
			Element portElement = (Element) plugin.getElementsByTagName(
					"systemname").item(0);
			if (portElement.getFirstChild().getNodeValue() != null) {
				portElement.getFirstChild().setNodeValue(newname);
			}
		}
		TransformerFactory tFactory = TransformerFactory.newInstance();
		Transformer transformer;
		try {
			transformer = tFactory.newTransformer();
			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(pluginInfoFile);
			transformer.transform(source, result);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return true;

	}

	public static boolean autorun(boolean isChecked) {
		File systemInfoFile = new File(ROOT_DIR, "sydsysteminfo.xml");
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = null;

		try {
			db = dbf.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			CLog.e(e.toString());
			return false;
		}
		Document document = null;
		try {
			document = db.parse(systemInfoFile);
		} catch (SAXException e) {
			CLog.e(e.toString());
			return false;
		} catch (IOException e) {
			CLog.e(e.toString());
			return false;
		}
		if (!systemInfoFile.exists()) {
			CLog.e("�ļ� sydsysteminfo.xml������");
			return false;
		}

		try {

			document.normalize();

			Node root = document.getDocumentElement();

			if (root.hasChildNodes()) {

				NodeList ftpnodes = root.getChildNodes();
				for (int i = 0; i < ftpnodes.getLength(); i++) {
					NodeList ftplist = ftpnodes.item(i).getChildNodes();
					for (int k = 0; k < ftplist.getLength(); k++) {
						Node subnode = ftplist.item(k);
						// if
						// (subnode.getNodeType()==Node.ELEMENT_NODE&&subnode.
						// getNodeName()=="ftp-chn")
						// {
						// ftpnodes.item(i).removeChild(subnode);
						// }

						if (subnode.getNodeType() == Node.ELEMENT_NODE
								&& subnode.getNodeName().equals("autorun")) {
							if (isChecked) {
								subnode.getFirstChild().setNodeValue("yes");
							} else {
								subnode.getFirstChild().setNodeValue("no");
							}
						}
					}

				}
			}

			TransformerFactory tFactory = TransformerFactory.newInstance();
			Transformer transformer = tFactory.newTransformer();
			// transformer.setOutputProperty(OutputKeys.ENCODING, "GB2312");
			DOMSource source = new DOMSource(document);
			StreamResult result = new StreamResult(systemInfoFile);
			transformer.transform(source, result);
		} catch (Exception ex) {
			CLog.e("�޸�sydsysteminfo.xml�������");
		}

		return true;
	}

	public static boolean updateSSL(boolean isChecked) {
		File systemInfoFile = new File(ROOT_DIR, "sydsysteminfo.xml");
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = null;

		try {
			db = dbf.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			CLog.e(e.toString());
			return false;
		}
		Document document = null;
		try {
			document = db.parse(systemInfoFile);
		} catch (SAXException e) {
			CLog.e(e.toString());
			return false;
		} catch (IOException e) {
			CLog.e(e.toString());
			return false;
		}
		if (!systemInfoFile.exists()) {
			CLog.e("�ļ� sydsysteminfo.xml������");
			return false;
		}

		if (!systemInfoFile.exists()) {
			CLog.e("�ļ� ydsysteminfo.xml������");
			return false;
		}

		try {

			document.normalize();

			Node root = document.getDocumentElement();

			if (root.hasChildNodes()) {

				NodeList ftpnodes = root.getChildNodes();
				for (int i = 0; i < ftpnodes.getLength(); i++) {
					NodeList ftplist = ftpnodes.item(i).getChildNodes();
					for (int k = 0; k < ftplist.getLength(); k++) {
						Node subnode = ftplist.item(k);
						// if
						// (subnode.getNodeType()==Node.ELEMENT_NODE&&subnode.
						// getNodeName()=="ftp-chn")
						// {
						// ftpnodes.item(i).removeChild(subnode);
						// }

						if (subnode.getNodeType() == Node.ELEMENT_NODE
								&& subnode.getNodeName().equals("ssl")) {
							if (isChecked) {
								subnode.getFirstChild().setNodeValue("yes");
							} else {
								subnode.getFirstChild().setNodeValue("no");
							}
						}
					}

				}
			}

			TransformerFactory tFactory = TransformerFactory.newInstance();
			Transformer transformer = tFactory.newTransformer();
			// transformer.setOutputProperty(OutputKeys.ENCODING, "GB2312");
			DOMSource source = new DOMSource(document);
			StreamResult result = new StreamResult(systemInfoFile);
			transformer.transform(source, result);
		} catch (Exception ex) {
			CLog.e("�޸�sydsysteminfo.xml�������");
		}
		return true;
	}

}

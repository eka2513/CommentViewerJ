package jp.co.nicovideo.eka2513.commentviewerj.util;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import jp.co.nicovideo.eka2513.commentviewerj.constants.CommentViewerConstants;
import jp.co.nicovideo.eka2513.commentviewerj.exception.CommentViewerException;
import jp.co.nicovideo.eka2513.commentviewerj.plugin.PluginBase;

import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class PluginUtil {

	public static void main(String[] args) {
		PluginUtil.loadPlugins();
	}

	public static void write(List<Class<? extends PluginBase>> list) {
		try {
			String filename = CommentViewerConstants.PLUGIN_XML_DIR + "plugins.xml";
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			DOMImplementation domImpl=builder.getDOMImplementation();
			Document document = domImpl.createDocument("","plugins",null);
			Element root = document.getDocumentElement();
			for (Class<? extends PluginBase> clazz : list) {
				Element plugin = document.createElement("plugin");
				plugin.setAttribute("class", clazz.getName());
				root.appendChild(plugin);
			}
			TransformerFactory transFactory = TransformerFactory.newInstance();
			Transformer transformer = transFactory.newTransformer();
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.setOutputProperty(OutputKeys.METHOD, "xml");
			transformer.setOutputProperty("{http://xml.apache.org/xalan}indent-amount","2");
			DOMSource source = new DOMSource(document);
			File newXML = new File(filename);
			FileOutputStream os = new FileOutputStream(newXML);
			StreamResult result = new StreamResult(os);
			transformer.transform(source, result);

		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (TransformerConfigurationException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (TransformerException e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("unchecked")
	public static List<Class<? extends PluginBase>> loadPluginClasses() {
		FileInputStream fis = null;
		InputStreamReader isr = null;
		BufferedReader br = null;
		List<Class<? extends PluginBase>> list = new ArrayList<Class<? extends PluginBase>>();
		try {
			String filename = CommentViewerConstants.PLUGIN_XML_DIR + "plugins.xml";
			fis = new FileInputStream(new File(filename));
			isr = new InputStreamReader(fis, "UTF-8");
			br = new BufferedReader(isr);
			StringBuffer xml = new StringBuffer();
			String buf;
			while ((buf = br.readLine()) != null) {
				xml.append(buf);
			}

			DocumentBuilder builder = DocumentBuilderFactory.newInstance()
					.newDocumentBuilder();
			Document doc = builder.parse(new ByteArrayInputStream(
					xml.toString().getBytes("UTF-8")));
			XPathFactory factory = XPathFactory.newInstance();
			XPath xpath = factory.newXPath();
			// 複数ノード取得
			String location = "/plugins/plugin/@class";
			NodeList entries = (NodeList) xpath.evaluate(location, doc,
					XPathConstants.NODESET);
			for (int i = 0; i < entries.getLength(); i++) {
				try {
					Class<? extends PluginBase> clazz = (Class<? extends PluginBase>) Class.forName(entries.item(i).getNodeValue());
					list.add(clazz);
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}
			}
		} catch (ParserConfigurationException e) {
			throw new CommentViewerException(e);
		} catch (UnsupportedEncodingException e) {
			throw new CommentViewerException(e);
		} catch (SAXException e) {
			throw new CommentViewerException(e);
		} catch (IOException e) {
			throw new CommentViewerException(e);
		} catch (XPathExpressionException e) {
			throw new CommentViewerException(e);
		} finally {
				try {
					if (br != null)
						br.close();
					if (isr != null)
						isr.close();
					if (fis != null)
						fis.close();
				} catch (IOException ignore) {
					br = null;
					isr = null;
					fis = null;
				}
		}
		return list;
	}

	/**
	 * プラグインのインスタンスをListで返す
	 * @return
	 */
	public static List<PluginBase> loadPlugins() {
		FileInputStream fis = null;
		InputStreamReader isr = null;
		BufferedReader br = null;
		List<PluginBase> list = new ArrayList<PluginBase>();
		try {
			String filename = CommentViewerConstants.PLUGIN_XML_DIR + "plugins.xml";
			fis = new FileInputStream(new File(filename));
			isr = new InputStreamReader(fis, "UTF-8");
			br = new BufferedReader(isr);
			StringBuffer xml = new StringBuffer();
			String buf;
			while ((buf = br.readLine()) != null) {
				xml.append(buf);
			}

			DocumentBuilder builder = DocumentBuilderFactory.newInstance()
					.newDocumentBuilder();
			Document doc = builder.parse(new ByteArrayInputStream(
					xml.toString().getBytes("UTF-8")));
			XPathFactory factory = XPathFactory.newInstance();
			XPath xpath = factory.newXPath();
			// 複数ノード取得
			String location = "/plugins/plugin/@class";
			NodeList entries = (NodeList) xpath.evaluate(location, doc,
					XPathConstants.NODESET);
			for (int i = 0; i < entries.getLength(); i++) {
				PluginBase p = new PluginSettingUtil<PluginBase>().load(
						CommentViewerConstants.PLUGIN_XML_DIR + entries.item(i).getNodeValue() + ".dat");
				if (p == null) {
					System.err.println(String.format("%sのロードに失敗しました", entries.item(i).getNodeValue()));
					continue;
				}
				list.add(p);
			}
		} catch (ParserConfigurationException e) {
			throw new CommentViewerException(e);
		} catch (UnsupportedEncodingException e) {
			throw new CommentViewerException(e);
		} catch (SAXException e) {
			throw new CommentViewerException(e);
		} catch (IOException e) {
			throw new CommentViewerException(e);
		} catch (XPathExpressionException e) {
			throw new CommentViewerException(e);
		} finally {
				try {
					if (br != null)
						br.close();
					if (isr != null)
						isr.close();
					if (fis != null)
						fis.close();
				} catch (IOException ignore) {
					br = null;
					isr = null;
					fis = null;
					ignore.printStackTrace();
				}
		}
		return list;
	}
}

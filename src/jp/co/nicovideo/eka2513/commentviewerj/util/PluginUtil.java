package jp.co.nicovideo.eka2513.commentviewerj.util;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import jp.co.nicovideo.eka2513.commentviewerj.constants.CommentViewerConstants;
import jp.co.nicovideo.eka2513.commentviewerj.exception.CommentViewerException;
import jp.co.nicovideo.eka2513.commentviewerj.plugin.PluginBase;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class PluginUtil {

	public static void main(String[] args) {
		PluginUtil.loadPlugins();
	}

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
				}
		}
		return list;
	}
}

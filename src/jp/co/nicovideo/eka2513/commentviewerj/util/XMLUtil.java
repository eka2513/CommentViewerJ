package jp.co.nicovideo.eka2513.commentviewerj.util;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import jp.co.nicovideo.eka2513.commentviewerj.constants.CommentViewerConstants;
import jp.co.nicovideo.eka2513.commentviewerj.dto.ChatMessage;
import jp.co.nicovideo.eka2513.commentviewerj.dto.ChatResultMessage;
import jp.co.nicovideo.eka2513.commentviewerj.dto.ThreadMessage;
import jp.co.nicovideo.eka2513.commentviewerj.exception.CommentViewerException;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;


public class XMLUtil implements CommentViewerConstants {

	public static ChatResultMessage getChatResultMessage(String xml) {
		//<chat_result thread="1266480252" status="0" no="151"/>
		ChatResultMessage message = new ChatResultMessage();
		try {
			DocumentBuilder builder = DocumentBuilderFactory.newInstance()
			        .newDocumentBuilder();
			Document doc = builder.parse(new ByteArrayInputStream(xml.getBytes("UTF-8")));
			XPathFactory factory = XPathFactory.newInstance();
            XPath xpath = factory.newXPath();
            // 単一ノード取得
            String location = "/chat_result/@thread";
            message.setThread(xpath.evaluate(location, doc));
            location = "/chat_result/@status";
            message.setStatus(xpath.evaluate(location, doc));
            location = "/chat_result/@no";
            message.setNo(xpath.evaluate(location, doc));
		} catch (ParserConfigurationException e) {
			throw new CommentViewerException(e);
		} catch (UnsupportedEncodingException e) {
			throw new CommentViewerException(e);
		} catch (SAXException e) {
			System.out.println(xml);
			throw new CommentViewerException(e);
		} catch (IOException e) {
			throw new CommentViewerException(e);
		} catch (XPathExpressionException e) {
			throw new CommentViewerException(e);
		}
		return message;
	}

	public static ChatMessage getChatMessage(String xml) {
		ChatMessage message = new ChatMessage();
		try {
			DocumentBuilder builder = DocumentBuilderFactory.newInstance()
			        .newDocumentBuilder();
			Document doc = builder.parse(new ByteArrayInputStream(xml.getBytes("UTF-8")));
			XPathFactory factory = XPathFactory.newInstance();
            XPath xpath = factory.newXPath();
            // 単一ノード取得
            String location = "/chat/@thread";
            message.setThread(xpath.evaluate(location, doc));
            location = "/chat/@no";
            message.setNo(xpath.evaluate(location, doc));
            location = "/chat/@vpos";
            message.setVpos(xpath.evaluate(location, doc));
            location = "/chat/@date";
            message.setDate(xpath.evaluate(location, doc));
            location = "/chat/@date_usec";
            message.setDate_usec(xpath.evaluate(location, doc));
            location = "/chat/@mail";
            message.setMail(xpath.evaluate(location, doc));
            location = "/chat/@user_id";
            message.setUser_id(xpath.evaluate(location, doc));

            location = "/chat/@premium";
            message.setPremium(xpath.evaluate(location, doc));
            location = "/chat/@anonymity";
            message.setAnonymity(xpath.evaluate(location, doc));
            location = "/chat/@score";
            message.setScore(xpath.evaluate(location, doc));
            location = "/chat/@locale";
            message.setLocale(xpath.evaluate(location, doc));
            location = "/chat/text()";
            message.setText(xpath.evaluate(location, doc));
		} catch (ParserConfigurationException e) {
			throw new CommentViewerException(e);
		} catch (UnsupportedEncodingException e) {
			throw new CommentViewerException(e);
		} catch (SAXException e) {
			System.out.println(xml);
			throw new CommentViewerException(e);
		} catch (IOException e) {
			throw new CommentViewerException(e);
		} catch (XPathExpressionException e) {
			throw new CommentViewerException(e);
		}
		return message;
	}

	public static ThreadMessage getThreadMessage(String xml) {
		ThreadMessage message = new ThreadMessage();
		try {
			DocumentBuilder builder = DocumentBuilderFactory.newInstance()
			        .newDocumentBuilder();
			Document doc = builder.parse(new ByteArrayInputStream(xml.getBytes("UTF-8")));
			XPathFactory factory = XPathFactory.newInstance();
            XPath xpath = factory.newXPath();
            // 単一ノード取得
            String location = "/thread/@resultcode";
            message.setResultcode(xpath.evaluate(location, doc));
            location = "/thread/@thread";
            message.setThread(xpath.evaluate(location, doc));
            location = "/thread/@last_res";
            message.setLast_res(xpath.evaluate(location, doc));
            location = "/thread/@ticket";
            message.setTicket(xpath.evaluate(location, doc));
            location = "/thread/@revision";
            message.setRevision(xpath.evaluate(location, doc));
            location = "/thread/@server_time";
            message.setServer_time(xpath.evaluate(location, doc));
		} catch (ParserConfigurationException e) {
		} catch (UnsupportedEncodingException ignore) {
		} catch (SAXException e) {
			throw new CommentViewerException(e);
		} catch (IOException e) {
			throw new CommentViewerException(e);
		} catch (XPathExpressionException e) {
			throw new CommentViewerException(e);
		}
		return message;
	}

	public static Map<String, String> parsePlayerStatus(String xml) {
		Map<String, String> result = new HashMap<String, String>();
		try {
			DocumentBuilder builder = DocumentBuilderFactory.newInstance()
			        .newDocumentBuilder();
			Document doc = builder.parse(new ByteArrayInputStream(xml.getBytes("UTF-8")));
			XPathFactory factory = XPathFactory.newInstance();
            XPath xpath = factory.newXPath();
            // 単一ノード取得
            String location = "//ms/addr/text()";
            result.put(ADDR, xpath.evaluate(location, doc));
            location = "//ms/port/text()";
            result.put(PORT, xpath.evaluate(location, doc));
            location = "//ms/thread/text()";
            result.put(THREAD, xpath.evaluate(location, doc));
            location = "/getplayerstatus/@time";
            result.put(TIME, xpath.evaluate(location, doc));
            location = "//stream/start_time";
            result.put(START_TIME, xpath.evaluate(location, doc));
            location = "//stream/base_time";
            result.put(BASE_TIME, xpath.evaluate(location, doc));
            location = "//user/user_id";
            result.put(USER_ID, xpath.evaluate(location, doc));
            location = "//user/is_premium";
            result.put(PREMIUM, xpath.evaluate(location, doc));

		} catch (ParserConfigurationException e) {
		} catch (UnsupportedEncodingException ignore) {
		} catch (SAXException e) {
			throw new CommentViewerException(e);
		} catch (IOException e) {
			throw new CommentViewerException(e);
		} catch (XPathExpressionException e) {
			throw new CommentViewerException(e);
		}
		return result;
	}
	public static Map<String, String> parsePublishStatus(String xml) {
		Map<String, String> result = new HashMap<String, String>();
		try {
			DocumentBuilder builder = DocumentBuilderFactory.newInstance()
			        .newDocumentBuilder();
			Document doc = builder.parse(new ByteArrayInputStream(xml.getBytes("UTF-8")));
			XPathFactory factory = XPathFactory.newInstance();
            XPath xpath = factory.newXPath();
            // 単一ノード取得
            String location = "/getpublishstatus/@status";
            result.put(STATUS, xpath.evaluate(location, doc));
            location = "/getpublishstatus/stream/token/text()";
            result.put(TOKEN, xpath.evaluate(location, doc));
		} catch (ParserConfigurationException e) {
		} catch (UnsupportedEncodingException ignore) {
		} catch (SAXException e) {
			throw new CommentViewerException(e);
		} catch (IOException e) {
			throw new CommentViewerException(e);
		} catch (XPathExpressionException e) {
			throw new CommentViewerException(e);
		}
		return result;
	}

}

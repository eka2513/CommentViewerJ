package jp.co.nicovideo.eka2513.commentviewerj.util;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

public class PluginSettingUtil<T> {

//	public static void main(String[] args) {
//		new XmlEncodeUtil<SimplePlugin>().save(new SimplePlugin());
//		SimplePlugin obj = new XmlEncodeUtil<SimplePlugin>().load(SimplePlugin.class);
//		obj.foo("あああああ");
//	}

	/**
	 * インスタンスをxmlにセーブします
	 * @param object
	 */
	public void save(T object) {
		try {
			XMLEncoder xmlEncoder = new XMLEncoder(new BufferedOutputStream(
					new FileOutputStream(createFilename(object))));
			xmlEncoder.writeObject(object);
			xmlEncoder.close();
		} catch (FileNotFoundException ignore) {
		}
	}

	/**
	 * インスタンスをxmlからロードします
	 * @param clazz
	 * @return
	 */
	public T load(String filename) {
		try {
			XMLDecoder xmlDecoder = new XMLDecoder(new BufferedInputStream(
					new FileInputStream(filename)));
			@SuppressWarnings("unchecked")
			T object = (T) xmlDecoder.readObject();
			xmlDecoder.close();
			return object;
		} catch (FileNotFoundException e) {
			return null;
		}
	}

	private String createFilename(T object) {
		return object.getClass().getName() + ".xml";
	}
}

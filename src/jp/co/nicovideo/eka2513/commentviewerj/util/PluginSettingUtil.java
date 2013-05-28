package jp.co.nicovideo.eka2513.commentviewerj.util;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import jp.co.nicovideo.eka2513.commentviewerj.constants.CommentViewerConstants;
import jp.co.nicovideo.eka2513.commentviewerj.plugin.form.sample.ChukeiPlugin;

public class PluginSettingUtil<T> {

	public static void main(String[] args) {
		ChukeiPlugin plugin = new ChukeiPlugin();
		new PluginSettingUtil<ChukeiPlugin>().save(plugin);
		plugin = new PluginSettingUtil<ChukeiPlugin>().load(
			CommentViewerConstants.PLUGIN_XML_DIR + ChukeiPlugin.class.getName() + ".dat"
		);
	}

	/**
	 * インスタンスをxmlにセーブします
	 * @param object
	 */
	public void save(T object) {
//		new SerializerUtil<T>().save(createFilename(object), object);
		try {
			XMLEncoder xmlEncoder = new XMLEncoder(new BufferedOutputStream(
					new FileOutputStream(createFilename(object))));
			xmlEncoder.writeObject(object);
			xmlEncoder.close();
		} catch (FileNotFoundException ignore) {
		}
	}

	/**
	 * インスタンスをロードします
	 * @param filename ファイル名
	 * @return インスタンス
	 */
	public T load(String filename) {
//		return new SerializerUtil<T>().load(filename);
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

	public String createFilename(T object) {
		return CommentViewerConstants.PLUGIN_XML_DIR + object.getClass().getName() + ".dat";
	}
}

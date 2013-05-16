package jp.co.nicovideo.eka2513.commentviewerj.util;

import jp.co.nicovideo.eka2513.commentviewerj.constants.CommentViewerConstants;
import jp.co.nicovideo.eka2513.commentviewerj.plugin.sample.ChukeiRainbowPlugin;

public class PluginSettingUtil<T> {

	public static void main(String[] args) {
		ChukeiRainbowPlugin plugin = new ChukeiRainbowPlugin();
		plugin.setLoopCount(21);
		new PluginSettingUtil<ChukeiRainbowPlugin>().save(plugin);
		plugin = new PluginSettingUtil<ChukeiRainbowPlugin>().load(
			CommentViewerConstants.PLUGIN_XML_DIR + ChukeiRainbowPlugin.class.getName() + ".xml"
		);
	}

	/**
	 * インスタンスをxmlにセーブします
	 * @param object
	 */
	public void save(T object) {
		new SerializerUtil<T>().save(createFilename(object), object);
//		try {
//			XMLEncoder xmlEncoder = new XMLEncoder(new BufferedOutputStream(
//					new FileOutputStream(createFilename(object))));
//			xmlEncoder.writeObject(object);
//			xmlEncoder.close();
//		} catch (FileNotFoundException ignore) {
//		}
	}

	/**
	 * インスタンスをロードします
	 * @param filename ファイル名
	 * @return インスタンス
	 */
	public T load(String filename) {
		return new SerializerUtil<T>().load(filename);
//		try {
//			XMLDecoder xmlDecoder = new XMLDecoder(new BufferedInputStream(
//					new FileInputStream(filename)));
//			@SuppressWarnings("unchecked")
//			T object = (T) xmlDecoder.readObject();
//			xmlDecoder.close();
//			return object;
//		} catch (FileNotFoundException e) {
//			return null;
//		}
	}

	public String createFilename(T object) {
		return CommentViewerConstants.PLUGIN_XML_DIR + object.getClass().getName() + ".dat";
	}
}

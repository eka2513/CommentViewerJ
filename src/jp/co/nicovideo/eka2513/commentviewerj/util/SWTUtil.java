package jp.co.nicovideo.eka2513.commentviewerj.util;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import jp.nicovideo.eka2513.cookiegetter4j.util.StringUtil;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Device;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;

public class SWTUtil {

	/**
	 * vposを99:99:99形式にします
	 * @param vpos
	 */
	public static String vpos2Time(String vpos) {
		Integer v = StringUtil.inull2Val(vpos) / 100;
		if (v > 3600)
			return String.format("%d:%02d:%02d", Integer.valueOf(v/3600), Integer.valueOf((v % 3600)/60), Integer.valueOf(v % 60));
		return String.format("%02d:%02d", Integer.valueOf((v % 3600)/60), Integer.valueOf(v % 60));
	}

	/**
	 * URLからイメージを取得して、リサイズします
	 * @param device
	 * @param url
	 * @param dev
	 * @param width
	 * @param height
	 * @return
	 */
	public static Image getImage(Device device, String url, Device dev, int width, int height) {
		InputStream in = null;
		Image im = null;
		try {
			in = new URL(url).openStream();
			im = new Image(device, in);
			in.close();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
		} finally {
			if (in != null)
				try {
					in.close();
				} catch (IOException ignore) {
				}
		}
		if (im == null) {
			return im;
		}
		if (im.getBounds().width > im.getBounds().height) {
			//横長画像
			height = width * im.getBounds().height / im.getBounds().width;
		} else {
			//縦長画像
			width = height * im.getBounds().width / im.getBounds().height;
		}
		Image dstim = new Image(dev, width, height);
		GC gc = new GC(dstim);;
		gc.setAntialias(SWT.ON);
		gc.setInterpolation(SWT.HIGH);
		gc.drawImage(im, 0, 0, im.getBounds().width, im.getBounds().height, 0, 0, width, height);
		return dstim;
	}

}

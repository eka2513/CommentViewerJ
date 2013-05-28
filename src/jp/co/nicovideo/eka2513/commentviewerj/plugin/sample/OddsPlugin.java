package jp.co.nicovideo.eka2513.commentviewerj.plugin.sample;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jp.co.nicovideo.eka2513.commentviewerj.dto.ThreadMessage;
import jp.co.nicovideo.eka2513.commentviewerj.event.PluginCommentEvent;
import jp.co.nicovideo.eka2513.commentviewerj.event.PluginThreadEvent;
import jp.co.nicovideo.eka2513.commentviewerj.event.TimerPluginEvent;
import jp.co.nicovideo.eka2513.commentviewerj.exception.CommentNotSendException;
import jp.co.nicovideo.eka2513.commentviewerj.main.CommentViewerBase;
import jp.co.nicovideo.eka2513.commentviewerj.plugin.PluginBase;
import jp.co.nicovideo.eka2513.commentviewerj.util.NicoRequestUtil;
import jp.co.nicovideo.eka2513.commentviewerj.util.PluginSettingUtil;
import jp.nicovideo.eka2513.cookiegetter4j.util.StringUtil;

public class OddsPlugin extends PluginBase {

	private static final long serialVersionUID = -4189001056339491439L;

	public static void main(String[] args) {
		OddsPlugin plugin = new OddsPlugin();
		new PluginSettingUtil<OddsPlugin>().save(plugin);
	}

	private ThreadMessage threadMessage;

	public OddsPlugin() {
	}

	private static final String[] JOUNAMES = new String[]{
		"",
		"桐生","戸田","江戸川",
		"平和島","多摩川","浜名湖",
		"蒲郡","常滑","津",
		"三国","びわこ","住之江",
		"尼崎","鳴門","丸亀",
		"児島","宮島","徳山",
		"下関","若松","芦屋",
		"福岡","唐津","大村",
	};

	@Override
	public void commentReceived(CommentViewerBase source, PluginCommentEvent e) {
		if (threadMessage == null)
			return;
		if (e.getMessage().isNgComment())
			return;
		Integer lastNo = StringUtil.inull2Val(threadMessage.getLast_res());
		if (!e.getMessage().getPremium().equals("3") && StringUtil.inull2Val(e.getMessage().getNo()) > lastNo) {
			//主コメ以外で反応
			// /odds 若松 199,231,
			if (e.getMessage().getText().startsWith("/odds")) {
				String jouname = null;
				String kaime = null;
				String srace = null;
				Map<Integer, List<String>> m = StringUtil.groupMatchAll("\\/odds[ 　]+([^ 　]+)[ 　]+([^ 　]+)[ 　]+([1-69１-６９、,，]+)", e.getMessage().getText());
				for (Map.Entry<Integer, List<String>> entry : m.entrySet()) {
					for (String s : entry.getValue()) {
						if (entry.getKey() == 1) {
							jouname = s;
						} else if (entry.getKey() == 2) {
							srace = zen2hanEisu(StringUtil.groupMatchFirst("([0-9０-９]+)", s));
						} else if (entry.getKey() == 3) {
							kaime = s;
						}
					}
				}
				if (jouname == null || kaime == null || srace == null)
					return;
				Integer joucd = getJoucd(jouname);
				Integer race = Integer.valueOf(srace);
				if (joucd == 0)
					return;
				if (race < 1 || race > 12)
					return;
				List<Integer> kaimeList = expandKaime(kaime);
				if (kaimeList.size() == 0)
					return;
				String[] comments = createComment(joucd, race, kaimeList);
				for (String comment : comments) {
					try {
						sendUneiComment("184", comment, String.format("%s%02dR", jouname, race));
					} catch (CommentNotSendException ignore) {
						ignore.printStackTrace();
					}
				}
			}
		}
	}

	private Map<Integer, Map<String, String>> createOddsMap(Integer joucd, Integer race) {
		final String url = String.format(
				"http://www.mbrace.or.jp/od/O/%02d/OJ%02d.dat?%d", joucd, race, System.currentTimeMillis());
		NicoRequestUtil req = new NicoRequestUtil();
		String[] csv = req.get(url).split(",");
		if (csv.length < 50)
			return null;//非開催
		Map<Integer, Map<String, String>> result = new HashMap<Integer, Map<String,String>>();
		//２連単 298~
		Map<String,String> map = new HashMap<String, String>();
		int index = 298;
		for (int i=1; i<=6; i++) {
			for (int j=1; j<=6; j++) {
				if (i != j) {
					map.put(String.format("%d%d", i,j), csv[index]);
					index++;
				}
			}
		}
		result.put(2, map);
		//３連単 340~
		map = new HashMap<String, String>();
		index = 340;
		for (int i=1; i<=6; i++) {
			for (int j=1; j<=6; j++) {
				for (int k=1; k<=6; k++) {
					if (i != j && j != k && k != i) {
						map.put(String.format("%d%d%d", i,j,k), csv[index]);
						index++;
					}
				}
			}
		}
		result.put(3, map);
		return result;
	}

	private String addHyphen(String s) {
		StringBuffer sb = new StringBuffer();
		for (int i=0; i<s.length(); i++) {
			if (i>0)
				sb.append("-");
			sb.append(s.charAt(i));
		}
		return sb.toString();
	}

	private String[] createComment(Integer joucd, Integer race, List<Integer> kaimeList) {
		Map<Integer, Map<String,String>> oddsMap = createOddsMap(joucd, race);
		//20件ずつに区切る
		List<String> comments = new ArrayList<String>();
		StringBuffer sb = new StringBuffer("/perm ");
		for (int i=0; i<kaimeList.size(); i++) {
			sb.append(addHyphen(kaimeList.get(i).toString())).append(":");
			sb.append("<font color=\"#ffff00\">");
			if (kaimeList.get(i) < 100) {
				//２連単
				sb.append(oddsMap.get(2).get(kaimeList.get(i).toString())).append(" ");
			} else {
				//３連単
				sb.append(oddsMap.get(3).get(kaimeList.get(i).toString())).append(" ");
			}
			sb.append("</font>");

			if (i % 5 == 4) {
				sb.append("<br>");
			}
			if (i % 20 == 19) {
				comments.add(sb.toString());
				sb.setLength(0);
				sb.append("/perm ");
			}
		}
		if (sb.length() > 6)
			comments.add(sb.toString());
		return comments.toArray(new String[0]);
	}
	private String zen2hanEisu(String value) {
	    StringBuilder sb = new StringBuilder(value);
	    for (int i = 0; i < sb.length(); i++) {
	        int c = (int) sb.charAt(i);
	        if ((c >= 0xFF10 && c <= 0xFF19) || (c >= 0xFF21 && c <= 0xFF3A) || (c >= 0xFF41 && c <= 0xFF5A)) {
	            sb.setCharAt(i, (char) (c - 0xFEE0));
	        }
	    }
	    value = sb.toString();
	    return value;
	}
	private List<Integer> expandKaime(String kaime) {
		final String[] kaimeArray = kaime.split("[、,，]");
		List<Integer> expanded = new ArrayList<Integer>();
		String tmp;
		for (String s : kaimeArray) {
			tmp = zen2hanEisu(s);
			//2文字(連単) 3文字(3連単)以外の場合はスルー
			if (tmp.length() != 2 && tmp.length() != 3)
				continue;
			//9(流し)の展開
			if (tmp.length() == 2) {
				expanded.addAll(expand2tan(tmp));
			} else {
				expanded.addAll(expand3tan(tmp));
			}
		}
		Collections.sort(expanded);
		return expanded;
	}

	private Integer getJoucd(String jouname) {
		for (int i=0; i<JOUNAMES.length; i++) {
			if (jouname.equals(JOUNAMES[i]))
				return i;
		}
		return 0;
	}

	private List<Integer> expand2tan(String tmp) {
		List<Integer> expanded = new ArrayList<Integer>();
		char a;
		if (tmp.equals("99")) {
			for (int i=1; i<=6; i++) {
				for (int j=1; j<=6; j++) {
					if (i != j)
						expanded.add(Integer.valueOf(String.format("%d%d", i, j)));
				}
			}
		}
		else if (tmp.startsWith("9")) {
			Integer c = Integer.valueOf(tmp.charAt(1));
			for (int i=1; i<=6; i++) {
				a = String.valueOf(i).charAt(0);
				if (a != c)
					expanded.add(Integer.valueOf(String.format("%c%c", a, c)));
			}
		}
		else if (tmp.endsWith("9")) {
			Integer c = Integer.valueOf(tmp.charAt(0));
			for (int i=1; i<=6; i++) {
				a = String.valueOf(i).charAt(0);
				if (a != c)
					expanded.add(Integer.valueOf(String.format("%c%c", c, a)));
			}
		}
		else {
			expanded.add(Integer.valueOf(tmp));
		}
		return expanded;
	}
	private List<Integer> expand3tan(String tmp) {
		List<Integer> expanded = new ArrayList<Integer>();
		char[] c = tmp.toCharArray();
		char a,b;
		if (tmp.equals("999")) {
			//999
			for (int i=1; i<=6; i++) {
				for (int j=1; j<=6; j++) {
					for (int k=1; k<=6; k++) {
						if (i != j && j != k && k != i)
							expanded.add(Integer.valueOf(String.format("%d%d%d", i, j, k)));
					}
				}
			}
		}
		else if (c[0] == '9' && c[1] == '9' && c[2] != '9') {
			//991
			for (int i=1; i<=6; i++) {
				a = String.valueOf(i).charAt(0);
				for (int j=1; j<=6; j++) {
					b = String.valueOf(j).charAt(0);
					if (a != b && b != c[2] && c[2] != a)
						expanded.add(Integer.valueOf(String.format("%c%c%c", a, b, c[2])));
				}
			}
		}
		else if (c[0] == '9' && c[1] != '9' && c[2] == '9') {
			//919
			for (int i=1; i<=6; i++) {
				a = String.valueOf(i).charAt(0);
				for (int j=1; j<=6; j++) {
					b = String.valueOf(j).charAt(0);
					if (a != b && b != c[1] && c[1] != a)
						expanded.add(Integer.valueOf(String.format("%c%c%c", a, c[1], b)));
				}
			}
		}
		else if (c[0] != '9' && c[1] == '9' && c[2] == '9') {
			//199
			for (int i=1; i<=6; i++) {
				a = String.valueOf(i).charAt(0);
				for (int j=1; j<=6; j++) {
					b = String.valueOf(j).charAt(0);
					if (a != b && b != c[0] && c[0] != a)
						expanded.add(Integer.valueOf(String.format("%c%c%c", c[0], a, b)));
				}
			}
		}
		else if (c[0] != '9' && c[1] != '9' && c[2] == '9') {
			//129
			if (c[0] != c[1]) {
				for (int i=1; i<=6; i++) {
					a = String.valueOf(i).charAt(0);
					if (a != c[0] && c[1] != a)
						expanded.add(Integer.valueOf(String.format("%c%c%c", c[0], c[1], a)));
				}
			}
		}
		else if (c[0] != '9' && c[1] == '9' && c[2] != '9') {
			//192
			if (c[0] != c[2]) {
				for (int i=1; i<=6; i++) {
					a = String.valueOf(i).charAt(0);
					if (a != c[0] && c[2] != a)
						expanded.add(Integer.valueOf(String.format("%c%c%c", c[0], a, c[2])));
				}
			}
		}
		else if (c[0] == '9' && c[1] != '9' && c[2] != '9') {
			//912
			if (c[1] != c[2]) {
				for (int i=1; i<=6; i++) {
					a = String.valueOf(i).charAt(0);
					if (a != c[1] && c[2] != a)
						expanded.add(Integer.valueOf(String.format("%c%c%c", a, c[1], c[2])));
				}
			}
		} else {
			expanded.add(Integer.valueOf(tmp));
		}
		return expanded;
	}

	@Override
	public void commentResultReceived(CommentViewerBase source, PluginCommentEvent e) {
	}

	@Override
	public void threadReceived(CommentViewerBase source, PluginThreadEvent e) {
		threadMessage = e.getMessage();
	}
	@Override
	public void tick(CommentViewerBase source, TimerPluginEvent arg0) {
	}
	@Override
	public void connected(CommentViewerBase arg0) {
	}
	@Override
	public void disconnected(CommentViewerBase arg0) {
	}

	@Override
	public String getName() {
		return "競艇オッズ返答プラグイン";
	}

	@Override
	public String getVersion() {
		return "1.0.0";
	}
}

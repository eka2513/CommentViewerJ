CommentViewerJ
==============

Java製ニコ生コメントビューア

まだ開発中です。  
ライセンスはMIT予定です。  
最終的にはSWTでGUIつける予定です。

##プラグイン作成方法(暫定)
[SimplePlugin](https://github.com/eka2513/CommentViewerJ/blob/master/src/jp/co/nicovideo/eka2513/commentviewerj/plugin/SimplePlugin.java)
を参考にしてください。

AbstractCommentViewerPluginを継承して作成します  
コメント送信はスーパークラスのメソッドを呼んでください。  
このクラスには[PluginCommentEventListener](https://github.com/eka2513/CommentViewerJ/blob/master/src/jp/co/nicovideo/eka2513/commentviewerj/eventlistener/PluginCommentEventListener.java)がimplementしてあります  
現在は  

	public void threadReceived(PluginThreadEvent e);
	public void commentReceived(PluginCommentEvent e);
	public void commentResultReceived(PluginCommentEvent e);

の３つです。うえからそれぞれ、  

1. コメントサーバに接続した時にはじめに帰ってくる<threadタグ
2. コメント受信時の<chatタグ
3. コメント送信後に帰ってくる<chat_resultタグ

の受信時に呼び出されます

プラグインの呼び出しは、[plugins/plugin.xml](https://github.com/eka2513/CommentViewerJ/blob/master/plugins/plugins.xml)に設定することにより設定されます。  
xmlのファイル名はみたまんまですが、_「パッケージ名.クラス名.xml」_としてください。  
同じディレクトリのxmlファイルを読みに行きます。

_「パッケージ名.クラス名.xml」_ファイルの作成方法ですが
XMLEncoderを使用して作成してください。  
[PluginSettingUtil](https://github.com/eka2513/CommentViewerJ/blob/master/src/jp/co/nicovideo/eka2513/commentviewerj/util/PluginSettingUtil.java)でsaveメソッドを呼ぶことにより簡単に作成出来ます。
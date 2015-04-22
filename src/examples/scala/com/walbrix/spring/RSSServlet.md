title: ROMEを使ってRSS2.0フィードを出力するサーブレット
description: 今時のライブラリ選びについても一言

Javaで <a href="#" data-wikipedia-page="RSS">RSS</a>を出力するためのライブラリは何だっけと探した所、[ROME](http://rometools.github.io/rome/)というのがあったのでそれでフィードの生成処理を書いた。

ある目的のための<a href="#" data-wikipedia-page="オープンソース">オープンソース</a>なライブラリを探す時、一番気にするのはそのライブラリの最終更新日だ。数年間にわたって更新されていないうえにソースコードが <a href="#" data-wikipedia-page="GitHub">GitHub</a>へ移行されず未だに <a href="#" data-wikipedia-page="SourceForge.net">SourceForge</a>に置かれているようなものは既に打ち捨てられていると考えてよい。

一見重要そうな機能を持つライブラリのプロジェクトが打ち捨てられているということは、他にもっと良いものに取って代わられている可能性が高い。

その点では2009年を最後に更新の途絶えていた ROMEも危ぶまれたが、どうやら2014年に GitHubへ移行しグループ名も新たに <a href="#" data-wikipedia-page="Apache_Maven">Maven</a>リポジトリ上でも更新されたようで、まだ「生きている」と判断できた。

記述に <a href="#" data-wikipedia-page="Scala">Scala</a>を使用するなら Scalaのライブラリを探せば良いのではと思われるかもしれないが、Javaの方が良くも悪くも変化が少ない関係で結果的に長く使える（Scalaが廃れてもJavaは残る）のと、Scalaは言語のバージョンが変わるとバイナリが非互換になってしまうという難点がある（その点Javaは奇跡的なまでに後方互換性が高い）ので、なるべく Javaのライブラリを用いるようにしている(といっても実際はケースバイケース)。

### 参照

- [RSS2.0仕様](http://cyber.law.harvard.edu/rss/rss.html)

### このソースからわかること

- Scalaで <a href="#" data-wikipedia-page="Java_Servlet">Javaサーブレット</a>を記述する方法
- [サーブレットの初期化パラメータ (web.xmlに書いてある init-param）を読み取る](http://docs.oracle.com/javaee/6/api/javax/servlet/ServletContext.html#getInitParameter%28java.lang.String%29)方法
    - [web.xmlの例](${contextRoot}/src/examples/webapp/WEB-INF/web.xml)
- [Javaと Scalaでコレクション型の相互自動変換を行う](http://docs.scala-lang.org/ja/overviews/collections/conversions-between-java-and-scala-collections.html)方法
- [Commons IO IOUtils](http://commons.apache.org/proper/commons-io/javadocs/api-release/org/apache/commons/io/IOUtils.html) を使って [InputStream](https://docs.oracle.com/javase/jp/6/api/java/io/InputStream.html)から文字列を一気に読み取る方法
- [長整数(long)型の日付時刻情報を java.util.Date型に変換する](https://docs.oracle.com/javase/jp/6/api/java/util/Date.html#Date%28long%29)方法
- [ROMEを使って RSS2.0フィードを生成する](http://rometools.github.io/rome/RssAndAtOMUtilitiEsROMEV0.5AndAboveTutorialsAndArticles/RssAndAtOMUtilitiEsROMEV0.5TutorialUsingROMEToCreateAndWriteASyndicationFeed.html)方法
- [サーブレットのレスポンスに Content-Typeを設定する](http://docs.oracle.com/javaee/6/api/javax/servlet/ServletResponse.html#setContentType%28java.lang.String%29)方法

title: サーブレット内で自分自身のURLを動的に得る
description: 出、出〜！本番環境のアプリケーションサーバが warファイルを展開した一時ディレクトリにある設定ファイルを毎回手で書き換え奴〜

URL入りの自動送信メールを送ったり <a href="#" data-wikipedia-page="RSS">RSS</a>フィードを出力したりと、Webアプリケーションが自身の絶対URLを出力しなければならない時がたまにある。

アプリケーションのベースになるURLは開発環境と本番環境で必ず違うため、その差異をどこかで吸収しなければならない。まさかソースコードに直書きして、本番環境にデプロイする時に都度手で書きなおしてビルドしてないだろうね？

環境によってベースとなるURLを切り替えるやり方としては

- <a href="#" data-wikipedia-page="環境変数">環境変数</a>を使う
    - おいやめろ貴様
- どこかの設定ファイルに書く
    - web.xmlだったり何とか.propertiesだったり
    - Java Webアプリケーションでは全部のファイルが <a href="#" data-wikipedia-page="WAR_%28アーカイバ%29">war</a>ファイルに固められるので、その中に入ってしまっている設定ファイルを書き換えるのがしんどい（このページの冒頭で述べてる意味不明な一文をここで再読してみよう）
- [Java Preferences API](https://docs.oracle.com/javase/jp/6/api/java/util/prefs/package-summary.html)を使う
    - 誰がこんなの知ってんだよ
- <a href="#" data-wikipedia-page="Java_Naming_and_Directory_Interface">JNDI</a>に設定を書く
    - 「JNDIってデータベース接続設定を書いておく所ですよね。え？あそこって他の設定も書けるんですか？」
- データベースにシステム設定テーブルみたいなものを設けて設定をそこに書く
- 設定とかしないで、[HttpServletRequest](http://docs.oracle.com/javaee/6/api/javax/servlet/http/HttpServletRequest.html)オブジェクトに入っている情報から動的に都度作り出す

のようなものがあるが、これらのうち一番最後のやり方を示すのがこのソースである。

- [request.isSecure](http://docs.oracle.com/javaee/6/api/javax/servlet/ServletRequest.html#isSecure%28%29) - 暗号化された接続かどうか。つまり httpか httpsか。 (この値が当てになるかどうかはサービス構成次第なので気をつけられたし）
- [request.getServerName](http://docs.oracle.com/javaee/6/api/javax/servlet/ServletRequest.html#getServerName%28%29) - URLの[FQDN(ホスト名)](http://ja.wikipedia.org/wiki/Fully_Qualified_Domain_Name)部分。
- [request.getServerPort](http://docs.oracle.com/javaee/6/api/javax/servlet/ServletRequest.html#getServerPort%28%29) - [ポート番号](http://ja.wikipedia.org/wiki/ポート番号)
- [request.getContextPath](http://docs.oracle.com/javaee/6/api/javax/servlet/http/HttpServletRequest.html#getContextPath%28%29) - Webアプリケーションのコンテキストパス (例えばアプリケーションが hoge.warだったら多くの場合 /hoge） 
- それ以下に付け加えたいものがあればご自由に

ちなみに[PHPの場合も似たようなやり方が出来る](${contextRoot}/src/examples/webapp/api.php)。(リンク先ソース内、script_url関数)
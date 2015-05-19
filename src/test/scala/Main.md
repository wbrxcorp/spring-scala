title: 組み込みのJettyを起動する

サーブレットコンテナとしてのJettyを起動するさい、JNDIにデータソースとSMTPメールセッションを登録する処理もしている。

- [CreateMailTrapSession.scala](/src/test/scala/boot/CreateMailTrapSession.scala) - [Mailtrap.io](https://mailtrap.io/) へ向けた [SMTP](http://ja.wikipedia.org/wiki/Simple_Mail_Transfer_Protocol)セッションをJNDIに登録するコード

### このソースからわかるもの

- ScalikeJDBCのSQLログ表示を設定する方法
- <a href="#" data-wikipedia-page="Jetty">Jetty</a>の起動方法
    - [com.walbrix.jetty](${contextRoot}/src/main/scala/com/walbrix/jetty/package.scala)
- 組み込みJettyでデータソースを JNDIに登録する方法
- OSの判定方法
- ChromeDriverの実行バイナリを指定する方法
- [Selenium](http://www.seleniumhq.org/)の開始方法


title: Apache HttpComponentsと JSoupを使って Wikipediaから最初の段落を取ってくる
description: 「用語解説」をWebサイトに埋め込むには Wikipediaの最初の段落を自動的に取ってきて表示するとピッタリくるので、HTTPで Wikipediaからページをもらってきて最初の段落を拾い出すAPIを作成した。

- [Apache HttpComponents](http://hc.apache.org/) - JavaでのHTTP通信を扱うライブラリ。もともとクライアント部分は Commons HttpClientと呼ばれていた。
- [JSoup](http://jsoup.org/) - HTMLを解析して中身を取り出す処理を書くためのライブラリ。名称は Pythonで同じようなことをする著名ライブラリ [BeautifulSoup](http://www.crummy.com/software/BeautifulSoup/)から。
  JSoup自体にもHTTPクライアントとしての機能があるので本当は HttpComponentsを使わなくても同じことができるが、そこはそれ。

なお、[Wikipedia](http://ja.wikipedia.org/wiki/ウィキペディア)に無用な負荷をかけないためにデータベースを使ってキャッシュもする。

- [クライアント (HTML/JavaScript)](${contextRoot}/src/examples/webapp/wikipedia.html)
- [データベース スキーマ](${contextRoot}/src/examples/resources/db/migration/V0002__Wikipedia_cache.sql)
- [ScalikeJdbcSupport](${contextRoot}/src/main/scala/com/walbrix/spring/ScalikeJdbcSupport.scala) - [Spring Framework](http://ja.wikipedia.org/wiki/Spring_Framework)配下で ScalikeJDBCを利用するための trait
- [動作例](${contextRoot}/wikipedia.html)

たまには <a href="https://donate.wikimedia.org">Wikipediaに寄付</a>しましょう。

### このソースからわかること

- Scalaのパターンマッチを使って [正規表現](http://ja.wikipedia.org/wiki/正規表現)で文字列の一部を取り出す方法
- Spring MVCの @ExceptionHandler アノテーションを使って特定の例外に対するハンドラを定義する方法
- Spring MVCのパス名マッピングで、.(ドット)で区切らせずにマッチングを行う方法
- Scalaでリソース開放の処理を隠蔽する方法([Loanパターン](http://www.ne.jp/asahi/hishidama/home/tech/scala/sample/using.html)）
- [Apache HttpComponentsを使って Webサーバに対してHEAD, GETリクエストを実行し、レスポンスヘッダやボディを読み込む](http://hc.apache.org/httpcomponents-client-ga/tutorial/html/fundamentals.html#d5e49)方法
- [Javaと Scalaでコレクション型の相互自動変換を行う](http://docs.scala-lang.org/ja/overviews/collections/conversions-between-java-and-scala-collections.html)方法
- JSoupを使用して HTMLの中身、特に meta rel="canonical" や「特定のdiv配下にある最初のp」を取り出す方法
  他所の Webサーバからページをクロールして自分のシステムに保存する場合は、「自分がアクセスするのに使ったURL」よりも「canonicalなURL」を優先してキーにするべき。
  Wikipediaの場合は metaタグで canonical URLを表明している。
    - [正規 URL を使用する - ウェブマスター ツール ヘルプ](https://support.google.com/webmasters/answer/139066?hl=ja)
- MySQLでいう所の ```INSERT ... ON DUPLICATE KEY UPDATE``` に相当する [```MERGE INTO ...```](http://www.h2database.com/html/grammar.html#merge) を使用してH2 Databaseで「なければ挿入あれば更新」を実現する方法
    - [MySQL: INSERT...ON DUPLICATE KEY UPDATEまとめ - Qiita](http://qiita.com/yuzroz/items/f0eccf847b2ea42f885f)

#### リレーショナルデータベースでの「なければ挿入あれば更新」について

「SELECTして行の存在を確認してからINSERT/UPDATE」だと少なくとも SQLを3本書かなければいけなくてめんどくさいというのもそうだけど、それが[アトミックに実行](http://ja.wikipedia.org/wiki/不可分操作)されることを保証しなければならないことの方が問題。
「このメソッドが呼ばれる時、トランザクションはどんな[隔離レベル](http://ja.wikipedia.org/wiki/トランザクション分離レベル)で実行されてるか」とか「この[DBMS](http://ja.wikipedia.org/wiki/データベース管理システム)はどんな粒度でのロックをサポートしているか」とかいちいち考えたくないので、SQLレベルでサポートされているならその方法を使う方が安全確実。ただしDBベンダ依存になる。
もともと、SQLを生書きしてたら元からほぼ確実にベンダ依存だけどね。
title: ページキャプチャのリクエストハンドラ
description: パラメータで指定されたURLのスクリーンキャプチャ画像を生成して返すリクエストハンドラ。非常にコストのかかる処理なので結果をキャッシュし、同じURLへのリクエストにはキャッシュの内容を返す。

- [動作例](${contextRoot}/image-spinner.html)

リクエストパラメータurlで指定されたURLのスクリーンキャプチャをPNG形式で返す。とはいえ、ユーザーの言いなりで任意のURLに対してアクセスできてしまうようではセキュリティ上の問題があるので、外部ページへのアクセスは特定のドメインのみに限定している。

URLとして http(s)://... で始まらない文字列が与えられた場合、それはこのWebアプリケーション自身のリソースに対するアクセスとして取り扱う。

生成した画像はデータベースにキャッシュし、同じURLに対するページキャプチャ生成要求に対してはキャッシュの内容を返す。

- [PageCapture.scala](PageCapture.scala) - wkhtmltoimageコマンドを呼び出してページキャプチャ処理を行うコード
- [GetContextURL.scala](${contextRoot}/src/main/scala/com/walbrix/servlet/GetContextURL.scala) - サーブレット内で自分自身のURLを動的に生成するためのコード
- [Sha1Hash.scala](Sha1Hash.scala) - 文字列を[SHA-1ハッシュ](http://ja.wikipedia.org/wiki/SHA-1)の16進数表現に変換するコード。一度生成した画像データをデータベースにキャッシュするさい、キーとしてURLのような可変長データをそのまま使用すると効率が悪いので、URLをハッシュにかけたものを[プライマリキー](http://ja.wikipedia.org/wiki/主キー)にしている。
- [V0003__PageCapture_cache.sql](${contextRoot}/src/examples/resources/db/migration/V0003__PageCapture_cache.sql) - キャッシュ用の[テーブル](http://ja.wikipedia.org/wiki/表_%28データベース%29)定義

### このソースからわかること

- Scalaのパターンマッチを使って [正規表現](http://ja.wikipedia.org/wiki/正規表現)で文字列の一部を取り出す方法
- [ScalikeJDBC](http://scalikejdbc.org/)で [Blob](http://ja.wikipedia.org/wiki/バイナリ・ラージ・オブジェクト)型カラムをバイト配列として取得する方法
- [Spring MVC](http://docs.spring.io/spring/docs/current/spring-framework-reference/html/mvc.html)で画像などのバイナリデータをレスポンスする方法
- Spring MVCで[HTTPステータス](http://ja.wikipedia.org/wiki/HTTPステータスコード) 403 Forbidden をレスポンスする方法

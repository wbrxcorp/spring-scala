Smart-Tableで利用するためのサーバーサイド処理

### このソースからわかること

- Scalaで Enumみたいなものを定義する方法 (see [ScalaのEnumerationは使うな - Scalaで列挙型を定義するには | Scala Cookbook](http://xerial.org/scala-cookbook/recipes/2012/06/29/enumeration/))
- ScalikeJDBCの SQLSyntax (sqls"...")を使い、動的に与えられた複数の検索条件から SQLの where句を組み立てる方法
- [Spring MVCの @RequestParam](http://docs.spring.io/spring/docs/current/spring-framework-reference/html/mvc.html#mvc-ann-requestparam)引数ひとつでHTTPのクエリパラメータを全部受けとる方法

<blockquote class="twitter-tweet" lang="en"><p><a href="https://twitter.com/shimariso">@shimariso</a> toAndConditionOpt なんかは QueryDSL 関係無く使えますのでこちらをご利用いただいた方がSQLの見通しがよくなるのでお勧めです。 <a href="https://t.co/iksVDmGz3M">https://t.co/iksVDmGz3M</a></p>&mdash; がくぞ (@gakuzzzz) <a href="https://twitter.com/gakuzzzz/status/585353902322626561">April 7, 2015</a></blockquote> <script async src="//platform.twitter.com/widgets.js" charset="utf-8"></script>

とのことなので後ほど改良する。

### 参照

- [クライアント側コード (HTML/JavaScript)](${contextRoot}/src/examples/webapp/smart-table.html)
- [動作サンプル](${contextRoot}/smart-table.html)
- [データベーススキーマ](${contextRoot}/src/examples/resources/db/migration/V0001__Initial_version.sql)
- [郵便番号データ 13TOKYO.CSV.xz の投入](${contextRoot}/src/examples/scala/db/migration/V9999__TestData.scala)
- [SQLインジェクションは本当に避けられないのか (ワルブリックス株式会社 ブログ)](http://www.walbrix.com/jp/blog/2014-11-is-sql-injection-really-unavoidable.html)
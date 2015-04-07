Smart-Tableで利用するためのサーバーサイド処理

### このソースからわかること

- Scalaで Enumみたいなものを定義する方法 (see [ScalaのEnumerationは使うな - Scalaで列挙型を定義するには | Scala Cookbook](http://xerial.org/scala-cookbook/recipes/2012/06/29/enumeration/))
- ScalikeJDBCの SQLSyntax (sqls"...")を使い、動的に与えられた複数の検索条件から SQLの where句を組み立てる方法
- [Spring MVCの @RequestParam](http://docs.spring.io/spring/docs/current/spring-framework-reference/html/mvc.html#mvc-ann-requestparam)引数ひとつでHTTPのクエリパラメータを全部受けとる方法

### 参照

- [クライアント側コード (HTML/JavaScript)](${contextRoot}/src/examples/webapp/smart-table.html)
- [動作サンプル](${contextRoot}/smart-table.html)
- [データベーススキーマ](${contextRoot}/src/examples/resources/db/migration/V0001__Initial_version.sql)
- [郵便番号データ 13TOKYO.CSV.xz の投入](${contextRoot}/src/examples/scala/db/migration/V9999__TestData.scala)
- [SQLインジェクションは本当に避けられないのか (ワルブリックス株式会社 ブログ)](http://www.walbrix.com/jp/blog/2014-11-is-sql-injection-really-unavoidable.html)
title: ScalikeJDBCで One To Manyのマッピングをする
description: ScalikeJDBCは DSLのようなものでスキーマ定義をあらかじめ与えなくても SQLの実行結果を動的に１対多の関係にマッピングすることができる。

- [動作例](${contextRoot}/tagsinput.html) - タグ入力コントロールの動作サンプル

一般的に[O/Rマッパー](http://ja.wikipedia.org/wiki/オブジェクト関係マッピング)の関連マッピング機構では、システムに何らかの[DSL](http://ja.wikipedia.org/wiki/ドメイン固有言語)で（プロジェクトにおいてグローバルな）スキーマ定義を与える必要があり、常にそれに基づいてのみリレーショナルデータをオブジェクトへマッピングできるが、ScalikeJDBCでは予め構造に関するヒントをシステムに与えておかずとも生のSQL実行結果に対する関連マッピング処理をその場その場で直接記述することができる。これは抽象化された[ドメインモデル](http://ja.wikipedia.org/wiki/ドメインモデル)の共有を全体に渡って徹底するほどでもない程度の小さなプロジェクトでは重宝する。

(例えば、思いつきのスタートアップふぜいには壮大なドメインモデル設計などオーバーキルだ。なぜならどうせ彼らの書いたプログラムは捨てられる運命だからだ。9割のスタートアップは目が出ずに解散するし、残り1割になればシステムを作り直す金に困るはずがない。そう、本来ならば・・・)

products メソッドは、「商品(Product)」とそれを分類する「タグ(Tag)」という典型的な One to Manyモデル(ひとつの商品に対して複数の分類タグが関連付けられる)を例にとって、SQLの実行結果を1対多関係の case classへとマッピングする方法を示している。

このクラスは [Spring Frameworkの @RestController アノテーションにより RESTfulなサービスとして扱われる](https://spring.io/guides/gs/rest-service/)。メソッドの実行結果は自動的にJSONに変換されクライアントにレスポンスされる。

- [スキーマ](${contextRoot}/src/examples/resources/db/migration/V0004__Product.sql) - このサンプルのためのテーブル定義とテストデータ
- [ScalikeJdbcSupport](${contextRoot}/src/main/scala/com/walbrix/spring/ScalikeJdbcSupport.scala) - ワルブリックス株式会社特製の、ScalikeJDBCと Spring Frameworkをインテグレーションする trait


### このソースからわかること

- [ScalikeJDBCで SQLの実行結果を１対多関係の case classへとマッピングする](http://scalikejdbc.org/documentation/one-to-x.html)方法
    - このサンプルではSQLの代わりに ScalikeJDBCのDSLが使用されているが、生SQLを使う場合も考え方は同じとなる。

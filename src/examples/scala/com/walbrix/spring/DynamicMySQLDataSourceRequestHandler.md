title: 動的なデータソースを利用するサービス例
description: データベースへの接続情報をユーザーから受け取りそれをセッションに保存、それを接続情報として生成されたデータソースを利用してクエリを実行する

特殊な場合を除きそのようなことはするべきでないので真似しないこと。

- [Bean定義ファイル](${contextRoot}/src/examples/webapp/WEB-INF/dynamicds-servlet.xml)
- [ScalikeJdbcSupport](${contextRoot}/src/main/scala/com/walbrix/spring/ScalikeJdbcSupport.scala) - ワルブリックス株式会社特製の、ScalikeJDBCと Spring Frameworkをインテグレーションする trait

### このソースからわかること

- Spring MVCの @ExceptionHandler アノテーションで例外ハンドラを定義する方法

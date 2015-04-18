title: SpringでHTTPリクエスト毎に動的にデータソースを生成するためのファクトリ
description: 普通のシングルトンなデータソースだとリクエストの内容に応じて接続先のデータソースを変更することができないので、データソースを requestスコープで定義して毎回生成しようという話

このファクトリは、セッションに保存された接続先ホスト名・DB名・ユーザー名・パスワードを用いて動的にデータソースを生成する。普通のアプリケーションでは接続先データベースを固定するためそのようなことをしないしするべきでもないが、例えば [phpMyAdmin](http://www.phpmyadmin.net/)のようにデータベースの直接的な管理を目的とするアプリケーションを Springのトランザクション管理機構と協調できる形で作るとしたら必要だろう。普通作らないと思うけど。

[DataSource](https://docs.oracle.com/javase/jp/6/api/javax/sql/DataSource.html)オブジェクト自体は(コネクションプール版でない限り) [Connection](https://docs.oracle.com/javase/jp/6/api/java/sql/Connection.html)オブジェクトと違って接続リソースを保持するものではないので都度生成して回収をGCに任せにしてもリソースリークは発生しないはず。

なお、このファクトリを [FactoryBean](http://docs.spring.io/spring/docs/current/javadoc-api/org/springframework/beans/factory/FactoryBean.html)[DataSource] として作るとうまく requestスコープに収まってくれないので POJOにせざるを得なかった。

- [このファクトリを使用するBean定義ファイル](${contextRoot}/src/examples/webapp/WEB-INF/dynamicds-servlet.xml) - どっちかというとこちらの方が本題
- [このデータソースを実際に使用するコード](${contextRoot}/src/examples/scala/com/walbrix/spring/DynamicMySQLDataSourceRequestHandler.scala) (見えないけど)

### このソースからわかること

- MySQL用のデータソースを生成する方法


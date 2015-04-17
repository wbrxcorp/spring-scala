title: ScalikeJDBCを Spring Frameworkに統合する trait
description: ScalikeJDBCをSpringのトランザクション管理機構のもとで利用する

[ScalikeJDBC](http://scalikejdbc.org/)は[DataSource](https://docs.oracle.com/javase/jp/6/api/javax/sql/DataSource.html)を経由せず独自にコネクション管理を行うため、そのままでは Springのトランザクション管理機構と併用できない。

仕方ないので ScalikeJDBCの [DBSession](https://github.com/scalikejdbc/scalikejdbc/blob/master/scalikejdbc-core/src/main/scala/scalikejdbc/DBSession.scala)オブジェクトを Springのトランザクション管理機構向けにカスタマイズしたものを作成した。参照: [ScalikeJDBCを Springで使う - ブログ - ワルブリックス株式会社](http://www.walbrix.com/jp/blog/2013-12-spring-scalikejdbc.html)

DBSessionオブジェクトをカスタマイズするだけだとコネクションをもらったり返したりする操作をいちいち記述しなくてはならないので、Spring JDBCと同様に「SQL文をひとつ実行するごとにコネクションを取得・開放すればええやろ※」コンセプトで APIをラップする traitが ScalikeJdbcSupport である。
 
※[SpringのDataSourceUtils](http://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/jdbc/datasource/DataSourceUtils.html)を経由してコネクションを取得・開放する分には、同一トランザクション内にいる限り同一のコネクションがKeepAlive的に提供されるため一貫性や効率の問題は発生しない
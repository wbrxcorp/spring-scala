title: Flywayのマイグレーション処理を ScalikeJDBCで記述する

[Flyway](http://flywaydb.org/)を使うと、Ruby on Railsのマイグレーションのようにデータベースのスキーマをバージョンごとの増分で記述することができる。
(もっとも、記述方式は Railsのように抽象化されたDSLではなく生の SQLだが)

Flywayは静的な[SQLファイル(DDLを記述したもの)によるマイグレーション](http://flywaydb.org/documentation/migration/sql.html)に加えて、[プログラム(JDBC)によるマイグレーション](http://flywaydb.org/documentation/migration/java.html)もサポートしている。
末尾に掲載のソースは、後者を ScalikeJDBC向けにカスタマイズするための traitとなる。migrateメソッドが abstractとなっているのでこれをオーバーライドして使用する。

- [使用例](${contextRoot}/src/examples/scala/db/migration/V9999__TestData.scala)

### Flywayの動作概要

Flywayは、db.migration パッケージ以下にある下記の名前のファイルをnnnnの若い番号から順に実行する。
 
- Vnnnn__Migration_name.sql
- Vnnnn__Migration_name.class

(nnnnはバージョン番号、Migration_nameは任意の名称)

sqlファイルはSQLとしてそのまま実行される。
classは [JdbcMigration](http://flywaydb.org/documentation/api/javadoc/org/flywaydb/core/api/migration/jdbc/JdbcMigration.html)を implementしている必要がある。

### このソースからわかること

- 素の [java.sql.Connection](https://docs.oracle.com/javase/jp/6/api/java/sql/Connection.html)を [ScalikeJDBCの DBSession](https://github.com/scalikejdbc/scalikejdbc/blob/master/scalikejdbc-core/src/main/scala/scalikejdbc/DBSession.scala)でラップする方法
- [Scalaの trait](http://www.ne.jp/asahi/hishidama/home/tech/scala/trait.html)を in placeで newする方法

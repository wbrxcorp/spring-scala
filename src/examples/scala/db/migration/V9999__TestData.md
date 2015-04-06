このソースからわかるもの

- Flyway でテストデータを投入する方法
- ScalikeJDBCでの INSERT INTO文の使用方法
- OpenCSV で郵便番号データのような SJIS(MS932)のCSVを読み込む方法
- XZ圧縮されたデータを伸長して読み込む方法
- 文字列を NFKC正規化する方法

### 関連

- [V0001__Initial_version.sql](${contextRoot}/src/examples/resources/db/migration/V0001__Initial_version.sql) - スキーマ
- [com.walbrix.flyway.ScalikeJdbcMigration](${contextRoot}/src/main/scala/com/walbrix/flyway/ScalikeJdbcMigration.scala) - Flywayの Migrationを ScalikeJDBCで書くための奴
- [api-servlet.xml](${contextRoot}/src/examples/webapp/WEB-INF/api-servlet.xml) - Spring MVCの DispatcherServletがロードされた時に FlywayのMigrationを実行する例

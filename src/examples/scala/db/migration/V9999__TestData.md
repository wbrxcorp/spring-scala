title: Flywayでプログラムによるテストデータ投入を行う
description: テストデータのFlywayマイグレーションをテスト用のCLASSPATHに設置しておくことでスキーマ定義だけでなくテストデータの投入もできる 

H2などのインメモリデータベースで開発をしている場合は、バージョン番号 9999のマイグレーションをテストデータの投入に使えばスキーマのマイグレーションとバージョン番号がぶつからなくて良い。

永続的なデータベースを開発用に使用している場合（その方が普通だろう）、テストデータのマイグレーションはスキーマのバージョンとぶつからないようにしつつ若い番号から振る必要がある。

### このソースからわかること

- [Flyway](http://flywaydb.org/) でデータベースにテストデータを投入する方法
- ScalikeJDBCでの INSERT INTO文の使用方法
- [OpenCSV](http://opencsv.sourceforge.net/) で[郵便番号データ](http://www.post.japanpost.jp/zipcode/download.html)のような SJIS(MS932)のCSVを読み込む方法
- [XZ](http://ja.wikipedia.org/wiki/Xz_%28%E3%83%95%E3%82%A1%E3%82%A4%E3%83%AB%E3%83%95%E3%82%A9%E3%83%BC%E3%83%9E%E3%83%83%E3%83%88%29)圧縮されたデータを伸長して読み込む方法
- 文字列を [NFKC正規化](http://ja.wikipedia.org/wiki/Unicode%E6%AD%A3%E8%A6%8F%E5%8C%96)する方法
    NFKC正規化をすると、
    - カナが全角に統一される
    - 英数字が半角に統一される
    - 見た目が同一でコードの異なる文字が統一される (※正確さを犠牲にしてわかりやすさを取った説明)

### 関連

- [V0001__Initial_version.sql](${contextRoot}/src/examples/resources/db/migration/V0001__Initial_version.sql) - スキーマ
- [com.walbrix.flyway.ScalikeJdbcMigration](${contextRoot}/src/main/scala/com/walbrix/flyway/ScalikeJdbcMigration.scala) - Flywayの Migrationを ScalikeJDBCで書くための奴
- [api-servlet.xml](${contextRoot}/src/examples/webapp/WEB-INF/api-servlet.xml) - Spring MVCの DispatcherServletがロードされた時に FlywayのMigrationを実行する例

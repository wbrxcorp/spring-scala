title: Flywayでプログラムによるテストデータ投入を行う
description: テストデータのFlywayマイグレーションをテスト用のCLASSPATHに設置しておくことでスキーマ定義だけでなくテストデータの投入もできる 

[H2](http://ja.wikipedia.org/wiki/H2_Database)などのインメモリデータベースで開発をしている場合は、バージョン番号 9999のマイグレーションをテストデータの投入に使えばスキーマのマイグレーションとバージョン番号がぶつからなくて良い。

永続的なデータベースを開発用に使用している場合（その方が普通だろう）、テストデータのマイグレーションはスキーマのバージョンとぶつからないようにしつつ若い番号から振る必要がある。

### このソースからわかること

- [Flyway](http://flywaydb.org/)でデータベースにテストデータを投入する方法
- ScalikeJDBCでの INSERT INTO文の使用方法
- [OpenCSV](http://opencsv.sourceforge.net/)で[郵便番号データ](http://www.post.japanpost.jp/zipcode/download.html)のような [SJIS(MS932)](http://ja.wikipedia.org/wiki/Microsoftコードページ932)の[CSV](http://ja.wikipedia.org/wiki/Comma-Separated_Values)を読み込む方法
- [XZ圧縮](http://ja.wikipedia.org/wiki/Xz_%28ファイルフォーマット%29)されたデータを伸長して読み込む方法
- 文字列を [NFKC正規化](http://ja.wikipedia.org/wiki/Unicode正規化)する方法
    NFKC正規化をすると、
    - カナが全角に統一される
    - 英数字が半角に統一される
    - 見た目が同一でコードの異なる文字が統一される (※正確さを犠牲にしてわかりやすさを取った説明)
- [Loanパターン](http://www.ne.jp/asahi/hishidama/home/tech/scala/sample/using.html)を使ってリソースのクローズを確実に行う方法

### 関連

- [V0001__Initial_version.sql](${contextRoot}/src/examples/resources/db/migration/V0001__Initial_version.sql) - スキーマ
- [com.walbrix.flyway.ScalikeJdbcMigration](${contextRoot}/src/main/scala/com/walbrix/flyway/ScalikeJdbcMigration.scala) - Flywayの Migrationを ScalikeJDBCで書くための奴
- [api-servlet.xml](${contextRoot}/src/examples/webapp/WEB-INF/api-servlet.xml) - Spring MVCの DispatcherServletがロードされた時に FlywayのMigrationを実行する例

### Loanパターン

<blockquote class="twitter-tweet" lang="en"><p><a href="https://twitter.com/shimariso">@shimariso</a> 興味深いですね。本筋ではないですが close 処理は using(res) { r =&gt; } みたいに loan pattern にする方がサンプルとしては適切かもしれません。 <a href="https://t.co/XvZ81K98gT">https://t.co/XvZ81K98gT</a></p>&mdash; seratch_ja (@seratch_ja) <a href="https://twitter.com/seratch_ja/status/589609605925240832">April 19, 2015</a></blockquote>
<script async src="//platform.twitter.com/widgets.js" charset="utf-8"></script>

というご意見を頂いたので、Loanパターンというものを適用してみた。

[Scala using(Hishidama's Scala loan-pattern Memo)](http://www.ne.jp/asahi/hishidama/home/tech/scala/sample/using.html)

によると、「僕が考えたさい強のローンパターン」が色々あるので、これはあくまでパターンであって具体的なやり方まで決まってるわけじゃないみたいだ。
（確か ScalikeJDBCの中にも独自に usingの定義があった気がする）

結局、

[自動的に閉じるSource - ( ꒪⌓꒪) ゆるよろ日記](http://yuroyoro.hatenablog.com/entry/20101215/1292401822)

これが一番簡単そうだったのでそのまま使わせて頂いた。強い型付であるにもかかわらず Rubyみたいにこういう柔軟なことも出来てしまうのは Scalaのすごいところだと思う。

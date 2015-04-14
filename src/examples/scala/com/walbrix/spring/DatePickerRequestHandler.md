JSONのPOSTで日付を受け取り、1ヶ月後の日付を返すサンプル。

### このソースからわかること

- [Joda](http://www.joda.org/joda-time/)の日付時刻型を使ってRESTクライアントから時刻データを受け付ける方法
- Jodaで[タイムゾーン](http://www.joda.org/joda-time/apidocs/org/joda/time/DateTimeZone.html)変換をする方法
- Jodaで日付の加算処理をする方法

### 型のこと

- JavaScriptのDateオブジェクト(の文字列表現, [ISO 8601](http://ja.wikipedia.org/wiki/ISO_8601) 拡張形式の簡略化バージョン)をそのまま受け取りたい場合 = [DateTime](http://www.joda.org/joda-time/apidocs/org/joda/time/DateTime.html)
- YYYY-MM-DD などの簡単な書式でフォーマットされたタイムゾーンの含まれない日付/日付時刻文字列を受け取りたい場合 = [LocalDate](http://www.joda.org/joda-time/apidocs/org/joda/time/LocalDate.html), [LocalDateTime](http://www.joda.org/joda-time/apidocs/org/joda/time/LocalDateTime.html) 

[クライアント(HTML/JavaScript)](${contextRoot}/src/examples/webapp/datepicker.html)

[動作例](${contextRoot}/datepicker.html)
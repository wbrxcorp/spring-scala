[UI Bootstrapの DatePicker](https://angular-ui.github.io/bootstrap/#/datepicker)を使って日付選択のUIを実現するサンプル

### このソースからわかること

- DatePicker（ポップアップ型）の使い方
- AngularJSで日本語ロケールを使用する方法
- [Bootstrapの input-group でテキスト入力とボタンをくっつけて表示する](http://getbootstrap.com/components/#input-groups-buttons)方法
- [jsTimeZoneDetect](http://pellepim.bitbucket.org/jstz/)でJavaScript実行環境のタイムゾーン名称を得る方法
- AngularJSの(スクリプト・テンプレート)内で[日付をフォーマット](https://docs.angularjs.org/api/ng/filter/date)する方法
- 時差というものの面倒くささ

### 参照

- [AngularJSと UI Bootstrapで日付と時刻の入力をする - ブログ - ワルブリックス株式会社](http://www.walbrix.com/jp/blog/2014-03-angularjs-datepicker.html)
- [HTML5アプリケーションでの、サーバとクライアントの時差について - ブログ - ワルブリックス株式会社](http://www.walbrix.com/jp/blog/2014-03-html5-webapp-timezone-difference.html)
- [ローカル時刻で DatePickしたい人達の議論](https://github.com/angular-ui/bootstrap/issues/2072)

[サーバ(Scala/Spring MVC)](../scala/com/walbrix/spring/DatePickerRequestHandler.scala)

[動作例](${contextRoot}/datepicker.html)
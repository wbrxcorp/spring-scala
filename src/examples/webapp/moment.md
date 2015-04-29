title: AngularJSと Moment.jsで経過時間(「○日前」など)を表示する
description: 自分で作ると、結構めどい

- [動作例](${contextRoot}/moment.html)
- [サーバ側コード](${contextRoot}/src/examples/scala/com/walbrix/spring/RecentDocumentRequestHandler.scala) (Scala/Spring MVC)

[Moment.js](http://momentjs.com/)は JavaScriptで日付関係の処理をするためのライブラリで、[angular-moment](https://github.com/urish/angular-moment)を使うことでこれを AngularJSに統合できる。

日付データをフォーマットするだけなら [AngularJSの標準機能にある](https://docs.angularjs.org/api/ng/filter/date)ので Moment.jsの力を借りるまでもないが、あるタイムスタンプからの経過時間(「○日前」など)を表示するには Moment.jsが必要だろう。

ところで、ロケールを明示的にセットせずに Moment.jsを使うとデフォルト中国語？になってしまうようなのだが、これは当方の環境だけだろうか。

### このソースからわかること

- angular-momentを使って AngularJSと Moment.jsを組み合わせる方法
- AngularJSでブラウザの言語設定を知る方法
    - [$window](https://docs.angularjs.org/api/ng/service/$window) サービスを経由して [navigator.language](https://developer.mozilla.org/ja/docs/Web/API/NavigatorLanguage/language)にアクセスすれば良い

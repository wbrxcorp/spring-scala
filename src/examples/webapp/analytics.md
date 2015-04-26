title: AngularJSで Google Analyticsを利用する
description: Angularticsを使うと、Google Analyticsのイベントトラッキング用APIなどを AngularJSのコンテキストで利用できる。

[Google Analytics](http://ja.wikipedia.org/wiki/Google_Analytics)のコードから ```ga('send', 'pageview');``` の行を削除すること（Angularticsがこれ相当の処理を代わりに行うため）

[動作例](${contextRoot}/analytics.html)

### このソースからわかること

- [Angulartics](http://luisfarzati.github.io/angulartics/)を使って Google Analytics用のイベントトラッキングを行う方法
- Google Analyticsを無効にする（[オプトアウト](http://ja.wikipedia.org/wiki/オプトアウト)する）手段をユーザーに提供する方法
  建前上はプライバシー保護のための機能として実装するものではあるが、実質的には関係者のアクセスをアクセス解析にカウントさせないための機能ともいえる。
    - [JavaScriptのグローバル変数を使って Google Analyticsを無効にする](https://developers.google.com/analytics/devguides/collection/gajs/#disable)方法
    - JavaScriptで[Cookie](http://ja.wikipedia.org/wiki/HTTP_cookie)を発生させる方法
- [ngDialog](http://likeastore.github.io/ngDialog/) でポップアップメッセージを表示する方法

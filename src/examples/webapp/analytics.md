title: AngularJSで Google Analyticsを利用する
description: Angularticsを使うと、Google Analyticsのイベントトラッキング用APIなどを AngularJSのコンテキストで利用できる。

<a href="#" data-wikipedia-page="Google_Analytics">Google Analytics</a>のコードから ```ga('send', 'pageview');``` の行を削除すること（Angularticsがこれ相当の処理を代わりに行うため）

[動作例](${contextRoot}/analytics.html)

### このソースからわかること

- [Angulartics](http://luisfarzati.github.io/angulartics/)を使って Google Analytics用のイベントトラッキングを行う方法
- Google Analyticsを無効にする（<a href="#" data-wikipedia-page="オプトアウト">オプトアウト</a>する）手段をユーザーに提供する方法
  建前上は<a href="#" data-wikipedia-page="プライバシー">プライバシー</a>保護のための機能として実装するものではあるが、実質的には関係者のアクセスをアクセス解析にカウントさせないための機能ともいえる。
    - [JavaScriptのグローバル変数を使って Google Analyticsを無効にする](https://developers.google.com/analytics/devguides/collection/gajs/#disable)方法
    - JavaScriptで<a href="#" data-wikipedia-page="HTTP_cookie">Cookie</a>を発生させる方法
- [ngDialog](http://likeastore.github.io/ngDialog/) でポップアップメッセージを表示する方法

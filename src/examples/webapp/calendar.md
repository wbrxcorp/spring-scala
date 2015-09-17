title: AngularJSでカレンダーを表示する
description: 1ページに1ヶ月ずつ表示するカレンダーをAngularJSで実装する

カレンダーデータの生成はサーバ側で行っている。

[動作例](${contextRoot}/calendar.html)
[サーバ(Scala/Spring MVC)](../scala/com/walbrix/spring/CalendarRequestHandler.scala)

### このソースからわかること

- [ng-app属性を使用せずJavaScriptコードで明示的にAngularJSを活性化する方法](https://docs.angularjs.org/guide/bootstrap)
- [同値なオブジェクトが複数含まれる配列をng-repeatで正しくループさせる方法](http://stackoverflow.com/questions/16296670/angular-ng-repeat-error-duplicates-in-a-repeater-are-not-allowed)
- ng-repeatでループのインデックス値(現在何番目の要素を処理しているか)を知る方法
- ng-class属性を使って動的に要素のクラスを与える方法
    - [ngClassの公式ドキュメント](https://docs.angularjs.org/api/ng/directive/ngClass)
    - [AngularJSのng-classを使いこなそう - AngularJS Ninja Blog](http://angularjsninja.com/blog/2013/11/12/angularjs-ngclass/)
    - [ng-classで条件に応じてclass属性を設定 | Webエンジニアブログ](http://dim5.net/angularjs/ng-class-conditional.html)
- ngResourceオブジェクトの getメソッドでAPI呼び出し成功時の処理を記述する方法
    - getオブジェクトの戻り値を直接スコープ変数に代入すると通信中に表示が消えてしまうため呼び出し成功時に初めてスコープ変数を更新する

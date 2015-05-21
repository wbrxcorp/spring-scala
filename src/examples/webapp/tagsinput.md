title: AngularJSでタグ入力コントロールを使用する
description: 分類用のタグ(ラベルとも呼ばれたりする、任意の文字列)を複数付けることの出来る入力コントロールを AngularJSで利用する

- [動作例](${contextRoot}/tagsinput.html)
- [サーバ側コード (Scala/Spring MVC)](${contextRoot}/src/examples/scala/com/walbrix/spring/ProductRequestHandler.scala)

[ngTagsInput](http://mbenford.github.io/ngTagsInput/) を使うことで、タグ入力のコントロール(tags-input)を簡単に設置できる。コントロールは text プロパティを持つ Objectの Arrayにバインドされる。つまりこのコントロールを使って入出力されるのは
```[{text:"タグ1"},{text:"タグ2"},{text:"タグ3"}]```
のようなデータとなる。もしサーバーと通信する際にこれ以外のフォーマットでタグの集合を表現しなければならない場合は、通信の直線と直後で変換処理を行わなければならない。

tags-inputの内側に auto-completeを設置することで、タグの入力候補をサジェストする機能を追加できる。ユーザーが先頭の数文字を入力すると、source属性で指定された式が候補一覧として評価される。式が評価される際、$queryがユーザーの入力した数文字と置き換えられる。式の値はpromiseでも構わないため、サーバーに候補一覧を問い合わせることもできる。

### このソースからわかること

- ngTagsInputを使って入力サジェスト付きのタグ入力コントロールを設置する方法
- AngularJSの [$http](https://docs.angularjs.org/api/ng/service/$http) サービスを使って HTTP GETでサーバからデータを取得する方法
- [AngularJSで数値を通貨表示にフォーマットする](https://docs.angularjs.org/api/ng/filter/currency)方法
- AngularJSの [ng-class](https://docs.angularjs.org/api/ng/directive/ngClass) 属性を使い、真偽値に応じて異なる [FontAwesome](http://fortawesome.github.io/Font-Awesome/)アイコンを表示する方法
- AngularJSの [ng-repeat](https://docs.angularjs.org/api/ng/directive/ngRepeat)属性で 要素を繰り返し表示する際に、1度ごとに空白を挿入する方法
    - [How to add a space after ng-repeat element?](http://stackoverflow.com/questions/18434086/how-to-add-a-space-after-ng-repeat-element) - ng-repeatの代わりに ng-repeat-startと ng-repeat-endを使う
- [Bootstrap](http://ja.wikipedia.org/wiki/Bootstrap)で項目名ラベルと入力コントロールが横に並ぶ形のフォーム ([Horizontal Form](http://getbootstrap.com/css/#forms-horizontal))を設置する方法
- [$resource](https://docs.angularjs.org/api/ngResource/service/$resource) オブジェクトに対する操作で [promise](https://docs.angularjs.org/api/ng/service/$q)を取得する方法

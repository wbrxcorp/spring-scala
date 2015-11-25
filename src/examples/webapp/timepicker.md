title: AngularJSでユーザーに時刻を指定させるディレクティブ
description: きちんとした時刻指定フィールドを設けるのは案外難しいのでディレクティブ化した

[ui-bootstrap](https://angular-ui.github.io/bootstrap/)に TimePickerがあるが、下記の理由で使えない、又は使いにくいことが多い。

- 未選択状態を表現できない ([このパッチで改善する？](https://github.com/angular-ui/bootstrap/pull/4203))
- Dateオブジェクトにバインドされてしまう
    - 必要なフィールドをそこから取り出す処理をしないといけない(時刻指定では大抵時と分しか要らない)
    - もしDateオブジェクトをそのままサーバーに送ると[時差問題](http://www.walbrix.com/jp/blog/2014-03-html5-webapp-timezone-difference.html)の原因となる。
- でかくて邪魔

というわけで、select要素をふたつ並べて数値の配列(時、分)にバインドするうえに未選択状態(null)も表現可能で required指定も可能なディレクティブを作成することにした。
未選択状態を表現可能にするにあたって、時と分のうち片方しか指定されていないちぐはぐな状態を避けるための処置も行っている。

時刻選択については場面によって様々な要求がありえるので、汎用的なディレクティブを作成するのは諦めてその場限りのものを都度作るのがよいのではないかと思う。

- [動作例](${contextRoot}/timepicker.html)

### このソースからわかること

- [Bootstrapでのフォームの組み方](http://getbootstrap.com/css/#forms-horizontal)
- AngularJSで ng-model 属性を持つカスタムディレクティブを作成する方法
- AngularJSで HTMLファイル内にHTML断片のテンプレートを script要素で埋め込む方法

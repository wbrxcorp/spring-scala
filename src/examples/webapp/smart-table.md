AngularJS用のソート・サーチ・ページネーション付きテーブル [Smart-Table](http://lorenzofox3.github.io/smart-table-website/) の利用サンプル。Smart-Tableはページネーションのコントロールを表示する部分だけは Bootstrapに依存している。

### このソースからわかること

- AngularJSと Bootstrapを組み合わせる時にリンクのマウスオーバー時カーソルが縦棒のままになってしまうのを直すための追加スタイル記述
- Smart-Tableをサーバーサイドによるソート・サーチ・ページネーションで利用する方法
- JavaScriptでオブジェクトが特定のプロパティを持っているかどうか調べる方法
- [AngularJSで JavaScriptの Objectを合成する](https://docs.angularjs.org/api/ng/function/angular.extend)方法

### 参照

- [サーバー側コード (Scala w/Spring MVC)](${contextRoot}/src/examples/scala/com/walbrix/spring/SmartTableRequestHandler.scala)
- [動作サンプル](${contextRoot}/smart-table.html)

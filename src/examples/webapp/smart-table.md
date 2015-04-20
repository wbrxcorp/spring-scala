title: AngularJS用のソート・サーチ・ページネーション付きテーブル Smart-Tableを使う

Smart-Tableはページネーションのコントロールを表示する部分だけは <a href="#" data-wikipedia-page="Bootstrap">Bootstrap</a>に依存している。

- [動作サンプル](${contextRoot}/smart-table.html)
- [サーバー側コード (Scala w/Spring MVC)](${contextRoot}/src/examples/scala/com/walbrix/spring/SmartTableRequestHandler.scala)

### このソースからわかること

- AngularJSと Bootstrapを組み合わせる時にリンクのマウスオーバー時カーソルが縦棒のままになってしまうのを直すための追加スタイル記述
- [Smart-Table](http://lorenzofox3.github.io/smart-table-website/)をサーバーサイドによるソート・サーチ・ページネーションで利用する方法
- JavaScriptでオブジェクトが特定のプロパティを持っているかどうか調べる方法
- [AngularJSで JavaScriptの Objectを合成する](https://docs.angularjs.org/api/ng/function/angular.extend)方法

### 参照


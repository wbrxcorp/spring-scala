title: 画像データ付きのエンティティを編集する
description: Webで入力フォームの項目に「画像」があると一気に編集画面の実装が面倒になる。そこで、なるべく面倒でない実装方法を AngularJSで探ってみた。

- [動作例](${contextRoot}/entitywithimage.html)

AngularJSでフォームの編集を行うには、典型的には $resource を使用して JSON形式でサーバ側APIとのやりとりをするが、JSONで表現できるのはせいぜい文字列までである。
そのためフォーム内に画像データの項目があると、画像のフィールドだけ別の受け口を用意してサーバ側で一時ファイルとして保存した上でフォームのエンティティと関連付けるなどの処理をする必要が出てくる。
この処理はコードがとてもわかりにくくなるうえに、ユーザーにとっても画像データがどのタイミングで更新されるのか（ファイルをアップロードした瞬間なのか、それともフォームの保存ボタンを押した時なのか）が不明瞭なので、出来ればもっとシンプルにしたい。

ひとつの答えとして、画像データも文字列に変換して JSONに含めてしまおうという方法を試したのがこの例である。ちょうどHTMLにはバイナリデータを文字列として埋め込める [Data URIスキーム](https://ja.wikipedia.org/wiki/Data_URI_scheme)という仕組みがあるのでそれを使う。

画像ファイルを送信するとシステムにとって都合の良い寸法にリサイズを行い、埋め込み用のBase64文字列に変換してレスポンスするAPI（画像→文字列変換API）をサーバ側に用意する。

フォーム側は、ユーザーのファイル選択やドラッグ＆ドロップ操作により画像ファイルが選択されると画像→文字列変換APIを呼び出し、結果を文字列としてエンティティオブジェクトのプロパティにセットする。するとAngularJSは画像を即時に表示するため、ユーザーはアップロードした画像をすぐにプレビューすることが出来る。

ユーザーがフォームのsaveボタンを押すと、エンティティは $resourceを経由してサーバーにPOSTされる。

サーバーはPOSTされた JSONオブジェクトを通常通り処理するが、画像のフィールドだけはbase64をデコードしてデータベースなりファイルシステムなりに保存する。もっとも、表示側でも Data URIスキームをそのまま使用するならデコードする必要もないだろう。

この方法であれば画像データも JSONオブジェクトに一括で含めて POSTできるため全体がシンプルになる。但し、base64化することにより通信容量に3割程度のオーバーヘッドが発生する

### このソースからわかること

- [Bootstrapでのフォームの組み方](http://getbootstrap.com/css/#forms-horizontal)
- AngularJSの [$resource](https://docs.angularjs.org/api/ngResource/service/$resource) を使用してAPIと通信し、フォームデータのロードとセーブを行う方法
- [AngularJSのフォーム](https://docs.angularjs.org/api/ng/type/form.FormController)の dirty状態を操作する方法
- [ng-file-upload](https://github.com/danialfarid/ng-file-upload) で、マルチパートではなくシングルパートのPOSTリクエストでファイルをアップロードする方法
    - その際、[AngularUI Bootstrapのプログレスバー](https://angular-ui.github.io/bootstrap/#/progressbar)でアップロードの進捗状態を表示する
- [Data URIスキーム](https://ja.wikipedia.org/wiki/Data_URI_scheme)を使って画像データを表示する方法

[サーバ側コード(Scalatra)](${contextRoot}/src/examples/scala/com/walbrix/scalatra/EntityWithImageServlet.scala)

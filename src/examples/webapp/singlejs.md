title: 複数のJavaScriptファイルを結合してレスポンスするPHPスクリプト
description: 単純に結合してレスポンスするだけだとブラウザ側のキャッシュが効かず効率が悪いため、対象となるJSファイルの最終更新日時が変わっていなければ302を返すことでブラウザにキャッシュを使用させる

HTML側からは下記のようにして呼び出す。

```html
<script src="./singlejs.php/hogehoge.js"></script>
```

この時ブラウザはhogehoge.jsなる名前のJavaScriptファイルをロードしているつもりだが、裏ではPHPによって下記のJSファイルが動的に結合されている。

- ./js/singlejs/angular.min.js
- [./js/singlejs/main.js](./js/singlejs/main.js)
- [./js/singlejs/ctrl1.js](./js/singlejs/ctrl1.js)
- [./js/singlejs/ctrl2.js](./js/singlejs/ctrl2.js)

hogehoge.js の hogehoge 部分には実際には任意の文字列を与えることが可能であり、これは JavaScriptのグローバル変数 (windowオブジェクトのプロパティ) としてセットされる。

なぜあらかじめJSファイルを結合しておいて静的にロードさせるのではなくこのような回りくどいことをするかというと、「script要素で JSファイルをひとつ読み込むだけで何らかの機能を利用できる」という状況を **Webデザイナに対して** 提供するためである。

PHPで動的にJSを生成させる方式なら、JSを生成するついでにAPIのエンドポイントなどまで(リクエストパスから)自動で構成できるため、コンフィギュレーションの労力をWebデザイナに負担させずに済む。(むしろ本当の目的は結合自体よりもそちらのほうであると言える)

もっとも、本当に毎回動的にJSファイルを生成していては効率が悪いため、ブラウザの送ってくる If-Modified-Since ヘッダを参照して適切に 304 Not Modifiedを返す。

### このソースからわかること

- PHPで 200以外の[HTTPステータスコード](http://ja.wikipedia.org/wiki/HTTPステータスコード)を返す方法
- PHPでエラー発生時に実行を継続せず強制的に 500 Internel Server Errorを起こさせる方法
- [date.timezone](http://php.net/manual/ja/datetime.configuration.php#ini.date.timezone)が php.iniに設定されていない場合に[date_default_timezone_set関数](http://php.net/manual/ja/function.date-default-timezone-set.php)でタイムゾーンを設定する方法
- [PHPで文字列をJavaScript用にエスケープする](http://php.net/manual/ja/function.json-encode.php)方法
- PHPでPATH_INFOに対して[正規表現](http://ja.wikipedia.org/wiki/正規表現)を用いることでURIからパラメータを抽出する方法
- [PHPでファイルの存在を確認する](http://php.net/manual/ja/function.file-exists.php)方法
- [PHPでファイルの最終更新日時を得る](http://php.net/manual/ja/function.filemtime.php)方法
- PHPで [gmdate関数](http://php.net/manual/ja/function.gmdate.php)を使い時刻をHTTPヘッダの時刻形式にフォーマットする方法
- PHPで[時刻文字列をパースする](http://php.net/manual/ja/function.strtotime.php)方法 (タイムゾーン設定が正しくされている必要あり)
- [PHPで静的ファイルをレスポンスボディに流す](http://php.net/manual/ja/function.readfile.php)方法

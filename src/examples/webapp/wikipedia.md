title: jQueryと Bootstrapを使ってアンカーテキストのクリックでポップアップダイアログを表示する
description: クリックされたら何かの特別なアクションを起こす aタグ(リンク)を jQueryで実現したりなど

特定のdata-属性を持つ aタグをクリックすると付加情報をポップアップ表示する例。ここでは data-wikipedia-page 属性を持つリンクをクリックした際に Wikipediaから取得した情報を表示する。

- [動作例](${contextRoot}/wikipedia.html)
- [サーバ側コード (Scala/Spring MVC)](${contextRoot}/src/examples/scala/com/walbrix/spring/WikipediaRequestHandler.scala) - Wikipediaから指定した項目の概要（最初の段落）をもらってくるAPI

### このソースからわかること

- [jQuery](http://ja.wikipedia.org/wiki/JQuery)で[特定の属性を持つ要素を選択する](https://api.jquery.com/has-attribute-selector/)方法
- jQueryでaタグのクリックイベントに何らかのアクションを割り当てた際に、[元々の動作(href先にジャンプする)を取り消す](http://api.jquery.com/event.preventdefault/)方法
    - [jQuery - aタグの無効化 - preventDefaultとreturn falseの違いとか - Qiita](http://qiita.com/mwtonbel/items/f3c6e2373c348ea74b19)
- jQueryで要素の[属性にアクセスする](https://api.jquery.com/attr/)方法
- jQueryで要素の [data- 属性にアクセスする](https://api.jquery.com/data/)方法
- jQueryで[WebサーバからGETリクエストで JSONデータを取得する](http://api.jquery.com/jquery.getjson/)方法
    - 通信エラーは .fail() でハンドリング、高レベルのエラー(JSONのパースエラーなど)はコールバックの status文字列で判定(成功の場合は "success")
- jQueryを使って [Bootstrapのモーダルを表示・非表示する](http://getbootstrap.com/javascript/#js-programmatic-api)方法
    - [javascript - How to open a Bootstrap modal window using jQuery? - Stack Overflow](http://stackoverflow.com/questions/13183630/how-to-open-a-bootstrap-modal-window-using-jquery)
- jQueryで、[要素の(中身の)最後にHTMLを挿入する](http://api.jquery.com/append/)方法

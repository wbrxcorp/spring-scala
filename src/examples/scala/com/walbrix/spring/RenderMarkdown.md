title: PegDownで Markdownを HTMLに変換する
description: Javaで Markdownを扱うライブラリ PegDown を使って Markdown文字列を HTMLに変換する

- [PegDownの APIドキュメント](http://www.decodified.com/pegdown/api/org/pegdown/package-summary.html)

### このソースからわかること

- [PegDown](https://github.com/sirthias/pegdown)ライブラリを使って [Markdown](http://ja.wikipedia.org/wiki/Markdown)を HTMLに変換する方法
- PegDownにカスタムのリンクレンダラを渡すことでリンクに対するHTML生成をカスタマイズする方法
    - LinkRendererのインスタンスが[スレッドセーフ](http://ja.wikipedia.org/wiki/スレッドセーフ)かどうか[ドキュメント](http://www.decodified.com/pegdown/api/org/pegdown/LinkRenderer.html)には記載がないが、[ソース](https://github.com/sirthias/pegdown/blob/master/src/main/java/org/pegdown/LinkRenderer.java)を確認したところ大丈夫そうなので[シングルトン](http://ja.wikipedia.org/wiki/Singleton_パターン)とした。
    - 関連: [クリックで Wikipediaによる解説がポップアップする文書](${contextRoot}/wikipedia.html) - Wikipediaへのリンクを自動的にこれのための書式でレンダリングしている
    - リンク先URLのドメインに応じて [Font Awesome](http://fortawesome.github.io/Font-Awesome/) を使ったアイコンをアンカーテキストに挿入
    - http:// または https:// で始まるリンクは外部サイトと判断し、target="_blank" を追加
- [URLのホスト名部分を取り出す](https://docs.oracle.com/javase/jp/6/api/java/net/URL.html#getHost%28%29)方法
- Scalaで文字列が正規表現にマッチするかどうかを判定する方法
- Scalaで正規表現によるパターンマッチを行う方法
    - [Scala: 正規表現でマッチするかどうか調べる方法](http://qiita.com/suin/items/63703ad9fd538748ee46)
- [Font Awesomeでアイコンの重ね合わせ表示をする](http://fortawesome.github.io/Font-Awesome/examples/#stacked)方法
    - [Font Awesome で Qiitaロゴっぽいアイコンを表現](http://qiita.com/hkusu/items/fda8d8178dd693f95f3c)
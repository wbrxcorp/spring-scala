title: AngularJSとShowdownで Markdownをプレビュー表示する
description: Showdownと angular-markdown-directiveを使うと Markdownのプレビューを簡単にページに埋め込むことが出来る。

- [動作例](${contextRoot}/markdown.html)
- [Showdown](https://github.com/showdownjs/showdown) - JavaScriptで Markdownをプレビューするためのライブラリ
- [angular-markdown-directive](https://github.com/btford/angular-markdown-directive) - Showdownを AngularJSで利用するためのディレクティブ

### Showdownのエクステンション

Markdown方言をサポートするため、Showdownにはエクステンションの機構がある。
angular-markdown-directiveで Showdown用のエクステンションを有効にするには、[extensions](https://github.com/showdownjs/showdown/tree/master/compressed/extensions)内のjsファイルをロードした上で markdownConverterProviderで有効にする。

```js
config(['markdownConverterProvider', function (markdownConverterProvider) {
  markdownConverterProvider.config({
    extensions: ['twitter']
  });
}])
```
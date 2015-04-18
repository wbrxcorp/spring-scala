title: ファイル名のサフィックス部分を差し替える
description: 正規表現を使ってファイル名のサフィックス(拡張子)部分を書き換える処理の例

```scala
ReplaceFilenameSuffix("Hoge.java", ".scala")
```

のようにして使用する。

### このソースからわかること

- [正規表現で文字列を分割する](https://docs.oracle.com/javase/jp/6/api/java/lang/String.html#split%28java%2elang%2eString%29)方法

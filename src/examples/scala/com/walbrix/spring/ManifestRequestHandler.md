title: warファイルのマニフェストからアプリケーションのバージョンを得る
description: Webアプリケーションは自分のMANIFEST.MFをロードすることで自らのバージョンを知ることができる。

具体的には、[マニフェストのメイン属性](https://docs.oracle.com/javase/jp/1.5.0/guide/jar/jar.html#Main%20Attributes) Implementation-Versionを取得する。

### このソースからわかること

- Spring MVCで、@ExceptionHandlerアノテーションを使って特定の例外を [HTTPのエラーコード](http://ja.wikipedia.org/wiki/HTTP%E3%82%B9%E3%83%86%E3%83%BC%E3%82%BF%E3%82%B9%E3%82%B3%E3%83%BC%E3%83%89)に割り当てる方法
  ここでは [java.io.FileNotFoundException](https://docs.oracle.com/javase/jp/6/api/java/io/FileNotFoundException.html)を 404 Not Foundに割り当てている
- Scalaで関数を引数に取る関数を記述する方法 (参照: [Scala 関数メモ(Hishidama's Scala Function Memo)](http://www.ne.jp/asahi/hishidama/home/tech/scala/function.html))
- [warファイル内のリソースをロードする](http://docs.oracle.com/javaee/6/api/javax/servlet/ServletContext.html#getResourceAsStream%28java.lang.String%29)方法
- [MANIFEST.MFから情報を読み取る](https://docs.oracle.com/javase/jp/6/api/java/util/jar/Manifest.html)方法
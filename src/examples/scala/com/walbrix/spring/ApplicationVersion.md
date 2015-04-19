title: warファイルのマニフェストからアプリケーションのバージョンを得る
description: Webアプリケーションは自分のMANIFEST.MFをロードすることで自らのバージョンを知ることができる。

具体的には、[マニフェストのメイン属性](https://docs.oracle.com/javase/jp/1.5.0/guide/jar/jar.html#Main%20Attributes) <span class="glyphicon glyphicon-globe"></span> Implementation-Versionを取得する。

- [利用例: HighlightServlet.scala](${contextRoot}/src/examples/scala/com/walbrix/spring/HighlightServlet.scala) (initメソッド内)

### このソースからわかること

- [warファイル内のリソースをロードする](http://docs.oracle.com/javaee/6/api/javax/servlet/ServletContext.html#getResourceAsStream%28java.lang.String%29) <span class="glyphicon glyphicon-globe"></span> 方法
- [MANIFEST.MFから情報を読み取る](https://docs.oracle.com/javase/jp/6/api/java/util/jar/Manifest.html) <span class="glyphicon glyphicon-globe"></span> 方法

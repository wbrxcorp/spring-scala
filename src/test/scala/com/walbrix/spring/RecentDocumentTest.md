title: Webアプリケーションコンテキストを必要とするコンポーネントをユニットテストする
description: Springのテストフレームワークには、Web環境を前提に書かれたコードをユニットテストするための機能が備わっている。

[@WebAppConfiguration アノテーションを使う](http://docs.spring.io/spring/docs/current/spring-framework-reference/html/testing.html#testcontext-ctx-management-web)と、[ServletContext](http://docs.oracle.com/javaee/6/api/javax/servlet/ServletContext.html)などといった Web環境特有のサービスに依存するコンポーネントもユニットテストできるようになる。
(普通だと [javax.servlet.ほにゃらら](http://docs.oracle.com/javaee/6/api/javax/servlet/package-summary.html)に依存するクラスはアプリケーションサーバにデプロイするか本格的にモックを用意しない限り動かないが、Springの作法に従って書かれたものに関してはこれである程度何とかしてくれる)

- [RecentDocument](${contextRoot}/src/examples/scala/com/walbrix/spring/RecentDocument.scala) - テスト対象。この対象は classではなく [trait](http://www.ne.jp/asahi/hishidama/home/tech/scala/trait.html)なのでテストケースに mixinして実行できる。
  テスト対象を継承できない場合は ```@Autowired private var target:RecentDocument = _``` などとして Springに DIしてもらう。
  中で ServletContextを使用しているので普通だとアプリケーションサーバ内でしか動作しないが、このテストケースから実行される分には Springがモックを用意して何とかしてくれる。
- [RecentDocumentTest-context.xml](${contextRoot}/src/test/resources/com/walbrix/spring/RecentDocumentTest-context.xml) - このユニットテストを実行するための Bean定義ファイル([@ContextConfigurationアノテーション](http://docs.spring.io/spring/docs/current/javadoc-api/org/springframework/test/context/ContextConfiguration.html)で特に指定しない場合は、クラス名-context.xmlがデフォルトでロードされる)

### このソースからわかること

- JUnitでSpringのコンポーネントをユニットテストする方法
- ダミーのWebアプリケーションコンテキストを用いて Springの Web系コンポーネントをユニットテストする方法

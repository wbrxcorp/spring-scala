title: Jacksonの ObjectMapperを Scala対応にする
description: Java用のJSONプロセッサとしてよく使われている Jacksonだが、Scalaモジュールを追加することで これに Scala対応モジュールを組み込むことで Tupleや Either、Option、case classなどの Scala独自型も扱えるようになる。

ObjectMapperを Spring Frameworkでインスタンス化するには [Jackson2ObjectMapperFactoryBeanを使う](${contextRoot}/src/examples/webapp/WEB-INF/objectMapper.xml)。

### このソースからわかること

- [Jackson ObjectMapper](http://fasterxml.github.io/jackson-databind/javadoc/2.5/com/fasterxml/jackson/databind/ObjectMapper.html)に [DefaultScalaModule](https://github.com/FasterXML/jackson-module-scala/blob/master/src/main/scala/com/fasterxml/jackson/module/scala/DefaultScalaModule.scala)を追加することで Scala対応にする方法
- Jacksonを使って JSONと [JavaBeans](http://ja.wikipedia.org/wiki/JavaBeans)(や[case class](http://www.ne.jp/asahi/hishidama/home/tech/scala/class.html#h_case_class))との相互変換をする際に、Snake caseと [Camel case](http://ja.wikipedia.org/wiki/キャメルケース)の差を吸収する方法
- Scalaで型パラメータの情報を実行時に利用する方法
    - [Scalaで型消去を回避する](http://qiita.com/mather314/items/67fdbf8293edc0200444)
    - このあたりは Scalaの歴史の中でも何度か変遷してるっぽいので、deprecated（だったり、expected to be deprecatedだったり）なやり方をしてしまわないように注意
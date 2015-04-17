title: JacksonのObjectMapperを Scala対応にして Springで利用する
description: Spring Frameworkでは JSONプロセッサとして標準で Jacksonを利用できるが、これに Scala対応モジュールを組み込むことで Tupleや Either、Option、case classなどの Scala独自型も扱えるようになる。

[Jackson ObjectMapper](http://fasterxml.github.io/jackson-databind/javadoc/2.5/com/fasterxml/jackson/databind/ObjectMapper.html) を Scalaモジュール([com.fasterxml.jackson.module.scala.DefaultScalaModule](https://github.com/FasterXML/jackson-module-scala/blob/master/src/main/scala/com/fasterxml/jackson/module/scala/DefaultScalaModule.scala))付きで Springの Bean定義ファイル内に設ける例
ここではついでに [Joda-Time](http://www.joda.org/joda-time/)（[java.util.Date](https://docs.oracle.com/javase/jp/6/api/java/util/Date.html)よりも扱いやすい日付時刻データ型を提供するライブラリ）対応モジュールも追加している

- [この定義を利用している Bean定義ファイル例](${contextRoot}/src/examples/webapp/WEB-INF/api-servlet.xml) - <mvc:message-converters> 内の [MappingJackson2HttpMessageConverter](http://docs.spring.io/spring/docs/current/javadoc-api/org/springframework/http/converter/json/MappingJackson2HttpMessageConverter.html) から参照している

### 参照

- [jackson-module-scala](http://wiki.fasterxml.com/JacksonModuleScala)

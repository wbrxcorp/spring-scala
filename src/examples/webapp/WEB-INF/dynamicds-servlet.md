title: Springで動的なデータソースを実現するためのBean定義ファイル
description: requestスコープを使い、HTTPリクエストごとに異なる接続先を用いるDataSourceを実現する

- [com.walbrix.spring.DynamicMySQLDataSourceFactoryBean](${contextRoot}/src/examples/scala/com/walbrix/spring/DynamicMySQLDataSourceFactoryBean.scala) - リクエスト毎にデータソースを生成するファクトリ
- [com.walbrix.spring.DynamicMySQLDataSourceRequestHandler](${contextRoot}/src/examples/scala/com/walbrix/spring/DynamicMySQLDataSourceRequestHandler.scala) - 利用例

### このソースからわかること

- シングルトンとそうでないBeanのスコープギャップを <aop:scoped-proxy/> で吸収する方法
  <aop:config proxy-target-class="true"/> は必ず入れておいたほうが無難
  requestスコープを使えるようにするための条件を確認すること。([Spring MVCのDispatcherServlet](http://docs.spring.io/spring/docs/current/spring-framework-reference/html/mvc.html#mvc-servlet)を使ってるなら問題ない)
- [factory-bean、factory-method属性を使って POJOなファクトリBeanから Beanをインスタンス化する方法](http://docs.spring.io/spring/docs/current/spring-framework-reference/html/beans.html#beans-factory-class-instance-factory-method)


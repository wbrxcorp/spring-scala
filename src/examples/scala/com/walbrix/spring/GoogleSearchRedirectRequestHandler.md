title: Google検索にリダイレクトするリクエストハンドラ
description: サイト内検索を実現するため、site指定子付きで検索リクエストを Googleにリダイレクトする

[Googleカスタム検索エンジン](https://cse.google.co.jp/cse/)が遅いしあまり使えないので・・・

### このソースからわかること

- [URLEncoderクラスを使って文字列をURLエンコードする](http://docs.oracle.com/javase/jp/7/api/java/net/URLEncoder.html#encode%28java.lang.String,%20java.lang.String%29)方法
- [Spring MVCでリダイレクトを行う](http://docs.spring.io/spring/docs/current/spring-framework-reference/html/mvc.html#mvc-redirecting-redirect-prefix)方法
    - [Redirect to an external URL from controller action in Spring MVC](http://stackoverflow.com/questions/17955777/redirect-to-an-external-url-from-controller-action-in-spring-mvc)
    - [SpringMVCでリダイレクト先ページにパラメータを渡す方法](http://qiita.com/horimislime/items/387fa7805d1552149edb)
    - [Spring MVCのコントローラでの戻り値いろいろ](http://qiita.com/tag1216/items/3680b92cf96eb5a170f0)
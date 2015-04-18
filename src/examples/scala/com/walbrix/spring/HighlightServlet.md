title: ソースコードをハイライト表示するサーブレット
description: ・・・をテーマに、古典的な Javaサーブレットを Scalaで記述する例

このサイトでソースコードをハイライト表示するのに利用している[サーブレット](http://ja.wikipedia.org/wiki/Java_Servlet)。このページもこのサーブレットで表示されている。

このサーブレットはソースファイルのURIに対するリクエストを受け取るように web.xmlでマッピングされる。同じディレクトリ階層にある同名の .mdファイルをそのソースファイルの解説文としてロードし、テンプレートを適用してソースコード本体と一緒に表示する。

template サーブレット初期化パラメタで指定された JSPをテンプレートとして利用する。templateが与えられていない場合は適当なHTMLを出力する。

ハイライト表示といっても実際のハイライト処理はこのサーブレットではなく HTML側で [highlight.js](https://highlightjs.org/)を使って行われている。（サーバ側では HTML向けにソースコードをエスケープしているのみ）

- [このサーブレットをマッピングしている web.xml](${contextRoot}/src/examples/webapp/WEB-INF/web.xml)
- [このサイトで使用しているテンプレート](${contextRoot}/src/examples/webapp/WEB-INF/jsp/highlight.jsp)
- [PegDownを使ってMarkdownをHTMLに変換](./PegDown.scala)
- [ファイル名のサフィックスから言語を判定](./FilenameSuffixes.scala)

### どうしてこんなものを作ったか

- 一度自分で調べたことは後でもう一度調べなおしたくない。何らかの方法で取っておきたい。
- ソースコード断片を後で使える(検索できる)ように保存する方法を考えた
- ソースコードの断片といえどもコンテンツ。自分用のメモであると同時に、他人に読まれることも意識していたほうが資源の有効活用になる。
    - 自分が保有しているドメインの配下に存在している方がSEO的・セルフブランディング的に有利。自社のコンテンツを **[Qiita](https://qiita.com/)で読まれた所で Qiitaのブランドしか読者の印象に残らない** ので、少なくともブランディングの観点からは Qiitaに載せるメリットがない。[gist](https://gist.github.com/)でも同様。
- というか、断片を公開用に切り出してどこかに保存するよりも、実際に動くソースをそのまま晒すほうがひと手間少ないからそうしてみよう。
    - 実際の仕事でコードを書く前に検証用のコードを書くことが多いからそれをひとつのプロジェクトとしてまとめればそこそこ良い分量のサンプル集になる→[spring-scala](http://www.walbrix.com/spring-scala/)
    - [gradle](https://gradle.org/)で [war](http://ja.wikipedia.org/wiki/WAR_%28%E3%82%A2%E3%83%BC%E3%82%AB%E3%82%A4%E3%83%90%29)をビルドする時にソースコードも src/ 以下に含めちゃって、それをブラウズさせるためのサーブレットを仕込もう→これ
    - Java/Scala以外のソースもまあいいや入れちゃえ → [これとか](${contextRoot}/src/examples/webapp/api.php)
- ただのソースコードに自然言語で意味付けをするのは人間がやらなければならない（Google先生は2015年現在まだそれを自動で出来るほど進化していない）。
    - お手本は [Stack Overflow](http://ja.wikipedia.org/wiki/Stack_Overflow)。「○○をする方法は何ですか」という質問にみんながソースコードの断片を貼って回答するので Google先生がソースコードに自然言語で「意味」を紐付けできている。その結果、Googleに自然言語混じりでプログラミング関係の「質問」をすると高確率で Stack Overflowがヒットする。
    - Google先生が物事の意味を理解する材料はなんといってもハイパーリンクのアンカーテキストなので、ソースコード同士を自然言語で適切に記述されたアンカーテキストでまめにリンクしておくことで Google先生によくわかってもらえるという仮説。あと各ページの title と descriptionは超重要。
    - それを自前でやるなら [Markdown](http://ja.wikipedia.org/wiki/Markdown)で書けるようにしたい。ソースファイルと同じ階層に .mdファイルを置くだけでそれが読み込まれるようにしよう。

なのでハイライト表示は単に見やすくするためで、本当の目的はいろんなサンプルソースコードとその説明を Google先生フレンドリーな形で自社サイトに載せること。

### このソースからわかること

- Scalaで Javaサーブレットを記述する方法
- [サーブレットで 404 Not Foundをレスポンスする](http://docs.oracle.com/javaee/6/api/javax/servlet/http/HttpServletResponse.html#sendError%28int%29)方法
- [Commons IO IOUtils](http://commons.apache.org/proper/commons-io/javadocs/api-release/org/apache/commons/io/IOUtils.html) を使って [InputStream](https://docs.oracle.com/javase/jp/6/api/java/io/InputStream.html)から文字列を一気に読み取る方法
- [Commons Lang StringEscapeUtils](https://commons.apache.org/proper/commons-lang/javadocs/api-release/org/apache/commons/lang3/StringEscapeUtils.html)を使って文字列をHTML向けにエスケープする方法

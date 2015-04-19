title: Amazon S3だけで Mavenリポジトリをでっちあげる
description: mavenの面倒なツールを使わなくても、Amazon S3に所定のファイルをアップロードすれば自分用 Mavenリポジトリに。

自分で使うためだけのライブラリを置いておくために、自前の Mavenリポジトリを立ち上げる方法を調べると、やれ **WebDAVサーバー立ち上げろ** だの **ツールをインストールしろ** だのという情報ばかり出てくる。

 ![お断りします](${contextRoot}/img/okotowari.jpg "お断りします")

ただ jarファイル（と依存ライブラリの情報）をパブリックな場所に置いときたいだけなのにそんなん明らかにオーバーキルじゃないですか。

自分で試行錯誤してみたところ、所定のディレクトリ構成で pomや jarファイルをWebサーバに置けば少なくとも Gradleはそれを Mavenリポジトリとして認識してくれることがわかった。
よってAmazon S3だけで Mavenリポジトリができる。

- [spring-scala.pom](spring-scala.pom) - このスクリプトでアップロードする pomファイル
- [s3cmd](http://s3tools.org/s3cmd) - コマンドラインで Amazon S3とファイルのやりとりをするためのツール。Pythonでできている。

### ディレクトリ構造

Base location: http://mvn.walbrix.com/com/walbrix/spring-scala/${version}/

- [spring-scala-${version}.pom](http://mvn.walbrix.com/com/walbrix/spring-scala/${version}/spring-scala-${version}.pom)
- [spring-scala-${version}.jar](http://mvn.walbrix.com/com/walbrix/spring-scala/${version}/spring-scala-${version}.jar)
- [spring-scala-${version}-sources.jar](http://mvn.walbrix.com/com/walbrix/spring-scala/${version}/spring-scala-${version}-sources.jar)

title: gradleに読み込ませるだけの目的で作成されたPOMファイル
description: Mavenのパッケージは POM(XML)ファイルとディレクトリ構造だけででっちあげられる(Gradleからしか参照しない場合)。

**\__VERSION\__** の部分だけ書き換えて Amazon S3に jarファイルを一緒にアップロードすれば Gradleは Mavenパッケージとして認識してくれる。
（Mavenは多分ファイルのハッシュ値とかもいろいろチェックするのでこれだけではダメだが Gradleからしか使わないなら支障ない）

- [push.sh](push.sh) - それをやるスクリプト。build.gradle内にタスクとして書いたほうがスマートだが・・・

### 他プロジェクトからの参照方法

build.gradle

```
repositories {
    mavenCentral()
    maven {
        url "http://mvn.walbrix.com"
    }
}

dependencies {
    compile 'com.walbrix:spring-scala:${version}'
}
```

### ディレクトリ構造

Base location: http://mvn.walbrix.com/com/walbrix/spring-scala/${version}/

- [spring-scala-${version}.pom](http://mvn.walbrix.com/com/walbrix/spring-scala/${version}/spring-scala-${version}.pom)
- [spring-scala-${version}.jar](http://mvn.walbrix.com/com/walbrix/spring-scala/${version}/spring-scala-${version}.jar)
- [spring-scala-${version}-sources.jar](http://mvn.walbrix.com/com/walbrix/spring-scala/${version}/spring-scala-${version}-sources.jar)

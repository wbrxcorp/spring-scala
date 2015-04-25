title: 文字列の SHA-1ハッシュ値を16進文字列で得る 
description: パスワードの保存に使ったり、データ保存時のキーにしたり何かと便利なハッシュ値

但しパスワードなどセキュリティに関わる用途には[SHA-1](http://ja.wikipedia.org/wiki/SHA-1)の代わりにより強力な[SHA-256](http://ja.wikipedia.org/wiki/SHA-2)を使う方が良い。

### このソースからわかること

- Javaの [MessageDigest](https://docs.oracle.com/javase/jp/6/api/java/security/MessageDigest.html) でSHA-1ハッシュ値を計算する方法
- [commons-codecでバイナリデータを16進文字列に変換する](https://commons.apache.org/proper/commons-codec/apidocs/org/apache/commons/codec/binary/Hex.html#encodeHexString%28byte[]%29)方法
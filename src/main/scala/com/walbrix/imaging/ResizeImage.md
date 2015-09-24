title: ImageIOと BufferedImageを使ってイメージファイルをリサイズする
description: イメージファイルの読み書きに ImageIOを使うことで様々な形式の画像ファイルに対応する

リサイズ後の幅または高さを指定することで、アスペクト比を固定したままリサイズが行われる。また、元の画像ファイルフォーマットと同じ形式でリサイズ後の画像ファイルを出力する。入力の InputStreamはシーク可能でなくても良い。

### このソースからわかること

- [ImageIO](https://docs.oracle.com/javase/jp/6/api/javax/imageio/ImageIO.html)を使ってストリームから画像データを読み込み、[BufferedImage](https://docs.oracle.com/javase/jp/6/api/java/awt/image/BufferedImage.html)を生成する方法
    - [画像データを読み込む際に ファイルフォーマットの情報も得る](http://stackoverflow.com/questions/21057191/can-i-tell-what-the-file-type-of-a-bufferedimage-originally-was)
- BufferedImageをリサイズする方法
- アスペクト比を保ったまま矩形のリサイズを行うときの計算式
- ImageIOを使って BufferedImageを画像ファイルの形式でストリームに出力する方法

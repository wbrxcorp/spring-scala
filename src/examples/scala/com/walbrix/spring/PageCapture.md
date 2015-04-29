title: wkhtmltoimageコマンドを使用して Webページのスクリーンキャプチャを取得する
description: 案外Webページの見た目というのは人の印象に残っているものなので、それをサムネイルにしてナビゲーションするという手抜きも有効なのではないかという

- [動作例](${contextRoot}/image-spinner.html)

さすがに Pure Javaでモダンブラウザ並のレンダリングを出来る方法というのは聞いたことがないので、[WebKit](http://ja.wikipedia.org/wiki/WebKit)を使ってレンダリングをする外部コマンドに頼る。
[wkhtmltopdf](http://wkhtmltopdf.org/) には、Webページを PDF化したり画像ファイル化したりするプログラムが含まれている。

WebKitを動かせるほどのサーバー環境設定は知識がないとそれなりに難しいし、キャプチャにあたってはメモリもCPUパワーもかなり消費するので、Web製作者様におかれましてはWebページのキャプチャを自動生成する機能は安請け合いしないほうが良いと思います。(老婆心)

### このコードからわかること

- wkhtmltoimageコマンドの使用方法
    - [Gentoo Linux](http://ja.wikipedia.org/wiki/Gentoo_Linux)の場合は ```emerge wkhtmltopdf``` でインストールする(適当な日本語フォントも忘れずに)
    - [Qt](http://ja.wikipedia.org/wiki/Qt)/[X Window System](http://ja.wikipedia.org/wiki/X_Window_System)に依存するため、サーバサイドで実行するには Xvfbなどの仮想Xサーバを立ちあげておく必要がある
        - おまいらがハマるところ: DISPLAY環境変数 → Xvfbを :0 で起動しているなら ```DISPLAY=:0```
            - アプリケーションの実行環境で環境変数を有効にする方法は色々なので頑張れ
    - 出力ファイル名を - とすることで標準出力に画像データが出力される。但しその場合画像フォーマットを-fオプションで明示的に指定する必要がある。
    - ページ内で利用しているJavaScriptのロードに失敗しただけでも終了ステータス1を返すので注意(その場合でもイメージは出力される)
- [Scalaでサブプロセスを起動](http://www.scala-lang.org/api/current/index.html#scala.sys.process.package)してその[標準出力をキャプチャする](http://www.scala-lang.org/api/current/index.html#scala.sys.process.ProcessIO)方法
    - [How does the “scala.sys.process” from Scala 2.9 work?](http://stackoverflow.com/questions/6013415/how-does-the-scala-sys-process-from-scala-2-9-work)
    - サブプロセスの[終了コード](http://ja.wikipedia.org/wiki/終了ステータス)が0以外の場合は例外を上げる
- [Scalaでリストに入った文字列を連結する](http://www.scala-lang.org/api/current/index.html#scala.io.Source@mkString%28sep:String%29:String)方法
- [ImageIO](https://docs.oracle.com/javase/jp/6/api/javax/imageio/ImageIO.html)でストリームを通じて画像データの読み書きをする方法
- [BufferedImage](https://docs.oracle.com/javase/jp/6/api/java/awt/image/BufferedImage.html)に対して描画を行う方法

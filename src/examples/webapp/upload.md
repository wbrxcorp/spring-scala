[ng-file-upload](https://github.com/danialfarid/ng-file-upload) でファイルアップロードを行うサンプル。

angular-file-uploadとも呼ばれているが別人の作った同名のライブラリがあるため混同しないように注意。[cdnjsでは danialfarid-angular-file-upload として登録されている](https://cdnjs.com/libraries/danialfarid-angular-file-upload)。

### このソースからわかること

- ng-file-upload でファイルをアップロードする（また、ファイルと一緒にテキストなどの付随項目もPOSTする）方法

<hr>

[動作例](${contextRoot}/upload.html)

[サーバ側コード(Scala/Spring MVC)](${contextRoot}/src/examples/scala/com/walbrix/spring/UploadRequestHandler.scala)
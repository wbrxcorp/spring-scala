title: Googleアカウントでのログインを AngularJSで行う
description: Web上で動作するアプリケーションを作る時にログイン機能はほぼ必須だが、とりあえず Googleに任せてしまえば実装の手間を大幅に省くことができる。

- [動作例](${contextRoot}/gplus-login.html)

ログイン機能というものは何の新規性もない割にはいつも必要になるという意味で開発者にとって退屈きわまりないものだ。アプリやサービスのアイディアを形にするのが最優先なら、ログイン機能はGoogleに一旦任せて後回しにしてしまえば良い。幸い今は Androidの普及のおかげで、Googleのアカウントなら誰でも持っている。

[Angular Google Plus Sign-in Directive](http://jeradbitner.com/angular-directive.g-signin/)を使うと、AngularJSアプリケーションにログイン機能を非常に簡単に追加できる。

- アプリ登録(後述)を行い、クライアントIDを取得する
- HTMLテンプレート内のログインボタンを設置したい場所に ```<google-plus-signin clientid="クライアントID">``` を挿入する
- [$scope.$on](https://docs.angularjs.org/api/ng/type/$rootScope.Scope#$on) でログイン成功時のイベント event:google-plus-signin-success に対する処理を記述する
    - ログイン成功時にもらえるアクセストークンを持ってサーバを呼び出す
    - サーバはアクセストークンを使ってGoogleのサーバにユーザー情報を問い合わせ、セッションに保存する - [サーバ側コード例 (Scala/Spring MVC)](${contextRoot}/src/examples/scala/com/walbrix/spring/GoogleApiRequestHandler.scala)
        - アクセストークンは有効期間が短いとはいえパスワード並の効力を持つ情報なのでどこにも保存しないこと

### Google側へのアプリ登録と設定

Googleアカウントでのログイン機能を自分のサイトに設けるには、Google側にアプリ登録をして **クライアントID** を取得する必要がある。

[Googleデベロッパーコンソール](https://console.developers.google.com) にて適当なプロジェクトを作成し、作成したプロジェクトに対して下記の操作を行う。

- APIと認証
    - Googleアカウントでのログインに必要なAPIは "Google+ API" なのでこれを有効にする
    - 認証情報
        - OAuth
            - 新しいクライアントIDを作成
                - アプリケーションの種類=ウェブアプリケーション
                - 承認済みのJavaScript生成元 = サイトのURL(開発用、テスト用など複数ある時は1行ずつ記述)
                - 承認済みのリダイレクトURI = ここでは必要なし
                
### よくある失敗

ログインに失敗する場合は、サイトのURL（ブラウザのアドレスバー部分に表示されるURL）が「承認済みのJavaScript生成元」に登録されているかどうかよく確認すること。URLのホスト名部分は必ずしもインターネットから可視である必要はない（例えばlocalhostでも良い）が、標準以外のポート番号を使用している場合(開発環境ではよくあるはずだ)、URLにはポート番号まで含める必要がある。

### このソースからわかること

- Angular Google Plus Sign-in Directive を使って AngularJSアプリケーションに Googleアカウントでのログイン機能を持たせる方法

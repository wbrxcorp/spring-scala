title: 組み込みJettyのJNDIにメールセッションを登録する
description: Java EEの作法ではメールを送信するためのJavaMailセッションオブジェクトを JNDIから取得することになっているので、組み込みの Jettyでもそれに従ってみる

この例では、メール送信にまつわる機能のデバッグに大変便利なサービスである [Mailtrap.io](https://mailtrap.io/) へ向けた [SMTP](http://ja.wikipedia.org/wiki/Simple_Mail_Transfer_Protocol)セッションを [Jetty](http://ja.wikipedia.org/wiki/Jetty)の[JNDI](http://ja.wikipedia.org/wiki/Java_Naming_and_Directory_Interface)に登録している。

ちなみに、Tomcatの context.xmlに Mailtrap向けのSMTPセッション設定を記述すると下記のようになる。

```xml
<Resource name="mail/Session" type="javax.mail.Session" auth="Container"
    mail.host="mailtrap.io" mail.smtp.user="MAILTRAP_ID" password="MAILTRAP_PASSWORD"
    mail.transport.protocol="smtp" mail.smtp.auth="true" mail.smtp.host="mailtrap.io"
    mail.smtp.port="465" mail.debug="true" mail.smtp.socketFactory.port="465" />
```

### このソースからわかるもの

- 組み込みJettyで Mail Sessionを <a href="#" data-wikipedia-page="Java_Naming_and_Directory_Interface">JNDI</a>に登録する方法
- [Mailtrap](https://mailtrap.io/)を利用する Mail Sessionの作成方法

package com.walbrix.spring

import javax.mail.Session
import javax.mail.internet.InternetAddress

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jndi.JndiObjectFactoryBean
import org.springframework.mail.javamail.{JavaMailSenderImpl, MimeMailMessage, JavaMailSender}

/**
 * Created by shimarin on 14/11/02.
 */
class JisMailMessage(mailSender:JavaMailSender) extends MimeMailMessage(mailSender.createMimeMessage()) {
  val ENCODING = "iso-2022-jp"
  this.getMimeMessage().setHeader("Content-Transfer-Encoding", "7bit")
  def setFrom(address:String,personal:String):Unit = this.getMimeMessage().setFrom(new InternetAddress(address, personal, ENCODING))
  override def setSubject(subject:String):Unit = this.getMimeMessage().setSubject(subject, ENCODING)
  override def setText(text:String):Unit = this.getMimeMessage().setText(text, ENCODING)
  def send():Unit = mailSender.send(getMimeMessage)
}

case class SmtpAuth(username:String,password:String,starttls:Boolean = false)
case class SmtpConfig(host:String = "localhost",port:Int = 25, auth:Option[SmtpAuth] = None)

class MailSender extends JavaMailSenderImpl {
  def this(smtpConfig:SmtpConfig) = {
    this()
    this.setHost(smtpConfig.host)
    this.setPort(smtpConfig.port)
    smtpConfig.auth.foreach { auth =>
      this.setUsername(auth.username)
      this.setPassword(auth.password)
      val props = this.getJavaMailProperties
      props.setProperty("mail.smtp.auth", "true")
      if (auth.starttls) props.setProperty("mail.smtp.starttls.enable", "true")
    }
  }

  def this(session:Session) = {
    this()
    this.setSession(session)
  }

  /**
   * シングルトンのSessionに対してこれを呼ぶと競合を起こす可能性があるので注意すること
   * @param smtpFrom
   */
  def setReturnPath(smtpFrom:String):Unit = {
    getSession().getProperties().setProperty("mail.smtp.from", smtpFrom)
  }
  def createJisMailMessage = new JisMailMessage(this)
  def send(message:MimeMailMessage):Unit = super.send(message.getMimeMessage())
}

class MailSenderFactory {
  def getMailSender():MailSender = new MailSender(SmtpConfig())
}

class ConfigMailSenderFactory extends MailSenderFactory {
  private var host:String = "localhost"
  private var port:Int = 25

  private var username:String = _
  private var password:String = _
  private var startTls:Boolean = false

  def setHost(host:String):Unit = this.host = host
  def setPort(port:Int):Unit = this.port = port
  def setUsername(username:String):Unit = this.username = username
  def setPassword(password:String):Unit = this.password = password
  def setStartTls(startTls:Boolean):Unit = this.startTls = startTls

  override def getMailSender():MailSender = {
    new MailSender(SmtpConfig(
      host, port, Option(username).map { username =>
        SmtpAuth(username, password,startTls)
      }
    ))
  }
}

class JndiMailSenderFactory extends MailSenderFactory {
  private var jndiName:String = "java:comp/env/mail/Session"
  def setJndiName(jndiName:String):Unit = this.jndiName = jndiName
  override def getMailSender():MailSender = {
    val jofb = new JndiObjectFactoryBean()
    jofb.setJndiName(jndiName)
    jofb.afterPropertiesSet()
    new MailSender(jofb.getObject().asInstanceOf[Session])
  }
}

trait EmailSupport {
  @Autowired(required=false) private var mailSenderFactory:MailSenderFactory = new MailSenderFactory

  def createMailSender() = mailSenderFactory.getMailSender()
}

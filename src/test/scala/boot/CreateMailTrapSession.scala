package boot

import org.eclipse.jetty.plus.jndi.Resource

/**
 * Created by shimarin on 15/05/19.
 */
case class MailTrapConfig(id:String="MAILTRAP_ID",password:String="MAILTRAP_PASSWORD")

object CreateMailTrapSession extends com.typesafe.scalalogging.slf4j.LazyLogging {
  def apply(config:MailTrapConfig, jndiName:String = "java:comp/env/mail/Session"):Resource = {
    val mailref = new org.eclipse.jetty.jndi.factories.MailSessionReference()
    mailref.setUser(config.id)
    mailref.setPassword(config.password)
    val properties = new java.util.Properties()
    properties.setProperty("mail.smtp.auth","true")
    properties.setProperty("mail.smtp.host", "mailtrap.io")
    properties.setProperty("mail.smtp.port", "465")
    mailref.setProperties(properties)
    val resource = new Resource(jndiName, mailref) // newするだけでJettyのJNDIには登録されるので呼び出し側はこの値を捨てても構わない
    logger.info("Mailtrap SMTP session '%s' set up with ID='%s'".format(jndiName, config.id))
    resource
  }
}

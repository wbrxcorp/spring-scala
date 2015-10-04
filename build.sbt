// sbt test:console

scalaVersion := "2.11.7"

unmanagedSourceDirectories in Compile += baseDirectory.value / "src/examples/scala"
unmanagedResourceDirectories in Compile += baseDirectory.value / "src/examples/resources"

initialCommands in console := """import collection.JavaConversions._
import Main._
run"""

// main deps

libraryDependencies ++= Seq(
  "org.apache.velocity" % "velocity" % "1.7",
  "org.scalikejdbc" % "scalikejdbc_2.11" % "2.2.8",
  "com.typesafe.scala-logging" % "scala-logging-slf4j_2.11" % "2.1.2",
  "com.yahoo.platform.yui" % "yuicompressor" % "2.4.8",
  "org.flywaydb" % "flyway-core" % "3.2.1",
  "org.pegdown" % "pegdown" % "1.6.0",
  "commons-io" % "commons-io" % "2.4",
  "commons-codec" % "commons-codec" % "1.10",
  "javax.mail" % "mail" % "1.4.7"
)

libraryDependencies ++= Seq(
  "com.fasterxml.jackson.core" % "jackson-databind" % "2.6.2",
  "com.fasterxml.jackson.datatype" % "jackson-datatype-joda" % "2.6.2",
  "com.fasterxml.jackson.module" % "jackson-module-scala_2.11" % "2.6.1",
  "org.json4s" % "json4s-jackson_2.11" % "3.2.11"
)

libraryDependencies ++= Seq(
  "context-support","webmvc","jdbc"
).map(name => "org.springframework" % ("spring-" + name) % "4.2.1.RELEASE")

libraryDependencies ++= Seq(
  "jetty-webapp","jetty-jsp","jetty-plus"
).map("org.eclipse.jetty" % _ % "9.2.13.v20150730")

// examples deps

libraryDependencies ++= Seq(
  "net.sf.opencsv" % "opencsv" % "2.3",
  "org.tukaani" % "xz" % "1.5",
  "org.apache.httpcomponents" % "httpclient" % "4.5.1",
  "org.jsoup" % "jsoup" % "1.8.3",
  "org.twitter4j" % "twitter4j-core" % "4.0.4",
  "com.rometools" % "rome" % "1.5.1",
  "org.bouncycastle" % "bcprov-jdk16" % "1.46",
  "mysql" % "mysql-connector-java" % "5.1.36",
  "com.nimbusds" % "nimbus-jose-jwt" % "4.1.1"
)

// example runtime deps

libraryDependencies ++= Seq(
  "org.aspectj" % "aspectjweaver" % "1.8.7",
  "commons-fileupload" % "commons-fileupload" % "1.3.1",
  "ch.qos.logback" % "logback-classic" % "1.1.3"
)

// test deps

libraryDependencies ++= Seq(
  "junit" % "junit" % "4.12",
  "org.springframework" % "spring-test" % "4.2.1.RELEASE",
  "org.scalatest" % "scalatest_2.11" % "2.2.5",
  "com.h2database" % "h2" % "1.4.189"
)

libraryDependencies ++= Seq(
  "support","chrome-driver"
).map(name => "org.seleniumhq.selenium" % ("selenium-" + name) % "2.47.2")


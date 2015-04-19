package com.walbrix.spring

/**
 * ファイル名のサフィックス(MS-DOS的に言えば拡張子)から言語名称とソースコードハイライターに与えるクラス名を得る
 * 知らないサフィックスの場合は Noneを返す
 * Created by shimarin on 15/04/18.
 */
object FilenameSuffixes {
  // サフィックス部分を取り出すための正規表現
  private val suffix = """.+\.(.+)$""".r

  def apply(filename:String):Option[(String, String)] = filename match {
    case suffix("md") => Some("Markdown","markdown")
    case suffix("html") => Some("HTML", "html")
    case suffix("css") => Some("CSS", "css")
    case suffix("xml") => Some("XML", "xml")
    case suffix("pom") => Some("Maven POM", "xml")
    case suffix("php") => Some("PHP", "php")
    case suffix("jsp") => Some("JSP", "jsp")
    case suffix("java") => Some("Java", "java")
    case suffix("scala") => Some("Scala","scala")
    case suffix("js") => Some("JavaScript", "javascript")
    case suffix("ts") => Some("TypeScript", "typescript")
    case suffix("gradle") => Some("Gradle Buildfile", "gradle")
    case suffix("py") => Some("Python", "python")
    case suffix("sh") => Some("Shell Script", "sh")
    case _ => None
  }
}

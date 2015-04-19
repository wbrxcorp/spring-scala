package com.walbrix.spring

/**
 * Created by shimarin on 15/04/19.
 */
object ApplicationVersion {
  def apply(servletContext:javax.servlet.ServletContext):Option[String] = {
    Option(servletContext.getResourceAsStream("/META-INF/MANIFEST.MF")).map { is =>
      try (Option(new java.util.jar.Manifest(is).getMainAttributes.getValue("Implementation-Version"))) finally(is.close)
    }.flatten // 入れ子になったOptionは flattenすれば1枚剥くことができる
  }
}

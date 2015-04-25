package com.walbrix.spring

import org.pegdown.LinkRenderer

/**
 * Created by shimarin on 15/04/06.
 */
object MyLinkRenderer extends LinkRenderer {
  private val wikipediaPattern = """^http:\/\/ja\.wikipedia\.org\/wiki\/(.+)$""".r
  private val absoluteURLPattern = """^(https?:\/\/.+)$""".r

  override def render(node:org.pegdown.ast.ExpLinkNode, text:String):LinkRenderer.Rendering = {
    node.url match {
      case wikipediaPattern(pageName) =>
        new LinkRenderer.Rendering("#", text).withAttribute("data-wikipedia-page", pageName)
      case absoluteURLPattern(url) =>
        val domain = new java.net.URL(url).getHost
        new LinkRenderer.Rendering(url, "%s<small>(%s)</small>".format(text, domain)).withAttribute("target", "_blank")
      case _ => super.render(node, text)
    }

  }
}

object PegDown {
  def apply(markdown:String):String = {
    // PegDownProcessorは MTセーフでないので、スレッド間で共有しないようにしなくてはならない
    new org.pegdown.PegDownProcessor(org.pegdown.Extensions.ALL).markdownToHtml(markdown, MyLinkRenderer)
  }
}

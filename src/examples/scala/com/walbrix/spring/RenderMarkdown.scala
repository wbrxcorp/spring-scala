package com.walbrix.spring

import org.pegdown.LinkRenderer
import org.pegdown.LinkRenderer.Rendering

/**
 * Created by shimarin on 15/04/06.
 */

/**
 * Markdown文中に書かれたハイパーリンク表記をHTML形式にレンダリングする際に挟む処理
 * ここでは、リンク先サイトによって特殊な書式を適用したりアイコンを挿入したりなどする
 */
object MyLinkRenderer extends LinkRenderer {
  private val wikipediaPattern = """^http:\/\/ja\.wikipedia\.org\/wiki\/(.+)$""".r
  private val absoluteURLPattern = """^(https?:\/\/.+)$""".r

  // リンク先ドメイン名のパターンとそれに対応するリンクレンダラのテーブル
  val sites = Seq[(scala.util.matching.Regex,(String,String)=>Rendering)](
    """^(.*\.)?github.com$""".r -> { (url, text) => // github
      new Rendering(url, """%s<i class="fa fa-github"></i>""".format(text))
    },
    """^(.*\.)?github.io$""".r -> { (url, text) => // github pages
      new Rendering(url, """%s<i class="fa fa-github-alt"></i>""".format(text))
    },
    """^(.*\.)?google.com$""".r -> { (url, text) => // google.com
      new Rendering(url, """%s<span class="fa-stack"><i class="fa fa-stack-1x fa-square"></i><i class="fa fa-google fa-stack-1x fa-inverse"></i></span>""".format(text))
    },
    """^(.*\.)?stackoverflow.com$""".r -> { (url, text) => // SO
      new Rendering(url, """%s<i class="fa fa-stack-overflow"></i>""".format(text))
    },
    """^(.*\.)?qiita.com$""".r -> { (url, text) => // qiita
      new Rendering(url,"""%s<span class="fa-stack"><i class="fa fa-square fa-stack-1x"></i><i class="fa fa-search fa-stack-1x fa-inverse"></i></span>""".format(text))
    }
  )

  override def render(node:org.pegdown.ast.ExpLinkNode, text:String):Rendering = {
    node.url match {
      case wikipediaPattern(pageName) =>
        new Rendering("#", """%s<span class="glyphicon glyphicon-book"></span>""".format(text)).withAttribute("data-wikipedia-page", pageName)
      case absoluteURLPattern(url) =>
        val domain = new java.net.URL(url).getHost
        sites.find(_._1.findFirstIn(domain).nonEmpty).map { case (_, render) =>
          render(url, text).withAttribute("target", "_blank")
        }.getOrElse {
          new Rendering(url, """%s<small>(%s<i class="fa fa-external-link"></i>)</small>""".format(text, domain)).withAttribute("target", "_blank")
        }
      case _ => super.render(node, text)  // 絶対URLでない場合は普通にリンクをレンダリングする
    }
  }
}

object RenderMarkdown {
  def apply(markdown:String):String = {
    // PegDownProcessorは MTセーフでないので、スレッド間で共有しないようにしなくてはならない(要するに使うたびにnewすれば良い)
    new org.pegdown.PegDownProcessor(org.pegdown.Extensions.ALL).markdownToHtml(markdown, MyLinkRenderer)
  }
}


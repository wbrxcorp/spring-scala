package com.walbrix.spring

import org.pegdown.{Extensions, PegDownProcessor}

/**
 * Created by shimarin on 15/04/06.
 */
object PegDown {
  def apply(markdown:String):String = {
    // PegDownProcessorは MTセーフでないので、スレッド間で共有しないようにしなくてはならない
    new PegDownProcessor(Extensions.ALL).markdownToHtml(markdown)
  }
}

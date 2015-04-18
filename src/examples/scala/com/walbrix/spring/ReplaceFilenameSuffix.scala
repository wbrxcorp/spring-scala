package com.walbrix.spring

/**
 * Created by shimarin on 15/04/18.
 */
object ReplaceFilenameSuffix {
  def apply(filename:String, newSuffix:String):String = {
    filename.split("\\.(?=[^\\.]+$)")(0) + newSuffix
  }
}

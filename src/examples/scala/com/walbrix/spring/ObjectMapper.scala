package com.walbrix.spring

import scala.reflect.ClassTag

/**
 * Created by shimarin on 15/04/28.
 */
object ObjectMapper {
  val underlying = new com.fasterxml.jackson.databind.ObjectMapper

  // この ObjectMapperを Scala対応にする
  underlying.registerModule(com.fasterxml.jackson.module.scala.DefaultScalaModule)

  // JSONのプロパティ名 hoge_fuga を JavaBeans(やcase class)のフィールド名 hogeFugaに自動で対応付ける
  underlying.setPropertyNamingStrategy(
    new com.fasterxml.jackson.databind.PropertyNamingStrategy.LowerCaseWithUnderscoresStrategy)

  /**
   * InputStreamで与えられるJSONストリームをT型に変換する
   * Javaでは型パラメータが消去されてしまうので必要な場合は明示的にClassを引数で渡してやらなければいけないが、
   * Scalaでは暗黙の引数としてパラメータ型の情報を渡せるのでそういった引数を省略できる
   */
  def readValue[T:ClassTag](is:java.io.InputStream):T =
    underlying.readValue(is, implicitly[ClassTag[T]].runtimeClass.asInstanceOf[Class[T]])

  // 以降、readValueの他にも必要なメソッドを追加
}

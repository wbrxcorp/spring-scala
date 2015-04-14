package com.walbrix.spring

import com.walbrix.spring.mvc.{Success, Result}
import org.apache.commons.io.IOUtils
import org.springframework.web.bind.annotation._
import org.springframework.web.multipart.MultipartFile

/**
 * Created by shimarin on 15/04/14.
 */

case class Info(name:String,originalFilename:String,size:Long,contentType:String,comments:Option[String])

@RestController
@RequestMapping(Array("upload"))
class UploadRequestHandler {
  @RequestMapping(value=Array(""), method=Array(RequestMethod.POST))
  def upload(@RequestParam(value="comments",required=false) comments:String, @RequestParam file:MultipartFile):Result[Info] = {
    // アップロードされたファイルの情報を取得
    val info = Info(file.getName, file.getOriginalFilename, file.getSize, file.getContentType, Option(comments))

    // 内容は読み捨てる
    Option(file.getInputStream).foreach { is =>
      try {
        IOUtils.toByteArray(is)
      }
      finally {
        is.close()
      }
    }

    Success(info)
  }
}

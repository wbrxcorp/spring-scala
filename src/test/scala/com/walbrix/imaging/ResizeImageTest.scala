package com.walbrix.imaging

import java.io.FileOutputStream
import java.net.{HttpURLConnection, URL, URLConnection}

import org.junit.Test

/**
 * Created by shimarin on 15/09/23.
 */
class ResizeImageTest {
  @Test def testApply():Unit = {
    val src = getClass.getClassLoader.getResourceAsStream("com/walbrix/imaging/test.png")
    val dest = new FileOutputStream("build/test-resized.png")
    ResizeImage(src, dest, Left(320))
  }

  @Test def testApplyToNetworkStream:Unit = {
    val src = new URL("https://31.media.tumblr.com/c76720a16a5487f15d9425a9baf8be2f/tumblr_mvsan9zf8r1r00ey6o1_500.gif").openConnection.getInputStream
    //println(src.markSupported())
    val dest = new FileOutputStream("build/test-resized-from-stream.gif")
    ResizeImage(src, dest, Left(320))
  }
}

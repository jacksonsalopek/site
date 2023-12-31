package com.jacksonsalopek.site

import scala.io.Source

def readFileContent(path: String): String = {
  val stream = this.getClass.getClassLoader
    .getResourceAsStream(path)
  val content = Source.fromInputStream(stream).mkString
  stream.close
  content
}

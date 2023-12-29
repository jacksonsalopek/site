package com.jacksonsalopek.site

import scala.io.Source
import com.github.rjeschke.txtmark.Processor
import com.github.plokhotnyuk.jsoniter_scala.core._

def readFileContent(path: String): String = {
  val stream = this.getClass.getClassLoader
    .getResourceAsStream(path)
  val content = Source.fromInputStream(stream).mkString
  stream.close
  content
}

def readManifest(): Blog = {
  val manifestFileStr = readFileContent("posts/manifest.json")
  readFromString[Blog](manifestFileStr)
}

val renderPost = (contents: String) => Processor.process(contents)

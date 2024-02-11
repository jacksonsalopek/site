package com.jacksonsalopek.site

import scala.io.Source
import org.commonmark.node._
import org.commonmark.parser.Parser
import org.commonmark.renderer.html.HtmlRenderer
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

def renderPost(contents: String): String = {
  val renderer = HtmlRenderer.builder.build
  val doc = Parser.builder.build.parse(contents)
  renderer.render(doc)
}

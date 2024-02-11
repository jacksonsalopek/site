package com.jacksonsalopek.site

import com.github.plokhotnyuk.jsoniter_scala.macros._
import com.github.plokhotnyuk.jsoniter_scala.core._
import org.commonmark.node._
import org.commonmark.parser.Parser
import org.commonmark.renderer.html.HtmlRenderer

case class Post(
    filename: String,
    title: String,
    date: String,
    tags: List[String]
)

case class Blog(posts: List[Post])
object Blog extends HasManifest[Blog]:
  override def manifestPath = "posts/manifest.json"

given blogCodec: JsonValueCodec[Blog] = JsonCodecMaker.make

def getBlog: Blog = readFromString[Blog](Blog.getManifest)
def renderPost(contents: String): String = {
  val renderer = HtmlRenderer.builder.build
  val doc = Parser.builder.build.parse(contents)
  renderer.render(doc)
}

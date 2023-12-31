package com.jacksonsalopek.site

import com.github.rjeschke.txtmark.Processor
import com.github.plokhotnyuk.jsoniter_scala.macros._
import com.github.plokhotnyuk.jsoniter_scala.core._

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
def renderPost(contents: String): String = Processor.process(contents)

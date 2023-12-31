package com.jacksonsalopek.site

import com.github.rjeschke.txtmark.Processor
import com.github.plokhotnyuk.jsoniter_scala.core._

def readBlogManifest: Blog = {
  val manifestFileStr = readFileContent("posts/manifest.json")
  readFromString[Blog](manifestFileStr)
}

val renderPost = (contents: String) => Processor.process(contents)

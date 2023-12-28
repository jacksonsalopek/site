package com.jacksonsalopek.site

import com.github.rjeschke.txtmark.Processor

import scala.collection.JavaConverters._
import scala.io.Source

import ujson.read

def readFileContent(path: String): String = {
  val stream =
    Thread.currentThread.getContextClassLoader.getResourceAsStream(path)
  val content = Source.fromInputStream(stream).mkString
  stream.close
  content
}

case class Post(
    filename: String,
    title: String,
    date: String,
    tags: List[String]
)
case class Blog(posts: List[Post])

def readManifest(): Blog = {
  val manifestFileStr = readFileContent("posts/manifest.json")
  val manifest = read(manifestFileStr)
  Blog(posts = manifest("posts").arr.map { postJson =>
    Post(
      filename = postJson("filename").str,
      title = postJson("title").str,
      date = postJson("date").str,
      tags = postJson("tags").arr.map(_.str).toList
    )
  }.toList)
}

val renderPost = (contents: String) => Processor.process(contents)

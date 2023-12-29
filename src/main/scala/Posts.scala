package com.jacksonsalopek.site

import com.github.rjeschke.txtmark.Processor
import scala.collection.JavaConverters._
import scala.io.Source
import spray.json._

import BlogJsonProtocol._

def readFileContent(path: String): String = {
  val stream =
    Thread.currentThread.getContextClassLoader.getResourceAsStream(path)
  val content = Source.fromInputStream(stream).mkString
  stream.close
  content
}

def readManifest(): Blog = {
  val manifestFileStr = readFileContent("posts/manifest.json")
  manifestFileStr.parseJson.convertTo[Blog]
}

val renderPost = (contents: String) => Processor.process(contents)

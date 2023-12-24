package com.jacksonsalopek.site

import com.github.rjeschke.txtmark.Processor

import java.io.File
import java.net.URL
import java.util.{Collections, Enumeration}

import scala.collection.JavaConverters._
import scala.io.Source

def readFileContent(path: String): String = {
  val stream =
    Thread.currentThread.getContextClassLoader.getResourceAsStream(path)
  val content = Source.fromInputStream(stream).mkString
  stream.close
  content
}

def readFileLines(path: String): Seq[String] = {
  val stream =
    Thread.currentThread.getContextClassLoader.getResourceAsStream(path)
  val lines = Source.fromInputStream(stream).getLines.toSeq
  stream.close
  lines
}

val listPostFiles = () => readFileLines("posts/manifest.txt")
val listPosts = () =>
  listPostFiles().map { fileName => fileName.substring(0, fileName.length - 3) }
val renderPost = (contents: String) => Processor.process(contents)

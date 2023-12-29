package com.jacksonsalopek.site

import spray.json._
import DefaultJsonProtocol._

case class Post(
    filename: String,
    title: String,
    date: String,
    tags: List[String]
)

case class Blog(posts: List[Post])
object Blog

object BlogJsonProtocol extends DefaultJsonProtocol {
  implicit val postFormat: RootJsonFormat[Post] = jsonFormat4(Post.apply)
  implicit val blogFormat: RootJsonFormat[Blog] = jsonFormat1(Blog.apply)
}

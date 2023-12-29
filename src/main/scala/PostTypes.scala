package com.jacksonsalopek.site

import com.github.plokhotnyuk.jsoniter_scala.macros._
import com.github.plokhotnyuk.jsoniter_scala.core._

case class Post(
    filename: String,
    title: String,
    date: String,
    tags: List[String]
)

case class Blog(posts: List[Post])
given blogCodec: JsonValueCodec[Blog] = JsonCodecMaker.make

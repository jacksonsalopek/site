package com.jacksonsalopek.site

import com.github.plokhotnyuk.jsoniter_scala.macros._
import com.github.plokhotnyuk.jsoniter_scala.core._

case class Work(
    title: String,
    desc: String,
    langs: List[String],
    deps: List[String],
    url: String
)
case class Works(works: List[Work])
given worksCodec: JsonValueCodec[Works] = JsonCodecMaker.make

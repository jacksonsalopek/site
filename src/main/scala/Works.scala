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
object Works extends HasManifest[Works]:
  override def manifestPath: String = "works/manifest.json"

given worksCodec: JsonValueCodec[Works] = JsonCodecMaker.make

def getWorks: Works = readFromString[Works](Works.getManifest)

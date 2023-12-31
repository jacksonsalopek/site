package com.jacksonsalopek.site

import com.github.plokhotnyuk.jsoniter_scala.core._

def readWorksManifest: Works = {
  val manifestFileStr = readFileContent("works/manifest.json")
  readFromString[Works](manifestFileStr)
}

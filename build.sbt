import scala.sys.process._

ThisBuild / organization := "com.jacksonsalopek"
ThisBuild / scalaVersion := "3.3.1"
ThisBuild / version := "0.1.0-SNAPSHOT"

lazy val compileWebLangs = taskKey[Unit]("Compile SCSS -> CSS, TS -> JS")

compileWebLangs := {
  "scripts/compile_web_langs.sh" !
}

lazy val root = (project in file("."))
  .enablePlugins(SbtTwirl)
  .enablePlugins(NativeImagePlugin)
  .settings(
    name := "site",
    scalaVersion := "3.3.1",
    libraryDependencies ++= Seq(
      "io.activej" % "activej-launchers-http" % "6.0-beta2",
      "ch.qos.logback" % "logback-classic" % "1.3.4",
      "com.typesafe.scala-logging" %% "scala-logging" % "3.9.5",
      "org.commonmark" % "commonmark" % "0.21.0",
      "com.github.plokhotnyuk.jsoniter-scala" %% "jsoniter-scala-core" % "2.26.2",
      "com.github.plokhotnyuk.jsoniter-scala" %% "jsoniter-scala-macros" % "2.26.2" % "compile-internal"
    ),
    Compile / mainClass := Some("com.jacksonsalopek.site.Main"),
    nativeImageJvm := "graalvm-java21",
    nativeImageVersion := "21.0.1",
    nativeImageOptions ++= Seq(
      "--no-fallback",
      "--allow-incomplete-classpath",
      "--initialize-at-build-time=ch.qos.logback",
      "-H:ReflectionConfigurationFiles=" + baseDirectory.value.getAbsolutePath + "/config/reflectionconfig.json",
      "-H:ResourceConfigurationFiles=" + baseDirectory.value.getAbsolutePath + "/config/resourceconfig.json"
    )
  )

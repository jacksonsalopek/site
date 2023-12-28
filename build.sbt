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
    libraryDependencies ++= Seq(
      "io.activej" % "activej-launchers-http" % "6.0-beta2",
      "com.github.rjeschke" % "txtmark" % "0.13",
      "ch.qos.logback" % "logback-classic" % "1.4.14",
      "com.typesafe.scala-logging" %% "scala-logging" % "3.9.5",
      "com.lihaoyi" %% "ujson" % "3.1.3"
    ),
    Compile / mainClass := Some("com.jacksonsalopek.site.Main"),
    nativeImageJvm := "graalvm-java21",
    nativeImageVersion := "21.0.1"
  )

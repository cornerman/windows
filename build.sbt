import scala.scalanative.sbtplugin.{ScalaNativePlugin, ScalaNativePluginInternal}
import ScalaNativePlugin.autoImport._

name := "windows"
organization := "org.windows"
version := "0.0.1"

scalaVersion := "2.11.8"

nativeVerbose := true
nativeClangOptions := Seq("-O2")
// nativeEmitDependencyGraphPath := Some(file("out.dot"))

ScalaNativePluginInternal.projectSettings

libraryDependencies ++= (
    compilerPlugin("org.scala-native" % "tools_2.10" % "0.1-SNAPSHOT") ::
    compilerPlugin("org.scala-native" % "nir_2.10" % "0.1-SNAPSHOT") ::
    compilerPlugin("org.scala-native" % "util_2.10" % "0.1-SNAPSHOT") ::
    "org.scala-native" %% "scalalib" % "0.1-SNAPSHOT" ::
    Nil
  )

import ReleaseTransformations._
import sbtcrossproject.CrossPlugin.autoImport.{ crossProject, CrossType }

name := "minart"

organization in ThisBuild := "eu.joaocosta"
publishTo in ThisBuild := sonatypePublishToBundle.value
scalaVersion in ThisBuild := "2.11.12"
licenses in ThisBuild := Seq("MIT License" -> url("http://opensource.org/licenses/MIT"))
homepage in ThisBuild := Some(url("https://github.com/JD557/minart"))
scmInfo in ThisBuild := Some(
  ScmInfo(
    url("https://github.com/JD557/minart"),
    "scm:git@github.com:JD557/minart.git"
  )
)

val sharedSettings = Seq(
  organization := "eu.joaocosta",
  scalaVersion := "2.11.12",
  crossScalaVersions := Seq("2.11.12", "2.12.11", "2.13.3"),
  licenses := Seq("MIT License" -> url("http://opensource.org/licenses/MIT")),
  homepage := Some(url("https://github.com/JD557/minart")),
  scmInfo := Some(
    ScmInfo(
      url("https://github.com/JD557/minart"),
      "scm:git@github.com:JD557/minart.git"
    )
  ),
  autoAPIMappings := true
)

val testSettings = Seq(
  libraryDependencies ++= Seq(
    "org.specs2" %%% "specs2-core" % "4.8.3" % Test
  ),
  scalacOptions in Test ++= Seq("-Yrangepos")
)

val noTestSettings = Seq(
  libraryDependencies --= Seq(
    "org.specs2" %%% "specs2-core" % "4.8.3" % Test
  ),
  test := (()),
  test in Test := (())
)

val publishSettings = Seq(
  publishMavenStyle := true,
  publishArtifact in Test := false,
  pomIncludeRepository := { _ => false },
)

val noPublishSettings = Seq(
  skip in publish := true,
  publish := (()),
  publishLocal := (()),
  publishArtifact := false,
  publishTo := None
)

val jsSettings = Seq(
  libraryDependencies ++= Seq(
    "org.scala-js" %%% "scalajs-dom" % "1.0.0"
  )
)

val nativeSettings = Seq(
  scalaVersion := "2.11.12",
  crossScalaVersions := Seq("2.11.12"),
  libraryDependencies ++= Seq(
    "com.regblanc" %%% "native-sdl2" % "0.1"
  ),
  nativeLinkStubs := true,
  nativeMode := "release"
) ++ noPublishSettings ++ noTestSettings


lazy val root =
  crossProject(JVMPlatform, JSPlatform, NativePlatform)
  .in(file("."))
  .settings(sharedSettings)
  .settings(name := "minart")
  .settings(publishSettings)
  .jsSettings(jsSettings)
  .nativeSettings(nativeSettings)
  .dependsOn(core, pure)
  .aggregate(core, pure)

lazy val core =
  crossProject(JVMPlatform, JSPlatform, NativePlatform)
    .settings(sharedSettings)
    .settings(name := "minart-core")
    .settings(testSettings)
    .settings(publishSettings)
    .jsSettings(jsSettings)
    .nativeSettings(nativeSettings)

lazy val pure =
  crossProject(JVMPlatform, JSPlatform, NativePlatform)
    .dependsOn(core)
    .settings(sharedSettings)
    .settings(name := "minart-pure")
    .settings(testSettings)
    .settings(publishSettings)
    .jsSettings(jsSettings)
    .nativeSettings(nativeSettings)

lazy val examples = (project in file("examples"))
    .settings(sharedSettings)
    .settings(name := "minart-examples")
    .settings(noPublishSettings)
    .aggregate(
      `examples-colorSquare`.jvm,     `examples-colorSquare`.js,
      `examples-pureColorSquare`.jvm, `examples-pureColorSquare`.js,
      `examples-fire`.jvm,            `examples-fire`.js,
      `examples-snake`.jvm,           `examples-snake`.js,
      `examples-mousePointer`.jvm,    `examples-mousePointer`.js)

def example(project: sbtcrossproject.CrossProject.Builder, exampleName: String) = {
    project
    .in(file(s"examples/${exampleName}"))
    .dependsOn(core)
    .settings(sharedSettings)
    .settings(name := s"minart-examples-${exampleName}")
    .settings(noPublishSettings)
    .jsSettings(jsSettings)
    .jsSettings(scalaJSUseMainModuleInitializer := true)
    .nativeSettings(nativeSettings)
}

lazy val `examples-colorSquare` =
  example(crossProject(JVMPlatform, JSPlatform, NativePlatform), "colorsquare")

lazy val `examples-pureColorSquare` =
  example(crossProject(JVMPlatform, JSPlatform, NativePlatform), "purecolorsquare").dependsOn(pure)

lazy val `examples-fire` =
  example(crossProject(JVMPlatform, JSPlatform, NativePlatform), "fire")

lazy val `examples-snake` =
  example(crossProject(JVMPlatform, JSPlatform, NativePlatform), "snake")

lazy val `examples-mousePointer` =
  example(crossProject(JVMPlatform, JSPlatform, NativePlatform), "mousepointer")

releaseCrossBuild := true
releaseTagComment := s"Release ${(version in ThisBuild).value}"
releaseCommitMessage := s"Set version to ${(version in ThisBuild).value}"

releaseProcess := Seq[ReleaseStep](
  checkSnapshotDependencies,
  inquireVersions,
  runClean,
  runTest,
  setReleaseVersion,
  commitReleaseVersion,
  tagRelease,
  releaseStepCommandAndRemaining("+publishSigned"),
  releaseStepCommand("sonatypeBundleRelease"),
  setNextVersion,
  commitNextVersion,
  pushChanges)

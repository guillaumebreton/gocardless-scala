import scalariform.formatter.preferences._
import bintray.Keys._

name := "gocardless-scala"

version := "1.0.0"

organization := "octalmind"

licenses += ("MIT", url("http://opensource.org/licenses/MIT"))

scalaVersion := "2.11.6"

crossScalaVersions := Seq("2.11.6", "2.10.4")

libraryDependencies ++= Seq(
  "io.spray" %% "spray-client" % "1.3.3",
  "io.spray" %% "spray-json" % "1.3.0",
  "com.typesafe.akka" %% "akka-actor" % "2.3.9",
  "joda-time" % "joda-time" % "2.7",
  "org.joda" % "joda-convert" % "1.7",
  "org.scalatest" %% "scalatest" % "2.2.1" % "test"
)

scalacOptions ++= Seq(
  "-language:implicitConversions",
  "-unchecked",
  "-feature",
  "-deprecation",
  "-encoding", "UTF-8",
  "-language:existentials",
  "-language:higherKinds",
  "-language:implicitConversions",
  "-Xfatal-warnings",
  "-Xlint:_",
  "-Xfuture",
  "-Yno-adapted-args",
  "-Ywarn-dead-code",
  "-Ywarn-numeric-widen",
  "-Ywarn-value-discard"
)

scalariformSettings

ScalariformKeys.preferences := FormattingPreferences()
    .setPreference(RewriteArrowSymbols, true)
    .setPreference(AlignParameters, true)
    .setPreference(AlignSingleLineCaseStatements, true)

// publishMavenStyle := false
bintrayPublishSettings

repository in bintray := "maven"

bintrayOrganization in bintray := None

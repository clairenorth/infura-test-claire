import Dependencies._
import sbt.Keys.{libraryDependencies, logLevel}
import sbt.{Level, ThisBuild}
import sbtassembly.AssemblyPlugin.assemblySettings
val Http4sVersion = "0.22.2"
val CatsEffectVersion = "2.3.3"
val CirceVersion = "0.14.1"
val MunitVersion = "0.7.27"
val LogbackVersion = "1.2.5"
val GatlingVersion = "3.6.1"


ThisBuild / scalaVersion := "2.13.6"
ThisBuild / version := "0.1.0-SNAPSHOT"
ThisBuild / organization := "cn"


lazy val root = (project in file("."))
  .settings(settings)
  .aggregate(service, loadTests)

lazy val service = (project in file("ethereum-service"))
  .enablePlugins(JavaAppPackaging, DockerPlugin)
  .settings(settings)
  .settings(
    name := "ethereum-service",
    libraryDependencies ++= Seq(
      "org.http4s" %% "http4s-blaze-server" % Http4sVersion,
      "org.http4s" %% "http4s-circe" % Http4sVersion,
      "org.http4s" %% "http4s-dsl" % Http4sVersion,
      "org.typelevel" %% "cats-effect" % CatsEffectVersion,
      "io.circe" %% "circe-generic" % CirceVersion,
      "org.scalameta" %% "munit" % MunitVersion % Test,
      "ch.qos.logback" % "logback-classic" % LogbackVersion,
      PureConfig.library,
      Web3.library,
      Circe.core,
      Circe.generic,
      Circe.parser
    )
  )
  .settings(
    Docker / packageName := "ethereum-service",
    Docker / maintainer := "Claire North",
    dockerBaseImage := "openjdk:11",
    dockerExposedPorts := Seq(8080),
    dockerExposedVolumes := Seq("/opt/docker/logs")
  )
  .settings(
    assemblySettings,
    Compile / mainClass := Some("cn.ethereum.Main"),
    logLevel := Level.Warn,
  )

lazy val loadTests = (project in file("load-tests"))
  .settings(settings, assemblySettings)
  .enablePlugins(GatlingPlugin)
  .settings(libraryDependencies ++= Seq("io.gatling.highcharts" % "gatling-charts-highcharts" % GatlingVersion % "test,it",
    "io.gatling" % "gatling-test-framework" % GatlingVersion % "test,it",
    "io.gatling" % "gatling-core" % GatlingVersion))

lazy val settings = Seq(
  scalacOptions ++=  Seq(
    "-unchecked",
    "-feature",
    "-language:existentials",
    "-language:higherKinds",
    "-language:implicitConversions",
    "-language:postfixOps",
    "-deprecation",
    "-encoding",
    "utf8"
  ),
  resolvers ++= Seq(
    "Local Maven Repository" at "file://" + Path.userHome.absolutePath + "/.m2/repository",
    Resolver.sonatypeRepo("releases"),
    Resolver.sonatypeRepo("snapshots")
  )
)
exportJars := true
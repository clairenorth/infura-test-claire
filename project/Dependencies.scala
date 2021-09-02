import sbt._

object Dependencies {
  object Testing {
    lazy val scalaTest = "org.scalatest" %% "scalatest" % "3.2.8"
  }

  object PureConfig {
    lazy val version = "0.15.0"
    lazy val library = "com.github.pureconfig" %% "pureconfig" % version
  }

  object Web3 {
    lazy val version = "3.5.0"
    lazy val library = "org.web3j" % "core" % version
  }

  object Circe {
    lazy val version = "0.14.1"
    lazy val core = "io.circe" %% "circe-core" % version
    lazy val generic = "io.circe" %% "circe-generic" % version
    lazy val parser = "io.circe" %% "circe-parser" % version
  }
}

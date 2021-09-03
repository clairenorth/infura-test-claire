package cn.ethereum

import cats.effect.{ExitCode, IO, IOApp}
import org.log4s.{Logger, getLogger}
import org.log4s.Logger.InfoLevelLogger

import scala.concurrent.ExecutionContext
import scala.concurrent.ExecutionContext._
// Entry point to application
object Main extends IOApp {
  // starts the server and passes an implicit execution context
  override def run(args: List[String]): IO[ExitCode] = {
    implicit val ec: ExecutionContext = Implicits.global
    implicit val logger = getLogger
    Server.run
      .use(_ => IO.never)
      .as(ExitCode.Success)
  }
}

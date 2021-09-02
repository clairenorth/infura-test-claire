package cn.ethereum
import cats.effect._
import cats.implicits.toSemigroupKOps
import cn.ethereum.routes.{Api, HealthCheck}
import cn.ethereum.config.{Config, ServerConfig}
import cn.ethereum.service.HttpsEthereumService
import org.http4s.HttpRoutes
import org.http4s.blaze.server.BlazeServerBuilder
import org.http4s.server.{Router, Server => BlazeServer}
import org.http4s.implicits._

import scala.concurrent.ExecutionContext
import org.http4s.implicits._
import org.log4s.Logger
import org.web3j.protocol.http.HttpService
import org.web3j.protocol.{Web3j, Web3jService}

import scala.jdk.CollectionConverters

object Server {

  def run(implicit cs: ContextShift[IO],
          timer: Timer[IO],
          ec: ExecutionContext,
          logger: Logger): Resource[IO, BlazeServer] = {
    for {
      conf <- config
      key = conf.infuraApiConfig.key
      web3Service = Web3j
        .build(
          new HttpService(
            s"https://${conf.network}.infura.io/v3/${key}",
            true
          )
        )
      _ = IO(logger.info(s"Connected to Ethereum network ${conf.network}"))
      service = new HttpsEthereumService(web3Service)
      // <+> composes the routes together
      routes = HealthCheck.route <+> Api.routes(service)
      server <- server(conf.serverConfig, routes)
    } yield server
  }

  private[this] def config(implicit logger: Logger): Resource[IO, Config] = {
    for {
      config <- Resource.eval(Config.fromFile)
    } yield {
      logger.info(s"App configuration loaded ${config.toString}")
      config
    }
  }

  // Function that wires up server
  private[this] def server(config: ServerConfig, routes: HttpRoutes[IO])(
      implicit contextShift: ContextShift[IO],
      ec: ExecutionContext,
      timer: Timer[IO]
  ): Resource[IO, BlazeServer] = {
    BlazeServerBuilder[IO](ec)
      .bindHttp(config.port, config.host)
      .withHttpApp(routes.orNotFound)
      .resource
  }
}

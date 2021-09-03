package cn.ethereum.route

import cats.effect.IO
import io.circe.Json
import org.http4s.HttpRoutes
import org.http4s.dsl.io._
import org.http4s.circe.jsonEncoder

object HealthCheck {

  def route: HttpRoutes[IO] = HttpRoutes.of {
    case GET -> Root / "health" => Ok(Json.fromString("OK"))
  }
}

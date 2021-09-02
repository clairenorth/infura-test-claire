package cn.ethereum.routes

import cats.effect.{ContextShift, IO, Timer}
import cn.ethereum.service.EthereumService
import org.http4s.{EntityEncoder, HttpRoutes, MediaRange, Response}
import org.http4s.dsl._
import org.http4s.circe.jsonEncoder
import _root_.io.circe.syntax.EncoderOps
import _root_.io.circe.generic.semiauto._
import _root_.io.circe.Json

import cats.parse.Parser
import cn.ethereum.model.{GeneralError, InvalidBlockParam}
import cn.ethereum.routes.ApiUtils.{mapToDefaultBlockParam, present}
import cn.ethereum.service.models.EthereumNetworkError
import org.http4s.circe.CirceEntityCodec.circeEntityEncoder
import org.log4s.Logger
import org.web3j.protocol.core.DefaultBlockParameter
import org.web3j.protocol.core.methods.response.EthBlock
import org.web3j.protocol.core.methods.response.EthBlock.{Block, ResponseDeserialiser, TransactionResult}

import java.math.BigInteger
import scala.util.Try

object Api extends Http4sDsl[IO] {
  def routes(service: EthereumService)(implicit cs: ContextShift[IO],
                                       timer: Timer[IO],
                                       logger: Logger): HttpRoutes[IO] = {
    HttpRoutes.of[IO] {
      case GET -> Root / "getBlockByNumber" / blockParameter / showTransactionDetails => {
        for {
          block <- mapToDefaultBlockParam(blockParameter)
          result <- service.getBlockByNumber(
            block,
            showTransactionDetails.toBoolean
          )
          resp <- Ok(result.map(_.getRawResponse).asJson)
        } yield resp
      }.handleErrorWith {
        case invalid: InvalidBlockParam =>
          BadRequest(invalid.msg)
        case e: GeneralError =>
          BadGateway(e.getMessage)
      }

      case GET -> Root / "getTransactionByBlockNumberAndIndex" / blockParameter / transactionIndexPosition => {
        for {
          block <- mapToDefaultBlockParam(blockParameter)
          result <- service.getTransactionByBlockNumberAndIndex(
            block,
            org.web3j.utils.Numeric.toBigInt(transactionIndexPosition)
          )
          json = result.map(_.getRawResponse).asJson
          resp <- if (json.findAllByKey("error").nonEmpty) {
                  val msg = json.hcursor.downField("error").downField("message").top
                  BadGateway(msg.getOrElse(Json.Null))
                } else {
                  Ok(json)
                }
        } yield resp
      }.handleErrorWith {
        case invalid: InvalidBlockParam =>
          BadRequest(invalid.msg)
        case e: GeneralError =>
          BadGateway(e.getMessage)
      }
    }
  }
}

object ApiUtils {

  val mapToDefaultBlockParam =
    (blockParam: String) =>
      for {
        result <- IO(DefaultBlockParameter.valueOf(blockParam)).attempt
          .flatMap { x =>
            x match {
              case Left(e)      => IO(DefaultBlockParameter.valueOf(BigInteger.valueOf(blockParam.toLong)))
              case Right(value) => IO.pure(value)
            }
          }
      } yield result
}

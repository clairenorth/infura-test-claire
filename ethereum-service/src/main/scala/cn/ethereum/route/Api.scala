package cn.ethereum.route

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
import ApiUtils.mapToDefaultBlockParam
import cn.ethereum.service.models.EthereumNetworkError
import org.http4s.circe.CirceEntityCodec.circeEntityEncoder
import org.log4s.Logger
import org.web3j.protocol.core.DefaultBlockParameter
import org.web3j.protocol.core.methods.response.EthBlock
import org.web3j.protocol.core.methods.response.EthBlock.{Block, ResponseDeserialiser, TransactionResult}

import java.math.BigInteger
import scala.util.Try

// HTTP routes for the application. Modeled like functions.
object Api extends Http4sDsl[IO] {
  def routes(service: EthereumService)(implicit cs: ContextShift[IO],
                                       timer: Timer[IO],
                                       logger: Logger): HttpRoutes[IO] = {
    HttpRoutes.of[IO] {
      case GET -> Root / "blockByNumber" / blockParameter / showTransactionDetails => {
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
        case general: GeneralError =>
          BadGateway(general.getMessage)
      }

      case GET -> Root / "transactionByBlockNumberAndIndex" / blockParameter / transactionIndexPosition => {
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

  val mapToDefaultBlockParam: String => IO[DefaultBlockParameter] =
    (blockParam: String) =>
      for {
        result <- IO(DefaultBlockParameter.valueOf(blockParam))
          .flatMap(IO.pure).handleErrorWith(e => IO(DefaultBlockParameter.valueOf(new BigInteger(blockParam,16))))
      } yield result
}

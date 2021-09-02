package cn.ethereum.service

import cats.effect.{Async, ContextShift, IO}
import cn.ethereum.config.Config
import cn.ethereum.service.models.EthereumNetworkError
import io.circe.parser
import org.log4s.Logger
import org.web3j.protocol.core.methods.response.EthBlock.Block
import org.web3j.protocol.core.methods.response.{
  EthBlock,
  EthTransaction,
  Transaction
}
import org.web3j.protocol.core.{DefaultBlockParameter, Request}
import org.web3j.protocol.{Web3j, Web3jService}

import java.math.BigInteger
import scala.concurrent.ExecutionContext
import scala.language.implicitConversions
import scala.jdk.FutureConverters._
import scala.util.{Failure, Success}

trait EthereumService {

  /**
    * Returns information about a block by hash.
    * @param blockParam an integer block number, or the string "latest", "earliest" or "pending"
    * @param showTransactionDetails if set to true, it returns the full transaction objects, if false only the hashes of the transactions.
    * @return A block object, or None when no block was found
    */
  def getBlockByNumber(
      blockParam: DefaultBlockParameter,
      showTransactionDetails: Boolean = false
  ): IO[Option[EthBlock]]

  /**
    * Returns information about a transaction by block number and transaction index position.
    * @param blockParam an integer block number, or the string "latest", "earliest" or "pending"
    * @param transactionIndexPosition a hex of the integer representing the position in the block
    * @return A transaction object, or None when no transaction was found
    */
  def getTransactionByBlockNumberAndIndex(
      blockParam: DefaultBlockParameter,
      transactionIndexPosition: BigInteger): IO[Option[EthTransaction]]

}

class HttpsEthereumService(web3Service: Web3j)(
    implicit cs: ContextShift[IO],
    ec: ExecutionContext,
    logger: Logger
) extends EthereumService {
  override def getBlockByNumber(
      blockParam: DefaultBlockParameter,
      showTransactionDetails: Boolean
  ): IO[Option[EthBlock]] = Async[IO].async { callback =>
    web3Service
      .ethGetBlockByNumber(
        blockParam,
        showTransactionDetails
      )
      .sendAsync()
      .asScala
      .onComplete {
        case Failure(exception) =>
          logger.error(
            s"Error sending request to network: ${exception.getMessage}")
          callback(Left(EthereumNetworkError(exception)))
        case Success(value) =>
          callback(Right(Option(value)))
      }
  }
  //index of a transaction in the specified block
  override def getTransactionByBlockNumberAndIndex(
      blockParam: DefaultBlockParameter,
      transactionIndexPosition: BigInteger): IO[Option[EthTransaction]] =
    Async[IO].async { callback =>
      web3Service
        .ethGetTransactionByBlockNumberAndIndex(
          blockParam,
          transactionIndexPosition
        )
        .sendAsync()
        .asScala
        .onComplete {
          case Failure(exception) =>
            logger.error(
              s"Error sending request to network: ${exception.getMessage}")
            callback(Left(EthereumNetworkError(exception)))
          case Success(value) =>
            callback(Right(Option(value))) // null will translate to None in Scala
        }
    }
}

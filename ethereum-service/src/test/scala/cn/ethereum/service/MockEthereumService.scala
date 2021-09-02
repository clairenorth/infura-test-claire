package cn.ethereum.service
import cats.effect.IO
import org.web3j.protocol.core.DefaultBlockParameter
import org.web3j.protocol.core.methods.response.{EthBlock, EthTransaction}

import java.math.BigInteger

class MockEthereumService(resultA: IO[Option[EthBlock]],
                          resultB: IO[Option[EthTransaction]])
    extends EthereumService {

  override def getBlockByNumber(
      blockParam: DefaultBlockParameter,
      showTransactionDetails: Boolean): IO[Option[EthBlock]] = resultA

  override def getTransactionByBlockNumberAndIndex(
      blockParam: DefaultBlockParameter,
      transactionIndexPosition: BigInteger): IO[Option[EthTransaction]] =
    resultB
}

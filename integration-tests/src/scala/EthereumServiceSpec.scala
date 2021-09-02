import scala.concurrent.ExecutionContext

class EthereumServiceSpec extends AnyFreeSpec {
  implicit val contextShift: ContextShift[IO] = IO.contextShift(ExecutionContext.global)

}
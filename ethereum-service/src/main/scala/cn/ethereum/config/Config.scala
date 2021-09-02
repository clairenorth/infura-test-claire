package cn.ethereum.config

import cats.effect.IO
import pureconfig.ConfigSource
import pureconfig.generic.auto.exportReader

final case class Config(network: String,
                        infuraApiConfig: InfuraApiConfig,
                        serverConfig: ServerConfig)

object Config {
  def fromFile: IO[Config] = IO(ConfigSource.default.loadOrThrow[Config])
}

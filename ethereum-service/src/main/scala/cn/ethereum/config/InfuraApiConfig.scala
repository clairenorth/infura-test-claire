package cn.ethereum.config

import cats.effect.IO
import cn.ethereum.model.InvalidConfig

final case class InfuraApiConfig(key: String, secret: Option[String] = None)

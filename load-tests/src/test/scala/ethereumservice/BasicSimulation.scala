package ethereumservice

import io.gatling.core.Predef._    // required for Gatling core structure DSL
import io.gatling.jdbc.Predef._    // can be omitted if you don't use jdbcFeeder
import io.gatling.http.Predef._    // required for Gatling HTTP DSL

import scala.concurrent.duration._ // used for specifying duration unit, eg "5 second"

class BasicSimulation extends Simulation {

  // 5 static block numbers
  // transactions in those block numbers
  // basic load
  val blockNumbers = Array("0xc88cdd", "0xc88cd9")

  before {
      println("Load test simulation is about to start!")
    }

    after {
      println("Load test simulation is finished!")
    }

  val httpProtocol = http
    .baseUrl("http://localhost:8080")
    .acceptHeader("text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8") // Here are the common headers
    .acceptEncodingHeader("gzip, deflate")
    .acceptLanguageHeader("en-US,en;q=0.5")
    .userAgentHeader("Mozilla/5.0 (Macintosh; Intel Mac OS X 10.8; rv:16.0) Gecko/20100101 Firefox/16.0")

  val scn1 = scenario("Method 1") // A scenario is a chain of requests and pauses
    .exec(http("request_2")
      .get("/getBlockByNumber/0xc88cdd/genesis"))
    .pause(2)
    .exec(http("request_3")
      .get("/getBlockByNumber/0xc88cd9/latest"))
    .pause(2000.milliseconds)

  val scn2 = scenario("Method 2") // A scenario is a chain of requests and pauses
    .exec(http("request_2")
      .get("/getBlockByNumber/0xc88cdc/genesis"))
    .pause(2)
    .exec(http("request_3")
      .get("/computers/6"))
    .pause(3)
    .exec(http("request_4")
      .get("/"))

  setUp(scn1.inject(atOnceUsers(2)).protocols(httpProtocol))
}


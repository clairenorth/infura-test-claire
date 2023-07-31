# Infura API Load Testing Service 
author: Claire North

## Application Structure
Built using SBT modules. <br>
`/docs` - contain API documentation and Future Improvements if I were to make v 2.0. <br>
sbt modules: <br>
`/ethereum-service` - Decentralized application that enables interaction with the Ethereum blockchain network(s) via Infura Infrastructure. Built using Scala and HTTP4s framework. <br>
`/load-tests` - Load Tests creating using the Gatling Framework and run locally. Includes test results. <br>

## Ethereum Service
### Code structure
Modeled loosely off of [hexagonal architecture pattern](https://medium.com/idealo-tech-blog/hexagonal-ports-adapters-architecture-e3617bcf00a0).
- `Main.scala` - entrypoint to the application. Start the server and runs indefinitely until the JVM is killed
- `Server.scala` - methods to wire up server by loading configuration, binding http routes, creating [blaze](https://github.com/http4s/blaze) server as a resource. Has run method that is utilized by `Main`
- `/route` - location of all HTTP related files or code executed at outermost layer during request lifecycle (before core application logic). Includes specific HTTP routes and health check. 
- `/model` - location of domain models such as Error class.
- `/config` - location of config files modeled as classes. [PureConfig](https://github.com/pureconfig/pureconfig) maps the `application.conf` files to classes under the hood.
- `/service` - location of logic that interacts with Infura API. Actual API calls aren't executed here, but rather in the `/route` files. HTTP4s and IO type allows for easy decoupling of method creation and method execution.

Getting Started
Prerequisites:
* SBT - v1.5.5 - Build Tool (https://www.scala-sbt.org/1.x/docs/Installing-sbt-on-Mac.html) 
  * If you have brew installed, `brew install sbt`

Built using:
* Scala - v2.13.6
* [Http4s](https://http4s.org/) - v0.22.2 - Web Framework <br>
* Web3j - Web3 SDK to build transactions to be sent <br>
* Infura - Web 3 Infrastructure Provider <br>
* [Gatling](https://gatling.io/) - Load testing Framework <br>

* API Doc [link](docs/Api.md) <br>

## Running
This project utilizes [sbt-native-packager](http://www.scala-sbt.org/sbt-native-packager/) to Dockerize the application.

To run this application using Docker for the first time: <br>
From the root directory, `sbt project service/Docker/publishLocal && docker run --rm -p 8080:8080 ethereum-service:0.1.0-SNAPSHOT`. <br>
   This will build project, package it, create a Docker image for the service, then run the image in a container.  <br>
   a. If you want to view sbt generated Dockerfile, `cat target/docker/stage/Dockerfile`

Afterwards, service is accessible via `http://localhost:8080`. To check if the service is up
`http://localhost:8080/health`. You should see an "OK" response if it is.

## Locally via SBT
```
cd ethereum-service
sbt clean compile
sbt run
```
or from root directory
- options: service
```
sbt project {module-Name}/run
```

You can change the ethereum network the service will connect to 1 of two ways:
1. ENV variables
2. changing value in `application.conf` directory

For ENV variables,
Can set it one of two ways:
1. Pass via command line arguments <br>
   ex: `-Dkey=val` will set params as JVM arguments
2. Add them to `.sbtopts` file in the same format as passing via the command line

## Development
This project uses sbt-revolver, a plugin for SBT enabling a super-fast development turnaround for Scala applications.
It sports the following features:

Starting and stopping your application in the background of your interactive SBT shell (in a forked JVM)
Triggered restart: automatically restart your application as soon as some of its sources have been changed
To run the app via the plugin:
```
sbt ~reStart
```

## Load Tests
Prereq: have local instance of service running.

### Run from root directory
```
sbt project loadTests/Gatling/test
```

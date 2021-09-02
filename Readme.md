# Ethereum Service
Decentralized application that enables interaction with the Ethereum blockchain network(s) via Infura Infrastructure. Built using Scala.

## Architecture
## Components
Http4s - Web Framework <br>
Web3j - Web3 SDK to build transactions to be sent <br>
Infura - Web 3 Infrastructure Provider

## Running the dApp
### Entire app
`sbt run`

### Service alone
` sbt project service/run`

**Passing Environment Variables** <br>
Can do it one of two ways:
1. Pass via command line arguments <br>
   ex: `-Dkey=val` will set params as JVM arguments
2. Add them to `.sbtopts` file in the same format as passing via the command line

## Docker
This project utilizes [sbt-native-packager](http://www.scala-sbt.org/sbt-native-packager/) to dockerize the application.

To run this application using Docker: <br>
Prereq: Have Docker Engine running in the background
1. Run `sbt docker:publishLocal`. This will build project, package it, and build a Docker image on your local Docker server running. <br>
   1. If you want to view sbt generated Dockerfile, `cat target/docker/stage/Dockerfile`
2. Run `docker run --rm -p8080:8080 ethereum-service:{current-version-of-image}` Example: `docker run --rm -p8080:8080 ethereum-service:0.1.0-SNAPSHOT`.


## Development
`sbt ~reStart`
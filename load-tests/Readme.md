# Load Tests

Load test suite developed using Gatling Framework.

# Development
Sbt - version 1.5.5  <br>
[Gatling](https://gatling.io/docs/) - Framework <br>
# Additional
[Ethscan](git@github.com:shazow/ethspam.git) - to gather sample Json-RPC requests. <br>

# Results
Found in [docs](docs/Results.md).

# How to run
You’ll be able to run Gatling simulations using the prefix "gatling" + ":" + SBT standard test, testOnly, testQuick, etc… tasks from root directory.
ex) `sbt project loadTests/Gatling/test`

```
 sbt project loadTests/Gatling/test
```

At the end of each execution of a Gatling script, a Gatling Results Report is automatically created.

You will see a message in the console about where the report is located, i.e.:
```
Please open the following file: /Users/cnorth/Desktop/myGatlingTest/target/gatling/runtimeparameters-20200207112322164/index.html
```
The report will contain lots of useful metrics on the response times of your requests, as well as details of errors that were encountered.
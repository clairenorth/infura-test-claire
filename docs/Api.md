# Api

Document in lieu of a more formalized api spec.

# Endpoints

## GET /health
- Health check for service. Returns Ok if service is up and running.

## GET /blockByNumber/{{blockNumber}}/{{showTransactionDetails}}
Parameters:
- blockNumber
  - Type: String
  - Options:
    - HEX String - an integer block number
      - ex. c88cdd
    - String "earliest" for the earliest/genesis block
    - String "latest" - for the latest mined block
    - String "pending" - for the pending state/transactions
- showTransactionDetails
  - Type: String
  - Description: Boolean representing whether to show full transactional info

## GET /transactionByBlockNumberAndIndex/{{blockNumber}}/{{transactionIndexPosition}}
Parameters:
- blockNumber
  - Type: String
  - Options:
    - HEX String - an integer block number
    - String "earliest" for the earliest/genesis block
    - String "latest" - for the latest mined block
    - String "pending" - for the pending state/transactions
- transactionIndexPosition
  - Type: String
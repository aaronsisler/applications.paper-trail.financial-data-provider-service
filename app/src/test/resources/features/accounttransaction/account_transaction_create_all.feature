Feature: Account Transaction: Create

  Scenario: Create account transactions endpoint returns correctly when valid transactions with the same account ids are present
    Given application is up
    And valid transactions with the same account id are part of the request body for the create transaction endpoint
    And account id exists in the database for the transactions
    And the database connection succeeds for create transactions
    When the create transactions endpoint is invoked
    Then the newly created transactions are returned from the create transaction endpoint
    And the newly created transactions exist in the data store

  Scenario: Create transactions endpoint returns correctly when multiple account ids are present
    Given application is up
    And valid transactions with different account ids are part of the request body for the create transaction endpoint
    And both account ids exist in the database for the transactions
    And the database connection succeeds for create transactions
    When the create transactions endpoint is invoked
    Then the newly created transactions are returned from the create transaction endpoint
    And the newly created transactions exist in the data store

  Scenario Outline: Create transactions endpoint returns correct error when account id does not exist
    Given application is up
    And valid transactions with different account ids are part of the request body for the create transaction endpoint
    And one of the account transaction's account id does not exist in the database
    And the database connection succeeds for create transactions
    When the create transactions endpoint is invoked
    Then the correct bad request response is returned from the create transactions endpoint
      | <statusCode> | <responseMessage> |

    Examples:
      | statusCode | responseMessage             |
      | 400        | Account Ids do not exist: 2 |

  Scenario Outline: Create transactions endpoint returns correct error when relational issues occur
    Given application is up
    And valid transactions with the same account id are part of the request body for the create transaction endpoint
    And account id exists in the database for the transactions
    And the database save fails given a account id was deleted during the create transaction database call
    When the create transactions endpoint is invoked
    Then the correct bad request response is returned from the create transactions endpoint
      | <statusCode> | <responseMessage> |

    Examples:
      | statusCode | responseMessage                            |
      | 400        | A provided field is relationally incorrect |

  Scenario Outline: Create transactions endpoint returns correct errors when required field is missing
    Given application is up
    And a transaction in the request body has an invalid input
      | <accountTransactionId> | <accountId> | <amount> | <transactionDate> | <description> |
    When the create transactions endpoint is invoked
    Then the correct bad request response is returned from the create transactions endpoint
      | <statusCode> | <responseMessage> |

    Examples:
      | accountTransactionId | accountId | amount | transactionDate | description       | statusCode | responseMessage                                                                              |
      | 1                    | 1         | 1      | 2025-04-13      | valid_description | 400        | Account transaction id cannot be populated: 1                                                |
      |                      |           | 1      | 2025-04-13      | valid_description | 400        | post.accountTransactions[0].accountId::account id is mandatory and must be greater than zero |
      |                      | 1         |        | 2025-04-13      | valid_description | 400        | post.accountTransactions[0].amount::amount is mandatory and must be greater than zero        |
      |                      | 1         | 1      | 2025-04-13      |                   | 400        | post.accountTransactions[0].description::description is mandatory                            |
      |                      | 1         | 1      | 2025-04-13      | EMPTY_STRING      | 400        | post.accountTransactions[0].description::description is mandatory                            |
      |                      | 1         | 1      |                 | valid_description | 400        | post.accountTransactions[0].transactionDate::transactionDate is mandatory                    |

  Scenario Outline: returns correct errors when transaction date is invalid
    Given application is up
    And a transaction in the request body has an invalid input for the transaction date
      | <transactionDate> |
    When the create transactions endpoint is invoked
    Then the correct bad request response is returned from the create transactions endpoint
      | <statusCode> | <responseMessage> |
    And the transaction is not created

    Examples:
      | transactionDate | statusCode | responseMessage                                           |
      | "NOT_A_DATE"    | 400        | Text 'NOT_A_DATE' could not be parsed at index 0          |
      | "2025-13-25"    | 400        | Invalid value for MonthOfYear (valid values 1 - 12): 13   |
      | "2025-10-32"    | 400        | Invalid value for DayOfMonth (valid values 1 - 28/31): 32 |

  Scenario Outline: Create transactions endpoint is not able to connect to the database for get account by id
    Given application is up
    And valid transactions with the same account id are part of the request body for the create transaction endpoint
    And the connection to the database fails for the get account by ids
    When the create transactions endpoint is invoked
    Then the correct bad request response is returned from the create transactions endpoint
      | <statusCode> | <responseMessage> |
    And the transaction is not created

    Examples:
      | statusCode | responseMessage                                        |
      | 500        | Something went wrong while saving account transactions |


  Scenario Outline: Create transactions endpoint is not able to connect to the database for create transactions
    Given application is up
    And valid transactions with the same account id are part of the request body for the create transaction endpoint
    And account id exists in the database for the transactions
    And the connection to the database fails for the create transactions endpoint
    When the create transactions endpoint is invoked
    Then the correct bad request response is returned from the create transactions endpoint
      | <statusCode> | <responseMessage> |

    Examples:
      | statusCode | responseMessage                                        |
      | 500        | Something went wrong while saving account transactions |
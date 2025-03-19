Feature: Account Transaction: Update

  Scenario: Update Account Transaction endpoint returns correctly when valid account transaction is present
    Given application is up
    And the account transaction is part of the request body for the update account transaction endpoint
    And the account transaction id exists in the database
    And the account id exists in the database for the account transaction
    And the database connection succeeds for update account transaction
    When the update account transaction endpoint is invoked
    Then the updated account transaction is returned from the update account transaction endpoint

  Scenario Outline: Update Account Transaction endpoint returns correctly when account id does not exist
    Given application is up
    And the account transaction is part of the request body for the update account transaction endpoint
    And the account transaction id exists in the database
    And the account id does not exist in the database for the account transaction
    And the database connection succeeds for update account transaction
    When the update account transaction endpoint is invoked
    Then the correct failure response and message is returned from the update account transaction endpoint
      | <statusCode> | <responseMessage> |

    Examples:
      | statusCode | responseMessage                |
      | 400        | Account id does not exist: 147 |

  Scenario Outline: Update Account Transaction endpoint returns correctly when account transaction does not already exist in database
    Given application is up
    And the account transaction is part of the request body for the update account transaction endpoint
    And the account transaction id does not exist in the database
    When the update account transaction endpoint is invoked
    Then the correct failure response and message is returned from the update account transaction endpoint
      | <statusCode> | <responseMessage> |

    Examples:
      | statusCode | responseMessage                          |
      | 400        | Account transaction id does not exist: 1 |
#
  Scenario Outline: Update Account Transaction endpoint returns correct errors when required field is missing
    Given application is up
    And the account transaction in the update account transaction request body has an invalid input
      | <accountTransactionId> | <accountId> | <amount> | <description> |
    When the update account transaction endpoint is invoked
    Then the correct failure response and message is returned from the update account transaction endpoint
      | <statusCode> | <responseMessage> |

    Examples:
      | accountTransactionId | accountId | amount | description       | statusCode | responseMessage                                      |
      | -1                   | 147       | 123    | valid_description | 400        | Account transaction id must be positive and non-zero |
      | 0                    | 147       | 123    | valid_description | 400        | Account transaction id must be positive and non-zero |
      |                      | 147       | 123    | valid_description | 400        | Account transaction id must be positive and non-zero |
      | 1                    |           | 123    | valid_description | 400        | account id is mandatory                              |
      | 1                    | 147       |        | valid_description | 400        | amount is mandatory                                  |
      | 1                    | 147       | 123    |                   | 400        | description is mandatory                             |
      | 1                    | 147       | 123    | EMPTY_STRING      | 400        | description is mandatory                             |

  Scenario Outline: Update Account Transaction endpoint is not able to connect to the database
    Given application is up
    And the account transaction is part of the request body for the update account transaction endpoint
    And the connection to the database fails for the get account transaction by id within the account transaction endpoint
    When the update account transaction endpoint is invoked
    Then the correct failure response and message is returned from the update account transaction endpoint
      | <statusCode> | <responseMessage> |

    Examples:
      | statusCode | responseMessage                                           |
      | 500        | Something went wrong while saving the account transaction |

  Scenario Outline: Update Account Transaction endpoint is not able to connect to the database
    Given application is up
    And the account transaction is part of the request body for the update account transaction endpoint
    And the account transaction id exists in the database
    And the account id exists in the database for the account transaction
    And the connection to the database fails for the update account transaction endpoint
    When the update account transaction endpoint is invoked
    Then the correct failure response and message is returned from the update account transaction endpoint
      | <statusCode> | <responseMessage> |

    Examples:
      | statusCode | responseMessage                                           |
      | 500        | Something went wrong while saving the account transaction |
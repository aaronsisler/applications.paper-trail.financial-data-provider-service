Feature: Account Transaction: Delete By Id

  Scenario: Delete Account Transaction By Id endpoint returns correctly when account transaction exists and is deleted
    Given application is up
    And the account transaction id provided in the url is the correct format
    When the delete account transaction endpoint is invoked
    Then the correct response is returned from the delete account transaction endpoint
    And the correct account transaction is deleted

  Scenario: Delete Account Transaction By Id endpoint returns correctly when account transaction does not exist
    Given application is up
    And the account transaction id provided in the url is the correct format
    When the delete account transaction endpoint is invoked
    Then the correct response is returned from the delete account transaction endpoint
    And the correct account transaction is deleted

  Scenario Outline: Delete Account Transaction By Id endpoint is not able to parse the account transaction id in url
    Given application is up
    And the account transaction id provided in the url is the incorrect format
    When the delete account transaction endpoint is invoked
    Then the correct failure response is returned from the delete account transaction endpoint
      | <statusCode> | <responseMessage> |

    Examples:
      | statusCode | responseMessage                              |
      | 400        | Invalid parameter type: accountTransactionId |

  Scenario Outline: Delete Account Transaction By Id endpoint is not able to connect to the database
    Given application is up
    And the account transaction id provided in the url is the correct format
    And the connection to the database fails for the delete account transaction endpoint
    When the delete account transaction endpoint is invoked
    Then the correct failure response is returned from the delete account transaction endpoint
      | <statusCode> | <responseMessage> |

    Examples:
      | statusCode | responseMessage                                             |
      | 500        | Something went wrong while deleting the account transaction |
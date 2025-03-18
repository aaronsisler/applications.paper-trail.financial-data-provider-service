Feature: Account Transaction: Get By Id

  Scenario: Get Account Transaction By Id endpoint returns correctly when account transaction exists
    Given application is up
    And the account transaction id provided in the url is the correct format for the get account transaction by id endpoint
    And the requested account transaction exist in the database
    When the get account transaction by id endpoint is invoked
    Then the correct account transaction are returned from the get account transaction by id endpoint

  Scenario: Get Account Transaction By Id endpoint returns correctly when no account transaction exists
    Given application is up
    And the account transaction id provided in the url is the correct format for the get account transaction by id endpoint
    When the get account transaction by id endpoint is invoked
    Then the correct empty response is returned from the get account transaction by id endpoint

  Scenario Outline: Get Account Transaction By Id endpoint is not able to parse the account transaction id in url
    Given application is up
    And the account transaction id provided in the url is the incorrect format for the get account transaction by id endpoint
    When the get account transaction by id endpoint is invoked
    Then the correct failure response is returned from the get account transaction by id endpoint
      | <statusCode> | <responseMessage> |

    Examples:
      | statusCode | responseMessage                              |
      | 400        | Invalid parameter type: accountTransactionId |

  Scenario Outline: Get Account Transaction By Id endpoint is not able to connect to the database
    Given application is up
    And the account transaction id provided in the url is the correct format for the get account transaction by id endpoint
    And the connection to the database fails for the get account transaction by id
    When the get account transaction by id endpoint is invoked
    Then the correct failure response is returned from the get account transaction by id endpoint
      | <statusCode> | <responseMessage> |

    Examples:
      | statusCode | responseMessage                                             |
      | 500        | Something went wrong while fetching the account transaction |
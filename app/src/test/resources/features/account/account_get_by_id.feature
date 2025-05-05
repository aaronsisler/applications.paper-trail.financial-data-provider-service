Feature: Account: Get By Id

  Scenario: Get Account By Id endpoint returns correctly when account exists
    Given application is up
    And the account id provided in the url is the correct format for the get account by id endpoint
    And the requested account exist in the database
    When the get account by id endpoint is invoked
    Then the correct account are returned from the get account by id endpoint

  Scenario: Get Account By Id endpoint returns correctly when no account exists
    Given application is up
    And the account id provided in the url is the correct format for the get account by id endpoint
    When the get account by id endpoint is invoked
    Then the correct empty response is returned from the get account by id endpoint

  Scenario Outline: Get Account By Id endpoint is not able to parse the account id in url
    Given application is up
    And the account id provided in the url is the incorrect format for the get account by id endpoint
    When the get account by id endpoint is invoked
    Then the correct failure response is returned from the get account by id endpoint
      | <statusCode> | <responseMessage> |

    Examples:
      | statusCode | responseMessage                   |
      | 400        | Invalid parameter type: accountId |

  Scenario Outline: Get Account By Id endpoint is not able to connect to the database
    Given application is up
    And the account id provided in the url is the correct format for the get account by id endpoint
    And the connection to the database fails for the get account by id
    When the get account by id endpoint is invoked
    Then the correct failure response is returned from the get account by id endpoint
      | <statusCode> | <responseMessage> |

    Examples:
      | statusCode | responseMessage                                 |
      | 500        | Something went wrong while fetching the account |
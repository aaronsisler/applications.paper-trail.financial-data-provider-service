Feature: User: Get By Id

  Scenario: Get User By Id endpoint returns correctly when user exists
    Given application is up
    And the user id provided in the url is the correct format for the get user by id endpoint
    And the requested user exist in the database
    When the get user by id endpoint is invoked
    Then the correct user are returned from the get user by id endpoint

  Scenario: Get User By Id endpoint returns correctly when no user exists
    Given application is up
    And the user id provided in the url is the correct format for the get user by id endpoint
    And no user for the given id exists in the database
    When the get user by id endpoint is invoked
    Then the correct empty response is returned from the get user by id endpoint

  Scenario Outline: Get User By Id endpoint is not able to parse the user id in url
    Given application is up
    And the user id provided in the url is the incorrect format for the get user by id endpoint
    When the get user by id endpoint is invoked
    Then the correct failure response is returned from the get user by id endpoint
      | <statusCode> | <responseMessage> |

    Examples:
      | statusCode | responseMessage                |
      | 400        | Invalid parameter type: userId |

  Scenario Outline: Get User By Id endpoint is not able to connect to the database
    Given application is up
    And the user id provided in the url is the correct format for the get user by id endpoint
    And the connection to the database fails for the get user by id endpoint
    When the get user by id endpoint is invoked
    Then the correct failure response is returned from the get user by id endpoint
      | <statusCode> | <responseMessage> |

    Examples:
      | statusCode | responseMessage                              |
      | 500        | Something went wrong while fetching the user |
Feature: Household: Get By Id

  Scenario: Get Household By Id endpoint returns correctly when household exists
    Given application is up
    And the household id provided in the url is the correct format for the get household by id endpoint
    And the requested household exist in the database
    When the get household by id endpoint is invoked
    Then the correct household are returned from the get household by id endpoint

  Scenario: Get Household By Id endpoint returns correctly when no household exists
    Given application is up
    And the household id provided in the url is the correct format for the get household by id endpoint
    And no household for the given id exists in the database
    When the get household by id endpoint is invoked
    Then the correct empty response is returned from the get household by id endpoint

  Scenario Outline: Get Household By Id endpoint is not able to parse the household id in url
    Given application is up
    And the household id provided in the url is the incorrect format for the get household by id endpoint
    When the get household by id endpoint is invoked
    Then the correct failure response is returned from the get household by id endpoint
      | <statusCode> | <responseMessage> |

    Examples:
      | statusCode | responseMessage                     |
      | 400        | Invalid parameter type: householdId |

  Scenario Outline: Get Household By Id endpoint is not able to connect to the database
    Given application is up
    And the household id provided in the url is the correct format for the get household by id endpoint
    And the connection to the database fails for the get household by id endpoint
    When the get household by id endpoint is invoked
    Then the correct failure response is returned from the get household by id endpoint
      | <statusCode> | <responseMessage> |

    Examples:
      | statusCode | responseMessage                                   |
      | 500        | Something went wrong while fetching the household |
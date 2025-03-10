Feature: Household: Delete By Id

  Scenario: Delete By Id Household endpoint returns correctly when household exists and is deleted
    Given application is up
    And the household id provided exists in the database
    And the household id provided in the url is the correct format
    When the delete household endpoint is invoked
    Then the correct response is returned from the delete household endpoint
    And the correct household is deleted

  Scenario: Delete By Id Household endpoint returns correctly when household does not exist
    Given application is up
    And the household id provided in the url is the correct format
    And the household id provided does not exist in the database
    When the delete household endpoint is invoked
    Then the correct response is returned from the delete household endpoint
    And the correct household is deleted

  Scenario Outline: Delete By Id Household endpoint is not able to parse the household id in url
    Given application is up
    And the household id provided in the url is the incorrect format
    When the delete household endpoint is invoked
    Then the correct failure response is returned from the delete household endpoint
      | <statusCode> | <responseMessage> |

    Examples:
      | statusCode | responseMessage                     |
      | 400        | Invalid parameter type: householdId |

  Scenario Outline: Delete By Id Household endpoint is not able to connect to the database
    Given application is up
    And the household id provided in the url is the correct format
    And the connection to the database fails for the delete household endpoint
    When the delete household endpoint is invoked
    Then the correct failure response is returned from the delete household endpoint
      | <statusCode> | <responseMessage> |

    Examples:
      | statusCode | responseMessage                                   |
      | 500        | Something went wrong while deleting the household |
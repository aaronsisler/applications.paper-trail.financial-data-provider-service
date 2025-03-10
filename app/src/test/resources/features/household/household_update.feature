Feature: Household: Update

  Scenario: Update Household endpoint returns correctly when valid household is present
    Given application is up
    And the household is part of the request body for the update household endpoint
    When the update household endpoint is invoked
    Then the updated household is returned from the update household endpoint

  Scenario: Update Household endpoint returns correctly when household does not already exist in database
    Given application is up
    And the household is part of the request body for the update household endpoint
    And the household id does not exist in the database
    When the update household endpoint is invoked
    Then the correct bad request response is returned from the update household endpoint

  Scenario Outline: Update Household endpoint returns correct errors when required field is missing
    Given application is up
    And the household in the update household request body has an invalid input
      | <householdId> | <name> |
    When the update household endpoint is invoked
    Then the correct failure response and message is returned from the update household endpoint
      | <responseMessage> |

    Examples:
      | householdId | name         | responseMessage                            |
      | -1          | valid_name   | Household Id must be positive and non-zero |
      | 0           | valid_name   | Household Id must be positive and non-zero |
      |             | valid_name   | Household Id must be positive and non-zero |
      | 1           |              | name is mandatory                          |
      | 1           | EMPTY_STRING | name is mandatory                          |

  Scenario: Update Household endpoint is not able to connect to the database
    Given application is up
    And the household is part of the request body for the update household endpoint
    And the connection to the database fails for the update household endpoint
    When the update household endpoint is invoked
    Then the correct failure response is returned from the update household endpoint
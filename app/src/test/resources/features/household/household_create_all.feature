Feature: Household: Create All

  Scenario: Create All Households endpoint returns correctly when valid households present
    Given application is up
    And two valid households are part of the request body for the create all households endpoint
    When the create all households endpoint is invoked
    Then the newly created households are returned from the create all households endpoint

  Scenario: Create All Households endpoint returns correctly when no households present
    Given application is up
    And no households are part of the request body
    When the create all households endpoint is invoked
    Then the correct bad request response is returned from the create all households endpoint

  Scenario Outline: Create All Households endpoint returns correct errors when required field is missing
    Given application is up
    And the household in the request body has an invalid input
      | <householdId> | <name> |
    When the create all households endpoint is invoked
    Then the correct failure response and message is returned from the create all households endpoint
      | <responseMessage> |

    Examples:
      | householdId | name         | responseMessage                            |
      | 1           | valid_name   | Household Id cannot be populated: 1        |
      |             |              | post.households[0].name::name is mandatory |
      |             | EMPTY_STRING | post.households[0].name::name is mandatory |

  Scenario: Create All Households endpoint is not able to connect to the database
    Given application is up
    And two valid households are part of the request body for the create all households endpoint
    And the connection to the database fails for the create all households endpoint
    When the create all households endpoint is invoked
    Then the correct failure response is returned from the create all households endpoint
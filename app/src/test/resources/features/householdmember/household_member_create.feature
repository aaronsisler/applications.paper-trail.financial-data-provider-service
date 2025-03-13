Feature: Household Member: Create

  Scenario: Create household member endpoint returns correctly when valid household member present
    Given application is up
    And a valid household member is part of the request body for the create household member endpoint
    And user id exists in the database for the household member
    And household id exists in the database for the household member
    And the database connection succeeds for create household member
    When the create household member endpoint is invoked
    Then the newly created household member is returned from the create household member endpoint

  Scenario Outline: Create household member endpoint returns correct error when user id does not exist
    Given application is up
    And a valid household member is part of the request body for the create household member endpoint
    And user id does not exist in the household member
    When the create household member endpoint is invoked
    Then the correct bad request response is returned from the create household member endpoint
      | <statusCode> | <responseMessage> |
#
    Examples:
      | statusCode | responseMessage             |
      | 400        | User Id does not exist: 456 |

  Scenario Outline: Create household member endpoint returns correct error when household id does not exist
    Given application is up
    And a valid household member is part of the request body for the create household member endpoint
    And user id exists in the database for the household member
    And household id does not exist in the household member
    When the create household member endpoint is invoked
    Then the correct bad request response is returned from the create household member endpoint
      | <statusCode> | <responseMessage> |

    Examples:
      | statusCode | responseMessage                  |
      | 400        | Household Id does not exist: 123 |

  Scenario Outline: Create household member endpoint returns correct error when relational issues occur
    Given application is up
    And a valid household member is part of the request body for the create household member endpoint
    And user id exists in the database for the household member
    And household id exists in the database for the household member
    And the database save fails given a user or household was deleted during the create household member database call
    When the create household member endpoint is invoked
    Then the correct bad request response is returned from the create household member endpoint
      | <statusCode> | <responseMessage> |

    Examples:
      | statusCode | responseMessage                            |
      | 400        | A provided field is relationally incorrect |

  Scenario Outline: Create household member endpoint returns correct errors when required field is missing
    Given application is up
    And the household member in the request body has an invalid input
      | <householdMemberId> | <householdId> | <userId> |
    When the create household member endpoint is invoked
    Then the correct bad request response is returned from the create household member endpoint
      | <statusCode> | <responseMessage> |
#
    Examples:
      | householdMemberId | householdId | userId | statusCode | responseMessage                            |
      | 1                 | 1           | 1      | 400        | Household Member Id cannot be populated: 1 |
      |                   |             | 1      | 400        | household id is mandatory                  |
      |                   | 1           |        | 400        | user id is mandatory                       |

  Scenario Outline: Create Household member endpoint is not able to connect to the database for get user by id
    Given application is up
    And a valid household member is part of the request body for the create household member endpoint
    And the connection to the database fails for the get user by id
    When the create household member endpoint is invoked
    Then the correct bad request response is returned from the create household member endpoint
      | <statusCode> | <responseMessage> |
    And the household member is not created

    Examples:
      | statusCode | responseMessage                                    |
      | 500        | Something went wrong while saving household member |

  Scenario Outline: Create Household member endpoint is not able to connect to the database for get household by id
    Given application is up
    And a valid household member is part of the request body for the create household member endpoint
    And user id exists in the database for the household member
    And the connection to the database fails for the get household by id
    When the create household member endpoint is invoked
    Then the correct bad request response is returned from the create household member endpoint
      | <statusCode> | <responseMessage> |
    And the household member is not created

    Examples:
      | statusCode | responseMessage                                    |
      | 500        | Something went wrong while saving household member |

  Scenario Outline: Create Household member endpoint is not able to connect to the database for create household member
    Given application is up
    And a valid household member is part of the request body for the create household member endpoint
    And user id exists in the database for the household member
    And household id exists in the database for the household member
    And the connection to the database fails for the create household member endpoint
    When the create household member endpoint is invoked
    Then the correct bad request response is returned from the create household member endpoint
      | <statusCode> | <responseMessage> |

    Examples:
      | statusCode | responseMessage                                    |
      | 500        | Something went wrong while saving household member |
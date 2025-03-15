Feature: Institution: Update

  Scenario: Update Institution endpoint returns correctly when valid institution is present
    Given application is up
    And the institution is part of the request body for the update institution endpoint
    When the update institution endpoint is invoked
    Then the updated institution is returned from the update institution endpoint

  Scenario: Update Institution endpoint returns correctly when institution does not already exist in database
    Given application is up
    And the institution is part of the request body for the update institution endpoint
    And the institution id does not exist in the database
    When the update institution endpoint is invoked
    Then the correct bad request response is returned from the update institution endpoint

  Scenario Outline: Update Institution endpoint returns correct errors when required field is missing
    Given application is up
    And the institution in the update institution request body has an invalid input
      | <institutionId> | <name> |
    When the update institution endpoint is invoked
    Then the correct failure response and message is returned from the update institution endpoint
      | <responseMessage> |

    Examples:
      | institutionId | name         | responseMessage                              |
      | -1            | valid_name   | Institution Id must be positive and non-zero |
      | 0             | valid_name   | Institution Id must be positive and non-zero |
      |               | valid_name   | Institution Id must be positive and non-zero |
      | 1             |              | name is mandatory                            |
      | 1             | EMPTY_STRING | name is mandatory                            |

  Scenario: Update Institution endpoint is not able to connect to the database
    Given application is up
    And the institution is part of the request body for the update institution endpoint
    And the connection to the database fails for the update institution endpoint
    When the update institution endpoint is invoked
    Then the correct failure response is returned from the update institution endpoint
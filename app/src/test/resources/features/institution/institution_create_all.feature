Feature: Institution: Create All

  Scenario: Create All Institutions endpoint returns correctly when valid institutions present
    Given application is up
    And two valid institutions are part of the request body for the create all institutions endpoint
    When the create all institutions endpoint is invoked
    Then the newly created institutions are returned from the create all institutions endpoint

  Scenario: Create All Institutions endpoint returns correctly when no institutions present
    Given application is up
    And no institutions are part of the request body
    When the create all institutions endpoint is invoked
    Then the correct bad request response is returned from the create all institutions endpoint

  Scenario Outline: Create All Institutions endpoint returns correct errors when required field is missing
    Given application is up
    And the institution in the request body has an invalid input
      | <institutionId> | <name> |
    When the create all institutions endpoint is invoked
    Then the correct failure response and message is returned from the create all institutions endpoint
      | <responseMessage> |

    Examples:
      | institutionId | name         | responseMessage                              |
      | 1             | valid_name   | Institution Id cannot be populated: 1        |
      |               |              | post.institutions[0].name::name is mandatory |
      |               | EMPTY_STRING | post.institutions[0].name::name is mandatory |

  Scenario: Create All Institutions endpoint is not able to connect to the database
    Given application is up
    And two valid institutions are part of the request body for the create all institutions endpoint
    And the connection to the database fails for the create all institutions endpoint
    When the create all institutions endpoint is invoked
    Then the correct failure response is returned from the create all institutions endpoint
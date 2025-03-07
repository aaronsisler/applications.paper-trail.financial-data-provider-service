Feature: User: Create All

  Scenario: Create All Users endpoint returns correctly when valid users present
    Given application is up
    And two valid users are part of the request body for the create all users endpoint
    When the create all users endpoint is invoked
    Then the newly created users are returned from the create all users endpoint

  Scenario: Create All Users endpoint returns correctly when no users present
    Given application is up
    And no users are part of the request body
    When the create all users endpoint is invoked
    Then the correct bad request response is returned from the create all users endpoint

  Scenario Outline: Create All Users endpoint returns correct errors when required field is missing
    Given application is up
    And the user in the request body has an invalid input
      | <userId> | <username> | <firstName> | <lastName> |
    When the create all users endpoint is invoked
    Then the correct failure response and message is returned from the create all users endpoint
      | <responseMessage> |

    Examples:
      | userId | username       | firstName        | lastName         | responseMessage                                 |
      | 1      | valid_username | valid_first_name | valid_first_name | User Id cannot be populated: 1                  |
      |        |                | valid_first_name | valid_first_name | post.users[0].username::username is mandatory   |
      |        | EMPTY_STRING   | valid_first_name | valid_first_name | post.users[0].username::username is mandatory   |
      |        | valid_username |                  | valid_first_name | post.users[0].firstName::firstName is mandatory |
      |        | valid_username | EMPTY_STRING     | valid_first_name | post.users[0].firstName::firstName is mandatory |
      |        | valid_username | valid_first_name |                  | post.users[0].lastName::lastName is mandatory   |
      |        | valid_username | valid_first_name | EMPTY_STRING     | post.users[0].lastName::lastName is mandatory   |

  Scenario: Create All Users endpoint is not able to connect to the database
    Given application is up
    And two valid users are part of the request body for the create all users endpoint
    And the connection to the database fails for the create all users endpoint
    When the create all users endpoint is invoked
    Then the correct failure response is returned from the create all users endpoint
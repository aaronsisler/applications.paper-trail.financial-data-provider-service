Feature: User: Update

#  Scenario: Update User endpoint returns correctly when valid user and path id is present
#    Given application is up
#    And the user is part of the request body for the update user endpoint
#    When the update user endpoint is invoked
#    Then the updated user is returned from the update user endpoint

#  Scenario: Update User endpoint returns correctly when valid user and invalid path id is not present
#    Given application is up
#    And the user is part of the request body for the update user endpoint
#    When the update user endpoint is invoked
#    Then the correct failure response is returned from the update user endpoint

  Scenario: Update User endpoint returns correctly when user does not already exist in database
    Given application is up
    And the user is part of the request body for the update user endpoint
    And the user id does not exist in the database
    When the update user endpoint is invoked
    Then the correct bad request response is returned from the update user endpoint

#  Scenario Outline: Update User endpoint returns correct errors when required field is missing
#    Given application is up
#    And the user in the request body has an invalid input
#      | <userId> | <username> | <firstName> | <lastName> |
#    When the update user endpoint is invoked
#    Then the correct failure response and message is returned from the update user endpoint
#      | <responseMessage> |

#    Examples:
#      | userId | username       | firstName        | lastName         | responseMessage                                 |
#      | -1      | valid_username | valid_first_name | valid_first_name | User Id cannot be populated: 1                  |
#      | 0      | valid_username | valid_first_name | valid_first_name | User Id cannot be populated: 1                  |
#      |        |                | valid_first_name | valid_first_name | post.users[0].username::username is mandatory   |
#      |        | EMPTY_STRING   | valid_first_name | valid_first_name | post.users[0].username::username is mandatory   |
#      |        | valid_username |                  | valid_first_name | post.users[0].firstName::firstName is mandatory |
#      |        | valid_username | EMPTY_STRING     | valid_first_name | post.users[0].firstName::firstName is mandatory |
#      |        | valid_username | valid_first_name |                  | post.users[0].lastName::lastName is mandatory   |
#      |        | valid_username | valid_first_name | EMPTY_STRING     | post.users[0].lastName::lastName is mandatory   |

  Scenario: Update User endpoint is not able to connect to the database
    Given application is up
    And the user is part of the request body for the update user endpoint
    And the connection to the database fails for the update user endpoint
    When the update user endpoint is invoked
    Then the correct failure response is returned from the update user endpoint
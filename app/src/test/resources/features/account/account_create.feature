Feature: Account: Create

#  Scenario: Create account endpoint returns correctly when valid account present
#    Given application is up
#    And a valid account is part of the request body for the create account endpoint
#    And institution id exists in the database for the account
#    And household member id exists in the database for the account
#    And the database connection succeeds for create account
#    When the create account endpoint is invoked
#    Then the newly created account is returned from the create account endpoint
#
#  Scenario Outline: Create account endpoint returns correct error when institution id does not exist
#    Given application is up
#    And a valid account is part of the request body for the create account endpoint
#    And institution id does not exist in the account
#    When the create account endpoint is invoked
#    Then the correct bad request response is returned from the create account endpoint
#      | <statusCode> | <responseMessage> |

#    Examples:
#      | statusCode | responseMessage             |
#      | 400        | User Id does not exist: 456 |
#
#  Scenario Outline: Create account endpoint returns correct error when household member id does not exist
#    Given application is up
#    And a valid account is part of the request body for the create account endpoint
#    And institution id exists in the database for the account
#    And household member id does not exist in the account
#    When the create account endpoint is invoked
#    Then the correct bad request response is returned from the create account endpoint
#      | <statusCode> | <responseMessage> |
#
#    Examples:
#      | statusCode | responseMessage                  |
#      | 400        | Household Id does not exist: 123 |
#
#  Scenario Outline: Create account endpoint returns correct error when relational issues occur
#    Given application is up
#    And a valid account is part of the request body for the create account endpoint
#    And user id exists in the database for the account
#    And household id exists in the database for the account
#    And the database save fails given a user or household was deleted during the create account database call
#    When the create account endpoint is invoked
#    Then the correct bad request response is returned from the create account endpoint
#      | <statusCode> | <responseMessage> |
#
#    Examples:
#      | statusCode | responseMessage                            |
#      | 400        | A provided field is relationally incorrect |
#
#  Scenario Outline: Create account endpoint returns correct errors when required field is missing
#    Given application is up
#    And the account in the request body has an invalid input
#      | <householdMemberId> | <householdId> | <userId> |
#    When the create account endpoint is invoked
#    Then the correct bad request response is returned from the create account endpoint
#      | <statusCode> | <responseMessage> |

#    Examples:
#      | householdMemberId | householdId | userId | statusCode | responseMessage                            |
#      | 1                 | 1           | 1      | 400        | Household Member Id cannot be populated: 1 |
#      |                   |             | 1      | 400        | household id is mandatory                  |
#      |                   | 1           |        | 400        | user id is mandatory                       |
#
#  Scenario Outline: Create Household member endpoint is not able to connect to the database for get user by id
#    Given application is up
#    And a valid account is part of the request body for the create account endpoint
#    And the connection to the database fails for the get user by id
#    When the create account endpoint is invoked
#    Then the correct bad request response is returned from the create account endpoint
#      | <statusCode> | <responseMessage> |
#    And the account is not created
#
#    Examples:
#      | statusCode | responseMessage                           |
#      | 500        | Something went wrong while saving account |
#
#  Scenario Outline: Create Household member endpoint is not able to connect to the database for get household by id
#    Given application is up
#    And a valid account is part of the request body for the create account endpoint
#    And user id exists in the database for the account
#    And the connection to the database fails for the get household by id
#    When the create account endpoint is invoked
#    Then the correct bad request response is returned from the create account endpoint
#      | <statusCode> | <responseMessage> |
#    And the account is not created
#
#    Examples:
#      | statusCode | responseMessage                           |
#      | 500        | Something went wrong while saving account |
#
#  Scenario Outline: Create Household member endpoint is not able to connect to the database for create account
#    Given application is up
#    And a valid account is part of the request body for the create account endpoint
#    And user id exists in the database for the account
#    And household id exists in the database for the account
#    And the connection to the database fails for the create account endpoint
#    When the create account endpoint is invoked
#    Then the correct bad request response is returned from the create account endpoint
#      | <statusCode> | <responseMessage> |
#
#    Examples:
#      | statusCode | responseMessage                           |
#      | 500        | Something went wrong while saving account |
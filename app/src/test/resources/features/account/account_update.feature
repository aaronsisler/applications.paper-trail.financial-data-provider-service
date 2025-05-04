Feature: Account: Update

#  All is good
  Scenario: Update Account endpoint returns correctly when valid account is present
    Given application is up
    And an account is part of the request body for the update account endpoint
    And a record with a matching account id resides in the database
    And the database connection succeeds for update account
    When the update account endpoint is invoked
    Then the updated account is returned from the update account endpoint

#  Account Id is less than zero
  Scenario Outline: Update Account endpoint returns correctly when invalid account is present
    Given application is up
    And the account in the update account request body has an invalid input
      | <accountId> | <householdMemberId> | <institutionId> | <name> | <nickname> |
    When the update account endpoint is invoked
    Then the correct failure response and message is returned from the update account endpoint
      | <statusCode> | <responseMessage> |

    Examples:
      | accountId | householdMemberId | institutionId | name       | nickname       | statusCode | responseMessage                          |
      | -1        | 456               | 789           | Valid Name | Valid Nickname | 400        | Account id must be positive and non-zero |
      | 0         | 456               | 789           | Valid Name | Valid Nickname | 400        | Account id must be positive and non-zero |
      |           | 456               | 789           | Valid Name | Valid Nickname | 400        | Account id must be positive and non-zero |


#  Account Id is zero
#  Account id is blank/empty
#  Account is missing fields
#  Account cannot be found
#  Connection to get account throws error
#  Account's household member ids don't match
#    And the account's household member id does not match the record's household member id
#  Account's institution id don't match
#    And the account's institution id does not match the record's institution id
#  Connection to save account throws error
#  Relational issue pops up when save account is triggered
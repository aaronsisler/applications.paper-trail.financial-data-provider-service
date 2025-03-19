#Feature: Integration: Account Transaction
#
#  Scenario: Create and Get All Account Transactions
#    Given application is up
#    And a user exists in the database
#    And a household exists in the database
#    And a household member for the user and household exists in the database
#    And an institution exists in the database
#    And two accounts with different account ids for the institution and household member exists in the database
#    And a valid account transaction with the first account id is part of the request body for the create account transactions endpoint
#    When the create account transactions endpoint is invoked
#    Then the newly created account transaction with the first account id is returned from the create account transactions endpoint
#    And a valid account transaction with the second account id is part of the request body for the create account transactions endpoint
#    When the create account transactions endpoint is invoked
#    Then the newly created account transaction with the second account id is returned from the create account transactions endpoint
#    When the get all account transactions endpoint is invoked
#    Then the correct account transactions are returned from the get all account transactions endpoint
#    When the get all account transactions endpoint is invoked with the first account id
#    Then the correct account transactions are returned from the get all account transactions endpoint invoked with the first account id
#
#  Scenario: Get By Id and Update Account Transaction
#    Given application is up
#    And the update account transaction id provided exists in the database
#    And an update for the account transaction is valid and part of the request body for the update account transaction endpoint
#    When the update account transaction endpoint is invoked
#    Then the updated account transaction is returned from the update account transaction endpoint
#    And the updated account transaction is correct in the database
#
#  Scenario: Delete Account Transaction
#    Given application is up
#    And the delete account transaction id provided exists in the database
#    When the delete account transaction endpoint is invoked
#    Then the correct response is returned from the delete account transaction endpoint
#    And the correct account transaction is deleted

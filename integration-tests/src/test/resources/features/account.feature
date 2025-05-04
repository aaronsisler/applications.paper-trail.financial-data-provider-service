Feature: Integration: Account

  Scenario: Create and Get All Accounts
    Given application is up
    And a user exists in the database related to account creation
    And a household exist in the database related to account creation
    And an institution exist in the database related to account creation
    And two household members exist in the database related to account creation
    And a valid account with the institution and the first household member id is part of the request body for the create account endpoint
    When the create account endpoint is invoked
    Then the newly created account with the institution and the first household member is returned from the create account endpoint
    And a valid account with the institution and the second household member id is part of the request body for the create account endpoint
    When the create account endpoint is invoked
    Then the newly created account with the institution and the second household member is returned from the create account endpoint
    When the get all accounts endpoint is invoked
    Then the correct accounts are returned from the get all accounts endpoint
    When the get all accounts endpoint is invoked with the first household member id
    Then the correct accounts are returned from the get all accounts endpoint invoked with the first household member id

#  Scenario: Update Account and Get Updated Account By Id
#    Given application is up
#    And an account exists in the database
#    And the update account id provided exists in the database
#    And an update for the account is valid and part of the request body for the update account endpoint
#    When the update account endpoint is invoked
#    Then the updated account is returned from the update account endpoint
#    And the updated account is correct in the database
#
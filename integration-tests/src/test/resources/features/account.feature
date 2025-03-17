Feature: Integration: Household Member

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
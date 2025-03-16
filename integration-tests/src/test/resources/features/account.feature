Feature: Integration: Household Member

  Scenario: Create and Get All Accounts
    Given application is up
    And a user exists in the database related to account creation
    And a household exist in the database related to account creation
    And an institution exist in the database related to account creation
    And a household member exist in the database related to account creation
    And a valid account with the institution and household member is part of the request body for the create account endpoint
    When the create account endpoint is invoked
    Then the newly created account with the institution and household member is returned from the create account endpoint
#    And a valid household member with the second user id is part of the request body for the create household member endpoint
#    When the create household member endpoint is invoked
#    Then the newly created household member with the second user id is returned from the create household member endpoint
#    When the get all household members endpoint is invoked
#    Then the correct household members are returned from the get all household members endpoint
#    When the get all household members endpoint is invoked with the first user id
#    Then the correct household members are returned from the get all household members endpoint invoked with the first user id

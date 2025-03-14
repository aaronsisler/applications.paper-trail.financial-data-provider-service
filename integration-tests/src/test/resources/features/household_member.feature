Feature: Integration: Household Member

  Scenario: Create and Get All Household Members
    Given application is up
    And two users with different user ids exist in the database
    And a household exist in the database
    And a valid household member with the first user id is part of the request body for the create household member endpoint
    When the create household member endpoint is invoked
    Then the newly created household member with the first user id is returned from the create household member endpoint
    And a valid household member with the second user id is part of the request body for the create household member endpoint
    When the create household member endpoint is invoked
    Then the newly created household member with the second user id is returned from the create household member endpoint
    When the get all household members endpoint is invoked
    Then the correct household members are returned from the get all household members endpoint

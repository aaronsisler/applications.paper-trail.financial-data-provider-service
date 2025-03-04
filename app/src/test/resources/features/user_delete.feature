Feature: User: Get All

  Scenario: Delete User endpoint returns correctly when user exists and is deleted
    Given application is up
    And the user id provided exists in the database
    When the delete user endpoint is invoked
    Then the correct response is returned from the delete user endpoint
    And the correct user is deleted

  Scenario: Delete User endpoint returns correctly when user does not exist
    Given application is up
    And the user id provided does not exist in the database
    When the delete user endpoint is invoked
    Then the correct response is returned from the delete user endpoint
    And the correct user is deleted

  Scenario: Delete User endpoint is not able to connect to the database
    Given application is up
    And the connection to the database fails for the delete user endpoint
    When the delete user endpoint is invoked
    Then the correct failure response is returned from the delete user endpoint
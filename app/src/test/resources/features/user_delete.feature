Feature: User: Get All

  Scenario: Delete User endpoint returns correctly when is deleted
    Given application is up
    And user exist in the database
    When the delete user endpoint is invoked
    Then the correct user is deleted
    And the correct response is returned from the delete user endpoint

  Scenario: Delete User endpoint is not able to connect to the database
    Given application is up
    And the connection to the database fails for the delete user endpoint
    When the delete user endpoint is invoked
    Then the correct failure response is returned from the delete user endpoint
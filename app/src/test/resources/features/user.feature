Feature: User

  Scenario: Users endpoint returns correctly when users present
    Given application is up
    And two users exist in the database
    When the get all users endpoint is invoked
    Then the correct users are returned

#  @MockIntegration
  Scenario: Users endpoint returns correctly when no users present
    Given application is up
    And no users exist
    When the get all users endpoint is invoked negative
    Then the correct empty users response is returned

#  @MockIntegration
  Scenario: Users endpoint is not able to connect to the database
    Given application is up
    And the connection to the database fails
    When the get all users endpoint is invoked negative
    Then the correct failure response is returned

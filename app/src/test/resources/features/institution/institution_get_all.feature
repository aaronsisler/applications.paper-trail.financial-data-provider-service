Feature: Institution: Get All

  Scenario: Get All Institutions endpoint returns correctly when institutions present
    Given application is up
    And two institutions exist in the database
    When the get all institutions endpoint is invoked
    Then the correct institutions are returned

  Scenario: Get All Institutions endpoint returns correctly when no institutions present
    Given application is up
    And no institutions exist
    When the get all institutions endpoint is invoked
    Then the correct empty institutions response is returned
#
  Scenario: Get All Institutions endpoint is not able to connect to the database
    Given application is up
    And the connection to the database fails for the get all institutions endpoint
    When the get all institutions endpoint is invoked
    Then the correct failure response is returned from the get all institutions endpoint
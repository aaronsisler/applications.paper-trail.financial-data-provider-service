Feature: Household: Get All

  Scenario: Get All Households endpoint returns correctly when households present
    Given application is up
    And two households exist in the database
    When the get all households endpoint is invoked
    Then the correct households are returned

  Scenario: Get All Households endpoint returns correctly when no households present
    Given application is up
    And no households exist
    When the get all households endpoint is invoked
    Then the correct empty households response is returned

  Scenario: Get All Households endpoint is not able to connect to the database
    Given application is up
    And the connection to the database fails for the get all households endpoint
    When the get all households endpoint is invoked
    Then the correct failure response is returned from the get all households endpoint
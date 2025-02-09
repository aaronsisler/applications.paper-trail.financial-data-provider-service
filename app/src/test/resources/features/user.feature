Feature: User

  Scenario: Users endpoint returns correctly when no users present
    Given application is up
    And no users exist
    When the get all users endpoint is invoked
    Then the correct empty users response is returned

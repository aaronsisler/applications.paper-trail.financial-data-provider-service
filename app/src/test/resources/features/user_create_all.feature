Feature: User: Create All

  Scenario: Create All Users endpoint returns correctly when valid users present
    Given application is up
    And two valid users are part of the request body for the create all users endpoint
    When the create all users endpoint is invoked
    Then the newly created users are returned from the create all users endpoint

  Scenario: Create All Users endpoint returns correctly when no users present
    Given application is up
    And no users are part of the request body
    When the create all users endpoint is invoked
    Then the correct bad request response is returned from the create all users endpoint

#  Scenario: Create All Users endpoint returns correct errors when required field is missing
#    Given application is up
#    And an invalid user is part of the request body
#    When the create all users endpoint is invoked
#    Then the correct empty users response is returned

  Scenario: Create All Users endpoint is not able to connect to the database
    Given application is up
    And two valid users are part of the request body for the create all users endpoint
    And the connection to the database fails for the create all users endpoint
    When the create all users endpoint is invoked
    Then the correct failure response is returned from the create all users endpoint
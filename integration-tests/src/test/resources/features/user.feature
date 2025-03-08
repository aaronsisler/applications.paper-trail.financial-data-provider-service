Feature: Integration: User

  Scenario: Create and Get All Users
    Given application is up
    And two valid users are part of the request body for the create all users endpoint
    When the create all users endpoint is invoked
    Then the newly created users are returned from the create all users endpoint
    When the get all users endpoint is invoked
    Then the correct users are returned

#  Scenario: Get By Id and Update User
#    Given application is up
#    And two valid users are part of the request body for the create all users endpoint
#    When the create all users endpoint is invoked
#    Then the newly created users are returned from the create all users endpoint
#    When the get all users endpoint is invoked
#    Then the correct users are returned
#    When the delete user endpoint is invoked
#    Then the correct response is returned from the delete user endpoint
#    And the correct user is deleted

  Scenario: Delete User
    Given application is up
    And the user id provided exists in the database
    When the delete user endpoint is invoked
    Then the correct response is returned from the delete user endpoint
    And the correct user is deleted

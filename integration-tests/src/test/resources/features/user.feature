Feature: Integration: User

  Scenario: Create and Get All Users
    Given application is up
    And two valid users are part of the request body for the create all users endpoint
    When the create all users endpoint is invoked
    Then the newly created users are returned from the create all users endpoint
    When the get all users endpoint is invoked
    Then the correct users are returned from the get all users endpoint

  Scenario Outline: Get By Id and Update User
    Given application is up
    And the user id provided exists in the database
      | <username> | <firstName> | <lastName> |
    And an update for the user is valid and part of the request body for the update user endpoint
    When the update user endpoint is invoked
    Then the updated user is returned from the update user endpoint
    And the updated user is correct in the database

    Examples:
      | username                           | firstName        | lastName         |
      | update_user_and_check_user_updated | valid_first_name | valid_first_name |

  Scenario Outline: Delete User
    Given application is up
    And the user id provided exists in the database
      | <username> | <firstName> | <lastName> |
    When the delete user endpoint is invoked
    Then the correct response is returned from the delete user endpoint
    And the correct user is deleted
    Examples:
      | username                       | firstName        | lastName         |
      | get_user_by_id_and_delete_user | valid_first_name | valid_first_name |

Feature: Integration: Household

  Scenario: Create and Get All Households
    Given application is up
    And two valid households are part of the request body for the create all households endpoint
    When the create all households endpoint is invoked
    Then the newly created households are returned from the create all households endpoint
    When the get all households endpoint is invoked
    Then the correct households are returned from the get all households endpoint

  Scenario Outline: Get By Id and Update Household
    Given application is up
    And the household id provided exists in the database
      | <name> |
    And an update for the household is valid and part of the request body for the update household endpoint
    When the update household endpoint is invoked
    Then the updated household is returned from the update household endpoint
    And the updated household is correct in the database

    Examples:
      | name                                         |
      | update_household_and_check_household_updated |

  Scenario Outline: Delete Household
    Given application is up
    And the household id provided exists in the database
      | <name> |
    When the delete household endpoint is invoked
    Then the correct response is returned from the delete household endpoint
    And the correct household is deleted
    Examples:
      | name                                     |
      | get_household_by_id_and_delete_household |

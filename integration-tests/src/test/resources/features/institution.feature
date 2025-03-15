Feature: Integration: Institution

  Scenario: Create and Get All Institutions
    Given application is up
    And two valid institutions are part of the request body for the create all institutions endpoint
    When the create all institutions endpoint is invoked
    Then the newly created institutions are returned from the create all institutions endpoint
    When the get all institutions endpoint is invoked
    Then the correct institutions are returned from the get all institutions endpoint

  Scenario: Get By Id and Update Institution
    Given application is up
    And the institution id provided exists in the database
    And an update for the institution is valid and part of the request body for the update institution endpoint
    When the update institution endpoint is invoked
    Then the updated institution is returned from the update institution endpoint
    And the updated institution is correct in the database

#
#  Scenario Outline: Delete Institution
#    Given application is up
#    And the institution id provided exists in the database
#      | <name> |
#    When the delete institution endpoint is invoked
#    Then the correct response is returned from the delete institution endpoint
#    And the correct institution is deleted
#    Examples:
#      | name                                         |
#      | get_institution_by_id_and_delete_institution |

Feature: Institution: Delete By Id

  Scenario: Delete By Id Institution endpoint returns correctly when institution exists and is deleted
    Given application is up
    And the institution id provided exists in the database
    And the institution id provided in the url is the correct format
    When the delete institution endpoint is invoked
    Then the correct response is returned from the delete institution endpoint
    And the correct institution is deleted

  Scenario: Delete By Id Institution endpoint returns correctly when institution does not exist
    Given application is up
    And the institution id provided in the url is the correct format
    And the institution id provided does not exist in the database
    When the delete institution endpoint is invoked
    Then the correct response is returned from the delete institution endpoint
    And the correct institution is deleted

  Scenario Outline: Delete By Id Institution endpoint is not able to parse the institution id in url
    Given application is up
    And the institution id provided in the url is the incorrect format
    When the delete institution endpoint is invoked
    Then the correct failure response is returned from the delete institution endpoint
      | <statusCode> | <responseMessage> |

    Examples:
      | statusCode | responseMessage                       |
      | 400        | Invalid parameter type: institutionId |

  Scenario Outline: Delete By Id Institution endpoint is not able to connect to the database
    Given application is up
    And the institution id provided in the url is the correct format
    And the connection to the database fails for the delete institution endpoint
    When the delete institution endpoint is invoked
    Then the correct failure response is returned from the delete institution endpoint
      | <statusCode> | <responseMessage> |

    Examples:
      | statusCode | responseMessage                                     |
      | 500        | Something went wrong while deleting the institution |
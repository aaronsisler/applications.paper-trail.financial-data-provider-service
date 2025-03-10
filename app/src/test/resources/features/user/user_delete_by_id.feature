Feature: User: Delete By Id

  Scenario: Delete By Id User endpoint returns correctly when user exists and is deleted
    Given application is up
    And the user id provided exists in the database
    And the user id provided in the url is the correct format
    When the delete user endpoint is invoked
    Then the correct response is returned from the delete user endpoint
    And the correct user is deleted

  Scenario: Delete By Id User endpoint returns correctly when user does not exist
    Given application is up
    And the user id provided in the url is the correct format
    And the user id provided does not exist in the database
    When the delete user endpoint is invoked
    Then the correct response is returned from the delete user endpoint
    And the correct user is deleted

  Scenario Outline: Delete By Id User endpoint is not able to parse the user id in url
    Given application is up
    And the user id provided in the url is the incorrect format
    When the delete user endpoint is invoked
    Then the correct failure response is returned from the delete user endpoint
      | <statusCode> | <responseMessage> |

    Examples:
      | statusCode | responseMessage                |
      | 400        | Invalid parameter type: userId |

  Scenario Outline: Delete By Id User endpoint is not able to connect to the database
    Given application is up
    And the user id provided in the url is the correct format
    And the connection to the database fails for the delete user endpoint
    When the delete user endpoint is invoked
    Then the correct failure response is returned from the delete user endpoint
      | <statusCode> | <responseMessage> |

    Examples:
      | statusCode | responseMessage                              |
      | 500        | Something went wrong while deleting the user |
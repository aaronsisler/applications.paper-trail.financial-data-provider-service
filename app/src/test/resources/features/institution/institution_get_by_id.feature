Feature: Institution: Get By Id

  Scenario: Get Institution By Id endpoint returns correctly when institution exists
    Given application is up
    And the institution id provided in the url is the correct format for the get institution by id endpoint
    And the requested institution exist in the database
    When the get institution by id endpoint is invoked
    Then the correct institution are returned from the get institution by id endpoint

  Scenario: Get Institution By Id endpoint returns correctly when no institution exists
    Given application is up
    And the institution id provided in the url is the correct format for the get institution by id endpoint
    And no institution for the given id exists in the database
    When the get institution by id endpoint is invoked
    Then the correct empty response is returned from the get institution by id endpoint

  Scenario Outline: Get Institution By Id endpoint is not able to parse the institution id in url
    Given application is up
    And the institution id provided in the url is the incorrect format for the get institution by id endpoint
    When the get institution by id endpoint is invoked
    Then the correct failure response is returned from the get institution by id endpoint
      | <statusCode> | <responseMessage> |

    Examples:
      | statusCode | responseMessage                       |
      | 400        | Invalid parameter type: institutionId |

  Scenario Outline: Get Institution By Id endpoint is not able to connect to the database
    Given application is up
    And the institution id provided in the url is the correct format for the get institution by id endpoint
    And the connection to the database fails for the get institution by id
    When the get institution by id endpoint is invoked
    Then the correct failure response is returned from the get institution by id endpoint
      | <statusCode> | <responseMessage> |

    Examples:
      | statusCode | responseMessage                                     |
      | 500        | Something went wrong while fetching the institution |
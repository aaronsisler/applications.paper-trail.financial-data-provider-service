Feature: Household Member: Get All


  Scenario Outline: Get all household members endpoint returns correctly when user id param exists
    Given application is up
    And two household members exist in the database for a given user id
    And one household member exists in the database for a different user id
    And the url does contain the user id query param for the get all household members endpoint
    And the database connection succeeds for get all household members
    When the get all household members endpoint is invoked
    Then the correct household members are returned from the get all household members endpoint
      | <size> |

    Examples:
      | size |
      | 2    |

  Scenario Outline: Get all household members endpoint returns correctly when no query params exist
    Given application is up
    And two household members exist in the database for a given user id
    And one household member exists in the database for a different user id
    And the url does not contain query params for the get all household members endpoint
    And the database connection succeeds for get all household members
    When the get all household members endpoint is invoked
    Then the correct household members are returned from the get all household members endpoint
      | <size> |

    Examples:
      | size |
      | 3    |

  Scenario: Get All household members endpoint returns correctly when no household members present for a given user id
    Given application is up
    And no household members exist in the database for a given user id
    And the url does contain the user id query param for the get all household members endpoint
    When the get all household members endpoint is invoked
    Then the correct empty household members response is returned

  Scenario: Get All household members endpoint returns correctly when no household members present
    Given application is up
    And no household members exist in the database
    And the url does not contain query params for the get all household members endpoint
    When the get all household members endpoint is invoked
    Then the correct empty household members response is returned

  Scenario Outline: Get all household members endpoint returns is not able to parse the user id in query param
    Given application is up
    And the user id provided in the url is the incorrect format for the get household by id endpoint
    When the get all household members endpoint is invoked
    Then the correct failure response is returned from the get all household members endpoint
      | <statusCode> | <responseMessage> |

    Examples:
      | statusCode | responseMessage                |
      | 400        | Invalid parameter type: userId |

  Scenario Outline: Get all household member endpoint is not able to connect to the database for get all household members
    Given application is up
    And the url does not contain query params for the get all household members endpoint
    And the service is not able to connect to the database for get all household members
    When the get all household members endpoint is invoked
    Then the correct failure response is returned from the get all household members endpoint
      | <statusCode> | <responseMessage> |

    Examples:
      | statusCode | responseMessage                                       |
      | 500        | Something went wrong while fetching household members |

  Scenario Outline: Get all household member endpoint is not able to connect to the database for get by id for household member
    Given application is up
    And the url does contain the user id query param for the get all household members endpoint
    And the service is not able to connect to the database for get by user id household members
    When the get all household members endpoint is invoked
    Then the correct failure response is returned from the get all household members endpoint
      | <statusCode> | <responseMessage> |

    Examples:
      | statusCode | responseMessage                                       |
      | 500        | Something went wrong while fetching household members |
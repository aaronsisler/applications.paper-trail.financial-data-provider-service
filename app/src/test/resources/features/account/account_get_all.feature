Feature: Account: Get All

  Scenario Outline: Get all accounts endpoint returns correctly when household member id param exists
    Given application is up
    And two accounts exist in the database for a given household member id
    And one account exists in the database for a different household member id
    And the url does contain the household member id query param for the get all accounts endpoint
    And the database connection succeeds for get all accounts
    When the get all accounts endpoint is invoked
    Then the correct accounts are returned from the get all accounts endpoint
      | <size> |

    Examples:
      | size |
      | 2    |

  Scenario Outline: Get all accounts endpoint returns correctly when no query params exist
    Given application is up
    And two accounts exist in the database for a given household member id
    And one account exists in the database for a different household member id
    And the url does not contain query params for the get all accounts endpoint
    And the database connection succeeds for get all accounts
    When the get all accounts endpoint is invoked
    Then the correct accounts are returned from the get all accounts endpoint
      | <size> |

    Examples:
      | size |
      | 3    |

  Scenario: Get All accounts endpoint returns correctly when no accounts present for a given household member id
    Given application is up
    And no accounts exist in the database for a given household member id
    And the url does contain the household member id query param for the get all accounts endpoint
    When the get all accounts endpoint is invoked
    Then the correct empty accounts response is returned
#
  Scenario: Get All accounts endpoint returns correctly when no accounts present
    Given application is up
    And no accounts exist in the database
    And the url does not contain query params for the get all accounts endpoint
    When the get all accounts endpoint is invoked
    Then the correct empty accounts response is returned

  Scenario Outline: Get all accounts endpoint returns is not able to parse the household member id in query param
    Given application is up
    And the household member id provided in the url is the incorrect format for the get accounts by id endpoint
    When the get all accounts endpoint is invoked
    Then the correct failure response is returned from the get all accounts endpoint
      | <statusCode> | <responseMessage> |

    Examples:
      | statusCode | responseMessage                           |
      | 400        | Invalid parameter type: householdMemberId |

  Scenario Outline: Get all account endpoint is not able to connect to the database for get all accounts
    Given application is up
    And the url does not contain query params for the get all accounts endpoint
    And the service is not able to connect to the database for get all accounts
    When the get all accounts endpoint is invoked
    Then the correct failure response is returned from the get all accounts endpoint
      | <statusCode> | <responseMessage> |

    Examples:
      | statusCode | responseMessage                              |
      | 500        | Something went wrong while fetching accounts |

  Scenario Outline: Get all account endpoint is not able to connect to the database for get by id for account
    Given application is up
    And the url does contain the household member id query param for the get all accounts endpoint
    And the service is not able to connect to the database for get by household member id accounts
    When the get all accounts endpoint is invoked
    Then the correct failure response is returned from the get all accounts endpoint
      | <statusCode> | <responseMessage> |

    Examples:
      | statusCode | responseMessage                              |
      | 500        | Something went wrong while fetching accounts |
Feature: Account Transactions: Get All


  Scenario Outline: Get all account transactions endpoint returns correctly when account id param exists
    Given application is up
    And two account transactions exist in the database for a given account id
    And one account transaction exists in the database for a different account id
    And the url does contain the account id query param for the get all account transactions endpoint
    And the database connection succeeds for get all account transactions
    When the get all account transactions endpoint is invoked
    Then the correct account transactions are returned from the get all account transactions endpoint
      | <size> |

    Examples:
      | size |
      | 2    |

  Scenario Outline: Get all account transactions endpoint returns correctly when no query params exist
    Given application is up
    And two account transactions exist in the database for a given account id
    And one account transaction exists in the database for a different account id
    And the url does not contain query params for the get all account transactions endpoint
    And the database connection succeeds for get all account transactions
    When the get all account transactions endpoint is invoked
    Then the correct account transactions are returned from the get all account transactions endpoint
      | <size> |

    Examples:
      | size |
      | 3    |

  Scenario: Get All account transactions endpoint returns correctly when no account transactions present for a given account id
    Given application is up
    And no account transactions exist in the database for a given account id
    And the url does contain the account id query param for the get all account transactions endpoint
    When the get all account transactions endpoint is invoked
    Then the correct empty account transactions response is returned

  Scenario: Get All account transactions endpoint returns correctly when no account transactions present
    Given application is up
    And no account transactions exist in the database
    And the url does not contain query params for the get all account transactions endpoint
    When the get all account transactions endpoint is invoked
    Then the correct empty account transactions response is returned

  Scenario Outline: Get all account transactions endpoint returns is not able to parse the account id in query param
    Given application is up
    And the account id provided in the url is the incorrect format for the get household by id endpoint
    When the get all account transactions endpoint is invoked
    Then the correct failure response is returned from the get all account transactions endpoint
      | <statusCode> | <responseMessage> |

    Examples:
      | statusCode | responseMessage                   |
      | 400        | Invalid parameter type: accountId |

  Scenario Outline: Get all account transactions endpoint is not able to connect to the database for get all account transactions
    Given application is up
    And the url does not contain query params for the get all account transactions endpoint
    And the service is not able to connect to the database for get all account transactions
    When the get all account transactions endpoint is invoked
    Then the correct failure response is returned from the get all account transactions endpoint
      | <statusCode> | <responseMessage> |

    Examples:
      | statusCode | responseMessage                                          |
      | 500        | Something went wrong while fetching account transactions |

  Scenario Outline: Get all account transactions endpoint is not able to connect to the database for get by id for account transaction
    Given application is up
    And the url does contain the account id query param for the get all account transactions endpoint
    And the service is not able to connect to the database for get by account id account transactions
    When the get all account transactions endpoint is invoked
    Then the correct failure response is returned from the get all account transactions endpoint
      | <statusCode> | <responseMessage> |

    Examples:
      | statusCode | responseMessage                                          |
      | 500        | Something went wrong while fetching account transactions |
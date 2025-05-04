Feature: Account: Update

  Scenario: Update Account endpoint returns correctly when valid account is present
    Given application is up
    And an account is part of the request body for the update account endpoint
    And a record with a matching account id resides in the database
    And the database connection succeeds for update account
    When the update account endpoint is invoked
    Then the updated account is returned from the update account endpoint

  Scenario Outline: Update Account endpoint returns correctly when invalid account is present
    Given application is up
    And a record with a matching account id resides in the database
    And the account in the update account request body has input values
      | <accountId> | <householdMemberId> | <institutionId> | <name> | <nickname> |
    When the update account endpoint is invoked
    Then the correct failure response and message is returned from the update account endpoint
      | <statusCode> | <responseMessage> |

    Examples:
      | accountId | householdMemberId | institutionId | name       | nickname       | statusCode | responseMessage                                  |
      | -1        | 456               | 789           | Valid Name | Valid Nickname | 400        | Account id must be positive and non-zero         |
      | 0         | 456               | 789           | Valid Name | Valid Nickname | 400        | Account id must be positive and non-zero         |
      |           | 456               | 789           | Valid Name | Valid Nickname | 400        | Account id must be positive and non-zero         |
      | 123       | 45                | 789           | Valid Name | Valid Nickname | 400        | Account's household member id cannot be modified |
      | 123       | 456               | 78            | Valid Name | Valid Nickname | 400        | Account's institution id cannot be modified      |
      | 123       |                   | 789           | Valid Name | Valid Nickname | 400        | household member id is mandatory                 |
      | 123       | 456               |               | Valid Name | Valid Nickname | 400        | institution id is mandatory                      |
      | 123       | 456               | 789           |            | Valid Nickname | 400        | name is mandatory                                |

  Scenario Outline: Update Account endpoint returns correctly when account is not in database
    Given application is up
    And a record with a matching account id does not reside in the database
    And the account in the update account request body has input values
      | <accountId> | <householdMemberId> | <institutionId> | <name> | <nickname> |
    When the update account endpoint is invoked
    Then the correct failure response and message is returned from the update account endpoint
      | <statusCode> | <responseMessage> |

    Examples:
      | accountId | householdMemberId | institutionId | name       | nickname       | statusCode | responseMessage                    |
      | 123       | 456               | 789           | Valid Name | Valid Nickname | 400        | Account does not exist for id: 123 |

  Scenario Outline: Update Account endpoint returns correctly when account is not in database
    Given application is up
    And retrieving the record with a matching account id throws an error
    And the account in the update account request body has input values
      | <accountId> | <householdMemberId> | <institutionId> | <name> | <nickname> |
    When the update account endpoint is invoked
    Then the correct failure response and message is returned from the update account endpoint
      | <statusCode> | <responseMessage> |

    Examples:
      | accountId | householdMemberId | institutionId | name       | nickname       | statusCode | responseMessage                               |
      | 123       | 456               | 789           | Valid Name | Valid Nickname | 500        | Something went wrong while saving the account |

  Scenario Outline: Update Account endpoint returns correctly when account is not in database
    Given application is up
    And a record with a matching account id resides in the database
    And the account in the update account request body has input values
      | <accountId> | <householdMemberId> | <institutionId> | <name> | <nickname> |
    And the database connection fails for update account
    When the update account endpoint is invoked
    Then the correct failure response and message is returned from the update account endpoint
      | <statusCode> | <responseMessage> |

    Examples:
      | accountId | householdMemberId | institutionId | name       | nickname       | statusCode | responseMessage                               |
      | 123       | 456               | 789           | Valid Name | Valid Nickname | 500        | Something went wrong while saving the account |

  Scenario Outline: Update Account endpoint returns correctly when account is not in database
    Given application is up
    And a record with a matching account id resides in the database
    And the account in the update account request body has input values
      | <accountId> | <householdMemberId> | <institutionId> | <name> | <nickname> |
    And the database connection fails for update account due to a relational integrity issue
    When the update account endpoint is invoked
    Then the correct failure response and message is returned from the update account endpoint
      | <statusCode> | <responseMessage> |

    Examples:
      | accountId | householdMemberId | institutionId | name       | nickname       | statusCode | responseMessage                               |
      | 123       | 456               | 789           | Valid Name | Valid Nickname | 500        | Something went wrong while saving the account |

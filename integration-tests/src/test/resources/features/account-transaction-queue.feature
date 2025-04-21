Feature: Integration: Account Transaction Queue

  Scenario: Ingest Account Transactions from queue and save to data store
    Given application is up
    And a valid account transaction has a matching account in the data store
    And the account transaction is on the account transaction queue
    When the application tries to process the account transaction queue
    Then the account transaction is available for retrieval
    And the application removes the message from the queue


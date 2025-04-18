Feature: Account Transaction: Queue Subscription

  Scenario: Account transaction queue is not able to receive messages during polling
    Given application is up
    And the application is not able to receive messages from the account transaction queue
    When the application tries to process the account transaction queue
    Then the application does not save any account transaction

  Scenario: Account transaction queue is does not have any messages during polling
    Given application is up
    And the account transaction queue does not have any messages
    And the application is able to receive messages from the account transaction queue
    When the application tries to process the account transaction queue
    Then the application does not save any account transaction

  Scenario: Account transaction message on the queue is not able to be parsed
    Given application is up
    And the account transaction queue has a message that is not able to be parsed
    And the application is able to receive messages from the account transaction queue
    When the application tries to process the account transaction queue
    Then the application does not save any account transaction
    And the message is deleted from the account transaction queue

  Scenario: Account transaction message on the queue has a populated account transaction id
    Given application is up
    And the account transaction queue has a message that has a populated account transaction id
    And the application is able to receive messages from the account transaction queue
    When the application tries to process the account transaction queue
    Then the application does not save any account transaction
    And the message is deleted from the account transaction queue

  Scenario: Account transaction message on the queue does not have an existing account id
    Given application is up
    And the account transaction queue has a message that does not have a matching account id
    And the application is able to receive messages from the account transaction queue
    When the application tries to process the account transaction queue
    Then the application does not save any account transaction
    And the message is deleted from the account transaction queue

#  Scenario: Account transaction message on the queue is not able to be deleted
#    Given application is up
#    And the application is able to receive messages from the account transaction queue
#    And the account transaction queue has a message that
#    When the application tries to process the account transaction queue

#  Happy path
#  Scenario: Account transaction message on the queue is able to be saved and deleted from queue
#    Given application is up
#    And the application is able to receive messages from the account transaction queue
#    And the account transaction queue has a message that
#    When the application tries to process the account transaction queue

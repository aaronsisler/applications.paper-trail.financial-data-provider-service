Feature: Account Transaction: Queue Subscription

#  Not able to receive messages
  Scenario: Account transaction queue is not able to receive messages during polling
    Given application is up
    And the account transaction queue has a message that is valid
    And the application is not able to receive messages from the account transaction queue
    When the application tries to process the account transaction queue
    Then the application does not save any account transaction

#  Messages are empty
  Scenario: Account transaction queue is does not have any messages during polling
    Given application is up
    And the account transaction queue does not have any messages
    When the application tries to process the account transaction queue

#  Not able to parse message from queue
  Scenario: Account transaction message on the queue is not able to be parsed
    Given application is up
    And the account transaction queue has a message that is not able to be parsed
    When the application tries to process the account transaction queue

#  Account Transaction Id is not null
  Scenario: Account transaction message on the queue has a populated account transaction id
    Given application is up
    And the account transaction queue has a message that has a populated account transaction id
    When the application tries to process the account transaction queue


#  NOT VALID? Account Trans set cannot have more than one account id
  Scenario: Account transaction messages on the queue have different account ids
    Given application is up
    And the account transaction queue has two messages that have different account ids
    When the application tries to process the account transaction queue

#  Account Id does not exist
  Scenario: Account transaction message on the queue does not have an existing account id
    Given application is up
    And the account transaction queue has a message that
    When the application tries to process the account transaction queue


#  Not able to delete a message from queue
  Scenario: Account transaction message on the queue is not able to be deleted
    Given application is up
    And the account transaction queue has a message that
    When the application tries to process the account transaction queue

#  Happy path
  Scenario: Account transaction message on the queue is able to be saved and deleted from queue
    Given application is up
    And the account transaction queue has a message that
    When the application tries to process the account transaction queue

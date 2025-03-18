package com.ebsolutions.papertrail.financialdataproviderservice.transaction;

import com.ebsolutions.papertrail.financialdataproviderservice.account.AccountService;
import com.ebsolutions.papertrail.financialdataproviderservice.common.exception.DataConstraintException;
import com.ebsolutions.papertrail.financialdataproviderservice.common.exception.DataProcessingException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class TransactionService {

  private final AccountService accountService;
  private final TransactionRepository transactionRepository;

  public List<Transaction> getAllByAccountId(Integer accountId) {
    try {
      return transactionRepository.findByAccountId(accountId);
    } catch (Exception exception) {
      log.error("Error getting all", exception);
      throw new DataProcessingException(
          "Something went wrong while fetching transactions");
    }
  }

  public List<Transaction> getAll() {
    try {
      return transactionRepository.findAll();
    } catch (Exception exception) {
      log.error("Error getting all", exception);
      throw new DataProcessingException(
          "Something went wrong while fetching transactions");
    }
  }

  public Optional<Transaction> get(Integer householdMemberId) {
    try {
      return transactionRepository.findById(householdMemberId.longValue());
    } catch (Exception exception) {
      log.error("Error getting by id", exception);
      throw new DataProcessingException("Something went wrong while fetching the transactions");
    }
  }

  public List<Transaction> createAll(
      List<Transaction> transactions) {
    try {
      if (transactions.isEmpty()) {
        throw new DataConstraintException(Collections.singletonList("Transaction cannot be empty"));
      }

      if (!transactions.stream()
          .filter(transaction -> transaction.getId() != null)
          .toList()
          .isEmpty()) {

        List<String> existingUserErrorMessages =
            transactions.stream()
                .filter(transaction -> transaction.getId() != null)
                .map(transaction -> "Transaction Id cannot be populated: ".concat(
                    Integer.toString(transaction.getId())))
                .toList();

        throw new DataConstraintException(existingUserErrorMessages);
      }

      if (transactions.stream().map(Transaction::getAccountId).distinct().toList().size() > 1) {

        List<String> accountIds =
            transactions.stream()
                .map(Transaction::getAccountId)
                .distinct()
                .map(Object::toString)
                .toList();

        String accountIdString = String.join(",", accountIds);

        List<String> existingAccountErrorMessages =
            Collections.singletonList(
                "Transactions cannot contain more than one account id : ".concat(accountIdString));

        throw new DataConstraintException(existingAccountErrorMessages);
      }

      var account = accountService.get(transactions.getFirst().getAccountId());

      if (account.isEmpty()) {
        List<String> existingAccountErrorMessages =
            Collections.singletonList(
                "Account Id does not exist: ".concat(
                    Integer.toString(transactions.getFirst().getAccountId())));

        throw new DataConstraintException(existingAccountErrorMessages);
      }

      return transactionRepository.saveAll(transactions);
    } catch (DataConstraintException dataConstraintException) {
      throw dataConstraintException;
    } catch (DataIntegrityViolationException dataIntegrityViolationException) {
      log.error("Data Integrity:", dataIntegrityViolationException);
      throw dataIntegrityViolationException;
    } catch (Exception exception) {
      log.error("Error creating", exception);
      throw new DataProcessingException("Something went wrong while saving household member");
    }
  }

  public Transaction update(Transaction transaction) {
    try {
      if (transaction.getId() <= 0) {
        List<String> existingUserErrorMessages =
            Collections.singletonList("Transaction Id must be positive and non-zero");

        throw new DataConstraintException(existingUserErrorMessages);
      }

      boolean doesTransactionExist =
          transactionRepository.findById((long) transaction.getId()).isPresent();

      if (!doesTransactionExist) {
        List<String> existingUserErrorMessages =
            Collections.singletonList(
                "Transaction Id does not exist: ".concat(Integer.toString(transaction.getId())));

        throw new DataConstraintException(existingUserErrorMessages);
      }

      return transactionRepository.save(transaction);
    } catch (DataConstraintException dataConstraintException) {
      throw dataConstraintException;
    } catch (Exception exception) {
      log.error("Error saving", exception);
      throw new DataProcessingException("Something went wrong while saving the transaction");
    }
  }

  public void delete(Integer transactionId) {
    try {
      transactionRepository.deleteById(transactionId.longValue());
    } catch (DataConstraintException dataConstraintException) {
      throw dataConstraintException;
    } catch (Exception exception) {
      log.error("Error deleting", exception);
      throw new DataProcessingException("Something went wrong while deleting the transaction");
    }
  }
}

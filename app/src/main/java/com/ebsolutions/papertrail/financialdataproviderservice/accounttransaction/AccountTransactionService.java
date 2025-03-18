package com.ebsolutions.papertrail.financialdataproviderservice.accounttransaction;

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
public class AccountTransactionService {

  private final AccountService accountService;
  private final AccountTransactionRepository accountTransactionRepository;

  public List<AccountTransaction> getAllByAccountId(Integer accountId) {
    try {
      return accountTransactionRepository.findByAccountId(accountId);
    } catch (Exception exception) {
      log.error("Error getting all", exception);
      throw new DataProcessingException(
          "Something went wrong while fetching account transactions");
    }
  }

  public List<AccountTransaction> getAll() {
    try {
      return accountTransactionRepository.findAll();
    } catch (Exception exception) {
      log.error("Error getting all", exception);
      throw new DataProcessingException(
          "Something went wrong while fetching account transactions");
    }
  }

  public Optional<AccountTransaction> get(Integer accountTransactionId) {
    try {
      return accountTransactionRepository.findById(accountTransactionId.longValue());
    } catch (Exception exception) {
      log.error("Error getting by id", exception);
      throw new DataProcessingException(
          "Something went wrong while fetching the account transaction");
    }
  }

  public List<AccountTransaction> createAll(
      List<AccountTransaction> accountTransactions) {
    try {
      if (accountTransactions.isEmpty()) {
        throw new DataConstraintException(
            Collections.singletonList("Account transactions cannot be empty"));
      }

      if (!accountTransactions.stream()
          .filter(transaction -> transaction.getId() != null)
          .toList()
          .isEmpty()) {

        List<String> existingUserErrorMessages =
            accountTransactions.stream()
                .filter(transaction -> transaction.getId() != null)
                .map(transaction -> "Account transaction id cannot be populated: ".concat(
                    Integer.toString(transaction.getId())))
                .toList();

        throw new DataConstraintException(existingUserErrorMessages);
      }

      if (accountTransactions.stream().map(AccountTransaction::getAccountId).distinct().toList()
          .size() > 1) {

        List<String> accountIds =
            accountTransactions.stream()
                .map(AccountTransaction::getAccountId)
                .distinct()
                .map(Object::toString)
                .toList();

        String accountIdString = String.join(", ", accountIds);

        List<String> indistinctAccountsErrorMessages =
            Collections.singletonList(
                "Account transactions cannot contain more than one account id : "
                    .concat(accountIdString));

        throw new DataConstraintException(indistinctAccountsErrorMessages);
      }

      var account = accountService.get(accountTransactions.getFirst().getAccountId());

      if (account.isEmpty()) {
        List<String> nonExistingAccountErrorMessages =
            Collections.singletonList(
                "Account Id does not exist: ".concat(
                    Integer.toString(accountTransactions.getFirst().getAccountId())));

        throw new DataConstraintException(nonExistingAccountErrorMessages);
      }

      return accountTransactionRepository.saveAll(accountTransactions);
    } catch (DataConstraintException dataConstraintException) {
      throw dataConstraintException;
    } catch (DataIntegrityViolationException dataIntegrityViolationException) {
      log.error("Data Integrity:", dataIntegrityViolationException);
      throw dataIntegrityViolationException;
    } catch (Exception exception) {
      log.error("Error creating", exception);
      throw new DataProcessingException("Something went wrong while saving account transactions");
    }
  }

  public AccountTransaction update(AccountTransaction accountTransaction) {
    try {
      if (accountTransaction.getId() <= 0) {
        List<String> existingUserErrorMessages =
            Collections.singletonList("Account transaction id must be positive and non-zero");

        throw new DataConstraintException(existingUserErrorMessages);
      }

      boolean doesTransactionExist =
          accountTransactionRepository.findById((long) accountTransaction.getId()).isPresent();

      if (!doesTransactionExist) {
        List<String> existingUserErrorMessages =
            Collections.singletonList(
                "Account transaction id does not exist: ".concat(
                    Integer.toString(accountTransaction.getId())));

        throw new DataConstraintException(existingUserErrorMessages);
      }

      return accountTransactionRepository.save(accountTransaction);
    } catch (DataConstraintException dataConstraintException) {
      throw dataConstraintException;
    } catch (Exception exception) {
      log.error("Error saving", exception);
      throw new DataProcessingException(
          "Something went wrong while saving the account transaction");
    }
  }

  public void delete(Integer accountTransactionId) {
    try {
      accountTransactionRepository.deleteById(accountTransactionId.longValue());
    } catch (DataConstraintException dataConstraintException) {
      throw dataConstraintException;
    } catch (Exception exception) {
      log.error("Error deleting", exception);
      throw new DataProcessingException(
          "Something went wrong while deleting the account transaction");
    }
  }
}

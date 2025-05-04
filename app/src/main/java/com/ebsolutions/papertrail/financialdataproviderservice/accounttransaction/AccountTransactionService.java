package com.ebsolutions.papertrail.financialdataproviderservice.accounttransaction;

import com.ebsolutions.papertrail.financialdataproviderservice.account.Account;
import com.ebsolutions.papertrail.financialdataproviderservice.account.AccountService;
import com.ebsolutions.papertrail.financialdataproviderservice.common.exception.DataConstraintException;
import com.ebsolutions.papertrail.financialdataproviderservice.common.exception.DataProcessingException;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
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

      List<Integer> accountIds =
          accountTransactions
              .stream().map(AccountTransaction::getAccountId)
              .distinct()
              .toList();

      List<Account> accounts = accountService.getAllByAccountIds(accountIds);

      if (accounts.size() != accountIds.size()) {
        List<String> missingAccounts = accountIds.stream()
            .filter(
                accountId -> accounts.stream()
                    .noneMatch(account ->
                        Objects.equals(account.getId(), accountId)))
            .map(Object::toString)
            .toList();

        String accountIdsString = String.join(", ", missingAccounts);


        List<String> nonExistingAccountErrorMessages =
            Collections.singletonList(
                "Account Ids do not exist: ".concat(accountIdsString));

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

        throw new DataConstraintException(
            Collections.singletonList("Account transaction id must be positive and non-zero")
        );
      }

      boolean doesAccountTransactionExist =
          accountTransactionRepository.findById((long) accountTransaction.getId()).isPresent();

      if (!doesAccountTransactionExist) {
        List<String> nonExistingAccountTransactionErrorMessages =
            Collections.singletonList(
                "Account transaction id does not exist: ".concat(
                    Integer.toString(accountTransaction.getId())));

        throw new DataConstraintException(nonExistingAccountTransactionErrorMessages);
      }

      boolean doesAccountExist =
          accountService.get(accountTransaction.getAccountId()).isPresent();

      if (!doesAccountExist) {
        List<String> nonExistingAccountErrorMessages =
            Collections.singletonList(
                "Account id does not exist: ".concat(
                    Integer.toString(accountTransaction.getAccountId())));

        throw new DataConstraintException(nonExistingAccountErrorMessages);
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

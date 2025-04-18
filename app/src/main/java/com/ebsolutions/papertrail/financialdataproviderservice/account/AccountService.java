package com.ebsolutions.papertrail.financialdataproviderservice.account;

import com.ebsolutions.papertrail.financialdataproviderservice.common.exception.DataConstraintException;
import com.ebsolutions.papertrail.financialdataproviderservice.common.exception.DataProcessingException;
import com.ebsolutions.papertrail.financialdataproviderservice.householdmember.HouseholdMemberService;
import com.ebsolutions.papertrail.financialdataproviderservice.institution.InstitutionService;
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
public class AccountService {

  private static final String ERROR_GETTING_ALL_MESSAGE = "Error getting all";

  private final InstitutionService institutionService;
  private final HouseholdMemberService householdMemberService;
  private final AccountRepository accountRepository;

  public Optional<Account> get(Integer accountId) {
    try {
      return accountRepository.findById(accountId.longValue());
    } catch (Exception exception) {
      log.error("Error getting by id", exception);
      throw new DataProcessingException("Something went wrong while fetching the account");
    }
  }

  public List<Account> getAllByHouseholdMemberId(Integer householdMemberId) {
    try {
      return accountRepository.findByHouseholdMemberId(householdMemberId);
    } catch (Exception exception) {
      log.error(ERROR_GETTING_ALL_MESSAGE, exception);
      throw new DataProcessingException(
          "Something went wrong while fetching accounts");
    }
  }

  public List<Account> getAllByAccountIds(List<Integer> accountIds) {
    try {
      List<Long> longAccountIds =
          accountIds.stream().map(Long::valueOf).toList();

      return accountRepository.findAllById(longAccountIds);
    } catch (Exception exception) {
      log.error(ERROR_GETTING_ALL_MESSAGE, exception);
      throw new DataProcessingException(
          "Something went wrong while fetching accounts");
    }
  }

  public List<Account> getAll() {
    try {
      return accountRepository.findAll();
    } catch (Exception exception) {
      log.error(ERROR_GETTING_ALL_MESSAGE, exception);
      throw new DataProcessingException(
          "Something went wrong while fetching accounts");
    }
  }

  public Account create(
      Account account) {
    try {
      if (account.getId() != null) {
        throw new DataConstraintException(Collections.singletonList(
            "Account Id cannot be populated: ".concat(
                String.valueOf(account.getId()))));
      }

      var institution = institutionService.get(account.getInstitutionId());

      if (institution.isEmpty()) {
        List<String> existingUserErrorMessages =
            Collections.singletonList(
                "Institution Id does not exist: ".concat(
                    Integer.toString(account.getInstitutionId())));

        throw new DataConstraintException(existingUserErrorMessages);
      }

      var householdMember =
          householdMemberService.get(account.getHouseholdMemberId());

      if (householdMember.isEmpty()) {
        List<String> existingUserErrorMessages =
            Collections.singletonList(
                "Household Member Id does not exist: ".concat(
                    Integer.toString(account.getHouseholdMemberId())));

        throw new DataConstraintException(existingUserErrorMessages);
      }

      return accountRepository.save(account);
    } catch (DataConstraintException dataConstraintException) {
      throw dataConstraintException;
    } catch (DataIntegrityViolationException dataIntegrityViolationException) {
      log.error("Data Integrity:", dataIntegrityViolationException);
      throw dataIntegrityViolationException;
    } catch (Exception exception) {
      log.error("Error creating", exception);
      throw new DataProcessingException("Something went wrong while saving account");
    }
  }
}

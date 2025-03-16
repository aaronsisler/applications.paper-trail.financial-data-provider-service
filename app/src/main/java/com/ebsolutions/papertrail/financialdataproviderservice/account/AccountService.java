package com.ebsolutions.papertrail.financialdataproviderservice.account;

import com.ebsolutions.papertrail.financialdataproviderservice.common.exception.DataConstraintException;
import com.ebsolutions.papertrail.financialdataproviderservice.common.exception.DataProcessingException;
import com.ebsolutions.papertrail.financialdataproviderservice.householdmember.HouseholdMemberService;
import com.ebsolutions.papertrail.financialdataproviderservice.institution.InstitutionService;
import java.util.Collections;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccountService {

  private final InstitutionService institutionService;
  private final HouseholdMemberService householdMemberService;
  private final AccountRepository accountRepository;

  public List<Account> getAllById(Integer userId) {
    try {
      return accountRepository.findByUserId(userId);
    } catch (Exception exception) {
      log.error("Error getting all", exception);
      throw new DataProcessingException(
          "Something went wrong while fetching accounts");
    }
  }

  public List<Account> getAll() {
    try {
      return accountRepository.findAll();
    } catch (Exception exception) {
      log.error("Error getting all", exception);
      throw new DataProcessingException(
          "Something went wrong while fetching household members");
    }
  }

  public Account create(
      Account account) {
    try {
      if (account.getId() != null) {
        throw new DataConstraintException(Collections.singletonList(
            "Household Member Id cannot be populated: ".concat(
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

      var householdMember = householdMemberService.getAllById(account.getHouseholdMemberId());

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
      throw new DataProcessingException("Something went wrong while saving household member");
    }
  }
}

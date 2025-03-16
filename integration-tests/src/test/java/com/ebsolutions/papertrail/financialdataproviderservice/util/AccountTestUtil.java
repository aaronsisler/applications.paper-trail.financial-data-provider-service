package com.ebsolutions.papertrail.financialdataproviderservice.util;

import com.ebsolutions.papertrail.financialdataproviderservice.model.Account;
import org.junit.jupiter.api.Assertions;

public class AccountTestUtil {
  public static void assertExpectedAgainstActual(Account expectedAccount, Account actualAccount) {
    Assertions.assertEquals(expectedAccount.getId(),
        actualAccount.getId());

    Assertions.assertEquals(expectedAccount.getHouseholdMemberId(),
        actualAccount.getHouseholdMemberId());

    Assertions.assertEquals(expectedAccount.getInstitutionId(), actualAccount.getInstitutionId());
    Assertions.assertEquals(expectedAccount.getName(), actualAccount.getName());
    Assertions.assertEquals(expectedAccount.getNickname(), actualAccount.getNickname());
  }

  public static void assertExpectedAgainstCreated(Account expectedAccount, Account actualAccount) {

    Assertions.assertEquals(expectedAccount.getHouseholdMemberId(),
        actualAccount.getHouseholdMemberId());

    Assertions.assertEquals(expectedAccount.getInstitutionId(), actualAccount.getInstitutionId());
    Assertions.assertEquals(expectedAccount.getName(), actualAccount.getName());
    Assertions.assertEquals(expectedAccount.getNickname(), actualAccount.getNickname());
  }
}

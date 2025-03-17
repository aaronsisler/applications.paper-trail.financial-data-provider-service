package com.ebsolutions.papertrail.financialdataproviderservice.util;

import com.ebsolutions.papertrail.financialdataproviderservice.account.Account;
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
}

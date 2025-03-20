package com.ebsolutions.papertrail.financialdataproviderservice.util;

import com.ebsolutions.papertrail.financialdataproviderservice.model.AccountTransaction;
import org.junit.jupiter.api.Assertions;

public class AccountTransactionTestUtil {
  public static void assertExpectedAgainstActual(AccountTransaction expected,
                                                 AccountTransaction actual) {

    Assertions.assertEquals(expected.getId(), actual.getId());
    Assertions.assertEquals(expected.getAccountId(), actual.getAccountId());
    Assertions.assertEquals(expected.getAmount(), actual.getAmount());
    Assertions.assertEquals(expected.getDescription(), actual.getDescription());
  }

  public static void assertExpectedAgainstCreated(AccountTransaction expected,
                                                  AccountTransaction actual) {

    Assertions.assertEquals(expected.getAccountId(), actual.getAccountId());
    Assertions.assertEquals(expected.getAmount(), actual.getAmount());
    Assertions.assertEquals(expected.getDescription(), actual.getDescription());
  }
}

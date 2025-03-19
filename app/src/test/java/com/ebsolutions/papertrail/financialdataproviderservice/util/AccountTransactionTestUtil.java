package com.ebsolutions.papertrail.financialdataproviderservice.util;

import com.ebsolutions.papertrail.financialdataproviderservice.accounttransaction.AccountTransaction;
import org.junit.jupiter.api.Assertions;

public class AccountTransactionTestUtil {
  public static void assertExpectedAgainstActual(AccountTransaction expectedAccountTransaction,
                                                 AccountTransaction actualAccountTransaction) {
    Assertions.assertEquals(
        expectedAccountTransaction.getId(),
        actualAccountTransaction.getId());

    Assertions.assertEquals(expectedAccountTransaction.getAccountId(),
        actualAccountTransaction.getAccountId());

    Assertions.assertEquals(expectedAccountTransaction.getAmount(),
        actualAccountTransaction.getAmount());

    Assertions.assertEquals(
        expectedAccountTransaction.getDescription(),
        actualAccountTransaction.getDescription());
  }
}

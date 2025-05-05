package com.ebsolutions.papertrail.financialdataproviderservice.testdata;

import com.ebsolutions.papertrail.financialdataproviderservice.model.AccountTransaction;

public enum AccountTransactionTestData {
  ACCOUNT_TRANSACTION_UPDATE {
    @Override
    public AccountTransaction get() {
      return AccountTransaction.builder()
          .id(1)
          .accountId(4)
          .amount(123)
          .description("account_transaction_update_description")
          .build();
    }
  },
  ACCOUNT_TRANSACTION_DELETE {
    @Override
    public AccountTransaction get() {
      return AccountTransaction.builder()
          .id(2)
          .accountId(5)
          .amount(456)
          .description("account_transaction_delete_description")
          .build();
    }
  };

  public abstract AccountTransaction get();
}
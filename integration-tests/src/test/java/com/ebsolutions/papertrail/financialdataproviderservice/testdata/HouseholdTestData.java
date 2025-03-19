package com.ebsolutions.papertrail.financialdataproviderservice.testdata;

import com.ebsolutions.papertrail.financialdataproviderservice.model.Household;

public enum HouseholdTestData {
  NO_FOREIGN_KEY_USAGE {
    @Override
    public Household get() {
      return Household.builder()
          .id(1)
          .name("main_household")
          .build();
    }
  },
  UPDATE {
    @Override
    public Household get() {
      return Household.builder()
          .id(2)
          .name("update_household")
          .build();
    }
  },
  DELETE {
    @Override
    public Household get() {
      return Household.builder()
          .id(3)
          .name("delete_household")
          .build();
    }
  },
  HOUSEHOLD_MEMBER_CREATE {
    @Override
    public Household get() {
      return Household.builder()
          .id(4)
          .name("household_member_create_household")
          .build();
    }
  },
  ACCOUNT_CREATE {
    @Override
    public Household get() {
      return Household.builder()
          .id(5)
          .name("account_create_household")
          .build();
    }
  },
  ACCOUNT_TRANSACTION_CREATE {
    @Override
    public Household get() {
      return Household.builder()
          .id(6)
          .name("account_transaction_create_household")
          .build();
    }
  },
  ACCOUNT_TRANSACTION_UPDATE {
    @Override
    public Household get() {
      return Household.builder()
          .id(7)
          .name("account_transaction_update_household")
          .build();
    }
  },
  ACCOUNT_TRANSACTION_DELETE {
    @Override
    public Household get() {
      return Household.builder()
          .id(8)
          .name("account_transaction_delete_household")
          .build();
    }
  };

  public abstract Household get();
}
package com.ebsolutions.papertrail.financialdataproviderservice.tooling;

import com.ebsolutions.papertrail.financialdataproviderservice.model.Household;

public enum HouseholdTestData {
  NO_FOREIGN_KEY_USAGE {
    @Override
    public Household get() {
      return Household.builder()
          .householdId(1)
          .name("main_household")
          .build();
    }
  },
  UPDATE {
    @Override
    public Household get() {
      return Household.builder()
          .householdId(2)
          .name("update_household")
          .build();
    }
  },
  DELETE {
    @Override
    public Household get() {
      return Household.builder()
          .householdId(3)
          .name("delete_household")
          .build();
    }
  },
  HOUSEHOLD_MEMBER_CREATE {
    @Override
    public Household get() {
      return Household.builder()
          .householdId(4)
          .name("household_member_create_household")
          .build();
    }
  };

  public abstract Household get();
}
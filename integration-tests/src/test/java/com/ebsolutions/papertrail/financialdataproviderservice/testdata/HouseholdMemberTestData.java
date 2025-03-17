package com.ebsolutions.papertrail.financialdataproviderservice.testdata;

import com.ebsolutions.papertrail.financialdataproviderservice.model.HouseholdMember;

public enum HouseholdMemberTestData {
  ACCOUNT_CREATE_ONE {
    @Override
    public HouseholdMember get() {
      return HouseholdMember.builder()
          .id(1)
          .householdId(4)
          .userId(6)
          .build();
    }
  },
  ACCOUNT_CREATE_TWO {
    @Override
    public HouseholdMember get() {
      return HouseholdMember.builder()
          .id(2)
          .householdId(4)
          .userId(6)
          .build();
    }
  };

  public abstract HouseholdMember get();
}
package com.ebsolutions.papertrail.financialdataproviderservice.testdata;

import com.ebsolutions.papertrail.financialdataproviderservice.model.HouseholdMember;

public enum HouseholdMemberTestData {
  ACCOUNT_CREATE_ONE {
    @Override
    public HouseholdMember get() {
      return HouseholdMember.builder()
          .id(1)
          .householdId(5)
          .userId(6)
          .build();
    }
  },
  ACCOUNT_CREATE_TWO {
    @Override
    public HouseholdMember get() {
      return HouseholdMember.builder()
          .id(2)
          .householdId(5)
          .userId(7)
          .build();
    }
  },
  ACCOUNT_UPDATE {
    @Override
    public HouseholdMember get() {
      return HouseholdMember.builder()
          .id(3)
          .householdId(6)
          .userId(8)
          .build();
    }
  },
  ACCOUNT_TRANSACTION_CREATE {
    @Override
    public HouseholdMember get() {
      return HouseholdMember.builder()
          .id(4)
          .householdId(7)
          .userId(9)
          .build();
    }
  },
  ACCOUNT_TRANSACTION_UPDATE {
    @Override
    public HouseholdMember get() {
      return HouseholdMember.builder()
          .id(5)
          .householdId(6)
          .userId(10)
          .build();
    }
  },
  ACCOUNT_TRANSACTION_DELETE {
    @Override
    public HouseholdMember get() {
      return HouseholdMember.builder()
          .id(6)
          .householdId(9)
          .userId(11)
          .build();
    }
  },
  ACCOUNT_TRANSACTION_INGESTION {
    @Override
    public HouseholdMember get() {
      return HouseholdMember.builder()
          .id(7)
          .householdId(10)
          .userId(12)
          .build();
    }
  };

  public abstract HouseholdMember get();
}
package com.ebsolutions.papertrail.financialdataproviderservice.testdata;

import com.ebsolutions.papertrail.financialdataproviderservice.model.User;

public enum UserTestData {
  NO_FOREIGN_KEY_USAGE {
    @Override
    public User get() {
      return User.builder()
          .id(1)
          .username("main_user")
          .firstName("Main")
          .lastName("User")
          .build();
    }
  },
  UPDATE {
    @Override
    public User get() {
      return User.builder()
          .id(2)
          .username("update_user")
          .firstName("Update")
          .lastName("User")
          .build();
    }
  },
  DELETE {
    @Override
    public User get() {
      return User.builder()
          .id(3)
          .username("delete_user")
          .firstName("Delete")
          .lastName("User")
          .build();
    }
  },
  HOUSEHOLD_MEMBER_CREATE_ONE {
    @Override
    public User get() {
      return User.builder()
          .id(4)
          .username("household_member_create_user_1")
          .firstName("Household Member Create 1")
          .lastName("User")
          .build();
    }
  },
  HOUSEHOLD_MEMBER_CREATE_TWO {
    @Override
    public User get() {
      return User.builder()
          .id(5)
          .username("household_member_create_user_2")
          .firstName("Household Member Create 2")
          .lastName("User")
          .build();
    }
  },
  ACCOUNT_CREATE {
    @Override
    public User get() {
      return User.builder()
          .id(6)
          .username("account_create_user")
          .firstName("Account Create")
          .lastName("User")
          .build();
    }
  };

  public abstract User get();
}
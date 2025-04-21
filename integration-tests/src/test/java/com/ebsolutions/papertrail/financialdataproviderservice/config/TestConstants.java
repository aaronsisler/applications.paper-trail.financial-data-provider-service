package com.ebsolutions.papertrail.financialdataproviderservice.config;

public class TestConstants {
  public static final int APPLICATION_START_TIME_WAIT_PERIOD_IN_MILLISECONDS = 30000;
  public static final int QUEUE_POLLING_WAIT_PERIOD_IN_MILLISECONDS = 3000;
  public static final String EMPTY_STRING_ENUM = "EMPTY_STRING";
  public static final String EMPTY_STRING = "";

  public static final String BASE_URL = "http://localhost:8080";
  public static final String HEALTH_CHECK_URI = "/actuator/health";
  public static final String ACCOUNTS_URI = "/accounts";
  public static final String ACCOUNT_TRANSACTIONS_URI = "/account-transactions";
  public static final String HOUSEHOLDS_URI = "/households";
  public static final String HOUSEHOLD_MEMBERS_URI = "/household-members";
  public static final String INSTITUTIONS_URI = "/institutions";
  public static final String USERS_URI = "/users";
}

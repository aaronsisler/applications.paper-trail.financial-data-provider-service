package com.ebsolutions.papertrail.financialdataproviderservice.util;

import com.ebsolutions.papertrail.financialdataproviderservice.tooling.TestConstants;

public class CommonTestUtil {
  public static String isEmptyString(String value) {
    return TestConstants.EMPTY_STRING_ENUM.equals(value) ? TestConstants.EMPTY_STRING : value;
  }

  public static String isInvalidDate(String value) {
    return TestConstants.INVALID_DATE_ENUM.equals(value) ? TestConstants.INVALID_DATE : value;
  }
}

package com.ebsolutions.papertrail.financialdataproviderservice.common;

import com.ebsolutions.papertrail.financialdataproviderservice.tooling.TestConstants;

public class CommonTestUtil {
  public static String isEmptyString(String value) {
    return TestConstants.EMPTY_STRING_ENUM.equals(value) ? TestConstants.EMPTY_STRING : value;
  }
}

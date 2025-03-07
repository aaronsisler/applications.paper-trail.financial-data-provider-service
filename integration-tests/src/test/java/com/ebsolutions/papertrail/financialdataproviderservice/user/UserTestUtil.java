package com.ebsolutions.papertrail.financialdataproviderservice.user;

import com.ebsolutions.papertrail.financialdataproviderservice.tooling.TestConstants;
import org.junit.jupiter.api.Assertions;

public class UserTestUtil {
  protected static void assertExpectedUserAgainstActualUser(User expectedUser, User actualUser) {
    Assertions.assertEquals(expectedUser.getUserId(), actualUser.getUserId());
    Assertions.assertEquals(expectedUser.getUsername(), actualUser.getUsername());
    Assertions.assertEquals(expectedUser.getFirstName(), actualUser.getFirstName());
    Assertions.assertEquals(expectedUser.getLastName(), actualUser.getLastName());
  }

  protected static String isEmptyString(String value) {
    return TestConstants.EMPTY_STRING_ENUM.equals(value) ? TestConstants.EMPTY_STRING : value;
  }
}

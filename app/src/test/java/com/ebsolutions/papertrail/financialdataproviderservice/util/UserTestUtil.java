package com.ebsolutions.papertrail.financialdataproviderservice.util;

import com.ebsolutions.papertrail.financialdataproviderservice.user.User;
import org.junit.jupiter.api.Assertions;

public class UserTestUtil {
  public static void assertExpectedAgainstActual(User expectedUser, User actualUser) {
    Assertions.assertEquals(expectedUser.getId(), actualUser.getId());
    Assertions.assertEquals(expectedUser.getUsername(), actualUser.getUsername());
    Assertions.assertEquals(expectedUser.getFirstName(), actualUser.getFirstName());
    Assertions.assertEquals(expectedUser.getLastName(), actualUser.getLastName());
  }
}

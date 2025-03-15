package com.ebsolutions.papertrail.financialdataproviderservice.util;

import com.ebsolutions.papertrail.financialdataproviderservice.model.Institution;
import org.junit.jupiter.api.Assertions;

public class InstitutionTestUtil {
  public static void assertExpectedAgainstActual(Institution expectedInstitution,
                                                 Institution actualInstitution) {
    Assertions.assertEquals(expectedInstitution.getInstitutionId(),
        actualInstitution.getInstitutionId());
    Assertions.assertEquals(expectedInstitution.getName(), actualInstitution.getName());
  }

  public static void assertExpectedAgainstCreated(Institution expectedInstitution,
                                                  Institution actualInstitution) {
    Assertions.assertEquals(expectedInstitution.getName(), actualInstitution.getName());
  }

  public static Institution getTestDataInstitution() {
    return Institution.builder()
        .institutionId(1)
        .name("my_bank_institution")
        .build();
  }
}

package com.ebsolutions.papertrail.financialdataproviderservice.householdmember;

import com.ebsolutions.papertrail.financialdataproviderservice.common.BaseEntity;
import com.ebsolutions.papertrail.financialdataproviderservice.common.DatabaseConstants;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.validator.constraints.Range;

@Data
@Entity
@SuperBuilder
@RequiredArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Table(name = DatabaseConstants.HOUSEHOLD_MEMBER_TABLE)
public class HouseholdMember extends BaseEntity {

  @Range(min = 1, message = "household id is mandatory")
  @JsonProperty("householdId")
  @Schema(name = "householdId",
      requiredMode = Schema.RequiredMode.REQUIRED,
      description = "Household Id",
      example = "1")
  private int householdId;

  @Range(min = 1, message = "user id is mandatory")
  @JsonProperty("userId")
  @Schema(name = "userId",
      requiredMode = Schema.RequiredMode.REQUIRED,
      description = "User Id",
      example = "1")
  private int userId;
}
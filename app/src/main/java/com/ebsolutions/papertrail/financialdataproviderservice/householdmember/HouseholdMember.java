package com.ebsolutions.papertrail.financialdataproviderservice.householdmember;

import com.ebsolutions.papertrail.financialdataproviderservice.common.DatabaseConstants;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Range;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Data
@Table(name = DatabaseConstants.HOUSEHOLD_MEMBER_TABLE)
public class HouseholdMember {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @JsonProperty("householdMemberId")
  @Schema(description = "Household Member Id", example = "1")
  private int householdMemberId;

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
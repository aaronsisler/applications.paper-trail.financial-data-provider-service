package com.ebsolutions.papertrail.financialdataproviderservice.account;

import com.ebsolutions.papertrail.financialdataproviderservice.common.BaseEntity;
import com.ebsolutions.papertrail.financialdataproviderservice.common.DatabaseConstants;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
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
public class Account extends BaseEntity {

  @Range(min = 1, message = "institution id is mandatory")
  @JsonProperty("institutionId")
  @Schema(name = "institutionId",
      requiredMode = Schema.RequiredMode.REQUIRED,
      description = "Institution Id",
      example = "1")
  private int institutionId;

  @Range(min = 1, message = "household member id is mandatory")
  @JsonProperty("householdMemberId")
  @Schema(name = "householdMemberId",
      requiredMode = Schema.RequiredMode.REQUIRED,
      description = "Household Member Id",
      example = "1")
  private int householdMemberId;

  @NotBlank(message = "name is mandatory")
  @JsonProperty("name")
  @Schema(name = "name",
      requiredMode = Schema.RequiredMode.REQUIRED,
      description = "Name",
      example = "My Bank")
  private String name;

  @JsonProperty("nickname")
  @Schema(name = "nickname",
      description = "nickname",
      example = "Checking for Mom")
  private String nickname;
}
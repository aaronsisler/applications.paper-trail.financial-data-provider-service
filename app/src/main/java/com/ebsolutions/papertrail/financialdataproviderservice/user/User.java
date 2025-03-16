package com.ebsolutions.papertrail.financialdataproviderservice.user;

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

@Data
@Entity
@SuperBuilder
@RequiredArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Table(name = DatabaseConstants.USER_TABLE)
public class User extends BaseEntity {

  @NotBlank(message = "username is mandatory")
  @JsonProperty("username")
  @Schema(name = "username",
      requiredMode = Schema.RequiredMode.REQUIRED,
      description = "Username",
      example = "johnny_appleseed_42")
  private String username;

  @NotBlank(message = "firstName is mandatory")
  @JsonProperty("firstName")
  @Schema(name = "firstName",
      requiredMode = Schema.RequiredMode.REQUIRED,
      description = "First Name",
      example = "Johnny")
  private String firstName;

  @NotBlank(message = "lastName is mandatory")
  @JsonProperty("lastName")
  @Schema(name = "lastName",
      requiredMode = Schema.RequiredMode.REQUIRED,
      description = "Last Name",
      example = "Appleseed")
  private String lastName;
}
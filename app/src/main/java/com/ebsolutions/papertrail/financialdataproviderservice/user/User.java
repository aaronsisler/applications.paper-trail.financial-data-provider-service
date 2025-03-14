package com.ebsolutions.papertrail.financialdataproviderservice.user;

import com.ebsolutions.papertrail.financialdataproviderservice.common.DatabaseConstants;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Data
@Table(name = DatabaseConstants.USER_TABLE)
public class User {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @JsonProperty("userId")
  @Schema(description = "User Id", example = "1")
  private int userId;

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
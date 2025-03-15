package com.ebsolutions.papertrail.financialdataproviderservice.institution;

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
@Table(name = DatabaseConstants.INSTITUTION_TABLE)
public class Institution {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @JsonProperty("institutionId")
  @Schema(description = "Institution Id", example = "1")
  private int institutionId;

  @NotBlank(message = "name is mandatory")
  @JsonProperty("name")
  @Schema(name = "name",
      requiredMode = Schema.RequiredMode.REQUIRED,
      description = "Name",
      example = "Appleseed Institution")
  private String name;
}
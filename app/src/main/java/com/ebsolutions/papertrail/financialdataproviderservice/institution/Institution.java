package com.ebsolutions.papertrail.financialdataproviderservice.institution;

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
@Table(name = DatabaseConstants.INSTITUTION_TABLE)
public class Institution extends BaseEntity {

  @NotBlank(message = "name is mandatory")
  @JsonProperty("name")
  @Schema(name = "name",
      requiredMode = Schema.RequiredMode.REQUIRED,
      description = "Name",
      example = "Appleseed Institution")
  private String name;
}
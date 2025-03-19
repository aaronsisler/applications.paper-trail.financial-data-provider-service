package com.ebsolutions.papertrail.financialdataproviderservice.accounttransaction;

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
@Table(name = DatabaseConstants.ACCOUNT_TRANSACTION_TABLE)
public class AccountTransaction extends BaseEntity {

  @Range(min = 1, message = "account id is mandatory")
  @JsonProperty("accountId")
  @Schema(name = "accountId",
      requiredMode = Schema.RequiredMode.REQUIRED,
      description = "Account Id",
      example = "12")
  private int accountId;

  @Range(min = 1, message = "amount is mandatory")
  @JsonProperty("amount")
  @Schema(name = "amount",
      requiredMode = Schema.RequiredMode.REQUIRED,
      description = "Amount",
      example = "125")
  private int amount;

  @NotBlank(message = "description is mandatory")
  @JsonProperty("description")
  @Schema(name = "description",
      requiredMode = Schema.RequiredMode.REQUIRED,
      description = "Description",
      example = "Place we shopped at")
  private String description;
}
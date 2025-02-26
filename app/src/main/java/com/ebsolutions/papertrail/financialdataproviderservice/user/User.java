package com.ebsolutions.papertrail.financialdataproviderservice.user;

import com.ebsolutions.papertrail.financialdataproviderservice.common.DatabaseConstants;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = DatabaseConstants.USER_TABLE)
public class User {
  @Id
  @Schema(description = "User Id", example = "1")
  private int userId;
  @Schema(description = "Username", example = "johnny_appleseed_42")
  private String username;
  @Schema(description = "First Name", example = "Johnny")
  private String firstName;
  @Schema(description = "Last Name", example = "Appleseed")
  private String lastName;
}
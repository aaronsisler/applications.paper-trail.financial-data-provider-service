package com.ebsolutions.papertrail.financialdataproviderservice.user;

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
public class UserDto {
  private int userId;
  private String username;
  private String firstName;
  private String lastName;
}

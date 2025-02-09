package com.ebsolutions.papertrail.financialdataproviderservice.user;

import java.util.Collections;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class UserJdbcDao implements UserDao {
  public List<UserDto> readAll() {
    return Collections.emptyList();
  }
}

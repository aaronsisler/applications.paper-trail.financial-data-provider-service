package com.ebsolutions.papertrail.financialdataproviderservice.user;

import java.util.List;
import org.springframework.stereotype.Component;

@Component
public interface UserDao {
  List<UserDto> readAll();
}

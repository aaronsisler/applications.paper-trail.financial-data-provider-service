package com.ebsolutions.papertrail.financialdataproviderservice.user;

import com.ebsolutions.papertrail.financialdataproviderservice.model.User;
import java.util.Collections;
import java.util.List;
import lombok.AllArgsConstructor;

//@Component
@AllArgsConstructor
public class JdbcUserRepository {
  private final UserDao userDao;

  public List<User> getAll() {
    List<UserDto> userDtos = userDao.readAll();

    if (userDtos.isEmpty()) {
      return Collections.emptyList();
    }

    return userDtos.stream().map(userDto ->
            User.builder()
                .userId(userDto.getUserId())
                .username(userDto.getUsername())
                .firstName(userDto.getFirstName())
                .lastName(userDto.getLastName())
                .build())
        .toList();
  }
}

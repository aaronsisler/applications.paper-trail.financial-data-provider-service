package com.ebsolutions.papertrail.financialdataproviderservice.user;

import com.ebsolutions.papertrail.financialdataproviderservice.common.exception.DataConstraintException;
import com.ebsolutions.papertrail.financialdataproviderservice.common.exception.DataProcessingException;
import java.util.Collections;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
  private final UserRepository userRepository;

  public List<User> getAll() {
    try {
      return userRepository.findAll();
    } catch (Exception exception) {
      throw new DataProcessingException("Something went wrong while getting all users");
    }
  }

  public List<User> createAll(List<User> users) {
    try {
      if (users.isEmpty()) {
        throw new DataConstraintException(Collections.singletonList("Users cannot be empty"));
      }

      if (!users.stream().filter(user -> user.getUserId() > 0).toList().isEmpty()) {
        List<String> existingUserErrorMessages =
            users.stream()
                .map(user -> "User Id cannot be populated: ".concat(
                    Integer.toString(user.getUserId())))
                .toList();

        throw new DataConstraintException(existingUserErrorMessages);
      }

      return userRepository.saveAll(users);
    } catch (DataConstraintException dataConstraintException) {
      throw dataConstraintException;
    } catch (Exception exception) {
      throw new DataProcessingException("Something went wrong while saving all users");
    }
  }

  public User update(User user) {
    try {

      if (user.getUserId() == 0) {
        List<String> existingUserErrorMessages =
            Collections.singletonList("User Id must be populated and non-zero");

        throw new DataConstraintException(existingUserErrorMessages);
      }

      boolean doesUserExist = userRepository.findById((long) user.getUserId()).isEmpty();

      if (!doesUserExist) {
        List<String> existingUserErrorMessages =
            Collections.singletonList(
                "User Id does not exist: ".concat(Integer.toString(user.getUserId())));

        throw new DataConstraintException(existingUserErrorMessages);
      }

      return userRepository.save(user);
    } catch (DataConstraintException dataConstraintException) {
      System.out.println(dataConstraintException.getMessages());
      throw dataConstraintException;
    } catch (Exception exception) {
      log.error("Error saving", exception);
      throw new DataProcessingException("Something went wrong while saving all users");
    }
  }
}

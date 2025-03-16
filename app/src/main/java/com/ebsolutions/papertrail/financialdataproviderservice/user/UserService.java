package com.ebsolutions.papertrail.financialdataproviderservice.user;

import com.ebsolutions.papertrail.financialdataproviderservice.common.exception.DataConstraintException;
import com.ebsolutions.papertrail.financialdataproviderservice.common.exception.DataProcessingException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
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
      log.error("Error getting all", exception);
      throw new DataProcessingException("Something went wrong while fetching all users");
    }
  }

  public Optional<User> get(Integer userId) {
    try {
      return userRepository.findById(userId.longValue());
    } catch (Exception exception) {
      log.error("Error getting by id", exception);
      throw new DataProcessingException("Something went wrong while fetching the user");
    }
  }

  public List<User> createAll(List<User> users) {
    try {
      if (users.isEmpty()) {
        throw new DataConstraintException(Collections.singletonList("Users cannot be empty"));
      }

      if (!users.stream()
          .filter(user -> user.getId() != null)
          .toList()
          .isEmpty()) {

        List<String> existingUserErrorMessages =
            users.stream()
                .filter(user -> user.getId() != null)
                .map(user -> "User Id cannot be populated: ".concat(
                    Integer.toString(user.getId())))
                .toList();

        throw new DataConstraintException(existingUserErrorMessages);
      }

      return userRepository.saveAll(users);
    } catch (DataConstraintException dataConstraintException) {
      throw dataConstraintException;
    } catch (Exception exception) {
      log.error("Error creating", exception);
      throw new DataProcessingException("Something went wrong while saving all users");
    }
  }

  public User update(User user) {
    try {
      if (user.getId() <= 0) {
        List<String> existingUserErrorMessages =
            Collections.singletonList("User Id must be positive and non-zero");

        throw new DataConstraintException(existingUserErrorMessages);
      }

      boolean doesUserExist = userRepository.findById((long) user.getId()).isPresent();

      if (!doesUserExist) {
        List<String> existingUserErrorMessages =
            Collections.singletonList(
                "User Id does not exist: ".concat(Integer.toString(user.getId())));

        throw new DataConstraintException(existingUserErrorMessages);
      }

      return userRepository.save(user);
    } catch (DataConstraintException dataConstraintException) {
      throw dataConstraintException;
    } catch (Exception exception) {
      log.error("Error saving", exception);
      throw new DataProcessingException("Something went wrong while saving the user");
    }
  }

  public void delete(Integer userId) {
    try {
      userRepository.deleteById(userId.longValue());
    } catch (DataConstraintException dataConstraintException) {
      throw dataConstraintException;
    } catch (Exception exception) {
      log.error("Error deleting", exception);
      throw new DataProcessingException("Something went wrong while deleting the user");
    }
  }
}

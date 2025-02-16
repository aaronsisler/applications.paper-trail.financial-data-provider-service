package com.ebsolutions.papertrail.financialdataproviderservice.user;

import com.ebsolutions.papertrail.financialdataproviderservice.common.DatabaseConstants;
import com.ebsolutions.papertrail.financialdataproviderservice.common.exception.DataProcessingException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JdbcUserDao implements UserDao {

  public List<UserDto> readAll() {
    try (Connection conn = DriverManager.getConnection(
        DatabaseConstants.DB_URL,
        DatabaseConstants.DB_USER,
        DatabaseConstants.DB_PASSWORD)) {
      // Retrieve a record
      String selectQuery = "SELECT * FROM app_user";
      PreparedStatement preparedStatement = conn.prepareStatement(selectQuery);
      ResultSet rs = preparedStatement.executeQuery();

      List<UserDto> userDtos = new ArrayList<>();

      while (rs.next()) {
        userDtos.add(
            UserDto.builder()
                .userId(rs.getInt("user_id"))
                .username(rs.getString("username"))
                .firstName(rs.getString("first_name"))
                .lastName(rs.getString("last_name"))
                .build()
        );
      }

      return userDtos;
    } catch (SQLException e) {
      log.error("Something broke in the JDBC DAO", e);
      throw new DataProcessingException("Something went wrong with the database");
    }
  }
}

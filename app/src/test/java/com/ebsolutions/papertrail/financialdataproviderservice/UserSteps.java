package com.ebsolutions.papertrail.financialdataproviderservice;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import com.ebsolutions.papertrail.financialdataproviderservice.common.exception.DataProcessingException;
import com.ebsolutions.papertrail.financialdataproviderservice.config.Constants;
import com.ebsolutions.papertrail.financialdataproviderservice.model.ServerError;
import com.ebsolutions.papertrail.financialdataproviderservice.model.User;
import com.ebsolutions.papertrail.financialdataproviderservice.tooling.BaseTest;
import com.ebsolutions.papertrail.financialdataproviderservice.user.UserDao;
import com.ebsolutions.papertrail.financialdataproviderservice.user.UserDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MvcResult;

@RequiredArgsConstructor
public class UserSteps extends BaseTest {
  protected final UserDao userDao;

  protected MvcResult result;
  protected List<UserDto> userDtos = new ArrayList<>();
  private UserDto userDtoOne;
  private UserDto userDtoTwo;

  @And("two users exist in the database")
  public void twoUsersExistInTheDatabase() {
    userDtoOne =
        UserDto.builder()
            .userId(1)
            .username("first_user")
            .firstName("first")
            .lastName("user")
            .build();

    userDtoTwo =
        UserDto.builder()
            .userId(2)
            .username("second_user")
            .firstName("second")
            .lastName("user")
            .build();

    userDtos.add(userDtoOne);
    userDtos.add(userDtoTwo);

    when(userDao.readAll()).thenReturn(userDtos);
  }

  @And("no users exist")
  public void noUsersExist() {
    when(userDao.readAll()).thenReturn(Collections.emptyList());
  }

  @When("the get all users endpoint is invoked")
  public void theGetAllUsersEndpointIsInvoked() throws Exception {
    result = mockMvc.perform(get(Constants.USERS_URL)).andReturn();
  }

  @Then("the correct empty users response is returned")
  public void theCorrectEmptyUsersResponseIsReturned() {
    MockHttpServletResponse mockHttpServletResponse = result.getResponse();
    Assertions.assertEquals(HttpStatus.NO_CONTENT.value(), mockHttpServletResponse.getStatus());
  }

  @Then("the correct users are returned")
  public void theCorrectUsersAreReturned()
      throws JsonProcessingException, UnsupportedEncodingException {
    MockHttpServletResponse mockHttpServletResponse = result.getResponse();

    Assertions.assertEquals(HttpStatus.OK.value(), mockHttpServletResponse.getStatus());

    String content = mockHttpServletResponse.getContentAsString();
    List<User> users = objectMapper.readerForListOf(User.class).readValue(content);

    User userOne = users.getFirst();
    assertUserDtoAgainstUser(userDtoOne, userOne);

    User userTwo = users.getLast();
    assertUserDtoAgainstUser(userDtoTwo, userTwo);
  }

  @And("the connection to the database fails")
  public void theConnectionToTheDatabaseFails() {
    DataProcessingException dataProcessingException =
        new DataProcessingException("Generic Exception Message!");

    when(userDao.readAll()).thenThrow(dataProcessingException);
  }

  @Then("the correct failure response is returned")
  public void theCorrectFailureResponseIsReturned()
      throws UnsupportedEncodingException, JsonProcessingException {
    MockHttpServletResponse mockHttpServletResponse = result.getResponse();

    Assertions.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(),
        mockHttpServletResponse.getStatus());

    String content = mockHttpServletResponse.getContentAsString();

    ServerError serverError = objectMapper.readValue(content, ServerError.class);
    Assertions.assertEquals("Generic Exception Message!", serverError.getMessage());
  }

  private void assertUserDtoAgainstUser(UserDto userDto, User user) {
    Assertions.assertEquals(userDto.getUserId(), user.getUserId());
    Assertions.assertEquals(userDto.getUsername(), user.getUsername());
    Assertions.assertEquals(userDto.getFirstName(), user.getFirstName());
    Assertions.assertEquals(userDto.getLastName(), user.getLastName());
  }
}

package com.ebsolutions.papertrail.financialdataproviderservice;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import com.ebsolutions.papertrail.financialdataproviderservice.common.exception.DataProcessingException;
import com.ebsolutions.papertrail.financialdataproviderservice.config.Constants;
import com.ebsolutions.papertrail.financialdataproviderservice.model.ErrorResponse;
import com.ebsolutions.papertrail.financialdataproviderservice.tooling.BaseTest;
import com.ebsolutions.papertrail.financialdataproviderservice.user.User;
import com.ebsolutions.papertrail.financialdataproviderservice.user.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MvcResult;

@RequiredArgsConstructor
public class UserCreateAllSteps extends BaseTest {
  protected final UserRepository userRepository;

  private String requestContent;
  private MvcResult result;
  private User inputUserOne;
  private User inputUserTwo;
  private User expectedUserOne;
  private User expectedUserTwo;

  @And("two valid users are part of the request body for the create all users endpoint")
  public void twoValidUsersArePartOfTheRequestBodyForTheCreateAllUsersEndpoint()
      throws JsonProcessingException {
    inputUserOne =
        User.builder()
            .username("first_user")
            .firstName("first")
            .lastName("user")
            .build();

    inputUserTwo = User.builder()
        .username("second_user")
        .firstName("second")
        .lastName("user")
        .build();

    expectedUserOne =
        User.builder()
            .userId(1)
            .username("first_user")
            .firstName("first")
            .lastName("user")
            .build();

    expectedUserTwo = User.builder()
        .userId(2)
        .username("second_user")
        .firstName("second")
        .lastName("user")
        .build();

    requestContent =
        objectMapper.writeValueAsString(Arrays.asList(inputUserOne, inputUserTwo));

    when(userRepository.saveAll(any())).thenReturn(Arrays.asList(expectedUserOne, expectedUserTwo));
  }

  @And("no users are part of the request body")
  public void noUsersArePartOfTheRequestBody() {
    requestContent = new ArrayList<>().toString();
  }

  @And("the connection to the database fails for the create all users endpoint")
  public void theConnectionToTheDatabaseFailsForTheCreateAllUsersEndpoint() {
    DataProcessingException dataProcessingException = new DataProcessingException();

    when(userRepository.saveAll(any())).thenThrow(dataProcessingException);
  }

  @When("the create all users endpoint is invoked")
  public void theCreateAllUsersEndpointIsInvoked() throws Exception {
    result = mockMvc.perform(post(Constants.USERS_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .content(requestContent)
            .accept(MediaType.APPLICATION_JSON))
        .andReturn();
  }

  @Then("the newly created users are returned from the create all users endpoint")
  public void theCorrectUsersAreReturnedFromTheCreateAllUserEndpoint()
      throws JsonProcessingException, UnsupportedEncodingException {
    MockHttpServletResponse mockHttpServletResponse = result.getResponse();

    Assertions.assertEquals(HttpStatus.OK.value(), mockHttpServletResponse.getStatus());

    String content = mockHttpServletResponse.getContentAsString();
    List<User> users = objectMapper.readerForListOf(User.class).readValue(content);

    User userOne = users.getFirst();
    assertUserDtoAgainstUser(expectedUserOne, userOne);

    User userTwo = users.getLast();
    assertUserDtoAgainstUser(expectedUserTwo, userTwo);
  }

  @Then("the correct failure response is returned from the create all users endpoint")
  public void theCorrectFailureResponseIsReturnedFromTheCreateAllUsersEndpoint()
      throws UnsupportedEncodingException, JsonProcessingException {
    MockHttpServletResponse mockHttpServletResponse = result.getResponse();

    Assertions.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(),
        mockHttpServletResponse.getStatus());

    String content = mockHttpServletResponse.getContentAsString();

    ErrorResponse errorResponse = objectMapper.readValue(content, ErrorResponse.class);
    Assertions.assertEquals("Something went wrong while saving all users",
        errorResponse.getMessages().getFirst());
  }

  @Then("the correct bad request response is returned from the create all users endpoint")
  public void theCorrectBadRequestResponseIsReturnedFromTheCreateAllUsersEndpoint()
      throws UnsupportedEncodingException, JsonProcessingException {
    MockHttpServletResponse mockHttpServletResponse = result.getResponse();

    Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(),
        mockHttpServletResponse.getStatus());

    String content = mockHttpServletResponse.getContentAsString();

    ErrorResponse errorResponse = objectMapper.readValue(content, ErrorResponse.class);
    Assertions.assertEquals("Users cannot be empty",
        errorResponse.getMessages().getFirst());

    Mockito.verifyNoInteractions(userRepository);
  }


  private void assertUserDtoAgainstUser(User expectedUser, User actualUser) {
    Assertions.assertEquals(expectedUser.getUserId(), actualUser.getUserId());
    Assertions.assertEquals(expectedUser.getUsername(), actualUser.getUsername());
    Assertions.assertEquals(expectedUser.getFirstName(), actualUser.getFirstName());
    Assertions.assertEquals(expectedUser.getLastName(), actualUser.getLastName());
  }


}

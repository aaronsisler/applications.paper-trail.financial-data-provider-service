package com.ebsolutions.papertrail.financialdataproviderservice;

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
import java.util.Collections;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MvcResult;

@RequiredArgsConstructor
public class UserCreateAllSteps extends BaseTest {
  protected final UserRepository userRepository;

  private String requestContent;
  private MvcResult result;
  private User expectedUserOne;
  private User expectedUserTwo;

  @And("two valid users are part of the request body for the create all users endpoint")
  public void twoValidUsersArePartOfTheRequestBodyForTheCreateAllUsersEndpoint() {
    requestContent = new ArrayList<>().toString();
  }

  @And("no users are part of the request body")
  public void noUsersArePartOfTheRequestBody() {
  }

  @And("the connection to the database fails for the create all users endpoint")
  public void theConnectionToTheDatabaseFailsForTheCreateAllUsersEndpoint() {
    DataProcessingException dataProcessingException = new DataProcessingException();

    when(userRepository.saveAll(Collections.emptyList())).thenThrow(dataProcessingException);
  }

  @When("the create all users endpoint is invoked")
  public void theCreateAllUsersEndpointIsInvoked() throws Exception {
    result = mockMvc.perform(post(Constants.USERS_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .content(requestContent)
            .accept(MediaType.APPLICATION_JSON))
        .andReturn();
    System.out.println(result);
  }

  @Then("the newly created users are returned")
  public void theCorrectUsersAreReturned()
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


  private void assertUserDtoAgainstUser(User expectedUser, User actualUser) {
    // Figure out how to check the ordering and/or values
    //    Assertions.assert NotNull(actualUser.getUserId());
    Assertions.assertEquals(expectedUser.getUsername(), actualUser.getUsername());
    Assertions.assertEquals(expectedUser.getFirstName(), actualUser.getFirstName());
    Assertions.assertEquals(expectedUser.getLastName(), actualUser.getLastName());
  }
}

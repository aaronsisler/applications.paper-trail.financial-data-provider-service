package com.ebsolutions.papertrail.financialdataproviderservice;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import com.ebsolutions.papertrail.financialdataproviderservice.config.Constants;
import com.ebsolutions.papertrail.financialdataproviderservice.tooling.MockedIntegrationLayerBaseTest;
import com.ebsolutions.papertrail.financialdataproviderservice.user.User;
import com.ebsolutions.papertrail.financialdataproviderservice.user.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MvcResult;

@RequiredArgsConstructor
public class UserSteps extends MockedIntegrationLayerBaseTest {
  protected final UserRepository userRepository;


  private MvcResult result;
  private User expectedUserOne;
  private User expectedUserTwo;

  @And("two users exist in the database")
  public void twoUsersExistInTheDatabase() {
    expectedUserOne =
        User.builder()
            .userId(1)
            .username("first_user")
            .firstName("first")
            .lastName("user")
            .build();

    expectedUserTwo =
        User.builder()
            .userId(2)
            .username("second_user")
            .firstName("second")
            .lastName("user")
            .build();

    when(userRepository.findAll()).thenReturn(Arrays.asList(expectedUserOne, expectedUserTwo));
  }


  @When("the get all users endpoint is invoked")
  public void theGetAllUsersEndpointIsInvoked() throws Exception {
    result = mockMvc.perform(get(Constants.USERS_URL)).andReturn();
  }


  @Then("the correct users are returned")
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

  private void assertUserDtoAgainstUser(User expectedUser, User actualUser) {
    Assertions.assertEquals(expectedUser.getUserId(), actualUser.getUserId());
    Assertions.assertEquals(expectedUser.getUsername(), actualUser.getUsername());
    Assertions.assertEquals(expectedUser.getFirstName(), actualUser.getFirstName());
    Assertions.assertEquals(expectedUser.getLastName(), actualUser.getLastName());
  }
}

package com.ebsolutions.papertrail.financialdataproviderservice.user;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

import com.ebsolutions.papertrail.financialdataproviderservice.common.exception.DataProcessingException;
import com.ebsolutions.papertrail.financialdataproviderservice.config.Constants;
import com.ebsolutions.papertrail.financialdataproviderservice.model.ErrorResponse;
import com.ebsolutions.papertrail.financialdataproviderservice.tooling.BaseTest;
import com.ebsolutions.papertrail.financialdataproviderservice.util.CommonTestUtil;
import com.ebsolutions.papertrail.financialdataproviderservice.util.UserTestUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import java.io.UnsupportedEncodingException;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MvcResult;

@RequiredArgsConstructor
public class UserUpdateSteps extends BaseTest {
  protected final UserRepository userRepository;

  private String requestContent;
  private MvcResult result;
  private User expectedUserOne;

  @And("the user is part of the request body for the update user endpoint")
  public void theUserIsPartOfTheRequestBodyForTheUpdateUserEndpoint()
      throws JsonProcessingException {
    int validUserId = 1;

    User inputUserOne = User.builder()
        .id(validUserId)
        .username("first_user")
        .firstName("first")
        .lastName("user")
        .build();

    expectedUserOne =
        User.builder()
            .id(validUserId)
            .username("first_user")
            .firstName("first")
            .lastName("user")
            .build();

    requestContent =
        objectMapper.writeValueAsString(inputUserOne);

    when(userRepository.findById(1L)).thenReturn(Optional.ofNullable(expectedUserOne));

    when(userRepository.save(any())).thenReturn(expectedUserOne);
  }

  @And("the user id does not exist in the database")
  public void theUserIdDoesNotExistInTheDatabase() {
    when(userRepository.findById(1L)).thenReturn(Optional.empty());
  }

  @And("the connection to the database fails for the update user endpoint")
  public void theConnectionToTheDatabaseFailsForTheUpdateUserEndpoint() {
    DataProcessingException dataProcessingException = new DataProcessingException();

    when(userRepository.save(any())).thenThrow(dataProcessingException);
  }

  @And("the user in the update user request body has an invalid input")
  public void theUserInTheUpdateUserRequestBodyHasAnInvalidInput(DataTable dataTable)
      throws JsonProcessingException {
    int userId = dataTable.column(0).getFirst() == null ? 0 :
        Integer.parseInt(dataTable.column(0).getFirst());

    User inputUserOne = User.builder()
        .id(userId)
        .username(CommonTestUtil.isEmptyString(dataTable.column(1).getFirst()))
        .firstName(CommonTestUtil.isEmptyString(dataTable.column(2).getFirst()))
        .lastName(CommonTestUtil.isEmptyString(dataTable.column(3).getFirst()))
        .build();

    requestContent =
        objectMapper.writeValueAsString(inputUserOne);
  }

  @When("the update user endpoint is invoked")
  public void theUpdateUserEndpointIsInvoked() throws Exception {
    result = mockMvc.perform(put(Constants.USERS_URI)
            .contentType(MediaType.APPLICATION_JSON)
            .content(requestContent)
            .accept(MediaType.APPLICATION_JSON))
        .andReturn();
  }

  @Then("the updated user is returned from the update user endpoint")
  public void theUpdatedUserIsReturnedFromTheUpdateUserEndpoint()
      throws UnsupportedEncodingException, JsonProcessingException {
    MockHttpServletResponse mockHttpServletResponse = result.getResponse();

    Assertions.assertEquals(HttpStatus.OK.value(), mockHttpServletResponse.getStatus());

    String content = mockHttpServletResponse.getContentAsString();
    User user = objectMapper.readValue(content, User.class);

    UserTestUtil.assertExpectedAgainstActual(expectedUserOne, user);
  }

  @Then("the correct failure response is returned from the update user endpoint")
  public void theCorrectFailureResponseIsReturnedFromTheUpdateUserEndpoint()
      throws UnsupportedEncodingException, JsonProcessingException {
    MockHttpServletResponse mockHttpServletResponse = result.getResponse();

    Assertions.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(),
        mockHttpServletResponse.getStatus());

    String content = mockHttpServletResponse.getContentAsString();

    ErrorResponse errorResponse = objectMapper.readValue(content, ErrorResponse.class);
    Assertions.assertEquals("Something went wrong while saving the user",
        errorResponse.getMessages().getFirst());
  }

  @Then("the correct bad request response is returned from the update user endpoint")
  public void theCorrectBadRequestResponseIsReturnedFromTheUpdateUserEndpoint()
      throws UnsupportedEncodingException, JsonProcessingException {
    MockHttpServletResponse mockHttpServletResponse = result.getResponse();

    Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(),
        mockHttpServletResponse.getStatus());

    String content = mockHttpServletResponse.getContentAsString();

    ErrorResponse errorResponse = objectMapper.readValue(content, ErrorResponse.class);
    Assertions.assertEquals("User Id does not exist: 1",
        errorResponse.getMessages().getFirst());
  }


  @Then("the correct failure response and message is returned from the update user endpoint")
  public void theCorrectFailureResponseAndMessageIsReturnedFromTheUpdateUserEndpoint(
      DataTable dataTable) throws UnsupportedEncodingException, JsonProcessingException {

    MockHttpServletResponse mockHttpServletResponse = result.getResponse();


    Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(),
        mockHttpServletResponse.getStatus());

    String content = mockHttpServletResponse.getContentAsString();

    ErrorResponse errorResponse = objectMapper.readValue(content, ErrorResponse.class);
    Assertions.assertEquals(dataTable.row(0).getFirst(), errorResponse.getMessages().getFirst());
  }
}

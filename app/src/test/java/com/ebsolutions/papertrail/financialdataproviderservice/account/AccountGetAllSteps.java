package com.ebsolutions.papertrail.financialdataproviderservice.account;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import com.ebsolutions.papertrail.financialdataproviderservice.common.exception.DataProcessingException;
import com.ebsolutions.papertrail.financialdataproviderservice.config.Constants;
import com.ebsolutions.papertrail.financialdataproviderservice.householdmember.HouseholdMember;
import com.ebsolutions.papertrail.financialdataproviderservice.householdmember.HouseholdMemberRepository;
import com.ebsolutions.papertrail.financialdataproviderservice.model.ErrorResponse;
import com.ebsolutions.papertrail.financialdataproviderservice.tooling.BaseTest;
import com.ebsolutions.papertrail.financialdataproviderservice.util.HouseholdMemberTestUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MvcResult;

@RequiredArgsConstructor
public class AccountGetAllSteps extends BaseTest {
  private static final int USER_ID_TO_BE_FOUND = 456;

  private final HouseholdMemberRepository householdMemberRepository;

  private MvcResult result;
  private HouseholdMember expectedHouseholdMemberOne;
  private HouseholdMember expectedHouseholdMemberTwo;
  private HouseholdMember expectedHouseholdMemberThree;
  private String getHouseholdMemberUrl;

  @BeforeEach
  public void setup() {
    expectedHouseholdMemberOne = null;
    expectedHouseholdMemberTwo = null;
    expectedHouseholdMemberThree = null;
  }

  @And("two accounts exist in the database for a given household member id")
  public void twoHouseholdMembersExistInTheDatabaseForAGivenUserId() {
    expectedHouseholdMemberOne =
        HouseholdMember.builder()
            .id(1)
            .householdId(123)
            .userId(USER_ID_TO_BE_FOUND)
            .build();

    expectedHouseholdMemberTwo =
        HouseholdMember.builder()
            .id(2)
            .householdId(987)
            .userId(USER_ID_TO_BE_FOUND)
            .build();
  }

  @And("one account exists in the database for a different household member id")
  public void oneHouseholdMemberExistsInTheDatabaseForADifferentUserId() {
    expectedHouseholdMemberThree =
        HouseholdMember.builder()
            .id(3)
            .householdId(123)
            .userId(12345)
            .build();
  }

  @And("the url does contain the household member id query param for the get all accounts endpoint")
  public void theUrlDoesContainTheUserIdQueryParamForTheGetAllHouseholdMembersEndpoint() {
    getHouseholdMemberUrl =
        Constants.HOUSEHOLD_MEMBERS_URI + "?householdMemberId=" + USER_ID_TO_BE_FOUND;
  }

  @And("the database connection succeeds for get all accounts")
  public void theDatabaseConnectionSucceedsForGetAllHouseholdMembers() {
    when(householdMemberRepository.findAll()).thenReturn(
        Arrays.asList(expectedHouseholdMemberOne, expectedHouseholdMemberTwo,
            expectedHouseholdMemberThree));

    when(householdMemberRepository.findByUserId(USER_ID_TO_BE_FOUND)).thenReturn(
        Arrays.asList(expectedHouseholdMemberOne, expectedHouseholdMemberTwo));
  }

  @And("the url does not contain query params for the get all accounts endpoint")
  public void theUrlDoesNotContainQueryParamsForTheGetAllHouseholdMembersEndpoint() {
    getHouseholdMemberUrl = Constants.HOUSEHOLD_MEMBERS_URI;
  }

  @And("no accounts exist in the database for a given household member id")
  public void noHouseholdMembersExistInTheDatabaseForAGivenUserId() {
    when(householdMemberRepository.findByUserId(USER_ID_TO_BE_FOUND))
        .thenReturn(Collections.emptyList());
  }

  @And("no accounts exist in the database")
  public void noHouseholdMembersExistInTheDatabase() {
    when(householdMemberRepository.findByUserId(USER_ID_TO_BE_FOUND))
        .thenReturn(Collections.emptyList());
  }

  @And("the household member id provided in the url is the incorrect format for the get accounts by id endpoint")
  public void theUserIdProvidedInTheUrlIsTheIncorrectFormatForTheGetHouseholdByIdEndpoint() {
    String invalidUserId = "abc";
    getHouseholdMemberUrl = Constants.HOUSEHOLD_MEMBERS_URI + "?userId=" + invalidUserId;
  }

  @And("the service is not able to connect to the database for get all accounts")
  public void theServiceIsNotAbleToConnectToTheDatabaseForGetAllHouseholdMembers() {
    when(householdMemberRepository.findAll())
        .thenThrow(new DataProcessingException());
  }

  @And("the service is not able to connect to the database for get by user id accounts")
  public void theServiceIsNotAbleToConnectToTheDatabaseForGetByUserIdHouseholdMembers() {
    when(householdMemberRepository.findByUserId(any()))
        .thenThrow(new DataProcessingException());
  }

  @When("the get all accounts endpoint is invoked")
  public void theGetAllHouseholdMembersEndpointIsInvoked() throws Exception {
    result = mockMvc
        .perform(get(getHouseholdMemberUrl))
        .andReturn();
  }

  @Then("the correct failure response is returned from the get all accounts endpoint")
  public void theCorrectFailureResponseIsReturnedFromTheGetAllHouseholdMembersEndpoint(
      DataTable dataTable) throws JsonProcessingException, UnsupportedEncodingException {
    MockHttpServletResponse mockHttpServletResponse = result.getResponse();

    Assertions.assertEquals(Integer.parseInt(dataTable.column(0).getFirst()),
        mockHttpServletResponse.getStatus());

    String content = mockHttpServletResponse.getContentAsString();

    ErrorResponse errorResponse = objectMapper.readValue(content, ErrorResponse.class);
    Assertions.assertEquals(dataTable.column(1).getFirst(), errorResponse.getMessages().getFirst());
  }

  @Then("the correct bad request response is returned from the get all accounts endpoint")
  public void theCorrectBadRequestResponseIsReturnedFromTheGetAllHouseholdMembersEndpoint(
      DataTable dataTable) throws UnsupportedEncodingException, JsonProcessingException {
    MockHttpServletResponse mockHttpServletResponse = result.getResponse();

    Assertions.assertEquals(Integer.parseInt(dataTable.column(0).getFirst()),
        mockHttpServletResponse.getStatus());

    String content = mockHttpServletResponse.getContentAsString();

    ErrorResponse errorResponse = objectMapper.readValue(content, ErrorResponse.class);
    Assertions.assertEquals(dataTable.column(1).getFirst(), errorResponse.getMessages().getFirst());
  }

  @Then("the correct empty accounts response is returned")
  public void theCorrectEmptyHouseholdMembersResponseIsReturned() {
    MockHttpServletResponse mockHttpServletResponse = result.getResponse();

    Assertions.assertEquals(HttpStatus.NO_CONTENT.value(), mockHttpServletResponse.getStatus());
  }

  @Then("the correct accounts are returned from the get all accounts endpoint")
  public void theCorrectHouseholdMembersAreReturnedFromTheGetAllHouseholdMembersEndpoint(
      DataTable dataTable)
      throws UnsupportedEncodingException, JsonProcessingException {
    MockHttpServletResponse mockHttpServletResponse = result.getResponse();

    Assertions.assertEquals(HttpStatus.OK.value(), mockHttpServletResponse.getStatus());

    String content = mockHttpServletResponse.getContentAsString();
    List<HouseholdMember> householdMembers =
        objectMapper.readerForListOf(HouseholdMember.class).readValue(content);

    Assertions.assertEquals(
        Integer.parseInt(dataTable.column(0).getFirst()),
        householdMembers.size());

    HouseholdMember householdMemberOne = householdMembers.get(0);
    HouseholdMemberTestUtil.assertExpectedAgainstActual(expectedHouseholdMemberOne,
        householdMemberOne);

    HouseholdMember householdMemberTwo = householdMembers.get(1);
    HouseholdMemberTestUtil.assertExpectedAgainstActual(expectedHouseholdMemberTwo,
        householdMemberTwo);

    if (householdMembers.size() > 2) {
      HouseholdMember householdMemberThree = householdMembers.get(2);
      HouseholdMemberTestUtil.assertExpectedAgainstActual(expectedHouseholdMemberThree,
          householdMemberThree);
    }
  }
}

package com.ebsolutions.papertrail.financialdataproviderservice.householdmember;

import com.ebsolutions.papertrail.financialdataproviderservice.BaseStep;
import com.ebsolutions.papertrail.financialdataproviderservice.model.Household;
import com.ebsolutions.papertrail.financialdataproviderservice.model.HouseholdMember;
import com.ebsolutions.papertrail.financialdataproviderservice.model.User;
import com.ebsolutions.papertrail.financialdataproviderservice.tooling.TestConstants;
import com.ebsolutions.papertrail.financialdataproviderservice.util.CommonTestUtil;
import com.ebsolutions.papertrail.financialdataproviderservice.util.HouseholdMemberTestUtil;
import com.ebsolutions.papertrail.financialdataproviderservice.util.HouseholdTestUtil;
import com.ebsolutions.papertrail.financialdataproviderservice.util.UserTestUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.client.RestClient;

public class HouseholdMemberSteps extends BaseStep {
  private final List<Integer> newlyCreateHouseholdMemberIds = new ArrayList<>();

  private String requestContent;
  private RestClient.ResponseSpec response;
  private User userOne;
  private User userTwo;
  private Household household;

  private HouseholdMember expectedHouseholdMemberOne;
  private HouseholdMember expectedHouseholdMemberTwo;

  @And("two users with different user ids exist in the database")
  public void twoUsersWithDifferentUserIdsExistInTheDatabase() {
    // Nothing to do given test data setup
    userOne = UserTestUtil.getTestDataUser(1);
    userTwo = UserTestUtil.getTestDataUser(2);
  }

  @And("a household exist in the database")
  public void aHouseholdExistInTheDatabase() {
    // Nothing to do given test data setup
    household = HouseholdTestUtil.getTestDataHousehold();
  }

  @And("a valid household member with the first user id is part of the request body for the create household member endpoint")
  public void aValidHouseholdMemberWithTheFirstUserIdIsPartOfTheRequestBodyForTheCreateHouseholdMemberEndpoint()
      throws JsonProcessingException {
    expectedHouseholdMemberOne =
        HouseholdMember.builder()
            .householdId(household.getHouseholdId())
            .userId(userOne.getUserId())
            .build();

    requestContent = objectMapper.writeValueAsString(expectedHouseholdMemberOne);
  }


  @When("the create household member endpoint is invoked")
  public void theCreateHouseholdMemberEndpointIsInvoked() {
    response = CommonTestUtil.createThroughApi(restClient, TestConstants.HOUSEHOLD_MEMBERS_URI,
        requestContent);
  }

  @When("the get all household members endpoint is invoked")
  public void theGetAllHouseholdMembersEndpointIsInvoked() {
    response = CommonTestUtil.getThroughApi(restClient, TestConstants.HOUSEHOLD_MEMBERS_URI);
  }

  @Then("the newly created household member with the first user id is returned from the create household member endpoint")
  public void theNewlyCreatedHouseholdMemberWithTheFirstUserIdIsReturnedFromTheCreateHouseholdMemberEndpoint() {
    HouseholdMember householdMember = response.body(HouseholdMember.class);

    Assertions.assertNotNull(householdMember);

    HouseholdMemberTestUtil
        .assertExpectedAgainstCreated(expectedHouseholdMemberOne, householdMember);

    newlyCreateHouseholdMemberIds.add(householdMember.getHouseholdMemberId());

  }

  @And("a valid household member with the second user id is part of the request body for the create household member endpoint")
  public void aValidHouseholdMemberWithTheSecondUserIdIsPartOfTheRequestBodyForTheCreateHouseholdMemberEndpoint()
      throws JsonProcessingException {
    expectedHouseholdMemberTwo = HouseholdMember.builder()
        .householdId(household.getHouseholdId())
        .userId(userTwo.getUserId())
        .build();

    requestContent = objectMapper.writeValueAsString(expectedHouseholdMemberTwo);
  }

  @Then("the newly created household member with the second user id is returned from the create household member endpoint")
  public void theNewlyCreatedHouseholdMemberWithTheSecondUserIdIsReturnedFromTheCreateHouseholdMemberEndpoint() {
    HouseholdMember householdMember = response.body(HouseholdMember.class);

    Assertions.assertNotNull(householdMember);

    HouseholdMemberTestUtil
        .assertExpectedAgainstCreated(expectedHouseholdMemberTwo, householdMember);

    newlyCreateHouseholdMemberIds.add(householdMember.getHouseholdMemberId());
  }

  @Then("the correct household members are returned from the get all household members endpoint")
  public void theCorrectHouseholdMembersAreReturnedFromTheGetAllHouseholdMembersEndpoint() {
    List<HouseholdMember> householdMembers = response.body(
        new ParameterizedTypeReference<ArrayList<HouseholdMember>>() {
        });

    Assertions.assertNotNull(householdMembers);

    List<HouseholdMember> createdHouseholdMembers =
        householdMembers.stream()
            .filter(householdMember -> newlyCreateHouseholdMemberIds.contains(
                householdMember.getHouseholdMemberId())).toList();

    Assertions.assertEquals(2, createdHouseholdMembers.size());

    HouseholdMember householdMemberOne = householdMembers.getFirst();
    HouseholdMemberTestUtil
        .assertExpectedAgainstCreated(expectedHouseholdMemberOne, householdMemberOne);

    HouseholdMember householdMemberTwo = householdMembers.getLast();
    HouseholdMemberTestUtil
        .assertExpectedAgainstCreated(expectedHouseholdMemberTwo, householdMemberTwo);
  }

  @When("the get all household members endpoint is invoked with the first user id")
  public void theGetAllHouseholdMembersEndpointIsInvokedWithTheFirstUserId() {
    response = CommonTestUtil.getThroughApi(restClient,
        TestConstants.HOUSEHOLD_MEMBERS_URI + "?userId=" + userOne.getUserId());
  }

  @Then("the correct household members are returned from the get all household members endpoint invoked with the first user id")
  public void theCorrectHouseholdMembersAreReturnedFromTheGetAllHouseholdMembersEndpointInvokedWithTheFirstUserId() {
    List<HouseholdMember> householdMembers = response.body(
        new ParameterizedTypeReference<ArrayList<HouseholdMember>>() {
        });

    Assertions.assertNotNull(householdMembers);

    Assertions.assertEquals(1, householdMembers.size());

    HouseholdMember householdMemberOne = householdMembers.getFirst();
    HouseholdMemberTestUtil
        .assertExpectedAgainstCreated(expectedHouseholdMemberOne, householdMemberOne);
  }
}

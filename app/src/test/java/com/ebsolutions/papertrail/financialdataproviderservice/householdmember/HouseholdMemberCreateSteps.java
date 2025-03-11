package com.ebsolutions.papertrail.financialdataproviderservice.householdmember;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import com.ebsolutions.papertrail.financialdataproviderservice.config.Constants;
import com.ebsolutions.papertrail.financialdataproviderservice.household.Household;
import com.ebsolutions.papertrail.financialdataproviderservice.household.HouseholdRepository;
import com.ebsolutions.papertrail.financialdataproviderservice.tooling.BaseTest;
import com.ebsolutions.papertrail.financialdataproviderservice.user.User;
import com.ebsolutions.papertrail.financialdataproviderservice.user.UserRepository;
import com.ebsolutions.papertrail.financialdataproviderservice.util.HouseholdMemberTestUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
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
public class HouseholdMemberCreateSteps extends BaseTest {
  private final HouseholdMemberRepository householdMemberRepository;
  private final UserRepository userRepository;
  private final HouseholdRepository householdRepository;


  private String requestContent;
  private MvcResult result;
  private HouseholdMember expectedHouseholdMember;

  @And("a valid household member is part of the request body for the create household member endpoint")
  public void aValidHouseholdMemberIsPartOfTheRequestBodyForTheCreateHouseholdMemberEndpoint()
      throws JsonProcessingException {
    HouseholdMember inputHouseholdMember = HouseholdMember
        .builder()
        .householdId(123)
        .userId(456)
        .build();

    expectedHouseholdMember =
        HouseholdMember.builder()
            .householdId(123)
            .userId(456)
            .build();

    requestContent = objectMapper.writeValueAsString(inputHouseholdMember);

    when(householdMemberRepository.save(any())).thenReturn(expectedHouseholdMember);
  }

  @And("user id exists in the database for the household member")
  public void userIdExistsInTheDatabaseForTheHouseholdMember() {
    when(userRepository.findById(any())).thenReturn(Optional.of(User.builder().build()));
  }

  @And("household id exists in the database for the household member")
  public void householdIdExistsInTheDatabaseForTheHouseholdMember() {
    when(householdRepository.findById(any())).thenReturn(Optional.of(Household.builder().build()));
  }

  @When("the create household member endpoint is invoked")
  public void theCreateHouseholdMemberEndpointIsInvoked() throws Exception {
    result = mockMvc.perform(post(Constants.HOUSEHOLD_MEMBERS_URI)
            .contentType(MediaType.APPLICATION_JSON)
            .content(requestContent)
            .accept(MediaType.APPLICATION_JSON))
        .andReturn();
  }

  @Then("the newly created household member is returned from the create household member endpoint")
  public void theNewlyCreatedHouseholdMemberIsReturnedFromTheCreateHouseholdMemberEndpoint()
      throws UnsupportedEncodingException, JsonProcessingException {
    MockHttpServletResponse mockHttpServletResponse = result.getResponse();

    Assertions.assertEquals(HttpStatus.OK.value(), mockHttpServletResponse.getStatus());

    String content = mockHttpServletResponse.getContentAsString();
    HouseholdMember householdMember = objectMapper.readValue(content, HouseholdMember.class);

    HouseholdMemberTestUtil.assertExpectedAgainstActual(expectedHouseholdMember, householdMember);
  }
}

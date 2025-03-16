package com.ebsolutions.papertrail.financialdataproviderservice.householdmember;

import com.ebsolutions.papertrail.financialdataproviderservice.user.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@Validated
@RestController
@AllArgsConstructor
@RequestMapping("household-members")
public class HouseholdMemberController {
  private final HouseholdMemberService householdMemberService;

  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  @Operation(summary = "Get all household members")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200",
          content = {
              @Content(mediaType = "application/json",
                  array = @ArraySchema(schema = @Schema(implementation = HouseholdMember.class)))
          }),
      @ApiResponse(responseCode = "204", content = @Content(schema = @Schema(hidden = true)))})
  public ResponseEntity<?> getAllById(@RequestParam(required = false) Integer userId) {
    List<HouseholdMember> householdMembers;
    if (userId == null) {
      householdMembers = householdMemberService.getAll();
    } else {
      householdMembers = householdMemberService.getAllByUserId(userId);
    }

    return !householdMembers.isEmpty() ? ResponseEntity.ok(householdMembers) :
        ResponseEntity.noContent().build();
  }

  @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  @Operation(summary = "Create household member")
  @ApiResponse(responseCode = "200",
      content = {
          @Content(mediaType = "application/json",
              schema = @Schema(implementation = User.class))
      })
  public ResponseEntity<?> post(@Valid @RequestBody HouseholdMember householdMember) {
    return ResponseEntity.ok(householdMemberService.create(householdMember));
  }
}

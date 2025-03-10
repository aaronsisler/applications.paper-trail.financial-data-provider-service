package com.ebsolutions.papertrail.financialdataproviderservice.household;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@Validated
@RestController
@AllArgsConstructor
@RequestMapping("households")
public class HouseholdController {
  private final HouseholdService householdService;

  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  @Operation(summary = "Get all households")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200",
          content = {
              @Content(mediaType = "application/json",
                  array = @ArraySchema(schema = @Schema(implementation = Household.class)))
          }),
      @ApiResponse(responseCode = "204", content = @Content(schema = @Schema(hidden = true)))})
  public ResponseEntity<?> getAll() {
    List<Household> households = householdService.getAll();

    return !households.isEmpty() ? ResponseEntity.ok(households) :
        ResponseEntity.noContent().build();
  }

  @Operation(summary = "Get household")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200",
          content = {
              @Content(mediaType = "application/json",
                  schema = @Schema(implementation = Household.class))
          })})
  @GetMapping(value = "/{householdId}", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<?> get(@PathVariable @Valid Integer householdId) {
    Optional<Household> household = householdService.get(householdId);

    return household.isPresent()
        ?
        ResponseEntity.ok(household.get()) :
        ResponseEntity.noContent().build();

  }

  @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  @Operation(summary = "Create households")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200",
          content = {
              @Content(mediaType = "application/json",
                  array = @ArraySchema(schema = @Schema(implementation = Household.class)))
          })})
  public ResponseEntity<?> post(@Valid @RequestBody List<@Valid Household> households) {
    return ResponseEntity.ok(householdService.createAll(households));
  }

  @PutMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  @Operation(summary = "Update household")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200",
          content = {
              @Content(mediaType = "application/json",
                  schema = @Schema(implementation = Household.class))
          })})
  public ResponseEntity<?> put(@RequestBody @Valid Household household) {
    return ResponseEntity.ok().body(householdService.update(household));
  }

  @Operation(summary = "Delete household")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200",
          content = {
              @Content(mediaType = "application/json",
                  schema = @Schema(implementation = Household.class))
          })})
  @DeleteMapping(value = "/{householdId}", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<?> delete(@PathVariable @Valid Integer householdId) {

    householdService.delete(householdId);

    return ResponseEntity.noContent().build();
  }
}

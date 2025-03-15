package com.ebsolutions.papertrail.financialdataproviderservice.institution;

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
@RequestMapping("institutions")
public class InstitutionController {
  private final InstitutionService institutionService;

  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  @Operation(summary = "Get all institutions")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200",
          content = {
              @Content(mediaType = "application/json",
                  array = @ArraySchema(schema = @Schema(implementation = Institution.class)))
          }),
      @ApiResponse(responseCode = "204", content = @Content(schema = @Schema(hidden = true)))})
  public ResponseEntity<?> getAll() {
    List<Institution> institutions = institutionService.getAll();

    return !institutions.isEmpty() ? ResponseEntity.ok(institutions) :
        ResponseEntity.noContent().build();
  }

  @Operation(summary = "Get institution")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200",
          content = {
              @Content(mediaType = "application/json",
                  schema = @Schema(implementation = Institution.class))
          })})
  @GetMapping(value = "/{institutionId}", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<?> get(@PathVariable @Valid Integer institutionId) {
    Optional<Institution> institution = institutionService.get(institutionId);

    return institution.isPresent()
        ?
        ResponseEntity.ok(institution.get()) :
        ResponseEntity.noContent().build();
  }

  @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  @Operation(summary = "Create institutions")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200",
          content = {
              @Content(mediaType = "application/json",
                  array = @ArraySchema(schema = @Schema(implementation = Institution.class)))
          })})
  public ResponseEntity<?> post(@Valid @RequestBody List<@Valid Institution> institutions) {
    return ResponseEntity.ok(institutionService.createAll(institutions));
  }

  @PutMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  @Operation(summary = "Update institution")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200",
          content = {
              @Content(mediaType = "application/json",
                  schema = @Schema(implementation = Institution.class))
          })})
  public ResponseEntity<?> put(@RequestBody @Valid Institution institution) {
    return ResponseEntity.ok().body(institutionService.update(institution));
  }

  @Operation(summary = "Delete institution")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200",
          content = {
              @Content(mediaType = "application/json",
                  schema = @Schema(implementation = Institution.class))
          })})
  @DeleteMapping(value = "/{institutionId}", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<?> delete(@PathVariable @Valid Integer institutionId) {

    institutionService.delete(institutionId);

    return ResponseEntity.noContent().build();
  }
}

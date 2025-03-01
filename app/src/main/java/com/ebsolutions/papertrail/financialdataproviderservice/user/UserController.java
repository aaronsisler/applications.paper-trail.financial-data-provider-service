package com.ebsolutions.papertrail.financialdataproviderservice.user;

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
import org.springframework.web.bind.annotation.RestController;


@Validated
@RestController
@AllArgsConstructor
@RequestMapping("users")
public class UserController {
  private final UserRepository userRepository;

  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  @Operation(summary = "Get all users")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200",
          content = {
              @Content(mediaType = "application/json",
                  array = @ArraySchema(schema = @Schema(implementation = User.class)))
          }),
      @ApiResponse(responseCode = "204", content = @Content(schema = @Schema(hidden = true)))})
  public ResponseEntity<?> getAll() {
    List<User> users = userRepository.findAll();

    return !users.isEmpty() ? ResponseEntity.ok(users) : ResponseEntity.noContent().build();
  }

  @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  @Operation(summary = "Get all users")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200",
          content = {
              @Content(mediaType = "application/json",
                  array = @ArraySchema(schema = @Schema(implementation = User.class)))
          })})
  public ResponseEntity<?> post(@RequestBody List<@Valid User> users) {
    try {
      return ResponseEntity.ok(userRepository.saveAll(users));
    } catch (Exception exception) {
      System.out.println(exception.getMessage());
      return ResponseEntity.badRequest().body(exception);
    }
  }
}

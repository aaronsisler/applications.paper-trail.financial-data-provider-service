package com.ebsolutions.papertrail.financialdataproviderservice.account;

import com.ebsolutions.papertrail.financialdataproviderservice.user.User;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@Validated
@RestController
@AllArgsConstructor
@RequestMapping("accounts")
public class AccountController {
  private final AccountService accountService;

  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  @Operation(summary = "Get all accounts")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200",
          content = {
              @Content(mediaType = "application/json",
                  array = @ArraySchema(schema = @Schema(implementation = Account.class)))
          }),
      @ApiResponse(responseCode = "204", content = @Content(schema = @Schema(hidden = true)))})
  public ResponseEntity<?> getAllById(@RequestParam(required = false) Integer householdMemberId) {
    List<Account> accounts;
    if (householdMemberId == null) {
      accounts = accountService.getAll();
    } else {
      accounts = accountService.getAllByHouseholdMemberId(householdMemberId);
    }

    return !accounts.isEmpty() ? ResponseEntity.ok(accounts) :
        ResponseEntity.noContent().build();
  }

  @Operation(summary = "Get account")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200",
          content = {
              @Content(mediaType = "application/json",
                  schema = @Schema(implementation = Account.class))
          })})
  @GetMapping(value = "/{accountId}", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<?> get(@PathVariable @Valid Integer accountId) {

    Optional<Account> account = accountService.get(accountId);

    return account.isPresent() ? ResponseEntity.ok(account.get()) :
        ResponseEntity.noContent().build();
  }

  @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  @Operation(summary = "Create account")
  @ApiResponse(responseCode = "200",
      content = {
          @Content(mediaType = "application/json",
              schema = @Schema(implementation = User.class))
      })
  public ResponseEntity<?> post(@Valid @RequestBody Account account) {
    return ResponseEntity.ok(accountService.create(account));
  }

  @PutMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  @Operation(summary = "Update account")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200",
          content = {
              @Content(mediaType = "application/json",
                  schema = @Schema(implementation = Account.class))
          })})
  public ResponseEntity<?> put(@RequestBody @Valid Account account) {
    return ResponseEntity.ok().body(accountService.update(account));
  }
}

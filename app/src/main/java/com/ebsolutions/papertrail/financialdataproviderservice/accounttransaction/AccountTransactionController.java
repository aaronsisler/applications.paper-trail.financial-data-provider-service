package com.ebsolutions.papertrail.financialdataproviderservice.accounttransaction;

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
import org.springframework.web.bind.annotation.DeleteMapping;
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
@RequestMapping("account-transactions")
public class AccountTransactionController {
  private final AccountTransactionService accountTransactionService;

  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  @Operation(summary = "Get all transactions")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200",
          content = {
              @Content(mediaType = "application/json",
                  array = @ArraySchema(schema = @Schema(implementation = AccountTransaction.class)))
          }),
      @ApiResponse(responseCode = "204", content = @Content(schema = @Schema(hidden = true)))})
  public ResponseEntity<?> getAll(@RequestParam(required = false) Integer accountId) {
    List<AccountTransaction> accountTransactions;

    if (accountId == null) {
      accountTransactions = accountTransactionService.getAll();
    } else {
      accountTransactions = accountTransactionService.getAllByAccountId(accountId);
    }

    return !accountTransactions.isEmpty()
        ? ResponseEntity.ok(accountTransactions) :
        ResponseEntity.noContent().build();
  }

  @Operation(summary = "Get transaction")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200",
          content = {
              @Content(mediaType = "application/json",
                  schema = @Schema(implementation = User.class))
          }),
      @ApiResponse(responseCode = "204", content = @Content(schema = @Schema(hidden = true)))
  })
  @GetMapping(value = "/{accountTransactionId}", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<?> get(@PathVariable @Valid Integer accountTransactionId) {

    Optional<AccountTransaction> accountTransaction =
        accountTransactionService.get(accountTransactionId);

    return accountTransaction.isPresent()
        ? ResponseEntity.ok(accountTransaction.get()) :
        ResponseEntity.noContent().build();
  }

  @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  @Operation(summary = "Create transactions")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200",
          content = {
              @Content(mediaType = "application/json",
                  array = @ArraySchema(schema = @Schema(implementation = AccountTransaction.class)))
          })})
  public ResponseEntity<?> post(
      @Valid @RequestBody List<@Valid AccountTransaction> accountTransactions) {
    return ResponseEntity.ok(accountTransactionService.createAll(accountTransactions));
  }

  @PutMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  @Operation(summary = "Update transaction")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200",
          content = {
              @Content(mediaType = "application/json",
                  schema = @Schema(implementation = AccountTransaction.class))
          })})
  public ResponseEntity<?> put(@RequestBody @Valid AccountTransaction accountTransaction) {
    return ResponseEntity.ok().body(accountTransactionService.update(accountTransaction));
  }

  @Operation(summary = "Delete transaction")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "204", content = @Content(schema = @Schema(hidden = true)))
  })
  @DeleteMapping(value = "/{accountTransactionId}", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<?> delete(@PathVariable @Valid Integer accountTransactionId) {

    accountTransactionService.delete(accountTransactionId);

    return ResponseEntity.noContent().build();
  }
}
